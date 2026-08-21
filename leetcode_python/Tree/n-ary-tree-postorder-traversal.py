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

# V2
# IDEA : ITERATIVE with TWO STACKS (the classic "textbook" postorder)
#
#  stack_1 : the nodes still TO PROCESS
#  stack_2 : the nodes ALREADY processed, in REVERSE postorder
#
#  -> pop from stack_1, push onto stack_2, push its children onto stack_1.
#  -> at the end, popping stack_2 empty gives the postorder directly.
#
#  This is the SAME trick as V1' ("reverse preorder"), but instead of calling
#  res[::-1] at the end, stack_2 does the reversing FOR us (LIFO).
#
#  Trace on root = [1,null,3,2,4,null,5,6] :
#
#     stack_1      pop    stack_2 (bottom -> top)
#     [1]          1      [1]
#     [3,2,4]      4      [1,4]
#     [3,2]        2      [1,4,2]
#     [3]          3      [1,4,2,3]
#     [5,6]        6      [1,4,2,3,6]
#     [5]          5      [1,4,2,3,6,5]
#     []                  -> pop all -> [5,6,3,2,4,1]  ✅
#
# time = O(n)
# space = O(n)
class Solution(object):
    def postorder(self, root):
        if not root:
            return []

        stack_1 = [root]
        stack_2 = []

        while stack_1:
            node = stack_1.pop()
            # NOTE !!! we do NOT record the val yet, we park the node in stack_2
            stack_2.append(node)
            self.helper(node, stack_1)

        # drain stack_2 (LIFO) -> this is the reversing step
        res = []
        while stack_2:
            res.append(stack_2.pop().val)

        return res

    """
    NOTE !!!

    children go to stack_1 in NORMAL order (left -> right),
    so the RIGHTMOST child lands on stack_2 FIRST,
    which means it comes out of stack_2 LAST -> correct left..right order.
    """
    def helper(self, node, stack_1):
        for child in (node.children or []):
            stack_1.append(child)


# V2'
# IDEA : ITERATIVE with a STACK + DEQUE.appendleft() (no reversing at all)
#
#  Same traversal as V1' (node -> right..left), but instead of appending to a
#  list and reversing at the end, we push each val to the FRONT of a deque.
#
#      appendleft(1) -> [1]
#      appendleft(4) -> [4,1]
#      appendleft(2) -> [2,4,1]
#      appendleft(3) -> [3,2,4,1]
#      appendleft(6) -> [6,3,2,4,1]
#      appendleft(5) -> [5,6,3,2,4,1]   ✅
#
#  NOTE : deque.appendleft is O(1), while list.insert(0, x) would be O(n)
#         -> do NOT use a plain list here, it would make the whole thing O(n^2)
#
# time = O(n)
# space = O(n)
from collections import deque

class Solution(object):
    def postorder(self, root):
        if not root:
            return []

        res = deque()
        stack = [root]

        while stack:
            node = stack.pop()
            # NOTE !!! appendleft (NOT append) -> builds the answer back to front
            res.appendleft(node.val)
            self.helper(node, stack)

        return list(res)

    def helper(self, node, stack):
        # normal order push -> rightmost child popped first
        for child in (node.children or []):
            stack.append(child)


# V2''
# IDEA : ITERATIVE, SIMULATE the RECURSION with a CHILD-INDEX stack
#
#  This is the most "faithful" iterative version : it does EXACTLY what the
#  recursion in V0' does, with the call stack written out by hand.
#
#  each stack frame = [node, idx]
#     `idx` = which child of `node` we should visit NEXT
#
#   - idx < len(children) -> "recurse" into that child (idx += 1 first, so when
#                            we come back we continue with the NEXT child)
#   - idx == len(children) -> ALL children are done -> record node.val, pop frame
#
#  -> no reversing, no visited flag, no second stack. Just the call stack.
#
# time = O(n)
# space = O(h)  # h = tree height (only ONE frame per level, like real recursion)
class Solution(object):
    def postorder(self, root):
        if not root:
            return []

        res = []
        # frame = [node, next_child_idx]
        stack = [[root, 0]]

        while stack:
            self.helper(stack, res)

        return res

    """
    NOTE !!!

    handle ONE step of the top frame :

      - still has children left -> push the next child as a NEW frame
                                   (this is the "recursive call")
      - no children left        -> record node.val and pop
                                   (this is the "return" of the recursion)
    """
    def helper(self, stack, res):
        node, idx = stack[-1]
        children = node.children or []

        if idx < len(children):
            # advance MY pointer first, so I resume at the next child later
            stack[-1][1] += 1
            # "recursive call" on children[idx]
            stack.append([children[idx], 0])
        else:
            # all children done -> now it is my turn (postorder !)
            res.append(node.val)
            stack.pop()


# V3
# IDEA : RECURSIVE GENERATOR helper (`yield from`) - the python-idiomatic way
#
#  The helper YIELDS the vals one by one instead of building a list, so the
#  postorder definition reads almost like plain english :
#
#      for each child: yield everything from that child
#      then:           yield myself
#
#  -> useful when the caller only needs to ITERATE (e.g. `for v in ...`) or
#     only wants the first k values, since nothing is materialised until asked.
#
# time = O(n)
# space = O(h)  # h = tree height (generator frames), + O(n) for the final list()
class Solution(object):
    def postorder(self, root):
        return list(self.helper(root))

    def helper(self, node):
        # base case : empty subtree -> yields nothing
        if not node:
            return

        # step 1) delegate to each child's generator (left -> right)
        for child in (node.children or []):
            # NOTE !!! `yield from` = "re-yield everything this sub-generator produces"
            yield from self.helper(child)

        # step 2) then myself, LAST
        yield node.val
