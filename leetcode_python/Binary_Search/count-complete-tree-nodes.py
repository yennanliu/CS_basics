# V0 

# V1 
# http://bookshadow.com/weblog/2015/06/06/leetcode-count-complete-tree-nodes/
# https://blog.csdn.net/fuxuemingzhu/article/details/80781666
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def countNodes(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root: return 0
        nodes = 0
        left_height = self.getHeight(root.left)
        right_height = self.getHeight(root.right)
        if left_height == right_height:
            nodes = 2 ** left_height + self.countNodes(root.right)
        else:
            nodes = 2 ** right_height + self.countNodes(root.left)
        return nodes


    def getHeight(self, root):
        height = 0
        while root:
            height += 1
            root = root.left
        return height

# V2 
# Time:  O(h * logn) = O((logn)^2)
# Space: O(1)
class Solution(object):
    # @param {TreeNode} root
    # @return {integer}
    def countNodes(self, root):
        if root is None:
            return 0

        node, level = root, 0
        while node.left is not None:
            node = node.left
            level += 1

        # Binary search.
        left, right = 2 ** level, 2 ** (level + 1)
        while left < right:
            mid = left + (right - left) / 2
            if not self.exist(root, mid):
                right = mid
            else:
                left = mid + 1

        return left - 1

    # Check if the nth node exist.
    def exist(self, root, n):
        k = 1
        while k <= n:
            k <<= 1
        k >>= 2

        node = root
        while k > 0:
            if (n & k) == 0:
                node = node.left
            else:
                node = node.right
            k >>= 1
        return node is not None

