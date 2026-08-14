"""

2203. Minimum Weighted Subgraph With the Required Paths
Hard

You are given an integer n denoting the number of nodes of a weighted directed graph. The nodes are numbered from 0 to n - 1.

You are also given a 2D integer array edges where edges[i] = [from_i, to_i, weight_i] denotes that there exists a directed edge from from_i to to_i with weight weight_i.

Lastly, you are given three distinct integers src1, src2, and dest denoting three distinct nodes of the graph.

Return the minimum weight of a subgraph of the graph such that it is possible to reach dest from both src1 and src2 via a set of edges of this subgraph. In case such a subgraph does not exist, return -1.

A subgraph is a graph whose vertices and edges are subsets of the original graph. The weight of a subgraph is the sum of weights of its constituent edges.


Example 1:

Input: n = 6, edges = [[0,2,2],[0,5,6],[1,0,3],[1,4,5],[2,1,1],[2,3,3],[2,3,4],[3,4,2],[4,5,1]], src1 = 0, src2 = 1, dest = 5
Output: 9
Explanation:
The above figure represents the input graph.
The blue edges represent one of the subgraphs that yield the optimal answer.
Note that the subgraph [[1,0,3],[0,5,6]] also yields the optimal answer. It is not possible to get a subgraph with less weight.

Example 2:

Input: n = 3, edges = [[0,1,1],[2,1,1]], src1 = 0, src2 = 1, dest = 2
Output: -1
Explanation:
The above figure represents the input graph.
It can be seen that there does not exist any path from node 1 to node 2, hence there are no subgraphs satisfying all the constraints.


Constraints:

3 <= n <= 10^5
0 <= edges.length <= 10^5
edges[i].length == 3
0 <= from_i, to_i, src1, src2, dest <= n - 1
from_i != to_i
src1, src2, and dest are pairwise distinct.
1 <= weight_i <= 10^5

"""

# V0
# IDEA : THE OPTIMAL SUBGRAPH IS A "Y" — THREE DIJKSTRAS, ONE MEETING POINT
#
#   the two required paths share a suffix (possibly empty), so the shape is
#       src1 -> m ... and ... src2 -> m, then m -> dest
#   for some meeting node m. the total weight is then
#       d1[m] + d2[m] + dr[m]
#   where
#       d1 = shortest distances FROM src1 on the graph
#       d2 = shortest distances FROM src2 on the graph
#       dr = shortest distances TO dest, i.e. from dest on the REVERSED graph
#
#   minimise that sum over every node m. m == dest (no shared suffix) and
#   m == src1 (one path fully contained in the other) are both included, so
#   no special cases are needed.
#
#   NOTE : summing three distances is safe because reusing a shared edge is
#          never double-counted at the optimum — the overlap is exactly the
#          m -> dest tail.
#
# time = O((V + E) log V), space = O(V + E)
import heapq
from collections import defaultdict


class Solution(object):
    def minimumWeight(self, n, edges, src1, src2, dest):
        g = defaultdict(list)
        rg = defaultdict(list)
        for u, v, w in edges:
            g[u].append((v, w))
            rg[v].append((u, w))

        INF = float('inf')

        def dijkstra(source, graph):
            dist = [INF] * n
            dist[source] = 0
            pq = [(0, source)]
            while pq:
                d, u = heapq.heappop(pq)
                if d > dist[u]:
                    continue
                for v, w in graph[u]:
                    if d + w < dist[v]:
                        dist[v] = d + w
                        heapq.heappush(pq, (d + w, v))
            return dist

        d1 = dijkstra(src1, g)
        d2 = dijkstra(src2, g)
        dr = dijkstra(dest, rg)

        best = min(d1[m] + d2[m] + dr[m] for m in range(n))
        return -1 if best == INF else best
