"""

879. Profitable Schemes
Hard

There is a group of n members, and a list of various crimes they could commit.
The ith crime generates a profit[i] and requires group[i] members to participate in it.
If a member participates in one crime, that member can't participate in another crime.

Let's call a profitable scheme any subset of these crimes that generates at least
minProfit profit, and the total number of members participating in that subset of
crimes is at most n.

Return the number of schemes that can be chosen.
Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: n = 5, minProfit = 3, group = [2,2], profit = [2,3]
Output: 2
Explanation: To make a profit of at least 3, the group could either commit crimes 0 and 1,
or just crime 1.
In total, there are 2 schemes.

Example 2:

Input: n = 10, minProfit = 5, group = [2,3,5], profit = [6,7,8]
Output: 7
Explanation: To make a profit of at least 5, the group could commit any crimes,
as long as they commit one.
There are 7 possible schemes: (0), (1), (2), (0,1), (0,2), (1,2), and (0,1,2).


Constraints:

1 <= n <= 100
0 <= minProfit <= 100
1 <= group.length <= 100
1 <= group[i] <= 100
profit.length == group.length
0 <= profit[i] <= 100

"""

# V0
# IDEA : 2D 0/1 KNAPSACK
"""
 DP def:
    - dp[j][k] = number of subsets of the crimes seen so far that use
                 AT MOST j members and reach profit >= k
                 (profit is capped at minProfit, since anything beyond
                  minProfit behaves exactly the same)

 Init:
    - dp[j][0] = 1 for every j  (the empty subset already has profit >= 0)

 DP eq (for crime with g members, p profit):
    - dp[j][k] += dp[j - g][max(0, k - p)]
      looping j downwards, so each crime is used at most once (0/1 knapsack)
"""
# time = O(m * n * minProfit), m = len(group)
# space = O(n * minProfit)
class Solution(object):
    def profitableSchemes(self, n, minProfit, group, profit):
        MOD = 10 ** 9 + 7

        dp = [[0] * (minProfit + 1) for _ in range(n + 1)]
        # the empty scheme already satisfies "profit >= 0" for any member budget
        for j in range(n + 1):
            dp[j][0] = 1

        for g, p in zip(group, profit):
            # NOTE !!! loop members downwards -> 0/1 knapsack (each crime used once)
            for j in range(n, g - 1, -1):
                for k in range(minProfit, -1, -1):
                    # max(0, k - p) caps the profit at minProfit
                    dp[j][k] = (dp[j][k] + dp[j - g][max(0, k - p)]) % MOD

        return dp[n][minProfit]


# V1
# IDEA : TOP DOWN DP (memoization)
#
#   dfs(i, j, k) = number of schemes when we are at crime i,
#                  j members already used, current (capped) profit k
#
# time = O(m * n * minProfit)
# space = O(m * n * minProfit)
class Solution(object):
    def profitableSchemes(self, n, minProfit, group, profit):
        MOD = 10 ** 9 + 7
        m = len(group)
        memo = {}

        def dfs(i, j, k):
            if i == m:
                return 1 if k == minProfit else 0
            key = (i, j, k)
            if key in memo:
                return memo[key]
            # skip crime i
            res = dfs(i + 1, j, k)
            # take crime i (only if we still have enough members)
            if j + group[i] <= n:
                res += dfs(i + 1, j + group[i], min(k + profit[i], minProfit))
            res %= MOD
            memo[key] = res
            return res

        return dfs(0, 0, 0)
