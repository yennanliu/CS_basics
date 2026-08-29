<!-- afb8deac2112 -->
# 堆積與優先佇列

> **範圍** — 堆積（heap，這個資料結構）以及它所實作的優先佇列（priority queue，這個抽象資料型別），Python 與 Java 兩種語言。本檔案原本拆成 `heap.md` + `priority_queue.md`，兩邊把同樣的題目各解了一遍。
> **另見** — *從本檔案拆出去的深入主題*：[heap_advanced.md](./heap_advanced.md) — 延遲刪除、掃描線的「存活」堆積、後悔貪婪、資源池配置器、格子圖最佳優先搜尋；[heap_examples.md](./heap_examples.md) — 完整解過的 LC 題庫，每題每語言一份標準解；[heap_language_apis.md](./heap_language_apis.md) — 完整的 `heapq` / `PriorityQueue` API 參考，以及「不 pop 就 peek」的規則。
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

<!-- 91f4447a4cbf -->
#### **模式 5：排程問題**
- **描述**：依優先權／時間安排任務或事件
- **範例**：LC 1353、502、630、621、1834 - Max Events、IPO、Course Schedule III、Task Scheduler、Single-Threaded CPU
- **模式**：用堆積依開始／結束時間或優先權維護事件
- **關鍵洞察（時間掃描 + 截止期限堆積）**：依視窗**起點**排序，讓元素按時間順序進堆積；堆積依視窗**終點**排序，這樣每個時間格都服務**最急迫（最早截止）**的元素；過期的堆頂用延遲刪除清掉
- **特徵**：*「每單位時間只能處理一件事」* + *「每件事有有效區間／截止期限」* → 見 [heap_examples.md § LC 1353](./heap_examples.md#7-maximum-number-of-events-that-can-be-attended--lc-1353)

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

<!-- 8ec8200fe604 -->
#### **模式 8：延遲刪除（堆積中的過期資料）** ⭐⭐⭐⭐⭐
- **描述**：堆積裡的值後來會被**更新／失效**，但二元堆積沒有
  「decrease-key」／「移除任意元素」的操作 — 所以我們直接 push 新值，把舊的留在裡面
- **範例**：LC 3092、2349、1834、480、1825、2336、621、1353、2406
- **模式**：**堆積 = 候選集合（可能過期）** + **雜湊表 = 真值來源** →
  只在*讀取時*清理堆頂，而且只清*到堆頂有效為止*
- **關鍵洞察**：你從來不去堆積裡搜尋過期資料。你只檢查 `heap[0]`，
  而每筆過期資料整段執行下來最多被 pop 一次 → 攤還 O(log n)
- **另見**：[heap_advanced.md § 延遲刪除](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-) · [heap_examples.md § LC 3092](./heap_examples.md#18-most-frequent-ids--lc-3092)

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

<!-- 86447518d233 -->
#### **模式 12：帶限制的貪婪字串／序列建構** ⭐⭐⭐⭐
- **描述**：貪婪地用出現次數最多的元素來建構字串／序列，但當加進去會違反限制時（例如連續 3 個相同字元）就先跳過。用 max-heap 讓當前次數最多的元素隨時可取。
- **範例**：LC 1405（Longest Happy String）、LC 767（Reorganize String）、LC 621（Task Scheduler）、LC 358（Rearrange String k Distance Apart）
- **模式**：依計數排序的 max-heap；每一步先試堆頂元素 — 如果違反限制，就暫時改用第二個元素，再把第一個放回去
- **關鍵技巧**：兩種情況的迴圈
  1. **情況 1 — 違反限制**：poll 出 `second`，接上去、計數減一、若 > 0 再放回；然後把 `first` 放回（它**沒有**被消耗）
  2. **情況 2 — 安全**：接上 `first`、計數減一、若 > 0 再放回
- **另見**：[Java 模板 7](#template-7-greedy-string-building-with-consecutive-constraint--lc-1405) — 連續上限（1 還是 2）就是 LC 767 和 LC 1405 的差別所在

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

<!-- c70804bc750d -->
### 參考資料
- [LeetCode Heap Learn Card](https://leetcode.com/explore/learn/card/heap/)
- [GeeksforGeeks Heap Guide](https://www.geeksforgeeks.org/heap-data-structure/)


- [Python heapq documentation](https://docs.python.org/3/library/heapq.html)
- [Java PriorityQueue](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html)
- [Priority Queue Implementation Notes](https://docs.python.org/3/library/heapq.html#priority-queue-implementation-notes)

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- eecca8c78367 -->
### 模板對照表

標準模板放在這裡，而且是**兩種語言都有**。tier-4 的特化版本被拆到
[heap_advanced.md](./heap_advanced.md) — 右邊那一欄說明每個模板去了哪裡。

| 模板 | 使用情境 | 複雜度 | 位置 |
|---|---|---|---|
| **通用堆積** | 一般的最小／最大存取 | push/pop 為 O(log N) | 下方 |
| **第 k 個元素** | 第 k 大／第 k 小 | O(N log k) | 下方 |
| **Top K 頻率** | 最常出現／最少出現 | O(N log k) | 下方 |
| **合併 K 個來源** | 合併有序陣列／串列 | O(N log k) | 下方 |
| **雙堆積系統** | 串流的中位數 | 每次操作 O(log N) | 下方 |
| **視窗極值（2 個堆積）** | 可變視窗且同時需要最大**與**最小 | O(N log N) | 下方 |
| **區間排程** | 會議室、每天一個事件 | O(N log N) | 下方 |
| **貪婪 + 限制** | 建構不含連續 k 個相同字元的字串 | O(N log Σ) | 下方 |
| **優先佇列 + 冷卻佇列** | k 距離／任務排程 | O(N log Σ) | 下方 |
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

<!-- b750a12bfd7c -->
### 通用堆積模板
<!--CODE-->

<!--CODE-->

<!-- 28be47728840 -->
### 各模式專用模板

<!-- 5a84ff47d116 -->
#### **1. 第 k 個元素模板**

**💡 關鍵洞察：**
- **`第 k 小的元素` = 大小為 k 的 Max PQ 中最大的那個**
  - 用大小為 k 的 **max heap** 找第 k 小
  - max heap 的根（peek）就是第 k 小的元素
  - 為什麼？因為只留下最小的 k 個元素；其中最大的那個就是整體第 k 小

- **`第 k 大的元素` = 大小為 k 的 Min PQ 中最小的那個**
  - 用大小為 k 的 **min heap** 找第 k 大
  - min heap 的根（peek）就是第 k 大的元素
  - 為什麼？因為只留下最大的 k 個元素；其中最小的那個就是整體第 k 大

<!--CODE-->

**這個模板的變形**（同樣的大小為 k 的不變量，只是*比較器*不同）：

| LC | 題目 | 變化點 |
|----|---------|-----------|
| 1985 | Find the Kth Largest Integer in the Array | 元素是**數字字串** → 預設的字典序是錯的。要用 `(len, string)` 比較：字串越長數字越大，長度相同再退回字典序。大小為 k 的 min-heap，答案 = `heap[0]`。 |
| 1337 | The K Weakest Rows in a Matrix | push 元組 `(soldierCount, rowIndex)`，讓平手時以列索引決勝；維持大小為 k 的 max-heap，最後讀出來。 |

<!--CODE-->

<!--CODE-->

<!-- 7e075aeef488 -->
#### **2. Top K 頻率模板**
<!--CODE-->

**這個模板的變形**（先計數，再讓堆積替計數排序）：

| LC | 題目 | 變化點 |
|----|---------|-----------|
| 451 | Sort Characters By Frequency | 一樣是 `Counter` + **max heap**，但*輸出*的是 `char * freq` 而不只是 key。O(N) 的替代解法是桶排序。 |
| 1338 | Reduce Array Size to The Half | 計數的 max heap；不斷 pop 並累加，直到 `removed >= n/2`；答案 = pop 的次數。貪婪 = 永遠刪掉出現最多的那個值。 |
| 1405 | Longest Happy String | 依**剩餘次數**排序的 max heap + 一個「上次用過的字母」的檢查 — 形狀跟 LC 767 Reorganize String 一樣，但這裡允許同一個字母**連續兩次**（`aab` 合法，`aaa` 不合法）。 |
| 1054 | Distant Barcodes | 距離為 2 的 LC 767：依剩餘次數排序的 max heap，先填偶數索引再填奇數索引。 |

<!-- 5bfb951a1c2a -->
#### **3. 合併 K 個來源模板**
<!--CODE-->

**變形** — 合併*巢狀*來源（LC 1439）或*虛擬*格子（LC 373 / 378），以及
LC 632「最小覆蓋範圍」的變化：[heap_advanced.md § K 路合併變形](./heap_advanced.md#11-k-way-merge-variants)。

<!-- b5e42ed15154 -->
#### **4. 雙堆積系統模板（中位數）**
<!--CODE-->

<!-- 6ab03d0b819f -->
#### **5. 滑動視窗極值 — 兩個堆積 + 依索引過期** ⭐⭐⭐⭐

**核心想法**

雙端佇列可以 O(1) 求滑動視窗最大值，但它只能追蹤**一個**極值，而且只適用**固定大小**的視窗。當視窗是**可變大小**，或你需要**同時取得最大與最小**時，就用兩個堆積並依**索引**讓資料過期：

<!--CODE-->

`left` 前進時什麼都不用刪 — 只有當資料浮到堆頂時才會被丟掉。

**實例演練 — LC 1438 Longest Continuous Subarray With Absolute Diff ≤ Limit**

視窗合法的充要條件是 `max(window) - min(window) <= limit`。兩個堆積都留著；當視窗不合法時，把 `left` 跳到**兩個違規極值中較早那一個之後**（這是唯一能破壞違規配對的方式），然後對兩個堆積都做延遲清理。

<!--CODE-->

<!--CODE-->

> **堆積 vs 單調雙端佇列**：LC 1438 也有 O(N) 的雙 deque 解法。當驅逐規則**不是**「最舊的先出」時（例如你是依值或依某個任意條件驅逐），就用堆積版本 — 單調雙端佇列表達不了這種規則。

**這個模板的變形**（同樣是「max-heap + 依索引／座標過期」的形狀）：

| LC | 題目 | 變化點 |
|----|---------|-----------|
| 1696 | Jump Game VI | `dp[i] = nums[i] + max(dp[j])`，其中 `i-k <= j < i`。存 `(dp[j], j)` 的 max-heap；讀取前先 pop 掉所有 `j < i-k` 的。堆積裡放的是 **DP 值**，不是原始輸入。 |
| 1499 | Max Value of Equation | 把 `y_i + y_j + |x_i - x_j|`（限制 `x_j - x_i <= k`、`i < j`）改寫成 `(y_i - x_i) + (y_j + x_j)`。存 `(y_i - x_i, x_i)` 的 max-heap；當 `x_j - x_top > k` 就 pop；過期依據是**座標**，不是索引。 |

<!-- e9e027157b5f -->
### Java 模板庫（`PriorityQueue`）

> 上面 5 個模板以 Python 為主。下面 8 個是同一片問題空間，改用 Java 的
> `PriorityQueue` 寫成。它們原本是獨立的 `priority_queue.md`；下表把每個 Java
> 模板和它的 Python 對應版本配起來，兩邊都能對照著看。

| Java 模板（下方） | Python 對應版本（上方） | 代表題 |
|---|---|---|
| 模板 1：Top K Elements | 1. 第 k 個元素模板 | LC 215 |
| 模板 2：K 路合併 | 3. 合併 K 個來源模板 | LC 23 |
| 模板 3：雙堆積（中位數） | 4. 雙堆積系統模板 | LC 295 |
| 模板 4：區間排程 | *（這裡沒有 Python 對應版本 — 見下方 LC 1353 的指引）* | LC 253 |
| 模板 5：圖的最短路徑 | — 見 [Dijkstra.md](./Dijkstra.md) | LC 743 |
| 模板 6：自訂優先權 | 2. Top K 頻率模板 | LC 347 |
| 模板 7：貪婪字串建構 | *（沒有 Python 對應版本）* | LC 1405 |
| 模板 8：優先佇列 + 冷卻佇列 | *（沒有 Python 對應版本）* | LC 358 |

<!-- a94451f1680f -->
#### Java 模板對照表
| 模板類型 | 使用情境 | 堆積種類 | 複雜度 | 什麼時候用 |
|---------------|----------|-----------|------------|-------------|
| **Top K Elements** | 找最大／最小的 K 個 | Min／Max heap | O(n log k) | 固定 K 的挑選 |
| **K 路合併** | 合併有序串列 | Min heap | O(n log k) | 多個有序來源 |
| **雙堆積** | 求中位數 | Min + Max heap | O(log n) | 串流中位數／百分位 |
| **區間排程** | 處理區間 | Min heap | O(n log n) | 會議室、事件 |
| **圖的最短路徑** | Dijkstra | Min heap | O(E log V) | 加權圖 |
| **自訂優先權** | 複雜的排序規則 | 自訂比較器 | O(log n) | 多重條件排序 |
| **貪婪 + 限制** | 建構避免連續重複的字串 | Max heap | O(n log k) | Reorganize／happy string |
| **優先佇列 + 冷卻佇列** | 相隔 k 距離的排程 | Max heap + Queue | O(n log k) | Rearrange k-dist、task scheduler |

<!-- 85c56878cedf -->
### 模板 1：Top K Elements 模式 — LC 215
<!--CODE-->

<!--CODE-->

<!-- 06242b15cea8 -->
### 模板 2：K 路合併模式 — LC 23
<!--CODE-->

<!--CODE-->

**這個骨架的變形**（LC 632、355、373、378、1439）：
[heap_advanced.md § K 路合併變形](./heap_advanced.md#11-k-way-merge-variants)。

<!-- 66bbfe4ffbe7 -->
### 模板 3：雙堆積模式（求中位數）— LC 295

> Python 版本：上方的 [4. 雙堆積系統模板](#4-two-heap-system-template-median) — 同樣的
> 雙堆積不變量，只是改用 `heappushpop` 而不是顯式重新平衡。

<!--CODE-->

<!-- c06ae600c7c6 -->
### 模板 4：區間排程模式 — LC 253
<!--CODE-->

<!--CODE-->

**變形：Maximum Number of Events That Can Be Attended（LC 1353）** — 變化點：堆積裡放的是**目前開放中事件的結束日**，而且你是*一天一天*掃描（不是一個區間一個區間），每天參加**最早結束**的那個事件。LC 253 數的是同時重疊的區間數；LC 1353 則是每天貪婪地*挑一個*。

<!--CODE-->

LC 1353 的 Python 版本，以及「跳日 vs 逐日掃描」的取捨分析：
[heap_examples.md § LC 1353](./heap_examples.md#7-maximum-number-of-events-that-can-be-attended--lc-1353)。

<!-- 33c16e4061ea -->
### 模板 5：圖的最短路徑（Dijkstra）— LC 743

這屬於 [Dijkstra.md](./Dijkstra.md) 的範圍。優先佇列裡就只是 `(distance, node)`，外加一個
延遲刪除的守衛 `if d > dist[u]: continue`；在這裡重寫一遍並不會多講出什麼跟堆積有關的東西。

<!-- bac6a585adf1 -->
### 模板 6：自訂優先權模式
<!--CODE-->

<!--CODE-->

<!-- df6b5cc78fae -->
### 模板 7：帶連續限制的貪婪字串建構 — LC 1405
<!--CODE-->

**重點觀察：**
- 永遠貪婪地挑出現次數最多的（max-heap 保證這件事）。
- 當快要違反限制時，**暫時跳過**堆頂元素改用下一個 — 然後把堆頂**原封不動放回去**。
- `first` 只有在情況 2 才會被消耗；情況 1 會原樣重新插回。
- 只要改一下回看視窗的檢查，就能套用到任何「最多連續 K 個」的限制。

**變形：Reorganize String（LC 767）— 最多連續 1 個**
<!--CODE-->

<!-- d868deabe8e5 -->
### 模板 8：優先佇列 + 冷卻佇列（k 距離排程）— LC 358
<!--CODE-->

**重點觀察：**
- 冷卻佇列就是一條**長度為 k 的固定大小延遲線**。當它的大小達到 k 時，最舊的那筆剛好等滿 k 步，可以放行了。
- 如果 `pq` 空了但 `cooldown` 還有東西 → 表示放不下任何字元 → 回傳 `""`。
- **另一種冷卻寫法**：存 `[char, releaseTime]`，然後檢查 `cooldown.peek()[1] == time` 而不是檢查佇列大小。兩種寫法等價。
- 這個模式可以推廣：LC 621（Task Scheduler）用的是同一個想法，只是改成數閒置格；LC 767 則是 k=2 的特例。

**比較：冷卻佇列 vs 跳過再交換（模板 7）**
| 面向 | 冷卻佇列（模板 8） | 跳過再交換（模板 7） |
|--------|----------------------------|---------------------------|
| 最適合 | 可變的 k、較大的 k | 較小的 k（k=2 或 k=3） |
| 機制 | 用明確的佇列延後重新進場 | 回看視窗 + 交換 |
| 無解偵測 | cooldown 非空時 `pq.isEmpty()` | 不適用（沒有選項時就停止） |
| 較適用於 | LC 358、LC 621 | LC 1405、LC 767 |

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

<!-- ea4fa3974f42 -->
## 總結與快速參考

<!-- f9008d705012 -->
### 決策表 — 該用哪一種堆積模式？

由上往下讀；第一個吻合的列就是該用的模式。

| 如果題目說… | 就用 | 堆積的形狀 | 經典 LC |
|---|---|---|---|
| 「第 k 大」／「第 k 小」 | 第 k 個元素 | 第 k 大用大小為 k 的 **min**-heap；第 k 小用大小為 k 的 **max**-heap | 215、703、378、1492、1985、1337 |
| 「出現最多的前 k 個」／「最近的 k 個」 | Top K 頻率 | `Counter` → 依計數／距離排序、大小為 k 的堆積 | 347、692、973、658、451、1338、1054 |
| 「合併 k 個有序…」或一個有序的格子 | 合併 K 個來源 | 存 `(value, sourceIdx, elemIdx)` 的 min-heap | 23、373、378、632、786、1439 |
| 串流的「中位數」／「兩半平衡」 | 雙堆積系統 | 小的那一半用 max-heap + 大的那一半用 min-heap，兩者大小差不超過 1 | 295、480、1825 |
| 可變大小視窗且同時需要**最大與最小** | 視窗極值（2 個堆積） | 兩個存 `(value, index)` 的堆積，`index < left` 即為過期 | 1438、1696、1499 |
| 「最少幾間會議室／幾組」— 數重疊 | 區間排程 | 依起點排序，結束時間放 min-heap，堆積大小就是答案 | 253、2406、1094 |
| 「每單位時間一件事」+ 截止期限 | 區間排程（逐日掃描） | 依起點排序，**結束**日放 min-heap，參加最早截止的 | 1353、1834、1705 |
| 「不能連續 k 個相同」／「相隔 k」 | 貪婪 + 限制，或優先佇列 + 冷卻佇列 | 依剩餘次數排序的 max-heap（+ 一條 k 格的延遲線） | 1405、767、621、358、1054 |
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
