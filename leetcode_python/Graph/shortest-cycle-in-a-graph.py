"""

2608. Shortest Cycle in a Graph
Hard

There is a bi-directional graph with n vertices, where each vertex is labeled from 0 to n - 1. The edges in the graph are represented by a given 2D integer array edges, where edges[i] = [ui, vi] denotes an edge between vertex ui and vertex vi. Every vertex pair is connected by at most one edge, and no vertex has an edge to itself.

Return the length of the shortest cycle in the graph. If no cycle exists, return -1.

A cycle is a path that starts and ends at the same node, and each edge in the path is used only once.


Example 1:

Input: n = 7, edges = [[0,1],[1,2],[2,0],[3,4],[4,5],[5,6],[6,3]]
Output: 3
Explanation: The cycle with the smallest length is : 0 -> 1 -> 2 -> 0

Example 2:

Input: n = 4, edges = [[0,1],[0,2]]
Output: -1
Explanation: There are no cycles in this graph.


Constraints:

2 <= n <= 1000
1 <= edges.length <= 1000
edges[i].length == 2
0 <= ui, vi < n
ui != vi
There are no repeated edges.


"""

from collections import deque

# V0
# IDEA : BFS FROM EVERY VERTEX (girth of an unweighted graph)
#
#   run a BFS rooted at each vertex u, tracking dist[] and the parent each
#   node was reached from. while scanning an edge (a, b):
#     - b unseen           -> normal BFS tree edge
#     - b already seen and b is not a's parent -> this NON-tree edge closes a
#       cycle through the root, of length dist[a] + dist[b] + 1
#
#   the smallest such value over all roots is the girth.
#
#   NOTE : dist[a] + dist[b] + 1 can OVERESTIMATE for a given root (the two
#          BFS paths may share a prefix), but it is never an underestimate,
#          and for the root that actually lies ON a shortest cycle the value
#          is exact — so the global minimum is correct.
#   NOTE : the parent check must exclude only the edge we just came through,
#          which is safe here because the problem forbids repeated edges.
#   NOTE : the queue carries (node, parent) so each edge is judged once per
#          direction.
#   NOTE : BFS pops in non-decreasing dist order, so once 2 * dist[a] >= res
#          nothing later in THIS root's BFS can beat res -> abandon the root.
#
# time = O(n * (n + m)), space = O(n + m)
class Solution(object):
    def findShortestCycle(self, n, edges):
        INF = float('inf')
        g = [[] for _ in range(n)]
        for u, v in edges:
            g[u].append(v)
            g[v].append(u)

        res = INF
        for root in range(n):
            dist = [-1] * n
            dist[root] = 0
            q = deque([(root, -1)])
            while q:
                a, parent = q.popleft()
                if dist[a] * 2 >= res:
                    # every remaining pop is at least this deep -> give up
                    break
                for b in g[a]:
                    if dist[b] < 0:
                        dist[b] = dist[a] + 1
                        q.append((b, a))
                    elif b != parent:
                        cand = dist[a] + dist[b] + 1
                        if cand < res:
                            res = cand
        return res if res < INF else -1
