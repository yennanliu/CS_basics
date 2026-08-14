"""

993. Cousins in Binary Tree
Easy

Given the root of a binary tree with unique values and the values of two different nodes of the tree x and y, return true if the nodes corresponding to the values x and y in the tree are cousins, or false otherwise.

Two nodes of a binary tree are cousins if they have the same depth with different parents.

Note that in a binary tree, the root node is at the depth 0, and children of each depth k node are at the depth k + 1.

Example 1:

Input: root = [1,2,3,4], x = 4, y = 3
Output: false

Example 2:

Input: root = [1,2,3,null,4,null,5], x = 5, y = 4
Output: true

Example 3:

Input: root = [1,2,3,null,4], x = 2, y = 3
Output: false

Constraints:

The number of nodes in the tree is in the range [2, 100].
1 <= Node.val <= 100
Each node has a unique value.
x != y
x and y exist in the tree.

"""

# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# V0
# IDEA : DFS collecting (depth, parent) for both targets
#
#  Cousins  <=>  same depth AND different parent.
#  A single DFS records (depth, parent_value) for x and y, then compares.
#  NOTE: we must NOT short-circuit on the first hit - we need BOTH nodes.
#
# time = O(n)  # n = number of tree nodes
# space = O(h)  # h = tree height (recursion stack)
class Solution(object):
    def isCousins(self, root, x, y):
        info = {}

        def dfs(node, parent, depth):
            if not node:
                return
            if node.val == x or node.val == y:
                info[node.val] = (depth, parent)
            dfs(node.left, node.val, depth + 1)
            dfs(node.right, node.val, depth + 1)

        dfs(root, None, 0)

        depth_x, parent_x = info[x]
        depth_y, parent_y = info[y]
        return depth_x == depth_y and parent_x != parent_y

# V1
# IDEA : BFS level by level
#
#  Scan one level at a time. Within a level, note whether x and y were seen
#  and whether they came from the SAME parent (i.e. are siblings).
#  If both appear on the same level and are not siblings -> cousins.
#
# time = O(n)  # n = number of tree nodes
# space = O(w)  # w = max tree width (queue)
from collections import deque
class Solution(object):
    def isCousins(self, root, x, y):
        queue = deque([(root, None)])
        while queue:
            found = 0
            parents = set()
            for _ in range(len(queue)):
                node, parent = queue.popleft()
                if node.val == x or node.val == y:
                    found += 1
                    parents.add(id(parent))
                if node.left:
                    queue.append((node.left, node))
                if node.right:
                    queue.append((node.right, node))
            if found == 1:
                # only one of them sits on this level -> different depths
                return False
            if found == 2:
                # both here: cousins only when the parents differ
                return len(parents) == 2
        return False
