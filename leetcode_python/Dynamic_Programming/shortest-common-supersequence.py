"""

1092. Shortest Common Supersequence
Hard

Given two strings str1 and str2, return the shortest string that has both str1
and str2 as subsequences. If there are multiple valid strings, return any of them.

A string s is a subsequence of string t if deleting some number of characters
from t (possibly 0) results in the string s.


Example 1:

Input: str1 = "abac", str2 = "cab"
Output: "cabac"
Explanation:
str1 = "abac" is a subsequence of "cabac" because we can delete the first "c".
str2 = "cab" is a subsequence of "cabac" because we can delete the last "ac".
The answer provided is the shortest such string that satisfies these properties.

Example 2:

Input: str1 = "aaaaaaaa", str2 = "aaaaaaaa"
Output: "aaaaaaaa"


Constraints:

1 <= str1.length, str2.length <= 1000
str1 and str2 consist of lowercase English letters.

"""

# V0
# IDEA: LCS (2D DP) + BACKTRACK the DP table
"""

 DP def:
    - dp[i][j] = length of LCS of str1[:i] and str2[:j]

 DP eq:
    - if str1[i-1] == str2[j-1]: dp[i][j] = dp[i-1][j-1] + 1
    - else:                      dp[i][j] = max(dp[i-1][j], dp[i][j-1])

 Then walk back from (m, n):
    - chars in the LCS are emitted once
    - chars NOT in the LCS are emitted from whichever string they belong to
"""
# time = O(m * n)
# space = O(m * n)
class Solution(object):
    def shortestCommonSupersequence(self, str1, str2):
        m, n = len(str1), len(str2)

        # dp[i][j] = LCS length of str1[:i], str2[:j]
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if str1[i - 1] == str2[j - 1]:
                    dp[i][j] = dp[i - 1][j - 1] + 1
                else:
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])

        # backtrack -> build the supersequence in reverse
        res = []
        i, j = m, n
        while i > 0 or j > 0:
            if i == 0:
                j -= 1
                res.append(str2[j])
            elif j == 0:
                i -= 1
                res.append(str1[i])
            elif dp[i][j] == dp[i - 1][j]:
                # str1[i-1] is not in the LCS
                i -= 1
                res.append(str1[i])
            elif dp[i][j] == dp[i][j - 1]:
                # str2[j-1] is not in the LCS
                j -= 1
                res.append(str2[j])
            else:
                # matched char -> emit only once
                i -= 1
                j -= 1
                res.append(str1[i])

        return "".join(reversed(res))
