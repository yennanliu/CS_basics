# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79132336
# IDEA : RIGHT -> ROOT -> LEFT 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def convertBST(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        self.sum = 0
        def afterOrder(cur):
            if not cur: return
            afterOrder(cur.right)
            self.sum += cur.val
            cur.val = self.sum
            afterOrder(cur.left)
        afterOrder(root)
        return root

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def convertBST(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        def convertBSTHelper(root, cur_sum):
            if not root:
                return cur_sum

            if root.right:
                cur_sum = convertBSTHelper(root.right, cur_sum)
            cur_sum += root.val
            root.val = cur_sum
            if root.left:
                cur_sum = convertBSTHelper(root.left, cur_sum)
            return cur_sum

        convertBSTHelper(root, 0)
        return root
