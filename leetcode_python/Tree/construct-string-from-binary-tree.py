# V0

# V1 
# http://bookshadow.com/weblog/2017/06/04/leetcode-construct-string-from-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def tree2str(self, t):
        """
        :type t: TreeNode
        :rtype: str
        """
        if not t: return ''
        ans = str(t.val)
        if t.left or t.right: ans += '(' + self.tree2str(t.left) + ')'
        if t.right: ans += '(' + self.tree2str(t.right) + ')'
        return ans

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def tree2str(self, t):
        """
        :type t: TreeNode
        :rtype: str
        """
        if not t: return ""
        s = str(t.val)
        if t.left or t.right:
            s += "(" + self.tree2str(t.left) + ")"
        if t.right:
            s += "(" + self.tree2str(t.right) + ")"
        return s