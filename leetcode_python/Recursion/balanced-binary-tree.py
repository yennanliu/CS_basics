"""

110. Balanced Binary Tree
Easy

Given a binary tree, determine if it is height-balanced.

For this problem, a height-balanced binary tree is defined as:

a binary tree in which the left and right subtrees of every node differ in height by no more than 1.

 

Example 1:


Input: root = [3,9,20,null,null,15,7]
Output: true
Example 2:


Input: root = [1,2,2,3,3,null,null,4,4]
Output: false
Example 3:

Input: root = []
Output: true
 

Constraints:

The number of nodes in the tree is in the range [0, 5000].
-104 <= Node.val <= 104

"""


# V0
# IDEA: DFS (post order) + helper func
# time = O(n^2), height helper recomputed at every node
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def isBalanced(self, root):
        # edge
        if not root:
            return True # ??
        # ???
        _left = self.helper(root.left)
        _right = self.helper(root.right)
        if abs(_left - _right) > 1:
            return False
        
        return self.isBalanced(root.left) and \
               self.isBalanced(root.right)

    def helper(self, root):
        # NOTE !!! below
        if not root:
            return 0 # ??

        return 1 + max(
            self.helper(root.left), 
            self.helper(root.right)
        )


# V0-1
# time = O(n^2), height helper recomputed at every node
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def isBalanced(self, root):
        if not root:
            return True

        left_height = self.helper(root.left)
        right_height = self.helper(root.right)

        if abs(left_height - right_height) > 1:
            return False

        return (
            self.isBalanced(root.left)
            and
            self.isBalanced(root.right)
        )

    def helper(self, root):
        if not root:
            return 0

        return 1 + max(
            self.helper(root.left),
            self.helper(root.right)
        )


