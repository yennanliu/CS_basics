# Heap Data Structure

## Overview
**Heap** is a complete binary tree that satisfies the heap property, making it ideal for efficient access to the largest or smallest element in a dataset. It's the foundation for priority queues and heap sort algorithms.

<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/heap_space_time_complexity.png" ></p>

<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/heap_op_101.png" ></p>

### Key Properties
- **Time Complexity**: 
  - Insert: `O(log N)`
  - Delete: `O(log N)` 
  - Access Min/Max: `O(1)`
  - Build Heap: `O(N)`
- **Space Complexity**: `O(N)`
- **Core Idea**: Complete binary tree where parent-child relationship follows heap property
- **When to Use**: Need frequent access to min/max element, priority scheduling, sorting

### Heap Types
- **Min Heap**: Parent ≤ Children (root contains minimum)
- **Max Heap**: Parent ≥ Children (root contains maximum)

<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/type_of_heap.png" ></p>

### Priority Queue Relationship
- **Priority Queue**: Abstract data type with priority-based access
- **Heap**: Common implementation of priority queue
- **Key Difference**: Priority Queue is concept, Heap is implementation

### Problem Categories

#### **Pattern 1: Kth Element Problems**
- **Description**: Find the kth largest/smallest element in a dataset
- **Examples**: LC 215, 703, 1492 - Kth Largest Element, Kth Largest in Stream, Kth Factor
- **Pattern**: Use min/max heap of size k, maintain heap property

#### **Pattern 2: Top K Problems** 
- **Description**: Find top k elements with highest/lowest frequency or value
- **Examples**: LC 347, 692, 973 - Top K Frequent Elements, Top K Words, K Closest Points
- **Pattern**: Count frequency, use heap to maintain top k results

#### **Pattern 3: Merge Problems**
- **Description**: Merge multiple sorted arrays/lists efficiently
- **Examples**: LC 23, 373, 378 - Merge k Lists, K Smallest Pairs, Kth Smallest in Matrix
- **Pattern**: Use min heap to track current minimum from each source

#### **Pattern 4: Sliding Window Extrema**
- **Description**: Find min/max in sliding windows efficiently
- **Examples**: LC 239, 480, 1438 - Sliding Window Maximum, Sliding Median, Longest Subarray
- **Pattern**: Use heap with lazy deletion or deque for extrema tracking

#### **Pattern 5: Scheduling Problems**
- **Description**: Schedule tasks or events based on priority/timing
- **Examples**: LC 1353, 502, 630 - Max Events, IPO, Course Schedule III
- **Pattern**: Use heap to maintain events by start/end time or priority

#### **Pattern 6: Data Stream Problems**
- **Description**: Handle continuous data stream with min/max queries
- **Examples**: LC 295, 480, 1825 - Find Median, Sliding Median, Finding MK Average
- **Pattern**: Use two heaps (min + max) to maintain balanced structure

