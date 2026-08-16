"""

3650. Minimum Cost Path with Edge Reversals
Medium

You are given a directed, weighted graph with n nodes labeled from 0 to n -
1, and an array edges where edges[i] = [ui, vi, wi] represents a directed
edge from node ui to node vi with cost wi.

Each node ui has a switch that can be used at most once: when you arrive at
ui and have not yet used its switch, you may activate it on one of its
incoming edges vi → ui reverse that edge to ui → vi and immediately traverse
it.

The reversal is only valid for that single move, and using a reversed edge
costs 2 * wi.

Return the minimum total cost to travel from node 0 to node n - 1. If it is
not possible, return -1.

Example 1:

Input: n = 4, edges = [[0,1,3],[3,1,1],[2,3,4],[0,2,2]]
Output: 5
Explanation:
Use the path 0 → 1 (cost 3).
At node 1 reverse the original edge 3 → 1 into 1 → 3 and traverse it at cost
2 * 1 = 2.
Total cost is 3 + 2 = 5.

Example 2:

Input: n = 4, edges = [[0,2,1],[2,1,1],[1,3,1],[2,3,3]]
Output: 3
Explanation:
No reversal is needed. Take the path 0 → 2 (cost 1), then 2 → 1 (cost 1),
then 1 → 3 (cost 1).
Total cost is 1 + 1 + 1 = 3.

Constraints:

2 <= n <= 5 * 10^4
1 <= edges.length <= 10^5
edges[i] = [ui, vi, wi]
0 <= ui, vi <= n - 1
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
