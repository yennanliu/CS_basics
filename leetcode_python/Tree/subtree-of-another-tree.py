# V0 
# DFS + DFS 
class Solution(object):
    def isSubtree(self, s, t):
        """
        :type s: TreeNode
        :type t: TreeNode
        :rtype: bool
        """
        if not s and not t:
            return True
        if not s or not t:
            return False
        return self.isSameTree(s, t) or self.isSubtree(s.left, t) or self.isSubtree(s.right, t)
        
    def isSameTree(self, s, t):
        if not s and not t:
            return True
        if not s or not t:
            return False
        return s.val == t.val and self.isSameTree(s.left, t.left) and self.isSameTree(s.right, t.right)

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/71440802
# DFS + DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def isSubtree(self, s, t):
        """
        :type s: TreeNode
        :type t: TreeNode
        :rtype: bool
        """
        if not s and not t:
            return True
        if not s or not t:
            return False
        return self.isSameTree(s, t) or self.isSubtree(s.left, t) or self.isSubtree(s.right, t)
        
    def isSameTree(self, s, t):
        if not s and not t:
            return True
        if not s or not t:
            return False
        return s.val == t.val and self.isSameTree(s.left, t.left) and self.isSameTree(s.right, t.right)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/71440802
# BFS + DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def isSubtree(self, s, t):
        """
        :type s: TreeNode
        :type t: TreeNode
        :rtype: bool
        """
        if not s and not t:
            return True
        if not s or not t:
            return False
        que = collections.deque()
        que.append(s)
        while que:
            node = que.popleft()
            if not node:
                continue
            if self.isSameTree(node, t):
                return True
            que.append(node.left)
            que.append(node.right)
        return False
        
    def isSameTree(self, s, t):
        if not s and not t:
            return True
        if not s or not t:
            return False
        return s.val == t.val and self.isSameTree(s.left, t.left) and self.isSameTree(s.right, t.right)

# V2 
# Time:  O(m * n), m is the number of nodes of s, n is the number of nodes of t
# Space: O(h), h is the height of s
class Solution(object):
    def isSubtree(self, s, t):
        """
        :type s: TreeNode
        :type t: TreeNode
        :rtype: bool
        """
        def isSame(x, y):
            if not x and not y:
                return True
            if not x or not y:
                return False
            return x.val == y.val and \
                   isSame(x.left, y.left) and \
                   isSame(x.right, y.right)

        def preOrderTraverse(s, t):
            return s != None and \
                   (isSame(s, t) or \
                    preOrderTraverse(s.left, t) or \
                    preOrderTraverse(s.right, t))

        return preOrderTraverse(s, t)