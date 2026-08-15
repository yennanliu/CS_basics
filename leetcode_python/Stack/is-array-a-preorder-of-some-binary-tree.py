"""

2764. Is Array a Preorder of Some Binary Tree
Medium

Given a 0-indexed integer 2D array nodes, your task is to determine if the given array represents the preorder traversal of some binary tree.

For each index i, nodes[i] = [id, parentId], where id is the id of the node at the index i and parentId is the id of its parent in the tree (if the node has no parent, then parentId == -1).

Return true if the given array represents the preorder traversal of some tree, and false otherwise.

Note: the preorder traversal of a tree is a recursive way to traverse a tree in which we first visit the current node, then we do the preorder traversal for the left child, and finally, we do it for the right child.


Example 1:

Input: nodes = [[0,-1],[1,0],[2,0],[3,2],[4,2]]
Output: true
Explanation: The given nodes make the tree in the picture below.
We can show that this is the preorder traversal of the tree, first we visit node 0, then we do the preorder traversal of the right child which is [1], then we do the preorder traversal of the left child which is [2,3,4].

Example 2:

Input: nodes = [[0,-1],[1,0],[2,0],[3,1],[4,1]]
Output: false
Explanation: The given nodes make the tree in the picture below.
For the preorder traversal, first we visit node 0, then we do the preorder traversal of the right child which is [1,3,4], but we can see that in the given order, 2 comes between 1 and 3, so, it's not the preorder traversal of the tree.


Constraints:

1 <= nodes.length <= 10^5
nodes[i].length == 2
0 <= nodes[i][0] <= 10^5
-1 <= nodes[i][1] <= 10^5
The input is generated such that nodes make a binary tree.

"""

# V0
# IDEA : MONOTONIC "ANCESTOR PATH" STACK
#
#   Left/right is NOT fixed for us — we may hand a node's two children to the
#   traversal in either order. So the question is only whether the array is
#   SOME depth-first order of the tree, and that has a clean characterisation:
#
#       when a node is emitted, its parent must still be on the active
#       root-to-current path.
#
#   Keep that path in a stack. For each (id, par) in array order:
#     - pop until the stack top is `par` (every popped node's subtree is
#       finished, which is exactly what a DFS would do on the way back up),
#     - if the stack drains without exposing `par`, some node was emitted
#       outside its parent's subtree window -> not a preorder,
#     - otherwise push `id` and carry on.
#
#   NOTE : the root (par == -1) is unique, so the only node ever allowed to
#          face an empty stack is nodes[0]; if the real root sits later in the
#          array, nodes[0] already fails the empty-stack test.
#   NOTE : n reaches 1e5 and the tree can be a chain, so this is written
#          iteratively — a recursive DFS would blow Python's stack.
#
# time = O(n), space = O(n)
class Solution(object):
    def isPreorder(self, nodes):
        stack = []                     # current root -> node path

        for nid, par in nodes:
            while stack and stack[-1] != par:
                stack.pop()
            if not stack and par != -1:
                return False
            stack.append(nid)

        return True
