# Heap Language APIs — `heapq` & `PriorityQueue`

> **Scope** — The language mechanics of heaps: every `heapq` and `PriorityQueue` call you need, how to fake a max-heap, how to peek without popping, and the traps that come with a partially-ordered container; the algorithms that use them live in the heap sheets.
> **See also** — *parent sheet*: [heap.md](./heap.md) — the canonical heap templates and pattern selection. *Siblings split out of the same file*: [heap_advanced.md](./heap_advanced.md) — lazy deletion, regret greedy and the rarer templates; [heap_examples.md](./heap_examples.md) — the worked LC solution archive. *Neighbouring sheets*: [Collection.md](./Collection.md) — choosing among Java's collections; [sort.md](./sort.md) — heap sort in context.

## LeetCode Problem Lists

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## Overview

Python's `heapq` and Java's `PriorityQueue` are both **binary min-heaps**. Everything else — max-heaps,
custom orderings, peeking, lazy deletion — is built out of that one primitive, and most heap bugs in an
interview are API bugs rather than algorithm bugs.

### Key Properties
- **Complexity**: `push` / `pop` are O(log N); `peek` is O(1); `heapify` of an existing list is O(N)
- **Core Idea**: min-heap only — a max-heap is a min-heap over negated keys (Python) or a reversed
  comparator (Java)
- **When to Use**: read this once, then come back for the peek / max-heap / custom-comparator recipes

