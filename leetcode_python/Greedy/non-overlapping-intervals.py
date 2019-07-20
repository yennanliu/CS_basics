# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82728387
# Definition for an interval.
# class Interval(object):
#     def __init__(self, s=0, e=0):
#         self.start = s
#         self.end = e
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: int
        """
        if not intervals: return 0
        intervals.sort(key = lambda x : x.start)
        last = 0
        res = 0
        for i in range(1, len(intervals)):
            if intervals[last].end > intervals[i].start:
                if intervals[i].end < intervals[last].end:
                    last = i
                res += 1
            else:
                last = i
        return res

# V2 
# Time:  O(nlogn)
# Space: O(1)
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: int
        """
        intervals.sort(key=lambda interval: interval.start)
        result, prev = 0, 0
        for i in range(1, len(intervals)):
            if intervals[i].start < intervals[prev].end:
                if intervals[i].end < intervals[prev].end:
                    prev = i
                result += 1
            else:
                prev = i
        return result