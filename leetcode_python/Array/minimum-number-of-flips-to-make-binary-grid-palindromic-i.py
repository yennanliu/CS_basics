"""

3239. Minimum Number of Flips to Make Binary Grid Palindromic I
Medium

You are given an m x n binary matrix grid.

A row or column is considered palindromic if its values read the same forward and backward.

You can flip any number of cells in grid from 0 to 1, or from 1 to 0.

Return the minimum number of cells that need to be flipped to make either all rows palindromic or all columns palindromic.


Example 1:

Input: grid = [[1,0,0],[0,0,0],[0,0,1]]
Output: 2
Explanation:
Flipping the highlighted cells makes all the rows palindromic.

Example 2:

Input: grid = [[0,1],[0,1],[0,0]]
Output: 1
Explanation:
Flipping the highlighted cell makes all the columns palindromic.

Example 3:

Input: grid = [[1],[0]]
Output: 0
Explanation:
All rows are already palindromic.


Constraints:

m == grid.length
n == grid[i].length
1 <= m * n <= 2 * 10^5

"""

# V0
# IDEA : COUNT THE MISMATCHED MIRROR PAIRS, ONCE PER ORIENTATION
#
#   making one row palindromic means every mirrored pair inside it must
#   agree, and a disagreeing pair costs exactly one flip (change either
#   side). the pairs are independent, so the row cost is just the number of
#   mismatched pairs across all rows — and the column cost is the same count
#   taken down the columns.
#
#   the task allows satisfying EITHER orientation, so the answer is the
#   smaller of the two totals.
#
#   odd-length lines have a middle cell with no partner, which never costs
#   anything.
#
# time = O(m * n), space = O(1)
class Solution(object):
    def minFlips(self, grid):
        m, n = len(grid), len(grid[0])

        row_cost = 0
        for i in range(m):
            for j in range(n // 2):
                if grid[i][j] != grid[i][n - 1 - j]:
                    row_cost += 1

        col_cost = 0
        for j in range(n):
            for i in range(m // 2):
                if grid[i][j] != grid[m - 1 - i][j]:
                    col_cost += 1

        return min(row_cost, col_cost)


# V0-1
# IDEA : TRANSPOSE THE GRID AND REUSE ONE ROW-COST ROUTINE
#
#   the column question on `grid` IS the row question on `zip(*grid)`, so a
#   single helper that walks each line against its own reverse can answer
#   both halves once the transposed copy is materialised — no index
#   arithmetic on the mirrored coordinates at all.
#
# time = O(m * n), space = O(m * n)   (the transposed copy)
class Solution(object):
    def minFlips(self, grid):
        def line_cost(lines):
            total = 0
            for line in lines:
                line = list(line)
                half = len(line) // 2
                total += sum(1 for a, b in zip(line[:half], line[::-1])
                             if a != b)
            return total

        return min(line_cost(grid), line_cost(zip(*grid)))


# V0-2
# IDEA : BITMASK EACH LINE, XOR IT WITH ITS REVERSE, POPCOUNT
#
#   pack a line into an integer and pack the same line read backwards into a
#   second integer. a set bit in `val ^ rev` marks a position that disagrees
#   with its mirror, and every disagreeing PAIR lights up two such bits (both
#   j and its partner), so the flip count for that line is popcount / 2.
#
#   a middle cell of an odd-length line is compared with itself and can never
#   contribute, which is exactly the wanted behaviour.
#
# time = O(m * n) integer ops, space = O(m + n) for the packed words
class Solution(object):
    def minFlips(self, grid):
        def line_cost(lines):
            total = 0
            for line in lines:
                val = 0
                rev = 0
                for bit in line:
                    val = (val << 1) | bit
                for bit in reversed(list(line)):
                    rev = (rev << 1) | bit
                total += bin(val ^ rev).count("1") // 2
            return total

        return min(line_cost(grid), line_cost(zip(*grid)))
