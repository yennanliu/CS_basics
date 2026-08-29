<!-- d0cd2d660cc2 -->
# 堆積(heap) — LC 題解範例

> **範圍** — 堆積 / 優先佇列(priority queue)家族的題解存放處：每題每種語言一份標準解，把推理過程、追蹤與陷阱完整寫出來。
> **另見** — *主文件*：[heap.md](./heap.md) — 這些範例所實作的標準模板與模式選擇指南。*從同一份檔案拆出去的鄰近文件*：[heap_advanced.md](./heap_advanced.md) — 延遲刪除(lazy deletion)、反悔貪婪(regret greedy)、掃描線，以及較少見的堆積模板；[heap_language_apis.md](./heap_language_apis.md) — 完整的 `heapq` / `PriorityQueue` API 參考。

<!-- b869f594d6e2 -->
## LeetCode 題目清單

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

<!-- 05aba6161da5 -->
## 總覽

下面每一題每種語言都只解**一次**。同一個想法若有通用形式，模板會放在
[heap.md](./heap.md)（標準）或 [heap_advanced.md](./heap_advanced.md)
（罕見 / 困難），範例只做連結，不重複贅述。

<!-- fbb3ef24fd8e -->
### 關鍵性質
- **複雜度**：以註解寫在每份解法的最上方
- **核心想法**：每題每種語言一份標準解；只有在*複雜度不同*或有*獨特技巧*時才會出現
  第二種寫法，而且會明確說明理由
- **使用時機**：等你能默寫出對應的模板之後 — 這些是你有能力之後才會遇到的變化題

<!-- 598a9bfd2a3c -->
## LC 範例

<!-- 0eeb6ceddca1 -->
### 1) Kth Largest Element in a Stream — LC 703
<!--CODE-->

<!-- 25779f9a3c1e -->
### 2) Ugly Number II — LC 264
<!--CODE-->

<!-- 4158286d087c -->
### 3) Find Median from Data Stream — LC 295
<!--CODE-->

<!-- c80fb09bdc2c -->
### 4) Minimum Cost to Connect Sticks — LC 1167
<!--CODE-->

<!-- 5ba7d4d05d9c -->
### 5) The kth Factor of n — LC 1492
<!--CODE-->

<!-- 7c48623282d7 -->
### 6) Least Number of Unique Integers after K Removals — LC 1481
<!--CODE-->

<!-- fb0f75b1d4b4 -->
### 7) Maximum Number of Events That Can Be Attended — LC 1353

<!--CODE-->

**為什麼是對 `end day` 做貪婪（而不是 `start day`、也不是持續天數）？**

如果今天有兩個活動都可以參加，選**結束日較早**的那一個永遠不會比較差：結束較晚的那個至少還剩下一樣多的天數可以安排。依開始日排序只控制*活動什麼時候進入堆積*；堆積依結束日排序，則控制*今天這一天要花在哪個活動上*。

<!--CODE-->

**模式：時間掃描 + 截止日最小堆積（最早截止優先，earliest-deadline-first）**

| 步驟 | 資料結構 | 用途 |
|------|---------------|---------|
| 依開始日排序 | 陣列 + 指標 `i` | 活動照時間順序變成可參加；每個只被推入一次 |
| 追蹤可參加的活動 | `pq` = **結束日**的最小堆積 | `pq[0]` = 目前仍有效、最急迫的截止日 |
| 丟掉過期的 | `while pq[0] < day: pop` | **延遲刪除** — 堆積無法移除任意位置的元素 |
| 用掉一個時段 | pop `pq` + `day += 1` | 每天一個活動，貪婪地挑最急迫的 |
| 跳過空檔 | `if not pq: day = events[i][0]` | 消掉 O(天數範圍) 這個因子 |

**辨識這個模式的特徵：***「每一單位時間只能服務一個項目」* + *「每個項目有一段有效期 / 截止日」* → 依區間**開始**排序，堆積依區間**結束**排序。

**相似題目：**

