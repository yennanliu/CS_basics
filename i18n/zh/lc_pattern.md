<!-- daccc797b008 -->
# LeetCode 模式指南

> **範圍** — 入口：從題目敘述走到解它的那張 card — 敘述裡的訊號、限制條件的提示、模式、指向那張 card 的連結，以及長得像、其實不是的那一題。這裡只做路由：每個模板都在表格連到的 card 裡。
> **另見**：[complexity_cheatsheet.md § 6](./complexity_cheatsheet.md#6-interview-decision-guide) — 「限制提示」一欄讀的約束量尺；[lc_category.md](./lc_category.md) — wisdompeak 的分類，以及每一類由哪份 cheatsheet 負責。

---

<!-- 69bf940963a6 -->
## 怎麼用這一頁

1. **先讀 `n`。** 限制條件通常比敘述更早說出模式：`n ≤ 20` 是在邀請 O(2ⁿ)，`n ≤ 10⁵` 是 O(n log n) 或 O(n)，數值範圍到 10⁹ 則是在邀請對答案做二分搜尋。完整的量尺在 [complexity_cheatsheet.md § 6](./complexity_cheatsheet.md#6-interview-decision-guide)。
2. **在下面的家族表裡找訊號吻合的那一列。** 訊號是敘述裡的那句話，不是主題名稱。
3. **打開 card，寫程式前先讀最後一欄。** 「長得像，其實不是」就是這一列要擋下的錯：訊號吻合、卻需要另一張 card 的那種題目。

---

<!-- e684bdc1debf -->
## 辨識 — 訊號 → 模式 → card ⭐⭐⭐⭐⭐

一個模式一列。**Card** 欄連到解錨定題的那個模板；題目本身放在該 cheatsheet 的 examples 裡。

<!-- 0891bbc6b68e -->
### 陣列與字串

| 敘述裡的訊號 | 限制提示 | 模式 | Card | 長得像，其實不是 |
|---|---|---|---|---|
| 「最長／最短的連續子陣列或子字串」、「最多 k 種相異元素」 | n ≤ 10⁵ | 可變長度滑動視窗 | [sliding_window § Template 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-)（最長）、[Template 4](./sliding_window.md#template-4-shortest-window-satisfying-p--lc-209-)（最短） | 要剛好的總和 `sum == k`、而且值可能是負的 → 前綴和 + 雜湊表（LC 560） |
| 「包含 t 所有字元的最短子字串」、在 s 裡找 t 的 anagram／排列 | n ≤ 10⁵，字母表 26–128 | 字元計數視窗 | [sliding_window § Template 5](./sliding_window.md#template-5-char-count-window-with-the-haveneed-counter--lc-76-) | 要的是子序列、不是子字串 → 雙指標（LC 392） |
| 「剛好 k 個 … 的子陣列有幾個」 | n ≤ 10⁵ | 剛好 k = 最多 k − 最多 (k−1) | [sliding_window § Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) | 值可能是負的 → 前綴和 + 雜湊表 |
| 「總和為 k／可被 k 整除的子陣列有幾個」 | n ≤ 10⁵，允許負數 | 前綴和 + 計數 map | [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | 值全部是正的、要最長／最短 → 滑動視窗 |
| 「大量的區間和查詢」 | q ≤ 10⁵ | 前綴和陣列 | [prefix_sum § Template 1](./prefix_sum.md#template-1-basic-prefix-sum-range-queries--lc-303) | 查詢之間陣列會變 → Fenwick／線段樹（LC 307） |
| 「把 [l, r] 每個元素加上 v」，做很多次 | n、更新次數 ≤ 10⁵ | 差分陣列 | [difference_array § Template 1](./difference_array.md#template-1-basic-difference-array) | 每次更新後都要讀最大值 → 帶 lazy propagation 的線段樹 |
| **已排序**陣列上「和為 target 的一對／三元組」、「盛水容器」、「回文檢查」 | n ≤ 10⁵ | 相向雙指標 | [2_pointers § Template 2](./2_pointers.md#template-2-converging-pointers-from-both-ends--lc-11-) | 沒排序、而且要回傳索引 → 雜湊表（LC 1） |
| 「原地移除／去重」、「把 0 移到後面」 | O(1) 額外空間 | 讀寫雙指標 | [2_pointers § Template 1](./2_pointers.md#template-1-fastslow-read-write-compaction--lc-26-) | 順序不重要 → 和最後一個元素交換 |
| 「一趟把 0、1、2 排好」 | O(1) 空間 | 三路切分 | [2_pointers § Template 6](./2_pointers.md#template-6-three-way-partition-dutch-national-flag--lc-75-) | 超過三種 key → 計數排序 |
| 「最長回文子字串」、「回文子字串有幾個」 | n ≤ 1,000 | 從中心擴張 | [2_pointers § Template 3](./2_pointers.md#template-3-expand-from-centre--lc-5-) | 回文**子序列** → 區間 DP（LC 516） |
| 未排序陣列上的「two sum」、「之前看過嗎？」 | n ≤ 10⁵ | 查補數 | [hash_map § Template 1](./hash_map.md#template-1-complement-lookup--lc-1) | 輸入已排序、要求 O(1) 空間 → 雙指標（LC 167） |
| 「anagram 分組」、「數頻率」、「第一個不重複的」 | n ≤ 10⁴ | 依計算出的 key 分組／計數 | [hash_map § Template 3](./hash_map.md#template-3-grouping-by-a-computed-key--lc-49)、[Template 2](./hash_map.md#template-2-frequency-count--lc-242) | 依頻率取前 k → 堆積或桶排序（LC 347） |
| 「子陣列的最大和／最大乘積」 | n ≤ 10⁵ | Kadane：以這裡結尾的最佳一段 | [kadane_algorithm § 0-2](./kadane_algorithm.md#0-2-algorithm-pattern--template) | 長度 ≥ k 的子陣列，或總和 = k → 前綴和 |
| 「合併／插入區間」、「最少要幾間會議室」、「最少移除幾個才不重疊」 | n ≤ 10⁴ | 依起點排序（合併）或依終點排序（排程） | [intervals § Pattern 1](./intervals.md#pattern-1-interval-merging--lc-56-)、[Pattern 2](./intervals.md#pattern-2-interval-scheduling-greedy--lc-435-) | 同一時間有很多事件重疊 → 掃描線（LC 253、[scanning_line](./scanning_line.md#template-1-basic-interval-overlap--lc-253)） |
| 「第 k 大／第 k 小」、「最近的 k 個」 | n ≤ 10⁵，只問一次 | quickselect | [quickselect § Template 1](./quickselect.md#template-1-three-way-partition-quickselect--lc-215-) | 資料是串流，或要依序拿到全部 k 個 → 大小為 k 的堆積 |

<!-- d4ea311cbb43 -->
### 二分搜尋

| 敘述裡的訊號 | 限制提示 | 模式 | Card | 長得像，其實不是 |
|---|---|---|---|---|
| 「已排序陣列」、「找 target／第一個位置／插入位置」 | n ≤ 10⁵，要求 O(log n) | 經典二分搜尋 | [binary_search § 2.1](./binary_search.md#template-1-exact-match-on-a-closed-interval--lc-704-) | 沒排序、但要的是計數 → 先排序，或用雜湊表 |
| 「能在 k 份內裝下的最小容量／速度／最大和」、「最大化最小距離」 | 數值到 10⁹ | 對答案做二分搜尋 | [binary_search_on_answer § The Unified Template](./binary_search_on_answer.md#template-1-smallest-feasible-value--lc-875-) | 可行性檢查不單調（答案變大反而可能不行） → DP 或貪婪 |
| 「旋轉過的已排序陣列」 | n ≤ 5,000，要求 O(log n) | 二分搜尋，永遠有一半是排好的 | [binary_search](./binary_search.md) | 允許重複 → 最差變成 O(n)（LC 81） |

<!-- f2b8230df542 -->
### 鏈結串列

| 敘述裡的訊號 | 限制提示 | 模式 | Card | 長得像，其實不是 |
|---|---|---|---|---|
| 「反轉整條／一段／每 k 個一組」、「重新排列」 | O(1) 空間 | 指標反轉、dummy head | [linked_list § Template 1](./linked_list.md#template-1-iterative-reversal--lc-206-) | 可以用 O(n) 空間 → 先放進 list 再重建 |
| 「環」、「環的起點」、「中間節點」、「倒數第 k 個」 | O(1) 空間 | 快慢指標 | [2_pointers § Template 7](./2_pointers.md#template-7-fastslow-cycle-detection--lc-141-) | 1..n 陣列裡找重複 → 把同一招用在索引上（LC 287） |
| 「合併 k 條已排序串列」 | k ≤ 10⁴ | 用堆積做 k 路合併 | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | 只有兩條 → 用 dummy head 直接合併（LC 21） |

<!-- 27aa78ff63c6 -->
### 樹

| 敘述裡的訊號 | 限制提示 | 模式 | Card | 長得像，其實不是 |
|---|---|---|---|---|
| 「一層一層」、「右側視角」、「之字形」、「最小深度」 | 節點 ≤ 10⁴ | 帶層迴圈的 BFS | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | 由葉子往上彙總的值 → 後序 DFS |
| 「直徑」、「平衡嗎？」、「最大路徑和」，任何節點要從子節點算出來的東西 | 遞迴深度 = 樹高 | 回傳值的後序 DFS | [dfs § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) | 答案取決於從根下來的路徑 → 當參數往下傳（LC 112） |
| 「從根到葉、總和為 x 的路徑」、「所有路徑」 | 節點 ≤ 5,000 | 帶著路徑的前序 DFS | [tree_lca_distance § 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) | 任何往下的路徑、不只從根出發 → 路徑上的前綴和（LC 437） |
| 「最低共同祖先」 | 節點 ≤ 10⁵ | 用回傳值表示「找到了沒」的 LCA | [tree_lca_distance § 1](./tree_lca_distance.md#template-1-lca-by-returned-presence--lc-236) | 是 **BST** → 依值往下走，O(h)（LC 235） |
| 「合法的 BST」、「BST 第 k 小」、「BST iterator」 | 節點 ≤ 10⁴ | 中序是排好的 | [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98)、[Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) | 一般二元樹 → 沒有順序可以依靠 |
| 「由前序與中序建樹」 | 節點 ≤ 3,000 | 分治 + 索引 map | [binary_tree § Template 3](./binary_tree.md#template-3-build-from-preorder--inorder--lc-105) | 序列化／反序列化 → 帶 null 標記的前序（[tree_codec](./tree_codec.md)） |
| 「前綴搜尋」、「支援 `.` 的單字字典」、「網格裡找很多個字」 | 單字 ≤ 3·10⁴ | trie | [trie](./trie.md#templates--algorithms) | 一次只拿一個字比對一個集合 → hash set 就夠 |

<!-- 7204a7418ffd -->
### 圖

| 敘述裡的訊號 | 限制提示 | 模式 | Card | 長得像，其實不是 |
|---|---|---|---|---|
| 「島嶼數量」、「連通區域」、「flood fill」 | 網格 ≤ 300 × 300 | 網格 DFS／BFS，標記已走過 | [dfs § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | 邊一條一條進來、要問連通性 → 併查集 |
| 「最短路徑／最少步數」，每一步成本都是 1 | V + E ≤ 10⁵ | BFS（入隊時就標記） | [bfs § Template 2](./bfs.md#template-2-graph-bfs-mark-on-enqueue--lc-1971-) | 每一步成本不同 → Dijkstra；成本只有 0 或 1 → [0-1 BFS](./shortest_path_comparison.md#variant-0-1-bfs) |
| 「橘子腐爛」、「到最近的 0／門的距離」 | 網格 ≤ 10⁵ 格 | 多源 BFS | [bfs § Template 4](./bfs.md#template-4-multi-source-bfs--lc-994-) | 只要從一個源點出發的距離 → 一般 BFS |
| 「帶權最短路徑」、「網路延遲」、「最小體力路徑」 | E ≤ 10⁵，權重 ≥ 0 | Dijkstra | [Dijkstra § Template 1](./Dijkstra.md#template-1-basic-dijkstra) | 有負權重或「最多 k 站」 → [Bellman-Ford](./Bellman-Ford.md)（LC 787）；全點對且 n ≤ 400 → [Floyd-Warshall](./Floyd-Warshall.md) |
| 「課程表」、「有先修條件的任務順序」、「外星字典」 | V + E ≤ 10⁵ | 拓撲排序（Kahn 的 BFS） | [topology_sorting § Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) | 無向圖 → 根本沒有順序；用併查集檢查環 |
| 「多餘的連線」、「省份數量」、「合併帳號」 | n ≤ 10⁵ | 併查集 | [union_find § 0-3](./union_find.md#0-3-algorithm-pattern--template-) | 還需要路徑長度 → BFS／DFS |
| 「能不能分成兩組」、「是不是二分圖」 | n ≤ 100 | BFS 二著色 | [graph § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) | 三組以上 → 用回溯著色 |
| 「複製這張圖」、「除法求值」 | V ≤ 100 | 帶節點 map／帶權邊的 DFS | [dfs § Template 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399) | 固定的圖上有很多查詢 → 帶權併查集 |

<!-- e4056a8d26a6 -->
### 動態規劃

| 敘述裡的訊號 | 限制提示 | 模式 | Card | 長得像，其實不是 |
|---|---|---|---|---|
| 「爬樓梯／解碼有幾種方法」、「不能偷相鄰的房子」 | n ≤ 10⁵ | 一維 DP、滾動變數 | [dp § Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables--lc-198-) | 選擇形成一棵沒有重疊的樹 → 單純遞迴或回溯 |
| 「字串能不能切成字典裡的單字」 | n ≤ 300 | 前綴切分 DP | [dp § Template 1b](./dp.md#template-1b-prefix-partition-dp--lc-139-) | 要列出每一種切法 → 帶 memo 的回溯（LC 140） |
| 網格裡的「不同路徑數」、「最小路徑和」 | m、n ≤ 200 | 二維網格 DP | [dp § Template 2](./dp.md#template-2-2-d-grid-dp--lc-64-) | 路徑可以往任何方向走 → 最短路徑（Dijkstra／BFS） |
| 「和為 target 的子集」、「切成兩個和相等的部分」、「目標和」 | n ≤ 200，總和 ≤ 10⁴ | 0/1 背包 | [dp § Template 4](./dp.md#template-4-01-knapsack--lc-416-) | 物品可以重複用（找零，LC 322／518） → 完全背包，迴圈順序要反過來（[dp_loop_order](./dp_loop_order.md)） |
| 「最長遞增子序列」、「俄羅斯套娃」 | n ≤ 10⁵ | LIS，用 tails 做到 O(n log n) | [dp § Template 7](./dp.md#template-7-longest-increasing-subsequence--lc-300-) | 要連續的 → 滑動視窗或 Kadane |
| 「最長公共子序列」、「編輯距離」、「交錯字串」 | m、n ≤ 1,000 | 以前綴為狀態的二維字串 DP | [dp_string § LCS deep dive](./dp_string.md#deep-dive-the-prefix-based-indexing-pattern-lcs--variants--) | 公共**子字串** → 不相等時歸零 |
| 「帶冷卻期／手續費／最多 k 次交易的股票買賣」 | n ≤ 10⁵ | 狀態機 DP | [dp § Template 5](./dp.md#template-5-state-machine-dp--lc-309) | 只交易一次 → 追蹤目前最低價（LC 121） |
| 「戳氣球」、「合併石頭的最小成本」 | n ≤ 300 | 區間 DP | [dp § Template 3](./dp.md#template-3-interval-dp--lc-312) | 操作的順序無關緊要 → 貪婪 |
| 「走訪所有節點」、「把 n ≤ 16 個東西分配出去」 | n ≤ 16–20 | 位元遮罩 DP | [bit_manipulation § 2-3](./bit_manipulation.md#2-3-fill-buckets-one-at-a-time-bitmask-dp--lc-698-) | n 有好幾千 → 不是位元遮罩；找貪婪或以數值為狀態的 DP |

<!-- a024fc1f2258 -->
### 回溯

| 敘述裡的訊號 | 限制提示 | 模式 | Card | 長得像，其實不是 |
|---|---|---|---|---|
| 「所有子集」、「所有 k 個的組合」 | n ≤ 10–20 | 帶 `start_idx` 的回溯 | [backtrack § Template 3](./backtrack.md#template-3-subsets--lc-78-)、[Template 6](./backtrack.md#template-6-combinations--lc-77) | 只問**數量** → DP |
| 「所有排列」 | n ≤ 8 | 帶 `used[]` 的回溯 | [backtrack § Template 5](./backtrack.md#template-5-permutations--lc-46-) | 只要下一個排列 → 兩趟掃描（LC 31） |
| 「和為 target 的組合」，可否重複使用 | 候選 ≤ 30 | 傳 `i` 或 `i + 1` | [backtrack § Template 7](./backtrack.md#template-7-combination-sum--lc-39--lc-40-) | 順序有差、而且只要計數 → DP（LC 377） |
| 「網格裡找單字」、「N 皇后」、「數獨」 | 棋盤 ≤ 9 × 9 或 m·n ≤ 36 | 棋盤上的回溯 | [backtrack § Template 9](./backtrack.md#template-9-grid--word-search--lc-79-)、[Template 10](./backtrack.md#template-10-n-queens--lc-51) | 同一個網格裡找很多個字 → trie + 回溯（LC 212） |
| 「把字串切成回文」 | n ≤ 16 | 在切點上回溯 | [backtrack § Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) | 問**最少**切幾刀 → DP（LC 132） |

<!-- 773101af399a -->
### 堆積與貪婪

| 敘述裡的訊號 | 限制提示 | 模式 | Card | 長得像，其實不是 |
|---|---|---|---|---|
| 「出現最頻繁的前 k 個」、「最近的 k 個」、「串流中的第 k 大」 | n ≤ 10⁵ | 大小為 k 的堆積 | [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | 固定陣列、只問一次 → quickselect，平均 O(n) |
| 「資料流的中位數」 | 5 · 10⁴ 次呼叫 | 雙堆積 | [heap § Template 4](./heap.md#template-4-two-heaps-for-a-running-median--lc-295-) | 滑動視窗的中位數 → 還要能從視窗刪除（[monotonic_queue § Template 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-)） |
| 「帶冷卻的任務排程」、「重排字串讓相同字元不相鄰」 | n ≤ 10⁴，26 種 | 對最頻繁的那個做貪婪 | [greedy § Template 2](./greedy.md#template-2-activity-selection-with-heap--lc-621-)、[Template 5](./greedy.md#template-5-string-reorganization--lc-767-) | 必須保持輸入順序 → 用佇列模擬 |
| 「能不能跳到終點」、「最少跳幾次」 | n ≤ 10⁴ | 貪婪：最遠可達 | [greedy § Template 4](./greedy.md#template-4-jump-game-pattern--lc-55-) | 每一跳有成本 → DP 或 Dijkstra |

<!-- 059777f22c86 -->
### 堆疊與設計

| 敘述裡的訊號 | 限制提示 | 模式 | Card | 長得像，其實不是 |
|---|---|---|---|---|
| 「合法括號」、「解碼字串」、「簡化路徑」 | n ≤ 10⁴ | 放未關閉項目的堆疊 | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) | 只有一種括號 → 一個計數器就夠 |
| 「下一個更大／更暖的一天」、「股票跨度」 | n ≤ 10⁵ | 單調堆疊 | [stack § Template 3](./stack.md#template-3-monotonic-stack--next-greater--smaller--lc-739-) | 每個大小為 k 的視窗的最大值 → 單調雙端佇列（LC 239） |
| 「直方圖中最大的矩形」、「最大矩形」 | n ≤ 10⁵ | 單調堆疊，找左右邊界 | [monotonic_stack § Template 3](./monotonic_stack.md#template-3-largest-rectangle-in-histogram--lc-84) | 接雨水 → 雙指標比較簡單（LC 42） |
| 「每個大小為 k 的視窗的最大／最小值」 | n ≤ 10⁵ | 單調雙端佇列 | [monotonic_queue § Template 1](./monotonic_queue.md#template-1-sliding-window-maximum-decreasing-deque--lc-239) | 要的是總和、不是最大值 → 一般滑動視窗 |
| 「min stack」、「LRU cache」、「O(1) 的 get 與 put」 | 每次操作 O(1) | 每種查詢一個結構，同步維護 | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-)、[design § 0-2](./design.md#0-2-pattern) | 依頻率而不是依最近使用淘汰 → LFU，兩個 map（LC 460） |
| 「單點更新的區間和」 | n、q ≤ 3 · 10⁴ | Fenwick 樹／線段樹 | [segment_tree § Template 1](./segment_tree.md#template-1-binary-indexed-tree-fenwick-tree) | 沒有更新 → 前綴和陣列 |
| 「只出現一次的數字」、「數 1 的位元」、「用遮罩表示子集」 | 值放得進 32 位元 | 位元技巧 | [bit_manipulation § 1-2](./bit_manipulation.md#1-2-single-bit-tricks-with-code) | 值沒有上界 → 雜湊表 |

---

<!-- 8fe2652b84b8 -->
## 挑模式之前 — 六步驟循環

> 挑對模式只是六步中的第 3 步。圍繞它的另外五步才是面試官真正在評分的，
> 而且這一頁的每一題都一樣。第一次模擬面試前讀一次就好，不必在複習時讀。

1. **理解題目**：需求、輸入與輸出、答案可接受的形式。
2. **問清範圍與限制**：資料規模與數值範圍（就是上面的限制提示）、邊界、預期的時間與空間。
3. **想出解法**：先把暴力解講出來，再講更好的解法與它的演算法、資料結構。寫虛擬碼、點出邊界情況，談好了再開始寫。
4. **寫程式**：乾淨、變數名稱說清楚它裝什麼，並寫下測試案例。
5. **執行**：走過測試、修 bug，說出時間與空間複雜度，以及決定它們的那一行。
6. **延伸問題**：更好的上界；輸入會擴大、以串流進來，或放不進記憶體。

---

<!-- d960f0f63e40 -->
## 練習路徑

每個家族一題錨定題，讓第一輪就碰過上面的每一列。⭐ 標的是 [L3 core 集合](https://github.com/yennanliu/CS_basics/blob/master/data/l3_core.json)。每份清單都由易到難。

- **Core ⭐**：LC 1 Two Sum · LC 20 Valid Parentheses · LC 206 Reverse Linked List · LC 121 Best Time to Buy and Sell Stock · LC 3 Longest Substring Without Repeating Characters · LC 102 Level Order Traversal · LC 200 Number of Islands · LC 322 Coin Change · LC 207 Course Schedule · LC 78 Subsets · LC 347 Top K Frequent Elements · LC 739 Daily Temperatures · LC 875 Koko Eating Bananas · LC 76 Minimum Window Substring
- **Practice**：LC 167 Two Sum II · LC 560 Subarray Sum Equals K · LC 236 Lowest Common Ancestor · LC 994 Rotting Oranges · LC 743 Network Delay Time · LC 416 Partition Equal Subset Sum · LC 1143 Longest Common Subsequence · LC 295 Find Median from Data Stream
- **Stretch**：LC 787 Cheapest Flights Within K Stops · LC 239 Sliding Window Maximum · LC 84 Largest Rectangle in Histogram · LC 212 Word Search II · LC 312 Burst Balloons
