"""

149. Max Points on a Line
Hard

Given an array of points where points[i] = [xi, yi] represents a point on the
X-Y plane, return the maximum number of points that lie on the same straight
line.

Example 1:

Input: points = [[1,1],[2,2],[3,3]]

Output: 3

Example 2:

Input: points = [[1,1],[3,2],[5,3],[4,1],[2,3],[1,4]]

Output: 4

Constraints:

1 <= points.length <= 300
points[i].length == 2
-10^4 <= xi, yi <= 10^4
All the points are unique.

"""

# V0
# IDEA : FIX AN ANCHOR + HASHMAP OF REDUCED SLOPES (dy/g, dx/g)
#
#   Every line through >= 2 points passes through some point A. Fix A, and
#   every other point B lies on the line A-B, identified by its slope from A.
#   Points sharing A's slope are on ONE line (same direction through the same
#   point), so the best line through A = 1 + the biggest slope bucket.
#
#   The slope is stored as the reduced integer pair (dy / g, dx / g), never as
#   a float: dy / dx in floating point can make two different slopes compare
#   equal (or two equal ones unequal), and vertical lines (dx = 0) divide by 0.
#   The sign is normalised so (1, -1) and (-1, 1) - the same direction seen
#   from the other side - land in the same bucket.
#
#   e.g. A = (1,1): B = (2,2) -> (1,1); B = (3,3) -> (2,2)/2 = (1,1)
#        -> bucket (1,1) has 2 points -> line holds 1 + 2 = 3
#
#   Only points AFTER A are compared: a line through an earlier point was
#   already counted when that earlier point was the anchor.
#
# time = O(n^2 log C), space = O(n)   (C = coordinate range, for the gcd)
from collections import defaultdict


class Solution(object):
    def maxPoints(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        n = len(points)
        # edge case: one or two points always lie on one line
        if n <= 2:
            return n

        best = 1
        for i in range(n):
            anchor_x, anchor_y = points[i]
            # reduced slope (dy, dx) -> how many later points lie on it from anchor
            slope_count = defaultdict(int)

            for j in range(i + 1, n):
                dx = points[j][0] - anchor_x
                dy = points[j][1] - anchor_y
                slope = self.reduce_slope(dy, dx)
                slope_count[slope] += 1
                # NOTE !!! + 1 counts the anchor itself on that line
                best = max(best, slope_count[slope] + 1)

        return best

    def reduce_slope(self, dy, dx):
        """Return (dy, dx) divided by their gcd, with a canonical sign."""
        divisor = self.gcd(abs(dy), abs(dx))
        dy = dy // divisor
        dx = dx // divisor
        # canonical sign: keep dx positive; for a vertical line keep dy positive
        if dx < 0 or (dx == 0 and dy < 0):
            dy = -dy
            dx = -dx
        return (dy, dx)

    def gcd(self, a, b):
        """Euclid's gcd of two non-negative ints (never both 0: points are unique)."""
        while b != 0:
            a, b = b, a % b
        return a


# V1
# IDEA : BRUTE FORCE OVER PAIRS + CROSS PRODUCT COLLINEARITY CHECK
#
#   Any two distinct points define a line. For each pair (A, B), count every
#   point P with cross(B - A, P - A) == 0, i.e. P is collinear with A and B.
#   The cross product is pure integer math, so there is no slope to normalise
#   and no float to compare - simpler to get right, at the cost of a factor n.
#
#   e.g. A = (1,1), B = (2,2), P = (3,3)
#        (B - A) = (1,1), (P - A) = (2,2) -> 1*2 - 1*2 = 0 -> collinear
#
# NOTE : O(n^3) = 300^3 / 2 ~ 1.4 * 10^7 checks - passes for n <= 300, but V0
#        is the one to give in an interview.
#
# time = O(n^3), space = O(1)
class Solution2(object):
    def maxPoints(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        n = len(points)
        # edge case: one or two points always lie on one line
        if n <= 2:
            return n

        best = 2
        for i in range(n):
            ax, ay = points[i]
            for j in range(i + 1, n):
                # direction vector of the line A -> B
                line_dx = points[j][0] - ax
                line_dy = points[j][1] - ay

                on_line = 0
                for k in range(n):
                    point_dx = points[k][0] - ax
                    point_dy = points[k][1] - ay
                    # cross product 0 <=> P lies on the line through A and B
                    if line_dx * point_dy - line_dy * point_dx == 0:
                        on_line += 1

                best = max(best, on_line)

        return best
