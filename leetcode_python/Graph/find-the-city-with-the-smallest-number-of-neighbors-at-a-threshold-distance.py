"""

1334. Find the City With the Smallest Number of Neighbors at a Threshold Distance
Medium

There are n cities numbered from 0 to n-1. Given the array edges where
edges[i] = [from_i, to_i, weight_i] represents a bidirectional and weighted
edge between cities from_i and to_i, and given the integer distanceThreshold.

Return the city with the smallest number of cities that are reachable through
some path and whose distance is at most distanceThreshold, If there are
multiple such cities, return the city with the greatest number.

Notice that the distance of a path connecting cities i and j is equal to
the sum of the edges' weights along that path.


Example 1:

Input: n = 4, edges = [[0,1,3],[1,2,1],[1,3,4],[2,3,1]], distanceThreshold = 4
Output: 3
Explanation: The figure above describes the graph.
The neighboring cities at a distanceThreshold = 4 for each city are:
City 0 -> [City 1, City 2]
City 1 -> [City 0, City 2, City 3]
City 2 -> [City 0, City 1, City 3]
City 3 -> [City 1, City 2]
Cities 0 and 3 have 2 neighboring cities at a distanceThreshold = 4,
but we have to return city 3 since it has the greatest number.

Example 2:

Input: n = 5, edges = [[0,1,2],[0,4,8],[1,2,3],[1,4,2],[2,3,1],[3,4,1]], distanceThreshold = 2
Output: 0
Explanation: The figure above describes the graph.
The neighboring cities at a distanceThreshold = 2 for each city are:
City 0 -> [City 1]
City 1 -> [City 0, City 4]
City 2 -> [City 3, City 4]
City 3 -> [City 2, City 4]
City 4 -> [City 1, City 2, City 3]
The city 0 has 1 neighboring city at a distanceThreshold = 2.


Constraints:

2 <= n <= 100
1 <= edges.length <= n * (n - 1) / 2
edges[i].length == 3
0 <= from_i < to_i < n
1 <= weight_i, distanceThreshold <= 10^4
All pairs (from_i, to_i) are distinct.

"""

# V0
# IDEA: FLOYD-WARSHALL (all pairs shortest path)
#
#  n <= 100 -> O(n^3) is fine.
#
#  NOTE:
#   - dist[i][i] = 0, so city i always counts itself as a "neighbor".
#     That is fine, since the self count is +1 for EVERY city,
#     so it does not change which city wins.
#   - tie break: return the GREATEST city number
#     -> iterate i from n-1 down to 0 and use strict `<`
#
# time = O(n^3)
# space = O(n^2)
class Solution(object):
    def findTheCity(self, n, edges, distanceThreshold):
        INF = float('inf')

        # init dist matrix
        dist = [[INF] * n for _ in range(n)]
        for i in range(n):
            dist[i][i] = 0
        for u, v, w in edges:
            # keep the smallest weight if duplicated edges show up
            if w < dist[u][v]:
                dist[u][v] = w
                dist[v][u] = w

        # floyd-warshall
        for k in range(n):
            for i in range(n):
                if dist[i][k] == INF:
                    continue
                for j in range(n):
                    if dist[i][k] + dist[k][j] < dist[i][j]:
                        dist[i][j] = dist[i][k] + dist[k][j]

        res, best = n - 1, INF
        for i in range(n - 1, -1, -1):
            cnt = sum(1 for d in dist[i] if d <= distanceThreshold)
            if cnt < best:
                best = cnt
                res = i
        return res


# V0-1
# IDEA: DIJKSTRA (heap) from every node
#  different complexity from the Floyd-Warshall above:
#  better when the graph is sparse
# time = O(n * (E log V))
# space = O(n + E)
import collections
import heapq
class Solution(object):
    def findTheCity(self, n, edges, distanceThreshold):
        g = collections.defaultdict(list)
        for u, v, w in edges:
            g[u].append((v, w))
            g[v].append((u, w))

        def dijkstra(src):
            dist = [float('inf')] * n
            dist[src] = 0
            pq = [(0, src)]
            while pq:
                d, u = heapq.heappop(pq)
                if d > dist[u]:
                    continue
                for v, w in g[u]:
                    nd = d + w
                    if nd < dist[v]:
                        dist[v] = nd
                        heapq.heappush(pq, (nd, v))
            return sum(1 for x in dist if x <= distanceThreshold)

        res, best = n - 1, float('inf')
        for i in range(n - 1, -1, -1):
            cnt = dijkstra(i)
            if cnt < best:
                best = cnt
                res = i
        return res
