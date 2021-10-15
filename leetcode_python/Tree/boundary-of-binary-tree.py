"""

545. Boundary of Binary Tree (Medium)

https://xiaoguan.gitbooks.io/leetcode/content/LeetCode/545-boundary-of-binary-tree-medium.html

Given a binary tree, return the values of its boundary in anti-clockwise direction starting from root. Boundary includes left boundary, leaves, and right boundary in order without duplicate nodes.

Left boundary is defined as the path from root to the left-most node. Right boundary is defined as the path from root to the right-most node. If the root doesn't have left subtree or right subtree, then the root itself is left boundary or right boundary. Note this definition only applies to the input binary tree, and not applies to any subtrees.

The left-most node is defined as a leaf node you could reach when you always firstly travel to the left subtree if exists. If not, travel to the right subtree. Repeat until you reach a leaf node.

The right-most node is also defined by the same way with left and right exchanged.

Example 1

Input:
  1
   \
    2
   / \
  3   4

Ouput:
[1, 3, 4, 2]

Explanation:
The root doesn't have left subtree, so the root itself is left boundary.
The leaves are node 3 and 4.
The right boundary are node 1,2,4. Note the anti-clockwise direction means you should output reversed right boundary.
So order them in anti-clockwise without duplicates and we have [1,3,4,2].
Example 2

Input:
    ____1_____
   /          \
  2            3
 / \          / 
4   5        6   
   / \      / \
  7   8    9  10  

Ouput:
[1,2,4,7,8,9,10,6,3]

Explanation:
The left boundary are node 1,2,4. (4 is the left-most node according to definition)
The leaves are node 4,7,8,9,10.
The right boundary are node 1,3,6,10. (10 is the right-most node).
So order them in anti-clockwise without duplicate nodes we have [1,2,4,7,8,9,10,6,3].
Summary
Given a binary tree, return the values of its boundary in anti-clockwise direction starting from root. Boundary includes left boundary, leaves, and right boundary in order without duplicate nodes.

Left boundary is defined as the path from root to the left-most node. Right boundary is defined as the path from root to the right-most node. If the root doesn't have left subtree or right subtree, then the root itself is left boundary or right boundary. Note this definition only applies to the input binary tree, and not applies to any subtrees.

The left-most node is defined as a leaf node you could reach when you always firstly travel to the left subtree if exists. If not, travel to the right subtree. Repeat until you reach a leaf node.

The right-most node is also defined by the same way with left and right exchanged.

"""

# V0
#https://xiaoguan.gitbooks.io/leetcode/content/LeetCode/545-boundary-of-binary-tree-medium.html
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        def leftBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            nodes.append(root.val)
            if not root.left:
                leftBoundary(root.right, nodes)
            else:
                leftBoundary(root.left, nodes)
 
        def rightBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            if not root.right:
                rightBoundary(root.left, nodes)
            else:
                rightBoundary(root.right, nodes)
            nodes.append(root.val)
 
        def leaves(root, nodes):
            if not root:
                return
            if not root.left and not root.right:
                nodes.append(root.val)
                return
            leaves(root.left, nodes)
            leaves(root.right, nodes)
 
        if not root:
            return []
 
        nodes = [root.val]
        leftBoundary(root.left, nodes)
        leaves(root.left, nodes)
        leaves(root.right, nodes)
        rightBoundary(root.right, nodes)
        return nodes

# V1
# https://www.cnblogs.com/lightwindy/p/9583723.html
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        def leftBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            nodes.append(root.val)
            if not root.left:
                leftBoundary(root.right, nodes)
            else:
                leftBoundary(root.left, nodes)
 
        def rightBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            if not root.right:
                rightBoundary(root.left, nodes)
            else:
                rightBoundary(root.right, nodes)
            nodes.append(root.val)
 
        def leaves(root, nodes):
            if not root:
                return
            if not root.left and not root.right:
                nodes.append(root.val)
                return
            leaves(root.left, nodes)
            leaves(root.right, nodes)
 
        if not root:
            return []
 
        nodes = [root.val]
        leftBoundary(root.left, nodes)
        leaves(root.left, nodes)
        leaves(root.right, nodes)
        rightBoundary(root.right, nodes)
        return nodes

### Test case : dev

