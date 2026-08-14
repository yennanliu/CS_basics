"""

2196. Create Binary Tree From Descriptions
Medium

You are given a 2D integer array descriptions where descriptions[i] = [parent_i, child_i, isLeft_i] indicates that parent_i is the parent of child_i in a binary tree of unique values. Furthermore,

If isLeft_i == 1, then child_i is the left child of parent_i.
If isLeft_i == 0, then child_i is the right child of parent_i.

Construct the binary tree described by descriptions and return its root.

The test cases will be generated such that the binary tree is valid.


Example 1:

Input: descriptions = [[20,15,1],[20,17,0],[50,20,1],[50,80,0],[80,19,1]]
Output: [50,20,80,15,17,19]
Explanation: The root node is the node with value 50 since it has no parent.
The resulting binary tree is shown in the diagram.

Example 2:

Input: descriptions = [[1,2,1],[2,3,0],[3,4,1]]
Output: [1,2,null,null,3,4]
Explanation: The root node is the node with value 1 since it has no parent.
The resulting binary tree is shown in the diagram.


Constraints:

1 <= descriptions.length <= 10^4
descriptions[i].length == 3
1 <= parent_i, child_i <= 10^5
0 <= isLeft_i <= 1
The binary tree described by descriptions is valid.

"""

# V0
# IDEA : NODE POOL KEYED BY VALUE + A SET OF EVERY VALUE THAT HAS A PARENT
#
#   values are unique, so a dict value -> TreeNode lets each description wire
#   up its two nodes regardless of the order the rows arrive in (creating
#   either endpoint on demand).
#
#   the root is the one value that never appears as a CHILD, so track the set
#   of children while building and take the single leftover.
#
# time = O(n), space = O(n)
# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right
class Solution(object):
    def createBinaryTree(self, descriptions):
        nodes = {}
        children = set()

        def get(v):
            if v not in nodes:
                nodes[v] = TreeNode(v)
            return nodes[v]

        for parent, child, is_left in descriptions:
            p, c = get(parent), get(child)
            if is_left:
                p.left = c
            else:
                p.right = c
            children.add(child)

        root_val = next(v for v in nodes if v not in children)
        return nodes[root_val]
