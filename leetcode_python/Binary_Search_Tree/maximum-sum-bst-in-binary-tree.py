"""

1373. Maximum Sum BST in Binary Tree
Hard

Given a binary tree root, return the maximum sum of all keys of any sub-tree which is also
a Binary Search Tree (BST).

Assume a BST is defined as follows:

The left subtree of a node contains only nodes with keys less than the node's key.
The right subtree of a node contains only nodes with keys greater than the node's key.
Both the left and right subtrees must also be binary search trees.


Example 1:

Input: root = [1,4,3,2,4,2,5,null,null,null,null,null,null,4,6]
Output: 20
Explanation: Maximum sum in a valid Binary search tree is obtained in root node with key equal to 3.

Example 2:

Input: root = [4,3,null,1,2]
Output: 2
Explanation: Maximum sum in a valid Binary search tree is obtained in a single root node with key
equal to 2.

Example 3:

Input: root = [-4,-2,-5]
Output: 0
Explanation: All values are negatives. Return an empty BST.


Constraints:

The number of nodes in the tree is in the range [1, 4 * 10^4].
-4 * 10^4 <= Node.val <= 4 * 10^4

"""

# V0
# IDEA : POST-ORDER DFS returning (is_bst, subtree_min, subtree_max, subtree_sum)
#
#   a node roots a BST iff BOTH children root BSTs and
#     max(left subtree) < node.val < min(right subtree)
#   that check needs the extremes of each side, so every call returns 4 values
#   at once and the whole tree is validated in one bottom-up pass.
#
#   empty subtree -> (True, +inf, -inf, 0) : those sentinels make the
#   comparison above trivially pass for a leaf.
#
#   NOTE : the answer starts at 0 because an EMPTY BST is allowed -- an
#          all-negative tree therefore returns 0, not its largest node.
#   NOTE : once a subtree fails, the values it returns are irrelevant, only
#          the False flag propagates upward.
#
# time = O(n), space = O(h)   h = tree height
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def maxSumBST(self, root):
        INF = float('inf')
        self.res = 0

        def dfs(node):
            # returns (is_bst, min_val, max_val, total)
            if not node:
                return True, INF, -INF, 0

            l_ok, l_min, l_max, l_sum = dfs(node.left)
            r_ok, r_min, r_max, r_sum = dfs(node.right)

            if l_ok and r_ok and l_max < node.val < r_min:
                total = l_sum + r_sum + node.val
                self.res = max(self.res, total)
                return True, min(l_min, node.val), max(r_max, node.val), total

            return False, 0, 0, 0

        dfs(root)
        return self.res
