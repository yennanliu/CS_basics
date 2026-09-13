"""

897. Increasing Order Search Tree
Easy

Given the root of a binary search tree, rearrange the tree in in-order so that the leftmost node in the tree is now the root of the tree, and every node has no left child and only one right child.

Example 1:

https://assets.leetcode.com/uploads/2020/11/17/ex1.jpg

Input: root = [5,3,6,2,4,null,8,1,null,null,null,7,9]
Output: [1,null,2,null,3,null,4,null,5,null,6,null,7,null,8,null,9]

Example 2:

https://assets.leetcode.com/uploads/2020/11/17/ex2.jpg

Input: root = [5,1,7]
Output: [1,null,5,null,7]

Constraints:

The number of nodes in the given tree will be in the range [1, 100].
0 <= Node.val <= 1000

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82349263
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)
# space = O(n), for the inorder array (plus O(h) recursion stack)
class Solution(object):
    def increasingBST(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        array = self.inOrder(root)
        if not array:
            return None
        newRoot = TreeNode(array[0])
        curr = newRoot
        for i in range(1, len(array)):
            curr.right =TreeNode(array[i])
            curr = curr.right
        return newRoot
        
    def inOrder(self, root):
        if not root:
            return []
        array = []
        array.extend(self.inOrder(root.left))
        array.append(root.val)
        array.extend(self.inOrder(root.right))
        return array

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82349263
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)
# space = O(n), for the inorder array (plus O(h) recursion stack)
class Solution(object):
    def increasingBST(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        res = self.inOrder(root)
        if not res:
            return 
        dummy = TreeNode(-1)
        cur = dummy
        for node in res:
            node.left = node.right = None
            cur.right = node
            cur = cur.right
        return dummy.right
    
    def inOrder(self, root):
        if not root:
            return []
        res = []
        res.extend(self.inOrder(root.left))
        res.append(root)
        res.extend(self.inOrder(root.right))
        return res

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/82349263
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
# time = O(n)
# space = O(h), recursion stack only (no extra array)
class Solution(object):
    def increasingBST(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        dummy = TreeNode(-1)
        self.prev = dummy
        self.inOrder(root)
        return dummy.right
        
    def inOrder(self, root):
        if not root:
            return None
        self.inOrder(root.left)
        root.left = None
        self.prev.right = root
        self.prev = self.prev.right
        self.inOrder(root.right)

# V2
# time = O(n)
# space = O(h)
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    def increasingBST(self, root):
        """
        :type root: TreeNode
        :rtype: TreeNode
        """
        def increasingBSTHelper(root, tail):
            if not root:
                return tail
            result = increasingBSTHelper(root.left, root)
            root.left = None
            root.right = increasingBSTHelper(root.right, tail)
            return result
        return increasingBSTHelper(root, None)