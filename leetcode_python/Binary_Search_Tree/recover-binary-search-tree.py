# https://leetcode.com/problems/recover-binary-search-tree/description/

"""

99. Recover Binary Search Tree
Solved
Medium
Topics
premium lock icon
Companies
You are given the root of a binary search tree (BST), where the values of exactly two nodes of the tree were swapped by mistake. Recover the tree without changing its structure.

 

Example 1:


Input: root = [1,3,null,null,2]
Output: [3,1,null,null,2]
Explanation: 3 cannot be a left child of 1 because 3 > 1. Swapping 1 and 3 makes the BST valid.
Example 2:


Input: root = [3,1,4,null,null,2]
Output: [2,1,4,null,null,3]
Explanation: 2 cannot be in the right subtree of 3 because 2 < 3. Swapping 2 and 3 makes the BST valid.
 

Constraints:

The number of nodes in the tree is in the range [2, 1000].
-231 <= Node.val <= 231 - 1
 

Follow up: A solution using O(n) space is pretty straight-forward. Could you devise a constant O(1) space solution?

"""


# V0
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def recoverTree(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: None Do not return anything, modify root in-place instead.
        """
        pass


# V0-1
# IDEA: in-order DFS + BST (in-order is in increasing order) (gpt)
class Solution(object):
    def recoverTree(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: None Do not return anything, modify root in-place instead.
        """
        self.first_node = None
        self.second_node = None
        self.prev = None

        self.helper(root)

        # Swap the two incorrect values
        self.first_node.val, self.second_node.val = \
            self.second_node.val, self.first_node.val

    def helper(self, node):
        if not node:
            return

        # Left
        self.helper(node.left)

        # Current
        if self.prev and self.prev.val > node.val:
            if self.first_node is None:
                self.first_node = self.prev

            self.second_node = node

        self.prev = node

        # Right
        self.helper(node.right)


# V0-2
# IDEA: in-order DFS + BST (in-order is in increasing order) (gemini)
class Solution(object):

    def recoverTree(self, root):
        """
        :type root: Optional[TreeNode]

        :rtype: None Do not return anything, modify root in-place instead.
        """
        self.first = None
        self.second = None
        self.prev = None

        def inorder(node):
            if not node:
                return

            # 1. Traverse Left
            inorder(node.left)

            # 2. Process Current Node
            if self.prev and self.prev.val > node.val:
                # First violation: first node is ALWAYS prev
                if not self.first:
                    self.first = self.prev

                # Update second node on both 1st and 2nd violation
                self.second = node

            self.prev = node

            # 3. Traverse Right
            inorder(node.right)

        # Step 1: Identify swapped nodes via In-Order Traversal
        inorder(root)

        # Step 2: Swap values in-place
        if self.first and self.second:
            self.first.val, self.second.val = self.second.val, self.first.val


# V1


# V2