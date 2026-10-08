"""

222. Count Complete Tree Nodes
Medium

Given the root of a complete binary tree, return the number of the nodes in the tree.

According to Wikipedia, every level, except possibly the last, is completely filled in a complete binary tree, and all nodes in the last level are as far left as possible. It can have between 1 and 2^h nodes inclusive at the last level h.

Design an algorithm that runs in less than O(n) time complexity.

Example 1:

https://assets.leetcode.com/uploads/2021/01/14/complete.jpg

Input: root = [1,2,3,4,5,6]
Output: 6

Example 2:

Input: root = []
Output: 0

Example 3:

Input: root = [1]
Output: 1

Constraints:

The number of nodes in the tree is in the range [0, 5 * 10^4].
0 <= Node.val <= 5 * 10^4
The tree is guaranteed to be complete.

"""

# NOTE !!!
# Design an algorithm that runs in less than O(n) time complexity.

# V0 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def countNodes(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: int
        """
        
        pass


# V0-1
# IDEA: binary tree property (gemini)
class Solution(object):

  def countNodes(self, root):
    """:type root: Optional[TreeNode]

    :rtype: int
    """
    if not root:
      return 0

    left_height = self.getLeftHeight(root)
    right_height = self.getRightHeight(root)

    # 若最左深度等於最右深度，說明此樹為滿二元樹，節點數為 2^h - 1
    if left_height == right_height:

      # V1
      #return (1 << left_height) - 1

      # V2
      return 2**left_height - 1

    # 否則遞迴計算左右子樹並加上根節點自己
    return 1 + self.countNodes(root.left) + self.countNodes(root.right)

  def getLeftHeight(self, node):
    h = 0
    while node:
      h += 1
      node = node.left
    return h

  def getRightHeight(self, node):
    h = 0
    while node:
      h += 1
      node = node.right
    return h



# V0-3
# IDEA: DFS (TLE: should use < O(N) time complexity)
class Solution(object):

    def countNodes(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: int
        """
        # edge
        if not root:
            return 0

        if not root.left and not root.right:
            return 1

        self.cnt = 0

        self.helper(root)

        return self.cnt


    def helper(self, root):
        if not root:
            return
        self.cnt += 1

        self.helper(root.left)
        self.helper(root.right)



# V1 
# http://bookshadow.com/weblog/2015/06/06/leetcode-count-complete-tree-nodes/
# https://blog.csdn.net/fuxuemingzhu/article/details/80781666
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O((logn)^2)   # getHeight O(logn) per recursion level, O(logn) levels
# space = O(logn)      # recursion depth
class Solution(object):
    def countNodes(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root: return 0
        nodes = 0
        left_height = self.getHeight(root.left)
        right_height = self.getHeight(root.right)
        if left_height == right_height:
            nodes = 2 ** left_height + self.countNodes(root.right)
        else:
            nodes = 2 ** right_height + self.countNodes(root.left)
        return nodes


    def getHeight(self, root):
        height = 0
        while root:
            height += 1
            root = root.left
        return height

# V2 
# time = O(h * logn) = O((logn)^2)
# space = O(1)
class Solution(object):
    # @param {TreeNode} root
    # @return {integer}
    def countNodes(self, root):
        if root is None:
            return 0

        node, level = root, 0
        while node.left is not None:
            node = node.left
            level += 1

        # Binary search.
        left, right = 2 ** level, 2 ** (level + 1)
        while left < right:
            mid = left + (right - left) / 2
            if not self.exist(root, mid):
                right = mid
            else:
                left = mid + 1

        return left - 1

    # Check if the nth node exist.
    def exist(self, root, n):
        k = 1
        while k <= n:
            k <<= 1
        k >>= 2

        node = root
        while k > 0:
            if (n & k) == 0:
                node = node.left
            else:
                node = node.right
            k >>= 1
        return node is not None
