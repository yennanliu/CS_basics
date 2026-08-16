"""

3515. Shortest Path in a Weighted Tree
Hard

You are given an integer n and an undirected, weighted tree rooted at node 1
with n nodes numbered from 1 to n. This is represented by a 2D array edges of
length n - 1, where edges[i] = [u_i, v_i, w_i] indicates an undirected edge from
node u_i to v_i with weight w_i.

You are also given a 2D integer array queries of length q, where each queries[i]
is either:

[1, u, v, w'] – Update the weight of the edge between nodes u and v to w', where
(u, v) is guaranteed to be an edge present in edges.

[2, x] – Compute the shortest path distance from the root node 1 to node x.

Return an integer array answer, where answer[i] is the shortest path distance
from node 1 to x for the i^th query of [2, x].

Example 1:

Input: n = 2, edges = [[1,2,7]], queries = [[2,2],[1,1,2,4],[2,2]]

Output: [7,4]

Explanation:

Query [2,2]: The shortest path from root node 1 to node 2 is 7.

Query [1,1,2,4]: The weight of edge (1,2) changes from 7 to 4.

Query [2,2]: The shortest path from root node 1 to node 2 is 4.

Example 2:

Input: n = 3, edges = [[1,2,2],[1,3,4]], queries =
[[2,1],[2,3],[1,1,3,7],[2,2],[2,3]]

Output: [0,4,2,7]

Explanation:

Query [2,1]: The shortest path from root node 1 to node 1 is 0.

Query [2,3]: The shortest path from root node 1 to node 3 is 4.

Query [1,1,3,7]: The weight of edge (1,3) changes from 4 to 7.

Query [2,2]: The shortest path from root node 1 to node 2 is 2.

Query [2,3]: The shortest path from root node 1 to node 3 is 7.

Example 3:

Input: n = 4, edges = [[1,2,2],[2,3,1],[3,4,5]], queries =
[[2,4],[2,3],[1,2,3,3],[2,2],[2,3]]

Output: [8,3,2,5]

Explanation:

Query [2,4]: The shortest path from root node 1 to node 4 consists of edges
(1,2), (2,3), and (3,4) with weights 2 + 1 + 5 = 8.

Query [2,3]: The shortest path from root node 1 to node 3 consists of edges
(1,2) and (2,3) with weights 2 + 1 = 3.

Query [1,2,3,3]: The weight of edge (2,3) changes from 1 to 3.

Query [2,2]: The shortest path from root node 1 to node 2 is 2.

Query [2,3]: The shortest path from root node 1 to node 3 consists of edges
(1,2) and (2,3) with updated weights 2 + 3 = 5.

Constraints:

1 <= n <= 10^5

edges.length == n - 1

edges[i] == [u_i, v_i, w_i]

1 <= u_i, v_i <= n

1 <= w_i <= 10^4

The input is generated such that edges represents a valid tree.

1 <= queries.length == q <= 10^5

queries[i].length == 2 or 4

queries[i] == [1, u, v, w'] or,

queries[i] == [2, x]

1 <= u, v, x <= n

(u, v) is always an edge from edges.

1 <= w' <= 10^4

"""

# V0
# IDEA : EULER TOUR TURNS "EDGE UPDATE" INTO A RANGE ADD ON A FENWICK TREE
#
#   in a rooted tree the distance from the root to x is just the sum of the
#   edge weights on that one path, so dist(x) = dist(parent) + w(parent, x).
#   changing one edge (p, c) by delta therefore changes dist(y) by exactly
#   delta for every y in the subtree of c, and leaves every other node alone.
#
#   a DFS preorder numbers each subtree as one contiguous block [tin, tout], so
#   "add delta to a whole subtree" becomes "add delta to a range" and "read
#   dist(x)" becomes "read one position".  a Fenwick tree over the difference
#   array does range-add / point-query in O(log n) each.
#
#   the only bookkeeping left is deciding which endpoint of an updated edge is
#   the child -- the one whose parent is the other endpoint -- and remembering
#   each edge's current weight so the delta can be computed.
#
#   note the DFS is written with an explicit stack: n reaches 10^5, deep enough
#   to blow Python's recursion limit on a path-shaped tree.
#
# time = O((n + q) * log n), space = O(n)
class Solution(object):
    def treeQueries(self, n, edges, queries):
        g = [[] for _ in range(n + 1)]
        for u, v, w in edges:
            g[u].append((v, w))
            g[v].append((u, w))

        par = [0] * (n + 1)
        wpar = [0] * (n + 1)                 # weight of the edge to the parent
        order = []
        seen = [False] * (n + 1)
        seen[1] = True
        stack = [1]
        while stack:                          # DFS preorder, iterative
            u = stack.pop()
            order.append(u)
            for v, w in g[u]:
                if not seen[v]:
                    seen[v] = True
                    par[v] = u
                    wpar[v] = w
                    stack.append(v)

        tin = [0] * (n + 1)
        for i, u in enumerate(order):
            tin[u] = i
        size = [1] * (n + 1)
        for u in reversed(order):
            if u != 1:
                size[par[u]] += size[u]
        tout = [0] * (n + 1)
        for u in order:
            tout[u] = tin[u] + size[u] - 1

        # difference array whose prefix sums are the root distances
        diff = [0] * (n + 2)
        for u in order:
            if u != 1:
                diff[tin[u]] += wpar[u]
                diff[tout[u] + 1] -= wpar[u]
        bit = [0] * (n + 2)                   # O(n) Fenwick construction
        for i in range(1, n + 2):
            bit[i] += diff[i - 1]
            j = i + (i & -i)
            if j <= n + 1:
                bit[j] += bit[i]

        def add(i, delta):
            i += 1
            while i <= n + 1:
                bit[i] += delta
                i += i & -i

        def prefix(i):
            i += 1
            s = 0
            while i > 0:
                s += bit[i]
                i -= i & -i
            return s

        res = []
        for q in queries:
            if q[0] == 1:
                u, v, w = q[1], q[2], q[3]
                c = v if par[v] == u else u
                delta = w - wpar[c]
                if delta:
                    wpar[c] = w
                    add(tin[c], delta)
                    add(tout[c] + 1, -delta)
            else:
                res.append(prefix(tin[q[1]]))
        return res
