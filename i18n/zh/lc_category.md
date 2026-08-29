<!-- 6df330f5dcdd -->
# LC 題型分類索引（wisdompeak）

> **範圍** — 指向 wisdompeak 的題目分類表，並把它的 29 個分類對應到本 repo 中負責教它的那份 cheatsheet。分類表本身不在這裡複製一份。
> **另見**：[lc_pattern.md](./lc_pattern.md) — 用來實際照著念的「模式 → 題目」對照表；[00_template.md](./00_template.md) — 下面每份 cheatsheet 共用的結構。

<!-- 60d1c49d81a4 -->
## 來源

分類表由上游維護，也只在上游更新：

- **[wisdompeak/LeetCode — Readme.md](https://github.com/wisdompeak/LeetCode/blob/master/Readme.md)** — 29 個分類、約 1,300 題，每題都連到一份寫好的 C++ 解法。

這個檔案以前是那份 README 的逐字複製。別人的索引鏡像過來根本跟不上更新，這裡也從來沒有編輯過，而且它教的東西，各主題的 cheatsheet 都教得更好 — 所以現在只留一條連結。下面才是真正屬於本 repo 的東西：他們的每個分類，該看我們的哪一份。

<!-- 8aec2049e9d5 -->
## 他們的分類 → 我們的 cheatsheet

| wisdompeak 分類 | 從這裡開始 | 接著看 |
|---|---|---|
| 雙指標 | [2_pointers.md](./2_pointers.md) | [sliding_window.md](./sliding_window.md), [n_sum.md](./n_sum.md) |
| 二分搜尋 | [binary_search.md](./binary_search.md) | [sort.md](./sort.md) |
| 雜湊表 | [hash_map.md](./hash_map.md) | [hashing.md](./hashing.md), [set.md](./set.md) |
| 堆積／優先佇列 | [heap.md](./heap.md) | [heap_advanced.md](./heap_advanced.md) |
| 樹 | [tree.md](./tree.md) | [binary_tree.md](./binary_tree.md), [bst.md](./bst.md), [tree2.md](./tree2.md) |
| 線段樹 | [segment_tree.md](./segment_tree.md) | [binary_indexed_tree.md](./binary_indexed_tree.md) |
| 樹狀陣列 | [binary_indexed_tree.md](./binary_indexed_tree.md) | [prefix_sum.md](./prefix_sum.md) |
| 設計題 | [design.md](./design.md) | [design_examples.md](./design_examples.md), [ood_design.md](./ood_design.md) |
| 堆疊 | [stack.md](./stack.md) | [monotonic_stack.md](./monotonic_stack.md), [stack_expression_parsing.md](./stack_expression_parsing.md) |
| 雙端佇列 | [monotonic_queue.md](./monotonic_queue.md) | [queue.md](./queue.md) |
| DFS | [dfs.md](./dfs.md) | [dfs_advanced.md](./dfs_advanced.md), [backtrack.md](./backtrack.md) |
| BFS | [bfs.md](./bfs.md) | [bfs_advanced.md](./bfs_advanced.md) |
| 字典樹（Trie） | [trie.md](./trie.md) | [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md) |
| 鏈結串列 | [linked_list.md](./linked_list.md) | [2_pointers_linkedlist.md](./2_pointers_linkedlist.md) |
| 動態規劃 | [dp.md](./dp.md) | [dp_pattern.md](./dp_pattern.md), [knapsack.md](./knapsack.md), [kadane_algorithm.md](./kadane_algorithm.md) |
| 位元運算 | [bit_manipulation.md](./bit_manipulation.md) | [dp_bitmask.md](./dp_bitmask.md) |
| 分治法 | [advanced_divide_and_conquer.md](./advanced_divide_and_conquer.md) | [sort.md](./sort.md) |
| 字串 | [string.md](./string.md) | [palindrome.md](./palindrome.md), [dp_string.md](./dp_string.md) |
| 併查集 | [union_find.md](./union_find.md) | [diff_toposort_quickunion.md](./diff_toposort_quickunion.md) |
| 遞迴 | [recursion.md](./recursion.md) | [recursion_to_dp.md](./recursion_to_dp.md) |
| 圖 | [graph.md](./graph.md) | [topology_sorting.md](./topology_sorting.md), [Dijkstra.md](./Dijkstra.md), [graph_advanced.md](./graph_advanced.md) |
| 數學 | [math.md](./math.md) | [add_x_sum.md](./add_x_sum.md) |
| 貪婪 | [greedy.md](./greedy.md) | [intervals.md](./intervals.md), [scanning_line.md](./scanning_line.md) |
| 模擬 | [advanced_simulation.md](./advanced_simulation.md) | [matrix.md](./matrix.md) |
| SQL | — | [`leetcode_SQL/`](https://github.com/yennanliu/CS_basics/tree/master/leetcode_SQL) |

他們還有四個分類在這裡沒有對應，直接看上游即可：**Others**、**LeetCode Cup**、**Templates**（他們的 C++ 片段），以及每一筆連過去的單題解法資料夾。
