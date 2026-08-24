"""

3250. Find the Count of Monotonic Pairs I
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
1 <= nums[i] <= 50

"""

# V0
# IDEA : arr2 IS DETERMINED BY arr1 — SO COUNT THE VALID arr1 SEQUENCES
#
#   arr2[i] = nums[i] - arr1[i], so the pair is decided entirely by arr1, and
#   the two monotonicity rules turn into constraints on consecutive arr1
#   values :
#
#       arr1[i] >= arr1[i-1]                        (arr1 non-decreasing)
#       nums[i] - arr1[i] <= nums[i-1] - arr1[i-1]  (arr2 non-increasing)
#         <=>  arr1[i] >= arr1[i-1] + nums[i] - nums[i-1]
#
#   so with dp[i][v] = ways where arr1[i] = v,
#       dp[i][v] = sum of dp[i-1][p] over p <= min(v, v - (nums[i]-nums[i-1]))
#   which a prefix sum over the previous row answers in O(1) per v.
#
#   arr1[i] also has to stay inside [0, nums[i]] so that arr2 stays
#   non-negative.
#
"""

DP def
    arr2[i] = nums[i] - arr1[i], so the PAIR is fully decided by arr1, and
    the two monotonicity rules become constraints on consecutive arr1 values:

        arr1[i] >= arr1[i-1]                          (arr1 non-decreasing)
        arr1[i] >= arr1[i-1] + nums[i] - nums[i-1]    (arr2 non-increasing)

    dp[i][v]: number of valid ways with arr1[i] == v

              (v must stay in [0, nums[i]] so arr2 stays non-negative)

DP eq

     dp[i][v] = sum of dp[i-1][p]  for p <= min( v, v - (nums[i] - nums[i-1]) )


    -> e.g. that sum is a PREFIX SUM over the previous row, so each v is
              answered in O(1)

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
            for v in range(top + 1):
                pre[v + 1] = (pre[v] + dp[v]) % MOD
            delta = nums[i] - nums[i - 1]
            nxt = [0] * (top + 1)
            for v in range(nums[i] + 1):
                bound = min(v, v - delta)          # largest allowed arr1[i-1]
                if bound < 0:
                    continue
                bound = min(bound, top)
                nxt[v] = pre[bound + 1]
            dp = nxt

        return sum(dp) % MOD
