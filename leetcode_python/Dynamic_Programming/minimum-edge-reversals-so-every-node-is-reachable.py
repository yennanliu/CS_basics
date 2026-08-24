"""

2858. Minimum Edge Reversals So Every Node Is Reachable
Hard

There is a simple directed graph with n nodes labeled from 0 to n - 1. The graph would form a tree if its edges were bi-directional.

You are given an integer n and a 2D integer array edges, where edges[i] = [ui, vi] represents a directed edge going from node ui to node vi.

An edge reversal changes the direction of an edge, i.e., a directed edge going from node ui to node vi becomes a directed edge going from node vi to node ui.

For every node i in the range [0, n - 1], your task is to independently calculate the minimum number of edge reversals required so it is possible to reach any other node starting from node i through a sequence of directed edges.

Return an integer array answer, where answer[i] is the minimum number of edge reversals required so it is possible to reach any other node starting from node i through a sequence of directed edges.


Example 1:

Input: n = 4, edges = [[2,0],[2,1],[1,3]]
Output: [1,1,0,2]
Explanation: The image above shows the graph formed by the edges.
For node 0: after reversing the edge [2,0], it is possible to reach any other node starting from node 0.
So, answer[0] = 1.
For node 1: after reversing the edge [2,1], it is possible to reach any other node starting from node 1.
So, answer[1] = 1.
For node 2: it is already possible to reach any other node starting from node 2.
So, answer[2] = 0.
For node 3: after reversing the edges [1,3] and [2,1], it is possible to reach any other node starting from node 3.
So, answer[3] = 2.

Example 2:

Input: n = 3, edges = [[1,2],[2,0]]
Output: [2,0,1]
Explanation: The image above shows the graph formed by the edges.
For node 0: after reversing the edges [2,0] and [1,2], it is possible to reach any other node starting from node 0.
So, answer[0] = 2.
For node 1: it is already possible to reach any other node starting from node 1.
So, answer[1] = 0.
For node 2: after reversing the edge [1, 2], it is possible to reach any other node starting from node 2.
So, answer[2] = 1.


Constraints:

2 <= n <= 10^5
edges.length == n - 1
edges[i].length == 2
0 <= ui == edges[i][0] < n
0 <= vi == edges[i][1] < n
ui != vi
The input is generated such that if the edges were bi-directional, the graph would be a tree.

"""

# V0
# IDEA : RE-ROOTING DP (TREE DP) ON THE UNDIRECTED TREE
#
#   Treat every directed edge as an undirected edge carrying a cost:
#     - walking it in its natural direction  (u -> v) costs 0
#     - walking it against its direction     (v -> u) costs 1  (we must reverse it)
#
#   Because the underlying graph is a tree, "reach every other node from i"
#   means "orient every edge away from i", so answer[i] is simply the number of
#   edges that point *towards* i on the unique root-to-node paths — i.e. the sum
#   of walking costs from i to all other nodes... which for a tree collapses to a
#   single count per edge.
#
#   Step 1 : root the tree at 0 and count how many edges must be flipped -> ans[0].
#   Step 2 : re-root. Moving the root from u to an adjacent node v across an edge
#            whose walking cost (as seen from u) is c:
#              - c == 0 (edge u->v) : from v that edge now points backwards -> +1
#              - c == 1 (edge v->u) : from v that edge is already fine       -> -1
#            so  ans[v] = ans[u] + 1 - 2 * c
#
#   NOTE : n can be 10^5, so both traversals are written ITERATIVELY (an explicit
#          stack) — a recursive DFS would blow Python's recursion limit on a path
#          shaped tree.
#
"""

DP def
    (RE-ROOTING tree DP)

    treat every directed edge as undirected with a walking cost:
        along its direction  (u -> v) costs 0
        against it           (v -> u) costs 1   (it must be reversed)

    ans[i]: number of edges that must be flipped so that EVERY node is

            reachable from i  (= orient every edge away from i)

DP eq

     step 1) root at 0 and count the edges needing a flip -> ans[0]

     step 2) RE-ROOT across an edge u -> v whose walking cost from u is c:

        ans[v] = ans[u] + 1 - 2 * c

           c == 0 (edge u->v): from v that edge now points backwards -> +1
           c == 1 (edge v->u): from v that edge is already fine      -> -1


    -> e.g. NOTE !!! n can be 10^5, so BOTH traversals are written
              ITERATIVELY - a recursive DFS would blow the recursion limit
              on a path-shaped tree

     ans = the whole array

"""
# time = O(n), space = O(n)
class Solution(object):
    def minEdgeReversals(self, n, edges):
        # adj[u] = list of (neighbour, cost of walking u -> neighbour)
        adj = [[] for _ in range(n)]
        for u, v in edges:
            adj[u].append((v, 0))
            adj[v].append((u, 1))

        ans = [0] * n

        # ---- step 1 : cost of rooting the tree at node 0 ----
        total = 0
        visited = [False] * n
        visited[0] = True
        stack = [0]
        while stack:
            u = stack.pop()
            for v, c in adj[u]:
                if not visited[v]:
                    visited[v] = True
                    total += c
                    stack.append(v)
        ans[0] = total

        # ---- step 2 : re-root from 0 to every other node ----
        visited = [False] * n
        visited[0] = True
        stack = [0]
        while stack:
            u = stack.pop()
            for v, c in adj[u]:
                if not visited[v]:
                    visited[v] = True
                    ans[v] = ans[u] + 1 - 2 * c
                    stack.append(v)

        return ans
