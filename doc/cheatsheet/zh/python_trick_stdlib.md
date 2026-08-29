# 面試用的 Python 標準函式庫

> **範圍** — 逐個函式庫的參考手冊：`heapq`、`sortedcontainers`、`bisect`、`collections`、`itertools`、`functools` 與 `datetime`——每個呼叫、它們的複雜度，以及各自是為了取代哪一種手寫寫法而存在。
> **另見** — [python_trick.md](./python_trick.md) — 這些函式庫所建立在其上的語言慣用寫法；[python_trick_indexing.md](./python_trick_indexing.md) — 插入與切片的索引運算，包括 `bisect.insort` 的定位；[heap.md](./heap.md)、[binary_search.md](./binary_search.md)、[hash_map.md](./hash_map.md) — 這些資料結構與演算法本身（而非它們的 Python API）；[java_trick_collections.md](./java_trick_collections.md) — 對應的 Java 版本。

## LeetCode 題目清單

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)
- [Binary Search](https://leetcode.com/problem-list/binary-search/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)

## 總覽

本文從 [python_trick.md](./python_trick.md) 拆分出來——這七個模組原本佔了該檔案約三分之一的篇幅，
而且和不相干的語言慣用寫法交錯在一起。

### 關鍵性質
- **複雜度**：逐模組列出——這正是你該用它們而不是自己寫迴圈的理由
- **核心想法**：Python 沒有 `TreeMap`，也沒有最大堆積；`sortedcontainers` 與取負號就是標準答案，而知道這件事往往就是整道題的關鍵
- **使用時機**：當演算法已經想好，剩下的問題只是「該用哪個呼叫把它寫出來」


## `heapq` — 優先佇列

### `heapq` 基礎（預設為最小堆積）


**heapq** - 堆積佇列演算法（優先佇列）
- 預設是最小堆積（最小的元素位於索引 0）
- 要做最大堆積，就把值取負號或使用自訂比較
- 常見的面試使用情境：Top K 元素、第 K 大／小、合併 K 個有序串列

**參考資料：**
- https://docs.python.org/3/library/heapq.html
- https://github.com/python/cpython/blob/3.10/Lib/heapq.py

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

```text
n = 15 (complete tree, 4 levels)

level        shape           #nodes   max sift-down   work
  0            o                1           3           3
  1          o   o              2           2           4
  2         o o o o             4           1           4
  3       o o o o o o o o       8           0           0   <- half the nodes, FREE
                                                      ----
                                              total =  11   (< 2n = 30)
```

**實用準則：** 元素一開始就全部拿到了？→ 用 `heapify`（O(n)）。元素是一個一個進來的（串流）？→ 用 `heappush`（每次 O(log n)）。

```python
import heapq

#-------------------------------
# Basic Heap Operations
#-------------------------------

# Create empty heap
heap = []

# heappush: Add element to heap
# Time: O(log n)
heapq.heappush(heap, 5)
heapq.heappush(heap, 3)
heapq.heappush(heap, 7)
heapq.heappush(heap, 1)
print(heap)  # [1, 3, 7, 5] - min heap property maintained

# heappop: Remove and return smallest element
# Time: O(log n)
smallest = heapq.heappop(heap)
print(smallest)  # 1
print(heap)      # [3, 5, 7]

# heappushpop: Push then pop (more efficient than separate operations)
# Time: O(log n)
result = heapq.heappushpop(heap, 2)  # Push 2, then pop smallest
print(result)  # 2
print(heap)    # [3, 5, 7]

# heapreplace: Pop then push (more efficient than separate operations)
# Time: O(log n)
result = heapq.heapreplace(heap, 4)  # Pop smallest, then push 4
print(result)  # 3
print(heap)    # [4, 5, 7]

#-------------------------------
# PEEK : get TOP element WITHOUT popping  ***
#-------------------------------

# !!! heapq has NO peek() function
# -> the heap IS a plain python list, and the heap invariant guarantees
#    the SMALLEST element sits at index 0  =>  heap[0] IS the peek
# Time: O(1)

top = heap[0]
print(top)   # 4  -> heap NOT modified
print(heap)  # [4, 5, 7]

# safe peek (heap[0] raises IndexError when heap is empty)
top = heap[0] if heap else None

# max heap : push NEGATED values, negate back when peeking
max_heap = []
for v in [5, 3, 7]:
    heapq.heappush(max_heap, -v)
largest = -max_heap[0]   # 7   (peek, NOT pop)

# ── other ways to peek, and why they are WORSE ──
#   heap[0]                    -> O(1)   ✅ idiomatic
#   heapq.nsmallest(1, heap)[0]-> O(n)   ❌ scans whole list
#   min(heap)                  -> O(n)   ❌ ignores heap structure
#   pq.queue[0]                -> O(1)   only for queue.PriorityQueue (thread-safe wrapper)

# ── ⚠️ GOTCHA 1 : ONLY index 0 is meaningful ──
h = [1, 3, 9, 7, 5]   # a VALID min heap
h[0]    # 1  ✅ guaranteed smallest
h[1]    # 3  ❌ NOT necessarily the 2nd smallest
h[-1]   # 5  ❌ NOT the largest
# a heap is only PARTIALLY ordered! for the 2nd smallest -> sorted(h)[1] (O(n log n))

# ── ⚠️ GOTCHA 2 : IndexError on empty heap -> guard FIRST (short-circuit) ──
pq = []
while pq and pq[0] < 10:   # ✅ `pq and ...` must come first
    heapq.heappop(pq)
# while pq[0] < 10 and pq:  # ❌ IndexError

# ── classic use : LAZY DELETION (peek -> drop stale tops) ──
# LC 3092 Most Frequent IDs / LC 218 Skyline / LC 1834 Single-Threaded CPU
# heapq can NOT remove an element from the middle, so we mark entries stale
# and pop them only when they surface at the top.
#
#   while pq and -pq[0][0] != c_map[pq[0][1]]:   # peek, compare, discard
#       heapq.heappop(pq)
#   ans = -pq[0][0] if pq else 0                 # now the top is VALID

# ── avoid a separate peek when you REPLACE the top anyway ──
# heapq.heapreplace(h, x)   # pop then push -> 1 sift (heap must be non-empty)
# heapq.heappushpop(h, x)   # push then pop -> cheaper when x <= h[0]

# ── java comparison ──
#   python : heap[0]      -> IndexError if empty  (NO peek() exists)
#   java   : pq.peek()    -> null if empty
#            pq.element() -> throws NoSuchElementException if empty

#-------------------------------
# Convert List to Heap
#-------------------------------

# heapify: Transform list into heap in-place
# Time: O(n) - more efficient than n × heappush
nums = [5, 7, 9, 1, 3]
heapq.heapify(nums)
print(nums)  # [1, 3, 9, 7, 5] - min heap

#-------------------------------
# Top K Elements (Most Common Interview Pattern)
#-------------------------------

# nsmallest: Find k smallest elements
# Time: O(n log k)
nums = [5, 7, 9, 1, 3, 4, 6, 8, 2]
k_smallest = heapq.nsmallest(3, nums)
print(k_smallest)  # [1, 2, 3]

# nlargest: Find k largest elements
# Time: O(n log k)
k_largest = heapq.nlargest(3, nums)
print(k_largest)  # [9, 8, 7]

# With key function (common in LC problems)
people = [(1, 'Alice'), (3, 'Bob'), (2, 'Charlie')]
top_2_by_id = heapq.nsmallest(2, people, key=lambda x: x[0])
print(top_2_by_id)  # [(1, 'Alice'), (2, 'Charlie')]

#-------------------------------
# Max Heap Pattern (Negate Values)
#-------------------------------

# Python heapq is min heap, for max heap: negate values
max_heap = []
for val in [5, 7, 9, 1, 3]:
    heapq.heappush(max_heap, -val)  # Negate for max heap

# Get largest element
largest = -heapq.heappop(max_heap)
print(largest)  # 9

# Example: Top K Frequent Elements
from collections import Counter
def topKFrequent(nums, k):
    count = Counter(nums)
    # Use negative frequency for max heap
    return heapq.nlargest(k, count.keys(), key=count.get)

#-------------------------------
# Merge K Sorted Lists/Arrays
#-------------------------------

# merge: Merge multiple sorted iterables
# Time: O(n log k) where k = number of iterables
list1 = [1, 3, 5]
list2 = [2, 4, 6]
list3 = [0, 7, 8]
merged = list(heapq.merge(list1, list2, list3))
print(merged)  # [0, 1, 2, 3, 4, 5, 6, 7, 8]

# Custom comparison for merge
# Example: Merge by second element of tuple
data1 = [(1, 'a'), (3, 'c')]
data2 = [(2, 'b'), (4, 'd')]
merged = list(heapq.merge(data1, data2, key=lambda x: x[0]))
print(merged)  # [(1, 'a'), (2, 'b'), (3, 'c'), (4, 'd')]

#-------------------------------
# Common LeetCode Patterns
#-------------------------------

# Pattern 1: Kth Largest Element (LC 215)
def findKthLargest(nums, k):
    # Use min heap of size k
    heap = nums[:k]
    heapq.heapify(heap)

    for num in nums[k:]:
        if num > heap[0]:
            heapq.heapreplace(heap, num)

    return heap[0]

# Pattern 2: Top K Frequent Elements (LC 347)
def topKFrequent(nums, k):
    count = Counter(nums)
    return heapq.nlargest(k, count.keys(), key=count.get)

# Pattern 3: Kth Smallest in Sorted Matrix (LC 378)
def kthSmallest(matrix, k):
    """
    Use heap to track smallest elements across rows
    """
    n = len(matrix)
    heap = []

    # Add first element from each row
    for r in range(min(k, n)):
        heapq.heappush(heap, (matrix[r][0], r, 0))

    result = 0
    for _ in range(k):
        result, r, c = heapq.heappop(heap)
        if c + 1 < len(matrix[0]):
            heapq.heappush(heap, (matrix[r][c+1], r, c+1))

    return result

# Pattern 4: Merge K Sorted Lists (LC 23)
def mergeKLists(lists):
    """
    Merge k sorted linked lists
    """
    heap = []
    # Initialize heap with first node from each list
    for i, lst in enumerate(lists):
        if lst:
            heapq.heappush(heap, (lst.val, i, lst))

    dummy = ListNode(0)
    current = dummy

    while heap:
        val, i, node = heapq.heappop(heap)
        current.next = node
        current = current.next

        if node.next:
            heapq.heappush(heap, (node.next.val, i, node.next))

    return dummy.next

# Pattern 5: Sliding Window Maximum (LC 239)
# Note: Usually solved with deque, but can use heap
def maxSlidingWindow(nums, k):
    """
    Use max heap (negate values) with index tracking
    """
    heap = []
    result = []

    for i, num in enumerate(nums):
        # Add to max heap (negate for max heap)
        heapq.heappush(heap, (-num, i))

        # Remove elements outside window
        while heap[0][1] <= i - k:
            heapq.heappop(heap)

        # Window is full
        if i >= k - 1:
            result.append(-heap[0][0])

    return result

#-------------------------------
# Advanced: Custom Comparison with Classes
#-------------------------------

# For complex objects, use tuples or dataclass with functools.total_ordering
from dataclasses import dataclass
from functools import total_ordering

@total_ordering
@dataclass
class Task:
    priority: int
    name: str

    def __lt__(self, other):
        return self.priority < other.priority

# Use with heapq
task_heap = []
heapq.heappush(task_heap, Task(3, "Low priority"))
heapq.heappush(task_heap, Task(1, "High priority"))
heapq.heappush(task_heap, Task(2, "Medium priority"))

top_task = heapq.heappop(task_heap)
print(top_task)  # Task(priority=1, name='High priority')

#-------------------------------
# Performance Tips
#-------------------------------

# 1. Use heapify() instead of repeated heappush() - O(n) vs O(n log n)
# SLOW:
heap = []
for num in nums:
    heapq.heappush(heap, num)  # O(n log n)

# FAST:
heap = nums[:]
heapq.heapify(heap)  # O(n)

# 2. Use nsmallest/nlargest for small k
# When k << n: nsmallest/nlargest are optimized
# When k ≈ n: just sort the array

# 3. For Top K problems with streaming data: maintain heap of size k
```

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


### 用取負號模擬最大堆積


Python 的 `heapq` 只實作了**最小堆積**——`heapify()` 沒有 `reverse=True` 這種選項。

要模擬**最大堆積**，就把優先權鍵值取負號：

```python
# Instead of storing (dist, x, y), store (-dist, x, y)

from heapq import heapify, heappop

pq = [(-10, "A"), (-5, "B"), (-20, "C")]
heapify(pq)

print(heappop(pq))  # (-20, 'C')  ← largest original dist (20) comes out first
print(heappop(pq))  # (-10, 'A')
print(heappop(pq))  # (-5,  'B')
```

**推入的寫法：**
```python
import heapq

max_heap = []
heapq.heappush(max_heap, (-priority, value))

# Pop: negate back to get original value
neg_pri, val = heapq.heappop(max_heap)
print(-neg_pri, val)
```

**多鍵值範例（主鍵遞減、次鍵遞增）：**
```python
# Sort by dist descending; on tie, by name ascending
heapq.heappush(pq, (-dist, name))
```

> **經驗法則**：想讓哪個欄位遞減，就把那個欄位取負號；其餘的保持原樣。

## `sortedcontainers` — 有序映射

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

```python
from sortedcontainers import SortedDict, SortedList

# ---------------- SortedDict (TreeMap) ----------------
sd = SortedDict()

# O(log n) basic ops
sd[key] = value          # insert / update
v = sd.get(key)          # lookup (None if missing)
del sd[key]              # delete
key in sd                # membership

# Ordered access — keys() is an indexable sorted view
sd.keys()[0]             # min key
sd.keys()[-1]            # max key
sd.peekitem(0)           # (min_key, val)
sd.peekitem(-1)          # (max_key, val)

# Floor / Ceiling via bisect methods on the keys (THE TreeMap superpower)
keys = sd.keys()
i = sd.bisect_left(target)    # first index with key >= target
j = sd.bisect_right(target)   # first index with key >  target
#   ceiling(target) = keys[i]      if i < len(sd)      (smallest key >= target)
#   floor(target)   = keys[j - 1]  if j > 0            (largest  key <= target)

# Range query: iterate keys in [lo, hi)
lo_i = sd.bisect_left(lo)
hi_i = sd.bisect_left(hi)
for k in sd.keys()[lo_i:hi_i]:
    process(k, sd[k])

# Range query, cleaner: irange (inclusive both ends by default)
for k in sd.irange(lo, hi):
    process(k, sd[k])

sd.popitem(0)            # pollFirstEntry()
sd.popitem(-1)           # pollLastEntry()

# ---------------- SortedList (TreeSet / multiset) ----------------
sl = SortedList()
sl.add(x)                # O(log n) insert, stays sorted
sl.remove(x)             # O(log n) delete one occurrence
sl[0], sl[-1]            # min / max
i = sl.bisect_left(x)    # floor/ceiling index, same idea as above
```

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

```python
# ✅ the safe floor / ceiling idiom — memorize this
i = d.bisect_left(x)
ceil_key = d.keys()[i] if i < len(d) else None       # ceilingKey(x)

j = d.bisect_right(x) - 1
floor_key = d.keys()[j] if j >= 0 else None          # floorKey(x)
```

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

```java
// java
// LC 729 - My Calendar I
TreeMap<Integer, Integer> calendar = new TreeMap<>();

public boolean book(int start, int end) {
    Integer prev = calendar.floorKey(start);    // largest start <= start
    Integer next = calendar.ceilingKey(start);  // smallest start >= start

    if ((prev == null || calendar.get(prev) <= start) &&
        (next == null || end <= next)) {
        calendar.put(start, end);
        return true;
    }
    return false;
}
```

**V1) 最貼近的一比一移植 — `SortedDict` + 顯式的 floor/ceiling**

