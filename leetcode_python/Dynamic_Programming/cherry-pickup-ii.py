"""

1463. Cherry Pickup II
Hard

You are given a rows x cols matrix grid representing a field of cherries where grid[i][j] represents the number of cherries that you can collect from the (i, j) cell.

You have two robots that can collect cherries for you:

Robot #1 is located at the top-left corner (0, 0), and
Robot #2 is located at the top-right corner (0, cols - 1).

Return the maximum number of cherries collection using both robots by following the rules below:

From a cell (i, j), robots can move to cell (i + 1, j - 1), (i + 1, j), or (i + 1, j + 1).
When any robot passes through a cell, It picks up all cherries, and the cell becomes an empty cell.
When both robots stay in the same cell, only one takes the cherries.
Both robots cannot move outside of the grid at any moment.
Both robots should reach the bottom row in grid.


Example 1:

Input: grid = [[3,1,1],[2,5,1],[1,5,5],[2,1,1]]
Output: 24
Explanation: Path of robot #1 and #2 are described in color green and blue respectively.
Cherries taken by Robot #1, (3 + 2 + 5 + 2) = 12.
Cherries taken by Robot #2, (1 + 5 + 5 + 1) = 12.
Total of cherries: 12 + 12 = 24.

Example 2:

Input: grid = [[1,0,0,0,0,0,1],[2,0,0,0,0,3,0],[2,0,9,0,0,0,0],[0,3,0,5,4,0,0],[1,0,2,3,0,0,6]]
Output: 28
Explanation: Path of robot #1 and #2 are described in color green and blue respectively.
Cherries taken by Robot #1, (1 + 9 + 5 + 2) = 17.
Cherries taken by Robot #2, (1 + 3 + 4 + 3) = 11.
Total of cherries: 17 + 11 = 28.


Constraints:

rows == grid.length
cols == grid[i].length
2 <= rows, cols <= 70
0 <= grid[i][j] <= 100

"""

# V0
# IDEA : DP ON BOTH ROBOTS AT ONCE (they always share the same row)
#
#   both robots step down exactly one row per move, so after i moves they
#   are both on row i -> the state is (row, col1, col2) instead of two
#   independent paths.
#   dp[j1][j2] = best total after processing the current row with the
#   robots in columns j1 and j2.
#   transition: each robot came from one of 3 columns -> 9 predecessors.
#   NOTE : when j1 == j2 the cell is counted ONCE, not twice.
#
"""

DP def
    both robots move DOWN exactly one row per step, so after i steps they are
    BOTH on row i -> the state is (row, col1, col2), not two separate paths

    dp[i][j1][j2]: max cherries collected after finishing row i

                   with robot1 in column j1 and robot2 in column j2

DP eq

     gain = grid[i][j1] + (0 if j1 == j2 else grid[i][j2])

     dp[i][j1][j2] = gain + max( dp[i-1][y1][y2] )

                     for y1 in (j1-1, j1, j1+1), y2 in (j2-1, j2, j2+1)


    -> e.g. 3 x 3 = 9 predecessors per state

     NOTE !!! when j1 == j2 the cell is counted ONCE, not twice

     init: dp[0][0][n-1] = grid[0][0] + grid[0][n-1]
     ans = max(dp[m-1][j1][j2])

"""
# time = O(rows * cols^2 * 9), space = O(cols^2)
class Solution(object):
    def cherryPickup(self, grid):
        m, n = len(grid), len(grid[0])
        NEG = float('-inf')

        dp = [[NEG] * n for _ in range(n)]
        dp[0][n - 1] = grid[0][0] + (grid[0][n - 1] if n > 1 else 0)

        for i in range(1, m):
            nxt = [[NEG] * n for _ in range(n)]
            for j1 in range(n):
                for j2 in range(n):
                    gain = grid[i][j1] + (0 if j1 == j2 else grid[i][j2])
                    best = NEG
                    for y1 in (j1 - 1, j1, j1 + 1):
                        if y1 < 0 or y1 >= n:
                            continue
                        for y2 in (j2 - 1, j2, j2 + 1):
                            if y2 < 0 or y2 >= n:
                                continue
                            if dp[y1][y2] > best:
                                best = dp[y1][y2]
                    if best != NEG:
                        nxt[j1][j2] = best + gain
            dp = nxt

        return max(max(row) for row in dp)
