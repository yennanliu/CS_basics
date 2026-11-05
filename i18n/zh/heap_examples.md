<!-- e0cea72a32fe -->
# 堆積（Heap） — 實戰題解

> **範圍** — 堆積／優先佇列（priority queue）家族的題目集：每題都歸到它所化約成的 [heap.md](./heap.md) 模板之下，並寫出它額外多出的那一步；模板本身，以及需要進階技巧的題目，放在相鄰的文件裡。
> **另見** — *主文件*：[heap.md](./heap.md) — 這些範例所實作的標準模板與模式選擇指南。*從同一份檔案拆出去的鄰近文件*：[heap_advanced.md](./heap_advanced.md) — 延遲刪除（lazy deletion）、反悔貪婪（regret greedy）、掃描線，以及較少見的堆積模板；[heap_language_apis.md](./heap_language_apis.md) — 完整的 `heapq` / `PriorityQueue` API 參考。

<!-- b869f594d6e2 -->
## LeetCode 題目清單

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

<!-- 3c35043be697 -->
## 總覽

[heap.md](./heap.md) 放的是八個堆積模板；這份文件放的是題目。下面每一節對應一個模板，順序相同，
每張卡片點出這題在模板之上多出的那一步。解法需要核心模板沒有的技巧（延遲刪除、反悔、
範圍跳躍的格子）的題目，詳解放在 [heap_advanced.md](./heap_advanced.md)；別份文件已經解過的題目，
這裡只留一列指標，絕不再寫第二份。

<!-- 4f7c43f22480 -->
## 索引

