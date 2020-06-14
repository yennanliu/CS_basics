# V0
# IDEA : DFS
# DEMO : defaultdict
# In [26]: import collections 
# In [27]: m = collections.defaultdict(int)
#
# In [28]: m
# Out[28]: defaultdict(int, {})
#
# In [29]: m[0]
# Out[29]: 0
#
# In [30]: m
# Out[30]: defaultdict(int, {0: 0})
#
# In [31]: m[1]
# Out[31]: 0
#
# In [32]: m
# Out[32]: defaultdict(int, {0: 0, 1: 0})
#
# In [33]: m[1]=1
#
# In [34]: m
# Out[34]: defaultdict(int, {0: 0, 1: 1})
#
# In [35]: m[2] +=1
#
# In [36]: m
# Out[36]: defaultdict(int, {0: 0, 1: 1, 2: 1})
import collections
class Solution(object):
    def findDuplicateSubtrees(self, root):
        res = []
        m = collections.defaultdict(int)
        self.dfs(root, m, res)
        return res

    def dfs(self, root, m, res):
        if not root:
            return '#'
        ### Notice here 
        # every "self.dfs" means a "new start", so it will re-consider the duplicated sub-tree from current node
        # i.e. : self.dfs(root.left, m, res) will re-consider the duplicated sub-tree left node
        path = str(root.val) + '-' + self.dfs(root.left, m, res) + '-' + self.dfs(root.right, m, res)
        ### Notice here
        # if m[path] == 1 means if there is already one element in the m 
        #   -> so res append the path  (if m[path] == 1)
        # becasue it means "deplicate"
        if m[path] == 1:
            res.append(root) 
        m[path] += 1
        return path

# V0'
import collections
class Solution(object):
    def findDuplicateSubtrees(self, root):
        self.res = []
        self.m = collections.defaultdict(int)
        self.dfs(root)
        return self.res

    def dfs(self, root):
        if not root:
            return ''
        path = str(root.val) + '-' + self.dfs(root.left) + '-' + self.dfs(root.right)
        if self.m[path] == 1:
            self.res.append(root) 
        self.m[path] += 1
        return path

# V0''
# IDEA : DFS 
class Solution(object):
    def findDuplicateSubtrees(self, root):
        count = collections.Counter()
        ans = []
        def dfs(node):
            if not node: return "#"
            serial = "{}-{}-{}".format(node.val, dfs(node.left), dfs(node.right))
            count[serial] += 1
            if count[serial] == 2:
                ans.append(node)
            return serial

        dfs(root)
        return ans

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81053453
# IDEA : DFS
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def findDuplicateSubtrees(self, root):
        """
        :type root: TreeNode
        :rtype: List[TreeNode]
        """
        res = []
        m = collections.defaultdict(int)
        self.helper(root, m, res)
        return res

    def helper(self, root, m, res):
        if not root:
            return '#'
        path = str(root.val) + ',' + self.helper(root.left, m, res) + ',' + self.helper(root.right, m, res)
        ### Notice here
        # if m[path] == 1 means if there is already one element in the m 
        #   -> so res append the path  (if m[path] == 1)
        # becasue it means "deplicate"
        if m[path] == 1:
            res.append(root) 
        m[path] += 1
        return path

### Test case : dev 

# V1'
# IDEA : DFS 
# https://leetcode.com/problems/find-duplicate-subtrees/solution/
class Solution(object):
    def findDuplicateSubtrees(self, root):
        count = collections.Counter()
        ans = []
        def collect(node):
            if not node: return "#"
            serial = "{},{},{}".format(node.val, collect(node.left), collect(node.right))
            count[serial] += 1
            if count[serial] == 2:
                ans.append(node)
            return serial

        collect(root)
        return ans
     
# V1''
# IDEA :  Unique Identifier
# https://leetcode.com/problems/find-duplicate-subtrees/solution/
class Solution(object):
    def findDuplicateSubtrees(self, root):
        trees = collections.defaultdict()
        trees.default_factory = trees.__len__
        count = collections.Counter()
        ans = []
        def lookup(node):
            if node:
                uid = trees[node.val, lookup(node.left), lookup(node.right)]
                count[uid] += 1
                if count[uid] == 2:
                    ans.append(node)
                return uid
        lookup(root)
        return ans

# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def findDuplicateSubtrees(self, root):
        """
        :type root: TreeNode
        :rtype: List[TreeNode]
        """
        def getid(root, lookup, trees):
            if root:
                node_id = lookup[root.val, \
                                 getid(root.left, lookup, trees), \
                                 getid(root.right, lookup, trees)]
                trees[node_id].append(root)
                return node_id
        trees = collections.defaultdict(list)
        lookup = collections.defaultdict()
        lookup.default_factory = lookup.__len__
        getid(root, lookup, trees)
        return [roots[0] for roots in trees.values() if len(roots) > 1]

# Time:  O(n * h)
# Space: O(n * h)
class Solution2(object):
    def findDuplicateSubtrees(self, root):
        """
        :type root: TreeNode
        :rtype: List[TreeNode]
        """
        def postOrderTraversal(node, lookup, result):
            if not node:
                return ""
            s = "(" + postOrderTraversal(node.left, lookup, result) + \
                str(node.val) + \
                postOrderTraversal(node.right, lookup, result) + \
                ")"
            if lookup[s] == 1:
                result.append(node)
            lookup[s] += 1
            return s
        lookup = collections.defaultdict(int)
        result = []
        postOrderTraversal(root, lookup, result)
        return result