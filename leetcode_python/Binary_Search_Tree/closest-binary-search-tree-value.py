# V0 
# KEY : BST PROPERTY : RIGHT > ROOT > LEFT
class Solution(object):
    def closestValue(self, root, target):
        a = root.val
        kid = root.left if target < a else root.right
        if not kid: return a
        b = self.closestValue(kid, target)
        return min((b, a), key=lambda x: abs(target - x))

# V0'
# IDEA : BST property + tree traversal
class Solution(object):
    def closestValue(self, root, target):
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