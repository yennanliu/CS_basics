# G L3 Coding — Final-Week Review

> **Scope** — What to read, in what order, and which problems to drill in the last seven
> days before a G SWE L3 coding loop, chosen from what this repo already has. Not a new
> problem list: every problem here comes from [`data/l3_core.json`](../data/l3_core.json) or
> [`must_lc_list.md`](./must_lc_list.md), and every pattern from [`cheatsheet/`](./cheatsheet/).
> **See also**: [`derivation_cards.md`](./derivation_cards.md) — the per-problem cards this
> plan points at; [`lc-readiness-guide.md`](./lc-readiness-guide.md) — the cost curve behind
> the day order.

Numbers are as of **2026-10-26** (`python3 script/l3_core.py status`). Re-run it; do not
trust a count typed here once the week starts.

---

## 1. The bar, in one paragraph

G L3 has no system-design round, so the coding rounds are the packet. The bar is **a clean
medium in ~45 minutes**: the pattern named early, the complexity stated and justified,
readable code, and edge cases tested out loud *before* saying "done". It is not a clever
hard. So this week is for retention and execution on problems already solved, not for new
volume.

The one execution gap the readiness run measured: submissions are rejected **more often on
easy problems (44%) than on hard ones (31%)**. That is speed without a verification habit.
Every timed drill this week ends with a dry run on one normal and one edge input.

---

## 2. Which list to drill from

| Priority | Source | Size | Use it for |
|---|---|---|---|
| **1** | [`data/l3_core.json`](../data/l3_core.json) — Blind 75 ∪ (NeetCode 150 ∩ README `MUST`) | **100** | The drill set. Fixed, so its `ok` share means something day to day. |
| 2 | [`must_lc_list.md`](./must_lc_list.md) — every README row flagged `MUST`, by category | 192 | Breadth pool *after* the core's `again` rows are cleared. Too big for one week. |
| 3 | [`g_recent_asked.md`](./g_recent_asked.md) — scraped G Discuss posts, Jan–Aug 2026 | — | A sanity check, not a list. Self-reported, and no problem appears in more than 4 threads. |

Where the core stands, by the log's latest verdict:

| Verdict | Count | This week |
|---|---|---|
| `again` | **28** | Primary target. 24 of them are chronic (8+ attempts) — read the card before re-solving. |
| no verdict | **25** | Second. Many are stale (LC 40, 78: ~555 days; the four bit problems: ~890 days). |
| `ok` | 47 | Only if stale (>30 days) and in a weak section. |

The 28 `again` rows, by section:

| Section | `again` problems |
|---|---|
| Arrays / strings / two pointers | 5, 15, 19, 54, 238, 271, 647 |
| Linked list / design / heap / queue | 23, 143, 146, 206, 239, 295 |
| Trees / trie | 124, 208, 211 |
| Backtracking | 17, 39, 51, 131, 139, 212 |
| Dynamic programming | 53, 72, 91, 1143 |
| Hashing / sorting / greedy | 128, 621 |

---

## 3. Reading order

Read these in order. Tiers 1–2 are the ones to finish no matter what; tier 3 is per day.

### Tier 1 — triage and complexity (day 1, ~2 h)

| # | Doc | Why first |
|---|---|---|
| 1 | [`pattern_recognition.md`](./pattern_recognition.md) | Problem signal → pattern. The first 3 minutes of every round. |
| 2 | [`cheatsheet/lc_pattern.md`](./cheatsheet/lc_pattern.md) | The same triage, with each pattern's template. |
| 3 | [`cheatsheet/complexity_cheatsheet.md`](./cheatsheet/complexity_cheatsheet.md) | You will be asked for time and space in every round. |
| 4 | [`cheatsheet/python_complexity.md`](./cheatsheet/python_complexity.md) | The cost of each Python built-in you will write (`list.pop(0)`, `in` on a list, slicing). |

### Tier 2 — the cards for chronic problems (spread over the week)

[`derivation_cards.md`](./derivation_cards.md) — 40 cards, one per chronic problem: the
invariant, the loop or recursion contract, the line that sets the complexity, the edges that
keep failing. **Read the card, then re-derive the solution from it.** Do not re-read the
solution file. The day plan below names which cards go with which day.

