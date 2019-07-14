# V0 

# V1 
# https://blog.csdn.net/coder_orz/article/details/51706119
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None

class Solution:
    # @param {TreeNode} root
    # @return {string[]}
    def binaryTreePaths(self, root):
        res, path_list = [], []
        self.dfs(root, path_list, res)
        return res

    def dfs(self, root, path_list, res):
        if not root:
            return
        path_list.append(str(root.val))
        if not root.left and not root.right:
            res.append('->'.join(path_list))
        if root.left:
            self.dfs(root.left, path_list, res)
        if root.right:
            self.dfs(root.right, path_list, res)
        path_list.pop()

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51706119
class Solution:
    # @param {TreeNode} root
    # @return {string[]}
    def binaryTreePaths(self, root):
        res = []
        if not root:
            return res
        if not root.left and not root.right:
            res.append(str(root.val))
            return res
        for path in self.binaryTreePaths(root.left):
            res.append(str(root.val) + '->' + path)
        for path in self.binaryTreePaths(root.right):
            res.append(str(root.val) + '->' + path)
        return res

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51706119
# IDEA : DFS (RECURSION)

class Solution:
    # @param {TreeNode} root
    # @return {string[]}
    def binaryTreePaths(self, root):
        res, stack = [], [(root, '')]
        while stack:
            node, curs = stack.pop()
            if node:
                if not node.left and not node.right:
                    res.append(curs + str(node.val))
                stack.append((node.left, curs + str(node.val) + '->'))
                stack.append((node.right, curs + str(node.val) + '->'))
        return res

# V1'''
# https://blog.csdn.net/coder_orz/article/details/51706119
# IDEA : BFS 
class Solution:
    # @param {TreeNode} root
    # @return {string[]}
    def binaryTreePaths(self, root):
        res, queue = [], [(root, '')]
        while queue:
            node, curs = queue.pop()
            if node:
                if not node.left and not node.right:
                    res.append(curs + str(node.val))
                queue.insert(0, (node.left, curs + str(node.val) + '->'))
                queue.insert(0, (node.right, curs + str(node.val) + '->'))
        return res
        
# V2 
# Time:  O(n * h)
# Space: O(h)
class Solution(object):
    # @param {TreeNode} root
    # @return {string[]}
    def binaryTreePaths(self, root):
        result, path = [], []
        self.binaryTreePathsRecu(root, path, result)
        return result

    def binaryTreePathsRecu(self, node, path, result):
        if node is None:
            return

        if node.left is node.right is None:
            ans = ""
            for n in path:
                ans += str(n.val) + "->"
            result.append(ans + str(node.val))

        if node.left:
            path.append(node)
            self.binaryTreePathsRecu(node.left, path, result)
            path.pop()

        if node.right:
            path.append(node)
            self.binaryTreePathsRecu(node.right, path, result)
            path.pop()