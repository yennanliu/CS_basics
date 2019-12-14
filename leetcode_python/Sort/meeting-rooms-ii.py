# Given an array of meeting time intervals consisting 
# of start and end times [[s1,e1],[s2,e2],...] (si < ei),
# find the minimum number of conference rooms required.
# For example,
# Given [[0, 30],[5, 10],[15, 20]],
# return 2.

# V0 
# https://www.1point3acres.com/bbs/thread-295648-1-1.html
class Solution:
    """
    @param intervals: an array of meeting time intervals
    @return: the minimum number of conference rooms required
    """
    def minMeetingRooms(self, intervals):
        open_close = []   
        needed_room = 0  
        res = 0  
        for interval in intervals:
            # "open" the room
            open_close.append((interval[0], "open"))
            # "close" the room
            open_close.append((interval[1], "close"))

        # sort the time 
        open_close_ = open_close.sort(lambda x : x[0])
        # go through every start-end time slot
        for i in open_close_:
            # if there is a "open" => open 2 new room 
            if i[1] == "open":
                needed_room += 2
                res = max(res, needed_room)
            # if there is a "close" => close 1 new room 
            elif i[1] == "close":
                needed_room -= 1
        return res

# V1 
# https://www.jiuzhang.com/solution/meeting-rooms-ii/#tag-highlight-lang-python
# IDEA  : TO HAVE A ARRAY OF ALL "ROOM OPEN" AND "ROOM CLOSE" EVENTS
# "ROOM OPEN" EVENT : (TIME, 1)
# "ROOM CLOSE" EVENT : (TIME, -1)
# SO AFTER RE-ORDER ON THE TIME, WE WILL HAVE AN ORDERED EVENT ARRAY LIKE BELOW
# [ [t1, 1], [t1,1], [t3, -1], [t4, 1], [t5, -1]]
# THEN WE CAN GO THROUGH ALL EVENT IN THE EVENTS ARRAY AND CALCULATE # OF ROOM NEEDED
# DEMO 1
# In [23]: intervals= [[0, 30],[5, 10],[15, 20]]
# In [24]: Solution().minMeetingRooms(intervals)
# Out[24]: 2
# DEMO 2 
# In [40]: intervals
# Out[40]: [[0, 30], [5, 10], [15, 20]]

# In [41]: sorted([(0, 1), (30, -1), (5, 1), (10, -1), (15, 1), (20, -1)])
# Out[41]: [(0, 1), (5, 1), (10, -1), (15, 1), (20, -1), (30, -1)]

# In [42]: Solution().minMeetingRooms(intervals)
# []
# [(0, 1), (30, -1)]
# [(0, 1), (30, -1), (5, 1), (10, -1)]
# [(0, 1), (30, -1), (5, 1), (10, -1), (15, 1), (20, -1)]
# Out[42]: 2
class Solution:
    """
    @param intervals: an array of meeting time intervals
    @return: the minimum number of conference rooms required
    """
    def minMeetingRooms(self, intervals):
        points = []
        for interval in intervals:
            print (points)
            points.append((interval[0], 1))
            points.append((interval[1], -1))
        print (points)    
        meeting_rooms = 0
        ongoing_meetings = 0
        for _, delta in sorted(points):
            ongoing_meetings += delta
            meeting_rooms = max(meeting_rooms, ongoing_meetings)          
        return meeting_rooms

# V1'
# https://blog.csdn.net/yurenguowang/article/details/76665171
class Solution:
    def minMeetingRooms(self, intervals):
        if intervals is None or len(intervals) == 0:
            return 0
        tmp = []
        # label start and end point 
        for inter in intervals:
            tmp.append((inter.start, True))
            tmp.append((inter.end, False))
        # order the array with time 
        tmp = sorted(tmp, key=lambda v: (v[0], v[1]))
        n = 0
        max_num = 0
        for arr in tmp:
            # start point : +1 
            if arr[1]:
                n += 1
            # end point : -1 
            else:
                n -= 1
            max_num = max(n, max_num)
        return max_num

# V2 
# https://blog.csdn.net/yurenguowang/article/details/76665171
# Definition for an interval.
# class Interval(object):
#     def __init__(self, s=0, e=0):
#         self.start = s
#         self.end = e
class Solution:
    def minMeetingRooms(self, intervals):
        if intervals is None or len(intervals) == 0:
            return 0

        tmp = []

        # set up start and end points 
        for inter in intervals:
            tmp.append((inter.start, True))
            tmp.append((inter.end, False))

        # sort 
        tmp = sorted(tmp, key=lambda v: (v[0], v[1]))

        n = 0
        max_num = 0
        for arr in tmp:
            # start point +1 
            if arr[1]:
                n += 1
            # end point -1 
            else:
                n -= 1
            max_num = max(n, max_num)
        return max_num

# V3
# Time:  O(nlogn)
# Space: O(n)
class Solution(object):
    # @param {Interval[]} intervals
    # @return {integer}
    def minMeetingRooms(self, intervals):
        starts, ends = [], []
        for i in intervals:
            starts.append(i.start)
            ends.append(i.end)

        starts.sort()
        ends.sort()

        s, e = 0, 0
        min_rooms, cnt_rooms = 0, 0
        while s < len(starts):
            if starts[s] < ends[e]:
                cnt_rooms += 1  # Acquire a room.
                # Update the min number of rooms.
                min_rooms = max(min_rooms, cnt_rooms)
                s += 1
            else:
                cnt_rooms -= 1  # Release a room.
                e += 1

        return min_rooms

# time: O(nlogn)
# space: O(n)
from heapq import heappush, heappop
# V4
class Solution2(object):
    def minMeetingRooms(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: int
        """
        if not intervals:
            return 0
        
        intervals.sort(key=lambda x: x.start)
        free_rooms = []
        
        heappush(free_rooms, intervals[0].end)
        for interval in intervals[1:]:
            if free_rooms[0] <= interval.start:
                heappop(free_rooms)
            
            heappush(free_rooms, interval.end)
        
        return len(free_rooms)