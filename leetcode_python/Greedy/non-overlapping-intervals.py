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
# IDEA: GREEDY + SORT ON END + max_end
# time = O(n log n)
# space = O(1)
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        if len(intervals) <= 1:
            return 0

        """
        NOTE !!!

         greedy idea,
         -> sort on `end` (min -> big)
        """
        intervals.sort(key=lambda x: x[1])

        count = 0
        """
        NOTE !!!

        maintain the `prev_end`,
        to check if there is an overlapping
        """
        prev_end = intervals[0][1]

        for i in range(1, len(intervals)):
            start, end = intervals[i]

            """
            NOTE !!!

            check if there is an overlapping
            """
            if start < prev_end:
                count += 1
            else:
                prev_end = end

        return count


# V0-1
# IDEA: GREEDY + SORT ON END + max_end
# time = O(n log n)
# space = O(1)
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: int
        """
        if not intervals:
            return 0

        # CRITICAL GREEDY FIX: Sort strictly by END times (x[1]).
        # If end times match, Python naturally falls back to sorting by start times (x[0]).
        intervals.sort(key=lambda x: x[1])
        
        # Track the end time of the last non-overlapping interval we successfully kept
        prev_end = intervals[0][1]
        deletions = 0
        
        # Walk through the intervals starting from the second one
        for i in range(1, len(intervals)):
            current_start = intervals[i][0]
            current_end = intervals[i][1]
            
            # CRITICAL FIX: If the current interval starts BEFORE the previous one ends,
            # we have an overlap! Because we sorted by end times, the current interval
            # is guaranteed to end later than prev_end, so we greedily erase it.
            if current_start < prev_end:
                deletions += 1
            else:
                # No overlap! We safely keep this interval and update our end-marker boundary
                prev_end = current_end
                
        return deletions


# V0
# IDEA : 2 POINTERS + sorting + intervals
# TODO : make it general : (sort by x[0] or x[1] and the op)
# time = O(n log n)
# space = O(1)
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        ### NOTE THIS !!!
        intervals.sort(key = lambda x : x[1])
        #print ("intervals = " + str(intervals))
        res = []
        cnt = 0
        last = intervals[0]
        # edge case
        if not intervals:
            return 0
        ### NOTE : start from idx = 1
        for i in range(1, len(intervals)):
            # case 1
            if intervals[i][0] < last[1]:
                cnt += 1
            # case 2
            else:
                last = intervals[i]
                last[1] = max(intervals[i][1], last[1])
        return cnt

# V0'
# IDEA : 2 POINTERS + sorting + intervals
# time = O(n log n)
# space = O(1)
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        if not intervals: return 0
        ### NOTE THIS !!!
        intervals.sort(key = lambda x : x[0])
        #intervals.sort(key = lambda x : [x[0],x[1]])  # this one is OK as well
        """
        ### NOTE : last is the "idx" of last element
                 -> we'll leverage it for overlap intervals removal
        """
        last = 0
        res = 0
        for i in range(1, len(intervals)):
            if intervals[last][1] > intervals[i][0]:
                """
                ### NOTE : if "last" element's "second" element > intervals[i] 's  "second" element
                         -> we need to use intervals[i] 's index as last index
                """
                if intervals[i][1] < intervals[last][1]:
                    last = i
                res += 1
            else:
                last = i
        return res

# V0''
# IDEA : 2 POINTERS + sorting + intervals
# time = O(n log n)
# space = O(1)
class Solution(object):
    def eraseOverlapIntervals(self, intervals):
        cnt = 0
        if len(intervals) == 0:
            return 0
        #intervals.sort(key = lambda x : x[0]) # this one is OK as well
        intervals.sort(key = lambda x : [x[0],x[1]])
        ### NOTE : we init last as intervals[0]
        last = intervals[0]
        ### NOTE : we start from idx = 1
        for i in range(1,len(intervals)):
            #print ("i = " + str(i) + " last = " + str(last) + " intervals[i] = " + str(intervals[i]))
            if last[1] > intervals[i][0]:
                """
                ### NOTE : if "last" element's "second" element > intervals[i] 's  "second" element
                         -> we need to use intervals[i] 's index as last index
                """
                if intervals[i][1] < last[1]:
                    last[0] = intervals[i][0]
                    last[1] = intervals[i][1] 
                cnt += 1
            else:
                last[0] = intervals[i][0]
                last[1] = intervals[i][1]               
        return cnt

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
