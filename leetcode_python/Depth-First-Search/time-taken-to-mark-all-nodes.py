"""

3241. Time Taken to Mark All Nodes
Hard

There exists an undirected tree with n nodes numbered 0 to n - 1. You are given a 2D integer array edges of length n - 1, where edges[i] = [ui, vi] indicates that there is an edge between nodes ui and vi in the tree.

Initially, all nodes are unmarked. For each node i:

If i is odd, the node will get marked at time x if there is at least one node adjacent to it which was marked at time x - 1.
If i is even, the node will get marked at time x if there is at least one node adjacent to it which was marked at time x - 2.

Return an array times where times[i] is the time when all nodes get marked in the tree, if you mark node i at time t = 0.

Note that the answer for each times[i] is independent, i.e. when you mark node i all other nodes are unmarked.


Example 1:

Input: edges = [[0,1],[0,2]]
Output: [2,4,3]
Explanation:
For i = 0:
    Node 1 is marked at t = 1, and Node 2 at t = 2.
For i = 1:
    Node 0 is marked at t = 2, and Node 2 at t = 4.
For i = 2:
    Node 0 is marked at t = 2, and Node 1 at t = 3.

Example 2:

Input: edges = [[0,1]]
Output: [1,2]
Explanation:
For i = 0:
    Node 1 is marked at t = 1.
For i = 1:
    Node 0 is marked at t = 2.

Example 3:

Input: edges = [[2,4],[0,1],[2,3],[0,2]]
Output: [4,6,3,5,5]
Explanation:


Constraints:

2 <= n <= 10^5
edges.length == n - 1
edges[i].length == 2
0 <= edges[i][0], edges[i][1] <= n - 1
The input is generated such that edges represents a valid tree.

"""

# V0
# IDEA : REROOTING — ONE DOWNWARD PASS, THEN PUSH THE ANSWER OUTWARDS
#
#   marking spreads along edges with a per-node delay : entering node v costs
#   1 if v is odd and 2 if it is even. so from a fixed root the finishing
#   time is the deepest weighted path, computed bottom-up as
#
#       down[u] = max over children v of (down[v] + cost(v))
#
#   answering for EVERY start would be n separate passes, so reroot instead :
#   carry `up[u]` — the best time reachable by leaving u through its parent —
#   and combine it with the children when descending. a node's contribution
#   to its own child must be excluded, which is why the TOP TWO child values
#   are kept: the best one for every child except the one that produced it,
#   which falls back to the runner-up.
#
#   both passes are iterative; a 10^5-node path would overflow python's
#   recursion.
#
# time = O(n), space = O(n)
class Solution(object):
    def timeTaken(self, edges):
        n = len(edges) + 1
        adj = [[] for _ in range(n)]
        for u, v in edges:
            adj[u].append(v)
            adj[v].append(u)

        def cost(v):
            return 1 if v % 2 else 2

        # iterative DFS order from root 0
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

        down = [0] * n
        for u in reversed(order):                 # children before parents
            p = parent[u]
            if p != -1:
                cand = down[u] + cost(u)
                if cand > down[p]:
                    down[p] = cand

        up = [0] * n
        res = [0] * n
        for u in order:                           # parents before children
            # the two best child contributions, so a child can be excluded
            b1 = b2 = 0
            for v in adj[u]:
                if v == parent[u]:
                    continue
                cand = down[v] + cost(v)
                if cand > b1:
                    b1, b2 = cand, b1
                elif cand > b2:
                    b2 = cand
            res[u] = max(up[u], b1)
            for v in adj[u]:
                if v == parent[u]:
                    continue
                other = b2 if (down[v] + cost(v)) == b1 else b1
                up[v] = max(up[u], other) + cost(u)
        return res
