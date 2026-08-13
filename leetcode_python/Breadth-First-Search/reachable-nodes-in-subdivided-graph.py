"""

882. Reachable Nodes In Subdivided Graph
Hard

You are given an undirected graph (the "original graph") with n nodes labeled from
0 to n - 1. You decide to subdivide each edge in the graph into a chain of nodes,
with the number of new nodes varying between each edge.

The graph is given as a 2D array of edges where edges[i] = [ui, vi, cnti] indicates
that there is an edge between nodes ui and vi in the original graph, and cnti is the
total number of new nodes that you will subdivide the edge into.
Note that cnti == 0 means you will not subdivide the edge.

To subdivide the edge [ui, vi], replace it with (cnti + 1) new edges and cnti new nodes.
The new nodes are x1, x2, ..., xcnti, and the new edges are [ui, x1], [x1, x2],
[x2, x3], ..., [xcnti-1, xcnti], [xcnti, vi].

In this new graph, you want to know how many nodes are reachable from the node 0,
where a node is reachable if the distance is maxMoves or less.

Given the original graph and maxMoves, return the number of nodes that are reachable
from node 0 in the new graph.


Example 1:

Input: edges = [[0,1,10],[0,2,1],[1,2,2]], maxMoves = 6, n = 3
Output: 13
Explanation: The edge subdivisions are shown in the image above.
The nodes that are reachable are highlighted in yellow.

Example 2:

Input: edges = [[0,1,4],[1,2,6],[0,2,8],[1,3,1]], maxMoves = 10, n = 4
Output: 23

Example 3:

Input: edges = [[1,2,4],[1,4,5],[1,3,1],[2,3,4],[3,4,5]], maxMoves = 17, n = 5
Output: 1
Explanation: Node 0 is disconnected from the rest of the graph,
so only node 0 is reachable.


Constraints:

0 <= edges.length <= min(n * (n - 1) / 2, 10^4)
edges[i].length == 3
0 <= ui < vi < n
There are no multiple edges in the graph.
0 <= cnti <= 10^4
0 <= maxMoves <= 10^9
1 <= n <= 3000

"""

# V0
# IDEA : DIJKSTRA (weighted BFS) + COUNT SUBDIVIDED NODES PER EDGE
#
#   1) Do NOT expand the subdivided nodes (there can be 10^8 of them).
#      Instead treat edge (u, v, cnt) as a single weighted edge of cost cnt + 1
#      and run Dijkstra from node 0 to get dist[] on the ORIGINAL nodes.
#
#   2) An original node is reachable iff dist[node] <= maxMoves.
#
#   3) For each edge (u, v, cnt), the subdivided nodes reachable from the u side
#      are min(cnt, maxMoves - dist[u]), and from the v side
#      min(cnt, maxMoves - dist[v]).  Their sum is capped at cnt so we never
#      double count a node that is reachable from both ends.
#
# time = O(E * log(E))
# space = O(N + E)
import heapq
from collections import defaultdict
class Solution(object):
    def reachableNodes(self, edges, maxMoves, n):
        g = defaultdict(list)
        for u, v, cnt in edges:
            # cnt new nodes on the edge -> cnt + 1 moves to cross it
            g[u].append((v, cnt + 1))
            g[v].append((u, cnt + 1))

        dist = [float('inf')] * n
        dist[0] = 0
        pq = [(0, 0)]  # (distance, node)
        while pq:
            d, u = heapq.heappop(pq)
            if d > dist[u]:
                continue  # stale entry
            for v, w in g[u]:
                nd = d + w
                if nd < dist[v]:
                    dist[v] = nd
                    heapq.heappush(pq, (nd, v))

        # original nodes we can stand on
        res = sum(1 for d in dist if d <= maxMoves)

        # subdivided nodes, counted edge by edge
        for u, v, cnt in edges:
            from_u = min(cnt, max(0, maxMoves - dist[u]))
            from_v = min(cnt, max(0, maxMoves - dist[v]))
            # cap at cnt -> avoids double counting the middle nodes
            res += min(cnt, from_u + from_v)

        return res
