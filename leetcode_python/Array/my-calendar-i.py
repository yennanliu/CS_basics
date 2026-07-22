"""

729. My Calendar I
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are implementing a program to use as your calendar. We can add a new event if adding the event will not cause a double booking.

A double booking happens when two events have some non-empty intersection (i.e., some moment is common to both events.).

The event can be represented as a pair of integers startTime and endTime that represents a booking on the half-open interval [startTime, endTime), the range of real numbers x such that startTime <= x < endTime.

Implement the MyCalendar class:

MyCalendar() Initializes the calendar object.
boolean book(int startTime, int endTime) Returns true if the event can be added to the calendar successfully without causing a double booking. Otherwise, return false and do not add the event to the calendar.
 

Example 1:

Input
["MyCalendar", "book", "book", "book"]
[[], [10, 20], [15, 25], [20, 30]]
Output
[null, true, false, true]

Explanation
MyCalendar myCalendar = new MyCalendar();
myCalendar.book(10, 20); // return True
myCalendar.book(15, 25); // return False, It can not be booked because time 15 is already booked by another event.
myCalendar.book(20, 30); // return True, The event can be booked, as the first event takes every time less than 20, but not including 20.
 

Constraints:

0 <= start < end <= 109
At most 1000 calls will be made to book.
 


"""
# V0 
class MyCalendar(object):

    def __init__(self):
        

    def book(self, startTime, endTime):
        """
        :type startTime: int
        :type endTime: int
        :rtype: bool
        """
        

# V1-1
# IDEA: INTERVAL (gpt)
class MyCalendar(object):

    def __init__(self):
        self.booking = []

    def book(self, startTime, endTime):
        if self.is_overlap(startTime, endTime):
            return False

        self.booking.append([startTime, endTime])
        return True

    def is_overlap(self, startTime, endTime):
        for s, e in self.booking:
            if startTime < e and s < endTime:
                return True
        return False


# V1-2
# IDEA: INTERVAL (GEMINI)
class MyCalendar(object):

    def __init__(self):
        # Store bookings as a list of [start, end]
        self.booking = []

    def book(self, startTime, endTime):
        """
        :type startTime: int
        :type endTime: int
        :rtype: bool
        """
        # 1. Check for overlaps with every existing event
        for s, e in self.booking:
            # Golden Rule of Overlaps:
            # The new event must start before the existing event ends, AND
            # the new event must end after the existing event starts.
            if startTime < e and endTime > s:
                return False
                
        # 2. If the loop finishes without returning False, there are no overlaps!
        self.booking.append([startTime, endTime])
        return True


# V2
# time = O(nlogn) on average, O(n^2) on worst case
# space = O(n)
class Node(object):
    def __init__(self, start, end):
        self.__start = start
        self.__end = end
        self.__left = None
        self.__right = None


    def insert(self, node):
        if node.__start >= self.__end:
            if not self.__right:
                self.__right = node
                return True
            return self.__right.insert(node)
        elif node.__end <= self.__start:
            if not self.__left:
                self.__left = node
                return True
            return self.__left.insert(node)
        else:
            return False


class MyCalendar(object):
    def __init__(self):
        self.__root = None


    def book(self, start, end):
        """
        :type start: int
        :type end: int
        :rtype: bool
        """
        if self.__root is None:
            self.__root = Node(start, end)
            return True
        return self.root.insert(Node(start, end))


# time = O(n^2)
# space = O(n)
class MyCalendar2(object):

    def __init__(self):
        self.__calendar = []


    def book(self, start, end):
        """
        :type start: int
        :type end: int
        :rtype: bool
        """
        for i, j in self.__calendar:
            if start < j and end > i:
                return False
        self.__calendar.append((start, end))
        return True