```python
# python
# LC 729 - My Calendar I
# IDEA: mirror floorKey / ceilingKey with bisect_right / bisect_left, then guard the index
from sortedcontainers import SortedDict

class MyCalendar:
    # time = O(log N) per booking, space = O(N)
    def __init__(self):
        self.calendar = SortedDict()   # {start: end}

    def book(self, start: int, end: int) -> bool:
        keys = self.calendar.keys()

        # floorKey(start): largest key <= start
        i = self.calendar.bisect_right(start)
        prev = keys[i - 1] if i > 0 else None

        # ceilingKey(start): smallest key >= start
        j = self.calendar.bisect_left(start)
        nxt = keys[j] if j < len(keys) else None

        if (prev is None or self.calendar[prev] <= start) and \
           (nxt is None or end <= nxt):
            self.calendar[start] = end
            return True
        return False
```

**V2) 更 Python 味的寫法 — 以 `(start, end)` tuple 組成的 `SortedList`** ⭐ *面試首選*

只用一個有序結構，而不是拆成 key/value，重疊條件也能直接讀出來：

```python
# python
# LC 729 - My Calendar I
# IDEA: keep intervals sorted as tuples; only the neighbours at the insert
#       position can possibly overlap
from sortedcontainers import SortedList

class MyCalendar:
    # time = O(log N) per booking, space = O(N)
    def __init__(self):
        self.books = SortedList()      # sorted (start, end) tuples

    def book(self, start: int, end: int) -> bool:
        i = self.books.bisect_left((start, end))
        if i > 0 and self.books[i - 1][1] > start:            # prev spills into us
            return False
        if i < len(self.books) and end > self.books[i][0]:    # we spill into next
            return False
        self.books.add((start, end))
        return True
```

