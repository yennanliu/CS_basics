"""

94. Binary Tree Inorder Traversal

Given the root of a binary tree, return the inorder traversal of its nodes' values.

 

Example 1:


Input: root = [1,null,2,3]
Output: [1,3,2]
Example 2:

Input: root = []
Output: []
Example 3:

Input: root = [1]
Output: [1]
 

Constraints:

The number of nodes in the tree is in the range [0, 100].
-100 <= Node.val <= 100
 

Follow up: Recursive solution is trivial, could you do it iteratively?

"""

# V0
# IDEA : recursion
# https://leetcode.com/problems/binary-tree-inorder-traversal/solutions/332283/python-recursive-and-iterative-solutions/
class Solution(object):
    # left -> root -> right
    def inorderTraversal(self, root):
        # help func
        def dfs(root, res):
            if not root:
                return
            if root.left:
                dfs(root.left)
            res.append(root.val)
            if root.right:
                dfs(root.right)
        res = []
        dfs(root)
        return root

# V0'
# IDEA : ITERATION
class Solution:
    def inorderTraversal(self, root):
        stack = []
        res = []
        
        while True:
            # NOTE !!! : we GO THROUGH left sub tree to the end first, and form our stack on the same time, then do in-order transversal
            while root:
                stack.append(root)
                root = root.left
            
            if len(stack) == 0:
                # NOTE here
                break
            
            root = stack.pop()
            res.append(root.val)
            root = root.right
        return res

# V0'
class Solution(object):
    def inorderTraversal(self, root):
        answer = []
        def inorder(root):
            if root == None:
                return None
            if root.left != None:
                inorder(root.left)
            answer.append(root.val)
            if root.right != None:
                inorder(root.right)
        inorder(root)
        return answer

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79294461
# IDEA : Recursion (INORDER TRAVERSAL)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def inorderTraversal(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        answer = []
        def inorder(root):
            if root == None:
                return None
            if root.left != None:
                inorder(root.left)
            answer.append(root.val)
            if root.right != None:
                inorder(root.right)
        inorder(root)
        return answer

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79294461
# Iteration 
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def inorderTraversal(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        stack = []
        answer = []
        while True:
            while root:
                stack.append(root)
                root = root.left
            if not stack:
                return answer
            root = stack.pop()
            answer.append(root.val)
            root = root.right

# V1
# IDEA : ITERATION
# https://leetcode.com/problems/binary-tree-inorder-traversal/solutions/2256663/python-recursive-and-iterative-inorder-traversals/
class Solution:
    def inorderTraversal(self, root: Optional[TreeNode]) -> List[int]:
        stack = []
        res = []
        
        while True:
            while root:
                stack.append(root)
                root = root.left
            
            if len(stack) == 0:
                break
            
            root = stack.pop()
            res.append(root.val)
            root = root.right
        return res

# V1''
# https://www.jiuzhang.com/solution/binary-tree-inorder-traversal/#tag-highlight-lang-python
class Solution:
    """
    @param root: A Tree
    @return: Inorder in ArrayList which contains node values.
    """
    def inorderTraversal(self, root):
        if root is None:
            return []
        # create a dumm node, left point to root 
        # put it into stack, and set dummy as stack head
        # is the current place of iterator
        dummy = TreeNode(0)
        dummy.right = root
        stack = [dummy]
            
        inorder = []
        # move iterator to next place every time
        # i.e. modify stack make stack head to next place
        while stack:
            node = stack.pop()
            if node.right:
                node = node.right
                while node:
                    stack.append(node)
                    node = node.left
            if stack:
                inorder.append(stack[-1].val)             
        return inorder

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
    def inorderTraversal(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        result, curr = [], root
        while curr:
            if curr.left is None:
                result.append(curr.val)
                curr = curr.right
            else:
                node = curr.left
                while node.right and node.right != curr:
                    node = node.right

                if node.right is None:
                    node.right = curr
                    curr = curr.left
                else:
                    result.append(curr.val)
                    node.right = None
                    curr = curr.right

        return result

# V2' 
# Time:  O(n)
# Space: O(h)
# Stack Solution
class Solution2(object):
    def inorderTraversal(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        result, stack = [], [(root, False)]
        while stack:
            root, is_visited = stack.pop()
            if root is None:
                continue
            if is_visited:
                result.append(root.val)
            else:
                stack.append((root.right, False))
                stack.append((root, True))
                stack.append((root.left, False))
        return result