"""

1249. Minimum Remove to Make Valid Parentheses
Medium

Given a string s of '(' , ')' and lowercase English characters.

Your task is to remove the minimum number of parentheses ( '(' or ')', in any positions ) so that the resulting parentheses string is valid and return any valid string.

Formally, a parentheses string is valid if and only if:

It is the empty string, contains only lowercase characters, or
It can be written as AB (A concatenated with B), where A and B are valid strings, or
It can be written as (A), where A is a valid string.


Example 1:

Input: s = "lee(t(c)o)de)"
Output: "lee(t(c)o)de"
Explanation: "lee(t(co)de)" , "lee(t(c)ode)" would also be accepted.

Example 2:

Input: s = "a)b(c)d"
Output: "ab(c)d"

Example 3:

Input: s = "))(("
Output: ""
Explanation: An empty string is also valid.


Constraints:

1 <= s.length <= 10^5
s[i] is either '(' , ')', or lowercase English letter.

"""

# V0
# IDEA : GREEDY, TWO PASSES
"""
 Pass 1 (left -> right) : drop every ')' that has no unmatched '(' before it
 Pass 2 (right -> left) : drop every '(' that has no unmatched ')' after it

 Whatever survives both passes is balanced, and we removed
 only parentheses that COULD NOT be matched -> minimum removal.
"""
# time = O(n)
# space = O(n)
class Solution(object):
    def minRemoveToMakeValid(self, s):
        # pass 1 : remove unmatched ')'
        tmp = []
        open_cnt = 0
        for c in s:
            if c == ')':
                if open_cnt == 0:
                    continue
                open_cnt -= 1
            elif c == '(':
                open_cnt += 1
            tmp.append(c)

        # pass 2 : remove unmatched '(' (scan backward)
        res = []
        close_cnt = 0
        for c in reversed(tmp):
            if c == '(':
                if close_cnt == 0:
                    continue
                close_cnt -= 1
            elif c == ')':
                close_cnt += 1
            res.append(c)

        return ''.join(reversed(res))


# V1
# IDEA : STACK OF INDEXES (mark the bad positions, then rebuild)
# time = O(n)
# space = O(n)
class Solution(object):
    def minRemoveToMakeValid(self, s):
        stack = []          # indexes of unmatched '('
        remove = set()      # indexes to drop
        for i, c in enumerate(s):
            if c == '(':
                stack.append(i)
            elif c == ')':
                if stack:
                    stack.pop()
                else:
                    remove.add(i)
        remove.update(stack)
        return ''.join(c for i, c in enumerate(s) if i not in remove)
