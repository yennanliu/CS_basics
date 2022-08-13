"""

700. Search in a Binary Search Tree
Easy

You are given the root of a binary search tree (BST) and an integer val.

Find the node in the BST that the node's value equals val and return the subtree rooted with that node. If such a node does not exist, return null.

 

Example 1:


Input: root = [4,2,7,1,3], val = 2
Output: [2,1,3]
Example 2:


Input: root = [4,2,7,1,3], val = 5
Output: []
 

Constraints:

The number of nodes in the tree is in the range [1, 5000].
1 <= Node.val <= 107
root is a binary search tree.
1 <= val <= 107

"""

# V0

# V1
# IDEA : RECURSION + BST property
# https://leetcode.com/problems/search-in-a-binary-search-tree/solution/
class Solution:
    def searchBST(self, root: TreeNode, val: int) -> TreeNode:
        if root is None or val == root.val:
            return root
        
        return self.searchBST(root.left, val) if val < root.val \
            else self.searchBST(root.right, val)

# V1'
# IDEA : ITERATION
# https://leetcode.com/problems/search-in-a-binary-search-tree/solution/
class Solution:
    def searchBST(self, root: TreeNode, val: int) -> TreeNode:
        while root is not None and root.val != val:
            root = root.left if val < root.val else root.right
        return root

# V1''
# IDEA : ITERATION
# https://leetcode.com/problems/search-in-a-binary-search-tree/discuss/171663/Python-solution
class Solution(object):
    def searchBST(self, root, val):
        trav = root
        while trav:
            if trav.val == val:
                return trav
            elif trav.val < val:
                trav = trav.right
            else:
                trav = trav.left

# V2