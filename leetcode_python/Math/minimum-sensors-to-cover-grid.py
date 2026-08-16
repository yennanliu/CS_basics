"""

3648. Minimum Sensors to Cover Grid
Medium

You are given three integers n, m, and k, where n and m are the numbers of rows and columns of a grid, respectively.

A sensor placed at cell (r, c) covers every cell (i, j) whose Chebyshev distance to (r, c) is at most k, that is, max(|i - r|, |j - c|) <= k.

Return the minimum number of sensors required to cover every cell of the grid.


Example 1:

Input: n = 5, m = 5, k = 1
Output: 4
Explanation:
Placing sensors at (1, 1), (1, 4), (4, 1) and (4, 4) covers all 25 cells, and no placement of 3 sensors can cover the whole grid.

Example 2:

Input: n = 2, m = 2, k = 2
Output: 1
Explanation:
A single sensor placed at (0, 0) already covers every cell, since every cell is within Chebyshev distance 2.


Constraints:

1 <= n, m <= 10^6
0 <= k <= 10^6

"""

# V0
# IDEA : THE CHEBYSHEV BALL IS A SQUARE, SO ROWS AND COLUMNS DECOUPLE
#
#   a sensor covers a (2k+1) x (2k+1) axis-aligned square. because that shape
#   is a cartesian product of a row interval and a column interval, covering
#   the grid is exactly covering [0, n) with intervals of length 2k+1 AND
#   covering [0, m) with intervals of length 2k+1, independently.
#
#   a 1-d segment of length n needs ceil(n / (2k+1)) intervals, and taking the
#   product of the two 1-d optimal placements gives a valid 2-d cover, so the
#   product is both achievable and a lower bound.
#
# time = O(1), space = O(1)
class Solution(object):
    def minSensors(self, n, m, k):
        side = 2 * k + 1
        return ((n + side - 1) // side) * ((m + side - 1) // side)
