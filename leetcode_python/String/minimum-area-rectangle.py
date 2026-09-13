"""

939. Minimum Area Rectangle
Medium

You are given an array of points in the X-Y plane points where points[i] = [x_i, y_i].

Return the minimum area of a rectangle formed from these points, with sides parallel to the X and Y axes. If there is not any such rectangle, return 0.

Example 1:

https://assets.leetcode.com/uploads/2021/08/03/rec1.JPG

Input: points = [[1,1],[1,3],[3,1],[3,3],[2,2]]
Output: 4

Example 2:

https://assets.leetcode.com/uploads/2021/08/03/rec2.JPG

Input: points = [[1,1],[1,3],[3,1],[3,3],[4,1],[4,3]]
Output: 2

Constraints:

1 <= points.length <= 500
points[i].length == 2
0 <= x_i, y_i <= 4 * 10^4
All the given points are unique.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82973125
# time = O(1)
# space = O(1)
class Solution(object):
    def computeArea(self, A, B, C, D, E, F, G, H):
        """
        :type A: int
        :type B: int
        :type C: int
        :type D: int
        :type E: int
        :type F: int
        :type G: int
        :type H: int
        :rtype: int
        """
        points = [((A, B), (C, D)), ((E, F), (G, H))]
        points.sort()
        ((A, B), (C, D)), ((E, F), (G, H)) = points
        area1 = (D - B) * (C - A)
        area2 = (H - F) * (G - E)
        x, y = (min(C, G) - max(A, E)), (min(D, H) - max(B, F))
        area = 0
        if x > 0 and y > 0:
            area = x * y
        return area1 + area2 - area

# V2
# time = O(n^1.5) on average, O(n^2) worst case
# space = O(n)
import collections
class Solution(object):
    def minAreaRect(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        nx = len(set(x for x, y in points))
        ny = len(set(y for x, y in points))

        p = collections.defaultdict(list)
        if nx > ny:
            for x, y in points:
                p[x].append(y)
        else:
            for x, y in points:
                p[y].append(x)

        lookup = {}
        result = float("inf")
        for x in sorted(p):
            p[x].sort()
            for j in range(len(p[x])):
                for i in range(j):
                    y1, y2 = p[x][i], p[x][j]
                    if (y1, y2) in lookup:
                        result = min(result, (x-lookup[y1, y2]) * abs(y2-y1))
                    lookup[y1, y2] = x
        return result if result != float("inf") else 0

# V3
# time = O(n^2)
# space = O(n)
class Solution2(object):
    def minAreaRect(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        lookup = set()
        result = float("inf")
        for x1, y1 in points:
            for x2, y2 in lookup:
                if (x1, y2) in lookup and (x2, y1) in lookup:
                    result = min(result, abs(x1-x2) * abs(y1-y2))
            lookup.add((x1, y1))
        return result if result != float("inf") else 0
