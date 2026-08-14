"""

1579. Remove Max Number of Edges to Keep Graph Fully Traversable
Hard

Alice and Bob have an undirected graph of n nodes and three types of edges:

Type 1: Can be traversed by Alice only.
Type 2: Can be traversed by Bob only.
Type 3: Can be traversed by both Alice and Bob.

Given an array edges where edges[i] = [typei, ui, vi] represents a bidirectional edge of type typei between nodes ui and vi, find the maximum number of edges you can remove so that after removing the edges, the graph can still be fully traversed by both Alice and Bob. The graph is fully traversed by Alice and Bob if starting from any node, they can reach all other nodes.

Return the maximum number of edges you can remove, or return -1 if Alice and Bob cannot fully traverse the graph.

Example 1:

Input: n = 4, edges = [[3,1,2],[3,2,3],[1,1,3],[1,2,4],[1,1,2],[2,3,4]]
Output: 2
Explanation: If we remove the 2 edges [1,1,2] and [1,1,3]. The graph will still be fully traversable by Alice and Bob. Removing any additional edge will not make it so. So the maximum number of edges we can remove is 2.

Example 2:

Input: n = 4, edges = [[3,1,2],[3,2,3],[1,1,4],[2,1,4]]
Output: 0
Explanation: Notice that removing any edge will not make the graph fully traversable by Alice and Bob.

Example 3:

Input: n = 4, edges = [[3,2,3],[1,1,2],[2,3,4]]
Output: -1
Explanation: In the current graph, Alice cannot reach node 4 from the other nodes. Likewise, Bob cannot reach 1. Therefore it's impossible to make the graph fully traversable.

Constraints:

1 <= n <= 10^5
1 <= edges.length <= min(10^5, 3 * n * (n - 1) / 2)
edges[i].length == 3
1 <= typei <= 3
1 <= ui < vi <= n
All tuples (typei, ui, vi) are distinct.

"""

# V0
# IDEA : UNION FIND x2 (add the shared type-3 edges first)
#
#   keep one DSU for Alice and one for Bob.
#   a type-3 edge helps BOTH, so it is strictly better than a private
#   edge -> process all type-3 edges first and keep the ones that merge
#   something. then top each DSU up with its own private edges.
#   an edge that merges nothing is redundant -> removable.
#   NOTE : if either DSU does not end with a single component the graph
#          cannot be fully traversed -> -1.
#
# time = O(E * alpha), space = O(n)
class Solution(object):
    def maxNumEdgesToRemove(self, n, edges):
        pa = list(range(n + 1))
        pb = list(range(n + 1))

        def find(p, x):
            while p[x] != x:
                p[x] = p[p[x]]
                x = p[x]
            return x

        ca = cb = n   # components (node 0 is unused)
        kept = 0
        for t, u, v in edges:
            if t != 3:
                continue
            ra, rb = find(pa, u), find(pa, v)
            if ra != rb:
                pa[ra] = rb
                ca -= 1
                rc, rd = find(pb, u), find(pb, v)
                pb[rc] = rd
                cb -= 1
                kept += 1

        for t, u, v in edges:
            if t == 1:
                ra, rb = find(pa, u), find(pa, v)
                if ra != rb:
                    pa[ra] = rb
                    ca -= 1
                    kept += 1
            elif t == 2:
                ra, rb = find(pb, u), find(pb, v)
                if ra != rb:
                    pb[ra] = rb
                    cb -= 1
                    kept += 1

        if ca != 1 or cb != 1:
            return -1
        return len(edges) - kept
