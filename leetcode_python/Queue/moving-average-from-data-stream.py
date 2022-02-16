"""

# https://baihuqian.github.io/2018-08-09-moving-average-from-data-stream/
# https://www.cnblogs.com/grandyang/p/5450001.html

[LeetCode] 346. Moving Average from Data Stream
 

Given a stream of integers and a window size, calculate the moving average of all integers in the sliding window.

Example:

MovingAverage m = new MovingAverage(3);
m.next(1) = 1
m.next(10) = (1 + 10) / 2
m.next(3) = (1 + 10 + 3) / 3
m.next(5) = (10 + 3 + 5) / 3

"""

# V0
# IDEA : deque
from collections import deque
class MovingAverage(object):

    def __init__(self, size):
        self.size = size
        self.q = deque([])

    def next(self, val):
        self.q.append(val)
        self.size += 1
        if len(self.q) > self.size:
            self.q.pop(0)
        return sum(self.q) / len(self.q)

# V0'
# IDEA : deque
from collections import deque
class MovingAverage(object):

    def __init__(self, size):
        self.__size = size
        self.__q = deque([])

    def next(self, val):
        if len(self.__q) == self.__size:
            self.__q.popleft()            
        self.__q.append(val)
        return 1.0 * sum(self.__q) / len(self.__q)

# V1
# https://www.cnblogs.com/lightwindy/p/9736836.html
# https://nifannn.github.io/2018/10/01/Algorithm-Notes-Leetcode-346-Moving-Average-from-Data-Stream/
from collections import deque
 class MovingAverage(object):
    def __init__(self, size):
        """
        Initialize your data structure here.
        :type size: int
        """
        self.__size = size
        self.__sum = 0
        self.__q = deque([])
 
    def next(self, val):
        """
        :type val: int
        :rtype: float
        """
        if len(self.__q) == self.__size:
            self.__sum -= self.__q.popleft()
        self.__sum += val
        self.__q.append(val)
        return 1.0 * self.__sum / len(self.__q)
 
# V2 
# Time:  O(1)
# Space: O(w)
from collections import deque
class MovingAverage(object):

    def __init__(self, size):
        """
        Initialize your data structure here.
        :type size: int
        """
        self.__size = size
        self.__sum = 0
        self.__q = deque([])

    def next(self, val):
        """
        :type val: int
        :rtype: float
        """
        if len(self.__q) == self.__size:
            self.__sum -= self.__q.popleft()
        self.__sum += val
        self.__q.append(val)
        return 1.0 * self.__sum / len(self.__q)
# Your MovingAverage object will be instantiated and called as such:
# obj = MovingAverage(size)
# param_1 = obj.next(val)