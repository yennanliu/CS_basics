# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79645198
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def addOneRow(self, root, v, d):
        """
        :type root: TreeNode
        :type v: int
        :type d: int
        :rtype: TreeNode
        """
        if not root: return root
        if d == 1:
            left = TreeNode(v)
            left.left = root
            root = left
        elif d == 2:
            left = TreeNode(v)
            right = TreeNode(v)
            left.left = root.left
            right.right = root.right
            root.left = left
            root.right = right
        else:
            self.addOneRow(root.left, v, d - 1)
            self.addOneRow(root.right, v, d - 1)
        return root
        
# V2 
# Time:  O(n)
# Space: O(h)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def addOneRow(self, root, v, d):
        """
        :type root: TreeNode
        :type v: int
        :type d: int
        :rtype: TreeNode
        """
        if d in (0, 1):
            node = TreeNode(v)
            if d == 1:
                node.left = root
            else:
                node.right = root
            return node
        if root and d >= 2:
            root.left = self.addOneRow(root.left,  v, d-1 if d > 2 else 1)
            root.right = self.addOneRow(root.right, v, d-1 if d > 2 else 0)
        return root