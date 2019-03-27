# V1 : DEV 

# V2 
# binary search 
# https://blog.csdn.net/fuxuemingzhu/article/details/83714891
# 
# ******  1) Binary search via bisect ******
# 
# http://kuanghy.github.io/2016/06/14/python-bisect
# def grade(score,breakpoints=[60, 70, 80, 90], grades='FDCBA'):
#     """
#     set up breakpoints associated with grades
# 
#     i.e. 
#       60  70  80  90 
#     F    D   C   B   A
#
#     so we can know which grades the score (func input) is belong with 
#     """
#     i = bisect.bisect(breakpoints, score)
#     return grades[i]
#
# print [grade(score) for score in [33, 99, 77, 70, 89, 90, 100]]
# ['F', 'A', 'C', 'C', 'B', 'A', 'A'] # output 
#
# ****** 2) binary search with bisect ******
# def binary_search_bisect(lst, x):
#     from bisect import bisect_left
#     i = bisect_left(lst, x)
#     if i != len(lst) and lst[i] == x:
#         return i
#     return None

class RecentCounter:

    def __init__(self):
        self.nums = []

    def ping(self, t):
        """
        :type t: int
        :rtype: int
        """
        from bisect import bisect
        self.nums.append(t)
        cur_pos = len(self.nums)
        prev_pos = bisect.bisect_left(self.nums, t - 3000)
        return cur_pos - prev_pos

# Your RecentCounter object will be instantiated and called as such:
# obj = RecentCounter()
# param_1 = obj.ping(t)

# V3 
# https://blog.csdn.net/fuxuemingzhu/article/details/83714891
import collections
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
