# VO 

# V1 
# http://bookshadow.com/weblog/2016/07/16/leetcode-guess-number-higher-or-lower-ii/
# DP state func :
# dp[i][j] = min(k + max(dp[i][k - 1], dp[k + 1][j]))
"""

DP def
    (INTERVAL DP - minimax: we choose the guess, the adversary chooses the
     worse side)

    dp[i][j]: MIN amount of money guaranteed to be enough to find any

              number in [i, j]

DP eq

     dp[i][j] = min over x in [i, j) of

                   x + max( dp[i][x-1], dp[x+1][j] )


    -> e.g. `x +` is what guessing x costs when it is wrong,
              and `max(...)` is the adversary picking the worse half

     grow by INTERVAL LENGTH (gap) so both halves are already solved

     init: dp[i][i] = 0    (a single candidate needs no guess)
     ans = dp[1][n]

"""
# time = O(n^3)  # O(n^2) intervals x O(n) split-point scan
# space = O(n^2)
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
"""

DP def
    (INTERVAL DP - minimax: we choose the guess, the adversary chooses the
     worse side)

    dp[i][j]: MIN amount of money guaranteed to be enough to find any

              number in [i, j]

DP eq

     dp[i][j] = min over x in [i, j) of

                   x + max( dp[i][x-1], dp[x+1][j] )


    -> e.g. `x +` is what guessing x costs when it is wrong,
              and `max(...)` is the adversary picking the worse half

     grow by INTERVAL LENGTH (gap) so both halves are already solved

     init: dp[i][i] = 0    (a single candidate needs no guess)
     ans = dp[1][n]

"""
# time = O(n^3)  # O(n^2) intervals x O(n) split-point scan
# space = O(n^2)
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