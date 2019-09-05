# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79858752
# IDEA : POSTORDER  + DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def pruneTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if not root: return
        root.left = self.pruneTree(root.left)
        root.right = self.pruneTree(root.right)
        if not root.left and not root.right and root.val == 0:
            return None
        return root

# V1'
# https://www.jiuzhang.com/solution/binary-tree-pruning/#tag-highlight-lang-python
class Solution:
    """
    @param root: the root
    @return: the same tree where every subtree (of the given tree) not containing a 1 has been removed
    """
    def pruneTree(self, root):
        if root is None:
            return None
        root.left = self.pruneTree(root.left)
        root.right = self.pruneTree(root.right)
        if root.val == 0 and root.left == None and root.right == None:
            return None 
        else:
            return root

# V1''
# http://bookshadow.com/weblog/2018/04/09/leetcode-binary-tree-pruning/
class Solution(object):
    def pruneTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if not root: return root
        root.left = self.pruneTree(root.left)
        root.right = self.pruneTree(root.right)
        return root if root.left or root.right or root.val else None
        
# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def pruneTree(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if not root:
            return None
        root.left = self.pruneTree(root.left)
        root.right = self.pruneTree(root.right)
        if not root.left and not root.right and root.val == 0:
            return None
        return root