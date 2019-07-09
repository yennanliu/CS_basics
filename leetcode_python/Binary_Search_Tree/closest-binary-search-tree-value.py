# V0 

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