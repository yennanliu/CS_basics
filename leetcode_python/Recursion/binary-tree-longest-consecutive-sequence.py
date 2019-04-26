# Given a binary tree, find the length of the longest consecutive sequence path.

 

# The path refers to any sequence of nodes from some starting node to any node in the tree along the parent-child connections. The longest consecutive path need to be from parent to child (cannot be the reverse).

# For example,

#    1
#     \
#      3
#     / \
#    2   4
#         \
#          5
# Longest consecutive sequence path is 3-4-5, so return 3.

#    2
#     \
#      3
#     / 
#    2    
#   / 
#  1
# Longest consecutive sequence path is 2-3,not3-2-1, so return 2.



# V0 : DEV 

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def longestConsecutive(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.max_len = 0

        def longestConsecutiveHelper(root):
            if not root:
                return 0

            left_len = longestConsecutiveHelper(root.left)
            right_len = longestConsecutiveHelper(root.right)

            cur_len = 1
            if root.left and root.left.val == root.val + 1:
                cur_len = max(cur_len, left_len + 1)
            if root.right and root.right.val == root.val + 1:
                cur_len = max(cur_len, right_len + 1)

            self.max_len = max(self.max_len, cur_len)

            return cur_len

        longestConsecutiveHelper(root)
        return self.max_len
