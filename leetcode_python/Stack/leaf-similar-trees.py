# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81748617
# Inorder  
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution:
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        leaves1 = []
        leaves2 = []
        self.inOrder(root1, leaves1)
        self.inOrder(root2, leaves2)
        return leaves1 == leaves2
    
    def inOrder(self, root, leaves):
        if not root:
            return
        self.inOrder(root.left, leaves)
        if not root.left and not root.right:
            leaves.append(root.val)
        self.inOrder(root.right, leaves)

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/81748617
# pastOrder  
class Solution:
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        return self.getLeafs(root1) == self.getLeafs(root2)
    
    def getLeafs(self, root):
        res = []
        if not root:
            return res
        if not root.left and not root.right:
            return [root.val]
        res.extend(self.getLeafs(root.left))
        res.extend(self.getLeafs(root.right))
        return res

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/81748617
# postOrder
class Solution:
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        return self.postOrder(root1) == self.postOrder(root2)
        
    def postOrder(self, root):
        stack = []
        stack.append(root)
        res = []
        while stack:
            node = stack.pop()
            if not node: continue
            stack.append(node.left)
            stack.append(node.right)
            if not node.left and not node.right:
                res.append(node.val)
        return res

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/81748617
# looping  
class Solution:
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        return self.preOrder(root1) == self.preOrder(root2)
        
    def preOrder(self, root):
        stack = []
        stack.append(root)
        res = []
        while stack:
            node = stack.pop()
            if not node: continue
            if not node.left and not node.right:
                res.append(node.val)
            stack.append(node.left)
            stack.append(node.right)
        return res

# V2 
# Time:  O(n)
# Space: O(h)
import itertools
# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# V2'
class Solution(object):
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        def dfs(node):
            if not node:
                return
            if not node.left and not node.right:
                yield node.val
            for i in dfs(node.left):
                yield i
            for i in dfs(node.right):
                yield i
        return all(a == b for a, b in
                   itertools.izip_longest(dfs(root1), dfs(root2)))