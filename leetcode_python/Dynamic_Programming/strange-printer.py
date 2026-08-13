"""

664. Strange Printer
Hard

There is a strange printer with the following two special properties:

- The printer can only print a sequence of the same character each time.
- At each turn, the printer can print new characters starting from and ending at any
  place and will cover the original existing characters.

Given a string s, return the minimum number of turns the printer needed to print it.

Example 1:

Input: s = "aaabbb"
Output: 2
Explanation: Print "aaa" first and then print "bbb".

Example 2:

Input: s = "aba"
Output: 2
Explanation: Print "aaa" first and then print "b" from the second place of the string,
which will cover the existing character 'a'.

Constraints:

1 <= s.length <= 100
s consists of lowercase English letters.

"""

# V0
# IDEA : INTERVAL DP
#
#  Pre-step: collapse runs of equal characters ("aaabbb" -> "ab").
#  A run costs the same as a single character, and collapsing shrinks n a lot.
#
#  DP def:
#    - dp[i][j] = minimum turns to print s[i..j]
#
#  DP eq (think about the LAST character s[j]):
#    - worst case: print s[j] on its own turn      -> dp[i][j] = dp[i][j-1] + 1
#    - better: if some k in [i, j-1] has s[k] == s[j], the SAME turn that printed
#      s[k] can be stretched to cover position j, so s[j] is free:
#        dp[i][j] = min(dp[i][j], dp[i][k] + dp[k+1][j-1])
#      (dp[k+1][j-1] is 0 when the middle segment is empty)
#
#  Iterate i downwards so dp[i][k] / dp[k+1][j-1] are already computed.
#
# time = O(n^3)
# space = O(n^2)
class Solution(object):
    def strangePrinter(self, s):
        # collapse consecutive duplicates
        compact = []
        for ch in s:
            if not compact or compact[-1] != ch:
                compact.append(ch)

        n = len(compact)
        if n == 0:
            return 0

        dp = [[0] * n for _ in range(n)]
        for i in range(n - 1, -1, -1):
            dp[i][i] = 1
            for j in range(i + 1, n):
                # baseline: s[j] needs its own turn
                dp[i][j] = dp[i][j - 1] + 1
                for k in range(i, j):
                    if compact[k] == compact[j]:
                        mid = dp[k + 1][j - 1] if k + 1 <= j - 1 else 0
                        dp[i][j] = min(dp[i][j], dp[i][k] + mid)

        return dp[0][n - 1]
