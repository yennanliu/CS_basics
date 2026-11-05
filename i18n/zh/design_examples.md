<!-- 936b6ec5b063 -->
# 系統設計題 — 實作範例

> **範圍** — [design.md](./design.md) 背後的「設計一個 X」實作題，每題一張題目 card，依它逼出來的結構搭配（design.md 的模式）歸類。
> **另見**：[design.md](./design.md) — 上層文件：這些解法在演練的模式、五步驟方法與挑選表；[design_patterns.md](./design_patterns.md) — 一致性雜湊、限流器與負載平衡，同一關卡會問但不是 LC 題；[ood_design.md](./ood_design.md) — LLD 關卡的類別建模與 SOLID；[iterator.md](./iterator.md) — 單獨談 iterator 的契約；[trie.md](./trie.md)、[heap.md](./heap.md)、[hash_map.md](./hash_map.md)、[linked_list.md](./linked_list.md) — 被組合起來的各個結構。

<!-- b7355bc4a6c2 -->
## LeetCode 題目清單

- [Design](https://leetcode.com/problem-list/design/)
- [Data Stream](https://leetcode.com/problem-list/data-stream/)

<!-- 5c5c6b4042b7 -->
## 概觀

模式在 [design.md](./design.md)：一道設計題就是一個存資料的結構，再加上第二個結構，而它唯一的工作是讓某一個操作變快。這一頁是題目。
每張 card 寫出它歸約到的模式、它多加的那一步，以及一份標準解法。結構本身就是別處核心 card 的設計（LRU 在 [linked_list.md](./linked_list.md)、
動態中位數在 [heap.md](./heap.md)、min stack 在 [stack.md](./stack.md)）就在那裡解，這裡只列成指標。

<!-- 8fa2ce401ba4 -->
## 索引

| LC | 題目 | 歸約到 | 難度 | 在哪 |
|---|---|---|---|---|
| 146 | LRU Cache | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | Medium | 在 linked_list.md 解 |
| 460 | LFU Cache | [linked_list § LFU variant](./linked_list.md#lfu-variant--one-list-per-frequency-plus-a-min_freq-pointer-lc-460-) | Hard | 在 linked_list.md 解 |
| 432 | All O(1) Data Structure | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | Hard | [見下方](#all-o1-data-structure--lc-432-) |
| 703 | Kth Largest Element in a Stream | [heap § Template 1](./heap.md#template-1-size-k-min-heap-for-the-kth-largest--lc-215-) | Easy | 在 heap_examples.md 解 |
| 355 | Design Twitter | [heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) | Medium | [見下方](#design-twitter--lc-355) |
| 295 | Find Median from Data Stream | [heap § Template 4](./heap.md#template-4-two-heaps-for-a-running-median--lc-295-) | Hard | 在 heap.md 解 |
| 211 | Design Add and Search Words Data Structure | [trie § Template 3](./trie.md#template-3-trie-with-wildcard-support--lc-211) | Medium | 在 trie.md 解 |
| 642 | Design Search Autocomplete System | [trie § Template 4](./trie.md#template-4-autocomplete-trie--lc-1268) | Hard | [見下方](#design-search-autocomplete-system--lc-642) |
| 362 | Design Hit Counter | [queue § Template 6](./queue.md#template-6-stream-processing-pattern--lc-346) | Medium | [見下方](#design-hit-counter--lc-362) |
| 353 | Design Snake Game | [queue § Template 6](./queue.md#template-6-stream-processing-pattern--lc-346) | Medium | [見下方](#design-snake-game--lc-353) |
| 380 | Insert Delete GetRandom O(1) | [design § Pattern 6](./design.md#pattern-6-hashmap--array-swap-to-end-delete) | Medium | 在 [set_examples.md](./set_examples.md#11-insert-delete-getrandom-o1--lc-380--set-semantics-array-storage-) 解 |
| 155 | Min Stack | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) | Medium | 在 stack.md 解 |
| 1381 | Design a Stack With Increment Operation | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) | Medium | [見下方](#design-a-stack-with-increment-operation--lc-1381) |
| 895 | Maximum Frequency Stack | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) | Hard | [見下方](#maximum-frequency-stack--lc-895-) |
| 981 | Time Based Key-Value Store | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Medium | [見下方](#time-based-key-value-store--lc-981) |
| 729 | My Calendar I | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Medium | [見下方](#my-calendar-i--lc-729) |
| 731 | My Calendar II | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Medium | [見下方](#my-calendar-ii--lc-731) |
| 732 | My Calendar III | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Hard | [見下方](#my-calendar-iii--lc-732) |
| 715 | Range Module | [design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) | Hard | [見下方](#range-module--lc-715) |
| 2034 | Stock Price Fluctuation | [heap_advanced § two lazy heaps](./heap_advanced.md#variant--two-mirrored-lazy-heaps-over-one-map-lc-2034-) | Medium | 在 heap_advanced.md 解 |
| 1166 | Design File System | [design § Pattern 9](./design.md#pattern-9-path-hierarchy) | Medium | [見下方](#design-file-system--lc-1166) |
| 635 | Design Log Storage System | [design § Pattern 9](./design.md#pattern-9-path-hierarchy) | Medium | [見下方](#design-log-storage-system--lc-635) |
| 588 | Design In-Memory File System | [design § Pattern 9](./design.md#pattern-9-path-hierarchy) | Hard | [見下方](#design-in-memory-file-system--lc-588) |
| 348 | Design Tic-Tac-Toe | [design § Pattern 10](./design.md#pattern-10-aggregate-counters-updated-on-write) | Medium | [見下方](#design-tic-tac-toe--lc-348) |
| 1396 | Design Underground System | [design § Pattern 10](./design.md#pattern-10-aggregate-counters-updated-on-write) | Medium | [見下方](#design-underground-system--lc-1396) |

其他帶 `design` 標籤、這裡沒有解的題目：

| LC | 題目 | 想法 | 在哪 |
|---|---|---|---|
| 705 / 706 | Design HashSet / HashMap | 桶陣列 + 分離鏈結 | [hash_map.md](./hash_map.md) |
| 707 | Design Linked List | dummy head + 一個 size 欄位 | [linked_list.md](./linked_list.md) |
| 1472 | Design Browser History | 陣列 + 游標；`visit` 會截掉往前的歷史 | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) 的練習 |
| 1352 | Product of the Last K Numbers | 前綴乘積；`add(0)` 時清空 list，`k` 超過長度就回答 0 | [prefix_sum.md](./prefix_sum.md) |
| 297 / 449 | Serialize and Deserialize Binary Tree / BST | 帶 null 標記的前序；BST 可以省掉標記，用 `(lower, upper)` 邊界重建 | [tree.md](./tree.md) |
| 745 | Prefix and Suffix Search | 把每個 `suffix + '{' + word` 插進同一棵字典樹，查 `suf + '{' + pre` | [trie.md](./trie.md) |
| 676 / 677 | Implement Magic Dictionary / Map Sum Pairs | 允許恰好一個字元不同的 DFS／每個節點存前綴和 | [trie.md](./trie.md) |

<!-- ca9cf080da09 -->
## 雜湊表 + 鏈結串列的例題

<!-- 25f391ae0b6c -->
### All O(1) Data Structure — LC 432 ⭐⭐⭐⭐

> **關鍵** — 串列裡放的是**計數桶**而不是 key，依計數由小到大排列，所以最小的 key 在 `head.next`、
> 最大的在 `tail.prev`。計數每次只變 1，所以 key 只會移到**相鄰**的桶，而那個桶就在舊桶旁邊以 O(1) 建立。

**歸約到**：[linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) — 同一條帶哨兵的雙向鏈結串列，只是每個節點是一個「同計數 key 的桶」，也不必維護 `min_freq`（LFU 的那個整數變成了串列本身的順序）。

<!--CODE-->

**陷阱**
- 在把新桶插到舊桶旁邊**之前**就刪掉舊桶：對桶 1 裡唯一的 key 做 `inc`，桶 1 被拆掉，
  桶 2 就沒有可以插在後面的錨點了。
- 掃描找最小／最大的桶：只有「計數 → 桶」的 map 時，`getMinKey` 是
  O(不同計數的個數)。是串列的順序讓它變成 O(1)。
- `dec` 到 0 時必須從 `count_of` 刪掉這個 key，不然之後的 `inc` 會從過期的計數開始。

<!-- 8db9308f630a -->
## 雜湊表 + 堆積的例題

<!-- b87c41c0faa7 -->
### Design Twitter — LC 355

> **關鍵** — 每個使用者的推文本來就依時間排好，所以動態牆就是對追蹤對象的清單做 **k 路合併**，
> 拿到 10 則就停。全域時鐘讓不同使用者的時間戳可以互相比較；
> 推進堆積時取負號，最小堆積就會先彈出最新的推文。

**歸約到**：[heap § Template 3](./heap.md#template-3-k-way-merge--lc-23-) — 合併 k 個有序串列，但是惰性地做：只拉前 10 筆。實體建模（為什麼 `Tweet` 帶著時間、為什麼時鐘是全域的）在 [ood_design.md](./ood_design.md)。

<!--CODE-->

**陷阱**
- 在 `getNewsFeed` 裡把使用者加進自己的追蹤集合會改到狀態：之後
  `unfollow(u, u)` 就把使用者自己的推文藏起來了。改成和 `{userId}` 取聯集。
- `unfollow` 用 `remove` 而不是 `discard`，遇到從沒追蹤過的使用者會丟例外。
- 把所有推文合併完再取前 10 是 O(推文總數)；堆積彈出 10 次就停。

<!-- 8faf460c8459 -->
## 字典樹的例題

| LC | 題目 | 在哪 |
|---|---|---|
| 211 | Design Add and Search Words Data Structure | [trie § Template 3](./trie.md#template-3-trie-with-wildcard-support--lc-211) — 那張 card 的錨定題 |

<!-- b12378ee90bb -->
### Design Search Autocomplete System — LC 642

> **關鍵** — 指向字典樹的游標**跟著已輸入的前綴移動**，所以每按一個鍵就是往子節點走一步，
> 不是從根重新走一遍。一旦某個字元不在字典樹上，游標就保持 `None` 直到 `#`，
> 這句話之後的每個按鍵都回答 `[]`。

**歸約到**：[trie § Template 4](./trie.md#template-4-autocomplete-trie--lc-1268) — 同樣的前綴走訪，再加上每個句子在執行期間會變的熱度（所以前三名是在查詢時排，不是在插入時快取）。

<!--CODE-->

**陷阱**
- 某個字元找不到時把游標重設回根：在 `"i "` 失配之後輸入 `"i a"`，
  會從根重新比對，推薦出根本沒有這個前綴的句子。
- 同熱度時依句子的 ASCII 順序決定（`' '` 排在字母前面），也就是一般的字串比較；
  只依熱度排序會在 LC 642 的範例 1 出錯。

<!-- 6f0a4c6313b8 -->
## 串流／佇列的例題

<!-- 7d67918676d3 -->
### Design Hit Counter — LC 362

> **關鍵** — 時間戳是非遞減地進來的，所以過期的點擊永遠在**前面**：
> `getHits` 從左邊彈完之後，佇列裡剛好就是最近 300 秒的點擊。

**歸約到**：[queue § Template 6](./queue.md#template-6-stream-processing-pattern--lc-346) — 串流上的移動視窗，以時間而不是以筆數為準。

<!--CODE-->

追問（每秒有大量點擊）：依 `timestamp % 300` 分桶，不管進來多少點擊，記憶體都是 O(300)。
這就是要給的第二種解法，理由就是這一點。

<!--CODE-->

**陷阱**
- 視窗是 `(timestamp - 300, timestamp]`：時間 1 的點擊到 301 就不算了。拿 `< timestamp - 300`
  當淘汰條件，會讓它多留一秒。

<!-- af8fe589876a -->
### Design Snake Game — LC 353

> **關鍵** — 蛇身是一個雙端佇列（頭在左、尾在右），**再加上**一個存同樣格子的集合。
> 在檢查新的頭**之前**先把尾巴移出集合，因為頭可以合法地走進尾巴正要離開的那一格。

**歸約到**：[queue § Template 6](./queue.md#template-6-stream-processing-pattern--lc-346) — 一個隨串流前進而消耗前端的佇列，旁邊再放一個集合做 O(1) 的成員檢查。

<!--CODE-->

**陷阱**
- 先檢查碰撞再移除尾巴：長度 4 的蛇在 2×2 的格子裡繞圈，
  頭走進尾巴正要空出來的那格時會被判定死亡。
- 食物一次吃一個、依序吃：只有 `food[0]` 是有效的。

<!-- bdac3794ef88 -->
## 雜湊表 + 陣列的例題

| LC | 題目 | 在哪 |
|---|---|---|
| 380 | Insert Delete GetRandom O(1) | [set_examples § 11](./set_examples.md#11-insert-delete-getrandom-o1--lc-380--set-semantics-array-storage-) — 把要刪的元素和最後一個交換，再 pop |

<!-- 35c5872b24b2 -->
## 堆疊 + 輔助狀態的例題

<!-- c28538eb7dcb -->
### Design a Stack With Increment Operation — LC 1381

> **關鍵** — `increment(k, val)` 是加到最底下的 k 個元素，所以只在第 k 個元素上記**一次**，
> 當成「底下所有元素待加的增量」；`pop` 在回傳之前把自己的增量交給下一個元素。
> 於是每個元素看到的，剛好就是涵蓋到它的那些增量。

**歸約到**：[stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) — 每一層一個輔助值，跟著那一層一起 push 和 pop；這裡的輔助值是待加的增量，而不是目前的最小值。

<!--CODE-->

<!--CODE-->

**陷阱**
- 在 `increment` 裡逐一處理底下 k 個元素，每次呼叫是 O(k)；改用增量就是 O(1)。
- Java：pop 時忘了 `inc[size] = 0`，舊的增量會漏到下一個 push 到這格的元素上。

<!-- 94a8f48ea41b -->
### Maximum Frequency Stack — LC 895 ⭐⭐⭐⭐

> **關鍵** — `group[f]` 是一個堆疊，依 push 順序存放所有計數**曾經達到** f 的值。
> push 時把值放進新計數的桶，但不從較低的桶移除，所以 pop 之後，
> 較早的那份已經在桶 `f - 1` 裡等著：不必清理，而且桶是連續的，`max_freq` 每次最多只降 1。

**歸約到**：[stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-) — 每一層保存狀態；這裡的層是頻率，每一層本身是一個堆疊，所以同頻率時自然由最近的勝出。

<!--CODE-->

<!--CODE-->

**陷阱**
- push 時把值從舊桶移走（像 LFU 那樣）會讓 pop 出錯：從桶 2 彈出之後，
  這個值必須還留在桶 1、還在它原本的 push 位置上。
- 用 `(-freq, -push_index, value)` 的堆積也行，但每次呼叫是 O(log n) — 先把它當第一個想法講出來，
  再給出分桶的解法。

<!-- f4c5ede74faf -->
## 有序 map／時間索引的例題

<!-- 28918e6cfc98 -->
### Time Based Key-Value Store — LC 981

> **關鍵** — `set` 被呼叫時時間戳嚴格遞增，所以每個 key 的 list
> **本來就依時間排好**；`get` 就是二分搜尋最後一筆時間 ≤ t 的資料。

**歸約到**：[design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — 雜湊表對到依時間排好的 list；「floor」查詢就是 `bisect_right - 1`。Java 的二分搜尋寫法在 [binary_search_examples.md](./binary_search_examples.md#13-time-based-key-value-store-lc-981)。

<!--CODE-->

**陷阱**
- 對 `(time, value)` tuple 的 list 做 `bisect`，第二個欄位需要一個哨兵
  （`(t, chr(127))`），而遇到開頭字元更大的值就會出錯。只對時間做 bisect。
- 比所有 `set` 都早的查詢必須回傳 `""`，而不是 `values[-1]`：要檢查的是 `i == -1`。

<!-- 398ab45fddcb -->
### My Calendar I — LC 729

> **關鍵** — 已存的預約兩兩不重疊、以起點為 key，所以新的
> `[start, end)` 只可能和其中兩個衝突：起點在 `start` 或之前的那個（floor），
> 以及起點在它之後的那個（ceiling）。

**歸約到**：[design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — 有序 map 上的 floor／ceiling 查詢。

<!--CODE-->

<!--CODE-->

**陷阱**
- 區間是左閉右開：先 `[10, 20)` 再 `[20, 30)` 是合法的。floor 的檢查寫成 `>=` 會把它擋掉。
- Python 沒有 `TreeMap`：`list.insert` 是 O(n)，對 LC 729 的 1,000 次呼叫沒問題；要講出來，或改用
  `sortedcontainers.SortedList`。

<!-- 940b065da46f -->
### My Calendar II — LC 731

> **關鍵** — 每個起點記 `+1`、每個終點記 `-1`；依 key 排序後的累加和，
> 就是那一刻正在進行的預約數。先暫時把新預約加進去，
> 只要累加和一碰到 3 就撤回。

**歸約到**：[design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — 把有序 map 當成稀疏的差分陣列（就是 [scanning_line.md](./scanning_line.md) 的掃描線計數）。

<!--CODE-->

**陷阱**
- 先檢查再加入需要掃第二遍；先加入、必要時撤回只要掃一遍。
- 總成本 O(n²) 在 1,000 次呼叫下沒問題；另一種解法（保存被重複預約的區間）
  也是同樣的上界。

<!-- a59913b90a32 -->
### My Calendar III — LC 732

> **關鍵** — 和 LC 731 一樣的增量 map，只是永遠不撤回：答案是掃描過程中累加和的**最大值**。

**歸約到**：[design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — LC 731 的掃描，回報峰值而不是檢查上限。

<!--CODE-->

<!--CODE-->

**陷阱**
- 同一個 key 上的 `-1` 和 `+1` 必須在讀取累加和之前先抵銷：`[10, 20)` 和
  `[20, 30)` 從不重疊。map 會把它們併成同一個 key，所以這點自動成立。

<!-- 245ce5375c37 -->
### Range Module — LC 715

> **關鍵** — 讓追蹤中的區間保持**互不重疊、已排序、彼此不相鄰、且非空**。每個操作
> 用兩次二分搜尋找出它碰到的那一段已存區間，再把整段換成最多兩個區間，
> 在回傳前把不變量恢復。

**歸約到**：[design § Pattern 8](./design.md#pattern-8-sorted-map--time-index) — LC 729 的有序區間 map，改成可變的：寫入會合併鄰居，刪除會把區間切開。線段樹的寫法在 [segment_tree.md](./segment_tree.md)。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 相鄰的區間必須合併：先 `add [10, 20)` 再 `add [20, 30)` 是同一個區間，否則 `query [15, 25)`
  會錯誤地失敗。`addRange` 裡的搜尋用的是 `ends >= left` 和 `starts <= right`，兩邊都含等號。
- `removeRange` 裡頭尾兩段可能來自**同一個**區間（被切成兩段）；要先把兩段都算出來，
  再替換那一整段。

<!-- 076f54d4e19f -->
## 路徑階層的例題

<!-- 0d1aa89f3ae6 -->
### Design File System — LC 1166

> **關鍵** — `createPath` 只需要知道**父路徑**存在，而父路徑就是到最後一個 `/` 為止的部分。
> 一個「完整路徑 → 值」的 map 一次查找就能回答；
> 因為沒有 `ls`，所以不需要樹。

**歸約到**：[design § Pattern 9](./design.md#pattern-9-path-hierarchy) — 選擇中「用完整路徑當 key 的 map」那一邊。

<!--CODE-->

**陷阱**
- 在 `"/a"` 之前建立 `"/a/b"` 必須失敗：檢查父路徑就是整道題。
- 建立已存在的路徑要回傳 `False`，並保留舊值。

<!-- 8c580b65e315 -->
### Design Log Storage System — LC 635

> **關鍵** — 時間戳是固定寬度的 `Year:Month:Day:Hour:Minute:Second` 字串，所以
> 截到某個粒度就是取**前綴**，而把兩個前綴當字串比較，就等於把它們當時間比較。
> 範圍查詢於是變成每筆 log 做兩次字串比較。

**歸約到**：[design § Pattern 9](./design.md#pattern-9-path-hierarchy) — 時間戳是由六個片段組成的路徑，粒度決定要比到多深。

<!--CODE-->

**陷阱**
- 解析成整數、逐欄比較也行，但固定寬度的格式正是這道題設計好的技巧。
- 在選定的粒度下，兩端都包含在內。

<!-- 0da429f6dd7a -->
### Design In-Memory File System — LC 588

> **關鍵** — `ls`、`mkdir` 和寫檔都走同一條路徑，所以結構就是
> **由路徑片段組成的字典樹**，節點是存放子目錄與檔案的目錄。對檔案 `ls`
> 只回傳它的名字；對目錄則回傳排序後的子項名稱。

**歸約到**：[design § Pattern 9](./design.md#pattern-9-path-hierarchy) — 選擇中「字典樹」那一邊，因為 `ls` 要列出某個前綴底下的東西。

<!--CODE-->

**陷阱**
- Python 3 的 `dict.keys() + dict.keys()` 會丟 `TypeError`；要先轉成 list。
- `ls("/")` 切開後沒有任何片段；走訪必須回傳根，而不是失敗。

<!-- 17d41a4d048e -->
## 聚合計數的例題

<!-- fd32d4119689 -->
### Design Tic-Tac-Toe — LC 348

> **關鍵** — 某一列、某一行或某條對角線放滿 n 個同一玩家的棋子，該玩家就贏。每條線上
> 玩家 1 記 `+1`、玩家 2 記 `-1`；一條線屬於某個玩家，若且唯若它的
> 總和是 `±n`，而一步棋最多只碰到四條線。

**歸約到**：[design § Pattern 10](./design.md#pattern-10-aggregate-counters-updated-on-write) — 在寫入時更新聚合值，讓讀取（誰贏了？）是 O(1)。

<!--CODE-->

**陷阱**
- 奇數邊長棋盤的中心同時在兩條對角線上：要兩個 `if`，不是 `if / elif`。
- 每步之後掃描整個棋盤，每步是 O(n²)；計數器是 O(1)。

<!-- 08deaaa668e6 -->
### Design Underground System — LC 1396

> **關鍵** — 查詢的是每條 `(start, end)` 路線的平均，所以為每條路線保存**總時間
> 和行程次數**，在出站時更新。乘客進行中的行程是唯一的個人狀態，
> 行程結束時就刪掉。

**歸約到**：[design § Pattern 10](./design.md#pattern-10-aggregate-counters-updated-on-write) — 平均值就是在寫入時保存的總和與次數。

<!--CODE-->

**陷阱**
- 保存每一趟的時間、讀取時再平均，會讓 `getAverageTime` 變成 O(行程數)。
- 路線有方向：`(A, B)` 和 `(B, A)` 是不同的 key。
