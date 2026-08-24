"""

1692. Count Ways to Distribute Candies
Hard

There are n unique candies (labeled 1 through n) and k bags. You are asked to distribute all the
candies into the bags such that every bag has at least one candy.

There can be multiple ways to distribute the candies. Two ways are considered different if the
candies in one bag in the first way are not all in the same bag in the second way. The order of the
bags and the order of the candies within each bag do not matter.

For example, (1), (2,3) and (2), (1,3) are considered different because candies 2 and 3 in the bag
(2,3) in the first way are not in the same bag in the second way (they are split between the bags
(2) and (1,3)). However, (1), (2,3) and (3,2), (1) are considered the same because the candies in
each bag are all in the same bags in both ways.

Given two integers, n and k, return the number of different ways to distribute the candies. As the
answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: n = 3, k = 2
Output: 3
Explanation: You can distribute 3 candies into 2 bags in 3 ways:
(1), (2,3)
(1,2), (3)
(1,3), (2)

Example 2:

Input: n = 4, k = 2
Output: 7
Explanation: You can distribute 4 candies into 2 bags in 7 ways:
(1), (2,3,4)
(1,2), (3,4)
(1,3), (2,4)
(1,4), (2,3)
(1,2,3), (4)
(1,2,4), (3)
(1,3,4), (2)

Example 3:

Input: n = 20, k = 5
Output: 206085257
Explanation: You can distribute 20 candies into 5 bags in 1881780996 ways.
1881780996 modulo 10^9 + 7 = 206085257.


Constraints:

1 <= k <= n <= 1000

"""

# V0
# IDEA : STIRLING NUMBERS OF THE SECOND KIND (bags are indistinguishable)
#
#   "order of the bags does not matter" + "every bag non-empty" is exactly
#   S(n, k), the number of ways to partition an n-set into k non-empty blocks.
#
#   recurrence, by where candy n goes:
#     - into one of the j already-formed bags  -> S(n-1, j) * j
#     - alone, opening a brand new bag         -> S(n-1, j-1)
#     S(n, j) = j * S(n-1, j) + S(n-1, j-1),  S(0, 0) = 1
#
#   NOTE : one row is enough if j is swept DOWNWARDS, so that dp[j-1] still
#          holds the previous row's value when dp[j] is written.
#
"""

DP def
    "bag order does not matter" + "no empty bag" is exactly the STIRLING
    NUMBER OF THE SECOND KIND S(n, k) - partitions of an n-set into k
    non-empty blocks

    dp[i][j] = S(i, j): ways to put the first i candies into j non-empty bags

DP eq

     dp[i][j] = j * dp[i-1][j]     # candy i joins one of the j existing bags

              + dp[i-1][j-1]       # candy i opens a BRAND NEW bag


    -> e.g. one row suffices if j is swept DOWNWARD, so dp[j-1] still holds
              the previous row's value when dp[j] is overwritten

     init: dp[0][0] = 1, and dp[i][0] = 0 for i >= 1
     ans = dp[n][k] % (10^9 + 7)

"""
# time = O(n * k), space = O(k)
class Solution(object):
    def waysToDistribute(self, n, k):
        MOD = 10 ** 9 + 7
        dp = [0] * (k + 1)
        dp[0] = 1                       # S(0, 0) = 1
        for i in range(1, n + 1):
            for j in range(min(i, k), 0, -1):
                dp[j] = (dp[j] * j + dp[j - 1]) % MOD
            dp[0] = 0                   # S(i, 0) = 0 for i >= 1
        return dp[k]
