"""

1278. Palindrome Partitioning III
Hard

You are given a string s containing lowercase letters and an integer k. You need to :

First, change some characters of s to other lowercase English letters.
Then divide s into k non-empty disjoint substrings such that each substring is a palindrome.

Return the minimal number of characters that you need to change to divide the string.


Example 1:

Input: s = "abc", k = 2
Output: 1
Explanation: You can split the string into "ab" and "c", and change 1 character in "ab" to make it palindrome.

Example 2:

Input: s = "aabbc", k = 3
Output: 0
Explanation: You can split the string into "aa", "bb" and "c", all of them are palindrome.

Example 3:

Input: s = "leetcode", k = 8
Output: 0


Constraints:

1 <= k <= s.length <= 100.
s only contains lowercase English letters.

"""

# V0
# IDEA : 2 STAGE DP
#
#  1) cost[i][j] = number of changes to turn s[i..j] into a palindrome
#       cost[i][j] = cost[i+1][j-1] + (s[i] != s[j])
#
#  2) dp[i][j] = min changes to split the prefix s[0..i-1] into j palindromes
#       dp[i][j] = min( dp[m][j-1] + cost[m][i-1] ) for m in [j-1, i-1]
#
#  answer = dp[n][k]
#
# time = O(n^2 * k)
# space = O(n^2)
class Solution(object):
    def palindromePartition(self, s, k):
        n = len(s)
        if k >= n:
            # every char is its own palindrome
            return 0

        # 1) cost table, filled by increasing substring length
        cost = [[0] * n for _ in range(n)]
        for i in range(n - 1, -1, -1):
            for j in range(i + 1, n):
                cost[i][j] = cost[i + 1][j - 1] + (0 if s[i] == s[j] else 1)

        # 2) partition DP
        INF = float('inf')
        dp = [[INF] * (k + 1) for _ in range(n + 1)]
        dp[0][0] = 0
        for i in range(1, n + 1):
            # can not make more parts than we have characters
            for j in range(1, min(i, k) + 1):
                for m in range(j - 1, i):
                    if dp[m][j - 1] == INF:
                        continue
                    dp[i][j] = min(dp[i][j], dp[m][j - 1] + cost[m][i - 1])
        return dp[n][k]
