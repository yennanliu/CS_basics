<!-- cf8e6e95503b -->
# 堆積與優先佇列

> **範圍** — 堆積（heap，這個資料結構）以及它所實作的優先佇列（priority queue，這個抽象資料型別），Python 與 Java 兩種語言。本檔案原本拆成 `heap.md` + `priority_queue.md`，兩邊把同樣的題目各解了一遍。
> **另見** — *從本檔案拆出去的深入主題*：[heap_advanced.md](./heap_advanced.md) — 延遲刪除、掃描線的「存活」堆積、後悔貪婪、資源池配置器、格子圖最佳優先搜尋；[heap_examples.md](./heap_examples.md) — 題目集，每題都歸到下面它所化約成的模板之下；[heap_language_apis.md](./heap_language_apis.md) — 完整的 `heapq` / `PriorityQueue` API 參考，以及「不 pop 就 peek」的規則。
> *鄰近文件*：[priority_queue.md](./priority_queue.md) — 轉址用的 stub；[monotonic_queue.md](./monotonic_queue.md) — 什麼時候滑動視窗極值用雙端佇列比堆積好；[Dijkstra.md](./Dijkstra.md) — 最經典的優先佇列演算法；[streaming_algorithms.md](./streaming_algorithms.md) — 串流上的 top-k；[sort.md](./sort.md) — 放在排序脈絡下的堆積排序。

<!-- b869f594d6e2 -->
## LeetCode 題目清單

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

<!-- b5cce6881827 -->
## 時間複雜度

| 資料結構 | 搜尋 | 插入 | 刪除 | 最小／最大 |
| -------------- | -------- | -------- | -------- | -------- |
| 堆積（Heap） | O(n) | O(log n) | O(log n) | O(1) |

> 讀取堆頂元素（min-heap 取最小／max-heap 取最大）是 **O(1)**；要找*另一端*的極值則是 **O(n)**。從 `n` 個既有元素建堆，用 heapify 是 **O(N)** — *不是* `O(N log N)`。空間為 **O(N)**。

<!-- f3e2c4c25293 -->
## 總覽
**堆積（Heap）** 是一棵滿足堆積性質的完全二元樹，因此非常適合用來高效存取資料集中的最大或最小元素。它是優先佇列與堆積排序演算法的基礎。

<p align="center"><img src="../pic/heap_space_time_complexity.png"></p>

<p align="center"><img src="../pic/heap_op_101.png"></p>

