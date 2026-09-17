"""

132. Palindrome Partitioning II
Hard

Given a string s, partition s such that every substring of the partition is a palindrome.

Return the minimum cuts needed for a palindrome partitioning of s.


Example 1:

Input: s = "aab"
Output: 1
Explanation: The palindrome partitioning ["aa","b"] could be produced using 1 cut.

Example 2:

Input: s = "a"
Output: 0

Example 3:

Input: s = "ab"
Output: 1


Constraints:

1 <= s.length <= 2000
s consists of lowercase English letters only.

"""

# V0
# IDEA : DP (palindrome table + min-cut per prefix, built in ONE pass)
#
#   cut[i] = min cuts for the prefix s[0..i]
#   pal[j][i] = is s[j..i] a palindrome
#
#   the two tables are filled together, which is the whole trick : while we
#   extend the right end i, every j <= i that closes a palindrome at i gives a
#   candidate  cut[i] = cut[j-1] + 1  (cut right before j).
#
#   e.g. s = "aab", i = 1 : j = 0 -> "aa" is a palindrome and j == 0, so the
#        whole prefix is already one palindrome -> cut[1] = 0.
#
#   NOTE !!! j == 0 means "no cut at all", not cut[-1] + 1 -> it must be
#            special-cased to 0, otherwise every answer is one too big.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def minCut(self, s):
        """
        :type s: str
        :rtype: int
        """
        # edge
        if not s:
            return 0

        n = len(s)
        pal = [[False] * n for _ in range(n)]
        cut = [0] * n

        for i in range(n):
            # worst case : cut before every character
            mn = i
            for j in range(i + 1):
                # s[j..i] is a palindrome if the ends match and the inside is
                # one too (or the inside is shorter than 2 chars)
                if s[j] == s[i] and (i - j < 2 or pal[j + 1][i - 1]):
                    pal[j][i] = True
                    if j == 0:
                        mn = 0
                    else:
                        mn = min(mn, cut[j - 1] + 1)
            cut[i] = mn

        return cut[n - 1]
