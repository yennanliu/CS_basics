"""

3402. Minimum Operations to Make Columns Strictly Increasing
Easy

You are given a m x n matrix grid consisting of non-negative integers.

In one operation, you can increment the value of any grid[i][j] by 1.

Return the minimum number of operations needed to make all columns of grid
strictly increasing.

Example 1:

Input: grid = [[3,2],[1,3],[3,4],[0,1]]

Output: 15

Explanation:

To make the 0^th column strictly increasing, we can apply 3 operations on
grid[1][0], 2 operations on grid[2][0], and 6 operations on grid[3][0].
To make the 1^st column strictly increasing, we can apply 4 operations on
grid[3][1].

Example 2:

Input: grid = [[3,2,1],[2,1,0],[1,2,3]]

Output: 12

Explanation:

To make the 0^th column strictly increasing, we can apply 2 operations on
grid[1][0], and 4 operations on grid[2][0].
To make the 1^st column strictly increasing, we can apply 2 operations on
grid[1][1], and 2 operations on grid[2][1].
To make the 2^nd column strictly increasing, we can apply 2 operations on
grid[1][2].

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 50
0 <= grid[i][j] < 2500

"""

# V0
# IDEA : PER-COLUMN GREEDY, RAISE EACH CELL TO THE MINIMUM LEGAL VALUE
#
#   columns never interact — the only requirement links grid[i][j] with
#   grid[i+1][j] — so each column is an independent problem.
#
#   inside a column values may only go up, so the top cell is untouchable and
#   every following cell must reach at least (previous final value + 1).
#   raising a cell higher than that is never useful: it costs more now and only
#   forces the cells below to be raised further.  therefore the pointwise
#   minimum legal column is also the cheapest one, and it is built in one
#   downward sweep.
#
# time = O(m * n), space = O(1)
class Solution(object):
    def minimumOperations(self, grid):
        m, n = len(grid), len(grid[0])
        ans = 0
        for j in range(n):
            prev = grid[0][j]
            for i in range(1, m):
                need = prev + 1
                if grid[i][j] < need:
                    ans += need - grid[i][j]
                    prev = need
                else:
                    prev = grid[i][j]
        return ans
