"""

1473. Paint House III
Hard

There is a row of m houses in a small city, each house must be painted with one of the n colors (labeled from 1 to n), some houses that have been painted last summer should not be painted again.

A neighborhood is a maximal group of continuous houses that are painted with the same color.

For example: houses = [1,2,2,3,3,2,1,1] contains 5 neighborhoods [{1}, {2,2}, {3,3}, {2}, {1,1}].

Given an array houses, an m x n matrix cost and an integer target where:

houses[i]: is the color of the house i, and 0 if the house is not painted yet.
cost[i][j]: is the cost of paint the house i with the color j + 1.

Return the minimum cost of painting all the remaining houses in such a way that there are exactly target neighborhoods. If it is not possible, return -1.


Example 1:

Input: houses = [0,0,0,0,0], cost = [[1,10],[10,1],[10,1],[1,10],[5,1]], m = 5, n = 2, target = 3
Output: 9
Explanation: Paint houses of this way [1,2,2,1,1]
This array contains target = 3 neighborhoods, [{1}, {2,2}, {1,1}].
Cost of paint all houses (1 + 1 + 1 + 1 + 5) = 9.

Example 2:

Input: houses = [0,2,1,2,0], cost = [[1,10],[10,1],[10,1],[1,10],[5,1]], m = 5, n = 2, target = 3
Output: 11
Explanation: Some houses are already painted, Paint the houses of this way [2,2,1,2,2]
This array contains target = 3 neighborhoods, [{2,2}, {1}, {2,2}].
Cost of paint the first and last house (10 + 1) = 11.

Example 3:

Input: houses = [3,1,2,3], cost = [[1,1,1],[1,1,1],[1,1,1],[1,1,1]], m = 4, n = 3, target = 3
Output: -1
Explanation: Houses are already painted with a total of 4 neighborhoods [{3},{1},{2},{3}] different of target = 3.


Constraints:

m == houses.length == cost.length
n == cost[i].length
1 <= m <= 100
1 <= n <= 20
1 <= target <= m
0 <= houses[i] <= n
1 <= cost[i][j] <= 10^4

"""

# V0
# IDEA : DP ON (house, colour, neighborhoods so far)
#
#   dp[j][k] = cheapest way to paint houses 0..i so that house i has colour
#   j and the row contains exactly k neighborhoods.
#   moving to house i+1 with colour j2:
#     k grows by 1 when j2 != j (a new neighborhood starts), stays otherwise.
#   NOTE : an already-painted house (houses[i] != 0) has its colour forced
#          and costs 0 - trying to repaint it is illegal, not just costly.
#   answer = min over colours of dp[j][target], or -1 if unreachable.
#
"""

DP def
    dp[i][j][k]: cheapest way to paint houses 0..i so that house i has

                 colour j and the row contains exactly k neighborhoods

DP eq

     moving to house i+1 with colour j2:

        dp[i+1][j2][k'] = min over j of dp[i][j][k] + paint(i+1, j2)

        where k' = k + 1 when j2 != j  (a NEW neighborhood starts)
                 = k     when j2 == j


    -> e.g. NOTE !!! an ALREADY-PAINTED house (houses[i] != 0) has its
              colour FORCED and costs 0 - trying to repaint it is illegal,
              not merely expensive

     init: house 0 -> dp[j][1] = cost[0][j-1], or 0 for the forced colour
     ans = min over colours of dp[m-1][j][target], or -1 if unreachable

"""
# time = O(m * n^2 * target), space = O(n * target)
class Solution(object):
    def minCost(self, houses, cost, m, n, target):
        INF = float('inf')

        # dp[color(1..n)][groups]
        dp = [[INF] * (target + 1) for _ in range(n + 1)]
        if houses[0] == 0:
            for j in range(1, n + 1):
                dp[j][1] = cost[0][j - 1]
        else:
            dp[houses[0]][1] = 0

        for i in range(1, m):
            nxt = [[INF] * (target + 1) for _ in range(n + 1)]
            for j in range(1, n + 1):
                # colour j is not allowed if this house is already painted otherwise
                if houses[i] != 0 and houses[i] != j:
                    continue
                paint = 0 if houses[i] != 0 else cost[i][j - 1]
                for k in range(1, target + 1):
                    best = INF
                    for j0 in range(1, n + 1):
                        prev_k = k if j0 == j else k - 1
                        if prev_k < 1:
                            continue
                        if dp[j0][prev_k] < best:
                            best = dp[j0][prev_k]
                    if best < INF:
                        nxt[j][k] = best + paint
            dp = nxt

        res = min(dp[j][target] for j in range(1, n + 1))
        return -1 if res == INF else res
