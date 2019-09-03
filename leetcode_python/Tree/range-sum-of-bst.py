# V0
# TO CHECK : ARRAY -> BST :  # 108 Convert Sorted Array to Binary Search Tree`

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83961263
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def rangeSumBST(self, root, L, R):
        """
        :type root: TreeNode
        :type L: int
        :type R: int
        :rtype: int
        """
        if not root:
            return 0
        res = 0
        if L <= root.val <= R:
            res += root.val
            res += self.rangeSumBST(root.left, L, R)
            res += self.rangeSumBST(root.right, L, R)
        elif root.val < L:
            res += self.rangeSumBST(root.right, L, R)
        elif root.val > R:
            res += self.rangeSumBST(root.left, L, R)
        return res

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83961263
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def rangeSumBST(self, root, L, R):
        """
        :type root: TreeNode
        :type L: int
        :type R: int
        :rtype: int
        """
        res = [0]
        self.dfs(root, L, R, res)
        return res[0]
    
    def dfs(self, root, L, R, res):
        if not root:
            return
        if L <= root.val <= R:
            res[0] += root.val
        if root.val < R:
            self.dfs(root.right, L, R, res)
        if root.val > L:
            self.dfs(root.left, L, R, res)

# V2 
# Time:  O(n)
# Space: O(h)
# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def rangeSumBST(self, root, L, R):
        """
        :type root: TreeNode
        :type L: int
        :type R: int
        :rtype: int
        """
        result = 0
        s = [root]
        while s:
            node = s.pop()
            if node:
                if L <= node.val <= R:
                    result += node.val
                if L < node.val:
                    s.append(node.left)
                if node.val < R:
                    s.append(node.right)
        return result