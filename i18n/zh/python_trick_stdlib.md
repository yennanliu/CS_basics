<!-- 21475c6a6e89 -->
# 面試用的 Python 標準函式庫

> **範圍** — 逐個函式庫的參考手冊：`heapq`、`sortedcontainers`、`bisect`、`collections`、`itertools`、`functools` 與 `datetime`——每個呼叫、它們的複雜度，以及各自是為了取代哪一種手寫寫法而存在。
> **另見** — [python_trick.md](./python_trick.md) — 這些函式庫所建立在其上的語言慣用寫法；[python_trick_indexing.md](./python_trick_indexing.md) — 插入與切片的索引運算，包括 `bisect.insort` 的定位；[heap.md](./heap.md)、[binary_search.md](./binary_search.md)、[hash_map.md](./hash_map.md) — 這些資料結構與演算法本身（而非它們的 Python API）；[java_trick_collections.md](./java_trick_collections.md) — 對應的 Java 版本。

<!-- 54efa70cbe8a -->
## LeetCode 題目清單

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)
- [Binary Search](https://leetcode.com/problem-list/binary-search/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)

<!-- 96fa2aa7f3c0 -->
## 總覽

本文從 [python_trick.md](./python_trick.md) 拆分出來——這七個模組原本佔了該檔案約三分之一的篇幅，
而且和不相干的語言慣用寫法交錯在一起。

<!-- 3401319951f4 -->
### 關鍵性質
- **複雜度**：逐模組列出——這正是你該用它們而不是自己寫迴圈的理由
- **核心想法**：Python 沒有 `TreeMap`，也沒有最大堆積；`sortedcontainers` 與取負號就是標準答案，而知道這件事往往就是整道題的關鍵
- **使用時機**：當演算法已經想好，剩下的問題只是「該用哪個呼叫把它寫出來」

<!-- 5fccd8eebd6b -->
## `heapq` — 優先佇列

<!-- eb0cdddb8f88 -->
### `heapq` 基礎（預設為最小堆積） ⭐⭐⭐⭐⭐


**heapq** - 堆積佇列演算法（優先佇列）
- 預設是最小堆積（最小的元素位於索引 0）
- 要做最大堆積，就把值取負號或使用自訂比較
- 常見的面試使用情境：Top K 元素、第 K 大／小、合併 K 個有序串列

**參考資料：**
- https://docs.python.org/3/library/heapq.html
- https://github.com/python/cpython/blob/3.10/Lib/heapq.py

<!-- f84c4175a73e -->
#### **heapq 主要操作（複雜度速查表）**

| 操作 | 時間 | 空間 | 做什麼／為什麼是這個成本 |
|-----------|------|-------|------------------------------|
| `heapq.heapify(lst)` | **O(n)** | O(1) *(原地)* | 把任意 list **原地**轉成合法的最小堆積。之所以便宜，是因為它是**由下而上**篩選（見下方說明），而**不是** n × push |
| `heapq.heappush(h, x)` | **O(log n)** | O(1) | 附加到尾端，再**往上篩**最多 `log n` 層 |
| `heapq.heappop(h)` | **O(log n)** | O(1) | 把最後一個元素移到根，再**往下篩**最多 `log n` 層 |
| `h[0]`（peek） | **O(1)** | O(1) | 堆積不變式保證最小值位於索引 0。`heapq` **沒有** `peek()` |
| `heapq.heappushpop(h, x)` | **O(log n)** | O(1) | 先推再彈，只做 **1 次篩選**而非 2 次。當 `x <= h[0]` 時免費（`O(1)`） |
| `heapq.heapreplace(h, x)` | **O(log n)** | O(1) | 先彈再推，**1 次篩選**。堆積必須**非空**（否則 `IndexError`） |
| `heapq.nsmallest(k, it)` | **O(n log k)** | O(k) | 掃描時維護一個大小為 k 的堆積。當 k 接近 n 時會退回用 `sorted()` |
| `heapq.nlargest(k, it)` | **O(n log k)** | O(k) | 同上，只是比較方向相反 |
| `heapq.merge(*iters)` | **O(N log k)** | O(k) | 惰性合併 k 個**已排序**的可迭代物；回傳 generator，**不會**建出 list |
| `len(h)` | **O(1)** | O(1) | 堆積就只是一個普通的 `list` |
| 用 n 次 `heappush` 建堆 | **O(n log n)** | O(1) | ❌ 建堆的慢方法——元素已經全都有了就該用 `heapify` |

**為什麼 `heapify()` 是 O(n) 而不是 O(n log n)？**

- `heapify` 用的是**由下而上的建堆法**（Floyd 演算法）：從最後一個非葉節點（`n//2 - 1`）往索引 0 走，對每個節點**往下篩**。
- 訣竅在於**大多數節點都靠近底部**、幾乎不動：約 n/2 個葉節點成本為 0，約 n/4 個節點最多往下篩 1 層，約 n/8 個最多 2 層……
- 總工作量 = `Σ (n / 2^(h+1)) * h`（h = 0..log n），收斂到 **2n → O(n)**。
- 相對地，一個一個推入是從底部**往上篩**，而大多數節點就住在底部 → 每次 push 真的可能花 `log n` → **O(n log n)**。

<!--CODE-->

**實用準則：** 元素一開始就全部拿到了？→ 用 `heapify`（O(n)）。元素是一個一個進來的（串流）？→ 用 `heappush`（每次 O(log n)）。

<!--CODE-->

**使用 heapq 的常見面試題：**
- LC 215: Kth Largest Element in an Array
- LC 347: Top K Frequent Elements
- LC 373: Find K Pairs with Smallest Sums
- LC 378: Kth Smallest Element in a Sorted Matrix
- LC 23: Merge k Sorted Lists
- LC 295: Find Median from Data Stream（用 2 個堆積）
- LC 253: Meeting Rooms II（區間排程）
- LC 767: Reorganize String（貪婪 + 堆積）

**小結：**
- ✅ heapq 提供高效率的最小堆積（優先佇列）
- ✅ push/pop 為 O(log n)，peek（heap[0]）為 O(1)
- ✅ heapify 為 O(n)，nsmallest/nlargest 為 O(n log k)
- ✅ 要最大堆積：把值取負號，或用 `-val`
- ✅ 要自訂比較：用 tuple 的排序，或實作 `__lt__`

<!-- 39339f383d7c -->
### 用取負號模擬最大堆積 ⭐⭐⭐⭐⭐


Python 的 `heapq` 只實作了**最小堆積**——`heapify()` 沒有 `reverse=True` 這種選項。

要模擬**最大堆積**，就把優先權鍵值取負號：

<!--CODE-->

**推入的寫法：**
<!--CODE-->

**多鍵值範例（主鍵遞減、次鍵遞增）：**
<!--CODE-->

> **經驗法則**：想讓哪個欄位遞減，就把那個欄位取負號；其餘的保持原樣。

<!-- a4547c3efdf3 -->
## `sortedcontainers` — 有序映射

<!-- 7c2c6b5862d1 -->
### `SortedDict` / `SortedList` — Python 版的 TreeMap


**想法**

Python **沒有內建的 `TreeMap`**（Java 的 `java.util.TreeMap`）。標準的替代品是
**`sortedcontainers`**——一個純 Python 函式庫，它讓鍵維持在**排序順序**，
同時支援 `O(log n)` 的插入／刪除／查找，以及 `O(log n)` 的
floor / ceiling / 區間查詢。內部實作是 list-of-lists（不是樹），但
**API 與 Big-O 的行為都跟平衡 BST 一樣**，所以在 LC 上它就是首選的「TreeMap」。

- `SortedDict` ↔ Java `TreeMap`（有序的 **key → value** 映射）
- `SortedList` ↔ Java `TreeSet` / multiset（有序的值；允許重複）
- 鍵／值會自動保持排序——**每次插入都不必重新排序**（這正是它勝過
  `list.sort()` 的地方），也不像在普通 list 上用 `bisect` 那樣 `insert` 是 `O(n)`。

**`SortedDict` vs `TreeMap` — 它們並不是同一種資料結構** ⭐⭐⭐⭐

| | Python `SortedDict` | Java `TreeMap` |
|---|---|---|
| **來源** | `pip install sortedcontainers` — **不是標準函式庫**（LeetCode 上已預裝） | `java.util`，內建 |
| **實作** | `dict` + 鍵的 `SortedList`（list-of-lists，近似 B-tree） | 紅黑樹（自平衡 BST） |
| **`d[k]` / `get(k)`** | **`O(1)`** — 單純的雜湊查找 | **`O(log n)`** — 沿樹往下走 |
| **插入／刪除** | 攤還 `O(log n)` | `O(log n)` |
| **floor / ceiling** | 透過 `bisect_*` 做到 `O(log n)` → 回傳的是**索引** | 透過 `floorKey/ceilingKey` 做到 `O(log n)` → 回傳的是**鍵**或 `null` |
| **第 k 小的鍵** | **`O(log n)`** — `d.keys()[k]` ✅ | ❌ 不支援（要 `O(n)` 迭代） |
| **自訂排序** | `SortedDict(key_func)` — 只能做鍵的**轉換** | `Comparator` — 任意的雙參數邏輯 |
| **重複鍵** | ❌ | ❌ |

> **重點：** 在值查找上 `SortedDict` *比* `TreeMap` 更快（走雜湊而不是走樹），
> 而且它能做 `TreeMap` 根本做不到的**索引存取**（`keys()[k]`）。
> `TreeMap` 的 `Comparator` 是唯一嚴格更有表達力的地方。

**核心 API**

<!--CODE-->

**Java `TreeMap` → Python `SortedDict` 速查表** ⭐⭐⭐⭐⭐

| Java `TreeMap` | Python `SortedDict` | 需要的防護 |
|---|---|---|
| `firstKey()` / `lastKey()` | `d.keys()[0]` / `d.keys()[-1]` | 非空 |
| `firstEntry()` / `lastEntry()` | `d.peekitem(0)` / `d.peekitem(-1)` | 非空 |
| `floorKey(x)` — 最大的 ≤ x | `d.keys()[d.bisect_right(x) - 1]` | `idx >= 0` |
| `ceilingKey(x)` — 最小的 ≥ x | `d.keys()[d.bisect_left(x)]` | `idx < len(d)` |
| `lowerKey(x)` — 嚴格 < x | `d.keys()[d.bisect_left(x) - 1]` | `idx >= 0` |
| `higherKey(x)` — 嚴格 > x | `d.keys()[d.bisect_right(x)]` | `idx < len(d)` |
| `subMap(lo, true, hi, true)` | `d.irange(lo, hi)` | — |
| `headMap(hi, true)` / `tailMap(lo, true)` | `d.irange(maximum=hi)` / `d.irange(minimum=lo)` | — |
| `pollFirstEntry()` / `pollLastEntry()` | `d.popitem(0)` / `d.popitem(-1)` | 非空 |
| `descendingMap()` | `reversed(d)` | — |
| `new TreeMap<>(comparator)` | `SortedDict(key_func)` | 是轉換函式，不是比較器 |

⚠️ **頭號陷阱——回傳的是索引，不是鍵。** Java 的 `floorKey/ceilingKey` 回傳鍵
（或 `null`）；Python 的 `bisect_*` 回傳的是**索引**，而它可能是 `-1` 或 `len(d)`。
由於 `keys()[-1]` 會默默回傳**最大的鍵**，少了防護就是*安靜的錯誤答案*，
而不是當場崩潰：

<!--CODE-->

⚠️ **不要重建鍵的串列。** `bisect.bisect_left(list(sd.keys()), x)` 會複製
每一個鍵 = `O(n)`，把 `O(log n)` 的優勢整個毀掉。請用 `sd.bisect_left(x)`。

**使用時機**

| 需求 | 使用 |
|------|-----|
| 快速 `O(1)` 查找，**不需要順序** | 單純的 `dict` / `set` |
| 需要排序，但**只插入一次然後讀取** | 對 `list` 排序（一次 `O(n log n)`） |
| **反覆插入／刪除** + 需要順序 / floor / ceiling / 區間 | **`SortedDict` / `SortedList`** |
| 需要**有序且可重複的值**（multiset） | **`SortedList`** |
| 只需要最小／最大值（中間不需要順序） | `heapq` |

> 只要資料會**隨時間變動**，*而且*你需要「最接近的鍵」、「下一個較大的鍵」或
> 「`[a, b]` 內的所有鍵」，就該拿出 `SortedContainers`。如果陣列是靜態的，
> 一次性排序 + `bisect` 更簡單也更快。

**使用範例 — LC 729 My Calendar I（用 floor/ceiling 檢查重疊）**

這就是那個經典問題：「我該怎麼把這段 Java `TreeMap` 程式碼移植到 Python？」
Java 原版：

<!--CODE-->

**V1) 最貼近的一比一移植 — `SortedDict` + 顯式的 floor/ceiling**

