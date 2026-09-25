# Branch and PR sweep — September 2026

> **Scope** — what was on the remote on 2026-09-24, what was deleted and why, what was left and
> what should happen to it. Item 17 of [`project-review-2026-09.zh.md`](./project-review-2026-09.zh.md).
> **See also**: [`utility-scripts.md`](./utility-scripts.md#prune_branchessh) — the script that does the
> repeatable half.

When the sweep began on the morning of 2026-09-24, the remote carried **127 branches besides `master`**
and **14 open pull requests**: the 100 branches master already contained, listed below with their tips, and
27 with commits it did not. (The 126 quoted in the first draft was taken after the first branch had
already gone by hand.) Two more branches were pushed during the session, so the closing list of what is
left runs to 29. Every worktree session pushes a `worktree-*` branch and the merge leaves it behind, so
`git branch -r` had stopped being a readable list of what was in flight. The sweep has two halves, and
only the first is mechanical.

## What was deleted — 100 branches master already contained

Deleted with `bash script/prune_branches.sh --delete`, whose rule is: a branch goes when it is an
ancestor of `origin/master`, or when `git cherry` finds a patch-identical twin in master for every
commit it has beyond master (a squash- or rebase-merge). **Nothing here lost a change** — that is what
qualified them — and each tip SHA is recorded. For the 96 `merged` branches the commits themselves are in
master's history, and `git push origin <sha>:refs/heads/<name>` puts the pointer back from any clone. The
four `patch-equivalent` branches are different: master holds the same patches under other commit ids, and
the deleted ref was the last thing on the remote pointing at the originals, so their tips can only be pushed
back from a clone that fetched them before the sweep and has not garbage-collected since. The script now
keeps every tip it deletes under `refs/pruned/<name>@<sha>` in the clone that ran it; this first sweep ran
before it did.

`lc-coverage-kamyu-batch2` went first, by hand, as the test that a remote delete is permitted from this
machine; the script removed the other ninety-nine. Two of them deserve a word. `lc-java-dev-1-add-time-space-complexity` and
`lc-java-dev-2-add-time-complexity` were 2025 forks that `git rev-list` counted as 4,000+ commits ahead —
but `git cherry` matched every one of those commits to a patch in master (4,173 and 4,176 twins, zero
unique), so they were pre-rewrite snapshots of history master already has, not divergent work.

The script has tightened since this sweep ran, and the four `patch-equivalent` deletions were re-checked
against the stricter rules. All four pass the whitespace-sensitive test that replaced `git cherry`'s
whitespace-blind one: every commit has a byte-identical twin in master. The two Java forks would **not**
qualify today, because they carry 14 and 15 merge commits beyond master, and branches with merge commits are
now kept. Nothing was lost by deleting them. Both tips are still reachable on the remote through
`backup-20260318`, `cheatsheet-web-page` and `dev-2-cheatsheet-to-website`, so
`git push origin <sha>:refs/heads/<name>` still restores either one.

| Branch | Tip | Why |
|---|---|---|
| `add-lc-problem-list-refs` | `ee2dc3648` | merged |
| `cheatsheet-cleanup-2026-08` | `6b1638509` | merged |
| `cheatsheet-priority-markers` | `6e0a42fbe` | merged |
| `cheatsheet-python-complexity` | `ab7a38c73` | merged |
| `cheatsheet-tier1-wip` | `e67f790c8` | merged |
| `chore/untrack-site-build` | `ab1b81b8d` | merged |
| `doc/site-review` | `13b8584d7` | merged |
| `docs/dp-cheatsheet-rolling-vars-knapsack` | `d4ed36839` | merged |
| `docs/dp-split-deep-dives` | `7e8f9bcea` | merged |
| `docs/knapsack-01-zh-cheatsheet` | `08f4886df` | merged |
| `dp-def-comments` | `7150f8cee` | merged |
| `faq-expand-java-python-backend` | `0237f853e` | merged |
| `feat/cheatsheet-followups` | `a748c34e7` | merged |
| `feat/cheatsheet-format-hierarchy` | `001ee13df` | merged |
| `feat/cheatsheet-zh` | `fd32eadc7` | merged |
| `feat/complexity-quiz` | `16b7e69af` | merged |
| `feat/java-missing-solutions` | `7a9966cc7` | merged |
| `feat/java-missing-solutions-batch2` | `f0dae03ce` | merged |
| `feat/lc-coach` | `94eec0a81` | merged |
| `feat/lc-interview-coach-skill` | `08694753c` | merged |
| `feat/nav-skills-landing-review` | `d2af8c8b2` | merged |
| `feat/py-multi-version` | `7608bf0a2` | merged |
| `feat/py-multi-version-wave2` | `d0402c7b4` | merged |
| `feat/py-multi-version-wave3` | `619be136c` | merged |
| `feat/site-layout-width` | `87131f069` | merged |
| `feat/zh-i18n-store` | `4c6f8a631` | patch-equivalent (squash- or rebase-merged) |
| `fix-pq-stub-crossrefs` | `4a5a81fec` | merged |
| `fix/ds-algo-cleanup` | `be16a4920` | merged |
| `fix/quality-ci-and-correctness` | `6e65d7b35` | merged |
| `fix/readme-company-lc-tags` | `820bbd9a6` | merged |
| `fix/readme-complexity` | `1db1aa0e9` | merged |
| `fix/readme-time-space-notes` | `737fad616` | merged |
| `g-recent-asked-scrape` | `26fdfc31d` | merged |
| `interview-prep-expansion` | `84b7bc3e9` | merged |
| `lc-add-solutions` | `e52d2d0d2` | merged |
| `lc-coverage-audit-repair` | `4a608be11` | merged |
| `lc-coverage-kamyu-batch10` | `dfa55b4e4` | merged |
| `lc-coverage-kamyu-batch11` | `ccfc1f3bd` | merged |
| `lc-coverage-kamyu-batch12` | `c51bf0249` | merged |
| `lc-coverage-kamyu-batch13` | `d03eeb16f` | merged |
| `lc-coverage-kamyu-batch14` | `cc8260534` | merged |
| `lc-coverage-kamyu-batch15` | `bdf6f6df9` | merged |
| `lc-coverage-kamyu-batch16` | `84476db8f` | merged |
| `lc-coverage-kamyu-batch17` | `e3a0fe7f5` | merged |
| `lc-coverage-kamyu-batch18` | `c60af1d19` | merged |
| `lc-coverage-kamyu-batch19` | `5bc1f8bcd` | merged |
| `lc-coverage-kamyu-batch20` | `f52a25362` | merged |
| `lc-coverage-kamyu-batch2` | `ca071c8b7` | merged |
| `lc-coverage-kamyu-batch4` | `481af757f` | merged |
| `lc-coverage-kamyu-batch5` | `a8bfafeb5` | merged |
| `lc-coverage-kamyu-batch6` | `bbb42cb20` | merged |
| `lc-coverage-kamyu-batch7` | `c71fe14b5` | merged |
| `lc-coverage-kamyu-batch8` | `98d7f3a15` | merged |
| `lc-coverage-kamyu-batch9` | `73c92c9aa` | patch-equivalent (squash- or rebase-merged) |
| `lc-java-dev-1-add-time-space-complexity` | `ddda1ece3` | patch-equivalent (squash- or rebase-merged) |
| `lc-java-dev-2-add-time-complexity` | `71890d4da` | patch-equivalent (squash- or rebase-merged) |
| `lc-java-kamyu-array` | `c576d123a` | merged |
| `lc-java-kamyu-batch2` | `425d9a552` | merged |
| `lc-java-kamyu-batch3` | `0522f648f` | merged |
| `lc-java-kamyu-variants` | `3b62c9634` | merged |
| `lc-readiness-eval` | `b9a764195` | merged |
| `lc-skill-improve-plan` | `a69685c41` | merged |
| `lc-skills-impl` | `6120d6e48` | merged |
| `readme-normalize-lc-numbers` | `a3eb844e8` | merged |
| `refactor/algorithm-python` | `fc17041fc` | merged |
| `site-nav-simplify` | `6a76bcf30` | merged |
| `skill/readme-main-table-guard` | `fab494f77` | merged |
| `visualizer-terminal-theme` | `fd4f4fd2d` | merged |
| `worktree-add-lc-skill` | `2eb137288` | merged |
| `worktree-algo-viz-enhance` | `48eeb6dd4` | merged |
| `worktree-bit-manip-foundations` | `9c11ccd24` | merged |
| `worktree-bit-manipulation-fundamentals` | `87dae933d` | merged |
| `worktree-cheatsheet-lc-coverage` | `ff768a7c3` | merged |
| `worktree-cheatsheet-split-review` | `5f7cfb0ed` | merged |
| `worktree-cheatsheet-tier2` | `98a3b6dac` | merged |
| `worktree-cheatsheet-tier3` | `f41bb4a96` | merged |
| `worktree-cheatsheet-tier3-dedup` | `cbb61b467` | merged |
| `worktree-check-readme` | `31f188751` | merged |
| `worktree-classics-hard-cheatsheets` | `43bd06c34` | merged |
| `worktree-ctci-cheatsheet-gaps` | `3710d89b0` | merged |
| `worktree-derivation-cards` | `be1ebf004` | merged |
| `worktree-dp-2d-table-viz` | `3a17f822f` | merged |
| `worktree-faq-zh` | `d2cfd6c41` | merged |
| `worktree-l3-core` | `d67cbc6d0` | merged |
| `worktree-lc-add-n-queens` | `255bed5cf` | merged |
| `worktree-lc-add-page` | `20197c0fb` | merged |
| `worktree-lc-cheatsheet-skill` | `f30a9fa68` | merged |
| `worktree-lc-goog-gap` | `ff68cc1db` | merged |
| `worktree-lc-java-normalize` | `db499550d` | merged |
| `worktree-lc-python-normalize` | `0e9c6bdf6` | merged |
| `worktree-patience-sorting` | `289372241` | merged |
| `worktree-prefix-sum-zh` | `b7b99b197` | merged |
| `worktree-quiz-expand` | `391e963f6` | merged |
| `worktree-readme-tags` | `934ba465e` | merged |
| `worktree-rename-lc-add` | `7e428254f` | merged |
| `worktree-roadmap-tag-filter` | `54f358ad9` | merged |
| `worktree-site-improvements` | `aa2435533` | merged |
| `worktree-site-roadmap` | `d16ad7272` | merged |
| `worktree-suggest-review` | `18695be3d` | merged |
| `worktree-suggest-review-guide` | `5044b7ede` | merged |

## What was left — 29 branches with commits master does not have

27 at the snapshot, plus the two pushed during the session and noted under the open PRs. Deleting these is a
decision, not a chore, so the script only lists them. Grouped by what the decision is.

### Open pull requests — merge or close from GitHub

The `gh` identity on this machine can read but not write, so each of these needs a click. Recommendations
follow the September review where it took a view.

| PR | Branch | Ahead | Recommendation |
|---|---|---|---|
| [#177](https://github.com/yennanliu/CS_basics/pull/177) | `worktree-imported-rows` | 3 | **Merge after #176.** Item 14: `imported` status cell, the 10 duplicates merged, `validateIndex` build error. Based on master, so it includes #176's two commits; merging #176 first leaves only its own. |
| [#176](https://github.com/yennanliu/CS_basics/pull/176) | `worktree-index-gate-baseline` | 2 | **Merge first.** Item 6: 31 dead links repointed, 17 status cells normalised, log date fixed, baseline burned down. |
| [#172](https://github.com/yennanliu/CS_basics/pull/172) | `worktree-project-review` | 3 | **Merge.** The September review this sweep is an item of. |
| [#171](https://github.com/yennanliu/CS_basics/pull/171) | `worktree-readme-full-page` | 1 | **Merge** — the review named it (§4.6): filter the problem index in place. |
| [#170](https://github.com/yennanliu/CS_basics/pull/170) | `worktree-skill-page-assets` | 3 | Review and merge: the agent-skill pages' shared chrome. |
| [#169](https://github.com/yennanliu/CS_basics/pull/169) | `worktree-readme-landing-page` | 2 | Review and merge, or close if #171 superseded it — both rework the README page (44 files, +4,664/−4,222). |
| [#155](https://github.com/yennanliu/CS_basics/pull/155) (draft) | `cheatsheet-interviewer-review` | 4 | **Merge** — the review named it (§4.6) and item 5 depends on it. |
| [#129](https://github.com/yennanliu/CS_basics/pull/129) | `worktree-repo-health-fixes` | 8 | **Merge** — the review named it (§4.6): Java `pom.xml` + `src/test` in CI. 7 of 8 commits are unique; one already landed. |
| [#107](https://github.com/yennanliu/CS_basics/pull/107) (draft) | `doc/py-multi-version-progress` | 6 | **Close.** A progress log for the py-multi-version waves, all of which merged in August; 354 files of tooling nobody has run since. |
| [#57](https://github.com/yennanliu/CS_basics/pull/57) (draft) | `worktree-multi-source-scraper` | 2 | **Close** unless the Reddit/Blind/HN scraper is wanted; untouched since 2026-08-11 and off the coding-loop path. |
| [#52](https://github.com/yennanliu/CS_basics/pull/52) | `feat/site-improvements` | 5 | **Close as superseded.** Its −192,664 lines were the committed `_site/`, which `chore/untrack-site-build` removed; its e2e smoke test exists as `site/e2e-check.js`. |
| [#37](https://github.com/yennanliu/CS_basics/pull/37) | `dependabot/…/markdown-it-14.2.0` | 1 | **Merge** (or comment `@dependabot rebase` first). Two-line lockfile bump. |
| [#35](https://github.com/yennanliu/CS_basics/pull/35) | `backup-20260502` | 1 | **Not redundant — decide.** Its `data/progress.txt` has a `20260502: 154,1761,1277,2289` day master never got, and a different `20260224` line; plus 20 lines in `ws/Workspace25.java`. Fold the day into the log with `/lc-log`, then close and delete. |
| [#34](https://github.com/yennanliu/CS_basics/pull/34) | `backup-20260427` | 1 | **Not redundant — decide.** A `data/progress.md` section (2026-04-27: the NeetCode DP playlist, a cheatsheet LC-example checklist, AI-suggested siblings) master never got. Keep what is still useful, then close and delete. |

Two more branches from this session were pushed without a PR yet: `worktree-progress-source` (item 10,
stacked on #177) and `worktree-landing-scope` (item 13, stacked on that). Their compare links are in the
session hand-over; merge them in order after #177.

### Unmerged, no open PR — open one or delete

| Branch | Last commit | Ahead | What it holds | Recommendation |
|---|---|---|---|---|
| `worktree-cheatsheet-in-the-room` | 2026-09-23 | 1 | The "In the room" block on the twenty tier-5 cheatsheets — item 5 of the review | **Open a PR.** |
| `cheatsheet-cross-file-merge` | 2026-09-10 | 1 | Gives each cross-file duplicated cheatsheet solution one owner | Open a PR, or delete if the Aug cleanup covered it. |
| `feat/java-coverage-fixes` | 2026-08-20 | 5 | README rows for 346 Java solutions that had none (90 files) — its PR #101 merged an *earlier* tip; these 5 commits came after | **Worth a look**: `check_readme.py` still reports 346 unlinked Java files, which is this branch's subject. Rebase and open a PR, or delete. |
| `lc-coverage-kamyu` | 2026-08-15 | 1 | 50 Python solutions for LC 2095–2151, none in master; PR #71 was closed unmerged | Decide whether those 50 generated drafts are wanted; if not, delete. |
| `feat/java-multi-version` | 2026-08-20 | 1 | WIP: V1/V2 alternates on 79 Java solutions; PR #100 closed unmerged | Delete — superseded by the normalisation pass. |
| `feat/py-multi-version-wave4` | 2026-08-25 | 1 | 4 Array variants; PR #136 closed unmerged | Delete. |
| `chore/untrack-vendored-node-modules`, `rebase/pr130` | 2026-09-02/03 | 9 / 8 | Untrack the vendored aws-sdk `node_modules`; PRs #130, #134, #135 all closed unmerged | Check whether the tree still carries it (`git ls-files -- '*node_modules*'`); redo as a fresh PR or delete both. |
| `worktree-site-doocs-comparison` | 2026-09-02 | 1 | A doc comparing the site against leetcode.doocs.org | Open a PR into `doc/`, or delete. |

### Ancient forks — delete when you are sure

| Branch | Last commit | Ahead |
|---|---|---|
| `backup-20260318` | 2026-03-18 | 6,115 |
| `cheatsheet-web-page` | 2025-09-06 | 4,529 |
| `dev-2-cheatsheet-to-website` | 2025-07-02 | 4,362 |
| `dev-1-doc-to-website` | 2025-04-21 | 3,922 |

Pre-rewrite snapshots of history, like the two Java branches above — but for these `git cherry` finds
commits with **no** twin in master, so the script leaves them. The odds that a 2025 doc-to-website branch
holds anything the site has not since rebuilt are low; check with
`git diff --stat origin/master...origin/<name> -- doc site` before deleting.

## Keeping it swept

```bash
bash script/prune_branches.sh            # dry run after every merge round
bash script/prune_branches.sh --delete   # when the first group reads right
```

Local worktrees under `.claude/worktrees/` are not touched by any of this: eleven of the nineteen point at
branches that have now been merged and deleted upstream. `git worktree list` shows them;
`git worktree remove <path>` and `git branch -d <name>` clear one.
