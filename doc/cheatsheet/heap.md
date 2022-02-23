# Heap
- a special `completed binary tree`
- definition : given a tree, if P is C's parent node -> P always `<=` or `>=` C.
- types
    - min heap
        - given a tree, if P is C's parent node -> P always `<=` C.
    - max heap
        - given a tree, if P is C's parent node -> P always `>=` C.
- was invented for `heap sort`
- a heap is a specialized tree-based data structure which is essentially an almost completed tree that satisfies the heap property:
    - In a max heap
        - for any given node C, if P is a parent node of C, then the key (the value) of P is greater than or equal to the key of C
    - In a min heap
        - the key of P is less than or equal to the key of C.[2] The node at the "top" of the heap (with no parents) is called the root node.
- Ref
    - https://www.geeksforgeeks.org/heap-data-structure/

<p><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/heap.png" ></p>

## 0) Concept  

### 0-1) Types
    - `priority queue`
        - LC 295, 787, 1492

### 0-2) Pattern

## 1) General form

### 1-0) Basic OP
- build
- insert
- update
- get
- delete
- extract_max
- delete_max
- replace
- find_max

### 1-1) heapq (`heap queue` AKA `priority queue`) (Py api)
- Note :
    - in Py, heapq is `MIN heap`
        - if we need max heap, can use `-1 * val`
    - in Py implementation, `index start from 0`
    - `pop()` will return `min` element (instead of max element)

- Basic API
    - heapify : transform list to heap
    - heappush : put element into heap
    - heappop  : get (remove) element from heap
    - heappushpop : heappush then heappop (put first, then pop)
    - heapreplace : heappop then heappush (pop first, then put)
    - nlargest : return top N large elements
    - nsmallest : return top N least elements
- Ref
    - https://docs.python.org/zh-tw/3/library/heapq.html
    - https://ithelp.ithome.com.tw/articles/10247299
    - https://cloud.tencent.com/developer/article/1794191#:~:text=heapq%20%E5%BA%93%E6%98%AFPython%E6%A0%87%E5%87%86,%E7%AD%89%E4%BA%8E)%E5%AE%83%E7%9A%84%E5%AD%90%E8%8A%82%E7%82%B9%E3%80%82

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
```

### 1-2) Heap VS Stack VS Queue

### 1-3) Heap sort
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

### 1-4) Priority Queue
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
```python
# 355 Design Twitter
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
        followees = self.follower_followees_map[userId]
        followees.add(userId)
        
        # reversed(.) returns a listreverseiterator, so the complexity is O(1) not O(n)
        candidate_tweets = [reversed(self.user_tweets_map[u]) for u in followees]

        tweets = []
        # complexity is 10lg(n), n is twitter's user number in worst case
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

## 2) LC Example

### 2-1) Kth Largest Element in a Stream
```python
# 703 Kth Largest Element in a Stream
# V0
# IDEA : heap op
class KthLargest(object):

    def __init__(self, k, nums):
        self.pool = nums
        self.size = len(self.pool)
        self.k = k
        heapq.heapify(self.pool)
        while self.size > k:
            # heappop : get minimum element out from heap and return it 
            heapq.heappop(self.pool)
            self.size -= 1

    def add(self, val):
        if self.size < self.k:
            # heappush : put item input heap, and make heap unchanged
            heapq.heappush(self.pool, val)
            self.size += 1
        elif val > self.pool[0]:
            # get minimum value from heap and return it, and put new item into heap
            heapq.heapreplace(self.pool, val)
        return self.pool[0]
```

### 2-2) Ugly Number II
```python
# 264 Ugly Number II
# V0 
# IDEA : HEAP
# using brute force is too slow -> time out error
# -> so here we generate "ugly number" by ourself, and order them via heap (heappush)
# -> and return the i-th element as request
class Solution(object):
    def nthUglyNumber(self, n):
        heap = [1]
        visited = set([1])      
        for i in range(n):
            val = heapq.heappop(heap)
            for factor in [2,3,5]:
                if val*factor not in visited:
                    heapq.heappush(heap, val*factor)
                    visited.add(val*factor)    
        return val

# V1
import heapq
class Solution(object):
    def nthUglyNumber(self, n):
        ugly_number = 0

        heap = []
        heapq.heappush(heap, 1)
        for _ in range(n):
            ugly_number = heapq.heappop(heap)
            if ugly_number % 2 == 0:
                heapq.heappush(heap, ugly_number * 2)
            elif ugly_number % 3 == 0:
                heapq.heappush(heap, ugly_number * 2)
                heapq.heappush(heap, ugly_number * 3)
            else:
                heapq.heappush(heap, ugly_number * 2)
                heapq.heappush(heap, ugly_number * 3)
                heapq.heappush(heap, ugly_number * 5)

        return ugly_number
```

