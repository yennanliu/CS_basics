"""

1676. Lowest Common Ancestor of a Binary Tree IV
Medium

Given the root of a binary tree and an array of TreeNode objects nodes, return the lowest common
ancestor (LCA) of all the nodes in nodes. All the nodes will exist in the tree, and all values of
the tree's nodes are unique.

Extending the definition of LCA on Wikipedia: "The lowest common ancestor of n nodes p1, p2, ..., pn
in a binary tree T is the lowest node that has every pi as a descendant (where we allow a node to be
a descendant of itself) for every valid i". A descendant of a node x is a node y that is on the path
from node x to some leaf node.


Example 1:

Input: root = [3,5,1,6,2,0,8,null,null,7,4], nodes = [4,7]
Output: 2
Explanation: The lowest common ancestor of nodes 4 and 7 is node 2.

Example 2:

Input: root = [3,5,1,6,2,0,8,null,null,7,4], nodes = [1]
Output: 1
Explanation: The lowest common ancestor of a single node is the node itself.

Example 3:

Input: root = [3,5,1,6,2,0,8,null,null,7,4], nodes = [7,6,2,4]
Output: 5
Explanation: The lowest common ancestor of the nodes 7, 6, 2, and 4 is node 5.


Constraints:

The number of nodes in the tree is in the range [1, 10^4].
-10^9 <= Node.val <= 10^9
All Node.val are unique.
All nodes[i] will exist in the tree.
All nodes[i] are distinct.

"""

# V0
# IDEA : DFS "BUBBLE UP A HIT" (the classic LCA recursion, targets held in a set)
#
#   dfs(node) returns the LCA of all targets found inside node's subtree,
#   or None if the subtree contains none.
#     - node is itself a target -> return node (a node may be its own ancestor,
#       and any deeper target is already covered by node)
#     - both children return non-None -> the split happens here -> return node
#     - otherwise -> forward whichever side found something
#
#   NOTE : values are unique, so a set of target VALUES is a safe membership test.
#   NOTE : this works for any number of targets, not just two -- the moment two
#          branches both report a hit, that node dominates all of them.
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def lowestCommonAncestor(self, root, nodes):
        targets = set()
        for node in nodes:
            targets.add(node.val)

        def dfs(node):
            if node is None or node.val in targets:
                return node
            left = dfs(node.left)
            right = dfs(node.right)
            if left and right:
                return node
            return left or right

        return dfs(root)
