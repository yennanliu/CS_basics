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


# V0-1
# IDEA : PREFIX SUMS IN ALL FOUR DIRECTIONS + GROW k UPWARDS
#
#   on top of the row / column prefix sums, precompute the two DIAGONAL
#   prefix sums as well :
#       dg[i][j] = grid[i-1][j-1] + dg[i-1][j-1]      (down-right runs)
#       ag[i][j] = grid[i-1][j-1] + ag[i-1][j+1]      (down-left  runs)
#   then for a square of side k at (r, c) :
#       main diagonal = dg[r+k][c+k] - dg[r][c]
#       anti diagonal = ag[r+k][c+1] - ag[r][c+k+1]
#   i.e. BOTH diagonals become O(1) instead of the O(k) walk of V0.
#
#   here k grows from 2 upwards and the best k seen is kept, which makes the
#   diagonal test (the cheapest one now) the first filter of each candidate.
#
# time = O(m * n * min(m,n)^2), space = O(m * n)
class Solution(object):
    def largestMagicSquare(self, grid):
        m, n = len(grid), len(grid[0])

        rowPre = [[0] * (n + 1) for _ in range(m + 1)]
        colPre = [[0] * (n + 1) for _ in range(m + 1)]
        dg = [[0] * (n + 2) for _ in range(m + 2)]
        ag = [[0] * (n + 2) for _ in range(m + 2)]
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                v = grid[i - 1][j - 1]
                rowPre[i][j] = rowPre[i][j - 1] + v
                colPre[i][j] = colPre[i - 1][j] + v
                dg[i][j] = dg[i - 1][j - 1] + v
        for i in range(1, m + 1):
            for j in range(n, 0, -1):
                ag[i][j] = ag[i - 1][j + 1] + grid[i - 1][j - 1]

        def ok(r, c, k):
            # r, c are 0-based top-left of the k x k square
            target = dg[r + k][c + k] - dg[r][c]
            if ag[r + k][c + 1] - ag[r][c + k + 1] != target:
                return False
            for i in range(r + 1, r + k + 1):
                if rowPre[i][c + k] - rowPre[i][c] != target:
                    return False
            for j in range(c + 1, c + k + 1):
                if colPre[r + k][j] - colPre[r][j] != target:
                    return False
            return True

        best = 1
        for k in range(2, min(m, n) + 1):
            found = False
            for r in range(m - k + 1):
                for c in range(n - k + 1):
                    if ok(r, c, k):
                        found = True
                        break
                if found:
                    break
            if found:
                best = k
        return best


# V0-2
# IDEA : PURE BRUTE FORCE - RE-ADD EVERY CELL OF EVERY CANDIDATE SQUARE
#
#   no precomputation at all : for each side k (largest first) and each
#   top-left corner, sum the k rows, the k columns and the 2 diagonals
#   directly from grid and compare them. Return on the first hit.
#
#   this is the O(k^2)-per-square baseline the prefix-sum versions above
#   optimise; kept because m, n <= 50 makes it still viable and it needs no
#   auxiliary memory.
#
# time = O(m * n * min(m,n)^3), space = O(1)
class Solution(object):
    def largestMagicSquare(self, grid):
        m, n = len(grid), len(grid[0])

        def ok(r, c, k):
            target = sum(grid[r][c:c + k])
            for i in range(r + 1, r + k):
                if sum(grid[i][c:c + k]) != target:
                    return False
            for j in range(c, c + k):
                if sum(grid[i][j] for i in range(r, r + k)) != target:
                    return False
            if sum(grid[r + t][c + t] for t in range(k)) != target:
                return False
            if sum(grid[r + t][c + k - 1 - t] for t in range(k)) != target:
                return False
            return True

        for k in range(min(m, n), 1, -1):
            for r in range(m - k + 1):
                for c in range(n - k + 1):
                    if ok(r, c, k):
                        return k
        return 1