# V0-2
# time = O(n^2), height helper recomputed at every node
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def isBalanced(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: bool
        """
        # Base case: An empty tree is always balanced
        if not root:
            return True 
            
        # 1. Use the helper to calculate the heights of the left and right subtrees
        left_height = self.get_height(root.left)
        right_height = self.get_height(root.right)
        
        # 2. Check if the current node violates the balance rule
        if abs(left_height - right_height) > 1:
            return False
        
        # 3. Recursively confirm that BOTH the left and right subtrees are also balanced
        return self.isBalanced(root.left) and self.isBalanced(root.right)

    def get_height(self, root):
        """
        Helper function to calculate the max depth/height of a tree.
        """
        # CRITICAL FIX: The height of an empty tree is the integer 0, not True!
        if not root:
            return 0
            
        return 1 + max(
            self.get_height(root.left), 
            self.get_height(root.right)
        )

# V0
# https://blog.csdn.net/coder_orz/article/details/51335758
# time = O(n^2), getAllDepth traverses subtree at every recursive call
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def isBalanced(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if root == None:
            return True

        self.getAllDepth(root)

        left_depth = root.left.val if root.left else 0
        right_depth = root.right.val if root.right else 0
        if abs(left_depth - right_depth) <= 1:
            return self.isBalanced(root.left) and self.isBalanced(root.right)
        else:
            return False

    def getAllDepth(self, root):
        if root == None:
            return 0
        root.val = 1 + max(self.getAllDepth(root.left), self.getAllDepth(root.right))
        return root.val

# V1
# IDEA : TOP DOWN RECURSION
# https://leetcode.com/problems/balanced-binary-tree/solution/
# time = O(n^2), height recomputed at every node
# space = O(h), h = tree height (recursion stack)
class Solution:
    # Compute the tree's height via recursion
    def height(self, root: TreeNode) -> int:
        # An empty tree has height -1
        if not root:
            return -1
        return 1 + max(self.height(root.left), self.height(root.right))
    
    def isBalanced(self, root: TreeNode) -> bool:
        # An empty tree satisfies the definition of a balanced tree
        if not root:
            return True

        # Check if subtrees have height within 1. If they do, check if the
        # subtrees are balanced
        return abs(self.height(root.left) - self.height(root.right)) < 2 \
            and self.isBalanced(root.left) \
            and self.isBalanced(root.right)

# V1'
# IDEA : BOTTOM UP RECURSION
# https://leetcode.com/problems/balanced-binary-tree/solution/
# time = O(n), each node visited once
# space = O(h), h = tree height (recursion stack)
class Solution:
    # Return whether or not the tree at root is balanced while also returning
    # the tree's height
    def isBalancedHelper(self, root: TreeNode) -> (bool, int):
        # An empty tree is balanced and has height -1
        if not root:
            return True, -1
        
        # Check subtrees to see if they are balanced. 
        leftIsBalanced, leftHeight = self.isBalancedHelper(root.left)
        if not leftIsBalanced:
            return False, 0
        rightIsBalanced, rightHeight = self.isBalancedHelper(root.right)
        if not rightIsBalanced:
            return False, 0
        
        # If the subtrees are balanced, check if the current tree is balanced
        # using their height
        return (abs(leftHeight - rightHeight) < 2), 1 + max(leftHeight, rightHeight)
        
    def isBalanced(self, root: TreeNode) -> bool:
        return self.isBalancedHelper(root)[0]

# V1''
# https://blog.csdn.net/coder_orz/article/details/51335758
# IDEA : DFS
# time = O(n^2), getAllDepth traverses subtree at every recursive call
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def isBalanced(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if root == None:
            return True

        self.getAllDepth(root)

        left_depth = root.left.val if root.left else 0
        right_depth = root.right.val if root.right else 0
        if abs(left_depth - right_depth) <= 1:
            return self.isBalanced(root.left) and self.isBalanced(root.right)
        else:
            return False

    def getAllDepth(self, root):
        if root == None:
            return 0
        root.val = 1 + max(self.getAllDepth(root.left), self.getAllDepth(root.right))
        return root.val

# V1'''
# https://blog.csdn.net/coder_orz/article/details/51335758
# IDEA : OPTIMIZED DFS
# time = O(n), each node visited once
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def isBalanced(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        return self.dfs(root) != -1

    def dfs(self, root):
        if root == None:
            return True

        left_depth = self.dfs(root.left)
        if left_depth == -1:
            return -1
        right_depth = self.dfs(root.right)
        if right_depth == -1:
            return -1
        return 1 + max(left_depth, right_depth) if abs(left_depth - right_depth) <= 1  else -1 

# V1''''
# https://blog.csdn.net/coder_orz/article/details/51335758
# IDEA : RECURSION
# time = O(n^2), getDepth recomputed at every node
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def isBalanced(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if root == None:
            return True

        left_depth = self.getDepth(root.left)
        right_depth = self.getDepth(root.right)
        if abs(left_depth - right_depth) <= 1:
            return self.isBalanced(root.left) and self.isBalanced(root.right)
        else:
            return False

    def getDepth(self, root):
        if root == None:
            return 0
        return 1 + max(self.getDepth(root.left), self.getDepth(root.right))

# V1''''''
# https://www.jiuzhang.com/solution/balanced-binary-tree/#tag-highlight-lang-python
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        this.val = val
        this.left, this.right = None, None
"""
# time = O(n), each node visited once
# space = O(h), h = tree height (recursion stack)
class Solution:
    """
    @param root: The root of binary tree.
    @return: True if this Binary tree is Balanced, or false.
    """
    def isBalanced(self, root):
        balanced, _ = self.validate(root)
        return balanced
        
    def validate(self, root):
        if root is None:
            return True, 0
            
        balanced, leftHeight = self.validate(root.left)
        if not balanced:
            return False, 0
        balanced, rightHeight = self.validate(root.right)
        if not balanced:
            return False, 0 
        return abs(leftHeight - rightHeight) <= 1, max(leftHeight , rightHeight) + 1

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
    # @return a boolean
    def isBalanced(self, root):
        def getHeight(root):
            if root is None:
                return 0
            left_height, right_height = \
                getHeight(root.left), getHeight(root.right)
            if left_height < 0 or right_height < 0 or \
               abs(left_height - right_height) > 1:
                return -1
            return max(left_height, right_height) + 1
        return (getHeight(root) >= 0)