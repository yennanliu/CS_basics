# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79439026
# http://bookshadow.com/weblog/2017/08/06/leetcode-print-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def printTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[str]]
        """
        self.height = self.findDepth(root)
        self.width = (1 << self.height) - 1
        self.dmap = [[""] * self.width for x in range(self.height)]
        self.traverse(root, 1, self.width >> 1)
        return self.dmap
    def findDepth(self, root):
        if not root: return 0
        return 1 + max(self.findDepth(root.left), self.findDepth(root.right))
    def traverse(self, root, depth, offset):
        if not root: return
        self.dmap[depth - 1][offset] = str(root.val)
        gap = 1 + self.width >> depth + 1
        self.traverse(root.left, depth + 1, offset - gap)
        self.traverse(root.right, depth + 1, offset + gap)

# V2 
# Time:  O(h * 2^h)
# Space: O(h * 2^h)
class Solution(object):
    def printTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[str]]
        """
        def getWidth(root):
            if not root:
                return 0
            return 2 * max(getWidth(root.left), getWidth(root.right)) + 1

        def getHeight(root):
            if not root:
                return 0
            return max(getHeight(root.left), getHeight(root.right)) + 1

        def preorderTraversal(root, level, left, right, result):
            if not root:
                return
            mid = left + (right-left)/2
            result[level][mid] = str(root.val)
            preorderTraversal(root.left, level+1, left, mid-1, result)
            preorderTraversal(root.right, level+1, mid+1, right, result)

        h, w = getHeight(root), getWidth(root)
        result = [[""] * w for _ in xrange(h)]
        preorderTraversal(root, 0, 0, w-1, result)
        return result