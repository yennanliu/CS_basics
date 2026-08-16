"""

3418. Maximum Amount of Money Robot Can Earn
Medium

You are given an m x n grid. A robot starts at the top-left corner of the grid
(0, 0) and wants to reach the bottom-right corner (m - 1, n - 1). The robot can
move either right or down at any point in time.

The grid contains a value coins[i][j] in each cell:

If coins[i][j] >= 0, the robot gains that many coins.
If coins[i][j] < 0, the robot encounters a robber, and the robber steals the
absolute value of coins[i][j] coins.

The robot has a special ability to neutralize robbers in at most 2 cells on its
path, preventing them from stealing coins in those cells.

Note: The robot's total coins can be negative.

Return the maximum profit the robot can gain on the route.

Example 1:

Input: coins = [[0,1,-1],[1,-2,3],[2,-3,4]]

Output: 8

Explanation:

An optimal path for maximum coins is:

Start at (0, 0) with 0 coins (total coins = 0).
Move to (0, 1), gaining 1 coin (total coins = 0 + 1 = 1).
Move to (1, 1), where there's a robber stealing 2 coins. The robot uses one
neutralization here, avoiding the robbery (total coins = 1).
Move to (1, 2), gaining 3 coins (total coins = 1 + 3 = 4).
Move to (2, 2), gaining 4 coins (total coins = 4 + 4 = 8).

Example 2:

Input: coins = [[10,10,10],[10,10,10]]

Output: 40

Explanation:

An optimal path for maximum coins is:

Start at (0, 0) with 10 coins (total coins = 10).
Move to (0, 1), gaining 10 coins (total coins = 10 + 10 = 20).
Move to (0, 2), gaining another 10 coins (total coins = 20 + 10 = 30).
Move to (1, 2), gaining the final 10 coins (total coins = 30 + 10 = 40).

Constraints:

m == coins.length
n == coins[i].length
1 <= m, n <= 500
-1000 <= coins[i][j] <= 1000

"""

# V0
# IDEA : GRID DP WITH THE NUMBER OF NEUTRALISATIONS USED AS A THIRD AXIS
#
#   the only thing the robot carries besides its position is how many of its two
#   free "shields" it has already spent, so the state is (row, col, used) with
#   used in {0, 1, 2} — a 3-fold copy of the usual right/down DP.
#
#   arriving at a cell either pays the cell (used unchanged) or shields it and
#   collects 0 instead (used + 1).  shielding a *positive* cell is allowed by
#   this formulation but can never win, since 0 <= coins there, so we do not
#   need to special-case robbers.
#
#   the whole thing is a max, not a min, and negative totals are legal, so the
#   unreachable sentinel has to be -infinity rather than 0 — otherwise a
#   phantom "zero cost" path would leak in from outside the grid.
#
#   only the previous row is ever read, so one rolling row of triples suffices.
#
# time = O(m * n * 3), space = O(n * 3)
class Solution(object):
    def maximumAmount(self, coins):
        NEG = float('-inf')
        m, n = len(coins), len(coins[0])
        prev = [[NEG] * 3 for _ in range(n)]
        for i in range(m):
            cur = [[NEG] * 3 for _ in range(n)]
            row = coins[i]
            for j in range(n):
                v = row[j]
                c = cur[j]
                if i == 0 and j == 0:
                    c[0] = v
                    c[1] = 0
                    c[2] = 0
                    continue
                for k in range(3):
                    best = NEG
                    if i > 0 and prev[j][k] > best:
                        best = prev[j][k]
                    if j > 0 and cur[j - 1][k] > best:
                        best = cur[j - 1][k]
                    if best > NEG:
                        c[k] = best + v
                    if k > 0:
                        b2 = NEG
                        if i > 0 and prev[j][k - 1] > b2:
                            b2 = prev[j][k - 1]
                        if j > 0 and cur[j - 1][k - 1] > b2:
                            b2 = cur[j - 1][k - 1]
                        if b2 > c[k]:
                            c[k] = b2
            prev = cur
        return max(prev[n - 1])
