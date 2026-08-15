"""

3303. Find the Occurrence of First Almost Equal Substring
Hard

You are given two strings s and pattern.

A string x is called almost equal to y if you can change at most one character in x to make it identical to y.

Return the smallest starting index of a substring in s that is almost equal to pattern. If no such index exists, return -1.

A substring is a contiguous non-empty sequence of characters within a string.


Example 1:

Input: s = "abcdefg", pattern = "bcdffg"
Output: 1
Explanation:
The substring s[1..6] == "bcdefg" can be converted to "bcdffg" by changing s[4] to "f".

Example 2:

Input: s = "ababbababa", pattern = "bacaba"
Output: 4
Explanation:
The substring s[4..9] == "bababa" can be converted to "bacaba" by changing s[6] to "c".

Example 3:

Input: s = "abcd", pattern = "dba"
Output: -1

Example 4:

Input: s = "dde", pattern = "d"
Output: 0


Constraints:

1 <= pattern.length < s.length <= 10^5
s and pattern consist only of lowercase English letters.

"""

# V0
# IDEA : MATCH THE PATTERN FROM BOTH ENDS — ONE MISMATCH MAY SIT IN THE MIDDLE
#
#   a window is almost equal when it agrees with the pattern everywhere
#   except at most one position. so if the pattern's first `p` characters
#   match at the window's start, and its last `q` characters match at the
#   window's end, the window works whenever
#
#       p + q >= m - 1
#
#   (the one uncovered position is the character we are allowed to change).
#
#   both p and q come from Z-arrays :
#       forward  : z over pattern + '#' + s
#       backward : the same over the two strings REVERSED
#   each linear, after which every start is an O(1) test and the first hit is
#   returned.
#
# time = O(n + m), space = O(n + m)
class Solution(object):
    def minStartingIndex(self, s, pattern):
        n, m = len(s), len(pattern)

        def z_array(t):
            k = len(t)
            z = [0] * k
            l = r = 0
            for i in range(1, k):
                if i < r:
                    z[i] = min(r - i, z[i - l])
                while i + z[i] < k and t[z[i]] == t[i + z[i]]:
                    z[i] += 1
                if i + z[i] > r:
                    l, r = i, i + z[i]
            return z

        fwd = z_array(pattern + '#' + s)          # prefix match at each start
        rev = z_array(pattern[::-1] + '#' + s[::-1])
        off = m + 1

        for start in range(n - m + 1):
            p = fwd[off + start]
            if p >= m:
                return start
            end = start + m - 1
            q = rev[off + (n - 1 - end)]          # suffix match at the window end
            if p + q >= m - 1:
                return start
        return -1
