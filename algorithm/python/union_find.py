#---------------------------------------------------------------
# UNION FIND (Disjoint Set Union)
#---------------------------------------------------------------
#
# Maintain a partition of {0..n-1} into disjoint sets with two ops:
#   find(x)     -> representative ("root") of x's set
#   union(x, y) -> merge the sets containing x and y
#
# References:
#   - LC 323. Number of Connected Components in an Undirected Graph
#   - https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/UnionFind.java


# V0 : minimal (parent array + connected-component count)
# find walks parent pointers up to the root; no path compression.
class UnionFind:
    def __init__(self, n):
        self.count = n                       # number of disjoint sets
        self.parent = [i for i in range(n)]

    def find(self, x):
        while x != self.parent[x]:
            x = self.parent[x]
        return x

    def union(self, x, y):
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x == root_y:
            return
        self.parent[root_x] = root_y
        self.count -= 1

    def connected(self, x, y):
        return self.find(x) == self.find(y)


# V1 : union by rank + path compression (near O(1) amortized per op)
class UnionFindOptimized:
    def __init__(self, n):
        self.parent = [i for i in range(n)]
        self.rank = [0] * n
        self.count = n

    def find(self, x):
        # path compression: point every node on the path straight to root
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x == root_y:
            return
        # attach the shorter tree under the taller one
        if self.rank[root_x] < self.rank[root_y]:
            root_x, root_y = root_y, root_x
        self.parent[root_y] = root_x
        if self.rank[root_x] == self.rank[root_y]:
            self.rank[root_x] += 1
        self.count -= 1

    def connected(self, x, y):
        return self.find(x) == self.find(y)


if __name__ == "__main__":
    uf = UnionFind(5)
    uf.union(0, 1)
    uf.union(1, 2)
    assert uf.connected(0, 2)
    assert not uf.connected(0, 3)
    assert uf.count == 3                 # {0,1,2}, {3}, {4}

    uf2 = UnionFindOptimized(5)
    uf2.union(0, 1)
    uf2.union(3, 4)
    assert uf2.connected(0, 1)
    assert not uf2.connected(0, 3)
    assert uf2.count == 3
    print("Success.")
