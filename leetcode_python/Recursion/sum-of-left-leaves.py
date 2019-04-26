# V0 : DEV 

# V1
# http://bookshadow.com/weblog/2016/09/25/leetcode-sum-of-left-leaves/ 
class Solution(object):
    def sumOfLeftLeaves(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        ans = 0
        if root: 
            l, r = root.left, root.right
            if l and (l.left or l.right) is None:
                ans += l.val
            ans += self.sumOfLeftLeaves(l) + self.sumOfLeftLeaves(r)
        return ans

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def sumOfLeftLeaves(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def sumOfLeftLeavesHelper(root, is_left):
            if not root:
                return 0
            if not root.left and not root.right:
                return root.val if is_left else 0
            return sumOfLeftLeavesHelper(root.left, True) + \
                   sumOfLeftLeavesHelper(root.right, False)

        return sumOfLeftLeavesHelper(root, False)

# V1' 
class Solution(object):
    def sumOfLeftLeavesHelper(self, root, is_left):
        if not root:
            return 0
        if not root.left and not root.right:
            return root.val if is_left else 0
        return self.sumOfLeftLeavesHelper(root.left, True) + \
           self.sumOfLeftLeavesHelper(root.right, False)

    def sumOfLeftLeaves(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        return self.sumOfLeftLeavesHelper(root, False)
