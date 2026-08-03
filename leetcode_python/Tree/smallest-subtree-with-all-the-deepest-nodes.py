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


# V0-1

# V0-2


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
