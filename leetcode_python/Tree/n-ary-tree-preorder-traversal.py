"""

589. N-ary Tree Preorder Traversal
Easy

Given the root of an n-ary tree, return the preorder traversal of its nodes' values.

Nary-Tree input serialization is represented in their level order traversal.
Each group of children is separated by the null value (See examples)

Example 1:

Input: root = [1,null,3,2,4,null,5,6]
Output: [1,3,5,6,2,4]

Example 2:

Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
Output: [1,2,3,6,7,11,14,4,8,12,5,9,13,10]


Constraints:

The number of nodes in the tree is in the range [0, 10^4].
0 <= Node.val <= 10^4
The height of the n-ary tree is less than or equal to 1000.


Follow up: Recursive solution is trivial, could you do it iteratively?

"""

# Definition for a Node.
# class Node(object):
#     def __init__(self, val=None, children=None):
#         self.val = val
#         self.children = children

# V0
# IDEA : DFS (recursion) -> visit node, then its children left to right
# time = O(n)
# space = O(h)  # h = tree height (recursion stack)
class Solution(object):
    def preorder(self, root):
        res = []

        def dfs(node):
            if not node:
                return
            res.append(node.val)
            for child in (node.children or []):
                dfs(child)

        dfs(root)
        return res

# V1
# IDEA : ITERATIVE with a STACK (answers the follow up)
#        -> push children in REVERSED order, so the leftmost child is popped first
# time = O(n)
# space = O(n)
class Solution(object):
    def preorder(self, root):
        if not root:
            return []

        res = []
        stack = [root]
        while stack:
            node = stack.pop()
            res.append(node.val)
            for child in reversed(node.children or []):
                stack.append(child)

        return res
