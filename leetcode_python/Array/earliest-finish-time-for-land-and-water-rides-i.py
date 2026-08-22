"""

3633. Earliest Finish Time for Land and Water Rides I
Easy

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

1 <= n, m <= 100
landStartTime.length == landDuration.length == n
waterStartTime.length == waterDuration.length == m
1 <= landStartTime[i], landDuration[i], waterStartTime[j], waterDuration[j] <= 1000

"""

# V0
# IDEA : BRUTE FORCE OVER BOTH ORDERS
#
#   waiting is never useful before the FIRST ride: boarding it exactly at its
#   own start time is optimal, because every later boarding only pushes the
#   second ride's start later or leaves it unchanged.
#
#   so a plan is fully described by (which land ride, which water ride, which
#   goes first), and its finish time is
#       t = start_first + duration_first
#       finish = max(t, start_second) + duration_second
#
#   n, m <= 100 so all 2*n*m plans can simply be enumerated.
#
# time = O(n * m), space = O(1)
class Solution(object):
    def earliestFinishTime(self, landStartTime, landDuration, waterStartTime, waterDuration):
        best = float('inf')
        for i in range(len(landStartTime)):
            t = landStartTime[i] + landDuration[i]
            for j in range(len(waterStartTime)):
                fin = max(t, waterStartTime[j]) + waterDuration[j]
                if fin < best:
                    best = fin
        for j in range(len(waterStartTime)):
            t = waterStartTime[j] + waterDuration[j]
            for i in range(len(landStartTime)):
                fin = max(t, landStartTime[i]) + landDuration[i]
                if fin < best:
                    best = fin
        return best


# V0-1
# IDEA : ONLY THE EARLIEST-FINISHING FIRST RIDE MATTERS -- O(n + m)
#
#   fix the order (say land first). the finish time is
#       max(landFinish_i, ws_j) + wd_j
#   which is NON-DECREASING in landFinish_i, so for EVERY choice of j the best
#   partner i is the same one : the land ride with the smallest ls_i + ld_i.
#
#   that collapses the double loop of V0 into "one min over the first category,
#   then one min over the second", done once per order.
#
# time = O(n + m), space = O(1)
class Solution(object):
    def earliestFinishTime(self, landStartTime, landDuration, waterStartTime, waterDuration):
        def best(firstStart, firstDur, secondStart, secondDur):
            t = min(s + d for s, d in zip(firstStart, firstDur))
            return min(max(t, s) + d for s, d in zip(secondStart, secondDur))

        return min(
            best(landStartTime, landDuration, waterStartTime, waterDuration),
            best(waterStartTime, waterDuration, landStartTime, landDuration),
        )


# V0-2
# IDEA : BUCKET THE SECOND CATEGORY BY OPENING TIME (PREFIX / SUFFIX MINIMA)
#
#   answer the query "given I am free at time t, when can I finish a ride of the
#   second category?" for EVERY t in O(1), by splitting the rides in two:
#     - already open (ss_j <= t)  -> finish t + sd_j   -> want min duration,
#       so openBy[T] = prefix-min of sd_j over ss_j <= T
#     - not open yet (ss_j >  t)  -> finish ss_j + sd_j -> want min of that sum,
#       so fromT[T] = suffix-min of (ss_j + sd_j) over ss_j >= T
#   then f(t) = min(t + openBy[t], fromT[t + 1]).
#
#   times are bounded (start, duration <= 1000 => any finish <= 2000), so both
#   tables are plain arrays indexed by time -- a counting/bucket table rather
#   than a sort or a scan. Unlike V0-1 this handles every first ride cheaply,
#   which is what you would need if a first ride could be forbidden per query.
#
# time = O(n + m + MAX_T), space = O(MAX_T)
class Solution(object):
    def earliestFinishTime(self, landStartTime, landDuration, waterStartTime, waterDuration):
        INF = float('inf')
        LIM = 2001          # every reachable time index, inclusive

        def best(firstStart, firstDur, secondStart, secondDur):
            openBy = [INF] * (LIM + 2)
            fromT = [INF] * (LIM + 2)
            for s, d in zip(secondStart, secondDur):
                if d < openBy[s]:
                    openBy[s] = d
                if s + d < fromT[s]:
                    fromT[s] = s + d
            for T in range(1, LIM + 1):
                if openBy[T - 1] < openBy[T]:
                    openBy[T] = openBy[T - 1]
            for T in range(LIM - 1, -1, -1):
                if fromT[T + 1] < fromT[T]:
                    fromT[T] = fromT[T + 1]

            res = INF
            for s, d in zip(firstStart, firstDur):
                t = s + d
                cand = min(t + openBy[t], fromT[t + 1])
                if cand < res:
                    res = cand
            return res

        return min(
            best(landStartTime, landDuration, waterStartTime, waterDuration),
            best(waterStartTime, waterDuration, landStartTime, landDuration),
        )
