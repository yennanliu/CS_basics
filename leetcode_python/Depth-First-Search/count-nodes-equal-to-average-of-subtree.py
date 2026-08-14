"""

2265. Count Nodes Equal to Average of Subtree
Medium

Given the root of a binary tree, return the number of nodes where the value of the node is equal to the average of the values in its subtree.

Note:

The average of n elements is the sum of the n elements divided by n and rounded down to the nearest integer.
A subtree of root is a tree consisting of root and all of its descendants.


Example 1:

Input: root = [4,8,5,0,1,null,6]
Output: 5
Explanation:
For the node with value 4: The average of its subtree is (4 + 8 + 5 + 0 + 1 + 6) / 6 = 24 / 6 = 4.
For the node with value 5: The average of its subtree is (5 + 6) / 2 = 11 / 2 = 5.
For the node with value 0: The average of its subtree is 0 / 1 = 0.
For the node with value 1: The average of its subtree is 1 / 1 = 1.
For the node with value 6: The average of its subtree is 6 / 1 = 6.

Example 2:

Input: root = [1]
Output: 1
Explanation: For the node with value 1: The average of its subtree is 1 / 1 = 1.


Constraints:

The number of nodes in the tree is in the range [1, 1000].
0 <= Node.val <= 1000

"""

# V0
# IDEA : ITERATIVE POST-ORDER DFS (bubble up (sum, count) per subtree)
#
#   every node needs its subtree sum and node count, which are available only
#   after both children are done -> post-order.
#
#     sum[u]   = val[u] + sum[left] + sum[right]
#     count[u] = 1      + cnt[left] + cnt[right]
#     node u counts when sum[u] // count[u] == val[u]
#
#   NOTE : the tree can be a 1000-node chain, so the traversal is written with
#          an explicit stack instead of recursion.
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def averageOfSubtree(self, root):
        if not root:
            return 0

        res = 0
        info = {}                     # id(node) -> (subtree sum, subtree size)
        stack = [(root, False)]
        while stack:
            node, done = stack.pop()
            if not done:
                stack.append((node, True))
                if node.left:
                    stack.append((node.left, False))
                if node.right:
                    stack.append((node.right, False))
                continue

            s, c = node.val, 1
            for kid in (node.left, node.right):
                if kid:
                    ks, kc = info[id(kid)]
                    s += ks
                    c += kc
            info[id(node)] = (s, c)
            if s // c == node.val:
                res += 1

        return res
