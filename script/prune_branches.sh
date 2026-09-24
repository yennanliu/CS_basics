#!/usr/bin/env bash
# Delete the remote branches that master already contains.
#
#   bash script/prune_branches.sh            # dry run: print what would go, and what stays
#   bash script/prune_branches.sh --delete   # delete the qualifying branches on the remote
#   REMOTE=origin BASE=master bash script/prune_branches.sh
#
# Why it exists. Every worktree session here pushes a `worktree-*` branch and
# the merge leaves it behind: by Sep 2026 the remote carried 127 branches besides
# master, 100 of them already in master, and `git branch -r` had stopped being a
# readable list of what was in flight. This is the sweep, made repeatable.
#
# What qualifies. A branch goes when master already has everything it has:
#   (a) it is an ancestor of <remote>/<base>          — a plain merge; or
#   (b) `git cherry` finds a patch-identical twin in master for every commit
#       it has beyond master, and none of those commits is a merge — a squash-
#       or rebase-merge. `git cherry` skips merge commits, so a branch that has
#       one beyond master is kept: its merge resolution could carry a change no
#       twin accounts for, and nothing here has examined it.
# Anything else is unmerged work and is only LISTED, with how many commits it
# is ahead and its last commit date. Deciding what to do with unmerged work is a
# person's job, so this script never touches it — not even a `backup-*` that
# looks stale. Deleting a remote branch does not delete a local one or a
# worktree that has it checked out.
#
# How it deletes. Each branch is deleted under --force-with-lease against the
# tip that was classified, so a push that lands between the fetch and the
# delete makes that one deletion fail instead of removing work nobody looked at.
#
# Recovery. Every deleted branch is printed with its tip SHA, and the tip is
# kept in this clone under refs/pruned/<name> so the commits stay reachable
# here: an (a) branch's commits are in master anyway, but a (b) branch's are
# only *equivalent* to master's, and the ref that was deleted was their last
# holder on the remote. To put the branch back:
#   git push <remote> refs/pruned/<name>:refs/heads/<name>
# and `git update-ref -d refs/pruned/<name>` drops a keepsake you are done with.
set -euo pipefail

REMOTE=${REMOTE:-origin}
BASE=${BASE:-master}
DELETE=0
for arg in "$@"; do
  case "$arg" in
    --delete) DELETE=1 ;;
    -h|--help) awk 'NR == 1 { next } !/^#/ { exit } { sub(/^# ?/, ""); print }' "$0"; exit 0 ;;
    *) echo "unknown argument: $arg" >&2; exit 2 ;;
  esac
done

git fetch --prune --quiet "$REMOTE"
base_ref="$REMOTE/$BASE"
git rev-parse --verify --quiet "$base_ref" >/dev/null || { echo "no such ref: $base_ref" >&2; exit 2; }
# The remote's default branch is never a candidate, even when BASE is something
# else. `git fetch` does not refresh refs/remotes/<remote>/HEAD, so ask the
# remote which branch that is; if it cannot say, set -e stops us here.
git remote set-head "$REMOTE" --auto >/dev/null
default_ref=$(git symbolic-ref --quiet --short "refs/remotes/$REMOTE/HEAD" || true)
default_name=${default_ref#"$REMOTE/"}

# Three parallel arrays: bash 3.2 (macOS) has no associative ones.
prune_name=()
prune_why=()
prune_sha=()
keep=()
while IFS= read -r ref; do
  name=${ref#"$REMOTE/"}
  [ "$name" = "$BASE" ] && continue
  [ "$name" = "HEAD" ] && continue
  [ -n "$default_name" ] && [ "$name" = "$default_name" ] && continue
  merges=$(git rev-list --merges --count "$base_ref..$ref")
  if git merge-base --is-ancestor "$ref" "$base_ref"; then
    prune_name+=("$name"); prune_why+=("merged"); prune_sha+=("$(git rev-parse "$ref")")
  elif [ "$merges" -eq 0 ] && [ -z "$(git cherry "$base_ref" "$ref" | grep '^+' || true)" ]; then
    prune_name+=("$name"); prune_why+=("patch-equivalent"); prune_sha+=("$(git rev-parse "$ref")")
  else
    ahead=$(git rev-list --count "$base_ref..$ref")
    note=""
    if [ "$merges" -gt 0 ] && [ -z "$(git cherry "$base_ref" "$ref" | grep '^+' || true)" ]; then
      note="  (every non-merge commit has a twin in $BASE, but $merges merge commit(s) were not examined)"
    fi
    keep+=("$(git log -1 --format=%cs "$ref") ahead=$ahead $name$note")
  fi
done < <(git for-each-ref --format='%(refname:short)' "refs/remotes/$REMOTE/")

echo "== ${#prune_name[@]} branch(es) $REMOTE/$BASE already contains =="
for i in "${!prune_name[@]}"; do
  printf '  %s  %-16s %s\n' "${prune_sha[$i]:0:9}" "${prune_why[$i]}" "${prune_name[$i]}"
done

echo
echo "== ${#keep[@]} branch(es) with commits $BASE does not have — left alone =="
for entry in "${keep[@]+"${keep[@]}"}"; do printf '  %s\n' "$entry"; done | sort

if [ "$DELETE" -eq 1 ] && [ "${#prune_name[@]}" -gt 0 ]; then
  echo
  push_args=()
  for i in "${!prune_name[@]}"; do
    git update-ref "refs/pruned/${prune_name[$i]}" "${prune_sha[$i]}"
    push_args+=("--force-with-lease=refs/heads/${prune_name[$i]}:${prune_sha[$i]}" ":refs/heads/${prune_name[$i]}")
  done
  git push "$REMOTE" "${push_args[@]}"
  echo "deleted ${#prune_name[@]} branch(es) from $REMOTE; each tip is kept here under refs/pruned/<name>"
elif [ "$DELETE" -eq 0 ] && [ "${#prune_name[@]}" -gt 0 ]; then
  echo
  echo "dry run — re-run with --delete to remove the first group from $REMOTE"
fi
