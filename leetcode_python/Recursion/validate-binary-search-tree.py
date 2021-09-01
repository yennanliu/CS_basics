"""

98. Validate Binary Search Tree
Medium

Given the root of a binary tree, determine if it is a valid binary search tree (BST).

A valid BST is defined as follows:

The left subtree of a node contains only nodes with keys less than the node's key.
The right subtree of a node contains only nodes with keys greater than the node's key.
Both the left and right subtrees must also be binary search trees.
 

Example 1:


Input: root = [2,1,3]
Output: true

Example 2:


Input: root = [5,1,4,null,null,3,6]
Output: false
Explanation: The root node's value is 5 but its right child's value is 4.
 

Constraints:

The number of nodes in the tree is in the range [1, 104].
-231 <= Node.val <= 231 - 1

"""

# V0
# IDEA: RECURSION 
class Solution(object):
    def isValidBST(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        return self.valid(root, float('-inf'), float('inf'))
        
    def valid(self, root, min_, max_):
        if not root: return True
        if root.val >= max_ or root.val <= min_:
            return False
        return self.valid(root.left, min_, root.val) and self.valid(root.right, root.val, max_)

# V0'
# IDEA : BFS + Inorder traversal
class Solution:
    def isValidBST(self, root):
        pre, cur, stack = None, root, []
        while stack or cur:
            while cur:
                stack.append(cur)
                cur = cur.left
            s = stack.pop()
            if pre and s.val <= pre.val:
                return False
            pre, cur = s, s.right
        return True

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/70209865
class Solution(object):
    def isValidBST(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        return self.valid(root, float('-inf'), float('inf'))
        
    def valid(self, root, min_, max_):
        if not root: return True
        if root.val >= max_ or root.val <= min_:
            return False
        return self.valid(root.left, min_, root.val) and self.valid(root.right, root.val, max_)

# V1'
# IDEA : BFS + Inorder traversal
# https://leetcode.com/problems/validate-binary-search-tree/discuss/32465/Python-Inorder-Traversal
class Solution:
    def isValidBST(self, root):
        pre, cur, stack = None, root, []
        while stack or cur:
            while cur:
                stack.append(cur)
                cur = cur.left
            s = stack.pop()
            if pre and s.val <= pre.val:
                return False
            pre, cur = s, s.right
        return True

# V1'
# https://www.jiuzhang.com/solution/validate-binary-search-tree/#tag-highlight-lang-python
class Solution:
    """
    @param root: The root of binary tree.
    @return: True if the binary tree is BST, or false
    """
    def isValidBST(self, root):
        if root is None:
            return True
            
        stack = []
        while root:
            stack.append(root)
            root = root.left
            
        last_node = stack[-1]
        while stack:
            node = stack.pop()
            if node.right:
                node = node.right
                while node:
                    stack.append(node)
                    node = node.left

            # the only difference compare to an inorder traversal iteration
            # this problem disallowed equal values so it's <= not <
            if stack:
                if stack[-1].val <= last_node.val:
                    return False
                last_node = stack[-1]
        return True

# V1''
# https://www.jiuzhang.com/solution/validate-binary-search-tree/#tag-highlight-lang-python
class Solution:
    """
    @param root: The root of binary tree.
    @return: True if the binary tree is BST, or false
    """
    def isValidBST(self, root):
        self.lastVal = None
        self.isBST = True
        self.validate(root)
        return self.isBST

    def validate(self, root):
        if root is None:
            return
        self.validate(root.left)
        if self.lastVal is not None and self.lastVal >= root.val:
            self.isBST = False
            return
        self.lastVal = root.val
        self.validate(root.right)

# V1'''
# IDEA : BFS + Inorder traversal
# https://leetcode.com/problems/validate-binary-search-tree/discuss/166691/Python-solution
class Solution(object):
    def isValidBST(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        stack = []
        trav = root
        prev = -float('inf')
        while trav or stack:
            if trav:
                stack.append(trav)
                trav = trav.left
            else:
                u = stack.pop()
                if u:
                    if u.val <= prev:
                        return False
                    prev = u.val
                trav = u.right
        return True

# V1''''
# https://www.jiuzhang.com/solution/validate-binary-search-tree/#tag-highlight-lang-python
class Solution:
    """
    @param root: The root of binary tree.
    @return: True if the binary tree is BST, or false
    """  
    def isValidBST(self, root):
        # write your code here
        isBST, minNode, maxNode = self.divideConquer(root)
        return isBST
        
    def divideConquer(self, root):
        if root is None:
            return True, None, None
        
        leftIsBST, leftMin, leftMax = self.divideConquer(root.left)
        rightIsBST, rightMin, rightMax = self.divideConquer(root.right)
        if not leftIsBST or not rightIsBST:
            return False, None, None
        if leftMax is not None and leftMax >= root.val:
            return False, None, None
        if rightMin is not None and rightMin <= root.val:
            return False, None, None
        
        # is BST
        minNode = leftMin if leftMin is not None else root.val
        maxNode = rightMax if rightMax is not None else root.val
        
        return True, minNode, maxNode

# V1'''''
# IDEA : PY generators
# https://leetcode.com/problems/validate-binary-search-tree/discuss/715307/Python-generators-rule
class Solution:
    def isValidBST(self, root: TreeNode) -> bool:
        
        def preorder(node):
            if node is None:
                return
            yield from preorder(node.left)
            yield node.val
            yield from preorder(node.right)
            
        val = float('-inf')
        for nextval in preorder(root):
            if nextval <= val:
                return False
            val = nextval
            
        return True

# V2 
# Time:  O(n)
# Space: O(1)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# Morris Traversal Solution
class Solution(object):
    # @param root, a tree node
    # @return a list of integers
    def isValidBST(self, root):
        prev, cur = None, root
        while cur:
            if cur.left is None:
                if prev and prev.val >= cur.val:
                    return False
                prev = cur
                cur = cur.right
            else:
                node = cur.left
                while node.right and node.right != cur:
                    node = node.right

                if node.right is None:
                    node.right = cur
                    cur = cur.left
                else:
                    if prev and prev.val >= cur.val:
                        return False
                    node.right = None
                    prev = cur
                    cur = cur.right

        return True

# Time:  O(n)
# Space: O(h)
class Solution2(object):
    # @param root, a tree node
    # @return a boolean
    def isValidBST(self, root):
        return self.isValidBSTRecu(root, float("-inf"), float("inf"))

    def isValidBSTRecu(self, root, low, high):
        if root is None:
            return True

        return low < root.val and root.val < high \
            and self.isValidBSTRecu(root.left, low, root.val) \
            and self.isValidBSTRecu(root.right, root.val, high)