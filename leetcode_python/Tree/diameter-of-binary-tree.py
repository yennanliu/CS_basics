# V0 

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