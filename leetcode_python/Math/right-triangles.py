"""

3128. Right Triangles
Medium

You are given a 2D boolean matrix grid.

Return an integer that is the number of right triangles that can be made with the 3 elements of grid such that all of them have a value of 1.

Note:

A collection of 3 elements of grid is a right triangle if one of its elements is in the same row with another element and in the same column with the third element. The 3 elements do not have to be next to each other.


Example 1:

Input: grid = [[0,1,0],[0,1,1],[0,1,0]]
Output: 2
Explanation:
There are two right triangles.

Example 2:

Input: grid = [[1,0,0,0],[0,1,0,1],[1,0,0,0]]
Output: 0
Explanation:
There are no right triangles.

Example 3:

Input: grid = [[1,0,1],[1,0,0],[1,0,0]]
Output: 2
Explanation:
There are two right triangles.


Constraints:

1 <= grid.length <= 1000
1 <= grid[i].length <= 1000
0 <= grid[i][j] <= 1

"""

# V0
# IDEA : COUNT BY THE CORNER — THE CELL THAT SUPPLIES BOTH LEGS
#
#   the definition names one distinguished element : the one sharing a row
#   with the second and a column with the third. treat that as the RIGHT-ANGLE
#   corner and count each triangle exactly once, from its corner.
#
#   for a corner at (i, j) the partners are free to be any other 1 in row i
#   and any other 1 in column j, so it contributes
#
#       (row_ones[i] - 1) * (col_ones[j] - 1)
#
#   two O(m*n) passes — one to tally the row and column sums, one to add up
#   the products.
#
# time = O(m * n), space = O(m + n)
class Solution(object):
    def numberOfRightTriangles(self, grid):
        m, n = len(grid), len(grid[0])
        row_ones = [sum(row) for row in grid]
        col_ones = [0] * n
        for i in range(m):
            for j in range(n):
                col_ones[j] += grid[i][j]

        res = 0
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    res += (row_ones[i] - 1) * (col_ones[j] - 1)
        return res
