"""

1120. Maximum Average Subtree
Easy

Given the root of a binary tree, return the maximum average value of a subtree of that tree.
Answers within 10^-5 of the actual answer will be accepted.

A subtree of a tree is any node of that tree plus all its descendants.

The average value of a tree is the sum of its values, divided by the number of nodes.


Example 1:

Input: root = [5,6,1]
Output: 6.00000
Explanation:
For the node with value = 5 we have an average of (5 + 6 + 1) / 3 = 4.
For the node with value = 6 we have an average of 6 / 1 = 6.
For the node with value = 1 we have an average of 1 / 1 = 1.
So the answer is 6 which is the maximum.

Example 2:

Input: root = [0,null,1]
Output: 1.00000


Constraints:

The number of nodes in the tree is in the range [1, 10^4].
0 <= Node.val <= 10^5

"""

# V0
# IDEA : POST-ORDER DFS (each node returns (subtree sum, subtree size))
#
#   the average of a subtree needs 2 aggregates : sum and node count.
#   both compose bottom-up :
#     sum(node)  = node.val + sum(left)  + sum(right)
#     size(node) = 1        + size(left) + size(right)
#
#   so one post-order pass computes every subtree's average and we keep
#   the running max.
#   NOTE : use float() before dividing, otherwise python2 does integer
#          division and the averages get truncated.
#
# time = O(n), space = O(h)   h = tree height (recursion stack)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def maximumAverageSubtree(self, root):
        self.res = 0.0

        def dfs(node):
            if not node:
                return 0, 0
            l_sum, l_cnt = dfs(node.left)
            r_sum, r_cnt = dfs(node.right)
            cur_sum = node.val + l_sum + r_sum
            cur_cnt = 1 + l_cnt + r_cnt
            self.res = max(self.res, float(cur_sum) / cur_cnt)
            return cur_sum, cur_cnt

        dfs(root)
        return self.res
