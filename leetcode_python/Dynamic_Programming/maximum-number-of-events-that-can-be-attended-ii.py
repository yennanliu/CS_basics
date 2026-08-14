"""

1751. Maximum Number of Events That Can Be Attended II
Hard

You are given an array of events where events[i] = [startDayi, endDayi, valuei]. The i^th event starts at startDayi and ends at endDayi, and if you attend this event, you will receive a value of valuei. You are also given an integer k which represents the maximum number of events you can attend.

You can only attend one event at a time. If you choose to attend an event, you must attend the entire event. Note that the end day is inclusive: that is, you cannot attend two events where one of them starts and the other ends on the same day.

Return the maximum sum of values that you can receive by attending events.

Example 1:

Input: events = [[1,2,4],[3,4,3],[2,3,1]], k = 2
Output: 7
Explanation: Choose the green events, 0 and 1 (0-indexed) for a total value of 4 + 3 = 7.

Example 2:

Input: events = [[1,2,4],[3,4,3],[2,3,10]], k = 2
Output: 10
Explanation: Choose event 2 for a total value of 10.
Notice that you cannot attend any other event as they overlap, and that you do not have to attend k events.

Example 3:

Input: events = [[1,1,1],[2,2,2],[3,3,3],[4,4,4]], k = 3
Output: 9
Explanation: Although the events do not overlap, you can only attend 3 events. Pick the highest valued three.

Constraints:

1 <= k <= events.length
1 <= k * events.length <= 10^6
1 <= startDayi <= endDayi <= 10^9
1 <= valuei <= 10^6

"""

# V0
# IDEA : DP + BINARY SEARCH (weighted interval scheduling, capped at k picks)
#
#   sort events by END day, then let f[i][j] = best total value using the
#   first i events while attending at most j of them.
#     - skip event i -> f[i - 1][j]
#     - take event i -> f[p][j - 1] + value_i
#   where p = how many events finish STRICTLY before event i starts, found by
#   bisect_left on the (sorted) end days with key = start_i.
#   NOTE : the end day is inclusive, so "ends on start_i" still clashes,
#          which is exactly why bisect_left (not bisect_right) is used.
#
# time = O(n log n + n * k), space = O(n * k)
from bisect import bisect_left
class Solution(object):
    def maxValue(self, events, k):
        events.sort(key=lambda x: x[1])
        n = len(events)
        ends = [e[1] for e in events]

        f = [[0] * (k + 1) for _ in range(n + 1)]
        for i in range(1, n + 1):
            st, _, val = events[i - 1]
            p = bisect_left(ends, st, 0, i - 1)
            for j in range(1, k + 1):
                f[i][j] = max(f[i - 1][j], f[p][j - 1] + val)
        return f[n][k]
