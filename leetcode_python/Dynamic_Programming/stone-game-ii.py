"""

1140. Stone Game II
Medium

Alice and Bob continue their games with piles of stones. There are a number of
piles arranged in a row, and each pile has a positive integer number of stones
piles[i]. The objective of the game is to end with the most stones.

Alice and Bob take turns, with Alice starting first.

On each player's turn, that player can take all the stones in the first X
remaining piles, where 1 <= X <= 2M. Then, we set M = max(M, X). Initially,
M = 1.

The game continues until all the stones have been taken.

Assuming Alice and Bob play optimally, return the maximum number of stones
Alice can get.

Example 1:

Input: piles = [2,7,9,4,4]

Output: 10

Explanation:

If Alice takes one pile at the beginning, Bob takes two piles, then Alice
takes 2 piles again. Alice can get 2 + 4 + 4 = 10 stones in total.
If Alice takes two piles at the beginning, then Bob can take all three piles
left. In this case, Alice get 2 + 7 = 9 stones in total.
So we return 10 since it's larger.

Example 2:

Input: piles = [1,2,3,4,5,100]

Output: 104

Constraints:

1 <= piles.length <= 100
1 <= piles[i] <= 10^4

"""

# V0
# IDEA : TOP-DOWN DP (MEMO) ON (i, M) + SUFFIX SUM
#
#   dp(i, M) = max stones the player TO MOVE collects from piles[i:], given M
#
#   Whatever the mover does not get from piles[i:], the opponent gets. So
#   after taking X piles, the mover's total is
#       suffix[i] - dp(i + X, max(M, X))      # all that's left - opponent's best
#
#     dp(i, M) = max over 1 <= X <= 2M of  suffix[i] - dp(i + X, max(M, X))
#     dp(i, M) = suffix[i]   if i + 2M >= n   # can take everything left
#
#   Answer = dp(0, 1).
#
#   e.g. piles = [2,7,9,4,4] -> dp(0, 1) = 10
#
# time = O(n^3), space = O(n^2)
class Solution(object):
    def stoneGameII(self, piles):
        """
        :type piles: List[int]
        :rtype: int
        """
        n = len(piles)

        # suffix[i] = stones in piles[i:]   (suffix[n] = 0)
        self.suffix = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            self.suffix[i] = self.suffix[i + 1] + piles[i]

        self.n = n
        self.memo = {}
        return self.dp(0, 1)

    def dp(self, i, m):
        """Max stones the player to move collects from piles[i:] with M = m."""
        # no piles left
        if i >= self.n:
            return 0

        # NOTE !!! if 2M reaches the end, take everything - the best possible move
        if i + 2 * m >= self.n:
            return self.suffix[i]

        if (i, m) in self.memo:
            return self.memo[(i, m)]

        best = 0
        for x in range(1, 2 * m + 1):
            # the opponent moves next on piles[i + x:] with the new M
            opponent = self.dp(i + x, max(m, x))
            mine = self.suffix[i] - opponent
            if mine > best:
                best = mine

        self.memo[(i, m)] = best
        return best


# V0-1
# IDEA : BOTTOM-UP DP ON (i, M) + SUFFIX SUM
#
#   Same recurrence as V0, filled as a table. dp[i][m] only reads rows
#   i + x > i, so iterate i from n-1 down to 0. M never needs to exceed n
#   (once 2M >= n the player can take everything anyway).
#
# time = O(n^3), space = O(n^2)
class Solution2(object):
    def stoneGameII(self, piles):
        """
        :type piles: List[int]
        :rtype: int
        """
        n = len(piles)

        # suffix[i] = stones in piles[i:]   (suffix[n] = 0)
        suffix = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            suffix[i] = suffix[i + 1] + piles[i]

        # dp[i][m] = max stones the player to move collects from piles[i:] with M = m
        # row n (no piles left) stays 0
        dp = [[0] * (n + 1) for _ in range(n + 1)]

        for i in range(n - 1, -1, -1):
            for m in range(1, n + 1):
                # can take everything that is left
                if i + 2 * m >= n:
                    dp[i][m] = suffix[i]
                    continue

                best = 0
                for x in range(1, 2 * m + 1):
                    # max(m, x) <= n here, because i + x < n
                    opponent = dp[i + x][max(m, x)]
                    mine = suffix[i] - opponent
                    if mine > best:
                        best = mine
                dp[i][m] = best

        return dp[0][1]
