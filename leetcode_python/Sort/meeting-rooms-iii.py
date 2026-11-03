"""

2402. Meeting Rooms III
Hard

You are given an integer n. There are n rooms numbered from 0 to n - 1.

You are given a 2D integer array meetings where meetings[i] = [starti, endi]
means that a meeting will be held during the half-closed time interval
[starti, endi). All the values of starti are unique.

Meetings are allocated to rooms in the following manner:

1. Each meeting will take place in the unused room with the lowest number.
2. If there are no available rooms, the meeting will be delayed until a room
   becomes free. The delayed meeting should have the same duration as the
   original meeting.
3. When a room becomes unused, meetings that have an earlier original start
   time should be given the room.

Return the number of the room that held the most meetings. If there are
multiple rooms, return the room with the lowest number.

A half-closed interval [a, b) is the interval between a and b including a and
not including b.

Example 1:

Input: n = 2, meetings = [[0,10],[1,5],[2,7],[3,4]]

Output: 0

Explanation:

- At time 0, both rooms are not being used. The first meeting starts in room 0.
- At time 1, only room 1 is not being used. The second meeting starts in room 1.
- At time 2, both rooms are being used. The third meeting is delayed.
- At time 3, both rooms are being used. The fourth meeting is delayed.
- At time 5, the meeting in room 1 finishes. The third meeting starts in
  room 1 for the time period [5,10).
- At time 10, the meetings in both rooms finish. The fourth meeting starts in
  room 0 for the time period [10,11).
Both rooms 0 and 1 held 2 meetings, so we return 0.

Example 2:

Input: n = 3, meetings = [[1,20],[2,10],[3,5],[4,9],[6,8]]

Output: 1

Explanation:

- At time 1, all three rooms are not being used. The first meeting starts in room 0.
- At time 2, rooms 1 and 2 are not being used. The second meeting starts in room 1.
- At time 3, only room 2 is not being used. The third meeting starts in room 2.
- At time 4, all three rooms are being used. The fourth meeting is delayed.
- At time 5, the meeting in room 2 finishes. The fourth meeting starts in
  room 2 for the time period [5,10).
- At time 6, all three rooms are being used. The fifth meeting is delayed.
- At time 10, the meetings in rooms 1 and 2 finish. The fifth meeting starts
  in room 1 for the time period [10,12).
Room 0 held 1 meeting while rooms 1 and 2 each held 2 meetings, so we return 1.

Constraints:

1 <= n <= 100
1 <= meetings.length <= 10^5
meetings[i].length == 2
0 <= starti < endi <= 5 * 10^5
All the values of starti are unique.

"""

# V0
# IDEA : SORT BY START + TWO HEAPS (FREE ROOMS BY NUMBER / BUSY ROOMS BY END TIME)
#
#   Process meetings in original start order (rule 3: earlier start gets the
#   room first). Keep two min-heaps:
#     free_rooms : room numbers that are idle          -> pop = lowest number (rule 1)
#     busy_rooms : (end_time, room) for rooms in use   -> pop = first to finish
#
#   For each meeting [start, end):
#     1. release every busy room whose end_time <= start into free_rooms
#        (half-open interval: a room ending at `start` IS free at `start`)
#     2. a free room exists -> take the lowest-numbered one, runs [start, end)
#     3. no free room       -> wait for the earliest-finishing room; the meeting
#        keeps its duration, so it runs [end_time, end_time + (end - start))
#        (rule 2). Ties on end_time pop the lower room, because the heap
#        compares (end_time, room) tuples.
#
#   e.g. n = 2, [[0,10],[1,5],[2,7],[3,4]]
#        [0,10]->room0, [1,5]->room1, [2,7] waits -> room1 @5..10,
#        [3,4] waits -> rooms 0,1 both free @10, tie -> room0 @10..11
#        counts = [2, 2] -> 0
#
#   Brute force (simulate time tick by tick) is O(max_time * n) and TLEs.
#
# time = O(m log m + m log n), space = O(n)   (m = len(meetings))
import heapq


class Solution(object):
    def mostBooked(self, n, meetings):
        """
        :type n: int
        :type meetings: List[List[int]]
        :rtype: int
        """
        # earlier original start gets served first
        sorted_meetings = sorted(meetings, key=lambda meeting: meeting[0])

        # every room starts idle; a sorted list is already a valid min-heap
        free_rooms = list(range(n))
        # (end_time, room) for every room currently holding a meeting
        busy_rooms = []
        meeting_count = [0] * n

        for start, end in sorted_meetings:
            # 1. free every room whose meeting is over by `start`
            while busy_rooms and busy_rooms[0][0] <= start:
                _, room = heapq.heappop(busy_rooms)
                heapq.heappush(free_rooms, room)

            if free_rooms:
                # 2. lowest-numbered idle room, meeting runs on time
                room = heapq.heappop(free_rooms)
                heapq.heappush(busy_rooms, (end, room))
            else:
                # 3. all rooms busy: delay until the earliest one frees up
                # NOTE !!! the delayed meeting keeps its DURATION, so the new
                #          end is (room's end time) + (end - start), not `end`
                earliest_end, room = heapq.heappop(busy_rooms)
                duration = end - start
                heapq.heappush(busy_rooms, (earliest_end + duration, room))

            meeting_count[room] += 1

        # most meetings; strict `>` keeps the lowest room number on ties
        best_room = 0
        for room in range(1, n):
            if meeting_count[room] > meeting_count[best_room]:
                best_room = room
        return best_room


# V1
# IDEA : SORT BY START + LINEAR SCAN OVER ROOM FREE TIMES (NO HEAP)
#
#   Same simulation as V0, but keep one array room_free_at[i] = the time
#   room i becomes idle. For each meeting, scan rooms 0..n-1:
#     - the first room with room_free_at[i] <= start is the lowest-numbered
#       idle room -> take it, runs [start, end)
#     - otherwise remember the room with the smallest room_free_at (first
#       one wins ties -> lowest number) and delay the meeting into it.
#
#   Works because n <= 100, so an O(n) scan per meeting is cheap. Simpler to
#   write live than two heaps; V0 is the answer if the interviewer grows n.
#
# time = O(m log m + m * n), space = O(n)
class Solution2(object):
    def mostBooked(self, n, meetings):
        """
        :type n: int
        :type meetings: List[List[int]]
        :rtype: int
        """
        sorted_meetings = sorted(meetings, key=lambda meeting: meeting[0])

        room_free_at = [0] * n
        meeting_count = [0] * n

        for start, end in sorted_meetings:
            chosen_room = -1
            # fallback if every room is busy: the one that frees up first
            earliest_room = 0

            for room in range(n):
                if room_free_at[room] <= start:
                    # lowest-numbered idle room: stop scanning
                    chosen_room = room
                    break
                # strict `<` keeps the lower room number on ties
                if room_free_at[room] < room_free_at[earliest_room]:
                    earliest_room = room

            if chosen_room != -1:
                room_free_at[chosen_room] = end
            else:
                # delayed meeting keeps its duration
                chosen_room = earliest_room
                room_free_at[chosen_room] += end - start

            meeting_count[chosen_room] += 1

        best_room = 0
        for room in range(1, n):
            if meeting_count[room] > meeting_count[best_room]:
                best_room = room
        return best_room