### References
- [Python heapq documentation](https://docs.python.org/3/library/heapq.html)
- [Java PriorityQueue](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html)
- [Priority Queue Implementation Notes](https://docs.python.org/3/library/heapq.html#priority-queue-implementation-notes)

## Python `heapq`

### API Reference
- Note :
    - in Py, heapq is `MIN heap`
        - if we need max heap, can use `-1 * val`
            - LC 1492
    - in Py implementation, `index start from 0`
    - `pop()` will return `min` element (instead of max element)
    - 2 ways build heap (in py)
        - heappush(heap, num)
        - heapify(array)
    - complexity
        - push/pop (each)
            - time : O(log(N))
            - space : O(N)
            - ref : [SF - whats-the-time-complexity-of-functions-in-heapq-library](https://stackoverflow.com/questions/38806202/whats-the-time-complexity-of-functions-in-heapq-library#:~:text=heapq%20is%20a%20binary%20heap,O(n%20log%20n))
        - so, if implement push/pop on all elements, will cost
            - time : O(N log(N))
            - space : O(N)
- Basic API
    - heapify : transform list to heap
    - heappush : put element into heap
    - heappop  : get (remove) top element from heap
        - Min heap : delete top element from the Min Heap
        - Max heap : delete top element from the Max Heap
    - heappushpop : heappush then heappop (put first, then pop)
    - heapreplace : heappop then heappush (pop first, then put)
    - nlargest : return top N large elements
    - nsmallest : return top N least elements
- Ref
    - https://docs.python.org/zh-tw/3/library/heapq.html
    - https://ithelp.ithome.com.tw/articles/10247299
    - https://cloud.tencent.com/developer/article/1794191#:~:text=heapq%20%E5%BA%93%E6%98%AFPython%E6%A0%87%E5%87%86,%E7%AD%89%E4%BA%8E)%E5%AE%83%E7%9A%84%E5%AD%90%E8%8A%82%E7%82%B9%E3%80%82
    - https://python.plainenglish.io/python-for-interviewing-an-overview-of-the-core-data-structures-666abdf8b698

```python
#------------------------
# PY API examples
#------------------------

#----------------------
# 1) build heapq
#----------------------
In [43]: import heapq
    ...:
    ...:
    ...: array = [10, 17, 50, 7, 30, 24, 27, 45, 15, 5, 36, 21]
    ...: heap = []
    ...: for num in array:
    ...:     heapq.heappush(heap, num)
    ...: print("array:", array)
    ...: print("heap: ", heap)
    ...:
    ...: heapq.heapify(array)
    ...: print("array:", array)
array: [10, 17, 50, 7, 30, 24, 27, 45, 15, 5, 36, 21]
heap:  [5, 7, 21, 15, 10, 24, 27, 45, 17, 30, 36, 50]
array: [5, 7, 21, 10, 17, 24, 27, 45, 15, 30, 36, 50]

# NOTE : there are 2 ways create heap (in py)
#  1) heappush(heap, num)
#  2) heapify(array)
#
# -> we can see above results are a bit different. However this not affect the "min heap" property in py. We can still get min element, and heap will get updated accordingly.

#----------------------
# 1') build heapq V2
#----------------------

# https://leetcode.com/explore/learn/card/heap/644/common-applications-of-heap/4022/

import heapq

# Construct an empty Min Heap
minHeap = []
heapq.heapify(minHeap)

# Construct an empty Max Heap
# As there are no internal functions to construct a Max Heap in Python,
# So, we will not construct a Max Heap.

# Construct a Heap with Initial values
# this process is called "Heapify"
# The Heap is a Min Heap
heapWithValues = [3,1,2]
heapq.heapify(heapWithValues)

# Trick in constructing a Max Heap
# As there are no internal functions to construct a Max Heap
# We can multiply each element by -1, then heapify with these modified elements.
# The top element will be the smallest element in the modified set,
# It can also be converted to the maximum value in the original dataset.
# Example
maxHeap = [1,2,3]
maxHeap = [-x for x in maxHeap]
heapq.heapify(maxHeap)
# The top element of maxHeap is -3
# Convert -3 to 3, which is the maximum value in the original maxHeap

#----------------------
# 2) insert into element
#----------------------

# https://leetcode.com/explore/learn/card/heap/644/common-applications-of-heap/4023/

# Insert an element to the Min Heap
heapq.heappush(minHeap, 5)

# Insert an element to the Max Heap
# Multiply the element by -1
# As we are converting the Min Heap to a Max Heap
heapq.heappush(maxHeap, -1 * 5)


#----------------------
# 3) delete the top element
#----------------------

# https://leetcode.com/explore/learn/card/heap/644/common-applications-of-heap/4025/

# Delete top element from the Min Heap
heapq.heappop(minHeap)

# Delete top element from the Max Heap
heapq.heappop(maxHeap)


#----------------------
# 3) get top element
#----------------------

# https://leetcode.com/explore/learn/card/heap/644/common-applications-of-heap/4024/

# Get top element from the Min Heap
# i.e. the smallest element
minHeap[0]
# Get top element from the Max Heap
# i.e. the largest element
# When inserting an element, we multiplied it by -1
# Therefore, we need to multiply the element by -1 to revert it back
-1 * maxHeap[0]

#----------------------
# 2) sorting via heapq
#----------------------
In [44]: array = [10, 17, 50, 7, 30, 24, 27, 45, 15, 5, 36, 21]
    ...: heap = []
    ...: for num in array:
    ...:     heapq.heappush(heap, num)
    ...: print(heap[0])
5

In [45]: heap_sort = [heapq.heappop(heap) for _ in range(len(heap))]
    ...: print("heap sort result: ", heap_sort)
heap sort result:  [5, 7, 10, 15, 17, 21, 24, 27, 30, 36, 45, 50]

#----------------------
# 3) get Min or Max from heap
#----------------------

In [48]: array = [10, 17, 50, 7, 30, 24, 27, 45, 15, 5, 36, 21]
    ...: heapq.heapify(array)
    ...: print(heapq.nlargest(2, array))
    ...: print(heapq.nsmallest(3, array))
[50, 45]
[5, 7, 10]

#----------------------
# 4) merge 2 sorted list via heap
#----------------------
In [49]: array_a = [10, 7, 15, 8]
    ...: array_b = [17, 3, 8, 20, 13]
    ...: array_merge = heapq.merge(sorted(array_a), sorted(array_b))
    ...: print("merge result:", list(array_merge))
merge result: [3, 7, 8, 8, 10, 13, 15, 17, 20]


#----------------------
# 5) heap replace element
#----------------------

In [50]: array_c = [10, 7, 15, 8]
    ...: heapq.heapify(array_c)
    ...: print("before:", array_c)
    ...: # heappushpop : push first, then pop
    ...: item = heapq.heappushpop(array_c, 5)
    ...: print("after: ", array_c)
    ...: print(item)
    ...:
before: [7, 8, 15, 10]
after:  [7, 8, 15, 10]
5


In [51]: array_d = [10, 7, 15, 8]
    ...: heapq.heapify(array_d)
    ...: print("before:", array_d)
    ...: # pop first, then push
    ...: item = heapq.heapreplace(array_d, 5)
    ...: print("after: ", array_d)
    ...: print(item)
before: [7, 8, 15, 10]
after:  [5, 8, 15, 10]
7

#----------------------
# 5) make a MAX heapq
#----------------------
In [54]: numbers = [4,1,24,2,1]
    ...:
    ...: # invert numbers so that the largest values are now the smalles
    ...:
    ...: numbers = [-1 * n for n in numbers]
    ...:
    ...: # turn numbers into min heap
    ...: heapq.heapify(numbers)
    ...:
    ...: # pop out 5 times
    ...: klargest = []
    ...: for i in range(len(numbers)):
    ...:     # multiply by -1 to get our inital number back
    ...:     klargest.append(-1 * heapq.heappop(numbers))
    ...:

In [55]: klargest
Out[55]: [24, 4, 2, 1, 1]

```


### Peek: Get TOP Element WITHOUT Popping ⭐⭐⭐⭐⭐

**Key Idea**: Python's `heapq` has **NO `peek()` function** — the heap *is* a plain `list`, and the
heap invariant guarantees the min sits at index `0`. So **`pq[0]` IS the peek**, and it is `O(1)`.

#### **Ways to peek (Python)**

| Way | Time | Verdict |
|-----|------|---------|
| `pq[0]` | O(1) | ✅ **the idiomatic way** |
| `heapq.nsmallest(1, pq)[0]` | O(n) | ❌ scans the whole list, ignores heap structure |
| `min(pq)` | O(n) | ❌ same problem |
| `pq.queue[0]` | O(1) | only for `queue.PriorityQueue` (list + lock wrapper, thread-safe but slower) |

```python
# python
import heapq

pq = []
heapq.heappush(pq, 5)
heapq.heappush(pq, 3)
heapq.heappush(pq, 7)

# ── PEEK (no pop) ─────────────────────────────
top = pq[0]            # time = O(1), space = O(1)  -> 3
print(pq)              # [3, 5, 7]  <- heap UNCHANGED

# ── safe peek on possibly-empty heap ──────────
top = pq[0] if pq else None      # pq[0] raises IndexError when empty

# ── max-heap: push NEGATED keys, negate back on peek ──
max_pq = []
for v in [5, 3, 7]:
    heapq.heappush(max_pq, -v)
largest = -max_pq[0]   # 7   (peek, NOT pop)
```

#### **⚠️ Gotchas**

```python
# python
pq = [1, 3, 9, 7, 5]   # a VALID min-heap

# ✅ ONLY index 0 is meaningful
pq[0]     # 1  -> guaranteed smallest

# ❌ a heap is only PARTIALLY ordered - these mean NOTHING
pq[1]     # 3  -> NOT necessarily the 2nd smallest
pq[-1]    # 5  -> NOT the largest
sorted(pq)[1]   # if you truly need the 2nd smallest, this is O(n log n)

# ❌ IndexError on empty heap -> always guard
empty = []
# empty[0]                     # IndexError: list index out of range
while empty and empty[0] < 10: # ✅ short-circuit: `empty and ...` MUST come first
    heapq.heappop(empty)
```

#### **Classic use: lazy deletion (peek → discard stale tops)**

The most common reason to peek is **lazy deletion** — you never remove a stale entry from the middle
of the heap (heapq can't), you just pop it off the top once it surfaces.

```python
# python
# LC 3092 - Most Frequent IDs  (also LC 218 Skyline, LC 1834 Single-Threaded CPU)
# IDEA: max-heap of (-count, id); an entry is STALE if its stored count != the live count.
#       Peek at pq[0], drop stale tops, then pq[0] is the true answer.
# time = O(n log n), space = O(n)
while pq and -pq[0][0] != c_map[pq[0][1]]:   # peek, compare, discard
    heapq.heappop(pq)

ans = -pq[0][0] if pq else 0                 # now the top is VALID
```

#### **Ops that avoid a separate peek**

When you are going to *replace* the top anyway, these do it in one sift instead of two:

```python
# python
heapq.heapreplace(pq, item)   # pop THEN push -> returns old top. Heap must be non-empty.
heapq.heappushpop(pq, item)   # push THEN pop -> cheaper when item <= current top

# typical "keep k largest" loop - peek to compare, replace in one shot
for num in nums:
    if len(pq) < k:
        heapq.heappush(pq, num)
    elif num > pq[0]:                 # peek
        heapq.heapreplace(pq, num)    # 1 sift instead of heappop + heappush
```

#### **Java equivalent**

```java
// java
PriorityQueue<Integer> pq = new PriorityQueue<>();

Integer top = pq.peek();    // time = O(1), returns NULL on empty (no exception)
Integer top2 = pq.element();// time = O(1), THROWS NoSuchElementException on empty

// NOTE: Java's peek() is a real method; Python has no peek() -> use pq[0]
// NOTE: iterating a Java PQ (for/toString) does NOT give sorted order - same
//       partial-order caveat as python's pq[1], pq[-1]
```

| | Python `heapq` | Java `PriorityQueue` |
|---|---|---|
| Peek | `pq[0]` | `pq.peek()` |
| Empty behaviour | `IndexError` | `peek()` → `null`, `element()` → throws |
| Empty check | `if pq:` | `pq.isEmpty()` |
| Max-heap peek | `-pq[0]` (negated push) | `pq.peek()` with `Collections.reverseOrder()` |


### Heap Sort
```python
# https://docs.python.org/zh-tw/3/library/heapq.html
def heapsort(iterable):

    h = []
    for value in iterable:
        heappush(h, value)
    return [heappop(h) for i in range(len(h))]

# heapsort([1, 3, 5, 7, 9, 2, 4, 6, 8, 0])
# >>> [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
```

## Java `PriorityQueue`

### Operations
```java
import java.util.*;

// Create PQ (min-heap by default)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Create max-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

// Add elements
minHeap.offer(5);
minHeap.add(3);  // Same as offer

// Remove and return smallest/largest
Integer smallest = minHeap.poll();

// Peek without removing
Integer top = minHeap.peek();

// Check if empty
boolean isEmpty = minHeap.isEmpty();

// Get size
int size = minHeap.size();

// Clear all elements
minHeap.clear();

// Custom comparator
PriorityQueue<int[]> customPQ = new PriorityQueue<>(
    (a, b) -> Integer.compare(a[0], b[0])  // Compare first element
);
```

### Ordering Demo
```java
// java
PriorityQueue pq
// random insert
for i in {2,4,1,9,6}:
    pq.add(i)

while pq not empty:
    // every time get the one minimum element
    print(pq.pop())

// the output should be in order (small -> big)
// 1,2,4,6,9
```

## LC Examples

### Design Twitter — LC 355

The timeline is a **k-way merge** over the followees\' tweet lists (each already newest-first), so
`heapq.merge` gives the 10 newest posts without materialising every list.

```python
# 355 Design Twitter
# https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E8%AE%BE%E8%AE%A1Twitter.md
from collections import defaultdict
from heapq import merge
class Twitter(object):
    
    def __init__(self):
        self.follower_followees_map = defaultdict(set)
        self.user_tweets_map = defaultdict(list)
        self.time_stamp = 0

    def postTweet(self, userId, tweetId):
        self.user_tweets_map[userId].append((self.time_stamp, tweetId))
        self.time_stamp -= 1

    def getNewsFeed(self, userId):
        # get the followees list
        followees = self.follower_followees_map[userId]
        # add userId as well, since he/she can also see his/her post in the timeline
        followees.add(userId)
        
        # reversed(.) returns a listreverseiterator, so the complexity is O(1) not O(n)
        candidate_tweets = [reversed(self.user_tweets_map[u]) for u in followees]

        tweets = []
        """
        python starred expression :
        -> will extend Iterable Unpacking
        example 1 : *candidate_tweets
        exmaple 2 : a, *b, c = range(5)
        ref :
        https://www.python.org/dev/peps/pep-3132/
        https://blog.csdn.net/weixin_41521681/article/details/103528136
        http://swaywang.blogspot.com/2012/01/pythonstarred-expression.html
        https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick.md
        """
        # complexity is 10*log(n), n is twitter's user number in worst case
        for t in merge(*candidate_tweets):
            tweets.append(t[1])
            if len(tweets) == 10:
                break
        return tweets

    def follow(self, followerId, followeeId):
        self.follower_followees_map[followerId].add(followeeId)

    def unfollow(self, followerId, followeeId):
        self.follower_followees_map[followerId].discard(followeeId)
```

## Summary & Quick Reference

| Operation | Python `heapq` | Java `PriorityQueue` |
|---|---|---|
| Create min-heap | `h = []` | `new PriorityQueue<>()` |
| Create max-heap | negate keys: `heappush(h, -v)` | `new PriorityQueue<>(Collections.reverseOrder())` |
| Build from a list | `heapq.heapify(lst)` — O(N) | `new PriorityQueue<>(collection)` — O(N) |
| Push | `heapq.heappush(h, v)` | `pq.offer(v)` / `pq.add(v)` |
| Pop top | `heapq.heappop(h)` | `pq.poll()` |
| Peek top | `h[0]` — **no `peek()` exists** | `pq.peek()` |
| Pop then push | `heapq.heapreplace(h, v)` | `pq.poll(); pq.offer(v);` |
| Push then pop | `heapq.heappushpop(h, v)` | `pq.offer(v); pq.poll();` |
| Top k largest | `heapq.nlargest(k, it)` | size-k min-heap, then drain |
| Top k smallest | `heapq.nsmallest(k, it)` | size-k max-heap, then drain |
| Merge sorted iterables | `heapq.merge(a, b, ...)` | k-way merge by hand |
| Empty check | `if h:` | `pq.isEmpty()` |
| Custom order | tuples, or `__lt__` on the class | comparator lambda / `Comparable` |

**Three rules that prevent most API bugs**

1. Only index `0` is meaningful. `h[1]`, `h[-1]`, and iterating a Java `PriorityQueue` all give you
   **partial** order, not sorted order.
2. Build a comparator with `Integer.compare(a, b)` / `Long.compare(a, b)`, never `a - b` — subtraction
   overflows for large or negative values.
3. Guard the empty case: `h[0]` raises `IndexError`, Java\'s `peek()` returns `null` and `element()`
   throws. In a `while` condition put the emptiness test **first** so it short-circuits.