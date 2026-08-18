#---------------------------------------------------------------
# BINARY SEARCH TREE (BST)
#---------------------------------------------------------------
#
# Scope: the ORDERED binary tree and its insert / search / delete.
#        See binary_tree.py for the unordered binary tree and the
#        traversal orders, tree.py for the general N-ary tree.
#
# THE BST INVARIANT -- for every node:
#
#         everything in the        node        everything in the
#         left subtree is    <--   val   -->   right subtree is
#         SMALLER                              LARGER
#
#              8
#            /   \
#           3     10
#          / \      \
#         1   6      14
#            / \    /
#           4   7  13
#
# Two consequences worth memorising:
#   - search/insert/delete only ever follow ONE root-to-leaf path,
#     so they cost O(H) where H is the height
#   - an IN-ORDER walk emits the values already SORTED
#
# H is log N only while the tree stays balanced. Inserting sorted
# input degrades the BST into a linked list and every operation
# becomes O(N) -- that is what AVL / red-black trees exist to prevent.
#
# Time  : search / insert / delete  O(H) -> O(log N) balanced, O(N) worst
# Space : O(N) storage, O(H) recursion stack
#
# References:
#   - https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/


class Node:
    def __init__(self, value):
        self.value = value
        self.left = None
        self.right = None


class BST:
    """Binary search tree of unique, comparable values."""

    def __init__(self, values=()):
        self.root = None
        for value in values:
            self.insert(value)

    #--- insert ---------------------------------------------------
    def insert(self, value):
        """Walk down comparing, then hang the new node off the empty side."""
        self.root = self._insert(self.root, value)

    def _insert(self, node, value):
        if node is None:                 # fell off the tree -> this is the spot
            return Node(value)
        if value < node.value:
            node.left = self._insert(node.left, value)
        elif value > node.value:
            node.right = self._insert(node.right, value)
        # value == node.value -> duplicate, ignore it
        return node

    #--- search ---------------------------------------------------
    def search(self, value):
        """Return the node holding `value`, or None. Iterative: no stack needed."""
        node = self.root
        while node:
            if value < node.value:
                node = node.left
            elif value > node.value:
                node = node.right
            else:
                return node
        return None

    def __contains__(self, value):
        return self.search(value) is not None

    def min_value(self):
        """Smallest value = leftmost node."""
        if self.root is None:
            return None
        return self._min_node(self.root).value

    def max_value(self):
        """Largest value = rightmost node."""
        if self.root is None:
            return None
        node = self.root
        while node.right:
            node = node.right
        return node.value

    @staticmethod
    def _min_node(node):
        while node.left:
            node = node.left
        return node

    #--- delete ---------------------------------------------------
    def delete(self, value):
        """Remove `value`, keeping the BST invariant intact.

        Three cases once the node is found:

          0 children  drop it
          1 child     splice the child in where the node was
          2 children  copy in the IN-ORDER SUCCESSOR (the smallest
                      value in the right subtree -- the only value
                      that keeps every comparison valid), then delete
                      that successor from the right subtree
        """
        self.root = self._delete(self.root, value)

    def _delete(self, node, value):
        if node is None:
            return None
        if value < node.value:
            node.left = self._delete(node.left, value)
        elif value > node.value:
            node.right = self._delete(node.right, value)
        else:
            if node.left is None:                 # 0 or 1 child
                return node.right
            if node.right is None:
                return node.left
            successor = self._min_node(node.right)   # 2 children
            node.value = successor.value
            node.right = self._delete(node.right, successor.value)
        return node

    #--- traversals ----------------------------------------------
    def inorder(self):
        """Sorted order, by construction."""
        values = []

        def walk(node):
            if node:
                walk(node.left)
                values.append(node.value)
                walk(node.right)

        walk(self.root)
        return values

    def preorder(self):
        values = []

        def walk(node):
            if node:
                values.append(node.value)
                walk(node.left)
                walk(node.right)

        walk(self.root)
        return values

    def height(self):
        """Edges on the longest root-to-leaf path. Empty = -1, single node = 0."""

        def walk(node):
            if node is None:
                return -1
            return 1 + max(walk(node.left), walk(node.right))

        return walk(self.root)

    def __len__(self):
        return len(self.inorder())


if __name__ == "__main__":
    #        8
    #      /   \
    #     3     10
    #    / \      \
    #   1   6      14
    #      / \    /
    #     4   7  13
    bst = BST([8, 3, 10, 1, 6, 14, 4, 7, 13])

    # an in-order walk of a BST is sorted -- this is the defining property
    assert bst.inorder() == [1, 3, 4, 6, 7, 8, 10, 13, 14]
    assert bst.preorder() == [8, 3, 1, 6, 4, 7, 10, 14, 13]
    assert len(bst) == 9
    assert bst.height() == 3

    assert 6 in bst and 99 not in bst
    assert bst.search(6).value == 6
    assert bst.search(99) is None
    assert bst.min_value() == 1
    assert bst.max_value() == 14

    # duplicates are ignored
    bst.insert(6)
    assert len(bst) == 9

    bst.delete(1)                     # leaf
    assert bst.inorder() == [3, 4, 6, 7, 8, 10, 13, 14]

    bst.delete(14)                    # one child (13 on the left)
    assert bst.inorder() == [3, 4, 6, 7, 8, 10, 13]

    bst.delete(3)                     # two children -> successor 4 moves up
    assert bst.inorder() == [4, 6, 7, 8, 10, 13]

    bst.delete(8)                     # the root, two children
    assert bst.inorder() == [4, 6, 7, 10, 13]

    bst.delete(999)                   # absent -> no-op
    assert bst.inorder() == [4, 6, 7, 10, 13]

    # sorted input degrades the BST into a chain: height == N-1, not log N
    degenerate = BST([1, 2, 3, 4, 5])
    assert degenerate.height() == 4

    print("Success.")
