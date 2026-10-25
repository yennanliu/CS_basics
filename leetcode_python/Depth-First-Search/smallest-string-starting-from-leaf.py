"""

988. Smallest String Starting From Leaf
Medium

You are given the root of a binary tree where each node has a value in the
range [0, 25] representing the letters 'a' to 'z'.

Return the lexicographically smallest string that starts at a leaf of this
tree and ends at the root.

As a reminder, any shorter prefix of a string is lexicographically smaller.

For example, "ab" is lexicographically smaller than "aba".
A leaf of a node is a node that has no children.

Example 1:

Input: root = [0,1,2,3,4,3,4]

Output: "dba"

Example 2:

Input: root = [25,1,3,1,3,0,2]

Output: "adz"

Example 3:

Input: root = [2,2,1,null,1,0,null,0]

Output: "abc"

Constraints:

The number of nodes in the tree is in the range [1, 8500].
0 <= Node.val <= 25

"""

# V0
# IDEA : DFS (PRE-ORDER) + BACKTRACK, COMPARE AT EACH LEAF
#
#   keep the root -> current node path as a list of chars. at a leaf,
#   reverse it to get the leaf -> root string and keep the smaller one.
#   the path list is shared, so pop the char on the way back (backtrack).
#
#   NOTE : the tempting "min(left answer, right answer) + my char" is WRONG,
#          when one child answer is a prefix of the other, the chars ABOVE decide:
#          root z, left subtree gives "ab", right subtree gives "aba"
#          -> greedy keeps "ab" -> "abz", but "abaz" < "abz"
#          -> so compare FULL leaf -> root strings, only at the leaves.
#
#   e.g. [0,1,2,3,4,3,4] -> leaves give "dba","eba","dca","eca" -> "dba"
#
#   NOTE : recursion depth = tree height; a skewed 8500-node tree exceeds
#          python's default limit (1000) -> see V1 for the iterative version
#
# time = O(n * h), space = O(h)    (h = tree height, building one string per leaf)

# Definition for a binary tree node.
# class TreeNode(object):
#     def __init__(self, val=0, left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right


class Solution(object):
    def smallestFromLeaf(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: str
        """
        # edge case: empty tree has no leaf -> no string
        if not root:
            return ""

        # chars on the root -> current node path
        self.path = []
        # best leaf -> root string seen so far (None = no leaf reached yet)
        self.smallest = None

        self.dfs(root)
        return self.smallest

    def dfs(self, node):
        """Visit node, updating self.smallest at every leaf."""
        if not node:
            return

        # value 0..25 -> 'a'..'z'
        self.path.append(chr(ord('a') + node.val))

        # NOTE !!! only a LEAF ends a valid string (a node with one child does not)
        if not node.left and not node.right:
            # the path is root -> leaf, the answer is read leaf -> root
            candidate = "".join(reversed(self.path))
            if self.smallest is None or candidate < self.smallest:
                self.smallest = candidate

        self.dfs(node.left)
        self.dfs(node.right)

        # backtrack: remove this node's char before returning to the parent
        self.path.pop()


# V1
# IDEA : ITERATIVE DFS WITH AN EXPLICIT STACK (each entry carries its leaf -> root string)
#
#   same comparison as V0, but no recursion: a skewed tree of 8500 nodes is
#   deeper than python's default recursion limit (1000), so this is the
#   version that is safe on every input.
#   each stack entry holds (node, string from this node up to the root),
#   so a child's string is just its own char + the parent's string.
#
# time = O(n * h), space = O(n * h)    (every stack entry owns its string)
class Solution2(object):
    def smallestFromLeaf(self, root):
        """
        :type root: Optional[TreeNode]
        :rtype: str
        """
        if not root:
            return ""

        smallest = None
        stack = [(root, chr(ord('a') + root.val))]

        while stack:
            node, suffix = stack.pop()

            # leaf: suffix already reads leaf -> root
            if not node.left and not node.right:
                if smallest is None or suffix < smallest:
                    smallest = suffix
                continue

            # prepend the child's char, since the child sits further from the root
            if node.left:
                stack.append((node.left, chr(ord('a') + node.left.val) + suffix))
            if node.right:
                stack.append((node.right, chr(ord('a') + node.right.val) + suffix))

        return smallest
