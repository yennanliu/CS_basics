"""

1763. Longest Nice Substring
Easy

A string s is nice if, for every letter of the alphabet that s contains, it appears both in uppercase and lowercase. For example, "abABB" is nice because 'A' and 'a' appear, and 'B' and 'b' appear. However, "abA" is not because 'b' appears, but 'B' does not.

Given a string s, return the longest substring of s that is nice. If there are multiple, return the substring of the earliest occurrence. If there are none, return an empty string.

Example 1:

Input: s = "YazaAay"
Output: "aAa"
Explanation: "aAa" is a nice string because 'A/a' is the only letter of the alphabet in s, and both 'A' and 'a' appear.
"aAa" is the longest nice substring.

Example 2:

Input: s = "Bb"
Output: "Bb"
Explanation: "Bb" is a nice string because both 'B' and 'b' appear. The whole string is a substring.

Example 3:

Input: s = "c"
Output: ""
Explanation: There are no nice substrings.

Constraints:

1 <= s.length <= 100
s consists of uppercase and lowercase English letters.

"""

# V0
# IDEA : DIVIDE AND CONQUER ON A "BAD" CHARACTER
#
#   if some char c in s has no partner (c.swapcase() missing from s), then no
#   nice substring can contain c -> split s at that position and recurse on the
#   two sides, keeping the longer answer (left wins ties => earliest occurrence).
#   if every char has its partner, the whole string is already nice.
#   NOTE : ties must prefer the LEFT half, hence the strict ">" comparison.
#
# time = O(n^2) worst case, space = O(n)
class Solution(object):
    def longestNiceSubstring(self, s):
        if len(s) < 2:
            return ""
        seen = set(s)
        for i in range(len(s)):
            if s[i].swapcase() in seen:
                continue
            left = self.longestNiceSubstring(s[:i])
            right = self.longestNiceSubstring(s[i + 1:])
            return right if len(right) > len(left) else left
        return s
