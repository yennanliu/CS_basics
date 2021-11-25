"""

Given the root of a binary tree, construct a string consists of parenthesis and integers from a binary tree with the preorder traversing way, and return it.

Omit all the empty parenthesis pairs that do not affect the one-to-one mapping relationship between the string and the original binary tree.

 

Example 1:


Input: root = [1,2,3,4]
Output: "1(2(4))(3)"
Explanation: Originallay it needs to be "1(2(4)())(3()())", but you need to omit all the unnecessary empty parenthesis pairs. And it will be "1(2(4))(3)"
Example 2:


Input: root = [1,2,3,null,4]
Output: "1(2()(4))(3)"
Explanation: Almost the same as the first example, except we cannot omit the first parenthesis pair to break the one-to-one mapping relationship between the input and the output.
 

Constraints:

The number of nodes in the tree is in the range [1, 104].
-1000 <= Node.val <= 1000
Accepted
109,899
Submissions
194,930

"""

# V0
# IDEA : DFS + DFS 
class Solution(object):
    def isSubtree(self, s, t):
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

# V0'
# IDEA : BFS + DFS 
class Solution(object):
    def isSubtree(self, s, t):
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

# V0'' (### TO FIX)
# IDEA : tree -> string, and check if sub string
# # https://blog.csdn.net/fuxuemingzhu/article/details/71440802
# class Solution(object):
#     def isSubtree(self, s, t):
#         r_s = self.dfs(s, ",")
#         r_t = self.dfs(t, ",")
#         if r_t == None and r_s == None:
#             return True
#         return r_t in r_s
#
#     def dfs(self, root, tmp):
#         if root == None:
#             tmp += "#,"
#             return
#         self.dfs(root.left, tmp + str(root.val) + ",")
#         self.dfs(root.right,tmp + str(root.val) + ",")

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