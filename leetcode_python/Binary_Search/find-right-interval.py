# V0 

# V1 
# http://bookshadow.com/weblog/2016/10/30/leetcode-find-right-interval/
# Definition for an interval.
# class Interval(object):
#     def __init__(self, s=0, e=0):
#         self.start = s
#         self.end = e
class Solution(object):
    def findRightInterval(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: List[int]
        """
        invs = sorted((x.start, i) for i, x in enumerate(intervals))
        ans = []
        for x in intervals:
            idx = bisect.bisect_right( invs, (x.end,) )
            ans.append(invs[idx][1] if idx < len(intervals) else -1)
        return ans
        
# V2 
# Time:  O(nlogn)
# Space: O(n)
import bisect
class Solution(object):
    def findRightInterval(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: List[int]
        """
        sorted_intervals = sorted((interval.start, i) for i, interval in enumerate(intervals))
        result = []
        for interval in intervals:
            idx = bisect.bisect_left(sorted_intervals, (interval.end,))
            result.append(sorted_intervals[idx][1] if idx < len(sorted_intervals) else -1)
        return result
