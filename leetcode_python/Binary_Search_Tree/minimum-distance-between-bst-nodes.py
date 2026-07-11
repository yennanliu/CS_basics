# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79402127
# IDEA : INORDER 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)
# space = O(n)  # vals list holds all n node values
class Solution(object):
    def minDiffInBST(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        vals = []
        def inOrder(root):
            if not root:
                return 
            inOrder(root.left)
            vals.append(root.val)
            inOrder(root.right)
        inOrder(root)
        return min([vals[i + 1] - vals[i] for i in range(len(vals) - 1)])

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79402127
# time = O(n)
# space = O(h)  # h = tree height (recursion stack)
class Solution:
    def minDiffInBST(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.res = float("inf")
        self.prev = None
        self.inOrder(root)
        return self.res
    
    def inOrder(self, root):
        if not root: return
        self.inOrder(root.left)
        if self.prev:
            self.res = min(self.res, root.val - self.prev.val)
        self.prev = root
        self.inOrder(root.right)

# V2
# time = O(n)
# space = O(h)
class Solution(object):
    def minDiffInBST(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def dfs(node):
            if not node:
                return
            dfs(node.left)
            self.result = min(self.result, node.val-self.prev)
            self.prev = node.val
            dfs(node.right)

        self.prev = float('-inf')
        self.result = float('inf')
        dfs(root)
        return self.result