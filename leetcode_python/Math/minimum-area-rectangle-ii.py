# V1 : dev 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/83961509
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
# Time:  O(n^2) ~ O(n^3)
# Space: O(n^2)
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
