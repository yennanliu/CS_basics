# V0
# IDEA : DFS
class Solution(object):
    def findTarget(self, root, k):
        """
        :type root: TreeNode
        :type k: int
        :rtype: bool
        """
        def dfs(root):
            if not root:
                return False
            if k - root.val in res:
                return True
            res.add(root.val)
            return dfs(root.left) or dfs(root.right)
        # use Set() here
        res = set()
        return dfs(root)

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