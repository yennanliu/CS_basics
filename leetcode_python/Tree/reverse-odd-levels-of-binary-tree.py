"""

2415. Reverse Odd Levels of Binary Tree
Medium

Given the root of a perfect binary tree, reverse the node values at each odd
level of the tree.

For example, suppose the node values at level 3 are [2,1,3,4,7,11,29,18], then
it should become [18,29,11,7,4,3,1,2].

Return the root of the reversed tree.

A binary tree is perfect if all parent nodes have two children and all leaves
are on the same level.

The level of a node is the number of edges along the path between it and the
root node.

Example 1:

Input: root = [2,3,5,8,13,21,34]

Output: [2,5,3,8,13,21,34]

Explanation:

The tree has only one odd level. The nodes at level 1 are 3, 5 respectively,
which are reversed and become 5, 3.

Example 2:

Input: root = [7,13,11]

Output: [7,11,13]

Explanation:

The nodes at level 1 are 13, 11, which are reversed and become 11, 13.

Example 3:

Input: root = [0,1,2,0,0,0,0,1,1,1,1,2,2,2,2]

Output: [0,2,1,0,0,0,0,2,2,2,2,1,1,1,1]

Explanation:

The odd levels have non-zero values. The nodes at level 1 were 1, 2, and are
2, 1 after the reversal. The nodes at level 3 were 1, 1, 1, 1, 2, 2, 2, 2, and
are 2, 2, 2, 2, 1, 1, 1, 1 after the reversal.

Constraints:

The number of nodes in the tree is in the range [1, 2^14].
0 <= Node.val <= 10^5
root is a perfect binary tree.

"""

# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right

# V0
# IDEA : DFS ON MIRROR PAIRS (left, right, level) -> SWAP VALUES ON ODD LEVELS
#
#   reversing a level of a PERFECT tree = swapping each node with its MIRROR
#   node (the one at the same position counted from the other end).
#   mirror pairs are generated the same way as LC 101 (Symmetric Tree):
#
#     the mirror of (a, b)'s children is (a.left, b.right) and (a.right, b.left)
#
#   so one recursion walks every mirror pair exactly once, and swaps the
#   values when the pair sits on an odd level. Only VALUES move, not nodes,
#   so the tree's shape (and the parents' pointers) never change.
#
#   e.g. [2,3,5,8,13,21,34]
#        pair (3,5) at level 1 -> swap -> (5,3)
#        pairs (8,34), (13,21) at level 2 -> even, keep
#        -> [2,5,3,8,13,21,34]
#
# time = O(n), space = O(h) = O(log n) recursion stack (tree is perfect)
class Solution(object):
    def reverseOddLevels(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        if not root:
            return root

        # the root is level 0 (even) and has no mirror; start from its children
        self.swap_mirror_pairs(root.left, root.right, 1)
        return root

    def swap_mirror_pairs(self, left_node, right_node, level):
        """Visit the mirror pair (left_node, right_node) at `level` and below; returns nothing."""
        # perfect tree: both are None together, at the level under the leaves
        if not left_node or not right_node:
            return

        if level % 2 == 1:
            left_node.val, right_node.val = right_node.val, left_node.val

        # NOTE !!! outer children pair up, and inner children pair up
        #          (left.left <-> right.right, left.right <-> right.left)
        self.swap_mirror_pairs(left_node.left, right_node.right, level + 1)
        self.swap_mirror_pairs(left_node.right, right_node.left, level + 1)


# V1
# IDEA : BFS LEVEL ORDER -> COLLECT EACH ODD LEVEL, REVERSE ITS VALUES WITH TWO POINTERS
#
#   the literal reading of the problem: walk the tree level by level, keep the
#   nodes of the current level in a list, and on an odd level swap the values
#   of nodes[i] and nodes[len - 1 - i] from both ends inward.
#   Easier to come up with than V0, at the cost of holding a whole level
#   (up to n / 2 nodes on the last level) in memory.
#
# time = O(n), space = O(n)
from collections import deque


class Solution2(object):
    def reverseOddLevels(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        if not root:
            return root

        queue = deque([root])
        level = 0

        while queue:
            # every node currently in the queue belongs to `level`
            level_nodes = []
            for _ in range(len(queue)):
                node = queue.popleft()
                level_nodes.append(node)
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)

            if level % 2 == 1:
                # two pointers from both ends: swap values, leave nodes in place
                left = 0
                right = len(level_nodes) - 1
                while left < right:
                    left_node = level_nodes[left]
                    right_node = level_nodes[right]
                    left_node.val, right_node.val = right_node.val, left_node.val
                    left += 1
                    right -= 1

            level += 1

        return root