<!--CODE-->

**V2) 更 Python 味的寫法 — 以 `(start, end)` tuple 組成的 `SortedList`** ⭐ *面試首選*

只用一個有序結構，而不是拆成 key/value，重疊條件也能直接讀出來：

<!--CODE-->

**V3) 零相依的退路 — 只用標準函式庫的 `bisect`**

如果 import 被限制成只能用標準函式庫：搜尋仍是 `O(log N)`，但 `list.insert()`
需要搬移元素 → 每次預約 `O(N)`。對 LC 729（≤ 1000 次呼叫）而言綽綽有餘。

<!--CODE-->

**使用範例 — LC 220 Contains Duplicate III（用 `SortedList` 做區間查詢）**

<!--CODE-->

**相關 LeetCode 題目**

| 題目 | LC# | 用到的 TreeMap 操作 |
|---------|-----|-------------------|
| My Calendar I | 729 | 用 floor/ceiling 檢查重疊 |
| My Calendar II | 731 | 用有序映射計算重疊次數 |
| My Calendar III | 732 | 最大重疊數（在有序鍵上做差分陣列） |
| Contains Duplicate III | 220 | 滑動視窗中的 ceiling + 區間檢查 |
| Time Based Key-Value Store | 981 | 對時間戳做 floor |
| Data Stream as Disjoint Intervals | 352 | 透過 floor/ceiling 合併區間 |
| Count of Smaller Numbers After Self | 315 | 由右往左掃描時搭配 `SortedList` + `bisect` |
| Sliding Window Median | 480 | `SortedList` 的新增／移除，再取中間索引 |
| The Skyline Problem | 218 | 高度的 multiset（`SortedList`） |

