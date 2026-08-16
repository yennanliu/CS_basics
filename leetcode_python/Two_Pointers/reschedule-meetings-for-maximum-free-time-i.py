"""

3439. Reschedule Meetings for Maximum Free Time I
Medium

You are given an integer eventTime denoting the duration of an event, where the
event occurs from time t = 0 to time t = eventTime.

You are also given two integer arrays startTime and endTime, each of length n.
These represent the start and end time of n non-overlapping meetings, where the
i^th meeting occurs during the time [startTime[i], endTime[i]].

You can reschedule at most k meetings by moving their start time while
maintaining the same duration, to maximize the longest continuous period of free
time during the event.

The relative order of all the meetings should stay the same and they should
remain non-overlapping.

Return the maximum amount of free time possible after rearranging the meetings.

Note that the meetings can not be rescheduled to a time outside the event.

Example 1:

Input: eventTime = 5, k = 1, startTime = [1,3], endTime = [2,5]

Output: 2

Explanation:

Reschedule the meeting at [1, 2] to [2, 3], leaving no meetings during the time
[0, 2].

Example 2:

Input: eventTime = 10, k = 1, startTime = [0,2,9], endTime = [1,4,10]

Output: 6

Explanation:

Reschedule the meeting at [2, 4] to [1, 3], leaving no meetings during the time
[3, 9].

Example 3:

Input: eventTime = 5, k = 2, startTime = [0,1,2,3,4], endTime = [1,2,3,4,5]

Output: 0

Explanation:

There is no time during the event not occupied by meetings.

Constraints:

1 <= eventTime <= 10^9
n == startTime.length == endTime.length
2 <= n <= 10^5
1 <= k <= n
0 <= startTime[i] < endTime[i] <= eventTime
endTime[i] <= startTime[i + 1] where i lies in the range [0, n - 2].

"""

# V0
# IDEA : THE MEETINGS ARE JUST SPACERS — SLIDE A WINDOW OVER THE GAPS
#
#   forget the meetings and look at the n + 1 gaps: before the first meeting,
#   between consecutive ones, and after the last.  the order of the meetings is
#   fixed and their durations never change, so rescheduling meeting i can only
#   shuffle free time from one side of it to the other — the *total* free time
#   is invariant.
#
#   sliding k consecutive meetings all the way to one side welds together the
#   k + 1 gaps they separate, and no other move can merge more than that: each
#   moved meeting removes exactly one wall, and there are only k moves.  so the
#   answer is the largest sum of k + 1 consecutive gaps, a fixed-width window
#   sum in one pass.
#
# time = O(n), space = O(n)
class Solution(object):
    def maxFreeTime(self, eventTime, k, startTime, endTime):
        n = len(startTime)
        gaps = [startTime[0]]
        for i in range(1, n):
            gaps.append(startTime[i] - endTime[i - 1])
        gaps.append(eventTime - endTime[n - 1])

        width = k + 1
        cur = sum(gaps[:width])
        ans = cur
        for i in range(width, len(gaps)):
            cur += gaps[i] - gaps[i - width]
            if cur > ans:
                ans = cur
        return ans
