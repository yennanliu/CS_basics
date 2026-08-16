"""

3625. Count Number of Trapezoids II
Hard

You are given a 2D integer array points where points[i] = [xi, yi] represents
the coordinates of the ith point on the Cartesian plane.

Return the number of unique trapezoids that can be formed by choosing any four
distinct points from points.

A trapezoid is a convex quadrilateral with at least one pair of parallel sides.
Two lines are parallel if and only if they have the same slope.


Example 1:

Input: points = [[-3,2],[3,0],[2,3],[3,2],[2,-3]]
Output: 2
Explanation:
There are two distinct ways to pick four points that form a trapezoid:
The points [-3,2], [2,3], [3,2], [2,-3] form one trapezoid.
The points [2,3], [3,2], [3,0], [2,-3] form another trapezoid.

Example 2:

Input: points = [[0,0],[1,0],[0,1],[2,1]]
Output: 1
Explanation:
There is only one trapezoid which can be formed.


Constraints:

4 <= points.length <= 500
-1000 <= xi, yi <= 1000
All points are pairwise distinct.

"""

# V0
# IDEA : PAIR UP PARALLEL SEGMENTS, THEN UNDO THE PARALLELOGRAM DOUBLE COUNT
#
#   take any two segments that are parallel but lie on *different* lines.
#   two points sit on each of two distinct parallel lines, so the four points
#   are distinct and in convex position (a line meets the triangle of the
#   other three in one point, so none can be interior). their hull is exactly
#   a trapezoid whose parallel sides are those two segments. so every such
#   segment pair yields one trapezoid, and conversely every trapezoid arises
#   from a pair of its parallel sides.
#
#   the map is not quite a bijection. a convex quadrilateral has three ways to
#   split its 4 vertices into two segments: the two pairs of opposite sides
#   and the pair of diagonals. diagonals cross, so they are never parallel;
#   therefore a quadrilateral is generated once if it has one pair of parallel
#   sides, and twice if it has two — i.e. exactly when it is a parallelogram.
#   subtract the parallelograms once and every trapezoid is counted once.
#
#   parallelograms are counted by the diagonal property: the diagonals bisect
#   each other, so a parallelogram is exactly an unordered pair of segments
#   sharing a midpoint and having different directions. (same midpoint plus
#   same direction means the two segments are collinear — a degenerate flat
#   figure, correctly excluded.) no 4-point set is hit twice here either: if
#   A+B = C+D and A+C = B+D then B = C.
#
#   everything is kept in exact integers — direction reduced by gcd with a
#   canonical sign, line id = dy*x - dx*y, midpoint id = (x1+x2, y1+y2) —
#   so no float slope ever has to be compared for equality.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def countTrapezoids(self, points):
        def norm(dx, dy):
            a, b = abs(dx), abs(dy)
            while b:
                a, b = b, a % b
            g = a
            dx //= g
            dy //= g
            if dx < 0 or (dx == 0 and dy < 0):
                dx, dy = -dx, -dy
            return dx, dy

        n = len(points)

        by_dir = {}   # direction -> {line id: how many segments on that line}
        by_mid = {}   # midpoint  -> {direction: how many segments}

        for i in range(n):
            x1, y1 = points[i]
            for j in range(i):
                x2, y2 = points[j]
                dx, dy = norm(x2 - x1, y2 - y1)

                line = dy * x1 - dx * y1
                d = by_dir.get((dx, dy))
                if d is None:
                    d = by_dir[(dx, dy)] = {}
                d[line] = d.get(line, 0) + 1

                mid = (x1 + x2, y1 + y2)
                m = by_mid.get(mid)
                if m is None:
                    m = by_mid[mid] = {}
                m[(dx, dy)] = m.get((dx, dy), 0) + 1

        ans = 0
        # same direction, different line -> a trapezoid
        for d in by_dir.values():
            s = 0
            for t in d.values():
                ans += s * t
                s += t
        # same midpoint, different direction -> a parallelogram, counted twice
        for m in by_mid.values():
            s = 0
            for t in m.values():
                ans -= s * t
                s += t
        return ans
