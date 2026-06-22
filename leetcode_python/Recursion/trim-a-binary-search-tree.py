"""

669. Trim a Binary Search Tree
Medium

Given the root of a binary search tree and the lowest and highest boundaries as low and high, trim the tree so that all its elements lies in [low, high]. Trimming the tree should not change the relative structure of the elements that will remain in the tree (i.e., any node's descendant should remain a descendant). It can be proven that there is a unique answer.

Return the root of the trimmed binary search tree. Note that the root may change depending on the given bounds.

 

Example 1:


Input: root = [1,0,2], low = 1, high = 2
Output: [1,null,2]
Example 2:


Input: root = [3,0,4,null,2,null,null,1], low = 1, high = 3
Output: [3,2,null,1]
Example 3:

Input: root = [1], low = 1, high = 2
Output: [1]
Example 4:

Input: root = [1,null,2], low = 1, high = 3
Output: [1,null,2]
Example 5:

Input: root = [1,null,2], low = 2, high = 4
Output: [2]
 

Constraints:

The number of nodes in the tree in the range [1, 104].
0 <= Node.val <= 104
The value of each node in the tree is unique.
root is guaranteed to be a valid binary search tree.
0 <= low <= high <= 104

"""

# V0
# IDEA: BST PROPERTY + DFS (POST ORDER) (GEMINI)
class Solution(object):
    def trimBST(self, root, low, high):
        """
        :type root: Optional[TreeNode]
        :type low: int
        :type high: int
        :rtype: Optional[TreeNode]
        """
        # Base Case: An empty tree needs no trimming
        if not root:
            return None
            
        # Scenario 1: The current root value is too small.
        # Everything in the left subtree is guaranteed to be even smaller, so discard it entirely.
        # The new root for this position will be the trimmed version of the right subtree.
        if root.val < low:
            return self.trimBST(root.right, low, high)
            
        # Scenario 2: The current root value is too large.
        # Everything in the right subtree is guaranteed to be even larger, so discard it entirely.
        # The new root for this position will be the trimmed version of the left subtree.
        if root.val > high:
            return self.trimBST(root.left, low, high)
            
        # Scenario 3: The current root value is perfectly within bounds [low, high].
        # Keep this node, but recursively trim both its left and right subtrees.
        root.left = self.trimBST(root.left, low, high)
        root.right = self.trimBST(root.right, low, high)
        
        # Return the safely modified node back up to its parent
        return root


# V0-1
# IDEA: BST PROPERTY + DFS (POST ORDER) (GPT)
class Solution(object):
    def trimBST(self, root, low, high):
        # Base case:
        # If the current node is None, there is nothing to trim.
        if not root:
            return None

        # Case 1:
        # Current node's value is smaller than the allowed range.
        #
        # BST property:
        #   root.left  < root.val < root.right
        #
        # Since root.val < low, every node in the LEFT subtree
        # must also be < low.
        #
        # Therefore:
        #   - root is invalid
        #   - entire left subtree is invalid
        # We can safely discard both and continue trimming
        # only the right subtree.
        if root.val < low:
            return self.trimBST(root.right, low, high)

        # Case 2:
        # Current node's value is larger than the allowed range.
        #
        # Since root.val > high, every node in the RIGHT subtree
        # must also be > high.
        #
        # Therefore:
        #   - root is invalid
        #   - entire right subtree is invalid
        # We can safely discard both and continue trimming
        # only the left subtree.
        if root.val > high:
            return self.trimBST(root.left, low, high)

        # Case 3:
        # Current node is inside [low, high].
        #
        # We keep the current node, but its children may still
        # contain invalid nodes, so recursively trim them.
        #
        # The returned subtree root becomes the new left child.
        root.left = self.trimBST(root.left, low, high)

        # Similarly trim the right subtree and reconnect it.
        root.right = self.trimBST(root.right, low, high)

        # Return the current node as the root of the trimmed subtree.
        return root


# V0
# IDEA :RECURSION, BST
# IDEA : USE BST'S PROPERTY : 
# -> FOR EVERY NODE : right > node > left 
# -> USE ABOVE PROPERTY FOR BST TRIMMING
# NOTE : Trimming the tree should not change the relative structure of the elements that will remain in the tree (i.e., any node's descendant should remain a descendant)
#    -> e.g. if there is a node outside of (L,R), we have to NOT ONLY delete that node,  BUT ALSO append all of the sub tree of that node
class Solution:
    def trimBST(self, root, L, R):
        if not root:
            return 
        # NOTICE HERE 
        # SINCE IT'S BST
        # SO if root.val < L, THE root.right MUST LARGER THAN L
        # SO USE self.trimBST(root.right, L, R) TO FIND THE NEXT "VALIDATE" ROOT AFTER TRIM
        # THE REASON USE self.trimBST(root.right, L, R) IS THAT MAYBE NEXT ROOT IS TRIMMED AS WELL, SO KEEP FINDING VIA RECURSION
        if root.val < L:
            return self.trimBST(root.right, L, R)
        # NOTICE HERE 
        # SINCE IT'S BST
        # SO if root.val > R, THE root.left MUST SMALLER THAN R
        # SO USE self.trimBST(root.left, L, R) TO FIND THE NEXT "VALIDATE" ROOT AFTER TRIM
        if root.val > R:
            return self.trimBST(root.left, L, R)
        root.left = self.trimBST(root.left, L, R)
        root.right = self.trimBST(root.right, L, R)
        return root 

