"""

3249. Count the Number of Good Nodes
Medium

There is an undirected tree with n nodes labeled from 0 to n - 1, and rooted at node 0. You are given a 2D integer array edges of length n - 1, where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree.

A node is good if all the subtrees rooted at its children have the same size.

Return the number of good nodes in the given tree.

A subtree of treeName is a tree consisting of a node in treeName and all of its descendants.


Example 1:

Input: edges = [[0,1],[0,2],[1,3],[1,4],[2,5],[2,6]]
Output: 7
Explanation:
All of the nodes of the given tree are good.

Example 2:

Input: edges = [[0,1],[1,2],[2,3],[3,4],[0,5],[1,6],[2,7],[3,8]]
Output: 6
Explanation:
There are 6 good nodes in the given tree.


Constraints:

2 <= n <= 10^5
edges.length == n - 1
edges[i].length == 2
0 <= edges[i][0], edges[i][1] <= n - 1
The input is generated such that edges represents a valid tree.

"""

# V0
# IDEA : POST-ORDER SUBTREE SIZES, THEN COMPARE EACH NODE'S CHILDREN
#
#   subtree sizes come from one bottom-up pass :
#       size[u] = 1 + sum of children sizes
#   and a node is good when all its children carry the SAME size. leaves have
#   no children, so they are vacuously good.
#
#   the traversal is iterative — a 10^5-node path would exceed python's
#   recursion limit. reversing the DFS discovery order gives a valid
#   post-order, since a parent always appears before its children.
#
# time = O(n), space = O(n)
class Solution(object):
    def countGoodNodes(self, edges):
        n = len(edges) + 1
        adj = [[] for _ in range(n)]
        for u, v in edges:
            adj[u].append(v)
            adj[v].append(u)

        parent = [-1] * n
        order = []
        stack = [0]
        seen = [False] * n
        seen[0] = True
        while stack:
            u = stack.pop()
            order.append(u)
            for v in adj[u]:
                if not seen[v]:
                    seen[v] = True
                    parent[v] = u
                    stack.append(v)

        size = [1] * n
        for u in reversed(order):
            if parent[u] != -1:
                size[parent[u]] += size[u]

        res = 0
        for u in range(n):
            first = None
            good = True
            for v in adj[u]:
                if v == parent[u]:
                    continue
                if first is None:
                    first = size[v]
                elif size[v] != first:
                    good = False
                    break
            if good:
                res += 1
        return res
