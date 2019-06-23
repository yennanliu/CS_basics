
# LeetCode 469. Convex Polygon

# Given a list of points that form a polygon when joined sequentially, find if this polygon is convex (Convex polygon definition).

# Note:

# There are at least 3 and at most 10,000 points.
# Coordinates are in the range -10,000 to 10,000.
# You may assume the polygon formed by given points is always a simple polygon (Simple polygon definition). In other words, we ensure that exactly two edges intersect at each vertex, and that edges otherwise don't intersect each other.
# Example 1:

# [[0,0],[0,1],[1,1],[1,0]]

# Answer: True

# Explanation:
# Example 2:

# [[0,0],[0,10],[10,10],[10,0],[5,5]]

# Answer: False

# Explanation:


# V1 : DEV 



# V2
# http://bookshadow.com/weblog/2016/12/04/leetcode-convex-polygon/
class Solution(object):
    def isConvex(self, points):
        """
        :type points: List[List[int]]
        :rtype: bool
        """
        def crossProduct(p0, p1, p2):
            x0, y0 = p0
            x1, y1 = p1
            x2, y2 = p2
            return (x2 - x0) * (y1 - y0) - (x1 - x0) * (y2 - y0)

        size = len(points)
        last = 0
        for x in range(size):
            p0, p1, p2 = points[x], points[(x + 1) % size], points[(x + 2) % size]
            p = crossProduct(p0, p1, p2)
            if p * last < 0:
                return False
            last = p
        return True


# V3
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def isConvex(self, points):
        """
        :type points: List[List[int]]
        :rtype: bool
        """
        def det(A):
            return A[0][0]*A[1][1] - A[0][1]*A[1][0]

        n, prev, curr = len(points), 0, None
        for i in range(len(points)):
            A = [[points[(i+j) % n][0] - points[i][0], points[(i+j) % n][1] - points[i][1]] for j in (1, 2)]
            curr = det(A)
            if curr:
                if curr * prev < 0:
                    return False
                prev = curr
        return True

