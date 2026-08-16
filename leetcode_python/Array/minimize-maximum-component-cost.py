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
