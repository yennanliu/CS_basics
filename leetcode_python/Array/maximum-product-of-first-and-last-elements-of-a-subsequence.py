"""

3584. Maximum Product of First and Last Elements of a Subsequence
Medium

You are given an integer array nums and an integer m.

Return the maximum product of the first and last elements of any subsequence of nums of size m.


Example 1:

Input: nums = [-1,-9,2,3,-2,-3,1], m = 1
Output: 81
Explanation:
The subsequence [-9] has the largest product of the first and last elements: -9 * -9 = 81. Therefore, the answer is 81.

Example 2:

Input: nums = [1,3,-5,5,6,-4], m = 3
Output: 20
Explanation:
The subsequence [-5, 6, -4] has the largest product of the first and last elements.

Example 3:

Input: nums = [2,-1,2,-6,5,2,-5,7], m = 2
Output: 35
Explanation:
The subsequence [5, 7] has the largest product of the first and last elements.


Constraints:

1 <= nums.length <= 10^5
-10^5 <= nums[i] <= 10^5
1 <= m <= nums.length

"""

# V0
# IDEA : ONLY THE TWO ENDPOINTS MATTER, SO SLIDE A GAP OF m-1
#
#   the middle m-2 picks are irrelevant to the score; a subsequence of size
#   m exists with endpoints i and j exactly when j - i >= m - 1. so the task
#   collapses to "maximise nums[i] * nums[j] over all pairs at least m-1
#   apart".
#
#   because of negative values the best partner for nums[j] is either the
#   running maximum or the running minimum of the allowed prefix, and both
#   are maintained in O(1) as j advances.
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumProduct(self, nums, m):
        n = len(nums)
        hi = float('-inf')
        lo = float('inf')
        ans = float('-inf')
        for j in range(m - 1, n):
            i = j - m + 1
            v = nums[i]
            if v > hi:
                hi = v
            if v < lo:
                lo = v
            b = nums[j]
            c1 = hi * b
            c2 = lo * b
            if c1 > ans:
                ans = c1
            if c2 > ans:
                ans = c2
        return ans
