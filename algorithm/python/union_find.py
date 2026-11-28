#---------------------------------------------------------------
# UNION FIND (Disjoint Set Union)
#---------------------------------------------------------------
#
# Scope: quick union, and the two optimisations that make it almost
#        O(1). See quick_find.py for the eager version it improves on,
#        union_find_if_cyclic.py for cycle detection built on it, and
#        doc/cheatsheet/union_find.md for the LeetCode patterns.
#
# N items start out separate. Two operations arrive in any order:
#     union(x, y)       put x and y in the same group
#     connected(x, y)   are x and y in the same group?
#
# THE IDEA: every group is a TREE, stored as one array of parents.
#   parent[i] is the item above i; a ROOT is its own parent.
#   find(x) climbs to the root -- the root is the group's name.
#   union(x, y) hangs one root under the other: one write.
#
#     parent = [0, 0, 1, 3, 3]          0       3
#                                       |       |
#       find(2): 2 -> 1 -> 0            1       4
#       connected(2, 4)? roots 0 != 3   |
#       union(2, 4): parent[0] = 3      2
#
# THE PROBLEM with doing only that: trees can grow into long chains
# (union(0,1), union(1,2), union(2,3) ...), and then find is O(N).
#
# THE TWO FIXES (V1)
#   Union by rank     always hang the SHORTER tree under the taller
#                     one, so the height grows only when two equally
#                     tall trees meet -> height <= log N
#   Path compression  while find climbs, re-point every vertex on the
#                     way straight at the root, so the next find from
#                     any of them is one step
#
#       before find(2)        after find(2)
#            0                     0
#            |                    / \
#            1                   1   2
#            |
#            2
#
#                     find                    union
#     quick find      O(1)                    O(N)
#     quick union V0  O(N) worst (a chain)    O(N) worst
#     optimised   V1  ~O(1) amortised         ~O(1) amortised
#                     (inverse Ackermann: below 5 for any real N)
#
# Space : O(N)
#
# Used by: LC 323 Number of Connected Components, LC 547 Number of
#          Provinces, LC 684 Redundant Connection, LC 721 Accounts
#          Merge, Kruskal's minimum spanning tree.
#
# References:
#   - https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/UnionFind.java


# V0 : quick union -- parent array only, no optimisation
class UnionFind:
    """Minimal union-find: correct, but find can be O(N) on a chain."""

    def __init__(self, n):
        self.parent = list(range(n))     # everyone starts as their own root
        self.count = n                   # number of separate groups

    def find(self, x):
        """Climb parent links to the root of x's tree."""
        while x != self.parent[x]:
            x = self.parent[x]
        return x

    def union(self, x, y):
        """Merge the groups of x and y. Returns False if already together."""
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x == root_y:
            return False
        self.parent[root_x] = root_y     # hang x's whole tree under y's root
        self.count -= 1
        return True

    def connected(self, x, y):
        return self.find(x) == self.find(y)


# V1 : union by rank + path compression -- the one to write in an interview
class UnionFindOptimized:
    """Union-find with both optimisations: effectively O(1) per operation."""

    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n              # an upper bound on each root's tree height
        self.count = n

    def find(self, x):
        """Return the root of x, flattening the path on the way.

        Two passes: climb to the root, then walk the same path again
        pointing every vertex directly at it. (A loop rather than
        recursion, so a long chain cannot hit the recursion limit.)
        """
        root = x
        while root != self.parent[root]:
            root = self.parent[root]
        while x != root:                 # path compression
            next_x = self.parent[x]
            self.parent[x] = root
            x = next_x
        return root

    def union(self, x, y):
        """Merge the groups of x and y. Returns False if already together."""
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x == root_y:
            return False

        # union by rank: the shorter tree goes UNDER the taller one
        if self.rank[root_x] < self.rank[root_y]:
            root_x, root_y = root_y, root_x      # make root_x the taller
        self.parent[root_y] = root_x
        if self.rank[root_x] == self.rank[root_y]:
            self.rank[root_x] += 1               # equal heights: the result is one taller
        self.count -= 1
        return True

    def connected(self, x, y):
        return self.find(x) == self.find(y)


def count_components(n, edges):
    """LC 323: number of connected components of an undirected graph."""
    uf = UnionFindOptimized(n)
    for a, b in edges:
        uf.union(a, b)
    return uf.count


if __name__ == "__main__":
    for cls in (UnionFind, UnionFindOptimized):
        uf = cls(5)
        assert uf.count == 5
        assert uf.union(0, 1) is True
        assert uf.union(1, 2) is True
        assert uf.connected(0, 2)                # 0-1 and 1-2, so 0-2
        assert not uf.connected(0, 3)
        assert uf.count == 3                     # {0,1,2}, {3}, {4}
        assert uf.union(0, 2) is False           # already together: no change
        assert uf.count == 3

    # V0's weakness: unions in this order build a chain 0 -> 1 -> 2 -> 3 -> 4
    chain = UnionFind(5)
    for i in range(4):
        chain.union(i, i + 1)
    assert chain.parent == [1, 2, 3, 4, 4]       # find(0) climbs 4 links

    # the same unions with both fixes: nobody is more than one step from the root
    flat = UnionFindOptimized(5)
    for i in range(4):
        flat.union(i, i + 1)
    root = flat.find(0)
    assert all(flat.parent[i] == root for i in range(5))

    # path compression, seen directly: build a chain by hand, then find
    squash = UnionFindOptimized(4)
    squash.parent = [0, 0, 1, 2]                 # 3 -> 2 -> 1 -> 0
    assert squash.find(3) == 0
    assert squash.parent == [0, 0, 0, 0]         # everything now points at the root

    # LC 323's examples
    assert count_components(5, [[0, 1], [1, 2], [3, 4]]) == 2
    assert count_components(5, [[0, 1], [1, 2], [2, 3], [3, 4]]) == 1

    # 20000 items in one chain: the iterative find needs no deep recursion
    big = UnionFindOptimized(20000)
    for i in range(19999):
        big.union(i, i + 1)
    assert big.count == 1 and big.connected(0, 19999)

    # both versions always agree
    import random

    random.seed(1)
    simple = UnionFind(30)
    fast = UnionFindOptimized(30)
    for _ in range(200):
        a = random.randrange(30)
        b = random.randrange(30)
        assert simple.union(a, b) == fast.union(a, b)
        c = random.randrange(30)
        d = random.randrange(30)
        assert simple.connected(c, d) == fast.connected(c, d)
        assert simple.count == fast.count

    print("Success.")
