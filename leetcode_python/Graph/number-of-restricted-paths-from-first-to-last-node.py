"""

1786. Number of Restricted Paths From First to Last Node
Medium

There is an undirected weighted connected graph. You are given a positive integer n which denotes that the graph has n nodes labeled from 1 to n, and an array edges where each edges[i] = [ui, vi, weighti] denotes that there is an edge between nodes ui and vi with weight equal to weighti.

A path from node start to node end is a sequence of nodes [z0, z1, z2, ..., zk] such that z0= start and zk = end and there is an edge between zi and zi+1 where 0 <= i <= k-1.

The distance of a path is the sum of the weights on the edges of the path. Let distanceToLastNode(x) denote the shortest distance of a path between node n and node x. A restricted path is a path that also satisfies that distanceToLastNode(zi) > distanceToLastNode(zi+1) where 0 <= i <= k-1.

Return the number of restricted paths from node 1 to node n. Since that number may be too large, return it modulo 10^9 + 7.

Example 1:

Input: n = 5, edges = [[1,2,3],[1,3,3],[2,3,1],[1,4,2],[5,2,2],[3,5,1],[5,4,10]]
Output: 3
Explanation: Each circle contains the node number in black and its distanceToLastNode value in blue. The three restricted paths are:
1) 1 --> 2 --> 5
2) 1 --> 2 --> 3 --> 5
3) 1 --> 3 --> 5

Example 2:

Input: n = 7, edges = [[1,3,1],[4,1,2],[7,3,4],[2,5,3],[5,6,1],[6,7,2],[7,5,3],[2,6,4]]
Output: 1
Explanation: Each circle contains the node number in black and its distanceToLastNode value in blue. The only restricted path is 1 --> 3 --> 7.

Constraints:

1 <= n <= 2 * 10^4
n - 1 <= edges.length <= 4 * 10^4
edges[i].length == 3
1 <= ui, vi <= n
ui!= vi
1 <= weighti <= 10^5
There is at most one edge between any two nodes.
There is at least one path between any two nodes.

"""

# V0
# IDEA : DIJKSTRA FROM NODE n, THEN DP IN ORDER OF INCREASING DISTANCE
#
#   dist[x] = shortest distance from x to n (the graph is undirected, so one
#   Dijkstra run rooted at n gives all of them).
#   a restricted path is one whose dist values strictly decrease, so the graph
#   of legal moves u -> v (dist[u] > dist[v]) is a DAG ordered by dist.
#     f[n] = 1,  f[u] = sum of f[v] over neighbours v with dist[v] < dist[u]
#   processing nodes in increasing dist guarantees every f[v] we read is final,
#   so no recursion is needed.
#
# time = O((n + m) log n), space = O(n + m)
from heapq import heappush, heappop
class Solution(object):
    def countRestrictedPaths(self, n, edges):
        MOD = 10 ** 9 + 7
        g = [[] for _ in range(n + 1)]
        for u, v, w in edges:
            g[u].append((v, w))
            g[v].append((u, w))

        INF = float("inf")
        dist = [INF] * (n + 1)
        dist[n] = 0
        pq = [(0, n)]
        while pq:
            d, u = heappop(pq)
            if d > dist[u]:
                continue
            for v, w in g[u]:
                if d + w < dist[v]:
                    dist[v] = d + w
                    heappush(pq, (dist[v], v))

        f = [0] * (n + 1)
        f[n] = 1
        order = sorted(range(1, n + 1), key=lambda x: dist[x])
        for u in order:
            if u == n:
                continue
            total = 0
            for v, _ in g[u]:
                if dist[v] < dist[u]:
                    total += f[v]
            f[u] = total % MOD
        return f[1]
