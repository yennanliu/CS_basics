"""

2128. Remove All Ones With Row and Column Flips
Medium
(premium / locked problem)

You are given an m x n binary matrix grid.

In one operation, you can choose any row or column and flip each value in that row or column (i.e., changing all 0's to 1's, and all 1's to 0's).

Return true if it is possible to remove all 1's from grid using any number of operations or false otherwise.


Example 1:

Input: grid = [[0,1,0],[1,0,1],[0,1,0]]
Output: true
Explanation: One possible way to remove all 1's from grid is to:
- Flip the middle row
- Flip the middle column

Example 2:

Input: grid = [[1,1,0],[0,0,0],[0,0,0]]
Output: false
Explanation: It is impossible to remove all 1's from grid.

Example 3:

Input: grid = [[0]]
Output: true
Explanation: There are no 1's in grid and it is already all 0's.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 300
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : EVERY ROW MUST EQUAL ROW 0 OR ITS EXACT COMPLEMENT
#
#   fix the column flips first : whatever they are, they turn row 0 into all
#   zeros, and that choice is forced (flip column j iff grid[0][j] == 1).
#
#   after those column flips, each remaining row is now either all 0s
#   (nothing left to do) or all 1s (one row flip clears it). "all 0s or all
#   1s after that transformation" is exactly the condition
#       row == row0   or   row == complement(row0)
#
#   so one pass comparing every row against row 0 settles it.
#
# time = O(m * n), space = O(n)
class Solution(object):
    def removeOnes(self, grid):
        first = grid[0]
        flipped = [1 - x for x in first]
        return all(row == first or row == flipped for row in grid)