### References
- [LeetCode Heap Learn Card](https://leetcode.com/explore/learn/card/heap/)
- [GeeksforGeeks Heap Guide](https://www.geeksforgeeks.org/heap-data-structure/)

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Time Complexity | When to Use |
|---------------|----------|-----------------|-------------|
| **Universal Heap** | General min/max operations | O(log N) insert/delete | Any heap-based problem |
| **Kth Element** | Find kth largest/smallest | O(N log k) | Fixed size heap needed |
| **Top K Frequency** | Find most/least frequent | O(N log k) | Frequency-based selection |
| **Merge K Sources** | Merge sorted arrays/lists | O(N log k) | Multiple sorted inputs |
| **Two Heap System** | Maintain median/balance | O(log N) | Data stream with median |
| **Heap + HashSet** | Duplicate handling | O(log N) | Need uniqueness constraint |

### Universal Heap Template
```python
def solve_with_heap(nums, k=None):
    import heapq
    
    # Create heap (min heap by default in Python)
    heap = []
    
    # Build heap approach 1: Insert elements one by one
    for num in nums:
        heapq.heappush(heap, num)
    
    # Build heap approach 2: Heapify existing array
    # heapq.heapify(nums)  # O(N) time
    
    # Access min element (don't remove): heap[0]
    # Remove min element: heapq.heappop(heap)
    # Insert element: heapq.heappush(heap, value)
    
    # For max heap, use negative values
    # max_heap = [-x for x in nums]
    # heapq.heapify(max_heap)
    # max_val = -max_heap[0]  # Get max without removing
    # max_val = -heapq.heappop(max_heap)  # Remove and get max
    
    return heap
```

```java
// Java Universal Template
public class HeapSolution {
    public void solveWithHeap(int[] nums, int k) {
        // Min Heap
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        
        // Max Heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        
        // Add elements
        for (int num : nums) {
            minHeap.offer(num);
        }
        
        // Access min: minHeap.peek()
        // Remove min: minHeap.poll()
        // Add element: minHeap.offer(value)
    }
}
```

### Specific Pattern Templates

#### **1. Kth Element Template**

**💡 Key Insight:**
- **`kth smallest element` = biggest element from a Max PQ of size k**
  - Use **max heap** of size k to find kth smallest
  - The root (peek) of the max heap is the kth smallest element
  - Why? Keep only the k smallest elements; the largest among them is the kth smallest overall

- **`kth largest element` = smallest element from a Min PQ of size k**
  - Use **min heap** of size k to find kth largest
  - The root (peek) of the min heap is the kth largest element
  - Why? Keep only the k largest elements; the smallest among them is the kth largest overall

```python
def find_kth_largest(nums, k):
    import heapq

    # Method 1: Min heap of size k
    heap = []
    for num in nums:
        if len(heap) < k:
            heapq.heappush(heap, num)
        elif num > heap[0]:
            heapq.heapreplace(heap, num)

    return heap[0]  # kth largest

def find_kth_smallest(nums, k):
    import heapq

    # Method 1: Max heap of size k (use negative values)
    heap = []
    for num in nums:
        if len(heap) < k:
            heapq.heappush(heap, -num)
        elif num < -heap[0]:
            heapq.heapreplace(heap, -num)

    return -heap[0]  # kth smallest
```

#### **2. Top K Frequency Template**
```python
def top_k_frequent(nums, k):
    from collections import Counter
    import heapq
    
    # Count frequencies
    count = Counter(nums)
    
    # Method 1: Min heap approach
    heap = []
    for num, freq in count.items():
        heapq.heappush(heap, (freq, num))
        if len(heap) > k:
            heapq.heappop(heap)
    
    return [item[1] for item in heap]
    
    # Method 2: Max heap approach
    # heap = [(-freq, num) for num, freq in count.items()]
    # heapq.heapify(heap)
    # return [heapq.heappop(heap)[1] for _ in range(k)]
```

#### **3. Merge K Sources Template**
```python
def merge_k_sorted_arrays(arrays):
    import heapq
    
    heap = []
    result = []
    
    # Initialize heap with first element from each array
    for i, arr in enumerate(arrays):
        if arr:  # Check if array is not empty
            heapq.heappush(heap, (arr[0], i, 0))
    
    while heap:
        val, array_idx, element_idx = heapq.heappop(heap)
        result.append(val)
        
        # Add next element from same array
        if element_idx + 1 < len(arrays[array_idx]):
            next_val = arrays[array_idx][element_idx + 1]
            heapq.heappush(heap, (next_val, array_idx, element_idx + 1))
    
    return result
```

#### **4. Two Heap System Template (Median)**
```python
class MedianFinder:
    def __init__(self):
        import heapq
        self.small = []  # max heap (use negative values)
        self.large = []  # min heap
    
    def addNum(self, num):
        import heapq
        
        # Add to appropriate heap
        if len(self.small) == len(self.large):
            heapq.heappush(self.large, -heapq.heappushpop(self.small, -num))
        else:
            heapq.heappush(self.small, -heapq.heappushpop(self.large, num))
    
    def findMedian(self):
        if len(self.small) == len(self.large):
            return (self.large[0] - self.small[0]) / 2.0
        else:
            return float(self.large[0])
```

#### **5. Heap with Deduplication Template**
```python
def solve_with_unique_heap(nums):
    import heapq
    
    heap = []
    seen = set()
    
    for num in nums:
        if num not in seen:
            heapq.heappush(heap, num)
            seen.add(num)
    
    return heap
```

### Python heapq API Reference
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
# V0
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

## 2) LC Example

### 2-0) Top K Frequent Elements
```java
// java
// LC 347
// V1
// IDEA : HEAP
// https://leetcode.com/problems/top-k-frequent-elements/editorial/
public int[] topKFrequent_2(int[] nums, int k) {
    // O(1) time
    if (k == nums.length) {
        return nums;
    }

    // 1. build hash map : character and how often it appears
    // O(N) time
    Map<Integer, Integer> count = new HashMap();
    for (int n: nums) {
        count.put(n, count.getOrDefault(n, 0) + 1);
    }

    // init heap 'the less frequent element first'
    Queue<Integer> heap = new PriorityQueue<>(
            (n1, n2) -> count.get(n1) - count.get(n2));

    // 2. keep k top frequent elements in the heap
    // O(N log k) < O(N log N) time
    for (int n: count.keySet()) {
        heap.add(n);
        if (heap.size() > k) heap.poll();
    }

    // 3. build an output array
    // O(k log k) time
    int[] top = new int[k];
    for(int i = k - 1; i >= 0; --i) {
        top[i] = heap.poll();
    }
    return top;
}
```

### 2-1) Kth Largest Element in a Stream
```python
# 703 Kth Largest Element in a Stream

# V0
# IDEA : HEAP
# NOTE !!! : we ONLY need to return k biggest element
#           -> we ONLY need to keep at most k element
#               -> if element more than k, then pop element out
#                   -> then return 0 element directly
import heapq
class KthLargest:

    def __init__(self, k, nums):
        self.k = k
        heapq.heapify(nums)
        self.heap = nums
        while len(self.heap) > k:
            heapq.heappop(self.heap)

    def add(self, val):
        if len(self.heap) < self.k:
            heapq.heappush(self.heap, val)
        else:
            heapq.heappushpop(self.heap, val)
            
        return self.heap[0]

# V0'
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
            # heapreplace : pop first, then push
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

### 2-6) Least Number of Unique Integers after K Removals
```python
# LC 1481. Least Number of Unique Integers after K Removals
# NOTE : there's also Counter approaches
# V0
# IDEA : Counter
from collections import Counter
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        # edge case
        if not arr:
            return 0
        cnt = dict(Counter(arr))
        cnt_sorted = sorted(cnt.items(), key = lambda x : x[1])
        #print ("cnt_sorted = " + str(cnt_sorted))
        removed = 0
        for key, freq in cnt_sorted:
            """
            NOTE !!!
                -> we need to remove exactly k elements and make remain unique integers as less as possible
                -> since we ALREADY sort num_counter,
                -> so the elements NOW are ordering with their count
                    -> so we need to remove ALL element while k still > 0
                    -> so k -= freq, since for element key, there are freq count for it in arr
            """
            if freq <= k:
                k -= freq
                removed += 1

        return len(cnt.keys()) - removed

# V1'''''
# IDEA : Counter + heapq
# https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/discuss/704179/python-solution%3A-Counter-and-Priority-Queue
# IDEA
# -> Count the occurence of each number.
# -> We want to delete the number with lowest occurence thus we can use minimum steps to reduce the total unique numbers in the list. For example,[4,3,1,1,3,3,2]. The Counter of this array will be: {3:3, 1:2, 4:1, 2:1}. Given k = 3, the greedy approach is to delete 2 and 4 first because both of them are appearing once. We need an ordering data structure to give us the lowest occurence of number each time. As you may know, Priority Queue comes to play
# -> Use heap to build PQ for the counter. We store each member as a tuple: (count, number) Python heap module will sort it based on the first member of the tuple.
# -> loop through k times to pop member out of heap and check if we need to push it back
class Solution(object):
    def findLeastNumOfUniqueInts(self, arr, k):
            # use counter, and heap (priority queue)
            from collections import Counter
            import heapq
            h = []
            for key, val in Counter(arr).items():
                heapq.heappush(h,(val,key))

            while k > 0:
                item = heapq.heappop(h)    
                if item[0] != 1:
                    heapq.heappush(h, (item[0]-1, item[1]))      
                k -=1

            return len(h)

# V1'''''''
# IDEA : Counter + heapq
# https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/discuss/1542356/Python-MinHeap-Solution
class Solution:
    def findLeastNumOfUniqueInts(self, arr, k):
        counter = collections.Counter(arr)
        minHeap = []
        for key, val in counter.items():
            heapq.heappush(minHeap, val)
        
        while k:
            minHeap[0] -= 1
            if minHeap[0] == 0:
                heapq.heappop(minHeap)
            k -= 1
        
        return len(minHeap)
```

### 2-7) Maximum Number of Events That Can Be Attended
```python
# LC 1353. Maximum Number of Events That Can Be Attended
# V0
# IDEA : PRIORITY QUEUE
# NOTE !!!
# We just need to attend d where startTimei <= d <= endTimei, then we CAN attend the meeting
# startTimei <= d <= endTimei. You can only attend one event at any time d.
class Solution:
    def maxEvents(self, events: List[List[int]]) -> int:
        # algorithm: greedy+heap
        # step1: loop from min to max day
        # step2: each iteration put the candidates in the heap
        # step3: each iteration eliminate the ineligibility ones from the heap
        # step4: each iteration choose one event attend if it is possible
        # time complexity: O(max(n1logn1, n2))
        # space complexity: O(n1)
        events.sort(key = lambda x: -x[0])
        h = []
        ans = 0
        minDay = 1 #events[-1][0]
        maxDay = 100001 #max(x[1] for x in events) + 1
        for day in range(minDay, maxDay):
            # add all days that can start today
            while events and events[-1][0] <= day:
                heapq.heappush(h, events.pop()[1])
            
            # remove all days that cannot start
            while h and h[0]<day:
                heapq.heappop(h)
            
            # if can attend meeting
            if h:
                heapq.heappop(h)
                ans += 1            
        return ans

# V0'
# IDEA : PRIORITY QUEUE
# NOTE !!!
# We just need to attend d where startTimei <= d <= endTimei, then we CAN attend the meeting
# startTimei <= d <= endTimei. You can only attend one event at any time d.
class Solution:
    def maxEvents(self, events):
        events.sort(key = lambda x: (-x[0], -x[1]))
        endday = []
        ans = 0
        for day in range(1, 100001, 1):
            # check if events is not null and  events start day = day (events[-1][0] == day)
            # if above conditions are True, we insert "events.pop()[1]" to endday 
            while events and events[-1][0] == day:
                heapq.heappush(endday, events.pop()[1])
            # check if endday is not null, if first day in endday < day, then we pop its element
            while endday and endday[0] < day:
                heapq.heappop(endday)
            # if there is still remaining elements in endday -> means we CAN atten the meeting, so ans += 1 
            if endday:
                ans += 1
                heapq.heappop(endday)
        return  ans
```

### 2-8) Maximum Frequency Stack
```python
# LC 895. Maximum Frequency Stack
# V1'
# IDEA : STACK
# https://leetcode.com/problems/maximum-frequency-stack/solution/
class FreqStack(object):

    def __init__(self):
        self.freq = collections.Counter()
        self.group = collections.defaultdict(list)
        self.maxfreq = 0

    def push(self, x):
        f = self.freq[x] + 1
        self.freq[x] = f
        if f > self.maxfreq:
            self.maxfreq = f
        self.group[f].append(x)

    def pop(self):
        x = self.group[self.maxfreq].pop()
        self.freq[x] -= 1
        if not self.group[self.maxfreq]:
            self.maxfreq -= 1

        return x
```

### 2-9) Ugly Number II
```python
# LC 264 Ugly Number II
# V0 
# IDEA : HEAP
# using brute force is too slow -> time out error
# -> so here we generate "ugly number" by ourself, and order them via heap (heappush)
# -> and return the i-th element as request
import heapq
class Solution(object):
    def nthUglyNumber(self, n):
        # NOTE : we init heap as [1], visited = set([1])
        heap = [1]
        visited = set([1])      
        for i in range(n):
            # NOTE !!! trick here, we use last element via heappop
            val = heapq.heappop(heap)
            # and we genrate ugly by ourself
            for factor in [2,3,5]:
                if val*factor not in visited:
                    heapq.heappush(heap, val*factor)
                    visited.add(val*factor)    
        return val
```


### 2-10) Find K Pairs with Smallest Sums

```java
// java
// LC 373

// V0-1
// IDEA: PQ (fixed by gpt)
/**
 *  IDEA:
 *
 *  ✅ Use a min-heap (priority queue) to:
 *
 *  - Always retrieve the next smallest sum pair
 *
 *  - Efficiently keep track of candidates
 *
 */
public List<List<Integer>> kSmallestPairs_0_1(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> res = new ArrayList<>();

    if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0 || k <= 0) {
        return res;
    }

    // Min-heap to store [sum, index in nums1, index in nums2]
    /**
     *  NOTE !!!
     *
     *  min PQ structure:
     *
     *   [ sum, nums_1_idx, nums_2_idx ]
     *
     *
     *   - Heap stores: int[] {sum, index in nums1, index in nums2}
     *
     *   - It's sorted by sum = nums1[i] + nums2[j]
     *
     */
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

    // Add the first k pairs (nums1[0] + nums2[0...k])
    /**  NOTE !!!
     *
     *  we init PQ as below:
     *
     *  - We insert first k pairs: (nums1[i], nums2[0])
     *
     *   - Why nums2[0]?
     *     -> Because nums2 is sorted,
     *       so (nums1[i], nums2[0]) is the smallest possible for that row.
     *
     *
     *   -> so, we insert `nums_1[i] + nums_2[0]`  to PQ for now
     *
     *
     */
    for (int i = 0; i < nums1.length && i < k; i++) {
        minHeap.offer(new int[] { nums1[i] + nums2[0], i, 0 });
    }

    /** NOTE !!!   Pop from Heap and Expand
     *
     * - Poll the `smallest` sum pair (i, j) and add it to result.
     *
     * - You now consider the next element in that row, which is (i, j + 1).
     *
     */
    while (k > 0 && !minHeap.isEmpty()) {

        // current smallest val from PQ
        int[] current = minHeap.poll();
        int i = current[1]; // index in nums1
        int j = current[2]; // index in nums2

        res.add(Arrays.asList(nums1[i], nums2[j]));

        /**
         *  NOTE !!! Push the Next Pair in the Same Row
         *
         *  - This ensures you're exploring pairs in increasing sum order:
         *
         *      - From (i, 0) → (i, 1) → (i, 2) ...
         *
         * - Since the arrays are sorted, this gives increasing sums
         *
         *
         */
        if (j + 1 < nums2.length) {
            minHeap.offer(new int[] { nums1[i] + nums2[j + 1], i, j + 1 });
        }

        k--;
    }

    return res;
}
```

### 2-11) Kth Smallest Element in a Sorted Matrix

```java
// java
// LC 378
// Reference: leetcode_java/src/main/java/LeetCodeJava/Heap/KthSmallestElementInASortedMatrix.java

// V0-1
// IDEA: MAX PQ (Priority Queue)
/**
 *  KEY INSIGHT !!!
 *
 *  `kth smallest element` ~= biggest element from a Max PQ
 *
 *  - Use MAX heap of size k to find kth smallest element
 *  - Keep only the k smallest elements in the heap
 *  - The root (peek) of max heap = kth smallest element overall
 *
 *  Why?
 *  - We maintain a max heap of size k
 *  - This heap contains the k smallest elements seen so far
 *  - The largest among these k elements is at the root
 *  - This root element is exactly the kth smallest element
 */
public int kthSmallest_0_1(int[][] matrix, int k) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
        return 0;
    }

    int n = matrix.length;
    int m = matrix[0].length;

    /** NOTE !!!
     *
     *  Use MAX PQ (max heap)
     *
     *  Since the problem asks for `kth smallest element`
     *  = biggest element from a Max PQ of size k
     */
    // Max-heap: largest value at top
    PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            pq.offer(matrix[i][j]);
            /** NOTE !!!
             *
             *  Use `pq.size()` to check if we've reached k elements
             *
             *  NO NEED for separate counter variables like size or cnt
             */
            if (pq.size() > k) {
                pq.poll(); // remove largest, keep only k smallest
            }
        }
    }

    // Top of max-heap = kth smallest element
    return pq.peek();
}
```

## Problems by Pattern

### Pattern-Based Problem Classification

#### **Pattern 1: Kth Element Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Kth Largest Element in Array | 215 | Min heap of size k | Medium | Kth Element |
| Kth Largest Element in Stream | 703 | Maintain min heap size k | Easy | Kth Element |
| Kth Smallest Element in BST | 230 | Inorder + heap or Morris | Medium | Kth Element |
| Kth Smallest in Sorted Matrix | 378 | Min heap with coordinates | Medium | Merge K Sources |
| Find Kth Largest in Each Subarray | 1738 | Sliding window + heap | Hard | Two Heap System |
| Kth Factor of n | 1492 | Max heap with size limit | Medium | Kth Element |
| Kth Smallest Prime Fraction | 786 | Min heap with fractions | Medium | Merge K Sources |

#### **Pattern 2: Top K Frequency Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Top K Frequent Elements | 347 | Counter + min heap | Medium | Top K Frequency |
| Top K Frequent Words | 692 | Counter + custom comparator | Medium | Top K Frequency |
| K Closest Points to Origin | 973 | Distance + max heap | Medium | Top K Frequency |
| Find K Closest Elements | 658 | Distance + heap/two pointers | Medium | Top K Frequency |
| Least Number of Unique Integers after K Removals | 1481 | Counter + min heap | Medium | Top K Frequency |
| Reorganize String | 767 | Frequency + max heap | Medium | Top K Frequency |
| Task Scheduler | 621 | Frequency + max heap + queue | Medium | Top K Frequency |

#### **Pattern 3: Merge Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Merge k Sorted Lists | 23 | Min heap with ListNode | Hard | Merge K Sources |
| Find K Pairs with Smallest Sums | 373 | Min heap with pairs | Medium | Merge K Sources |
| Smallest Range Covering Elements from K Lists | 632 | Min heap + sliding window | Hard | Merge K Sources |
| Merge k Sorted Arrays | N/A | Min heap with indices | Medium | Merge K Sources |
| Kth Smallest Element in Sorted Matrix | 378 | Min heap BFS-style | Medium | Merge K Sources |

#### **Pattern 4: Sliding Window Extrema**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Sliding Window Maximum | 239 | Deque or lazy heap | Hard | Universal Heap |
| Sliding Window Median | 480 | Two heaps system | Hard | Two Heap System |
| Constrained Subsequence Sum | 1425 | DP + monotonic deque | Hard | Universal Heap |
| Longest Continuous Subarray | 1438 | Two heaps + sliding window | Medium | Two Heap System |

#### **Pattern 5: Scheduling Problems**  
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Maximum Number of Events Attended | 1353 | Min heap by end time | Medium | Universal Heap |
| IPO | 502 | Two heaps (profit + capital) | Hard | Two Heap System |
| Course Schedule III | 630 | Greedy + max heap | Hard | Universal Heap |
| Meeting Rooms II | 253 | Min heap by end time | Medium | Universal Heap |
| Car Pooling | 1094 | Heap by drop-off time | Medium | Universal Heap |
| Minimum Cost to Hire K Workers | 857 | Heap + greedy | Hard | Top K Frequency |

#### **Pattern 6: Data Stream Problems**
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Find Median from Data Stream | 295 | Two heaps (balanced) | Hard | Two Heap System |
| Design Twitter | 355 | Heap merge for timeline | Medium | Merge K Sources |
| Kth Largest Element in Stream | 703 | Min heap maintenance | Easy | Kth Element |
| Finding MK Average | 1825 | Three data structures | Hard | Two Heap System |

### Advanced Heap Problems
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Ugly Number II | 264 | Three pointers + heap | Medium | Heap + HashSet |
| Super Ugly Number | 313 | Dynamic heap generation | Medium | Heap + HashSet |
| Maximum Frequency Stack | 895 | Frequency map + heap | Hard | Top K Frequency |
| Minimum Cost to Connect Sticks | 1167 | Greedy + min heap | Medium | Universal Heap |
| Last Stone Weight | 1046 | Max heap simulation | Easy | Universal Heap |
| Maximum Performance of Team | 1383 | Sort + heap greedy | Hard | Top K Frequency |

### Heap + Other Data Structure Combos
| Problem | LC # | Key Technique | Difficulty | Template |
|---------|------|---------------|------------|----------|
| Design Twitter | 355 | Heap + HashMap + Set | Medium | Merge K Sources |
| Trapping Rain Water II | 407 | Heap + BFS | Hard | Universal Heap |
| Swim in Rising Water | 778 | Heap + Union Find | Hard | Universal Heap |
| Cheapest Flights Within K Stops | 787 | Dijkstra + heap | Medium | Universal Heap |
| Network Delay Time | 743 | Dijkstra + min heap | Medium | Universal Heap |

## Pattern Selection Strategy

### Decision Framework Flowchart

```
Problem Analysis for Heap Usage:

