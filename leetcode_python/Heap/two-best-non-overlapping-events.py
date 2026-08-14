"""

2054. Two Best Non-Overlapping Events
Medium

You are given a 0-indexed 2D integer array of events where events[i] = [startTime_i, endTime_i, value_i]. The ith event starts at startTime_i and ends at endTime_i, and if you attend this event, you will receive a value of value_i. You can choose at most two non-overlapping events to attend such that the sum of their values is maximized.

Return this maximum sum.

Note that the start time and end time is inclusive: that is, you cannot attend two events where one of them starts and the other ends at the same time. More specifically, if you attend an event with end time t, the next event must start at or after t + 1.


Example 1:

Input: events = [[1,3,2],[4,5,2],[2,4,3]]
Output: 4
Explanation: Choose the green events, 0 and 1 for a sum of 2 + 2 = 4.

Example 2:

Input: events = [[1,3,2],[4,5,2],[1,5,5]]
Output: 5
Explanation: Choose event 2 for a sum of 5.

Example 3:

Input: events = [[1,5,3],[1,5,1],[6,6,5]]
Output: 8
Explanation: Choose events 0 and 2 for a sum of 3 + 5 = 8.


Constraints:

2 <= events.length <= 10^5
events[i].length == 3
1 <= startTime_i <= endTime_i <= 10^9
1 <= value_i <= 10^6

"""

# V0
# IDEA : SORT BY START + SUFFIX MAX + BINARY SEARCH
#
#   sort events by start time and build suf[i] = max value among events i..n-1.
#   for an event (s, e, v) chosen as the FIRST one, any legal partner must
#   start at >= e + 1; because the array is sorted by start, those partners
#   form the suffix beginning at idx = first index with start > e, found by
#   bisect_right on the starts array.
#
#   candidate answer = v + (suf[idx] if idx < n else 0)
#   taking a single event is covered because suf contributes 0 past the end.
#
# time = O(n log n), space = O(n)
from bisect import bisect_right
class Solution(object):
    def maxTwoEvents(self, events):
        events.sort()
        n = len(events)
        starts = [e[0] for e in events]

        suf = [0] * (n + 1)
        for i in range(n - 1, -1, -1):
            suf[i] = max(suf[i + 1], events[i][2])

        res = 0
        for _, e, v in events:
            idx = bisect_right(starts, e)
            total = v + suf[idx]
            if total > res:
                res = total
        return res
