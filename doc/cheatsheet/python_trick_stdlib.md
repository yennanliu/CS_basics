# Python Standard Library for Interviews

> **Scope** — The library-by-library reference: `heapq`, `sortedcontainers`, `bisect`, `collections`, `itertools`, `functools` and `datetime` — the calls, their complexity, and the idiom each one exists to replace.
> **See also**: [python_trick.md](./python_trick.md) — the language idioms these sit on top of; [python_trick_indexing.md](./python_trick_indexing.md) — insertion and slicing arithmetic, including where `bisect.insort` fits; [heap.md](./heap.md), [binary_search.md](./binary_search.md), [hash_map.md](./hash_map.md) — the structures and algorithms rather than their Python APIs; [java_trick_collections.md](./java_trick_collections.md) — the Java equivalents.

## LeetCode Problem Lists

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)
- [Binary Search](https://leetcode.com/problem-list/binary-search/)
- [Hash Table](https://leetcode.com/problem-list/hash-table/)

## Overview

Split out of [python_trick.md](./python_trick.md), where these seven modules took up roughly a
third of the file interleaved with unrelated language idioms.

### Key Properties
- **Complexity**: stated per module — it is the reason to reach for these rather than write the loop
- **Core Idea**: Python has no `TreeMap` and no max-heap; `sortedcontainers` and negation are the standard answers, and knowing that is often the whole question
- **When to Use**: when the algorithm is decided and the remaining question is which call expresses it


## `heapq` — Priority Queue

### `heapq` basics (min-heap by default)


**heapq** - Heap queue algorithm (priority queue)
- Min Heap by default (smallest element at index 0)
- For Max Heap, negate values or use custom comparison
- Common interview use cases: Top K elements, Kth largest/smallest, merge K sorted lists

**References:**
- https://docs.python.org/3/library/heapq.html
- https://github.com/python/cpython/blob/3.10/Lib/heapq.py

#### **heapq main operations (complexity cheat table)**

| Operation | Time | Space | What it does / why that cost |
|-----------|------|-------|------------------------------|
| `heapq.heapify(lst)` | **O(n)** | O(1) *(in-place)* | Turns an arbitrary list into a valid min heap **in place**. Cheap because it sifts **bottom-up** (see note below), NOT n × push |
| `heapq.heappush(h, x)` | **O(log n)** | O(1) | Append at the end, then **sift up** at most `log n` levels |
| `heapq.heappop(h)` | **O(log n)** | O(1) | Move last element to root, then **sift down** at most `log n` levels |
| `h[0]` (peek) | **O(1)** | O(1) | Heap invariant guarantees the min sits at index 0. `heapq` has **no** `peek()` |
| `heapq.heappushpop(h, x)` | **O(log n)** | O(1) | Push then pop, **1 sift** instead of 2. Free (`O(1)`) when `x <= h[0]` |
| `heapq.heapreplace(h, x)` | **O(log n)** | O(1) | Pop then push, **1 sift**. Heap must be **non-empty** (else `IndexError`) |
| `heapq.nsmallest(k, it)` | **O(n log k)** | O(k) | Keeps a size-k heap while scanning. Falls back to `sorted()` when k is close to n |
| `heapq.nlargest(k, it)` | **O(n log k)** | O(k) | Same, with a reversed comparison |
| `heapq.merge(*iters)` | **O(N log k)** | O(k) | Lazily merges k **already sorted** iterables; returns a generator, does NOT build a list |
| `len(h)` | **O(1)** | O(1) | A heap is just a plain `list` |
| build via n × `heappush` | **O(n log n)** | O(1) | ❌ The slow way to build a heap — use `heapify` when you already have all elements |

**Why is `heapify()` O(n) and not O(n log n)?**

- `heapify` uses **bottom-up heap construction** (Floyd's algorithm): it walks from the last non-leaf node (`n//2 - 1`) down to index 0 and sifts each node **down**.
- The trick is that **most nodes are near the bottom** and barely move: ~n/2 leaves cost 0 work, ~n/4 nodes sift at most 1 level, ~n/8 at most 2 levels ...
- Total work = `Σ (n / 2^(h+1)) * h` for h = 0..log n, which converges to **2n → O(n)**.
- By contrast, pushing one-by-one sifts **up** from the bottom, where most nodes live → each push really can cost `log n` → **O(n log n)**.

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

**Practical rule:** got all elements up front? → `heapify` (O(n)). Elements arrive one at a time (streaming)? → `heappush` (O(log n) each).

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

**Common Interview Problems Using heapq:**
- LC 215: Kth Largest Element in an Array
- LC 347: Top K Frequent Elements
- LC 373: Find K Pairs with Smallest Sums
- LC 378: Kth Smallest Element in a Sorted Matrix
- LC 23: Merge k Sorted Lists
- LC 295: Find Median from Data Stream (use 2 heaps)
- LC 253: Meeting Rooms II (interval scheduling)
- LC 767: Reorganize String (greedy + heap)

**Summary:**
- ✅ heapq provides efficient min heap (priority queue)
- ✅ O(log n) for push/pop, O(1) for peek (heap[0])
- ✅ O(n) for heapify, O(n log k) for nsmallest/nlargest
- ✅ For max heap: negate values or use `-val`
- ✅ For custom comparison: use tuple ordering or implement `__lt__`


### Max-heap via negation


Python's `heapq` only implements a **min heap** — there is no `reverse=True` option for `heapify()`.

To simulate a **max heap**, negate the priority key:

```python
# Instead of storing (dist, x, y), store (-dist, x, y)

from heapq import heapify, heappop

pq = [(-10, "A"), (-5, "B"), (-20, "C")]
heapify(pq)

print(heappop(pq))  # (-20, 'C')  ← largest original dist (20) comes out first
print(heappop(pq))  # (-10, 'A')
print(heappop(pq))  # (-5,  'B')
```

**Push pattern:**
```python
import heapq

max_heap = []
heapq.heappush(max_heap, (-priority, value))

# Pop: negate back to get original value
neg_pri, val = heapq.heappop(max_heap)
print(-neg_pri, val)
```

**Multi-key example (primary DESC, secondary ASC):**
```python
# Sort by dist descending; on tie, by name ascending
heapq.heappush(pq, (-dist, name))
```

> **Rule of thumb**: negate whichever field(s) you want in descending order; leave the rest unchanged.

## `sortedcontainers` — Ordered Map

### `SortedDict` / `SortedList` — Python's TreeMap


**Idea**

Python has **no built-in `TreeMap`** (Java's `java.util.TreeMap`). The standard
replacement is **`sortedcontainers`** — a pure-Python library that keeps keys in
**sorted order** while supporting `O(log n)` insert / delete / lookup and `O(log n)`
floor / ceiling / range queries. Internally it's a list-of-lists (not a tree), but the
**API and Big-O behave like a balanced BST**, so it's the go-to "TreeMap" for LC.

- `SortedDict` ↔ Java `TreeMap` (sorted **key → value** map)
- `SortedList` ↔ Java `TreeSet` / multiset (sorted values; duplicates allowed)
- Keys/values stay sorted automatically — **no re-sorting on every insert** (the win
  over `list.sort()`), unlike `bisect` on a plain list where `insert` is `O(n)`.

**`SortedDict` vs `TreeMap` — they are NOT the same data structure** ⭐⭐⭐⭐

| | Python `SortedDict` | Java `TreeMap` |
|---|---|---|
| **Source** | `pip install sortedcontainers` — **NOT stdlib** (preinstalled on LeetCode) | `java.util`, built-in |
| **Implementation** | `dict` + `SortedList` of keys (list-of-lists, B-tree-ish) | Red-black tree (self-balancing BST) |
| **`d[k]` / `get(k)`** | **`O(1)`** — plain hash lookup | **`O(log n)`** — tree descent |
| **insert / delete** | `O(log n)` amortized | `O(log n)` |
| **floor / ceiling** | `O(log n)` via `bisect_*` → returns an **index** | `O(log n)` via `floorKey/ceilingKey` → returns a **key** or `null` |
| **k-th smallest key** | **`O(log n)`** — `d.keys()[k]` ✅ | ❌ not supported (`O(n)` iteration) |
| **Custom ordering** | `SortedDict(key_func)` — a key **transform** only | `Comparator` — arbitrary 2-arg logic |
| **Duplicate keys** | ❌ | ❌ |

> **Takeaway:** `SortedDict` is *faster* than `TreeMap` for value lookups (hash, not tree walk),
> and it can do **index access** (`keys()[k]`) that `TreeMap` simply cannot.
> `TreeMap`'s `Comparator` is the one thing that's strictly more expressive.

**Core API**

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

**Java `TreeMap` → Python `SortedDict` cheat table** ⭐⭐⭐⭐⭐

| Java `TreeMap` | Python `SortedDict` | Guard needed |
|---|---|---|
| `firstKey()` / `lastKey()` | `d.keys()[0]` / `d.keys()[-1]` | non-empty |
| `firstEntry()` / `lastEntry()` | `d.peekitem(0)` / `d.peekitem(-1)` | non-empty |
| `floorKey(x)` — largest ≤ x | `d.keys()[d.bisect_right(x) - 1]` | `idx >= 0` |
| `ceilingKey(x)` — smallest ≥ x | `d.keys()[d.bisect_left(x)]` | `idx < len(d)` |
| `lowerKey(x)` — strictly < x | `d.keys()[d.bisect_left(x) - 1]` | `idx >= 0` |
| `higherKey(x)` — strictly > x | `d.keys()[d.bisect_right(x)]` | `idx < len(d)` |
| `subMap(lo, true, hi, true)` | `d.irange(lo, hi)` | — |
| `headMap(hi, true)` / `tailMap(lo, true)` | `d.irange(maximum=hi)` / `d.irange(minimum=lo)` | — |
| `pollFirstEntry()` / `pollLastEntry()` | `d.popitem(0)` / `d.popitem(-1)` | non-empty |
| `descendingMap()` | `reversed(d)` | — |
| `new TreeMap<>(comparator)` | `SortedDict(key_func)` | transform, not comparator |

⚠️ **The #1 gotcha — index, not key.** Java's `floorKey/ceilingKey` return a key
(or `null`); Python's `bisect_*` return an **index** that can be `-1` or `len(d)`.
Because `keys()[-1]` silently returns the **MAX key**, a missing guard is a *silent
wrong answer*, not a crash:

```python
# ✅ the safe floor / ceiling idiom — memorize this
i = d.bisect_left(x)
ceil_key = d.keys()[i] if i < len(d) else None       # ceilingKey(x)

j = d.bisect_right(x) - 1
floor_key = d.keys()[j] if j >= 0 else None          # floorKey(x)
```

⚠️ **Don't rebuild the key list.** `bisect.bisect_left(list(sd.keys()), x)` copies
every key = `O(n)`, destroying the `O(log n)` win. Use `sd.bisect_left(x)`.

**When to use**

| Need | Use |
|------|-----|
| Fast `O(1)` lookup, **no ordering** | plain `dict` / `set` |
| Sorted, but **inserted once then read** | sort a `list` (`O(n log n)` once) |
| **Repeated inserts/deletes** + need order / floor / ceiling / range | **`SortedDict` / `SortedList`** |
| Sorted **values with duplicates** (multiset) | **`SortedList`** |
| Only need min/max (no ordering between) | `heapq` |

> Reach for `SortedContainers` the moment the data **mutates over time** *and* you need
> "closest key", "next greater key", or "all keys in `[a, b]`". If the array is static,
> a one-time sort + `bisect` is simpler and faster.

**Use example — LC 729 My Calendar I (floor/ceiling overlap check)**

The canonical "how do I port this Java `TreeMap` code to Python?" question.
Java original:

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

**V1) Closest 1:1 port — `SortedDict` + explicit floor/ceiling**

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

**V2) More idiomatic Python — `SortedList` of `(start, end)` tuples** ⭐ *interview pick*

One sorted structure instead of a key/value split, and the overlap condition reads directly:

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

**V3) Zero-dependency fallback — stdlib `bisect` only**

If imports are restricted to stdlib: search stays `O(log N)`, but `list.insert()`
shifts elements → `O(N)` per booking. Fine for LC 729 (≤ 1000 calls).

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

**Use example — LC 220 Contains Duplicate III (range query via `SortedList`)**

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

**Relative LeetCode problems**

| Problem | LC# | TreeMap operation |
|---------|-----|-------------------|
| My Calendar I | 729 | floor/ceiling for overlap check |
| My Calendar II | 731 | count overlaps with ordered map |
| My Calendar III | 732 | max overlapping (diff array on ordered keys) |
| Contains Duplicate III | 220 | ceiling + range check in sliding window |
| Time Based Key-Value Store | 981 | floor on timestamp |
| Data Stream as Disjoint Intervals | 352 | merge intervals via floor/ceiling |
| Count of Smaller Numbers After Self | 315 | `SortedList` + `bisect` while scanning right→left |
| Sliding Window Median | 480 | `SortedList` add/remove, index middle |
| The Skyline Problem | 218 | multiset of heights (`SortedList`) |

> See the **TreeMap Pattern (Template 7)** in
> [hash_map.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/hash_map.md)
> for the Java `TreeMap` side-by-side comparison.

## `bisect` — Binary Search on a Sorted List

### `bisect_left` and `bisect_right`

- algorithm for `NOT sorting an array eveytime` whenever there is a new inserted element 
```python
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

### `deque` (double-ended queue)

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

### `OrderedDict` (hash map + linked list)

- check [Collection.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/Collection.md)

## `itertools`

### `itertools` — product, permutations, combinations, accumulate

```python
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

### `lru_cache`, `cmp_to_key` and friends

- functools.lru_cache
    - implement cache via LRU (Least Recently Used (LRU) cache) in py
- ref
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
