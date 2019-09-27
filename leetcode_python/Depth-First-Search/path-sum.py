# V0 

# V1 
# https://blog.csdn.net/coder_orz/article/details/51595815
# IDEA : DFS (recursion) 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def hasPathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: bool
        """
        if not root:
            return False
        if not root.left and not root.right:
            return True if sum == root.val else False
        else:
            return self.hasPathSum(root.left, sum-root.val) or self.hasPathSum(root.right, sum-root.val)

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51595815
# IDEA : DFS (non - recursion) (via stack)
class Solution(object):
    def hasPathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: bool
        """
        stack = [(root, sum)]
        while len(stack) > 0:
            node, tmp_sum = stack.pop() # make node = root, and tmp_sum = sum 
            if node:
                if not node.left and not node.right and node.val == tmp_sum:
                    return True
                stack.append((node.right, tmp_sum-node.val))
                stack.append((node.left, tmp_sum-node.val))
        return False

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51595815
# IDEA : BFS (via queue)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def hasPathSum(self, root, sum):
        """
        :type root: TreeNode
        :type sum: int
        :rtype: bool
        """
        queue = [(root, sum)]
        while len(queue) > 0:
            node, tmp_sum = queue.pop()
            if node:
                if not node.left and not node.right and node.val == tmp_sum:
                    return True
                queue.insert(0, (node.right, tmp_sum-node.val))
                queue.insert(0, (node.left, tmp_sum-node.val))
        return False

# V1'''
# https://www.jiuzhang.com/solution/path-sum/#tag-highlight-lang-python
class Solution:
    """
    @param root: the tree
    @param sum: the sum
    @return:  if the tree has a root-to-leaf path 
    """
    def pathSum(self, root, sum):
        # Write your code here.
        if root == None:
            return False;
        elif (root.val == sum and root.left == None and root.right == None):
            return True;
        else:
            return self.pathSum(root.left, sum - root.val) or self.pathSum(root.right, sum - root.val)

# V2 
# Time:  O(n)
# Space: O(h), h is height of binary tree
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param root, a tree node
    # @param sum, an integer
    # @return a boolean
    def hasPathSum(self, root, sum):
        if root is None:
            return False

        if root.left is None and root.right is None and root.val == sum:
            return True

        return self.hasPathSum(root.left, sum - root.val) or self.hasPathSum(root.right, sum - root.val)
