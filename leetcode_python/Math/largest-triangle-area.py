# V1 

# V2 
# http://bookshadow.com/weblog/2018/04/09/leetcode-largest-triangle-area/
### Shoelace formula  ### : get the area of triangle from its 3 points 
# https://en.wikipedia.org/wiki/Shoelace_formula
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
# Time:  O(n^3)
# Space: O(1)
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