| LC | 題目 | 化約到 | 難度 | 位置 |
|---|---|---|---|---|
| LC 1046 | Last Stone Weight | [heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | 簡單 | [卡片](#last-stone-weight--lc-1046) |
| LC 703 | Kth Largest Element in a Stream | [heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | 簡單 | [卡片](#kth-largest-element-in-a-stream--lc-703-) |
| LC 1167 | Minimum Cost to Connect Sticks | [heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | 中等 | [卡片](#minimum-cost-to-connect-sticks--lc-1167) |
| LC 1492 | The kth Factor of n | [heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | 中等 | [卡片](#the-kth-factor-of-n--lc-1492) |
| LC 973 | K Closest Points to Origin | [heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | 中等 | [卡片](#k-closest-points-to-origin--lc-973-) |
| LC 1383 | Maximum Performance of a Team | [heap_advanced § 6](./heap_advanced.md#6-sort-by-one-criterion--fixed-size-heap-on-the-other) | 困難 | 詳解在 heap_advanced.md |
| LC 347 | Top K Frequent Elements | [heap § 模板 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | 中等 | 詳解在 heap.md |
| LC 451 | Sort Characters By Frequency | [heap § 模板 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | 中等 | [卡片](#sort-characters-by-frequency--lc-451) |
| LC 1481 | Least Number of Unique Integers after K Removals | [heap § 模板 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | 中等 | [卡片](#least-number-of-unique-integers-after-k-removals--lc-1481) |
| LC 1647 | Minimum Deletions to Make Character Frequencies Unique | [heap § 模板 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | 中等 | [卡片](#minimum-deletions-to-make-character-frequencies-unique--lc-1647) |
| LC 3092 | Most Frequent IDs | [heap_advanced § 1](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-) | 中等 | 詳解在 heap_advanced.md |
| LC 23 | Merge k Sorted Lists | [heap § 模板 3](./heap.md#template-3-k-way-merge--lc-23-) | 困難 | 詳解在 heap.md |
| LC 264 | Ugly Number II | [Dijkstra_examples § Variation B](./Dijkstra_examples.md#variation-b--lc-264-ugly-number-ii) | 中等 | 詳解在 Dijkstra_examples.md |
| LC 373 | Find K Pairs with Smallest Sums | [heap § 模板 3](./heap.md#template-3-k-way-merge--lc-23-) | 中等 | [卡片](#find-k-pairs-with-smallest-sums--lc-373) |
| LC 378 | Kth Smallest Element in a Sorted Matrix | [heap § 模板 3](./heap.md#template-3-k-way-merge--lc-23-) | 中等 | [卡片](#kth-smallest-element-in-a-sorted-matrix--lc-378-) |
| LC 1675 | Minimize Deviation in Array | [heap § 模板 3](./heap.md#template-3-k-way-merge--lc-23-) | 困難 | [卡片](#minimize-deviation-in-array--lc-1675) |
| LC 295 | Find Median from Data Stream | [heap § 模板 4](./heap.md#template-4-two-heaps-for-a-running-median--lc-295-) | 困難 | 詳解在 heap.md |
| LC 480 | Sliding Window Median | [monotonic_queue § 模板 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-) | 困難 | 詳解在 monotonic_queue.md |
| LC 1353 | Maximum Number of Events That Can Be Attended | [scanning_line_examples § LC 1353](./scanning_line_examples.md#5-maximum-number-of-events-that-can-be-attended--lc-1353-) | 中等 | 詳解在 scanning_line_examples.md |
| LC 2406 | Divide Intervals Into Minimum Number of Groups | [scanning_line_examples § LC 2406](./scanning_line_examples.md#2-divide-intervals-into-minimum-number-of-groups--lc-2406--lc-253-restated) | 中等 | 詳解在 scanning_line_examples.md |
| LC 767 | Reorganize String | [heap § 模板 7](./heap.md#template-7-greedy-build-with-a-run-length-cap--lc-1405) | 中等 | [卡片](#reorganize-string--lc-767-) |
| LC 621 | Task Scheduler | [heap § 模板 8](./heap.md#template-8-max-heap--cooldown-queue--lc-358) | 中等 | [卡片](#task-scheduler--lc-621-) |
| LC 871 | Minimum Number of Refueling Stops | [heap_advanced § 4](./heap_advanced.md#4-greedy-with-regret--undo-the-worst-past-decision-) | 困難 | 詳解在 heap_advanced.md |
| LC 2617 | Minimum Number of Visited Cells in a Grid | [heap_advanced § 8](./heap_advanced.md#8-grid-shortest-path-with-range-jumps) | 困難 | 詳解在 heap_advanced.md |
| LC 895 | Maximum Frequency Stack | [design_examples § LC 895](./design_examples.md#maximum-frequency-stack--lc-895-) | 困難 | 詳解在 design_examples.md — 頻率分桶，不需要堆積 |

<!-- efa795375674 -->
## 大小為 k 的堆積範例

<!-- c6b69d7f20e6 -->
### Last Stone Weight — LC 1046

> **關鍵** — 這裡的堆積沒有上限 k：它就是**整個池子**，每一步 pop 出最重的兩顆，再把差值 push
> 回去。最重的兩顆永遠是最先 pop 出來的兩個，所以每次相撞是 O(log n)。

**歸約到**：[heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — max-heap 是整份輸入而不是最好的 k 個，每次 pop 兩個。

<!--CODE-->

<!--CODE-->

**陷阱**
- `[2,2]` 會把兩顆都撞毀：堆積最後是**空的**，所以要先檢查是否為空再讀 `heap[0]`，並回傳 0。
- 兩顆一樣重時還 push 一個 0，會留下一顆之後「免費」相撞的 `0` 石頭 — 不影響答案，
  但是白做一次 push；直接略過。

<!-- 0f2f964743ef -->
### Kth Largest Element in a Stream — LC 703 ⭐⭐⭐⭐

> **關鍵** — 大小為 k 的 min-heap 會**跨呼叫保留**：`add` 先 push，超過 k 就驅逐最小的，
> 再回傳堆頂。建構物件就是對初始陣列逐一呼叫有上限的 `add`，而初始陣列可能少於 k 個數。

**歸約到**：[heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — 同一個堆積，改成欄位，讓它在查詢之間保留下來。

<!--CODE-->

<!--CODE-->

**陷阱**
- `nums` 可能比 k 短（`KthLargest(1, [])`）：建構子裡絕不能假設堆積已滿 —
  一律經過 `add` 就同時處理兩種情況。
- 先 heapify `nums` 再 pop 到剩 k 個，會改動呼叫端的串列；請自己建一個堆積。

**類似** — 順序統計 BST 對*任意*名次都能回答同樣的查詢：
[bst_advanced.md](./bst_advanced.md#template-3-size-augmented-order-statistic-bst--lc-703-)。

<!-- 256c21e794e4 -->
### Minimum Cost to Connect Sticks — LC 1167

> **關鍵** — 永遠合併**最短的兩根**：一根棍子的長度在它參與的每次合併都要付一次，
> 所以最短的應該放在合併樹的最深處（Huffman 編碼）。合併後的棍子放回池子裡。

**歸約到**：[heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — 沒有上限、裝著整個池子的 min-heap，像 LC 1046 一樣每次 pop 兩個。

<!--CODE-->

**陷阱**
- 只有一根棍子時成本是 **0**：迴圈不能執行，所以條件是 `len(heap) > 1`。
- 排序一次再由左往右合併是錯的：`[1,8,3,5]` 先合併 1+3 = 4，接著是 **4+5**，不是
  4+8 — 合併後的棍子必須重新和其他棍子競爭。

<!-- 66f964230a09 -->
### The kth Factor of n — LC 1492

> **關鍵** — 因數成對出現 `(i, n // i)`，且 `i ≤ √n`，所以 √n 次試除就能找到全部。
> 用大小為 k 的 **max**-heap 存看過的因數，留住最小的 k 個；只要 n 至少有 k 個因數，
> 堆頂就是第 k 小。

**歸約到**：[heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — 鏡像成第 k **小**，對象是成對產生的因數。

<!--CODE-->

**陷阱**
- 完全平方數的平方根只算一次：`n = 16` → `4` 不能 push 兩次，否則 k = 3 會因為錯的理由
  回傳 4，k = 4 則差一。
- 因數少於 k 個（`n = 4, k = 4`）要回傳 **-1**；檢查堆積大小，而不只是是否為空。
- 不用堆積的話，小因數本來就依序出現、大因數則反序出現 — 用兩個串列就是 O(√n) 時間。
  被問到更好的解法時就說這個。

<!-- 64df7ba40aad -->
### K Closest Points to Origin — LC 973 ⭐⭐⭐⭐

> **關鍵** — 「最近的 k 個」就是「依距離最小的 k 個」：用大小為 k、以距離平方為 key 的
> **max**-heap，留下的 k 個裡最遠的在堆頂，被驅逐的就是它。

**歸約到**：[heap § 模板 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) — 鏡像成最小的 k 個，key 由點推導而來。

<!--CODE-->

<!--CODE-->

**陷阱**
- 把全部 n 個點放進 **min**-heap 再 pop k 次是 O(n log n)、O(n) 空間 — 上限才是這個模板的重點。
- 比較 `Math.sqrt` 距離只會平白多出浮點誤差；距離平方的大小順序完全一樣。

**類似** — 單次查詢、靜態資料時，O(n) 平均的答案是 quickselect：
[quickselect § 模板 2](./quickselect.md#template-2-quickselect-on-a-derived-key--lc-973-)。
點是串流進來、或記憶體必須維持 O(k) 時，堆積勝出。

| LC | 題目 | 所在 | 為什麼不在這裡 |
|---|---|---|---|
| LC 1383 | Maximum Performance of a Team | [heap_advanced § 6](./heap_advanced.md#6-sort-by-one-criterion--fixed-size-heap-on-the-other) | 大小為 k 的堆積是跑在依另一個屬性排序的迴圈裡 |

<!-- b966b330e685 -->
## 先計數再用堆積的範例

| LC | 題目 | 所在 | 說明 |
|---|---|---|---|
| LC 347 | Top K Frequent Elements | [heap § 模板 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | 錨點題，旁邊附 O(n) 的桶排序版本 |
| LC 3092 | Most Frequent IDs | [heap_advanced § 1](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-) | 計數在 push 之後還會變 → 延遲刪除 |

<!-- bbb650648a6a -->
### Sort Characters By Frequency — LC 451

> **關鍵** — 計數一旦確定，堆積就是**整段整段**地輸出：pop 出最頻繁的字元，把它附加 `count`
> 次。這裡沒有 k，所以堆積沒有上限，一路清空。

**歸約到**：[heap § 模板 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) — 先計數，再依計數排序；k 就是相異字元的數量。

<!--CODE-->

<!--CODE-->

**陷阱**
- 大寫和小寫是不同字元：`"Aabb"` → `"bbAa"` 或 `"bbaA"`，絕不會是 `"bbaa"`。
- 在 Java 的迴圈裡用 `+=` 組字串是 O(n²)；請用 `StringBuilder`。
- 計數最多是 n，所以依計數做桶排序是 O(n) — 和 LC 347 的第二種解法是同一招，見
  [heap § 模板 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-)。

<!-- d7c632e06e77 -->
### Least Number of Unique Integers after K Removals — LC 1481

> **關鍵** — 要讓剩下的相異值最少，就**先把計數最小的值**整個清掉：計數的 min-heap，
> 只要整個計數還放得進 k 就 pop。只放得進一部分的值不值得碰。

**歸約到**：[heap § 模板 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) — 先計數，再依計數 pop；停止條件是預算 k，而不是大小。

<!--CODE-->

**陷阱**
- 一次只減一個複本（k 次 pop／push）是 O(k log d)，雖然會過，但把移除次數花在永遠刪不完的值上；
  請整個計數一起 pop。
- `heap and` 要放前面：`[1]`、k = 1 會把堆積清空，答案是 0。

<!-- c33da5dd37ea -->
### Minimum Deletions to Make Character Frequencies Unique — LC 1647

> **關鍵** — 用 max-heap 從**最大的計數往下**處理：堆頂等於下一個計數時，就削掉一個再 push
> 回去。一個計數只有在嚴格大於堆積裡所有計數時才會離開堆積，所以離開的計數全都不同。

**歸約到**：[heap § 模板 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) — 先計數，再對計數用 max-heap；多出的一步是平手時減一再放回。

<!--CODE-->

<!--CODE-->

**陷阱**
- 被削到 **0** 的計數代表那個字母已整個刪除；不要再 push 回去，否則兩個 0 會「撞在一起」
  又被多算一次（`"abcabc"` 是 3，不會更多）。
- 用排序的版本要小心：某個計數被歸零之後，它**後面**的計數仍可能相撞
  （`"aebacabbbad"` → 4,4,1,1,1 需要刪 3 次）。一遇到 0 就跳出迴圈會回傳 2。
- 另一個常見答案是用 `set` 記錄用過的計數（一直減到沒用過為止）；它是同一個貪婪，只是不排序。

<!-- c97a16b9f53f -->
## K 路合併範例

| LC | 題目 | 所在 | 說明 |
|---|---|---|---|
| LC 23 | Merge k Sorted Lists | [heap § 模板 3](./heap.md#template-3-k-way-merge--lc-23-) | 錨點題，旁邊附兩兩分治合併 |
| LC 264 | Ugly Number II | [Dijkstra_examples § Variation B](./Dijkstra_examples.md#variation-b--lc-264-ugly-number-ii) | 堆積 + seen 集合，展開 `v·2, v·3, v·5` — 最佳優先的生成 |

<!-- 96b0a2050e15 -->
### Find K Pairs with Smallest Sums — LC 373

> **關鍵** — 虛擬格子的第 i 列 `(i, 0), (i, 1), …` 本來就依和排序，所以這些列就是
> **k 個有序串列**。把每列的頭 `(i, 0)` 放進去，pop 時只讓那一列前進到 `(i, j + 1)` —
> 每個配對只被 push 一次，所以不需要 `seen` 集合。

**歸約到**：[heap § 模板 3](./heap.md#template-3-k-way-merge--lc-23-) — 來源是一個從未真正建出來的格子的各列，而且只有前 k 列可能有用。

<!--CODE-->

<!--CODE-->

**陷阱**
- 把每一列都放進去是白花 O(n₁ log n₁)：第 i ≥ k 列的配對前面至少有 k 個更小的配對，
  所以只放 `min(k, len(nums1))` 列。
- k 可能超過 `len(nums1) · len(nums2)`（`[1,2]`、`[3]`、k = 3 → 兩組配對）：堆積空了就停。
- 每次 pop 都同時展開 `(i + 1, j)` 和 `(i, j + 1)` 是最佳優先的變形，那個**就**需要 `seen`
  集合 — 見
  [Dijkstra_examples § 11](./Dijkstra_examples.md#11-best-first-search-on-an-implicit-graph--lc-373)。

<!-- 77d123059564 -->
### Kth Smallest Element in a Sorted Matrix — LC 378 ⭐⭐⭐⭐

> **關鍵** — 每一列都是有序來源；每列放一個頭來合併，**第 k 次 pop** 就是答案。
> 只有前 `min(k, n)` 列可能含有最小的 k 個值之一。

**歸約到**：[heap § 模板 3](./heap.md#template-3-k-way-merge--lc-23-) — 各列就是 k 個有序串列，合併在第 k 個值時停止。

<!--CODE-->

<!--CODE-->

**陷阱**
- 對**全部 n² 個格子**用大小為 k 的 max-heap 也行，但它沒用到各列已排序：
  O(n² log k)。寫哪一個之前先說明為什麼合併比較好。
- 重複值要分開算：`[[1,2],[1,3]]`、k = 2 是 **1**。
- k 接近 n² 時，對值域做二分搜尋是 O(n log(max − min))，勝過堆積：
  [binary_search_on_answer § 模板 4](./binary_search_on_answer.md#template-4-count-at-most-over-a-value-range--lc-378-)。

<!-- dd13df7991c8 -->
### Minimize Deviation in Array — LC 1675

> **關鍵** — 先把每個奇數提高到它能到達的**最大**值（× 2）；從此以後值只能減半。
> 於是每個數都是一個往下走的值來源，偏差就是目前各個頭的 `max − min`：永遠把目前的
> **最大值**減半、追蹤一路以來的最小值，最大值變成奇數時就停 — 那個來源已經用完。

**歸約到**：[heap § 模板 3](./heap.md#template-3-k-way-merge--lc-23-) — 往下跑的 k 路合併（每個數一個頭，最大值在頂），也就是 LC 632 涵蓋所有串列的最小範圍的形狀。

<!--CODE-->

<!--CODE-->

**陷阱**
- 把小的值乘上去而不是把最大值減半：正規化之後每一步都是減半，而只有把**最大值**減半
  才能縮小 `max − min`。
- 最小值不在堆頂 — 必須手動追蹤，每次減半之後都要更新（`[2,10,8]` 要把 8 → 4 → 2
  算進去才得到 3）。
- 要在檢查奇數**之前**記錄偏差：最後那個奇數最大值仍然給出一個合法答案。

<!-- 1a7bb20efb17 -->
## 雙堆積中位數範例

| LC | 題目 | 所在 | 說明 |
|---|---|---|---|
| LC 295 | Find Median from Data Stream | [heap § 模板 4](./heap.md#template-4-two-heaps-for-a-running-median--lc-295-) | 錨點題 |
| LC 480 | Sliding Window Median | [monotonic_queue § 模板 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-) | 視窗還會移除值 → 有序多重集合，或雙堆積 + 延遲刪除 |

<!-- f7f67d1c5dcf -->
## 結束時間堆積範例

| LC | 題目 | 所在 | 說明 |
|---|---|---|---|
| LC 1353 | Maximum Number of Events That Can Be Attended | [scanning_line_examples § LC 1353](./scanning_line_examples.md#5-maximum-number-of-events-that-can-be-attended--lc-1353-) | 每個事件只花掉其範圍中的**一**天 → 堆積存截止日，而不是結束時間；[heap § 模板 6](./heap.md#template-6-min-heap-of-end-times--lc-253-) 說明為什麼 LC 253 的計數會高估 |
| LC 2406 | Divide Intervals Into Minimum Number of Groups | [scanning_line_examples § LC 2406](./scanning_line_examples.md#2-divide-intervals-into-minimum-number-of-groups--lc-2406--lc-253-restated) | 閉區間版的 LC 253：只有 `end < start` 時才沿用一組 |

<!-- 0bae5dcea86d -->
## 連續長度上限範例

<!-- 3e167a8faee8 -->
### Reorganize String — LC 767 ⭐⭐⭐⭐

> **關鍵** — 有解**若且唯若**最頻繁的字母放得進每隔一格：`max_freq ≤ (n + 1) // 2`。
> 成立之後，每一輪拿**最頻繁的兩個**字母：它們不同，而且下一輪的第一個字母不可能是
> 這一輪的第二個，因為第二個的計數永遠不會超過第一個。

**歸約到**：[heap § 模板 7](./heap.md#template-7-greedy-build-with-a-run-length-cap--lc-1405) — 連續長度上限為 1，一次 pop 兩個取代了往回看的檢查。

<!--CODE-->

<!--CODE-->

**第二種解法 — 不用堆積，O(n)。** 可行性檢查本身就能建出答案：把字母依頻率由高到低，
先寫進第 0、2、4… 格，再寫進第 1、3、5… 格。同一個字母的兩個複本相隔兩格，而從偶數格
繞到奇數格的地方是安全的，因為檢查保證最頻繁的字母放得進偶數格。

<!--CODE-->

**陷阱**
- 跳過可行性檢查：`"aaab"` 必須回傳 `""`；一次兩個的迴圈反而會剩下 `a` 被連放兩次。
- 偶奇填格只有在最頻繁的字母**先放**時才成立 — `"abbccc"` 依字母順序填，會在繞回處讓
  兩個 `c` 相鄰。
- 堆積最多 26 筆，所以 `log 26` 是常數；說 O(n) 就好。

**類似** — LC 1054（同一題換成數字） · LC 358（一般的間隔 k → 模板 8）

<!-- a877ea11689d -->
## 冷卻佇列範例

<!-- eda6c82bbcba -->
### Task Scheduler — LC 621 ⭐⭐⭐⭐⭐

> **關鍵** — 只問**長度**，所以只有最頻繁的任務重要：它逼出 `max_freq − 1` 個寬度為 `n + 1`
> 的框，再加上最後一個框，放所有計數並列最多的任務。如果任務數比這個排法的格子還多，
> 就永遠不需要閒置，答案是 `len(tasks)`。

**歸約到**：[heap § 模板 8](./heap.md#template-8-max-heap--cooldown-queue--lc-358) — 冷卻排程，用封閉公式數出來而不是模擬。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**為什麼公式是 V0** — 它是 O(N) 時間、O(1) 空間，是面試官問「回傳最少的時間單位數」時預期的答案，
而且那張框的圖就是一個畫得出來的證明。下面的模擬就是模板 8 本身；當題目要的是任務的**順序**，
或冷卻規則不一致時，才用它。

**第二種解法 — 用堆積加冷卻佇列模擬。** 執行過的任務帶著可以再執行的時間在 FIFO 裡等；
到了那個時刻就回到堆積。

<!--CODE-->

**陷阱**
- 忘了 `max(len(tasks), …)`：`["A","C","A","B","D","B"]`、n = 1 的框數是 4，
  但六個任務需要六個時間單位。
- 只算一個最多次的任務：`AAABBB`、n = 2 是 8 而不是 7 — 最後一個框放的是所有並列
  `max_freq` 的任務。
- 模擬裡計數取了負號：`remaining < 0` 表示還有複本；`== 0` 就把這個任務永遠丟掉。

**類似** — LC 358（要問順序 → 模板 8） · LC 767（間隔 2，模板 7） · LC 2365（Task
Scheduler II：順序固定，用雜湊表記錄下一個允許的日子）
