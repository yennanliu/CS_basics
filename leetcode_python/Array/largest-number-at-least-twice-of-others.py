# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79188909
# IDEA : FIND THE 1ST, 2ND MAX
class Solution(object):
    def dominantIndex(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums) == 1:
            return 0
        largest = max(nums)
        ind = nums.index(largest)
        nums.pop(ind)
        if largest >= 2 * max(nums):
            return ind
        else:
            return -1

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79188909
# IDEA : SORT 
class Solution(object):
    def dominantIndex(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums) == 1:
            return 0
        largest = max(nums)
        ind = nums.index(largest)
        nums.sort()
        if largest >= 2 * nums[-2]:
            return ind
        else:
            return -1

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79188909
# IDEA : HEAP
import heapq
class Solution(object):
    def dominantIndex(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums) == 1:
            return 0
        heap = [(-num, i) for i, num in enumerate(nums)]
        heapq.heapify(heap)
        largest, ind = heapq.heappop(heap)
        if largest <= 2 * heapq.heappop(heap)[0]:
            return ind
        return -1

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def dominantIndex(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        m = max(nums)
        if all(m >= 2*x for x in nums if x != m):
            return nums.index(m)
        return -1