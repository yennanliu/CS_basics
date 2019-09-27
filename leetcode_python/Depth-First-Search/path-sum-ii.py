# V0 
class Solution(object):
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: List[List[int]]
        """
        if not root: return []
        res = []
        self.dfs(root, sum, res, [root.val])
        return res

    def dfs(self, root, target, res, path):
        if not root: return
        if sum(path) == target and not root.left and not root.right:
            res.append(path)
            return
        if root.left:
            self.dfs(root.left, target, res, path + [root.left.val])
        if root.right:
            self.dfs(root.right, target, res, path + [root.right.val])
            
# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80779574
# IDEA : DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: List[List[int]]
        """
        if not root: return []
        res = []
        self.dfs(root, sum, res, [root.val])
        return res

    def dfs(self, root, target, res, path):
        if not root: return
        if sum(path) == target and not root.left and not root.right:
            res.append(path)
            return
        if root.left:
            self.dfs(root.left, target, res, path + [root.left.val])
        if root.right:
            self.dfs(root.right, target, res, path + [root.right.val])

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80779574
# IDEA : DFS 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: List[List[int]]
        """
        res = []
        self.dfs(root, sum, res, [])
        return res

    def dfs(self, root, target, res, path):
        if not root: return
        path += [root.val]
        if sum(path) == target and not root.left and not root.right:
            res.append(path[:])
            return
        if root.left:
            self.dfs(root.left, target, res, path[:])
        if root.right:
            self.dfs(root.right, target, res, path[:])
        path.pop(-1)

# V1''
# https://www.jiuzhang.com/solution/path-sum-ii/#tag-highlight-lang-python
class Solution:
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: List[List[int]]
        """
        # sum is overwriter, so new function my sum ....
        def mysum(nums):
            count = 0
            for n in nums:
                count += n
            return count
            
        # dfs find each path
        def findPath(root, path):
            if root.left is None and root.right is None:
                if mysum(path + [root.val]) == sum: 
                    allPath.append([t for t in path + [root.val]])      
            if root.left: findPath(root.left, path + [root.val])
            if root.right: findPath(root.right, path + [root.val])    
        allPath = []
        if root: findPath(root, [])
        return allPath

# V2 
# Time:  O(n)
# Space: O(h), h is height of binary tree
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param root, a tree node
    # @param sum, an integer
    # @return a list of lists of integers
    def pathSum(self, root, sum):
        return self.pathSumRecu([], [], root, sum)


    def pathSumRecu(self, result, cur, root, sum):
        if root is None:
            return result

        if root.left is None and root.right is None and root.val == sum:
            result.append(cur + [root.val])
            return result

        cur.append(root.val)
        self.pathSumRecu(result, cur, root.left, sum - root.val)
        self.pathSumRecu(result, cur,root.right, sum - root.val)
        cur.pop()
        return result