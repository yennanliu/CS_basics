"""

963. Minimum Area Rectangle II
Medium

You are given an array of points in the X-Y plane points where points[i] = [x_i, y_i].

Return the minimum area of any rectangle formed from these points, with sides not necessarily parallel to the X and Y axes. If there is not any such rectangle, return 0.

Answers within 10^-5 of the actual answer will be accepted.

Example 1:

https://assets.leetcode.com/uploads/2018/12/21/1a.png

Input: points = [[1,2],[2,1],[1,0],[0,1]]
Output: 2.00000
Explanation: The minimum area rectangle occurs at [1,2],[2,1],[1,0],[0,1], with an area of 2.

Example 2:

https://assets.leetcode.com/uploads/2018/12/22/2.png

Input: points = [[0,1],[2,1],[1,1],[1,0],[2,0]]
Output: 1.00000
Explanation: The minimum area rectangle occurs at [1,0],[1,1],[2,1],[2,0], with an area of 1.

Example 3:

https://assets.leetcode.com/uploads/2018/12/22/3.png

Input: points = [[0,3],[1,2],[3,1],[1,3],[2,1]]
Output: 0
Explanation: There is no possible rectangle to form from these points.

Constraints:

1 <= points.length <= 50
points[i].length == 2
0 <= x_i, y_i <= 4 * 10^4
All the given points are unique.

"""

# V0
# IDEA : GROUP POINT PAIRS BY DIAGONAL (MIDPOINT + LENGTH)
#
#   a quadrilateral is a rectangle  <=>  its two diagonals share the same
#   midpoint AND have the same length. so treat every pair of points as a
#   possible diagonal and bucket it by
#       key = (x1 + x2, y1 + y2, squared length)
#   (sums instead of midpoints keep everything in integers).
#
#   any two pairs in the same bucket are the two diagonals of a rectangle
#   with corners p1, p3, p2, p4 — the sides are |p1 p3| and |p1 p4|.
#
#   e.g. [1,2]-[1,0] and [2,1]-[0,1] : both sum to (2,2), both length^2 4
#        -> sides sqrt(2) * sqrt(2) -> area 2
#
#   brute force over 4 points is O(n^4); see V1 for the O(n^3) corner check.
#
# time = O(n^2) pairs + O(sum of bucket_size^2) to match them, worst O(n^4)
#        in theory, near O(n^2) in practice
# space = O(n^2)
import math
from collections import defaultdict


class Solution(object):
    def minAreaFreeRect(self, points):
        """
        :type points: List[List[int]]
        :rtype: float
        """
        n = len(points)

        # (sum_x, sum_y, squared_length) -> list of point pairs on that diagonal
        diagonals = defaultdict(list)
        for i in range(n):
            x1, y1 = points[i]
            for j in range(i + 1, n):
                x2, y2 = points[j]
                squared_length = (x1 - x2) ** 2 + (y1 - y2) ** 2
                key = (x1 + x2, y1 + y2, squared_length)
                diagonals[key].append((points[i], points[j]))

        min_area = float('inf')
        for pairs in diagonals.values():
            # every two diagonals in a bucket make one rectangle
            for a in range(len(pairs)):
                p1, p2 = pairs[a]
                for b in range(a + 1, len(pairs)):
                    p3, p4 = pairs[b]
                    # NOTE !!! p1 is a corner; p3 and p4 are its neighbours
                    side1 = math.hypot(p1[0] - p3[0], p1[1] - p3[1])
                    side2 = math.hypot(p1[0] - p4[0], p1[1] - p4[1])
                    min_area = min(min_area, side1 * side2)

        if min_area == float('inf'):
            return 0.0
        return min_area


# V1
# IDEA : FIX A RIGHT-ANGLE CORNER, DERIVE THE FOURTH POINT
#
#   pick a corner p1 and two other points p2, p3. if the vectors
#   (p2 - p1) and (p3 - p1) are perpendicular (dot product 0), the fourth
#   corner must be  p4 = p2 + p3 - p1  — just look it up in a set.
#
#   n <= 50, so O(n^3) = 125k steps is well inside the limit, and this
#   version needs no insight beyond "a rectangle has a right angle".
#
# time = O(n^3), space = O(n)
class Solution2(object):
    def minAreaFreeRect(self, points):
        """
        :type points: List[List[int]]
        :rtype: float
        """
        point_set = set()
        for x, y in points:
            point_set.add((x, y))

        n = len(points)
        min_area = float('inf')
        for i in range(n):
            x1, y1 = points[i]
            for j in range(n):
                if j == i:
                    continue
                x2, y2 = points[j]
                for k in range(j + 1, n):
                    if k == i:
                        continue
                    x3, y3 = points[k]

                    # the angle at p1 must be 90 degrees
                    dot = (x2 - x1) * (x3 - x1) + (y2 - y1) * (y3 - y1)
                    if dot != 0:
                        continue

                    # fourth corner closes the parallelogram
                    x4 = x2 + x3 - x1
                    y4 = y2 + y3 - y1
                    if (x4, y4) not in point_set:
                        continue

                    side1 = math.hypot(x2 - x1, y2 - y1)
                    side2 = math.hypot(x3 - x1, y3 - y1)
                    min_area = min(min_area, side1 * side2)

        if min_area == float('inf'):
            return 0.0
        return min_area
