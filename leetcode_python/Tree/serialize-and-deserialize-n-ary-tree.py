"""

428. Serialize and Deserialize N-ary Tree
Hard

Serialization is the process of converting a data structure or object into a
sequence of bits so that it can be stored in a file or memory buffer, or
transmitted across a network connection link to be reconstructed later in the
same or another computer environment.

Design an algorithm to serialize and deserialize an N-ary tree. An N-ary tree is
a rooted tree in which each node has no more than N children. There is no
restriction on how your serialization/deserialization algorithm should work. You
just need to ensure that an N-ary tree can be serialized to a string and this
string can be deserialized to the original tree structure.

For example, you may serialize the following 3-ary tree as [1 [3[5 6] 2 4]].
Note that this is just an example, you do not necessarily need to follow this
format.

Or you can follow LeetCode's level order traversal serialization format, where
each group of children is separated by the null value, e.g.
[1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14].

Example 1:

Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
Output: [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]

Example 2:

Input: root = [1,null,3,2,4,null,5,6]
Output: [1,null,3,2,4,null,5,6]

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


# V0
# IDEA : PRE-ORDER DFS + CHILD COUNT
#
#  Emit "val,childCount" per node in pre-order. The child count tells the
#  decoder exactly how many sub-trees to read next, so no null markers and no
#  ambiguity - a single linear scan rebuilds the tree.
#
#     serialize:  1,3,3,2,5,0,6,0,2,0,4,0
#                 ^ ^   ^ ^
#                 | |   | node 3 has 2 children
#                 | node 1 has 3 children
#
# time = O(n)   # serialize and deserialize both
# space = O(n)
class Codec(object):

    def serialize(self, root):
        """Encodes a tree to a single string.

        :type root: Node
        :rtype: str
        """
        out = []

        def dfs(node):
            if node is None:
                return
            out.append(str(node.val))
            out.append(str(len(node.children)))
            for child in node.children:
                dfs(child)

        dfs(root)
        return ",".join(out)

    def deserialize(self, data):
        """Decodes your encoded data to tree.

        :type data: str
        :rtype: Node
        """
        if not data:
            return None

        # an iterator keeps the read cursor local (no member/global state)
        tokens = iter(data.split(","))

        def build():
            val = int(next(tokens))
            size = int(next(tokens))
            node = Node(val, [])
            for _ in range(size):
                node.children.append(build())
            return node

        return build()


# V1
# IDEA : BFS (LEVEL ORDER) + "#" AS THE END-OF-CHILDREN MARKER
#
#  Distinct trick: no recursion at all, so a 1000-deep tree cannot blow the
#  Python recursion limit. Children of a node are emitted contiguously and
#  terminated by "#".
#
# time = O(n)
# space = O(n)
from collections import deque
class Codec2(object):

    def serialize(self, root):
        if root is None:
            return ""
        # "rootVal" then, for every node in BFS order, its children ended by "#"
        out = [str(root.val)]
        q = deque([root])
        while q:
            node = q.popleft()
            for child in node.children:
                out.append(str(child.val))
                q.append(child)
            out.append("#")     # end of this node's children list
        return ",".join(out)

    def deserialize(self, data):
        if not data:
            return None
        tokens = data.split(",")
        root = Node(int(tokens[0]), [])
        i = 1
        q = deque([root])
        while q and i < len(tokens):
            node = q.popleft()
            while tokens[i] != "#":
                child = Node(int(tokens[i]), [])
                node.children.append(child)
                q.append(child)
                i += 1
            i += 1              # skip the "#"
        return root
