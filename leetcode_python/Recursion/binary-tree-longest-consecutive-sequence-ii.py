# Given a binary tree, you need to find the length of Longest Consecutive Path in Binary Tree.

# Especially, this path can be either increasing or decreasing. For example, [1,2,3,4] and [4,3,2,1] are both considered valid, but the path [1,2,4,3] is not valid. On the other hand, the path can be in the child-Parent-child order, where not necessarily be parent-child order.

# Example 1:

# Input:
#         1
#        / \
#       2   3
# Output: 2
# Explanation: The longest consecutive path is [1, 2] or [2, 1].
# Example 2:

# Input:
#         2
#        / \
#       1   3
# Output: 3
# Explanation: The longest consecutive path is [1, 2, 3] or [3, 2, 1].
# Note: All the values of tree nodes are in the range of [-1e7, 1e7].


# V0 : DEV 

# V1 

# V2 
# Time:  O(n)
# Space: O(h)

class Solution(object):
    def longestConsecutive(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def longestConsecutiveHelper(root):
            if not root:
                return 0, 0
            left_len = longestConsecutiveHelper(root.left)
            right_len = longestConsecutiveHelper(root.right)
            cur_inc_len, cur_dec_len = 1, 1
            if root.left:
                if root.left.val == root.val + 1:
                    cur_inc_len = max(cur_inc_len, left_len[0] + 1)
                elif root.left.val == root.val - 1:
                    cur_dec_len = max(cur_dec_len, left_len[1] + 1)
            if root.right:
                if root.right.val == root.val + 1:
                    cur_inc_len = max(cur_inc_len, right_len[0] + 1)
                elif root.right.val == root.val - 1:
                    cur_dec_len = max(cur_dec_len, right_len[1] + 1)
            self.max_len = max(self.max_len, cur_dec_len + cur_inc_len - 1)
            return cur_inc_len, cur_dec_len

        self.max_len = 0
        longestConsecutiveHelper(root)
        return self.max_len
