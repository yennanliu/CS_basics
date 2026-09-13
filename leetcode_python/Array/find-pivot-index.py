"""

724. Find Pivot Index
Easy

Given an array of integers nums, calculate the pivot index of this array.

The pivot index is the index where the sum of all the numbers strictly to the left of the index is equal to the sum of all the numbers strictly to the index's right.

If the index is on the left edge of the array, then the left sum is 0 because there are no elements to the left. This also applies to the right edge of the array.

Return the leftmost pivot index. If no such index exists, return -1.

Example 1:

Input: nums = [1,7,3,6,5,6]
Output: 3
Explanation:
The pivot index is 3.
Left sum = nums[0] + nums[1] + nums[2] = 1 + 7 + 3 = 11
Right sum = nums[4] + nums[5] = 5 + 6 = 11

Example 2:

Input: nums = [1,2,3]
Output: -1
Explanation:
There is no index that satisfies the conditions in the problem statement.

Example 3:

Input: nums = [2,1,-1]
Output: 0
Explanation:
The pivot index is 0.
Left sum = 0 (no elements to the left of index 0)
Right sum = nums[1] + nums[2] = 1 + -1 = 0

Constraints:

1 <= nums.length <= 10^4
-1000 <= nums[i] <= 1000

Note: This question is the same as 1991: https://leetcode.com/problems/find-the-middle-index-in-array/

"""

# V0 

# V1
# http://bookshadow.com/weblog/2017/11/13/leetcode-find-pivot-index/
# time = O(n)
# space = O(1)
class Solution(object):
    def pivotIndex(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        sums = sum(nums)
        total = 0
        for x, n in enumerate(nums):
            if sums - n == 2 * total: 
            	return x
            total += n
        return -1

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79248308
class Solution(object):
    # time = O(n)
    # space = O(1)
    def pivotIndex(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums) == 0:
            return -1
        left = 0
        right = sum(nums)
        for i in range(len(nums)):
            if i != 0:
                left += nums[i - 1]
            right -= nums[i]
            if left == right:
                return i
        return -1

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79248308
class Solution(object):
    # time = O(n)
    # space = O(n)
    def pivotIndex(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums: return -1
        N = len(nums)
        sums = [0] * (N + 1)
        for i in range(N):
            sums[i + 1] = sums[i] + nums[i]
        for i in range(N):
            if sums[i] == sums[-1] - sums[i + 1]:
                return i
        return -1

# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def pivotIndex(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        total = sum(nums)
        left_sum = 0
        for i, num in enumerate(nums):
            if left_sum == (total-left_sum-num):
                return i
            left_sum += num
        return -1