1. Do you need the kth largest/smallest element?
   ├── YES → Use Kth Element Template
   │   ├── Static dataset → Build heap of size k
   │   └── Stream data → Maintain heap of size k
   └── NO → Continue to 2

2. Do you need top k elements by frequency/value?
   ├── YES → Use Top K Frequency Template
   │   ├── Need frequency count → Counter + heap
   │   └── Direct value comparison → Simple heap
   └── NO → Continue to 3

3. Are you merging multiple sorted sources?
   ├── YES → Use Merge K Sources Template
   │   ├── Arrays/Lists → Min heap with indices
   │   └── Complex objects → Min heap with custom comparator
   └── NO → Continue to 4

4. Do you need to maintain min/max in sliding window?
   ├── YES → Use Sliding Window Template
   │   ├── Simple min/max → Deque (preferred)
   │   └── Complex conditions → Heap with lazy deletion
   └── NO → Continue to 5

5. Are you scheduling events/tasks by time/priority?
   ├── YES → Use Scheduling Template
   │   ├── Event intervals → Heap by end/start time
   │   └── Task priorities → Heap by priority value
   └── NO → Continue to 6

6. Do you need to maintain median or balanced structure?
   ├── YES → Use Two Heap System Template
   │   ├── Median tracking → Min heap + max heap
   │   └── Balance requirement → Size-balanced heaps
   └── NO → Use Universal Heap Template
