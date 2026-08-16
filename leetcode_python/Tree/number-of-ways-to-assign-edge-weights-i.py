"""

3558. Number of Ways to Assign Edge Weights I
Medium

There is an undirected tree with n nodes labeled from 1 to n, rooted at node 1.
The tree is represented by a 2D integer array edges of length n - 1, where
edges[i] = [u_i, v_i] indicates that there is an edge between nodes u_i and v_i.

Initially, all edges have a weight of 0. You must assign each edge a weight of
either 1 or 2.

The cost of a path between any two nodes u and v is the total weight of all
edges in the path connecting them.

Select any one node x at the maximum depth. Return the number of ways to assign
edge weights in the path from node 1 to x such that its total cost is odd.

Since the answer may be large, return it modulo 10^9 + 7.

Note: Ignore all edges not in the path from node 1 to x.

Example 1:

Input: edges = [[1,2]]

Output: 1

Explanation:

The path from Node 1 to Node 2 consists of one edge (1 → 2).

Assigning weight 1 makes the cost odd, while 2 makes it even. Thus, the number
of valid assignments is 1.

Example 2:

Input: edges = [[1,2],[1,3],[3,4],[3,5]]

Output: 2

Explanation:

The maximum depth is 2, with nodes 4 and 5 at the same depth. Either node can be
selected for processing.

For example, the path from Node 1 to Node 4 consists of two edges (1 → 3 and 3 →
4).

Assigning weights (1,2) or (2,1) results in an odd cost. Thus, the number of
valid assignments is 2.

Constraints:

2 <= n <= 10^5

edges.length == n - 1

edges[i] == [u_i, v_i]

1 <= u_i, v_i <= n

edges represents a valid tree.

"""

# V0
# IDEA : ONLY THE PARITY MATTERS, SO EXACTLY HALF THE ASSIGNMENTS WORK
#
#   weights are 1 or 2, so an edge changes the parity of the path cost only
#   when it is given a 1.  the cost of the path is odd exactly when an odd
#   number of its edges got the 1.
#
#   for a path of L edges that count is the number of odd-sized subsets of L
#   items, which is 2^(L-1) -- half of all 2^L assignments, for every L >= 1.
#   an easy way to see it: fix the first L - 1 edges arbitrarily, and the last
#   edge is then forced to one of its two values.
#
#   so the whole problem reduces to finding L, the maximum depth of the tree,
#   which one BFS from the root delivers.  every deepest node gives the same L,
#   which is why the statement lets any of them be picked.
#
# time = O(n), space = O(n)
class Solution(object):
    def assignEdgeWeights(self, edges):
        MOD = 10 ** 9 + 7
        n = len(edges) + 1
        g = [[] for _ in range(n + 1)]
        for u, v in edges:
            g[u].append(v)
            g[v].append(u)

        depth = [0] * (n + 1)
        seen = [False] * (n + 1)
        seen[1] = True
        stack = [1]
        best = 0
        while stack:
            u = stack.pop()
            if depth[u] > best:
                best = depth[u]
            for v in g[u]:
                if not seen[v]:
                    seen[v] = True
                    depth[v] = depth[u] + 1
                    stack.append(v)
        return pow(2, best - 1, MOD) if best else 0
