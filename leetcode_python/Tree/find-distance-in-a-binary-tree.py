# https://leetcode.com/problems/find-distance-in-a-binary-tree/description/
# https://leetcode.ca/all/1740.html

"""

1740. Find Distance in a Binary Tree
Given the root of a binary tree and two integers p and q, return the distance between the nodes of value p and value q in the tree.

The distance between two nodes is the number of edges on the path from one to the other.

 

Example 1:


Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 0
Output: 3
Explanation: There are 3 edges between 5 and 0: 5-3-1-0.
Example 2:


Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 7
Output: 2
Explanation: There are 2 edges between 5 and 7: 5-2-7.
Example 3:


Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 5
Output: 0
Explanation: The distance between a node and itself is 0.
 

Constraints:

The number of nodes in the tree is in the range [1, 104].
0 <= Node.val <= 109
All Node.val are unique.
p and q are values in the tree.
Difficulty:
Medium
Lock:
Prime
Company:
Amazon
Problem Solution
1740-Find-Distance-in-a-Binary-Tree
All Problems:
Link to All Problems


"""


# V0
class Solution:
    def findDistance(self, root, p, q):
        if p == q or not root:
            return 0
            
        # Step 1: Find the Lowest Common Ancestor node
        lca = self.get_lca(root, p, q)
        
        # Step 2: Calculate path distances starting from that LCA node
        dist_p = self.get_dist(lca, p, 0)
        dist_q = self.get_dist(lca, q, 0)
        
        """
        NOTE !!!

        why we can just do `dist_p + dist_q` as final dist ??

        -> ONLY two structural cases:


        case 1:

                  LCA
                 /   \
                p     q


        case 2:


                p (=LCA)
                  \
                   ...
                     q


        """
        # Total distance is always the sum of both paths from the LCA
        return dist_p + dist_q

    def get_lca(self, root, p, q):
        if not root:
            return None
            
        # Match by node values since p and q are integers
        if root.val == p or root.val == q:
            return root
            
        left = self.get_lca(root.left, p, q)
        right = self.get_lca(root.right, p, q)
        
        # If both sides return a node, the current node is their split point (LCA)
        if left and right:
            return root
            
        return left if left else right

    # NOTE !!! below helper func
    # pre-order DFS
    def get_dist(self, root, target, dist):
        """
        NOTE !!!
        If not root, we return `-1`
        but NOT `0`, since 0 is also considered as a valid state (found at current node).
        """
        if not root:
            return -1

        if root.val == target:
            return dist

        left = self.get_dist(root.left, target, dist + 1)
        right = self.get_dist(root.right, target, dist + 1)

        # If the left subtree found the target, pass that valid distance up
        if left != -1:
            return left

        # Otherwise, return whatever the right subtree found (either a valid distance or -1)
        return right

# V1-1
# IDEA: LCA + get_dist + DFS (gpt)
class Solution:
    def findDistance(self, root, p, q):
        if p == q:
            return 0

        lca = self.get_lca(root, p, q)

        dist_p = self.get_dist(lca, p, 0)
        dist_q = self.get_dist(lca, q, 0)

        return dist_p + dist_q

    def get_lca(self, root, p, q):
        if not root:
            return None

        if root.val == p or root.val == q:
            return root

        left = self.get_lca(root.left, p, q)
        right = self.get_lca(root.right, p, q)

        if left and right:
            return root

        return left if left else right

    def get_dist(self, root, target, dist):
    	"""
    	NOTE !!!

    	if not root, we return `-1`

    	but NOT `0`, since 0 is also considered as a valid state
    	"""
        if not root:
            return -1

        if root.val == target:
            return dist

        left = self.get_dist(root.left, target, dist + 1)
        if left != -1:
            return left

        return self.get_dist(root.right, target, dist + 1)


# V1-2
# IDEA: LCA + get_dist + DFS (GEMINI)
class Solution(object):
    def findDistance(self, root, p, q):
        """
        :type root: TreeNode
        :type p: int
        :type q: int
        :rtype: int
        """
        if p == q:
            return 0
            
        # FIX 1: Pass 'root' and added 'self.' prefix 
        lca = self.get_lca(root, p, q)
        
        # Calculate distances relative to the LCA node
        dist_p = self.get_dist(lca, p, 0)
        dist_q = self.get_dist(lca, q, 0)
        
        # Simple unified return works for all cases!
        return dist_p + dist_q

    def get_lca(self, root, p, q):
        # FIX 2: Added missing empty tree base case safety rule
        if not root:
            return None
            
        # FIX 3: Compare against node integer values (.val) instead of objects
        if root.val == p or root.val == q:
            return root
        
        _left = self.get_lca(root.left, p, q)
        _right = self.get_lca(root.right, p, q)
        
        if _left and _right:
            return root
        return _left if _left else _right

    def get_dist(self, root, target_val, dist):
        if not root:
            return -1  # Return -1 to clearly signify a dead end path
        
        if root.val == target_val:
            return dist
            
        # FIX 4: Capture and return the valid path results back up the chain
        left_dist = self.get_dist(root.left, target_val, dist + 1)
        if left_dist != -1:
            return left_dist
            
        return self.get_dist(root.right, target_val, dist + 1)



# V2-1
# https://leetcode.ca/2021-03-23-1740-Find-Distance-in-a-Binary-Tree/
class Solution:
    def findDistance(self, root: Optional[TreeNode], p: int, q: int) -> int:
        def lca(root, p, q):
            if root is None or root.val in [p, q]:
                return root
            left = lca(root.left, p, q)
            right = lca(root.right, p, q)
            if left is None:
                return right
            if right is None:
                return left
            return root

        def dfs(root, v):
            if root is None:
                return -1
            if root.val == v:
                return 0
            left, right = dfs(root.left, v), dfs(root.right, v)
            if left == right == -1:
                return -1
            return 1 + max(left, right)

        g = lca(root, p, q)
        return dfs(g, p) + dfs(g, q)




# V2-2
# https://leetcode.ca/2021-03-23-1740-Find-Distance-in-a-Binary-Tree/
class Solution:
    def findDistance(self, root: TreeNode, p: int, q: int) -> int:
        # initialize variables to hold the depth of the target nodes and their common ancestor
        p_depth, q_depth, lca = -1, -1, None

        def dfs(node: TreeNode, depth: int) -> None:
            nonlocal p_depth, q_depth, lca
            if not node:
                return

            # check if the current node is one of the target nodes
            if node.val == p:
                p_depth = depth
            elif node.val == q:
                q_depth = depth

            # if both target nodes are found, return the LCA and stop searching
            if p_depth != -1 and q_depth != -1 and not lca:
                lca = node
                return

            # traverse the left and right subtrees
            dfs(node.left, depth + 1)
            dfs(node.right, depth + 1)

            # if the current node is the LCA, return it
            if node == lca:
                return

            # if one of the target nodes is found in the left or right subtree, update its depth
            if p_depth != -1 and q_depth == -1 and (node.left and node.left.val == q or node.right and node.right.val == q):
                q_depth = depth + 1
            elif p_depth == -1 and q_depth != -1 and (node.left and node.left.val == p or node.right and node.right.val == p):
                p_depth = depth + 1

        dfs(root, 0)

        # if one or both target nodes are not found, return -1
        if p_depth == -1 or q_depth == -1:
            return -1

        # return the distance between the target nodes
        return abs(p_depth - q_depth)





