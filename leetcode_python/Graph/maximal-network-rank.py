"""

1615. Maximal Network Rank
Medium

There is an infrastructure of n cities with some number of roads connecting these cities. Each roads[i] = [ai, bi] indicates that there is a bidirectional road between cities ai and bi.

The network rank of two different cities is defined as the total number of directly connected roads to either city. If a road is directly connected to both cities, it is only counted once.

The maximal network rank of the infrastructure is the maximum network rank of all pairs of different cities.

Given the integer n and the array roads, return the maximal network rank of the entire infrastructure.


Example 1:

Input: n = 4, roads = [[0,1],[0,3],[1,2],[1,3]]
Output: 4
Explanation: The network rank of cities 0 and 1 is 4 as there are 4 roads that are connected to either 0 or 1. The road between 0 and 1 is only counted once.

Example 2:

Input: n = 5, roads = [[0,1],[0,3],[1,2],[1,3],[2,3],[2,4]]
Output: 5
Explanation: There are 5 roads that are connected to cities 1 or 2.

Example 3:

Input: n = 8, roads = [[0,1],[1,2],[2,3],[2,4],[5,6],[5,7]]
Output: 5
Explanation: The network rank of 2 and 5 is 5. Notice that all the cities do not have to be connected.


Constraints:

2 <= n <= 100
0 <= roads.length <= n * (n - 1) / 2
roads[i].length == 2
0 <= ai, bi <= n-1
ai != bi
Each pair of cities has at most one road connecting them.

"""

# V0
# IDEA : DEGREE COUNT + INCLUSION-EXCLUSION over all pairs
#
#   rank(a, b) = deg[a] + deg[b] - (1 if a and b are directly linked else 0)
#   -- the shared road would otherwise be double counted.
#
#   n <= 100 so all n*(n-1)/2 pairs is only ~5000 checks; keep an adjacency
#   matrix so the "are they linked" test is O(1).
#
# time = O(n^2 + E), space = O(n^2)
class Solution(object):
    def maximalNetworkRank(self, n, roads):
        deg = [0] * n
        linked = [[False] * n for _ in range(n)]
        for a, b in roads:
            deg[a] += 1
            deg[b] += 1
            linked[a][b] = True
            linked[b][a] = True

        res = 0
        for a in range(n):
            for b in range(a + 1, n):
                rank = deg[a] + deg[b] - (1 if linked[a][b] else 0)
                if rank > res:
                    res = rank
        return res
