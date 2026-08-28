# 繁體中文 Cheatsheets — Translation Progress

Every sheet in [`doc/cheatsheet/`](./cheatsheet/) has a Traditional Chinese
counterpart under [`doc/cheatsheet/zh/`](./cheatsheet/zh/) with the same filename.
The site builds both and the navbar carries a **中文 / EN** button that swaps
between the two — see the *Traditional Chinese cheatsheets* section of
[CLAUDE.md](../CLAUDE.md) for the full workflow.

**This file is generated. Do not edit it by hand:**

```bash
python3 script/zh_cheatsheet.py status --write
```

## How a translation is produced

Roughly 70% of these sheets is fenced code, and that code must survive
byte-for-byte. So the translator never sees it:

```text
doc/cheatsheet/<slug>.md
   │  extract  — every fence collapses to a one-line <!--CODE:n--> marker
   ▼
.zh-work/<slug>.md            (prose only, gitignored)
   │  translate — markers must survive, exactly once, in order
   ▼
.zh-work/<slug>.zh.md
   │  merge    — the original code blocks are spliced back in
   ▼
doc/cheatsheet/zh/<slug>.md
```

`python3 script/zh_cheatsheet.py verify` re-extracts both sides and fails if a
translated sheet's code blocks differ from the English original in any way.

## Known limitations

- **Anchor links across sheets** (`./heap.md#overview`) keep the English
  fragment, which does not exist on the translated page — the link lands at the
  top of the right sheet rather than at the right section.
- **The star legend and the priority tooltips** inside a sheet are still English;
  they come from `site/build-lib.js`, not from the markdown.
- A sheet marked **⚠️ stale** below has been edited in English since it was
  translated. Re-run extract → translate → merge for it.


## Status — 1 / 129 sheets (1%)