```

### Template Selection Guide

#### **When to Use Min Heap vs Max Heap**
```python
# Min Heap Usage (Python default)
situations = [
    "Finding kth largest → Use min heap of size k",
    "Merge k sorted arrays → Track smallest current elements", 
    "Dijkstra's algorithm → Process smallest distances first",
    "Event scheduling → Process earliest end times"
]

# Max Heap Usage (negate values in Python)
situations = [
    "Finding kth smallest → Use max heap of size k (negated)",
    "Task scheduling → Process highest priority first",
    "Median tracking → Left half uses max heap",
    "Frequency problems → Process most frequent first"
]
```

#### **Heap Size Considerations**
- **Fixed size k**: For kth element problems, maintain heap size ≤ k
- **Unbounded**: For merge problems, heap size = number of sources
- **Balanced**: For median problems, keep |size1 - size2| ≤ 1

### Common Decision Points

#### **Heap vs Other Data Structures**
```python
# Use Heap When:
conditions = [
    "Need frequent min/max access: O(1)",
    "Dynamic insertion with ordering: O(log N)",
    "Kth element queries: O(N log k)",
    "Priority-based processing needed"
]

# Use Array/Sort When:
conditions = [
    "One-time kth element: O(N) with quickselect",
    "All elements needed sorted: O(N log N)",
    "Static dataset, no updates"
]

