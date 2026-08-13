"""

1026. Maximum Difference Between Node and Ancestor
Medium

Given the root of a binary tree, find the maximum value v for which there exist different nodes a and b where v = |a.val - b.val| and a is an ancestor of b.

A node a is an ancestor of b if either: any child of a is equal to b or any child of a is an ancestor of b.


Example 1:

Input: root = [8,3,10,1,6,null,14,null,null,4,7,13]
Output: 7
Explanation: We have various ancestor-node differences, some of which are given below :
|8 - 3| = 5
|3 - 7| = 4
|8 - 1| = 7
|10 - 13| = 3
Among all possible differences, the maximum value of 7 is obtained by |8 - 1| = 7.

Example 2:

Input: root = [1,null,2,null,0,3]
Output: 3


Constraints:

The number of nodes in the tree is in the range [2, 5000].
0 <= Node.val <= 10^5

"""

# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# V0
# IDEA : DFS, carry the MIN and MAX seen on the path from the root
#
#   for a node b, the best |a.val - b.val| over all ancestors a is
#   max(maxAncestor - b.val, b.val - minAncestor).
#   so we only need to carry 2 numbers down, not the whole path.
#
#   equivalently (and simpler) : at every LEAF, the answer contributed by
#   that root->leaf path is (max on path - min on path). take the overall max.
#
# time = O(n)
# space = O(h), recursion depth
class Solution(object):
    def maxAncestorDiff(self, root):
        self.res = 0

        def dfs(node, lo, hi):
            if not node:
                return
            lo = min(lo, node.val)
            hi = max(hi, node.val)
            self.res = max(self.res, hi - lo)
            dfs(node.left, lo, hi)
            dfs(node.right, lo, hi)

        if not root:
            return 0
        dfs(root, root.val, root.val)
        return self.res
