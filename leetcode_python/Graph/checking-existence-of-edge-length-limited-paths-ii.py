"""

1724. Checking Existence of Edge Length Limited Paths II
Hard

An undirected graph of n nodes is defined by edgeList, where edgeList[i] = [ui, vi, disi] denotes an edge between nodes ui and vi with distance disi. Note that there may be multiple edges between two nodes, and the graph may not be connected.

Implement the DistanceLimitedPathsExist class:

DistanceLimitedPathsExist(int n, int[][] edgeList) Initializes the class with an undirected graph.
boolean query(int p, int q, int limit) Returns true if there exists a path from p to q such that each edge on the path has a distance strictly less than limit, and otherwise false.


Example 1:

Input
["DistanceLimitedPathsExist", "query", "query", "query", "query"]
[[6, [[0, 2, 4], [0, 3, 2], [1, 2, 3], [2, 3, 1], [4, 5, 5]]], [2, 3, 2], [1, 3, 3], [2, 0, 3], [0, 5, 6]]
Output
[null, true, false, true, false]

Explanation
DistanceLimitedPathsExist distanceLimitedPathsExist = new DistanceLimitedPathsExist(6, [[0, 2, 4], [0, 3, 2], [1, 2, 3], [2, 3, 1], [4, 5, 5]]);
distanceLimitedPathsExist.query(2, 3, 2); // return true. There is an edge from 2 to 3 of distance 1, which is less than 2.
distanceLimitedPathsExist.query(1, 3, 3); // return false. There is no way to go from 1 to 3 with distances strictly less than 3.
distanceLimitedPathsExist.query(2, 0, 3); // return true. There is a way to go from 2 to 0 with distance < 3: travel from 2 to 3 to 0.
distanceLimitedPathsExist.query(0, 5, 6); // return false. There are no paths from 0 to 5.


Constraints:

2 <= n <= 10^4
0 <= edgeList.length <= 10^4
edgeList[i].length == 3
0 <= ui, vi, p, q <= n-1
ui != vi
p != q
1 <= disi, limit <= 10^9
At most 10^4 calls will be made to query.

"""

# V0
# IDEA : PERSISTENT (VERSIONED) UNION FIND - queries arrive online, so LC 1697's
#        offline sort-the-queries trick is unavailable
#
#   build the MST incrementally by processing edges in ASCENDING weight, and
#   STAMP each union with the weight that caused it:
#       version[x] = w   means "x got attached to its parent at weight w"
#
#   then a `find` restricted to limit just refuses to climb a link that was
#   created at weight >= limit:
#       while p[x] != x and version[x] < limit: x = p[x]
#
#   this reconstructs exactly the union-find state as it was right after all
#   edges of weight < limit had been merged - so
#       query(p, q, limit)  <=>  find(p, limit) == find(q, limit)
#
#   NOTE : union by RANK only, NO path compression - compression would rewrite
#          parents out of weight order and destroy the version history.
#   NOTE : "strictly less than limit" matches `version[x] < limit`.
#
# time = O(E log E) to build, O(log n) per query, space = O(n)
INF = float('inf')
class PersistentUnionFind(object):
    def __init__(self, n):
        self.p = list(range(n))
        self.rank = [0] * n
        self.version = [INF] * n

    def find(self, x, t=INF):
        while self.p[x] != x and self.version[x] < t:
            x = self.p[x]
        return x

    def union(self, a, b, t):
        pa, pb = self.find(a), self.find(b)
        if pa == pb:
            return False
        if self.rank[pa] > self.rank[pb]:
            self.version[pb] = t
            self.p[pb] = pa
        else:
            self.version[pa] = t
            self.p[pa] = pb
            if self.rank[pa] == self.rank[pb]:
                self.rank[pb] += 1
        return True


class DistanceLimitedPathsExist(object):
    def __init__(self, n, edgeList):
        self.uf = PersistentUnionFind(n)
        for u, v, w in sorted(edgeList, key=lambda e: e[2]):
            self.uf.union(u, v, w)

    def query(self, p, q, limit):
        return self.uf.find(p, limit) == self.uf.find(q, limit)


# Your DistanceLimitedPathsExist object will be instantiated and called as such:
# obj = DistanceLimitedPathsExist(n, edgeList)
# param_1 = obj.query(p,q,limit)
