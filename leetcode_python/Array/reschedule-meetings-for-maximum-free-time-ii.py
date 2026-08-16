"""

3440. Reschedule Meetings for Maximum Free Time II
Medium

You are given an integer eventTime denoting the duration of an event. You are
also given two integer arrays startTime and endTime, each of length n.

These represent the start and end times of n non-overlapping meetings that occur
during the event between time t = 0 and time t = eventTime, where the i^th
meeting occurs during the time [startTime[i], endTime[i]].

You can reschedule at most one meeting by moving its start time while
maintaining the same duration, such that the meetings remain non-overlapping, to
maximize the longest continuous period of free time during the event.

Return the maximum amount of free time possible after rearranging the meetings.

Note that the meetings can not be rescheduled to a time outside the event and
they should remain non-overlapping.

Note: In this version, it is valid for the relative ordering of the meetings to
change after rescheduling one meeting.

Example 1:

Input: eventTime = 5, startTime = [1,3], endTime = [2,5]

Output: 2

Explanation:

Reschedule the meeting at [1, 2] to [2, 3], leaving no meetings during the time
[0, 2].

Example 2:

Input: eventTime = 10, startTime = [0,7,9], endTime = [1,8,10]

Output: 7

Explanation:

Reschedule the meeting at [0, 1] to [8, 9], leaving no meetings during the time
[0, 7].

Example 3:

Input: eventTime = 10, startTime = [0,3,7,9], endTime = [1,4,8,10]

Output: 6

Explanation:

Reschedule the meeting at [3, 4] to [8, 9], leaving no meetings during the time
[1, 7].

Example 4:

Input: eventTime = 5, startTime = [0,1,2,3,4], endTime = [1,2,3,4,5]

Output: 0

Explanation:

There is no time during the event not occupied by meetings.

Constraints:

1 <= eventTime <= 10^9
n == startTime.length == endTime.length
2 <= n <= 10^5
0 <= startTime[i] < endTime[i] <= eventTime
endTime[i] <= startTime[i + 1] where i lies in the range [0, n - 2].

"""

# V0
# IDEA : ONE MEETING MAY TELEPORT — ASK WHETHER SOME OTHER GAP CAN HOST IT
#
#   again work with the n + 1 gaps.  moving a single meeting i can do one of two
#   things:
#
#     - slide it against a neighbour, which merges gaps i and i+1 into
#       gaps[i] + gaps[i+1];
#     - lift it out entirely and drop it into some *other* gap, which merges
#       gaps[i] + duration_i + gaps[i+1].
#
#   the second option is what the relaxed ordering buys us, and it is available
#   exactly when some gap other than i and i+1 is at least as wide as the
#   meeting.  the two neighbouring gaps are excluded because putting the meeting
#   back next to itself is the first case, not the second.
#
#   "is there a wide enough gap outside {i, i+1}" is answered in O(1) by prefix
#   and suffix maxima of the gap array.  the do-nothing answer, the plain widest
#   gap, is covered because gaps[i] + gaps[i+1] already dominates it.
#
# time = O(n), space = O(n)
class Solution(object):
    def maxFreeTime(self, eventTime, startTime, endTime):
        n = len(startTime)
        gaps = [startTime[0]]
        for i in range(1, n):
            gaps.append(startTime[i] - endTime[i - 1])
        gaps.append(eventTime - endTime[n - 1])
        g = len(gaps)

        pre = [0] * g
        suf = [0] * g
        run = 0
        for i in range(g):
            pre[i] = run
            if gaps[i] > run:
                run = gaps[i]
        run = 0
        for i in range(g - 1, -1, -1):
            suf[i] = run
            if gaps[i] > run:
                run = gaps[i]

        ans = 0
        for i in range(n):
            dur = endTime[i] - startTime[i]
            merged = gaps[i] + gaps[i + 1]
            outside = pre[i] if pre[i] > suf[i + 1] else suf[i + 1]
            if outside >= dur:
                merged += dur
            if merged > ans:
                ans = merged
        return ans
