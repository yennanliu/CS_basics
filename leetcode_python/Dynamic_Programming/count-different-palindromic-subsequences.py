"""

730. Count Different Palindromic Subsequences
Hard

Given a string s, return the number of different non-empty palindromic subsequences in s.
Since the answer may be very large, return it modulo 10^9 + 7.

A subsequence of a string is obtained by deleting zero or more characters from the string.

A sequence is palindromic if it is equal to the sequence reversed.

Two sequences a1, a2, ... and b1, b2, ... are different if there is some i for which ai != bi.


Example 1:

Input: s = "bccb"
Output: 6
Explanation: The 6 different non-empty palindromic subsequences are
'b', 'c', 'bb', 'cc', 'bcb', 'bccb'.
Note that 'bcb' is counted only once, even though it occurs twice.

Example 2:

Input: s = "abcdabcdabcdabcdabcdabcdabcdabcddcbadcbadcbadcbadcbadcbadcbadcba"
Output: 104860361
Explanation: There are 3104860382 different non-empty palindromic subsequences,
which is 104860361 modulo 10^9 + 7.


Constraints:

1 <= s.length <= 1000
s[i] is either 'a', 'b', 'c', or 'd'.

"""

# V0
# IDEA : INTERVAL DP
#
#   DP def:
#     - dp[i][j] = number of DISTINCT non-empty palindromic subsequences in s[i..j]
#
#   DP eq:
#     - s[i] != s[j]:
#         dp[i][j] = dp[i+1][j] + dp[i][j-1] - dp[i+1][j-1]   (inclusion-exclusion)
#
#     - s[i] == s[j]: let lo / hi be the first / last index strictly inside (i, j)
#       holding that same character.
#         no such char (lo > hi) : dp[i][j] = 2*dp[i+1][j-1] + 2
#             -> every inner palindrome can be wrapped by s[i]..s[j], plus "c" and "cc"
#         exactly one (lo == hi) : dp[i][j] = 2*dp[i+1][j-1] + 1
#             -> "c" is already counted by the inner part, only "cc" is new
#         two or more           : dp[i][j] = 2*dp[i+1][j-1] - dp[lo+1][hi-1]
#             -> subtract the palindromes double counted between the two copies
#
# time = O(n^2 * 4)  (the lo/hi scan is bounded by the 4-letter alphabet in practice)
# space = O(n^2)
class Solution(object):
    def countPalindromicSubsequences(self, s):
        MOD = 10 ** 9 + 7
        n = len(s)
        if n == 0:
            return 0

        dp = [[0] * n for _ in range(n)]
        for i in range(n):
            dp[i][i] = 1  # single char is one palindrome

        # grow by interval length so dp[i+1][j-1] etc. are already known
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1

                if s[i] != s[j]:
                    dp[i][j] = dp[i + 1][j] + dp[i][j - 1] - dp[i + 1][j - 1]
                else:
                    # find the innermost pair of the same char inside (i, j)
                    lo, hi = i + 1, j - 1
                    while lo <= hi and s[lo] != s[i]:
                        lo += 1
                    while lo <= hi and s[hi] != s[i]:
                        hi -= 1

                    if lo > hi:
                        dp[i][j] = 2 * dp[i + 1][j - 1] + 2
                    elif lo == hi:
                        dp[i][j] = 2 * dp[i + 1][j - 1] + 1
                    else:
                        dp[i][j] = 2 * dp[i + 1][j - 1] - dp[lo + 1][hi - 1]

                dp[i][j] %= MOD

        return dp[0][n - 1] % MOD
