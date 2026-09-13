"""

747. Largest Number At Least Twice of Others
Easy

You are given an integer array nums where the largest integer is unique.

Determine whether the largest element in the array is at least twice as much as every other number in the array. If it is, return the index of the largest element, or return -1 otherwise.

Example 1:

Input: nums = [3,6,1,0]
Output: 1
Explanation: 6 is the largest integer.
For every other number in the array x, 6 is at least twice as big as x.
The index of value 6 is 1, so we return 1.

Example 2:

Input: nums = [1,2,3,4]
Output: -1
Explanation: 4 is less than twice the value of 3, so we return -1.

Constraints:

2 <= nums.length <= 50
0 <= nums[i] <= 100
The largest element in nums is unique.

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79188909
# IDEA : FIND THE 1ST, 2ND MAX
# time = O(n)
# space = O(1)
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
# time = O(n log n)
# space = O(1)
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
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(1)
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