**V3) 零相依的退路 — 只用標準函式庫的 `bisect`**

如果 import 被限制成只能用標準函式庫：搜尋仍是 `O(log N)`，但 `list.insert()`
需要搬移元素 → 每次預約 `O(N)`。對 LC 729（≤ 1000 次呼叫）而言綽綽有餘。

```python
# python
# LC 729 - My Calendar I
import bisect

class MyCalendar:
    # time = O(N) per booking (list shifting), space = O(N)
    def __init__(self):
        self.books = []                # sorted (start, end) tuples

    def book(self, start: int, end: int) -> bool:
        i = bisect.bisect_left(self.books, (start, end))
        if i > 0 and self.books[i - 1][1] > start:
            return False
        if i < len(self.books) and end > self.books[i][0]:
            return False
        self.books.insert(i, (start, end))
        return True
```

**使用範例 — LC 220 Contains Duplicate III（用 `SortedList` 做區間查詢）**

```python
from sortedcontainers import SortedList

def containsNearbyAlmostDuplicate(nums, indexDiff, valueDiff):
    # keep a sliding window of the last `indexDiff` values, kept sorted
    window = SortedList()
    for i, num in enumerate(nums):
        # ceiling: smallest value >= num - valueDiff
        pos = window.bisect_left(num - valueDiff)
        if pos < len(window) and window[pos] <= num + valueDiff:
            return True
        window.add(num)
        if len(window) > indexDiff:        # evict the value that falls out of window
            window.remove(nums[i - indexDiff])
    return False
```

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

