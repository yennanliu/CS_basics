"""

559. Maximum Depth of N-ary Tree
Easy

Given a n-ary tree, find its maximum depth.

The maximum depth is the number of nodes along the longest path from the root node down to
the farthest leaf node.

Nary-Tree input serialization is represented in their level order traversal,
each group of children is separated by the null value (See examples).

Example 1:

Input: root = [1,null,3,2,4,null,5,6]
Output: 3

Example 2:

Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
Output: 5


Constraints:

The total number of nodes is in the range [0, 10^4].
The depth of the n-ary tree is less than or equal to 1000.

"""

# Definition for a Node.
# class Node(object):
#     def __init__(self, val=None, children=None):
#         self.val = val
#         self.children = children

# V0
# IDEA : DFS (recursion)
#        -> depth(node) = 1 + max(depth(child) for child in children), 0 for a null node
# time = O(n)
# space = O(h)  # h = tree height (recursion stack)
class Solution(object):
    def maxDepth(self, root):
        if not root:
            return 0

        # a leaf (no children) has depth 1
        if not root.children:
            return 1

        return 1 + max(self.maxDepth(child) for child in root.children)

# V1
# IDEA : BFS (level order), avoids deep recursion
# time = O(n)
# space = O(w)  # w = max width of the tree
from collections import deque
class Solution(object):
    def maxDepth(self, root):
        if not root:
            return 0

        depth = 0
        q = deque([root])
        while q:
            depth += 1
            for _ in range(len(q)):
                node = q.popleft()
                for child in (node.children or []):
                    q.append(child)

        return depth
