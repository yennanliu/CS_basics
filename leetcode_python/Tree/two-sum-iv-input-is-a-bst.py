"""

Given the root of a Binary Search Tree and a target number k, 
return true if there exist two elements in the BST such that their 
sum is equal to the given target.


Example 1:


Input: root = [5,3,6,2,4,null,7], k = 9
Output: true

Example 2:


Input: root = [5,3,6,2,4,null,7], k = 28
Output: false

Example 3:

Input: root = [2,1,3], k = 4
Output: true

Example 4:

Input: root = [2,1,3], k = 1
Output: false

Example 5:

Input: root = [2,1,3], k = 3
Output: true
 

Constraints:

The number of nodes in the tree is in the range [1, 104].
-104 <= Node.val <= 104
root is guaranteed to be a valid binary search tree.
-105 <= k <= 105


"""

# V0
# IDEA : DFS
# In [1]: True or False
# Out[1]: True
class Solution(object):
    def findTarget(self, root, k):
        s = set()
        return self.dfs(root, s, k)

    def dfs(self, root, s, k):
        if not root:
            return False ### NOTE this
        if k - root.val in s:
            return True
        s.add(root.val)
        ### NOTE this
        return self.dfs(root.left, s, k) or self.dfs(root.right, s, k)

# V0'
# IDEA : DFS
class Solution(object):
    def findTarget(self, root, k):
        def dfs(root):
            if not root:
                return False
            if k - root.val in res:
                return True
            res.add(root.val)
            ### NOTE this
            return dfs(root.left) or dfs(root.right)
        # use Set() here
        res = set()
        return dfs(root)

# V0'
# IDEA : BFS
import collections
class Solution(object):
    def findTarget(self, root, k):
        q = collections.deque()
        q.append(root)
        s = set()
        while q:
            for i in range(len(q)):
                tmp = q.popleft()
                if k - tmp.val in s:
                    return True
                # NOTE here ! (we do this after if condition)    
                s.add(tmp.val)
                if tmp.left:
                    q.append(tmp.left)
                if tmp.right:
                    q.append(tmp.right)
        return False

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79120732
# BFS V1 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def findTarget(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: bool
        """
        if not root: return False
        bfs, s = [root], set()
        for i in bfs:
            print i.val
            if k - i.val in s : return True
            s.add(i.val)
            if i.left : bfs.append(i.left)
            if i.right : bfs.append(i.right)
            print([b.val for b in bfs])
        return False

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79120732
# BFS V2
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def findTarget(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: bool
        """
        que = collections.deque()
        que.append(root)
        res = set()
        while que:
            size = len(que)
            for _ in range(size):
                node = que.popleft()
                if not node:
                    continue
                if k - node.val in res:
                    return True
                res.add(node.val)
                que.append(node.left)
                que.append(node.right)
        return False

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79120732
# DFS V1 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def findTarget(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: bool
        """
        res = self.inOrder(root)
        resset = set(res)
        for num in res:
            if k != 2 * num and k - num in resset:
                return True
        return False
    
    def inOrder(self, root):
        if not root:
            return []
        res = []
        res.extend(self.inOrder(root.left))
        res.append(root.val)
        res.extend(self.inOrder(root.right))
        return res

# V1'''
# https://blog.csdn.net/fuxuemingzhu/article/details/79120732
# DFS V2 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def findTarget(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: bool
        """
        res = set()
        def inOrder(root):
            if not root:
                return False
            if k - root.val in res:
                return True
            res.add(root.val)
            return inOrder(root.left) or inOrder(root.right)
        return inOrder(root)

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def findTarget(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: bool
        """
        class BSTIterator(object):
            def __init__(self, root, forward):
                self.__node = root
                self.__forward = forward
                self.__s = []
                self.__cur = None
                self.next()

            def val(self):
                return self.__cur

            def next(self):
                while self.__node or self.__s:
                    if self.__node:
                        self.__s.append(self.__node)
                        self.__node = self.__node.left if self.__forward else self.__node.right
                    else:
                        self.__node = self.__s.pop()
                        self.__cur = self.__node.val
                        self.__node = self.__node.right if self.__forward else self.__node.left
                        break


        if not root:
            return False
        left, right = BSTIterator(root, True), BSTIterator(root, False)
        while left.val() < right.val():
            if left.val() + right.val() == k:
                return True
            elif left.val() + right.val() < k:
                left.next()
            else:
                right.next()
        return False