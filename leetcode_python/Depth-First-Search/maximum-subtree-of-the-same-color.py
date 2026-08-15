"""

3004. Maximum Subtree of the Same Color
Medium
🔒 (premium)

You are given a 2D integer array edges representing a tree with n nodes, numbered from 0 to n - 1, rooted at node 0, where edges[i] = [ui, vi] means there is an edge between the nodes vi and ui.

You are also given a 0-indexed integer array colors of size n, where colors[i] is the color assigned to node i.

We want to find a node v such that every node in the subtree of v has the same color.

Return the size of such subtree with the maximum number of nodes possible.


Example 1:

Input: edges = [[0,1],[0,2],[0,3]], colors = [1,1,2,3]
Output: 1
Explanation: Each color is represented as: 1 -> Red, 2 -> Green, 3 -> Blue.
We can see that the subtree rooted at node 0 has children with different colors.
Any other subtree is of the same color and has a size of 1.
Hence, we return 1.

Example 2:

Input: edges = [[0,1],[0,2],[0,3]], colors = [1,1,1,1]
Output: 4
Explanation: The whole tree has the same color, and the subtree rooted at node 0 has the most number of nodes which is 4.
Hence, we return 4.

Example 3:

Input: edges = [[0,1],[0,2],[2,3],[2,4]], colors = [1,2,3,3,3]
Output: 3
Explanation: Each color is represented as: 1 -> Red, 2 -> Green, 3 -> Blue.
We can see that the subtree rooted at node 0 has children with different colors.
Any other subtree is of the same color, but the subtree rooted at node 2 has size 3 which is the maximum.
Hence, we return 3.


Constraints:

n == colors.length
1 <= n <= 5 * 10^4
edges.length == n - 1
edges[i].length == 2
0 <= ui, vi < n
1 <= colors[i] <= 10^5
The input is generated such that the graph represented by edges is a tree.

"""

# V0
# IDEA : POST-ORDER DFS — A SUBTREE IS UNIFORM IFF ALL ITS CHILDREN ARE
#
#   for node v :
#       size[v]    = 1 + sum of children sizes
#       uniform[v] = every child subtree is uniform AND every child's colour
#                    equals colors[v]
#
#   both are pure post-order facts, so one bottom-up pass computes them and
#   the answer is the largest size among the uniform nodes.
#
#   NOTE : the walk is ITERATIVE. n reaches 5*10^4 and the tree may be a
#          path, which would blow python's recursion limit.
#
# time = O(n), space = O(n)
class Solution(object):
    def maximumSubtreeSize(self, edges, colors):
        n = len(colors)
        adj = [[] for _ in range(n)]
        for u, v in edges:
            adj[u].append(v)
            adj[v].append(u)

        size = [1] * n
        uniform = [True] * n
        parent = [-1] * n

        # iterative DFS producing a post-order sequence
        order = []
        stack = [0]
        seen = [False] * n
        seen[0] = True
        while stack:
            u = stack.pop()
            order.append(u)
            for w in adj[u]:
                if not seen[w]:
                    seen[w] = True
                    parent[w] = u
                    stack.append(w)

        res = 1
        for u in reversed(order):            # children always settled first
            p = parent[u]
            if p != -1:
                size[p] += size[u]
                if not uniform[u] or colors[u] != colors[p]:
                    uniform[p] = False
            if uniform[u]:
                res = max(res, size[u])
        return res
