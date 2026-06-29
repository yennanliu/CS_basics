"""
114. Flatten Binary Tree to Linked List
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given the root of a binary tree, flatten the tree into a "linked list":

The "linked list" should use the same TreeNode class where the right child pointer points to the next node in the list and the left child pointer is always null.
The "linked list" should be in the same order as a pre-order traversal of the binary tree.
 

Example 1:


Input: root = [1,2,5,3,4,null,6]
Output: [1,null,2,null,3,null,4,null,5,null,6]
Example 2:

Input: root = []
Output: []
Example 3:

Input: root = [0]
Output: [0]
 

Constraints:

The number of nodes in the tree is in the range [0, 2000].
-100 <= Node.val <= 100
 

Follow up: Can you flatten the tree in-place (with O(1) extra space)?
"""


# V0
# IDEA: DFS (post-order) (gpt)
class Solution(object):
    def flatten(self, root):
        """
        Do not return anything, modify root `in-place` instead.
        """

        # Flatten the tree starting from the root.
        self.helper(root)

    def helper(self, node):
        # Base case:
        # An empty subtree has no tail.
        if not node:
            return None

        # Recursively flatten the left subtree.
        # left_tail is the last node of the flattened left subtree.
        left_tail = self.helper(node.left)

        # Recursively flatten the right subtree.
        # right_tail is the last node of the flattened right subtree.
        right_tail = self.helper(node.right)

        # If there is a left subtree:
        #
        # Before:
        #
        #      node
        #     /    \
        #   left   right
        #
        # After:
        #
        # node
        #   \
        #   left
        #      \
        #      right
        #
        if left_tail:

            # Connect the tail of the flattened left subtree
            # to the original right subtree.
            left_tail.right = node.right

            # Move the entire left subtree to become
            # the right subtree.
            node.right = node.left

            # The left child must be None according
            # to the problem statement.
            node.left = None

        # Return the tail of the flattened subtree.
        #
        # Priority:
        # 1. If a right subtree exists, its tail is the answer.
        # 2. Otherwise, if only a left subtree exists,
        #    its tail is the answer.
        # 3. Otherwise this node itself is the tail.
        if right_tail:
            return right_tail

        if left_tail:
            return left_tail

        return node



# V0-1
# IDEA: DFS (post-order) (O(1) space) (CLAUDE)
class Solution(object):
    def flatten(self, root):
        curr = root
        while curr:
            if curr.left:
                rightmost = curr.left
                while rightmost.right:        # find left subtree's rightmost
                    rightmost = rightmost.right
                rightmost.right = curr.right  # splice original right after it
                curr.right = curr.left        # move left to right
                curr.left = None
            curr = curr.right                 # advance down the new right spine


# V0-2
# IDEA: DFS (post-order) + prev node (GEMINI)
class Solution(object):
    def flatten(self, root):
        """
        :type root: TreeNode
        :rtype: None Do not return anything, modify root in-place instead.
        """
        # Keeps track of the node that should come immediately AFTER the current node
        self.prev = None
        self.helper(root)

    def helper(self, node):
        if not node:
            return
            
        # 1. Traverse BACKWARDS: Right first, then Left
        self.helper(node.right)
        self.helper(node.left)
        
        # 2. Stitch the current node to the flattening sequence chain
        node.right = self.prev  # Right child points to the head of the processed chain
        node.left = None         # Left child must be cleared completely per requirements
        
        # 3. Move the head pointer to the current node
        self.prev = node



# V0
# IDEA: DFS + PREORDER
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Recursion/FlattenBinaryTreeToLinkedList.java#L73
class Solution:
    def flatten(self, root):
        """
        Do not return anything, modify root in-place instead.
        """
        nodes = []

        def preorder(node):
            if not node:
                return

            nodes.append(node)
            preorder(node.left)
            preorder(node.right)

        preorder(root)

        for i in range(len(nodes) - 1):
            curr = nodes[i]
            nxt = nodes[i + 1]

            curr.left = None
            curr.right = nxt

        if nodes:
            nodes[-1].left = None
            nodes[-1].right = None


