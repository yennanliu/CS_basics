"""

1682. Longest Palindromic Subsequence II
Medium

A subsequence of a string s is considered a good palindromic subsequence if:

- It is a subsequence of s.
- It is a palindrome (has the same value if reversed).
- It has an even length.
- No two consecutive characters are equal, except the two middle ones.

For example, if s = "abcabcabb", then "abba" is considered a good palindromic subsequence, while
"bcb" (not even length) and "bbbb" (has equal consecutive characters) are not.

Given a string s, return the length of the longest good palindromic subsequence in s.


Example 1:

Input: s = "bbabab"
Output: 4
Explanation: The longest good palindromic subsequence of s is "baab".

Example 2:

Input: s = "dcbccacdb"
Output: 4
Explanation: The longest good palindromic subsequence of s is "dccd".


Constraints:

1 <= s.length <= 250
s consists of lowercase English letters.

"""

# V0
# IDEA : INTERVAL DP WITH AN EXTRA "LAST PAIRED CHAR" DIMENSION
#
#   the palindrome is built from the OUTSIDE in, pairing s[i] with s[j].
#   "no two consecutive chars are equal (except the middle pair)" means the pair
#   we add now must differ from the pair we added one level out -> carry that
#   char as a third state dimension.
#
#   dp[i][j][c] = longest good palindromic subsequence inside s[i..j] given the
#                 enclosing pair used character c (c = 26 means "none yet")
#     if s[i] == s[j] and s[i] != c :  dp = dp[i+1][j-1][s[i]] + 2
#     else                          :  dp = max(dp[i+1][j][c], dp[i][j-1][c])
#
#   NOTE : when the two ends match AND are usable, taking them is always optimal
#          (the classic LPS exchange argument still holds under this constraint).
#   NOTE : rows only depend on row i+1 and on the same row -> roll two rows,
#          iterating i downwards and j upwards.
#
"""

DP def
    the palindrome is built from the OUTSIDE in, pairing s[i] with s[j].
    "no two consecutive characters are equal (except the middle pair)" means
    the pair added NOW must differ from the pair added one level OUT - so
    carry that character as a third dimension.

    dp[i][j][c]: longest GOOD palindromic subsequence inside s[i..j], given

                 the ENCLOSING pair used character c (c = 26 means "none yet")

DP eq

     if s[i] == s[j] and s[i] != c:

        dp[i][j][c] = dp[i+1][j-1][ s[i] ] + 2

     else:

        dp[i][j][c] = max( dp[i+1][j][c], dp[i][j-1][c] )


    -> e.g. when the ends match AND are usable, TAKING them is always
              optimal (the classic LPS exchange argument still holds here)

     rows depend only on row i+1 and the same row -> roll two rows,
     i downward and j upward

     ans = dp[0][n-1][26]    (an odd result is impossible - good palindromes
                              have even length)

"""
# time = O(n^2 * 26), space = O(n * 26)
class Solution(object):
    def longestPalindromeSubseq(self, s):
        n = len(s)
        NONE = 26
        # nxt[j][c] = dp[i+1][j][c], cur[j][c] = dp[i][j][c]
        nxt = [[0] * 27 for _ in range(n)]
        cur = None

        for i in range(n - 1, -1, -1):
            cur = [[0] * 27 for _ in range(n)]
            for j in range(i + 1, n):
                row = cur[j]
                if s[i] == s[j]:
                    inner = nxt[j - 1][ord(s[i]) - ord("a")] + 2 if j - 1 >= 0 else 2
                    ci = ord(s[i]) - ord("a")
                    for c in range(27):
                        if c != ci:
                            row[c] = inner
                        else:
                            a, b = nxt[j][c], cur[j - 1][c]
                            row[c] = a if a > b else b
                else:
                    prev = cur[j - 1]
                    top = nxt[j]
                    for c in range(27):
                        a, b = top[c], prev[c]
                        row[c] = a if a > b else b
            nxt = cur

        return cur[n - 1][NONE] if n > 0 else 0
