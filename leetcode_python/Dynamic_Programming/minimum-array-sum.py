"""

3366. Minimum Array Sum
Medium

You are given an integer array nums and three integers k, op1, and op2.

You can perform the following operations on nums:

Operation 1: Choose an index i and divide nums[i] by 2, rounding up to the nearest whole number. You can perform this operation at most op1 times, and not more than once per index.
Operation 2: Choose an index i and subtract k from nums[i], but only if nums[i] is greater than or equal to k. You can perform this operation at most op2 times, and not more than once per index.

Note: Both operations can be applied to the same index, but at most once each.

Return the minimum possible sum of all elements in nums after performing any number of operations.


Example 1:

Input: nums = [2,8,3,19,3], k = 3, op1 = 1, op2 = 1
Output: 23
Explanation:
Apply Operation 2 to nums[1] = 8, making nums[1] = 5.
Apply Operation 1 to nums[3] = 19, making nums[3] = 10.
The resulting array becomes [2, 5, 3, 10, 3], which has the minimum possible sum of 23 after applying the operations.

Example 2:

Input: nums = [2,4,3], k = 3, op1 = 2, op2 = 1
Output: 3
Explanation:
Apply Operation 1 to nums[0] = 2, making nums[0] = 1.
Apply Operation 1 to nums[1] = 4, making nums[1] = 2.
Apply Operation 2 to nums[2] = 3, making nums[2] = 0.
The resulting array becomes [1, 2, 0], which has the minimum possible sum of 3 after applying the operations.


Constraints:

1 <= nums.length <= 100
0 <= nums[i] <= 10^5
0 <= k <= 10^5
0 <= op1, op2 <= nums.length

"""

# V0
# IDEA : DP OVER (INDEX, OPERATION-1 BUDGET LEFT, OPERATION-2 BUDGET LEFT)
#
#   the operations compete for two shared budgets, so a per-element greedy
#   fails — spending a halving here may be worth more elsewhere. but each
#   index has only four possible fates :
#
#       leave it, halve it, subtract k, or both (in either order)
#
#   and the ORDER matters for the "both" case : halving first then
#   subtracting can differ from subtracting first then halving, so both are
#   tried when they are legal.
#
#   with n, op1, op2 all at most 100 the table is a million states of O(1)
#   work each.
#
"""

DP def
    the two operations compete for SHARED budgets, so a per-element greedy
    fails - spending a halving here may be worth more elsewhere

    dp[a][b]: MIN sum of the elements processed so far, given a op1's

              and b op2's still left

DP eq

     for each value v (half = ceil(v / 2)):

        dp_new[a][b]         = min(..., dp[a][b] + v)          # untouched

        dp_new[a-1][b]       = min(..., dp[a][b] + half)       # halve only

        dp_new[a][b-1]       = min(..., dp[a][b] + v - k)      # subtract only
                                                               (if v >= k)

        dp_new[a-1][b-1]     = min(..., dp[a][b] + both orders)


    -> e.g. NOTE !!! for the "BOTH" case the ORDER matters - halve-then-
              subtract can differ from subtract-then-halve, so both are tried
              when legal

     n, op1, op2 <= 100 -> a million states of O(1) work each

     init: dp[op1][op2] = 0
     ans = min over a, b of dp[a][b]

"""
# time = O(n * op1 * op2), space = O(op1 * op2)
class Solution(object):
    def minArraySum(self, nums, k, op1, op2):
        INF = float('inf')
        # dp[a][b] = min sum so far having a op1's and b op2's left
        dp = [[INF] * (op2 + 1) for _ in range(op1 + 1)]
        dp[op1][op2] = 0

        for v in nums:
            nxt = [[INF] * (op2 + 1) for _ in range(op1 + 1)]
            half = (v + 1) // 2
            for a in range(op1 + 1):
                for b in range(op2 + 1):
                    cur = dp[a][b]
                    if cur == INF:
                        continue

                    def relax(na, nb, add):
                        if cur + add < nxt[na][nb]:
                            nxt[na][nb] = cur + add

                    relax(a, b, v)                              # untouched
                    if a:
                        relax(a - 1, b, half)                   # halve only
                    if b and v >= k:
                        relax(a, b - 1, v - k)                  # subtract only
                    if a and b:
                        if half >= k:                           # halve, then subtract
                            relax(a - 1, b - 1, half - k)
                        if v >= k:                              # subtract, then halve
                            relax(a - 1, b - 1, (v - k + 1) // 2)
            dp = nxt

        return min(min(row) for row in dp)
