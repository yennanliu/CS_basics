# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82973125
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
# Time:  O(n^1.5) on average
#        O(n^2) on worst
# Space: O(n)
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

# Time:  O(n^2)
# Space: O(n)
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
