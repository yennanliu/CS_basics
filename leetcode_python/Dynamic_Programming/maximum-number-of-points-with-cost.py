"""

1937. Maximum Number of Points with Cost
Medium

You are given an m x n integer matrix points (0-indexed). Starting with 0 points, you want to maximize the number of points you can get from the matrix.

To gain points, you must pick one cell in each row. Picking the cell at coordinates (r, c) will add points[r][c] to your score.

However, you will lose points if you pick a cell too far from the cell that you picked in the previous row. For every two adjacent rows r and r + 1 (where 0 <= r < m - 1), picking cells at coordinates (r, c1) and (r + 1, c2) will subtract abs(c1 - c2) from your score.

Return the maximum number of points you can achieve.

abs(x) is defined as:

x for x >= 0.
-x for x < 0.


Example 1:

Input: points = [[1,2,3],[1,5,1],[3,1,1]]
Output: 9
Explanation:
The blue cells denote the optimal cells to pick, which have coordinates (0, 2), (1, 1), and (2, 0).
You add 3 + 5 + 3 = 11 to your score.
However, you must subtract abs(2 - 1) + abs(1 - 0) = 2 from your score.
Your final score is 11 - 2 = 9.

Example 2:

Input: points = [[1,5],[2,3],[4,2]]
Output: 11
Explanation:
The blue cells denote the optimal cells to pick, which have coordinates (0, 1), (1, 1), and (2, 0).
You add 5 + 3 + 4 = 12 to your score.
However, you must subtract abs(1 - 1) + abs(1 - 0) = 1 from your score.
Your final score is 12 - 1 = 11.


Constraints:

m == points.length
n == points[r].length
1 <= m, n <= 10^5
1 <= m * n <= 10^5
0 <= points[r][c] <= 10^5

"""

# V0
# IDEA : ROW DP + PREFIX/SUFFIX MAX (split the abs() into two directions)
#
#   naive transition is g[j] = p[j] + max over k of (f[k] - |k - j|) -> O(n^2).
#   split by the sign of (k - j) to drop the absolute value :
#
#     k <= j :  f[k] - (j - k) = (f[k] + k) - j   -> keep a running max of f[k]+k
#                                                    while sweeping LEFT -> RIGHT
#     k >= j :  f[k] - (k - j) = (f[k] - k) + j   -> keep a running max of f[k]-k
#                                                    while sweeping RIGHT -> LEFT
#
#   two linear sweeps per row give the same answer in O(n).
#
#   NOTE : k == j is covered by both sweeps, which is harmless (cost 0).
#
"""

DP def
    dp[r][j]: MAX points collectable having picked column j on row r

DP eq

     the naive form is O(n^2) per row:

        dp[r][j] = points[r][j] + max over k of ( dp[r-1][k] - |k - j| )

     SPLIT by the sign of (k - j) to drop the absolute value:

        k <= j :  dp[r-1][k] - (j - k) = (dp[r-1][k] + k) - j
                  -> running max of dp[k] + k, sweeping LEFT -> RIGHT

        k >= j :  dp[r-1][k] - (k - j) = (dp[r-1][k] - k) + j
                  -> running max of dp[k] - k, sweeping RIGHT -> LEFT


    -> e.g. two linear sweeps per row give the same answer in O(n)

     NOTE !!! k == j is covered by BOTH sweeps, which is harmless (cost 0)

     init: dp[0] = points[0]
     ans = max(dp[m-1])

"""
# time = O(m * n), space = O(n)
class Solution(object):
    def maxPoints(self, points):
        n = len(points[0])
        f = points[0][:]
        NEG = float('-inf')
        for r in range(1, len(points)):
            p = points[r]
            g = [NEG] * n

            best = NEG                      # max of f[k] + k for k <= j
            for j in range(n):
                if f[j] + j > best:
                    best = f[j] + j
                g[j] = p[j] + best - j

            best = NEG                      # max of f[k] - k for k >= j
            for j in range(n - 1, -1, -1):
                if f[j] - j > best:
                    best = f[j] - j
                cand = p[j] + best + j
                if cand > g[j]:
                    g[j] = cand

            f = g
        return max(f)
