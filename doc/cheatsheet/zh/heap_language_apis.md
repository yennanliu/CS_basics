# 堆積的語言 API — `heapq` 與 `PriorityQueue`

> **範圍** — 堆積在語言層面的機械細節：`heapq` 和 `PriorityQueue` 你會用到的每個呼叫、怎麼假造出最大堆積、怎麼在不 pop 的情況下偷看堆頂，以及「只有部分有序」的容器會帶來哪些陷阱；用到這些 API 的演算法本身，放在其他堆積的表單裡。
> **另見** — *母表單*：[heap.md](./heap.md) — 標準的堆積模板與模式選擇。*從同一份檔案拆出來的兄弟表單*：[heap_advanced.md](./heap_advanced.md) — 延遲刪除、反悔貪婪與其他比較少見的模板；[heap_examples.md](./heap_examples.md) — LC 詳解題庫。*相鄰表單*：[Collection.md](./Collection.md) — 怎麼在 Java 各種 collection 之間做選擇；[sort.md](./sort.md) — 放在排序脈絡下看的堆積排序。

## LeetCode 題目清單

- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## 概觀

Python 的 `heapq` 和 Java 的 `PriorityQueue` 都是**二元最小堆積**。其他東西 — 最大堆積、
自訂排序、peek、延遲刪除 — 全都是從這個原始操作堆出來的。面試中大部分的堆積 bug，其實是
API 用錯，不是演算法想錯。

### 關鍵性質
- **複雜度**：`push` / `pop` 是 O(log N)；`peek` 是 O(1)；把既有 list 做 `heapify` 是 O(N)
- **核心想法**：只有最小堆積 — 最大堆積就是把 key 取負號後的最小堆積（Python），或是把
  comparator 反過來（Java）
- **什麼時候用**：先讀一遍，之後再回來查 peek／最大堆積／自訂 comparator 的寫法

