"""

3608. Minimum Time for K Connected Components
Medium

You are given an integer n and an undirected graph with n nodes labeled from 0
to n - 1. This is represented by a 2D array edges, where
edges[i] = [ui, vi, timei] indicates an undirected edge between nodes ui and vi
that can be removed at timei.

You are also given an integer k.

Initially, the graph may be connected or disconnected. Your task is to find the
minimum time t such that after removing all edges with time <= t, the graph
contains at least k connected components.

Return the minimum time t.

A connected component is a subgraph of a graph in which there exists a path
between any two vertices, and no vertex of the subgraph shares an edge with a
vertex outside of the subgraph.


Example 1:

Input: n = 2, edges = [[0,1,3]], k = 2
Output: 3
Explanation:
Initially, there is one connected component {0, 1}.
At time = 1 or 2, the graph remains unchanged.
At time = 3, edge [0, 1] is removed, resulting in k = 2 connected components
{0}, {1}. Thus, the answer is 3.

Example 2:

Input: n = 3, edges = [[0,1,2],[1,2,4]], k = 3
Output: 4
Explanation:
Initially, there is one connected component {0, 1, 2}.
At time = 2, edge [0, 1] is removed, resulting in two connected components {0},
{1, 2}.
At time = 4, edge [1, 2] is removed, resulting in k = 3 connected components
{0}, {1}, {2}. Thus, the answer is 4.

Example 3:

Input: n = 3, edges = [[0,2,5]], k = 2
Output: 0
Explanation:
Since there are already k = 2 disconnected components {1}, {0, 2}, no edge
removal is needed. Thus, the answer is 0.


Constraints:

1 <= n <= 10^5
0 <= edges.length <= 10^5
edges[i] = [ui, vi, timei]
0 <= ui, vi < n
ui != vi
1 <= timei <= 10^9
1 <= k <= n
There are no duplicate edges.
"""

# V0
# IDEA : REVERSE KRUSKAL — ADD EDGES BACK FROM THE LATEST TIME DOWN
#
#   surviving at threshold t means "time > t", so the surviving edge set only
#   ever grows as t shrinks, and therefore the component count f(t) is
#   monotonically non-decreasing in t. that monotonicity is what lets us look
#   for a single cutoff instead of testing every t.
#
#   deleting edges from a union-find is awkward, so run time backwards: sort
#   edges by time and re-insert them from the largest time downwards. right
#   after inserting the edge of time t we hold exactly the graph "time >= t",
#   and just before inserting it we hold a subset of "time > t".
#
#   so the first moment the live count drops below k, at edge time t, pins the
#   answer from both sides: f(t) >= (count before the insert) >= k, while every
#   smaller threshold keeps at least the edges we have now, hence gives a count
#   <= (count after the insert) < k. if the count never falls below k even with
#   every edge present, the graph already has k components and t = 0 works.
#
# time = O(m * log(m) + (n + m) * alpha(n)), space = O(n)
class Solution(object):
    def minTime(self, n, edges, k):
        parent = list(range(n))

        def find(x):
            root = x
            while parent[root] != root:
                root = parent[root]
            while parent[x] != root:
                parent[x], x = root, parent[x]
            return root

        cnt = n
        for u, v, t in sorted(edges, key=lambda e: -e[2]):
            ru, rv = find(u), find(v)
            if ru != rv:
                parent[ru] = rv
                cnt -= 1
                if cnt < k:
                    return t
        return 0


# V0-1
# IDEA : BINARY SEARCH ON THE ANSWER + UNION-FIND RECOUNT
#
#   f(t) = number of components after deleting every edge with time <= t is
#   monotonically non-decreasing in t (a bigger t deletes a superset), so the
#   predicate "f(t) >= k" is a step function and binary search applies.
#
#   only the distinct edge times (plus 0) can ever be the answer, because f
#   changes value only when t crosses one of them -- so binary search over the
#   sorted candidate list instead of over [0, 10^9].
#
#   each probe rebuilds a fresh union-find over the surviving edges and counts
#   components, which is the "check from scratch" counterpart of V0's single
#   incremental sweep.
#
# time = O(m * log(m) * alpha(n)), space = O(n + m)
class Solution(object):
    def minTime(self, n, edges, k):
        def components(limit):
            parent = list(range(n))

            def find(x):
                while parent[x] != x:
                    parent[x] = parent[parent[x]]
                    x = parent[x]
                return x

            cnt = n
            for u, v, t in edges:
                if t <= limit:
                    continue
                ru, rv = find(u), find(v)
                if ru != rv:
                    parent[ru] = rv
                    cnt -= 1
            return cnt

        cand = [0] + sorted(set(e[2] for e in edges))
        lo, hi = 0, len(cand) - 1
        while lo < hi:
            mid = (lo + hi) // 2
            if components(cand[mid]) >= k:
                hi = mid
            else:
                lo = mid + 1
        return cand[lo]


# V0-2
# IDEA : BINARY SEARCH ON THE ANSWER + ITERATIVE DFS COMPONENT COUNT
#
#   same monotone predicate as V0-1, but the component count is obtained by
#   plain graph traversal on an adjacency list instead of a disjoint-set
#   structure -- no path compression, no union by rank, just "walk every node
#   once and bump a counter each time a new walk starts".
#
#   the DFS is written with an explicit stack so a 10^5-node path graph cannot
#   blow Python's recursion limit.
#
# time = O(m * log(m) + (n + m) * log(m)), space = O(n + m)
class Solution(object):
    def minTime(self, n, edges, k):
        def components(limit):
            adj = [[] for _ in range(n)]
            for u, v, t in edges:
                if t > limit:
                    adj[u].append(v)
                    adj[v].append(u)
            seen = [False] * n
            cnt = 0
            for s in range(n):
                if seen[s]:
                    continue
                cnt += 1
                seen[s] = True
                stack = [s]
                while stack:
                    x = stack.pop()
                    for y in adj[x]:
                        if not seen[y]:
                            seen[y] = True
                            stack.append(y)
            return cnt

        cand = [0] + sorted(set(e[2] for e in edges))
        lo, hi = 0, len(cand) - 1
        while lo < hi:
            mid = (lo + hi) // 2
            if components(cand[mid]) >= k:
                hi = mid
            else:
                lo = mid + 1
        return cand[lo]
