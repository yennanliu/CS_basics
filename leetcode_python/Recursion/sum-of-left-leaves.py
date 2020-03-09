"""
LeetCode 404. Sum of Left Leaves

Find the sum of all left leaves in a given binary tree.

Example:

    3
   / \
  9  20
    /  \
   15   7

There are two left leaves in the binary tree, with values 9 and 15 respectively. Return 24.

"""
# V0 
class Solution:
    def sumOfLeftLeaves(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root: return 0
        self.sum = 0
        self.inOrder(root)
        return self.sum

    def inOrder(self, root):
        if not root: return 
        if root.left:
            self.inOrder(root.left)
            ### BE AWARE OF BELOW CONDITION :
            # -> ONLY CONSIDER THE LEFT SUB-TREE (AT THE END) 
            if not root.left.left and not root.left.right:
                self.sum += root.left.val
        if root.right:
            self.inOrder(root.right)

# V1
# http://bookshadow.com/weblog/2016/09/25/leetcode-sum-of-left-leaves/ 
class Solution(object):
    def sumOfLeftLeaves(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        ans = 0
        if root: 
            l, r = root.left, root.right
            if l and (l.left or l.right) is None:
                ans += l.val
            ans += self.sumOfLeftLeaves(l) + self.sumOfLeftLeaves(r)
        return ans

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/54178595
class Solution:
    def sumOfLeftLeaves(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root: return 0
        self.sum = 0
        self.inOrder(root)
        return self.sum

    def inOrder(self, root):
        if not root: return 
        if root.left:
            self.inOrder(root.left)
            if not root.left.left and not root.left.right:
                self.sum += root.left.val
        if root.right:
            self.inOrder(root.right)

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/54178595
class Solution:
    def sumOfLeftLeaves(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if not root: return 0
        stack = []
        stack.append(root)
        leftsum = 0
        while stack:
            node = stack.pop()
            if not node: continue
            if node.left:
                if not node.left.left and not node.left.right:
                    leftsum += node.left.val
                stack.append(node.left)
            if node.right:
                stack.append(node.right)
        return leftsum

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def sumOfLeftLeaves(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        def sumOfLeftLeavesHelper(root, is_left):
            if not root:
                return 0
            if not root.left and not root.right:
                return root.val if is_left else 0
            return sumOfLeftLeavesHelper(root.left, True) + \
                   sumOfLeftLeavesHelper(root.right, False)

        return sumOfLeftLeavesHelper(root, False)

# V2' 
class Solution(object):
    def sumOfLeftLeavesHelper(self, root, is_left):
        if not root:
            return 0
        if not root.left and not root.right:
            return root.val if is_left else 0
        return self.sumOfLeftLeavesHelper(root.left, True) + \
           self.sumOfLeftLeavesHelper(root.right, False)

    def sumOfLeftLeaves(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        return self.sumOfLeftLeavesHelper(root, False)
