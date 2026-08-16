"""

3635. Earliest Finish Time for Land and Water Rides II
Medium

You are given two categories of theme park attractions: land rides and water
rides.

Land rides

landStartTime[i] – the earliest time the i^th land ride can be boarded.

landDuration[i] – how long the i^th land ride lasts.

Water rides

waterStartTime[j] – the earliest time the j^th water ride can be boarded.

waterDuration[j] – how long the j^th water ride lasts.

A tourist must experience exactly one ride from each category, in either
order.

A ride may be started at its opening time or any later moment.

If a ride is started at time t, it finishes at time t + duration.

Immediately after finishing one ride the tourist may board the other (if it
is already open) or wait until it opens.

Return the earliest possible time at which the tourist can finish both rides.

Example 1:

Input: landStartTime = [2,8], landDuration = [4,1], waterStartTime = [6], waterDuration = [3]
Output: 9
Explanation:
Plan A (land ride 0 → water ride 0):
Start land ride 0 at time landStartTime[0] = 2. Finish at 2 +
landDuration[0] = 6.
Water ride 0 opens at time waterStartTime[0] = 6. Start immediately at 6,
finish at 6 + waterDuration[0] = 9.
Plan B (water ride 0 → land ride 1):
Start water ride 0 at time waterStartTime[0] = 6. Finish at 6 +
waterDuration[0] = 9.
Land ride 1 opens at landStartTime[1] = 8. Start at time 9, finish at 9 +
landDuration[1] = 10.
Plan C (land ride 1 → water ride 0):
Start land ride 1 at time landStartTime[1] = 8. Finish at 8 +
landDuration[1] = 9.
Water ride 0 opened at waterStartTime[0] = 6. Start at time 9, finish at 9 +
waterDuration[0] = 12.
Plan D (water ride 0 → land ride 0):
Start water ride 0 at time waterStartTime[0] = 6. Finish at 6 +
waterDuration[0] = 9.
Land ride 0 opened at landStartTime[0] = 2. Start at time 9, finish at 9 +
landDuration[0] = 13.
Plan A gives the earliest finish time of 9.

Example 2:

Input: landStartTime = [5], landDuration = [3], waterStartTime = [1], waterDuration = [10]
Output: 14
Explanation:
Plan A (water ride 0 → land ride 0):
Start water ride 0 at time waterStartTime[0] = 1. Finish at 1 +
waterDuration[0] = 11.
Land ride 0 opened at landStartTime[0] = 5. Start immediately at 11 and
finish at 11 + landDuration[0] = 14.
Plan B (land ride 0 → water ride 0):
Start land ride 0 at time landStartTime[0] = 5. Finish at 5 +
landDuration[0] = 8.
Water ride 0 opened at waterStartTime[0] = 1. Start immediately at 8 and
finish at 8 + waterDuration[0] = 18.
Plan A provides the earliest finish time of 14.

Constraints:

1 <= n, m <= 5 * 10^4
landStartTime.length == landDuration.length == n
waterStartTime.length == waterDuration.length == m
1 <= landStartTime[i], landDuration[i], waterStartTime[j], waterDuration[j] <= 10^5

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
