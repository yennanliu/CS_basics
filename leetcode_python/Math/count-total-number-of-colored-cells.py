"""

2579. Count Total Number of Colored Cells
Medium

There exists an infinitely large two-dimensional grid of uncolored unit cells.
You are given a positive integer n, indicating that you must do the following routine for n minutes:

At the first minute, color any arbitrary unit cell blue.
Every minute thereafter, color blue every uncolored cell that touches a blue cell.

Return the number of colored cells at the end of n minutes.


Example 1:

Input: n = 1
Output: 1
Explanation: After 1 minute, there is only 1 blue cell, so we return 1.

Example 2:

Input: n = 2
Output: 5
Explanation: After 2 minutes, there are 4 colored cells on the boundary and 1 in the center,
so we return 5.


Constraints:

1 <= n <= 10^5

"""

# V0
# IDEA : MATH (closed form for the diamond / Aztec shape)
#
#   after n minutes the colored region is a diamond of "radius" n - 1 :
#   every cell whose Manhattan distance from the start is <= n - 1.
#
#   viewing it column by column there are 2n - 1 columns with heights
#       1, 3, 5, ..., 2n - 1, ..., 5, 3, 1
#   the middle column has 2n - 1 cells and the two arithmetic wings each sum to
#   (n - 1)^2, giving  (2n - 1) + 2 * (n - 1)^2 = 2 * n * (n - 1) + 1.
#
#   NOTE : n can reach 10^5, so the answer reaches ~2 * 10^10 — that overflows
#          32-bit in other languages; python ints are fine, but the closed form
#          is what keeps this O(1) instead of simulating 10^5 rings.
#
# time = O(1), space = O(1)
class Solution(object):
    def coloredCells(self, n):
        return 2 * n * (n - 1) + 1
