"""

1541. Minimum Insertions to Balance a Parentheses String
Medium

Given a parentheses string s containing only the characters '(' and ')'. A parentheses string is balanced if:

Any left parenthesis '(' must have a corresponding two consecutive right parenthesis '))'.
Left parenthesis '(' must go before the corresponding two consecutive right parenthesis '))'.

In other words, we treat '(' as an opening parenthesis and '))' as a closing parenthesis.

For example, "())", "())(())))" and "(())())))" are balanced, ")()", "()))" and "(()))" are not balanced.

You can insert the characters '(' and ')' at any position of the string to balance it if needed.

Return the minimum number of insertions needed to make s balanced.


Example 1:

Input: s = "(()))"
Output: 1
Explanation: The second '(' has two matching '))', but the first '(' has only ')' matching. We need to add one more ')' at the end of the string to be "(())))" which is balanced.

Example 2:

Input: s = "())"
Output: 0
Explanation: The string is already balanced.

Example 3:

Input: s = "))())("
Output: 3
Explanation: Add '(' to match the first '))', Add '))' to match the last '('.


Constraints:

1 <= s.length <= 10^5
s consists of '(' and ')' only.

"""

# V0
# IDEA : GREEDY COUNTER (a "close" is the 2-char token '))')
#
#   scan left to right keeping `need` = number of '(' still waiting for
#   their '))'. no real stack is required, only its size.
#
#   on '(' : need += 1
#   on ')' : we are starting a closing token.
#            - if the NEXT char is also ')' consume both;
#              otherwise the token is half-missing -> insert 1 ')'.
#            - now the token is complete : if need > 0 it pays off one '(',
#              otherwise there is nothing to close -> insert 1 '('.
#
#   at the end every leftover '(' still needs a full '))' -> 2 each.
#
# time = O(n), space = O(1)
class Solution(object):
    def minInsertions(self, s):
        n = len(s)
        res = 0
        need = 0
        i = 0
        while i < n:
            if s[i] == '(':
                need += 1
                i += 1
            else:
                if i + 1 < n and s[i + 1] == ')':
                    i += 2
                else:
                    res += 1      # insert the missing second ')'
                    i += 1
                if need > 0:
                    need -= 1
                else:
                    res += 1      # insert a '(' for this orphan '))'
        return res + need * 2
