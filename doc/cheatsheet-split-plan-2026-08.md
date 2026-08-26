# Cheatsheet Split & Simplify Plan — August 2026

A follow-up review of [`doc/cheatsheet/`](cheatsheet/) — 80 markdown files, 143,068 lines — measuring **size and internal redundancy** rather than format.

The [August 2026 structural review](cheatsheet-review-2026-08.md) fixed *format* drift and it has held. Re-measured at the time of writing:

| Check | Aug review (after) | Now |
|---|---:|---:|
| Untagged code fences | 0 | **0** |
| Heading-level jumps | 0 | **0** |
| Headings stating the LC number twice | 0 | 6 |
| Files with a `Scope` statement | 35 | **79 / 79** |
| `Missing Google Patterns` catch-alls | 0 | 3 (regrown under new names) |

What that review **deliberately deferred** is what this document plans:

> *"~450 `# V0 / V0' / V0''` solution variants … Collapsing them means deciding which variant teaches something the others do not."*
> *"`lc_category.md` (1,513 lines) is a verbatim paste of an external GitHub README … It should be a link."*

and, implicitly, the thing it never addressed at all: **the files are too big to use as cheatsheets.** 21 sheets are over 2,000 lines. `tree.md` is 6,485.

---

## Verdict

The content is still strong. The problem is that every large sheet has grown into three documents wearing one filename: a teaching doc, a solution archive, and a deep-dive appendix.

**30% of the corpus — 43,201 lines — sits inside `LC Example` / `Problems by pattern` / `Detailed examples` tail sections.**

| file | lines | example-tail | share |
|---|---:|---:|---:|
| `stack.md` | 3,483 | 2,723 | **78%** |
| `binary_indexed_tree.md` | 786 | 688 | **87%** |
| `bit_manipulation.md` | 1,289 | 960 | 74% |
| `greedy.md` | 1,660 | 1,179 | 71% |
| `set.md` | 1,382 | 906 | 65% |
| `union_find.md` | 1,882 | 1,152 | 61% |
| `monotonic_stack.md` | 1,501 | 928 | 61% |
| `matrix.md` | 2,035 | 1,185 | 58% |
| `Dijkstra.md` | 2,359 | 1,338 | 56% |
| `topology_sorting.md` | 2,454 | 1,266 | 51% |
| `2_pointers.md` | 4,658 | 2,356 | 50% |
| `backtrack.md` | 3,423 | 1,729 | 50% |
| `linked_list.md` | 2,868 | 1,419 | 49% |
| `design.md` | 2,647 | 1,308 | 49% |
| `heap.md` | 5,345 | 2,266 | 42% |
| `hash_map.md` | 4,119 | 1,734 | 42% |
| `binary_search.md` | 4,591 | 1,895 | 41% |
| `bst.md` | 3,956 | 1,468 | 37% |
| `string.md` | 3,512 | 1,310 | 37% |
| `dfs.md` | 6,033 | 2,217 | 36% |
| `bfs.md` | 4,256 | 1,203 | 28% |
| `dp.md` | 5,157 | 1,155 | 22% |
| `tree.md` | 6,485 | 1,414 | 21% |

---

## The four pathologies

### 1. The example tail dwarfs the teaching part

`stack.md` is the clearest case — and it has *no* summary or decision section at all:

```text
## 0) Concept              lines   57– 511
## 1) Core Patterns        lines  511– 761   (250 lines)
## 2) LeetCode Examples    lines  761–3483   (2,723 lines — 78% of the file)
```

The reader who wants "when do I reach for a stack" has nowhere to look.

### 2. The same problem solved 3–7 times inside one file

Counting only `###`/`####` **solution headings**, not passing mentions:

