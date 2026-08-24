"""

3276. Select Cells in Grid With Maximum Score
Hard

You are given a 2D matrix grid consisting of positive integers.

You have to select one or more cells from the matrix such that the following conditions are satisfied:

No two selected cells are in the same row of the matrix.
The values in the set of selected cells are unique.

Your score will be the sum of the values of the selected cells.

Return the maximum score you can achieve.


Example 1:

Input: grid = [[1,2,3],[4,3,2],[1,1,1]]
Output: 8
Explanation:
We can select the cells with values 1, 3, and 4 that are colored above.

Example 2:

Input: grid = [[8,7,6],[8,3,2]]
Output: 15
Explanation:
We can select the cells with values 7 and 8 that are colored above.


Constraints:

1 <= grid.length, grid[i].length <= 10
1 <= grid[i][j] <= 100

"""

# V0
# IDEA : BITMASK OVER THE ROWS, ITERATING THE DISTINCT VALUES
#
#   the two rules pull in different directions : one cell per row, and no
#   value used twice. sweeping the VALUES makes the second rule automatic —
#   process each distinct value once and decide which single row (if any)
#   claims it.
#
#   the state is then just which rows are already occupied, and there are at
#   most 10 rows, so 1024 masks :
#
#       dp[mask] = best score using exactly the rows in `mask`
#       for each value v, for each row r that contains v and is free in mask
#           dp[mask | 1<<r] = max(..., dp[mask] + v)
#
#   iterating values in an outer loop and copying dp each round keeps a value
#   from being taken twice.
#
"""

DP def
    the two rules pull in different directions - one cell per row, and no
    value used twice. sweeping the VALUES makes the second rule automatic:
    process each distinct value once and decide which single row (if any)
    claims it.

    dp[mask]: best score using exactly the rows in `mask`

              (at most 10 rows -> 1024 masks)

DP eq

     for each distinct value v, for each row r containing v and free in mask:

        dp_new[mask | (1 << r)] = max( ..., dp[mask] + v )


    -> e.g. iterating the values in an OUTER loop and copying dp each round
              is what stops one value from being taken twice

     init: dp[0] = 0, everything else unreachable
     ans = max(dp)

"""
# time = O(distinct values * 2^m * m), space = O(2^m)
from collections import defaultdict


class Solution(object):
    def maxScore(self, grid):
        m = len(grid)
        rows_with = defaultdict(list)
        for i, row in enumerate(grid):
            for v in set(row):
                rows_with[v].append(i)

        full = 1 << m
        dp = [-1] * full
        dp[0] = 0

        for v, rows in rows_with.items():
            nxt = dp[:]                       # option : skip this value
            for mask in range(full):
                if dp[mask] < 0:
                    continue
                for r in rows:
                    if not (mask >> r) & 1:
                        cand = dp[mask] + v
                        if cand > nxt[mask | (1 << r)]:
                            nxt[mask | (1 << r)] = cand
            dp = nxt

        return max(dp)
