"""

543. Diameter of Binary Tree
Easy

Given the root of a binary tree, return the length of the diameter of the tree.

The diameter of a binary tree is the length of the longest path between any two nodes in a tree. This path may or may not pass through the root.

The length of a path between two nodes is represented by the number of edges between them.

 

Example 1:


Input: root = [1,2,3,4,5]
Output: 3
Explanation: 3 is the length of the path [4,2,1,3] or [5,2,1,3].
Example 2:

Input: root = [1,2]
Output: 1
 

Constraints:

The number of nodes in the tree is in the range [1, 104].
-100 <= Node.val <= 100


"""
# V0 
class Solution:
    def diameterOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root:
            return 0
        self.diameter = 0
        self.dfs(root)
        return self.diameter
        
    def dfs(self, root):
        if not root:
            return 0
        left = self.dfs(root.left)
        right = self.dfs(root.right)
        self.diameter = max(self.diameter, left + right)
        return 1 + max(left, right)

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/70338312
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution:
    def diameterOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root:
            return 0
        self.diameter = 0
        self.getDepth(root)
        return self.diameter
        
    def getDepth(self, root):
        if not root:
            return 0
        left = self.getDepth(root.left)
        right = self.getDepth(root.right)
        self.diameter = max(self.diameter, left + right)
        return 1 + max(left, right)
        
# V1'
# https://www.jiuzhang.com/solution/diameter-of-binary-tree/#tag-highlight-lang-python
class Solution():
    def __init__(self):
        self.max = 0
 
    def diameterOfBinaryTree(self, root):
        self.helper(root)
        return self.max
 
    def helper(self, root):
        if not root:
            return 0
        left = self.helper(root.left)
        right = self.helper(root.right)
        self.max = max(self.max, left + right)
        return 1 + max(left, right)

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def diameterOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        return self.depth(root, 0)[1]
    
    def depth(self, root, diameter):
        if not root: 
            return 0, diameter
        left, diameter = self.depth(root.left, diameter)
        right, diameter = self.depth(root.right, diameter)
        return 1 + max(left, right), max(diameter, left + right)