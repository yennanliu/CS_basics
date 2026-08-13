"""

1453. Maximum Number of Darts Inside of a Circular Dartboard
Hard

Alice is throwing n darts on a very large wall. You are given an array darts
where darts[i] = [xi, yi] is the position of the ith dart that Alice threw on the
wall.

Bob knows the positions of the n darts on the wall. He wants to place a dartboard
of radius r on the wall so that the maximum number of darts that Alice throws lie
on the dartboard.

Given the integer r, return the maximum number of darts that can lie on the
dartboard.


Example 1:

Input: darts = [[-2,0],[2,0],[0,2],[0,-2]], r = 2
Output: 4
Explanation: Circle dartboard with center in (0,0) and radius = 2 contain all
points.

Example 2:

Input: darts = [[-3,0],[3,0],[2,6],[5,4],[0,9],[7,8]], r = 5
Output: 5
Explanation: Circle dartboard with center in (0,4) and radius = 5 contain all
points except the point (7,8).


Constraints:

1 <= darts.length <= 100
darts[i].length == 2
-10^4 <= xi, yi <= 10^4
All the darts are unique
1 <= r <= 5000

"""

import math


# V0
# IDEA: GEOMETRY - "the best circle can always be pushed onto 2 points"
#
#  KEY OBSERVATION:
#    take any optimal dartboard. slide it until its boundary touches a dart, then
#    rotate it around that dart until the boundary touches a 2nd dart. the set of
#    covered darts never shrinks. so an optimal circle either
#      (a) covers just 1 dart, or
#      (b) has 2 of the darts exactly on its boundary.
#
#  -> enumerate every pair (i, j) with dist(i, j) <= 2r, build the (at most 2)
#     circle centers of radius r through both, and count covered darts.
#
#  NOTE: use an epsilon when comparing distances - the centers are floats
#
# time = O(n^3)
# space = O(1)
class Solution(object):
    def numPoints(self, darts, r):
        EPS = 1e-7
        n = len(darts)

        def count(cx, cy):
            c = 0
            for x, y in darts:
                if (x - cx) ** 2 + (y - cy) ** 2 <= r * r + EPS:
                    c += 1
            return c

        # a single dart is always coverable
        res = 1

        for i in range(n):
            x1, y1 = darts[i]
            for j in range(i + 1, n):
                x2, y2 = darts[j]
                dx, dy = x2 - x1, y2 - y1
                d2 = dx * dx + dy * dy
                # too far apart -> no circle of radius r holds both
                if d2 > 4 * r * r:
                    continue
                d = math.sqrt(d2)
                mx, my = (x1 + x2) / 2.0, (y1 + y2) / 2.0
                # distance from the chord midpoint to the circle center
                h = math.sqrt(max(0.0, r * r - d2 / 4.0))
                # unit vector perpendicular to the chord
                ux, uy = -dy / d, dx / d
                res = max(res, count(mx + h * ux, my + h * uy))
                res = max(res, count(mx - h * ux, my - h * uy))

        return res
