# V1 : DEV 


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/83098201
class Solution(object):

    def __init__(self, rects):
        """
        :type rects: List[List[int]]
        """
        self.rects = rects
        self.N = len(rects)
        areas = [(x2 - x1 + 1) * (y2 - y1 + 1) for x1, y1, x2, y2 in rects]
        self.preSum = [0] * self.N
        self.preSum[0] = areas[0]
        for i in range(1, self.N):
            self.preSum[i] = self.preSum[i - 1] + areas[i]
        self.total = self.preSum[-1]

    def pickRect(self):
        rand = random.randint(0, self.total - 1)
        return bisect.bisect_right(self.preSum, rand)

    def pickPoint(self, rect):
        x1, y1, x2, y2 = rect
        x = random.randint(x1, x2)
        y = random.randint(y1, y2)
        return x, y
        
    def pick(self):
        """
        :rtype: List[int]
        """
        rectIndex = self.pickRect()
        rect = self.rects[rectIndex]
        return self.pickPoint(rect)
        


# Your Solution object will be instantiated and called as such:
# obj = Solution(rects)
# param_1 = obj.pick()


# V3 
# Time:  ctor: O(n)
#        pick: O(logn)
# Space: O(n)

import random
import bisect


class Solution(object):

    def __init__(self, rects):
        """
        :type rects: List[List[int]]
        """
        self.__rects = list(rects)
        self.__prefix_sum = [(x[2]-x[0]+1)*(x[3]-x[1]+1) for x in rects]
        for i in range(1, len(self.__prefix_sum)):
            self.__prefix_sum[i] += self.__prefix_sum[i-1]

    def pick(self):
        """
        :rtype: List[int]
        """
        target = random.randint(0, self.__prefix_sum[-1]-1)
        left = bisect.bisect_right(self.__prefix_sum, target)
        rect = self.__rects[left]
        width, height = rect[2]-rect[0]+1, rect[3]-rect[1]+1
        base = self.__prefix_sum[left]-width*height
        return [rect[0]+(target-base)%width, rect[1]+(target-base)//width]


