# V0
class Solution(object):
    def longestUnivaluePath(self, root):
        longest = [0]
        def dfs(node):
            if not node:
                return 0
            left_len, right_len = dfs(node.left), dfs(node.right)
            ### NOTICE : not only node.left exist, but node.left.val == node.val
            if node.left and node.left.val == node.val:
                left = (left_len + 1) 
            else:
                left = 0
            ### NOTICE : not only node.right exist, but node.right.val == node.val
            if node.right and node.right.val == node.val: 
                right = (right_len + 1)
            else:
                right = 0 
            longest[0] = max(longest[0], left + right)
            return max(left, right)
        dfs(root)
        return longest[0]

# V1
# https://leetcode.com/problems/longest-univalue-path/discuss/108142/Python-Simple-to-Understand
# IDEA : DFS
class Solution(object):
    def longestUnivaluePath(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        # Time: O(n)
        # Space: O(n)
        longest = [0]
        def traverse(node):
            if not node:
                return 0
            left_len, right_len = traverse(node.left), traverse(node.right)
            left = (left_len + 1) if node.left and node.left.val == node.val else 0
            right = (right_len + 1) if node.right and node.right.val == node.val else 0
            longest[0] = max(longest[0], left + right)
            return max(left, right)
        traverse(root)
        return longest[0]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79248926
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def longestUnivaluePath(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        longest = [0]
        def dfs(root):
            if not root:
                return 0
            left_len, right_len = dfs(root.left), dfs(root.right)
            left = left_len + 1 if root.left and root.left.val == root.val else 0
            right = right_len + 1 if root.right and root.right.val == root.val else 0
            longest[0] = max(longest[0], left + right)
            return max(left, right)
        dfs(root)
        return longest[0]

### Test case : dev 

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79248926
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def longestUnivaluePath(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root: return 0
        self.res = 0
        self.getPath(root)
        return self.res
    
    def getPath(self, root):
        if not root: return 0
        left = self.getPath(root.left)
        right = self.getPath(root.right)
        pl, pr = 0, 0
        if root.left and root.left.val == root.val: pl = 1 + left
        if root.right and root.right.val == root.val: pr = 1 + right
        self.res = max(self.res, pl + pr)
        return max(pl, pr)

# V1'''
# https://www.jiuzhang.com/solution/longest-univalue-path/#tag-highlight-lang-python
class Solution(object):
    # dfs go through all left and right sub trees 
    def do_dfs(self, root, answer):
        
        if not root:
            return 0
        
        p1 = self.do_dfs(root.left, answer)
        p2 = self.do_dfs(root.right, answer)

        l,r = 0,0
        if root.left and root.left.val == root.val:
            l = p1+1
        
        if root.right and root.right.val == root.val:
            r = p2+1
        
        answer[0] = max(answer[0] , l+r)
        return max(l,r)
                
    def longestUnivaluePath(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root:
            return 0
        
        answer = [-1]
        self.do_dfs(root, answer)
        return answer[0]

# V1''''
# https://leetcode.com/problems/longest-univalue-path/solution/
# IDEA : RECURSION
# Time Complexity: O(N)
# Space Complexity: O(H) 
class Solution(object):
    def longestUnivaluePath(self, root):
        self.ans = 0

        def arrow_length(node):
            if not node: return 0
            left_length = arrow_length(node.left)
            right_length = arrow_length(node.right)
            left_arrow = right_arrow = 0
            if node.left and node.left.val == node.val:
                left_arrow = left_length + 1
            if node.right and node.right.val == node.val:
                right_arrow = right_length + 1
            self.ans = max(self.ans, left_arrow + right_arrow)
            return max(left_arrow, right_arrow)

        arrow_length(root)
        return self.ans

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def longestUnivaluePath(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        result = [0]
        def dfs(node):
            if not node:
                return 0
            left, right = dfs(node.left), dfs(node.right)
            left = (left+1) if node.left and node.left.val == node.val else 0
            right = (right+1) if node.right and node.right.val == node.val else 0
            result[0] = max(result[0], left+right)
            return max(left, right)

        dfs(root)
        return result[0]