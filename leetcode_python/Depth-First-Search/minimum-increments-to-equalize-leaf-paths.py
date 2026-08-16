"""

3593. Minimum Increments to Equalize Leaf Paths
Medium

You are given an integer n and an undirected tree rooted at node 0 with n nodes numbered from 0 to n - 1. This is represented by a 2D array edges of length n - 1, where edges[i] = [ui, vi] indicates an edge between nodes ui and vi.

Each node i has an associated cost given by cost[i], representing the cost to traverse that node.

The score of a path is defined as the sum of the costs of all nodes along the path.

Your goal is to make the scores of all root-to-leaf paths equal by increasing the cost of any number of nodes by any non-negative amount.

Return the minimum number of nodes whose cost must be increased to make all root-to-leaf path scores equal.


Example 1:

Input: n = 3, edges = [[0,1],[0,2]], cost = [2,1,3]
Output: 1
Explanation:
There are two root-to-leaf paths:
Path 0 -> 1 has score 2 + 1 = 3.
Path 0 -> 2 has score 2 + 3 = 5.
We can raise the cost of node 1 by 2, making all root-to-leaf path scores equal to 5. Thus, the answer is 1.

Example 2:

Input: n = 3, edges = [[0,1],[1,2]], cost = [5,1,4]
Output: 0
Explanation:
There is only one root-to-leaf path, so no operations are needed. Thus, the answer is 0.

Example 3:

Input: n = 5, edges = [[0,4],[0,1],[1,2],[1,3]], cost = [3,4,1,1,7]
Output: 1
Explanation:
There are three root-to-leaf paths:
Path 0 -> 4 has score 3 + 7 = 10.
Path 0 -> 1 -> 2 has score 3 + 4 + 1 = 8.
Path 0 -> 1 -> 3 has score 3 + 4 + 1 = 8.
We can raise the cost of node 1 by 2, making all root-to-leaf path scores equal to 10. Thus, the answer is 1.


Constraints:

2 <= n <= 10^5
edges.length == n - 1
edges[i] == [ui, vi]
0 <= ui, vi < n
cost.length == n
1 <= cost[i] <= 10^9
The input is generated such that edges represents a valid tree.

"""

# V0
# IDEA : BOTTOM-UP MAX, CHARGING ONE OPERATION PER SHORT CHILD
#
#   let f(u) be the largest score among the paths from u down to a leaf.
#   every child subtree must be lifted to that same f value, and a single
#   operation on the child's own node can absorb the whole shortfall at once
#   (the amount raised is unlimited). so a child costs one operation iff it
#   is strictly below the max — and lifting the child's node keeps all the
#   deeper paths inside that subtree equal to each other.
#
#   a node with 10^5 descendants would blow the recursion limit, so the
#   post-order traversal is done with an explicit stack.
#
# time = O(n), space = O(n)
class Solution(object):
    def minIncrease(self, n, edges, cost):
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
            for w in adj[u]:
                if not seen[w]:
                    seen[w] = True
                    parent[w] = u
                    stack.append(w)

        best = [0] * n          # max downward path score starting at the node
        ops = 0
        for u in reversed(order):
            mx = 0
            for w in adj[u]:
                if w != parent[u]:
                    if best[w] > mx:
                        mx = best[w]
            for w in adj[u]:
                if w != parent[u] and best[w] < mx:
                    ops += 1
            best[u] = cost[u] + mx
        return ops
