# V0

# V1 
# http://bookshadow.com/weblog/2017/08/21/leetcode-maximum-width-of-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def widthOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        q = [(root, 1)]
        ans = 0
        while q:
            width = q[-1][-1] - q[0][-1] + 1
            ans = max(ans, width)
            q0 = []
            for n, i in q:
                if n.left: q0.append((n.left, i * 2))
                if n.right: q0.append((n.right, i * 2 + 1))
            q = q0
        return ans

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79645897
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def widthOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        queue = collections.deque()
        queue.append((root, 1))
        res = 0
        while queue:
            width = queue[-1][1] - queue[0][1] + 1
            res = max(width, res)
            for _ in range(len(queue)):
                n, c = queue.popleft()
                if n.left: queue.append((n.left, c * 2))
                if n.right: queue.append((n.right, c * 2 + 1))
        return res

# V3 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def widthOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def dfs(node, i, depth, leftmosts):
            if not node:
                return 0
            if depth >= len(leftmosts):
                leftmosts.append(i)
            return max(i-leftmosts[depth]+1, \
                       dfs(node.left, i*2, depth+1, leftmosts), \
                       dfs(node.right, i*2+1, depth+1, leftmosts))

        leftmosts = []
        return dfs(root, 1, 0, leftmosts)