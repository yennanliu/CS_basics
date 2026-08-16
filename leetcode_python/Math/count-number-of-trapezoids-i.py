"""

3623. Count Number of Trapezoids I
Medium

You are given a 2D integer array points, where points[i] = [xi, yi] represents
the coordinates of the ith point on the Cartesian plane.

A horizontal trapezoid is a convex quadrilateral with at least one pair of
horizontal sides (i.e. parallel to the x-axis). Two lines are parallel if and
only if they have the same slope.

Return the number of unique horizontal trapezoids that can be formed by
choosing any four distinct points from points.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: points = [[1,0],[2,0],[3,0],[2,2],[3,2]]
Output: 3
Explanation:
There are three distinct ways to pick four points that form a horizontal
trapezoid:
Using points [1,0], [2,0], [3,2], and [2,2].
Using points [2,0], [3,0], [3,2], and [2,2].
Using points [1,0], [3,0], [3,2], and [2,2].

Example 2:

Input: points = [[0,0],[1,0],[0,1],[2,1]]
Output: 1
Explanation:
There is only one horizontal trapezoid that can be formed.


Constraints:

4 <= points.length <= 10^5
-10^8 <= xi, yi <= 10^8
All points are pairwise distinct.

"""

# V0
# IDEA : COUNT HORIZONTAL SEGMENTS PER ROW, THEN PAIR ACROSS ROWS
#
#   a horizontal side is just two points sharing a y. so bucket the points by
#   y: a row holding v points contributes C(v, 2) candidate horizontal sides.
#
#   now pick one side from row y1 and one from a different row y2. the four
#   points are automatically distinct, and automatically in convex position —
#   the horizontal line y = y1 meets the triangle of the other three points in
#   a single point, so nothing can sit inside the others. hence every such
#   pair really is a horizontal trapezoid, and the count is just the product
#   of the two rows' side counts.
#
#   no double counting is possible: from the 4-point set the split back into
#   the two sides is forced (2 points per row), and a quadrilateral cannot own
#   two *different* pairs of horizontal sides — that would make all four sides
#   parallel. so a single running prefix sum s of "sides seen so far" gives
#   every cross-row pair exactly once.
#
# time = O(n), space = O(n)
class Solution(object):
    def countTrapezoids(self, points):
        MOD = 10 ** 9 + 7

        cnt = {}
        for _, y in points:
            cnt[y] = cnt.get(y, 0) + 1

        ans = 0
        s = 0
        for v in cnt.values():
            t = v * (v - 1) // 2
            ans = (ans + s * t) % MOD
            s += t
        return ans
