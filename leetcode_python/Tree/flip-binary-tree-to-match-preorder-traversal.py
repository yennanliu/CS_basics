# V0 

# V1
# https://buptwc.com/2019/01/09/Leetcode-971-Flip-Binary-Tree-to-Match-Preorder-Traversal/
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
# Time:  O(n)
# Space: O(h)

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