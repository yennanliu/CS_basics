# V0 
class Solution(object):
    def averageOfLevels(self, root):
        """
        :type root: TreeNode
        :rtype: List[float]
        """
        res = []
        self.dfs(root, 0, res)
        return [sum(line) / float(len(line)) for line in res]
    
    def dfs(self, root, level, res):
        if not root:
            return
        if level >= len(res):
            res.append([])
        res[level].append(root.val)
        self.dfs(root.left, level + 1, res)
        self.dfs(root.right, level + 1, res)

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79088554
# DFS V1 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def averageOfLevels(self, root):
        """
        :type root: TreeNode
        :rtype: List[float]
        """
        info = [] # the first element is sum of the level,the second element is nodes in this level
        def dfs(node, depth=0):
            if not node:
                return
            if len(info) <= depth:
                info.append([0, 0])
            info[depth][0] += node.val
            info[depth][1] += 1
            # print(info)
            dfs(node.left, depth + 1)
            dfs(node.right, depth + 1)
        dfs(root)
        return [s / float(c) for s,c in info]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79088554
# DFS V2 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def averageOfLevels(self, root):
        """
        :type root: TreeNode
        :rtype: List[float]
        """
        res = []
        self.getLevel(root, 0, res)
        return [sum(line) / float(len(line)) for line in res]
    
    def getLevel(self, root, level, res):
        if not root:
            return
        if level >= len(res):
            res.append([])
        res[level].append(root.val)
        self.getLevel(root.left, level + 1, res)
        self.getLevel(root.right, level + 1, res)

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79088554
# BFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def averageOfLevels(self, root):
        """
        :type root: TreeNode
        :rtype: List[float]
        """
        que = collections.deque()
        res = []
        que.append(root)
        while que:
            size = len(que)
            row = []
            for _ in range(size):
                node = que.popleft()
                if not node:
                    continue
                row.append(node.val)
                que.append(node.left)
                que.append(node.right)
            if row:
                res.append(sum(row) / float(len(row)))
        return res

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def averageOfLevels(self, root):
        """
        :type root: TreeNode
        :rtype: List[float]
        """
        result = []
        q = [root]
        while q:
            total, count = 0, 0
            next_q = []
            for n in q:
                total += n.val
                count += 1
                if n.left:
                    next_q.append(n.left)
                if n.right:
                    next_q.append(n.right)
            q = next_q
            result.append(float(total) / count)
        return result