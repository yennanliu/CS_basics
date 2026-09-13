"""

1644. Lowest Common Ancestor of a Binary Tree II
Medium

Given the root of a binary tree, return the lowest common ancestor (LCA) of two given
nodes, p and q. If either node p or q does NOT exist in the tree, return null.
All values of the nodes in the tree are unique.

According to the definition of LCA on Wikipedia: "The lowest common ancestor of two nodes
p and q in a binary tree T is the lowest node that has both p and q as descendants (where
we allow a node to be a descendant of itself)". A descendant of a node x is a node y that
is on the path from node x to some leaf node.


Example 1:

Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
Output: 3
Explanation: The LCA of nodes 5 and 1 is 3.

Example 2:

Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
Output: 5
Explanation: The LCA of nodes 5 and 4 is 5. A node can be a descendant of itself
according to the definition of LCA.

Example 3:

Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 10
Output: null
Explanation: Node 10 does not exist in the tree, so return null.


Constraints:

The number of nodes in the tree is in the range [1, 10^4].
-10^9 <= Node.val <= 10^9
All Node.val are unique.
p != q


Follow up: Can you find the LCA traversing the tree, without checking nodes existence?

"""

# V0
# IDEA : POST ORDER DFS + COUNT THE HITS (LC 236, one pass)
#
#   LC 236's recursion is NOT enough here: it happily returns p when only p is in
#   the tree, but this problem wants null unless BOTH exist. So run the same
#   recursion and additionally count how many targets were actually seen.
#
#   e.g. p = 5, q = 10 (not in tree) -> dfs still bubbles 5 up to the root,
#        but found == 1 -> return None
#
#   NOTE !!! the p/q check must happen AFTER recursing into both children
#            (post order). LC 236 returns the moment root == p, which would skip
#            p's subtree -- and q may be sitting inside it, so it would never be
#            counted and a valid answer would become None.
#
# time = O(n), space = O(h)
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
        # edge
        if not root or not p or not q:
            return None

        self.found = 0

        def dfs(node):
            if not node:
                return None

            # NOTE !!! children FIRST, so a target nested under p/q is still counted
            left = dfs(node.left)
            right = dfs(node.right)

            if node == p or node == q:
                self.found += 1
                return node

            # p, q are on different sides -> split point -> this node is the LCA
            if left and right:
                return node

            return left or right

        lca = dfs(root)

        # only a real LCA if BOTH p and q were actually met
        return lca if self.found == 2 else None


# V0-1
# IDEA : 2 PASS (existence check, then plain LC 236)
#
#   Justified as a separate variant: it trades O(n) extra space for not having to
#   get the post-order subtlety above right -- pass 1 proves both nodes exist,
#   pass 2 is LC 236 verbatim (and may early-return at root == p).
#
# time = O(n), space = O(n)
class Solution2(object):
    def lowestCommonAncestor(self, root, p, q):
        if not root or not p or not q:
            return None

        seen = set()

        def collect(node):
            if not node:
                return
            seen.add(node.val)
            collect(node.left)
            collect(node.right)

        collect(root)

        if p.val not in seen or q.val not in seen:
            return None

        def lca(node):
            if not node or node == p or node == q:
                return node
            left = lca(node.left)
            right = lca(node.right)
            if left and right:
                return node
            return left or right

        return lca(root)
