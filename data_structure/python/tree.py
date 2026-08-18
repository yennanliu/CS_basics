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
    """A general tree, addressed through its root."""

    def __init__(self, root_value=None):
        self.root = TreeNode(root_value) if root_value is not None else None

    def size(self, node="__root__"):
        """Number of nodes in the subtree (1 for itself + every child's size)."""
        node = self.root if node == "__root__" else node
        if node is None:
            return 0
        return 1 + sum(self.size(child) for child in node.children)

    def height(self, node="__root__"):
        """Longest path down, in EDGES. A single node has height 0."""
        node = self.root if node == "__root__" else node
        if node is None:
            return -1                      # empty tree, so that a leaf works out to 0
        if node.is_leaf():
            return 0
        return 1 + max(self.height(child) for child in node.children)

    def find(self, value, node="__root__"):
        """Depth-first search for the first node holding `value`."""
        node = self.root if node == "__root__" else node
        if node is None:
            return None
        if node.value == value:
            return node
        for child in node.children:
            found = self.find(value, child)
            if found:
                return found
        return None

    def dfs(self, node="__root__"):
        """Pre-order depth-first walk: parent first, then each subtree."""
        node = self.root if node == "__root__" else node
        if node is None:
            return []
        values = [node.value]
        for child in node.children:
            values.extend(self.dfs(child))
        return values

    def bfs(self):
        """Level-order breadth-first walk, using a queue."""
        if self.root is None:
            return []
        values, queue = [], deque([self.root])
        while queue:
            node = queue.popleft()
            values.append(node.value)
            queue.extend(node.children)
        return values

    def leaves(self):
        return [v for v in self.dfs() if self.find(v).is_leaf()]

    def pretty(self, node="__root__", depth=0):
        """Indented text drawing of the tree."""
        node = self.root if node == "__root__" else node
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
    assert sorted(tree.leaves()) == ["b", "d", "e", "f"]

    empty = Tree()
    assert empty.size() == 0 and empty.height() == -1 and empty.bfs() == []

    print(tree.pretty(), end="")
    print("Success.")
