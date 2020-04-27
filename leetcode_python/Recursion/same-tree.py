# 100. Same Tree
# Easy
#
# 56
#
# Add to List
#
# Share
# Given two binary trees, write a function to check if they are the same or not.
#
# Two binary trees are considered the same if they are structurally identical and the nodes have the same value.
#
# Example 1:
#
# Input:     1         1
#           / \       / \
#          2   3     2   3
#
#         [1,2,3],   [1,2,3]
#
# Output: true
# Example 2:
#
# Input:     1         1
#           /           \
#          2             2
#
#         [1,2],     [1,null,2]
#
# Output: false
# Example 3:
#
# Input:     1         1
#           / \       / \
#          2   1     1   2
#
#         [1,2,1],   [1,1,2]
#
# Output: false

# V0 
# IDEA : Recursion
class Solution(object):
    def isSameTree(self, p, q):
        # if p != None and q != None
        if p and q:
            return p.val == q.val and \
                       self.isSameTree(p.left, q.left) and \
                       self.isSameTree(p.right, q.right)
        # if p == None or q == None  or p == q == None
        return p == q == None

# V0'
# IDEA : Iteration (DFS)
class Solution(object):
    def isSameTree(self, p, q):
        def dfs(p, q):
            if p == None and q != None:
                return False
            if p != None and q == None:
                return False
            if p == None and q == None:
                return True
            if p.val != q.val:
                return False
            return dfs(p.left, q.left) and dfs(p.right, q.right) 
        return dfs(p,q)

# V1
# https://leetcode.com/problems/same-tree/solution/
# IDEA : Recursion
# Time complexity : O(N), where N is a number of nodes in the tree, since one visits each node exactly once.
# Space complexity : O(logN) in the best case of completely balanced tree and O(N) in the worst case of completely unbalanced tree, to keep a recursion stack.
class Solution:
    def isSameTree(self, p, q):
        """
        :type p: TreeNode
        :type q: TreeNode
        :rtype: bool
        """    
        # p and q are both None
        if not p and not q:
            return True
        # one of p and q is None
        if not q or not p:
            return False
        if p.val != q.val:
            return False
        return self.isSameTree(p.right, q.right) and \
               self.isSameTree(p.left, q.left)

### Test case (TODO : how to test with tree data structure)
# s=Solution()
# assert s.isSameTree([1,2,3], [1,2,3]) == True
# assert s.isSameTree([1,2,3], [1,2,4]) == False
# assert s.isSameTree([], []) == True
# assert s.isSameTree([0], [1]) == False
# assert s.isSameTree([1,2,3], [1,2, None]) == False
# assert s.isSameTree([None,None,None], [None,None,None]) == True

# V1'
# https://leetcode.com/problems/same-tree/solution/
# IDEA : Iteration  (BFS)
# Time complexity : O(N) since each node is visited exactly once.
# Space complexity : O(log(N)) in the best case of completely balanced tree and O(N) in the worst case of completely unbalanced tree, to keep a deque.
from collections import deque
class Solution:
    def isSameTree(self, p, q):
        """
        :type p: TreeNode
        :type q: TreeNode
        :rtype: bool
        """    
        def check(p, q):
            # if both are None
            if not p and not q:
                return True
            # one of p and q is None
            if not q or not p:
                return False
            if p.val != q.val:
                return False
            return True      
        deq = deque([(p, q),])
        while deq:
            p, q = deq.popleft()
            if not check(p, q):
                return False         
            if p:
                deq.append((p.left, q.left))
                deq.append((p.right, q.right))
                    
        return True

# V1''
# http://bookshadow.com/weblog/2016/08/18/leetcode-same-tree/
# IDEA : Recursion
# time : O(N), where N is a number of nodes in the tree
# space :  O(2**n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def isSameTree(self, p, q):
        """
        :type p: TreeNode
        :type q: TreeNode
        :rtype: bool
        """
        if p and q:
            return p.val == q.val and \
                       self.isSameTree(p.left, q.left) and \
                       self.isSameTree(p.right, q.right)
        return p is None and q is None

# V1'''
# https://www.cnblogs.com/loadofleaf/p/5502249.html
# IDEA : Iteration (DFS)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def isSameTree(self, p, q):
        """
        :type p: TreeNode
        :type q: TreeNode
        :rtype: bool
        """
        def dfs(p, q):
            if p == None and q != None:
                return False
            if p != None and q == None:
                return False
            if p == None and q == None:
                return True
            if p.val != q.val:
                return False
            return dfs(p.left, q.left) and dfs(p.right, q.right) 
        return dfs(p,q)

# V2
# Time:  O(n)
# Space: O(h), h is height of binary tree
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param p, a tree node
    # @param q, a tree node
    # @return a boolean
    def isSameTree(self, p, q):
        if p is None and q is None:
            return True

        if p is not None and q is not None:
            return p.val == q.val and self.isSameTree(p.left, q.left) and self.isSameTree(p.right, q.right)

        return False