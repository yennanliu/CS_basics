# Heap

<p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/heap_space_time_complexity.png" ></p>

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/heap_op_101.png"></p>



- Intro
    - https://leetcode.com/explore/learn/card/heap/
    - In many CS applications, we only need to `access the largest or smallest element` in the dataset. We DO NOT care about `the order of other data in the data set`. How do we efficiently access the largest or smallest element in the current dataset? -> The answer would be `Heap`.
    - `Heap` is a "complete binary tree"

    - Priority Queue (PQ)
        - Priority queue is one of the implementations of heap
        - a priority queue is an `abstract data type` similar to a regular queue or stack data structure in which each element additionally has a `"priority"` associated with it. In a priority queue, an element with high priority is served before an element with low priority.
        - `Heap != Priority Queue`
        - Priority Queue is a abstract data type
        - Heap is a way to implemenrt Priority Queue

- Heap
    - a special `completed binary tree` (heap is binary tree)
    - The value of each node must be no greater than (or no less than) the value of its child nodes.
    - Properties:
        - Insertion of an element into the Heap has a time complexity of `O( log N)`
        - Deletion of an element from the Heap has a time complexity of `O( log N)`
        - The maximum/minimum value in the Heap can be obtained with `O(1)` time complexity.
    <p align="center"><img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/type_of_heap.png" ></p>

    - Definition : if P is parent node, C is child node -> P always `<=` or `>=` C.
    - Types
        - `min` heap
            - if P parent node, C is child node -> P always <= C
            - 父節點總是 <= 子節點的值 (不需 右子節點 <= 父節點 <= 左子節點)
        - `max` heap
            - if P parent node, C is child node -> P always >= C
            - 父節點總是 >= 子節點的值 (不需 右子節點 <= 父節點 <= 左子節點)
- was invented for `heap sort`
- a heap is a specialized tree-based data structure which is essentially an almost completed tree that satisfies the heap property:
    - In a max heap
        - for any given node C, if P is a parent node of C, then the key (the value) of P is greater than or equal to the key of C
    - In a min heap
        - the key of P is less than or equal to the key of C.[2] The node at the "top" of the heap (with no parents) is called the root node.
- Ref
    - https://www.geeksforgeeks.org/heap-data-structure/

## 0) Concept  

### 0-1) Types
    - `priority queue`
        - LC 295, 787, 1492
    - K th biggest integer
        - LC 215

### 0-2) Pattern

## 1) General form

### 1-0) Basic OP

- V1
    - build heap
    - upHeap
    - downHeap
    - insert
    - update
    - get
    - delete
    - extract_max
    - delete_max
    - replace
    - find_max
- V2
    - Construct a Max Heap and a Min Heap.
    - Insert elements into a Heap.
    - Get the top element of a Heap.
    - Delete the top element from a Heap.
    - Get the length of a Heap.
    - Perform time and space complexity analysis for common applications that use a Heap.

- V3
    - up heap
        - (new big element added to max heap, move it to root)
    - down heap
        - (new samll element added to max heap, move it to sub tree)
    - build heap
    - PQ

### 1-1) heapq (`heap queue` AKA `priority queue`) (Py api)
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