## `bisect` — 在有序 list 上做二分搜尋

### `bisect_left` 與 `bisect_right`

- 這套演算法讓你在每次插入新元素時 `NOT sorting an array eveytime`（不必整個重新排序） 
```text
# https://docs.python.org/zh-tw/3/library/bisect.html
# src code : https://github.com/python/cpython/blob/3.10/Lib/bisect.py
# https://myapollo.com.tw/zh-tw/python-bisect/
# https://www.liujiangblog.com/course/python/57


"""
NOTE !!! before using bisect, we need SORT the array
"""

#-------------------------------
# bisect_left
#-------------------------------
# will return an idx for inserting new element a, and keep the new array sorted, if element a already existed in array, will insert to "original" a's left idx

# example 1
In [3]: import bisect
   ...: a = [2,4,6]
   ...: idx = bisect.bisect_left(a, 3)
   ...: print (idx)
   ...:
   ...: a.insert(idx, 3)
   ...: print (a)
   ...:
1
[2, 3, 4, 6]


# example 2
In [4]: import bisect
   ...: a = [2, 4, 6]
   ...: idx = bisect.bisect_left(a, 4)
   ...: print (idx)
   ...:
   ...: a.insert(idx, 4)
   ...: print (a)
1
[2, 4, 4, 6]

#-------------------------------
# bisect_right
#-------------------------------
# will return an idx for inserting new element a, and keep the new array sorted, if element a already existed in array, will insert to "original" a's right idx

In [5]:
   ...: import bisect
   ...: a = a = [2, 2, 4, 4, 6, 6, 8, 8]
   ...: idx = bisect.bisect_right(a, 4)
   ...: print (idx)
   ...:
   ...: a.insert(idx, 4)
   ...: print (a)
4
[2, 2, 4, 4, 4, 6, 6, 8, 8]

#-------------------------------
# bisect
#-------------------------------
# bisect.bisect : 
#   -> similar as bisect.bisect_right
#   -> similar as bisect.bisect_left, but will insert to element RIGHT instead
# https://docs.python.org/zh-tw/3/library/bisect.html
# https://blog.csdn.net/qq_34914551/article/details/100062973

# example 1
# In [3]: import bisect
#    ...: a = [2, 4, 6, 8]
#    ...: idx = bisect.bisect(a, 7)
#    ...: print (idx)
#    ...: a.insert(idx, 7)
#    ...: print (a)
# 3
# [2, 4, 6, 7, 8]

#-------------------------------
# insort, insort_right, insort_left
#-------------------------------
# insort, insort_right, insort_left : will get idx and insert to array  (with idx) directly

# example 1
In [8]: import bisect
   ...: a = [2, 4, 6, 8]
   ...: bisect.insort_left(a, 4)
   ...: print (a)
   ...:
[2, 4, 4, 6, 8]

# exmaple 2
In [7]: import bisect
   ...: a = [2, 4, 6, 8]
   ...: bisect.insort_right(a, 4)
   ...: print (a)
[2, 4, 4, 6, 8]
```

