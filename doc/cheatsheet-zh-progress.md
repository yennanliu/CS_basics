# 繁體中文 Cheatsheets — Translation Progress

The cheatsheets under [`doc/cheatsheet/`](./cheatsheet/) are the only markdown
tree. A translation is a *sparse overlay* of translated sections in
`i18n/zh/<slug>.md`, and the site composes the two into a full Chinese document
at build time — see the *Traditional Chinese cheatsheets* section of
[CLAUDE.md](../CLAUDE.md).

**This file is generated. Do not edit it by hand:**

```bash
node script/zh.js status --write
```

## How a translation is stored

Roughly 70% of these sheets is fenced code, and that code must read identically
in both languages — so it is never stored twice:

```text
doc/cheatsheet/<slug>.md          the only markdown tree
   │  every fence lifts out to a one-line <!--CODE--> marker
   │  the prose is cut into sections at each heading
   ▼
i18n/zh/<slug>.md                 <!-- hash --> + the translated section
   │  compose — English structure, translated prose, original code
   ▼
_site/cheatsheets/<slug>.zh.html
```

Each section is keyed by a hash of **its English text**. Edit one section of an
English sheet and only that section's translation goes missing; the rest of the
sheet stays current. A section with no entry falls back to English, so a
half-translated sheet renders with English gaps rather than failing.

## Known limitations

- **The star legend and the priority tooltips** inside a sheet are still English;
  they come from `site/build-lib.js`, not from the markdown.
- **LC problem titles stay in English** — they are proper names, and keeping them
  is what makes a problem findable on LeetCode itself.


## Status — 5060 / 5090 sections (99%)

