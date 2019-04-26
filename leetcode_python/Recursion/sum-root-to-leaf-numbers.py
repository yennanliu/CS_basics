# V0 : DEV 

# V1
# http://bookshadow.com/weblog/2016/01/07/leetcode-sum-root-to-leaf-numbers/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution(object):
    def sumNumbers(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def dfs(root, val):
            val = val * 10 + root.val
            if (root.left or root.right) is None:
                return val
            sums = 0
            if root.left:
                sums += dfs(root.left, val)
            if root.right:
                sums += dfs(root.right, val)
            return sums
        if root is None:
            return 0
        return dfs(root, 0)


# V2
# Time:  O(n)
# Space: O(h), h is height of binary tree

class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param root, a tree node
    # @return an integer
    def sumNumbers(self, root):
        return self.sumNumbersRecu(root, 0)

    def sumNumbersRecu(self, root, num):
        if root is None:
            return 0

        if root.left is None and root.right is None:
            return num * 10 + root.val

        return self.sumNumbersRecu(root.left, num * 10 + root.val) + self.sumNumbersRecu(root.right, num * 10 + root.val)
