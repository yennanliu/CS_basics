#!/usr/bin/env bash
# Delete the remote branches that master already contains.
#
#   bash script/prune_branches.sh            # dry run: print what would go, and what stays
#   bash script/prune_branches.sh --delete   # delete the qualifying branches on the remote
#   REMOTE=origin BASE=master bash script/prune_branches.sh
#
# Why it exists. Every worktree session here pushes a `worktree-*` branch and
# the merge leaves it behind: by Sep 2026 the remote carried 126 branches, 96 of
# them fully merged, and `git branch -r` had stopped being a readable list of
# what was in flight. This is the sweep, made repeatable.
#
# What qualifies. A branch goes when master already has everything it has:
#   (a) it is an ancestor of <remote>/<base>          — a plain merge; or
#   (b) `git cherry` finds a patch-identical twin in master for every commit
#       it has beyond master                          — a squash- or rebase-merge.
# Anything else is unmerged work and is only LISTED, with how many commits it
# is ahead and its last commit date. Deciding what to do with unmerged work is a
# person's job, so this script never touches it — not even a `backup-*` that
# looks stale. Deleting a remote branch does not delete a local one or a
# worktree that has it checked out.
#
# Recovery. Every deleted branch is printed with its tip SHA. The commits are
# in master's history (that is what qualified them), so nothing is lost; to
# resurrect the ref anyway: git push <remote> <sha>:refs/heads/<name>.
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
# The remote's default branch is never a candidate, even when BASE is something else.
default_ref=$(git symbolic-ref --quiet --short "refs/remotes/$REMOTE/HEAD" || true)
default_name=${default_ref#"$REMOTE/"}

prune=()
keep=()
while IFS= read -r ref; do
  name=${ref#"$REMOTE/"}
  [ "$name" = "$BASE" ] && continue
  [ "$name" = "HEAD" ] && continue
  [ -n "$default_name" ] && [ "$name" = "$default_name" ] && continue
  if git merge-base --is-ancestor "$ref" "$base_ref"; then
    prune+=("$name merged")
  elif [ -z "$(git cherry "$base_ref" "$ref" | grep '^+' || true)" ]; then
    prune+=("$name patch-equivalent")
  else
    ahead=$(git rev-list --count "$base_ref..$ref")
    keep+=("$(git log -1 --format=%cs "$ref") ahead=$ahead $name")
  fi
done < <(git for-each-ref --format='%(refname:short)' "refs/remotes/$REMOTE/")

echo "== ${#prune[@]} branch(es) $REMOTE/$BASE already contains =="
for entry in "${prune[@]+"${prune[@]}"}"; do
  name=${entry% *}; why=${entry##* }
  printf '  %s  %-14s %s\n' "$(git rev-parse --short "$REMOTE/$name")" "$why" "$name"
done

echo
echo "== ${#keep[@]} branch(es) with commits master does not have — left alone =="
for entry in "${keep[@]+"${keep[@]}"}"; do printf '  %s\n' "$entry"; done | sort

if [ "$DELETE" -eq 1 ] && [ "${#prune[@]}" -gt 0 ]; then
  echo
  names=()
  for entry in "${prune[@]}"; do names+=("${entry% *}"); done
  git push "$REMOTE" --delete "${names[@]}"
  echo "deleted ${#names[@]} branch(es) from $REMOTE"
elif [ "$DELETE" -eq 0 ] && [ "${#prune[@]}" -gt 0 ]; then
  echo
  echo "dry run — re-run with --delete to remove the first group from $REMOTE"
fi
