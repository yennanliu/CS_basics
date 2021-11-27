"""

Given a binary tree, return the level order traversal of its nodes’ values. 
(ie, from left to right, level by level).

Example 1:


Input: root = [3,9,20,null,null,15,7]
Output: [[3],[9,20],[15,7]]

Example 2:

Input: root = [1]
Output: [[1]]

Example 3:

Input: root = []
Output: []
 

Constraints:

The number of nodes in the tree is in the range [0, 2000].
-1000 <= Node.val <= 1000



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
# IDEA : BFS
class Solution(object):
    def levelOrder(self, root):
        if not root:
            return []
        res = []
        _layer=0
        q = [[root, _layer]]
        while q:
            for i in range(len(q)):
                tmp, _layer = q.pop(0)
                if len(res) <= _layer:
                    res.append([])
                res[_layer].append(tmp.val)
                if tmp.left:
                    q.append([tmp.left, _layer+1])
                if tmp.right:
                    q.append([tmp.right, _layer+1])
        return res

# V0'
# IDEA : BFS
import collections
class Solution(object):
    def levelOrder(self, root):
        if not root:
            return []
        r = []
        q = collections.deque()
        q.append(root)
        l = 0
        while q:
            ### NOTE THIS : `for i in range(len(q))`, but not `for i in q`
            #             -> we visit idx of nodes in q, but not nodes itself
            for i in range(len(q)):
                tmp = q.popleft()
                if len(r) < l+1:
                    r.append([])
                r[l].append(tmp.val)
                if tmp.left:
                    q.append(tmp.left)
                if tmp.right:
                    q.append(tmp.right)
            l += 1
        return r

# V0'
# IDEA : BFS 
# Definition for a binary tree node.
class Solution(object):
    def levelOrder(self, root):
        res = []
        if not root:return res
        ### NOTE : WE USE  collections.deque() in most of the BFS problems
        queue = collections.deque()
        queue.append(root)
        while queue:
            ### NOTE : we have this level help list here
            level = []
            for i in range(len(queue)):  # NOTE THAT HERE WE HAVE TO GO THROUGH EVERY ELEMENT IN THE SAME LAYER OF BST 
                node = queue.popleft()
                level.append(node.val)
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            ### NOTE : we use ref collect result from every layer
            res.append(level)
        return res

# V0'
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
        self.dfs(root, 0, res)
        return res

    def dfs(self, root, level, res):
        if not root: 
            return
        if len(res) == level: 
            res.append([])
        res[level].append(root.val)
        if root.left: 
            self.dfs(root.left, level + 1, res)
        if root.right: 
            self.dfs(root.right, level + 1, res)

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
            ### NOTE : we use ref collect result from every layer
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
