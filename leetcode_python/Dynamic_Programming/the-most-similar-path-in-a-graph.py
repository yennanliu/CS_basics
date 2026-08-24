"""

1548. The Most Similar Path in a Graph
Hard

We have n cities and m bi-directional roads where roads[i] = [a_i, b_i] connects city a_i with city b_i. Each city has a name consisting of exactly three upper-case English letters given in the string array names. Starting at any city x, you can reach any city y where y != x (i.e., the cities and the roads are forming an undirected connected graph).

You will be given a string array targetPath. You should find a path in the graph of the same length and with the minimum edit distance to targetPath.

You need to return the order of the nodes in the path with the minimum edit distance. The path should be of the same length of targetPath and should be valid (i.e., there should be a direct road between ans[i] and ans[i + 1]). If there are multiple answers return any one of them.

The edit distance here is the number of positions i at which the chosen path's city name differs from targetPath[i].


Example 1:

Input: n = 5, roads = [[0,2],[0,3],[1,2],[1,3],[1,4],[2,4]], names = ["ATL","PEK","LAX","DXB","HND"], targetPath = ["ATL","DXB","HND","LAX"]
Output: [0,2,4,2]
Explanation: [0,2,4,2], [0,3,0,2] and [0,3,1,2] are accepted answers.
[0,2,4,2] is equivalent to ["ATL","LAX","HND","LAX"] which has edit distance = 1 with targetPath.
[0,3,0,2] is equivalent to ["ATL","DXB","ATL","LAX"] which has edit distance = 1 with targetPath.
[0,3,1,2] is equivalent to ["ATL","DXB","PEK","LAX"] which has edit distance = 1 with targetPath.

Example 2:

Input: n = 4, roads = [[1,0],[2,0],[3,0],[2,1],[3,1],[3,2]], names = ["ATL","PEK","LAX","DXB"], targetPath = ["ABC","DEF","GHI","JKL","MNO","PQR","STU","VWX"]
Output: [0,1,0,1,0,1,0,1]
Explanation: Any path in this graph has edit distance = 8 with targetPath.

Example 3:

Input: n = 6, roads = [[0,1],[1,2],[2,3],[3,4],[4,5]], names = ["ATL","PEK","LAX","ATL","DXB","HND"], targetPath = ["ATL","DXB","HND","DXB","ATL","LAX","PEK"]
Output: [3,4,5,4,3,2,1]
Explanation: [3,4,5,4,3,2,1] is the only path with edit distance = 0 with targetPath.
It's equivalent to ["ATL","DXB","HND","DXB","ATL","LAX","PEK"]


Constraints:

2 <= n <= 100
m == roads.length
n - 1 <= m <= (n * (n - 1) / 2)
0 <= a_i, b_i <= n - 1
a_i != b_i
The graph is guaranteed to be connected and each pair of nodes may have at most one direct road.
names.length == n
names[i].length == 3
names[i] consists of upper-case English letters.
There can be two cities with the same name.
1 <= targetPath.length <= 100
targetPath[i].length == 3
targetPath[i] consists of upper-case English letters.


Follow up: If each node can be visited only once in the path, What should you change in your solution?

"""

# V0
# IDEA : DP OVER (step, city) + PARENT POINTERS TO REBUILD THE PATH
#
#   since the path length is FIXED at len(targetPath), the "edit distance"
#   degenerates to a plain per-position mismatch count — no insert/delete.
#
#   f[i][j] = min mismatches for a valid path of length i+1 whose last city
#             is j.
#
#     f[0][j] = (names[j] != targetPath[0])
#     f[i][j] = min over neighbours k of j of  f[i-1][k]  +  (names[j] != targetPath[i])
#
#   store pre[i][j] = the k that achieved the minimum, then walk the pointers
#   backwards from the best final city to reconstruct the answer.
#   NOTE : nodes MAY repeat in the path (see example 1), so no visited set.
#
"""

DP def
    the path length is FIXED at len(targetPath), so the "edit distance"
    degenerates to a plain per-position MISMATCH COUNT - no insert / delete

    f[i][j]: MIN mismatches for a valid path of length i+1 whose LAST city is j

    pre[i][j]: the predecessor that achieved that minimum (to rebuild the path)

DP eq

     f[0][j] = (names[j] != targetPath[0])

     f[i][j] = min over neighbours k of j of f[i-1][k]

               + (names[j] != targetPath[i])


    -> e.g. NOTE !!! nodes MAY repeat in the path, so there is no visited set

     ans = walk `pre` backwards from the best final city

"""
# time = O(L * (n + m)) with L = len(targetPath), space = O(L * n)
class Solution(object):
    def mostSimilar(self, n, roads, names, targetPath):
        g = [[] for _ in range(n)]
        for a, b in roads:
            g[a].append(b)
            g[b].append(a)

        m = len(targetPath)
        INF = float('inf')
        f = [[INF] * n for _ in range(m)]
        pre = [[-1] * n for _ in range(m)]

        for j in range(n):
            f[0][j] = 0 if names[j] == targetPath[0] else 1

        for i in range(1, m):
            for j in range(n):
                cost = 0 if names[j] == targetPath[i] else 1
                best = INF
                arg = -1
                for k in g[j]:
                    if f[i - 1][k] < best:
                        best = f[i - 1][k]
                        arg = k
                f[i][j] = best + cost
                pre[i][j] = arg

        last = 0
        for j in range(1, n):
            if f[m - 1][j] < f[m - 1][last]:
                last = j

        res = [0] * m
        for i in range(m - 1, -1, -1):
            res[i] = last
            last = pre[i][last]
        return res
