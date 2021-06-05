"""

Given an array of intervals intervals where intervals[i] = [starti, endi], 
return the minimum number of intervals you need to remove 
to make the rest of the intervals non-overlapping.



Example 1:

Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
Output: 1
Explanation: [1,3] can be removed and the rest of the intervals are non-overlapping.

Example 2:

Input: intervals = [[1,2],[1,2],[1,2]]
Output: 2
Explanation: You need to remove two [1,2] to make the rest of the intervals non-overlapping.

Example 3:

Input: intervals = [[1,2],[2,3]]
Output: 0
Explanation: You don't need to remove any of the intervals since they're already non-overlapping.
 

Constraints:

1 <= intervals.length <= 2 * 104
intervals[i].length == 2
-2 * 104 <= starti < endi <= 2 * 104

"""

# V0
# IDEA : 2 POINTERS
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        if not intervals: return 0
        intervals.sort(key = lambda x : x[0])
        last = 0
        res = 0
        for i in range(1, len(intervals)):
            if intervals[last][1] > intervals[i][0]:
                if intervals[i][1] < intervals[last][1]:
                    last = i
                res += 1
            else:
                last = i
        return res

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

### Test case : dev

# V1'
# https://leetcode.com/problems/non-overlapping-intervals/discuss/91721/Short-Ruby-and-Python
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        end = float('-inf')
        erased = 0
        for i in sorted(intervals, key=lambda i: i.end):
            if i.start >= end:
                end = i.end
            else:
                erased += 1
        return erased

# V1''
# https://leetcode.com/problems/non-overlapping-intervals/discuss/91768/Python-greedy-solution-with-explanation
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: int
        """
        if not intervals: return 0
        intervals.sort(key=lambda x: x.start)  # sort on start time
        currEnd, cnt = intervals[0].end, 0
        for x in intervals[1:]:
            if x.start < currEnd:  # find overlapping interval
                cnt += 1
                currEnd = min(currEnd, x.end)  # erase the one with larger end time
            else:
                currEnd = x.end   # update end time
        return cnt

# V1'''
# https://leetcode.com/problems/non-overlapping-intervals/discuss/276056/Python-Greedy-Interval-Scheduling
# IDEA : GREEDY
class Solution(object):
    def eraseOverlapIntervals(intervals):
        end, cnt = float('-inf'), 0
        for i in sorted(intervals, key=lambda x: x[1]):
            if i[0] >= end: 
                end = i[1]
            else: 
                cnt += 1
        return cnt

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
