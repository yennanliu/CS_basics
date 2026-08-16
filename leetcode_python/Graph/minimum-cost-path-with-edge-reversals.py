"""

3650. Minimum Cost Path with Edge Reversals
Medium

You are given a directed, weighted graph with n nodes labeled from 0 to n - 1, and a 2D array edges where edges[i] = [ui, vi, wi] represents a directed edge from node ui to node vi with cost wi.

Each node ui has a switch that lets you reverse any of its outgoing edges, but at extra cost:

You may travel from ui to vi along that edge at a cost of wi.
Or you may reverse the edge and travel from vi to ui at a cost of 2 * wi.

Return the minimum total cost to travel from node 0 to node n - 1. If it is not possible, return -1.


Example 1:

Input: n = 4, edges = [[0,1,3],[3,1,1],[2,3,4],[0,2,2]]
Output: 5
Explanation:
Travel from node 0 to node 1 using the edge [0,1,3] at a cost of 3.
Reverse the edge [3,1,1] and travel from node 1 to node 3 at a cost of 2 * 1 = 2.
The total cost is 3 + 2 = 5, which is the minimum possible.


Constraints:

2 <= n <= 5 * 10^4
1 <= edges.length <= 10^5
edges[i] = [ui, vi, wi]
0 <= ui, vi <= n - 1
ui != vi
1 <= wi <= 1000

"""

# V0
# IDEA : MODEL THE REVERSAL AS A SECOND, PRICIER EDGE + DIJKSTRA
#
#   the reversal decision is local to a single traversal — nothing about it
#   is remembered afterwards — so it does not enlarge the state space at all.
#   every directed edge (u, v, w) simply becomes two edges in a plain graph:
#   u -> v costing w, and v -> u costing 2w.
#
#   all costs are positive, so dijkstra on that doubled edge set is exactly
#   the answer, and unreachability shows up as an infinite distance.
#
# time = O((V + E) log V), space = O(V + E)
import heapq


class Solution(object):
    def minCost(self, n, edges):
        adj = [[] for _ in range(n)]
        for u, v, w in edges:
            adj[u].append((v, w))
            adj[v].append((u, 2 * w))

        INF = float('inf')
        dist = [INF] * n
        dist[0] = 0
        pq = [(0, 0)]
        while pq:
            d, u = heapq.heappop(pq)
            if d > dist[u]:
                continue
            if u == n - 1:
                return d
            for v, w in adj[u]:
                nd = d + w
                if nd < dist[v]:
                    dist[v] = nd
                    heapq.heappush(pq, (nd, v))
        return -1 if dist[n - 1] == INF else dist[n - 1]
