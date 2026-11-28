#---------------------------------------------------------------
# TREE (general / N-ary)
#---------------------------------------------------------------
#
# Scope: the GENERAL tree -- every node may have any number of
#        children. See binary_tree.py (at most 2 children, plus the
#        traversal orders) and binary_search_tree.py (ordered binary
#        tree) for the specialised forms.
#
#                 root
#                /  |  \
#               a   b   c
#              / \      |
#             d   e     f
#
# Vocabulary used throughout the repo:
#   root      the single node with no parent
#   leaf      a node with no children
#   depth     edges from the ROOT down to a node   (root has depth 0)
#   height    edges from a node down to its deepest leaf (leaf = 0)
#   size      total number of nodes
#
# Time  : add_child      O(1)
#         find / size / height / traversals   O(N)
# Space : O(N) for the tree, O(H) recursion stack for the walks
#
# References:
#   - https://stackoverflow.com/questions/2358045/how-can-i-implement-a-tree-in-python


from collections import deque


class TreeNode:
    """A node holding a value and a list of child nodes."""

    def __init__(self, value):
        self.value = value
        self.children = []

    def add_child(self, child):
        """Attach a TreeNode (or a raw value) as a child; return the child node."""
        if not isinstance(child, TreeNode):
            child = TreeNode(child)
        self.children.append(child)
        return child

    def is_leaf(self):
        return not self.children

    def __str__(self):
        return str(self.value)


class Tree:
    """A general tree, addressed through its root.

    Every public method answers for the WHOLE tree, or for the subtree
    under `node` when one is passed in. The recursion itself lives in a
    private `_helper(node)` so each public method stays one line.
    """

    def __init__(self, root_value=None):
        self.root = TreeNode(root_value) if root_value is not None else None

    def _start(self, node):
        """Where a walk begins: the given subtree, or the root by default."""
        return self.root if node is None else node

    #--- measurements ---------------------------------------------
    def size(self, node=None):
        """Number of nodes: 1 for the node itself + the size of every child."""
        return self._size(self._start(node))

    def _size(self, node):
        if node is None:                   # empty tree
            return 0
        total = 1
        for child in node.children:
            total += self._size(child)
        return total

    def height(self, node=None):
        """Longest path down, in EDGES. A single node has height 0."""
        return self._height(self._start(node))

    def _height(self, node):
        if node is None:
            return -1                      # empty tree, so that a leaf works out to 0
        tallest_child = -1
        for child in node.children:
            tallest_child = max(tallest_child, self._height(child))
        return 1 + tallest_child           # a leaf: 1 + (-1) = 0

    #--- search ---------------------------------------------------
    def find(self, value, node=None):
        """Depth-first search for the first node holding `value`, else None."""
        return self._find(self._start(node), value)

    def _find(self, node, value):
        if node is None:
            return None
        if node.value == value:
            return node
        for child in node.children:
            found = self._find(child, value)
            if found is not None:
                return found
        return None

    #--- traversals -----------------------------------------------
    def dfs(self, node=None):
        """Pre-order depth-first walk: parent first, then each subtree."""
        values = []
        self._dfs(self._start(node), values)
        return values

    def _dfs(self, node, values):
        if node is None:
            return
        values.append(node.value)          # visit the parent...
        for child in node.children:        # ...then all of each subtree in turn
            self._dfs(child, values)

    def bfs(self):
        """Level-order breadth-first walk, using a queue."""
        if self.root is None:
            return []
        values = []
        queue = deque([self.root])
        while queue:
            node = queue.popleft()
            values.append(node.value)
            queue.extend(node.children)    # children wait behind this whole level
        return values

    def leaves(self):
        """Values of every node with no children, left to right."""
        values = []
        self._leaves(self.root, values)
        return values

    def _leaves(self, node, values):
        if node is None:
            return
        if node.is_leaf():
            values.append(node.value)
        for child in node.children:
            self._leaves(child, values)

    #--- display --------------------------------------------------
    def pretty(self, node=None, depth=0):
        """Indented text drawing of the tree, one node per line."""
        node = self._start(node)
        if node is None:
            return ""
        out = "  " * depth + str(node.value) + "\n"
        for child in node.children:
            out += self.pretty(child, depth + 1)
        return out


if __name__ == "__main__":
    #        root
    #       /  |  \
    #      a   b   c
    #     / \      |
    #    d   e     f
    tree = Tree("root")
    a = tree.root.add_child("a")
    b = tree.root.add_child("b")
    c = tree.root.add_child("c")
    a.add_child("d")
    a.add_child("e")
    c.add_child("f")

    assert tree.size() == 7
    assert tree.height() == 2
    assert tree.height(b) == 0             # b is a leaf

    # pre-order: parent, then the whole of each subtree in turn
    assert tree.dfs() == ["root", "a", "d", "e", "b", "c", "f"]
    # level-order: one depth at a time
    assert tree.bfs() == ["root", "a", "b", "c", "d", "e", "f"]

    assert tree.find("e").value == "e"
    assert tree.find("zzz") is None
    assert tree.leaves() == ["d", "e", "b", "f"]

    # subtree queries: pass the node to start from
    assert tree.size(a) == 3 and tree.dfs(c) == ["c", "f"]

    # two nodes may hold the same VALUE; leaves() must look at the node,
    # not look the value up again
    twins = Tree("x")
    twins.root.add_child("x")
    assert twins.leaves() == ["x"]

    empty = Tree()
    assert empty.size() == 0 and empty.height() == -1 and empty.bfs() == []

    print(tree.pretty(), end="")
    print("Success.")
