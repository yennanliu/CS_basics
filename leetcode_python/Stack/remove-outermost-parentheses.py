"""

1021. Remove Outermost Parentheses
Easy

A valid parentheses string is either empty "", "(" + A + ")", or A + B, where A and B are valid parentheses strings, and + represents string concatenation.

For example, "", "()", "(())()", and "(()(()))" are all valid parentheses strings.

A valid parentheses string s is primitive if it is nonempty, and there does not exist a way to split it into s = A + B, with A and B nonempty valid parentheses strings.

Given a valid parentheses string s, consider its primitive decomposition: s = P1 + P2 + ... + Pk, where Pi are primitive valid parentheses strings.

Return s after removing the outermost parentheses of every primitive string in the primitive decomposition of s.


Example 1:

Input: s = "(()())(())"
Output: "()()()"
Explanation:
The input string is "(()())(())", with primitive decomposition "(()())" + "(())".
After removing outer parentheses of each part, this is "()()" + "()" = "()()()".

Example 2:

Input: s = "(()())(())(()(()))"
Output: "()()()()(())"
Explanation:
The input string is "(()())(())(()(()))", with primitive decomposition "(()())" + "(())" + "(()(()))".
After removing outer parentheses of each part, this is "()()" + "()" + "()(())" = "()()()()(())".

Example 3:

Input: s = "()()"
Output: ""
Explanation:
The input string is "()()", with primitive decomposition "()" + "()".
After removing outer parentheses of each part, this is "" + "" = "".


Constraints:

1 <= s.length <= 10^5
s[i] is either '(' or ')'.
s is a valid parentheses string.

"""

# V0
# IDEA : DEPTH COUNTER (a stack of size 1)
#
#   the outermost '(' of a primitive is the one that takes depth 0 -> 1,
#   and its matching ')' is the one that takes depth 1 -> 0.
#   so : keep a running depth, and SKIP exactly those two.
#
#     '(' : keep it only if depth > 0, then depth += 1
#     ')' : depth -= 1, then keep it only if depth > 0
#
# time = O(n)
# space = O(n), for the output
class Solution(object):
    def removeOuterParentheses(self, s):
        res = []
        depth = 0
        for ch in s:
            if ch == '(':
                if depth > 0:
                    res.append(ch)
                depth += 1
            else:
                depth -= 1
                if depth > 0:
                    res.append(ch)
        return ''.join(res)
