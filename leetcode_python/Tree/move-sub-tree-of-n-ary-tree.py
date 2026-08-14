"""

1516. Move Sub-Tree of N-Ary Tree
Hard

Given the root of an N-ary tree of unique values, and two nodes of the tree p and q.

You should move the subtree of the node p to become a direct child of node q. If p is already a direct child of q, do not change anything. Node p must be the last child in the children list of node q.

Return the root of the tree after adjusting it.


There are 3 cases for nodes p and q:

1. Node q is in the sub-tree of node p.
2. Node p is in the sub-tree of node q.
3. Neither node p is in the sub-tree of node q nor node q is in the sub-tree of node p.

In cases 2 and 3, you just need to move p (with its sub-tree) to be a child of q, but in case 1 the tree may be disconnected, thus you need to reconnect the tree again. Please read the examples carefully before solving this problem.


Nary-Tree input serialization is represented in their level order traversal, each group of children is separated by the null value (See examples).

For example, the tree is serialized as [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14].


Example 1:

Input: root = [1,null,2,3,null,4,5,null,6,null,7,8], p = 4, q = 1
Output: [1,null,2,3,4,null,5,null,6,null,7,8]
Explanation: This example follows the second case as node p is in the sub-tree of node q. We move node p with its sub-tree to be a direct child of node q.
Notice that node 4 is the last child of node 1.

Example 2:

Input: root = [1,null,2,3,null,4,5,null,6,null,7,8], p = 7, q = 4
Output: [1,null,2,3,null,4,5,null,6,null,7,8]
Explanation: Node 7 is already a direct child of node 4. We don't change anything.

Example 3:

Input: root = [1,null,2,3,null,4,5,null,6,null,7,8], p = 3, q = 8
Output: [1,null,2,null,4,5,null,7,8,null,null,null,3,null,6]
Explanation: This example follows case 3 because node p is not in the sub-tree of node q and vice-versa. We can move node 3 with its sub-tree and make it as node 8's child.

Example 4:

Input: root = [1,null,2,3,null,4], p = 1, q = 4
Output: [4,null,1,null,2,3]
Explanation: This example follows case 1 because node q is in the sub-tree of node p. Disconnect 4 with its parent and move node 1 with its sub-tree and make it as node 4's child.


Constraints:

The total number of nodes is between [2, 1000].
Each node has a unique value.
p != null
q != null
p and q are two different nodes (i.e. p != q).

"""

# V0
# IDEA : PARENT MAP + 3-CASE SURGERY
#
#   build a parent pointer for every node with one iterative DFS, then
#   walk up from q : if we ever hit p, q lives inside p's subtree (case 1).
#
#   case 0 : p is already a direct child of q  -> nothing to do.
#   case 1 (q inside p's subtree) : cutting p out would strand the part of
#           the tree above p, so first LIFT q into p's old slot :
#             - detach q from its own parent
#             - put q where p used to be (or make q the new root if p was root)
#             - append p as q's last child
#   case 2/3 : p's subtree does not contain q, so a plain move is safe :
#             detach p from its parent, append it to q.children.
#   NOTE : in cases 2/3 p can never be the root (the root's subtree is the
#          whole tree, which would put q inside it -> case 1), so p always
#          has a parent there.
#
# time = O(n), space = O(n)
"""
# Definition for a Node.
class Node:
    def __init__(self, val=None, children=None):
        self.val = val
        self.children = children if children is not None else []
"""
class Solution(object):
    def moveSubTree(self, root, p, q):
        if p in q.children:
            return root

        # iterative DFS -> parent of every node (recursion could hit a
        # 1000-deep chain and blow the interpreter stack)
        parent = {}
        stack = [root]
        while stack:
            node = stack.pop()
            for child in node.children:
                parent[child] = node
                stack.append(child)

        # is q inside p's subtree ?
        q_under_p = False
        cur = parent.get(q)
        while cur is not None:
            if cur is p:
                q_under_p = True
                break
            cur = parent.get(cur)

        if q_under_p:
            parent[q].children.remove(q)
            if p is root:
                q.children.append(p)
                return q
            pp = parent[p]
            pp.children[pp.children.index(p)] = q
            q.children.append(p)
            return root

        parent[p].children.remove(p)
        q.children.append(p)
        return root
