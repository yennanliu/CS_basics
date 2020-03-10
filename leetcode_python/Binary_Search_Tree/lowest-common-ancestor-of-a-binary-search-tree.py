# V0
# IDEA : GO THROUGH ALL BST (no need to use BFS, or DFS, can just use BST property)
# THIS METHOD IS MORE GENERAL
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """
        pathp = self.findPath(root, p)
        pathq = self.findPath(root, q)
        res = root
        for i in range(1, min(len(pathp), len(pathq))):
            if pathp[i] == pathq[i]:
                res = pathp[i]
        return res


    def findPath(self, root, p):
        path = []
        while root.val != p.val:
            path.append(root)
            if p.val > root.val:
                root = root.right
            elif p.val < root.val:
                root = root.left
        path.append(p)
        return path

# V0'
# IDEA : BST PROPERTY
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """
        pointer = root
        while pointer:
            if p.val > pointer.val and q.val > pointer.val:
                pointer = pointer.right
            elif p.val < pointer.val and q.val < pointer.val:
                pointer = pointer.left
            else:
                return pointer

# V0''
# IDEA : BST PROPERTY
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        if not root or root == q or root == p:
            return root
        if p.val < root.val and q.val < root.val:
            return  self.lowestCommonAncestor(root.left, p, q)
        elif p.val > root.val and q.val > root.val:
            return self.lowestCommonAncestor(root.right, p, q)
        return root

# V1 
# https://blog.csdn.net/coder_orz/article/details/51498796
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """
        pointer = root
        while pointer:
            if p.val > pointer.val and q.val > pointer.val:
                pointer = pointer.right
            elif p.val < pointer.val and q.val < pointer.val:
                pointer = pointer.left
            else:
                return pointer
                
# V1'
# https://blog.csdn.net/coder_orz/article/details/51498796
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """
        if not root:
            return None
        if p.val < root.val and q.val < root.val:
            return self.lowestCommonAncestor(root.left, p, q)
        elif p.val > root.val and q.val > root.val:
            return self.lowestCommonAncestor(root.right, p, q)
        else:
            return root

# V1''
# https://blog.csdn.net/coder_orz/article/details/51498796
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """
        pathp = self.findPath(root, p)
        pathq = self.findPath(root, q)
        res = root
        for i in xrange(1, min(len(pathp), len(pathq))):
            if pathp[i] == pathq[i]:
                res = pathp[i]
        return res


    def findPath(self, root, p):
        path = []
        while root.val != p.val:
            path.append(root)
            if p.val > root.val:
                root = root.right
            elif p.val < root.val:
                root = root.left
        path.append(p)
        return path

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param {TreeNode} root
    # @param {TreeNode} p
    # @param {TreeNode} q
    # @return {TreeNode}
    def lowestCommonAncestor(self, root, p, q):
        s, b = sorted([p.val, q.val])
        while not s <= root.val <= b:
            # Keep searching since root is outside of [s, b].
            root = root.left if s <= root.val else root.right
        # s <= root.val <= b.
        return root