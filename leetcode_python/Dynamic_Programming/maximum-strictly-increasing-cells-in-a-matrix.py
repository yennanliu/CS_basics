"""

2713. Maximum Strictly Increasing Cells in a Matrix
Hard

Given a 1-indexed m x n integer matrix mat, you can select any cell in the matrix as your starting cell.

From the starting cell, you can move to any other cell in the same row or column, but only if the value of the destination cell is strictly greater than the value of the current cell. You can repeat this process as many times as possible, moving from cell to cell until you can no longer make any moves.

Your task is to find the maximum number of cells that you can visit in the matrix by starting from some cell.

Return an integer denoting the maximum number of cells that can be visited.


Example 1:

Input: mat = [[3,1],[3,4]]
Output: 2
Explanation: The image shows how we can visit 2 cells starting from row 1, column 2. It can be shown that we cannot visit more than 2 cells no matter where we start from, so the answer is 2.

Example 2:

Input: mat = [[1,1],[1,1]]
Output: 1
Explanation: Since the cells must be strictly increasing, we can only visit one cell in this example.

Example 3:

Input: mat = [[3,1,6],[-9,5,7]]
Output: 4
Explanation: The image above shows how we can visit 4 cells starting from row 2, column 1. It can be shown that we cannot visit more than 4 cells no matter where we start from, so the answer is 4.


Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 10^5
1 <= m * n <= 10^5
-10^5 <= mat[i][j] <= 10^5

"""

# V0
# IDEA : SORT BY VALUE + DP with PER-ROW / PER-COLUMN MAXIMA
#
#   let f[i][j] = longest strictly increasing path ENDING at cell (i, j).
#   a predecessor must sit in row i or column j with a strictly smaller value,
#   so
#       f[i][j] = 1 + max( best f over row i with smaller value,
#                          best f over col j with smaller value )
#
#   process the cells in INCREASING value order and keep
#       rowMax[i] = max f over the cells of row i already processed
#       colMax[j] = max f over the cells of col j already processed
#   then the two maxima above are exactly rowMax[i] and colMax[j].
#
#   NOTE : "strictly" greater is the whole trick for EQUAL values. All cells
#          sharing one value must be handled as a BATCH: first READ
#          rowMax/colMax for every cell of the batch, only THEN write the
#          results back. Interleaving read and write would let one cell use
#          another cell of the same value as its predecessor.
#   NOTE : m * n <= 10^5 but m (or n) alone can be 10^5, so allocate
#          rowMax/colMax by their own lengths, and iterate (no recursion).
#
# time = O(m * n * log(m * n)), space = O(m * n)
class Solution(object):
    def maxIncreasingCells(self, mat):
        m, n = len(mat), len(mat[0])

        groups = {}
        for i in range(m):
            row = mat[i]
            for j in range(n):
                groups.setdefault(row[j], []).append((i, j))

        row_max = [0] * m
        col_max = [0] * n
        ans = 0
        for v in sorted(groups):
            cells = groups[v]
            # pass 1 : read only
            cur = [1 + max(row_max[i], col_max[j]) for i, j in cells]
            # pass 2 : write back
            for k in range(len(cells)):
                i, j = cells[k]
                if cur[k] > row_max[i]:
                    row_max[i] = cur[k]
                if cur[k] > col_max[j]:
                    col_max[j] = cur[k]
                if cur[k] > ans:
                    ans = cur[k]
        return ans
