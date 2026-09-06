"""

979. Distribute Coins in Binary Tree
Medium
Topics
premium lock icon
Companies
You are given the root of a binary tree with n nodes where each node in the tree has node.val coins. There are n coins in total throughout the whole tree.

In one move, we may choose two adjacent nodes and move one coin from one node to another. A move may be from parent to child, or from child to parent.

Return the minimum number of moves required to make every node have exactly one coin.

 

Example 1:


Input: root = [3,0,0]
Output: 2
Explanation: From the root of the tree, we move one coin to its left child, and one coin to its right child.
Example 2:


Input: root = [0,3,0]
Output: 3
Explanation: From the left child of the root, we move two coins to the root [taking two moves]. Then, we move one coin from the root of the tree to the right child.
 

Constraints:

The number of nodes in the tree is n.
1 <= n <= 100
0 <= Node.val <= n
The sum of all Node.val is n.
 

"""


# V0 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def distributeCoins(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: int
        """
        pass


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
# time = O(n)
# space = O(h)  # h = tree height, worst O(n)
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
# time = O(n)
# space = O(h)
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