### 參考資料
- [Python heapq documentation](https://docs.python.org/3/library/heapq.html)
- [Java PriorityQueue](https://docs.oracle.com/javase/8/docs/api/java/util/PriorityQueue.html)
- [Priority Queue Implementation Notes](https://docs.python.org/3/library/heapq.html#priority-queue-implementation-notes)

## Python `heapq`

### API 速查
- 注意 :
    - 在 Python 裡，heapq 是 `MIN heap`
        - 如果要最大堆積，可以用 `-1 * val`
            - LC 1492
    - 在 Python 的實作中，`index start from 0`
    - `pop()` 會回傳 `min` 元素（不是最大的那個）
    - 建堆積的兩種方式（Python）
        - heappush(heap, num)
        - heapify(array)
    - 複雜度
        - push/pop（各自）
            - time : O(log(N))
            - space : O(N)
            - ref : [SF - whats-the-time-complexity-of-functions-in-heapq-library](https://stackoverflow.com/questions/38806202/whats-the-time-complexity-of-functions-in-heapq-library#:~:text=heapq%20is%20a%20binary%20heap,O(n%20log%20n))
        - 所以如果對所有元素都做一次 push/pop，成本是
            - time : O(N log(N))
            - space : O(N)
- 基本 API
    - heapify : 把 list 轉成堆積
    - heappush : 把元素放進堆積
    - heappop  : 取出（並移除）堆頂元素
        - Min heap : 刪掉最小堆積的頂端元素
        - Max heap : 刪掉最大堆積的頂端元素
    - heappushpop : 先 heappush 再 heappop（先放，再取）
    - heapreplace : 先 heappop 再 heappush（先取，再放）
    - nlargest : 回傳最大的 N 個元素
    - nsmallest : 回傳最小的 N 個元素
- 參考
    - https://docs.python.org/zh-tw/3/library/heapq.html
    - https://ithelp.ithome.com.tw/articles/10247299
    - https://cloud.tencent.com/developer/article/1794191#:~:text=heapq%20%E5%BA%93%E6%98%AFPython%E6%A0%87%E5%87%86,%E7%AD%89%E4%BA%8E)%E5%AE%83%E7%9A%84%E5%AD%90%E8%8A%82%E7%82%B9%E3%80%82
    - https://python.plainenglish.io/python-for-interviewing-an-overview-of-the-core-data-structures-666abdf8b698

```text
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


### Peek：不 pop 就拿到堆頂元素 ⭐⭐⭐⭐⭐

**核心想法**：Python 的 `heapq` **沒有 `peek()` 這個函式** — 堆積本身就是一個普通的 `list`，
而堆積性質保證最小值一定在索引 `0`。所以 **`pq[0]` 就是 peek**，而且是 `O(1)`。

#### **peek 的幾種寫法（Python）**

| 寫法 | 時間 | 評價 |
|-----|------|-----|
| `pq[0]` | O(1) | ✅ **道地寫法** |
| `heapq.nsmallest(1, pq)[0]` | O(n) | ❌ 掃過整個 list，完全沒用到堆積結構 |
| `min(pq)` | O(n) | ❌ 同樣的問題 |
| `pq.queue[0]` | O(1) | 只適用 `queue.PriorityQueue`（list 加鎖的包裝，執行緒安全但比較慢） |

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

#### **⚠️ 容易踩到的坑**

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

#### **經典用法：延遲刪除（peek → 丟掉過期的堆頂）**

會用到 peek 最常見的理由就是**延遲刪除** — 你永遠不從堆積中間移除過期的項目（heapq 也做不到），
而是等它浮到頂端時再 pop 掉。

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

#### **不需要另外 peek 的操作**

如果你本來就打算*換掉*堆頂，下面這些操作一次下沉就搞定，不用做兩次：

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

#### **Java 的對應寫法**

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
| 空的時候 | `IndexError` | `peek()` → `null`，`element()` → 丟例外 |
| 判斷是否為空 | `if pq:` | `pq.isEmpty()` |
| 最大堆積的 peek | `-pq[0]`（push 時取負號） | `pq.peek()` 搭配 `Collections.reverseOrder()` |


### 堆積排序
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

### 操作
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

### 排序示範
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

## LC 範例

### Design Twitter — LC 355

時間軸本質上是對所有追蹤對象的推文清單做**k 路合併**（每一份都已經是最新在前），
所以用 `heapq.merge` 就能拿到最新的 10 則貼文，不必把每份清單都展開。

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

## 總結與速查

| 操作 | Python `heapq` | Java `PriorityQueue` |
|---|---|---|
| 建最小堆積 | `h = []` | `new PriorityQueue<>()` |
| 建最大堆積 | key 取負號：`heappush(h, -v)` | `new PriorityQueue<>(Collections.reverseOrder())` |
| 從 list 建堆積 | `heapq.heapify(lst)` — O(N) | `new PriorityQueue<>(collection)` — O(N) |
| Push | `heapq.heappush(h, v)` | `pq.offer(v)` / `pq.add(v)` |
| Pop 堆頂 | `heapq.heappop(h)` | `pq.poll()` |
| Peek 堆頂 | `h[0]` — **沒有 `peek()` 這種東西** | `pq.peek()` |
| 先 pop 再 push | `heapq.heapreplace(h, v)` | `pq.poll(); pq.offer(v);` |
| 先 push 再 pop | `heapq.heappushpop(h, v)` | `pq.offer(v); pq.poll();` |
| 前 k 大 | `heapq.nlargest(k, it)` | 大小為 k 的最小堆積，最後倒出來 |
| 前 k 小 | `heapq.nsmallest(k, it)` | 大小為 k 的最大堆積，最後倒出來 |
| 合併多個已排序序列 | `heapq.merge(a, b, ...)` | 自己手刻 k 路合併 |
| 判斷是否為空 | `if h:` | `pq.isEmpty()` |
| 自訂排序 | tuple，或在 class 上定義 `__lt__` | comparator lambda／`Comparable` |

**避開大多數 API bug 的三條規則**

1. 只有索引 `0` 有意義。`h[1]`、`h[-1]`，以及走訪 Java `PriorityQueue`，拿到的都是
   **部分**有序，不是排好序的結果。
2. comparator 要用 `Integer.compare(a, b)` / `Long.compare(a, b)` 來寫，絕對不要用 `a - b` —
   數值很大或是負數時，減法會溢位。
3. 空的情況要先擋：`h[0]` 會丟 `IndexError`，Java 的 `peek()` 回傳 `null`、`element()` 會丟例外。
   在 `while` 條件裡，把「是否為空」的判斷放**最前面**，才能短路。
