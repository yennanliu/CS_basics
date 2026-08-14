"""

1827. Minimum Operations to Make the Array Increasing
Easy

You are given an integer array nums (0-indexed). In one operation, you can choose an element of the array and increment it by 1.

For example, if nums = [1,2,3], you can choose to increment nums[1] to make nums = [1,3,3].

Return the minimum number of operations needed to make nums strictly increasing.

An array nums is strictly increasing if nums[i] < nums[i+1] for all 0 <= i < nums.length - 1. An array of length 1 is trivially strictly increasing.


Example 1:

Input: nums = [1,1,1]
Output: 3
Explanation: You can do the following operations:
1) Increment nums[2], so nums becomes [1,1,2].
2) Increment nums[1], so nums becomes [1,2,2].
3) Increment nums[2], so nums becomes [1,2,3].

Example 2:

Input: nums = [1,5,2,4,1]
Output: 14

Example 3:

Input: nums = [8]
Output: 0


Constraints:

1 <= nums.length <= 5000
1 <= nums[i] <= 10^4

"""

# V0
# IDEA : GREEDY LEFT -> RIGHT (keep every element as small as legally possible)
#
#   we can only INCREASE values, so the first element is never touched and each
#   later element should end up at exactly max(nums[i], prev + 1) -- anything
#   higher only makes the following elements more expensive, anything lower is
#   illegal.
#
#   cost at i = max(0, prev + 1 - nums[i]).
#
#   NOTE : `prev` must be the value AFTER the fix, not the original nums[i-1].
#
# time = O(n), space = O(1)
class Solution(object):
    def minOperations(self, nums):
        res = 0
        prev = 0
        for v in nums:
            need = prev + 1
            if v < need:
                res += need - v
                prev = need
            else:
                prev = v
        return res
