# V0 

# V1 
# http://bookshadow.com/weblog/2017/02/26/leetcode-minimum-absolute-difference-in-bst/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution(object):
    def getMinimumDifference(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.last = -0x80000000
        self.ans = 0x7FFFFFFF
        def inOrderTraverse(root):
            if not root: return
            inOrderTraverse(root.left)
            self.ans = min(self.ans, root.val - self.last) # since there is no "root.val - self.last" value when 1st func call, so we use self.ans to deal with this 
            self.last = root.val
            inOrderTraverse(root.right)
        inOrderTraverse(root)
        return self.ans

# V1'
# http://bookshadow.com/weblog/2017/02/26/leetcode-minimum-absolute-difference-in-bst/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution(object):
    def getMinimumDifference(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        left, right = root.left, root.right
        ans = 0x7FFFFFFF
        if left:
            while left.right: left = left.right
            ans = min(root.val - left.val, self.getMinimumDifference(root.left))
        if right:
            while right.left: right = right.left
            ans = min(ans, right.val - root.val, self.getMinimumDifference(root.right))
        return ans

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def getMinimumDifference(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def inorderTraversal(root, prev, result):
            if not root:
                return (result, prev)

            result, prev = inorderTraversal(root.left, prev, result)
            if prev: result = min(result, root.val - prev.val)
            return inorderTraversal(root.right, root, result)

        return inorderTraversal(root, None, float("inf"))[0]