# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/71440281
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution:
    def findTilt(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.sums = 0
        self.postOrder(root)
        return self.sums
    
    def postOrder(self, root):
        if not root:
            return 0
        left = self.postOrder(root.left)
        right = self.postOrder(root.right)
        self.sums += abs(left - right)
        return left + right + root.val

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def findTilt(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def postOrderTraverse(root, tilt):
            if not root:
                return 0, tilt
            left, tilt = postOrderTraverse(root.left, tilt)
            right, tilt = postOrderTraverse(root.right, tilt)
            tilt += abs(left-right)
            return left+right+root.val, tilt

        return postOrderTraverse(root, 0)[1]