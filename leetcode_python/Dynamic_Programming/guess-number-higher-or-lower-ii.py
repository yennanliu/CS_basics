# VO 

# V1 
# http://bookshadow.com/weblog/2016/07/16/leetcode-guess-number-higher-or-lower-ii/
# DP state func : 
# dp[i][j] = min(k + max(dp[i][k - 1], dp[k + 1][j]))
class Solution(object):
    def getMoneyAmount(self, n):
        """
        :type n: int
        :rtype: int
        """
        dp = [[0] * (n+1) for _ in range(n+1)]
        for gap in range(1, n):
            for lo in range(1, n+1-gap):
                hi = lo + gap
                dp[lo][hi] = min(x + max(dp[lo][x-1], dp[x+1][hi])
                                   for x in range(lo, hi))
        return dp[1][n]

# V2 
# Time:  O(n^2)
# Space: O(n^2)
class Solution(object):
    def getMoneyAmount(self, n):
        """
        :type n: int
        :rtype: int
        """
        pay = [[0] * n for _ in range(n+1)]
        for i in reversed(range(n)):
            for j in range(i+1, n):
                pay[i][j] = min(k+1 + max(pay[i][k-1], pay[k+1][j]) \
                                for k in range(i, j+1))
        return pay[0][n-1]