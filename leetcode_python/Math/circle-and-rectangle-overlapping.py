"""

1401. Circle and Rectangle Overlapping
Medium

You are given a circle represented as (radius, xCenter, yCenter) and an axis-aligned rectangle represented as (x1, y1, x2, y2), where (x1, y1) are the coordinates of the bottom-left corner, and (x2, y2) are the coordinates of the top-right corner of the rectangle.

Return true if the circle and rectangle are overlapped otherwise return false. In other words, check if there is any point (xi, yi) that belongs to the circle and the rectangle at the same time.


Example 1:

Input: radius = 1, xCenter = 0, yCenter = 0, x1 = 1, y1 = -1, x2 = 3, y2 = 1
Output: true
Explanation: Circle and rectangle share the point (1,0).

Example 2:

Input: radius = 1, xCenter = 1, yCenter = 1, x1 = 1, y1 = -3, x2 = 2, y2 = -1
Output: false

Example 3:

Input: radius = 1, xCenter = 0, yCenter = 0, x1 = -1, y1 = 0, x2 = 0, y2 = 1
Output: true


Constraints:

1 <= radius <= 2000
-10^4 <= xCenter, yCenter <= 10^4
-10^4 <= x1 < x2 <= 10^4
-10^4 <= y1 < y2 <= 10^4

"""

# V0
# IDEA : MATH (clamp the centre into the rectangle, then measure)
#
#   the rectangle point closest to the centre is obtained by clamping the
#   centre's coordinates into [x1, x2] and [y1, y2] independently, because
#   the rectangle is axis-aligned and x/y are separable.
#   they overlap iff dist(centre, closest point) <= radius, compared with
#   squares so no floating point is involved.
#
# time = O(1), space = O(1)
class Solution(object):
    def checkOverlap(self, radius, xCenter, yCenter, x1, y1, x2, y2):
        # closest point of the rectangle to the circle centre
        cx = min(max(xCenter, x1), x2)
        cy = min(max(yCenter, y1), y2)
        dx, dy = xCenter - cx, yCenter - cy
        return dx * dx + dy * dy <= radius * radius
