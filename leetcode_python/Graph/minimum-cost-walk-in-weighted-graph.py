"""

3108. Minimum Cost Walk in Weighted Graph
Hard

There is an undirected weighted graph with n vertices labeled from 0 to n - 1.

You are given the integer n and an array edges, where edges[i] = [ui, vi, wi] indicates that there is an edge between vertices ui and vi with a weight of wi.

A walk on a graph is a sequence of vertices and edges. The walk starts and ends with a vertex, and each edge connects the vertex that comes before it and the vertex that comes after it. It's important to note that a walk may visit the same edge or vertex more than once.

The cost of a walk starting at node u and ending at node v is defined as the bitwise AND of the weights of the edges traversed during the walk. In other words, if the sequence of edge weights encountered during the walk is w0, w1, w2, ..., wk, then the cost is calculated as w0 & w1 & w2 & ... & wk, where & denotes the bitwise AND operator.

You are also given a 2D array query, where query[i] = [si, ti]. For each query, you need to find the minimum cost of the walk starting at vertex si and ending at vertex ti. If there exists no such walk, the answer is -1.

Return the array answer, where answer[i] denotes the minimum cost of a walk for query i.


Example 1:

Input: n = 5, edges = [[0,1,7],[1,3,7],[1,2,1]], query = [[0,3],[3,4]]
Output: [1,-1]
Explanation:
To achieve the cost of 1 in the first query, we need to move on the following edges: 0->1 (weight 7), 1->2 (weight 1), 2->1 (weight 1), 1->3 (weight 7).
In the second query, there is no walk between nodes 3 and 4, so the answer is -1.

Example 2:

Input: n = 3, edges = [[0,2,7],[0,1,15],[1,2,6],[1,2,1]], query = [[1,2]]
Output: [0]
Explanation:
To achieve the cost of 0 in the first query, we need to move on the following edges: 1->2 (weight 1), 2->1 (weight 6).


Constraints:

2 <= n <= 10^5
0 <= edges.length <= 10^5
edges[i].length == 3
0 <= ui, vi <= n - 1
ui != vi
0 <= wi <= 10^5
1 <= query.length <= 10^5
query[i].length == 2
0 <= si, ti <= n - 1

"""

# V0
# IDEA : A WALK MAY REUSE EDGES — SO IT CAN AND *EVERY* EDGE OF ITS COMPONENT
#
#   ANDing more weights can only clear bits, never set them, so the cheapest
#   walk wants to traverse as many distinct edges as possible. and since a
#   walk is free to repeat edges, it can detour down any edge of the
#   connected component and come straight back.
#
#   therefore the answer for a reachable pair is exactly the AND of ALL edge
#   weights in their component — no path finding required :
#       union-find the components, then fold each edge's weight into its
#       component's accumulator.
#
#   s == t answers 0 (an empty walk needs no edges), and different components
#   answer -1.
#
# time = O((n + E + Q) * alpha), space = O(n)
class Solution(object):
    def minimumCost(self, n, edges, query):
        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        for u, v, _ in edges:
            ru, rv = find(u), find(v)
            if ru != rv:
                parent[ru] = rv

        ALL = -1                                # identity for AND (all bits set)
        acc = [ALL] * n
        for u, _, w in edges:
            r = find(u)
            acc[r] &= w

        res = []
        for s, t in query:
            if s == t:
                res.append(0)
            elif find(s) != find(t):
                res.append(-1)
            else:
                res.append(acc[find(s)])
        return res