| LC # | 題目 | 共通模式 | 關鍵差異 |
|------|---------|---------------|----------------|
| 1751 | Max Number of Events That Can Be Attended II | 輸入的活動資料相同 | 活動佔用**整段**區間並帶有價值 → DP + 二分搜尋，**不是**堆積 |
| 621 | Task Scheduler | 時間掃描 + 堆積，每個時刻一個時段 | 依頻率的最大堆積 + 冷卻佇列（見 [§ 17](#17-task-scheduler--lc-621)） |
| 253 | Meeting Rooms II | 依開始排序，結束時間的最小堆積 | 數的是*同時進行*的區間數，不是挑出一個子集合 |
| 2406 | Divide Intervals Into Min Number of Groups | 依開始排序，結束時間的最小堆積 | 與 253 相同，只是換成區間分組的說法（見 [§ 15](#15-divide-intervals-into-minimum-number-of-groups--lc-2406)） |
| 630 | Course Schedule III | 依截止日貪婪 + 堆積 | 最大堆積做**替換**：超時就丟掉耗時最長的課 |
| 502 | IPO | 依一個鍵排序，依另一個鍵建堆積 | 雙堆積貪婪（資本 → 利潤的最大堆積） |
| 871 | Min Number of Refueling Stops | 把可達的選項推進去，卡住時再貪婪地取最好的 | 油量的最大堆積，只有卡住時才 pop（見 [§ 13](#13-minimum-number-of-refueling-stops--lc-871)） |
| 1834 | Single-Threaded CPU | 推進時間、推入已抵達的任務、取出最好的 | 以 (處理時間, 索引) 建最小堆積；時間直接跳到下一個抵達時刻 |
| 767 | Reorganize String | 每個位置一個時段，用堆積貪婪挑選 | 依剩餘數量的最大堆積 + 上次使用過的字元擋著 |

<!-- 5b7ffea08146 -->
### 8) Maximum Frequency Stack — LC 895
<!--CODE-->

<!-- fd30120311c5 -->
### 9) Find K Pairs with Smallest Sums — LC 373

<!--CODE-->

<!-- 6895d2ee8d46 -->
### 10) Kth Smallest Element in a Sorted Matrix — LC 378

<!--CODE-->

<!-- c77b36b703a2 -->
### 11) Minimum Deletions to Make Character Frequencies Unique — LC 1647

<!--CODE-->

<!-- 130b4e60be66 -->
### 12) Maximum Performance of a Team — LC 1383

<!--CODE-->

<!-- f7639dfa4604 -->
### 13) Minimum Number of Refueling Stops — LC 871

<!--CODE-->

<!-- af84cdd698e2 -->
### 14) Minimum Number of Visited Cells in a Grid — LC 2617

<!--CODE-->

<!-- b7b9a33c0fa2 -->
### 15) Divide Intervals Into Minimum Number of Groups — LC 2406

<!--CODE-->

<!-- 8c05ed75b270 -->
### 16) Minimize Deviation in Array — LC 1675

<!--CODE-->

<!-- 0a5e5356a9a1 -->
### 17) Task Scheduler — LC 621

<!--CODE-->

**為什麼要檢查 `remaining_cnt < 0`？**

任務的數量以**負數**儲存，用來假裝成最大堆積。
- `-3 + 1 = -2` → 還剩 2 份 → 放進冷卻佇列。
- `-1 + 1 =  0` → 任務用完了 → 直接丟掉，不再排回佇列。

**模式：最大堆積 + 冷卻佇列（貪婪排程）**

| 步驟 | 資料結構 | 用途 |
|------|---------------|---------|
| 挑下一個任務 | `max_heap`（數量取負） | 永遠優先安排出現次數最多的任務 |
| 強制冷卻 | `cooling_queue` deque | 把任務押住，直到經過 `time + n` |
| 重新啟用 | 從佇列取出 → 推回堆積 | 任務重新變成可安排 |

**相似題目：**

| LC # | 題目 | 共通模式 |
|------|---------|---------------|
| 767 | Reorganize String | 最大堆積，依頻率交錯排列 |
| 1353 | Maximum Number of Events That Can Be Attended | 貪婪 + 依截止日的堆積 |
| 502 | IPO | 雙堆積貪婪（利潤 + 資本） |
| 1675 | Minimize Deviation in Array | 最大堆積 + 貪婪縮小 |
| 295 | Find Median from Data Stream | 雙堆積系統 |

<!-- 92c61ab96b74 -->
### 18) Most Frequent IDs — LC 3092

> 模式：**延遲刪除(Lazy Deletion)** — 通用形式見 [heap_advanced.md § Lazy Deletion](./heap_advanced.md#1-lazy-deletion--heap--hashmap-of-truth-)。
> 參考：`leetcode_python/Heap/most-frequent-ids.py`

**題目**：`nums[i]` 是一個 ID，`freq[i]` 表示加入該 ID 這麼多份（若為負數則是移除）。
每一步之後，回報**出現次數最多**的那個 ID 的數量（集合為空則回報 0）。

**為什麼單純用堆積會失敗**：一個 ID 的數量會隨時間*改變*。當 `2` 的數量從 3 掉到 0 時，
`(-3, 2)` 這筆資料埋在堆積裡的某處，我們沒辦法在 O(log n) 內找到它。

**核心想法**

<!--CODE-->

每一步：更新 `c_map`、推入新的 `(-freq, id)`，然後**只從堆頂做延遲刪除，
直到堆頂與真實值一致為止**。

<!--CODE-->

注意步驟 2：我們只 pop 掉**剛好一筆** — 也就是擋住答案的那一筆 — 其他過期的資料
全都原地留著。這就是整個技巧的全部。

<!--CODE-->

<!--CODE-->

**邊界情況**
- 集合變成空的（`nums=[5,5,3], freq=[2,-2,1]` → `[2,0,1]`）：當 `5` 的數量歸零時
  我們推入 `(0, 5)`。因為 `-0 == 0 == c_map[5]`，這筆是**有效的**、會留下 — 答案
  正確地是 `0`。（推入數量為零的項目無害，而且讓檢查邏輯保持一致。）
- 數量可能達到 `n * max(freq) = 1e10` → **Java 要用 `long`**，`int` 會溢位。

**相似題目（延遲刪除）**

| 題目 | LC # | 什麼會過期 | 過期判斷 | 難度 |
|---------|------|-----------------|-----------|------------|
| Most Frequent IDs | 3092 | 某個 ID 的頻率改變了 | `heapVal != map[id]` | Medium |
| Design a Number Container System | 2349 | 某個索引被指派了新的數字 | `heapIdx` 目前的數字 != 這個數字 | Medium |
| Single-Threaded CPU | 1834 | —（純粹的可用性掃描） | 指標 + 時間閘門 | Medium |
| Sliding Window Median | 480 | 元素滑出視窗了 | `val in removed` 計數器 | Hard |
| Finding MK Average | 1825 | 元素離開了最後 m 筆的串流 | delete-set / multiset | Hard |
| Sliding Window Maximum | 239 | 索引掉出視窗了 | `pq[0].idx <= i - k` | Hard |
| Maximum Number of Events | 1353 | 活動的截止日已過 | `pq[0] < day` | Medium |
| The Number of Beautiful Subsets / Seat Manager | 1845 | 座位被預訂 / 取消預訂 | 重用已釋出 id 的最小堆積 | Medium |
| Process Tasks Using Servers | 2073 | 伺服器忙碌到時刻 t | 雙堆積 + 時間閘門 | Medium |
| Minimum Number of Visited Cells in Grid | 2617 | 格子已經定案 | 每列 / 每行一個 PQ + 延遲 pop | Hard |
| Task Scheduler II / Dijkstra (743, 1631, 778) | — | 後來找到了更短的路徑 | `d > dist[node]: continue` | Medium |

> 💡 **Dijkstra 是最有名的延遲刪除演算法。** 那行經典的
> `if d > dist[u]: continue` *就是*一次延遲刪除 — 它直接丟掉一筆過期的距離資料，
> 而不是對堆積做 decrease-key。同樣的模式，換件衣服而已。


> 下面五個範例來自舊的 `priority_queue.md`，是**以 Java 為主**的 — 對應的
> Python 版本放在 [heap.md](./heap.md) 的模板裡。

<!-- b541ef3e6e63 -->
### 19) K Closest Points to Origin — LC 973
<!--CODE-->

<!-- c79dcf08ef13 -->
### 20) Reorganize String — LC 767
<!--CODE-->

<!-- 67d54a38d266 -->
### 21) Sliding Window Median — LC 480
<!--CODE-->

<!-- 806aa4c484ee -->
### 22) Sort Characters By Frequency — LC 451
<!--CODE-->

<!-- 5b070afa0524 -->
### 23) Last Stone Weight — LC 1046
<!--CODE-->

<!-- 2b6001fbce74 -->
## 總結與速查

| # | 題目 | LC | 呈現語言 | 模式（模板所在位置） |
|---|---------|----|----------------|------------------------------|
| 1 | Kth Largest Element in a Stream | 703 | Python | 第 K 個元素 — [heap.md](./heap.md#specific-pattern-templates) |
| 2 | Ugly Number II | 264 | Python | 堆積生成 + 去重集合 |
| 3 | Find Median from Data Stream | 295 | Python | 雙堆積 — [heap.md](./heap.md#specific-pattern-templates) |
| 4 | Minimum Cost to Connect Sticks | 1167 | Python | 重複「取兩個最小的」 |
| 5 | The kth Factor of n | 1492 | Python | 大小上限為 k 的最大堆積 |
| 6 | Least Number of Unique Integers after K Removals | 1481 | Python | Counter + 次數的最小堆積 |
| 7 | Maximum Number of Events That Can Be Attended | 1353 | Python | 時間掃描 + 截止日堆積 |
| 8 | Maximum Frequency Stack | 895 | Python | 依頻率分桶的堆疊（不需要堆積） |
| 9 | Find K Pairs with Smallest Sums | 373 | Java | 對虛擬網格做 K 路合併 |
| 10 | Kth Smallest Element in a Sorted Matrix | 378 | Java | 大小為 k 的最大堆積 |
| 11 | Minimum Deletions to Make Character Frequencies Unique | 1647 | Java | 頻率唯一性 — [heap_advanced.md](./heap_advanced.md) |
| 12 | Maximum Performance of a Team | 1383 | Java | 排序 + 固定大小堆積 — [heap_advanced.md](./heap_advanced.md) |
| 13 | Minimum Number of Refueling Stops | 871 | Java | 反悔貪婪 — [heap_advanced.md](./heap_advanced.md) |
| 14 | Minimum Number of Visited Cells in a Grid | 2617 | Java | 每列 / 每行 PQ + 延遲刪除 — [heap_advanced.md](./heap_advanced.md) |
| 15 | Divide Intervals Into Minimum Number of Groups | 2406 | Java | 區間排程 — [heap.md](./heap.md#template-4-interval-scheduling-pattern--lc-253) |
| 16 | Minimize Deviation in Array | 1675 | Java | 最大堆積 + 貪婪縮小 |
| 17 | Task Scheduler | 621 | Python | 最大堆積 + 冷卻佇列 |
| 18 | Most Frequent IDs | 3092 | Python + Java | 延遲刪除 — [heap_advanced.md](./heap_advanced.md) |
| 19 | K Closest Points to Origin | 973 | Java | 大小為 k 的最大堆積 |
| 20 | Reorganize String | 767 | Java | 貪婪組字串 |
| 21 | Sliding Window Median | 480 | Java | 兩個有序 multiset（`TreeMap`） |
| 22 | Sort Characters By Frequency | 451 | Java | Counter + 最大堆積（也可用桶排序） |
| 23 | Last Stone Weight | 1046 | Java | 最大堆積模擬 |