# V0-1
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right

class Solution(object):
    def flatten(self, root):
        """
        :type root: TreeNode
        :rtype: None Do not return anything, modify root in-place instead.
        """
        curr = root
        while curr:
            if curr.left:
                # Find the rightmost node of the left subtree
                rightmost = curr.left
                while rightmost.right:
                    rightmost = rightmost.right
                
                # Rewire: connect the original right subtree to the rightmost node
                rightmost.right = curr.right
                
                # Move the left subtree over to the right side
                curr.right = curr.left
                curr.left = None
                
            # Move down the flattened right path
            curr = curr.right


# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/70241424
class Solution(object):
    def flatten(self, root):
        """
        :type root: TreeNode
        :rtype: void Do not return anything, modify root in-place instead.
        """
        res = []
        self.preOrder(root, res)
        for i in range(len(res) - 1):
            res[i].left = None
            res[i].right = res[i + 1]
    
    def preOrder(self, root, res):
        if not root: return
        res.append(root)
        self.preOrder(root.left, res)
        self.preOrder(root.right, res)

# V1'
# http://bookshadow.com/weblog/2016/09/02/leetcode-flatten-binary-tree-to-linked-list/
class Solution(object):
    def flatten(self, root):
        """
        :type root: TreeNode
        :rtype: void Do not return anything, modify root in-place instead.
        """
        pointer = dummy = TreeNode(None)
        stack = [root]
        while stack:
            top = stack.pop()
            if not top: continue
            stack.append(top.right)
            stack.append(top.left)
            pointer.right = top
            pointer.left = None
            pointer = top

# V1''
# http://bookshadow.com/weblog/2016/09/02/leetcode-flatten-binary-tree-to-linked-list/
class Solution(object):
    def flatten(self, root):
        """
        :type root: TreeNode
        :rtype: void Do not return anything, modify root in-place instead.
        """
        self.pointer = None
        def traverse(root):
            if not root: return
            traverse(root.right)
            traverse(root.left)
            root.right = self.pointer
            root.left = None
            self.pointer = root
        traverse(root)

# V1'''
# https://www.jiuzhang.com/solution/flatten-binary-tree-to-linked-list/#tag-highlight-lang-python
class Solution:
    last_node = None
    
    """
    @param root: a TreeNode, the root of the binary tree
    @return: nothing
    """
    def flatten(self, root):
        if root is None:
            return
        
        if self.last_node is not None:
            self.last_node.left = None
            self.last_node.right = root
            
        self.last_node = root
        right = root.right
        self.flatten(root.left)
        self.flatten(right)

# V1''''
# https://www.jiuzhang.com/solution/flatten-binary-tree-to-linked-list/#tag-highlight-lang-python
class Solution:
    """
    @param root: a TreeNode, the root of the binary tree
    @return: nothing
    """
    def flatten(self, root):
        self.helper(root)
        
    # restructure and return last node in preorder
    def helper(self, root):
        if root is None:
            return None
            
        left_last = self.helper(root.left)
        right_last = self.helper(root.right)
        
        # connect 
        if left_last is not None:
            left_last.right = root.right
            root.right = root.left
            root.left = None
            
        if right_last is not None:
            return right_last
            
        if left_last is not None:
            return left_last            
        return root

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
    # @return nothing, do it in place
    def flatten(self, root):
        self.flattenRecu(root, None)

    def flattenRecu(self, root, list_head):
        if root:
            list_head = self.flattenRecu(root.right, list_head)
            list_head = self.flattenRecu(root.left, list_head)
            root.right = list_head
            root.left = None
            return root
        else:
            return list_head

class Solution2(object):
    list_head = None
    # @param root, a tree node
    # @return nothing, do it in place
    def flatten(self, root):
        if root:
            self.flatten(root.right)
            self.flatten(root.left)
            root.right = self.list_head
            root.left = None
            self.list_head = root
