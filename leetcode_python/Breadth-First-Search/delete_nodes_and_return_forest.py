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
class Solution(object):
    def delNodes(self, root, to_delete):
        """
        :type root: TreeNode
        :type to_delete: List[int]
        :rtype: List[TreeNode]
        """
        pass


# V0-1
# IDEA: post order DFS (gpt)
class Solution(object):
    def delNodes(self, root, to_delete):
        if not root:
            return []

        self.res = []
        self.to_delete = set(to_delete)

        self.helper(root)

        # Root itself might not have been added yet
        if root.val not in self.to_delete:
            self.res.append(root)

        return self.res

    def helper(self, root):
        if not root:
            return None

        root.left = self.helper(root.left)
        root.right = self.helper(root.right)

        if root.val in self.to_delete:
            if root.left:
                self.res.append(root.left)

            if root.right:
                self.res.append(root.right)

            return None

        return root


# V0-2
# IDEA: post order DFS (GEMINI)
class Solution(object):
    def delNodes(self, root, to_delete):
        """
        :type root: TreeNode
        :type to_delete: List[int]
        :rtype: List[TreeNode]
        """
        if not root:
            return []

        # Convert to set for O(1) lookups
        to_delete_set = set(to_delete)
        res = []

        def dfs(node):
            if not node:
                return None

            # 1. Post-order traversal (bottom-up)
            node.left = dfs(node.left)
            node.right = dfs(node.right)

            # 2. If current node needs to be deleted
            if node.val in to_delete_set:
                # Add surviving children as new roots to result
                if node.left:
                    res.append(node.left)
                if node.right:
                    res.append(node.right)
                # Disconnect current node from its parent
                return None

            # Keep current node connected to its parent
            return node

        # Process the tree
        root = dfs(root)

        # If the original root wasn't deleted, add it to the forest
        if root:
            res.append(root)

        return res


# V0-3
# IDEA: post order DFS (gpt)
class Solution(object):
    def delNodes(self, root, to_delete):
        """
        :type root: TreeNode
        :type to_delete: List[int]
        :rtype: List[TreeNode]
        """
        if not root:
            return []

        self.res = []
        to_delete = set(to_delete)

        self.helper(root, to_delete, True)

        return self.res

    def helper(self, root, to_delete, is_root):
        if not root:
            return None

        deleted = root.val in to_delete

        # If this node is a new root and is NOT deleted,
        # add it to the result.
        if is_root and not deleted:
            self.res.append(root)

        # Process children.
        root.left = self.helper(root.left, to_delete, deleted)
        root.right = self.helper(root.right, to_delete, deleted)

        # If this node is deleted, it should disappear from its parent.
        if deleted:
            return None

        return root


# V1
# https://leetcode.com/problems/delete-nodes-and-return-forest/editorial/
# IDEA :  Recursion (Postorder Traversal)
# time = O(n)
# space = O(n)  # recursion stack + forest result
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
# time = O(n)
# space = O(n)  # queue + forest result
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
