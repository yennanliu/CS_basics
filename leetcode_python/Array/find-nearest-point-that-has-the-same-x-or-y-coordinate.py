"""

1779. Find Nearest Point That Has the Same X or Y Coordinate
Easy

You are given two integers, x and y, which represent your current location on a Cartesian grid: (x, y). You are also given an array points where each points[i] = [ai, bi] represents that a point exists at (ai, bi). A point is valid if it shares the same x-coordinate or the same y-coordinate as your location.

Return the index (0-indexed) of the valid point with the smallest Manhattan distance from your current location. If there are multiple, return the valid point with the smallest index. If there are no valid points, return -1.

The Manhattan distance between two points (x1, y1) and (x2, y2) is abs(x1 - x2) + abs(y1 - y2).

Example 1:

Input: x = 3, y = 4, points = [[1,2],[3,1],[2,4],[2,3],[4,4]]
Output: 2
Explanation: Of all the points, only [3,1], [2,4] and [4,4] are valid. Of the valid points, [2,4] and [4,4] have the smallest Manhattan distance from your current location, with a distance of 1. [2,4] has the smallest index, so return 2.

Example 2:

Input: x = 3, y = 4, points = [[3,4]]
Output: 0
Explanation: The answer is allowed to be on the same location as your current location.

Example 3:

Input: x = 3, y = 4, points = [[2,3]]
Output: -1
Explanation: There are no valid points.

Constraints:

1 <= points.length <= 10^4
points[i].length == 2
1 <= x, y, ai, bi <= 10^4

"""

# V0
# IDEA : SINGLE PASS, KEEP THE BEST VALID POINT
#
#   a point is valid only when it shares x or y with (x, y). for a valid point
#   one of the two terms of the Manhattan distance is 0, so the distance is
#   just abs(a - x) + abs(b - y) either way.
#   NOTE : the tie-break is "smallest index", so update only on a STRICTLY
#          smaller distance while scanning left to right.
#
# time = O(n), space = O(1)
class Solution(object):
    def nearestValidPoint(self, x, y, points):
        res, best = -1, float("inf")
        for i in range(len(points)):
            a, b = points[i]
            if a == x or b == y:
                d = abs(a - x) + abs(b - y)
                if d < best:
                    res, best = i, d
        return res


# V0-1
# IDEA : MATERIALIZE (DISTANCE, INDEX) PAIRS AND LET min() DO THE TIE-BREAK
#
#   instead of hand-rolling "update only on a strictly smaller distance", pack
#   each valid point into the tuple (distance, index). tuples compare
#   lexicographically, so min() picks the smallest distance and, among equal
#   distances, the smallest index — the required tie-break falls out for free.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def nearestValidPoint(self, x, y, points):
        cand = [(abs(a - x) + abs(b - y), i)
                for i, (a, b) in enumerate(points)
                if a == x or b == y]
        return min(cand)[1] if cand else -1


# V0-2
# IDEA : EXPANDING MANHATTAN RADIUS OVER THE TWO LINES THROUGH (x, y)
#
#   every valid point sits on the vertical line X = x or the horizontal line
#   Y = y, so the only cells at distance r are (x, y-r), (x, y+r), (x-r, y)
#   and (x+r, y). index the input by coordinate pair, keeping the SMALLEST
#   index per pair, then grow r from 0 upwards and stop at the first radius
#   that hits any of those four cells.
#
#   this trades the scan over points for a scan over radii, so it wins when
#   the coordinate range is small relative to the number of points.
#
# time = O(n + C) where C is the coordinate span
# space = O(n)
class Solution(object):
    def nearestValidPoint(self, x, y, points):
        best_idx = {}
        limit = 0
        for i, (a, b) in enumerate(points):
            if (a, b) not in best_idx:
                best_idx[(a, b)] = i
            limit = max(limit, abs(a - x), abs(b - y))

        for r in range(limit + 1):
            hits = [best_idx[c] for c in
                    ((x, y - r), (x, y + r), (x - r, y), (x + r, y))
                    if c in best_idx]
            if hits:
                return min(hits)
        return -1
