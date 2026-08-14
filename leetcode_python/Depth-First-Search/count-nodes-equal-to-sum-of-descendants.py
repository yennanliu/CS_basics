"""

1973. Count Nodes Equal to Sum of Descendants
Medium

Given the root of a binary tree, return the number of nodes where the value of the node is equal to the sum of the values of its descendants.

A descendant of a node x is any node that is on the path from node x to some leaf node. The sum is considered to be 0 if the node has no descendants.


Example 1:

Input: root = [10,3,4,2,1]
Output: 2
Explanation:
For the node with value 10: The sum of its descendants is 3+4+2+1 = 10.
For the node with value 3: The sum of its descendants is 2+1 = 3.

Example 2:

Input: root = [2,3,null,2,null]
Output: 0
Explanation:
No node has a value that is equal to the sum of its descendants.

Example 3:

Input: root = [0]
Output: 1
For the node with value 0: The sum of its descendants is 0 since it has no descendants.


Constraints:

The number of nodes in the tree is in the range [1, 10^5].
0 <= Node.val <= 10^5

"""

# V0
# IDEA : POST-ORDER DFS (bottom-up subtree sums)
#
#   dfs(node) returns the sum of the WHOLE subtree rooted at node.
#   the descendant sum of node is then dfs(left) + dfs(right), so we compare
#   that against node.val before returning node.val + left + right.
#
#   NOTE : a leaf has descendant sum 0, so a leaf with val == 0 counts.
#   NOTE : done iteratively (explicit stack) - the tree can hold 10^5 nodes
#          and may be a chain, which would blow python's recursion limit.
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def equalToDescendants(self, root):
        res = 0
        total = {}          # id(node) -> subtree sum
        stack = [(root, False)]
        while stack:
            node, done = stack.pop()
            if node is None:
                continue
            if not done:
                stack.append((node, True))
                stack.append((node.left, False))
                stack.append((node.right, False))
                continue
            l = total.get(id(node.left), 0)
            r = total.get(id(node.right), 0)
            if l + r == node.val:
                res += 1
            total[id(node)] = node.val + l + r
        return res
