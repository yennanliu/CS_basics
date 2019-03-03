

# V1 : DEV 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/83714891
class RecentCounter:

    def __init__(self):
        self.nums = []

    def ping(self, t):
        """
        :type t: int
        :rtype: int
        """
        self.nums.append(t)
        cur_pos = len(self.nums)
        prev_pos = bisect.bisect_left(self.nums, t - 3000)
        return cur_pos - prev_pos

# Your RecentCounter object will be instantiated and called as such:
# obj = RecentCounter()
# param_1 = obj.ping(t)



# V3 
# https://blog.csdn.net/fuxuemingzhu/article/details/83714891
class RecentCounter:

    def __init__(self):
        self.que = collections.deque()

    def ping(self, t):
        """
        :type t: int
        :rtype: int
        """
        while self.que and self.que[0] < t - 3000:
            self.que.popleft()
        self.que.append(t)
        return len(self.que)


# V4
# Time:  O(1) on average
# Space: O(w), w means the size of the last milliseconds.

import collections


class RecentCounter(object):

    def __init__(self):
        self.__q = collections.deque()

    def ping(self, t):
        """
        :type t: int
        :rtype: int
        """
        self.__q.append(t)
        while self.__q[0] < t-3000:
            self.__q.popleft()
        return len(self.__q)