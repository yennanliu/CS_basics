# V0

# V1
# http://bookshadow.com/weblog/2017/01/22/leetcode-predict-the-winner/
# IDEA : DP
# DP EQUATION :
# solve(nums) = max(nums[0] - solve(nums[1:]), nums[-1] - solve(nums[:-1]))
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
# Time:  O(n^2)
# Space: O(n)
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