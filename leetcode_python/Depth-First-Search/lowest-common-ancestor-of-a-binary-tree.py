# V0 
class Solution(object):
    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """
        if not root or p == root or q == root:
            return root
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        if left and right:
            return root
        return left if left else right

# V0'
# DFS : TO FIX 
# class Solution(object):
#     def lowestCommonAncestor(self, root, p, q):
#         if not root or p == root or q == root:
#             return root
#         r = []
#         r_p = self.dfs(root, p, r)
#         r_q = self.dfs(root, q, r)
#         if len(r_p) > len(r_q):
#             r_long, r_short = r_p, r_q 
#         else:
#             r_long, r_short = r_q, r_p
#         return r_short[-1]
#     def dfs(self, root, t, r):
#         if not root or t == root: 
#             return root
#         r.append(root.val)
#         if t in r:
#             return r 
#         self.dfs(root.left, t, r)
#         self.dfs(root.right, t, r)
        
# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80778001
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
        if not root or p == root or q == root:
            return root
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        if left and right:
            return root
        return left if left else right

# V1'
# https://www.jiuzhang.com/solution/lowest-common-ancestor-of-a-binary-tree/#tag-highlight-lang-python
class Solution:
    """
    @param: root: The root of the binary search tree.
    @param: A: A TreeNode in a Binary.
    @param: B: A TreeNode in a Binary.
    @return: Return the lowest common ancestor(LCA) of the two nodes.
    """
    def lowestCommonAncestor(self, root, A, B):
        # A & : if there is B below => A 
        # B & : if there is A below => B 
        # A & : if there is nothing below => A 
        # B & : if there is everything below => B 
        if root is None:
            return None
        
        if root == A or root == B:
            return root
        
        left_result = self.lowestCommonAncestor(root.left, A, B)
        right_result = self.lowestCommonAncestor(root.right, A, B)
        
        # split to A and B 
        if left_result and right_result: 
            return root
        
        # if there is one point or LCA at sub left tree 
        if left_result:
            return left_result
        
        # if there is one point or LCA at sub right tree 
        if right_result:
            return right_result
        
        # if there is nothing at left, right sub tree 
        return None

# V1'
# https://leetcode.com/articles/lowest-common-ancestor-of-a-binary-tree/
# IDEA : BFS 
class Solution:

    def lowestCommonAncestor(self, root, p, q):
        """
        :type root: TreeNode
        :type p: TreeNode
        :type q: TreeNode
        :rtype: TreeNode
        """

        # Stack for tree traversal
        stack = [root]

        # Dictionary for parent pointers
        parent = {root: None}

        # Iterate until we find both the nodes p and q
        while p not in parent or q not in parent:

            node = stack.pop()

            # While traversing the tree, keep saving the parent pointers.
            if node.left:
                parent[node.left] = node
                stack.append(node.left)
            if node.right:
                parent[node.right] = node
                stack.append(node.right)

        # Ancestors set() for node p.
        ancestors = set()

        # Process all ancestors for node p using parent pointers.
        while p:
            ancestors.add(p)
            p = parent[p]

        # The first ancestor of q which appears in
        # p's ancestor set() is their lowest common ancestor.
        while q not in ancestors:
            q = parent[q]
        return q

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    # @param {TreeNode} root
    # @param {TreeNode} p
    # @param {TreeNode} q
    # @return {TreeNode}
    def lowestCommonAncestor(self, root, p, q):
        if root in (None, p, q):
            return root

        left, right = [self.lowestCommonAncestor(child, p, q) \
                         for child in (root.left, root.right)]
        # 1. If the current subtree contains both p and q,
        #    return their LCA.
        # 2. If only one of them is in that subtree,
        #    return that one of them.
        # 3. If neither of them is in that subtree,
        #    return the node of that subtree.
        return root if left and right else left or right