# https://leetcode.ca/all/549.html

"""

549. Binary Tree Longest Consecutive Sequence II
Given a binary tree, you need to find the length of Longest Consecutive Path in Binary Tree.

Especially, this path can be either increasing or decreasing. For example, [1,2,3,4] and [4,3,2,1] are both considered valid, but the path [1,2,4,3] is not valid. On the other hand, the path can be in the child-Parent-child order, where not necessarily be parent-child order.

Example 1:

Input:
        1
       / \
      2   3
Output: 2
Explanation: The longest consecutive path is [1, 2] or [2, 1].
 

Example 2:

Input:
        2
       / \
      1   3
Output: 3
Explanation: The longest consecutive path is [1, 2, 3] or [3, 2, 1].
 

Note: All the values of tree nodes are in the range of [-1e7, 1e7].

Difficulty:
Medium
Lock:
Prime
Company:
Amazon Apple Facebook Google


"""



# V0
class Solution(object):
    def longestConsecutive(self, root):
        pass


# V1

# V2-1
# IDEA: DFS
# https://leetcode.ca/2017-06-01-549-Binary-Tree-Longest-Consecutive-Sequence-II/
class Solution(object):
    def longestConsecutive(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """

        def dfs(root):
            if not root:
                # val, increasing length, decreasing length, max length
                return float('-inf'), 0, 0, 0
            # inc/dec starting from root
            inc = dec = 1
            left, leftInc, leftDec, leftMax = dfs(root.left)
            right, rightInc, rightDec, rightMax = dfs(root.right)
            if root.val + 1 == left:
                inc = max(leftInc + 1, inc)
            if root.val - 1 == left:
                dec = max(leftDec + 1, dec)
            if root.val + 1 == right:
                inc = max(rightInc + 1, inc)
            if root.val - 1 == right:
                dec = max(rightDec + 1, dec)
            # note: max may occur in children subtree, not current root's tree
            return root.val, inc, dec, max(inc + dec - 1, leftMax, rightMax, inc, dec)
            # -1 because root is counted twice, even left/right both null

        return dfs(root)[3]



# V2-2
# IDEA: DFS
# https://leetcode.ca/2017-06-01-549-Binary-Tree-Longest-Consecutive-Sequence-II/
class Solution:
    def longestConsecutive(self, root: TreeNode) -> int:
        def dfs(root):
            if root is None:
                return [0, 0]
            nonlocal ans
            incr = decr = 1
            i1, d1 = dfs(root.left)
            i2, d2 = dfs(root.right)
            if root.left:
                if root.left.val + 1 == root.val:
                    incr = i1 + 1
                if root.left.val - 1 == root.val:
                    decr = d1 + 1
            if root.right:
                if root.right.val + 1 == root.val:
                    incr = max(incr, i2 + 1) # incr1 and incr2 must be in different tree branches, eg. left child cannot be both increasing and decreasing. Same as decr1 and decr2
                if root.right.val - 1 == root.val:
                    decr = max(decr, d2 + 1)
            ans = max(ans, incr + decr - 1) # -1 because root is counted twice, even left/right both null
            return [incr, decr]

        ans = 0
        dfs(root)
        return ans


# V2-3
# IDEA
# https://leetcode.ca/2017-06-01-549-Binary-Tree-Longest-Consecutive-Sequence-II/
lass Solution: # issue with this solution, duplicated dfs() search
    def longestConsecutive(self, root: TreeNode) -> int:
        def helper(node: TreeNode, diff: int) -> int:
            if not node:
                return 0

            left = right = 0
            if node.left and node.val - node.left.val == diff:
                left = 1 + helper(node.left, diff)
            if node.right and node.val - node.right.val == diff:
                right = 1 + helper(node.right, diff)

            return max(left, right)
        if not root:
            return 0

        res = helper(root, 1) + helper(root, -1) + 1 # +1 is increasing, -1 is decreasing
        return max(res, max(longestConsecutive(root.left), longestConsecutive(root.right)))


# V3
# time = O(n)
# space = O(h), h = tree height (recursion stack)
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
