"""

3157. Find the Level of Tree with Minimum Sum
Medium
🔒 (premium)

Given the root of a binary tree root where each node has a value, return the level of the tree that has the minimum sum of values among all the levels (in case of a tie, return the lowest level).

Note that the root of the tree is at level 1 and the level of any other node is its distance from the root + 1.


Example 1:

Input: root = [50,6,2,30,80,7]
Output: 2
Explanation:
The sum of values at level 1 is 50, at level 2 is 6 + 2 = 8, and at level 3 is 30 + 80 + 7 = 117.
Level 2 has the minimum sum, so the answer is 2.

Example 2:

Input: root = [36,17,10,null,null,24]
Output: 3
Explanation:
The sum of values at level 1 is 36, at level 2 is 17 + 10 = 27, and at level 3 is 24.
Level 3 has the minimum sum, so the answer is 3.


Constraints:

The number of nodes in the tree is in the range [1, 10^5].
1 <= Node.val <= 10^9

"""

# V0
# IDEA : LEVEL-ORDER BFS, TRACKING THE BEST SUM AND ITS LEVEL
#
#   a queue drained one whole level at a time gives each level's sum
#   directly. keeping a strict "<" comparison means the FIRST (lowest) level
#   wins a tie, which is what the spec asks for.
#
#   the walk is iterative rather than recursive — 10^5 nodes could form a
#   path and blow the recursion limit.
#
# time = O(n), space = O(width)
from collections import deque


# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def minimumLevel(self, root):
        if not root:
            return 0
        q = deque([root])
        level = 0
        best_sum = None
        best_level = 0

        while q:
            level += 1
            total = 0
            for _ in range(len(q)):
                node = q.popleft()
                total += node.val
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)
            if best_sum is None or total < best_sum:
                best_sum = total
                best_level = level
        return best_level