| Sheet | Sections | 繁體中文 |
|---|---:|:---:|
| [2_pointers](./cheatsheet/2_pointers.md) | 41 | [✅](../i18n/zh/2_pointers.md) |
| [2_pointers_examples](./cheatsheet/2_pointers_examples.md) | 82 | [✅](../i18n/zh/2_pointers_examples.md) |
| [2_pointers_linkedlist](./cheatsheet/2_pointers_linkedlist.md) | 16 | [✅](../i18n/zh/2_pointers_linkedlist.md) |
| [2_pointers_quickselect](./cheatsheet/2_pointers_quickselect.md) | 17 | [✅](../i18n/zh/2_pointers_quickselect.md) |
| [Bellman-Ford](./cheatsheet/Bellman-Ford.md) | 56 | [✅](../i18n/zh/Bellman-Ford.md) |
| [Collection](./cheatsheet/Collection.md) | 20 | [✅](../i18n/zh/Collection.md) |
| [Dijkstra](./cheatsheet/Dijkstra.md) | 60 | [✅](../i18n/zh/Dijkstra.md) |
| [Dijkstra_examples](./cheatsheet/Dijkstra_examples.md) | 33 | [✅](../i18n/zh/Dijkstra_examples.md) |
| [Floyd-Warshall](./cheatsheet/Floyd-Warshall.md) | 49 | [✅](../i18n/zh/Floyd-Warshall.md) |
| [add_x_sum](./cheatsheet/add_x_sum.md) | 17 | [✅](../i18n/zh/add_x_sum.md) |
| [advanced_divide_and_conquer](./cheatsheet/advanced_divide_and_conquer.md) | 45 | [✅](../i18n/zh/advanced_divide_and_conquer.md) |
| [advanced_simulation](./cheatsheet/advanced_simulation.md) | 43 | [✅](../i18n/zh/advanced_simulation.md) |
| [advanced_string_algorithms](./cheatsheet/advanced_string_algorithms.md) | 39 | [✅](../i18n/zh/advanced_string_algorithms.md) |
| [array](./cheatsheet/array.md) | 43 | [✅](../i18n/zh/array.md) |
| [array_examples](./cheatsheet/array_examples.md) | 21 | [✅](../i18n/zh/array_examples.md) |
| [array_overlap_explaination](./cheatsheet/array_overlap_explaination.md) | 24 | [✅](../i18n/zh/array_overlap_explaination.md) |
| [backtrack](./cheatsheet/backtrack.md) | 37 | [✅](../i18n/zh/backtrack.md) |
| [backtrack_advanced](./cheatsheet/backtrack_advanced.md) | 19 | [✅](../i18n/zh/backtrack_advanced.md) |
| [backtrack_examples](./cheatsheet/backtrack_examples.md) | 23 | [✅](../i18n/zh/backtrack_examples.md) |
| [bfs](./cheatsheet/bfs.md) | 51 | [✅](../i18n/zh/bfs.md) |
| [bfs_advanced](./cheatsheet/bfs_advanced.md) | 38 | [✅](../i18n/zh/bfs_advanced.md) |
| [bfs_examples](./cheatsheet/bfs_examples.md) | 31 | [✅](../i18n/zh/bfs_examples.md) |
| [binary_indexed_tree](./cheatsheet/binary_indexed_tree.md) | 32 | [✅](../i18n/zh/binary_indexed_tree.md) |
| [binary_search](./cheatsheet/binary_search.md) | 54 | [✅](../i18n/zh/binary_search.md) |
| [binary_search_examples](./cheatsheet/binary_search_examples.md) | 43 | [✅](../i18n/zh/binary_search_examples.md) |
| [binary_search_on_answer](./cheatsheet/binary_search_on_answer.md) | 42 | [✅](../i18n/zh/binary_search_on_answer.md) |
| [binary_tree](./cheatsheet/binary_tree.md) | 74 | [72/74](../i18n/zh/binary_tree.md) |
| [bit_manipulation](./cheatsheet/bit_manipulation.md) | 33 | [✅](../i18n/zh/bit_manipulation.md) |
| [bit_manipulation_examples](./cheatsheet/bit_manipulation_examples.md) | 22 | [✅](../i18n/zh/bit_manipulation_examples.md) |
| [bst](./cheatsheet/bst.md) | 61 | [✅](../i18n/zh/bst.md) |
| [bst_advanced](./cheatsheet/bst_advanced.md) | 51 | [✅](../i18n/zh/bst_advanced.md) |
| [bst_examples](./cheatsheet/bst_examples.md) | 41 | [✅](../i18n/zh/bst_examples.md) |
| [code_interview_general_cheatsheet](./cheatsheet/code_interview_general_cheatsheet.md) | 2 | [✅](../i18n/zh/code_interview_general_cheatsheet.md) |
| [combinatorics_math_patterns](./cheatsheet/combinatorics_math_patterns.md) | 39 | [✅](../i18n/zh/combinatorics_math_patterns.md) |
| [complexity_cheatsheet](./cheatsheet/complexity_cheatsheet.md) | 38 | [✅](../i18n/zh/complexity_cheatsheet.md) |
| [complexity_drills](./cheatsheet/complexity_drills.md) | 23 | [22/23](../i18n/zh/complexity_drills.md) |
| [concurrency_patterns](./cheatsheet/concurrency_patterns.md) | 11 | [✅](../i18n/zh/concurrency_patterns.md) |
| [design](./cheatsheet/design.md) | 43 | [✅](../i18n/zh/design.md) |
| [design_examples](./cheatsheet/design_examples.md) | 34 | [✅](../i18n/zh/design_examples.md) |
| [design_patterns](./cheatsheet/design_patterns.md) | 8 | [✅](../i18n/zh/design_patterns.md) |
| [dfs](./cheatsheet/dfs.md) | 45 | [✅](../i18n/zh/dfs.md) |
| [dfs_advanced](./cheatsheet/dfs_advanced.md) | 27 | [✅](../i18n/zh/dfs_advanced.md) |
| [dfs_examples](./cheatsheet/dfs_examples.md) | 70 | [✅](../i18n/zh/dfs_examples.md) |
| [diff_toposort_quickunion](./cheatsheet/diff_toposort_quickunion.md) | 18 | [✅](../i18n/zh/diff_toposort_quickunion.md) |
| [difference_array](./cheatsheet/difference_array.md) | 56 | [✅](../i18n/zh/difference_array.md) |
| [dp](./cheatsheet/dp.md) | 93 | [✅](../i18n/zh/dp.md) |
| [dp_advanced](./cheatsheet/dp_advanced.md) | 137 | [✅](../i18n/zh/dp_advanced.md) |
| [dp_bitmask](./cheatsheet/dp_bitmask.md) | 16 | [✅](../i18n/zh/dp_bitmask.md) |
| [dp_digit](./cheatsheet/dp_digit.md) | 18 | [✅](../i18n/zh/dp_digit.md) |
| [dp_examples](./cheatsheet/dp_examples.md) | 25 | [✅](../i18n/zh/dp_examples.md) |
| [dp_monotonic_stack](./cheatsheet/dp_monotonic_stack.md) | 31 | [✅](../i18n/zh/dp_monotonic_stack.md) |
| [dp_pattern](./cheatsheet/dp_pattern.md) | 74 | [✅](../i18n/zh/dp_pattern.md) |
| [dp_string](./cheatsheet/dp_string.md) | 33 | [✅](../i18n/zh/dp_string.md) |
| [graph](./cheatsheet/graph.md) | 49 | [✅](../i18n/zh/graph.md) |
| [graph_advanced](./cheatsheet/graph_advanced.md) | 37 | [✅](../i18n/zh/graph_advanced.md) |
| [graph_examples](./cheatsheet/graph_examples.md) | 18 | [✅](../i18n/zh/graph_examples.md) |
| [greedy](./cheatsheet/greedy.md) | 49 | [✅](../i18n/zh/greedy.md) |
| [greedy_examples](./cheatsheet/greedy_examples.md) | 28 | [✅](../i18n/zh/greedy_examples.md) |
| [hash_map](./cheatsheet/hash_map.md) | 43 | [✅](../i18n/zh/hash_map.md) |
| [hash_map_examples](./cheatsheet/hash_map_examples.md) | 93 | [✅](../i18n/zh/hash_map_examples.md) |
| [hashing](./cheatsheet/hashing.md) | 46 | [✅](../i18n/zh/hashing.md) |
| [heap](./cheatsheet/heap.md) | 54 | [✅](../i18n/zh/heap.md) |
| [heap_advanced](./cheatsheet/heap_advanced.md) | 24 | [✅](../i18n/zh/heap_advanced.md) |
| [heap_examples](./cheatsheet/heap_examples.md) | 29 | [✅](../i18n/zh/heap_examples.md) |
| [heap_language_apis](./cheatsheet/heap_language_apis.md) | 20 | [✅](../i18n/zh/heap_language_apis.md) |
| [intervals](./cheatsheet/intervals.md) | 70 | [68/70](../i18n/zh/intervals.md) |
| [iterator](./cheatsheet/iterator.md) | 14 | [✅](../i18n/zh/iterator.md) |
| [java_trick](./cheatsheet/java_trick.md) | 74 | [✅](../i18n/zh/java_trick.md) |
| [java_trick_collections](./cheatsheet/java_trick_collections.md) | 57 | [✅](../i18n/zh/java_trick_collections.md) |
| [java_trick_strings_sorting](./cheatsheet/java_trick_strings_sorting.md) | 42 | [✅](../i18n/zh/java_trick_strings_sorting.md) |
| [kadane_algorithm](./cheatsheet/kadane_algorithm.md) | 42 | [✅](../i18n/zh/kadane_algorithm.md) |
| [knapsack](./cheatsheet/knapsack.md) | 45 | [✅](../i18n/zh/knapsack.md) |
| [knapsack_01_zh](./cheatsheet/knapsack_01_zh.md) | 27 | [✅](../i18n/zh/knapsack_01_zh.md) |
| [lc_category](./cheatsheet/lc_category.md) | 3 | [✅](../i18n/zh/lc_category.md) |
| [lc_pattern](./cheatsheet/lc_pattern.md) | 72 | [✅](../i18n/zh/lc_pattern.md) |
| [linked_list](./cheatsheet/linked_list.md) | 28 | [✅](../i18n/zh/linked_list.md) |
| [linked_list_examples](./cheatsheet/linked_list_examples.md) | 34 | [✅](../i18n/zh/linked_list_examples.md) |
| [math](./cheatsheet/math.md) | 46 | [✅](../i18n/zh/math.md) |
| [matrix](./cheatsheet/matrix.md) | 67 | [✅](../i18n/zh/matrix.md) |
| [matrix_examples](./cheatsheet/matrix_examples.md) | 27 | [✅](../i18n/zh/matrix_examples.md) |
| [monotonic_queue](./cheatsheet/monotonic_queue.md) | 23 | [✅](../i18n/zh/monotonic_queue.md) |
| [monotonic_stack](./cheatsheet/monotonic_stack.md) | 75 | [✅](../i18n/zh/monotonic_stack.md) |
| [n_sum](./cheatsheet/n_sum.md) | 16 | [✅](../i18n/zh/n_sum.md) |
| [ood_design](./cheatsheet/ood_design.md) | 34 | [✅](../i18n/zh/ood_design.md) |
| [palindrome](./cheatsheet/palindrome.md) | 66 | [✅](../i18n/zh/palindrome.md) |
| [prefix_sum](./cheatsheet/prefix_sum.md) | 80 | [76/80](../i18n/zh/prefix_sum.md) |
| [prefix_sum_advanced](./cheatsheet/prefix_sum_advanced.md) | 23 | [10/23](../i18n/zh/prefix_sum_advanced.md) |
| [prefix_sum_examples](./cheatsheet/prefix_sum_examples.md) | 15 | [✅](../i18n/zh/prefix_sum_examples.md) |
| [priority_queue](./cheatsheet/priority_queue.md) | 3 | [✅](../i18n/zh/priority_queue.md) |
| [python_gotchas](./cheatsheet/python_gotchas.md) | 44 | [✅](../i18n/zh/python_gotchas.md) |
| [python_trick](./cheatsheet/python_trick.md) | 66 | [✅](../i18n/zh/python_trick.md) |
| [python_trick_indexing](./cheatsheet/python_trick_indexing.md) | 28 | [✅](../i18n/zh/python_trick_indexing.md) |
| [python_trick_stdlib](./cheatsheet/python_trick_stdlib.md) | 24 | [✅](../i18n/zh/python_trick_stdlib.md) |
| [queue](./cheatsheet/queue.md) | 58 | [✅](../i18n/zh/queue.md) |
| [recursion](./cheatsheet/recursion.md) | 30 | [✅](../i18n/zh/recursion.md) |
| [recursion_to_dp](./cheatsheet/recursion_to_dp.md) | 64 | [✅](../i18n/zh/recursion_to_dp.md) |
| [scanning_line](./cheatsheet/scanning_line.md) | 60 | [✅](../i18n/zh/scanning_line.md) |
| [scanning_line_examples](./cheatsheet/scanning_line_examples.md) | 25 | [✅](../i18n/zh/scanning_line_examples.md) |
| [segment_tree](./cheatsheet/segment_tree.md) | 52 | [✅](../i18n/zh/segment_tree.md) |
| [set](./cheatsheet/set.md) | 37 | [✅](../i18n/zh/set.md) |
| [set_examples](./cheatsheet/set_examples.md) | 32 | [✅](../i18n/zh/set_examples.md) |
| [shortest_path_comparison](./cheatsheet/shortest_path_comparison.md) | 24 | [✅](../i18n/zh/shortest_path_comparison.md) |
| [sliding_window](./cheatsheet/sliding_window.md) | 44 | [✅](../i18n/zh/sliding_window.md) |
| [sliding_window_advanced](./cheatsheet/sliding_window_advanced.md) | 76 | [✅](../i18n/zh/sliding_window_advanced.md) |
| [sliding_window_examples](./cheatsheet/sliding_window_examples.md) | 18 | [✅](../i18n/zh/sliding_window_examples.md) |
| [sort](./cheatsheet/sort.md) | 66 | [✅](../i18n/zh/sort.md) |
| [stack](./cheatsheet/stack.md) | 21 | [✅](../i18n/zh/stack.md) |
| [stack_examples](./cheatsheet/stack_examples.md) | 33 | [✅](../i18n/zh/stack_examples.md) |
| [stack_expression_parsing](./cheatsheet/stack_expression_parsing.md) | 13 | [✅](../i18n/zh/stack_expression_parsing.md) |
| [stock_trading](./cheatsheet/stock_trading.md) | 37 | [✅](../i18n/zh/stock_trading.md) |
| [streaming_algorithms](./cheatsheet/streaming_algorithms.md) | 46 | [✅](../i18n/zh/streaming_algorithms.md) |
| [string](./cheatsheet/string.md) | 34 | [✅](../i18n/zh/string.md) |
| [string_examples](./cheatsheet/string_examples.md) | 38 | [✅](../i18n/zh/string_examples.md) |
| [string_matching_kmp_rolling_hash](./cheatsheet/string_matching_kmp_rolling_hash.md) | 44 | [✅](../i18n/zh/string_matching_kmp_rolling_hash.md) |
| [string_operations](./cheatsheet/string_operations.md) | 11 | [✅](../i18n/zh/string_operations.md) |
| [time_space_complexity](./cheatsheet/time_space_complexity.md) | 39 | [✅](../i18n/zh/time_space_complexity.md) |
| [topology_sorting](./cheatsheet/topology_sorting.md) | 47 | [✅](../i18n/zh/topology_sorting.md) |
| [topology_sorting_examples](./cheatsheet/topology_sorting_examples.md) | 16 | [✅](../i18n/zh/topology_sorting_examples.md) |
| [tree](./cheatsheet/tree.md) | 55 | [52/55](../i18n/zh/tree.md) |
| [tree2](./cheatsheet/tree2.md) | 101 | [100/101](../i18n/zh/tree2.md) |
| [tree_backtrack](./cheatsheet/tree_backtrack.md) | 17 | [✅](../i18n/zh/tree_backtrack.md) |
| [tree_codec](./cheatsheet/tree_codec.md) | 26 | [✅](../i18n/zh/tree_codec.md) |
| [tree_construction](./cheatsheet/tree_construction.md) | 16 | [✅](../i18n/zh/tree_construction.md) |
| [tree_examples](./cheatsheet/tree_examples.md) | 49 | [45/49](../i18n/zh/tree_examples.md) |
| [tree_lca_distance](./cheatsheet/tree_lca_distance.md) | 37 | [✅](../i18n/zh/tree_lca_distance.md) |
| [trie](./cheatsheet/trie.md) | 26 | [✅](../i18n/zh/trie.md) |
| [trie_examples](./cheatsheet/trie_examples.md) | 11 | [✅](../i18n/zh/trie_examples.md) |
| [union_find](./cheatsheet/union_find.md) | 23 | [✅](../i18n/zh/union_find.md) |
| [union_find_examples](./cheatsheet/union_find_examples.md) | 30 | [✅](../i18n/zh/union_find_examples.md) |