| file | duplicate headings | worst offenders |
|---|---:|---|
| `binary_search.md` | 32 | LC 34 ×**7**, LC 875 ×5, LC 704/33/153/74/410/1011 ×3 |
| `dfs.md` | 20 | LC 112/200/450/1254/694 ×3 |
| `sliding_window.md` | 15 | LC 992 ×4, LC 3/209 ×3 |
| `prefix_sum.md` | 14 | LC 303/560/370/1248/2615/769 ×3 |
| `intervals.md` | 11 | LC 56/435/986/253/729 ×3 |
| `dp.md` | 11 | LC 70/72 ×3 |
| `bfs.md` | 11 | LC 994 ×4, LC 542/127 ×3 |
| `Dijkstra.md` | 10 | LC 1631 ×**8**, LC 64 ×4 |
| `2_pointers.md` | 10 | LC 27 ×3 |
| `monotonic_stack.md` | 9 | LC 84 ×5 |

Concrete instances:

- **`binary_search.md`** solves LC 34 at §1.5, §1.7, §2.2, §2.3, §4.13, §4.16 and §2-1. §4.16 is titled `Find First and Last Position - Alternative Implementation — LC 34 — LC 34` — the double-LC-number anti-pattern, back.
- **`dfs.md`** presents LC 694 three times: `Pattern 8` (line 96), `Template 8` (line 937), `§2-28` (line 5360, 226 lines). Its 18 `Problem Categories` map **1:1** onto its 16 `Templates` — two parallel presentations of one list. Four more LC examples (`2-28`…`2-31`) sit *after* `Related Topics`, i.e. appended past the end of the document.
- **`tree.md`** §3.14 fully solves LC 606 and LC 536; §4-1 then re-solves LC 606 while *linking back to* §3.14 for the explanation, and §4-7 re-solves LC 536 in 263 lines with three variants. §0-2's nine "Common Tree Patterns" (549 lines) restate §1's templates, which restate §3's algorithms — three parallel passes over the same nine ideas.
- **~450 `V0 / V0' / V0'' / V1` variant blocks** remain: `tree.md` 40, `dfs.md` 37, `backtrack.md` 35, `stack.md` 32, `array.md` 28, `hash_map.md` 25.

### 3. A sheet duplicating its own satellites

Shared **worked problems** (heading-level), which is what the Scope lines were supposed to prevent:

| pair | shared | problems |
|---|---:|---|
| `tree.md` × `tree2.md` | **21** | 98, 100, 101, 104, 105, 110, 111, 114, 117, 124, 199, 222, 226, 236, 297, 606, 617, 863, 987, 1448, 1740 |
| `dp.md` × `dp_pattern.md` | 13 | 62, 64, 70, 91, 300, 309, 312, 322, 403, 410, 416, 1143, 1751 |
| `stack.md` × `monotonic_stack.md` | 12 | 32, 84, 155, 388, 402, 496, 503, 735, 739, 901, 907, 2104 |
| `tree2.md` × `dfs.md` | 9 | 94, 98, 112, 113, 129, 236, 297, 543, 606 |
| `tree.md` × `dfs.md` | 7 | 98, 236, 297, 536, 606, 652, 662 |
| `dp.md` × `recursion_to_dp.md` | 7 | 62, 70, 72, 198, 322, 403, 516 |
| `bst.md` × `dfs.md` | 6 | 98, 449, 538, 669, 701, 776 |
| `dfs.md` × `bfs.md` | 6 | 200, 399, 417, 623, 662, 1530 |
| `intervals.md` × `scanning_line.md` | 6 | 56, 253, 452, 986, 1235, 1465 |

`tree2.md`'s Scope says "template-first, no theory" and `tree.md`'s says "concepts" — but they solve the same 21 problems. `dfs.md`'s example tail is ~21 tree/BST problems: it teaches DFS by re-solving `tree.md` and `bst.md`.

### 4. Multiple decision/summary sections, and catch-alls creeping back

- `dfs.md`: `Pattern Selection Strategy` + `Summary & Quick Reference` + `Quick Decision Tree: Which DFS Pattern to Use?`
- `bst.md`: `Pattern Selection Strategy` + `Summary & Quick Reference` + `🚀 Quick Decision Tree`
- `Dijkstra.md`: three
- `binary_search.md`: `3) Summary & Quick Reference` lands at line 1,952 — and 2,639 more lines follow it, including `5) Additional High-Frequency Templates`
- Catch-alls regrown: `binary_search.md:3989` *Classic Google Patterns*, `hash_map.md:3791` *Google-Specific Patterns*, `complexity_cheatsheet.md:589` *Classic Google Interview Problems*

