"""

2096. Step-By-Step Directions From a Binary Tree Node to Another
Medium

You are given the root of a binary tree with n nodes. Each node is uniquely assigned a value from 1 to n. You are also given an integer startValue representing the value of the start node s, and a different integer destValue representing the value of the destination node t.

Find the shortest path starting from node s and ending at node t. Generate step-by-step directions of such path as a string consisting of only the uppercase letters 'L', 'R', and 'U'. Each letter indicates a specific direction:

'L' means to go from a node to its left child node.
'R' means to go from a node to its right child node.
'U' means to go from a node to its parent node.

Return the step-by-step directions of the shortest path from node s to node t.


Example 1:

Input: root = [5,1,2,3,null,6,4], startValue = 3, destValue = 6
Output: "UURL"
Explanation: The shortest path is: 3 -> 1 -> 5 -> 2 -> 6.

Example 2:

Input: root = [2,1], startValue = 2, destValue = 1
Output: "L"
Explanation: The shortest path is: 2 -> 1.


Constraints:

The number of nodes in the tree is n.
2 <= n <= 10^5
1 <= Node.val <= n
All the values in the tree are unique.
1 <= startValue, destValue <= n
startValue != destValue

"""

# V0
# IDEA : ROOT -> NODE PATHS, THEN DROP THE SHARED PREFIX (LCA)
#
#   record the L/R turns taken from the root down to s, and the same down to
#   t. the two strings share a prefix — that prefix is the path to their
#   lowest common ancestor.
#
#   after stripping the common prefix :
#       every remaining step of the s-path is walked BACKWARDS  -> 'U'
#       every remaining step of the t-path is walked FORWARDS   -> kept as is
#
#   so the answer is  'U' * len(rest_of_s)  +  rest_of_t.
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def getDirections(self, root, startValue, destValue):

        def find(node, target, path):
            if not node:
                return False
            if node.val == target:
                return True
            path.append('L')
            if find(node.left, target, path):
                return True
            path[-1] = 'R'
            if find(node.right, target, path):
                return True
            path.pop()
            return False

        src, dst = [], []
        find(root, startValue, src)
        find(root, destValue, dst)

        i = 0
        while i < len(src) and i < len(dst) and src[i] == dst[i]:
            i += 1
        return 'U' * (len(src) - i) + ''.join(dst[i:])
