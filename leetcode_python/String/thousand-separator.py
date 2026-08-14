"""

1556. Thousand Separator
Easy

Given an integer n, add a dot (".") as the thousands separator and return it in string format.

Example 1:

Input: n = 987
Output: "987"

Example 2:

Input: n = 1234
Output: "1.234"

Constraints:

0 <= n <= 2^31 - 1

"""

# V0
# IDEA : STRING (chop the decimal form into 3-digit groups from the right)
#
#   walk from the tail of str(n) taking 3 chars at a time, the leftover
#   head (1..3 chars) is the first group, then join with ".".
#
# time = O(d), space = O(d), d = number of digits
class Solution(object):
    def thousandSeparator(self, n):
        s = str(n)
        groups = []
        i = len(s)
        while i > 3:
            groups.append(s[i - 3:i])
            i -= 3
        groups.append(s[:i])
        groups.reverse()
        return '.'.join(groups)