> Java `TreeMap` 的並列對照，請見
> [hash_map.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/hash_map.md)
> 中的 **TreeMap Pattern (Template 7)**。

<!-- 9f67bbcc8008 -->
## `bisect` — 在有序 list 上做二分搜尋

<!-- 3e28c6bd8d92 -->
### `bisect_left` 與 `bisect_right` ⭐⭐⭐⭐⭐

- 這套演算法讓你在每次插入新元素時 `NOT sorting an array eveytime`（不必整個重新排序） 
<!--CODE-->

<!-- f66cc8c387e8 -->
## `collections`

<!-- a9d3bda962dd -->
### `defaultdict`

<!--CODE-->

<!-- ddc3b1109140 -->
### `Counter` ⭐⭐⭐⭐

<!--CODE-->

<!-- b924d753aa40 -->
### `deque`（雙端佇列） ⭐⭐⭐⭐

<!--CODE-->

<!-- ea10b294034a -->
### `OrderedDict`（雜湊表 + 鏈結串列）⭐⭐⭐

從 Python 3.7 起，一般的 `dict` 就已經保有插入順序，所以 `OrderedDict` 的重點不再是
「**擁有**順序」，而是「**在既有順序裡低成本地搬動某一筆**」——兩個一般 `dict` 沒有的方法，
而且都是 O(1)：

