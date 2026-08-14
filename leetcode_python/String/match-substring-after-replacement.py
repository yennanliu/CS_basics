"""

2301. Match Substring After Replacement
Hard

You are given two strings s and sub. You are also given a 2D character array mappings where mappings[i] = [oldi, newi] indicates that you may perform the following operation any number of times:

Replace a character oldi of sub with newi.

Each character in sub cannot be replaced more than once.

Return true if it is possible to make sub a substring of s by replacing zero or more characters according to mappings. Otherwise, return false.

A substring is a contiguous non-empty sequence of characters within a string.


Example 1:

Input: s = "fool3e7bar", sub = "leet", mappings = [["e","3"],["t","7"],["t","8"]]
Output: true
Explanation: Replace the first 'e' in sub with '3' and 't' in sub with '7'.
Now sub = "l3e7" is a substring of s, so we return true.

Example 2:

Input: s = "fooleetbar", sub = "f00l", mappings = [["o","0"]]
Output: false
Explanation: The string "f00l" is not a substring of s and no replacements can be made.
Note that we cannot replace '0' with 'o'.

Example 3:

Input: s = "Fool33tbaR", sub = "leetd", mappings = [["e","3"],["t","7"],["t","8"],["d","b"],["p","b"]]
Output: true
Explanation: Replace the first and second 'e' in sub with '3' and 'd' in sub with 'b'.
Now sub = "l33tb" is a substring of s, so we return true.


Constraints:

1 <= sub.length <= s.length <= 5000
0 <= mappings.length <= 1000
mappings[i].length == 2
oldi != newi
s and sub consist of uppercase and lowercase English letters and digits.
mappings[i][0] and mappings[i][1] are either uppercase or lowercase English letters or digits.

"""

# V0
# IDEA : PRECOMPUTE "WHAT CAN EACH sub CHARACTER BECOME", THEN SLIDE
#
#   "each character cannot be replaced more than once" means the positions of
#   sub are INDEPENDENT — position j either stays as sub[j] or becomes one of
#   its mapped targets. so build
#       allowed[c] = { c } | { new : (c, new) in mappings }
#   and a window of s matches iff every position satisfies
#       s[i + j] in allowed[sub[j]]
#
#   the sizes (5000 x 5000) make the direct sliding comparison ~2.5 * 10^7
#   set lookups, which is fine — and no KMP variant applies cleanly here
#   because the "equality" relation is not transitive.
#
# time = O(len(s) * len(sub)), space = O(len(mappings))
from collections import defaultdict


class Solution(object):
    def matchReplacement(self, s, sub, mappings):
        allowed = defaultdict(set)
        for old, new in mappings:
            allowed[old].add(new)

        n, m = len(s), len(sub)
        for i in range(n - m + 1):
            ok = True
            for j in range(m):
                a, b = sub[j], s[i + j]
                if a != b and b not in allowed[a]:
                    ok = False
                    break
            if ok:
                return True
        return False
