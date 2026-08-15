"""

2128. Remove All Ones With Row and Column Flips
Medium
🔒 (premium)

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
Explanation: There are no 1's in grid and it is already all zero.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 300
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : EVERY ROW MUST EQUAL ROW 0, OR BE ITS EXACT COMPLEMENT
#
#   column flips act on every row identically, so they can never change the
#   RELATIONSHIP between two rows — two rows that agree in a column keep
#   agreeing, two that differ keep differing. a row flip only inverts one
#   whole row.
#
#   so the grid is clearable iff, after the column flips that zero out row 0,
#   every other row is already all-0 or all-1 — which is exactly the
#   condition "row i equals row 0, or is its bitwise complement".
#
# time = O(m * n), space = O(n)
class Solution(object):
    def removeOnes(self, grid):
        first = grid[0]
        flipped = [1 - x for x in first]
        for row in grid[1:]:
            if row != first and row != flipped:
                return False
        return True