### NOTE : below is WRONG !!!
# -> 1) if there is a node outside of (L,R), we have to NOT ONLY delete that node,  BUT ALSO append all of the sub tree of that node
# -> 2) we should reutrn root as final step
# class Solution(object):
#     def trimBST(self, root, low, high):
#         print ("root.val = " + str(root.val))
#         if not root:
#             return root
#         elif root.val < low or root.val > high:
#             return None
#         else:
#             return root
#         root.left = self.trimBST(root.left, low, high)
#         root.right = self.trimBST(root.right, low, high)

# V0'
# IDEA : DFS
class Solution(object):
    def trimBST(self, root, low, high):
        def dfs(root):
            if not root:
                return
            if root.val < low:
                # NOTE THIS !!!
                return  dfs(root.right)
            if root.val > high:
                # NOTE THIS !!!
                return dfs(root.left)
            root.left = dfs(root.left)
            root.right = dfs(root.right)
            return root

        # edge case
        if not root:
            return
        
        """
        NOTE THIS !!!
        """
        _root = dfs(root)
        print ("-> _root = " + str(_root))
        return _root

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83869684
class Solution:
    def trimBST(self, root, L, R):
        """
        :type root: TreeNode
        :type L: int
        :type R: int
        :rtype: TreeNode
        """
        if not root:
            return None
        if root.val > R:
            return self.trimBST(root.left, L, R)
        elif root.val < L:
            return self.trimBST(root.right, L, R)
        else:
            root.left = self.trimBST(root.left, L, R)
            root.right = self.trimBST(root.right, L, R)
            return root

### Test case : dev

# V1'
# https://www.polarxiong.com/archives/LeetCode-669-trim-a-binary-search-tree.html
# Definition for a binary tree node.
# class TreeNode:
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution:
    def trimBST(self, root, L, R):
        """
        :type root: TreeNode
        :type L: int
        :type R: int
        :rtype: TreeNode
        """
        if not root:
            return root
        if root.val < L:
            return self.trimBST(root.right, L, R)
        if root.val > R:
            return self.trimBST(root.left, L, R)
        root.left = self.trimBST(root.left, L, R)
        root.right = self.trimBST(root.right, L, R)
        return root

# V1''
# https://leetcode.com/problems/trim-a-binary-search-tree/discuss/107051/Python-Easy-to-Understand
# Time: O(n)
# Space: O(lgn)
class Solution(object):
    def trimBST(self, root, L, R):
        def trim(node):
            if not node:
                return None
            node.left, node.right = trim(node.left), trim(node.right)
            # Node's value is not within range, 
            # select one or none of its children as replacement.
            if not (L <= node.val <= R):
                node = node.left if node.left else node.right
            return node
        return trim(root)

# V1'''
# IDEA : RECURSION
# https://leetcode.com/problems/trim-a-binary-search-tree/solution/
class Solution(object):
    def trimBST(self, root, L, R):
        def trim(node):
            if not node:
                return None
            elif node.val > R:
                return trim(node.left)
            elif node.val < L:
                return trim(node.right)
            else:
                node.left = trim(node.left)
                node.right = trim(node.right)
                return node
        return trim(root)

# V1''''
# https://leetcode.com/problems/trim-a-binary-search-tree/discuss/107056/Python-Straightforward-with-Explanation
class Solution(object):
    def trimBST(self, root, L, R):
        def trim(node):
            if node:
                if node.val > R:
                    return trim(node.left)
                elif node.val < L:
                    return trim(node.right)
                else:
                    node.left = trim(node.left)
                    node.right = trim(node.right)
                    return node
        return trim(root)

# V2
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def trimBST(self, root, L, R):
        """
        :type root: TreeNode
        :type L: int
        :type R: int
        :rtype: TreeNode
        """
        if not root:
            return None
        if root.val < L:
            return self.trimBST(root.right, L, R)
        if root.val > R:
            return self.trimBST(root.left, L, R)
        root.left, root.right = self.trimBST(root.left, L, R), self.trimBST(root.right, L, R)
        return root
