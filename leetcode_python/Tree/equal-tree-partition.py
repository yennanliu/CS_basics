# V0 
# IDEA : GET THE SUM LIST (self.mp = {}), 
#        -> THEN CHECK IF THERE EXIST ANY i, j IN  self.mp 
#        -> SUCH THAT i = j/2 
class Solution:
    def checkEqualTree(self, root):
        self.mp = {}
        sum = self.dfs(root)
        if(sum == 0):
            return self.mp[0] > 1
        return sum % 2 == 0 and not self.mp.get(sum / 2) == None

    def dfs(self, root):
        if(root == None):
            return 0
        sum = root.val + self.dfs(root.left) + self.dfs(root.right)
        if(self.mp.get(sum) == None):
            self.mp[sum] = 1
        else:
            self.mp[sum] += 1
        return sum

# V1 
# http://bookshadow.com/weblog/2017/08/21/leetcode-equal-tree-partition/
# IDEA : GET THE SUM OF ALL TREE, CHECK IF THERE IS SUB TREE SUM == 2* SUM 
# PROCESS : 
# STEP 1) GET THE SUM OF TREE
# STEP 2) GO THROGH EACH ELEMENT, CHECK IF THE SUM OF ALL THEIR SUB TREE = 2* (SUM OF TOTAL TREE) 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def checkEqualTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        self.dmap = {}
        def solve(n, c):
            if not n: return 0
            self.dmap[c] = n.val + solve(n.left, c * 2) + solve(n.right, c * 2 + 1)
            return self.dmap[c]
        solve(root, 1)
        total = self.dmap[1]
        del self.dmap[1]
        return any(v * 2 == total for k, v in self.dmap.iteritems())

# V1'
# https://www.jiuzhang.com/solution/equal-tree-partition/#tag-highlight-lang-python
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        self.val = val
        self.left, self.right = None, None
"""
class Solution:
    """
    @param root: a TreeNode
    @return: return a boolean
    """
    def checkEqualTree(self, root):
        self.mp = {}
        sum = self.dfs(root)
        if(sum == 0):
            return self.mp[0] > 1
        return sum % 2 == 0 and not self.mp.get(sum / 2) == None

    def dfs(self, root):
        if(root == None):
            return 0
        sum = root.val + self.dfs(root.left) + self.dfs(root.right)
        if(self.mp.get(sum) == None):
            self.mp[sum] = 1
        else:
            self.mp[sum] += 1
        return sum

# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def checkEqualTree(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        def getSumHelper(node, lookup):
            if not node:
                return 0
            total = node.val + \
                    getSumHelper(node.left, lookup) + \
                    getSumHelper(node.right, lookup)
            lookup[total] += 1
            return total

        lookup = collections.defaultdict(int)
        total = getSumHelper(root, lookup)
        if total == 0:
            return lookup[total] > 1
        return total%2 == 0 and (total/2) in lookup