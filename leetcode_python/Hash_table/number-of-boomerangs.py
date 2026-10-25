"""

447. Number of Boomerangs
Medium
Topics
premium lock icon
Companies
You are given n points in the plane that are all distinct, where points[i] = [xi, yi]. A boomerang is a tuple of points (i, j, k) such that the distance between i and j equals the distance between i and k (the order of the tuple matters).

Return the number of boomerangs.

 

Example 1:

Input: points = [[0,0],[1,0],[2,0]]
Output: 2
Explanation: The two boomerangs are [[1,0],[0,0],[2,0]] and [[1,0],[2,0],[0,0]].
Example 2:

Input: points = [[1,1],[2,2],[3,3]]
Output: 2
Example 3:

Input: points = [[1,1]]
Output: 0
 

Constraints:

n == points.length
1 <= n <= 500
points[i].length == 2
-104 <= xi, yi <= 104
All the points are unique.


"""

# V0
class Solution(object):
    def numberOfBoomerangs(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        pass


# V0-1
# IDEA: HASHAMP + BRUTE FORCE (GPT)
class Solution(object):
    def numberOfBoomerangs(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        res = 0
        n = len(points)

        # Treat each point as the center
        for i in range(n):
            dist_map = {}

            x1, y1 = points[i]

            # Count distances from center i to every other point
            for j in range(n):
                if i == j:
                    continue

                x2, y2 = points[j]

                # Squared Euclidean distance
                dist = (x1 - x2) ** 2 + (y1 - y2) ** 2

                dist_map[dist] = dist_map.get(dist, 0) + 1

            # Count ordered pairs of points with equal distances
            for count in dist_map.values():
                res += count * (count - 1)

        return res



# V0-2
# IDEA: HASHAMP + BRUTE FORCE (GEMINI)
from collections import defaultdict


class Solution(object):

  def numberOfBoomerangs(self, points):
    """:type points: List[List[int]]

    :rtype: int
    """
    total_boomerangs = 0

    # 依次固定每一個點作為中心點 i
    for i in range(len(points)):
      dist_map = defaultdict(int)
      x1, y1 = points[i]

      for j in range(len(points)):
        if i == j:
          continue
        x2, y2 = points[j]

        # 計算平方距離，避免浮點數精確度問題
        d_sq = (x1 - x2) ** 2 + (y1 - y2) ** 2
        dist_map[d_sq] += 1

      # 對於每一個距離計數 count，可以形成的有序對數量為 count * (count - 1)
      for count in dist_map.values():
        if count >= 2:
          total_boomerangs += count * (count - 1)

    return total_boomerangs


# V0-3
# time = O(n^2)
# space = O(n)
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

# V1 
# IDEA : collections.defaultdict
# http://bookshadow.com/weblog/2016/11/06/leetcode-number-of-boomerangs/
# https://blog.csdn.net/fuxuemingzhu/article/details/54379349
# time = O(n^2)
# space = O(n)
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
# https://www.jiuzhang.com/solution/number-of-boomerangs/#tag-highlight-lang-python
# time = O(n^2)
# space = O(n)  # disCount dict bounded by n
class Solution(object):
    def getDistance(self, a, b):
        dx = a[0] - b[0]
        dy = a[1] - b[1]
        return dx * dx + dy * dy

    def numberOfBoomerangs(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        if points == None:
            return 0
        ans = 0
        for i in xrange(len(points)):
            disCount = {}
            for j in xrange(len(points)):
                if i == j:
                    continue
                distance = self.getDistance(points[i], points[j])
                count = disCount.get(distance, 0)
                disCount[distance] = count + 1
            for distance in disCount:
                ans += disCount[distance] * (disCount[distance] - 1)
        return ans

# V1''
# IDEA : TWO POINTERS
# DEV 

# V2 
# time = O(n^2)
# space = O(n)
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
