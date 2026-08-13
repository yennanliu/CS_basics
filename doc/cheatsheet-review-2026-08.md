# Cheatsheet Review — August 2026

A structural review of all 74 markdown files in [`doc/cheatsheet/`](cheatsheet/) (142,281 lines at the time of review), and the cleanup that followed.

This document exists so the same drift can be recognised early next time. The rules distilled from it now live in [`CLAUDE.md`](../CLAUDE.md#cheatsheet-style-guide) and [`doc/cheatsheet/00_template.md`](cheatsheet/00_template.md).

---

## Verdict

The content was strong. Every problem found was **organisational**, not factual:

1. Two competing document skeletons, applied inconsistently — 24 files used one, 26 the other, 4 mixed both, ~25 used neither.
2. Sections appended to the end of files (`LC Examples`, `Missing Google Patterns`) that restated material already covered above.
3. Several topics split across 2–5 files with no stated division of labour.

Clean at the outset and still clean: **0 broken image paths, 0 broken relative links, 0 unclosed code fences, exactly 1 H1 per file.**

---

## What was measured

| Check | Before | After |
|---|---:|---:|
| Files | 74 | 75 (one merged away, two docs added) |
| Total lines | 142,281 | 141,079 |
| Untagged code fences | 371 | **0** |
| Headings repeating the LC number (`(LC 347) — LC 347`) | 68 | **0** |
| Heading-level jumps (`h2` → `h4`) | 8 files | **0** |
| Header blocks stating complexity twice | 12 files | **0** |
| Catch-all `Missing Google Patterns` sections | 11 | **0** |
| Files with a `Scope` statement | 0 | 35 |
| Java/Python code blocks carrying `time = O(...)` | 35% | 35% *(not addressed — see Not done)* |

---

## Findings and what was done about them

### 1. Two competing skeletons

**Skeleton A** (`0) Concept` → `1) General form` → `2) LC Example`) suited short docs. **Skeleton B** (`Overview` → `Problem Categories` → `Templates & Algorithms` → `LC Examples` → `Problems by Pattern` → `Pattern Selection Strategy` → `Summary`) is what every large doc had independently converged on — but `00_template.md` (21 lines) only described A.

**Done** — `00_template.md` rewritten to define both, with the rule *pick by size, not by topic; never mix them in one file; convert A→B past ~800 lines*. Recorded in `CLAUDE.md`.

### 2. Append-log sections

The recurring failure mode: a section bolted onto the end of a file re-solving problems the templates above already solved.

Verified case by case with code-similarity comparison rather than by problem number — **this mattered**, because most such sections turned out to hold *different implementations* (Python variants against Java templates), not copies. Only the genuine duplicates were removed:

| File | Removed | Why it was safe |
|---|---:|---|
| `tree2.md` | 208 lines | 9 of 11 `LC Examples` entries duplicated a numbered template above **in the same language**. The 2 that had no template (LC 98, LC 199) were kept and the section retitled to say so. |
| `priority_queue.md` | 65 lines | `LC Examples` re-solved LC 703 / 347 / 23, already done as `2-6` / `2-2` / `2-3`. Its numbering continued from the earlier section (`2-17`…), showing it was appended. |
| `tree.md` | 32 lines | `4-4)` and `4-2)` were both LC 606; `4-4)` said so itself ("Same problem as 4-2"). |
| `prefix_sum.md` | 28 lines | `Legacy Examples` held a partial LC 560 fragment superseded by Template 2. The LC 1094 snippet was its only coverage, so it was kept and refiled under a real heading. |

**Not removed after checking**: `bst.md` (778 lines) and `binary_tree.md` (298 lines) — their `2) LC Example` sections are Python solutions against Java templates above. Different content, kept.

`Missing Google Patterns` (11 files) was likewise **not** redundant — it held Tarjan articulation points, Ford-Fulkerson, floating-point binary search, re-rooting DP. It was a naming and filing problem, not duplication. All 11 were resolved one of two ways:

- **Dissolved** where the content belonged to an existing section — `dp.md`, `dp_pattern.md`, `graph.md`, `sliding_window.md` (3 templates → §1, the fixed-vs-variable note → §0, tips → §4) and `2_pointers.md` (5 techniques → §2, tips → §4).
- **Renamed to say what they contain** where the content is a coherent group of advanced techniques that earns its own section — e.g. `Advanced Tree Techniques — Binary Lifting, Re-rooting, Morris Traversal`, `Advanced Trie Variants — XOR Trie, Stream Matching, Delete`.

A first pass renamed all 11 to a single generic `Advanced / Interview-Focused Patterns`, which just moved the catch-all rather than removing it — code review caught this and it was redone as above.

### 3. Mechanical format drift

- **371 untagged code fences** — 219 ASCII traces, 114 plain output, 38 code. All tagged ` ```text ` (or the right language). `2_pointers.md` already used ` ```text ` 28×, so the convention existed; it was just unevenly applied.
- **68 headings stating the LC number twice** — `### 2-1) Kth Largest Element in an Array (LC 215) — LC 215`. Worst: `binary_search.md` (26), `priority_queue.md` (17), `kadane_algorithm.md` (12).
- **12 files stated complexity twice in the header** — a `## Time Complexity` table, then a `- **Time Complexity**:` bullet 15 lines later under `Key Properties`. Collapsed: the table keeps the numbers (enriched with whatever the bullet added — heapify O(N), array-vs-list dequeue, set union cost), the bullet becomes a pointer.
- **8 heading-level jumps** and several ambiguous duplicate headings (`bfs.md` had `Concrete Example: LC 994` and `Summary` twice each, under different parents — different content, so both were qualified rather than deleted).

### 4. Topic split across files without a stated boundary

Cross-file prose overlap was low (<3%), so these were never copy-paste — they were **undeclared scope collisions**, where two files grew toward the same subject.

| Cluster | Files | Resolution |
|---|---|---|
| Heap | `heap.md`, `priority_queue.md` | **Merged** — see below |
| Tree | `tree.md`, `tree2.md`, `binary_tree.md`, `bst.md`, `tree_backtrack.md` | Scope statements + `tree.md` restructure |
| DP | `dp.md`, `dp_pattern.md`, `recursion_to_dp.md`, `kadane_algorithm.md`, `stock_trading.md` | Scope statements + both DP docs restructured |
| Shortest path | `graph.md`, `Dijkstra.md`, `Bellman-Ford.md`, `Floyd-Warshall.md`, `shortest_path_comparison.md` | Scope statements; `graph.md`'s re-implementations replaced with a chooser table |
| Hash | `hash_map.md`, `hashing.md`, `set.md`, `Collection.md` | Scope statements |
| Stack / queue | `stack.md`, `monotonic_stack.md`, `monotonic_queue.md`, `queue.md` | Scope statements |
| Intervals | `intervals.md`, `scanning_line.md`, `difference_array.md`, `array_overlap_explaination.md`, `prefix_sum.md` | Scope statements |
| Complexity | `complexity_cheatsheet.md`, `time_space_complexity.md`, `complexity_drills.md` | Scope statements |
| Toposort / union-find | `topology_sorting.md`, `diff_toposort_quickunion.md`, `union_find.md` | Scope statements |

Every file in those clusters now opens with:

```markdown
> **Scope** — <what this file owns, and what it deliberately does not>.
> **See also**: [other.md](./other.md) — <why you'd go there>.
```

This is the cheapest guard against recurrence: a new section that does not fit the Scope line is a signal to file it elsewhere.

---

## The heap / priority_queue merge

A priority queue is the ADT; a binary heap is the implementation. Two files meant LC 215, 23, 253, 295, 347, 378, 621, 703 and 373 were each solved twice — once in Python (`heap.md`), once in Java (`priority_queue.md`) — with parallel-but-diverging template libraries, two `Complexity Quick Reference` tables, two `Template Quick Reference` tables and two `Common Patterns & Tricks` sections.

`heap.md` is now the single doc (5,342 lines, down from 6,461 across the two):

- Python templates keep the spine (`Specific Pattern Templates`, 12 entries).
- Java templates follow as `Java Template Library (PriorityQueue)`, preceded by a table pairing each Java template with its Python counterpart. Nothing was rewritten, so nothing was lost.
- `Language APIs` consolidates Python `heapq`, Java `PriorityQueue`, and the peek-without-popping section.
- Of `priority_queue.md`'s 18 worked examples, 13 duplicated either a template in the same file or an example in `heap.md`; the 5 unique ones (LC 973, 767, 480, 451, 1046) were carried over.
- `priority_queue.md` is now a redirect stub with a "where things went" table, so inbound links keep working.

---

## The tree cluster

`tree.md`'s section 3 was the worst structural damage found. It ran `3.1)` … `3.4)`, then jumped to `1-1-15)` … `1-1-22)` — and *underneath* `3.4) Flatten Binary Tree to Linked List` sat ~785 lines of `#### 1-1-3)` … `#### 1-1-14-1)` subsections about depth, LCA, tree merging, node counting and serialisation. None of it had anything to do with flattening.

Rebuilt into 14 coherent `###` sections:

- LCA (275 lines) and Merge Two Binary Trees promoted to their own sections.
- Node-counting subsections folded into the existing `3.2) Node Count Algorithms — LC 222`, which covered the same ground.
- Depth/max-path subsections folded into `Tree Height and Depth Operations`.
- Serialisation subsections folded into `Tree ⟷ String Codec Pattern`, which is exactly what they document.
- All stale `1-1-NN` cross-references and anchor links repaired.

Also: the two LC 199 sections (`3.1)` Java BFS and `4-1)` Python DFS) were merged into one, and section 4 renumbered.

---

## The DP cluster

**`dp_pattern.md`** — the `Missing Google Patterns` block held four genuinely new patterns filed nowhere. Promoted to numbered patterns `16. Game Theory / Minimax DP`, `17. DP on a DAG with Topological Sort`, `18. Monotonic Queue DP Optimization`, and moved to sit with patterns 1–15. Its Digit DP entry duplicated `§10`, so its template was folded in there. `Memoization vs Tabulation` moved under `DP Optimization Techniques`.

**`dp.md`** — `Quick Decision Tree: Which DP Pattern to Use?` was a second top-level decision section next to `Decision Framework`; folded in as a subsection. `Category 9: Monotonic Stack + DP` was a stray `h2` stranded after `Summary`, orphaned from Categories 1–8; moved up next to the other pattern content and given a lead-in explaining why it is documented in full.

---

## Code-review follow-ups (PR #58)

Four findings were raised on the PR. Each was checked against the code rather than accepted at face value:

| Finding | Verdict | Action |
|---|---|---|
| `dag_dp` accepts `source_value` but seeds `dp[0]` | **Valid** | Added a `source` parameter, seed `dp[source]`, and stopped propagating `-inf` into reachable nodes |
| Java subtraction comparators (`(a, b) -> a - b`) overflow | **Valid** | All 27 in `heap.md` rewritten to `Integer.compare`, argument order preserved |
| Renaming `Missing Google Patterns` to `Advanced / Interview-Focused Patterns` is still a catch-all | **Valid** | Redone — see above |
| Digit DP: `tight and d == limit` "can turn tight mode on again for digit 9" | **Invalid** | `and` short-circuits, so `tight` being false makes the comparison unreachable; `d == limit` and `d == digits[pos]` are equivalent. Rewritten to `digits[pos]` anyway, purely so the line stays correct if someone later edits the guard |

One sub-point was declined: converting the Dijkstra template to `long` distances with `Long.compare`. Overflow there needs a path sum above 2³¹, which no LeetCode input produces, and it would make the template diverge from the canonical one in [`Dijkstra.md`](cheatsheet/Dijkstra.md) that readers are pointed to.

The subtraction-comparator pattern also appears in other cheatsheets and in `leetcode_java/`. Only `heap.md` was fixed here — the file this PR rewrote. A repo-wide sweep is a separate change.

## Not done

Two findings were deliberately left alone, both because they need per-problem judgement rather than a rule:

- **`time = O(...)` coverage is 35%** of 3,177 Java/Python blocks. Zero in `java_trick.md` (149 blocks), `python_gotchas.md`, `complexity_drills.md`; 4% in `python_trick.md` (111 blocks). Adding these requires reading each solution, not a sweep.
- **~450 `# V0 / V0' / V0''` solution variants**, many being three near-identical spellings of one idea (`tree.md` 25, `dfs.md` 25, `backtrack.md` 22). Collapsing them means deciding which variant teaches something the others do not. `00_template.md` now states the rule for new code ("one canonical solution per problem; a second variant needs a stated reason"), but existing ones were left in place.

Two other observations worth acting on eventually:

- **`lc_category.md`** (1,513 lines) is a verbatim paste of an external GitHub README, including 12 `TODO` markers that are not this repo's. It should be a link.
- **`code_interview_general_cheatsheet.md`** is 31 lines and probably belongs inside another doc.
