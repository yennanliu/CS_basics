"""

2385. Amount of Time for Binary Tree to Be Infected
Medium

You are given the root of a binary tree with unique values, and an integer start. At minute 0, an infection starts from the node with value start.

Each minute, a node becomes infected if:

The node is currently uninfected.
The node is adjacent to an infected node.

Return the number of minutes needed for the entire tree to be infected.


Example 1:

Input: root = [1,5,3,null,4,10,6,9,2], start = 3
Output: 4
Explanation: The following nodes are infected during:
- Minute 0: Node 3
- Minute 1: Nodes 1, 10 and 6
- Minute 2: Node 5
- Minute 3: Node 4
- Minute 4: Nodes 9 and 2
It takes 4 minutes for the whole tree to be infected so we return 4.

Example 2:

Input: root = [1], start = 1
Output: 0
Explanation: At minute 0, the only node in the tree is infected so we return 0.


Constraints:

The number of nodes in the tree is in the range [1, 10^5].
1 <= Node.val <= 10^5
Each node has a unique value.
A node is adjacent to another node if it has a direct edge to it.
start exists in the tree.


"""

# V0
# IDEA : THE INFECTION IGNORES THE PARENT/CHILD DIRECTION — MAKE IT A GRAPH
#
#   spreading to "adjacent" nodes means it walks UP to the parent as well as
#   down to the children, so the tree structure is really an undirected graph
#   here.
#
#   so : one pass to record each node's parent (turning the tree into an
#   adjacency map), then a plain BFS from `start`. the answer is the depth of
#   the last BFS layer, i.e. the eccentricity of `start`.
#
#   NOTE : both passes are iterative — the tree can hold 10^5 nodes and be
#          shaped like a path.
#
# time = O(n), space = O(n)
from collections import deque, defaultdict


# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def amountOfTime(self, root, start):
        g = defaultdict(list)
        stack = [root]
        while stack:
            node = stack.pop()
            for child in (node.left, node.right):
                if child:
                    g[node.val].append(child.val)
                    g[child.val].append(node.val)
                    stack.append(child)

        seen = {start}
        q = deque([start])
        minutes = -1
        while q:
            minutes += 1
            for _ in range(len(q)):
                u = q.popleft()
                for v in g[u]:
                    if v not in seen:
                        seen.add(v)
                        q.append(v)
        return minutes
