# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79248926
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def longestUnivaluePath(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        longest = [0]
        def dfs(root):
            if not root:
                return 0
            left_len, right_len = dfs(root.left), dfs(root.right)
            left = left_len + 1 if root.left and root.left.val == root.val else 0
            right = right_len + 1 if root.right and root.right.val == root.val else 0
            longest[0] = max(longest[0], left + right)
            return max(left, right)
        dfs(root)
        return longest[0]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79248926
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution(object):
    def longestUnivaluePath(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root: return 0
        self.res = 0
        self.getPath(root)
        return self.res
    
    def getPath(self, root):
        if not root: return 0
        left = self.getPath(root.left)
        right = self.getPath(root.right)
        pl, pr = 0, 0
        if root.left and root.left.val == root.val: pl = 1 + left
        if root.right and root.right.val == root.val: pr = 1 + right
        self.res = max(self.res, pl + pr)
        return max(pl, pr)

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def longestUnivaluePath(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        result = [0]
        def dfs(node):
            if not node:
                return 0
            left, right = dfs(node.left), dfs(node.right)
            left = (left+1) if node.left and node.left.val == node.val else 0
            right = (right+1) if node.right and node.right.val == node.val else 0
            result[0] = max(result[0], left+right)
            return max(left, right)

        dfs(root)
        return result[0]