**Mixed skeletons** (forbidden by [`CLAUDE.md`](../CLAUDE.md#which-skeleton)) in `hash_map.md`, `bst.md`, `string.md`, `dfs.md` — Skeleton B's `Templates & Algorithms` followed by Skeleton A's `0) Concept` / `1) General form` / `2) LC Example`.

**Flat dumps**: `python_trick.md` is 3,672 lines under a *single* `## 1) Examples` heading with 68 unsorted `###`s. `java_trick.md` has both `6) Other tricks` and `9) Others`.

---

## The reference model is itself only half-done

`dp.md` was the file the split was piloted on — five satellites extracted (`knapsack`, `dp_string`, `dp_bitmask`, `dp_digit`, `dp_monotonic_stack`). It is **still 5,157 lines**:

```text
## Templates & Algorithms          121–2700   (2,579 lines)
## Comprehensive Pattern Analysis 2700–3473   (773, overlapping the above)
## LC Examples                    3582–4655   (1,073)
## Advanced DP Techniques         5042–5157   (trailing bolt-on)
```

plus 11 duplicate LC headings and 13 problems shared with `dp_pattern.md`. **The satellites were extracted but the parent was never trimmed.** "Do what we did for `dp.md`" therefore has to mean *split **and** trim*, or the outcome is 20 files of 5,000 lines.

---

## Plan

### Rule 1 — three documents per heavy topic

| role | file | contents | ceiling |
|---|---|---|---:|
| main | `X.md` | Overview → Problem Categories → decision table → canonical templates → 5–8 must-know worked examples → Summary | **1,500 lines** |
| archive | `X_examples.md` | the long tail of worked problems, one canonical solution each | — |
| appendix | `X_advanced.md` | rare / hard techniques a first pass should skip | — |

Where a satellite already exists (`tree2.md`, `monotonic_stack.md`, `dp_pattern.md`, `binary_tree.md`), it absorbs the material instead of a new file being invented.

### Rule 2 — what the main sheet may contain

1. **One canonical solution per problem per language.** A second variant needs a stated reason (different complexity, different language idiom, distinct trick).
2. **One** decision section. **One** summary section.
3. An example may not re-solve what a template above already solves — link to the template.
4. A problem owned by another sheet moves there, or is reduced to a one-line pointer.
5. No catch-all headings. New material is filed under the pattern it belongs to.

### Tier 1 — the eight tier-5 sheets over 4,000 lines

| file | now | main target | split out |
|---|---:|---:|---|
| `tree.md` | 6,485 | ~1,400 | `tree_lca_distance.md`, `tree_codec.md` (§3.13–3.14), `tree_construction.md`, `tree_examples.md`; fold §0-2's nine patterns into §1 templates |
| `dfs.md` | 6,033 | ~1,300 | collapse `Patterns 1–18` into `Templates 1–16`; `dfs_advanced.md` (Tarjan, Hierholzer, depth-indexed stack); `dfs_examples.md` |
| `heap.md` | 5,345 | ~1,400 | `heap_examples.md`; `Language APIs` is a `Collection.md` concern |
| `dp.md` | 5,157 | ~1,500 | finish the pilot: dedup against `dp_pattern.md`, tail → `dp_examples.md` |
| `2_pointers.md` | 4,658 | ~1,200 | QuickSelect + median-of-medians (~500 lines, mis-filed) → `2_pointers_quickselect.md`; `2_pointers_examples.md` |
| `binary_search.md` | 4,591 | ~1,100 | largest dedup win (32 duplicate headings); `binary_search_on_answer.md`; `binary_search_examples.md` |
| `bfs.md` | 4,256 | ~1,200 | `bfs_examples.md`; `bfs_advanced.md` (multi-source, bidirectional, 0-1 BFS) |
| `hash_map.md` | 4,119 | ~1,200 | un-mix the skeletons; `hash_map_examples.md` |

### Tier 2 — same treatment, smaller files ✅ **done**

`bst.md`, `string.md`, `stack.md` (single largest tail share), `backtrack.md`, `sliding_window.md`, `graph.md`, `linked_list.md`, `design.md`, `array.md`, `topology_sorting.md`, `tree2.md`, `Dijkstra.md`, `prefix_sum.md`, `matrix.md`, and the two language sheets (`python_trick.md`, `java_trick.md` — categorise the flat dump, delete `Other tricks` / `Others`).

Delivered in three passes: six sheets in [PR #116](https://github.com/yennanliu/CS_basics/pull/116), then the remaining ten in [PR #117](https://github.com/yennanliu/CS_basics/pull/117) as batch A (`linked_list`, `design`, `array`, `java_trick`, `python_trick`) and batch B (`topology_sorting`, `matrix`, `prefix_sum`, `Dijkstra`, `tree2`). **Ten sheets became twenty-four**; the largest file in the set went from 3,672 lines to 2,085.

Four targets were missed on purpose, and each is a judgement worth keeping:

- **`design.md` 265** — its concept half was always 210 lines; every other line was one of its two satellites. Padding it back to 1,000 would mean re-deriving what the satellites own.
- **`python_trick.md` 2,085** — 230 of those arrived *after* the split, when `array.md`'s multi-key sort essay moved in and replaced a 40-line subset of itself. The file's problem was never length; it was 68 entries under a single heading in no order.
- **`array.md` 1,556** — a hub now, routing to the eight families that own most array-tagged problems.
- **`tree2.md` 1,935** — the rest of its reduction depends on a cross-file call, below.

Two contract items were added by defects these passes hit, and hold for any future tier:

1. **GitHub does not trim the slug it builds.** A heading ending in a star run keeps a trailing `-`; 74 anchors under `doc/cheatsheet` already depend on that. A checker that strips the star run before slugifying will wave broken links through.
2. **Re-filing creates duplicate sibling headings.** Sections that were far apart arrive next to each other carrying generic `h4`s — `The Rule`, `Summary Table`, `Core Idea`, `Similar LC problems` all collided this way. Fail the batch on a repeated heading under the same parent, and qualify each collision with the section it belongs to.

### Tier 3 — dedup only, no split

`greedy.md`, `union_find.md`, `set.md`, `bit_manipulation.md`, `monotonic_stack.md`, `intervals.md`, `binary_indexed_tree.md`, `segment_tree.md`, `scanning_line.md`, `sort.md`, `trie.md`, `math.md`. High tail share, but small enough that Rule 2 alone fixes them.

Re-measured at the end of Tier 2 — **12 sheets, 18,012 lines, none over 1,882**. Two things changed from what this plan assumed:

| file | lines | example tail | dup LC |
|---|---:|---:|---:|
| `union_find` | 1,882 | 61% | **18** |
| `math` | 1,804 | 12% | 3 |
| `scanning_line` | 1,797 | 39% | **15** |
| `trie` | 1,792 | 67% | 8 |
| `sort` | 1,781 | 28% | 6 |
| `greedy` | 1,660 | **83%** | 7 |
| `monotonic_stack` | 1,500 | 55% | 11 |
| `set` | 1,381 | 79% | 1 |
| `segment_tree` | 1,317 | 53% | 11 |
| `bit_manipulation` | 1,289 | **88%** | 2 |
| `intervals` | 1,023 | 39% | 10 |
| `binary_indexed_tree` | 786 | **93%** | 5 |

- **The duplication hot spots are `union_find` (18 distinct LC numbers in more than one `###` heading) and `scanning_line` (15)** — both worse than `prefix_sum`'s 14, which was the worst measured before this pass. `monotonic_stack` at 11 is not the leader this plan expected.
- **`greedy`, `bit_manipulation` and `binary_indexed_tree` are 83–93% example tail.** At those shares "dedup only" is the wrong instruction: what is left after Rule 2 is a sheet that is almost entirely worked solutions, so they want the same parent/satellite split the earlier tiers used, not a trim.

### No action

The ~30 sheets under ~1,200 lines that are already right: `knapsack.md`, `kadane_algorithm.md`, `palindrome.md`, `hashing.md`, `dp_string.md`, `dp_digit.md`, `dp_bitmask.md`, `dp_monotonic_stack.md`, `monotonic_queue.md`, `iterator.md`, `n_sum.md`, `add_x_sum.md`, `difference_array.md`, `complexity_*`, `Bellman-Ford.md`, `Floyd-Warshall.md`, `shortest_path_comparison.md`, `string_matching_kmp_rolling_hash.md`, `python_gotchas.md`, `ood_design.md`, `concurrency_patterns.md`, `diff_toposort_quickunion.md`, `array_overlap_explaination.md`, `recursion.md`, `recursion_to_dp.md`, `advanced_*`, `streaming_algorithms.md`, `knapsack_01_zh.md`, `tree_backtrack.md`.

### Carried forward from Tier 2

- **Cross-file consolidation — the phase every tier feeds and none finishes.** The first named item is now measured: **`tree.md` and `tree2.md` both hold traversal templates**, and on normalised code `tree.md`'s Level-Order template and `tree2.md`'s `1.4)` are identical statement for statement, LC 987 shares 70%, and preorder / inorder / postorder share 48–59% each. Resolving it means deciding which of the two sheets owns traversal templates at all. Other hard numbers, re-measured at the end of Tier 2: one LC 449 code block is byte-identical between `bst_examples` and `dfs_examples`; LC 701 is duplicated in both languages; LC 98 is named in 9 sheets and LC 49 in 9; `monotonic_stack.md` and `stack_examples.md` share 16 LC numbers. The LC-name counts include index and table mentions, so they bound the work rather than measure it — each pair needs the same before/after check the Tier 2 batches used.
- **`array.md`'s `1-1-5) Sort Array` and `1-1-6) Flatten Array`** — 270 lines of language-level array operations that may belong with `sort.md`. Left in place deliberately; it is a cross-file call.
- **`2_pointers_quickselect.md` vs `advanced_divide_and_conquer.md`** — the latter owns QuickSelect in more depth; the satellite is registered but should probably be folded in.
- **~25 pre-existing correctness bugs** from the PR #114 review, verified as untouched by Tier 1: un-seeded BFS queues, undefined identifiers, missing null guards, Hoare's return value used as a pivot index.
- **The site slugify fix** — one line in `site/build-lib.js`. It collapses every run of non-alphanumerics to a single `-`, so ` — ` becomes `-` where GitHub gives `--`; ~93 anchors resolve on GitHub and 404 on the published page.
- **52 broken anchors elsewhere in `doc/cheatsheet`**, found by a repo-wide sweep under GitHub's rule at the end of Tier 2. None is in a file Tier 2 touched. Most look like the trailing-`-` star-run case above.

### Still open from the Aug review

- `lc_category.md` — 1,516 lines, a verbatim paste of an external GitHub README including 12 `TODO` markers that are not this repo's. Should be a link.
- `code_interview_general_cheatsheet.md` — 33 lines, belongs inside another doc.
- `time = O(...)` coverage is 35% of ~3,177 code blocks.

---

## Expected outcome

**143,068 → roughly 105,000–115,000 lines**, no sheet over ~1,500 lines, ~20 new satellite files, nothing worth keeping lost. Recoverable duplication runs 15–25% per heavy sheet.

## Constraints

- Every new `doc/cheatsheet/*.md` needs a [`data/cheatsheet_meta.json`](../data/cheatsheet_meta.json) entry — **the site build fails otherwise, on purpose.**
- Every new file opens with H1 → `> **Scope**` → `> **See also**` → `## LeetCode Problem Lists`.
- The parent's `See also` line must name each satellite it spawned, and each satellite must point back.
- Moving a section breaks inbound anchor links from other sheets. A repo-wide link check belongs at the end of each phase.
- `_site/` is generated by CI and gitignored — never commit it. Verify locally with `SKIP_FONTS=1 bash site/build.sh`.

## Risk

The mechanics are safe; the *judgement* is not. Deciding which of three `V0 / V0' / V1` spellings teaches something the others do not is exactly why the August pass skipped them. Mitigation: phase by cluster — **tree → dfs/bfs/graph → arrays & strings → dp → hashing/stacks/queues** — so each phase is reviewable on its own, and never delete a block whose duplicate is only *similar*; verify same problem, same language, same approach first.
