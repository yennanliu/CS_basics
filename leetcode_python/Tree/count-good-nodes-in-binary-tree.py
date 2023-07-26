"""

1448. Count Good Nodes in Binary Tree
Medium

Given a binary tree root, a node X in the tree is named good if in the path from root to X there are no nodes with a value greater than X.

Return the number of good nodes in the binary tree.

 

Example 1:



Input: root = [3,1,4,3,null,1,5]
Output: 4
Explanation: Nodes in blue are good.
Root Node (3) is always a good node.
Node 4 -> (3,4) is the maximum value in the path starting from the root.
Node 5 -> (3,4,5) is the maximum value in the path
Node 3 -> (3,1,3) is the maximum value in the path.
Example 2:



Input: root = [3,3,null,4,2]
Output: 3
Explanation: Node 2 -> (3, 3, 2) is not good, because "3" is higher than it.
Example 3:

Input: root = [1]
Output: 1
Explanation: Root is considered as good.
 

Constraints:

The number of nodes in the binary tree is in the range [1, 10^5].
Each node's value is between [-10^4, 10^4].

"""


# V0

# V1
# IDEA : DFS
# https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
class Solution:
    def goodNodes(self, root: TreeNode) -> int:
	
        def dfs(node, max_so_far):
            nonlocal num_good_nodes
            if max_so_far <= node.val:
                num_good_nodes += 1
            if node.right:
                dfs(node.right, max(node.val, max_so_far))
            if node.left:
                dfs(node.left, max(node.val, max_so_far))
        
        num_good_nodes = 0
        dfs(root, float("-inf"))
        return num_good_nodes

# V2
# IDEA : DFS + ITERATIVE
# https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
class Solution:
    def goodNodes(self, root: TreeNode) -> int:
        stack = [(root, float("-inf"))]
        num_good_nodes = 0
        while stack:
            node, max_so_far = stack.pop()
            if max_so_far <= node.val:
                num_good_nodes += 1
            if node.left:
                stack.append((node.left, max(node.val, max_so_far)))
            if node.right:
                stack.append((node.right, max(node.val, max_so_far)))
        
        return num_good_nodes

# V3
# IDEA : BFS
# https://leetcode.com/problems/count-good-nodes-in-binary-tree/editorial/
class Solution:
    def goodNodes(self, root: TreeNode) -> int:
        num_good_nodes = 0
        
        # Use collections.deque for efficient popping
        queue = deque([(root, float("-inf"))])
        while queue:
            node, max_so_far = queue.popleft()
            if max_so_far <= node.val:
                num_good_nodes += 1
            if node.right:
                queue.append((node.right, max(node.val, max_so_far)))
            if node.left:
                queue.append((node.left, max(node.val, max_so_far)))
        
        return num_good_nodes