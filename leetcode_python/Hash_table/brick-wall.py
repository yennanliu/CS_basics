# V0 
# IDEA : HASH TABLE + COUNTER UPDATE
class Solution(object):
    def leastBricks(self, wall):
        """
        :type wall: List[List[int]]
        :rtype: int
        """
        left_counter = collections.Counter()
        count = 0
        for row in wall:
            left = 0
            for i in range(len(row) - 1):
                left += row[i]
                left_counter.update([left])
                count = max(count, left_counter[left])
        return len(wall) - count

# V1 
# http://bookshadow.com/weblog/2017/04/09/leetcode-brick-wall/
class Solution(object):
    def leastBricks(self, wall):
        """
        :type wall: List[List[int]]
        :rtype: int
        """
        rims = collections.Counter()
        for bricks in wall:
            cnt = 0
            for b in bricks:
                if cnt: rims[cnt] += 1
                cnt += b
        return len(wall) - max(rims.values() or [0])

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80526298
class Solution(object):
    def leastBricks(self, wall):
        """
        :type wall: List[List[int]]
        :rtype: int
        """
        left_counter = collections.Counter()
        count = 0
        for row in wall:
            left = 0
            for i in range(len(row) - 1):
                left += row[i]
                left_counter.update([left])
                count = max(count, left_counter[left])
        return len(wall) - count
        
# V2 
# Time:  O(n), n is the total number of the bricks
# Space: O(m), m is the total number different widths
import collections
class Solution(object):
    def leastBricks(self, wall):
        """
        :type wall: List[List[int]]
        :rtype: int
        """
        widths = collections.defaultdict(int)
        result = len(wall)
        for row in wall:
            width = 0
            for i in range(len(row)-1):
                width += row[i]
                widths[width] += 1
                result = min(result, len(wall) - widths[width])
        return result