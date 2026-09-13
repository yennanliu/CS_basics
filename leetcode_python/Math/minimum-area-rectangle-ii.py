"""

963. Minimum Area Rectangle II
Medium

You are given an array of points in the X-Y plane points where points[i] = [x_i, y_i].

Return the minimum area of any rectangle formed from these points, with sides not necessarily parallel to the X and Y axes. If there is not any such rectangle, return 0.

Answers within 10^-5 of the actual answer will be accepted.

Example 1:

https://assets.leetcode.com/uploads/2018/12/21/1a.png

Input: points = [[1,2],[2,1],[1,0],[0,1]]
Output: 2.00000
Explanation: The minimum area rectangle occurs at [1,2],[2,1],[1,0],[0,1], with an area of 2.

Example 2:

https://assets.leetcode.com/uploads/2018/12/22/2.png

Input: points = [[0,1],[2,1],[1,1],[1,0],[2,0]]
Output: 1.00000
Explanation: The minimum area rectangle occurs at [1,0],[1,1],[2,1],[2,0], with an area of 1.

Example 3:

https://assets.leetcode.com/uploads/2018/12/22/3.png

Input: points = [[0,3],[1,2],[3,1],[1,3],[2,1]]
Output: 0
Explanation: There is no possible rectangle to form from these points.

Constraints:

1 <= points.length <= 50
points[i].length == 2
0 <= x_i, y_i <= 4 * 10^4
All the given points are unique.

"""

# V0

# V1 : dev 

# V2
# https://blog.csdn.net/fuxuemingzhu/article/details/83961509
# time = O(n^2)  # n = len(points)
# space = O(n)
class Solution(object):
    def minAreaRect(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        points = list(map(tuple, points))
        points.sort()
        pset = set(points)
        N = len(points)
        res = float('inf')
        for i in range(N - 1):
            p1 = points[i]
            for j in range(i + 1, N):
                p4 = points[j]
                if p4[0] == p1[0] or p4[1] == p1[1]:
                    continue
                p2 = (p1[0], p4[1])
                p3 = (p4[0], p1[1])
                if p2 in pset and p3 in pset:
                    res = min(res, abs(p3[0] - p1[0]) * abs(p2[1] - p1[1]))
        return res if res != float("inf") else 0

# V2'
# time = O(n^3)  # n = len(points)
# space = O(n)
class Solution(object):
    def minAreaRect(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        points = list(map(tuple, points))
        points.sort()
        xdict, ydict = collections.defaultdict(list), collections.defaultdict(list)
        pset = set()
        res = float("inf")
        for point in points:
            xdict[point[0]].append(point)
            ydict[point[1]].append(point)
            pset.add(point)
        for x1 in list(xdict.keys()):
            if len(xdict[x1]) == 1:
                continue
            for i in range(len(xdict[x1]) - 1):
                p1 = xdict[x1][i]
                for j in range(i + 1, len(xdict[x1])):
                    p2 = xdict[x1][j]
                    for p3 in ydict[p1[1]]:
                        if p3 != p1:
                            if (p3[0], p2[1]) in pset:
                                res = min(res, abs((p3[0] - p1[0]) * (p2[1] - p1[1])))
        return res if res != float("inf") else 0

# V3
# time = O(n^2) ~ O(n^3)  # n = len(points)
# space = O(n^2)
import collections
import itertools
class Solution(object):
    def minAreaFreeRect(self, points):
        """
        :type points: List[List[int]]
        :rtype: float
        """
        points.sort()
        points = [complex(*z) for z in points]
        lookup = collections.defaultdict(list)
        for P, Q in itertools.combinations(points, 2):
            lookup[P-Q].append((P+Q) / 2)

        result = float("inf")
        for A, candidates in lookup.items():
            for P, Q in itertools.combinations(candidates, 2):
                if A.real * (P-Q).real + A.imag * (P-Q).imag == 0.0:
                    result = min(result, abs(A) * abs(P-Q))
        return result if result < float("inf") else 0.0
