"""

1782. Count Pairs Of Nodes
Hard

You are given an undirected graph defined by an integer n, the number of nodes, and a 2D integer array edges, the edges in the graph, where edges[i] = [ui, vi] indicates that there is an undirected edge between ui and vi. You are also given an integer array queries.

Let incident(a, b) be defined as the number of edges that are connected to either node a or b.

The answer to the j^th query is the number of pairs of nodes (a, b) that satisfy both of the following conditions:

a < b
incident(a, b) > queries[j]

Return an array answers such that answers.length == queries.length and answers[j] is the answer of the j^th query.

Note that there can be multiple edges between the same two nodes.

Example 1:

Input: n = 4, edges = [[1,2],[2,4],[1,3],[2,3],[2,1]], queries = [2,3]
Output: [6,5]
Explanation: The calculations for incident(a, b) are shown in the table above.
The answers for each of the queries are as follows:
- answers[0] = 6. All the pairs have an incident(a, b) value greater than 2.
- answers[1] = 5. All the pairs except (3, 4) have an incident(a, b) value greater than 3.

Example 2:

Input: n = 5, edges = [[1,5],[1,5],[3,4],[2,5],[1,3],[5,1],[2,3],[2,5]], queries = [1,2,3,4,5]
Output: [10,10,9,8,6]

Constraints:

2 <= n <= 2 * 10^4
1 <= edges.length <= 10^5
1 <= ui, vi <= n
ui!= vi
1 <= queries.length <= 20
0 <= queries[j] < edges.length

"""

# V0
# IDEA : SORTED DEGREES + TWO POINTERS, THEN FIX UP THE SHARED EDGES
#
#   incident(a, b) = deg[a] + deg[b] - (number of edges between a and b),
#   because those edges were counted twice.
#   step 1 : ignore the shared edges. sort the degrees and count, with two
#            pointers, how many pairs satisfy deg[a] + deg[b] > q  -> O(n).
#   step 2 : only pairs that actually share an edge were over-counted, and
#            there are at most len(edges) distinct such pairs. for each of
#            them, if deg[a] + deg[b] > q but the true value
#            deg[a] + deg[b] - shared <= q, remove it from the count.
#   NOTE : there are at most 20 queries, so O(q * (n + m)) is fine.
#
# time = O(m + n log n + Q * (n + m)), space = O(n + m)
from collections import defaultdict
class Solution(object):
    def countPairs(self, n, edges, queries):
        deg = [0] * (n + 1)
        shared = defaultdict(int)
        for u, v in edges:
            deg[u] += 1
            deg[v] += 1
            if u > v:
                u, v = v, u
            shared[(u, v)] += 1

        s = sorted(deg[1:])

        res = []
        for q in queries:
            cnt = 0
            lo, hi = 0, n - 1
            while lo < hi:
                if s[lo] + s[hi] > q:
                    cnt += hi - lo
                    hi -= 1
                else:
                    lo += 1
            for (u, v), c in shared.items():
                if deg[u] + deg[v] > q and deg[u] + deg[v] - c <= q:
                    cnt -= 1
            res.append(cnt)
        return res
