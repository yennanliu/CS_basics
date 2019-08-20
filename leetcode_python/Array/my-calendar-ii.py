# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82820204
# IDEA : GREEDY 
class MyCalendarTwo(object):

    def __init__(self):
        # every booked interval 
        self.booked = list()
        # every overlap interval 
        self.overlaped = list()

    def book(self, start, end):
        """
        :type start: int
        :type end: int
        :rtype: bool
        """
        for os, oe in self.overlaped:
            if max(os, start) < min(oe, end):
                return False
        for bs, be in self.booked:
            ss = max(bs, start)
            ee = min(be, end)
            if ss < ee:
                self.overlaped.append((ss, ee))
        self.booked.append((start, end))
        return True
# Your MyCalendarTwo object will be instantiated and called as such:
# obj = MyCalendarTwo()
# param_1 = obj.book(start,end)

# V2 
# Time:  O(n^2)
# Space: O(n)
class MyCalendarTwo(object):

    def __init__(self):
        self.__overlaps = []
        self.__calendar = []


    def book(self, start, end):
        """
        :type start: int
        :type end: int
        :rtype: bool
        """
        for i, j in self.__overlaps:
            if start < j and end > i:
                return False
        for i, j in self.__calendar:
            if start < j and end > i:
                self.__overlaps.append((max(start, i), min(end, j)))
        self.__calendar.append((start, end))
        return True