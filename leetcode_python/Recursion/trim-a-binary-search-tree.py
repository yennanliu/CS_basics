# V0 
# IDEA :RECURSION, BST
# IDEA : USE BST'S PROPERTY : 
# -> FOR EVERY NODE : right > node > left 
# -> USE ABOVE PROPERTY FOR BST TRIMMING
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
