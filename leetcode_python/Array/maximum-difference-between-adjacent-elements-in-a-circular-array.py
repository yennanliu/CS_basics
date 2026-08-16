"""

3423. Maximum Difference Between Adjacent Elements in a Circular Array
Easy

Given a circular array nums, find the maximum absolute difference between
adjacent elements.

Note: In a circular array, the first and last elements are adjacent.

Example 1:

Input: nums = [1,2,4]

Output: 3

Explanation:

Because nums is circular, nums[0] and nums[2] are adjacent. They have the
maximum absolute difference of |4 - 1| = 3.

Example 2:

Input: nums = [-5,-10,-5]

Output: 5

Explanation:

The adjacent elements nums[0] and nums[1] have the maximum absolute difference
of |-5 - (-10)| = 5.

Constraints:

2 <= nums.length <= 100
-100 <= nums[i] <= 100

"""

# V0
# IDEA : SCAN THE PAIRS, WRAPPING THE INDEX WITH MODULO
#
#   a circular array has exactly n adjacent pairs — the n - 1 ordinary ones plus
#   the (last, first) pair — so indexing with (i + 1) % n visits every pair once
#   and no pair twice.  no structure to exploit beyond that.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxAdjacentDistance(self, nums):
        n = len(nums)
        return max(abs(nums[i] - nums[(i + 1) % n]) for i in range(n))
