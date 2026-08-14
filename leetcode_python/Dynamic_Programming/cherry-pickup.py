"""

741. Cherry Pickup
Hard

You are given an n x n grid representing a field of cherries, each cell is one of
three possible integers.

  - 0 means the cell is empty, so you can pass through,
  - 1 means the cell contains a cherry that you can pick up and pass through, or
  - -1 means the cell contains a thorn that blocks your way.

Return the maximum number of cherries you can collect by following the rules below:

  - Starting at the position (0, 0) and reaching (n - 1, n - 1) by moving right or down
    through valid path cells (cells with value 0 or 1).
  - After reaching (n - 1, n - 1), returning to (0, 0) by moving left or up through
    valid path cells.
  - When passing through a path cell containing a cherry, you pick it up, and the cell
    becomes an empty cell 0.
  - If there is no valid path between (0, 0) and (n - 1, n - 1), then no cherries
    can be collected.


Example 1:

Input: grid = [[0,1,-1],[1,0,-1],[1,1,1]]
Output: 5
Explanation: The player started at (0, 0) and went down, down, right right to reach (2, 2).
4 cherries were picked up during this single trip, and the matrix becomes
[[0,1,-1],[0,0,-1],[0,0,0]].
Then, the player went left, up, up, left to return home, picking up one more cherry.
The total number of cherries picked up is 5, and this is the maximum possible.

Example 2:

Input: grid = [[1,1,-1],[1,-1,1],[-1,1,1]]
Output: 0


Constraints:

n == grid.length
n == grid[i].length
1 <= n <= 50
grid[i][j] is -1, 0, or 1.
grid[0][0] != -1
grid[n - 1][n - 1] != -1

"""

# V0
# IDEA : 2 WALKERS DP (round trip == two simultaneous down/right walks)
#
#   Key reframing: going there and coming back is the SAME as sending two walkers
#   from (0,0) to (n-1,n-1), both only moving down/right. If both step at the same
#   pace, after k steps a walker at row r is at column k - r.
#
#   DP def:
#     - dp[r1][r2] = max cherries collected after k steps, walker 1 on row r1,
#                    walker 2 on row r2 (columns implied by k)
#
#   DP eq (4 predecessor combos: each walker came from up or from left):
#     - dp_k[r1][r2] = max(dp_{k-1}[r1-1][r2-1], dp_{k-1}[r1-1][r2],
#                          dp_{k-1}[r1][r2-1],   dp_{k-1}[r1][r2]) + gain
#       where gain = grid[r1][c1] + grid[r2][c2], counted ONCE if both are on the
#       same cell (a cherry can only be picked up once).
#
#   -inf marks unreachable states; a blocked or unreachable end means 0.
#
# time = O(n^3)
# space = O(n^2)
class Solution(object):
    def cherryPickup(self, grid):
        n = len(grid)
        NEG = float("-inf")

        dp = [[NEG] * n for _ in range(n)]
        dp[0][0] = grid[0][0]

        # k = number of steps taken; both walkers are on anti-diagonal r + c == k
        for k in range(1, 2 * n - 1):
            ndp = [[NEG] * n for _ in range(n)]
            lo = max(0, k - n + 1)   # smallest row with a valid column
            hi = min(n - 1, k)       # largest row with a valid column

            for r1 in range(lo, hi + 1):
                c1 = k - r1
                if grid[r1][c1] == -1:
                    continue
                for r2 in range(lo, hi + 1):
                    c2 = k - r2
                    if grid[r2][c2] == -1:
                        continue

                    best = NEG
                    for pr1 in (r1 - 1, r1):
                        for pr2 in (r2 - 1, r2):
                            if pr1 >= 0 and pr2 >= 0 and dp[pr1][pr2] > best:
                                best = dp[pr1][pr2]
                    if best == NEG:
                        continue  # unreachable state

                    gain = grid[r1][c1]
                    if r1 != r2:  # same cell -> count the cherry only once
                        gain += grid[r2][c2]
                    ndp[r1][r2] = best + gain

            dp = ndp

        # if (n-1, n-1) was never reached, dp holds -inf -> no cherries
        return max(0, dp[n - 1][n - 1])
