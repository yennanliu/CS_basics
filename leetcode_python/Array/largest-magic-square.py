"""

1895. Largest Magic Square
Medium

A k x k magic square is a k x k grid filled with integers such that every row sum, every column sum, and both diagonal sums are all equal. The integers in the magic square do not have to be distinct. Every 1 x 1 grid is trivially a magic square.

Given an m x n integer grid, return the size (i.e., the side length k) of the largest magic square that can be found within this grid.


Example 1:

Input: grid = [[7,1,4,5,6],[2,5,1,6,4],[1,5,4,3,2],[1,2,7,3,4]]
Output: 3
Explanation: The largest magic square has a size of 3.
Every row sum, column sum, and diagonal sum of this magic square is equal to 12.
- Row sums: 5+1+6 = 5+4+3 = 2+7+3 = 12
- Column sums: 5+5+2 = 1+4+7 = 6+3+3 = 12
- Diagonal sums: 5+4+3 = 6+4+2 = 12

Example 2:

Input: grid = [[5,1,3,1],[9,3,3,1],[1,3,3,8]]
Output: 2


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 50
1 <= grid[i][j] <= 10^6

"""

# V0
# IDEA : PREFIX SUMS PER ROW / PER COLUMN + SHRINKING BRUTE FORCE
#
#   rowPre[i][j] = sum of grid[i][0 .. j-1]
#   colPre[i][j] = sum of grid[0 .. i-1][j]
#   -> any row slice / column slice of a candidate square is O(1).
#
#   try sizes from the largest possible down to 2 and return the first k
#   that has a valid square anywhere; 1 is always achievable.
#   for each top-left corner, verify k row sums, k column sums and the 2
#   diagonals (the diagonals are walked directly, O(k)).
#
#   NOTE : bail out on the FIRST row sum that differs from the reference,
#          which keeps the average cost far below the worst case.
#
# time = O(m * n * min(m,n)^2), space = O(m * n)
class Solution(object):
    def largestMagicSquare(self, grid):
        m, n = len(grid), len(grid[0])

        rowPre = [[0] * (n + 1) for _ in range(m)]
        colPre = [[0] * n for _ in range(m + 1)]
        for i in range(m):
            for j in range(n):
                rowPre[i][j + 1] = rowPre[i][j] + grid[i][j]
                colPre[i + 1][j] = colPre[i][j] + grid[i][j]

        def ok(r, c, k):
            target = rowPre[r][c + k] - rowPre[r][c]
            for i in range(r + 1, r + k):
                if rowPre[i][c + k] - rowPre[i][c] != target:
                    return False
            for j in range(c, c + k):
                if colPre[r + k][j] - colPre[r][j] != target:
                    return False
            d1 = 0
            d2 = 0
            for t in range(k):
                d1 += grid[r + t][c + t]
                d2 += grid[r + t][c + k - 1 - t]
            return d1 == target and d2 == target

        for k in range(min(m, n), 1, -1):
            for r in range(m - k + 1):
                for c in range(n - k + 1):
                    if ok(r, c, k):
                        return k

        return 1
