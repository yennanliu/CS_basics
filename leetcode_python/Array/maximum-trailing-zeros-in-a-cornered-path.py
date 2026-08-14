"""

2245. Maximum Trailing Zeros in a Cornered Path
Medium

You are given a 2D integer array grid of size m x n, where each cell contains a positive integer.

A cornered path is defined as a set of adjacent cells with at most one turn. More specifically, the path should exclusively move either horizontally or vertically up to the turn (if there is one), without returning to a previously visited cell. After the turn, the path will then move exclusively in the alternate direction: move vertically if it moved horizontally, and vice versa, also without returning to a previously visited cell.

The product of a path is defined as the product of all the values in the path.

Return the maximum number of trailing zeros in the product of a cornered path found in grid.

Note:

Horizontal movement means moving in either the left or right direction.
Vertical movement means moving in either the up or down direction.


Example 1:

Input: grid = [[23,17,15,3,20],[8,1,20,27,11],[9,4,6,2,21],[40,9,1,10,6],[22,7,4,5,3]]
Output: 3
Explanation: The grid on the left shows a valid cornered path.
It has a product of 15 * 20 * 6 * 1 * 10 = 18000 which has 3 trailing zeros.
It can be shown that this is the maximum trailing zeros in the product of a cornered path.

The grid in the middle is not a cornered path as it has more than one turn.
The grid on the right is not a cornered path as it requires a return to a previously visited cell.

Example 2:

Input: grid = [[4,3,2],[7,6,1],[8,8,8]]
Output: 0
Explanation: The grid is shown in the figure above.
There are no cornered paths in the grid that result in a product with a trailing zero.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 10^5
1 <= m * n <= 10^5
1 <= grid[i][j] <= 1000

"""

# V0
# IDEA : TRAILING ZEROS = min(#2, #5) — SO ONLY THE 2/5 EXPONENTS MATTER
#
#   replace every cell by how many factors of 2 and of 5 it holds, then build
#   PREFIX SUMS along each row and each column. now the exponent totals of any
#   horizontal or vertical run are O(1) lookups.
#
#   a cornered path is one horizontal arm plus one vertical arm meeting at a
#   corner cell, so for each cell (i, j) there are exactly four shapes :
#       left+up, left+down, right+up, right+down
#   each of which counts the corner cell TWICE, hence the subtraction.
#
#   the answer is the best min(twos, fives) over all cells and all four
#   shapes. a straight (unturned) path is covered too, since one arm may be
#   just the corner cell itself.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def maxTrailingZeros(self, grid):
        m, n = len(grid), len(grid[0])

        def factors(x, p):
            c = 0
            while x % p == 0:
                x //= p
                c += 1
            return c

        two = [[factors(grid[i][j], 2) for j in range(n)] for i in range(m)]
        five = [[factors(grid[i][j], 5) for j in range(n)] for i in range(m)]

        # row prefix : rp2[i][j] = sum over columns 0..j-1 of row i
        rp2 = [[0] * (n + 1) for _ in range(m)]
        rp5 = [[0] * (n + 1) for _ in range(m)]
        for i in range(m):
            for j in range(n):
                rp2[i][j + 1] = rp2[i][j] + two[i][j]
                rp5[i][j + 1] = rp5[i][j] + five[i][j]

        # column prefix : cp2[i][j] = sum over rows 0..i-1 of column j
        cp2 = [[0] * n for _ in range(m + 1)]
        cp5 = [[0] * n for _ in range(m + 1)]
        for i in range(m):
            for j in range(n):
                cp2[i + 1][j] = cp2[i][j] + two[i][j]
                cp5[i + 1][j] = cp5[i][j] + five[i][j]

        res = 0
        for i in range(m):
            for j in range(n):
                left2, left5 = rp2[i][j + 1], rp5[i][j + 1]            # cols 0..j
                right2 = rp2[i][n] - rp2[i][j]                          # cols j..n-1
                right5 = rp5[i][n] - rp5[i][j]
                up2, up5 = cp2[i + 1][j], cp5[i + 1][j]                 # rows 0..i
                down2 = cp2[m][j] - cp2[i][j]                           # rows i..m-1
                down5 = cp5[m][j] - cp5[i][j]

                c2, c5 = two[i][j], five[i][j]                          # counted twice
                for a2, a5 in ((left2, left5), (right2, right5)):
                    for b2, b5 in ((up2, up5), (down2, down5)):
                        res = max(res, min(a2 + b2 - c2, a5 + b5 - c5))
        return res
