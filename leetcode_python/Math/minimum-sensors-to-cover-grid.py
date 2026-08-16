"""

3648. Minimum Sensors to Cover Grid
Medium

You are given n × m grid and an integer k.

A sensor placed on cell (r, c) covers all cells whose Chebyshev distance
from (r, c) is at most k.

The Chebyshev distance between two cells (r1, c1) and (r2, c2) is max(|r1 − r2|,|c1 − c2|).

Your task is to return the minimum number of sensors required to cover every
cell of the grid.

Example 1:

Input: n = 5, m = 5, k = 1
Output: 4
Explanation:
Placing sensors at positions (0, 3), (1, 0), (3, 3), and (4, 1) ensures
every cell in the grid is covered. Thus, the answer is 4.

Example 2:

Input: n = 2, m = 2, k = 2
Output: 1
Explanation:
With k = 2, a single sensor can cover the entire 2 * 2 grid regardless of
its position. Thus, the answer is 1.

Constraints:

1 <= n <= 10^3
1 <= m <= 10^3
0 <= k <= 10^3

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
