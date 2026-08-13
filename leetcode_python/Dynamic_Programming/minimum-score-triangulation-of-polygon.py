"""

1039. Minimum Score Triangulation of Polygon
Medium

You have a convex n-sided polygon where each vertex has an integer value.
You are given an integer array values where values[i] is the value of the ith vertex
in clockwise order.

Polygon triangulation is a process where you divide a polygon into a set of triangles
and the vertices of each triangle must also be vertices of the original polygon.
Note that no other shapes other than triangles are allowed in the division.
This process will result in n - 2 triangles.

You will triangulate the polygon. For each triangle, the weight of that triangle is
the product of the values at its vertices. The total score of the triangulation is
the sum of these weights over all n - 2 triangles.

Return the minimum possible score that you can achieve with some triangulation of
the polygon.


Example 1:

Input: values = [1,2,3]
Output: 6
Explanation: The polygon is already triangulated, and the score of the only
triangle is 6.

Example 2:

Input: values = [3,7,4,5]
Output: 144
Explanation: There are two triangulations, with possible scores:
3*7*5 + 4*5*7 = 245, or 3*4*5 + 3*4*7 = 144.
The minimum score is 144.

Example 3:

Input: values = [1,3,1,4,1,5]
Output: 13
Explanation: The minimum score triangulation is
1*1*3 + 1*1*4 + 1*1*5 + 1*1*1 = 13.


Constraints:

n == values.length
3 <= n <= 50
1 <= values[i] <= 100

"""

# V0
# IDEA: INTERVAL DP
"""
 DP def:
    - dp[i][j] = min score to triangulate the sub-polygon whose vertices are
                 i, i+1, ..., j  (the edge i-j is one of its sides)

 DP eq:
    - dp[i][j] = min over k in (i, j) of
                    dp[i][k] + dp[k][j] + values[i]*values[k]*values[j]
      (k is the third vertex of the triangle sitting on edge (i, j))

 base:
    - dp[i][j] = 0 when j - i < 2  (a line, not a polygon)

 answer: dp[0][n-1]
"""
# time = O(n^3)
# space = O(n^2)
class Solution(object):
    def minScoreTriangulation(self, values):
        n = len(values)
        dp = [[0] * n for _ in range(n)]

        # length = number of vertices in the sub-polygon
        for length in range(3, n + 1):
            for i in range(0, n - length + 1):
                j = i + length - 1
                best = float('inf')
                for k in range(i + 1, j):
                    cur = dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]
                    best = min(best, cur)
                dp[i][j] = best

        return dp[0][n - 1]


# V1
# IDEA: TOP-DOWN MEMOIZATION (same recurrence)
# time = O(n^3)
# space = O(n^2)
class Solution2(object):
    def minScoreTriangulation(self, values):
        n = len(values)
        memo = {}

        def dfs(i, j):
            if j - i < 2:
                return 0
            if (i, j) in memo:
                return memo[(i, j)]
            best = float('inf')
            for k in range(i + 1, j):
                best = min(
                    best,
                    dfs(i, k) + dfs(k, j) + values[i] * values[k] * values[j],
                )
            memo[(i, j)] = best
            return best

        return dfs(0, n - 1)
