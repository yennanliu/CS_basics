"""

1749. Maximum Absolute Sum of Any Subarray
Medium

You are given an integer array nums. The absolute sum of a subarray [numsl, numsl+1, ..., numsr-1, numsr] is abs(numsl + numsl+1 + ... + numsr-1 + numsr).

Return the maximum absolute sum of any (possibly empty) subarray of nums.

Note that abs(x) is defined as follows:

If x is a negative integer, then abs(x) = -x.
If x is a non-negative integer, then abs(x) = x.


Example 1:

Input: nums = [1,-3,2,3,-4]
Output: 5
Explanation: The subarray [2,3] has absolute sum = abs(2+3) = abs(5) = 5.

Example 2:

Input: nums = [2,-5,1,-4,3,-2]
Output: 8
Explanation: The subarray [-5,1,-4] has absolute sum = abs(-5+1-4) = abs(-8) = 8.


Constraints:

1 <= nums.length <= 10^5
-10^4 <= nums[i] <= 10^4

"""

# V0
# IDEA : KADANE TWICE (max subarray sum AND min subarray sum)
#
#   |sum| is maximised either by the most POSITIVE subarray or by the most
#   NEGATIVE one - so run Kadane in both directions at once:
#
#     f = max sum of a subarray ending here = max(f, 0) + x
#     g = min sum of a subarray ending here = min(g, 0) + x
#
#   answer = max over all positions of max(f, -g).
#
#   NOTE : the empty subarray is allowed and sums to 0, so seeding the
#          answer at 0 is correct - it also means we never return negative.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxAbsoluteSum(self, nums):
        f = g = 0
        res = 0

        for x in nums:
            f = max(f, 0) + x
            g = min(g, 0) + x
            res = max(res, f, -g)

        return res
