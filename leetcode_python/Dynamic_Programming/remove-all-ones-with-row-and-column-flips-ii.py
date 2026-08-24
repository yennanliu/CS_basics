"""

2174. Remove All Ones With Row and Column Flips II
Medium
(premium / locked problem)

You are given a 0-indexed m x n binary matrix grid.

In one operation, you can choose any i and j that meet the following conditions:

0 <= i < m
0 <= j < n
grid[i][j] == 1

and change the values of all cells in row i and column j to zero.

Return the minimum number of operations needed to remove all 1's from grid.


Example 1:

Input: grid = [[1,1,1],[1,1,1],[0,1,0]]
Output: 2
Explanation:
In the first operation, change all cell values of row 1 and column 1 to zero.
In the second operation, change all cell values of row 0 and column 0 to zero.

Example 2:

Input: grid = [[0,1,0],[1,0,1],[0,1,0]]
Output: 2
Explanation:
In the first operation, change all cell values of row 1 and column 1 to zero.
In the second operation, change all cell values of row 2 and column 2 to zero.
Note that we cannot perform an operation using row 1 and column 1 because grid[1][1] != 1.

Example 3:

Input: grid = [[0,0],[0,0]]
Output: 0
Explanation:
There are no 1's to remove so return zero.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 15
1 <= m * n <= 15
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : THE WHOLE GRID FITS IN 15 BITS -> BFS / MEMOISED SEARCH OVER STATES
#
#   m * n <= 15, so encode the entire matrix as a single bitmask (cell (i, j)
#   is bit i * n + j). an operation at a cell holding a 1 clears its whole row
#   and column, which is one precomputed AND-mask per cell.
#
#   dp(state) = 0 when state == 0, else
#               1 + min over cells set in state of dp(state & clear[cell])
#
#   memoise on the state; at most 2^15 of them, each branching over <= 15
#   cells.
#
"""

DP def
    m * n <= 15, so the WHOLE grid is one bitmask (cell (i, j) is bit i*n + j).
    an operation at a cell holding a 1 clears its entire row and column, which
    is one precomputed AND-mask per cell.

    dp(state): MIN operations to clear the grid described by `state`

    clear[c] : the mask to AND with, wiping cell c's row and column

DP eq

     dp(0) = 0

     dp(state) = 1 + min over cells c SET in state of

                    dp( state & clear[c] )


    -> e.g. memoise on the state - at most 2^15 of them, each branching
              over <= 15 cells

     ans = dp(initial state)

"""
# time = O(2^(m*n) * m * n), space = O(2^(m*n))
class Solution(object):
    def removeOnes(self, grid):
        m, n = len(grid), len(grid[0])
        total = m * n

        state = 0
        for i in range(m):
            for j in range(n):
                if grid[i][j]:
                    state |= 1 << (i * n + j)

        # clear[c] : mask to AND with, wiping cell c's row and column
        clear = [0] * total
        for i in range(m):
            for j in range(n):
                wipe = 0
                for y in range(n):
                    wipe |= 1 << (i * n + y)
                for x in range(m):
                    wipe |= 1 << (x * n + j)
                clear[i * n + j] = ((1 << total) - 1) ^ wipe

        memo = {0: 0}

        def dp(s):
            if s in memo:
                return memo[s]
            best = float('inf')
            for c in range(total):
                if s >> c & 1:
                    best = min(best, 1 + dp(s & clear[c]))
            memo[s] = best
            return best

        return dp(state)
