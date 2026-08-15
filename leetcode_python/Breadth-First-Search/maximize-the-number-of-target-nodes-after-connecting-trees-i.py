"""

3372. Maximize the Number of Target Nodes After Connecting Trees I
Medium

There exist two undirected trees with n and m nodes, labeled from [0, n - 1] and [0, m - 1], respectively.

You are given two 2D integer arrays edges1 and edges2 of lengths n - 1 and m - 1, respectively, where edges1[i] = [a_i, b_i] indicates that there is an edge between nodes a_i and b_i in the first tree and edges2[i] = [u_i, v_i] indicates that there is an edge between nodes u_i and v_i in the second tree. You are also given an integer k.

Node u is target to node v if the number of edges on the path from u to v is less than or equal to k. Note that a node is always target to itself.

Return an array of n integers answer, where answer[i] is the maximum possible number of nodes target to node i of the first tree if you have to connect one node from the first tree to another node in the second tree.

Note that queries are independent from each other. That is, for every query you will remove the added edge before proceeding to the next query.


Example 1:

Input: edges1 = [[0,1],[0,2],[2,3],[2,4]], edges2 = [[0,1],[0,2],[0,3],[2,7],[1,4],[4,5],[4,6]], k = 2
Output: [9,7,9,8,8]
Explanation:
For i = 0, connect node 0 from the first tree to node 0 from the second tree.
For i = 1, connect node 1 from the first tree to node 0 from the second tree.
For i = 2, connect node 2 from the first tree to node 4 from the second tree.
For i = 3, connect node 3 from the first tree to node 4 from the second tree.
For i = 4, connect node 4 from the first tree to node 4 from the second tree.

Example 2:

Input: edges1 = [[0,1],[0,2],[0,3],[0,4]], edges2 = [[0,1],[1,2],[2,3]], k = 1
Output: [6,3,3,3,3]
Explanation:
For every i, connect node i of the first tree with any node of the second tree.


Constraints:

2 <= n, m <= 1000
edges1.length == n - 1
edges2.length == m - 1
edges1[i].length == edges2[i].length == 2
0 <= a_i, b_i < n
0 <= u_i, v_i < m
The input is generated such that edges1 and edges2 represent valid trees.
0 <= k <= 1000

"""

# V0
# IDEA : COUNT WITHIN k IN TREE 1, WITHIN k-1 IN TREE 2, AND TAKE THE BEST BRIDGE
#
#   the added edge costs one step, so a node of the second tree is target to
#   i only if it lies within k - 1 of whichever node the bridge attaches to.
#   that choice is free and independent of i, so the second tree contributes
#   its BEST such count, the same constant for every i.
#
#       answer[i] = (nodes within k of i in tree 1)
#                 + max over j of (nodes within k-1 of j in tree 2)
#
#   k = 0 makes the bridge useless, which falls out since "within -1" is 0.
#
#   n, m <= 1000, so a BFS from every node in both trees is affordable.
#
# time = O(n^2 + m^2), space = O(n + m)
from collections import deque


class Solution(object):
    def maxTargetNodes(self, edges1, edges2, k):

        def build(edges):
            n = len(edges) + 1
            adj = [[] for _ in range(n)]
            for a, b in edges:
                adj[a].append(b)
                adj[b].append(a)
            return n, adj

        def counts_within(n, adj, limit):
            res = [0] * n
            if limit < 0:
                return res
            for s in range(n):
                dist = [-1] * n
                dist[s] = 0
                q = deque([s])
                c = 0
                while q:
                    u = q.popleft()
                    if dist[u] > limit:
                        continue
                    c += 1
                    for v in adj[u]:
                        if dist[v] == -1:
                            dist[v] = dist[u] + 1
                            if dist[v] <= limit:
                                q.append(v)
                res[s] = c
            return res

        n1, adj1 = build(edges1)
        n2, adj2 = build(edges2)

        near1 = counts_within(n1, adj1, k)
        near2 = counts_within(n2, adj2, k - 1)
        bonus = max(near2) if near2 else 0
        return [c + bonus for c in near1]
