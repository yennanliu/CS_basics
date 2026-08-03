# https://leetcode.com/problems/delete-leaves-with-a-given-value/description/

"""

1325. Delete Leaves With a Given Value
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given a binary tree root and an integer target, delete all the leaf nodes with value target.

Note that once you delete a leaf node with value target, if its parent node becomes a leaf node and has the value target, it should also be deleted (you need to continue doing that until you cannot).

 

Example 1:



Input: root = [1,2,3,2,null,2,4], target = 2
Output: [1,null,3,null,4]
Explanation: Leaf nodes in green with value (target = 2) are removed (Picture in left). 
After removing, new nodes become leaf nodes with value (target = 2) (Picture in center).
Example 2:



Input: root = [1,3,3,3,2], target = 3
Output: [1,3,null,null,2]
Example 3:



Input: root = [1,2,null,2,null,2], target = 2
Output: [1]
Explanation: Leaf nodes in green with value (target = 2) are removed at each step.
 

Constraints:

The number of nodes in the tree is in the range [1, 3000].
1 <= Node.val, target <= 1000
 

"""


# V0
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def removeLeafNodes(self, root, target):
        """
        :type root: Optional[TreeNode]
        :type target: int
        :rtype: Optional[TreeNode]
        """
        pass



# V0-1
# IDEA: DFS (gpt)
class Solution(object):
    def removeLeafNodes(self, root, target):
        return self.helper(root, target)

    # NOTE !!!
    # helper func should return `modified tree`, but NOT `bollean`
    def helper(self, root, target):
        if not root:
            return None

        root.left = self.helper(root.left, target)
        root.right = self.helper(root.right, target)

        if not root.left and not root.right and root.val == target:
            return None

        # NOTE !!!
        # below
        return root


# V0-2
# IDEA: DFS (GEMINI)
class Solution(object):
    def removeLeafNodes(self, root, target):
        """
        :type root: TreeNode
        :type target: int
        :rtype: TreeNode
        """
        if not root:
            return None
            
        root.left = self.removeLeafNodes(root.left, target)
        root.right = self.removeLeafNodes(root.right, target)
        
        if not root.left and not root.right and root.val == target:
            return None
            
        return root


# V0-3
# IDEA: DFS (gpt)
class Solution(object):
    def removeLeafNodes(self, root, target):
        if not self.helper(root, target):
            return None
        return root

    def helper(self, root, target):
        if not root:
            return False

        if root.left and not self.helper(root.left, target):
            root.left = None

        if root.right and not self.helper(root.right, target):
            root.right = None

        return not (root.left is None and
                    root.right is None and
                    root.val == target)


# V1-1
# IDEA: Recursion (Postorder Traversal)
# https://leetcode.com/problems/delete-leaves-with-a-given-value/editorial/
class Solution:
    def removeLeafNodes(
        self, root: Optional[TreeNode], target: int
    ) -> Optional[TreeNode]:
        # Base case
        if root is None:
            return None

        # 1. Visit the left children
        root.left = self.removeLeafNodes(root.left, target)

        # 2. Visit the right children
        root.right = self.removeLeafNodes(root.right, target)

        # 3. Check if the current node is a leaf node and its value equals target
        if root.left is None and root.right is None and root.val == target:
            # Delete the node by returning None to the parent, effectively disconnecting it
            return None

        # Keep it untouched otherwise
        return root


# V1-2
# IDEA: Iterative (PostOrder Traversal)
# https://leetcode.com/problems/delete-leaves-with-a-given-value/editorial/
class Solution:
    def removeLeafNodes(
        self, root: Optional[TreeNode], target: int
    ) -> Optional[TreeNode]:
        if not root:
            return None

        stack = []
        current_node = root
        last_right_node = None

        while stack or current_node:
            # Push left nodes to the stack until reaching the leftmost node.
            while current_node:
                stack.append(current_node)
                current_node = current_node.left

            # Access the top node on the stack without removing it.
            # This node is the current candidate for processing.
            current_node = stack[-1]

            # Check if the current node has an unexplored right subtree.
            # If so, shift to the right subtree unless it's the subtree we just visited.
            if current_node.right and current_node.right is not last_right_node:
                current_node = current_node.right
                continue  # Continue in the while loop to push this new subtree's leftmost nodes.

            # Remove the node from the stack, since we're about to process it.
            stack.pop()

            # If the node has no right subtree or the right subtree has already been visited,
            # then check if it is a leaf node with the target value.
            if (
                not current_node.right
                and not current_node.left
                and current_node.val == target
            ):
                # If the stack is empty after popping, it means the root was a target leaf node.
                if not stack:
                    return None  # The tree becomes empty as the root itself is removed.

                # Identify the parent of the current node.
                parent = stack[-1] if stack else None

                # Disconnect the current node from its parent.
                if parent and parent.left is current_node:
                    parent.left = None  # If current is a left child, set the left pointer to null.
                elif parent and parent.right is current_node:
                    parent.right = None  # If current is a right child, set the right pointer to null.

            # Mark this node as visited by setting 'last_right_node' to 'current_node' before moving to the next iteration.
            last_right_node = current_node
            # Reset 'current_node' to None to ensure the next node from the stack is processed.
            current_node = None

        return root  # Return the modified tree



# V2