### Tier 3 — core DSA and pattern cheatsheets (one block per day)

The cheatsheets carry their own interview weight, `tier` in
[`data/cheatsheet_meta.json`](../data/cheatsheet_meta.json): **5** must know, **4** high
value, **3** worth knowing, **2** niche. The week reads every tier-5 sheet and the tier-4
sheets that cover a core `again` row. Where every other sheet in `cheatsheet/` sits is in
[§7](#7-every-cheatsheet-placed).

| Day | Tier 5 — read | Tier 4 — read |
|---|---|---|
| 1 | [`array`](./cheatsheet/array.md), [`hash_map`](./cheatsheet/hash_map.md), [`2_pointers`](./cheatsheet/2_pointers.md), [`sliding_window`](./cheatsheet/sliding_window.md), [`string`](./cheatsheet/string.md) | [`prefix_sum`](./cheatsheet/prefix_sum.md), [`palindrome`](./cheatsheet/palindrome.md), [`n_sum`](./cheatsheet/n_sum.md) |
| 2 | [`linked_list`](./cheatsheet/linked_list.md), [`stack`](./cheatsheet/stack.md), [`heap`](./cheatsheet/heap.md) | [`monotonic_stack`](./cheatsheet/monotonic_stack.md), [`design`](./cheatsheet/design.md) |
| 3 | [`binary_search`](./cheatsheet/binary_search.md), [`binary_search_on_answer`](./cheatsheet/binary_search_on_answer.md), [`tree`](./cheatsheet/tree.md), [`binary_tree`](./cheatsheet/binary_tree.md), [`bst`](./cheatsheet/bst.md), [`tree_lca_distance`](./cheatsheet/tree_lca_distance.md) | [`trie`](./cheatsheet/trie.md), [`tree2`](./cheatsheet/tree2.md) |
| 4 | [`bfs`](./cheatsheet/bfs.md), [`dfs`](./cheatsheet/dfs.md), [`graph`](./cheatsheet/graph.md) | [`topology_sorting`](./cheatsheet/topology_sorting.md), [`union_find`](./cheatsheet/union_find.md), [`Dijkstra`](./cheatsheet/Dijkstra.md) |
| 5 | [`backtrack`](./cheatsheet/backtrack.md) | [`recursion`](./cheatsheet/recursion.md) |
| 6 | [`dp`](./cheatsheet/dp.md), [`knapsack`](./cheatsheet/knapsack.md) | [`dp_pattern`](./cheatsheet/dp_pattern.md), [`dp_string`](./cheatsheet/dp_string.md) |
| 7 | — | [`greedy`](./cheatsheet/greedy.md), [`intervals`](./cheatsheet/intervals.md) |

In each sheet, read the **template and its invariant**, not the worked examples. The
examples are what the drills are for.

---

## 4. The seven days

Each day: read the block (≤ 1 h), then drill **4–6 problems timed at 25 min each**, in
Python, talking out loud. The expensive sections (recursion, trees, backtracking, graphs)
come early. That is where the cost curve says passes are spent.

| Day | Block | Drill (`again` first, then no verdict) | Cards |
|---|---|---|---|
| 1 | Tier 1 + arrays & hashing | 15, 238, 128, 5, 647, 271 · then 424, 217 | 3, 424, 128, 5 |
| 2 | Linear structures | 206, 143, 23, 146, 295, 239 | 143, 739 |
| 3 | Search & trees | 124, 208, 211 · then 230, 235, 100 | 300, 153, 104, 110 |
| 4 | Graphs | 200, 207, 133, 269, 127 *(all `ok` — keep them warm; G posts repeat 200 and 207)* · then 417 | 207, 133, 261, 323, 127, 269 |
| 5 | Backtracking | 17, 39, 131, 139, 51, 212 · then 22, 46, 78, 40, 79 | 79, 139 |
| 6 | DP | 53, 91, 1143, 72 · then 309, 329, 97 | 322, 91, 1143, 97, 416, 213 |
| 7 | Greedy & intervals + **two full mocks** | 621, 19, 54 · then 45, 55, 57 | 253, 435 |

The four bit-manipulation core problems (190, 191, 268, 371) are last. They are short, so do
them only if day 7 has room.

**The day before the loop**: no new problems. Re-read tier 1 and the cards for that week's
remaining `again` rows. Then stop.

---

## 5. The daily loop, with the repo's tools

```text
/l3-core next 5                  # today's picks: never logged → no verdict → again → stale ok
/lc-coach mock interview me on LC 146   # a 45-min mock, scored like a real G packet
/lc-coach review <file>          # after each timed solve: verdict + the line that sets complexity
/lc-log 146 ok, 295 again!!      # record the day — this is what the next pick reads
/l3-core status --only again     # end of day: what is still coming back
```

What counts as `ok` this week: solved **unaided**, first attempt, **under 25 minutes**, with
complexity stated and edges tested. Anything less is `again`. Being honest here is what makes
tomorrow's `/l3-core next` pick the right problems.

The [Review Plan](https://yennanliu.github.io/CS_basics/lc-review-plan.html) page answers the
same question across the whole log, not just the core, if a section runs dry.

---

## 6. What to skip this week

| Skip | Why |
|---|---|
| `system_design/`, `doc/faq/` | No design round at L3; the FAQs are backend reference. |
| Hard-tier grinding, LC contests | L3 volume and breadth are already cleared. More hards is the wrong lever. |
| `leetcode_company_V*/`, `google_leetcode_problems_by_tags.md` | Raw frequency dumps. The core set already distilled them. |
| [`goog_swe_prep_plan_*.md`](./goog_swe_prep_plan_claude.md) | Multi-week plans from Mar 2026, not a final week. |
| [`g_swe_final_week_review.md`](./g_swe_final_week_review.md) | Written in Jun 2026 for a Forward Deployed Engineer loop, not L3 SWE. |
| Java solutions | Python is the interview language. |

---

## 7. Every cheatsheet, placed

All 134 sheets in [`cheatsheet/`](./cheatsheet/), by their `tier`. Read top to bottom. Stop
wherever the week runs out.

### Tier 5 — must know (24): all read in §3

Every one is on a day in [§3](#tier-3--core-dsa-and-pattern-cheatsheets-one-block-per-day).
[`knapsack_01_zh`](./cheatsheet/knapsack_01_zh.md) is the 中文 walk-through of [`knapsack`](./cheatsheet/knapsack.md). Read whichever is faster.

### Tier 4 — high value (55): 15 in §3, the rest as backup

A tier-4 `_examples` / `_advanced` sheet is for **when that day's drill comes back `again`**.
Open the sheet for the pattern that failed, not all of them.

| Day | Backup sheets | Core problems they cover |
|---|---|---|
| 1 | [`2_pointers_examples`](./cheatsheet/2_pointers_examples.md), [`array_examples`](./cheatsheet/array_examples.md), [`hash_map_examples`](./cheatsheet/hash_map_examples.md), [`sliding_window_examples`](./cheatsheet/sliding_window_examples.md), [`sliding_window_advanced`](./cheatsheet/sliding_window_advanced.md), [`string_examples`](./cheatsheet/string_examples.md), [`prefix_sum_advanced`](./cheatsheet/prefix_sum_advanced.md), [`matrix`](./cheatsheet/matrix.md), [`matrix_examples`](./cheatsheet/matrix_examples.md) | 15, 54, 238, 424 |
| 2 | [`linked_list_examples`](./cheatsheet/linked_list_examples.md), [`2_pointers_linkedlist`](./cheatsheet/2_pointers_linkedlist.md), [`stack_examples`](./cheatsheet/stack_examples.md), [`stack_expression_parsing`](./cheatsheet/stack_expression_parsing.md), [`queue`](./cheatsheet/queue.md), [`heap_examples`](./cheatsheet/heap_examples.md), [`heap_advanced`](./cheatsheet/heap_advanced.md), [`design_examples`](./cheatsheet/design_examples.md) | 19, 23, 143, 146, 239, 295 |
| 3 | [`binary_search_examples`](./cheatsheet/binary_search_examples.md), [`patience_sorting`](./cheatsheet/patience_sorting.md), [`bst_examples`](./cheatsheet/bst_examples.md), [`bst_advanced`](./cheatsheet/bst_advanced.md), [`tree_codec`](./cheatsheet/tree_codec.md), [`tree_construction`](./cheatsheet/tree_construction.md), [`sort`](./cheatsheet/sort.md), [`2_pointers_quickselect`](./cheatsheet/2_pointers_quickselect.md) | 105, 297, 300, 347 |
| 4 | [`topology_sorting_examples`](./cheatsheet/topology_sorting_examples.md), [`Dijkstra_examples`](./cheatsheet/Dijkstra_examples.md) | 207, 269 |
| 5 | [`backtrack_advanced`](./cheatsheet/backtrack_advanced.md) | 51, 131, 212 |
| 6 | [`dp_advanced`](./cheatsheet/dp_advanced.md), [`dp_loop_order`](./cheatsheet/dp_loop_order.md), [`recursion_to_dp`](./cheatsheet/recursion_to_dp.md) | 72, 91, 139, 1143 |
| Any evening | [`python_trick`](./cheatsheet/python_trick.md), [`python_trick_stdlib`](./cheatsheet/python_trick_stdlib.md), [`python_trick_indexing`](./cheatsheet/python_trick_indexing.md), [`time_space_complexity`](./cheatsheet/time_space_complexity.md), [`Collection`](./cheatsheet/Collection.md) | Python idiom, and arguing complexity out loud |

**Skip:** [`java_trick`](./cheatsheet/java_trick.md), [`java_trick_collections`](./cheatsheet/java_trick_collections.md), [`java_trick_strings_sorting`](./cheatsheet/java_trick_strings_sorting.md) (Python is the
interview language) and [`ood_design`](./cheatsheet/ood_design.md) (the low-level-design round, not an L3 coding round).

### Tier 3 — worth knowing (41): only the ones a core problem needs

| Read if | Sheet |
|---|---|
| LC 53 or 152 fails | [`kadane_algorithm`](./cheatsheet/kadane_algorithm.md) |
| LC 239 fails | [`monotonic_queue`](./cheatsheet/monotonic_queue.md) |
| LC 121 or 309 fails | [`stock_trading`](./cheatsheet/stock_trading.md) |
| LC 252 or 253 fails | [`scanning_line`](./cheatsheet/scanning_line.md) |
| Day 7 reaches the bit problems | [`bit_manipulation`](./cheatsheet/bit_manipulation.md) |
| A mock trips on a Python detail | [`python_gotchas`](./cheatsheet/python_gotchas.md) |
| Complexity answers are slow | [`complexity_drills`](./cheatsheet/complexity_drills.md) |

**Skip the other 34 this week:** `add_x_sum`, `difference_array`, `prefix_sum_examples`, `string_matching_kmp_rolling_hash`, `string_operations`, `hashing`, `set`, `set_examples`, `heap_language_apis`, `tree_backtrack`, `tree_examples`, `trie_examples`, `bfs_advanced`, `bfs_examples`, `dfs_advanced`, `dfs_examples`, `diff_toposort_quickunion`, `graph_advanced`, `graph_examples`, `shortest_path_comparison`, `union_find_examples`, `advanced_divide_and_conquer`, `backtrack_examples`, `dp_bitmask`, `dp_examples`, `dp_monotonic_stack`, `greedy_examples`, `scanning_line_examples`, `binary_indexed_tree`, `segment_tree`, `bit_manipulation_examples`, `math`, `design_patterns`, `iterator`.

### Tier 2 — niche (14): skip

`advanced_string_algorithms`, `Bellman-Ford`, `Floyd-Warshall`, `dp_digit`, `array_overlap_explaination`, `memory_constrained_algorithms`, `streaming_algorithms`, `combinatorics_math_patterns`, `math_logic_puzzles`, `advanced_simulation`, `concurrency_patterns`, plus three stubs that only redirect
(`priority_queue`, `code_interview_general_cheatsheet`, `lc_category`).
