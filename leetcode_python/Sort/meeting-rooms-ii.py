"""

253. Meeting Rooms II
Medium

Given an array of meeting time intervals intervals where intervals[i] = [starti, endi], return the minimum number of conference rooms required.

 

Example 1:

Input: intervals = [[0,30],[5,10],[15,20]]
Output: 2
Example 2:

Input: intervals = [[7,10],[2,4]]
Output: 1
 

Constraints:

1 <= intervals.length <= 104
0 <= starti < endi <= 106

"""

# V0
# IDEA : SCANNING LINE : Sort all time points and label the start and end points. Move a vertical line from left to right.
class Solution:
     def minMeetingRooms(self, intervals):
            lst = []
            """
            NOTE THIS !!!
            """
            for start, end in intervals:
                lst.append((start, 1))
                lst.append((end, -1))
            # all of below sort work
            #lst.sort()
            #lst.sort(key = lambda x : [x[0], x[1]])
            lst.sort(key = lambda x : x[0])
            res, curr_rooms = 0, 0
            for t, n in lst:
                curr_rooms += n
                res = max(res, curr_rooms)
            return res

# V0'
# IDEA : Chronological Ordering
# https://leetcode.com/problems/meeting-rooms-ii/solution/
class Solution:
    def minMeetingRooms(self, intervals):
        
        # If there are no meetings, we don't need any rooms.
        if not intervals:
            return 0

        used_rooms = 0

        # Separate out the start and the end timings and sort them individually.
        start_timings = sorted([i[0] for i in intervals])
        end_timings = sorted(i[1] for i in intervals)
        L = len(intervals)

        # The two pointers in the algorithm: e_ptr and s_ptr.
        end_pointer = 0
        start_pointer = 0

        # Until all the meetings have been processed
        while start_pointer < L:
            # If there is a meeting that has ended by the time the meeting at `start_pointer` starts
            if start_timings[start_pointer] >= end_timings[end_pointer]:
                # Free up a room and increment the end_pointer.
                used_rooms -= 1
                end_pointer += 1

            # We do this irrespective of whether a room frees up or not.
            # If a room got free, then this used_rooms += 1 wouldn't have any effect. used_rooms would
            # remain the same in that case. If no room was free, then this would increase used_rooms
            used_rooms += 1    
            start_pointer += 1   

        return used_rooms

# V0''
# IDEA : SCANNING LINE
# Step 1 : split intervals to points, and label start, end point
# Step 2 : reorder the points
# Step 3 : go through every point, if start : result + 1, if end : result -1, and record the maximum result in every iteration
class Solution:
    def minMeetingRooms(self, intervals):
        if intervals is None or len(intervals) == 0:
            return 0

        tmp = []

        # set up start and end points 
        for inter in intervals:
            tmp.append((inter[0], True))
            tmp.append((inter[1], False))

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
                n -= 1 # release the meeting room
            max_num = max(n, max_num)
        return max_num

# V0'''
# IDEA : Priority Queues
# https://leetcode.com/problems/meeting-rooms-ii/solution/
class Solution:
    def minMeetingRooms(self, intervals):
        
        # If there is no meeting to schedule then no room needs to be allocated.
        if not intervals:
            return 0

        # The heap initialization
        free_rooms = []

        # Sort the meetings in increasing order of their start time.
        intervals.sort(key= lambda x: x[0])

        # Add the first meeting. We have to give a new room to the first meeting.
        heapq.heappush(free_rooms, intervals[0][1])

        # For all the remaining meeting rooms
        for i in intervals[1:]:

            # If the room due to free up the earliest is free, assign that room to this meeting.
            if free_rooms[0] <= i[0]:
                heapq.heappop(free_rooms)

            # If a new room is to be assigned, then also we add to the heap,
            # If an old room is allocated, then also we have to add to the heap with updated end time.
            heapq.heappush(free_rooms, i[1])

        # The size of the heap tells us the minimum rooms required for all the meetings.
        return len(free_rooms)

# V0''''
# TODO : fix below
# IDEA : SCANNING LINE
# Step 1 : split intervals to points, and label start, end point
# Step 2 : reorder the points
# Step 3 : go through every point, if start : result + 1, if end : result -1, and record the maximum result in every iteration
# https://www.1point3acres.com/bbs/thread-295648-1-1.html
# class Solution:
#     """
#     @param intervals: an array of meeting time intervals
#     @return: the minimum number of conference rooms required
#     """
#     def minMeetingRooms(self, intervals):
#         open_close = []   
#         needed_room = 0  
#         res = 0  
#         for interval in intervals:
#             # "open" the room
#             open_close.append((interval[0], "open"))
#             # "close" the room
#             open_close.append((interval[1], "close"))
#
#         # sort the time 
#         open_close_ = open_close.sort(lambda x : x[0])
#         # go through every start-end time slot
#         for i in open_close_:
#             # if there is a "open" => open 2 new room 
#             if i[1] == "open":
#                 needed_room += 2
#                 res = max(res, needed_room)
#             # if there is a "close" => close 1 new room 
#             elif i[1] == "close":
#                 needed_room -= 1
#         return res

