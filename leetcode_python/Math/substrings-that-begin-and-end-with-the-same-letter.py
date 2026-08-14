"""

2083. Substrings That Begin and End With the Same Letter
Medium
(premium / locked problem)

You are given a 0-indexed string s consisting of only lowercase English letters. Return the number of substrings in s that begin and end with the same character.

A substring is a contiguous non-empty sequence of characters within a string.


Example 1:

Input: s = "abcba"
Output: 7
Explanation:
The substrings of length 1 that start and end with the same letter are: "a", "b", "c", "b", and "a".
The substring of length 3 that starts and ends with the same letter is: "bcb".
The substring of length 5 that starts and ends with the same letter is: "abcba".

Example 2:

Input: s = "abacad"
Output: 9
Explanation:
The substrings of length 1 that start and end with the same letter are: "a", "b", "a", "c", "a", and "d".
The substrings of length 3 that start and end with the same letter are: "aba" and "aca".
The substring of length 5 that starts and ends with the same letter is: "abaca".

Example 3:

Input: s = "a"
Output: 1
Explanation:
The substring of length 1 that starts and ends with the same letter is: "a".


Constraints:

1 <= s.length <= 10^5
s consists only of lowercase English letters.

"""

# V0
# IDEA : COMBINATORICS PER LETTER — CHOOSE 2 POSITIONS (plus the singletons)
#
#   a valid substring is determined by picking a start and an end position
#   holding the SAME letter (start <= end). for a letter occurring c times
#   that is
#       C(c, 2) + c  =  c * (c + 1) / 2
#
#   summing over the 26 letters gives the answer. equivalently, sweep the
#   string keeping a running count per letter and add it after incrementing —
#   the version below does the closed form, which is one pass either way.
#
# time = O(n), space = O(26)
from collections import Counter


class Solution(object):
    def numberOfSubstrings(self, s):
        return sum(c * (c + 1) // 2 for c in Counter(s).values())
