"""

3419. Minimize the Maximum Edge Weight of Graph
Medium

You are given two integers, n and threshold, as well as a directed weighted
graph of n nodes numbered from 0 to n - 1. The graph is represented by a 2D
integer array edges, where edges[i] = [A_i, B_i, W_i] indicates that there is an
edge going from node A_i to node B_i with weight W_i.

You have to remove some edges from this graph (possibly none), so that it
satisfies the following conditions:

Node 0 must be reachable from all other nodes.
The maximum edge weight in the resulting graph is minimized.
Each node has at most threshold outgoing edges.

Return the minimum possible value of the maximum edge weight after removing the
necessary edges. If it is impossible for all conditions to be satisfied, return
-1.

Example 1:

Input: n = 5, edges = [[1,0,1],[2,0,2],[3,0,1],[4,3,1],[2,1,1]], threshold = 2

Output: 1

Explanation:

Remove the edge 2 -> 0. The maximum weight among the remaining edges is 1.

Example 2:

Input: n = 5, edges = [[0,1,1],[0,2,2],[0,3,1],[0,4,1],[1,2,1],[1,4,1]], threshold = 1

Output: -1

Explanation:

It is impossible to reach node 0 from node 2.

Example 3:

Input: n = 5, edges = [[1,2,1],[1,3,3],[1,4,5],[2,3,2],[3,4,2],[4,0,1]], threshold = 1

Output: 2

Explanation:

Remove the edges 1 -> 3 and 1 -> 4. The maximum weight among the remaining edges
is 2.

Example 4:

Input: n = 5, edges = [[1,2,1],[1,3,3],[1,4,5],[2,3,2],[4,0,1]], threshold = 1

Output: -1

Constraints:

2 <= n <= 10^5
1 <= threshold <= n - 1
1 <= edges.length <= min(10^5, n * (n - 1) / 2).
edges[i].length == 3
0 <= A_i, B_i < n
A_i != B_i
1 <= W_i <= 10^6
There may be multiple edges between a pair of nodes, but they must have unique
weights.

"""

# V0
# IDEA : REVERSE THE EDGES, THEN RUN A MINIMAX (BOTTLENECK) DIJKSTRA FROM 0
#
#   two observations collapse this problem.
#
#   first, the out-degree cap is a red herring.  if node 0 is reachable from
#   everybody we can keep, for each node, just the single outgoing edge that
#   takes it one step along its chosen path to 0.  that leaves every node with
#   out-degree 1 <= threshold, and threshold >= 1 always.  so the cap never
#   binds and can be ignored.
#
#   second, "0 is reachable from v" in the original graph is "v is reachable
#   from 0" in the reversed graph.  and since we may delete any edges we like,
#   the cost of keeping v connected is the *bottleneck* of its best path: the
#   smallest possible value of the largest edge along some 0 -> v route.
#
#   bottleneck distances obey the same greedy exchange argument as ordinary
#   shortest paths — relaxation just uses max(dist[u], w) instead of a sum — so
#   Dijkstra works verbatim.  the answer is the worst bottleneck over all nodes,
#   or -1 if any node is unreachable.
#
# time = O((V + E) log V), space = O(V + E)
import heapq


class Solution(object):
    def minMaxWeight(self, n, edges, threshold):
        g = [[] for _ in range(n)]
        for a, b, w in edges:
            g[b].append((a, w))
        INF = float('inf')
        dist = [INF] * n
        dist[0] = 0
        pq = [(0, 0)]
        seen = 0
        ans = 0
        while pq:
            d, u = heapq.heappop(pq)
            if d > dist[u]:
                continue
            seen += 1
            if d > ans:
                ans = d
            for v, w in g[u]:
                nd = d if d > w else w
                if nd < dist[v]:
                    dist[v] = nd
                    heapq.heappush(pq, (nd, v))
        return ans if seen == n else -1
