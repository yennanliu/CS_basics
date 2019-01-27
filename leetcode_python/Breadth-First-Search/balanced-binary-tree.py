

# V1 : dev 


# V2  


class Solution(object):
    def maxDepth(self, root):        
        if root == None:
            return 0        
        else:
            return max( self.maxDepth(root.left), self.maxDepth(root.right)) + 1                               
        
    
    def isBalanced(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if root == None:
            return True
        
        else:
            if abs( self.maxDepth(root.left) - self.maxDepth(root.right) ) > 1:
                return False
            else:
                # EVERY NODE SHOULD FOLLOW THE DEFINITON OF binary tree (depth of NODE < 1 )
                # a binary tree in which the depth of the two subtrees of EVERY NODE never differ by more than 1.
                return self.isBalanced(root.left) and self.isBalanced(root.right) 
                


# V3 
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