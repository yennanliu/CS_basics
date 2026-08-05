"""

865. Smallest Subtree with all the Deepest Nodes
Solved
Medium
Topics
premium lock icon
Companies
Given the root of a binary tree, the depth of each node is the shortest distance to the root.

Return the smallest subtree such that it contains all the deepest nodes in the original tree.

A node is called the deepest if it has the largest depth possible among any node in the entire tree.

The subtree of a node is a tree consisting of that node, plus the set of all descendants of that node.

 

Example 1:


Input: root = [3,5,1,6,2,0,8,null,null,7,4]
Output: [2,7,4]
Explanation: We return the node with value 2, colored in yellow in the diagram.
The nodes coloured in blue are the deepest nodes of the tree.
Notice that nodes 5, 3 and 2 contain the deepest nodes in the tree but node 2 is the smallest subtree among them, so we return it.
Example 2:

Input: root = [1]
Output: [1]
Explanation: The root is the deepest node in the tree.
Example 3:

Input: root = [0,1,3,null,2]
Output: [2]
Explanation: The deepest node in the tree is 2, the valid subtrees are the subtrees of nodes 2, 1 and 0 but the subtree of node 2 is the smallest.
 

Constraints:

The number of nodes in the tree will be in the range [1, 500].
0 <= Node.val <= 500
The values of the nodes in the tree are unique.
 

Note: This question is the same as 1123: https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/


"""


# V0
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        pass


# V0-0-0-1
# IDEA: DFS + LCA (GPT) (straightforward)
"""
NOTE !!!


helper func (dfs) needs to return below 2 items:

 - the deepest depth in this subtree
 - the subtree containing all deepest nodes

"""
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        return self.helper(root)[1]

    # return (max_depth, answer_node)
    def helper(self, root):
        if not root:
            return (0, None)

        left_depth, left_node = self.helper(root.left)
        right_depth, right_node = self.helper(root.right)

        if left_depth > right_depth:
            return (left_depth + 1, left_node)

        if right_depth > left_depth:
            return (right_depth + 1, right_node)

        return (left_depth + 1, root)


# V0-0-1
# IDEA: DFS + LCA (gemini) (straightforward)
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        if not root:
            return None
            
        # 1. Build your dictionary of {node: depth}
        self.d_map = {}
        self.build_depth_map(root, 0)
        
        # 2. Find the absolute maximum depth in the tree
        max_depth = max(self.d_map.values())
        

        """
        NOTE !!! below,

        how we find the LCA from given node
        """
        # 3. Find where the nodes with max_depth intersect!
        return self.find_lca(root, max_depth)

    # Your original helper (renamed for clarity)
    def build_depth_map(self, root, depth):
        if not root:
            return
            
        self.d_map[root] = depth
        self.build_depth_map(root.left, depth + 1)
        self.build_depth_map(root.right, depth + 1)


    """
    NOTE !!! below,

    helper func gets LCA
    """
    # The missing step: Finding the Lowest Common Ancestor
    def find_lca(self, node, max_depth):
        # Base case
        if not node:
            return None
            
        # If this specific node is one of the deepest nodes, return it!
        if self.d_map[node] == max_depth:
            return node
            
        # Look for deepest nodes in the left and right subtrees
        _left = self.find_lca(node.left, max_depth)
        _right = self.find_lca(node.right, max_depth)
        
        # If both left and right branches found a deepest node, 
        # THIS current node is where they meet!
        if _left and _right:
            return node
            
        # Otherwise, just pass up whichever side found something
        return _left if _left else _right



# V0-0-2
# IDEA: LCA (GEMINI)
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        self.max_depth = -1
        self.node = None
        
        self.helper(root, 0)
        
        return self.node
        
    # post order DFS
    # return: the maximum depth found in this subtree
    def helper(self, root, depth):
        # 1. Base case: If we hit a null node, return the depth we are currently at
        if not root:
            return depth
            
        # 2. Ask the left and right children how deep they go
        _left_depth = self.helper(root.left, depth + 1)
        _right_depth = self.helper(root.right, depth + 1)
        
        # 3. If both sides go to the exact same depth, THIS node is their LCA!
        # We also check if this depth is >= the deepest we've ever seen overall.
        if _left_depth == _right_depth and _left_depth >= self.max_depth:
            self.max_depth = _left_depth
            self.node = root
            
        # 4. Return the deepest value found back up to the parent
        return max(_left_depth, _right_depth)


# V0-1
# IDEA: LCA (gpt)
"""

helper func:

 - the deepest depth in the subtree
 
 - the subtree root containing all deepest nodes
"""
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        return self.helper(root)[1]

    def helper(self, root):
        # Base case
        if not root:
            return (0, None)

        # Process left subtree
        left_depth, left_node = self.helper(root.left)

        # Process right subtree
        right_depth, right_node = self.helper(root.right)

        # Left subtree is deeper
        if left_depth > right_depth:
            return (left_depth + 1, left_node)

        # Right subtree is deeper
        if right_depth > left_depth:
            return (right_depth + 1, right_node)

        # Both sides have the `same depth`
        # Current node is the LCA of deepest nodes
        return (left_depth + 1, root)


# V0-2
# IDEA: LCA (gemini)
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        # The helper returns a tuple: (depth, lca_node)
        depth, lca_node = self.dfs_helper(root)
        
        # We only need the node to return to LeetCode
        return lca_node
        
    def dfs_helper(self, node):
        # 1. Base case: Null nodes have a depth of 0 and no LCA
        if not node:
            return 0, None
            
        # 2. Post-order: Ask the children for their max depth and LCA
        left_depth, left_lca = self.dfs_helper(node.left)
        right_depth, right_lca = self.dfs_helper(node.right)
        
        # 3. Evaluate based on the depths returned by the children
        
        if left_depth == right_depth:
            # The deepest leaves are perfectly balanced on both sides of this node.
            # This makes the CURRENT node the Lowest Common Ancestor!
            return left_depth + 1, node
            
        elif left_depth > right_depth:
            # The deepest leaves are entirely in the left subtree.
            # We pass the left_lca upwards.
            return left_depth + 1, left_lca
            
        else:
            # The deepest leaves are entirely in the right subtree.
            # We pass the right_lca upwards.
            return right_depth + 1, right_lca



# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82432130
# https://www.cnblogs.com/grandyang/p/10703653.html
# IDEA :
# CASE 1) : depth(left) == depth(right) -> return root 
# CASE 2) : depth(left) >  depth(right) -> return left , depth(left) <  depth(right) -> return right  
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        return self.depth(root)[1]
        
    def depth(self, root):
        if not root: return 0, None
        l, r = self.depth(root.left), self.depth(root.right)
        if l[0] > r[0]:
            return l[0] + 1, l[1]
        elif l[0] < r[0]:
            return r[0] + 1, r[1]
        else:
            return l[0] + 1, root

# V2
# time = O(n)
# space = O(h), h = tree height
import collections
class Solution(object):
    def subtreeWithAllDeepest(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        Result = collections.namedtuple("Result", ("node", "depth"))

        def dfs(node):
            if not node:
                return Result(None, 0)
            left, right = dfs(node.left), dfs(node.right)
            if left.depth > right.depth:
                return Result(left.node, left.depth+1)
            if left.depth < right.depth:
                return Result(right.node, right.depth+1)
            return Result(node, left.depth+1)

        return dfs(root).node
