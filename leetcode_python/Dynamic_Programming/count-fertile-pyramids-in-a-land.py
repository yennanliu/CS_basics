"""

2088. Count Fertile Pyramids in a Land
Hard

A farmer has a rectangular grid of land with m rows and n columns that can be divided into unit cells. Each cell is either fertile (represented by a 1) or barren (represented by a 0). All cells outside the grid are considered barren.

A pyramidal plot of land can be defined as a set of cells with the following criteria:

The number of cells in the set has to be greater than 1 and all cells must be fertile.
The apex of a pyramid is the topmost cell of the pyramid. The height of a pyramid is the number of rows it covers. Let (r, c) be the apex of the pyramid, and its height be h. Then, the plot comprises of cells (i, j) where r <= i <= r + h - 1 and c - (i - r) <= j <= c + (i - r).

An inverse pyramidal plot of land can be defined as a set of cells with similar criteria outlined above with the following modification:

Instead of the apex, the highest cell is the bottommost cell of the pyramid. Let (r, c) be the apex, and its height be h. Then, the plot comprises of cells (i, j) where r - h + 1 <= i <= r and c - (r - i) <= j <= c + (r - i).

Given a 0-indexed m x n binary matrix grid representing the farmland, return the total number of pyramidal and inverse pyramidal plots that can be found in grid.


Example 1:

Input: grid = [[0,1,1,0],[1,1,1,1]]
Output: 2
Explanation: The 2 possible pyramidal plots are shown in blue and red respectively.
There are no inverse pyramidal plots in this grid.
Hence total number of pyramidal and inverse pyramidal plots is 2 + 0 = 2.

Example 2:

Input: grid = [[1,1,1],[1,1,1]]
Output: 2
Explanation: The pyramidal plot is shown in blue, and the inverse pyramidal plot is shown in red.
Hence the total number of plots is 1 + 1 = 2.

Example 3:

Input: grid = [[1,1,1,1,0],[1,1,1,1,1],[1,1,1,1,1],[0,1,0,0,1]]
Output: 13
Explanation: There are 7 pyramidal plots, 3 of which are shown in the blue and red plots.
There are 6 inverse pyramidal plots, 2 of which are shown in the purple plot.
The total number of plots is 7 + 6 = 13.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 1000
1 <= m * n <= 10^5
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : DP ON "TALLEST PYRAMID WITH APEX HERE", RUN TWICE (flip the grid)
#
#   dp[i][j] = the HEIGHT of the tallest pyramid whose apex is (i, j),
#              and 0 when the cell itself is barren.
#
#   recurrence, for an interior column j (1 <= j <= n-2) with g[i][j] == 1 :
#       dp[i][j] = 1 + min(dp[i+1][j-1], dp[i+1][j], dp[i+1][j+1])
#   growing one row taller needs all three cells below to already support
#   that much — and because a barren cell stores 0, the min automatically
#   caps the pyramid at height 1 there. bottom row and border columns are 1
#   (or 0 if barren).
#
#   a height-h apex yields h - 1 valid plots (heights 2..h, height 1 doesn't
#   count), so summing dp[i][j] - 1 over the fertile cells counts every
#   UPRIGHT pyramid. flipping the grid vertically and rerunning counts the
#   inverse ones.
#
"""

DP def
    dp[i][j]: HEIGHT of the tallest pyramid whose APEX is (i, j)

              -> 0 when the cell itself is barren

DP eq

     for an interior column j with grid[i][j] == 1:

        dp[i][j] = 1 + min( dp[i+1][j-1], dp[i+1][j], dp[i+1][j+1] )

     bottom row / border columns: dp[i][j] = 1  (0 if barren)


    -> e.g. growing one row taller needs ALL THREE cells below to support
              it; a barren cell stores 0, so the min caps the height there

     a height-h apex yields h - 1 valid plots (heights 2..h), so

        ans = sum(dp[i][j] - 1) over fertile cells

     run it TWICE - once on grid, once on grid[::-1] - to count the
     upright AND the upside-down pyramids

"""
# time = O(m * n), space = O(m * n)
class Solution(object):
    def countPyramids(self, grid):
        def count(g):
            m, n = len(g), len(g[0])
            dp = [[0] * n for _ in range(m)]
            total = 0
            for i in range(m - 1, -1, -1):
                for j in range(n):
                    if g[i][j] == 0:
                        continue
                    if i == m - 1 or j == 0 or j == n - 1:
                        dp[i][j] = 1
                    else:
                        dp[i][j] = 1 + min(dp[i + 1][j - 1],
                                           dp[i + 1][j],
                                           dp[i + 1][j + 1])
                    total += dp[i][j] - 1
            return total

        return count(grid) + count(grid[::-1])
