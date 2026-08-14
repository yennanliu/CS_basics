"""

1719. Number Of Ways To Reconstruct A Tree
Hard

You are given an array pairs, where pairs[i] = [xi, yi], and:

There are no duplicates.
xi < yi

Let ways be the number of rooted trees that satisfy the following conditions:

The tree consists of nodes whose values appeared in pairs.
A pair [xi, yi] exists in pairs if and only if xi is an ancestor of yi or yi is an ancestor of xi.
Note: the tree does not have to be a binary tree.

Two ways are considered to be different if there is at least one node that has different parents in both ways.

Return:

0 if ways == 0
1 if ways == 1
2 if ways > 1

A rooted tree is a tree that has a single root node, and all edges are oriented to be outgoing from the root.

An ancestor of a node is any node on the path from the root to that node (excluding the node itself). The root has no ancestors.


Example 1:

Input: pairs = [[1,2],[2,3]]
Output: 1
Explanation: There is exactly one valid rooted tree, which is shown in the above figure.

Example 2:

Input: pairs = [[1,2],[2,3],[1,3]]
Output: 2
Explanation: There are multiple valid rooted trees. Three of them are shown in the above figures.

Example 3:

Input: pairs = [[1,2],[2,3],[2,4],[1,5]]
Output: 0
Explanation: There are no valid rooted trees.


Constraints:

1 <= pairs.length <= 10^5
1 <= xi < yi <= 500
The elements in pairs are unique.

"""

# V0
# IDEA : DEGREE ORDERING + ADJACENCY SUBSET CHECK
#
#   a pair (x, y) means one is an ancestor of the other, so adj(x) is exactly
#   {ancestors of x} + {descendants of x}. two structural facts follow:
#
#   1) if y is an ANCESTOR of x, then everything adjacent to x is also
#      adjacent to y  ->  adj(x) is a SUBSET of adj(y).
#      (put x itself in adj(x) and y in adj(y) so the subset test is clean.)
#   2) an ancestor always has degree >= its descendant.
#
#   so : sort the nodes by degree ASCENDING. for node x, its parent must be
#   the first neighbour appearing LATER in that order - call it y. verify
#   adj(x) <= adj(y); if it fails no tree exists -> 0.
#
#   if x has no such later neighbour it must be the root. more than one root
#   candidate -> the "tree" is disconnected -> 0.
#
#   ambiguity : deg(x) == deg(y) means adj(x) == adj(y), so x and y can swap
#   places (parent <-> child) -> more than one tree -> 2.
#
#   NOTE : node ids are <= 500, so the whole thing is tiny; the cost is
#          dominated by reading `pairs`.
#
# time = O(V^2 + E), space = O(V + E), V <= 500
class Solution(object):
    def checkWays(self, pairs):
        adj = {}
        for x, y in pairs:
            if x not in adj:
                adj[x] = set([x])
            if y not in adj:
                adj[y] = set([y])
            adj[x].add(y)
            adj[y].add(x)

        nodes = sorted(adj, key=lambda v: len(adj[v]))

        roots = 0
        multiple = False
        for i, x in enumerate(nodes):
            # first neighbour of x with >= degree -> the only possible parent
            parent = None
            for j in range(i + 1, len(nodes)):
                if nodes[j] in adj[x]:
                    parent = nodes[j]
                    break

            if parent is None:
                roots += 1
                continue

            if not adj[x] <= adj[parent]:
                return 0
            if len(adj[x]) == len(adj[parent]):
                multiple = True

        if roots > 1:
            return 0
        return 2 if multiple else 1
