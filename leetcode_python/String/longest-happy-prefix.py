"""

1392. Longest Happy Prefix
Hard

A string is called a happy prefix if is a non-empty prefix which is also a suffix (excluding itself).

Given a string s, return the longest happy prefix of s. Return an empty string "" if no such prefix exists.


Example 1:

Input: s = "level"
Output: "l"
Explanation: s contains 4 prefix excluding itself ("l", "le", "lev", "leve"), and suffix ("l", "el", "vel", "evel"). The largest prefix which is also suffix is given by "l".

Example 2:

Input: s = "ababab"
Output: "abab"
Explanation: "abab" is the largest prefix which is also suffix. They can overlap in the original string.


Constraints:

1 <= s.length <= 10^5
s contains only lowercase English letters.

"""

# V0
# IDEA : KMP FAILURE FUNCTION (longest proper prefix that is also a suffix)
#
#   lps[i] = length of the longest proper prefix of s[:i+1] that is also
#            a suffix of s[:i+1].
#   that is exactly the definition of a "happy prefix", so the answer for
#   the whole string is s[:lps[n-1]].
#   build lps in one left-to-right pass: keep j = current matched length,
#   on mismatch fall back to lps[j-1] (the next best border) instead of
#   restarting from 0.
#   NOTE : "proper" matters -> j never reaches n because we compare s[i]
#          against s[j] with j < i, so the full string is never returned.
#
# time = O(n), space = O(n)
class Solution(object):
    def longestPrefix(self, s):
        n = len(s)
        lps = [0] * n
        j = 0
        for i in range(1, n):
            while j > 0 and s[i] != s[j]:
                j = lps[j - 1]
            if s[i] == s[j]:
                j += 1
            lps[i] = j
        return s[: lps[n - 1]]
