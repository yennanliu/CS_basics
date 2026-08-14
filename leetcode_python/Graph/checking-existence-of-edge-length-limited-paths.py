"""

1697. Checking Existence of Edge Length Limited Paths
Hard

An undirected graph of n nodes is defined by edgeList, where edgeList[i] = [ui, vi, disi] denotes an
edge between nodes ui and vi with distance disi. Note that there may be multiple edges between two
nodes.

Given an array queries, where queries[j] = [pj, qj, limitj], your task is to determine for each
queries[j] whether there is a path between pj and qj such that each edge on the path has a distance
strictly less than limitj.

Return a boolean array answer, where answer.length == queries.length and the jth value of answer is
true if there is a path for queries[j] is true, and false otherwise.


Example 1:

Input: n = 3, edgeList = [[0,1,2],[1,2,4],[2,0,8],[1,0,16]], queries = [[0,1,2],[0,2,5]]
Output: [false,true]
Explanation: Note that there are two overlapping edges between 0 and 1 with distances 2 and 16.
For the first query, between 0 and 1 there is no path where each distance is less than 2, thus we
return false for this query.
For the second query, there is a path (0 -> 1 -> 2) of two edges with distances less than 5, thus we
return true for this query.

Example 2:

Input: n = 5, edgeList = [[0,1,10],[1,2,5],[2,3,9],[3,4,13]], queries = [[0,4,14],[1,4,13]]
Output: [true,false]


Constraints:

2 <= n <= 10^5
1 <= edgeList.length, queries.length <= 10^5
edgeList[i].length == 3
queries[j].length == 3
0 <= ui, vi, pj, qj <= n - 1
ui != vi
pj != qj
1 <= disi, limitj <= 10^9
There may be multiple edges between two nodes.

"""

# V0
# IDEA : OFFLINE QUERIES + UNION-FIND (sort both sides by weight, add edges once)
#
#   a query (p, q, limit) is true iff p and q are connected in the subgraph made
#   of the edges with weight < limit. those subgraphs are NESTED (increasing limit
#   only ADDS edges), so process the queries offline in increasing `limit` and
#   keep a union-find that we only ever grow -- each edge is unioned exactly once.
#
#   NOTE : the queries must be answered in their ORIGINAL order, so carry the
#          original index along when sorting.
#   NOTE : strict "< limit" -> the edge pointer advances while weight < limit.
#
# time = O((E + Q) log(E + Q)) dominated by the two sorts, space = O(n + Q)
class Solution(object):
    def distanceLimitedPathsExist(self, n, edgeList, queries):
        parent = list(range(n))
        rank = [0] * n

        def find(x):
            # iterative path compression (n up to 1e5)
            root = x
            while parent[root] != root:
                root = parent[root]
            while parent[x] != root:
                parent[x], x = root, parent[x]
            return root

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra == rb:
                return
            if rank[ra] < rank[rb]:
                ra, rb = rb, ra
            parent[rb] = ra
            if rank[ra] == rank[rb]:
                rank[ra] += 1

        edges = sorted(edgeList, key=lambda e: e[2])
        order = sorted(range(len(queries)), key=lambda i: queries[i][2])

        res = [False] * len(queries)
        j = 0
        for idx in order:
            p, q, limit = queries[idx]
            while j < len(edges) and edges[j][2] < limit:
                union(edges[j][0], edges[j][1])
                j += 1
            res[idx] = find(p) == find(q)
        return res
