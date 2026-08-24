"""

2247. Maximum Cost of Trip With K Highways
Hard
(premium / locked problem)

A series of highways connect n cities numbered from 0 to n - 1. You are given a 2D integer array highways where highways[i] = [city1_i, city2_i, toll_i] indicates that there is a highway that connects city1_i and city2_i, allowing a car to go from city1_i to city2_i and vice versa for a cost of toll_i.

You are also given an integer k. You are going on a trip that crosses exactly k highways. You may start at any city, but you may only visit each city at most once during your trip.

Return the maximum cost of your trip. If there is no trip that meets the requirements, return -1.


Example 1:

Input: n = 5, highways = [[0,1,4],[2,1,3],[1,4,11],[3,2,3],[3,4,2]], k = 3
Output: 17
Explanation:
One possible trip is to go from 0 -> 1 -> 4 -> 3. The cost of this trip is 4 + 11 + 2 = 17.
Another possible trip is to go from 4 -> 1 -> 2 -> 3. The cost of this trip is 11 + 3 + 3 = 17.
It can be proven that 17 is the maximum possible cost of any valid trip.
Note that the trip 4 -> 1 -> 0 -> 1 is not allowed because you visit the city 1 twice.

Example 2:

Input: n = 4, highways = [[0,1,3],[2,3,2]], k = 2
Output: -1
Explanation: There are no valid trips of length 2, so return -1.


Constraints:

2 <= n <= 15
1 <= highways.length <= 50
highways[i].length == 3
0 <= city1_i, city2_i <= n - 1
city1_i != city2_i
0 <= toll_i <= 100
1 <= k <= 50
There are no duplicate highways.

"""

# V0
# IDEA : BITMASK DP OVER (VISITED SET, CURRENT CITY) — A HAMILTONIAN-STYLE WALK
#
#   "each city at most once" plus n <= 15 is the classic travelling-salesman
#   shape :
#       dp[mask][i] = max cost of a trip that visited exactly `mask` and is
#                     standing at city i
#   with the transition dp[mask | 1<<v][v] = dp[mask][i] + toll(i, v) for
#   every unvisited neighbour v.
#
#   k highways means k + 1 distinct cities, so the answer is the best dp
#   value over the masks with popcount == k + 1.
#
#   NOTE : k >= n makes the trip impossible outright (not enough cities), and
#          the -1 fallback also covers a disconnected graph.
#
"""

DP def
    "each city at most once" plus n <= 15 is the classic travelling-salesman
    shape

    dp[mask][i]: MAX toll of a trip that visited exactly the cities in `mask`

                 and is currently standing at city i

                 -> -1 marks unreachable

DP eq

     dp[mask | (1 << v)][v] = max( dp[mask | (1<<v)][v],

                                   dp[mask][i] + toll(i, v) )

        for every unvisited neighbour v of i


    -> e.g. k highways means k + 1 DISTINCT cities, so

         ans = best dp[mask][i] over masks with popcount(mask) == k + 1

     NOTE !!! k >= n is impossible outright (not enough cities), and the -1
              fallback also covers a disconnected graph

     init: dp[1 << i][i] = 0 for every i

"""
# time = O(2^n * n^2), space = O(2^n * n)
from collections import defaultdict


class Solution(object):
    def maximumCost(self, n, highways, k):
        if k >= n:
            return -1                     # k + 1 distinct cities needed

        adj = defaultdict(list)
        for u, v, w in highways:
            adj[u].append((v, w))
            adj[v].append((u, w))

        size = 1 << n
        dp = [[-1] * n for _ in range(size)]
        for i in range(n):
            dp[1 << i][i] = 0

        res = -1
        for mask in range(size):
            visited = bin(mask).count('1')
            for i in range(n):
                if dp[mask][i] < 0:
                    continue
                if visited == k + 1:
                    res = max(res, dp[mask][i])
                    continue              # no point extending further
                for v, w in adj[i]:
                    if mask >> v & 1:
                        continue
                    nxt = mask | (1 << v)
                    if dp[mask][i] + w > dp[nxt][v]:
                        dp[nxt][v] = dp[mask][i] + w
        return res
