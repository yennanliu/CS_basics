"""

1612. Check If Two Expression Trees are Equivalent
Medium

A binary expression tree is a kind of binary tree used to represent arithmetic expressions. Each node of a binary expression tree has either zero or two children. Leaf nodes (nodes with 0 children) correspond to operands (variables), and internal nodes (nodes with two children) correspond to the operators. In this problem, we only consider the '+' operator (i.e. addition).

You are given the roots of two binary expression trees, root1 and root2. Return true if the two binary expression trees are equivalent. Otherwise, return false.

Two binary expression trees are equivalent if they evaluate to the same value regardless of what the variables are set to.


Example 1:

Input: root1 = [x], root2 = [x]
Output: true

Example 2:

Input: root1 = [+,a,+,null,null,b,c], root2 = [+,+,a,b,c]
Output: true
Explanation: a + (b + c) == (b + c) + a

Example 3:

Input: root1 = [+,a,+,null,null,b,c], root2 = [+,+,a,b,d]
Output: false
Explanation: a + (b + c) != (b + d) + a


Constraints:

The number of nodes in both trees are equal, odd and, in the range [1, 4999].
Node.val is '+' or a lower-case English letter.
It's guaranteed that the tree given is a valid binary expression tree.


Follow up: What will you change in your solution if the tree also supports the '-' operator (i.e. subtraction)?

"""

# V0
# IDEA : COUNT LEAVES (addition is commutative + associative -> only the
#        multiset of operands matters)
#
#   with '+' as the only operator, any tree evaluates to
#     sum over variables of (count of that leaf) * value
#   so two trees are equivalent iff every variable appears the same number
#   of times in both.
#
#   trick : walk tree1 adding +1 per leaf and tree2 adding -1 per leaf into
#   the SAME counter; equivalent iff every entry ends at 0.
#
#   NOTE : follow-up with '-' -> keep the same counter but flip the sign
#          when descending into the right child of a '-' node.
#
# time = O(n1 + n2), space = O(26) counters + O(h) stack
from collections import Counter
# Definition for a binary tree node.
# class Node(object):
#     def __init__(self, val=" ", left=None, right=None):
#         self.val = val
#         self.left = left
#         self.right = right

class Solution(object):
    def checkEquivalence(self, root1, root2):
        cnt = Counter()

        def walk(root, sign):
            stack = [root]
            while stack:
                node = stack.pop()
                if node is None:
                    continue
                if node.val != '+':
                    cnt[node.val] += sign
                stack.append(node.left)
                stack.append(node.right)

        walk(root1, 1)
        walk(root2, -1)
        return all(v == 0 for v in cnt.values())
