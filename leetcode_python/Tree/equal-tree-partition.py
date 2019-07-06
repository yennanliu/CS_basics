# V0 

# V1 
# http://bookshadow.com/weblog/2017/08/21/leetcode-equal-tree-partition/
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