"""

1690. Stone Game VII
Medium

Alice and Bob take turns playing a game, with Alice starting first.

There are n stones arranged in a row. On each player's turn, they can remove either the leftmost
stone or the rightmost stone from the row and receive points equal to the sum of the remaining
stones' values in the row. The winner is the one with the higher score when there are no stones left
to remove.

Bob found that he will always lose this game (poor Bob, he always loses), so he decided to minimize
the score's difference. Alice's goal is to maximize the difference in the score.

Given an array of integers stones where stones[i] represents the value of the ith stone from the
left, return the difference in Alice and Bob's score if they both play optimally.


Example 1:

Input: stones = [5,3,1,4,2]
Output: 6
Explanation:
- Alice removes 2 and gets 5 + 3 + 1 + 4 = 13 points. Alice = 13, Bob = 0, stones = [5,3,1,4].
- Bob removes 5 and gets 3 + 1 + 4 = 8 points. Alice = 13, Bob = 8, stones = [3,1,4].
- Alice removes 3 and gets 1 + 4 = 5 points. Alice = 18, Bob = 8, stones = [1,4].
- Bob removes 1 and gets 4 points. Alice = 18, Bob = 12, stones = [4].
- Alice removes 4 and gets 0 points. Alice = 18, Bob = 12, stones = [].
The score difference is 18 - 12 = 6.

Example 2:

Input: stones = [7,90,5,1,100,10,10,2]
Output: 122


Constraints:

n == stones.length
2 <= n <= 1000
1 <= stones[i] <= 1000

"""

# V0
# IDEA : INTERVAL DP ON THE SCORE DIFFERENCE (zero-sum game -> one table, not two)
#
#   both players want to maximise (my score - opponent score) from their own turn,
#   so a single value suffices:
#     dp[i][j] = best achievable (mover - other) on the sub-row stones[i..j]
#
#   the mover either drops stones[i] and banks sum(i+1..j), or drops stones[j] and
#   banks sum(i..j-1); afterwards the roles flip, hence the minus sign:
#     dp[i][j] = max( sum(i+1..j) - dp[i+1][j],
#                     sum(i..j-1) - dp[i][j-1] )
#     dp[i][i] = 0
#
#   NOTE : dp[i][*] only needs row i+1 and cells already written on row i, so a
#          single 1-D array swept with i descending / j ascending is enough.
#
"""

DP def
    a ZERO-SUM game, so one table suffices (not one per player)

    dp[i][j]: best achievable (mover score - other score) on the sub-row

              stones[i..j]

DP eq

     dp[i][j] = max(
                   sum(i+1..j) - dp[i+1][j],     # drop stones[i], bank the rest
                   sum(i..j-1) - dp[i][j-1]      # drop stones[j], bank the rest
                )

     dp[i][i] = 0


    -> e.g. the MINUS is the role flip - after my move the opponent faces
              the same problem on the smaller row

     NOTE !!! dp[i][*] only needs row i+1 plus cells already written on row
              i, so ONE 1-D array swept with i descending / j ascending is
              enough

     ans = dp[0][n-1]

"""
# time = O(n^2), space = O(n)
class Solution(object):
    def stoneGameVII(self, stones):
        n = len(stones)
        pre = [0] * (n + 1)
        for i in range(n):
            pre[i + 1] = pre[i] + stones[i]

        dp = [0] * n                      # dp[j] plays the role of dp[i][j]
        for i in range(n - 1, -1, -1):
            dp[i] = 0                     # dp[i][i]
            for j in range(i + 1, n):
                # dp[j] still holds dp[i+1][j]; dp[j-1] already holds dp[i][j-1]
                drop_left = (pre[j + 1] - pre[i + 1]) - dp[j]
                drop_right = (pre[j] - pre[i]) - dp[j - 1]
                dp[j] = drop_left if drop_left > drop_right else drop_right
        return dp[n - 1]