### 2-3) Find Median from Data Stream
```python
# 295 Find Median from Data Stream
# V0
# https://docs.python.org/zh-tw/3/library/heapq.html
# https://github.com/python/cpython/blob/3.10/Lib/heapq.py
# Note !!! 
#  -> that the heapq in python is a min heap, thus we need to invert the values in the smaller half to mimic a "max heap".
# IDEA : python heapq (heap queue AKA priority queue)
#  -> Step 1) init 2 heap : small, large
#              -> small : stack storage "smaller than half value" elements
#              -> large : stack storage "bigger than half value" elements
#  -> Step 2) check if len(self.small) == len(self.large)
#  -> Step 3-1) add num:  (if len(self.small) == len(self.large))
#              -> since heapq in python is "min" heap, so we need to add minus to smaller stack for "max" heap simulation
#              -> e.g. : 
#                        "-num" in -heappushpop(self.small, -num)
#                        "-heappushpop" is for balacing the "-" back (e.g. -(-value) == value)
#             and pop "biggest" elment in small stack to big stack
#  -> Step 3-2) add num:  (if len(self.small) != len(self.large))
#             -> pop smallest element from large heap to small heap
#             -> e.g. heappush(self.small, -heappushpop(self.large, num))
#  -> Step 4) return median
#             -> if even length (len(self.small) == len(self.large))
#                 -> return float(self.large[0] - self.small[0]) / 2.0
#             -> if odd length ((len(self.small) != len(self.large)))
#                 -> return float(self.large[0])
from heapq import *
class MedianFinder:
    def __init__(self):
        self.small = []  # the smaller half of the list, max heap (invert min-heap)
        self.large = []  # the larger half of the list, min heap

    def addNum(self, num):
        """

        doc : https://docs.python.org/3/library/heapq.html
        src code : https://github.com/python/cpython/blob/3.10/Lib/heapq.py
        
        * heappush(heap, item)
            -> Push the value item onto the heap, maintaining the heap invariant.

        * heappop(heap)
            -> Pop and return the smallest item from the heap, maintaining the heap invariant. If the heap is empty, IndexError is raised. To access the smallest item without popping it, use heap[0].

        * heappushpop(heap, item)
            -> Push item on the heap, then pop and return the smallest item from the heap. The combined action runs more efficiently than heappush() followed by a separate call to heappop().
        """
        if len(self.small) == len(self.large):
            heappush(self.large, -heappushpop(self.small, -num))
        else:
            heappush(self.small, -heappushpop(self.large, num))

    def findMedian(self):
        # even length
        if len(self.small) == len(self.large):
            return float(self.large[0] - self.small[0]) / 2.0
        # odd length
        else:
            return float(self.large[0])
```

### 2-4) Minimum Cost to Connect Sticks
```python
# LC 1167 Minimum Cost to Connect Sticks
# V0
# IDEA : heapq
class Solution(object):
    def connectSticks(self, sticks):
        from heapq import * 
        heapify(sticks)
        res = 0
        while len(sticks) > 1:
            s1 = heappop(sticks)
            s2 = heappop(sticks)
            res += s1 + s2 # merge 2 shortest sticks
            heappush(sticks, s1 + s2)
        return res 
```

### 2-5) The kth Factor of n
```python
# LC 1492  The kth Factor of n
# note : there is also brute force, math approaches

# V1
# IDEA : HEAP
# Initialize max heap. Use PriorityQueue in Java and heap in Python. heap is a min-heap. Hence, to implement max heap, change the sign of divisor before pushing it into the heap.
# https://leetcode.com/problems/the-kth-factor-of-n/solution/
class Solution:
    def kthFactor(self, n, k):
        # push into heap
        # by limiting size of heap to k
        def heappush_k(num):
            heappush(heap, - num)
            if len(heap) > k:
                heappop(heap)
            
        # Python heap is min heap 
        # -> to keep max element always on top,
        # one has to push negative values
        heap = []
        for x in range(1, int(n**0.5) + 1):
            if n % x == 0:
                heappush_k(x)
                if x != n // x:
                    heappush_k(n // x)
                
        return -heappop(heap) if k == len(heap) else -1
```