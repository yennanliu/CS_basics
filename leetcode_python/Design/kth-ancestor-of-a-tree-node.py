"""

1483. Kth Ancestor of a Tree Node
Hard

You are given a tree with n nodes numbered from 0 to n - 1 in the form of a parent array parent where parent[i] is the parent of ith node. The root of the tree is node 0. Find the kth ancestor of a given node.

The kth ancestor of a tree node is the kth node in the path from that node to the root node.

Implement the TreeAncestor class:

TreeAncestor(int n, int[] parent) Initializes the object with the number of nodes in the tree and the parent array.
int getKthAncestor(int node, int k) return the kth ancestor of the given node node. If there is no such ancestor, return -1.


Example 1:

Input
["TreeAncestor", "getKthAncestor", "getKthAncestor", "getKthAncestor"]
[[7, [-1, 0, 0, 1, 1, 2, 2]], [3, 1], [5, 2], [6, 3]]
Output
[null, 1, 0, -1]

Explanation
TreeAncestor treeAncestor = new TreeAncestor(7, [-1, 0, 0, 1, 1, 2, 2]);
treeAncestor.getKthAncestor(3, 1); // returns 1 which is the parent of 3
treeAncestor.getKthAncestor(5, 2); // returns 0 which is the grandparent of 5
treeAncestor.getKthAncestor(6, 3); // returns -1 because there is no such ancestor


Constraints:

1 <= k <= n <= 5 * 10^4
parent.length == n
parent[0] == -1
0 <= parent[i] < n for all 0 < i < n
0 <= node < n
There will be at most 5 * 10^4 queries.

"""

# V0
# IDEA : BINARY LIFTING (jump 2^j steps at a time)
#
#   up[j][v] = the 2^j-th ancestor of v, built from
#     up[j][v] = up[j-1][ up[j-1][v] ]
#   i.e. two jumps of 2^(j-1) make one jump of 2^j.
#   a query decomposes k into its binary digits and takes one jump per set
#   bit -> O(log n) hops instead of O(k) parent walks.
#   NOTE : -1 means "past the root"; once we land on it we stop, since a
#          missing ancestor stays missing.
#   n <= 5*10^4 < 2^16, so 17 levels cover every possible k.
#
# time = O(n log n) build, O(log n) per query
# space = O(n log n)
class TreeAncestor(object):
    LOG = 17

    def __init__(self, n, parent):
        self.up = [[-1] * n for _ in range(self.LOG)]
        self.up[0] = list(parent)
        for j in range(1, self.LOG):
            prev = self.up[j - 1]
            cur = self.up[j]
            for v in range(n):
                mid = prev[v]
                if mid != -1:
                    cur[v] = prev[mid]

    def getKthAncestor(self, node, k):
        for j in range(self.LOG):
            if node == -1:
                return -1
            if (k >> j) & 1:
                node = self.up[j][node]
        return node


# Your TreeAncestor object will be instantiated and called as such:
# obj = TreeAncestor(n, parent)
# param_1 = obj.getKthAncestor(node,k)
