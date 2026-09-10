"""

295. Find Median from Data Stream
Hard

The median is the middle value in an ordered integer list. If the size of the list is even, there is no middle value and the median is the mean of the two middle values.

For example, for arr = [2,3,4], the median is 3.
For example, for arr = [2,3], the median is (2 + 3) / 2 = 2.5.
Implement the MedianFinder class:

MedianFinder() initializes the MedianFinder object.
void addNum(int num) adds the integer num from the data stream to the data structure.
double findMedian() returns the median of all elements so far. Answers within 10-5 of the actual answer will be accepted.
 

Example 1:

Input
["MedianFinder", "addNum", "addNum", "findMedian", "addNum", "findMedian"]
[[], [1], [2], [], [3], []]
Output
[null, null, null, 1.5, null, 2.0]

Explanation
MedianFinder medianFinder = new MedianFinder();
medianFinder.addNum(1);    // arr = [1]
medianFinder.addNum(2);    // arr = [1, 2]
medianFinder.findMedian(); // return 1.5 (i.e., (1 + 2) / 2)
medianFinder.addNum(3);    // arr[1, 2, 3]
medianFinder.findMedian(); // return 2.0
 

Constraints:

-105 <= num <= 105
There will be at least one element in the data structure before calling findMedian.
At most 5 * 104 calls will be made to addNum and findMedian.
 

Follow up:

If all integer numbers from the stream are in the range [0, 100], how would you optimize your solution?
If 99% of all integer numbers from the stream are in the range [0, 100], how would you optimize your solution?

"""


# V0
class MedianFinder(object):

    def __init__(self):
        pass

    def addNum(self, num):
        """
        :type num: int
        :rtype: None
        """
        pass

    def findMedian(self):
        """
        :rtype: float
        """
        pass



# V0-1
# IDEA: 2 PQ (small, big) (gemini)
"""

1. Steps:


step 1)
    先入 small：不管三七二十一，先塞進 small。


step 2)
    過濾出最大值給 large：將 small 裡的最大值彈出放入 large。
    這一步自動確保了 small 的所有數字 <= large 的所有數字。

step 3)
    保持數量平衡：如果 large 的長度超過 small，就把 large 的最小值搬回 small。

    -> 如此一來，small 的長度只可能等於 len(large) 或 len(large) + 1 !!





2. 
    利用 Python heapq 預設為小頂堆（Min-Heap）的特性：

    small (Max-Heap)：存負數（-num）來模擬大頂堆。

    large (Min-Heap)：直接存正數。



3. NOTE !!!


maintain PQ as

    -> small 的長度只可能等於 len(large) 或 len(large) + 1

    -> len(small_half) >= len(large_half)
"""
import heapq

class MedianFinder(object):

    def __init__(self):
        # small_half:
        # Max-Heap
        # 維護較小的一半數字
        # Python 沒有 Max-Heap，所以存負數
        self.small_half = []

        # large_half:
        # Min-Heap
        # 維護較大的一半數字
        # 存正數
        self.large_half = []

    def addNum(self, num):
        """
        :type num: int
        :rtype: None
        """

        # Step 1:
        # 先把 num 放進 small_half
        heapq.heappush(self.small_half, -num)

        # Step 2:
        # 把 small_half 最大的數字移到 large_half
        #
        # 因為 small_half 存負數，
        # heap top 是「負數中最小」= 原本最大的數字
        moved_num = -heapq.heappop(self.small_half)

        heapq.heappush(self.large_half, moved_num)

        # Step 3:
        # 維持：
        #
        # len(small_half) >= len(large_half)
        #
        # 如果 large_half 比 small_half 多，
        # 就把 large_half 最小的數字移回 small_half
        if len(self.large_half) > len(self.small_half):
            moved_num = heapq.heappop(self.large_half)
            heapq.heappush(self.small_half, -moved_num)

    def findMedian(self):
        """
        :rtype: float
        """

        small_size = len(self.small_half)
        large_size = len(self.large_half)

        # Case 1:
        # 奇數個數字
        #
        # small_half 會多一個
        # median = small_half 最大值
        if small_size > large_size:
            return float(-self.small_half[0])

        # Case 2:
        # 偶數個數字
        #
        # median =
        # (small_half 最大值 + large_half 最小值) / 2
        return (-self.small_half[0] + self.large_half[0]) / 2.0



# V0-2
# IDEA: 2 PQ (small, big PQ) (gpt)
"""
NOTE !!!

->

        # small_half: max heap
        # Store negative values because Python heapq is a min heap
        self.small_half = []

        # big_half: min heap
        self.big_half = []

"""
import heapq


