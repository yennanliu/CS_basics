"""

783. Minimum Distance Between BST Nodes
Easy

Given the root of a Binary Search Tree (BST), return the minimum difference between the values of any two different nodes in the tree.

Example 1:

https://assets.leetcode.com/uploads/2021/02/05/bst1.jpg

Input: root = [4,2,6,1,3]
Output: 1

Example 2:

https://assets.leetcode.com/uploads/2021/02/05/bst2.jpg

Input: root = [1,0,48,null,null,12,49]
Output: 1

Constraints:

The number of nodes in the tree is in the range [2, 100].
0 <= Node.val <= 10^5

Note: This question is the same as 530: https://leetcode.com/problems/minimum-absolute-difference-in-bst/

"""

# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79402127
# IDEA : INORDER 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)
# space = O(n)  # vals list holds all n node values
class Solution(object):
    def minDiffInBST(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        vals = []
        def inOrder(root):
            if not root:
                return 
            inOrder(root.left)
            vals.append(root.val)
            inOrder(root.right)
        inOrder(root)
        return min([vals[i + 1] - vals[i] for i in range(len(vals) - 1)])

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79402127
# time = O(n)
# space = O(h)  # h = tree height (recursion stack)
class Solution:
    def minDiffInBST(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.res = float("inf")
        self.prev = None
        self.inOrder(root)
        return self.res
    
    def inOrder(self, root):
        if not root: return
        self.inOrder(root.left)
        if self.prev:
            self.res = min(self.res, root.val - self.prev.val)
        self.prev = root
        self.inOrder(root.right)

# V2
# time = O(n)
# space = O(h)
class Solution(object):
    def minDiffInBST(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def dfs(node):
            if not node:
                return
            dfs(node.left)
            self.result = min(self.result, node.val-self.prev)
            self.prev = node.val
            dfs(node.right)

        self.prev = float('-inf')
        self.result = float('inf')
        dfs(root)
        return self.result