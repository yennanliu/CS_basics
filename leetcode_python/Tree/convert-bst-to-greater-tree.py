"""
538. Convert BST to Greater Tree
Medium

Given the root of a Binary Search Tree (BST), convert it to a Greater Tree such that every key of the original BST is changed to the original key plus the sum of all keys greater than the original key in BST.

As a reminder, a binary search tree is a tree that satisfies these constraints:

The left subtree of a node contains only nodes with keys less than the node's key.
The right subtree of a node contains only nodes with keys greater than the node's key.
Both the left and right subtrees must also be binary search trees.
 

Example 1:


Input: root = [4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
Output: [30,36,21,36,35,26,15,null,null,null,33,null,null,null,8]
Example 2:

Input: root = [0,null,1]
Output: [1,null,1]
Example 3:

Input: root = [1,0,2]
Output: [3,3,2]
Example 4:

Input: root = [3,2,4,1]
Output: [7,9,4,10]
 

Constraints:

The number of nodes in the tree is in the range [0, 104].
-104 <= Node.val <= 104
All the values in the tree are unique.
root is guaranteed to be a valid binary search tree.
 

Note: This question is the same as 1038: https://leetcode.com/problems/binary-search-tree-to-greater-sum-tree/

"""

# V0
# IDEA : DFS + recursion
#      -> NOTE : via DFS, the op will being executed in `INVERSE` order (last visit will be run first, then previous, then ...)
#      -> e.g. node1 -> node2 -> ... nodeN
#      ->      will run nodeN -> nodeN-1 ... node1
#
# DEMO : input [4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
#
# -> start : root = 4
# --------------
# visit steps
# --------------
#
# dfs(4) -> dfs(6) -> dfs(7) -> dfs(8) 
# -> dfs(5)
# -> dfs(1) -> dfs(2) -> dfs(3)
# -> dfs(0)
#
# --------------
# OP steps
# --------------
# so the _sum will be calculated in `inverse` order
#  -> dfs(8) : _sum =  8
#  -> dfs(7) : _sum =  7 + 8 = 15
#  -> dfs(6) : _sum = 6 + 15 = 21
#  -> dfs(5) : _sum = 5 + 21 = 26
#  -> dfs(4) : _sum = 4 + 26 = 30
#  -> dfs(3) : _sum = 3 + 30 = 33
#  -> dfs(2) : _sum = 2 + 33 = 35
#  -> dfs(1) : _sum = 1 + 35 = 36
#  -> dfs(0) : _sum = 0 + 36 = 0
#
# --------------
# logs
# --------------
# root.val = 4
# root.val = 6
# root.val = 7
# root.val = 8
# root.val = 5
# root.val = 1
# root.val = 2
# root.val = 3
# root.val = 0
class Solution(object):

    def convertBST(self, root):
        self.sum = 0
        self.dfs(root)
        return root

    def dfs(self, cur):
        if not cur: 
            return
        #print ("root.val = " + str(root.val))
        self.dfs(cur.right)
        self.sum += cur.val
        cur.val = self.sum
        self.dfs(cur.left)

# V0'
# IDEA : BFS
class Solution(object):
    def convertBST(self, root):
        total = 0      
        node = root
        stack = []
        while stack or node is not None:
            while node is not None:
                stack.append(node)
                node = node.right
            node = stack.pop()
            total += node.val
            node.val = total
            node = node.left
        return root

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

### Test case : dev 

# V1'
# https://leetcode.com/problems/convert-bst-to-greater-tree/solution/
# IDEA : RECURSION 
# Time complexity : O(N)
# Space complexity : O(N)
class Solution(object):
    def __init__(self):
        self.total = 0

    def convertBST(self, root):
        if root is not None:
            self.convertBST(root.right)
            self.total += root.val
            root.val = self.total
            self.convertBST(root.left)
        return root

# V1''
# https://leetcode.com/problems/convert-bst-to-greater-tree/solution/
# Iteration with a Stack
# Time complexity : O(N)
# Space complexity : O(N)
class Solution(object):
    def convertBST(self, root):
        total = 0
        
        node = root
        stack = []
        while stack or node is not None:
            # push all nodes up to (and including) this subtree's maximum on
            # the stack.
            while node is not None:
                stack.append(node)
                node = node.right

            node = stack.pop()
            total += node.val
            node.val = total

            # all nodes with values between the current and its parent lie in
            # the left subtree.
            node = node.left
        return root

# V1'''
# https://leetcode.com/problems/convert-bst-to-greater-tree/solution/
# IDEA :  Reverse Morris In-order Traversal 
# Time complexity : O(N)
# Space complexity : O(1)
class Solution(object):
    def convertBST(self, root):
        # Get the node with the smallest value greater than this one.
        def get_successor(node):
            succ = node.right
            while succ.left is not None and succ.left is not node:
                succ = succ.left
            return succ
                
        total = 0
        node = root
        while node is not None:
            # If there is no right subtree, then we can visit this node and
            # continue traversing left.
            if node.right is None:
                total += node.val
                node.val = total
                node = node.left
            # If there is a right subtree, then there is a node that has a
            # greater value than the current one. therefore, we must traverse
            # that node first.
            else:
                succ = get_successor(node)
                # If there is no left subtree (or right subtree, because we are
                # in this branch of control flow), make a temporary connection
                # back to the current node.
                if succ.left is None:
                    succ.left = node
                    node = node.right
                # If there is a left subtree, it is a link that we created on
                # a previous pass, so we should unlink it and visit this node.
                else:
                    succ.left = None
                    total += node.val
                    node.val = total
                    node = node.left
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