class MedianFinder(object):

    def __init__(self):

        # small_half: max heap
        # Store negative values because Python heapq is a min heap
        self.small_half = []

        # big_half: min heap
        self.big_half = []

        # Invariant:
        # len(small_half) == len(big_half)
        # OR
        # len(small_half) == len(big_half) + 1
        #
        # And:
        # max(small_half) <= min(big_half)

    def addNum(self, num):
        """
        :type num: int
        :rtype: None
        """

        # Add to small_half first
        heapq.heappush(self.small_half, -num)

        # Move the largest value from small_half
        # to big_half
        moved_num = -heapq.heappop(self.small_half)

        heapq.heappush(self.big_half, moved_num)

        # Keep small_half having the same size
        # or one more element than big_half
        if len(self.big_half) > len(self.small_half):
            moved_num = heapq.heappop(self.big_half)
            heapq.heappush(self.small_half, -moved_num)

    def findMedian(self):
        """
        :rtype: float
        """

        # Odd number of elements
        # small_half has one extra element
        if len(self.small_half) > len(self.big_half):
            return float(-self.small_half[0])

        # Even number of elements
        left = -self.small_half[0]
        right = self.big_half[0]

        return (left + right) / 2.0


# V0-2
# IDEA: 2 PQ (small, big) (GPT)
import heapq

class MedianFinder(object):

    def __init__(self):
        # small half
        # max heap
        # Python only has min heap,
        # so store negative values
        self.s_pq = []

        # big half
        # min heap
        self.b_pq = []

    def addNum(self, num):
        """
        :type num: int
        :rtype: None
        """

        # Put num into the correct heap
        if not self.s_pq:
            heapq.heappush(self.s_pq, -num)

        elif num <= -self.s_pq[0]:
            # num belongs to small half
            heapq.heappush(self.s_pq, -num)

        else:
            # num belongs to big half
            heapq.heappush(self.b_pq, num)

        # Balance heaps
        #
        # small heap can have at most 1 more element
        if len(self.s_pq) > len(self.b_pq) + 1:
            num = -heapq.heappop(self.s_pq)
            heapq.heappush(self.b_pq, num)

        elif len(self.b_pq) > len(self.s_pq):
            num = heapq.heappop(self.b_pq)
            heapq.heappush(self.s_pq, -num)

    def findMedian(self):
        """
        :rtype: float
        """

        s_size = len(self.s_pq)
        b_size = len(self.b_pq)

        # Same size
        if s_size == b_size:
            return (-self.s_pq[0] + self.b_pq[0]) / 2.0

        # Small heap has one extra element
        return float(-self.s_pq[0])


# V0-3
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
# time = O(log n) per addNum, O(1) per findMedian
# space = O(n)
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

# V1
# https://leetcode.com/problems/find-median-from-data-stream/discuss/74062/Short-simple-JavaC%2B%2BPython-O(log-n)-%2B-O(1)
from heapq import *
# time = O(log n) per addNum, O(1) per findMedian
# space = O(n)
class MedianFinder:

    def __init__(self):
        self.heaps = [], []

    def addNum(self, num):
        small, large = self.heaps
        heappush(small, -heappushpop(large, num))
        if len(large) < len(small):
            heappush(large, -heappop(small))

    def findMedian(self):
        small, large = self.heaps
        if len(large) > len(small):
            return float(large[0])
        return (large[0] - small[0]) / 2.0

# V1''
# https://leetcode.com/problems/find-median-from-data-stream/discuss/74047/JavaPython-two-heap-solution-O(log-n)-add-O(1)-find
from heapq import *
# time = O(log n) per addNum, O(1) per findMedian
# space = O(n)
class MedianFinder:
    def __init__(self):
        self.small = []  # the smaller half of the list, max heap (invert min-heap)
        self.large = []  # the larger half of the list, min heap

    def addNum(self, num):
        if len(self.small) == len(self.large):
            heappush(self.large, -heappushpop(self.small, -num))
        else:
            heappush(self.small, -heappushpop(self.large, num))

    def findMedian(self):
        if len(self.small) == len(self.large):
            return float(self.large[0] - self.small[0]) / 2.0
        else:
            return float(self.large[0])

# V1''''
# https://leetcode.com/problems/find-median-from-data-stream/discuss/74047/JavaPython-two-heap-solution-O(log-n)-add-O(1)-find
# JAVA
# private PriorityQueue<Integer> small = new PriorityQueue<>(Collections.reverseOrder());
# private PriorityQueue<Integer> large = new PriorityQueue<>();
# private boolean even = true;
#
# public double findMedian() {
#     if (even)
#         return (small.peek() + large.peek()) / 2.0;
#     else
#         return small.peek();
# }
#
# public void addNum(int num) {
#     if (even) {
#         large.offer(num);
#         small.offer(large.poll());
#     } else {
#         small.offer(num);
#         large.offer(small.poll());
#     }
#     even = !even;
# }

# V1'''
# https://leetcode.com/problems/find-median-from-data-stream/discuss/74044/Very-Short-O(log-n)-%2B-O(1)
# time = O(log n) per addNum, O(1) per findMedian
# space = O(n)
class MedianFinder:
    def __init__(s):
        h = [[], 1, -1, i := []]
        s.addNum = lambda n: heapq.heappush(h[-1], -heapq.heappushpop(h[0], n * h[1])) or h.reverse()
        s.findMedian = lambda: (h[0][0] * h[1] - i[0]) / 2

