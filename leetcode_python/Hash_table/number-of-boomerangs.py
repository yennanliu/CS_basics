# Time:  O(n^2)
# Space: O(n)
# Given n points in the plane that are all pairwise distinct,
# a "boomerang" is a tuple of points (i, j, k) such that the distance
# between i and j equals the distance between i and k (the order of the tuple matters).
#
# Find the number of boomerangs. You may assume that n will be at most 500
# and coordinates of points are all in the range [-10000, 10000] (inclusive).
#
# Example:
# Input:
# [[0,0],[1,0],[2,0]]
#
# Output:
# 2
#
# Explanation:
# The two boomerangs are [[1,0],[0,0],[2,0]] and [[1,0],[2,0],[0,0]]

# Given n points in the plane that are all pairwise distinct, a "boomerang" is a tuple of points (i, j, k) 
# such that the distance between i and j equals the distance between i and k 
# (the order of the tuple matters).
# e.g. : 
# Input:
# [[0,0],[1,0],[2,0]]

# Output:
# 2

# Explanation:
# The two boomerangs are [[1,0],[0,0],[2,0]] and [[1,0],[2,0],[0,0]]


# V0 

# V1 
# IDEA : collections.defaultdict
# http://bookshadow.com/weblog/2016/11/06/leetcode-number-of-boomerangs/
# https://blog.csdn.net/fuxuemingzhu/article/details/54379349
import collections
class Solution(object):
    def numberOfBoomerangs(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        ans = 0
        for x1, y1 in points:
            dmap = collections.defaultdict(int)
            for x2, y2 in points:
                dmap[(x1 - x2) ** 2 + (y1 - y2) ** 2] += 1
            for d in dmap:
                ans += dmap[d] * (dmap[d] - 1)
        return ans

# V1'
# IDEA : TWO POINTERS
# DEV 

# V2 
import collections
class Solution(object):
    def numberOfBoomerangs(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        result = 0

        for i in range(len(points)):
            group = collections.defaultdict(int)
            for j in range(len(points)):
                if j == i:
                    continue
                dx, dy =  points[i][0] - points[j][0], points[i][1] - points[j][1]
                group[dx**2 + dy**2] += 1

            for _, v in group.items():
                if v > 1:
                    result += v * (v-1)

        return result

    def numberOfBoomerangs2(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        cnt = 0
        for a, i in enumerate(points):
            dis_list = []
            for b, k in enumerate(points[:a] + points[a + 1:]):
                dis_list.append((k[0] - i[0]) ** 2 + (k[1] - i[1]) ** 2)
            for z in list(collections.Counter(dis_list).values()):
                if z > 1:
                    cnt += z * (z - 1)
        return cnt