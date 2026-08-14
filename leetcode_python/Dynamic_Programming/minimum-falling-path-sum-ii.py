"""

1289. Minimum Falling Path Sum II
Hard

Given an n x n integer matrix grid, return the minimum sum of a falling path
with non-zero shifts.

A falling path with non-zero shifts is a choice of exactly one element from each
row of grid such that no two elements chosen in adjacent rows are in the same column.


Example 1:

Input: grid = [[1,2,3],[4,5,6],[7,8,9]]
Output: 13
Explanation:
The possible falling paths are:
[1,5,9], [1,5,7], [1,6,7], [1,6,8],
[2,4,8], [2,4,9], [2,6,7], [2,6,8],
[3,4,8], [3,4,9], [3,5,7], [3,5,9]
The falling path with the smallest sum is [1,5,7], so the answer is 13.

Example 2:

Input: grid = [[7]]
Output: 7


Constraints:

n == grid.length == grid[i].length
1 <= n <= 200
-99 <= grid[i][j] <= 99

"""

# V0
# IDEA: 1D DP (rolling array)
"""

 DP def:
    - dp[j] = min falling path sum ending at column j of the current row


 DP eq:
    - new_dp[j] = grid[i][j] + min(dp[k] for k != j)
"""
# time = O(n^3)
# space = O(n)
class Solution(object):
    def minFallingPathSum(self, grid):
        n = len(grid)

        # edge
        if n == 1:
            return grid[0][0]

        dp = [0] * n
        for row in grid:
            new_dp = [0] * n
            for j in range(n):
                """
                NOTE !!!

                the ONLY constraint is `k != j`
                -> we may NOT reuse the same column as the previous row
                """
                best = min(dp[k] for k in range(n) if k != j)
                new_dp[j] = row[j] + best
            dp = new_dp

        return min(dp)


# V1
# IDEA: 1D DP + track the 2 SMALLEST values of the previous row
#
#  -> min(dp[k] for k != j) is either the smallest value of dp
#     (when its column != j) or the 2nd smallest (when it IS j).
#     so we only need (min1, idx1, min2) instead of a full inner scan.
#
# time = O(n^2)
# space = O(n)
class Solution(object):
    def minFallingPathSum(self, grid):
        n = len(grid)

        # edge
        if n == 1:
            return grid[0][0]

        INF = float("inf")
        # min1 = smallest so far, idx1 = its column, min2 = 2nd smallest
        min1, idx1, min2 = 0, -1, 0

        for row in grid:
            n_min1, n_idx1, n_min2 = INF, -1, INF
            for j in range(n):
                # pick min1 unless it sits on the same column j
                prev = min2 if j == idx1 else min1
                cur = row[j] + prev

                if cur < n_min1:
                    n_min2 = n_min1
                    n_min1, n_idx1 = cur, j
                elif cur < n_min2:
                    n_min2 = cur

            min1, idx1, min2 = n_min1, n_idx1, n_min2

        return min1
