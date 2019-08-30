# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/86563872
# https://blog.csdn.net/danspace1/article/details/88737508
# IDEA : 
# TOTAL MOVES = abs(coins left need) + abs(coins right need)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def distributeCoins(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.ans = 0
        
        def dfs(root):
            # return the balance of the node
            if not root: return 0
            left = dfs(root.left)
            right = dfs(root.right)
            self.ans += abs(left) + abs(right)
            return root.val -1 + left + right 
        dfs(root)
        return self.ans
        
# V2 
# Time:  O(n)
# Space: O(h)
# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

        
class Solution(object):
    def distributeCoins(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def dfs(root, result):
            if not root:
                return 0
            left, right = dfs(root.left, result), dfs(root.right, result)
            result[0] += abs(left) + abs(right)
            return root.val + left + right - 1

        result = [0]
        dfs(root, result)
        return result[0]