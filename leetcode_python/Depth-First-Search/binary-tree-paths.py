"""

257. Binary Tree Paths
Easy

Given the root of a binary tree, return all root-to-leaf paths in any order.

A leaf is a node with no children.

 

Example 1:


Input: root = [1,2,3,null,5]
Output: ["1->2->5","1->3"]
Example 2:

Input: root = [1]
Output: ["1"]
 

Constraints:

The number of nodes in the tree is in the range [1, 100].
-100 <= Node.val <= 100

"""

# V0
# IDEA : BFS
class Solution(object):
    def binaryTreePaths(self, root):
        # edge case
        if not root:
            return
        # bfs
        res = []
        cache = ""
        q = [[root, cache]]
        while q:
            for i in range(len(q)):
                tmp_root, cache = q.pop(0)
                # NOTE : this condition
                if tmp_root and not tmp_root.left and not tmp_root.right:
                    cache += ("->" + str(tmp_root.val))
                    res.append(cache)
                """
                NOTE !!! we append tmp_root.val to cache within left, right sub tree op
                """
                if tmp_root.left:
                    q.append([tmp_root.left, cache + "->" + str(tmp_root.val)])
                if tmp_root.right:
                    q.append([tmp_root.right, cache + "->" + str(tmp_root.val)])

        #print ("res = " + str(res))
        #return [x.strip("->") for x in res]
        return ["->".join(x.split("->")[1:]) for x in res]

# V0
# IDEA : BFS
class Solution(object):
    def binaryTreePaths(self, root):
        # edge case
        if not root:
            return []
        cache = ""
        res = []
        q = [[cache, root]]
        while q:
            for i in range(len(q)):
                cache, tmp = q.pop(0)
                """
                NOTE this !!!

                1) condition : if tmp and not tmp.left and not tmp.right
                2) we need to append cache + str(tmp.val) to res
                   -> since we also need to collect "bottom" sbt-tree's val
                """
                if tmp and not tmp.left and not tmp.right:
                    res.append(cache + str(tmp.val))
                """
                NOTE !!! we append tmp_root.val to cache within left, right sub tree op
                """
                if tmp.left:
                    q.append([ cache + "{}->".format(tmp.val), tmp.left])
                if tmp.right:
                    q.append([ cache + "{}->".format(tmp.val), tmp.right])

        #print ("res = " + str(res))
        return res

# V0'
# IDEA : BFS
class Solution:
    def binaryTreePaths(self, root):
        res = []
        ### NOTE : we set q like this : [[root, cur]]
        cur = ""
        q = [[root, cur]]
        while q:
            for i in range(len(q)):
                node, cur = q.pop(0)
                ### NOTE : if node exist, but no sub tree (i.e. not root.left and not root.right)
                #         -> append cur to result
                if node:
                    if not node.left and not node.right:
                        res.append(cur + str(node.val))
                ### NOTE : we keep cur to left sub tree
                if node.left:
                    q.append((node.left, cur + str(node.val) + '->'))
                ### NOTE : we keep cur to left sub tree
                if node.right:
                    q.append((node.right, cur + str(node.val) + '->'))
        return res

# V0'
# IDEA : DFS 
class Solution:
    def binaryTreePaths(self, root):
        ans = []
        def dfs(r, tmp):
            if r.left:
                dfs(r.left, tmp + [str(r.left.val)])
            if r.right:
                dfs(r.right, tmp + [str(r.right.val)])
            if not r.left and not r.right:
                ans.append('->'.join(tmp))
        if not root:
            return []
        dfs(root, [str(root.val)])
        return ans

# V1
# IDEA : DFS
# https://leetcode.com/problems/binary-tree-paths/discuss/68335/Python-DFS
class Solution:
    def binaryTreePaths(self, root):
        ans = []
        def dfs(r, tmp):
            if r.left:
                dfs(r.left, tmp + [str(r.left.val)])
            if r.right:
                dfs(r.right, tmp + [str(r.right.val)])
            if not r.left and not r.right:
                ans.append('->'.join(tmp))
        if not root:
            return []
        dfs(root, [str(root.val)])
        return ans

# V1'
# IDEA : DFS
# https://leetcode.com/problems/binary-tree-paths/discuss/237550/Python-solution
class Solution:
    def binaryTreePaths(self, root):
        def dfs(root):
            if not root:
                return
            if not root.left and not root.right:
                tmp.append(str(root.val))
                res.append("->".join(tmp))
                tmp.pop()
                return
            tmp.append(str(root.val))
            dfs(root.left)
            dfs(root.right)
            tmp.pop()
        tmp = []
        res = []
        dfs(root)
        return res

# V1''
# https://blog.csdn.net/coder_orz/article/details/51706119
# DEMO
# In [14]: x
# Out[14]: [1, 2, 3]
# In [15]: res = []
# In [16]: res.append('->'.join("123"))
# In [17]: res
# Out[17]: ['1->2->3']
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

# V1'''
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

# V1'''''
# https://blog.csdn.net/coder_orz/article/details/51706119
# IDEA : BFS (RECURSION)
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

# V1'''''''
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

# V1'''''''''  
# https://www.jiuzhang.com/solution/binary-tree-paths/#tag-highlight-lang-python
class Solution:
    """
    @param root: the root of the binary tree
    @return: all root-to-leaf paths
    """
    def binaryTreePaths(self, root):
        if root is None:
            return []
            
        result = []
        self.dfs(root, [str(root.val)], result)
        return result

    def dfs(self, node, path, result):
        if node.left is None and node.right is None:
            result.append('->'.join(path))
            return

        if node.left:
            path.append(str(node.left.val))
            self.dfs(node.left, path, result)
            path.pop()
        
        if node.right:
            path.append(str(node.right.val))
            self.dfs(node.right, path, result)
            path.pop() 

# V1''''''''
# https://www.jiuzhang.com/solution/binary-tree-paths/#tag-highlight-lang-python
class Solution:
    """
    @param root: the root of the binary tree
    @return: all root-to-leaf paths
    """
    def binaryTreePaths(self, root):
        if root is None:
            return []
            
        if root.left is None and root.right is None:
            return [str(root.val)]

        leftPaths = self.binaryTreePaths(root.left)
        rightPaths = self.binaryTreePaths(root.right)
        
        paths = []
        for path in leftPaths + rightPaths:
            paths.append(str(root.val) + '->' + path)
            
        return paths

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