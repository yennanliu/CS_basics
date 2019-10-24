# V0

# V1
# http://bookshadow.com/weblog/2017/05/15/leetcode-delete-operation-for-two-strings/
# IDEA : DP + Longest Common Subsequence
class Solution(object):
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        return len(word1) + len(word2) - 2 * self.lcs(word1, word2)

    def lcs(self, word1, word2):
        len1, len2 = len(word1), len(word2)
        dp = [[0] * (len2 + 1) for x in range(len1 + 1)]
        for x in range(len1):
            for y in range(len2):
                dp[x + 1][y + 1] = max(dp[x][y + 1], dp[x + 1][y])
                if word1[x] == word2[y]:
                    dp[x + 1][y + 1] = dp[x][y] + 1
        return dp[len1][len2]

# V1'
# http://bookshadow.com/weblog/2017/05/15/leetcode-delete-operation-for-two-strings/
# IDEA : DP
# DP EQUATION : 
# dp[x][y] = x + y     if x == 0 or y == 0
# dp[x][y] = dp[x - 1][y - 1]     if word1[x] == word2[y]
# dp[x][y] = min(dp[x - 1][y], dp[x][y - 1]) + 1     otherwise
class Solution(object):
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        len1, len2 = len(word1), len(word2)
        dp = [[0] * (len2 + 1) for x in range(len1 + 1)]
        for x in range(len1 + 1):
            for y in range(len2 + 1):
                if x == 0 or y == 0:
                    dp[x][y] = x + y
                elif word1[x - 1] == word2[y - 1]:
                    dp[x][y] = dp[x - 1][y - 1]
                else:
                    dp[x][y] = min(dp[x - 1][y], dp[x][y - 1]) + 1
        return dp[len1][len2]

# V1'
# https://www.jiuzhang.com/solution/delete-operation-for-two-strings/#tag-highlight-lang-python
class Solution:
    """
    @param word1: a string
    @param word2: a string
    @return: return a integer
    """
    def minDistance(self, word1, word2):
        # write your code here
        m, n = len(word1), len(word2)
        dp = [[0] * (n + 1) for i in range(m + 1)]
        for i in range(m):
            for j in range(n):
                dp[i + 1][j + 1] = max(dp[i][j + 1], dp[i + 1][j], dp[i][j] + (word1[i] == word2[j]))
        return m + n - 2 * dp[m][n]

# V2
# Time:  O(m * n)
# Space: O(n)
class Solution(object):
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        m, n = len(word1), len(word2)
        dp = [[0] * (n+1) for _ in range(2)]
        for i in range(m):
            for j in range(n):
                dp[(i+1)%2][j+1] = max(dp[i%2][j+1], \
                                       dp[(i+1)%2][j], \
                                       dp[i%2][j] + (word1[i] == word2[j]))
        return m + n - 2*dp[m%2][n]