## `collections`

### `defaultdict`

```python
# defaultdict never raises KeyError — returns a default value for missing keys
from collections import defaultdict

#----------------------------
# example 1 : int (default 0)
#----------------------------
d = defaultdict(int)
for ch in "aabbbc":
    d[ch] += 1
print(dict(d))  # {'a': 2, 'b': 3, 'c': 1}

#----------------------------
# example 2 : list (default [])
#----------------------------
graph = defaultdict(list)
edges = [(0,1),(0,2),(1,3)]
for u, v in edges:
    graph[u].append(v)
    graph[v].append(u)
# graph[0] -> [1, 2],  graph[99] -> []  (no KeyError)

#----------------------------
# example 3 : set (default set())
#----------------------------
d = defaultdict(set)
d['key'].add(1)
d['key'].add(2)
print(d['key'])  # {1, 2}

#----------------------------
# example 4 : nested defaultdict (adjacency matrix with weights)
#----------------------------
dist = defaultdict(lambda: defaultdict(lambda: float('inf')))
dist[0][1] = 5
print(dist[0][1])   # 5
print(dist[0][99])  # inf
```

### `Counter`

```python
from collections import Counter

#----------------------------
# basic usage
#----------------------------
c = Counter("aabbbc")
print(c)            # Counter({'b': 3, 'a': 2, 'c': 1})
print(c['b'])       # 3
print(c['z'])       # 0  (no KeyError, returns 0)

# most_common(k) : top k frequent elements
print(c.most_common(2))   # [('b', 3), ('a', 2)]

# Counter arithmetic
c1 = Counter("aab")
c2 = Counter("abc")
print(c1 + c2)  # Counter({'a': 3, 'b': 2, 'c': 1})
print(c1 - c2)  # Counter({'a': 1})  (only positive counts)
print(c1 & c2)  # Counter({'a': 1, 'b': 1})  intersection (min)
print(c1 | c2)  # Counter({'a': 2, 'b': 1, 'c': 1})  union (max)

# update (add) vs subtract
c = Counter({'a': 3})
c.update({'a': 1, 'b': 2})
print(c)    # Counter({'a': 4, 'b': 2})
c.subtract({'a': 2})
print(c)    # Counter({'a': 2, 'b': 2})

# LC 347 Top K Frequent Elements
from collections import Counter
def topKFrequent(nums, k):
    return [x for x, _ in Counter(nums).most_common(k)]
```

