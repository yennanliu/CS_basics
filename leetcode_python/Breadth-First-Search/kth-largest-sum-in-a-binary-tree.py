"""

2583. Kth Largest Sum in a Binary Tree
Medium

You are given the root of a binary tree and a positive integer k.

The level sum in the tree is the sum of the values of the nodes that are on the same level.

Return the kth largest level sum in the tree (not necessarily distinct).
If there are fewer than k levels in the tree, return -1.

Note that two nodes are on the same level if they have the same distance from the root.


Example 1:

Input: root = [5,8,9,2,1,3,7,4,6], k = 2
Output: 13
Explanation: The level sums are the following:
- Level 1: 5.
- Level 2: 8 + 9 = 17.
- Level 3: 2 + 1 + 3 + 7 = 13.
- Level 4: 4 + 6 = 10.
The 2nd largest level sum is 13.

Example 2:

Input: root = [1,2,null,3], k = 1
Output: 3
Explanation: The largest level sum is 3.


Constraints:

The number of nodes in the tree is n.
2 <= n <= 10^5
1 <= Node.val <= 10^6
1 <= k <= n

"""

# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right

# V0
# IDEA : BFS (level order traversal) + SORT
#
#   collect one sum per level with a standard level-order BFS (the classic
#   `for _ in range(len(q))` trick freezes the current level's width so we know
#   exactly which nodes belong to it), then sort the sums descending and take
#   the k-th.
#
#   NOTE : k is bounded by n (the NODE count), not by the level count, so the
#          tree can easily have fewer than k levels — that case must return -1,
#          NOT crash on an index error.
#
#   NOTE : n can reach 10^5 which means the tree may be a 10^5-deep chain, so
#          BFS (iterative) is used rather than a recursive DFS.
#
#   NOTE : sums can reach 10^5 * 10^6 = 10^11 — fine for python ints, but this
#          is the "long" that other languages need here.
#
# time = O(n * log n), space = O(n)
from collections import deque
class Solution(object):
    def kthLargestLevelSum(self, root, k):
        sums = []
        q = deque([root])
        while q:
            total = 0
            for _ in range(len(q)):
                node = q.popleft()
                total += node.val
                if node.left:
                    q.append(node.left)
                if node.right:
                    q.append(node.right)
            sums.append(total)
        if len(sums) < k:
            return -1
        sums.sort(reverse=True)
        return sums[k - 1]
