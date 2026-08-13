"""

1104. Path In Zigzag Labelled Binary Tree
Medium

In an infinite binary tree where every node has two children, the nodes are
labelled in row order.

In the odd numbered rows (ie., the first, third, fifth,...), the labelling is
left to right, while in the even numbered rows (second, fourth, sixth,...),
the labelling is right to left.

Given the label of a node in this tree, return the labels in the path from the
root of the tree to the node with that label.


Example 1:

Input: label = 14
Output: [1,3,4,14]

Example 2:

Input: label = 26
Output: [1,2,6,10,26]


Constraints:

1 <= label <= 10^6

"""

# V0
# IDEA: MATH (mirror the label back to the "normal" numbering)
#
#   level L (1-based) holds labels in [2^(L-1), 2^L - 1].
#   if the level were labelled left -> right, the parent would be label // 2.
#   because odd/even levels alternate direction, we FIRST mirror the label
#   inside its own level:
#
#       mirror = (2^(L-1) + 2^L - 1) - label
#
#   then parent = mirror // 2  (the parent level is mirrored too,
#   so this single mirror per step is enough).
#
# time = O(log label)
# space = O(log label)
class Solution(object):
    def pathInZigZagTree(self, label):
        # find the level (1-based) that contains `label`
        level = 1
        while (1 << level) <= label:
            level += 1

        res = [0] * level
        while level > 0:
            res[level - 1] = label
            # NOTE !!! mirror within the level, then go to the parent
            label = ((1 << (level - 1)) + (1 << level) - 1 - label) >> 1
            level -= 1

        return res
