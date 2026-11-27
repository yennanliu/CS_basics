"""

1123. Lowest Common Ancestor of Deepest Leaves
Medium

Given the root of a binary tree, return the lowest common ancestor of its
deepest leaves.

Recall that:

- The node of a binary tree is a leaf if and only if it has no children.
- The depth of the root of the tree is 0. If the depth of a node is d, the
  depth of each of its children is d + 1.
- The lowest common ancestor of a set S of nodes, is the node A with the
  largest depth such that every node in S is in the subtree with root A.

Example 1:

Input: root = [3,5,1,6,2,0,8,null,null,7,4]

Output: [2,7,4]

Explanation: We return the node with value 2, colored in yellow in the
diagram. The nodes coloured in blue are the deepest leaf-nodes of the tree.
Note that nodes 6, 0, and 8 are also leaf nodes, but the depth of them is 2,
but the depth of nodes 7 and 4 is 3.

Example 2:

Input: root = [1]

Output: [1]

Explanation: The root is the deepest node in the tree, and it's the lca of
itself.

Example 3:

Input: root = [0,1,3,null,2]

Output: [2]

Explanation: The deepest leaf node in the tree is 2, the lca of one node is
itself.

Constraints:

The number of nodes in the tree will be in the range [1, 1000].
0 <= Node.val <= 1000
The values of the nodes in the tree are unique.

Note: This question is the same as 865:
https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/

"""

# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right


# V0
# IDEA : POST-ORDER DFS, EACH CALL RETURNS (SUBTREE HEIGHT, LCA OF ITS DEEPEST LEAVES)
#
#   for a node, compare the heights of its two subtrees:
#     - left taller  -> every deepest leaf is on the left, so the answer is the left's LCA
#     - right taller -> same, on the right
#     - equal        -> deepest leaves exist on BOTH sides, so this node is their LCA
#
#   e.g. [3,5,1,6,2,0,8,null,null,7,4]
#     node 2: left (7) h=1, right (4) h=1 -> equal -> LCA = 2, h=2
#     node 5: left (6) h=1, right (2) h=2 -> right taller -> LCA = 2, h=3
#     node 3: left (5) h=3, right (1) h=2 -> left taller  -> LCA = 2
#
# time = O(n), space = O(h)   (h = tree height, the recursion stack)
class Solution(object):
    def lcaDeepestLeaves(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        height, lca = self.height_and_lca(root)
        return lca

    def height_and_lca(self, node):
        """Return (height of node's subtree, LCA of that subtree's deepest leaves)."""
        # an empty subtree has height 0 and no leaves
        if node is None:
            return 0, None

        # post-order: both children must report before this node can decide
        left_height, left_lca = self.height_and_lca(node.left)
        right_height, right_lca = self.height_and_lca(node.right)

        if left_height > right_height:
            return left_height + 1, left_lca
        if right_height > left_height:
            return right_height + 1, right_lca

        # NOTE !!! equal heights -> the deepest leaves are split across both
        #          sides (or this is a leaf), so THIS node is their LCA
        return left_height + 1, node


# V1
# IDEA : TWO PASSES -- FIND THE MAX DEPTH, THEN A CLASSIC LCA SEARCH FOR THE NODES AT IT
#
#   pass 1 records the deepest depth in the tree.
#   pass 2 is LC 236's LCA search, where the "targets" are every node at that depth:
#     a node at max depth returns itself; a node whose left AND right searches both
#     found a target is the meeting point; otherwise pass up whichever side found one.
#
#   NOTE : same O(n), but two traversals and a stored depth -- V0 is the one to say
#          in an interview; this one is the natural first idea if you know LC 236
#
# time = O(n), space = O(h)
class Solution2(object):
    def lcaDeepestLeaves(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        if root is None:
            return None

        max_depth = self.max_depth(root, 0)
        return self.find_lca(root, 0, max_depth)

    def max_depth(self, node, depth):
        """Return the largest depth of any node in node's subtree."""
        if node is None:
            # the parent was the last real node on this path
            return depth - 1
        left_depth = self.max_depth(node.left, depth + 1)
        right_depth = self.max_depth(node.right, depth + 1)
        return max(left_depth, right_depth)

    def find_lca(self, node, depth, max_depth):
        """Return the LCA of the nodes at max_depth inside node's subtree, or None."""
        if node is None:
            return None

        # a deepest node is the LCA of itself (it has no deeper descendants)
        if depth == max_depth:
            return node

        left_found = self.find_lca(node.left, depth + 1, max_depth)
        right_found = self.find_lca(node.right, depth + 1, max_depth)

        # deepest nodes on both sides meet here
        if left_found is not None and right_found is not None:
            return node

        # otherwise every deepest node (if any) is on one side
        if left_found is not None:
            return left_found
        return right_found


# V2
# IDEA : BFS LEVEL ORDER + PARENT POINTERS, THEN CLIMB THE LAST LEVEL UNTIL IT MERGES
#
#   BFS records each node's parent and ends holding the last level -- exactly the
#   deepest leaves. Replace every node in that set by its parent, one level at a
#   time; all of them climb in lockstep (same depth), so the first time the set
#   shrinks to ONE node, that node is their lowest common ancestor.
#
#   e.g. last level {7, 4} -> parents {2} -> one node -> answer 2
#
# time = O(n), space = O(n)   (parent map + queue)
from collections import deque


class Solution3(object):
    def lcaDeepestLeaves(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: Optional[TreeNode]
        """
        if root is None:
            return None

        parent = {root: None}
        queue = deque([root])
        last_level = []

        # level-order traversal; after the loop last_level is the deepest level
        while queue:
            last_level = list(queue)
            level_size = len(queue)
            for _ in range(level_size):
                node = queue.popleft()
                for child in (node.left, node.right):
                    if child is not None:
                        parent[child] = node
                        queue.append(child)

        # climb all deepest nodes together until they collapse into one
        current = set(last_level)
        while len(current) > 1:
            next_level = set()
            for node in current:
                next_level.add(parent[node])
            current = next_level

        # the set holds exactly one node: the LCA
        for node in current:
            return node
