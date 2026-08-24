"""

1216. Valid Palindrome III
Hard

Given a string s and an integer k, return true if s is a k-palindrome.

A string is k-palindrome if it can be transformed into a palindrome by removing at most
k characters from it.

Example 1:

Input: s = "abcdeca", k = 2
Output: true
Explanation: Remove 'b' and 'e' characters.

Example 2:

Input: s = "abbababa", k = 1
Output: true


Constraints:

1 <= s.length <= 1000
s consists of only lowercase English letters.
1 <= k <= s.length

"""

# V0
# IDEA: 2D DP (Longest Palindromic Subsequence)
"""
 DP def:
    - dp[i][j] = length of the longest palindromic subsequence within s[i..j]

 DP eq:
    - s[i] == s[j] : dp[i][j] = dp[i+1][j-1] + 2
    - s[i] != s[j] : dp[i][j] = max(dp[i+1][j], dp[i][j-1])

 Answer:
    - the min number of deletions to make s a palindrome is n - LPS(s)
    - so s is k-palindrome  <=>  n - dp[0][n-1] <= k
"""
# time = O(n^2)
# space = O(n^2)
class Solution(object):
    def isValidPalindrome(self, s, k):
        n = len(s)
        if n == 0:
            return True

        dp = [[0] * n for _ in range(n)]

        # init : single char is a palindrome of length 1
        for i in range(n):
            dp[i][i] = 1

        # NOTE !!! loop i backward, so dp[i+1][*] is already computed
        for i in range(n - 2, -1, -1):
            for j in range(i + 1, n):
                if s[i] == s[j]:
                    dp[i][j] = dp[i + 1][j - 1] + 2
                else:
                    dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])

        return n - dp[0][n - 1] <= k


# V1
# IDEA: 2D DP (min deletions directly), rolling 1D array
#       f[i][j] = min deletions to turn s[i..j] into a palindrome
"""

DP def
    (Longest Palindromic Subsequence)

    dp[i][j]: length of the longest palindromic subsequence within s[i..j]

DP eq

     if s[i] == s[j]:  dp[i][j] = dp[i+1][j-1] + 2

     else:             dp[i][j] = max( dp[i+1][j], dp[i][j-1] )


    -> e.g. the MINIMUM deletions to make s a palindrome is n - LPS(s), so

         s is k-palindrome  <=>  n - dp[0][n-1] <= k

     NOTE !!! loop i BACKWARD so dp[i+1][*] is already computed

     init: dp[i][i] = 1
     ans = (n - dp[0][n-1]) <= k

"""
# time = O(n^2)
# space = O(n)
class Solution_1(object):
    def isValidPalindrome(self, s, k):
        n = len(s)
        # dp[j] over the current row i
        prev = [0] * n
        for i in range(n - 2, -1, -1):
            cur = [0] * n
            # dp[i][i] = 0 deletions
            for j in range(i + 1, n):
                if s[i] == s[j]:
                    # prev is row (i+1) -> prev[j-1] == dp[i+1][j-1]
                    cur[j] = prev[j - 1]
                else:
                    cur[j] = 1 + min(prev[j], cur[j - 1])
            prev = cur
        return prev[n - 1] <= k if n > 1 else True
