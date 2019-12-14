"""

Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), determine if a person could attend all meetings.

For example,
Given [[0, 30],[5, 10],[15, 20]],
return false.

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
    # @param {Interval[]} intervals
    # @return {boolean}
    def canAttendMeetings(self, intervals):
        intervals.sort(key=lambda x: x[0])
        for i in range(1, len(intervals)):
            if intervals[i][0] < intervals[i-1][1]:
                return False
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
# https://blog.csdn.net/qq508618087/article/details/50750465
class Solution(object):
    def canAttendMeetings(self, v):
        """
        :type intervals: List[Interval]
        :rtype: bool
        """
        v.sort(key = lambda val: val.start)
        return not any(v[i].start < v[i-1].end for i in range(1,len(v)))

# V1''
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