| 呼叫 | 作用 | 一般 `dict` 的等價寫法 |
|---|---|---|
| `d.move_to_end(k)` | 把 `k` 送到**最右端**（最新） | `d[k] = d.pop(k)` — 同樣 O(1)，但多一次雜湊查找 |
| `d.move_to_end(k, last=False)` | 把 `k` 送到**最左端**（最舊） | 沒有一行寫法 — 只能重建整個 dict，O(n) |
| `d.popitem()` | 彈出**最新**的一對（LIFO） | `d.popitem()` — 相同 |
| `d.popitem(last=False)` | 彈出**最舊**的一對（FIFO） | `d.pop(next(iter(d)))` — 仍是 O(1)，但要兩個敘述 |

最後一列就是這個型別在面試裡仍然存在的全部理由：**O(1) 的「淘汰最舊的一筆」**正好就是 LRU
的淘汰步驟，而一般的 `dict` 沒有任何單一呼叫可以做到。

<!--CODE-->

<!-- 236221191812 -->
#### **`OrderedDict` as an LRU cache (LC 146)**

把這個 dict 由左往右讀成「最久沒用到 → 最近剛用到」。每次存取就把該 key 往右搬；淘汰永遠
從左端取。兩者都是上面那些 O(1) 呼叫，這正是讓 `get` 與 `put` 能達到題目要求的 O(1)，而
不必手寫一條雙向鏈結串列的原因。

