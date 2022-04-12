"""

703. Kth Largest Element in a Stream
Easy

Design a class to find the kth largest element in a stream. Note that it is the kth largest element in the sorted order, not the kth distinct element.

Implement KthLargest class:

KthLargest(int k, int[] nums) Initializes the object with the integer k and the stream of integers nums.
int add(int val) Appends the integer val to the stream and returns the element representing the kth largest element in the stream.
 

Example 1:

Input
["KthLargest", "add", "add", "add", "add", "add"]
[[3, [4, 5, 8, 2]], [3], [5], [10], [9], [4]]
Output
[null, 4, 5, 5, 8, 8]

Explanation
KthLargest kthLargest = new KthLargest(3, [4, 5, 8, 2]);
kthLargest.add(3);   // return 4
kthLargest.add(5);   // return 5
kthLargest.add(10);  // return 5
kthLargest.add(9);   // return 8
kthLargest.add(4);   // return 8
 

Constraints:

1 <= k <= 104
0 <= nums.length <= 104
-104 <= nums[i] <= 104
-104 <= val <= 104
At most 104 calls will be made to add.
It is guaranteed that there will be at least k elements in the array when you search for the kth element.

"""

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
# IDEA : HEAP
import heapq
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