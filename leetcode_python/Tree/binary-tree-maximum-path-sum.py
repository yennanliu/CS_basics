"""

124. Binary Tree Maximum Path Sum
Hard

A path in a binary tree is a sequence of nodes where each pair of adjacent nodes in the sequence has an edge connecting them. A node can only appear in the sequence at most once. Note that the path does not need to pass through the root.

The path sum of a path is the sum of the node's values in the path.

Given the root of a binary tree, return the maximum path sum of any non-empty path.

 

Example 1:


Input: root = [1,2,3]
Output: 6
Explanation: The optimal path is 2 -> 1 -> 3 with a path sum of 2 + 1 + 3 = 6.
Example 2:


Input: root = [-10,9,20,null,null,15,7]
Output: 42
Explanation: The optimal path is 15 -> 20 -> 7 with a path sum of 15 + 20 + 7 = 42.
 

Constraints:

The number of nodes in the tree is in the range [1, 3 * 104].
-1000 <= Node.val <= 1000

"""

# V0

# V1
# IDEA : DFS
# https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/209995/Python-solution
class Solution(object):
    def maxPathSum(self, root):
        def dfs(root):
            if not root:
                return 0
            l_max = dfs(root.left)
            r_max = dfs(root.right)
            if l_max < 0:
                l_max = root.val
            else:
                l_max += root.val
            if r_max < 0:
                r_max = root.val
            else:
                r_max += root.val
            self.maximum = max(self.maximum, l_max+r_max-root.val)
            return max(l_max, r_max)
           
        self.maximum = -float('inf')
        dfs(root)
        return self.maximum 

# V1'
# IDEA : DFS
# https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/209995/Python-solution
class Solution(object):
    def maxPathSum(self, root):
        def dfs(root):
            if not root:
                return 0, -float('inf')
            l_max, res_l = dfs(root.left)
            r_max, res_r = dfs(root.right)
            if l_max < 0:
                l_max = root.val
            else:
                l_max += root.val
            if r_max < 0:
                r_max = root.val
            else:
                r_max += root.val
            maximum = l_max+r_max-root.val
            return max(l_max, r_max), max(maximum, res_l, res_r)
        
        res = dfs(root)[1]
        return res if res != -float('inf') else 0

# V1''
# IDEA : DFS + bottom up
# https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/329033/Python-bottom-up-DFS-solution
class Solution(object):
    def maxPathSum(self, root):
        def maxSum(root):
            if not root:
                return 0
            l_sum = maxSum(root.left)
            r_sum = maxSum(root.right)
            l = max(0, l_sum)
            r = max(0, r_sum)
            res[0] = max(res[0], root.val + l + r)
            return root.val + max(l, r)
        
        res = [-float('inf')]
        maxSum(root)
        return res[0]

# V1'''
# https://leetcode.com/problems/binary-tree-maximum-path-sum/discuss/767709/Python-easy-as-hell-solution
class Solution(object):
    max_path = 0
    def maxPathSum(self, root):
        self.max_path = root.val if root else 0
        def findMaxPath(root):
            if not root: return 0
            
            left = findMaxPath(root.left)
            right = findMaxPath(root.right)
            self.max_path = max([self.max_path, root.val, root.val + left, root.val + right, root.val + left + right])

            return max([root.val, root.val + left, root.val + right])

        findMaxPath(root)
        return self.max_path

# V2