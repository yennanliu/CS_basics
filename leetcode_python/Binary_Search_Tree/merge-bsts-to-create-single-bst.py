"""

1932. Merge BSTs to Create Single BST
Hard

You are given n BST (binary search tree) root nodes for n separate BSTs stored in an array trees (0-indexed). Each BST in trees has at most 3 nodes, and no two roots have the same value. In one operation, you can:

Select two distinct indices i and j such that the value stored at one of the leaves of trees[i] is equal to the root value of trees[j].
Replace the leaf node in trees[i] with trees[j].
Remove trees[j] from trees.

Return the root of the resulting BST if it is possible to form a valid BST after performing n - 1 operations, or null if it is impossible to create a valid BST.

A BST (binary search tree) is a binary tree where each node satisfies the following property:

Every node in the node's left subtree has a value strictly less than the node's value.
Every node in the node's right subtree has a value strictly greater than the node's value.

A leaf is a node that has no children.


Example 1:

Input: trees = [[2,1],[3,2,5],[5,4]]
Output: [3,2,5,1,null,4]
Explanation:
In the first operation, pick i=1 and j=0, and merge trees[0] into trees[1].
Delete trees[0], so trees = [[3,2,5,1],[5,4]].
In the second operation, pick i=0 and j=1, and merge trees[1] into trees[0].
Delete trees[1], so trees = [[3,2,5,1,null,4]].
The resulting tree, shown above, is a valid BST, so return its root.

Example 2:

Input: trees = [[5,3,8],[3,2,6]]
Output: []
Explanation:
Pick i=0 and j=1 and merge trees[1] into trees[0].
Delete trees[1], so trees = [[5,3,8,2,6]].
The resulting tree is shown above. This is the only valid operation that can be performed, but the resulting tree is not a valid BST, so return null.

Example 3:

Input: trees = [[5,4],[3]]
Output: []
Explanation: It is impossible to perform any operations.


Constraints:

n == trees.length
1 <= n <= 5 * 10^4
The number of nodes in each tree is in the range [1, 3].
Each node in the input may have children but no grandchildren.
No two roots of trees have the same value.
All the trees in the input are valid BSTs.
1 <= TreeNode.val <= 5 * 10^4

"""

# V0
# IDEA : FIND THE UNIQUE OVERALL ROOT, THEN SPLICE + VALIDATE IN ONE DFS
#
#   count how many times every value appears over ALL nodes (roots and leaves).
#   the final root must be a tree root whose value is used exactly once - if it
#   also appeared as some leaf, that tree would have to swallow it, so it could
#   not be the top. there is at most one usable candidate.
#
#   then walk down from that candidate keeping (lo, hi) BST bounds. whenever we
#   stand on a LEAF whose value is the root of a still unused tree, graft that
#   tree in place (copy its two children onto the leaf) and re-examine the node.
#
#   the merge is valid iff : every visited node respects its bounds, AND every
#   one of the n trees got consumed exactly once.
#
#   NOTE : the merged chain can be 5*10^4 deep -> the DFS must be ITERATIVE.
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
from collections import Counter
class Solution(object):
    def canMerge(self, trees):
        cnt = Counter()
        by_root = {}
        for t in trees:
            by_root[t.val] = t
            cnt[t.val] += 1
            if t.left:
                cnt[t.left.val] += 1
            if t.right:
                cnt[t.right.val] += 1

        root = None
        for t in trees:
            if cnt[t.val] == 1:
                root = t
                break
        if root is None:
            return None

        del by_root[root.val]
        used = 1
        stack = [(root, float('-inf'), float('inf'))]
        while stack:
            node, lo, hi = stack.pop()
            if not (lo < node.val < hi):
                return None
            if node.left is None and node.right is None:
                sub = by_root.pop(node.val, None)
                if sub is not None:
                    node.left, node.right = sub.left, sub.right
                    used += 1
                    stack.append((node, lo, hi))
                continue
            if node.left:
                stack.append((node.left, lo, node.val))
            if node.right:
                stack.append((node.right, node.val, hi))

        return root if used == len(trees) else None