# V1
# IDEA : Priority Queues
# https://leetcode.com/problems/meeting-rooms-ii/solution/
class Solution:
    def minMeetingRooms(self, intervals):
        
        # If there is no meeting to schedule then no room needs to be allocated.
        if not intervals:
            return 0

        # The heap initialization
        free_rooms = []

        # Sort the meetings in increasing order of their start time.
        intervals.sort(key= lambda x: x[0])

        # Add the first meeting. We have to give a new room to the first meeting.
        heapq.heappush(free_rooms, intervals[0][1])

        # For all the remaining meeting rooms
        for i in intervals[1:]:

            # If the room due to free up the earliest is free, assign that room to this meeting.
            if free_rooms[0] <= i[0]:
                heapq.heappop(free_rooms)

            # If a new room is to be assigned, then also we add to the heap,
            # If an old room is allocated, then also we have to add to the heap with updated end time.
            heapq.heappush(free_rooms, i[1])

        # The size of the heap tells us the minimum rooms required for all the meetings.
        return len(free_rooms)

# V1'
# IDEA : Chronological Ordering
# https://leetcode.com/problems/meeting-rooms-ii/solution/
class Solution:
    def minMeetingRooms(self, intervals):
        
        # If there are no meetings, we don't need any rooms.
        if not intervals:
            return 0

        used_rooms = 0

        # Separate out the start and the end timings and sort them individually.
        start_timings = sorted([i[0] for i in intervals])
        end_timings = sorted(i[1] for i in intervals)
        L = len(intervals)

        # The two pointers in the algorithm: e_ptr and s_ptr.
        end_pointer = 0
        start_pointer = 0

        # Until all the meetings have been processed
        while start_pointer < L:
            # If there is a meeting that has ended by the time the meeting at `start_pointer` starts
            if start_timings[start_pointer] >= end_timings[end_pointer]:
                # Free up a room and increment the end_pointer.
                used_rooms -= 1
                end_pointer += 1

            # We do this irrespective of whether a room frees up or not.
            # If a room got free, then this used_rooms += 1 wouldn't have any effect. used_rooms would
            # remain the same in that case. If no room was free, then this would increase used_rooms
            used_rooms += 1    
            start_pointer += 1   

        return used_rooms

# V1''
# IDEA : Sort all time points and label the start and end points. Move a vertical line from left to right.
# https://leetcode.com/problems/meeting-rooms-ii/discuss/322622/Simple-Python-solutions
class Solution:
     def minMeetingRooms(self, intervals):
            lst = []
            for start, end in intervals:
                lst.append((start, 1))
                lst.append((end, -1))
            lst.sort()
            res, curr_rooms = 0, 0
            for t, n in lst:
                curr_rooms += n
                res = max(res, curr_rooms)
            return res

# V1'''
# IDEA : Priority Queues
# https://leetcode.com/problems/meeting-rooms-ii/discuss/322622/Simple-Python-solutions
class Solution:
    def minMeetingRooms(self, intervals):
            intervals.sort(key = lambda x: x[0])
            res = 0
            heap, heap_size = [], 0
            for interval in intervals:
                while heap and heap[0] <= interval[0]:
                    heapq.heappop(heap)
                    heap_size -= 1
                heapq.heappush(heap, interval[1])
                heap_size += 1
                res = max(res, heap_size)
            return res

# V1''''
# https://leetcode.com/problems/meeting-rooms-ii/discuss/67965/Concise-python-implementation
class Solution(object):
    def minMeetingRooms(self, intervals):
        stimes, etimes = sorted([i[0] for i in intervals]), sorted([i[1] for i in intervals])
        ei = 0
        for st in stimes:
            if st >= etimes[ei]:
                ei += 1
        return len(intervals) - ei

# V1'''''
# https://leetcode.com/problems/meeting-rooms-ii/discuss/208109/Python-solution
class Solution:
    def minMeetingRooms(self, intervals):
        if not intervals:
            return 0
        intervals = sorted(intervals, key = lambda x: x[0])
        end_points = collections.deque(sorted([interval[1] for interval in intervals]))
        res = 1
        pop_count = 0
        for i in range(1, len(intervals)):
            while end_points and end_points[0] <= intervals[i][0]:
                end_points.popleft()
                pop_count += 1
            res = max(res, i-pop_count+1)
        return res

# V1'''''''
# IDEA : min-heap (priority queue)
# https://leetcode.com/problems/meeting-rooms-ii/discuss/208109/Python-solution
class Solution:
    def minMeetingRooms(self, intervals):
        """
        :type intervals: List[Interval]
        :rtype: int
        """
        if not intervals:
            return 0
        intervals = sorted(intervals, key = lambda x: x[0])
        heap = []
        heapq.heapify(heap)
        res = 1
        for interval in intervals:
            if not heap:
                heapq.heappush(heap, interval[1])
            else:
                if heap[0] <= interval[0]:
                    heapq.heappop(heap)
                heapq.heappush(heap, interval[1])
            res = max(res, len(heap))
        return res

# V1''''''''
# IDEA : min-heap (priority queue)
# https://leetcode.com/problems/meeting-rooms-ii/discuss/1031292/Simple-Python-Solution
class Solution:
    def minMeetingRooms(self, intervals):
        intervals.sort(key=lambda i: i[0])
        h = []
        maxRooms = 0
        for start,end in intervals:
            if len(h) == 0:
                heapq.heappush(h, end)
            else:
                free = heapq.heappop(h)
                if start < free: # question, can a meeting start at exact same point? Assume no
                    heapq.heappush(h, free)
                heapq.heappush(h,end)
            if len(h) > maxRooms:
                maxRooms = len(h)
        return maxRooms

# V1'''''''''
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
            #print (points)
            points.append((interval[0], 1))
            points.append((interval[1], -1))
        #print (points)    
        meeting_rooms = 0
        ongoing_meetings = 0
        for _, delta in sorted(points):
            ongoing_meetings += delta
            meeting_rooms = max(meeting_rooms, ongoing_meetings)          
        return meeting_rooms

# V1'''''''''
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