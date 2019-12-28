# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82390672
class Solution(object):
    
    def stoneGame(self, piles):
        """
        :type piles: List[int]
        :rtype: bool
        """
        if not piles:
            return False
        self.F = [[0 for i in range(len(piles))] for j in range(len(piles))]
        self.S = [[0 for i in range(len(piles))] for j in range(len(piles))]
        _sum = sum(piles)
        alex = self.f(piles, 0, len(piles) - 1)
        return alex > _sum / 2
        
    def f(self, piles, i, j):
        """
        select first
        """
        if i == j:
            return piles[i]
        if self.F[i][j] != 0:
            return self.F[i][j]
        curr = max(piles[i] + self.s(piles, i + 1, j), piles[j] + self.s(piles, i, j - 1))
        self.F[i][j] = curr
        return curr
        
    def s(self, piles, i, j):
        """
        select later
        """
        if i == j:
            return 0
        if self.S[i][j] != 0:
            return self.S[i][j]
        curr = min(self.f(piles, i + 1, j), self.f(piles, i, j - 1))
        self.S[i][j] = curr
        return curr

# V2
# Time:  O(n^2)
# Space: O(n)
class Solution(object):
    def stoneGame(self, piles):
        """
        :type piles: List[int]
        :rtype: bool
        """
        if len(piles) % 2 == 0 or len(piles) == 1:
            return True

        dp = [0] * len(piles)
        for i in reversed(range(len(piles))):
            dp[i] = piles[i]
            for j in range(i+1, len(piles)):
                dp[j] = max(piles[i] - dp[j], piles[j] - dp[j - 1])
        return dp[-1] >= 0