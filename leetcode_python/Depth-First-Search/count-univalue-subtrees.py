# V0 

# V1 
# https://xingxingpark.com/Leetcode-250-Count-Univalue-Subtrees/
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution:
    def countUnivalSubtrees(self, root):
        self.count = 0
        self.checkUni(root)
        return self.count

# If both children are "True" and root.val is equal to both children's values that exist, 
# then root node is uniValue subtree node. 
    def checkUni(self, root):
        if not root:
            return True
        l, r = self.checkUni(root.left), self.checkUni(root.right)
        if l and r and (not root.left or root.left.val == root.val) and \
        (not root.right or root.right.val == root.val):
            self.count += 1
            return True
        return False

# V1'
# https://www.jiuzhang.com/solution/count-univalue-subtrees/
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        self.val = val
        self.left, self.right = None, None
"""
class Solution:
    """
    @param root: the given tree
    @return: the number of uni-value subtrees.
    """
    def countUnivalSubtrees(self, root):
        # write your code here
        self.count = 0
        self.checkUni(root)
        return self.count
    
    # bottom-up, first check the leaf nodes and count them, 
    # then go up, if both children are "True" and root.val is 
    # equal to both children's values if exist, then root node
    # is uniValue suntree node. 
    def checkUni(self, root):
        if not root:
            return True
        l, r = self.checkUni(root.left), self.checkUni(root.right)
        if l and r and (not root.left or root.left.val == root.val) and \
        (not root.right or root.right.val == root.val):
            self.count += 1
            return True
        return False

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    # @param {TreeNode} root
    # @return {integer}
    def countUnivalSubtrees(self, root):
        [is_uni, count] = self.isUnivalSubtrees(root, 0)
        return count

    def isUnivalSubtrees(self, root, count):
        if not root:
            return [True, count]

        [left, count] = self.isUnivalSubtrees(root.left, count)
        [right, count] = self.isUnivalSubtrees(root.right, count)
        if self.isSame(root, root.left, left) and \
           self.isSame(root, root.right, right):
                count += 1
                return [True, count]

        return [False, count]

    def isSame(self, root, child, is_uni):
        return not child or (is_uni and root.val == child.val)