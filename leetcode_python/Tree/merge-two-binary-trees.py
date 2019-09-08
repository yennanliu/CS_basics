# V0

# V1 
# https://www.polarxiong.com/archives/LeetCode-617-merge-two-binary-trees.html
class Solution:
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        if t1 is not None and t2 is not None:
            t1.left = self.mergeTrees(t1.left, t2.left)
            t1.right = self.mergeTrees(t1.right, t2.right)
            t1.val += t2.val
            return t1
        return t1 if t2 is None else t2

# V1'
# https://www.jiuzhang.com/solution/merge-two-binary-trees/#tag-highlight-lang-python
class Solution:
    """
    @param t1: the root of the first tree
    @param t2: the root of the second tree
    @return: the new binary tree after merge
    """
    def mergeTrees(self, t1, t2):
        # Write your code here
        if t1 is None:
            return t2
        if t2 is None:
            return t1
        t3 = TreeNode(t1.val + t2.val)
        t3.left = self.mergeTrees(t1.left, t2.left)
        t3.right = self.mergeTrees(t1.right, t2.right)
        return t3

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def mergeTrees(self, t1, t2):
        """
        :type t1: TreeNode
        :type t2: TreeNode
        :rtype: TreeNode
        """
        if t1 is None:
            return t2
        if t2 is None:
            return t1
        t1.val += t2.val
        t1.left = self.mergeTrees(t1.left, t2.left)
        t1.right = self.mergeTrees(t1.right, t2.right)
        return t1