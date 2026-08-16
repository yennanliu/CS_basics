"""

3635. Earliest Finish Time for Land and Water Rides II
Medium

You are given two categories of theme park attractions: land rides and water rides.

Land rides are represented by two arrays landStartTime and landDuration, where landStartTime[i] is the earliest time the ith land ride can be boarded, and landDuration[i] is how long it lasts.
Water rides are represented by two arrays waterStartTime and waterDuration, where waterStartTime[j] is the earliest time the jth water ride can be boarded, and waterDuration[j] is how long it lasts.

A tourist must experience exactly one ride of each type, in either order.

A ride can be boarded at any time t >= the ride's start time. If a ride is boarded at time t, it ends at time t + duration.
Immediately after finishing one ride the tourist may board the other (if it is already available) or wait for it to become available.

Return the earliest possible time at which the tourist can finish both rides.


Example 1:

Input: landStartTime = [2,8], landDuration = [4,1], waterStartTime = [6], waterDuration = [3]
Output: 9
Explanation:
Board the land ride at index 0 at time 2, it finishes at 2 + 4 = 6. Then board the water ride at index 0 at time 6, it finishes at 6 + 3 = 9.
Hence, the earliest time to finish both rides is 9.

Example 2:

Input: landStartTime = [5], landDuration = [3], waterStartTime = [1], waterDuration = [10]
Output: 14
Explanation:
Board the water ride at index 0 at time 1, it finishes at 1 + 10 = 11. Then board the land ride at index 0 at time 11, it finishes at 11 + 3 = 14.
Hence, the earliest time to finish both rides is 14.


Constraints:

1 <= n, m <= 2 * 10^5
landStartTime.length == landDuration.length == n
waterStartTime.length == waterDuration.length == m
1 <= landStartTime[i], landDuration[i] <= 10^9
1 <= waterStartTime[j], waterDuration[j] <= 10^9

"""

# V0
# IDEA : SORT THE SECOND RIDE BY START TIME + PREFIX/SUFFIX MINIMA
#
#   fix the order (say land then water) and fix the land ride, giving a
#   handover time t. the best second ride minimises
#       max(t, start[j]) + dur[j]
#   which splits cleanly at t:
#     * rides already available (start[j] <= t) cost t + dur[j]  -> want the
#       smallest dur[j] among them,
#     * rides not yet open (start[j] > t)   cost start[j] + dur[j] -> want the
#       smallest start[j] + dur[j] among them.
#
#   sorting the second category by start time turns "already available" into
#   a prefix and "not yet open" into a suffix, so a prefix-min of dur and a
#   suffix-min of start+dur answer each query after one binary search.
#
#   run the whole thing twice, once per order.
#
# time = O(n log n + m log m), space = O(n + m)
import bisect


class Solution(object):
    def earliestFinishTime(self, landStartTime, landDuration, waterStartTime, waterDuration):
        def build(start, dur):
            order = sorted(range(len(start)), key=lambda i: start[i])
            st = [start[i] for i in order]
            du = [dur[i] for i in order]
            m = len(st)
            pre = [0] * m           # pre[i] = min dur over first i+1
            best = float('inf')
            for i in range(m):
                if du[i] < best:
                    best = du[i]
                pre[i] = best
            suf = [0] * (m + 1)     # suf[i] = min (st+du) over i..m-1
            suf[m] = float('inf')
            for i in range(m - 1, -1, -1):
                suf[i] = min(suf[i + 1], st[i] + du[i])
            return st, pre, suf

        def query(pack, t):
            st, pre, suf = pack
            # rides with st[j] <= t
            k = bisect.bisect_right(st, t)
            res = suf[k]
            if k > 0:
                res = min(res, t + pre[k - 1])
            return res

        land = build(landStartTime, landDuration)
        water = build(waterStartTime, waterDuration)

        best = float('inf')
        for i in range(len(landStartTime)):
            t = landStartTime[i] + landDuration[i]
            best = min(best, query(water, t))
        for j in range(len(waterStartTime)):
            t = waterStartTime[j] + waterDuration[j]
            best = min(best, query(land, t))
        return best
