"""

1610. Maximum Number of Visible Points
Hard

You are given an array points, an integer angle, and your location, where location = [posx, posy] and points[i] = [xi, yi] both denote integral coordinates on the X-Y plane.

Initially, you are facing directly east from your position. You cannot move from your position, but you can rotate. In other words, posx and posy cannot be changed. Your field of view in degrees is represented by angle, determining how wide you can see from any given view direction. Let d be the amount in degrees that you rotate counterclockwise. Then, your field of view is the inclusive range of angles [d - angle/2, d + angle/2].

You can see some set of points if, for each point, the angle formed by the point, your position, and the immediate east direction from your position is in your field of view.

There can be multiple points at one coordinate. There may be points at your location, and you can always see these points regardless of your rotation. Points do not obstruct your vision to other points.

Return the maximum number of points you can see.


Example 1:

Input: points = [[2,1],[2,2],[3,3]], angle = 90, location = [1,1]
Output: 3
Explanation: The shaded region represents your field of view. All points can be made visible in your field of view, including [3,3] even though [2,2] is in front and in the same line of sight.

Example 2:

Input: points = [[2,1],[2,2],[3,4],[1,1]], angle = 90, location = [1,1]
Output: 4
Explanation: All points can be made visible in your field of view, including the one at your location.

Example 3:

Input: points = [[1,0],[2,1]], angle = 13, location = [1,1]
Output: 1
Explanation: You can only see one of the two points, as shown above.


Constraints:

1 <= points.length <= 10^5
points[i].length == 2
location.length == 2
0 <= angle < 360
0 <= posx, posy, xi, yi <= 100

"""

# V0
# IDEA : POLAR ANGLES + SLIDING WINDOW ON A CIRCULAR SORTED ARRAY
#
#   distance is irrelevant -- only each point's bearing matters. Compute
#   atan2(dy, dx) for every point, sort, and the question becomes
#   "the densest window of width `angle` on a circle".
#
#   handle the wrap-around by appending every bearing + 2*pi to the array,
#   then for each start i binary-search the last bearing <= v[i] + width.
#
#   NOTE : points sitting exactly ON your location have no bearing; they
#          are always visible, so count them separately and add at the end.
#   NOTE : add a tiny epsilon before the binary search -- the field of view
#          is INCLUSIVE and atan2 arithmetic loses a few ulps.
#
# time = O(n log n), space = O(n)
from bisect import bisect_right
from math import atan2, pi
class Solution(object):
    def visiblePoints(self, points, angle, location):
        px, py = location
        same = 0
        bearings = []
        for x, y in points:
            if x == px and y == py:
                same += 1
            else:
                bearings.append(atan2(y - py, x - px))

        bearings.sort()
        n = len(bearings)
        if n == 0:
            return same

        extended = bearings + [t + 2 * pi for t in bearings]
        width = angle * pi / 180.0 + 1e-9

        best = 0
        for i in range(n):
            j = bisect_right(extended, extended[i] + width)
            if j - i > best:
                best = j - i
        return best + same
