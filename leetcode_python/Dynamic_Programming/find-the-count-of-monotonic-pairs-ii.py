"""

3251. Find the Count of Monotonic Pairs II
Hard

You are given an array of positive integers nums of length n.

We call a pair of non-negative integer arrays (arr1, arr2) monotonic if:

The lengths of both arrays are n.
arr1 is monotonically non-decreasing, in other words, arr1[0] <= arr1[1] <= ... <= arr1[n - 1].
arr2 is monotonically non-increasing, in other words, arr2[0] >= arr2[1] >= ... >= arr2[n - 1].
arr1[i] + arr2[i] == nums[i] for all indices 0 <= i <= n - 1.

Return the count of monotonic pairs.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [2,3,2]
Output: 4
Explanation:
The good pairs are:
([0, 1, 1], [2, 2, 1])
([0, 1, 2], [2, 2, 0])
([0, 2, 2], [2, 1, 0])
([1, 2, 2], [1, 1, 0])

Example 2:

Input: nums = [5,5,5,5]
Output: 126


Constraints:

1 <= n == nums.length <= 2000
1 <= nums[i] <= 1000

"""

# V0
# IDEA : SAME PREFIX-SUM DP AS LC 3250 — IT WAS ALREADY O(n * max)
#
#   arr2 is pinned by arr1 (arr2[i] = nums[i] - arr1[i]), and the two
#   monotonicity rules combine into a single lower bound on each step :
#
#       arr1[i] >= arr1[i-1] + max(0, nums[i] - nums[i-1])
#
#   so dp[i][v] sums dp[i-1][p] over p <= min(v, v - (nums[i] - nums[i-1])),
#   which a running prefix sum answers in O(1).
#
#   this sequel only lifts the value ceiling from 50 to 1000, so the table
#   grows to 2000 * 1001 = 2 million cells — still linear in the state count.
#
"""

DP def
    arr2 is PINNED by arr1 (arr2[i] = nums[i] - arr1[i]), and the two
    monotonicity rules collapse into one lower bound per step:

        arr1[i] >= arr1[i-1] + max(0, nums[i] - nums[i-1])

    dp[i][v]: number of valid ways with arr1[i] == v

              (v in [0, nums[i]] so arr2 stays non-negative)

DP eq

     dp[i][v] = sum of dp[i-1][p]  for p <= min( v, v - (nums[i] - nums[i-1]) )

              = pre[bound + 1]      # running prefix sum of the previous row


    -> e.g. the prefix sum is what keeps each v at O(1), so the whole
              table is O(n * max value)

     init: dp[0][v] = 1 for v in [0, nums[0]]
     ans = sum(dp[n-1]) % (10^9 + 7)

"""
# time = O(n * max value), space = O(max value)
class Solution(object):
    def countOfPairs(self, nums):
        MOD = 10 ** 9 + 7
        n = len(nums)
        top = max(nums)

        dp = [0] * (top + 1)
        for v in range(nums[0] + 1):
            dp[v] = 1

        for i in range(1, n):
            pre = [0] * (top + 2)
            run = 0
            for v in range(top + 1):
                run = (run + dp[v]) % MOD
                pre[v + 1] = run
            delta = nums[i] - nums[i - 1]
            nxt = [0] * (top + 1)
            for v in range(nums[i] + 1):
                bound = v - delta if delta > 0 else v
                if bound < 0:
                    continue
                if bound > top:
                    bound = top
                nxt[v] = pre[bound + 1]
            dp = nxt

        return sum(dp) % MOD
