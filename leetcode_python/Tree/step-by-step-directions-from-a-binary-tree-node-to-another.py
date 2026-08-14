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
# IDEA : ROOT-TO-NODE PATHS + STRIP THE COMMON PREFIX (the LCA)
#
#   find the 'L'/'R' path from the root down to s and down to t. their common
#   prefix is exactly the path to their lowest common ancestor.
#
#   after dropping that prefix :
#     - every remaining step of the s-path becomes a 'U' (walk up to the LCA)
#     - the remaining t-path is appended unchanged (walk back down)
#
#   NOTE : the DFS builds the path with an explicit list and pops on the way
#          out, so it stays O(n) rather than concatenating strings.
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

        p_start, p_dest = [], []
        find(root, startValue, p_start)
        find(root, destValue, p_dest)

        # drop the shared prefix (the path down to the LCA)
        i = 0
        while i < len(p_start) and i < len(p_dest) and p_start[i] == p_dest[i]:
            i += 1

        return 'U' * (len(p_start) - i) + ''.join(p_dest[i:])
