"""

1746. Maximum Subarray Sum After One Operation
Medium

You are given an integer array nums. You must perform exactly one operation where you can replace one element nums[i] with nums[i] * nums[i].

Return the maximum possible subarray sum after exactly one operation. The subarray must be non-empty.


Example 1:

Input: nums = [2,-1,-4,-3]
Output: 17
Explanation: You can perform the operation on index 2 (0-indexed) to make nums = [2,-1,16,-3]. Now, the maximum subarray sum is 2 + -1 + 16 = 17.

Example 2:

Input: nums = [1,-1,1,1,-1,-1,1]
Output: 4
Explanation: You can perform the operation on index 1 (0-indexed) to make nums = [1,1,1,1,-1,-1,1], Now, the maximum subarray sum is 1 + 1 + 1 + 1 = 4.


Constraints:

1 <= nums.length <= 10^5
-10^4 <= nums[i] <= 10^4

"""

# V0
# IDEA : KADANE WITH A STATE FLAG (0 = not squared yet, 1 = already squared)
#
#   plain Kadane cannot express "exactly one element was squared", so carry
#   TWO running values for subarrays ending at the current index:
#
#     f = best sum, NO square used yet
#     g = best sum, the square ALREADY used somewhere inside
#
#   transitions on element x:
#     f' = max(f, 0) + x                       (extend, or restart here)
#     g' = max( max(f, 0) + x*x ,  g + x )
#            ^ square x now, after a clean prefix
#                                  ^ x joins a run that already spent it
#
#   the answer must USE the operation, so track the max of g only.
#
#   NOTE : `max(f, 0)` is the restart rule - a negative prefix is dropped.
#   NOTE : g is never seeded from 0 alone; every g value passes through a
#          squared term, so "exactly one" is enforced automatically.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxSumAfterOperation(self, nums):
        f = g = 0
        res = float('-inf')

        for x in nums:
            nf = max(f, 0) + x
            ng = max(max(f, 0) + x * x, g + x)
            f, g = nf, ng
            res = max(res, g)

        return res
