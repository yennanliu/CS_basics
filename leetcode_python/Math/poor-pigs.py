# V1 : DEV 




# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/81079261
class Solution(object):
    def poorPigs(self, buckets, minutesToDie, minutesToTest):
        """
        :type buckets: int
        :type minutesToDie: int
        :type minutesToTest: int
        :rtype: int
        """
        tests = minutesToTest / minutesToDie + 1
        pigs = 0
        while tests ** pigs < buckets:
            pigs += 1
        return pigs


# V3 
# Time:  O(1)
# Space: O(1)
import math
class Solution(object):
    def poorPigs(self, buckets, minutesToDie, minutesToTest):
        """
        :type buckets: int
        :type minutesToDie: int
        :type minutesToTest: int
        :rtype: int
        """
        return int(math.ceil(math.log(buckets) / math.log(minutesToTest / minutesToDie + 1)))
       