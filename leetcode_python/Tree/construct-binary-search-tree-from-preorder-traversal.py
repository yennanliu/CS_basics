"""

1008. Construct Binary Search Tree from Preorder Traversal
Medium

Given an array of integers preorder, which represents the preorder traversal of a BST (i.e., binary search tree), construct the tree and return its root.

It is guaranteed that there is always possible to find a binary search tree with the given requirements for the given test cases.

A binary search tree is a binary tree where for every node, any descendant of Node.left has a value strictly less than Node.val, and any descendant of Node.right has a value strictly greater than Node.val.

A preorder traversal of a binary tree displays the value of the node first, then traverses Node.left, then traverses Node.right.


Example 1:

Input: preorder = [8,5,1,7,10,12]
Output: [8,5,10,1,7,null,12]

Example 2:

Input: preorder = [1,3]
Output: [1,null,3]


Constraints:

1 <= preorder.length <= 100
1 <= preorder[i] <= 1000
All the values of preorder are unique.

"""

# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# V0
# IDEA : RECURSION + UPPER BOUND (single pass)
#
#   preorder gives us the node BEFORE its subtrees,
#   so we can consume the array left to right and only need to know
#   the largest value still allowed in the current subtree.
#
#   build(bound):
#     - if the next value > bound -> it does NOT belong here, stop
#     - otherwise consume it as the root, then
#         left  subtree : values < root.val   -> build(root.val)
#         right subtree : values < bound      -> build(bound)
#
# time = O(n)
# space = O(n), recursion depth (skewed tree)
class Solution(object):
    def bstFromPreorder(self, preorder):
        self.i = 0

        def build(bound):
            if self.i == len(preorder) or preorder[self.i] > bound:
                return None
            node = TreeNode(preorder[self.i])
            self.i += 1
            node.left = build(node.val)
            node.right = build(bound)
            return node

        return build(float('inf'))


# V1
# IDEA : MONOTONIC STACK (iterative, no recursion depth limit)
#
#   keep a DECREASING stack of nodes on the current right spine.
#   for each value v :
#     - if v < stack top -> it is the top's left child
#     - else pop while stack top < v ; the LAST popped node is the parent,
#       and v becomes its right child
#
# time = O(n)
# space = O(n)
class Solution(object):
    def bstFromPreorder(self, preorder):
        root = TreeNode(preorder[0])
        stack = [root]
        for v in preorder[1:]:
            node = TreeNode(v)
            if v < stack[-1].val:
                stack[-1].left = node
            else:
                parent = None
                while stack and stack[-1].val < v:
                    parent = stack.pop()
                parent.right = node
            stack.append(node)
        return root
