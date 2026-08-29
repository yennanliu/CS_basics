<!-- 5fde78565d54 -->
# Collection（容器選擇）

> **範圍** — 跨容器的**選型指南**：什麼工作該用哪個 Java／Python 容器，以及各自的 API 陷阱。
> **另見**：[hash_map.md](./hash_map.md)；[set.md](./set.md)；[heap.md](./heap.md)；[queue.md](./queue.md)；[stack.md](./stack.md)。

<!-- 8ee5a41d23f4 -->
## LeetCode 題目清單

- [Array](https://leetcode.com/problem-list/array/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)
- [Linked List](https://leetcode.com/problem-list/linked-list/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

<!-- 442b0cdc3b57 -->
## 0) 概念

<!-- 75a716c93388 -->
### 0-1) 題型分類

- 演算法
    - dict/collections 操作
        - collections.Counter
        - collections.Counter().most_common()
    - 排序
    - 取出出現最多次的元素
    - 取出只由指定字母組成的子字串
    - 自訂排序
        - LC 791

- 資料結構
    - dict
    - set
    - 陣列

<!-- 0de0d3d03b4b -->
## 1) 一般形式

<!-- 422aea68ac1b -->
### 1-0) 取出容器中存在的元素（自訂排序）
<!--CODE-->

<!-- fd559876ea18 -->
#### 1-2) 重要方法 - most_common()
<!--CODE-->

<!-- 8f32ad560d44 -->
#### 1-5) `OrderedDict`（雜湊表 + 鏈結串列）— LC 146
<!--CODE-->

<!-- 5fa13fe640ce -->
## 2) LC 範例

<!-- 766801d0e74f -->
## 3) 這題該用哪個容器？ ⭐⭐⭐⭐⭐

> 這份文件是**選型指南**。結構選定之後，演算法的細節請看各自的專門文件 —
> 見 [3-5) 深入文件](#3-5-深入文件)。

<!-- 459dcc548b8b -->
### 3-1) 容器速查表 ⭐⭐⭐⭐⭐

| 容器 | 依 key 查找 | 依索引查找 | 最小／最大 | 有序走訪 | Java / Python |
|---|---|---|---|---|---|
| **陣列 / List** | O(N) | **O(1)** | O(N) | 插入順序 | `ArrayList` / `list` |
| **雜湊表** | 平均 **O(1)** | – | O(N) | 無順序 | `HashMap` / `dict`, `Counter` |
| **雜湊集合** | 平均 **O(1)** | – | O(N) | 無順序 | `HashSet` / `set` |
| **堆積（PQ）** | O(N) | – | peek **O(1)**，pop O(log N) | 無順序 | `PriorityQueue` / `heapq` |
| **雙端佇列** | O(N) | 兩端 O(1) | *單調時* O(1) | 插入順序 | `ArrayDeque` / `collections.deque` |
| **鏈結串列** | O(N) | O(N) | O(N) | 插入順序 | 自行寫節點 / 自行寫節點 |
| **有序 Map** | O(log N) + **floor/ceiling** | – | O(log N) | **依 key 排序** | `TreeMap` / 排序陣列上的 `bisect` |
| **保留插入順序的 Map** | 平均 **O(1)** | – | O(N) | **插入／存取順序** | `LinkedHashMap` / `OrderedDict` |

**Key Idea**：挑**能力最弱但仍答得出這個查詢**的結構。一個 counter 就夠卻搬出堆積（或 `HashMap` 就夠卻用 `TreeMap`），代價是多一個 `log N`，還有面試分數。

<!-- 7b3ecd8c20de -->
### 3-2) 題目訊號 → 容器 → 原因 ⭐⭐⭐⭐⭐

| 題目裡的訊號 | 該用 | 原因 | LC |
|---|---|---|---|
| 「計數／頻率／anagram／能不能用 Y 拼出 X」 | **雜湊表**（`Counter`） | 每個字元 O(1)，取代一層巢狀迴圈 | 383, 819 |
| 「top K／最近的 K 個／出現最多的 K 個」 | **大小為 K 的堆積** | 只留 K 個，其餘丟掉 → O(N log K) | 973, 692, 621 |
| 「合併 K 個已排序的東西」 | **K 個頭節點組成的最小堆積** | O(log K) 取得下一個全域最小值 | 23 |
| 「滿足某條件的最長／最短視窗」 | **滑動視窗 + counter map** | 視窗狀態在兩端都是 O(1) 更新 | 3, 904 |
| 「子陣列和為 K／數這種子陣列有幾個」 | **前綴和 + 雜湊表** | 把「區間」變成「兩個前綴的差」 | 560 |
| 「距離 k 以內出現過同樣的值」 | **雜湊表 `value → last index`** | 只需要最後一次的索引，不必記全部 | 219 |
| 「切開後每個字母只出現在一段裡」 | **雜湊表 `char → last index`** | 最後索引決定了這一段最少要延伸到哪 | 763 |
| 「自訂字母表／非標準順序」 | **長度 26 的 rank 陣列** | rank 查找 O(1)，比 comparator 快 | 953, 791 |
| 「複製帶有任意指標的結構」 | **雜湊表 `old node → new node`** | 一趟就解決前向參照 | 138 |
| 「insert / delete / **getRandom** 全部 O(1)」 | **陣列 + `value → index` map** | 陣列負責隨機取；跟尾端交換讓刪除保持 O(1) | 380 |
| 「有淘汰機制的快取，操作全 O(1)」 | **雜湊表 + 雙向鏈結串列** | map 負責查找，串列負責記錄新舊順序 | 146, 460 |
| 「每一列／行／宮格內是否重複」 | **雜湊集合的陣列** | 每個限制條件一個集合，一趟掃完 | 36 |
| 「**每個**大小為 k 的視窗的最大／最小值」 | **單調雙端佇列**（堆積只有搭配延遲刪除才行） | 被壓過的元素之後永遠用不到 | 239 |
| 「兩條串列／路徑會不會相交」 | 先用**節點的雜湊集合**，再優化成雙指標 | 集合先證明想法；指標再把 O(N) 空間拿掉 | 160 |
| 「某個 key 的 floor／ceiling／前驅」 | **有序 map**（`TreeMap` / `bisect`） | 純雜湊表答不出「最接近的 key」 | 699, 218 |
| 「掃描線：**現在**有哪些是活躍的」 | **堆積 + 雜湊表**（延遲刪除）或有序 multiset | 沒有 decrease-key → 標記為過期，讀取時再彈出 | 218 |
| 「值 → 符號，貪婪地從大的開始」 | **兩個平行陣列**（由大到小） | 順序固定又很短；用 map 反而弄丟順序 | 12 |
| 「**原地**合併兩個已排序陣列」 | **不用額外容器** — 從後往前雙指標 | 往回寫永遠不會蓋掉還沒讀的資料 | 88 |
| 「網格：島嶼／區域／可達性」 | **`visited` 二維陣列 + 佇列／堆疊** | 整數 key 很密集 → 陣列勝過雜湊集合 | 200, 419 |
| 「依指定順序造訪網格上的目標」 | **排序過的目標清單 + 每段做一次 BFS** | 順序是全域的，距離是局部的 | 675 |
| 「iterator 需要往前看一個元素」 | 在欄位裡**快取下一個元素** | 底層的 iterator 沒辦法倒回去 | 284 |
| 「建立／合併串列，head 可能會變」 | **dummy head 節點**，不需額外容器 | 把 head 的特例處理全部消掉 | 21, 2 |

<!-- d3a7d4fbef40 -->
### 3-3) 面試中真的會考的組合 ⭐⭐⭐⭐

| 組合 | 要維持的不變式 | LC | 完整模板 |
|---|---|---|---|
| **雜湊表 + 雙向鏈結串列** | map 的 value 直接指*向節點*，所以拆鏈是 O(1) | 146, 460 | [design.md](design.md) `2-3` / `2-4` |
| **陣列 + `value → index` map** | 刪除時：把最後一個元素搬進洞裡，**接著**修正它在 map 裡的索引，最後才 `pop()` | 380 | [array.md](array.md) — LC 380 模式 |
| **堆積 + 雜湊表（「延遲刪除」）** | 堆積裡可能有過期項目；**只在**堆頂和 map 不一致時才一直往外彈 | 218 | [heap.md](heap.md) Template 8 |
| **有序 map（floor / ceiling）** | key 保持排序；`floorKey(x)` = 最大的 ≤ x 的 key | 699, 218 | [hash_map.md](hash_map.md), [java_trick.md](java_trick.md) |
| **前綴和 + 雜湊表** | 迴圈前先塞 `{0: 1}`，而且要*先讀再寫*當前前綴 | 560 | [prefix_sum.md](prefix_sum.md) |
| **滑動視窗 + counter map** | 不合法就縮；記得刪掉計數為 0 的 key，否則 `len(map)` 會騙你 | 3, 904 | [sliding_window.md](sliding_window.md) |
| **雜湊集合的陣列** | 每個獨立限制一個集合，全部在同一趟填完 | 36 | [array.md](array.md) |

> **容易踩到的坑**：`Counter` / `defaultdict` 會**在讀取時建 key**（`d[k]` 就插入了）。當答案取決於
> `len(d)`（相異元素計數的視窗題）時，記得刪掉歸零的 key，或改用 `d.get(k, 0)` 讀。

<!-- 6181aee66549 -->
### 3-4) 上面那些 Python 操作的 Java 對應 ⭐⭐⭐⭐

前面幾節都只寫 Python；這裡是同一套工具的 Java 版。

| Python | Java |
|---|---|
| `Counter(s)` / `defaultdict(int)` | `map.merge(k, 1, Integer::sum)` 或 `map.getOrDefault(k, 0) + 1` |
| `defaultdict(list)` | `map.computeIfAbsent(k, x -> new ArrayList<>()).add(v)` |
| `Counter(...).most_common(k)` | 大小為 K 的 `PriorityQueue` 搭配 comparator |
| `OrderedDict`（加上 `move_to_end`、`popitem`） | `LinkedHashMap(cap, 0.75f, true)`（加上 `removeEldestEntry`） |
| 排序陣列上的 `bisect` | `TreeMap.floorKey` / `ceilingKey` |
| `set` | `HashSet`（要保留插入順序就用 `LinkedHashSet`） |

<!--CODE-->

<!--CODE-->

<!-- 0f452de9c9fc -->
### 3-5) 深入文件

| 文件 | 用來查 |
|---|---|
| [hash_map.md](hash_map.md) | 雜湊表模式、碰撞、`TreeMap` 有序 map API |
| [hashing.md](hashing.md) | rolling hash、自訂雜湊 key |
| [set.md](set.md) | 集合專屬模式、去重、成員檢查 |
| [array.md](array.md) | 原地技巧、索引當雜湊、LC 380 模式 |
| [linked_list.md](linked_list.md) | dummy head、雙指標、反轉 |
| [heap.md](heap.md) | top-K、K 路合併、延遲刪除（Template 8） |
| [queue.md](queue.md) / [monotonic_queue.md](monotonic_queue.md) | 雙端佇列、滑動視窗極值（LC 239） |
| [design.md](design.md) | LRU / LFU / iterator 類的設計題（LC 146, 460, 284） |
| [java_trick.md](java_trick.md) / [python_trick.md](python_trick.md) | 各語言的容器 API |

<!-- 7cbf45d3b01a -->
### 3-6) 延伸練習

- LC 380 Insert Delete GetRandom O(1) — 陣列 + `value → index` map
- LC 460 LFU Cache — 雜湊表 + 用有序 map 做的頻率桶
- LC 692 Top K Frequent Words — counter + 大小為 K 的堆積（同分時比字串）
- LC 763 Partition Labels — `char → last index` map
- LC 560 Subarray Sum Equals K — 前綴和 + 雜湊表
- LC 36 Valid Sudoku — 雜湊集合的陣列
- LC 138 Copy List with Random Pointer — `old node → new node` map
- LC 219 Contains Duplicate II — `value → last index` map
- LC 953 Verifying an Alien Dictionary — 字元 rank 陣列
- LC 699 Falling Squares — 有序 map／座標掃描
