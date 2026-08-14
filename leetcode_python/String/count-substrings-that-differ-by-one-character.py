"""

1638. Count Substrings That Differ by One Character
Medium

Given two strings s and t, find the number of ways you can choose a non-empty substring of s and replace a single character by a different character such that the resulting substring is a substring of t. In other words, find the number of substrings in s that differ from some substring in t by exactly one character.

For example, the underlined substrings in "computer" and "computation" only differ by the 'e'/'a', so this is a valid way.

Return the number of substrings that satisfy the condition above.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s = "aba", t = "baba"
Output: 6
Explanation: The following are the pairs of substrings from s and t that differ by exactly 1 character:
("aba", "baba")
("aba", "baba")
("aba", "baba")
("aba", "baba")
("aba", "baba")
("aba", "baba")
The underlined portions are the substrings that are chosen from s and t.

Example 2:

Input: s = "ab", t = "bb"
Output: 3
Explanation: The following are the pairs of substrings from s and t that differ by 1 character:
("ab", "bb")
("ab", "bb")
("ab", "bb")
The underlined portions are the substrings that are chosen from s and t.


Constraints:

1 <= s.length, t.length <= 100
s and t consist of lowercase English letters only.

"""

# V0
# IDEA : PIVOT ON THE MISMATCH, then expand both ways
#
#   every valid pair has exactly ONE differing position. So enumerate that
#   position: a pair (i, j) with s[i] != t[j] is the mismatch. Around it:
#     L = how far s[i-1..] and t[j-1..] stay equal going left
#     R = how far s[i+1..] and t[j+1..] stay equal going right
#   any of the (L+1) left extents combines with any of the (R+1) right
#   extents, so this mismatch contributes (L+1) * (R+1) substring pairs.
#
#   NOTE : the counts are pairs of (substring of s, substring of t), which
#          is exactly what the problem asks -- identical substrings picked
#          at different offsets count separately.
#
# time = O(m * n * min(m, n)), space = O(1) ; m, n <= 100
class Solution(object):
    def countSubstrings(self, s, t):
        m, n = len(s), len(t)
        res = 0
        for i in range(m):
            for j in range(n):
                if s[i] == t[j]:
                    continue
                left = 0
                while i - left - 1 >= 0 and j - left - 1 >= 0 and \
                        s[i - left - 1] == t[j - left - 1]:
                    left += 1
                right = 0
                while i + right + 1 < m and j + right + 1 < n and \
                        s[i + right + 1] == t[j + right + 1]:
                    right += 1
                res += (left + 1) * (right + 1)
        return res