# Use Deque When:
conditions = [
    "Sliding window min/max: O(1) amortized",
    "Simple monotonic requirements",
    "No priority-based insertion needed"
]
```

## Summary & Quick Reference

### Complexity Quick Reference
| Operation | Time | Space | Notes |
|-----------|------|-------|--------|
| **Build Heap** | O(N) | O(N) | Heapify existing array |
| **Insert** | O(log N) | O(1) | Add single element |
| **Delete Min/Max** | O(log N) | O(1) | Remove root element |
| **Peek Min/Max** | O(1) | O(1) | Access root without removal |
| **Search Element** | O(N) | O(1) | Heap doesn't support efficient search |
| **Merge Two Heaps** | O(N + M) | O(N + M) | Create new heap |

### Template Quick Reference
| Template | Pattern | Key Code Snippet |
|----------|---------|------------------|
| **Universal Heap** | General operations | `heapq.heappush(heap, val)` |
| **Kth Element** | Fixed size heap | `if len(heap) > k: heappop(heap)` |
| **Top K Frequency** | Counter + heap | `Counter(nums) + heappush` |
| **Merge K Sources** | Multi-source merge | `heappush(heap, (val, src_idx, elem_idx))` |
| **Two Heap System** | Balanced structure | `small_heap (max) + large_heap (min)` |
| **Heap + HashSet** | Deduplication | `seen = set(); heappush if not in seen` |

### Common Patterns & Tricks

#### **Max Heap in Python (Using Negation)**
```python
import heapq

