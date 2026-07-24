"""

872. Leaf-Similar Trees
Solved
Easy
Topics
premium lock icon
Companies
Consider all the leaves of a binary tree, from left to right order, the values of those leaves form a leaf value sequence.



For example, in the given tree above, the leaf value sequence is (6, 7, 4, 9, 8).

Two binary trees are considered leaf-similar if their leaf value sequence is the same.

Return true if and only if the two given trees with head nodes root1 and root2 are leaf-similar.

 

Example 1:


Input: root1 = [3,5,1,6,2,9,8,null,null,7,4], root2 = [3,5,1,6,7,4,2,null,null,null,null,null,null,9,8]
Output: true
Example 2:


Input: root1 = [1,2,3], root2 = [1,3,2]
Output: false
 

Constraints:

The number of nodes in each tree will be in the range [1, 200].
Both of the given trees will have values in the range [0, 200].

"""

# V0
# IDEA: DFS
class Solution(object):
    def leafSimilar(self, root1, root2):
        # edge
        if not root1 and not root2:
            return True

        if not root1 or not root2:
            return False

        seq_1 = []
        seq_2 = []
        self.get_leaf_seq(root1, seq_1)
        self.get_leaf_seq(root2, seq_2)

        # ???
        return seq_1 == seq_2


    def get_leaf_seq(self, root, seq):
        # edge
        if not root:
            return
        if not root.left and not root.right:
            seq.append(root.val)

        # ??
        self.get_leaf_seq(root.left, seq)
        self.get_leaf_seq(root.right, seq)


# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81748617
# Inorder  
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(m + n)  # m, n = number of nodes in root1, root2
# space = O(m + n)  # recursion stack + leaves lists
class Solution:
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        leaves1 = []
        leaves2 = []
        self.inOrder(root1, leaves1)
        self.inOrder(root2, leaves2)
        return leaves1 == leaves2
    
    def inOrder(self, root, leaves):
        if not root:
            return
        self.inOrder(root.left, leaves)
        if not root.left and not root.right:
            leaves.append(root.val)
        self.inOrder(root.right, leaves)

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/81748617
# pastOrder
# time = O(m + n)
# space = O(m + n)
class Solution:
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        return self.getLeafs(root1) == self.getLeafs(root2)
    
    def getLeafs(self, root):
        res = []
        if not root:
            return res
        if not root.left and not root.right:
            return [root.val]
        res.extend(self.getLeafs(root.left))
        res.extend(self.getLeafs(root.right))
        return res

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/81748617
# postOrder
# time = O(m + n)
# space = O(m + n)
class Solution:
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        return self.postOrder(root1) == self.postOrder(root2)
        
    def postOrder(self, root):
        stack = []
        stack.append(root)
        res = []
        while stack:
            node = stack.pop()
            if not node: continue
            stack.append(node.left)
            stack.append(node.right)
            if not node.left and not node.right:
                res.append(node.val)
        return res

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/81748617
# looping
# time = O(m + n)
# space = O(m + n)
class Solution:
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        return self.preOrder(root1) == self.preOrder(root2)
        
    def preOrder(self, root):
        stack = []
        stack.append(root)
        res = []
        while stack:
            node = stack.pop()
            if not node: continue
            if not node.left and not node.right:
                res.append(node.val)
            stack.append(node.left)
            stack.append(node.right)
        return res

# V2
# time = O(n)
# space = O(h)
import itertools
# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# V2'
# time = O(m + n)
# space = O(m + n)
class Solution(object):
    def leafSimilar(self, root1, root2):
        """
        :type root1: TreeNode
        :type root2: TreeNode
        :rtype: bool
        """
        def dfs(node):
            if not node:
                return
            if not node.left and not node.right:
                yield node.val
            for i in dfs(node.left):
                yield i
            for i in dfs(node.right):
                yield i
        return all(a == b for a, b in
                   itertools.izip_longest(dfs(root1), dfs(root2)))
