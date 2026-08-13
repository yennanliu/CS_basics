"""

1037. Valid Boomerang
Easy

Given an array points where points[i] = [xi, yi] represents a point on the X-Y plane,
return true if these points are a boomerang.

A boomerang is a set of three points that are all distinct and not in a straight line.


Example 1:

Input: points = [[1,1],[2,3],[3,2]]
Output: true

Example 2:

Input: points = [[1,1],[2,2],[3,3]]
Output: false


Constraints:

points.length == 3
points[i].length == 2
0 <= xi, yi <= 100

"""

# V0
# IDEA : CROSS PRODUCT (avoid slope division / zero-division)
#
#  three points are collinear  <=>  cross product of the 2 edge vectors is 0
#  (x2-x1)*(y3-y1) - (y2-y1)*(x3-x1) == 0
#
#  NOTE : this single test also covers the "all distinct" requirement,
#         since two identical points make the cross product 0 as well
#
# time = O(1)
# space = O(1)
class Solution(object):
    def isBoomerang(self, points):
        (x1, y1), (x2, y2), (x3, y3) = points
        cross = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1)
        return cross != 0
