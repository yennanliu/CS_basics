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
