# V0
# IDEA : DFS 
# Explanation
# As we need to reach every node in the given tree, we will have to traverse the tree, either with a depth-first search, or with a breadth-first search.
# The main idea in this question is to give each node a position value. If we go down the left neighbor, then position -> position * 2; and if we go down the right neighbor, then position -> position * 2 + 1. This makes it so that when we look at the position values L and R of two nodes with the same depth, the width will be R - L + 1.
# DEMO : setdefault()
# https://www.w3schools.com/python/ref_dictionary_setdefault.asp
# The setdefault() method returns the value of the item with the specified key.
# If the key does not exist, insert the key, with the specified value, see example below
# car = {
#   "brand": "Ford",
#   "model": "Mustang",
#   "year": 1964
# }
#
# # get key "brand" value
# print (car.setdefault("brand"))
# # insert key "my_key"
# print (car.setdefault("my_key"))
# print (car)
# # add new key "color" and its value white
# print (car.setdefault("color", "white"))
# print (car)
class Solution(object):
    def widthOfBinaryTree(self, root):
        self.ans = 0
        left = {}
        def dfs(node, depth = 0, pos = 0):
            if node:
                left.setdefault(depth, pos)
                self.ans = max(self.ans, pos - left[depth] + 1)
                dfs(node.left, depth + 1, pos * 2)
                dfs(node.right, depth + 1, pos * 2 + 1)

        dfs(root)
        return self.ans

# V0'
# IDEA : DFS 
class Solution(object):
    def widthOfBinaryTree(self, root):
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

# V1
# https://www.jiuzhang.com/solution/maximum-width-of-binary-tree/#tag-highlight-lang-python
# IDEA : GIVEN index = idx -> its left tree index = idx*2 ; its right tree index = idx*2 + 1
#        -> SO GO THROUGH ALL LAYERS IN THE TREE, CALCULATE THEIR WIDTH, AND RETRUN THE MAX WIDTH WHICH IS THE NEEDED RESPONSE
# DEMO :
# given TREE : 
#          1
#        /   \
#       3     2
#      / \     \  
#     5   3     9 
#
# -> its index 
#
#          0
#        /   \
#       0     1
#      / \   / \  
#     0   1 2   3  
#
# -> remove the null ones 
#          0
#        /   \
#       0     1
#      / \     \  
#     0   1     3 
class Solution(object):
    def widthOfBinaryTree(self, root):
        queue = [(root, 0, 0)]
        cur_depth = left = ans = 0
        for node, depth, pos in queue:
            if node:
                queue.append((node.left, depth+1, pos*2))
                queue.append((node.right, depth+1, pos*2 + 1))
                if cur_depth != depth:
                    cur_depth = depth
                    left = pos
                ans = max(pos - left + 1, ans)
        return ans

### Test case : dev

# V1'
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

# V1''
# https://leetcode.com/problems/maximum-width-of-binary-tree/solution/
# IDEA : BFS 
# Time Complexity: O(N)
# Space Complexity: O(N)
class Solution(object):
    def widthOfBinaryTree(self, root):
        queue = [(root, 0, 0)]
        cur_depth = left = ans = 0
        for node, depth, pos in queue:
            if node:
                queue.append((node.left, depth+1, pos*2))
                queue.append((node.right, depth+1, pos*2 + 1))
                if cur_depth != depth:
                    cur_depth = depth
                    left = pos
                ans = max(pos - left + 1, ans)
        return ans

# V1'''
# https://leetcode.com/problems/maximum-width-of-binary-tree/solution/
# IDEA : DFS 
# Time Complexity: O(N)
# Space Complexity: O(N)
class Solution(object):
    def widthOfBinaryTree(self, root):
        self.ans = 0
        left = {}
        def dfs(node, depth = 0, pos = 0):
            if node:
                left.setdefault(depth, pos)
                self.ans = max(self.ans, pos - left[depth] + 1)
                dfs(node.left, depth + 1, pos * 2)
                dfs(node.right, depth + 1, pos * 2 + 1)

        dfs(root)
        return self.ans

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