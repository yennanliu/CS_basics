"""

2319. Check if Matrix Is X-Matrix
Easy

A square matrix is said to be an X-Matrix if both of the following conditions hold:

All the elements in the diagonals of the matrix are non-zero.
All other elements are 0.

Given a 2D integer array grid of size n x n representing a square matrix, return true if grid is an X-Matrix. Otherwise, return false.


Example 1:

Input: grid = [[2,0,0,1],[0,3,1,0],[0,5,2,0],[4,0,0,2]]
Output: true
Explanation: Refer to the diagram above.
An X-Matrix should have the green elements (diagonals) be non-zero and the red elements be 0.
Thus, grid is an X-Matrix.

Example 2:

Input: grid = [[5,7,0],[0,3,1],[0,5,0]]
Output: false
Explanation: Refer to the diagram above.
An X-Matrix should have the green elements (diagonals) be non-zero and the red elements be 0.
Thus, grid is not an X-Matrix.


Constraints:

n == grid.length == grid[i].length
3 <= n <= 100
0 <= grid[i][j] <= 10^5

"""

# V0
# IDEA : DIRECT SCAN (a cell is on the X iff i == j or i + j == n - 1)
#
#   walk every cell once:
#     - on a diagonal -> the value must be non-zero
#     - off both diagonals -> the value must be zero
#   any violation ends the scan immediately.
#
#   NOTE : for odd n the centre cell satisfies both conditions at once;
#          the "on diagonal" branch already covers it correctly.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def checkXMatrix(self, grid):
        n = len(grid)
        for i in range(n):
            for j in range(n):
                on_x = (i == j or i + j == n - 1)
                if on_x:
                    if grid[i][j] == 0:
                        return False
                elif grid[i][j] != 0:
                    return False
        return True
