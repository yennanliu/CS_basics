# Tier 1 Split — Progress Snapshot

Execution log for Tier 1 of [`cheatsheet-split-plan-2026-08.md`](cheatsheet-split-plan-2026-08.md): the eight tier-5 cheatsheets over 4,000 lines.

**Status: Tier 1 complete and the site build passes.** Registration, anchor repair and build verification are done; what remains is human review of the deletion calls and the cross-file phase. See [Verification](#verification) and [Still open](#still-open).

Two of the eight passes (`dfs`, `dp`) terminated early on an API session limit before running their own verification. Their output was checked centrally instead — both files were structurally complete, and both pass every check below.

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

## Verification

All 26 files were checked centrally with one script, so the two passes that died on a session limit are covered on the same terms as the six that reported:

| check | result |
|---|---|
| Exactly one H1 per file | ✅ 26/26 |
| Untagged or unclosed code fences | ✅ 0 |
| Heading-level jumps (`h2`→`h4`) | ✅ 0 |
| Headings stating one LC number twice | ✅ 0 |
| Duplicate LC solution headings **at section level** | ✅ 0 |
| Broken intra-file `](#…)` anchors | ✅ 0 |
| Broken cross-file `](./x.md#…)` anchors | ✅ 0 after repair |
| `data/cheatsheet_meta.json` coverage | ✅ 98 sheets, every `.md` registered, every entry has a file |
| `SKIP_FONTS=1 bash site/build.sh` | ✅ 98 cheatsheet pages, 417 files in `_site/` |

Registration applied — 18 entries. `binary_search_on_answer` went in at **tier 5**, not 4: the `Search & Sort` category blurb in `cheatsheet_meta.json` already says binary search on the answer is "the single most under-practised tier-5 skill". `heap_language_apis` is filed under `Trees & Heaps` rather than `Language Toolkit` so the four heap sheets sort together, and is the only new sheet needing a `title` override (its H1 contains backticks).

Inbound links repaired — three in `priority_queue.md`'s redirect table (`#2-lc-example` → `heap_examples.md#lc-examples`, `#problems-by-pattern` → `heap.md#decision-table--which-heap-pattern`, and the API row now points at `heap_language_apis.md`), one in `scanning_line.md`, and one in `union_find.md` that a repo-wide sweep caught: it pointed at `dfs.md#pattern-18-…`, now `dfs_advanced.md#template-11-…`.

### Two anchor conventions live in this repo

Worth knowing before the next phase, because it nearly caused a bad edit here. Anchor links across `doc/cheatsheet/` resolve under **two different slug rules**:

- **GitHub's** — strip punctuation, replace *each* space with `-`, keep trailing dashes. So `### 1.5) Find Boundaries (LC 34) ⭐⭐⭐⭐⭐` → `#15-find-boundaries-lc-34-`, and ` — ` becomes `--`.
- **the site's** — [`site/build-lib.js`](../site/build-lib.js) `slugify()`, which collapses every non-alphanumeric run to a single `-` and trims. Same heading → `#1-5-find-boundaries-lc-34`.

93 anchors follow GitHub's rule, 74 follow the site's. GitHub's is the majority and is what untouched sheets use (`Dijkstra.md`, `linked_list.md`, `bit_manipulation.md`, `hashing.md`), so **the 93 are correct when browsing the repo and silently broken on the published site.** Only 4 anchors repo-wide resolve under neither rule, all pre-existing in files this work did not touch: `00_template.md`, `backtrack.md`, `stack.md`, `tree2.md`.

The cheap fix is one change in `site/build-lib.js` — make `slugify()` match GitHub's rule — which repairs ~93 site links at once and costs nothing elsewhere. It is build tooling rather than content, so it was left out of this pass.

## Still open

1. **Cross-file consolidation.** Every pass was forbidden from touching its neighbours, so the cross-file duplication measured in the plan is untouched and in some cases now concentrated in the `_examples` sheets: `tree.md` × `tree2.md` 21 shared problems, `dfs.md` × the tree/BST sheets ~23, `dp.md` × `dp_pattern.md` 13, `stack.md` × `monotonic_stack.md` 12, `bfs.md` × `graph.md` 7, `hash_map.md` × `prefix_sum.md` (LC 560, 1248, 303, 325, 523, 525, 930, 974, 724). This is its own phase.

   One finding from that phase arrived early and is the first thing to settle: `advanced_divide_and_conquer.md` **already owns QuickSelect in depth** (`Template 5: Quickselect`, Java + Python, Hoare and 3-way partition variants, an LC 973 variation and a variations table). The new `2_pointers_quickselect.md` largely restates it in Python, and `sort.md:497` carries a `Quick Select — LC 215` template too. It is registered for now — that is reversible — but the likely end state is folding it into `advanced_divide_and_conquer.md`.

2. **Review of the judgement calls.** Roughly 755 lines were deleted from `tree.md` alone as verified duplicates, and ~1,204 from `binary_search.md` — the largest of the batch. Each deletion has a stated reason. Those reasons are worth reading before this merges; the plan flags this as the one genuinely risky part of the work. Two worth knowing: `binary_search.md` §4.9 (LC 410) was an **empty stub** — a fence containing one comment — and its LC 278 copy was both duplicate *and* buggy (`end = mid` never used), with the correct twin surviving.

3. **Skeleton A vs B for the small satellites.** `tree_construction.md` (382 lines) and `2_pointers_quickselect.md` (522) are under `CLAUDE.md`'s ~800-line threshold, where Skeleton A is the right shape, but were written with Skeleton B for consistency across the batch. Worth a second look.

4. **The site slugify fix** described above — one line in `site/build-lib.js`, repairs ~93 anchors on the published site.

## Next

Tier 2 of the plan: `bst`, `string`, `stack` (78% example tail, the largest single win left), `backtrack`, `sliding_window`, `graph`, `linked_list`, `design`, `array`, `topology_sorting`, `tree2`, `Dijkstra`, `prefix_sum`, `matrix`, `python_trick`, `java_trick`.
