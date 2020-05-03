# Given a binary search tree, write a function kthSmallest to find the kth smallest element in it.
#
# Note: 
#
# You may assume k is always valid, 1 ≤ k ≤ BST's total elements.
#
# Follow up:
#
# What if the BST is modified (insert/delete operations) often and you need to find the kth smallest frequently? How would you optimize the kthSmallest routine?
#
# Hint:
#
# Try to utilize the property of a BST.
#
# What if you could modify the BST node's structure?
#
# The optimal runtime complexity is O(height of BST).

# V0 
class Solution(object):
    def kthSmallest(self, root, k):
        self.k = k
        self.res = None
        self.dfs(root)
        return self.res

    def dfs(self, node):
        if not node:
            return
        self.dfs(node.left)
        self.k -= 1
        if self.k == 0:
            self.res = node.val
            return
        self.dfs(node.right)

# V1 
# http://bookshadow.com/weblog/2015/07/02/leetcode-kth-smallest-element-bst/
# IDEA : using inorder survey through whole BST
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution:
    # @param {TreeNode} root
    # @param {integer} k
    # @return {integer}
    def kthSmallest(self, root, k):
        stack = []
        node = root
        while node:
            stack.append(node)
            node = node.left
        x = 1
        while stack and x <= k:
            node = stack.pop()
            x += 1
            right = node.right
            while right:
                stack.append(right)
                right = right.left
        return node.val

### Test case
# dev

# V1'
# IDEA : Recursive 
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63829/Python-Easy-Iterative-and-Recursive-Solution
class Solution(object):
    def kthSmallest(self, root, k):
        self.k = k
        self.res = None
        self.dfs(root)
        return self.res

    def dfs(self, node):
        if not node:
            return
        self.dfs(node.left)
        self.k -= 1
        if self.k == 0:
            self.res = node.val
            return
        self.dfs(node.right)

# V1'''
# IDEA : Iterative 
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/discuss/63829/Python-Easy-Iterative-and-Recursive-Solution
class Solution(object):
    def kthSmallest(self,root, k):
        stack = []
        while root or stack:
            while root:
                stack.append(root)
                root = root.left
            root = stack.pop()
            k -= 1
            if k == 0:
                return root.val
            root = root.right

# V1'''''
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
# IDEA : RECURSION 
#Time complexity : O(N), to build a traversal.
# Space complexity : O(N) to keep an inorder traversal.
class Solution:
    def kthSmallest(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: int
        """
        def inorder(r):
            return inorder(r.left) + [r.val] + inorder(r.right) if r else []
    
        return inorder(root)[k - 1]

# V1''''''
# https://leetcode.com/problems/kth-smallest-element-in-a-bst/solution/
# IDEA : ITERATION
# Time complexity : O(H+k), where HH is a tree height. This complexity is defined by the stack,
# Space complexity : O(H+k), the same as for time complexity, O(N+k) in the worst case, and O(logN+k) in the average case.
class Solution:
    def kthSmallest(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: int
        """
        stack = []
        
        while True:
            while root:
                stack.append(root)
                root = root.left
            root = stack.pop()
            k -= 1
            if not k:
                return root.val
            root = root.right

# V1''''''''
# https://blog.csdn.net/zhangpeterx/article/details/102879948
class Solution:
    def kthSmallest(self, root: TreeNode, k: int) -> int:
        def inorder(r):
            return inorder(r.left) + [r.val] + inorder(r.right) if r else []    
        return inorder(root)[k - 1]

# V2 
# Time:  O(max(h, k))
# Space: O(h)
class Solution(object):
    # @param {TreeNode} root
    # @param {integer} k
    # @return {integer}
    def kthSmallest(self, root, k):
        s, cur, rank = [], root, 0

        while s or cur:
            if cur:
                s.append(cur)
                cur = cur.left
            else:
                cur = s.pop()
                rank += 1
                if rank == k:
                    return cur.val
                cur = cur.right

        return float("-inf")

# time: O(max(h, k))
# space: O(h)
from itertools import islice
class Solution2(object):
    def kthSmallest(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: int
        """
        def gen_inorder(root):
            if root:
                for n in gen_inorder(root.left):
                    yield n

                yield root.val

                for n in gen_inorder(root.right):
                    yield n
        return next(islice(gen_inorder(root), k-1, k))