"""

431. Encode N-ary Tree to Binary Tree
Hard

Design an algorithm to encode an N-ary tree into a binary tree and decode the
binary tree to get the original N-ary tree. An N-ary tree is a rooted tree in
which each node has no more than N children. Similarly, a binary tree is a
rooted tree in which each node has no more than 2 children. There is no
restriction on how your encode/decode algorithm should work. You just need to
ensure that an N-ary tree can be encoded to a binary tree and this binary tree
can be decoded to the original N-ary tree structure.

Nary-Tree input serialization is represented in their level order traversal,
each group of children is separated by the null value.

Example 1:

Input: root = [1,null,3,2,4,null,5,6]
Output: [1,null,3,2,4,null,5,6]

Example 2:

Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
Output: [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]

Example 3:

Input: root = []
Output: []

Constraints:

The number of nodes in the tree is in the range [0, 10^4].
0 <= Node.val <= 10^4
The height of the n-ary tree is less than or equal to 1000
Do not use class member/global/static variables to store states.
Your encode and decode algorithms should be stateless.

"""

# Definition for a Node.
class Node(object):
    def __init__(self, val=None, children=None):
        self.val = val
        self.children = children if children is not None else []


# Definition for a binary tree node.
class TreeNode(object):
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# V0
# IDEA : "LEFT-CHILD / RIGHT-SIBLING" REPRESENTATION
#
#  The classic trick for turning an N-ary tree into a binary one:
#
#     binary.left  = the FIRST child of the n-ary node
#     binary.right = the NEXT SIBLING of the n-ary node
#
#  So a node's whole children list becomes a right-going chain hanging off
#  its left pointer:
#
#      n-ary            binary
#        1                1
#      / | \             /
#     3  2  4           3
#    / \                 \
#   5   6                 2
#                          \
#                           4
#      (3's own children 5,6 hang off 3.left -> 5 -> right -> 6)
#
#  Decoding just walks that right chain back into a children list.
#
# time = O(n)
# space = O(n)
class Codec(object):

    def encode(self, root):
        """Encodes an n-ary tree to a binary tree.

        :type root: Node
        :rtype: TreeNode
        """
        if root is None:
            return None

        node = TreeNode(root.val)
        if root.children:
            # first child goes on the LEFT
            node.left = self.encode(root.children[0])
            # the rest form a RIGHT chain off that first child
            cur = node.left
            for child in root.children[1:]:
                cur.right = self.encode(child)
                cur = cur.right
        return node

    def decode(self, data):
        """Decodes your binary tree to an n-ary tree.

        :type data: TreeNode
        :rtype: Node
        """
        if data is None:
            return None

        node = Node(data.val, [])
        cur = data.left          # first child
        while cur:
            node.children.append(self.decode(cur))
            cur = cur.right      # next sibling
        return node
