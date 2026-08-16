"""

3407. Substring Matching Pattern
Easy

You are given a string s and a pattern string p, where p contains exactly one
'*' character.

The '*' in p can be replaced with any sequence of zero or more characters.

Return true if p can be made a substring of s, and false otherwise.

Example 1:

Input: s = "leetcode", p = "ee*e"

Output: true

Explanation:

By replacing the '*' with "tcod", the substring "eetcode" matches the pattern.

Example 2:

Input: s = "car", p = "c*v"

Output: false

Explanation:

There is no substring matching the pattern.

Example 3:

Input: s = "luck", p = "u*"

Output: true

Explanation:

The substrings "u", "uc", and "uck" match the pattern.

Constraints:

1 <= s.length <= 50
1 <= p.length <= 50
s contains only lowercase English letters.
p contains only lowercase English letters and exactly one '*'

"""

# V0
# IDEA : SPLIT AT THE STAR, ANCHOR THE PREFIX, THEN LOOK RIGHT
#
#   p is prefix + '*' + suffix, and a match is a *contiguous* window of s that
#   starts with prefix and ends with suffix, with anything in between.  so fix
#   the place where prefix sits, then the only remaining question is whether
#   suffix occurs anywhere at or after the end of that prefix — the '*' happily
#   absorbs whatever lies between them.
#
#   note the two halves may not overlap, which is why the search for suffix
#   starts at i + len(prefix) rather than at i.
#
# time = O(n^3) worst case on tiny inputs (n <= 50), space = O(n)
class Solution(object):
    def hasMatch(self, s, p):
        a, b = p.split('*')
        for i in range(len(s) + 1):
            if s.startswith(a, i) and b in s[i + len(a):]:
                return True
        return False
