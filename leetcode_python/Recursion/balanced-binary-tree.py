"""
Given a binary tree, determine if it is height-balanced.

For this problem, a height-balanced binary tree is defined as a binary tree in which the depth of the two subtrees of every node never differ by more than 1.

"""
# V0  
class Solution(object):
    def isBalanced(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if root == None:
            return True

        self.getAllDepth(root)

        left_depth = root.left.val if root.left else 0
        right_depth = root.right.val if root.right else 0
        if abs(left_depth - right_depth) <= 1:
            return self.isBalanced(root.left) and self.isBalanced(root.right)
        else:
            return False

    def getAllDepth(self, root):
        if root == None:
            return 0
        root.val = 1 + max(self.getAllDepth(root.left), self.getAllDepth(root.right))
        return root.val

# V1
# https://blog.csdn.net/coder_orz/article/details/51335758
# IDEA : DFS 
class Solution(object):
    def isBalanced(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if root == None:
            return True

        self.getAllDepth(root)

        left_depth = root.left.val if root.left else 0
        right_depth = root.right.val if root.right else 0
        if abs(left_depth - right_depth) <= 1:
            return self.isBalanced(root.left) and self.isBalanced(root.right)
        else:
            return False

    def getAllDepth(self, root):
        if root == None:
            return 0
        root.val = 1 + max(self.getAllDepth(root.left), self.getAllDepth(root.right))
        return root.val

# V1'
# https://blog.csdn.net/coder_orz/article/details/51335758
# IDEA : OPTIMIZED DFS
class Solution(object):
    def isBalanced(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        return self.dfs(root) != -1

    def dfs(self, root):
        if root == None:
            return True

        left_depth = self.dfs(root.left)
        if left_depth == -1:
            return -1
        right_depth = self.dfs(root.right)
        if right_depth == -1:
            return -1
        return 1 + max(left_depth, right_depth) if abs(left_depth - right_depth) <= 1  else -1 

# V1''
# https://blog.csdn.net/coder_orz/article/details/51335758
# IDEA : RECURSION 
class Solution(object):
    def isBalanced(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if root == None:
            return True

        left_depth = self.getDepth(root.left)
        right_depth = self.getDepth(root.right)
        if abs(left_depth - right_depth) <= 1:
            return self.isBalanced(root.left) and self.isBalanced(root.right)
        else:
            return False

    def getDepth(self, root):
        if root == None:
            return 0
        return 1 + max(self.getDepth(root.left), self.getDepth(root.right))

# V1'''
# https://www.jiuzhang.com/solution/balanced-binary-tree/#tag-highlight-lang-python
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        this.val = val
        this.left, this.right = None, None
"""
class Solution:
    """
    @param root: The root of binary tree.
    @return: True if this Binary tree is Balanced, or false.
    """
    def isBalanced(self, root):
        balanced, _ = self.validate(root)
        return balanced
        
    def validate(self, root):
        if root is None:
            return True, 0
            
        balanced, leftHeight = self.validate(root.left)
        if not balanced:
            return False, 0
        balanced, rightHeight = self.validate(root.right)
        if not balanced:
            return False, 0 
        return abs(leftHeight - rightHeight) <= 1, max(leftHeight , rightHeight) + 1

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
    # @return a boolean
    def isBalanced(self, root):
        def getHeight(root):
            if root is None:
                return 0
            left_height, right_height = \
                getHeight(root.left), getHeight(root.right)
            if left_height < 0 or right_height < 0 or \
               abs(left_height - right_height) > 1:
                return -1
            return max(left_height, right_height) + 1
        return (getHeight(root) >= 0)