"""

95. Unique Binary Search Trees II
Medium

Given an integer n, return all the structurally unique BST's (binary search trees), which has exactly n nodes of unique values from 1 to n. Return the answer in any order.

 

Example 1:


Input: n = 3
Output: [[1,null,2,null,3],[1,null,3,2],[2,1,3],[3,1,null,null,2],[3,2,null,1]]
Example 2:

Input: n = 1
Output: [[1]]
 

Constraints:

1 <= n <= 8

"""

# V0

# V1
# IDEA : RECURSION
# https://leetcode.com/problems/unique-binary-search-trees-ii/solution/
class Solution:
    def generateTrees(self, n):
        """
        :type n: int
        :rtype: List[TreeNode]
        """
        def generate_trees(start, end):
            if start > end:
                return [None,]
            
            all_trees = []
            for i in range(start, end + 1):  # pick up a root
                # all possible left subtrees if i is choosen to be a root
                left_trees = generate_trees(start, i - 1)
                
                # all possible right subtrees if i is choosen to be a root
                right_trees = generate_trees(i + 1, end)
                
                # connect left and right subtrees to the root i
                for l in left_trees:
                    for r in right_trees:
                        current_tree = TreeNode(i)
                        current_tree.left = l
                        current_tree.right = r
                        all_trees.append(current_tree)
            
            return all_trees
        
        return generate_trees(1, n) if n else []

# V1'
# IDEA : Recursion
# https://leetcode.com/problems/unique-binary-search-trees-ii/discuss/1288276/Python-%3A%3A-Recursion
from copy import deepcopy
class Solution:
    def make_trees(self, nodes):
        if not nodes:
            return [None]
        if len(nodes) == 1:
            return [TreeNode(nodes[0])]
        ans = []
        for i, node in enumerate(nodes):
            root = TreeNode(node)
            for left_child in self.make_trees(nodes[ :i]):
                for right_child in self.make_trees(nodes[i + 1: ]):
                    root.left = left_child
                    root.right = right_child
                    ans.append(deepcopy(root))
        return ans
    
    def generateTrees(self, n: int) -> List[TreeNode]:
        return self.make_trees(list(range(1, n + 1)))

# V2
# https://blog.csdn.net/fuxuemingzhu/article/details/80778651
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, x):
#         self.val = x
#         self.left = None
#         self.right = None
class Solution(object):
    def generateTrees(self, n):
        """
        :type n: int
        :rtype: List[TreeNode]
        """
        if n == 0: return []
        return self.generateTreesDFS(1, n)

    def generateTreesDFS(self, left, right):
        if left > right:
            return [None]
        res = []
        for i in range(left, right + 1):
            left_nodes = self.generateTreesDFS(left, i - 1)
            right_nodes = self.generateTreesDFS(i + 1, right)
            for left_node in left_nodes:
                for right_node in right_nodes:
                    root = TreeNode(i)
                    root.left = left_node
                    root.right = right_node
                    res.append(root)
        return res

# V3 
# Time:  O(4^n / n^(3/2)) ~= Catalan numbers
# Space: O(4^n / n^(3/2)) ~= Catalan numbers
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

    def __repr__(self):
        if self:
            serial = []
            queue = [self]

            while queue:
                cur = queue[0]

                if cur:
                    serial.append(cur.val)
                    queue.append(cur.left)
                    queue.append(cur.right)
                else:
                    serial.append("#")

                queue = queue[1:]

            while serial[-1] == "#":
                serial.pop()

            return repr(serial)

        else:
            return None

class Solution(object):
    # @return a list of tree node
    def generateTrees(self, n):
        return self.generateTreesRecu(1, n)

    def generateTreesRecu(self, low, high):
        result = []
        if low > high:
            result.append(None)
        for i in range(low, high + 1):
            left = self.generateTreesRecu(low, i - 1)
            right = self.generateTreesRecu(i + 1, high)
            for j in left:
                for k in right:
                    cur = TreeNode(i)
                    cur.left = j
                    cur.right = k
                    result.append(cur)
        return result