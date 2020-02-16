# Given a binary tree, return the vertical order traversal of its nodes' values. (ie, from top to bottom, column by column).

# If two nodes are in the same row and column, the order should be from left to right.

# Examples:
# Given binary tree [3,9,20,null,null,15,7],

#     3
#    / \
#   9  20
#     /  \
#    15   7
# return its vertical order traversal as:

# [
#   [9],
#   [3,15],
#   [20],
#   [7]
# ]
# Given binary tree [3,9,20,4,5,2,7],

#     _3_
#    /   \
#   9    20
#  / \   / \
# 4   5 2   7
# return its vertical order traversal as:

# [
#   [4],
#   [9],
#   [3,5,2],
#   [20],
#   [7]
# ]

# V0 
# DFS + collections.defaultdict(list)
#####  NEED TO VALIDATE #####
import collections
class Solution(object):
    def verticalOrder(self, root):
        idx = 0
        cols = collections.defaultdict(list)
        self.dfs(root, idx, cols)
        return [cols[c] for c in sorted(cols.keys())]

    def dfs(self, root, idx, cols):
        if not root: return []
        if not root.left and not root.right:
            return 
        cols[idx].append(root.value)
        if root.left:
            self.dfs(roots, idx - 1, cols)
        if root.right:
            self.dfs(roots, idx + 1, cols)

# V0'
# IDEA : BFS + collections.defaultdict(list)
#####  NEED TO VALIDATE #####
import collections
class Solution(object):
    def verticalOrder(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        # base case
        if not root: return []
        r = collections.defaultdict(list)
        q = [(root,0)]
        while q:
            tmp = q.pop()
            r[tmp[1]].append(root.value)
            if tmp[0].left:
                q.append((tmp[0].left, tmp[0] -1))
            if tmp[0].right:
                q.append((tmp[0].right, tmp[0] +1))
        return r

# V0''
# IDEA : BFS + collections.defaultdict(list)
import collections
class Solution(object):
    def verticalOrder(self, root):
        if not root: return []
        cols = collections.defaultdict(list)
        q = [(root, 0)]
        while q:
            for node, col in q:
                cols[col].append(node.val)
            new_q = []
            for node, col in q:
                if node.left:
                    new_q.append((node.left, col-1))
                if node.right:
                    new_q.append((node.right, col+1))
            q = new_q          
        return [cols[c] for c in sorted(cols.keys())]

# V1 
# https://blog.csdn.net/qq508618087/article/details/50760661
# https://blog.csdn.net/danspace1/article/details/86654851
# IDEA : BFS + collections.defaultdict(list) 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
import collections
class Solution(object):
    def verticalOrder(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        # base case
        if not root: return []
        cols = collections.defaultdict(list)
        q = [(root, 0)]
        while q:
            for node, col in q:
                cols[col].append(node.val)
            new_q = []
            for node, col in q:
                if node.left:
                    new_q.append((node.left, col-1))
                if node.right:
                    new_q.append((node.right, col+1))
            q = new_q
            
        return [cols[c] for c in sorted(cols.keys())]

# V1'
# https://www.jiuzhang.com/solution/binary-tree-vertical-order-traversal/#tag-highlight-lang-python
# IDEA : BFS + collections.defaultdict(list)
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        self.val = val
        self.left, self.right = None, None
"""
class Solution:
    # @param {TreeNode} root the root of binary tree
    # @return {int[][]} the vertical order traversal
    def verticalOrder(self, root):
        # Write your code here
        results = collections.defaultdict(list)
        import Queue
        queue = Queue.Queue()
        queue.put((root, 0))
        while not queue.empty():
            node, x = queue.get()
            if node:
                results[x].append(node.val)
                queue.put((node.left, x - 1))
                queue.put((node.right, x + 1))
                
        return [results[i] for i in sorted(results)]

# V2 
# Time:  O(n)
# Space: O(n)
import collections
# BFS + hash solution.
class Solution(object):
    def verticalOrder(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        cols = collections.defaultdict(list)
        queue = [(root, 0)]
        for node, i in queue:
            if node:
                cols[i].append(node.val)
                queue += (node.left, i - 1), (node.right, i + 1)
        return [cols[i] for i in range(min(cols.keys()),
                                        max(cols.keys()) + 1)] if cols else []