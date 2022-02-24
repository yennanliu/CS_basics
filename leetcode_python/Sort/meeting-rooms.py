"""

252. Meeting Rooms
Easy

Given an array of meeting time intervals where intervals[i] = [starti, endi], determine if a person could attend all meetings.

 
Example 1:

Input: intervals = [[0,30],[5,10],[15,20]]
Output: false
Example 2:

Input: intervals = [[7,10],[2,4]]
Output: true
 

Constraints:

0 <= intervals.length <= 104
intervals[i].length == 2
0 <= starti < endi <= 106

"""
# Time:  O(nlogn)
# Space: O(n)
#
# Definition for an interval.
# class Interval:
#     def __init__(self, s=0, e=0):
#         self.start = s
#         self.end = e
 
# V0
class Solution:
    def canAttendMeetings(self, intervals):
        """
        NOTE this
        """
        intervals.sort(key=lambda x: x[0])
        for i in range(1, len(intervals)):
            """
            NOTE this : 
                -> we compare ntervals[i][0] and ntervals[i-1][1]
            """
            if intervals[i][0] < intervals[i-1][1]:
                return False
        return True

# V0'
# IDEA : SORT
class Solution(object):
    def canAttendMeetings(self, intervals):
        # edge case
        if not intervals or len(intervals) == 1:
            return True
            
        intervals.sort(key = lambda x : [x[0], -x[1]])

        if len(intervals) == 2:
            return intervals[0][0] < intervals[1][0] and intervals[0][1] < intervals[1][1]

        last = intervals[0]
        for i in range(1, len(intervals)):
            # CASE 1 : last and intervals[i] overlap (3 situations)
            if (last[1] > intervals[i][0] and  intervals[i][0] < last[1]) or\
               (last[0] == intervals[i][0] and last[1] == intervals[i][1]) or\
               (last[0] < intervals[i][1] and last[1] > intervals[i][1]):
                return False
            # CASE 2 : last and intervals[i] NOT overlap
            else:
                last = intervals[i]
        return True

# V1 
class Solution:
    # @param {Interval[]} intervals
    # @return {boolean}
    def canAttendMeetings(self, intervals):
        intervals.sort(key=lambda x: x[0])
        for i in range(len(intervals)-1):
            if intervals[i][1] > intervals[i+1][0]:
                return False
        return True

# V1'
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/meeting-rooms/solution/
# JAVA
# public static boolean overlap(int[] interval1, int[] interval2) {
#     return (Math.min(interval1[1], interval2[1]) >
#             Math.max(interval1[0], interval2[0]));
# }

# V1''
# IDEA : SORTING
# https://leetcode.com/problems/meeting-rooms/solution/
class Solution:
    def canAttendMeetings(self, intervals):
        intervals.sort()
        for i in range(len(intervals) - 1):
            if intervals[i][1] > intervals[i + 1][0]:
                return False
        return True

# V1''''
# https://blog.csdn.net/qq508618087/article/details/50750465
class Solution(object):
    def canAttendMeetings(self, v):
        """
        :type intervals: List[Interval]
        :rtype: bool
        """
        v.sort(key = lambda val: val.start)
        return not any(v[i].start < v[i-1].end for i in range(1,len(v)))

# V1''''
# https://www.jiuzhang.com/solution/meeting-rooms/
# JAVA
# public class Solution {
#     public boolean canAttendMeetings(Interval[] intervals) {
#         if(intervals == null || intervals.length == 0) return true;
#         Arrays.sort(intervals, new Comparator<Interval>(){
#             public int compare(Interval i1, Interval i2){
#                 return i1.start - i2.start;
#             }
#         });
#         int end = intervals[0].end;
#         for(int i = 1; i < intervals.length; i++){
#             if(intervals[i].start < end) {
#                 return false;
#             }
#             end = Math.max(end, intervals[i].end);
#         }
#         return true;
#     }
# }

# V2 
class Solution:
    # @param {Interval[]} intervals
    # @return {boolean}
    def canAttendMeetings(self, intervals):
        intervals.sort(key=lambda x: x.start)

        for i in range(1, len(intervals)):
            if intervals[i].start < intervals[i-1].end:
                return False
        return True