# Create max heap by negating values
max_heap = [-x for x in nums]
heapq.heapify(max_heap)

# Insert into max heap
heapq.heappush(max_heap, -val)

# Get max value (remember to negate back)
max_val = -max_heap[0]  # peek
max_val = -heapq.heappop(max_heap)  # pop
```

#### **Heap with Custom Objects**
```python
# Method 1: Using tuples (automatic comparison)
heap = []
heapq.heappush(heap, (priority, data))

# Method 2: Using custom class with __lt__
class Task:
    def __init__(self, priority, data):
        self.priority = priority
        self.data = data
    
    def __lt__(self, other):
        return self.priority < other.priority

heap = []
heapq.heappush(heap, Task(1, "high priority"))
```

#### **Lazy Deletion Pattern**
```python
# For sliding window problems where direct deletion is needed
class LazyHeap:
    def __init__(self):
        self.heap = []
        self.deleted = set()
    
    def push(self, val):
        heapq.heappush(self.heap, val)
    
    def top(self):
        while self.heap and self.heap[0] in self.deleted:
            heapq.heappop(self.heap)
        return self.heap[0] if self.heap else None
    
    def delete(self, val):
        self.deleted.add(val)
```

### Problem-Solving Steps

1. **Identify the Pattern**
   - Look for keywords: "kth", "top k", "minimum/maximum", "merge", "median"
   - Determine if you need min heap, max heap, or both

2. **Choose the Right Template**
   - Use decision flowchart to select appropriate pattern
   - Consider space/time complexity requirements

3. **Handle Edge Cases**
   - Empty input arrays
   - k larger than array size
   - Single element cases

4. **Optimize Implementation**
   - Use heapify() for initial heap construction
   - Consider lazy deletion for sliding window problems
   - Balance heap sizes for median tracking

### Common Mistakes & Tips

**🚫 Common Mistakes:**

1. **Wrong heap type**: Using min heap when max heap needed (or vice versa)
   - Fix: Remember min heap for "kth largest", max heap for "kth smallest"

2. **Forgetting to negate for max heap**: Direct usage of min heap for max operations
   - Fix: Always negate values when simulating max heap in Python

3. **Not maintaining heap size**: Allowing unlimited growth in kth element problems
   - Fix: Always check heap size and pop when exceeding k

4. **Index errors in merge problems**: Incorrect boundary checking
   - Fix: Always verify array bounds before accessing elements

5. **Improper balancing**: Unbalanced heap sizes in two-heap system
   - Fix: Maintain |size1 - size2| ≤ 1

**✅ Best Practices:**

1. **Choose appropriate heap size**: Only maintain necessary elements
2. **Use heapify() for initialization**: O(N) is better than N × O(log N)
3. **Handle duplicates properly**: Use sets when uniqueness is required
4. **Consider alternative approaches**: Sometimes sorting is simpler for static data
5. **Test with edge cases**: Empty arrays, single elements, k=1 or k=n

### Interview Tips

1. **Ask clarifying questions**:
   - "Do we need to handle duplicates?"
   - "Can k be larger than array size?"
   - "Are we dealing with a stream or static data?"

2. **Explain your approach clearly**:
   - Start with the pattern recognition
   - Explain why heap is the optimal choice
   - Walk through the algorithm step-by-step

3. **Discuss complexity trade-offs**:
   - Compare heap solution with sorting approach
   - Explain space complexity implications
   - Consider online vs offline scenarios

4. **Code systematically**:
   - Start with the basic heap operations
   - Add edge case handling
   - Test with simple examples

5. **Common follow-up questions**:
   - "How would you handle updates to the data?"
   - "What if we need the kth element multiple times?"
   - "Can you optimize for space/time?"

### Related Topics

- **Priority Queues**: Heap is the primary implementation
- **Graph Algorithms**: Dijkstra's algorithm uses min heap
- **Greedy Algorithms**: Many greedy problems use heaps for optimization
- **Sorting**: Heap sort is O(N log N) in-place sorting
- **Binary Trees**: Heaps are complete binary trees
- **Dynamic Programming**: Some DP problems benefit from heap optimization

### Language-Specific Notes

#### **Python heapq**
- Only provides min heap
- Use negation for max heap
- Functions: heappush, heappop, heapify, nlargest, nsmallest

#### **Java PriorityQueue**
- Default is min heap
- Use custom comparator for max heap: `new PriorityQueue<>((a, b) -> b - a)`
- Methods: offer, poll, peek, size

#### **C++ priority_queue**
- Default is max heap
- Use `priority_queue<int, vector<int>, greater<int>>` for min heap
- Methods: push, pop, top, size