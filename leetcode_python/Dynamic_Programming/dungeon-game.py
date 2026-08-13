"""

174. Dungeon Game
Hard

The demons had captured the princess and imprisoned her in the bottom-right corner of a
dungeon. The dungeon consists of m x n rooms laid out in a 2D grid. Our valiant knight was
initially positioned in the top-left room and must fight his way through dungeon to rescue
the princess.

The knight has an initial health point represented by a positive integer. If at any point
his health point drops to 0 or below, he dies immediately.

Some of the rooms are guarded by demons (represented by negative integers), so the knight
loses health upon entering these rooms; other rooms are either empty (represented as 0) or
contain magic orbs that increase the knight's health (represented by positive integers).

To reach the princess as quickly as possible, the knight decides to move only rightward or
downward in each step.

Return the knight's minimum initial health so that he can rescue the princess.

Note that any room can contain threats or power-ups, even the first room the knight enters
and the bottom-right room where the princess is imprisoned.


Example 1:

Input: dungeon = [[-2,-3,3],[-5,-10,1],[10,30,-5]]
Output: 7
Explanation: The initial health of the knight must be at least 7 if he follows the optimal
path: RIGHT -> RIGHT -> DOWN -> DOWN.

Example 2:

Input: dungeon = [[0]]
Output: 1


Constraints:

m == dungeon.length
n == dungeon[i].length
1 <= m, n <= 200
-1000 <= dungeon[i][j] <= 1000

"""

# V0
# IDEA : 2D DP, FILLED BACKWARD (bottom-right -> top-left)
#
#  Why backward? Going forward we'd need to track BOTH "max health so far" and
#  "min health needed", which are not independently optimizable. Going backward
#  there is a single quantity to minimize.
#
#  DP def:
#    - dp[i][j] = minimum health required when ENTERING room (i, j)
#                 so the knight can reach the princess alive
#
#  DP eq:
#    - dp[i][j] = max(1, min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j])
#      (health must stay >= 1 at every step -> hence the max(1, ...))
#
#  Init:
#    - dp[m][n-1] = dp[m-1][n] = 1   (virtual cells just past the princess)
#    - every other out-of-range cell = infinity, so min() ignores it
#
# time = O(m * n)
# space = O(m * n)
class Solution(object):
    def calculateMinimumHP(self, dungeon):
        if not dungeon or not dungeon[0]:
            return 1

        m, n = len(dungeon), len(dungeon[0])
        INF = float("inf")

        # (m+1) x (n+1) so dp[i+1][j] / dp[i][j+1] are always in range
        dp = [[INF] * (n + 1) for _ in range(m + 1)]
        dp[m][n - 1] = 1
        dp[m - 1][n] = 1

        for i in range(m - 1, -1, -1):
            for j in range(n - 1, -1, -1):
                need = min(dp[i + 1][j], dp[i][j + 1]) - dungeon[i][j]
                dp[i][j] = max(1, need)

        return dp[0][0]
