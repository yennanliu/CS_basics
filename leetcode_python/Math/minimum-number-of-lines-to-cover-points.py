"""

2152. Minimum Number of Lines to Cover Points
Medium
(premium / locked problem)

You are given an array points where points[i] = [xi, yi] represents a point on an X-Y plane.

Straight lines are going to be added to the X-Y plane, such that every point is covered by at least one line.

Return the minimum number of straight lines needed to cover all the points.


Example 1:

Input: points = [[0,1],[2,3],[4,5],[4,3]]
Output: 2
Explanation: The minimum number of straight lines needed is two. One possible solution is to add:
- One line connecting the point at (0, 1) to the point at (4, 5).
- Another line connecting the point at (2, 3) to the point at (4, 3).

Example 2:

Input: points = [[0,2],[-2,-2],[1,4]]
Output: 1
Explanation: The minimum number of straight lines needed is one. The only solution is to add:
- One line connecting the point at (-2, -2) to the point at (1, 4).


Constraints:

1 <= points.length <= 10
points[i].length == 2
-100 <= xi, yi <= 100
All the points are unique.

"""

# V0
# IDEA : BITMASK DP OVER UNCOVERED POINTS (n <= 10)
#
#   any useful line passes through at least two points (a line for a single
#   leftover point costs 1 and covers only it), so precompute for every PAIR
#   (i, j) the mask of all points collinear with them.
#
#   dp(mask) = fewest lines covering the points still in `mask`. always
#   handle the LOWEST remaining point i — it must be covered by some line, so
#   either it pairs with some other remaining j (remove line[i][j]) or it is
#   alone and costs one line. fixing i this way kills the permutation
#   redundancy.
#
#   collinearity is tested with the CROSS PRODUCT, keeping everything in
#   exact integer arithmetic (no slopes, no division by zero).
#
# time = O(2^n * n), space = O(2^n)
class Solution(object):
    def minimumLines(self, points):
        n = len(points)
        if n <= 2:
            return 1

        def collinear(a, b, c):
            (x1, y1), (x2, y2), (x3, y3) = points[a], points[b], points[c]
            return (x2 - x1) * (y3 - y1) == (y2 - y1) * (x3 - x1)

        line = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(i + 1, n):
                m = 0
                for k in range(n):
                    if collinear(i, j, k):
                        m |= 1 << k
                line[i][j] = m

        memo = {}

        def dp(mask):
            if mask == 0:
                return 0
            if mask in memo:
                return memo[mask]
            i = (mask & -mask).bit_length() - 1     # lowest uncovered point
            rest = mask ^ (1 << i)
            if rest == 0:
                memo[mask] = 1
                return 1
            best = float('inf')
            for j in range(n):
                if rest >> j & 1:
                    a, b = (i, j) if i < j else (j, i)
                    best = min(best, 1 + dp(mask & ~line[a][b]))
            memo[mask] = best
            return best

        return dp((1 << n) - 1)
