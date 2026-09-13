"""

933. Number of Recent Calls
Easy

You have a RecentCounter class which counts the number of recent requests within a certain time frame.

Implement the RecentCounter class:

RecentCounter() Initializes the counter with zero recent requests.
int ping(int t) Adds a new request at time t, where t represents some time in milliseconds, and returns the number of requests that have happened in the inclusive range [t - 3000, t], that is, the new request plus every earlier request that is no more than 3000 milliseconds older.

It is guaranteed that every call to ping uses a strictly larger value of t than the previous call.

Example 1:

Input
["RecentCounter", "ping", "ping", "ping", "ping"]
[[], [1], [100], [3001], [3002]]
Output
[null, 1, 2, 3, 3]

Explanation
RecentCounter recentCounter = new RecentCounter();
recentCounter.ping(1);     // requests = [1], range is [-2999,1], return 1
recentCounter.ping(100);   // requests = [1, 100], range is [-2900,100], return 2
recentCounter.ping(3001);  // requests = [1, 100, 3001], range is [1,3001], return 3
recentCounter.ping(3002);  // requests = [1, 100, 3001, 3002], range is [2,3002], return 3

Constraints:

1 <= t <= 10^9
Each test case will call ping with strictly increasing values of t.
At most 10^4 calls will be made to ping.

"""

# V0

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

# time = O(log m) per ping  # m = total calls so far; bisect_left binary search
# space = O(m)  # nums list never shrinks, grows with total ping calls
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
# time = O(1) amortized per ping  # each element pushed/popped from deque at most once
# space = O(w)  # w = number of calls within the 3000ms window
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
# time = O(1) on average
# space = O(w), w means the size of the last milliseconds.
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
