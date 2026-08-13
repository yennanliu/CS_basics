"""

1430. Check If a String Is a Valid Sequence from Root to Leaves Path in a Binary Tree
Medium

Given a binary tree where each path going from the root to any leaf form a
valid sequence, check if a given string is a valid sequence in such binary tree.

We get the given string from the concatenation of an array of integers arr and
the concatenation of all values of the nodes along a path results in a sequence
in the given binary tree.


Example 1:

Input: root = [0,1,0,0,1,0,null,null,1,0,0], arr = [0,1,0,1]
Output: true
Explanation:
The path 0 -> 1 -> 0 -> 1 is a valid sequence (green color in the figure).
Other valid sequences are:
0 -> 1 -> 1 -> 0
0 -> 0 -> 0

Example 2:

Input: root = [0,1,0,0,1,0,null,null,1,0,0], arr = [0,0,1]
Output: false
Explanation: The path 0 -> 0 -> 1 does not exist, therefore it is not even a sequence.

Example 3:

Input: root = [0,1,0,0,1,0,null,null,1,0,0], arr = [0,1,1]
Output: false
Explanation: The path 0 -> 1 -> 1 is a sequence, but it is not a valid sequence.


Constraints:

1 <= arr.length <= 5000
0 <= arr[i] <= 9
Each node's value is between [0 - 9].

"""

# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right


# V0
# IDEA : DFS
#
#  -> walk down the tree and the array at the same time (index i)
#  -> prune as soon as node.val != arr[i]
#  -> `valid` means we consumed the WHOLE arr AND landed on a LEAF
#     (this is what makes example 3 false: 0 -> 1 -> 1 exists but the
#      last node is not a leaf)
#
# time = O(n)
# space = O(h)
# n = number of nodes, h = tree height
class Solution(object):
    def isValidSequence(self, root, arr):
        def dfs(node, i):
            if not node or i >= len(arr) or node.val != arr[i]:
                return False
            # NOTE !!! must be at the LAST arr element AND at a leaf
            if i == len(arr) - 1:
                return node.left is None and node.right is None
            return dfs(node.left, i + 1) or dfs(node.right, i + 1)

        if not arr:
            return False
        return dfs(root, 0)


# V1
# IDEA : BFS (level by level, level index == arr index)
#
#  -> keep only the nodes whose value matches arr at the current depth
#
# time = O(n)
# space = O(n)
from collections import deque
class Solution(object):
    def isValidSequence(self, root, arr):
        if not root or not arr or root.val != arr[0]:
            return False

        q = deque([root])
        for i in range(1, len(arr)):
            for _ in range(len(q)):
                node = q.popleft()
                for child in (node.left, node.right):
                    if child and child.val == arr[i]:
                        q.append(child)
            if not q:
                return False

        # at least one survivor must be a leaf
        return any(nd.left is None and nd.right is None for nd in q)
