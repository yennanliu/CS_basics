<!-- c422e9bf6924 -->
# Design — 完整範例

> **範圍** — [design.md](./design.md) 背後的解法檔案庫：二十題 LC「設計一個 X」的完整解，依每題被迫採用的結構組合分組，而不是依題號。
> **另見**：[design.md](./design.md) — 母文件：這些解法在演練的五大模式、五步驟方法與選型表；[design_patterns.md](./design_patterns.md) — 一致性雜湊、限流器與負載平衡，同一輪面試會問但不是 LC 題目；[ood_design.md](./ood_design.md) — LLD 關卡的類別建模與 SOLID；[iterator.md](./iterator.md) — 單獨談 iterator 的契約；[trie.md](./trie.md)、[heap.md](./heap.md)、[hash_map.md](./hash_map.md) — 被組合起來的個別結構。

<!-- b7355bc4a6c2 -->
## LeetCode 題目清單

- [Design](https://leetcode.com/problem-list/design/)
- [Data Stream](https://leetcode.com/problem-list/data-stream/)

<!-- 7a2d9cb15feb -->
## 總覽

這裡是 [design.md](./design.md) 的長尾。母文件留下五大結構模式、五步驟面試方法與選型表；這個檔案留下真正*套用*它們的設計，免得方法本身被 1,300 行的類別淹沒。

<!-- e4d92c4f5fb0 -->
### Key Properties
- **複雜度**：每個設計各自標註 — 每題的重點就是哪些操作是 O(1)、哪些是 O(log n)
- **核心想法**：底下每一個設計，都是「一個存資料的結構」加上「第二個結構，唯一任務是讓某個操作變快」
- **什麼時候用**：當你已經從題目讀出需要哪些操作，想看這組搭配從頭到尾怎麼寫出來

<!-- de4c8a6bd16b -->
## 快取與淘汰策略

<!-- f14c209586a9 -->
### 1) LRU Cache — LC 146 ⭐⭐⭐⭐⭐

<!--CODE-->

<!-- d4a2f633067d -->
### 2) LFU Cache — LC 460 ⭐⭐⭐⭐

<!--CODE-->

<!-- 18d4464152aa -->
### 3) All O(1) Data Structure — LC 432 ⭐⭐⭐⭐

<!--CODE-->

<!-- abf92bf70806 -->
### 4) Insert Delete GetRandom O(1) — LC 380 ⭐⭐⭐⭐

<!--CODE-->

<!-- 763b5a83cd10 -->
## 帶輔助狀態的堆疊

<!-- 7582a512bb95 -->
### 5) 用堆疊做頻率桶 — Max Frequency Stack, LC 895 ⭐⭐⭐⭐


**模式**：`HashMap<freq, Stack>` + `HashMap<value, freq>` 再加一個 `maxFreq` 計數器。跟 LC 432 一樣是「依計數分桶」的想法，但每個桶是**堆疊**，所以同分時由*最晚推入的*勝出。

**關鍵技巧**：`push` 時把值放進它**新**頻率的桶裡，**但不要從較低的桶移除它**。因此每個值會同時出現在桶 `1..f`，`pop` 之後前一份副本本來就躺在桶 `f-1` 裡 — 不需要任何清理。

**不變式**：`group[f]` 依推入順序，存放所有計數曾達到 `f` 的值；`maxFreq` 是最高的非空桶。

<!--CODE-->

<!--CODE-->

<!-- ea1f70cb1f0d -->
### 6) 堆疊 + 輔助狀態 — O(1) 取最小值與延遲遞增, LC 155 / LC 1381 ⭐⭐⭐⭐


**模式**：純堆疊沒辦法用 O(1) 回答聚合查詢 — 所以在每個元素旁邊順便推入**它底下那段前綴的答案**。因為堆疊只在頂端增減，當這個元素再度成為頂端時，存起來的答案一定還有效。

**不變式**：`stack[i].min == min(values[0..i])`，所以 `getMin()` 就只是讀堆頂的第二個欄位。

<!--CODE-->

<!--CODE-->

**變形 - 延遲遞增**（轉折：輔助欄位變成**要套用到底下所有元素的待處理差值**，`pop` 時往下推一層，把 O(k) 的批次更新變成 O(1)）

<!--CODE-->

<!--CODE-->

<!-- 9b8b342e95a1 -->
## 有序 Map、堆積與時間視窗

<!-- 4118bfd2a65c -->
### 7) 用有序 Map（TreeMap）做訂位／區間設計 — LC 715 / 729 / 731 / 732 / 2034 ⭐⭐⭐⭐⭐


**模式**：把區間存在**以起點為 key 的有序 map** 裡，所有查詢都用 `floorKey`（最大的 `<=` x 的 key）／`ceilingKey`（最小的 `>=` x 的 key）回答。這是 HashMap + LinkedList 在「時間軸上的區間查詢」這一側的對應物。

**Key Idea**：只有 2 個鄰居有影響。一個新區間 `[start, end)` **只可能**跟這兩個衝突
- 起點*在 `start` 或之前*的那個區間（`floorKey`），以及
- 起點*在 `start` 或之後*的那個區間（`ceilingKey`）。

**快速決策表**

| 目標 | 結構 | 例題 |
|------|-----------|---------|
| 完全不允許重疊（重複訂位） | 有序 map `start -> end`，檢查 2 個鄰居 | LC 729 |
| 允許最多 K 層重疊／回報最大重疊數 | 把有序 map 當成**差分／掃描線計數器**（起點 `+1`，終點 `-1`） | LC 731, LC 732 |
| 追蹤一組被覆蓋的範圍（新增／移除／查詢） | 存**已合併且互斥**區間的有序 map | LC 715 |
| 在動態 multiset 中找 `<=` / `>=` x 的最大值 | 有序 map `value -> count` | LC 2034 |

<!-- ed509f24a2a7 -->
#### 模板 A - 拒絕重疊（`floor` / `ceiling`）

**不變式**：map 裡永遠是**兩兩互斥**的區間，以起點為 key。

<!--CODE-->

<!--CODE-->

<!-- e7d246039da2 -->
#### 模板 B - 掃描線差分計數（最大重疊）

**轉折**：不要存區間，改在有序 map 裡存**起點 `+1`／終點 `-1`**。把 key **依排序順序**做前綴和，就是那個時刻的活躍訂位數。

<!--CODE-->

<!--CODE-->

<!-- 3af9c5d7b979 -->
#### 模板 C - 合併後的互斥範圍（新增／移除／查詢）

**轉折**：範圍是**可變的** — 寫入時必須跟鄰居合併，刪除時必須把它們切開。

**不變式**：區間互斥、已排序、**不相鄰**（`ends[i] < starts[i+1]`）、且非空。每個操作在回傳前都要把它恢復。

<!--CODE-->

<!--CODE-->

**類似題目（同一套有序 map 骨架）**

| LC | 題目 | 轉折 |
|----|---------|-------|
| 729 | My Calendar I | 完全不允許重疊 → 模板 A |
| 731 | My Calendar II | 允許雙重訂位，拒絕三重 → 模板 B + 回滾 |
| 732 | My Calendar III | 回報最大同時訂位數 → 模板 B |
| 715 | Range Module | 可變的覆蓋集合（新增／移除／查詢）→ 模板 C |
| 352 | Data Stream as Disjoint Intervals | `addNum` 就是模板 C 的 `addRange(v, v+1)`；`getIntervals` 回傳合併後的清單 |
| 855 | Exam Room | 有序的**座位集合**；`seat()` 時掃過所有空隙，找離最近鄰居距離最大的位置 |
| 2034 | Stock Price Fluctuation | 有序 map `price -> count`（multiset）做 O(log N) 最大／最小值 + HashMap `timestamp -> price` 處理更正 |

<!-- e194ce6355a7 -->
### 8) 兩個堆積 — 動態中位數, LC 295 ⭐⭐⭐⭐⭐


**模式**：把資料流切成**存較小一半的最大堆積**（`lo`）與**存較大一半的最小堆積**（`hi`）。

**不變式**（每次插入後都要恢復）：
1. `max(lo) <= min(hi)` — `lo` 的每個元素都 `<=` `hi` 的每個元素
2. `len(lo) == len(hi)` 或 `len(lo) == len(hi) + 1` — 所以中位數是 `lo.top()`（奇數）或兩個堆頂的平均（偶數）

**關鍵技巧**：插入時一律**先推進 `lo`，把它的最大值彈到 `hi`，再平衡回來**。這樣不用任何比較分支就能維持不變式 1。

<!--CODE-->

<!--CODE-->

**變形 - 單一固定大小的堆積**（轉折：我們只需要*第 k 個*順序統計量，不是中間那個，所以一個**大小為 k 的最小堆積**就夠了；它的根就是答案）

<!--CODE-->

<!--CODE-->

<!-- 668ab27f19cc -->
### 9) Time Based Key-Value Store — LC 981

<!--CODE-->

<!-- 6cda34ed561c -->
### 10) Design Hit Counter — LC 362

<!--CODE-->

<!-- 7c5a7b644143 -->
## 字典樹與前綴搜尋

<!-- 781d407909f2 -->
### 11) Design Search Autocomplete System — LC 642

<!--CODE-->

<!-- d2ad318abbfb -->
### 12) Design Add and Search Words Data Structure — LC 211

<!--CODE-->

<!-- bcc3ed624d58 -->
## 檔案系統與路徑

<!-- d225b6210f67 -->
### 13) Design File System — LC 1166

<!--CODE-->

<!-- f0d8c5dbecdd -->
### 14) Design In-Memory File System — LC 588

<!--CODE-->

<!-- eab1b9d62fc6 -->
### 15) Design Log Storage System — LC 635

<!--CODE-->

<!-- b5c899ae47e5 -->
## 動態消息、遊戲與模擬

<!-- 96d67f398167 -->
### 16) Design Twitter — LC 355

<!--CODE-->

<!-- 4a476fe0beb2 -->
### 17) Design Tic-Tac-Toe — LC 348

<!--CODE-->

<!-- 979cd79a6998 -->
### 18) Design Snake Game — LC 353

<!--CODE-->

<!-- be2b1d1ff76e -->
### 19) Design Underground System — LC 1396

<!--CODE-->

<!-- b4933c2986d4 -->
## 快速參考

<!-- ad8347cbe0b9 -->
### 20) 其他高頻的 `design` 標籤題目


- **LC 297. Serialize and Deserialize Binary Tree**（Hard）- 前序 DFS，用 `#` 當 null 的哨兵；反序列化時依同樣順序消耗這串 token（見 `tree.md`）
- **LC 449. Serialize and Deserialize BST**（Medium）- 想法相同，但 BST 的順序讓你可以省掉 null 標記，改用 `(lower, upper)` 邊界重建
- **LC 706 / 705. Design HashMap / HashSet**（Easy）- 桶陣列 + 分離鏈結法（見 `hash_map.md`）
- **LC 707. Design Linked List**（Medium）- dummy head + size 欄位（見 `linked_list.md`）
- **LC 745. Prefix and Suffix Search**（Hard）- 把每個 `suffix + '{' + word` 都插進同一棵字典樹，再搜尋 `suf + '{' + pre`
- **LC 676. Implement Magic Dictionary** / **LC 677. Map Sum Pairs**（Medium）- 字典樹變形：剛好一個字元不匹配的 DFS／前綴和聚合（見 `trie.md`）
- **LC 1472. Design Browser History**（Medium）- 陣列 + 目前索引（`visit` 時截斷往前的歷史），或兩個堆疊
- **LC 1352. Product of the Last K Numbers**（Medium）- 前綴乘積清單；`add(0)` 時重置清單，`k > len` 時答 0

---
