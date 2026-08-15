"""

2773. Height of Special Binary Tree
Medium

You are given a root, which is the root of a special binary tree with n nodes. The nodes of the special binary tree are numbered from 1 to n. Suppose the tree has k leaves in the following order: b1 < b2 < ... < bk.

The leaves of this tree have a special property! That is, for every leaf bi, the following conditions hold:

The right child of bi is bi + 1 if i < k, and b1 otherwise.
The left child of bi is bi - 1 if i > 1, and bk otherwise.

Return the height of the given tree.

Note: The height of a binary tree is the length of the longest path from the root to any other node.


Example 1:

Input: root = [1,2,3,null,null,4,5]
Output: 2
Explanation: The given tree is shown in the following picture. Each leaf's left child is the leaf to its left (shown with the blue edges). Each leaf's right child is the leaf to its right (shown with the red edges). We can see that the graph has a height of 2.

Example 2:

Input: root = [1,2]
Output: 1
Explanation: The given tree is shown in the following picture. There is only one leaf, so it doesn't have any left or right child. We can see that the graph has a height of 1.

Example 3:

Input: root = [1,2,3,null,null,4,null,5,6]
Output: 3
Explanation: The given tree is shown in the following picture. Each leaf's left child is the leaf to its left (shown with the blue edges). Each leaf's right child is the leaf to its right (shown with the red edges). We can see that the graph has a height of 3.


Constraints:

n == number of nodes in the tree
2 <= n <= 10^4
1 <= node.val <= n
The input is generated such that each node.val is unique.

"""

# V0
# IDEA : DFS THAT FILTERS OUT THE FAKE LEAF-TO-LEAF LINKS
#
#   the extra pointers only ever join two LEAVES, and they always come in a
#   mirrored pair: if leaf u's right pointer is leaf v, then v's left pointer
#   points straight back at u. That back-pointer is the tell:
#
#       u.left  is fake  <=>  u.left.right  is u
#       u.right is fake  <=>  u.right.left  is u
#
#   NOTE : a genuine parent-child edge can never satisfy this — it would make a
#          2-cycle inside a real tree — so the test never discards a real edge.
#   NOTE : the k == 1 case still works: the lone leaf points at ITSELF both
#          ways, so u.left is u and u.left.right is u -> fake, as wanted.
#
#   With the fake edges filtered the shape is an ordinary tree, and the height
#   is the deepest node's depth in edges.
#
#   NOTE : n reaches 1e4 and the tree may be a chain, so the walk uses an
#          explicit stack rather than recursion (Python's default limit is
#          ~1000 frames).
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def heightOfTree(self, root):
        if not root:
            return 0

        res = 0
        stack = [(root, 0)]
        while stack:
            node, depth = stack.pop()
            if depth > res:
                res = depth
            left, right = node.left, node.right
            if left is not None and left.right is not node:
                stack.append((left, depth + 1))
            if right is not None and right.left is not node:
                stack.append((right, depth + 1))

        return res
