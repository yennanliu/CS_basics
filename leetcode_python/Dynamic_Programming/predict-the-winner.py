# V0

# V1
# http://bookshadow.com/weblog/2017/01/22/leetcode-predict-the-winner/
# IDEA : DP
# DP EQUATION :
# solve(nums) = max(nums[0] - solve(nums[1:]), nums[-1] - solve(nums[:-1]))
"""

DP def
    (GAME / INTERVAL DP - both players play optimally, so score the game as
     "current player's total minus the opponent's")

    dp[i][j]: the best achievable (my score - opponent score) when only

              nums[i..j] is left and it is MY turn

DP eq

     dp[i][j] = max(
                   nums[i] - dp[i+1][j],     # take the LEFT  end
                   nums[j] - dp[i][j-1]      # take the RIGHT end
                )


    -> e.g. the MINUS is what flips the perspective to the opponent - after
              my move they face the same problem on the smaller interval

     base: dp[i][i] = nums[i]
     ans = dp[0][n-1] >= 0     # player 1 wins on a tie

"""
# time = O(n^2), n = len(nums)
# space = O(n^2)
class Solution(object):
    def PredictTheWinner(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        cache = dict()
        def solve(nums):
            if len(nums) <= 1: return sum(nums)
            tnums = tuple(nums)
            if tnums in cache: return cache[tnums]
            cache[tnums] = max(nums[0] - solve(nums[1:]), nums[-1] - solve(nums[:-1]))
            return cache[tnums]
        return solve(nums) >= 0

# V1'
# https://www.jiuzhang.com/solution/predict-the-winner/#tag-highlight-lang-python
# IDEA : DP
# DP EQUATION :
# dp[i][j]=max(num[i]−dp[i+1][j],num[j]−dp[i][j−1])
"""

DP def
    (GAME / INTERVAL DP - both players play optimally, so score the game as
     "current player's total minus the opponent's")

    dp[i][j]: the best achievable (my score - opponent score) when only

              nums[i..j] is left and it is MY turn

DP eq

     dp[i][j] = max(
                   nums[i] - dp[i+1][j],     # take the LEFT  end
                   nums[j] - dp[i][j-1]      # take the RIGHT end
                )


    -> e.g. the MINUS is what flips the perspective to the opponent - after
              my move they face the same problem on the smaller interval

     base: dp[i][i] = nums[i]
     ans = dp[0][n-1] >= 0     # player 1 wins on a tie

"""
# time = O(n^2), n = len(nums)
# space = O(n^2)
class Solution:
    # @param {int[]} nums an array of scores
    # @return {boolean} check if player 1 will win
    def PredictTheWinner(self, nums):
        # Write your code here
        n = len(nums)
        if n == 0:
            return True

        f = [[0 for _ in range(n)] for __ in range(n)]
        for i in range(n):
            f[i][i] = 0

        for l in range(1, n):
            for i in range(0, n - l):
                j = i + l
                t1 = nums[i] - f[i + 1][j]
                t2 = nums[j] - f[i][j - 1]
                if t1 > t2:
                    f[i][j] = t1
                else:
                    f[i][j] = t2
        return f[0][n - 1] >= 0

# V2
"""

DP def
    (GAME / INTERVAL DP - both players play optimally, so score the game as
     "current player's total minus the opponent's")

    dp[i][j]: the best achievable (my score - opponent score) when only

              nums[i..j] is left and it is MY turn

DP eq

     dp[i][j] = max(
                   nums[i] - dp[i+1][j],     # take the LEFT  end
                   nums[j] - dp[i][j-1]      # take the RIGHT end
                )


    -> e.g. the MINUS is what flips the perspective to the opponent - after
              my move they face the same problem on the smaller interval

     base: dp[i][i] = nums[i]
     ans = dp[0][n-1] >= 0     # player 1 wins on a tie

"""
# time = O(n^2), n = len(nums)
# space = O(n)
class Solution(object):
    def PredictTheWinner(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        if len(nums) % 2 == 0 or len(nums) == 1:
            return True

        dp = [0] * len(nums)
        for i in reversed(range(len(nums))):
            dp[i] = nums[i]
            for j in range(i+1, len(nums)):
                dp[j] = max(nums[i] - dp[j], nums[j] - dp[j - 1])

        return dp[-1] >= 0