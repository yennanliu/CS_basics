# LC Category Index (wisdompeak)

> **Scope** — A pointer to the wisdompeak problem taxonomy, and a map from its 29 categories to the cheatsheet in this repo that teaches each one. The taxonomy itself is not mirrored here.
> **See also**: [lc_pattern.md](./lc_pattern.md) — the curated pattern→problem map to study from; [00_template.md](./00_template.md) — the structure every sheet below follows.

## The source

The taxonomy lives upstream and is maintained there:

- **[wisdompeak/LeetCode — Readme.md](https://github.com/wisdompeak/LeetCode/blob/master/Readme.md)** — 29 categories, ~1,300 problems, each linking to a worked C++ solution.

This file used to be a verbatim copy of that README. A mirror of someone else's index
cannot be kept current, was never edited here, and taught nothing that the per-topic sheets
do not teach better — so it is a link now. What follows is the part that is genuinely this
repo's: which of our sheets covers each of their categories.

## Their category → our sheet

| wisdompeak category | Start here | Then |
|---|---|---|
| Two Pointers | [2_pointers.md](./2_pointers.md) | [sliding_window.md](./sliding_window.md), [n_sum.md](./n_sum.md) |
| Binary Search | [binary_search.md](./binary_search.md) | [sort.md](./sort.md) |
| Hash Map | [hash_map.md](./hash_map.md) | [hashing.md](./hashing.md), [set.md](./set.md) |
| Heap / Priority Queue | [heap.md](./heap.md) | [heap_advanced.md](./heap_advanced.md) |
| Tree | [tree.md](./tree.md) | [binary_tree.md](./binary_tree.md), [bst.md](./bst.md), [tree2.md](./tree2.md) |
| Segment Tree | [segment_tree.md](./segment_tree.md) | [binary_indexed_tree.md](./binary_indexed_tree.md) |
| Binary Index Tree | [binary_indexed_tree.md](./binary_indexed_tree.md) | [prefix_sum.md](./prefix_sum.md) |
| Design | [design.md](./design.md) | [design_examples.md](./design_examples.md), [ood_design.md](./ood_design.md) |
| Stack | [stack.md](./stack.md) | [monotonic_stack.md](./monotonic_stack.md), [stack_expression_parsing.md](./stack_expression_parsing.md) |
| Deque | [monotonic_queue.md](./monotonic_queue.md) | [queue.md](./queue.md) |
| DFS | [dfs.md](./dfs.md) | [dfs_advanced.md](./dfs_advanced.md), [backtrack.md](./backtrack.md) |
| BFS | [bfs.md](./bfs.md) | [bfs_advanced.md](./bfs_advanced.md) |
| Trie | [trie.md](./trie.md) | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| Linked List | [linked_list.md](./linked_list.md) | [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| Dynamic Programming | [dp.md](./dp.md) | [dp_pattern.md](./dp_pattern.md), [knapsack.md](./knapsack.md), [kadane_algorithm.md](./kadane_algorithm.md) |
| Bit Manipulation | [bit_manipulation.md](./bit_manipulation.md) | [dp_bitmask.md](./dp_bitmask.md) |
| Divide and Conquer | [advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) | [sort.md](./sort.md) |
| String | [string.md](./string.md) | [palindrome.md](./palindrome.md), [dp_string.md](./dp_string.md) |
| Union Find | [union_find.md](./union_find.md) | [diff_toposort_quickunion.md](./diff_toposort_quickunion.md) |
| Recursion | [recursion.md](./recursion.md) | [recursion_to_dp.md](./recursion_to_dp.md) |
| Graph | [graph.md](./graph.md) | [topology_sorting.md](./topology_sorting.md), [Dijkstra.md](./Dijkstra.md), [graph_advanced.md](./graph_advanced.md) |
| Math | [math.md](./math.md) | [add_x_sum.md](./add_x_sum.md) |
| Greedy | [greedy.md](./greedy.md) | [intervals.md](./intervals.md), [scanning_line.md](./scanning_line.md) |
| Simulation | [advanced_simulation.md](./advanced_simulation.md) | [matrix.md](./matrix.md) |
| SQL | — | [`leetcode_SQL/`](https://github.com/yennanliu/CS_basics/tree/master/leetcode_SQL) |

Four of their categories have no counterpart here and are best read upstream: **Others**,
**LeetCode Cup**, **Templates** (their C++ snippets), and the per-problem solution folders
each entry links to.
