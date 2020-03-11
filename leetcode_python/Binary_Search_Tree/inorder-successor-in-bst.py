"""
Given a binary search tree and a node in it, find the in-order successor of that node in the BST.

Note: If the given node has no in-order successor in the tree, return null.

"""

# V0 
# IDEA : DFS
class Solution(object):
    def inorderSuccessor(self, root, p):         
        def inOrder(root):
            if not root:
                return
            inOrder(root.left)
            l.append(root)
            inOrder(root.right)
            
        l = []
        inOrder(root)
        for i in range(len(l)):
            if l[i] == p:
                return l[i+1] if i+1 < len(l) else None

# V1 
# https://blog.csdn.net/danspace1/article/details/86667504
# IDEA : DFS
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def inorderSuccessor(self, root, p):
        """
        :type root: TreeNode
        :type p: TreeNode
        :rtype: TreeNode
        """           
        def inOrder(root):
            if not root:
                return
            inOrder(root.left)
            l.append(root)
            inOrder(root.right)
        
        l = []
        inOrder(root)
        for i in range(len(l)):
            if l[i] == p:
                return l[i+1] if i+1 < len(l) else None

# V1' 
# https://blog.csdn.net/danspace1/article/details/86667504
class Solution(object):
    def inorderSuccessor(self, root, p):
        """
        :type root: TreeNode
        :type p: TreeNode
        :rtype: TreeNode
        """       
        ans = None
        while root:
            if p.val < root.val:
                ans = root
                root = root.left
            else:
                root = root.right
        return ans
        
# V2 
# Time:  O(h)
# Space: O(1)
class Solution(object):
    def inorderSuccessor(self, root, p):
        """
        :type root: TreeNode
        :type p: TreeNode
        :rtype: TreeNode
        """
        # If it has right subtree.
        if p and p.right:
            p = p.right
            while p.left:
                p = p.left
            return p
        # Search from root.
        successor = None
        while root and root != p:
            if root.val > p.val:
                successor = root
                root = root.left
            else:
                root = root.right
        return successor