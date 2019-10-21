"""
Given a binary tree, return the level order traversal of its nodes’ values. (ie, from left to right, level by level).

For example:

Given binary tree [3,9,20,null,null,15,7],

    3
   / \
  9  20
    /  \
   15   7

return its level order traversal as:

[
  [3],
  [9,20],
  [15,7]
]

"""

# V0 
# IDEA : DFS 
# DEMO
# In [29]: x=[]

# In [30]: x
# Out[30]: []

# In [31]: x.append([])

# In [32]: x
# Out[32]: [[]]

# In [33]: x.append([])

# In [34]: x
# Out[34]: [[], []]

# In [35]: x.append([])

# In [36]: x
# Out[36]: [[], [], []]
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def levelOrder(self, root):
        res = []
        layer = 0 
        self.dfs(root,res,layer)
        return res 

    def dfs(self, root, res, layer):
        if not root: return 
        if len(res) == layer: # in case length of res is not enough, so just add more "space", (append([]))
            res.append([])        
        res[layer].append(root.value) 
        if root.left:
            dfs(root.left, res, layer +1)
        if root.right:
            dfs(root.right, res, layer +1)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79616156
# IDEA : DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def levelOrder(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        res = []
        self.level(root, 0, res)
        return res

    def level(self, root, level, res):
        if not root: return
        if len(res) == level: res.append([])
        res[level].append(root.val)
        if root.left: self.level(root.left, level + 1, res)
        if root.right: self.level(root.right, level + 1, res)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79616156
# IDEA : BFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def levelOrder(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[int]]
        """
        res = []
        if not root: return res
        queue = collections.deque()
        queue.append(root)
        while queue:
            level = []
            for i in range(len(queue)):  # NOTE THAT HERE WE HAVE TO GO THROUGH EVERY ELEMENT IN THE SAME LAYER OF BST 
                node = queue.popleft()
                level.append(node.val)
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            res.append(level)
        return res

# V1''
# https://www.jiuzhang.com/solution/binary-tree-level-order-traversal/#tag-highlight-lang-python
from collections import deque
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        self.val = val
        self.left, self.right = None, None
"""
class Solution:
    """
    @param root: A Tree
    @return: Level order a list of lists of integer
    """
    def levelOrder(self, root):
        if root is None:
            return []
            
        queue = deque([root])
        result = []
        while queue:
            level = []
            for _ in range(len(queue)):
                node = queue.popleft()
                level.append(node.val)
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            result.append(level)
        return result

# V1'''
# https://www.jiuzhang.com/solution/binary-tree-level-order-traversal/#tag-highlight-lang-python
class Solution:
    """
    @param root: The root of binary tree.
    @return: Level order in a list of lists of integers
    """
    def levelOrder(self, root):
        if not root:
            return []

        queue = [root]
        results = []
        while queue:
            next_queue = []
            results.append([node.val for node in queue])
            for node in queue:
                if node.left:
                    next_queue.append(node.left)
                if node.right:
                    next_queue.append(node.right)
            queue = next_queue
        return results

# V1''''
# https://www.jiuzhang.com/solution/binary-tree-level-order-traversal/#tag-highlight-lang-python
class Solution:
    """
    @param root: A Tree
    @return: Level order a list of lists of integer
    """
    def levelOrder(self, root):
        # write your code here
        result = []
        target_level = 0
        while True:
            level = []
            self.dfs(root, 0, target_level, level)
            if not level:
                break
            result.append(level)
            target_level += 1
        return result
        
    def dfs(self, root, cur_level, target_level, level):
        if not root or cur_level > target_level:
            return
        if cur_level == target_level:
            level.append(root.val)
        self.dfs(root.left, cur_level + 1, target_level, level)
        self.dfs(root.right, cur_level + 1, target_level, level)

# V2
# Time:  O(n)
# Space: O(n)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param root, a tree node
    # @return a list of lists of integers
    def levelOrder(self, root):
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
        return result