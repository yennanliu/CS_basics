"""

112. Path Sum
Easy

Given the root of a binary tree and an integer targetSum, return true if the tree has a root-to-leaf path such that adding up all the values along the path equals targetSum.

A leaf is a node with no children.

 

Example 1:


Input: root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
Output: true
Explanation: The root-to-leaf path with the target sum is shown.
Example 2:


Input: root = [1,2,3], targetSum = 5
Output: false
Explanation: There two root-to-leaf paths in the tree:
(1 --> 2): The sum is 3.
(1 --> 3): The sum is 4.
There is no root-to-leaf path with sum = 5.
Example 3:

Input: root = [], targetSum = 0
Output: false
Explanation: Since the tree is empty, there are no root-to-leaf paths.
 

Constraints:

The number of nodes in the tree is in the range [0, 5000].
-1000 <= Node.val <= 1000
-1000 <= targetSum <= 1000

"""

# V0
# IDEA: DFS (pre-order)(gpt)
# time = O(n)
# space = O(h), h is height of binary tree
class Solution(object):
    def hasPathSum(self, root, targetSum):
        if not root:
            return False

        return self.helper(root, targetSum, 0)

    def helper(self, root, targetSum, cur_sum):
        if not root:
            return False

        """
        NOTE !!!

        we do `cur_sum` here, but NOT in recursion call,
        so we have cleaner validation logic  at `leaf`
        """
        cur_sum += root.val

        if not root.left and not root.right:
            return cur_sum == targetSum

        return (
            self.helper(root.left, targetSum, cur_sum) or
            self.helper(root.right, targetSum, cur_sum)
        )




# V0-1
# IDEA: DFS (pre-order)(gpt)
# time = O(n)
# space = O(h), h is height of binary tree
class Solution(object):
    def hasPathSum(self, root, targetSum):
        if not root:
            return False

        return self.helper(root, targetSum, 0)

    def helper(self, root, targetSum, cur_sum):
        if not root:
            return False

        """
        NOTE !!!

        if we DON'T `cur_sum` here, but in recursion call,
        
        -> so we need to add root.val to cur cur_sum first
        """
        if not root.left and not root.right:
            return cur_sum + root.val == targetSum

        return (
            self.helper(root.left, targetSum, cur_sum + root.val) or
            self.helper(root.right, targetSum, cur_sum + root.val)
        )


# V0-2
# IDEA: DFS (pre-order)(GEMINI)
# time = O(n)
# space = O(h), h is height of binary tree
class Solution(object):
    def hasPathSum(self, root, targetSum):
        """
        :type root: TreeNode
        :type targetSum: int
        :rtype: bool
        """
        if not root:
            return False
        return self.helper(root, targetSum, 0)
        
    def helper(self, root, targetSum, cur_sum):
        if not root:
            return False
            
        # FIX: Add the current node's value immediately upon visiting it
        cur_sum += root.val
        
        # Now, when we reach a leaf, cur_sum contains the complete path total
        if not root.left and not root.right:
            return cur_sum == targetSum
            
        return (self.helper(root.left, targetSum, cur_sum) or
                self.helper(root.right, targetSum, cur_sum))





# V0
# IDEA : DFS
# time = O(n)
# space = O(h), h is height of binary tree
class Solution(object):
    def hasPathSum(self, root, sum):
        if not root:
            return False
        if not root.left and not root.right:
            return True if sum == root.val else False
        else:
            return self.hasPathSum(root.left, sum-root.val) or self.hasPathSum(root.right, sum-root.val)

# V1
# https://blog.csdn.net/coder_orz/article/details/51595815
# IDEA : DFS (recursion)
# time = O(n)
# space = O(h), h is height of binary tree
class Solution(object):
    def hasPathSum(self, root, sum):
        if not root:
            return False
        if not root.left and not root.right:
            return True if sum == root.val else False
        else:
            return self.hasPathSum(root.left, sum-root.val) or self.hasPathSum(root.right, sum-root.val)

# V1'
# https://blog.csdn.net/coder_orz/article/details/51595815
# IDEA : DFS (non - recursion) (via stack)
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(h), h is height of binary tree
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
# time = O(n)
# space = O(h), h is height of binary tree
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