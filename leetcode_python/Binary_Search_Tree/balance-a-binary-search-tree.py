"""

1382. Balance a Binary Search Tree
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given the root of a binary search tree, return a balanced binary search tree with the same node values. If there is more than one answer, return any of them.

A binary search tree is balanced if the depth of the two subtrees of every node never differs by more than 1.

 

Example 1:


Input: root = [1,null,2,null,3,null,4,null,null]
Output: [2,1,3,null,null,null,4]
Explanation: This is not the only correct answer, [3,1,4,null,2] is also correct.
Example 2:


Input: root = [2,1,3]
Output: [2,1,3]
 

Constraints:

The number of nodes in the tree is in the range [1, 104].
1 <= Node.val <= 105


"""


# V0
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def balanceBST(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        pass


# V1-1
# IDEA: IN ORDER DFS + DFS BUILD tree (gpt)
class Solution(object):
    def balanceBST(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        # edge case
        if not root:
            return None

        self.arr = []

        # BST -> sorted array
        self.in_order(root)

        return self.build_helper(self.arr)


    def in_order(self, root):
        if not root:
            return

        self.in_order(root.left)
        self.arr.append(root.val)
        self.in_order(root.right)


    def build_helper(self, array):
        # base case
        if not array:
            return None

        # middle element
        idx = len(array) // 2

        # middle becomes root
        root = TreeNode(array[idx])

        # left half
        root.left = self.build_helper(array[:idx])

        # right half
        root.right = self.build_helper(array[idx + 1:])

        return root


# V1-2
# IDEA: IN ORDER DFS + DFS BUILD tree (GEMINI)
class Solution(object):
    def balanceBST(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        # 1. FIX: Return 'root', not '[root]'
        if not root or (not root.left and not root.right):
            return root

        self.arr = []
        
        # 2. FIX: In-order traversal gives a sorted array
        self.in_order(root)

        # 3. FIX: Pass start (0) and end (len - 1) index bounds
        return self.build_helper(self.arr, 0, len(self.arr) - 1)

    def in_order(self, root):
        if not root:
            return
        self.in_order(root.left)
        self.arr.append(root.val)
        self.in_order(root.right)

    def build_helper(self, array, l_idx, r_idx):
        # Base case: valid range exhausted
        if l_idx > r_idx:
            return None

        # Pick the median element to ensure balance
        mid = (l_idx + r_idx) // 2
        root = TreeNode(array[mid])

        # Recurse on left and right index halves
        root.left = self.build_helper(array, l_idx, mid - 1)
        root.right = self.build_helper(array, mid + 1, r_idx)

        return root

# V2-1
# IDEA: Inorder Traversal + Recursive Construction
# https://leetcode.com/problems/balance-a-binary-search-tree/editorial/
class Solution:
    def balanceBST(self, root: TreeNode) -> TreeNode:
        # Create a list to store the inorder traversal of the BST
        inorder = []
        self.inorder_traversal(root, inorder)

        # Construct and return the balanced BST
        return self.create_balanced_bst(inorder, 0, len(inorder) - 1)

    def inorder_traversal(self, root: TreeNode, inorder: list):
        # Perform an inorder traversal to store the elements in sorted order
        if not root:
            return
        self.inorder_traversal(root.left, inorder)
        inorder.append(root.val)
        self.inorder_traversal(root.right, inorder)

    def create_balanced_bst(
        self, inorder: list, start: int, end: int
    ) -> TreeNode:
        # Base case: if the start index is greater than the end index, return None
        if start > end:
            return None

        # Find the middle element of the current range
        mid = start + (end - start) // 2

        # Recursively construct the left and right subtrees
        left_subtree = self.create_balanced_bst(inorder, start, mid - 1)
        right_subtree = self.create_balanced_bst(inorder, mid + 1, end)

        # Create a new node with the middle element and attach the subtrees
        node = TreeNode(inorder[mid], left_subtree, right_subtree)
        return node


# V2-2
# IDEA: Day-Stout-Warren Algorithm / In-Place Balancing
# https://leetcode.com/problems/balance-a-binary-search-tree/editorial/
class Solution:
    def balanceBST(self, root: TreeNode) -> TreeNode:
        if not root:
            return None

        # Step 1: Create the backbone (vine)
        # Temporary dummy node
        vine_head = TreeNode(0)
        vine_head.right = root
        current = vine_head
        while current.right:
            if current.right.left:
                self.right_rotate(current, current.right)
            else:
                current = current.right

        # Step 2: Count the nodes
        node_count = 0
        current = vine_head.right
        while current:
            node_count += 1
            current = current.right

        # Step 3: Create a balanced BST
        m = 2 ** math.floor(math.log2(node_count + 1)) - 1
        self.make_rotations(vine_head, node_count - m)
        while m > 1:
            m //= 2
            self.make_rotations(vine_head, m)

        balanced_root = vine_head.right
        # Delete the temporary dummy node
        vine_head = None
        return balanced_root

    # Function to perform a right rotation
    def right_rotate(self, parent: TreeNode, node: TreeNode):
        tmp = node.left
        node.left = tmp.right
        tmp.right = node
        parent.right = tmp

    # Function to perform a left rotation
    def left_rotate(self, parent: TreeNode, node: TreeNode):
        tmp = node.right
        node.right = tmp.left
        tmp.left = node
        parent.right = tmp

    # Function to perform a series of left rotations to balance the vine
    def make_rotations(self, vine_head: TreeNode, count: int):
        current = vine_head
        for _ in range(count):
            tmp = current.right
            self.left_rotate(current, tmp)
            current = current.right
