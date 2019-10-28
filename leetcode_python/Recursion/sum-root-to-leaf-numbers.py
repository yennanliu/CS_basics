# V0
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

# V1'
# https://www.jiuzhang.com/solution/sum-root-to-leaf-numbers/#tag-highlight-lang-python
class Solution:
    """
    @param root: the root of the tree
    @return: the total sum of all root-to-leaf numbers
    """
    def sumNumbers(self, root):
        # write your code here
        return self.dfs(root, 0)
    def dfs(self, root, prev):
        if(root == None):
            return 0
        sum = root.val + prev * 10
        if(root.left == None and root.right == None):
            return sum
        return self.dfs(root.left, sum) + self.dfs(root.right, sum)

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
