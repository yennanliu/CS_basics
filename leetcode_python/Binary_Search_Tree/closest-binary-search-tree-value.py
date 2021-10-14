"""

LeetCode 270. Closest Binary Search Tree Value

https://goodtecher.com/leetcode-270-closest-binary-search-tree-value/

Description
https://leetcode.com/problems/closest-binary-search-tree-value/

Given the root of a binary search tree and a target value, return the value in the BST that is closest to the target.

Example 1:


Input: root = [4,2,5,1,3], target = 3.714286
Output: 4
Example 2:

Input: root = [1], target = 4.428571
Output: 1
Constraints:

The number of nodes in the tree is in the range [1, 104].
0 <= Node.val <= 109
-109 <= target <= 109
Explanation
Base on the characteristics of binary search tree to search for the target.

"""

# V0
# IDEA : BST property + tree traversal
###     -> BST property : left < root < right (value ordering)
class Solution(object):
    def closestValue(self, root, target):

        gap = abs(root.val - target)
        ans = root

        ### INORDER TRANSVERSAL
        while root is not None:

            if root.val == target:
                return root.val

            # case 1) target < root.val
            elif target < root.val:
                # if found the other candidate that more close to the target
                if abs(root.val - target) < gap:
                    ans = root
                    gap = abs(root.val - target)
                # move left
                root = root.left
            # case 2) target > root.val
            else:
                if abs(root.val - target) < gap:
                    ans = root
                    gap = abs(root.val - target)
                root = root.right
        return ans.val

# V0'
# KEY : BST PROPERTY : RIGHT > ROOT > LEFT
class Solution(object):
    def closestValue(self, root, target):
        a = root.val
        kid = root.left if target < a else root.right
        if not kid: return a
        b = self.closestValue(kid, target)
        return min((b, a), key=lambda x: abs(target - x))

# V0'' : IDEA : DFS + SORT -> NEED TO VALIDATE
# class Solution(object):
#     def closestValue(self, root, target):
#
#         def help(root):
#             _list.append(root.val)
#             if root.left:
#                 self.help(root.left)
#             if root.right:
#                 self.help(root.right)
#
#         _list = []
#         help(root)
#         _list.sort()
#
#         _target = round(target)
#
#         if _target in _list:
#             return _target
#
#         _add = 1
#
#         while _target not in _list:
#             _tmp1 = _target + _add
#             _tmp2 = _target - _add
#
#             if _tmp1 in _list:
#                 return _tmp1
#             if _tmp2 in _list:
#                 return _tmp2
#             _add += 1

# V1 
# http://www.voidcn.com/article/p-phbluudb-qp.html
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def closestValue(self, root, target):
        """
        :type root: TreeNode
        :type target: float
        :rtype: int
        """
        gap = abs(root.val - target)
        ans = root
        while root is not None:
            if root.val == target:
                return root.val
            elif target < root.val:
                if abs(root.val - target) < gap:
                    ans = root
                    gap = abs(root.val - target)
                root = root.left
            else:
                if abs(root.val - target) < gap:
                    ans = root
                    gap = abs(root.val - target)
                root = root.right
        return ans.val

# V1'
# http://www.voidcn.com/article/p-phbluudb-qp.html
class Solution(object):
    def closestValue(self, root, target):
        a = root.val
        kid = root.left if target < a else root.right
        if not kid: return a
        b = self.closestValue(kid, target)
        return min((b, a), key=lambda x: abs(target - x))

# V1''
# http://www.voidcn.com/article/p-phbluudb-qp.html
class Solution(object):
    def closestValue(self, root, target):
        path = []
        while root:
            path += root.val,
            root = root.left if target < root.val else root.right
        return min(path[::-1], key=lambda x: abs(target - x))

# V1'''
# https://www.jiuzhang.com/solution/closest-binary-search-tree-value/#tag-highlight-lang-python
class Solution:
    """
    @param root: the given BST
    @param target: the given target
    @return: the value in the BST that is closest to the target
    """
    def closestValue(self, root, target):
        upper = root
        lower = root
        while root:
            if target > root.val:
                lower = root
                root = root.right
            elif target < root.val:
                upper = root
                root = root.left
            else:
                return root.val
        if abs(upper.val - target) <= abs(lower.val - target):
            return upper.val
        return lower.val

# V1''''
# https://www.jiuzhang.com/solution/closest-binary-search-tree-value/#tag-highlight-lang-python
class Solution:
    """
    @param root: the given BST
    @param target: the given target
    @return: the value in the BST that is closest to the target
    """
    def closestValue(self, root, target):
        if root is None:
            return None
            
        lower = self.get_lower_bound(root, target)
        upper = self.get_upper_bound(root, target)
        if lower is None:
            return upper.val
        if upper is None:
            return lower.val
            
        if target - lower.val < upper.val - target:
            return lower.val
        return upper.val
        
    def get_lower_bound(self, root, target):
        # get the largest node that < target
        if root is None:
            return None
        
        if target < root.val:
            return self.get_lower_bound(root.left, target)
            
        lower = self.get_lower_bound(root.right, target)
        return root if lower is None else lower
        
    def get_upper_bound(self, root, target):
        # get the smallest node that >= target
        if root is None:
            return None
        
        if target >= root.val:
            return self.get_upper_bound(root.right, target)
            
        upper = self.get_upper_bound(root.left, target)
        return root if upper is None else upper

# V2 
# Time:  O(h)
# Space: O(1)
class Solution(object):
    def closestValue(self, root, target):
        """
        :type root: TreeNode
        :type target: float
        :rtype: int
        """
        gap = float("inf")
        closest = float("inf")
        while root:
            if abs(root.val - target) < gap:
                gap = abs(root.val - target)
                closest = root.val
            if target == root.val:
                break
            elif target < root.val:
                root = root.left
            else:
                root = root.right
        return closest