"""

685. Redundant Connection II
Hard

In this problem, a rooted tree is a directed graph such that, there is exactly one node
(the root) for which all other nodes are descendants of this node, plus every node has
exactly one parent, except for the root node which has no parents.

The given input is a directed graph that started as a rooted tree with n nodes (with
distinct values from 1 to n), with one additional directed edge added. The added edge has
two different vertices chosen from 1 to n, and was not an edge that already existed.

The resulting graph is given as a 2D-array of edges. Each element of edges is a pair
[ui, vi] that represents a directed edge connecting nodes ui and vi, where ui is a parent
of child vi.

Return an edge that can be removed so that the resulting graph is a rooted tree of n nodes.
If there are multiple answers, return the answer that occurs last in the given 2D-array.

Example 1:

Input: edges = [[1,2],[1,3],[2,3]]
Output: [2,3]

Example 2:

Input: edges = [[1,2],[2,3],[3,4],[4,1],[1,5]]
Output: [4,1]

Constraints:

n == edges.length
3 <= n <= 1000
edges[i].length == 2
1 <= ui, vi <= n
ui != vi

"""

# V0
# IDEA : CASE ANALYSIS + UNION FIND
#
#   Adding one edge to a rooted tree breaks it in exactly one of three ways:
#
#   Case A -- some node ends up with TWO parents, and there is no cycle.
#             Removing the later of the two incoming edges fixes it.
#   Case B -- no node has two parents, so the extra edge closed a directed CYCLE.
#             Remove the edge that closes the cycle (the last one seen in a
#             union-find scan).
#   Case C -- a node has two parents AND there is a cycle. Then the cycle must
#             run through the FIRST of the two incoming edges (otherwise dropping
#             the second would already fix everything), so remove that first one.
#
#   Implementation:
#     pass 1 -- find a node with two parents; remember both incoming edges as
#               cand_first (earlier) and cand_second (later).
#     pass 2 -- union everything EXCEPT cand_second.
#               * cycle detected -> Case C if a two-parent node exists (return
#                 cand_first), else Case B (return the closing edge).
#               * no cycle       -> Case A (return cand_second).
#
# time = O(n * alpha(n))
# space = O(n)
class Solution(object):
    def findRedundantDirectedConnection(self, edges):
        n = len(edges)
        parent_of = [0] * (n + 1)
        cand_first = None    # earlier edge into the two-parent node
        cand_second = None   # later edge into the two-parent node

        # pass 1 : look for a node with two parents
        for u, v in edges:
            if parent_of[v] != 0:
                cand_first = [parent_of[v], v]
                cand_second = [u, v]
            else:
                parent_of[v] = u

        # pass 2 : union find, skipping cand_second
        uf = list(range(n + 1))

        def find(x):
            while uf[x] != x:
                uf[x] = uf[uf[x]]   # path compression (halving)
                x = uf[x]
            return x

        for u, v in edges:
            if cand_second is not None and u == cand_second[0] and v == cand_second[1]:
                continue
            ru, rv = find(u), find(v)
            if ru == rv:
                # a cycle exists even without cand_second
                if cand_first is not None:
                    return cand_first        # Case C
                return [u, v]                # Case B
            uf[rv] = ru

        return cand_second                   # Case A
