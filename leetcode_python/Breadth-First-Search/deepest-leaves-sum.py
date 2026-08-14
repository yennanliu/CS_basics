"""

1302. Deepest Leaves Sum
Medium

Given the root of a binary tree, return the sum of values of its deepest leaves.


Example 1:

Input: root = [1,2,3,4,5,null,6,7,null,null,null,null,8]
Output: 15

Example 2:

Input: root = [6,7,8,2,7,1,3,9,null,1,4,null,null,null,5]
Output: 19


Constraints:

The number of nodes in the tree is in the range [1, 10^4].
1 <= Node.val <= 100

"""

# V0
# IDEA : BFS LEVEL ORDER (keep only the last level's sum)
#
#   traverse level by level; for each level compute its total.
#   when the queue finally empties, the last total we computed belongs to the
#   deepest level -> that is the answer, no depth bookkeeping needed.
#
#   NOTE : `for _ in range(len(q))` snapshots the level size BEFORE we start
#          pushing children, which is what keeps the levels separated.
#
# time = O(n), space = O(w)   w = max level width
from collections import deque
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def deepestLeavesSum(self, root):
        if not root:
            return 0
        q = deque([root])
        res = 0
        while q:
            res = 0
            for _ in range(len(q)):
                node = q.popleft()
                res += node.val
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)
        return res
