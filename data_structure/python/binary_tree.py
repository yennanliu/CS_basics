#---------------------------------------------------------------
# BINARY TREE
#---------------------------------------------------------------
#
# Scope: the BINARY tree -- every node has at most a left and a right
#        child -- and the four traversal orders. No ordering rule is
#        imposed on the values; see binary_search_tree.py for that.
#        See tree.py for the general N-ary tree.
#
#             1
#           /   \
#          2     3
#         / \     \
#        4   5     6
#
# THE FOUR WALKS (the only thing that changes is WHEN the node itself
# is visited relative to its two subtrees):
#
#   pre-order    node, left, right     1 2 4 5 3 6   copy/serialise a tree
#   in-order     left, node, right     4 2 5 1 3 6   sorted output on a BST
#   post-order   left, right, node     4 5 2 6 3 1   delete/evaluate bottom-up
#   level-order  breadth-first (queue) 1 2 3 4 5 6   shortest path by depth
#
# Time  : every traversal O(N); height O(N)
# Space : O(H) recursion stack (H = height; O(N) if the tree degrades
#         into a chain, O(log N) when balanced)
#
# References:
#   - https://www.geeksforgeeks.org/binary-tree-array-implementation/


from collections import deque


class TreeNode:
    """A binary tree node: value plus left/right child pointers."""

    def __init__(self, value, left=None, right=None):
        self.value = value
        self.left = left
        self.right = right

    def __str__(self):
        return str(self.value)


def preorder(node):
    """node -> left -> right"""
    if node is None:
        return []
    return [node.value] + preorder(node.left) + preorder(node.right)


def inorder(node):
    """left -> node -> right"""
    if node is None:
        return []
    return inorder(node.left) + [node.value] + inorder(node.right)


def postorder(node):
    """left -> right -> node"""
    if node is None:
        return []
    return postorder(node.left) + postorder(node.right) + [node.value]


def level_order(root):
    """Breadth-first: visit depth 0, then depth 1, ... using a queue."""
    if root is None:
        return []
    values, queue = [], deque([root])
    while queue:
        node = queue.popleft()
        values.append(node.value)
        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right)
    return values


def inorder_iterative(root):
    """In-order without recursion: an explicit stack replaces the call stack.

    Walk left as far as possible pushing every node, then pop-and-visit
    and turn right. This is the pattern behind most 'BST iterator' problems.
    """
    values, stack, node = [], [], root
    while stack or node:
        while node:                    # dive left, remembering the way back
            stack.append(node)
            node = node.left
        node = stack.pop()             # nothing further left -> visit
        values.append(node.value)
        node = node.right              # then do the same for the right subtree
    return values


def height(node):
    """Edges on the longest root-to-leaf path. Empty tree = -1, leaf = 0."""
    if node is None:
        return -1
    return 1 + max(height(node.left), height(node.right))


def size(node):
    if node is None:
        return 0
    return 1 + size(node.left) + size(node.right)


#---------------------------------------------------------------
# Array representation
#---------------------------------------------------------------
#
# A COMPLETE binary tree needs no pointers at all: store it in an
# array and derive the relatives by index arithmetic. This is exactly
# how a binary heap is built -- see heap.py / MinHeap.py.
#
#   index      0  1  2  3  4  5        left child  : 2*i + 1
#   value      1  2  3  4  5  6        right child : 2*i + 2
#                                      parent      : (i - 1) // 2
#
class ArrayBinaryTree:
    """Binary tree stored in a flat list, addressed by index arithmetic."""

    def __init__(self, capacity=10):
        self.tree = [None] * capacity

    @staticmethod
    def left_of(i):
        return 2 * i + 1

    @staticmethod
    def right_of(i):
        return 2 * i + 2

    @staticmethod
    def parent_of(i):
        return (i - 1) // 2

    def set_root(self, value):
        self.tree[0] = value

    def set_left(self, value, parent):
        if self.tree[parent] is None:
            raise ValueError("no parent at index {}".format(parent))
        self.tree[self.left_of(parent)] = value

    def set_right(self, value, parent):
        if self.tree[parent] is None:
            raise ValueError("no parent at index {}".format(parent))
        self.tree[self.right_of(parent)] = value

    def __str__(self):
        return "".join(str(v) if v is not None else "-" for v in self.tree)


if __name__ == "__main__":
    #      1
    #    /   \
    #   2     3
    #  / \     \
    # 4   5     6
    root = TreeNode(1,
                    TreeNode(2, TreeNode(4), TreeNode(5)),
                    TreeNode(3, None, TreeNode(6)))

    assert preorder(root) == [1, 2, 4, 5, 3, 6]
    assert inorder(root) == [4, 2, 5, 1, 3, 6]
    assert postorder(root) == [4, 5, 2, 6, 3, 1]
    assert level_order(root) == [1, 2, 3, 4, 5, 6]

    # the iterative walk must agree with the recursive one
    assert inorder_iterative(root) == inorder(root)

    assert height(root) == 2
    assert size(root) == 6

    assert preorder(None) == [] and level_order(None) == [] and height(None) == -1

    #      A
    #    /   \
    #   B     C
    #  / \     \
    # D   E     F
    arr = ArrayBinaryTree(10)
    arr.set_root("A")
    arr.set_left("B", 0)
    arr.set_right("C", 0)
    arr.set_left("D", 1)
    arr.set_right("E", 1)
    arr.set_right("F", 2)
    assert str(arr) == "ABCDE-F---"

    # index arithmetic: B is at 1, so its children are at 3 and 4
    assert arr.tree[ArrayBinaryTree.left_of(1)] == "D"
    assert arr.tree[ArrayBinaryTree.right_of(1)] == "E"
    assert arr.tree[ArrayBinaryTree.parent_of(4)] == "B"

    print("Success.")