### `deque`（雙端佇列）

```python
from collections import deque

# deque() vs deque([]) — both produce an empty deque, functionally identical
q = deque()    # preferred: no unnecessary empty list created
q = deque([])  # equivalent but verbose; the [] is an extra throwaway object

# O(1) append/pop from BOTH ends (list.pop(0) is O(n))
d = deque([1, 2, 3])

d.append(4)       # [1, 2, 3, 4]
d.appendleft(0)   # [0, 1, 2, 3, 4]
d.pop()           # removes 4  -> [0, 1, 2, 3]
d.popleft()       # removes 0  -> [1, 2, 3]

# maxlen: auto-evicts oldest element (sliding window)
d = deque(maxlen=3)
for i in range(5):
    d.append(i)
print(d)  # deque([2, 3, 4], maxlen=3)

#----------------------------
# BFS template with deque
#----------------------------
def bfs(graph, start):
    visited = set([start])
    queue = deque([start])
    while queue:
        node = queue.popleft()
        for neighbor in graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)

#----------------------------
# Monotonic deque (LC 239 Sliding Window Maximum)
#----------------------------
def maxSlidingWindow(nums, k):
    d = deque()   # stores indices, decreasing order of values
    result = []
    for i, num in enumerate(nums):
        while d and nums[d[-1]] <= num:
            d.pop()
        d.append(i)
        if d[0] == i - k:   # front out of window
            d.popleft()
        if i >= k - 1:
            result.append(nums[d[0]])
    return result
```

