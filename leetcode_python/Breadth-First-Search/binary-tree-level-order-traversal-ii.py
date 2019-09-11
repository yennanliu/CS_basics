# V0
class Solution(object):
    def levelOrder(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        res = []
        self.level(root, 0, res)
        return res[::-1]

    def level(self, root, level, res):
        if not root: return
        if len(res) == level: res.append([])
        res[level].append(root.val)
        if root.left: self.level(root.left, level + 1, res)
        if root.right: self.level(root.right, level + 1, res)

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/49108449
# IDEA : DFS 
class Solution(object):
    def levelOrder(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        res = []
        self.level(root, 0, res)
        return res[::-1]

    def level(self, root, level, res):
        if not root: return
        if len(res) == level: res.append([])
        res[level].append(root.val)
        if root.left: self.level(root.left, level + 1, res)
        if root.right: self.level(root.right, level + 1, res)
        
# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/49108449
# IDEA : BFS  
class Solution:
    def levelOrderBottom(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        res = []
        que = collections.deque()
        que.append(root)
        level = 0
        while que:
            levelVal = []
            size = len(que)
            for _ in range(size):
                node = que.popleft()
                if not node:
                    continue
                levelVal.append(node.val)
                que.append(node.left)
                que.append(node.right)
            level += 1
            if levelVal:
                res.append(levelVal)
        return res[::-1]

# V2 
# Time:  O(n)
# Space: O(n)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Solution(object):
    def levelOrderBottom(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        if root is None:
            return []

        result, current = [], [root]
        while current:
            next_level, vals = [], []
            for node in current:
                vals.append(node.val)
                if node.left:
                    next_level.append(node.left)
                if node.right:
                    next_level.append(node.right)
            current = next_level
            result.append(vals)

        return result[::-1]