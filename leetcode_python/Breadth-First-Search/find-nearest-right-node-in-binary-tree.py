"""

1602. Find Nearest Right Node in Binary Tree
Medium

Given the root of a binary tree and a node u in the tree, return the nearest node on the same level that is to the right of u, or return null if u is the rightmost node in its level.


Example 1:

Input: root = [1,2,3,null,4,5,6], u = 4
Output: 5
Explanation: The nearest node on the same level to the right of node 4 is node 5.

Example 2:

Input: root = [3,null,4,2], u = 2
Output: null
Explanation: There are no nodes to the right of 2.


Constraints:

The number of nodes in the tree is in the range [1, 10^5].
1 <= Node.val <= 10^5
All values in the tree are distinct.
u is a node in the binary tree rooted at root.

"""

# V0
# IDEA : LEVEL-ORDER BFS (the "next node right" is the next queue entry)
#
#   process the tree level by level. Within a level the queue holds the
#   nodes left-to-right, so once we dequeue u the answer is simply the
#   node dequeued right after it -- unless u was the LAST node of that
#   level, in which case there is nothing to its right -> None.
#
#   NOTE : compare by identity (`is`) rather than by val; values happen to
#          be distinct here, but node identity is what the problem means.
#
# time = O(n), space = O(width of the tree)
from collections import deque
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right

class Solution(object):
    def findNearestRightNode(self, root, u):
        if not root:
            return None
        q = deque([root])
        while q:
            size = len(q)
            for i in range(size):
                node = q.popleft()
                if node is u:
                    # last node of this level -> nothing on its right
                    return q[0] if i < size - 1 else None
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)
        return None
