"""

3373. Maximize the Number of Target Nodes After Connecting Trees II
Hard

There exist two undirected trees with n and m nodes, labeled from [0, n - 1] and [0, m - 1], respectively.

You are given two 2D integer arrays edges1 and edges2 of lengths n - 1 and m - 1, respectively, where edges1[i] = [a_i, b_i] indicates that there is an edge between nodes a_i and b_i in the first tree and edges2[i] = [u_i, v_i] indicates that there is an edge between nodes u_i and v_i in the second tree.

Node u is target to node v if the number of edges on the path from u to v is even. Note that a node is always target to itself.

Return an array of n integers answer, where answer[i] is the maximum possible number of nodes that are target to node i of the first tree if you had to connect one node from the first tree to another node in the second tree.

Note that queries are independent from each other. That is, for every query you will remove the added edge before proceeding to the next query.


Example 1:

Input: edges1 = [[0,1],[0,2],[2,3],[2,4]], edges2 = [[0,1],[0,2],[0,3],[2,7],[1,4],[4,5],[4,6]]
Output: [8,7,7,8,8]
Explanation:
For i = 0, connect node 0 from the first tree to node 0 from the second tree.
For i = 1, connect node 1 from the first tree to node 4 from the second tree.
For i = 2, connect node 2 from the first tree to node 7 from the second tree.
For i = 3, connect node 3 from the first tree to node 0 from the second tree.
For i = 4, connect node 4 from the first tree to node 4 from the second tree.

Example 2:

Input: edges1 = [[0,1],[0,2],[0,3],[0,4]], edges2 = [[0,1],[1,2],[2,3]]
Output: [3,6,6,6,6]
Explanation:
For every i, connect node i of the first tree with any node of the second tree.


Constraints:

2 <= n, m <= 10^5
edges1.length == n - 1
edges2.length == m - 1
edges1[i].length == edges2[i].length == 2
0 <= a_i, b_i < n
0 <= u_i, v_i < m
The input is generated such that edges1 and edges2 represent valid trees.

"""

# V0
# IDEA : "EVEN DISTANCE" IS A 2-COLOURING — SO COUNTING COLLAPSES TO PARITIES
#
#   in a tree the path between two nodes is unique, and its length is even
#   exactly when the two nodes share the same colour in the bipartite
#   2-colouring. so inside tree 1, node i sees all nodes of its own colour.
#
#   crossing the bridge costs one edge, which flips the parity. so from i the
#   reachable-even nodes of tree 2 are those at ODD distance from the bridge
#   endpoint — and since the endpoint is ours to choose, the contribution is
#   the larger of tree 2's two colour classes, the same constant for every i.
#
#       answer[i] = (size of i's colour class in tree 1) + max(colour classes of tree 2)
#
#   two BFS colourings settle it, which is what makes 10^5 nodes fine where
#   LC 3372's per-node BFS would not be.
#
# time = O(n + m), space = O(n + m)
from collections import deque


class Solution(object):
    def maxTargetNodes(self, edges1, edges2):

        def colour(edges):
            n = len(edges) + 1
            adj = [[] for _ in range(n)]
            for a, b in edges:
                adj[a].append(b)
                adj[b].append(a)
            col = [-1] * n
            col[0] = 0
            q = deque([0])
            while q:
                u = q.popleft()
                for v in adj[u]:
                    if col[v] == -1:
                        col[v] = col[u] ^ 1
                        q.append(v)
            return col

        col1 = colour(edges1)
        col2 = colour(edges2)

        size1 = [col1.count(0), col1.count(1)]
        bonus = max(col2.count(0), col2.count(1))
        return [size1[c] + bonus for c in col1]
