# V0 : dev 

# V1 
# https://www.hrwhisper.me/leetcode-house-robber-iii/
class Solution(object):
    def rob(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        return self.dfs_rob(root)[0]

    def dfs_rob(self, root):
        if not root: return 0,0
        rob_L, no_rob_L = self.dfs_rob(root.left)
        rob_R, no_rob_R = self.dfs_rob(root.right)
        return max(no_rob_L + no_rob_R + root.val , rob_L + rob_R), rob_L + rob_R

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def rob(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def robHelper(root):
            if not root:
                return (0, 0)
            left, right = robHelper(root.left), robHelper(root.right)
            return (root.val + left[1] + right[1], max(left) + max(right))

        return max(robHelper(root))
