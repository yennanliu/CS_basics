"""

2371. Minimize Maximum Value in a Grid
Hard
(premium / locked problem)

You are given an m x n integer matrix grid containing distinct positive integers.

You have to replace each integer in the matrix with a positive integer satisfying the following conditions:

The relative order of every two elements that are in the same row or column should stay the same after the replacements.
The maximum number in the matrix after the replacements should be as small as possible.

The relative order stays the same if for all pairs of elements in the original matrix such that grid[r1][c1] > grid[r2][c2] where either r1 == r2 or c1 == c2, then it must be true that grid[r1][c1] > grid[r2][c2] after the replacements.

For example, if grid = [[2, 4, 5], [7, 3, 9]] then a good replacement could be either grid = [[1, 2, 3], [2, 1, 4]] or grid = [[1, 2, 3], [3, 1, 4]].

Return the resulting matrix. If there are multiple answers, return any of them.


Example 1:

Input: grid = [[3,1],[2,5]]
Output: [[2,1],[1,2]]
Explanation: The above diagram shows a valid replacement.
The maximum number in the matrix is 2. It can be shown that we cannot get a lower value.

Example 2:

Input: grid = [[10]]
Output: [[1]]
Explanation: We replace the only number in the matrix with 1.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 1000
1 <= m * n <= 10^5
1 <= grid[i][j] <= 10^9
grid[i][j] is unique.

"""

# V0
# IDEA : ASSIGN VALUES IN INCREASING ORDER, EACH AS SMALL AS ITS ROW/COLUMN ALLOWS
#
#   process the cells from smallest original value to largest. when a cell is
#   assigned, everything already placed in its row or column is strictly
#   smaller than it, so the tightest legal choice is
#       max(row_max[i], col_max[j]) + 1
#   and taking that minimum at every step is optimal — a greedy exchange
#   argument, since raising any assignment can only raise later ones.
#
#   then update both running maxima. values are distinct, so the ordering has
#   no ties to break.
#
# time = O(m * n log(m * n)), space = O(m * n)
class Solution(object):
    def minScore(self, grid):
        m, n = len(grid), len(grid[0])
        cells = sorted((grid[i][j], i, j) for i in range(m) for j in range(n))

        row_max = [0] * m
        col_max = [0] * n
        res = [[0] * n for _ in range(m)]

        for _, i, j in cells:
            value = max(row_max[i], col_max[j]) + 1
            res[i][j] = value
            row_max[i] = value
            col_max[j] = value
        return res