### `OrderedDict`（雜湊表 + 鏈結串列）

- 參見 [Collection.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/Collection.md)

## `itertools`

### `itertools` — product、permutations、combinations、accumulate

```text
# https://docs.python.org/zh-cn/3/library/itertools.html
# https://docs.python.org/zh-tw/3/library/itertools.html

# itertools — Functions creating iterators for efficient looping

#-----------------------------------------------------------------------------------------------------
# example 1 : itertools.accumulate : Aggregated sum
#-----------------------------------------------------------------------------------------------------
In [10]: import itertools
    ...: x = itertools.accumulate(range(10))
    ...: print (list(x))
[0, 1, 3, 6, 10, 15, 21, 28, 36, 45]

#-----------------------------------------------------------------------------------------------------
# example 2 : itertools.combinations : get NON-duplicated elements from collections (with given len)
#-----------------------------------------------------------------------------------------------------
In [15]:  x = itertools.combinations(range(4), 3)

In [16]: print (list(x))
[(0, 1, 2), (0, 1, 3), (0, 2, 3), (1, 2, 3)]

In [17]: x = itertools.combinations(range(4), 4)

In [18]: print (list(x))
[(0, 1, 2, 3)]

#-----------------------------------------------------------------------------------------------------
# example 3 : itertools.combinations_with_replacement : get duplicated or non-duplicated elements from collections (with given len)
#-----------------------------------------------------------------------------------------------------
In [19]: x = itertools.combinations_with_replacement('ABC', 2)

In [20]: print(list(x))
[('A', 'A'), ('A', 'B'), ('A', 'C'), ('B', 'B'), ('B', 'C'), ('C', 'C')]

In [21]: x = itertools.combinations_with_replacement('ABC', 1)

In [22]: print(list(x))
[('A',), ('B',), ('C',)]

In [24]: x = itertools.combinations_with_replacement([1,2,2,1], 2)

In [25]: print (list(x))
[(1, 1), (1, 2), (1, 2), (1, 1), (2, 2), (2, 2), (2, 1), (2, 2), (2, 1), (1, 1)]

#-----------------------------------------------------------------------------------------------------
# example 4 : itertools.compress : filter elements by True/False
#-----------------------------------------------------------------------------------------------------
In [26]: x = itertools.compress(range(5), (True, False, True, True, False))

In [27]: print (list(x))
[0, 2, 3]

#-----------------------------------------------------------------------------------------------------
# example 5 : itertools.count : a counter, can define start point and path len
#-----------------------------------------------------------------------------------------------------
# NOTE THIS !!!!
In [2]: x = itertools.count(start=20, step=-1)

In [3]: print(list(itertools.islice(x, 0, 10, 1)))
[20, 19, 18, 17, 16, 15, 14, 13, 12, 11]

#-----------------------------------------------------------------------------------------------------
# example 6 : itertools.groupby : group by lists by value
#-----------------------------------------------------------------------------------------------------
In [4]: x = itertools.groupby(range(10), lambda x: x < 5 or x > 8)

In [5]: for condition, numbers in x:
   ...:     print(condition, list(numbers))
   ...:
True [0, 1, 2, 3, 4]
False [5, 6, 7, 8]
True [9]

#-----------------------------------------------------------------------------------------------------
# example 7 : itertools.islice : slice on iterator
#-----------------------------------------------------------------------------------------------------
# https://docs.python.org/3/library/itertools.html#itertools.islice
# syntax : itertools.islice(seq, [start,] stop [, step])

In [6]:  x = itertools.islice(range(10), 0, 9, 2)

In [7]: print (list(x))
[0, 2, 4, 6, 8]

#-----------------------------------------------------------------------------------------------------
# example 8 : itertools.permutations : generate all combinations (ordering mattered)
#-----------------------------------------------------------------------------------------------------

In [9]: x = itertools.permutations(range(4), 3)

In [10]: print(list(x))
[(0, 1, 2), (0, 1, 3), (0, 2, 1), (0, 2, 3), (0, 3, 1), (0, 3, 2), (1, 0, 2), (1, 0, 3), (1, 2, 0), (1, 2, 3), (1, 3, 0), (1, 3, 2), (2, 0, 1), (2, 0, 3), (2, 1, 0), (2, 1, 3), (2, 3, 0), (2, 3, 1), (3, 0, 1), (3, 0, 2), (3, 1, 0), (3, 1, 2), (3, 2, 0), (3, 2, 1)]

#-----------------------------------------------------------------------------------------------------
# example 9 : itertools.product : generate multiple lists, and iterators's product
#-----------------------------------------------------------------------------------------------------

In [11]:  x = itertools.product('ABC', range(3))

In [12]: print(list(x))
[('A', 0), ('A', 1), ('A', 2), ('B', 0), ('B', 1), ('B', 2), ('C', 0), ('C', 1), ('C', 2)]
```

