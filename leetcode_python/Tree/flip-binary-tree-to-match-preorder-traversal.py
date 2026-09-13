"""

971. Flip Binary Tree To Match Preorder Traversal
Medium

You are given the root of a binary tree with n nodes, where each node is uniquely assigned a value from 1 to n. You are also given a sequence of n values voyage, which is the desired pre-order traversal of the binary tree.

Any node in the binary tree can be flipped by swapping its left and right subtrees. For example, flipping node 1 will have the following effect:

https://assets.leetcode.com/uploads/2021/02/15/fliptree.jpg

Flip the smallest number of nodes so that the pre-order traversal of the tree matches voyage.

Return a list of the values of all flipped nodes. You may return the answer in any order. If it is impossible to flip the nodes in the tree to make the pre-order traversal match voyage, return the list [-1].

Example 1:

https://assets.leetcode.com/uploads/2019/01/02/1219-01.png

Input: root = [1,2], voyage = [2,1]
Output: [-1]
Explanation: It is impossible to flip the nodes such that the pre-order traversal matches voyage.

Example 2:

https://assets.leetcode.com/uploads/2019/01/02/1219-02.png

Input: root = [1,2,3], voyage = [1,3,2]
Output: [1]
Explanation: Flipping node 1 swaps nodes 2 and 3, so the pre-order traversal matches voyage.

Example 3:

https://assets.leetcode.com/uploads/2019/01/02/1219-02.png

Input: root = [1,2,3], voyage = [1,2,3]
Output: []
Explanation: The tree's pre-order traversal already matches voyage, so no nodes need to be flipped.

Constraints:

The number of nodes in the tree is n.
n == voyage.length
1 <= n <= 100
1 <= Node.val, voyage[i] <= n
All the values in the tree are unique.
All the values in voyage are unique.

"""

# V0 

# V1
# https://buptwc.com/2019/01/09/Leetcode-971-Flip-Binary-Tree-to-Match-Preorder-Traversal/
# time = O(n)
# space = O(h)  # h = tree height, worst O(n)
class Solution(object):
    def flipMatchVoyage(self, root, voyage):
        self.index = 0
        res = []
        def dfs(root):
            if not root: return True
            if root.val != voyage[self.index]: return False
            self.index += 1
            # if there is left sub tree, and left sub tree value != current index value 
            if root.left and root.left.val != voyage[self.index]:
                if not root.right: return False
                res.append(root.val)
                # from right to left 
                return dfs(root.right) and dfs(root.left)
            return dfs(root.left) and dfs(root.right)

        if dfs(root):
            return res
        return [-1]

# V1'  
# https://zxi.mytechroad.com/blog/tree/leetcode-971-flip-binary-tree-to-match-preorder-traversal/
# https://zhanghuimeng.github.io/post/leetcode-971-flip-binary-tree-to-match-preorder-traversal/
# time = O(n)
# space = O(h)  # h = tree height, worst O(n)
class Solution:
  def flipMatchVoyage(self, root, voyage):
    self.pos = 0
    self.flips = []
    def solve(root):
      if not root: return      
      if root.val != voyage[self.pos]:
        self.flips = [-1]
        return
      if root.left and root.left.val != voyage[self.pos + 1]:
        root.left, root.right = root.right, root.left
        self.flips.append(root.val)
      self.pos += 1
      solve(root.left)
      solve(root.right)    
    solve(root)
    return self.flips

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
    def flipMatchVoyage(self, root, voyage):
        """
        :type root: TreeNode
        :type voyage: List[int]
        :rtype: List[int]
        """
        def dfs(root, voyage, i, result):
            if not root:
                return True
            if root.val != voyage[i[0]]:
                return False
            i[0] += 1
            if root.left and root.left.val != voyage[i[0]]:
                result.append(root.val)
                return dfs(root.right, voyage, i, result) and \
                       dfs(root.left, voyage, i, result)
            return dfs(root.left, voyage, i, result) and \
                   dfs(root.right, voyage, i, result)
        
        result = []
        return result if dfs(root, voyage, [0], result) else [-1]