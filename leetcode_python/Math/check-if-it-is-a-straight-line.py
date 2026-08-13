"""

1232. Check If It Is a Straight Line
Easy

You are given an array coordinates, coordinates[i] = [x, y], where [x, y] represents the
coordinate of a point. Check if these points make a straight line in the XY plane.

Example 1:

Input: coordinates = [[1,2],[2,3],[3,4],[4,5],[5,6],[6,7]]
Output: true

Example 2:

Input: coordinates = [[1,1],[2,2],[3,4],[4,5],[5,6],[7,7]]
Output: false


Constraints:

2 <= coordinates.length <= 1000
coordinates[i].length == 2
-10^4 <= coordinates[i][0], coordinates[i][1] <= 10^4
coordinates contains no duplicate point.

"""

# V0
# IDEA : MATH / CROSS PRODUCT
#        every point must be collinear with the first two. Use the cross product
#        instead of a slope so that vertical lines (dx == 0) need no special case:
#
#            (x1 - x0) * (y - y0) == (y1 - y0) * (x - x0)
# time = O(n)
# space = O(1)
class Solution(object):
    def checkStraightLine(self, coordinates):
        x0, y0 = coordinates[0]
        x1, y1 = coordinates[1]
        dx, dy = x1 - x0, y1 - y0

        for x, y in coordinates[2:]:
            if dx * (y - y0) != dy * (x - x0):
                return False
        return True
