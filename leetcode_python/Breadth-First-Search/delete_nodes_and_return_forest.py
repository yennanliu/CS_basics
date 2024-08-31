"""
1110. Delete Nodes And Return Forest
Solved
Medium
Topics
Companies
Given the root of a binary tree, each node in the tree has a distinct value.

After deleting all nodes with a value in to_delete, we are left with a forest (a disjoint union of trees).

Return the roots of the trees in the remaining forest. You may return the result in any order.

 

Example 1:


Input: root = [1,2,3,4,5,6,7], to_delete = [3,5]
Output: [[1,2,null,4],[6],[7]]
Example 2:

Input: root = [1,2,4,null,3], to_delete = [3]
Output: [[1,2,4]]
 

Constraints:

The number of nodes in the given tree is at most 1000.
Each node has a distinct value between 1 and 1000.
to_delete.length <= 1000
to_delete contains distinct values between 1 and 1000.

"""

# V0

# V1
# https://leetcode.com/problems/delete-nodes-and-return-forest/editorial/
# IDEA :  Recursion (Postorder Traversal)
class Solution:
    def delNodes(
        self, root: Optional[TreeNode], to_delete: List[int]
    ) -> List[TreeNode]:
        to_delete_set = set(to_delete)
        forest = []

        root = self._process_node(root, to_delete_set, forest)

        # If the root is not deleted, add it to the forest
        if root:
            forest.append(root)

        return forest

    def _process_node(
        self, node: TreeNode, to_delete_set: Set[int], forest: List[TreeNode]
    ) -> TreeNode:
        if not node:
            return None

        node.left = self._process_node(node.left, to_delete_set, forest)
        node.right = self._process_node(node.right, to_delete_set, forest)

        # Node Evaluation: Check if the current node needs to be deleted
        if node.val in to_delete_set:
            # If the node has left or right children, add them to the forest
            if node.left:
                forest.append(node.left)
            if node.right:
                forest.append(node.right)
            # Delete the current node by returning None to its parent
            return None

        return node

# V2
# https://leetcode.com/problems/delete-nodes-and-return-forest/editorial/
# IDEA : BFS
class Solution:
    def delNodes(
        self, root: Optional[TreeNode], to_delete: List[int]
    ) -> List[TreeNode]:
        if not root:
            return []

        to_delete_set = set(to_delete)
        forest = []

        nodes_queue = deque([root])

        while nodes_queue:
            current_node = nodes_queue.popleft()

            if current_node.left:
                nodes_queue.append(current_node.left)
                # Disconnect the left child if it needs to be deleted
                if current_node.left.val in to_delete_set:
                    current_node.left = None

            if current_node.right:
                nodes_queue.append(current_node.right)
                # Disconnect the right child if it needs to be deleted
                if current_node.right.val in to_delete_set:
                    current_node.right = None

            # If the current node needs to be deleted, add its non-null children to the forest
            if current_node.val in to_delete_set:
                if current_node.left:
                    forest.append(current_node.left)
                if current_node.right:
                    forest.append(current_node.right)

        # Ensure the root is added to the forest if it is not to be deleted
        if root.val not in to_delete_set:
            forest.append(root)

        return forest
