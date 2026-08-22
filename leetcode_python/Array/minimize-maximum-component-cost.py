"""

3613. Minimize Maximum Component Cost
Medium

You are given an undirected connected graph with n nodes labeled from 0 to n - 1
and a 2D integer array edges where edges[i] = [ui, vi, wi] denotes an undirected
edge between node ui and node vi with weight wi, and an integer k.

You are allowed to remove any number of edges from the graph such that the
resulting graph has at most k connected components.

The cost of a component is defined as the maximum edge weight in that component.
If a component has no edges, its cost is 0.

Return the minimum possible value of the maximum cost among all components after
such removals.


Example 1:

Input: n = 5, edges = [[0,1,4],[1,2,3],[1,3,2],[3,4,6]], k = 2
Output: 4
Explanation:
Remove the edge between nodes 3 and 4 (weight 6).
The resulting components have costs of 0 and 4, so the overall maximum cost is 4.

Example 2:

Input: n = 4, edges = [[0,1,5],[1,2,5],[2,3,5]], k = 1
Output: 5
Explanation:
No edge can be removed, since allowing only one component (k = 1) requires the
graph to stay fully connected.
That single component's cost equals its largest edge weight, which is 5.


Constraints:

1 <= n <= 5 * 10^4
0 <= edges.length <= 10^5
edges[i].length == 3
0 <= ui, vi < n
1 <= wi <= 10^6
1 <= k <= n
The input graph is connected.
"""

# V0
# IDEA : KRUSKAL — GROW FROM THE CHEAPEST EDGE UNTIL K COMPONENTS REMAIN
#
#   the cost we pay is the heaviest edge we choose to keep, so read the problem
#   the other way round: pick a budget w, keep only the edges of weight <= w,
#   and ask how many components that leaves. that count is non-increasing in w,
#   so the answer is the smallest budget whose count reaches k or fewer — and
#   keeping *every* edge within budget is never worse than keeping some, since
#   extra edges under the cap only merge components without raising the cost.
#
#   that makes a single kruskal sweep enough. add edges in ascending weight;
#   each one that merges two components drops the count by exactly one, so the
#   first merge that brings the count down to k is reached at the smallest
#   possible budget, and its weight is the answer.
#
#   the two degenerate ends fall out naturally: k == n lets every node stand
#   alone for cost 0, and an edgeless graph likewise costs 0.
#
# time = O(m * log(m) + (n + m) * alpha(n)), space = O(n)
class Solution(object):
    def minCost(self, n, edges, k):
        if k >= n:
            return 0

        parent = list(range(n))

        def find(x):
            root = x
            while parent[root] != root:
                root = parent[root]
            while parent[x] != root:
                parent[x], x = root, parent[x]
            return root

        cnt = n
        for u, v, w in sorted(edges, key=lambda e: e[2]):
            ru, rv = find(u), find(v)
            if ru != rv:
                parent[ru] = rv
                cnt -= 1
                if cnt <= k:
                    return w
        return 0


# V0-1
# IDEA : BINARY SEARCH THE BUDGET + DFS COMPONENT COUNT (NO UNION-FIND)
#
#   spell out the monotone predicate that V0 short-circuits: for a budget w,
#   keep every edge of weight <= w and count the connected components. that
#   count only shrinks as w grows, so binary search the sorted distinct weights
#   for the smallest w whose count is <= k.
#
#   the feasibility test here is a plain iterative DFS over a pre-built
#   adjacency list, skipping any edge heavier than the budget — no DSU at all,
#   which makes the "keep everything within budget" reading explicit.
#
# time = O(m log m + (n + m) log m), space = O(n + m)
class Solution(object):
    def minCost(self, n, edges, k):
        if k >= n:
            return 0

        adj = [[] for _ in range(n)]
        for u, v, w in edges:
            adj[u].append((v, w))
            adj[v].append((u, w))

        def components(limit):
            seen = [False] * n
            cnt = 0
            for s in range(n):
                if seen[s]:
                    continue
                cnt += 1
                if cnt > k:
                    return cnt          # already too fragmented
                seen[s] = True
                stack = [s]
                while stack:
                    x = stack.pop()
                    for y, w in adj[x]:
                        if w <= limit and not seen[y]:
                            seen[y] = True
                            stack.append(y)
            return cnt

        ws = sorted(set(w for _, _, w in edges))
        lo, hi = 0, len(ws) - 1
        while lo < hi:
            mid = (lo + hi) // 2
            if components(ws[mid]) <= k:
                hi = mid
            else:
                lo = mid + 1
        return ws[lo]


# V0-2
# IDEA : PRIM'S MST + PICK ITS (n - k)-TH SMALLEST EDGE
#
#   kruskal performs exactly n - 1 merges on a connected graph, in ascending
#   weight order, and those merging edges are an MST. after t merges the
#   component count is n - t, so reaching <= k components needs t >= n - k
#   merges — which means the answer is simply the (n - k)-th smallest edge of
#   ANY minimum spanning tree.
#
#   so the sort-and-union sweep can be replaced by building the MST any way we
#   like: grow it with Prim's heap from node 0, collect the n - 1 chosen
#   weights, sort them and read index n - k - 1.
#
#   k >= 1 and k < n here, so that index is always inside [0, n - 2].
#
# time = O(m log m), space = O(n + m)
import heapq
class Solution(object):
    def minCost(self, n, edges, k):
        if k >= n:
            return 0

        adj = [[] for _ in range(n)]
        for u, v, w in edges:
            adj[u].append((w, v))
            adj[v].append((w, u))

        seen = [False] * n
        seen[0] = True
        heap = list(adj[0])
        heapq.heapify(heap)
        mst = []
        while heap:
            w, x = heapq.heappop(heap)
            if seen[x]:
                continue
            seen[x] = True
            mst.append(w)
            for nw, y in adj[x]:
                if not seen[y]:
                    heapq.heappush(heap, (nw, y))

        mst.sort()
        return mst[n - k - 1]
