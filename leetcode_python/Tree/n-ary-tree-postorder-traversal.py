"""

590. N-ary Tree Postorder Traversal
Medium

Given the root of an n-ary tree, return the postorder traversal of its nodes' values.

Nary-Tree input serialization is represented in their level order traversal.
Each group of children is separated by the null value (See examples)

Example 1:

Input: root = [1,null,3,2,4,null,5,6]
Output: [5,6,3,2,4,1]

Example 2:

Input: root = [1,null,2,3,4,5,null,null,6,7,null,8,null,9,10,null,null,11,null,12,null,13,null,null,14]
Output: [2,6,14,11,7,3,12,8,4,13,9,10,5,1]


Constraints:

The number of nodes in the tree is in the range [0, 10^4].
0 <= Node.val <= 10^4
The height of the n-ary tree is less than or equal to 1000.


Follow up: Recursive solution is trivial, could you do it iteratively?

"""

# Definition for a Node.
# class Node(object):
#     def __init__(self, val=None, children=None):
#         self.val = val
#         self.children = children

# V0
# IDEA : DFS (recursion) -> visit all children left to right, then the node itself
# time = O(n)
# space = O(h)  # h = tree height (recursion stack)
class Solution(object):
    def postorder(self, root):
        res = []

        def dfs(node):
            if not node:
                return
            for child in (node.children or []):
                dfs(child)
            res.append(node.val)

        dfs(root)
        return res

# V1
# IDEA : ITERATIVE with a STACK (answers the follow up)
#        -> do a "root - right ... left" traversal (children pushed in normal order),
#           then REVERSE it, which gives "left ... right - root" = postorder
# time = O(n)
# space = O(n)
class Solution(object):
    def postorder(self, root):
        if not root:
            return []

        res = []
        stack = [root]
        while stack:
            node = stack.pop()
            res.append(node.val)
            # normal order push -> last child is popped (visited) first
            for child in (node.children or []):
                stack.append(child)

        return res[::-1]

# V0'
# IDEA : DFS (recursion) + HELPER FUNC (easiest to read)
#
#  Postorder for a n-ary tree = "ALL children first (left -> right), then myself"
#
#  Example : root = [1,null,3,2,4,null,5,6]
#
#            1
#          / | \
#         3  2  4
#        / \
#       5   6
#
#   -> visit 3's children (5, 6) -> visit 3
#   -> visit 2 (no children)
#   -> visit 4 (no children)
#   -> visit 1 (the root, LAST)
#   -> [5, 6, 3, 2, 4, 1]
#
# time = O(n)   # every node is visited exactly once
# space = O(h)  # h = tree height (recursion stack)
class Solution(object):
    def postorder(self, root):
        res = []
        self.helper(root, res)
        return res

    """
    NOTE !!!

    the helper does NOT return anything,
    it just APPENDS to the `res` list that is passed in.

    the ONLY thing that makes it "post" order is the ORDER of the 2 steps below:

        step 1) recursive call on children
        step 2) append node.val      <--- AFTER the children

    (if you swap step 1 and step 2, you get PREorder (LC 589) instead)
    """
    def helper(self, node, res):
        # base case : nothing to visit
        if not node:
            return

        # step 1) children first (left -> right)
        for child in (node.children or []):
            self.helper(child, res)

        # step 2) then the node itself
        res.append(node.val)


# V0''
# IDEA : DFS (recursion) + HELPER FUNC that RETURNS a list (no shared state)
#
#  Same idea as V0', but instead of appending into a shared `res`,
#  the helper RETURNS the postorder list of the subtree it is given.
#
#      postorder(node) = postorder(child_1) + ... + postorder(child_k) + [node.val]
#
#  -> easier to reason about (a pure func : same input -> same output),
#     but it creates intermediate lists, so it uses more space.
#
# time = O(n)
# space = O(n)  # the returned lists
class Solution(object):
    def postorder(self, root):
        return self.helper(root)

    def helper(self, node):
        # base case : empty subtree -> empty list
        if not node:
            return []

        res = []

        # collect ALL children's postorder result first
        for child in (node.children or []):
            res += self.helper(child)

        # then put MYSELF at the END
        res.append(node.val)

        return res


# V1'
# IDEA : ITERATIVE with a STACK + HELPER FUNC ("reverse preorder" trick)
#
#  Postorder      : child_1 ... child_k, node
#  Reverse of it  : node, child_k ... child_1     <--- a "root first" traversal !
#
#  So : do an EASY traversal (pop node -> record it -> push children in normal
#       order, so the LAST child is popped first), then REVERSE the result.
#
#  Trace on root = [1,null,3,2,4,null,5,6] :
#
#     stack        pop   res
#     [1]          1     [1]
#     [3,2,4]      4     [1,4]
#     [3,2]        2     [1,4,2]
#     [3]          3     [1,4,2,3]
#     [5,6]        6     [1,4,2,3,6]
#     [5]          5     [1,4,2,3,6,5]
#     []                 -> reverse -> [5,6,3,2,4,1]  ✅
#
# time = O(n)
# space = O(n)
class Solution(object):
    def postorder(self, root):
        if not root:
            return []

        res = []
        stack = [root]

        while stack:
            node = stack.pop()
            res.append(node.val)
            self.helper(node, stack)

        # NOTE !!! reverse at the end -> this is what turns it into postorder
        return res[::-1]

    """
    NOTE !!!

    push children in NORMAL order (left -> right),
    so the RIGHTMOST child is on TOP of the stack, and is popped FIRST.

    (compare with LC 589 preorder, which pushes children in REVERSED order)
    """
    def helper(self, node, stack):
        for child in (node.children or []):
            stack.append(child)


# V1''
# IDEA : ITERATIVE with a STACK, NO reversing ("visited" flag)
#
#  This is the "real" postorder simulation : each node is pushed TWICE.
#
#   - 1st time we meet a node -> push it BACK as `visited=True`,
#                                then push all its children (reversed order)
#   - 2nd time we meet it (visited=True) -> now all its children are done,
#                                so it is safe to record its val
#
#  -> `res` is built in the correct order directly (no res[::-1] at the end)
#
# time = O(n)
# space = O(n)
class Solution(object):
    def postorder(self, root):
        if not root:
            return []

        res = []
        # each stack item = (node, visited)
        stack = [(root, False)]

        while stack:
            node, visited = stack.pop()

            if visited:
                # all children already handled -> record myself
                res.append(node.val)
            else:
                self.helper(node, stack)

        return res

    """
    NOTE !!!

    ORDER of the pushes matters (stack = LIFO, last pushed is popped first) :

      1) push (node, True)      -> popped LAST  -> node.val recorded LAST  ✅
      2) push children reversed -> leftmost child is popped FIRST          ✅
    """
    def helper(self, node, stack):
        # myself first (so it is processed AFTER all children)
        stack.append((node, True))

        # then children in REVERSED order -> leftmost child popped first
        for child in reversed(node.children or []):
            stack.append((child, False))
