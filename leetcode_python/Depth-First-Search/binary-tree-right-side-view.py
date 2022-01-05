"""

199. Binary Tree Right Side View
Medium

Given the root of a binary tree, imagine yourself standing on the right side of it, return the values of the nodes you can see ordered from top to bottom.

 

Example 1:


Input: root = [1,2,3,null,5,null,4]
Output: [1,3,4]
Example 2:

Input: root = [1,null,3]
Output: [1,3]
Example 3:

Input: root = []
Output: []
 

Constraints:

The number of nodes in the tree is in the range [0, 100].
-100 <= Node.val <= 100

"""

# V0
# IDEA : DFS
class Solution(object):
    def rightSideView(self, root):
        def dfs(root, _layer):
            if len(res) <= _layer:
                res.append([])
            if root:
                res[_layer].append(root.val)
            if not root:
                return
            if root.left:
                dfs(root.left, _layer+1)
            if root.right:
                dfs(root.right, _layer+1)
        # edge case
        if not root:
            return
        res = [[]]
        _layer = 0
        dfs(root, _layer)
        return [x[-1] for x in res]

# V0'
# IDEA : BFS
class Solution(object):
    def rightSideView(self, root):
        # edge case
        if not root:
            return []
        res = [[]]
        _layer = 0
        q = [[root,_layer]]
        while q:
            for i in range(len(q)):
                tmp, _layer = q.pop(0)
                if len(res) <= _layer:
                    res.append([])
                res[_layer].append(tmp.val)
                if tmp.right:
                    q.append([tmp.right, _layer+1])
                if tmp.left:
                    q.append([tmp.left, _layer+1])
        ans = [i[0] for i in res]
        return ans

# V0'
# IDEA : DFS
class Solution(object):
    def rightSideView(self, root):
        def dfs(root, layer):
            if not root:
                return
            if len(res) <= layer+1:
            #if len(res) == layer:     # this works as well
                res.append([])
            res[layer].append(root.val)
            if root.right:
                dfs(root.right, layer+1)
            if root.left:
                dfs(root.left, layer+1)
            
        if not root:
            return []
        res =[[]]
        dfs(root, 0)
        return [x[0] for x in res if len(x) > 0]

# V0''
# IDEA : BFS
class Solution(object):
    def rightSideView(self, root):
        if not root:
            return []
        q = []
        layer=0
        res = []
        ### NOTE : we need layer, so we design our `node` in queue as (root, layer)
        q.append((root, layer))
        while q:
            for i in range(len(q)):
                tmp = q.pop()
                ### NOTE : every time we pop root
                #         -> and do the root.left, root.right op below
                root = tmp[0]
                layer = tmp[1]
                if len(res) == layer:
                    res.append([])
                res[layer].append(root.val)
                if root.right:
                    q.append((root.right, layer+1))
                if root.left:
                    q.append((root.left, layer+1))
        return [x[-1] for x in res]

# V0'''
# IDEA : DFS 
class Solution(object):
    def rightSideView(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        res = []
        self.levelOrder(root, 0, res)
        return [level[-1] for level in res]

    def levelOrder(self, root, level, res):
        if not root: return
        if len(res) == level: res.append([])
        res[level].append(root.val)
        if root.left: self.levelOrder(root.left, level + 1, res)
        if root.right: self.levelOrder(root.right, level + 1, res)
        
# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79557632
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def rightSideView(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        res = []
        self.levelOrder(root, 0, res)
        return [level[-1] for level in res]

    def levelOrder(self, root, level, res):
        if not root: return
        if len(res) == level: res.append([])
        res[level].append(root.val)
        if root.left: self.levelOrder(root.left, level + 1, res)
        if root.right: self.levelOrder(root.right, level + 1, res)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79557632
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def rightSideView(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        res = []
        if not root: return res
        queue = collections.deque()
        queue.append(root)
        while queue:
            res.append(queue[-1].val)
            for i in range(len(queue)):
                node = queue.popleft()
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
        return res

# V1'
# https://www.jiuzhang.com/solution/binary-tree-right-side-view/#tag-highlight-lang-python
class Solution:
    """
    @param root: the root of the given tree
    @return: the values of the nodes you can see ordered from top to bottom
    """
    def rightSideView(self, root):
        # write your code here
        def collect(node, depth):
            if node:
                if depth == len(view):
                    view.append(node.val)
                collect(node.right, depth + 1)
                collect(node.left, depth + 1)
        view = []
        collect(root, 0)
        return view

# V2 
# Time:  O(n)
# Space: O(h)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Solution(object):
    # @param root, a tree node
    # @return a list of integers
    def rightSideView(self, root):
        result = []
        self.rightSideViewDFS(root, 1, result)
        return result

    def rightSideViewDFS(self, node, depth, result):
        if not node:
            return

        if depth > len(result):
            result.append(node.val)

        self.rightSideViewDFS(node.right, depth+1, result)
        self.rightSideViewDFS(node.left, depth+1, result)


# BFS solution
# Time:  O(n)
# Space: O(n)
class Solution2(object):
    # @param root, a tree node
    # @return a list of integers
    def rightSideView(self, root):
        if root is None:
            return []

        result, current = [], [root]
        while current:
            next_level = []
            for node in current:
                if node.left:
                    next_level.append(node.left)
                if node.right:
                    next_level.append(node.right)                
            result.append(node.val)
            current = next_level
        return result