# V1'
# https://www.jiuzhang.com/solution/boundary-of-binary-tree/#tag-highlight-lang-python
class Solution:
    """
    @param root: a TreeNode
    @return: a list of integer
    """
    def boundaryOfBinaryTree(self, root):
        self.ans = []
        if root == None:
            return self.ans
        self.ans.append(root.val)
        if root.left == None and root.right == None:
            return self.ans
        self.dfsLeft(root.left)
        self.dfsLeaf(root)
        self.dfsRight(root.right)
        return self.ans
    
    def dfsLeft(self, rt):
        if rt == None or (rt.left == None and rt.right == None):
            return
        self.ans.append(rt.val)
        if rt.left != None:
            self.dfsLeft(rt.left)
        else:
            self.dfsLeft(rt.right)
        
    def dfsLeaf(self, rt):
        if rt == None:
            return 
        if rt.left == None and rt.right == None:
            self.ans.append(rt.val)
            return
        self.dfsLeaf(rt.left)
        self.dfsLeaf(rt.right)
    
    def dfsRight(self, rt):
        if rt == None or (rt.left == None and rt.right == None):
            return
        if rt.right != None:
            self.dfsRight(rt.right)
        else:
            self.dfsRight(rt.left)
        self.ans.append(rt.val)

# V1''
# https://www.jiuzhang.com/solution/boundary-of-binary-tree/#tag-highlight-lang-python
class Solution:
    """
    @param root: a TreeNode
    @return: return a list of integer
    """

    def boundaryOfBinaryTree(self, root):
        self.ans = []
        if(root == None):
            return self.ans
        self.ans.append(root.val)
        self.dfs(root.left, True, False)
        self.dfs(root.right, False, True)
        return self.ans

    def dfs(self, root, lft, rgt):
        if(root == None):
            return
        if(root.left == None and root.right == None):
            self.ans.append(root.val)
            return
        if(lft):
            self.ans.append(root.val)
        self.dfs(root.left, lft, rgt and root.right == None)
        self.dfs(root.right, lft and root.left == None, rgt)
        if(rgt):
            self.ans.append(root.val)
            
# V1'''
# http://bookshadow.com/weblog/2017/03/26/leetcode-boundary-of-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        if not root: return []
        if not root.left and not root.right: return [root.val]

        leaves = []
        def traverse(root):
            if not root.left and not root.right:
                leaves.append(root)
            if root.left:
                traverse(root.left)
            if root.right:
                traverse(root.right)
        traverse(root)

        left = []
        node = root
        while node and node != leaves[0]:
            left.append(node)
            if node.left: node = node.left
            else: node = node.right

        right = []
        node = root
        while node and node != leaves[-1]:
            right.append(node)
            if node.right: node = node.right
            else: node = node.left

        left = left[1:] if root.left else []
        right = right[1:] if root.right else []
        return [node.val for node in [root] + left + leaves + right[::-1]]

# V1''''
# https://blog.csdn.net/danspace1/article/details/86659807
class Solution:
    def boundaryOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        def leftBoundary(node):
            if not node or (node.left is None and node.right is None):
                return 
            b.append(node.val)
            if node.left:
                leftBoundary(node.left)
            else:
                leftBoundary(node.right)
            
        def leaves(node):
            if not node:
                return
            leaves(node.left)
            if node != root and node.left is None and node.right is None:
                b.append(node.val)
            
            leaves(node.right)
            
        def rightBoundary(node):
            if not node or (node.left is None and node.right is None):
                return            
            if node.right:
                rightBoundary(node.right)
            else:
                rightBoundary(node.left)
            b.append(node.val)
                 
        # base case
        if not root: return []
        b = [root.val]
        leftBoundary(root.left)
        leaves(root)
        rightBoundary(root.right)
        return b

# V2 
# Time:  O(n)
# Space: O(h)
class Solution(object):
    def boundaryOfBinaryTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[int]
        """
        def leftBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            nodes.append(root.val)
            if not root.left:
                leftBoundary(root.right, nodes)
            else:
                leftBoundary(root.left, nodes)

        def rightBoundary(root, nodes):
            if not root or (not root.left and not root.right):
                return
            if not root.right:
                rightBoundary(root.left, nodes)
            else:
                rightBoundary(root.right, nodes)
            nodes.append(root.val)

        def leaves(root, nodes):
            if not root:
                return
            if not root.left and not root.right:
                nodes.append(root.val)
                return
            leaves(root.left, nodes)
            leaves(root.right, nodes)

        if not root:
            return []

        nodes = [root.val]
        leftBoundary(root.left, nodes)
        leaves(root.left, nodes)
        leaves(root.right, nodes)
        rightBoundary(root.right, nodes)
        return nodes