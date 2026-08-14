"""

2277. Closest Node to Path in Tree
Hard
(premium / locked problem)

You are given a positive integer n representing the number of nodes in a tree, numbered from 0 to n - 1 (inclusive). You are also given a 2D integer array edges of length n - 1, where edges[i] = [node1_i, node2_i] denotes that there is a bidirectional edge connecting node1_i and node2_i in the tree.

You are given a 0-indexed integer array query of length m where query[i] = [start_i, end_i, node_i] means that for the ith query, you are tasked with finding the node on the path from start_i to end_i that is closest to node_i.

Return an integer array answer of length m, where answer[i] is the answer to the ith query.


Example 1:

Input: n = 7, edges = [[0,1],[0,2],[0,3],[1,4],[2,5],[2,6]], query = [[5,3,4],[5,3,6]]
Output: [0,2]
Explanation:
The path from node 5 to node 3 consists of the nodes 5, 2, 0, and 3.
The distance between node 4 and node 0 is 2.
Node 0 is the node on the path closest to node 4, so the answer to the first query is 0.
The distance between node 6 and node 2 is 1.
Node 2 is the node on the path closest to node 6, so the answer to the second query is 2.

Example 2:

Input: n = 3, edges = [[0,1],[1,2]], query = [[0,1,2]]
Output: [1]
Explanation:
The path from node 0 to node 1 consists of the nodes 0, 1.
The distance between node 2 and node 1 is 1.
Node 1 is the node on the path closest to node 2, so the answer to the first query is 1.

Example 3:

Input: n = 3, edges = [[0,1],[1,2]], query = [[0,0,0]]
Output: [0]
Explanation:
The path from node 0 to node 0 consists of the node 0.
Since 0 is the only node on the path, the answer to the first query is 0.


Constraints:

1 <= n <= 1000
edges.length == n - 1
edges[i].length == 2
0 <= node1_i, node2_i <= n - 1
node1_i != node2_i
1 <= query.length <= 1000
query[i].length == 3
0 <= start_i, end_i, node_i <= n - 1
The graph is a tree.

"""

# V0
# IDEA : n <= 1000 -> PRECOMPUTE ALL-PAIRS DISTANCES WITH n BFS RUNS
#
#   the tree is unweighted, so a BFS from every node fills dist[u][v] in
#   O(n * (V + E)) = about 10^6 steps.
#
#   with that table, membership on the path is a distance identity :
#       v lies on the path start -> end   <=>
#           dist[start][v] + dist[v][end] == dist[start][end]
#   (true only because the tree path is unique).
#
#   so each query scans all n nodes, keeps the ones on the path, and returns
#   the one minimising dist[node][v].
#
# time = O(n^2 + m * n), space = O(n^2)
from collections import deque, defaultdict


class Solution(object):
    def closestNode(self, n, edges, query):
        g = defaultdict(list)
        for u, v in edges:
            g[u].append(v)
            g[v].append(u)

        dist = [[-1] * n for _ in range(n)]
        for src in range(n):
            d = dist[src]
            d[src] = 0
            q = deque([src])
            while q:
                u = q.popleft()
                for v in g[u]:
                    if d[v] == -1:
                        d[v] = d[u] + 1
                        q.append(v)

        res = []
        for start, end, node in query:
            span = dist[start][end]
            best = -1
            best_d = float('inf')
            for v in range(n):
                if dist[start][v] + dist[v][end] != span:
                    continue                       # not on the path
                if dist[node][v] < best_d:
                    best_d = dist[node][v]
                    best = v
            res.append(best)
        return res