| Sheet | Lines | 繁體中文 |
|---|---:|:---:|
| [2_pointers](./cheatsheet/2_pointers.md) | 1091 | — |
| [2_pointers_examples](./cheatsheet/2_pointers_examples.md) | 3008 | — |
| [2_pointers_linkedlist](./cheatsheet/2_pointers_linkedlist.md) | 479 | — |
| [2_pointers_quickselect](./cheatsheet/2_pointers_quickselect.md) | 637 | — |
| [Bellman-Ford](./cheatsheet/Bellman-Ford.md) | 1119 | — |
| [Collection](./cheatsheet/Collection.md) | 472 | — |
| [Dijkstra](./cheatsheet/Dijkstra.md) | 898 | — |
| [Dijkstra_examples](./cheatsheet/Dijkstra_examples.md) | 1528 | — |
| [Floyd-Warshall](./cheatsheet/Floyd-Warshall.md) | 864 | — |
| [add_x_sum](./cheatsheet/add_x_sum.md) | 601 | — |
| [advanced_divide_and_conquer](./cheatsheet/advanced_divide_and_conquer.md) | 1139 | — |
| [advanced_simulation](./cheatsheet/advanced_simulation.md) | 1152 | — |
| [advanced_string_algorithms](./cheatsheet/advanced_string_algorithms.md) | 1175 | — |
| [array](./cheatsheet/array.md) | 1584 | — |
| [array_examples](./cheatsheet/array_examples.md) | 773 | — |
| [array_overlap_explaination](./cheatsheet/array_overlap_explaination.md) | 374 | — |
| [backtrack](./cheatsheet/backtrack.md) | 1223 | — |
| [backtrack_advanced](./cheatsheet/backtrack_advanced.md) | 653 | — |
| [backtrack_examples](./cheatsheet/backtrack_examples.md) | 1382 | — |
| [bfs](./cheatsheet/bfs.md) | 1020 | — |
| [bfs_advanced](./cheatsheet/bfs_advanced.md) | 1841 | — |
| [bfs_examples](./cheatsheet/bfs_examples.md) | 1213 | — |
| [binary_indexed_tree](./cheatsheet/binary_indexed_tree.md) | 814 | — |
| [binary_search](./cheatsheet/binary_search.md) | 1013 | — |
| [binary_search_examples](./cheatsheet/binary_search_examples.md) | 1142 | — |
| [binary_search_on_answer](./cheatsheet/binary_search_on_answer.md) | 1451 | — |
| [binary_tree](./cheatsheet/binary_tree.md) | 1672 | — |
| [bit_manipulation](./cheatsheet/bit_manipulation.md) | 604 | — |
| [bit_manipulation_examples](./cheatsheet/bit_manipulation_examples.md) | 741 | — |
| [bst](./cheatsheet/bst.md) | 1002 | — |
| [bst_advanced](./cheatsheet/bst_advanced.md) | 1553 | — |
| [bst_examples](./cheatsheet/bst_examples.md) | 988 | — |
| [code_interview_general_cheatsheet](./cheatsheet/code_interview_general_cheatsheet.md) | 11 | — |
| [combinatorics_math_patterns](./cheatsheet/combinatorics_math_patterns.md) | 784 | — |
| [complexity_cheatsheet](./cheatsheet/complexity_cheatsheet.md) | 696 | — |
| [complexity_drills](./cheatsheet/complexity_drills.md) | 527 | — |
| [concurrency_patterns](./cheatsheet/concurrency_patterns.md) | 160 | — |
| [design](./cheatsheet/design.md) | 266 | — |
| [design_examples](./cheatsheet/design_examples.md) | 1362 | — |
| [design_patterns](./cheatsheet/design_patterns.md) | 1151 | — |
| [dfs](./cheatsheet/dfs.md) | 1300 | — |
| [dfs_advanced](./cheatsheet/dfs_advanced.md) | 1713 | — |
| [dfs_examples](./cheatsheet/dfs_examples.md) | 2403 | — |
| [diff_toposort_quickunion](./cheatsheet/diff_toposort_quickunion.md) | 436 | — |
| [difference_array](./cheatsheet/difference_array.md) | 1040 | — |
| [dp](./cheatsheet/dp.md) | 1492 | — |
| [dp_advanced](./cheatsheet/dp_advanced.md) | 2885 | — |
| [dp_bitmask](./cheatsheet/dp_bitmask.md) | 443 | — |
| [dp_digit](./cheatsheet/dp_digit.md) | 674 | — |
| [dp_examples](./cheatsheet/dp_examples.md) | 857 | — |
| [dp_monotonic_stack](./cheatsheet/dp_monotonic_stack.md) | 492 | — |
| [dp_pattern](./cheatsheet/dp_pattern.md) | 2021 | — |
| [dp_string](./cheatsheet/dp_string.md) | 772 | — |
| [graph](./cheatsheet/graph.md) | 734 | — |
| [graph_advanced](./cheatsheet/graph_advanced.md) | 1047 | — |
| [graph_examples](./cheatsheet/graph_examples.md) | 852 | — |
| [greedy](./cheatsheet/greedy.md) | 580 | — |
| [greedy_examples](./cheatsheet/greedy_examples.md) | 1137 | — |
| [hash_map](./cheatsheet/hash_map.md) | 1074 | — |
| [hash_map_examples](./cheatsheet/hash_map_examples.md) | 2693 | — |
| [hashing](./cheatsheet/hashing.md) | 1078 | — |
| [heap](./cheatsheet/heap.md) | 1176 | — |
| [heap_advanced](./cheatsheet/heap_advanced.md) | 1092 | — |
| [heap_examples](./cheatsheet/heap_examples.md) | 1949 | — |
| [heap_language_apis](./cheatsheet/heap_language_apis.md) | 522 | — |
| [intervals](./cheatsheet/intervals.md) | 1023 | — |
| [iterator](./cheatsheet/iterator.md) | 436 | — |
| [java_trick](./cheatsheet/java_trick.md) | 1502 | — |
| [java_trick_collections](./cheatsheet/java_trick_collections.md) | 1161 | — |
| [java_trick_strings_sorting](./cheatsheet/java_trick_strings_sorting.md) | 844 | — |
| [kadane_algorithm](./cheatsheet/kadane_algorithm.md) | 1059 | [✅](./cheatsheet/zh/kadane_algorithm.md) |
| [knapsack](./cheatsheet/knapsack.md) | 1024 | — |
| [knapsack_01_zh](./cheatsheet/knapsack_01_zh.md) | 719 | — |
| [lc_category](./cheatsheet/lc_category.md) | 50 | — |
| [lc_pattern](./cheatsheet/lc_pattern.md) | 631 | — |
| [linked_list](./cheatsheet/linked_list.md) | 1383 | — |
| [linked_list_examples](./cheatsheet/linked_list_examples.md) | 1362 | — |
| [math](./cheatsheet/math.md) | 1804 | — |
| [matrix](./cheatsheet/matrix.md) | 958 | — |
| [matrix_examples](./cheatsheet/matrix_examples.md) | 1075 | — |
| [monotonic_queue](./cheatsheet/monotonic_queue.md) | 451 | — |
| [monotonic_stack](./cheatsheet/monotonic_stack.md) | 1501 | — |
| [n_sum](./cheatsheet/n_sum.md) | 675 | — |
| [ood_design](./cheatsheet/ood_design.md) | 961 | — |
| [palindrome](./cheatsheet/palindrome.md) | 1352 | — |
| [prefix_sum](./cheatsheet/prefix_sum.md) | 1124 | — |
| [prefix_sum_advanced](./cheatsheet/prefix_sum_advanced.md) | 514 | — |
| [prefix_sum_examples](./cheatsheet/prefix_sum_examples.md) | 469 | — |
| [priority_queue](./cheatsheet/priority_queue.md) | 29 | — |
| [python_gotchas](./cheatsheet/python_gotchas.md) | 852 | — |
| [python_trick](./cheatsheet/python_trick.md) | 2086 | — |
| [python_trick_indexing](./cheatsheet/python_trick_indexing.md) | 812 | — |
| [python_trick_stdlib](./cheatsheet/python_trick_stdlib.md) | 1145 | — |
| [queue](./cheatsheet/queue.md) | 1248 | — |
| [recursion](./cheatsheet/recursion.md) | 1408 | — |
| [recursion_to_dp](./cheatsheet/recursion_to_dp.md) | 1471 | — |
| [scanning_line](./cheatsheet/scanning_line.md) | 1116 | — |
| [scanning_line_examples](./cheatsheet/scanning_line_examples.md) | 688 | — |
| [segment_tree](./cheatsheet/segment_tree.md) | 1317 | — |
| [set](./cheatsheet/set.md) | 488 | — |
| [set_examples](./cheatsheet/set_examples.md) | 954 | — |
| [shortest_path_comparison](./cheatsheet/shortest_path_comparison.md) | 405 | — |
| [sliding_window](./cheatsheet/sliding_window.md) | 777 | — |
| [sliding_window_advanced](./cheatsheet/sliding_window_advanced.md) | 1528 | — |
| [sliding_window_examples](./cheatsheet/sliding_window_examples.md) | 472 | — |
| [sort](./cheatsheet/sort.md) | 1781 | — |
| [stack](./cheatsheet/stack.md) | 719 | — |
| [stack_examples](./cheatsheet/stack_examples.md) | 1836 | — |
| [stack_expression_parsing](./cheatsheet/stack_expression_parsing.md) | 629 | — |
| [stock_trading](./cheatsheet/stock_trading.md) | 664 | — |
| [streaming_algorithms](./cheatsheet/streaming_algorithms.md) | 1624 | — |
| [string](./cheatsheet/string.md) | 899 | — |
| [string_examples](./cheatsheet/string_examples.md) | 1351 | — |
| [string_matching_kmp_rolling_hash](./cheatsheet/string_matching_kmp_rolling_hash.md) | 1093 | — |
| [string_operations](./cheatsheet/string_operations.md) | 237 | — |
| [time_space_complexity](./cheatsheet/time_space_complexity.md) | 632 | — |
| [topology_sorting](./cheatsheet/topology_sorting.md) | 1185 | — |
| [topology_sorting_examples](./cheatsheet/topology_sorting_examples.md) | 1302 | — |
| [tree](./cheatsheet/tree.md) | 1000 | — |
| [tree2](./cheatsheet/tree2.md) | 1951 | — |
| [tree_backtrack](./cheatsheet/tree_backtrack.md) | 652 | — |
| [tree_codec](./cheatsheet/tree_codec.md) | 1112 | — |
| [tree_construction](./cheatsheet/tree_construction.md) | 439 | — |
| [tree_examples](./cheatsheet/tree_examples.md) | 1813 | — |
| [tree_lca_distance](./cheatsheet/tree_lca_distance.md) | 1376 | — |
| [trie](./cheatsheet/trie.md) | 1180 | — |
| [trie_examples](./cheatsheet/trie_examples.md) | 775 | — |
| [union_find](./cheatsheet/union_find.md) | 570 | — |
| [union_find_examples](./cheatsheet/union_find_examples.md) | 1587 | — |
