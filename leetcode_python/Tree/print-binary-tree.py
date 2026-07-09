"""


655. Print Binary Tree
Medium
Topics
premium lock icon
Companies
Given the root of a binary tree, construct a 0-indexed m x n string matrix res that represents a formatted layout of the tree. The formatted layout matrix should be constructed using the following rules:

The height of the tree is height and the number of rows m should be equal to height + 1.
The number of columns n should be equal to 2height+1 - 1.
Place the root node in the middle of the top row (more formally, at location res[0][(n-1)/2]).
For each node that has been placed in the matrix at position res[r][c], place its left child at res[r+1][c-2height-r-1] and its right child at res[r+1][c+2height-r-1].
Continue this process until all the nodes in the tree have been placed.
Any empty cells should contain the empty string "".
Return the constructed matrix res.

 

Example 1:


Input: root = [1,2]
Output: 
[["","1",""],
 ["2","",""]]
Example 2:


Input: root = [1,2,3,null,4]
Output: 
[["","","","1","","",""],
 ["","2","","","","3",""],
 ["","","4","","","",""]]
 

Constraints:

The number of nodes in the tree is in the range [1, 210].
-99 <= Node.val <= 99
The depth of the tree will be in the range [1, 10].
 



"""

# V0
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def printTree(self, root):
        pass


# V0-1
# IDEA: DFS + matrix (gpt)
class Solution(object):
    def printTree(self, root):
        if not root:
            return []

        # height of tree (0-based)
        self.height = self.get_tree_height(root)

        rows = self.height + 1
        cols = 2 ** (self.height + 1) - 1

        self.matrix = [[""] * cols for _ in range(rows)]

        # root starts at the middle
        self.helper(root, 0, (cols - 1) // 2)

        return self.matrix

    def get_tree_height(self, root):
        if not root:
            return -1          # so leaf has height 0

        return 1 + max(
            self.get_tree_height(root.left),
            self.get_tree_height(root.right)
        )

    def helper(self, root, row, col):
        if not root:
            return

        self.matrix[row][col] = str(root.val)

        if row == self.height:
            return

        offset = 2 ** (self.height - row - 1)

        self.helper(root.left, row + 1, col - offset)
        self.helper(root.right, row + 1, col + offset)



# V0-2
# IDEA: DFS + matrix (gemini)
class Solution(object):
    def printTree(self, root):
        """
        :type root: TreeNode
        :type rtype: List[List[str]]
        """
        if not root:
            return []

        # Get tree height (defined by the problem as 0-indexed height)
        # We subtract 1 because our helper function returns 1-indexed total depth.
        self.height = self.get_tree_height(root) - 1

        # Calculate layout boundaries based on problem rules
        l = self.height + 1
        w = 2**(self.height + 1) - 1

        # FIX: Correct 2D matrix initialization syntax
        self.matrix = [[""] * w for _ in range(l)]

        # FIX: Place the root at row 0, middle column (w - 1) // 2
        root_x = (w - 1) // 2
        
        # Start recursion (no visited set needed for trees)
        self.helper(root, root_x, 0)

        return self.matrix

    def get_tree_height(self, root):
        if not root:
            return 0
        
        # FIX: Call the correct method name recursively
        _left = self.get_tree_height(root.left)
        _right = self.get_tree_height(root.right)

        return max(_left, _right) + 1

    def helper(self, root, x, y):
        # Base case: if node is empty, stop processing
        if not root:
            return

        # FIX: Element values must be stored as strings
        self.matrix[y][x] = str(root.val)

        # Calculate exact child offsets following the LeetCode instructions
        offset = 2**(self.height - y - 1)

        x_sub_left = x - offset
        y_sub_left = y + 1

        x_sub_right = x + offset
        y_sub_right = y + 1

        # Traverse down to children
        self.helper(root.left, x_sub_left, y_sub_left)
        self.helper(root.right, x_sub_right, y_sub_right)


# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79439026
# http://bookshadow.com/weblog/2017/08/06/leetcode-print-binary-tree/
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def printTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[str]]
        """
        self.height = self.findDepth(root)
        self.width = (1 << self.height) - 1
        self.dmap = [[""] * self.width for x in range(self.height)]
        self.traverse(root, 1, self.width >> 1)
        return self.dmap
    def findDepth(self, root):
        if not root: return 0
        return 1 + max(self.findDepth(root.left), self.findDepth(root.right))
    def traverse(self, root, depth, offset):
        if not root: return
        self.dmap[depth - 1][offset] = str(root.val)
        gap = 1 + self.width >> depth + 1
        self.traverse(root.left, depth + 1, offset - gap)
        self.traverse(root.right, depth + 1, offset + gap)

# V1'
# https://www.jiuzhang.com/solution/print-binary-tree/#tag-highlight-lang-python
class Solution(object):
    def printTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[str]]
        """
        def get_height(node):
            if not node:
                return 0
            return 1 + max(get_height(node.left), get_height(node.right))

        def traverse(node, level, pos):
            if not node:
                return
            left_padding, spacing = 2 ** (rows - level - 1) - 1, 2 ** (rows - level) - 1
            index = left_padding + pos * (spacing + 1)
            res[level][index] = str(node.val)
            #traverse(node.left, level + 1, pos << 1)
            #traverse(node.right, level + 1, (pos << 1) + 1)
            traverse(node.left, level + 1, pos*2)        # N << 1 == N*2 
            traverse(node.right, level + 1, (pos*2) + 1)

        rows = get_height(root)
        cols = 2 ** rows - 1
        res = [['' for _ in range(cols)] for _ in range(rows)]
        traverse(root, 0, 0)
        return res

# V2 
# Time:  O(h * 2^h)
# Space: O(h * 2^h)
class Solution(object):
    def printTree(self, root):
        """
        :type root: TreeNode
        :rtype: List[List[str]]
        """
        def getWidth(root):
            if not root:
                return 0
            return 2 * max(getWidth(root.left), getWidth(root.right)) + 1

        def getHeight(root):
            if not root:
                return 0
            return max(getHeight(root.left), getHeight(root.right)) + 1

        def preorderTraversal(root, level, left, right, result):
            if not root:
                return
            mid = left + (right-left)/2
            result[level][mid] = str(root.val)
            preorderTraversal(root.left, level+1, left, mid-1, result)
            preorderTraversal(root.right, level+1, mid+1, right, result)

        h, w = getHeight(root), getWidth(root)
        result = [[""] * w for _ in xrange(h)]
        preorderTraversal(root, 0, 0, w-1, result)
        return result
