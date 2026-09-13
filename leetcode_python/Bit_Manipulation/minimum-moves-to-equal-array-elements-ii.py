"""

462. Minimum Moves to Equal Array Elements II
Medium

Given an integer array nums of size n, return the minimum number of moves required to make all array elements equal.

In one move, you can increment or decrement an element of the array by 1.

Test cases are designed so that the answer will fit in a 32-bit integer.

Example 1:

Input: nums = [1,2,3]
Output: 2
Explanation:
Only two moves are needed (remember each move increments or decrements one element):
[1,2,3]  =>  [2,2,3]  =>  [2,2,2]

Example 2:

Input: nums = [1,10,2,9]
Output: 16

Constraints:

n == nums.length
1 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0 

# V1
# http://bookshadow.com/weblog/2016/11/20/leetcode-minimum-moves-to-equal-array-elements-ii/
# IDEA : GET THE SUM OF ABSOULT DIFFERENCE BETWEEN ELMENTS AND THEIR MEDIAN
# time = O(n log n)  # sort
# space = O(1)  # excluding sort space
class Solution(object):
    def minMoves2(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        nums.sort()
        median = nums[len(nums) / 2]
        return sum(abs(num - median) for num in nums)

# V2
# time = O(n) on average  # quickselect
# space = O(1)
from random import randint
# Quick select solution.
class Solution(object):
    def minMoves2(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        def kthElement(nums, k):
            def PartitionAroundPivot(left, right, pivot_idx, nums):
                pivot_value = nums[pivot_idx]
                new_pivot_idx = left
                nums[pivot_idx], nums[right] = nums[right], nums[pivot_idx]
                for i in xrange(left, right):
                    if nums[i] > pivot_value:
                        nums[i], nums[new_pivot_idx] = nums[new_pivot_idx], nums[i]
                        new_pivot_idx += 1

                nums[right], nums[new_pivot_idx] = nums[new_pivot_idx], nums[right]
                return new_pivot_idx

            left, right = 0, len(nums) - 1
            while left <= right:
                pivot_idx = randint(left, right)
                new_pivot_idx = PartitionAroundPivot(left, right, pivot_idx, nums)
                if new_pivot_idx == k - 1:
                    return nums[new_pivot_idx]
                elif new_pivot_idx > k - 1:
                    right = new_pivot_idx - 1
                else:  # new_pivot_idx < k - 1.
                    left = new_pivot_idx + 1

        median = kthElement(nums, len(nums)/2 + 1)
        return sum(abs(num - median) for num in nums)

    # time = O(n log n)  # sorted()
    # space = O(n)
    def minMoves22(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        median = sorted(nums)[len(nums) / 2]
        return sum(abs(num - median) for num in nums)