<!--CODE-->

<!--CODE-->

**陷阱**

- **容量檢查要放在插入之後。** 先淘汰會在「插入的 key 本來就存在」時差一個：對已存在的 key
  做 `put` 從來不會讓 dict 變大，所以它絕不該觸發淘汰。
- **對已存在的 key 做 `put` 也算一次使用。** 在那個分支漏掉 `move_to_end`，key 就會留在舊
  位置而被太早淘汰。（單獨寫 `self.cache[key] = value` **不會**重排一個已存在的 key——只有
  新 key 才會落在最右端。）
- **`move_to_end` 對不存在的 key 會丟 `KeyError`**；`popitem` 對空 dict 也會丟 `KeyError`。
  兩者都靠你原本就在做的成員檢查擋住。
- **`last` 是命名上的陷阱，不是語法上的**：`popitem(last=False)` 彈出的是**最舊**的那筆。
  把它讀成「彈出最後一筆」會得到一個 MRU 快取——它會通過第一個範例，然後其餘全錯。

<!-- 503f7911ebf9 -->
#### **When a plain `dict` is not enough**

<!--CODE-->

其他情況優先用一般的 `dict`：它更快也更輕。只有在需要 LRU 式的重排（LC 146）、FIFO 淘汰，
或需要對順序敏感的相等比較時，才動用 `OrderedDict`。

**還會在哪裡出現**

| 題目 | 用法 |
|---|---|
| LC 146 LRU Cache | 上面那個範本 |
| LC 460 LFU Cache | **每個頻率桶各一個** `OrderedDict`；`popitem(last=False)` 用最近使用時間打破 LFU 的平手 |
| LC 1670 Design Front Middle Back Queue | 兩端都是 O(1) — 不過一般解法是兩個 `deque` |

- Java 的對應物是 `LinkedHashMap`（搭配 `accessOrder=true` 與 `removeEldestEntry`）——
  見 [java_trick_collections.md](./java_trick_collections.md)。
- 想看「選容器」的角度而非 API，見 [Collection.md](./Collection.md)；想看 LC 146 同樣接受的
  手寫雜湊表 + 雙向鏈結串列，見 [design.md](./design.md)。

<!-- d5d7f6d0ab5b -->
## `itertools`

<!-- f16410e4dd23 -->
### `itertools` — product、permutations、combinations、accumulate

<!--CODE-->

<!-- d7d6945ad01c -->
## `functools`

<!-- a279008bb358 -->
### `lru_cache`、`cmp_to_key` 及其同伴

- functools.lru_cache
    - 在 Python 中以 LRU（Least Recently Used，最近最少使用）方式實作快取
- 參考
    - https://walkonnet.com/archives/451257
    - https://docs.python.org/3/library/functools.html
<!--CODE-->

<!-- 59659e7580e7 -->
### `reduce()`

<!--CODE-->

<!-- 07eb08cd6ce9 -->
## `datetime`

<!-- bd9b33751725 -->
### `datetime` ↔ `str`

<!--CODE-->
