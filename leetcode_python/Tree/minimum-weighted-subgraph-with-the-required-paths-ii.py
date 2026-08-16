"""

3553. Minimum Weighted Subgraph With the Required Paths II
Hard

You are given an undirected weighted tree with n nodes, numbered from 0 to n -
1. It is represented by a 2D integer array edges of length n - 1, where edges[i]
= [u_i, v_i, w_i] indicates that there is an edge between nodes u_i and v_i with
weight w_i.

Additionally, you are given a 2D integer array queries, where queries[j] =
[src1_j, src2_j, dest_j].

Return an array answer of length equal to queries.length, where answer[j] is the
minimum total weight of a subtree such that it is possible to reach dest_j from
both src1_j and src2_j using edges in this subtree.

A subtree here is any connected subset of nodes and edges of the original tree
forming a valid tree.

Example 1:

Input: edges = [[0,1,2],[1,2,3],[1,3,5],[1,4,4],[2,5,6]], queries =
[[2,3,4],[0,2,5]]

Output: [12,11]

Explanation:

The blue edges represent one of the subtrees that yield the optimal answer.

answer[0]: The total weight of the selected subtree that ensures a path from
src1 = 2 and src2 = 3 to dest = 4 is 3 + 5 + 4 = 12.

answer[1]: The total weight of the selected subtree that ensures a path from
src1 = 0 and src2 = 2 to dest = 5 is 2 + 3 + 6 = 11.

Example 2:

Input: edges = [[1,0,8],[0,2,7]], queries = [[0,1,2]]

Output: [15]

Explanation:

answer[0]: The total weight of the selected subtree that ensures a path from
src1 = 0 and src2 = 1 to dest = 2 is 8 + 7 = 15.

Constraints:

3 <= n <= 10^5

edges.length == n - 1

edges[i].length == 3

0 <= u_i, v_i < n

1 <= w_i <= 10^4

1 <= queries.length <= 10^5

queries[j].length == 3

0 <= src1_j, src2_j, dest_j < n

src1_j, src2_j, and dest_j are pairwise distinct.

The input is generated such that edges represents a valid tree.

"""

# V0
# IDEA : THE STEINER TREE OF THREE NODES IS HALF THE SUM OF THEIR PAIRWISE PATHS
#
#   in a tree the cheapest connected subgraph joining src1, src2 and dest is the
#   union of the three pairwise paths, and those three paths all meet at one
#   node -- the "median" of the triple.  every edge of the union therefore lies
#   on exactly two of the three paths, so
#       weight = (d(a,b) + d(b,c) + d(a,c)) / 2
#   and no traversal per query is needed at all.
#
#   what is needed is d(u,v) = dist[u] + dist[v] - 2 * dist[lca(u,v)], i.e. a
#   fast LCA.  with 3 * 10^5 LCA lookups, an O(log n) climb per lookup is
#   already expensive, so the tree is flattened into an Euler tour: the LCA of
#   u and v is the shallowest node visited between their first appearances, and
#   a sparse table over the tour depths answers that range minimum in O(1).
#
#   both the tour and the root distances come from one iterative DFS -- n
#   reaches 10^5, far past Python's recursion limit on a path-shaped tree.
#
# time = O(n * log n + q), space = O(n * log n)
class Solution(object):
    def minimumWeight(self, edges, queries):
        n = len(edges) + 1
        g = [[] for _ in range(n)]
        for u, v, w in edges:
            g[u].append((v, w))
            g[v].append((u, w))

        dist = [0] * n
        depth = [0] * n
        first = [0] * n
        tour = []                              # Euler tour of node ids
        tdep = []                              # depth of each tour entry
        it = [0] * n                           # next child index per node
        parent = [-1] * n
        stack = [0]
        seen = [False] * n
        seen[0] = True
        while stack:
            u = stack[-1]
            if it[u] == 0:
                first[u] = len(tour)
                tour.append(u)
                tdep.append(depth[u])
            adv = False
            while it[u] < len(g[u]):
                v, w = g[u][it[u]]
                it[u] += 1
                if not seen[v]:
                    seen[v] = True
                    parent[v] = u
                    depth[v] = depth[u] + 1
                    dist[v] = dist[u] + w
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

        def lca(u, v):
            i, k = first[u], first[v]
            if i > k:
                i, k = k, i
            j = log[k - i + 1]
            a = sp[j][i]
            b = sp[j][k - (1 << j) + 1]
            return tour[a] if tdep[a] <= tdep[b] else tour[b]

        res = []
        for a, b, c in queries:
            dab = dist[a] + dist[b] - 2 * dist[lca(a, b)]
            dbc = dist[b] + dist[c] - 2 * dist[lca(b, c)]
            dac = dist[a] + dist[c] - 2 * dist[lca(a, c)]
            res.append((dab + dbc + dac) // 2)
        return res
