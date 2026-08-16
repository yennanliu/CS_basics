"""

3600. Maximize Spanning Tree Stability with Upgrades
Hard

You are given an integer n, representing n nodes numbered from 0 to n - 1
and a list of edges, where edges[i] = [ui, vi, si, musti]:

ui and vi indicates an undirected edge between nodes ui and vi.
si is the strength of the edge.
musti is an integer (0 or 1). If musti == 1, the edge must be included in
the spanning tree. These edges cannot be upgraded.

You are also given an integer k, the maximum number of upgrades you can
perform. Each upgrade doubles the strength of an edge, and each eligible
edge (with musti == 0) can be upgraded at most once.

The stability of a spanning tree is defined as the minimum strength score
among all edges included in it.

Return the maximum possible stability of any valid spanning tree. If it is
impossible to connect all nodes, return -1.

Note: A spanning tree of a graph with n nodes is a subset of the edges that
connects all nodes together (i.e. the graph is connected) without forming
any cycles, and uses exactly n - 1 edges.


Example 1:

Input: n = 3, edges = [[0,1,2,1],[1,2,3,0]], k = 1
Output: 2
Explanation:
Edge [0,1] with strength = 2 must be included in the spanning tree.
Edge [1,2] is optional and can be upgraded from 3 to 6 using one upgrade.
The resulting spanning tree includes these two edges with strengths 2 and 6.
The minimum strength in the spanning tree is 2, which is the maximum
possible stability.

Example 2:

Input: n = 3, edges = [[0,1,4,0],[1,2,3,0],[0,2,1,0]], k = 2
Output: 6
Explanation:
Since all edges are optional and up to k = 2 upgrades are allowed.
Upgrade edges [0,1] from 4 to 8 and [1,2] from 3 to 6.
The resulting spanning tree includes these two edges with strengths 8 and 6.
The minimum strength in the tree is 6, which is the maximum possible
stability.

Example 3:

Input: n = 3, edges = [[0,1,1,1],[1,2,1,1],[2,0,1,1]], k = 0
Output: -1
Explanation:
All edges are mandatory and form a cycle, which violates the spanning tree
property of acyclicity. Thus, the answer is -1.


Constraints:

2 <= n <= 10^5
1 <= edges.length <= 10^5
edges[i] = [ui, vi, si, musti]
0 <= ui, vi < n
ui != vi
1 <= si <= 10^5
musti is either 0 or 1
0 <= k <= n
There are no duplicate edges.

"""

# V0
# IDEA : BINARY SEARCH THE STABILITY + UNION-FIND FEASIBILITY CHECK
#
#   stability is a min over the chosen edges, so the predicate "some valid
#   spanning tree has stability >= lim" is monotone in lim: a tree good for
#   lim is good for every smaller lim too. that is the hook for a binary
#   search — we only need to decide feasibility for a fixed lim.
#
#   for a fixed lim an edge is usable in two ways. it is free if its
#   strength already reaches lim, and it is upgradable if doubling gets it
#   there (2 * s >= lim) at the price of one of the k upgrades. so the
#   question becomes: can we span the graph using the free edges plus at
#   most k upgradable ones?
#
#   here the matroid greedy answers exactly: contract every free edge
#   first, and whatever number of components c survives forces exactly
#   c - 1 upgraded edges — no ordering of the upgrades can do better, and
#   any upgradable edge that still merges two components is as good as any
#   other. so "add all free edges, then spend upgrades on merging edges
#   until the graph is one component" is optimal, and feasibility is just
#   "did we end with one component while staying inside k".
#
#   two things bound the search. must-edges cannot be upgraded and are
#   forced into the tree, so the answer can never exceed the smallest
#   must-edge strength — that is the search's upper end, and it also means
#   every must-edge is automatically free at any lim we test, so the check
#   never wastes an upgrade on one. and the search is only meaningful if
#   the must-edges form a forest (a cycle among them makes a tree
#   impossible) and the whole graph is connected; both are rejected up
#   front with -1.
#
#   finally, a forest of must-edges inside a connected usable graph can
#   always be extended to a full spanning tree, so testing plain
#   connectivity of the usable edges is enough — we never have to build
#   the tree explicitly.
#
# time = O(m * alpha(n) * log(M)), space = O(n)
class Solution(object):
    def maxStability(self, n, edges, k):

        def find(p, x):
            root = x
            while p[root] != root:
                root = p[root]
            while p[x] != root:
                p[x], x = root, p[x]
            return root

        def union(p, rank, a, b):
            ra, rb = find(p, a), find(p, b)
            if ra == rb:
                return False
            if rank[ra] < rank[rb]:
                ra, rb = rb, ra
            p[rb] = ra
            if rank[ra] == rank[rb]:
                rank[ra] += 1
            return True

        # must-edges must form a forest, and the graph must be connected
        p = list(range(n))
        rank = [0] * n
        comps = n
        cap = float("inf")
        for u, v, s, must in edges:
            if must:
                if s < cap:
                    cap = s
                if not union(p, rank, u, v):
                    return -1
                comps -= 1
        for u, v, _, _ in edges:
            if union(p, rank, u, v):
                comps -= 1
        if comps > 1:
            return -1

        def feasible(lim):
            p = list(range(n))
            rank = [0] * n
            comps = n
            for u, v, s, _ in edges:
                if s >= lim and union(p, rank, u, v):
                    comps -= 1
            if comps == 1:
                return True
            left = k
            for u, v, s, _ in edges:
                if left == 0:
                    break
                if 2 * s >= lim and union(p, rank, u, v):
                    comps -= 1
                    left -= 1
                    if comps == 1:
                        return True
            return comps == 1

        # lim = 1 always works: the graph is connected and every s >= 1.
        # nothing can beat the strongest edge doubled, nor a must-edge.
        best = 2 * max(e[2] for e in edges)
        lo, hi = 1, min(cap, best)
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if feasible(mid):
                lo = mid
            else:
                hi = mid - 1
        return lo
