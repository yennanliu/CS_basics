"""

3559. Number of Ways to Assign Edge Weights II
Hard

There is an undirected tree with n nodes labeled from 1 to n, rooted at node 1.
The tree is represented by a 2D integer array edges of length n - 1, where
edges[i] = [u_i, v_i] indicates that there is an edge between nodes u_i and v_i.

Initially, all edges have a weight of 0. You must assign each edge a weight of
either 1 or 2.

The cost of a path between any two nodes u and v is the total weight of all
edges in the path connecting them.

You are given a 2D integer array queries. For each queries[i] = [u_i, v_i],
determine the number of ways to assign weights to edges in the path such that
the cost of the path between u_i and v_i is odd.

Return an array answer, where answer[i] is the number of valid assignments for
queries[i].

Since the answer may be large, apply modulo 10^9 + 7 to each answer[i].

Note: For each query, disregard all edges not in the path between node u_i and
v_i.

Example 1:

Input: edges = [[1,2]], queries = [[1,1],[1,2]]

Output: [0,1]

Explanation:

Query [1,1]: The path from Node 1 to itself consists of no edges, so the cost is
0. Thus, the number of valid assignments is 0.

Query [1,2]: The path from Node 1 to Node 2 consists of one edge (1 → 2).
Assigning weight 1 makes the cost odd, while 2 makes it even. Thus, the number
of valid assignments is 1.

Example 2:

Input: edges = [[1,2],[1,3],[3,4],[3,5]], queries = [[1,4],[3,4],[2,5]]

Output: [2,1,4]

Explanation:

Query [1,4]: The path from Node 1 to Node 4 consists of two edges (1 → 3 and 3 →
4). Assigning weights (1,2) or (2,1) results in an odd cost. Thus, the number of
valid assignments is 2.

Query [3,4]: The path from Node 3 to Node 4 consists of one edge (3 → 4).
Assigning weight 1 makes the cost odd, while 2 makes it even. Thus, the number
of valid assignments is 1.

Query [2,5]: The path from Node 2 to Node 5 consists of three edges (2 → 1, 1 →
3, and 3 → 5). Assigning (1,2,2), (2,1,2), (2,2,1), or (1,1,1) makes the cost
odd. Thus, the number of valid assignments is 4.

Constraints:

2 <= n <= 10^5

edges.length == n - 1

edges[i] == [u_i, v_i]

1 <= queries.length <= 10^5

queries[i] == [u_i, v_i]

1 <= u_i, v_i <= n

edges represents a valid tree.

"""

# V0
# IDEA : PATH LENGTH VIA LCA, THEN THE PARITY COUNT IS 2^(L-1)
#
#   weights are 1 or 2, so only the edges given a 1 change the parity: the path
#   cost is odd exactly when an odd number of its L edges got a 1.  the number
#   of odd-sized subsets of L items is 2^(L-1) for every L >= 1 (fix the first
#   L - 1 edges freely and the last one is forced), and 0 when the path is
#   empty.
#
#   so each query only needs L, the number of edges between u and v, which is
#   depth[u] + depth[v] - 2 * depth[lca(u, v)].
#
#   with up to 10^5 queries an O(log n) climb per LCA is already the bottleneck,
#   so the tree is flattened into an Euler tour: the LCA is the shallowest node
#   visited between the two nodes' first appearances, and a sparse table over
#   the tour depths turns that into an O(1) range minimum.
#
# time = O(n * log n + q), space = O(n * log n)
class Solution(object):
    def assignEdgeWeights(self, edges, queries):
        MOD = 10 ** 9 + 7
        n = len(edges) + 1
        g = [[] for _ in range(n + 1)]
        for u, v in edges:
            g[u].append(v)
            g[v].append(u)

        depth = [0] * (n + 1)
        first = [0] * (n + 1)
        tour = []
        tdep = []
        it = [0] * (n + 1)
        seen = [False] * (n + 1)
        seen[1] = True
        stack = [1]
        while stack:
            u = stack[-1]
            if it[u] == 0:
                first[u] = len(tour)
                tour.append(u)
                tdep.append(depth[u])
            adv = False
            while it[u] < len(g[u]):
                v = g[u][it[u]]
                it[u] += 1
                if not seen[v]:
                    seen[v] = True
                    depth[v] = depth[u] + 1
                    stack.append(v)
                    adv = True
                    break
            if not adv:
                stack.pop()
                if stack:
                    p = stack[-1]
                    tour.append(p)
                    tdep.append(depth[p])

        m = len(tour)
        log = [0] * (m + 1)
        for i in range(2, m + 1):
            log[i] = log[i >> 1] + 1
        sp = [list(range(m))]
        j = 1
        while (1 << j) <= m:
            prev = sp[-1]
            half = 1 << (j - 1)
            cur = [0] * (m - (1 << j) + 1)
            for i in range(len(cur)):
                a = prev[i]
                b = prev[i + half]
                cur[i] = a if tdep[a] <= tdep[b] else b
            sp.append(cur)
            j += 1

        pw = [1] * (n + 1)
        for i in range(1, n + 1):
            pw[i] = pw[i - 1] * 2 % MOD

        res = []
        for u, v in queries:
            i, k = first[u], first[v]
            if i > k:
                i, k = k, i
            j = log[k - i + 1]
            a = sp[j][i]
            b = sp[j][k - (1 << j) + 1]
            d = tdep[a] if tdep[a] <= tdep[b] else tdep[b]
            L = depth[u] + depth[v] - 2 * d
            res.append(pw[L - 1] if L else 0)
        return res