# V1''''
# https://leetcode.com/problems/find-median-from-data-stream/discuss/74044/Very-Short-O(log-n)-%2B-O(1)
from heapq import *
# time = O(log n) per addNum, O(1) per findMedian
# space = O(n)
class MedianFinder:

    def __init__(self):
        self.data = 1, [], []

    def addNum(self, num):
        sign, h1, h2 = self.data
        heappush(h2, -heappushpop(h1, num * sign))
        self.data = -sign, h2, h1

    def findMedian(self):
        sign, h1, h2 = d = self.data
        return (h1[0] * sign - d[-sign][0]) / 2.0

# V1'''''
# http://bookshadow.com/weblog/2015/10/19/leetcode-find-median-data-stream/
# IDEA : HEAP
from heapq import *
# time = O(log n) per addNum, O(1) per findMedian
# space = O(n)
class MedianFinder:
    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.minHeap = []
        self.maxHeap = []

    def addNum(self, num):
        """
        Adds a num into the data structure.
        :type num: int
        :rtype: void
        """
        heappush(self.maxHeap, -num)
        minTop = self.minHeap[0] if len(self.minHeap) else None
        maxTop = self.maxHeap[0] if len(self.maxHeap) else None
        if minTop < -maxTop or len(self.minHeap) + 1 < len(self.maxHeap):
            heappush(self.minHeap, -heappop(self.maxHeap))
        if len(self.maxHeap) < len(self.minHeap):
            heappush(self.maxHeap, -heappop(self.minHeap))

    def findMedian(self):
        """
        Returns the median of current data stream
        :rtype: float
        """
        if len(self.minHeap) < len(self.maxHeap):
            return -1.0 * self.maxHeap[0]
        else:
            return (self.minHeap[0] - self.maxHeap[0]) / 2.0

# V1''''''
# http://bookshadow.com/weblog/2015/10/19/leetcode-find-median-data-stream/
# IDEA : HEAP
# time = O(log n) per addNum, O(1) per findMedian
# space = O(n)
import heapq
class MedianFinder:
    def __init__(self):
        self.small = []  # the smaller half of the list, min-heap with invert values
        self.large = []  # the larger half of the list, min heap

    def addNum(self, num):
        if len(self.small) == len(self.large):
            heapq.heappush(self.large, -heapq.heappushpop(self.small, -num))
        else:
            heapq.heappush(self.small, -heapq.heappushpop(self.large, num))

    def findMedian(self):
        if len(self.small) == len(self.large):
            return float(self.large[0] - self.small[0]) / 2.0
        else:
            return float(self.large[0])

# V1''''''''
# http://bookshadow.com/weblog/2015/10/19/leetcode-find-median-data-stream/
# IDEA : HEAP
# time = O(log n) per addNum, O(1) per findMedian (custom binary heap)
# space = O(n)
class Heap:
    def __init__(self, cmp):
        self.cmp = cmp
        self.heap = [None]

    def __swap__(self, x, y, a):
        a[x], a[y] = a[y], a[x]

    def size(self):
        return len(self.heap) - 1

    def top(self):
        return self.heap[1] if self.size() else None

    def append(self, num):
        self.heap.append(num)
        self.siftUp(self.size())

    def pop(self):
        top, last = self.heap[1], self.heap.pop()
        if self.size():
            self.heap[1] = last
            self.siftDown(1)
        return top

    def siftUp(self, idx):
        while idx > 1 and self.cmp(idx, idx / 2, self.heap):
            self.__swap__(idx / 2, idx, self.heap)
            idx /= 2

    def siftDown(self, idx):
        while idx * 2 <= self.size():
            nidx = idx * 2
            if nidx + 1 <= self.size() and self.cmp(nidx + 1, nidx, self.heap):
                nidx += 1
            if self.cmp(nidx, idx, self.heap):
                self.__swap__(nidx, idx, self.heap)
                idx = nidx
            else:
                break

# time = O(log n) per addNum, O(1) per findMedian
# space = O(n)
class MedianFinder:
    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.minHeap = Heap(cmp = lambda x, y, a: a[x] < a[y])
        self.maxHeap = Heap(cmp = lambda x, y, a: a[x] > a[y])

    def addNum(self, num):
        """
        Adds a num into the data structure.
        :type num: int
        :rtype: void
        """
        self.maxHeap.append(num)
        if self.minHeap.top() < self.maxHeap.top() \
          or self.minHeap.size() + 1 < self.maxHeap.size():
            self.minHeap.append(self.maxHeap.pop())
        if self.maxHeap.size() < self.minHeap.size():
            self.maxHeap.append(self.minHeap.pop())

    def findMedian(self):
        """
        Returns the median of current data stream
        :rtype: float
        """
        if self.minHeap.size() < self.maxHeap.size():
            return self.maxHeap.top()
        else:
            return (self.minHeap.top() + self.maxHeap.top()) / 2.0

# V2
