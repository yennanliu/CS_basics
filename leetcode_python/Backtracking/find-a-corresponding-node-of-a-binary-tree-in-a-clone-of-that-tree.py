"""

1379. Find a Corresponding Node of a Binary Tree in a Clone of That Tree
Easy

Given two binary trees original and cloned and given a reference to a node
target in the original tree.

The cloned tree is a copy of the original tree.

Return a reference to the same node in the cloned tree.

Note that you are not allowed to change any of the two trees or the target node
and the answer must be a reference to a node in the cloned tree.


Example 1:

Input: tree = [7,4,3,null,null,6,19], target = 3
Output: 3
Explanation: In all examples the original and cloned trees are shown. The target
node is a green node from the original tree. The answer is the yellow node from
the cloned tree.

Example 2:

Input: tree = [7], target = 7
Output: 7

Example 3:

Input: tree = [8,null,6,null,5,null,4,null,3,null,2,null,1], target = 4
Output: 4


Constraints:

The number of nodes in the tree is in the range [1, 10^4].
The values of the nodes of the tree are unique.
target node is a node from the original tree and is not null.


Follow up: Could you solve the problem if repeated values on the tree are
allowed?

"""

# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


# V0
# IDEA: walk BOTH trees in lock step (DFS)
#
#  the two trees have the exact same shape, so whenever we stand on `target`
#  in `original`, the node we stand on in `cloned` is the answer.
#
#  NOTE !!! we compare by IDENTITY (`is`), not by value -> works even when
#           the tree contains duplicated values (the follow up)
#
# time = O(n)
# space = O(h), h = tree height (recursion stack)
class Solution(object):
    def getTargetCopy(self, original, cloned, target):
        def dfs(node1, node2):
            if node1 is None:
                return None
            if node1 is target:
                return node2
            return dfs(node1.left, node2.left) or dfs(node1.right, node2.right)

        return dfs(original, cloned)


# V1
# IDEA: iterative DFS with an explicit stack (no recursion limit issue)
# time = O(n)
# space = O(h)
class Solution(object):
    def getTargetCopy(self, original, cloned, target):
        stack = [(original, cloned)]
        while stack:
            node1, node2 = stack.pop()
            if node1 is None:
                continue
            if node1 is target:
                return node2
            stack.append((node1.left, node2.left))
            stack.append((node1.right, node2.right))
        return None