## `functools`

### `lru_cache`、`cmp_to_key` 及其同伴

- functools.lru_cache
    - 在 Python 中以 LRU（Least Recently Used，最近最少使用）方式實作快取
- 參考
    - https://walkonnet.com/archives/451257
    - https://docs.python.org/3/library/functools.html
```python
# example 1
@lru_cache
def count_vowels(sentence):
    return sum(sentence.count(vowel) for vowel in 'AEIOUaeiou')

# example 2
@lru_cache(maxsize=32)
def get_pep(num):
    'Retrieve text of a Python Enhancement Proposal'
    resource = 'https://www.python.org/dev/peps/pep-%04d/' % num
    try:
        with urllib.request.urlopen(resource) as s:
            return s.read()
    except urllib.error.HTTPError:
        return 'Not Found'

# example 3
@lru_cache(maxsize=None)
def fib(n):
    if n < 2:
        return n
    return fib(n-1) + fib(n-2)

# example 4
@api.route("/user/info", methods=["GET"])
@functools.lru_cache()
@login_require
def get_userinfo_list():
    userinfos = UserInfo.query.all()
    userinfo_list = [user.to_dict() for user in userinfos]
    return jsonify(userinfo_list)
```

### `reduce()`

```python
from functools import reduce

# reduce(func, iterable[, initializer])
# Applies func cumulatively: func(func(a, b), c) ...

product = reduce(lambda a, b: a * b, [1, 2, 3, 4])   # 24
total   = reduce(lambda a, b: a + b, [1, 2, 3, 4], 0) # 10

# XOR all elements (LC 136 Single Number)
from functools import reduce
import operator
result = reduce(operator.xor, [4, 1, 2, 1, 2])  # 4
# equivalent to:
result = reduce(lambda a, b: a ^ b, [4, 1, 2, 1, 2])
```

## `datetime`

### `datetime` ↔ `str`

```python
# LC 681.Next Closest Time
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/next-closest-time.py

from datetime import datetime, timedelta

x = "10:20"

#--------------------------------------
# strptime : string -> datetime 
# (Return a datetime corresponding to date_string, parsed according to format.)
#--------------------------------------
x_datetime =  datetime.strptime(x, "%H:%M")
print (x_datetime)
# 1900-01-01 10:20:00


#--------------------------------------
# strftime : datetime -> string 
# (Return a string representing the date)
#--------------------------------------
x_str = x_datetime.strftime("%H:%M")
print (x_str)
# 10:20

# eatra : timedelta
tmp = x_datetime + timedelta(minutes=10)
print (tmp)
# 1900-01-01 10:30:00
```
