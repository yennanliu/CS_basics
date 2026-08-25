# Tier 1 Split — Progress Snapshot

Execution log for Tier 1 of [`cheatsheet-split-plan-2026-08.md`](cheatsheet-split-plan-2026-08.md): the eight tier-5 cheatsheets over 4,000 lines.

**Status: work-in-progress snapshot.** Committed on branch `cheatsheet-tier1-wip`, taken while the per-file passes were still finishing, so this is a checkpoint rather than a reviewed result. The verification steps listed under [Not done yet](#not-done-yet) — most importantly the `data/cheatsheet_meta.json` registration, without which the site build fails on purpose — have **not** been run.

---

## What was done

Eight files, one independent pass each, run in parallel. Every pass worked under the same contract:

- **One owner per file.** A pass could create or modify only its own sheet and the satellites it spawned. No pass could touch another sheet, and none could touch `data/cheatsheet_meta.json` — that is registered centrally afterwards, so parallel passes cannot conflict over it.
- **Move, don't rewrite.** Sections were extracted with `sed`/`python3` so surviving text is preserved byte-for-byte.
- **Delete only verified duplicates** — same LC problem, same language, same algorithmic approach as a block that survives. Different approach, different complexity or different language means both copies live, one of them relocated.
- Every new file opens with H1 → `> **Scope**` → `> **See also**` → `## LeetCode Problem Lists`, then Skeleton B.
- Each parent's `See also` line names every satellite it spawned, in `dp.md`'s style.

## Result

| parent | before | after | target | satellites created |
|---|---:|---:|---:|---|
| `tree.md` | 6,485 | **1,314** | 1,400 | `tree_lca_distance` 1,370 · `tree_codec` 1,110 · `tree_construction` 382 · `tree_examples` 1,805 |
| `dfs.md` | 6,033 | **1,300** | 1,300 | `dfs_advanced` 1,710 · `dfs_examples` 2,373 |
| `heap.md` | 5,345 | **1,175** | 1,400 | `heap_examples` 1,960 · `heap_advanced` 1,091 · `heap_language_apis` 521 |
| `dp.md` | 5,157 | **1,497** | 1,500 | `dp_examples` 871 · `dp_advanced` 2,853 |
| `2_pointers.md` | 4,658 | **1,090** | 1,200 | `2_pointers_examples` 3,007 · `2_pointers_quickselect` 522 |
| `binary_search.md` | 4,591 | **1,012** | 1,100 | `binary_search_examples` 1,141 · `binary_search_on_answer` 1,450 |
| `bfs.md` | 4,256 | **1,019** | 1,200 | `bfs_examples` 1,212 · `bfs_advanced` 1,840 |
| `hash_map.md` | 4,119 | **1,073** | 1,200 | `hash_map_examples` 2,712 |

**40,644 lines in 8 files → 37,410 lines in 26 files.** Every parent is at or under its ceiling; the largest is now `dp.md` at 1,497. Net reduction 3,234 lines is duplication removed, not content dropped — the satellites hold everything that moved.

## What each pass fixed

- **`tree.md`** — §0-2's nine "Common Tree Patterns" restated §1's templates which restated §3's algorithms; collapsed to one pattern→code→example table with the code in exactly one place. LC 606, 536, 104, 863, 297 each had two full treatments; collapsed to one. §5 renumbered to §3 after §3 and §4 were extracted.
- **`dfs.md`** — `Problem Categories` Patterns 1–18 and `Templates & Algorithms` Templates 1–16 were two parallel presentations of one list; merged into a pattern→template table plus one code block per template. Three competing decision sections (`Pattern Selection Strategy`, `Summary & Quick Reference`, `Quick Decision Tree`) merged into one. The four LC examples appended *after* `Related Topics` were folded into `dfs_examples.md`.
- **`heap.md`** — `Language APIs` split out with a ~40-line essential-API table left behind; `Problems by Pattern` + `Pattern Selection Strategy` + `Summary & Quick Reference` merged into one. The parallel Python and Java template libraries were preserved — they are not duplicates of each other.
- **`dp.md`** — finished the split this file was the pilot for: `Comprehensive Pattern Analysis` dissolved into the templates it restated, `Category 9` un-stranded from its h2, LC 70 and LC 72 collapsed from three treatments each to one, three tail sections merged into one summary.
- **`2_pointers.md`** — `0) Concept` was 2,223 lines of worked solutions; rebuilt as an actual concept section. QuickSelect / median-of-medians (~500 lines, mis-filed here — it is a selection algorithm, not a two-pointer technique) moved to its own sheet. Converted from Skeleton A to Skeleton B.
- **`binary_search.md`** — the worst intra-file duplication in the corpus: LC 34 solved seven times, LC 875 five times, six more problems three times each. Collapsed, with the genuinely distinct loop invariants (closed vs half-open vs record-and-continue vs `bisect`) kept once side by side. `5) Additional High-Frequency Templates` folded back into the templates section so templates no longer sit after the examples. The `Classic Google Patterns` catch-all heading is gone.
- **`bfs.md`** — LC 994 collapsed from four treatments to one, LC 542 and 127 from three. Four overlapping tail sections merged into one summary.
- **`hash_map.md`** — un-mixed the skeletons: `0) Concept` and `1) General form` dissolved out of what is otherwise a Skeleton B document. `Virtual Map (Remapping) Pattern` folded in as the remapping template the Scope line already claimed. The `🔥 Google-Specific Patterns` catch-all heading is gone.

## Not done yet

1. **`data/cheatsheet_meta.json` registration for all 18 new files.** The build fails without it, by design. Each pass reported a recommended `category` / `tier` / `title` entry; these need to be applied centrally in one edit.
2. **Inbound anchor repair.** Each pass listed the headings it moved out of its parent. Links from *other* sheets into those headings are now stale and need a repo-wide `](#...)` and `](./x.md#...)` sweep. One pre-existing broken link was found in passing: a `./tree.md#1-1-22-tree--string-codec-pattern-` target that never existed; its content is now in `tree_codec.md`.
3. **Build verification** — `SKIP_FONTS=1 bash site/build.sh`, which cannot pass until (1) is done.
4. **Cross-file consolidation.** Every pass was forbidden from touching its neighbours, so the cross-file duplication measured in the plan is untouched and in some cases now concentrated in the `_examples` sheets: `tree.md` × `tree2.md` 21 shared problems, `dfs.md` × the tree/BST sheets ~23, `dp.md` × `dp_pattern.md` 13, `stack.md` × `monotonic_stack.md` 12, `bfs.md` × `graph.md` 7. This is its own phase.

   One finding from that phase arrived early and changes a decision above: `advanced_divide_and_conquer.md` **already owns QuickSelect in depth** (`Template 5: Quickselect`, Java + Python, Hoare and 3-way partition variants, an LC 973 variation and a variations table). The new `2_pointers_quickselect.md` largely restates it in Python. The right end state is probably to fold that satellite into `advanced_divide_and_conquer.md` rather than register it as a new sheet — `sort.md:497` also carries a `Quick Select — LC 215` template. Decide this before step 1.
5. **Review of the judgement calls.** Roughly 755 lines were deleted from `tree.md` alone as verified duplicates, each with a stated reason. Those reasons are worth reading before this is merged — the plan flags this as the one genuinely risky part of the work.
6. **Skeleton A vs B for the small satellites.** `tree_construction.md` (382 lines) and `2_pointers_quickselect.md` (522) are under `CLAUDE.md`'s ~800-line threshold, where Skeleton A is the right shape, but were written with Skeleton B for consistency across the batch. Worth a second look.

## Next

Tier 2 of the plan: `bst`, `string`, `stack` (78% example tail, the largest single win left), `backtrack`, `sliding_window`, `graph`, `linked_list`, `design`, `array`, `topology_sorting`, `tree2`, `Dijkstra`, `prefix_sum`, `matrix`, `python_trick`, `java_trick`.
