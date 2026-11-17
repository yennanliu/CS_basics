"""

1614. Maximum Nesting Depth of the Parentheses
Easy

Given a valid parentheses string s, return the nesting depth of s. The
nesting depth is the maximum number of nested parentheses.


Example 1:

Input: s = "(1+(2*3)+((8)/4))+1"

Output: 3

Explanation:

Digit 8 is inside of 3 nested parentheses in the string.

Example 2:

Input: s = "(1)+((2))+(((3)))"

Output: 3

Explanation:

Digit 3 is inside of 3 nested parentheses in the string.

Example 3:

Input: s = "()(())((()()))"

Output: 3


Constraints:

1 <= s.length <= 100
s consists of digits 0-9 and characters '+', '-', '*', '/', '(', and ')'.
It is guaranteed that parentheses expression s is a VPS.

"""

# V0
# IDEA : COUNTER (A STACK THAT ONLY REMEMBERS ITS SIZE)
#
#   the depth at any position = number of '(' opened and not yet closed,
#   which is exactly the size a stack of '(' would have there. the input is
#   guaranteed valid, so we never need the stack's contents — only its size.
#   the answer is the largest size seen.
#
#   e.g. "(1)+((2))" -> depth 1,0,1,2,1,0 -> max = 2
#
# time = O(n), space = O(1)
class Solution(object):
    def maxDepth(self, s):
        """
        :type s: str
        :rtype: int
        """
        # number of '(' currently open
        depth = 0
        max_depth = 0

        for ch in s:
            if ch == '(':
                depth += 1
                # NOTE !!! update the max right after opening — that is the
                #          only moment depth can reach a new high
                max_depth = max(max_depth, depth)
            elif ch == ')':
                depth -= 1
            # digits and operators do not change the depth

        return max_depth


# V1
# IDEA : EXPLICIT STACK
#
#   same idea as V0, written with the stack the counter stands in for. worth
#   knowing because it is the shape that generalises: if the input could be
#   invalid, or had several bracket types, the stack contents are needed
#   (LC 20) and the counter alone is not enough.
#
# time = O(n), space = O(n)
class Solution2(object):
    def maxDepth(self, s):
        """
        :type s: str
        :rtype: int
        """
        # holds every '(' that has not been closed yet
        open_stack = []
        max_depth = 0

        for ch in s:
            if ch == '(':
                open_stack.append(ch)
                max_depth = max(max_depth, len(open_stack))
            elif ch == ')':
                # input is a valid parentheses string, so the stack is non-empty
                open_stack.pop()

        return max_depth
