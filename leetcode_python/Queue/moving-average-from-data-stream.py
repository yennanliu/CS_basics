"""

# https://baihuqian.github.io/2018-08-09-moving-average-from-data-stream/
# https://www.cnblogs.com/grandyang/p/5450001.html

346. Moving Average from Data Stream
Easy

Given a stream of integers and a window size, calculate the moving average of all integers in the sliding window.

Implement the MovingAverage class:

MovingAverage(int size) Initializes the object with the size of the window size.
double next(int val) Returns the moving average of the last size values of the stream.
 

Example 1:

Input
["MovingAverage", "next", "next", "next", "next"]
[[3], [1], [10], [3], [5]]
Output
[null, 1.0, 5.5, 4.66667, 6.0]

Explanation
MovingAverage movingAverage = new MovingAverage(3);
movingAverage.next(1); // return 1.0 = 1 / 1
movingAverage.next(10); // return 5.5 = (1 + 10) / 2
movingAverage.next(3); // return 4.66667 = (1 + 10 + 3) / 3
movingAverage.next(5); // return 6.0 = (10 + 3 + 5) / 3
 

Constraints:

1 <= size <= 1000
-105 <= val <= 105
At most 104 calls will be made to next.

"""

# V0
# IDEA : pop, append op
# python 3
class MovingAverage(object):

    def __init__(self, size):
        self.size = size
        self.stack = []
        self.cur = 0
        
    def next(self, val):
        if self.cur == self.size:
            self.stack.pop(0)
            self.cur -= 1
        self.stack.append(val)
        self.cur += 1
        return sum(self.stack) / self.cur

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