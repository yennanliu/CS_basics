"""

3071. Minimum Operations to Write the Letter Y on a Grid
Medium

You are given a 0-indexed n x n grid where n is odd, and grid[r][c] is 0, 1, or 2.

We say that a cell belongs to the Letter Y if it belongs to one of the following:

The diagonal starting at the top-left cell and ending at the center cell of the grid.
The diagonal starting at the top-right cell and ending at the center cell of the grid.
The vertical line starting at the center cell and ending at the bottom border of the grid.

The Letter Y is written on the grid if and only if:

All values at cells belonging to the Y are equal.
All values at cells not belonging to the Y are equal.
The values at cells belonging to the Y are different from the values at cells not belonging to the Y.

Return the minimum number of operations needed to write the letter Y on the grid given that in one operation you can change the value at any cell to 0, 1, or 2.


Example 1:

Input: grid = [[1,2,2],[1,1,0],[0,1,0]]
Output: 3
Explanation: We can write Y on the grid by applying the changes highlighted in blue in the image above. After the operations, all cells that belong to Y, denoted in bold, have the same value of 1 while those that do not belong to Y are equal to 0.
It can be shown that 3 is the minimum number of operations needed to write Y on the grid.

Example 2:

Input: grid = [[0,1,0,1,0],[2,1,0,1,2],[2,2,2,0,1],[2,2,2,2,2],[2,1,2,2,2]]
Output: 12
Explanation: We can write Y on the grid by applying the changes highlighted in blue in the image above. After the operations, all cells that belong to Y, denoted in bold, have the same value of 0 while those that do not belong to Y are equal to 2.
It can be shown that 12 is the minimum number of operations needed to write Y on the grid.


Constraints:

3 <= n <= 49
n == grid.length == grid[i].length
0 <= grid[i][j] <= 2
n is odd.

"""

# V0
# IDEA : ONLY 6 TARGET COLOURINGS EXIST — COUNT ONCE, THEN SCORE EACH
#
#   the final grid is fully described by two values : `a` on the Y and `b`
#   off it, with a != b. that is only 3 * 2 = 6 possibilities.
#
#   so tally, once, how many Y cells already hold each value and how many
#   non-Y cells hold each value. the cost of a choice (a, b) is
#
#       (Y cells not already a) + (non-Y cells not already b)
#
#   and the answer is the cheapest of the six.
#
#   membership in the Y is a coordinate test around the centre m = n // 2 :
#       above the centre  -> the two diagonals,  r == c or r + c == n - 1
#       at or below it    -> the stem,           c == m
#
# time = O(n^2), space = O(1)
class Solution(object):
    def minimumOperationsToWriteY(self, grid):
        n = len(grid)
        m = n // 2
        in_y = [0, 0, 0]
        out_y = [0, 0, 0]

        for r in range(n):
            for c in range(n):
                if (r < m and (r == c or r + c == n - 1)) or (r >= m and c == m):
                    in_y[grid[r][c]] += 1
                else:
                    out_y[grid[r][c]] += 1

        total_in = sum(in_y)
        total_out = sum(out_y)
        best = float('inf')
        for a in range(3):
            for b in range(3):
                if a == b:
                    continue
                best = min(best, (total_in - in_y[a]) + (total_out - out_y[b]))
        return best