<!-- 33e88cf6677a -->
### 關鍵性質
- **複雜度**：見上方的[時間複雜度](#time-complexity)表格
- **核心想法**：完全二元樹，且父子節點之間滿足堆積性質
- **什麼時候用**：需要頻繁存取最小／最大元素、優先權排程、排序

<!-- 56c7626fea12 -->
### 堆積的種類
- **最小堆積（Min Heap）**：父節點 ≤ 子節點（根是最小值）
- **最大堆積（Max Heap）**：父節點 ≥ 子節點（根是最大值）

<p align="center"><img src="../pic/type_of_heap.png"></p>

<!-- 7cfc50e0cfc4 -->
### 與優先佇列的關係
- **優先佇列（Priority Queue）**：以優先權存取的抽象資料型別
- **堆積（Heap）**：優先佇列最常見的實作方式
- **關鍵差別**：優先佇列是概念，堆積是實作

<!-- a9d276199ec7 -->
### 實作方式
- 通常用**二元堆積**（min-heap 或 max-heap）實作
- 需要進階操作時也可以用平衡二元搜尋樹或費氏堆積（Fibonacci heap）
- Python：`heapq` 模組（預設為 min-heap）
- Java：`PriorityQueue` 類別（預設為 min-heap）

<!-- f9c65b205839 -->
### 題型分類

<!-- f92b96526d3b -->
#### **模式 1：第 k 個元素問題**
- **描述**：在資料集中找第 k 大／第 k 小的元素
- **範例**：LC 215、703、1492 - Kth Largest Element、Kth Largest in Stream、Kth Factor
- **模式**：使用大小為 k 的 min／max heap，維持堆積性質

<!-- 0fc7da10a00e -->
#### **模式 2：Top K 問題**
- **描述**：找出頻率或數值最高／最低的前 k 個元素，或是讓所有頻率互不相同
- **範例**：
  - Top K：LC 347、692、973 - Top K Frequent Elements、Top K Words、K Closest Points
  - 頻率唯一化：LC 1647、1481 - Make Frequencies Unique、Least Unique After K Removals
- **模式**：先統計頻率，再用堆積維護 top k 結果或確保頻率唯一

<!-- 9b9aab6ab68b -->
#### **模式 3：合併問題**
- **描述**：高效合併多個有序陣列／串列
- **範例**：LC 23、373、378 - Merge k Lists、K Smallest Pairs、Kth Smallest in Matrix
- **模式**：用 min heap 追蹤每個來源目前的最小值

<!-- ee342f16805f -->
#### **模式 4：滑動視窗極值**
- **描述**：高效求出滑動視窗內的最小／最大值
- **範例**：LC 239、480、1438 - Sliding Window Maximum、Sliding Median、Longest Subarray
- **模式**：用堆積搭配延遲刪除，或用雙端佇列追蹤極值

<!-- cd2f57c3ea0c -->
#### **模式 5：排程問題**
- **描述**：依優先權／時間安排任務或事件
- **範例**：LC 1353、502、630、621、1834 - Max Events、IPO、Course Schedule III、Task Scheduler、Single-Threaded CPU
- **模式**：用堆積依開始／結束時間或優先權維護事件
- **關鍵洞察（時間掃描 + 截止期限堆積）**：依視窗**起點**排序，讓元素按時間順序進堆積；堆積依視窗**終點**排序，這樣每個時間格都服務**最急迫（最早截止）**的元素；過期的堆頂用延遲刪除清掉
- **特徵**：*「每單位時間只能處理一件事」* + *「每件事有有效區間／截止期限」* → 見 [scanning_line_examples.md § LC 1353](./scanning_line_examples.md#5-maximum-number-of-events-that-can-be-attended--lc-1353-)
- **陷阱**：**不要**套用 LC 253 的計數掃描。在那題裡 `[1,3]` 是*佔用*第 1～3 天；在這裡它只花掉 `{1,2,3}` 中的**一天**，而且每天最多一個事件 — 所以用最大重疊數會高估（`[[1,1],[1,1],[1,1]]` → 掃描得 3，答案是 1）。見 [scanning_line_examples.md § LC 1353](./scanning_line_examples.md#5-maximum-number-of-events-that-can-be-attended--lc-1353-)

<!-- 1692ad150603 -->
#### **模式 6：資料串流問題**
- **描述**：處理連續資料串流上的最小／最大值查詢
- **範例**：LC 295、480、1825 - Find Median、Sliding Median、Finding MK Average
- **模式**：用兩個堆積（min + max）維持平衡結構

<!-- 0fec79420e8b -->
#### **模式 7：可跳躍範圍的格子圖最短路徑**
- **描述**：在格子圖上求最短路徑，其中每個格子可以跳到一個範圍內的格子
- **範例**：LC 2617 - Minimum Number of Visited Cells in a Grid
- **模式**：DP + 每列／每行各一個優先佇列，搭配延遲刪除
- **關鍵洞察**：一般 BFS 每個格子要 O(N²)；用優先佇列可降到每格 O(log N)
- **相似題**：LC 778（Swim in Rising Water）、LC 1631（Path With Minimum Effort）

<!-- c1eda9cfb897 -->
#### **模式 8：延遲刪除（堆積中的過期資料）** ⭐⭐⭐⭐⭐
- **描述**：堆積裡的值後來會被**更新／失效**，但二元堆積沒有
  「decrease-key」／「移除任意元素」的操作 — 所以我們直接 push 新值，把舊的留在裡面
- **範例**：LC 3092、2349、1834、480、1825、2336、621、1353、2406
- **模式**：**堆積 = 候選集合（可能過期）** + **雜湊表 = 真值來源** →
  只在*讀取時*清理堆頂，而且只清*到堆頂有效為止*
- **關鍵洞察**：你從來不去堆積裡搜尋過期資料。你只檢查 `heap[0]`，
  而每筆過期資料整段執行下來最多被 pop 一次 → 攤還 O(log n)
- **另見**：[heap_advanced.md § 延遲刪除](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-)，其中完整走過 LC 3092
- **同時要兩個極值**（LC 2034）：把堆積做成鏡像 — 一份真值對照表，加上存同樣 `(price, ts)` 的
  最大堆積與最小堆積，兩邊用同一個判定清理 →
  [heap_advanced.md § LC 2034 變形](./heap_advanced.md#variant--two-mirrored-lazy-heaps-over-one-map-lc-2034-)
- **同一個想法在別處**：雜湊表的墓碑、LSM-tree 的刪除、MVCC 的死列、Dijkstra 的
  `d > dist[u]` 跳過 →
  [heap_advanced.md § 堆積之外](./heap_advanced.md#lazy-deletion-outside-the-heap--the-same-idea-in-other-structures-)

<!-- 31bcc46f2a90 -->
#### **模式 9：掃描線 + 「存活」區間堆積** ⭐⭐⭐⭐⭐
- **描述**：掃描一個座標軸；堆積裡放的是**當下覆蓋該座標**的所有區間
- **範例**：LC 218 The Skyline Problem、LC 1851 Minimum Interval to Include Each Query
- **模式**：堆積存 `(value, endCoordinate)` → 在起點插入，當 `end <= pos` 時在堆頂**延遲驅逐**，然後讀 `heap[0]`
- **特徵**：*「在每個 x，求所有覆蓋 x 的區間之最大／最小值」*
- **另見**：[heap_advanced.md § 掃描線](./heap_advanced.md#2-sweep-line--max-heap-of-alive-intervals-)

<!-- e2fd40fdfe7b -->
#### **模式 10：有上限的「後悔」堆積（k 次免費機會）** ⭐⭐⭐⭐
- **描述**：k 份免費資源 + 其餘部分的預算，而且要**線上（online）**決定
- **範例**：LC 1642 Furthest Building You Can Reach、LC 1792 Maximum Average Pass Ratio
- **模式**：樂觀地先給每個元素一次免費機會；min-heap 上限為 k；被擠掉的（最小的）那個改用預算支付
- **對照**：LC 630 擠掉的是**最大的**（max-heap replace）— 同樣是「先承諾再後悔」的想法，只是比較器相反
- **另見**：[heap_advanced.md § 有上限的後悔堆積](./heap_advanced.md#3-bounded-regret-heap--keep-the-k-best-pay-for-the-rest-)

<!-- 70320439f866 -->
#### **模式 11：兩個堆積當資源池** ⭐⭐⭐⭐
- **描述**：配置器模擬 — 不是每一題「兩個堆積」都是中位數問題
- **範例**：LC 1942 Smallest Unoccupied Chair、LC 1606 Find Servers、LC 1801 Orders in Backlog、LC 2073 Process Tasks Using Servers
- **模式**：`free` = 依**資源編號**排序的 min-heap，`busy` = 依**釋放時間**排序的 min-heap → 釋放 → 指派 → 佔用
- **另見**：[heap_advanced.md § 資源池](./heap_advanced.md#5-two-heaps-as-resource-pools-free-pool--busy-pool-)

<!-- 27e09eadefd2 -->
#### **模式 12：帶限制的貪婪字串／序列建構** ⭐⭐⭐⭐
- **描述**：貪婪地用出現次數最多的元素來建構字串／序列，但當加進去會違反限制時（例如連續 3 個相同字元）就先跳過。用 max-heap 讓當前次數最多的元素隨時可取。
- **範例**：LC 1405（Longest Happy String）、LC 767（Reorganize String）、LC 621（Task Scheduler）、LC 358（Rearrange String k Distance Apart）
- **模式**：依計數排序的 max-heap；每一步先試堆頂元素 — 如果違反限制，就暫時改用第二個元素，再把第一個放回去
- **關鍵技巧**：兩種情況的迴圈
  1. **情況 1 — 違反限制**：poll 出 `second`，接上去、計數減一、若 > 0 再放回；然後把 `first` 放回（它**沒有**被消耗）
  2. **情況 2 — 安全**：接上 `first`、計數減一、若 > 0 再放回
- **另見**：[模板 7](#template-7-greedy-build-with-a-run-length-cap--lc-1405) — 連續上限（1 還是 2）就是 LC 767 和 LC 1405 的差別所在

<!-- a22e436ef9a6 -->
#### **模式 13：優先佇列 + 冷卻佇列（k 距離排程）** ⭐⭐⭐⭐
- **描述**：從 max-heap 貪婪地取出次數最多的元素，用完後把它鎖進冷卻佇列 k 步才能再被使用。這是「相同元素之間至少要相隔 k」這類題目的標準模式。
- **範例**：LC 358（Rearrange String k Distance Apart）、LC 621（Task Scheduler）、LC 767（Reorganize String — k=2 的特例）
- **模式**：max-heap 挑下一個元素；用完後該元素進入冷卻佇列並記 `releaseTime = time + k`；當 `time == releaseTime` 時再移回堆積
- **關鍵洞察**：光靠優先佇列無法追蹤「上次使用位置」— 冷卻佇列扮演一條長度為 k 的延遲線，k 步之後自動讓元素重新可用
- **什麼時候用**：
  1. 題目說「相同元素至少相隔 k」或「冷卻時間 k」
  2. 需要貪婪地挑出當前可用且次數最多的元素
  3. 元素在「可用 → 使用中 → 冷卻中 → 可用」之間循環
- **與模式 7 的差異**：模式 7 檢查回看視窗並交換元素；模式 8 用一個明確的冷卻佇列來強制距離限制，對於可變的 k 來說更乾淨

<!-- b05cdc6266b8 -->
### 參考資料
- [LeetCode Heap Learn Card](https://leetcode.com/explore/learn/card/heap/)
- [GeeksforGeeks Heap Guide](https://www.geeksforgeeks.org/heap-data-structure/)


- [Python heapq documentation](https://docs.python.org/3/library/heapq.html)
- [Java PriorityQueue](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html)
- [Priority Queue Implementation Notes](https://docs.python.org/3/library/heapq.html#priority-queue-implementation-notes)

<!-- 6f7c0ef255bc -->
## 辨識

限制條件的線索來自 [complexity_cheatsheet.md](./complexity_cheatsheet.md) 裡的尺規。當集合一直在變、而你需要**反覆**取得當前極值時，答案就是堆積；對靜態資料只問一次的題目，通常有更便宜的做法。

| 題目敘述中的訊號 | 限制條件線索 | 該用 | 不是這個，如果 |
|---|---|---|---|
| 「第 k 大／第 k 小」、「最接近的 k 個」 | n ≤ 10⁵、k ≪ n → O(n log k) | [模板 1](#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) 大小為 k 的堆積 | 對靜態陣列只問一次 → quickselect，平均 O(n)（[quickselect.md](./quickselect.md)） |
| 「出現最頻繁的前 k 個」、「最常見的 k 個單字」 | 相異值 ≤ n | [模板 2](#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) 先計數，再用大小為 k 的堆積 | 計數上限為 n 且 k 接近 n → 桶排序，O(n) |
| 「合併 k 個有序串列／陣列」、「有序矩陣中第 k 小」 | k 個串列、共 N 個 → O(N log k) | [模板 3](#template-3-k-way-merge--lc-23-) K 路合併 | 只有兩個來源 → 一般的雙指標合併 |
| 「資料串流的中位數」、「平衡兩半」 | 大量 `add` + `query` 呼叫 | [模板 4](#template-4-two-heaps-for-a-running-median--lc-295-) 雙堆積 | 視窗也會*移除*值 → 延遲刪除（[heap_advanced.md](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-)） |
| 「max − min ≤ limit 的最長子陣列」、可變視窗同時需要最大**與**最小 | n ≤ 10⁵ | [模板 5](#template-5-two-heaps-with-index-expiry--lc-1438-) 雙堆積 + 依索引過期 | 固定大小視窗、只要一個極值 → 單調雙端佇列，O(n)（[monotonic_queue.md](./monotonic_queue.md)） |
| 「最少需要幾間會議室／幾組」、「同時重疊的最大區間數」 | n ≤ 10⁴–10⁵ 個區間 | [模板 6](#template-6-min-heap-of-end-times--lc-253-) 結束時間的 min-heap | 每個區間只花掉其範圍中的**一天**（LC 1353）→ 逐日掃描，見模板 6 的變形 |
| 「不能有三個相同字母連在一起」、「相鄰不可相同」 | 字母表 ≤ 26 | [模板 7](#template-7-greedy-build-with-a-run-length-cap--lc-1405) 貪婪 + 連續長度上限 | 間隔是一般的 k → 模板 8 |
| 「相同任務／字母至少相隔 k」、「冷卻時間 n」 | 字母表 ≤ 26 | [模板 8](#template-8-max-heap--cooldown-queue--lc-358) 堆積 + 冷卻佇列 | 只問**長度**（LC 621）→ 計數公式，O(n) |
| 加權最短路徑、邊權非負 | V, E ≤ 10⁵ | [Dijkstra.md](./Dijkstra.md) — 優先佇列演算法放在那裡 | 權重皆為 1 → BFS（[bfs.md](./bfs.md)） |
| push 進去的值之後會變動或過期 | | [heap_advanced.md § 延遲刪除](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-) | |

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 012d413b193a -->
### 模板對照表

標準模板放在這裡，每一個都是一張**兩種語言都有**的卡片，Python 在前。tier-4 的特化版本被拆到
[heap_advanced.md](./heap_advanced.md) — 右邊那一欄說明每個模板去了哪裡。

| 模板 | 使用情境 | 複雜度 | 位置 |
|---|---|---|---|
| **大小為 k 的堆積** | 第 k 大／第 k 小 | O(N log k) | [模板 1](#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) |
| **計數 + 大小為 k 的堆積** | 最常／最少出現、最接近的 k 個 | O(N log k) | [模板 2](#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) |
| **K 路合併** | 合併有序串列／陣列 | O(N log k) | [模板 3](#template-3-k-way-merge--lc-23-) |
| **雙堆積（中位數）** | 串流的中位數 | 每次 add 為 O(log N) | [模板 4](#template-4-two-heaps-for-a-running-median--lc-295-) |
| **雙堆積 + 依索引過期** | 可變視窗且同時需要最大**與**最小 | O(N log N) | [模板 5](#template-5-two-heaps-with-index-expiry--lc-1438-) |
| **結束時間的 min-heap** | 會議室、重疊數 | O(N log N) | [模板 6](#template-6-min-heap-of-end-times--lc-253-) |
| **貪婪 + 連續長度上限** | 建構不含連續 k 個相同字元的字串 | O(N log Σ) | [模板 7](#template-7-greedy-build-with-a-run-length-cap--lc-1405) |
| **堆積 + 冷卻佇列** | k 距離／任務排程 | O(N log Σ) | [模板 8](#template-8-max-heap--cooldown-queue--lc-358) |
| **圖的最短路徑** | 用優先佇列的 Dijkstra | O(E log V) | [Dijkstra.md](./Dijkstra.md) |
| **延遲刪除** | push 進去的值會變動或過期 | 攤還 O(log N) | [heap_advanced.md](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-) |
| **掃描 + 存活堆積** | 覆蓋 x 的區間之最大／最小值 | O(N log N) | [heap_advanced.md](./heap_advanced.md#2-sweep-line--max-heap-of-alive-intervals-) |
| **有上限的後悔堆積** | k 次免費機會 + 預算 | O(N log k) | [heap_advanced.md](./heap_advanced.md#3-bounded-regret-heap--keep-the-k-best-pay-for-the-rest-) |
| **帶後悔的貪婪** | 撤銷過去最差的決定 | O(N log N) | [heap_advanced.md](./heap_advanced.md#4-greedy-with-regret--undo-the-worst-past-decision-) |
| **資源池（2 個堆積）** | 依編號的空閒池 + 依釋放時間的忙碌池 | O(N log N) | [heap_advanced.md](./heap_advanced.md#5-two-heaps-as-resource-pools-free-pool--busy-pool-) |
| **排序 + 固定大小堆積** | 目標式 = `sum(A) × max/min(B)` | O(N log N) | [heap_advanced.md](./heap_advanced.md#6-sort-by-one-criterion--fixed-size-heap-on-the-other) |
| **格子圖最佳優先** | 展開代價最小的格子 | O(MN log MN) | [heap_advanced.md](./heap_advanced.md#7-min-heap-best-first-search-on-a-grid) |
| **格子圖範圍跳躍** | 每個格子跳到一個範圍 | O(MN log(M+N)) | [heap_advanced.md](./heap_advanced.md#8-grid-shortest-path-with-range-jumps) |
| **頻率唯一化** | 讓所有頻率互不相同 | O(N + K log K) | [heap_advanced.md](./heap_advanced.md#9-frequency-uniqueness--greedy--heap--hashset) |
| **堆積 + 去重集合** | 唯一性限制 | O(log N) | [heap_advanced.md](./heap_advanced.md#10-heap-with-deduplication) |

每張卡片都會用到的基本 `heapq` / `PriorityQueue` 呼叫（min-heap、用取負或
`reverseOrder()` 做 max-heap、`heapify`、peek）都在下方的[語言 API](#language-apis)。

<!-- 7859df8005bb -->
### 模板 1：用大小為 k 的 Min-Heap 求第 k 大 — LC 215 ⭐⭐⭐⭐⭐

**辨識** — 「第 k 大」、「第 k 小」、「最接近的 k 個」、「最強的 k 個」。n ≤ 10⁵ 且
k 遠小於 n → 預期的複雜度是 O(n log k)。

**關鍵** — 堆積裡放的是目前為止最大的 k 個值，所以它的**堆頂（這 k 個裡最小的）就是第 k 大**。
第 k 小則反過來：用大小為 k 的 **max**-heap 存最小的 k 個，堆頂就是答案。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n log k)**：迴圈跑 n 次，每次 `heappush` / `heappop` 操作的堆積
  最多只有 k + 1 個元素。
- **空間 O(k)**：`len(min_heap) > k` 時的 pop 讓堆積上限維持在 k。

**陷阱**
- 方向弄反：「第 k 大」用 **max**-heap 會留下最*小*的 k 個。`[3,2,1,5,6,4]`、
  k = 2 會回傳 2 而不是 5。寫程式前先把不變量講出來。
- 重複值要算進去：LC 215 `[3,2,3,1,2,4,5,5,6]`、k = 4 的答案是 **4** — 不要去重。
- 數字字串（LC 1985）會依字典序比較：`"10" < "6"`。Python 用 `(len(s), s)` 當堆積的 key，
  Java 則先比長度、再用 `compareTo`。
- 讓堆積長到 n、最後再 pop k 次是 O(n log n) — 設上限才是重點。

**不適用時** — 對靜態陣列只查一次：quickselect 平均 O(n)
（[quickselect.md](./quickselect.md)）。當值以串流方式到來（LC 703），
或要反覆查詢第 k 個元素時，堆積才勝出。

**練習** — LC 215 ⭐ · LC 703 *（同一個堆積，跨多次呼叫保留）* · LC 1985 *（key = `(len, s)`）* ·
LC 1337 *（key = `(soldiers, row)`）* · LC 378 *（有序矩陣中第 k 小 — 或用模板 3）*

<!-- d068954649e3 -->
### 模板 2：先計數，再對計數用大小為 k 的堆積 — LC 347 ⭐⭐⭐⭐

**辨識** — 「出現最頻繁的 k 個」、「前 k 個單字」、「最接近的 k 個點」：排名用的 key 是
**推導出來的**（計數、距離），而不是值本身。相異值 ≤ n。

**關鍵** — 雜湊表一次把輸入轉成 `(key, item)` 配對；之後就是對 **key** 做模板 1。
大小為 k 的 min-heap 存著最好的 k 組配對，堆頂是還留在裡面最弱的那一組。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n log k)**：計數是一次 O(n) 的掃描；堆積迴圈對每個相異值（≤ n）跑一次，
  而堆積上限為 k。
- **空間 O(n)**：`count` 表存了每個相異值；堆積再多 O(k)。

**陷阱**
- **平手時，堆積要有可比較的順序。** key 平手時 Python 會比較 tuple 的下一個欄位，
  所以 `(freq, obj)` 裡若 payload 無法比較就會丟 `TypeError` — 改 push `(freq, counter, obj)`
  （見[平手時的決勝](#tie-breaking)）。Java 則要把決勝規則寫進比較器。
- LC 692 要求平手時依**字母順序**，但堆積驅逐的是*最少*出現的 — 所以驅逐用的比較器是
  `(freq 遞增, word 遞減)`，和輸出順序相反。
- 自訂物件的堆積：Python 需要在類別上定義 `__lt__`（或用 tuple key）；Java 需要用
  `Integer.compare` 建的比較器，絕不能用 `a - b`。

**第二種解法 — 依計數做桶排序，O(n)。** 計數最多是 n，所以用計數當桶的索引，
由高往低讀：不需要比較排序，也不需要堆積。這是 LC 347 追問（「比 O(n log n) 更好」）的答案；
堆積仍是 V0，因為當 key 不是小整數（LC 973 的距離）或輸入是串流時，它照樣能用。

<!--CODE-->

**不適用時** — k 接近 n 且計數上限為 n：依頻率做桶排序是 O(n)
（即上面的第二種解法）。沒有 k、只是「全部依 key 排序」的靜態題，直接用 `sorted` 就好。

**練習** — LC 347 ⭐ · LC 973 *（key = 距離平方）* · LC 692 *（反向決勝）* ·
LC 451 *（輸出 `char * freq`）* · LC 1338 *（一直 pop 最頻繁的，直到刪掉一半）*

<!-- a837b76ac6fa -->
### 模板 3：K 路合併 — LC 23 ⭐⭐⭐⭐⭐

**辨識** — 「合併 k 個有序串列／陣列」、「涵蓋 k 個串列的最小範圍」、「第 k 小的配對／有序矩陣中第 k 小」。
k 個來源、總共 N 個元素 → O(N log k)。

**關鍵** — 堆積裡**每個非空來源恰好只有一個候選**，也就是它目前的頭。
全域最小值一定是這些頭之一，所以 pop 出它、再 push 該來源的下一個元素，
就能維持這個不變量。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(N log k)**：N 個節點每個都被 push 一次、pop 一次，而堆積
  最多只有 k 個項目（每個來源一個）。
- **空間 O(k)**：就是堆積；輸出直接重用輸入的節點。

**陷阱**
- 沒有索引的 `(node.val, node)`：兩個值相同時 Python 會去比較 `ListNode` →
  在 LC 23 `[[1,4,5],[1,3,4],[2,6]]` 上丟 `TypeError`。來源索引在堆積的項目中是唯一的，
  所以一定能分出勝負。
- `[]` 和 `[None]` 都是 LC 23 的合法輸入：初始化時跳過空來源，並回傳
  `dummy.next`（也就是 `None`）。
- 若是陣列，就 push `(value, source, index)` 並前進到 `index + 1` — 形狀相同，
  只是原本的 `next` 指標換成了索引。

**第二種解法 — 兩兩分治合併，不用堆積。** 先把串列兩兩合併，再把結果兩兩合併，
就像合併排序由下往上的各層。總共 log k 輪、每輪碰到全部 N 個節點，所以同樣是 O(N log k)，
額外空間是 O(1) 而不是堆積的 O(k)。把串列一個接一個併進同一個結果是陷阱寫法：
第一個串列會被重走 k 次，O(kN)。

<!--CODE-->

**不適用時** — 只有兩個來源：一般的雙指標合併就是 O(N)，不需要堆積。若答案只是
單一的第 k 個元素、而來源是矩陣的各列，對值域做二分搜尋可能勝過堆積（LC 378）。

**練習** — LC 23 ⭐ · LC 373 *（配對組成的虛擬格子）* · LC 378 *（把各列當來源）* ·
LC 632 *（同時追蹤 k 個頭的最大值）* · LC 1439 *（一列一列合併）* — 變形見
[heap_advanced.md § K 路合併變形](./heap_advanced.md#11-k-way-merge-variants)

<!-- dba22b624646 -->
### 模板 4：用雙堆積維護即時中位數 — LC 295 ⭐⭐⭐⭐⭐

**辨識** — 「資料串流的中位數」、「每次插入後回報中間值」、「平衡兩半」。
`add` / `query` 大量交錯呼叫，所以每次重新排序（O(n log n)）行不通。

**關鍵** — `small`（max-heap）存下半部、`large`（min-heap）存上半部；
**`small` 中每個值都 ≤ `large` 中每個值**，而且兩邊大小最多差一。
於是中位數可以直接從兩個堆頂以 O(1) 讀出來。

<!--CODE-->

<!--CODE-->

**複雜度**
- **每次 `addNum` 為 O(log n)**：固定次數的堆積 push 與 pop（`heappushpop` 只做一次 sift）。
  `findMedian` 讀兩個堆頂，O(1)。
- **空間 O(n)**：每個加入過的數字都在兩個堆積之一裡。

**試跑**

<!--CODE-->

**陷阱**
- 直接 push 進某一個堆積、只修正**大小**，會破壞順序：直接把 5 再把 1 加進
  `large`，`small` 的堆頂就可能大於 `large` 的。每個數字都要先繞經另一個堆積（或先和堆頂比較）。
- Java 的整數溢位：`(small.peek() + large.peek()) / 2.0` 會先把兩個 `int` 相加才轉型；
  兩個接近 `Integer.MAX_VALUE` 的值會溢位。先把其中一邊轉成 `double`。
- Python 的 `small` 存的是**負數**：讀 `-small[0]`，push `-num`。

**不適用時** — 視窗也會*移除*舊值（LC 480 滑動視窗中位數）：雙堆積需要延遲刪除
（[heap_advanced.md](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-)），
或改用有序容器。

**練習** — LC 295 ⭐ · LC 703 *（只要一個名次，一個堆積就夠）* · LC 480 *（+ 延遲刪除）* ·
LC 502 *（雙堆積當可用／鎖定池）* · LC 1825 *（三段切分）*

<!-- a96b16747d16 -->
### 模板 5：雙堆積 + 依索引過期 — LC 1438 ⭐⭐⭐⭐

**辨識** — **可變大小**的視窗，同時需要最大**與**最小：
「max − min ≤ limit 的最長子陣列」。n ≤ 10⁵ → O(n log n) 就夠了。

**關鍵** — 每筆資料都是 `(value, index)`，而且**當且僅當 `index < left` 時資料過期**。
`left` 前進時什麼都不刪；過期資料只有浮到堆頂時才會被 pop，所以比較時兩個堆頂
一定都在視窗內。要修正不合法的視窗，就把 `left` 跳到兩個違規極值中**較早**那一個之後 —
這是唯一能拆掉那組配對的移動。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**一次只縮一步** — 多數人第一次寫出來的形式，同樣正確：每次把 `left` 加 1，每一步之後都清理，而不是直接跳。
複雜度一樣是 O(N log N) — `left` 總共最多移動 N 次 — 只是迴圈多跑幾圈。

<!--CODE-->

**複雜度**
- **時間 O(N log N)**：每個索引在兩個堆積各被 push 一次、最多被 pop 一次，而清理用的
  `while` 只會 pop、從不掃描 — 所以它們所有的迭代加起來不會超過 push 的次數。
- **空間 O(N)**：過期資料要等浮上來才會離開，所以一個堆積可能存下全部 N 筆。

**試跑** — 以縮一步的形式跑 `nums = [8, 2, 4, 7]`、`limit = 4`。`.1` 那幾列就是延遲刪除：
元素只有在**位於堆頂***而且*索引 `< left` 時才會被 pop。

<!--CODE-->

**陷阱**
- **堆頂是 `h[0]`，絕不是 `h[-1]`。** `heapq` 的串列只有部分有序，所以 `h[-1]` 只是
  某個葉節點，不是最大值（見下方常見錯誤 #7）。這裡每次讀取都是 `max_h[0]` /
  `min_h[0]`。
- **兩個堆積都要清理，不能只清堆頂被移動的那個。** 在 `[8,2,4,7]` 上跳躍之後，
  *另一個*堆積的堆頂也可能落在新的 `left` 左邊；拿它來比較會得到錯的 `max − min`。
- **清理迴圈不需要 `heap and` 的防護。** 剛推入的元素索引是 `i >= left`，
  所以清理時任何一個堆積都不可能被清空。兩次 push 之後再清理一次什麼也不會做。

**不適用時** — 視窗是**固定大小**，或驅逐永遠是「最舊的先出」：兩個單調雙端佇列能以
O(N) 解 LC 1438 —
[monotonic_queue.md → Template 7](./monotonic_queue.md#template-7-two-deques--window-max-and-min-under-a-limit-lc-1438-)。
當驅逐規則是依值或某個任意條件時才用堆積，那是雙端佇列表達不了的。

**練習** — LC 1438 ⭐ · LC 1696 *（堆積存 `dp[j]`；當 `j < i − k` 時 pop）* ·
LC 1499 *（key 為 `y − x`；依**座標** `x_j − x_top > k` 過期）* · LC 480 *（中位數，雙堆積 + 延遲刪除）*

<!-- cdcaae23a8b1 -->
### 模板 6：結束時間的 Min-Heap — LC 253 ⭐⭐⭐⭐

**辨識** — 「最少需要幾間會議室／月台／組」、「同一時間最多有幾個區間重疊」。
n ≤ 10⁴–10⁵ 個區間 → 排序 + 堆積，O(n log n)。

**關鍵** — 依開始時間排序後，堆積存的是**使用中的會議室的結束時間**；堆頂就是
最早空出來的那間。開始時間在該結束時間或之後的會議就沿用這間（先 pop 再 push）；
否則就開一間新的。堆積的大小就是曾經需要的會議室數量。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n log n)**：排序，加上每個區間一次 push、最多一次 pop。
- **空間 O(n)**：最壞情況（所有會議都重疊）每個結束時間都在堆積裡。

**陷阱**
- `>=` 還是 `>`：`[[1,5],[5,10]]` 只需要**一間**，因為 5 點結束的會議會把會議室
  讓給 5 點開始的會議。寫成 `>` 會回傳 2。
- 依**結束**時間而不是開始時間排序會讓掃描失效：堆積必須依會議開始的順序看到它們。
- Java 比較器：`Integer.compare(a[0], b[0])`，絕不用 `a[0] - b[0]`（時間很大時會溢位）。

**不適用時** — 每個區間只花掉**其範圍中任選的一個時間單位**，而不是佔用整段
（LC 1353 Maximum Number of Events That Can Be Attended）。在那裡數重疊會
高估（`[[1,1],[1,1],[1,1]]` → 3，答案是 1）。要**一天一天**掃描，堆積存**開放中事件的
結束日**，每天參加最早結束的那一個：

<!--CODE-->

LC 1353 的 Python 版本，以及「跳日 vs 逐日掃描」的取捨分析：
[scanning_line_examples.md § LC 1353](./scanning_line_examples.md#5-maximum-number-of-events-that-can-be-attended--lc-1353-)。

**練習** — LC 253 ⭐ · LC 252 *（一間：到底有沒有任何重疊？）* · LC 1094 *（容量，不是
會議室）* · LC 2406 *（閉區間：只有 `start > heap[0]` 才 pop）* · LC 1353 *（每個事件一天）* · LC 1834 *（單執行緒
CPU：兩種排序）*

<!-- 533f553ad42e -->
### 模板 7：帶連續長度上限的貪婪建構 — LC 1405

**辨識** — 「建出最長、且沒有三個相同字母連在一起的字串」、「重新排列使相鄰兩個不相同」。
字母表很小（≤ 26，常常只有 3 個），計數很大。

**關鍵** — 永遠放**剩餘最多**的字母，因為它是最後才會沒地方放的那個；
當放它會打破連續長度上限時，改放**第二多**的，並把第一個**原封不動**放回去。
堆積只負責回答「誰剩最多」，所以最多只存整個字母表。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每次迴圈接上一個字母（最多 n = a + b + c 次），而每次堆積操作
  最多只有 3 筆，所以 O(log 3) = O(1)。
- **空間 O(1)**：堆積最多存整個字母表；輸出本身是 O(n)。

**陷阱**
- 在情況 1 消耗了 `first`：`(a, b, c) = (7, 1, 0)` 必須得到長度 5 的字串（`"aabaa"`）。
  在 `first` 被跳過時還把它減一，會少一個 `a`，回傳較短的字串。
- **只有**在被擋住的字母是最後一個時才跳出迴圈 — 在那裡寫 `continue` 會無窮迴圈。
- 上限就是回看檢查：`res[-1] == res[-2] == ch` 允許連續兩個（LC 1405）；LC 767
  只允許一個，所以檢查只要 `res[-1] == ch`。

**不適用時** — 相同字母之間的間隔是一般的 **k**：回看檢查會隨 k 變大，
用模板 8 的冷卻佇列更乾淨。

**練習** — LC 1405 · LC 767 *（上限為 1）* · LC 1054 *（數字版的 LC 767）* · LC 984 *（兩種字母，
不需要堆積）*

<!-- d2850c255bb9 -->
### 模板 8：Max-Heap + 冷卻佇列 — LC 358

**辨識** — 「相同字母／任務至少相隔 k」、「相同任務之間冷卻 n」。
字母表 ≤ 26，所以堆積大小是 O(1)。

**關鍵** — 用過的字母進入 FIFO 的**冷卻佇列**，而不是放回堆積。當佇列長度達到 k，
它最前面那個已經等了恰好 k − 1 個位置，可以放在下一個位置：把它移回堆積。
因此堆積提供的永遠只有**此刻**合法的字母。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：每次迭代放一個字母，每次堆積操作最多只有 26 筆。
- **空間 O(1)**：堆積和冷卻佇列各最多 26 個字母；輸出是 O(n)。

**陷阱**
- 把用過的字母直接放回堆積：`"aabbcc"`、k = 3 可能產生 `"abab…"`。
  強制間隔的正是冷卻佇列。
- 無解的輸入會以「堆積空了、佇列裡還有字母在等」結束：`"aaabc"`、k = 3
  必須回傳 `""` — 檢查 `len(res) == len(s)`，而不只是「迴圈結束了」。
- k = 0 或 1 代表沒有限制：在建任何東西之前就回傳 `s`（k = 0 的佇列永遠不會放行）。
- Java 版的比較器會讀 `freq[]`，所以要在字母離開堆積**之後**才更新 `freq[idx]`，
  絕不能在它還在堆積裡時改。

**不適用時** — 只問排程的**長度**（LC 621 Task Scheduler）：答案是
`max(n_tasks, (maxCount − 1) × (cooldown + 1) + numberOfTasksWithMaxCount)`，O(n)、不需要
堆積（[heap_examples.md § LC 621](./heap_examples.md#task-scheduler--lc-621-)）。只有需要實際順序時才用這個模板模擬。

**練習** — LC 358 ·
LC 1953 *（只問「做不做得到？」）* · LC 767 *（k = 2）* · LC 621 ⭐ *（數閒置格；或用公式）*

<!-- b046d01541f9 -->
## 語言 API

完整參考 — 每一個 `heapq` 呼叫及其輸出、peek 規則、偏序陷阱：
[heap_language_apis.md](./heap_language_apis.md)。

| 你需要什麼 | Python `heapq` | Java `PriorityQueue` |
|---|---|---|
| Min-heap | `h = []` | `new PriorityQueue<>()` |
| Max-heap | push 取負的鍵值：`heappush(h, -v)` | `new PriorityQueue<>(Collections.reverseOrder())` |
| 從串列建堆 — **O(N)**，不是 O(N log N) | `heapq.heapify(lst)` | `new PriorityQueue<>(collection)` |
| Push | `heapq.heappush(h, v)` | `pq.offer(v)` |
| Pop 堆頂 | `heapq.heappop(h)` | `pq.poll()` |
| **Peek** 堆頂 — O(1) | `h[0]` — **沒有 `peek()`** | `pq.peek()` |
| 先 pop 再 push（只做一次 sift） | `heapq.heapreplace(h, v)` | `pq.poll(); pq.offer(v);` |
| 先 push 再 pop（只做一次 sift） | `heapq.heappushpop(h, v)` | `pq.offer(v); pq.poll();` |
| 最大／最小的前 k 個 | `heapq.nlargest(k, it)` / `nsmallest` | 大小為 k 的 min-／max-heap，最後倒出來 |
| 判斷是否為空 | `if h:` | `pq.isEmpty()` |
| 自訂順序 | 元組鍵值，或在類別上定義 `__lt__` | comparator lambda |

**三條規則可以避開大部分堆積的 bug**

1. **只有索引 `0` 有意義。** `h[1]`、`h[-1]`，以及走訪 Java 的 `PriorityQueue`，得到的都是
   *偏序*，不是排序好的順序。
2. **比較器要用 `Integer.compare(a, b)` / `Long.compare(a, b)`，絕不要用 `a - b`** — 遇到很大的
   數或負數時減法會溢位。
3. **守住空的情況。** `h[0]` 會丟 `IndexError`；Java 的 `peek()` 回傳 `null`，而
   `element()` 會拋例外。在 `while` 條件裡把是否為空的判斷放**最前面**，才能短路。

<!--CODE-->

<!-- ca0675092699 -->
## 練習路線

| 階段 | 題目（易 → 難） | 增加了什麼 |
|---|---|---|
| **核心** ⭐ | LC 215 · LC 347 · LC 253 · LC 621 · LC 23 · LC 295 | 模板 1、2、6、8、3、4，各一次 |
| **練習** | LC 703 · LC 1046 · LC 973 · LC 692 · LC 767 · LC 1438 · LC 378 | 跨呼叫保留的堆積、模擬、推導出的 key、反向決勝、連續長度上限、依索引過期、把矩陣當 k 個來源 |
| **延伸** | LC 1353 · LC 502 · LC 480 · LC 632 · LC 358 | 每個事件一天、兩個池、還會刪除的中位數、k 個頭加上它們的最大值、冷卻佇列 |

<!-- ea4fa3974f42 -->
## 總結與快速參考

<!-- 7900334ee0be -->
### 複雜度速查

| 操作 | 二元堆積 | 有序陣列 | 平衡 BST |
|---|---|---|---|
| 從 n 個元素建構 | **O(n)**（heapify） | O(n log n) | O(n log n) |
| 插入 | O(log n) | O(n) | O(log n) |
| 刪除最小／最大 | O(log n) | O(1) | O(log n) |
| 讀取最小／最大 | O(1) | O(1) | O(log n) |
| 搜尋／刪除任意元素 | O(n) | O(log n) / O(n) | O(log n) |
| 合併兩個結構 | O(n + m) | O(n + m) | O(n + m) |
| 空間 | O(n) | O(n) | O(n) |

面試時值得主動講出來的推論：

- **沒有 decrease-key，也不能移除任意元素。**
  [heap_advanced.md](./heap_advanced.md) 裡的所有技巧，存在的理由就是為了繞開這件事。
- **大小為 k** 的堆積把 `O(N log N)` 變成 `O(N log k)`，把 `O(N)` 空間變成 `O(k)`。
- *另一端*的極值是 O(n)：min-heap 對它的最大值提供不了任何廉價資訊。

<!-- c3d0316b51ed -->
### 常見模式與技巧

<!-- 653ade273e41 -->
#### **Python 的 Max Heap（用取負數）**
<!--CODE-->

<!-- b8cd10836827 -->
#### **搭配自訂物件的堆積**
<!--CODE-->

<!-- 3621e6f9fd6a -->
#### **平手時的決勝**
<!--CODE-->

<!-- a146bf549fa3 -->
### 常見錯誤與提示

**🚫 常見錯誤**

1. **堆積方向搞反。** 「第 k **大**」用大小為 k 的 min-heap；「第 k **小**」用大小為 k 的
   max-heap。動手寫之前先把不變量唸出來：*「堆積裡放的是目前看過最大的 k 個，所以堆頂就是答案。」*
2. **忘記負回去** — 在 Python 用取負來假裝 max-heap 時，push *和* 讀取兩邊都要處理。
3. **讓堆積無限長大**：在第 k 個元素的題目裡，一旦 `len(h) > k` 就要 pop，否則你白白付了
   `O(N log N)`。
4. **雙堆積系統失衡。** *每一次*插入之後都要重新確立 `|len(small) - len(large)| <= 1`，
   而不是等到要讀中位數時才做。
5. **Java 裡用 `a - b` 當比較器** — 會溢位。請用 `Integer.compare` / `Long.compare`。
6. **在寫入時清理過期資料。** 延遲刪除是在**讀取**時清理**堆頂**，用 `while`（過期資料可能疊好幾筆），
   而且要加上空值守衛。
7. **讀 `h[1]` / `h[-1]`** 以為會拿到第二小或最大值。堆積只有偏序。

**✅ 最佳實務**

1. 對既有串列用 `heapify` — O(N) 勝過 N × O(log N)。
2. 反正都要換掉堆頂時，就用 `heapreplace` / `heappushpop`（只做一次 sift）。
3. push 元組時把**第一個**元素當排序鍵；加一個計數器來決勝平手。
4. 先考慮替代方案：靜態資料用排序、一次性的第 k 個用 quickselect、
   固定大小視窗用單調雙端佇列、真的需要任意刪除時用 `TreeMap` / `SortedList`。
5. 測 `k == 1`、`k == n`、空輸入，以及所有元素相同的情況。

<!-- 72eefa255a90 -->
### 面試提示

1. **先問清楚**：允許重複值嗎？`k > n` 可能嗎？是串流還是靜態資料？值會被更新嗎？
2. **說出模式名稱**（用上面的決策表），然後講出堆積的不變量 — 面試官在聽的就是這一句話。
3. **用 k 來表達複雜度**，而不只是用 n：`O(N log k)` 時間、`O(k)` 空間，正是堆積勝過排序的全部理由。
4. **預期會被追問**：「如果值會變動呢？」（延遲刪除）、「如果視窗是固定的呢？」（單調雙端佇列）、
   「如果要重複詢問第 k 個元素呢？」（把堆積留著）。

<!-- c0c3b27521ad -->
### 相關主題

- [Dijkstra.md](./Dijkstra.md) — 最經典的優先佇列演算法；它的 `if d > dist[u]: continue`
  就是一次延遲刪除
- [monotonic_queue.md](./monotonic_queue.md) — 固定大小視窗極值的 O(1) 替代方案
- [sort.md](./sort.md) — 堆積排序，以及什麼時候排序勝過堆積
- [streaming_algorithms.md](./streaming_algorithms.md) — 無界串流上的 top-k
- [greedy.md](./greedy.md) — 讓後悔類模式成立的交換論證
- [intervals.md](./intervals.md) / [scanning_line.md](./scanning_line.md) — 區間排程家族中
  不用堆積的另一半

<!-- 1ba475ca9223 -->
### 各語言注意事項

- **Python `heapq`** — 只有 min-heap；要 max-heap 就取負；`heappush`、`heappop`、`heapify`、
  `heapreplace`、`heappushpop`、`nlargest`、`nsmallest`、`merge`。
- **Java `PriorityQueue`** — 預設是 min-heap；要 max-heap 就用 `new PriorityQueue<>(Collections.reverseOrder())`
  或用 `Integer.compare` 建的比較器；`offer`、`poll`、`peek`、`size`。
- **C++ `priority_queue`** — 預設是 **max**-heap；要 min-heap 請用
  `priority_queue<int, vector<int>, greater<int>>`。

<!-- 1a51b06987b9 -->
### 決策表 — 該用哪一種堆積模式？

由上往下讀；第一個吻合的列就是該用的模式。`T1`–`T8` 連到上面的 card；其餘的列導向該列寫明的文件。

| 如果題目說… | 就用 | 堆積的形狀 | 經典 LC |
|---|---|---|---|
| 「第 k 大」／「第 k 小」 | [T1](#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) 第 k 個元素 | 第 k 大用大小為 k 的 **min**-heap；第 k 小用大小為 k 的 **max**-heap | 215、703、378、1492、1985、1337 |
| 「出現最多的前 k 個」／「最近的 k 個」 | [T2](#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) Top K 頻率 | `Counter` → 依計數／距離排序、大小為 k 的堆積 | 347、692、973、658、451、1338、1054 |
| 「合併 k 個有序…」或一個有序的格子 | [T3](#template-3-k-way-merge--lc-23-) 合併 K 個來源 | 存 `(value, sourceIdx, elemIdx)` 的 min-heap | 23、373、378、632、786、1439 |
| 串流的「中位數」／「兩半平衡」 | [T4](#template-4-two-heaps-for-a-running-median--lc-295-) 雙堆積系統 | 小的那一半用 max-heap + 大的那一半用 min-heap，兩者大小差不超過 1 | 295、480、1825 |
| 可變大小視窗且同時需要**最大與最小** | [T5](#template-5-two-heaps-with-index-expiry--lc-1438-) 視窗極值（2 個堆積） | 兩個存 `(value, index)` 的堆積，`index < left` 即為過期 | 1438、1696、1499 |
| 「最少幾間會議室／幾組」— 數重疊 | [T6](#template-6-min-heap-of-end-times--lc-253-) 區間排程 | 依起點排序，結束時間放 min-heap，堆積大小就是答案 | 253、2406、1094 |
| 「每單位時間一件事」+ 截止期限 | [T6](#template-6-min-heap-of-end-times--lc-253-) 區間排程（逐日掃描） | 依起點排序，**結束**日放 min-heap，參加最早截止的 | 1353、1834、1705 |
| 「不能連續 k 個相同」／「相隔 k」 | [T7](#template-7-greedy-build-with-a-run-length-cap--lc-1405) 貪婪 + 限制，或 [T8](#template-8-max-heap--cooldown-queue--lc-358) 優先佇列 + 冷卻佇列 | 依剩餘次數排序的 max-heap（+ 一條 k 格的延遲線） | 1405、767、621、358、1054 |
| 圖上的加權最短路徑 | Dijkstra — [Dijkstra.md](./Dijkstra.md) | 存 `(distance, node)` 的 min-heap | 743、787、1514、1631 |
| push 進去的值後來**變動或過期** | 延遲刪除 — [heap_advanced.md](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-) | 候選堆積 + 真值雜湊表；在讀取時清理**堆頂** | 3092、2349、2034、480、1825、239 |
| 「在每個 x，求所有覆蓋 x 的區間之最大值」 | 掃描 + 存活堆積 — [heap_advanced.md](./heap_advanced.md#2-sweep-line--max-heap-of-alive-intervals-) | 存 `(value, end)` 的 max-heap，`end <= x` 就驅逐 | 218、1851 |
| 「k 把梯子／k 次免費升級」+ 預算 | 有上限的後悔堆積 — [heap_advanced.md](./heap_advanced.md#3-bounded-regret-heap--keep-the-k-best-pay-for-the-rest-) | 上限為 k 的 min-heap；被擠掉的最小者改用預算支付 | 1642、1792 |
| 你要**到後來**才發現自己拿太多了 | 帶後悔的貪婪 — [heap_advanced.md](./heap_advanced.md#4-greedy-with-regret--undo-the-worst-past-decision-) | 先全拿，再 `poll()` 掉最差的決定 | 871、630、502 |
| 「編號最小的空椅／伺服器／座位」 | 資源池 — [heap_advanced.md](./heap_advanced.md#5-two-heaps-as-resource-pools-free-pool--busy-pool-) | 依編號的空閒堆積 + 依釋放時間的忙碌堆積 | 1942、1606、1801、2073、2102 |
| 目標式是 `sum(A) × max/min(B)` | 排序 + 固定大小堆積 — [heap_advanced.md](./heap_advanced.md#6-sort-by-one-criterion--fixed-size-heap-on-the-other) | 依 B 排序，A 用大小為 k 的堆積 | 857、1383 |
| 格子圖上代價是 minimax 或累積型 | 格子圖最佳優先 — [heap_advanced.md](./heap_advanced.md#7-min-heap-best-first-search-on-a-grid) | 從邊界初始化的 min-heap，每次展開最便宜的 | 407、778、1631、1368、675 |
| 每個格子跳到一個**範圍**的格子 | 格子圖範圍跳躍 — [heap_advanced.md](./heap_advanced.md#8-grid-shortest-path-with-range-jumps) | 每列一個、每行一個優先佇列，延遲 pop | 2617 |
| 「讓所有頻率互不相同」 | 頻率唯一化 — [heap_advanced.md](./heap_advanced.md#9-frequency-uniqueness--greedy--heap--hashset) | max-heap 逐次遞減，或用已使用頻率的集合 | 1647、1481 |
| **固定**大小視窗，而且只要最大值 | *不要用堆積* — [monotonic_queue.md](./monotonic_queue.md) | 單調雙端佇列，攤還 O(1) | 239、1425 |
| 只問一次第 k 個元素，之後沒有更新 | *不要用堆積* — quickselect，平均 O(N) | — | 215 |

