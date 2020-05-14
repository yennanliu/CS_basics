# 703. Kth Largest Element in a Stream
# Easy
#
# Design a class to find the kth largest element in a stream. Note that it is the kth largest element in the sorted order, not the kth distinct element.
#
# Your KthLargest class will have a constructor which accepts an integer k and an integer array nums, which contains initial elements from the stream. For each call to the method KthLargest.add, return the element representing the kth largest element in the stream.
#
# Example:
#
# int k = 3;
# int[] arr = [4,5,8,2];
# KthLargest kthLargest = new KthLargest(3, arr);
# kthLargest.add(3);   // returns 4
# kthLargest.add(5);   // returns 5
# kthLargest.add(10);  // returns 5
# kthLargest.add(9);   // returns 8
# kthLargest.add(4);   // returns 8
# Note:
# You may assume that nums' length ≥ k-1 and k ≥ 1.

# V0
class KthLargest(object):

    def __init__(self, k, nums):
        """
        :type k: int
        :type nums: List[int]
        """
        self.pool = nums
        self.size = len(self.pool)
        self.k = k
        heapq.heapify(self.pool)
        while self.size > k:
        	# heappop : get minimum element out from heap and return it 
            heapq.heappop(self.pool)
            self.size -= 1

    def add(self, val):
        """
        :type val: int
        :rtype: int
        """
        if self.size < self.k:
        	# heappush : put item input heap, and make heap unchanged
            heapq.heappush(self.pool, val)
            self.size += 1
        elif val > self.pool[0]:
        	# get minimum value from heap and return it, and put new item into heap
            heapq.heapreplace(self.pool, val)
        return self.pool[0]

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/81027116
# IDEA : HEAP
# DEMO : HEAP OP
# https://docs.python.org/zh-tw/3/library/heapq.html
# q = []
# heappush(q, item)
# heappop(q) # IF ONLY WANT TO CHECK THE MINIMUM (BUT NOT POP IT) -> q[0]
# heappushpop(q, item) # op : push -> pop 
# heapify(q) # Transform list x into a heap, in-place, in linear time.
# heapreplace(q, item) # op : pop -> push 
# nlargest(n, q) # op : return TOP N Max number as list 
class KthLargest(object):

    def __init__(self, k, nums):
        """
        :type k: int
        :type nums: List[int]
        """
        self.pool = nums
        self.size = len(self.pool)
        self.k = k
        heapq.heapify(self.pool)
        while self.size > k:
        	# heappop : get minimum element out from heap and return it 
            heapq.heappop(self.pool)
            self.size -= 1

    def add(self, val):
        """
        :type val: int
        :rtype: int
        """
        if self.size < self.k:
        	# heappush : put item input heap, and make heap unchanged
            heapq.heappush(self.pool, val)
            self.size += 1
        elif val > self.pool[0]:
        	# get minimum value from heap and return it, and put new item into heap
            heapq.heapreplace(self.pool, val)
        return self.pool[0]

### Test case
k = 3
nums= [4,5,8,2]
kthlargest=KthLargest(k, nums)
assert kthlargest.add(3) == 4 
assert kthlargest.add(5) == 5 
assert kthlargest.add(10) == 5 
assert kthlargest.add(9) == 8 
assert kthlargest.add(4) == 8 

# V1'
# https://www.jianshu.com/p/1ee69ca082b7
import heapq
class KthLargest:

    def __init__(self, k, nums):
        """
        :type k: int
        :type nums: List[int]
        """
        self.k = k
        heapq.heapify(nums)
        self.heap = nums
        while len(self.heap) > k:
            heapq.heappop(self.heap)

    def add(self, val):
        """
        :type val: int
        :rtype: int
        """
        if len(self.heap) < self.k:
            heapq.heappush(self.heap, val)
        else:
            heapq.heappushpop(self.heap, val)
            
        return self.heap[0]

# V2
