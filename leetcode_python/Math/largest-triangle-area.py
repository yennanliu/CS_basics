"""

812. Largest Triangle Area
Easy

Given an array of points on the X-Y plane points where points[i] = [x_i, y_i], return the area of the largest triangle that can be formed by any three different points. Answers within 10^-5 of the actual answer will be accepted.

Example 1:

https://s3-lc-upload.s3.amazonaws.com/uploads/2018/04/04/1027.png

Input: points = [[0,0],[0,1],[1,0],[0,2],[2,0]]
Output: 2.00000
Explanation: The five points are shown in the above figure. The red triangle is the largest.

Example 2:

Input: points = [[1,0],[0,0],[0,1]]
Output: 0.50000

Constraints:

3 <= points.length <= 50
-50 <= x_i, y_i <= 50
All the given points are unique.

"""

# V0

# V1 

# V2 
# http://bookshadow.com/weblog/2018/04/09/leetcode-largest-triangle-area/
### Shoelace formula  ### : get the area of triangle from its 3 points
# https://en.wikipedia.org/wiki/Shoelace_formula
# time = O(n^3)  # n = len(points)
# space = O(1)
class Solution(object):
    def largestTriangleArea(self, points):
        """
        :type points: List[List[int]]
        :rtype: float
        """
        triangleArea = lambda x1, y1, x2, y2, x3, y3: \
            abs(0.5 * (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)))
        size = len(points)
        ans = 0
        for i in range(size):
            for j in range(i + 1, size):
                for k in range(j + 1, size):
                    x1, y1 = points[i]
                    x2, y2 = points[j]
                    x3, y3 = points[k]
                    ans = max(ans, triangleArea(x1, y1, x2, y2, x3, y3))
        return ans

# V3
# time = O(n^3)  # n = len(points)
# space = O(1)
class Solution(object):
    def largestTriangleArea(self, points):
        """
        :type points: List[List[int]]
        :rtype: float
        """
        result = 0
        for i in range(len(points)-2):
            for j in range(i+1, len(points)-1):
                for k in range(j+1, len(points)):
                    result = max(result,
                                 0.5 * abs(points[i][0] * points[j][1] +
                                           points[j][0] * points[k][1] +
                                           points[k][0] * points[i][1] -
                                           points[j][0] * points[i][1] -
                                           points[k][0] * points[j][1] -
                                           points[i][0] * points[k][1]))
        return result

