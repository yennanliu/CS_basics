# V1 : DEV 


# V2 
class Solution:

    def __init__(self, w):
        """
        :type w: List[int]
        """
        self.preSum = [0] * len(w)
        self.preSum[0] = w[0]
        for i in range(1, len(w)):
            self.preSum[i] = self.preSum[i - 1] + w[i]

    def pickIndex(self):
        """
        :rtype: int
        """
        total = self.preSum[-1]
        rand = random.randint(0, total - 1)
        left, right = 0, len(self.preSum) - 1
        while left + 1 < right:
            mid = (left + right) // 2
            if rand >= self.preSum[mid]:
                left = mid
            else:
                right = mid
        if rand < self.preSum[left]:
            return left
        return right


# Your Solution object will be instantiated and called as such:
# obj = Solution(w)
# param_1 = obj.pickIndex()


# V3 
# Time:  ctor: O(n)
#        pickIndex: O(logn)
# Space: O(n)

import random
import bisect
class Solution(object):

    def __init__(self, w):
        """
        :type w: List[int]
        """
        self.__prefix_sum = list(w)
        for i in range(1, len(w)):
            self.__prefix_sum[i] += self.__prefix_sum[i-1]

    def pickIndex(self):
        """
        :rtype: int
        """
        target = random.randint(0, self.__prefix_sum[-1]-1)
        return bisect.bisect_right(self.__prefix_sum, target)


