"""

1660. Correct a Binary Tree
Medium

You have a binary tree with a small defect. There is exactly one invalid node where its right child
incorrectly points to another node at the same depth but to the invalid node's right.

Given the root of the binary tree with this defect, root, return the root of the binary tree after
removing this invalid node and every node underneath it (minus the node it incorrectly points to).

Custom testing:

The test input is read as 3 lines:

TreeNode root
int fromNode (not available to correctBinaryTree)
int toNode (not available to correctBinaryTree)

After the binary tree rooted at root is parsed, the TreeNode with value of fromNode will have its
right child pointer pointing to the TreeNode with a value of toNode. Then, root is passed to
correctBinaryTree.


Example 1:

Input: root = [1,2,3], fromNode = 2, toNode = 3
Output: [1,null,3]
Explanation: The node with value 2 is invalid, so remove it.

Example 2:

Input: root = [8,3,1,7,null,9,4,2,null,null,null,5,6], fromNode = 7, toNode = 4
Output: [8,3,1,null,null,9,4,null,null,5,6]
Explanation: The node with value 7 is invalid, so remove it and the node underneath it, node 2.


Constraints:

The number of nodes in the tree is in the range [3, 10^4].
-10^9 <= Node.val <= 10^9
All Node.val are unique.
fromNode != toNode
fromNode and toNode will exist in the tree and will be on the same depth.
toNode is to the right of fromNode.
fromNode.right is null in the initial tree from the test data.

"""

# V0
# IDEA : DFS VISITING RIGHT-TO-LEFT (the bad edge points to an ALREADY seen node)
#
#   the invalid node's right child is a node on the SAME depth but further RIGHT.
#   so if we traverse each node's RIGHT subtree before its LEFT subtree, the
#   target node is guaranteed to have been visited BEFORE we reach the invalid one.
#
#   -> keep a `seen` set of nodes; when node.right is already in `seen`,
#      this node is the invalid one -> return None to detach it (and its subtree).
#
#   NOTE : the set must hold the NODE objects (values are unique but identity is
#          what the defective pointer shares).
#   NOTE : must recurse right first, then left, otherwise `seen` is not yet populated.
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def correctBinaryTree(self, root):
        seen = set()

        def dfs(node):
            if node is None:
                return None
            if node.right is not None and node.right in seen:
                return None
            seen.add(node)
            node.right = dfs(node.right)
            node.left = dfs(node.left)
            return node

        return dfs(root)
