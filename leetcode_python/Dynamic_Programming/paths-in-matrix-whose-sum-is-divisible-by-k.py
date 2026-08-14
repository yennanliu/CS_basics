"""

2435. Paths in Matrix Whose Sum Is Divisible by K
Hard

You are given a 0-indexed m x n integer matrix grid and an integer k. You are currently at position (0, 0) and you want to reach position (m - 1, n - 1) moving only down or right.

Return the number of paths where the sum of the elements on the path is divisible by k. Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: grid = [[5,2,4],[3,0,5],[0,7,2]], k = 3
Output: 2
Explanation: There are two paths where the sum of the elements on the path is divisible by k.
The first path highlighted in red has a sum of 5 + 2 + 4 + 5 + 2 = 18 which is divisible by 3.
The second path highlighted in blue has a sum of 5 + 3 + 0 + 5 + 2 = 15 which is divisible by 3.

Example 2:

Input: grid = [[0,0]], k = 5
Output: 1
Explanation: The path highlighted in red has a sum of 0 + 0 = 0 which is divisible by 5.

Example 3:

Input: grid = [[7,3,4,9],[2,3,6,2],[2,3,7,0]], k = 1
Output: 10
Explanation: Every integer is divisible by 1 so the sum of the elements on every possible path is divisible by k.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 5 * 10^4
1 <= m * n <= 5 * 10^4
0 <= grid[i][j] <= 100
1 <= k <= 50

"""

# V0
# IDEA : DP ON (cell, remainder) (carry the running sum modulo k as an extra state)
#
#   dp[i][j][r] = number of paths from (0,0) to (i,j) whose sum % k == r.
#   from (i,j) we only came from (i-1,j) or (i,j-1), so
#     dp[i][j][r] = dp[i-1][j][r'] + dp[i][j-1][r']   with r' = (r - grid[i][j]) % k
#   answer is dp[m-1][n-1][0].
#
#   NOTE : m * n <= 5 * 10^4 and k <= 50 -> 2.5 * 10^6 states, fine.
#          keep only the previous row so the memory stays O(n * k).
#
# time = O(m * n * k), space = O(n * k)
class Solution(object):
    def numberOfPaths(self, grid, k):
        MOD = 10 ** 9 + 7
        m, n = len(grid), len(grid[0])

        # prev[j][r] : paths reaching (i-1, j) with remainder r
        prev = [[0] * k for _ in range(n)]
        for i in range(m):
            cur = [[0] * k for _ in range(n)]
            for j in range(n):
                v = grid[i][j] % k
                if i == 0 and j == 0:
                    cur[0][v] = 1
                    continue
                cell = cur[j]
                if i > 0:
                    up = prev[j]
                    for r in range(k):
                        cell[(r + v) % k] += up[r]
                if j > 0:
                    left = cur[j - 1]
                    for r in range(k):
                        cell[(r + v) % k] += left[r]
                for r in range(k):
                    cell[r] %= MOD
            prev = cur

        return prev[n - 1][0] % MOD
