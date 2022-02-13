"""

111. Minimum Depth of Binary Tree
Easy

Given a binary tree, find its minimum depth.

The minimum depth is the number of nodes along the shortest path from the root node down to the nearest leaf node.

Note: A leaf is a node with no children.

 

Example 1:


Input: root = [3,9,20,null,null,15,7]
Output: 2
Example 2:

Input: root = [2,null,3,null,4,null,5,null,6]
Output: 5
 

Constraints:

The number of nodes in the tree is in the range [0, 105].
-1000 <= Node.val <= 1000

"""

# V0
# IDEA : BFS
# compare with LC 104 : Maximum Depth of Binary Tree
class Solution(object):
    def minDepth(self, root):
        # edge case
        if not root:
            return 0
        if root and not root.left and not root.right:
            return 1
        layer = 1
        q = [[layer, root]]
        res = []
        while q:
            for i in range(len(q)):
                layer, tmp = q.pop(0)
                """
                NOTE !!! : via below condition, we get "layer" of " A leaf is a node with no children."
                """
                if tmp and not tmp.left and not tmp.right:
                    res.append(layer)
                if tmp.left:
                    q.append([layer+1, tmp.left])
                if tmp.right:
                    q.append([layer+1, tmp.right])
        # get min
        #print ("res = " + str(res))
        return min(res)

# V0'
# IDEA : DFS
# compare with LC 104 : Maximum Depth of Binary Tree
class Solution(object):
    def minDepth(self, root):
        if not root:
            return 0
        ### NOTE here : we need min depth, so if not root.left, then we need to return directly
        if not root.left:
            return 1 + self.minDepth(root.right)
        ### NOTE here : we need min depth, so if not root.right, then we need to return directly
        elif not root.right:
            return 1 + self.minDepth(root.left)
        else:
            return 1 + min(self.minDepth(root.left), self.minDepth(root.right))

# V0''
# IDEA : BFS
class Solution(object):
    def minDepth(self, root):
        if not root:
            return 0
        q = [[root, 1]]
        while q:
            for i in range(len(q)):
                cur, step = q.pop(0)
                ### NOTE this
                if not cur.left and not cur.right:
                    return step
                if cur.left:
                    q.append([cur.left, step + 1])
                if cur.right:
                    q.append([cur.right, step + 1])

# V1
# https://blog.csdn.net/coder_orz/article/details/51337522
# IDEA : DFS
class Solution(object):
    def minDepth(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if root == None:
            return 0

        if not root.left:
            return 1 + self.minDepth(root.right)
        elif not root.right:
            return 1 + self.minDepth(root.left)
        else:
            return 1 + min(self.minDepth(root.left), self.minDepth(root.right))

# V1
# http://bookshadow.com/weblog/2015/11/28/leetcode-minimum-depth-binary-tree/
# IDEA : DFS
class Solution(object):
    def minDepth(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        if root is None:
            return 0
        left = self.minDepth(root.left)
        right = self.minDepth(root.right)
        if left and right:
            return min(left, right) + 1
        return max(left, right) + 1

# V1'
# http://bookshadow.com/weblog/2015/11/28/leetcode-minimum-depth-binary-tree/
# IDEA : BFS 
class Solution(object):
    def minDepth(self, root):
        if not root:
            return 0
        q = [[root, 1]]
        while q:
            for i in range(len(q)):
                cur, step = q.pop(0)
                if not cur.left and not cur.right:
                    return step
                if cur.left:
                    q.append([cur.left, step + 1])
                if cur.right:
                    q.append([cur.right, step + 1])
              
# V1''
# https://www.jiuzhang.com/solution/minimum-depth-of-binary-tree/#tag-highlight-lang-python
"""
Definition of TreeNode:
class TreeNode:
    def __init__(self, val):
        this.val = val
        this.left, this.right = None, None
"""
class Solution:
    """
    @param root: The root of binary tree.
    @return: An integer
    """ 
    def minDepth(self, root):
        # write your code here
        return self.find(root)

    def find(self, node):
        if node is None:
            return 0
        left, right = 0, 0
        if node.left != None:
            left = self.find(node.left)
        else:
            return self.find(node.right) + 1

        if node.right != None:
            right = self.find(node.right)
        else:
            return left + 1
        return min(left,right) + 1

# V2 
# Time:  O(n)
# Space: O(h), h is height of binary tree
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

class Solution(object):
    # @param root, a tree node
    # @return an integer
    def minDepth(self, root):
        if root is None:
            return 0

        if root.left and root.right:
            return min(self.minDepth(root.left), self.minDepth(root.right)) + 1
        else:
            return max(self.minDepth(root.left), self.minDepth(root.right)) + 1