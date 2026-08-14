"""

1490. Clone N-ary Tree
Medium

Given a root of an N-ary tree, return a deep copy (clone) of the tree.

Each node in the n-ary tree contains a val (int) and a list (List[Node]) of its children.

class Node {
    public int val;
    public List<Node> children;
}

Nary-Tree input serialization is represented in their level order traversal, each group of children is separated by the null value (See examples).


Example 1:

Input: root = [1,null,3,2,4,null,5,6]
Output: [1,null,3,2,4,null,5,6]

Example 2:

Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
Output: [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]


Constraints:

The depth of the n-ary tree is less than or equal to 1000.
The total number of nodes is between [0, 10^4].


Follow up: Can your solution work for the graph problem?

"""

# V0
# IDEA : POST-ORDER DFS (clone the children first, then build the parent)
#
#   a tree has no shared nodes and no cycles, so no visited map is needed:
#   recursively clone every child, then wrap the copies in a fresh Node
#   carrying the same value.
#   NOTE : build a NEW children list - reusing root.children would leave
#          the clone pointing at the original nodes.
#   NOTE : for the graph follow-up this needs an old -> new dict so that a
#          revisited node returns its existing copy instead of recursing.
#
# time = O(n), space = O(h) recursion, h = depth
# Definition for a Node.
# class Node(object):
#     def __init__(self, val=None, children=None):
#         self.val = val
#         self.children = children if children is not None else []
class Solution(object):
    def cloneTree(self, root):
        if root is None:
            return None
        children = [self.cloneTree(child) for child in root.children]
        return Node(root.val, children)
