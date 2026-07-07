"""

436. Find Right Interval
Medium
Topics
premium lock icon
Companies
You are given an array of intervals, where intervals[i] = [starti, endi] and each starti is unique.

The right interval for an interval i is an interval j such that startj >= endi and startj is minimized. Note that i may equal j.

Return an array of right interval indices for each interval i. If no right interval exists for interval i, then put -1 at index i.

 

Example 1:

Input: intervals = [[1,2]]
Output: [-1]
Explanation: There is only one interval in the collection, so it outputs -1.
Example 2:

Input: intervals = [[3,4],[2,3],[1,2]]
Output: [-1,0,1]
Explanation: There is no right interval for [3,4].
The right interval for [2,3] is [3,4] since start0 = 3 is the smallest start that is >= end1 = 3.
The right interval for [1,2] is [2,3] since start1 = 2 is the smallest start that is >= end2 = 2.
Example 3:

Input: intervals = [[1,4],[2,3],[3,4]]
Output: [-1,2,-1]
Explanation: There is no right interval for [1,4] and [3,4].
The right interval for [2,3] is [3,4] since start2 = 3 is the smallest start that is >= end1 = 3.
 

Constraints:

1 <= intervals.length <= 2 * 104
intervals[i].length == 2
-106 <= starti <= endi <= 106
The start point of each interval is unique.
 


"""


# V0
class Solution(object):
    def findRightInterval(self, intervals):
        """
        :type intervals: List[List[int]]
        :rtype: List[int]
        """
        pass 

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
