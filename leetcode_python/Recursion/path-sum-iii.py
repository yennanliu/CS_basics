# V0 
# IDEA : BFS + DFS 
# => USE BFS FIND EVERY NODE IN THE TREE, AND USE DFS GET THR PATH SUM ON EVERY NODE (FOUND BY BFS)
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
        :rtype: int
        """
        res = [0]
        que = collections.deque()
        que.append(root)
        while que:
            node = que.popleft()
            if not node:
                continue
            self.dfs(node, res, 0, sum)
            que.append(node.left)
            que.append(node.right)
        return res[0]
    
    def dfs(self, root, res, path, target):
        if not root: return
        path += root.val
        if path == target:
            res[0] += 1
        self.dfs(root.left, res, path, target)
        self.dfs(root.right, res, path, target)

# V1 
# https://blog.csdn.net/xiaoxiaoley/article/details/79093996
class Solution(object):    
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: int
        """
        def dfs(root,sum):
            if root==None:
                return 0
            if root.val==sum:
                return 1+dfs(root.left,0)+dfs(root.right,0)
            return dfs(root.left,sum-root.val)+dfs(root.right,sum-root.val)
        
        if root==None:
            return 0
        return dfs(root,sum)+self.pathSum(root.left,sum)+self.pathSum(root.right,sum)

# V1'
# https://www.jiuzhang.com/solution/path-sum-iii/#tag-highlight-lang-python
class Solution:
    """
    @param root: 
    @param sum: 
    @return: nothing
    """
    def DFS(self, root, su, tmp):
        if None==root:
            return 0
        else:
            flag=0
            if su==tmp+root.val:
                flag=1
            return flag+self.DFS(root.left, su, tmp+root.val)+self.DFS(root.right, su, tmp+root.val)
    
    def pathSum(self, root, su):
        #write your code here
        if None==root:
            return 0
        else:
            return self.DFS(root, su, 0)+self.pathSum(root.left, su)+self.pathSum(root.right, su)

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/71097135
class Solution(object):
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: int
        """
        if not root: return 0
        return self.dfs(root, sum) + self.pathSum(root.left, sum) + self.pathSum(root.right, sum)
    
    def dfs(self, root, sum):
        res = 0
        if not root: return res
        sum -= root.val
        if sum == 0:
            res += 1
        res += self.dfs(root.left, sum)
        res += self.dfs(root.right, sum)
        return res

# V2' : BFS + DFS 
class Solution(object):
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: int
        """
        res = [0]
        que = collections.deque()
        que.append(root)
        while que:
            node = que.popleft()
            if not node:
                continue
            self.dfs(node, res, 0, sum)
            que.append(node.left)
            que.append(node.right)
        return res[0]
    
    def dfs(self, root, res, path, target):
        if not root: return
        path += root.val
        if path == target:
            res[0] += 1
        self.dfs(root.left, res, path, target)
        self.dfs(root.right, res, path, target)

# V3
# Time:  O(n)
# Space: O(h)
import collections
class Solution(object):
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: int
        """
        def pathSumHelper(root, curr, sum, lookup):
            if root is None:
                return 0
            curr += root.val
            result = lookup[curr-sum] if curr-sum in lookup else 0
            lookup[curr] += 1
            result += pathSumHelper(root.left, curr, sum, lookup) + \
                      pathSumHelper(root.right, curr, sum, lookup)
            lookup[curr] -= 1
            if lookup[curr] == 0:
                del lookup[curr]
            return result

        lookup = collections.defaultdict(int)
        lookup[0] = 1
        return pathSumHelper(root, 0, sum, lookup)

# Time:  O(n^2)
# Space: O(h)
class Solution2(object):
    def pathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: int
        """
        def pathSumHelper(root, prev, sum):
            if root is None:
                return 0

            curr = prev + root.val
            return int(curr == sum) + \
                   pathSumHelper(root.left, curr, sum) + \
                   pathSumHelper(root.right, curr, sum)

        if root is None:
            return 0

        return pathSumHelper(root, 0, sum) + \
               self.pathSum(root.left, sum) + \
               self.pathSum(root.right, sum)
