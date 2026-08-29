<!-- 5d4294bbb96d -->
# 系統設計題（Design）

> **範圍** — LC 的「設計一個 X」題型：從題目讀出必須支援的操作，再挑出能讓每個操作都做到 O(1) 或 O(log n) 的結構組合；實際寫出來的設計放在 examples 那一頁。
> **另見**：[design_examples.md](./design_examples.md) — 二十個設計的完整實作；[design_patterns.md](./design_patterns.md) — 一致性雜湊、限流器與負載平衡，同一關卡會問但不是 LC 題；[ood_design.md](./ood_design.md) — LLD 關卡的類別建模、SOLID 與設計模式；[iterator.md](./iterator.md) — 單獨談 iterator 的契約；[hash_map.md](./hash_map.md) 與 [heap.md](./heap.md) — 大多數設計題會組合的兩個結構。

<!-- b7355bc4a6c2 -->
## LeetCode 題目清單

- [Design](https://leetcode.com/problem-list/design/)
- [Data Stream](https://leetcode.com/problem-list/data-stream/)

<!-- 442b0cdc3b57 -->
## 0) 概念

<!-- 2d6554db2928 -->
### 0-1) 題型
- **資料結構設計**：自己刻資料結構（堆疊、佇列、雜湊表等）
- **快取設計**：LRU、LFU、帶時間的快取系統
- **系統元件設計**：檔案系統、搜尋系統、限流器
- **社群網路設計**：Twitter、Instagram 動態牆、追蹤／被追蹤關係
- **排程／訂位設計**：行事曆、會議室、停車場系統
- **串流／Iterator 設計**：資料串流處理、自訂 iterator
- **遊戲設計**：井字遊戲、貪食蛇、棋盤

<!-- 2dbfc4a4b745 -->
### 0-2) 模式

<!-- 6352b2da7476 -->
#### 模式 1：雜湊表 + 鏈結串列
- **適用情境**：對順序敏感的操作（LRU、LFU、插入順序）
- **例子**：LRU Cache、LFU Cache、Insert Delete GetRandom O(1)
- **關鍵點**：雜湊表給你 O(1) 查找，鏈結串列給你 O(1) 的順序調整

<!-- a406609f168d -->
#### 模式 2：雜湊表 + 堆積
- **適用情境**：以優先權為主的操作、top-k 問題
- **例子**：Design Twitter、串流上的 Top K Frequent Elements
- **關鍵點**：雜湊表管資料，堆積管優先順序

<!-- d6e1aada0e2d -->
#### 模式 3：字典樹（Trie，前綴樹）
- **適用情境**：自動補完、前綴搜尋、單字驗證
- **例子**：Search Autocomplete System、Add and Search Word、Design Search System
- **關鍵點**：前綴類操作只要 O(L)，L 是單字長度

<!-- 4832e06b5458 -->
#### 模式 4：物件導向設計（OOD）
- **適用情境**：多個元件互相互動的複雜系統
- **例子**：停車場、電梯系統、圖書館管理
- **關鍵點**：重點在類別、介面、關係與 SOLID 原則

<!-- d88094c22a77 -->
#### 模式 5：串流／佇列
- **適用情境**：即時資料處理、移動視窗類操作
- **例子**：Moving Average、Hit Counter、Rate Limiter
- **關鍵點**：時間視窗類操作用雙端佇列或佇列

<!-- 0de0d3d03b4b -->
## 1) 通用作法

<!-- f7f9ea61f2fe -->
### 1-1) 基本流程

<!-- cd6ebd664b48 -->
#### 步驟 1：釐清需求
- 需要支援哪些操作？
- 時間／空間複雜度的要求是什麼？
- 有哪些邊界情況？（空輸入、重複值、並行）
- 規模大概多大？（單機 vs 分散式）

<!-- ecae21b932d1 -->
#### 步驟 2：選資料結構
- 把每項需求對應到合適的資料結構
- 想清楚取捨（時間 vs 空間、簡潔 vs 效能）
- 通常需要多個結構搭配（雜湊表 + List、雜湊表 + 堆積等）

<!-- 0a5752ebfe68 -->
#### 步驟 3：定義類別結構
<!--CODE-->

<!-- acd69484af22 -->
#### 步驟 4：實作核心操作
- 專心把題目要求的方法做完
- 維持不變量（多個結構之間的資料一致性）
- 處理邊界情況

<!-- 40c5af99f7ad -->
#### 步驟 5：最佳化
- 找出瓶頸
- 需要 O(1) 的地方就換成能做到 O(1) 的結構
- 考慮延遲計算或快取

<!-- f726ece70878 -->
### 1-2) 面試技巧

<!-- ca3e1f58db6c -->
#### 技巧 1：先問清楚
- 「需要支援並行存取嗎？」（LC 題目通常不用）
- 「查一個不存在的 key 應該回傳什麼？」
- 「輸入大小或值域有限制嗎？」
- 「需要支援刪除／更新嗎？」

<!-- 5fb08b157dd8 -->
#### 技巧 2：先給簡單解
- 先用基本結構做出暴力版
- 說明時間／空間複雜度
- 再依需求往上優化

<!-- f032f6cce4c0 -->
#### 技巧 3：資料結構怎麼選
- **要快速查找？** → 雜湊表／雜湊集合
- **要維持順序？** → 鏈結串列、TreeMap、堆積
- **兩者都要？** → 組合起來（LRU 就是雜湊表 + 鏈結串列）
- **前綴操作？** → 字典樹（Trie）
- **區間查詢？** → 線段樹、樹狀陣列
- **跟時間有關的操作？** → 帶時間戳的佇列／雙端佇列

<!-- 4848363b52ba -->
#### 技巧 4：常見錯誤
- 沒有維持多個結構之間的一致性
- 忘了處理邊界情況（空、單一元素、重複）
- 沒算到輔助操作的時間複雜度
- 過度設計（需求允許的話就保持簡單）

<!-- 53d75fa76950 -->
#### 技巧 5：OOD 專屬提醒
- 介面與職責要劃清楚
- 類別與方法命名要有意義
- 想想 SOLID 原則（尤其是單一職責）
- 考慮可擴充性與可維護性

<!-- 1ca255e1f21a -->
### 1-3) 需要注意的地方

<!-- 4f7f4bcda7e6 -->
#### 注意 1：Python 的 OrderedDict
- 同時具備雜湊表與鏈結串列的能力
- `move_to_end(key)`：O(1) 重新排序
- `popitem(last=False)`：移除第一個（FIFO），`last=True` 則是 LIFO
- 拿來實作 LRU／LFU 快取非常合適

<!-- f21fbc8edb15 -->
#### 注意 2：雙結構模式
- 用多個資料結構時，務必讓它們保持同步
- 例子：LRU 用 `cache_dict`（查找）+ `cache_list`（順序）
- 新增／移除／修改時，**兩邊都要更新**

<!-- 1f103831c8e9 -->
#### 注意 3：鏈結串列的 dummy 節點
- 用 dummy head／tail 節點來簡化邊界情況
- 省掉頭尾操作的 null 檢查
- LRU Cache 的實作很常見

<!-- f7e49c5b3240 -->
#### 注意 4：以時間為基礎的過期機制
- 用時間戳 + 清理策略
- 惰性清理：存取到才把過期項目移除
- 積極清理：用堆積／佇列追蹤過期時間
- 取捨：空間（留著舊資料）vs 時間（清理成本）

<!-- 6c7d41dbbf08 -->
#### 注意 5：defaultdict 與 Counter
<!--CODE-->

<!-- 85315614d5a4 -->
### 1-4) 依分類整理的經典 LC 題

<!-- 2d802247a049 -->
#### 分類 1：快取設計 ⭐⭐⭐
- **LC 146. LRU Cache**（Medium）- 雜湊表 + 雙向鏈結串列
- **LC 460. LFU Cache**（Hard）- 雜湊表 + OrderedDict 做頻率桶
- **LC 432. All O(1) Data Structure**（Hard）- 雜湊表 + 由桶組成的雙向鏈結串列
- **LC 1756. Design Most Recently Used Queue**（Medium）

<!-- 136544f9bba0 -->
#### 分類 2：資料結構設計
- **LC 380. Insert Delete GetRandom O(1)**（Medium）- 雜湊表 + ArrayList
- **LC 381. Insert Delete GetRandom O(1) - Duplicates**（Hard）
- **LC 211. Design Add and Search Words Data Structure**（Medium）- 字典樹
- **LC 208. Implement Trie (Prefix Tree)**（Medium）
- **LC 641. Design Circular Deque**（Medium）
- **LC 622. Design Circular Queue**（Medium）
- **LC 225. Implement Stack using Queues**（Easy）
- **LC 232. Implement Queue using Stacks**（Easy）

<!-- cae6991141f7 -->
#### 分類 3：串流／時間相關設計
- **LC 346. Moving Average from Data Stream**（Easy）- 佇列
- **LC 362. Design Hit Counter**（Medium）- 帶時間戳的佇列
- **LC 353. Design Snake Game**（Medium）- 佇列 + 集合
- **LC 1396. Design Underground System**（Medium）- 雜湊表
- **LC 981. Time Based Key-Value Store**（Medium）- 雜湊表 + 二分搜尋

<!-- c024412592f7 -->
#### 分類 4：檔案系統設計
- **LC 1166. Design File System**（Medium）- 用雜湊表存路徑
- **LC 588. Design In-Memory File System**（Hard）- 類似 Trie 的巢狀 dict 結構
- **LC 1244. Design A Leaderboard**（Medium）- 雜湊表 + TreeMap

<!-- a33ee175d7fd -->
#### 分類 5：社群網路設計
- **LC 355. Design Twitter**（Medium）- 雜湊表 + 用堆積合併動態
- **LC 1603. Design Parking System**（Easy）- 單純的計數器

<!-- b1a242aca5f3 -->
#### 分類 6：搜尋／自動補完設計
- **LC 642. Design Search Autocomplete System**（Hard）- 字典樹 + 堆積
- **LC 1268. Search Suggestions System**（Medium）- 字典樹或排序
- **LC 1146. Snapshot Array**（Medium）- 用雜湊表存快照

<!-- 78495a758262 -->
#### 分類 7：Iterator 設計
- **LC 284. Peeking Iterator**（Medium）- 包一層並預看一個元素
- **LC 251. Flatten 2D Vector**（Medium）- 雙指標
- **LC 341. Flatten Nested List Iterator**（Medium）- 用堆疊做 DFS
- **LC 281. Zigzag Iterator**（Medium）- 由 iterator 組成的佇列

<!-- 367c9a04caf7 -->
#### 分類 8：限流器設計
- **LC 362. Design Hit Counter**（Medium）- 滑動視窗
- 設計 Token Bucket 限流器（常見面試題）
- 設計 Leaky Bucket 限流器（常見面試題）

<!-- 7f171d2dc6fd -->
#### 分類 9：遊戲設計
- **LC 348. Design Tic-Tac-Toe**（Medium）- 列／行／對角線計數器
- **LC 353. Design Snake Game**（Medium）- 佇列 + 集合
- **LC 1286. Iterator for Combination**（Medium）

<!-- 6653fcae8b57 -->
## 2) 模式選擇

「設計一個 X」從來不是單一結構就能解掉的。解法都是：**先指出哪一個操作用直覺的結構會很慢，再加上第二個結構，而它唯一的工作就是讓那個操作變快。** 先把題目要求的操作抄下來，再選搭配。

| 每個操作都必須… | 搭配 | 為什麼單一結構不夠 | 實作在 |
|---|---|---|---|
| O(1) get *以及* 依最近使用度 O(1) 淘汰 | **雜湊表 + 雙向鏈結串列** | 表負責找到節點；只有雙向節點能 O(1) 把自己拆下來 | [1) LRU](./design_examples.md#1-lru-cache--lc-146-) |
| O(1) get *以及* 依使用頻率 O(1) 淘汰 | **雜湊表 + 頻率 → 鏈結串列的 map** | 有了頻率，淘汰就變成「最小非空桶的頭」 | [2) LFU](./design_examples.md#2-lfu-cache--lc-460-)、[3) All O(1)](./design_examples.md#3-all-o1-data-structure--lc-432-) |
| O(1) 插入、刪除 *以及* **均勻隨機取值** | **雜湊表 + 陣列，刪除時與尾端交換** | 隨機取值需要連續索引，刪除需要查找 — 交換讓兩者都成立 | [4) Insert Delete GetRandom](./design_examples.md#4-insert-delete-getrandom-o1--lc-380-) |
| O(1) push/pop *再加上* O(1) 取最小、最大或計數 | **堆疊 + 一個平行的聚合值堆疊** | 聚合值只對堆疊的某個前綴有效，所以必須跟著一起 push 和 pop | [6) Min Stack](./design_examples.md#6-stack--auxiliary-state--o1-min-and-lazy-increment-lc-155--lc-1381-) |
| 有序查詢 — floor、ceiling、範圍 | **平衡二元搜尋樹／TreeMap**（`SortedDict`、`TreeMap`） | 雜湊表沒有順序，重疊與鄰居查詢會退化成 O(n) | [7) Ordered Map](./design_examples.md#7-ordered-map-treemap-for-booking--interval-design--lc-715--729--731--732--2034-) |
| 動態中位數，或「目前為止第 k 大」 | **兩個堆積**（低半部用最大堆積，高半部用最小堆積） | 只要兩半保持平衡，答案就在兩個根上 | [8) Two Heaps](./design_examples.md#8-two-heaps--running-median-lc-295-) |
| 查詢「時間 T 當下的值」 | **雜湊表 → 排序好的 list + 二分搜尋** | 值是按時間順序 append 的，所以是在已排序的 list 上搜尋 | [9) Time Based KV](./design_examples.md#9-time-based-key-value-store--lc-981) |
| 在滑動時間視窗上計數 | **雙端佇列，或由桶組成的環狀緩衝區** | 過期的從前面離開，新的從後面進來 | [10) Hit Counter](./design_examples.md#10-design-hit-counter--lc-362) |
| 字串的前綴／萬用字元比對 | **字典樹**，需要的話每個節點再掛堆積或快取好的 top-k | 是查詢長度的 O(L)，而不是掃過所有單字的 O(n·L) | [11) Autocomplete](./design_examples.md#11-design-search-autocomplete-system--lc-642)、[12) Add and Search Words](./design_examples.md#12-design-add-and-search-words-data-structure--lc-211) |
| 由 k 個追蹤來源合併出的動態牆 | **雜湊表 + 對每個來源的游標建堆積** | 合併 k 個有序串列，但要惰性做 — 你只需要前 `n` 筆 | [16) Design Twitter](./design_examples.md#16-design-twitter--lc-355) |
| 用路徑定址的階層結構 | **由路徑片段組成的字典樹**，或用完整路徑當 key 的 map | 差別就在要不要支援對前綴做 `ls` | [13)](./design_examples.md#13-design-file-system--lc-1166)、[14)](./design_examples.md#14-design-in-memory-file-system--lc-588) |

<!-- d7bd71fdb40f -->
### 動手寫之前要問的四個問題

1. **有哪些操作，各自需要什麼複雜度？** 在挑結構*之前*，先把類別骨架寫出來，並在每個方法上用註解標明要求的複雜度。錯誤解法多半是去優化了一個題目根本沒要求要快的操作。
2. **淘汰或過期的規則是什麼？** 最近使用度、使用頻率、時間視窗會導向三種不同的第二結構，而規則就是決定用哪個的依據。
3. **查詢需要順序嗎？** 只要有任何一個查詢是「最接近」「在……之前」「重疊」或「範圍」，雜湊表就不能是唯一的索引。
4. **不變量是什麼，在哪裡被恢復？** 兩個堆積的不變量是大小平衡；LRU 是「頭部最新」；延遲累加是「待處理的差值會套用到底下所有元素」。先把它講出來，再讓每個方法都以恢復它作結。

<!-- 2bf8aaf30756 -->
## 3) 實作範例

二十個設計，依各自逼出來的結構搭配分組，全部放在
**[design_examples.md](./design_examples.md)**：

| 分組 | 題目 |
|---|---|
| [快取與淘汰策略](./design_examples.md#caches--eviction-policies) | LC 146, 460, 432, 380 |
| [帶輔助狀態的堆疊](./design_examples.md#stacks-with-auxiliary-state) | LC 895, 155, 1381 |
| [有序 map、堆積與時間視窗](./design_examples.md#ordered-maps-heaps--time-windows) | LC 715, 729, 731, 732, 2034, 295, 981, 362 |
| [字典樹與前綴搜尋](./design_examples.md#tries--prefix-search) | LC 642, 211 |
| [檔案系統與路徑](./design_examples.md#file-systems--paths) | LC 1166, 588, 635 |
| [動態牆、遊戲與模擬](./design_examples.md#feeds-games--simulation) | LC 355, 348, 353, 1396 |

<!-- 9a5bb67ee6c0 -->
## 4) 系統設計類的實作題

一致性雜湊、兩種限流器與負載平衡演算法都搬到
**[design_patterns.md](./design_patterns.md)** 了。它們會在同一批關卡裡*以實作題的形式*被問到，但沒有一題是 LeetCode 題目；留在這裡會讓這份文件的範圍橫跨兩個主題。
