"""

2378. Choose Edges to Maximize Score in a Tree
Medium
(premium / locked problem)

You are given a weighted rooted tree consisting of n nodes represented as a 0-indexed 2D integer array edges of size n where edges[i] = [par_i, weight_i] represents the parent node of node i (i.e., the root is node 0) and the weight of the edge between node i and its parent. Since the root does not have a parent, you have edges[0] = [-1, -1].

Choose some edges from the tree such that no two chosen edges are adjacent and the sum of the weights of the chosen edges is maximized.

Return the maximum sum of the chosen edges.

Note:

You are allowed to not choose any edges in the tree, the sum of weights in this case will be 0.
Two edges are adjacent if they share a common node.


Example 1:

Input: edges = [[-1,-1],[0,5],[0,10],[2,6],[2,4]]
Output: 11
Explanation: The above diagram shows the edges that we have to choose colored in red.
The total score is 5 + 6 = 11.
It can be shown that no better score can be obtained.

Example 2:

Input: edges = [[-1,-1],[0,5],[0,-6],[0,7]]
Output: 7
Explanation: We choose the edge with weight 7.
Note that we cannot choose more than one edge because all edges are adjacent to each other.


Constraints:

n == edges.length
1 <= n <= 10^5
edges[i].length == 2
par_0 == weight_0 == -1
0 <= par_i <= n - 1 for all i > 0.
par_i != i
-10^6 <= weight_i <= 10^6
It can be shown that the answer is a valid integer.

"""

# V0
# IDEA : CLASSIC TREE MATCHING DP — TWO STATES PER NODE
#
#   dp[u][0] = best score in u's subtree with u NOT covered by a chosen edge
#   dp[u][1] = best score in u's subtree with u covered by an edge to one of
#              its children
#
#   with base = sum over children v of max(dp[v][0], dp[v][1]) :
#       dp[u][0] = base
#       dp[u][1] = base + max over v of ( w(u,v) + dp[v][0]
#                                         - max(dp[v][0], dp[v][1]) )
#   the bracket is the DELTA of switching child v from "free choice" to
#   "matched with u", so only the single best child needs picking.
#
#   negative weights are simply never chosen, since dp[u][1] only wins when
#   its delta is positive.
#
#   NOTE : the traversal is ITERATIVE (reverse BFS order) — n reaches 10^5
#          and the tree can be a path.
#
"""

DP def
    (tree matching DP - no two chosen edges may share a node)

    dp[u][0]: best score in u's subtree with u NOT covered by a chosen edge

    dp[u][1]: best score in u's subtree with u COVERED by an edge to a child

DP eq

     base = sum over children v of max(dp[v][0], dp[v][1])

     dp[u][0] = base

     dp[u][1] = base + max over children v of
                   ( w(u,v) + dp[v][0] - max(dp[v][0], dp[v][1]) )


    -> e.g. the bracket is the DELTA of switching child v from
              "free choice" to "matched with u", so only the single
              BEST child needs picking

     negative weights are never chosen (dp[u][1] only wins on a positive delta)

     ans = max(dp[root][0], dp[root][1])

"""
# time = O(n), space = O(n)
class Solution(object):
    def maxScore(self, edges):
        n = len(edges)
        children = [[] for _ in range(n)]
        for i in range(1, n):
            par, w = edges[i]
            children[par].append((i, w))

        # BFS order from the root; reversed, it processes children first
        order = [0]
        qi = 0
        while qi < len(order):
            u = order[qi]
            qi += 1
            for v, _ in children[u]:
                order.append(v)

        free = [0] * n       # dp[u][0]
        matched = [0] * n    # dp[u][1]
        for u in reversed(order):
            base = 0
            best_delta = float('-inf')
            for v, w in children[u]:
                best_v = max(free[v], matched[v])
                base += best_v
                best_delta = max(best_delta, w + free[v] - best_v)
            free[u] = base
            matched[u] = base + best_delta if best_delta > float('-inf') else base

        return max(free[0], matched[0])
