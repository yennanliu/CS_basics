"""

101. Symmetric Tree
Easy

Given the root of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).

 

Example 1:


Input: root = [1,2,2,3,4,4,3]
Output: true
Example 2:


Input: root = [1,2,2,null,3,null,3]
Output: false
 

Constraints:

The number of nodes in the tree is in the range [1, 1000].
-100 <= Node.val <= 100
 

Follow up: Could you solve it both recursively and iteratively?

"""

# V0
# IDEA : Iterative
class Solution:
    # @param root, a tree node
    # @return a boolean
    def isSymmetric(self, root):
        if root is None:
            return True
        stack = []
        # NOTE !!! we append root.left, and root.right to stack
        stack.append(root.left)
        stack.append(root.right)

        while stack:
            # NOTE !!! we pop and get p, q seperately
            p, q = stack.pop(), stack.pop()

            if p is None and q is None:
                continue

            if p is None or q is None or p.val != q.val:
                return False

            stack.append(p.left)
            stack.append(q.right)

            stack.append(p.right)
            stack.append(q.left)

        return True

# V0'
# IDEA : Recursive
class Solution(object):
    def isSymmetric(self, root):
        if not root:
            return True
        return self.mirror(root.left, root.right)

    def mirror(self, left, right):
        if not left or not right:
            return left == right
        if left.val != right.val:
            return False
        return self.mirror(left.left, right.right) and self.mirror(left.right, right.left)

# V1 
# https://blog.csdn.net/coder_orz/article/details/51579528
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def isSymmetric(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if not root:
            return True
        return self.mirror(root.left, root.right)

    def mirror(self, left, right):
        if not left or not right:
            return left == right
        if left.val != right.val:
            return False
        return self.mirror(left.left, right.right) and self.mirror(left.right, right.left)

# V1'
# https://blog.csdn.net/coder_orz/article/details/51579528
# DFS 
class Solution(object):
    def isSymmetric(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if not root:
            return True
        stackl, stackr = [root.left], [root.right]
        while len(stackl) > 0 and len(stackr) > 0:
            left = stackl.pop()
            right = stackr.pop()
            if not left and not right:
                continue
            elif not left or not right:
                return False
            if left.val != right.val:
                return False
            stackl.append(left.left)
            stackl.append(left.right)
            stackr.append(right.right)
            stackr.append(right.left)
        return len(stackl) == 0 and len(stackr) == 0

# V1''
# https://blog.csdn.net/coder_orz/article/details/51579528
# BFS 
class Solution(object):
    def isSymmetric(self, root):
        """
        :type root: TreeNode
        :rtype: bool
        """
        if not root:
            return True
        queuel, queuer = [root.left], [root.right]
        while len(queuel) > 0 and len(queuer) > 0:
            left = queuel.pop()
            right = queuer.pop()
            if not left and not right:
                continue
            elif not left or not right:
                return False
            if left.val != right.val:
                return False
            queuel.insert(0, left.left)
            queuel.insert(0, left.right)
            queuer.insert(0, right.right)
            queuer.insert(0, right.left)
        return len(queuel) == 0 and len(queuer) == 0

# V1'''
# https://www.jiuzhang.com/solution/symmetric-tree/#tag-highlight-lang-python
import collections
class Solution(object):
    def _is_symmetric(self, l, r):
        if not l and not r:
            return True
        if l and not r:
            return False
        if r and not l:
            return False
        if l.val != r.val:
            return False
        return self._is_symmetric(l.right, r.left) and \
            self._is_symmetric(l.left, r.right)

    def isSymmetric(self, root):
        if not root:
            return True
        return self._is_symmetric(root.left, root.right)
        
# V2 
# Definition for a  binary tree node
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# Iterative solution
class Solution:
    # @param root, a tree node
    # @return a boolean
    def isSymmetric(self, root):
        if root is None:
            return True
        stack = []
        stack.append(root.left)
        stack.append(root.right)

        while stack:
            p, q = stack.pop(), stack.pop()

            if p is None and q is None:
                continue

            if p is None or q is None or p.val != q.val:
                return False

            stack.append(p.left)
            stack.append(q.right)

            stack.append(p.right)
            stack.append(q.left)

        return True

# V3
# Recursive solution
class Solution2:
    # @param root, a tree node
    # @return a boolean
    def isSymmetric(self, root):
        if root is None:
            return True

        return self.isSymmetricRecu(root.left, root.right)

    def isSymmetricRecu(self, left, right):
        if left is None and right is None:
            return True
        if left is None or right is None or left.val != right.val:
            return False
        return self.isSymmetricRecu(left.left, right.right) and self.isSymmetricRecu(left.right, right.left)

# if __name__ == "__main__":
#     root = TreeNode(1)
#     root.left, root.right = TreeNode(2), TreeNode(2)
#     root.left.left, root.right.right = TreeNode(3), TreeNode(3)
#     root.left.right, root.right.left = TreeNode(4), TreeNode(4)
#     print(Solution().isSymmetric(root))

# V1
# IDEA : Recursive
# https://leetcode.com/problems/symmetric-tree/solution/
# JAVA
# public boolean isSymmetric(TreeNode root) {
#     return isMirror(root, root);
# }
#
# public boolean isMirror(TreeNode t1, TreeNode t2) {
#     if (t1 == null && t2 == null) return true;
#     if (t1 == null || t2 == null) return false;
#     return (t1.val == t2.val)
#         && isMirror(t1.right, t2.left)
#         && isMirror(t1.left, t2.right);
# }

# V1
# IDEA : Iterative
# https://leetcode.com/problems/symmetric-tree/solution/
# JAVA
# public boolean isSymmetric(TreeNode root) {
#     Queue<TreeNode> q = new LinkedList<>();
#     q.add(root);
#     q.add(root);
#     while (!q.isEmpty()) {
#         TreeNode t1 = q.poll();
#         TreeNode t2 = q.poll();
#         if (t1 == null && t2 == null) continue;
#         if (t1 == null || t2 == null) return false;
#         if (t1.val != t2.val) return false;
#         q.add(t1.left);
#         q.add(t2.right);
#         q.add(t1.right);
#         q.add(t2.left);
#     }
#     return true;
# }

# V4 
# Time:  O(n)
# Space: O(h), h is height of binary tree
# Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# Iterative solution
class Solution(object):
    # @param root, a tree node
    # @return a boolean
    def isSymmetric(self, root):
        if root is None:
            return True
        stack = []
        stack.append(root.left)
        stack.append(root.right)

        while stack:
            p, q = stack.pop(), stack.pop()

            if p is None and q is None:
                continue

            if p is None or q is None or p.val != q.val:
                return False

            stack.append(p.left)
            stack.append(q.right)

            stack.append(p.right)
            stack.append(q.left)

        return True

# Recursive solution
class Solution2(object):
    # @param root, a tree node
    # @return a boolean
    def isSymmetric(self, root):
        if root is None:
            return True

        return self.isSymmetricRecu(root.left, root.right)

    def isSymmetricRecu(self, left, right):
        if left is None and right is None:
            return True
        if left is None or right is None or left.val != right.val:
            return False
        return self.isSymmetricRecu(left.left, right.right) and self.isSymmetricRecu(left.right, right.left)