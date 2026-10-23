"""

2188. Minimum Time to Finish the Race
Hard

You are given a 0-indexed 2D integer array tires where tires[i] = [fi, ri] indicates that the ith tire can finish its xth successive lap in fi * ri^(x-1) seconds.

For example, if fi = 3 and ri = 2, then the tire would finish its 1st lap in 3 seconds, its 2nd lap in 3 * 2 = 6 seconds, its 3rd lap in 3 * 2^2 = 12 seconds, etc.

You are also given an integer changeTime and an integer numLaps.

The race consists of numLaps laps and you may start the race with any tire. You have an unlimited supply of each tire and after every lap, you may change to any given tire (including the current tire type) if you wait changeTime seconds.

Return the minimum time to finish the race.


Example 1:

Input: tires = [[2,3],[3,4]], changeTime = 5, numLaps = 4
Output: 21
Explanation:
Lap 1: Start with tire 0 and finish the lap in 2 seconds.
Lap 2: Continue with tire 0 and finish the lap in 2 * 3 = 6 seconds.
Lap 3: Change tires to a new tire 0 for 5 seconds and then finish the lap in another 2 seconds.
Lap 4: Continue with tire 0 and finish the lap in 2 * 3 = 6 seconds.
Total time = 2 + 6 + 5 + 2 + 6 = 21 seconds.
The minimum time to complete the race is 21 seconds.

Example 2:

Input: tires = [[1,10],[2,2],[3,4]], changeTime = 6, numLaps = 5
Output: 25
Explanation:
Lap 1: Start with tire 1 and finish the lap in 2 seconds.
Lap 2: Continue with tire 1 and finish the lap in 2 * 2 = 4 seconds.
Lap 3: Change tires to a new tire 1 for 6 seconds and then finish the lap in another 2 seconds.
Lap 4: Continue with tire 1 and finish the lap in 2 * 2 = 4 seconds.
Lap 5: Change tires to tire 0 for 6 seconds then finish the lap in another 1 second.
Total time = 2 + 4 + 6 + 2 + 4 + 6 + 1 = 25 seconds.
The minimum time to complete the race is 25 seconds.


Constraints:

1 <= tires.length <= 10^5
tires[i].length == 2
1 <= fi, changeTime <= 10^5
2 <= ri <= 10^5
1 <= numLaps <= 1000

"""

# V0
# IDEA : PRECOMPUTE "BEST STINT OF k LAPS", THEN A 1D DP OVER THE LAPS
#
#   a stint never runs long : with r >= 2 the lap time at least doubles, so
#   once  f * r^(k-1) > changeTime + f  it is strictly better to pit. that
#   caps a useful stint at about 18 laps (2^17 already exceeds 10^5).
#
#   DP def
#     best_stint[k] : cheapest total time to run k CONSECUTIVE laps on one
#                     fresh tire, minimised over all tires
#     dp[i]         : min time to finish i laps, counting a pit stop BEFORE
#                     every stint (including the first, subtracted at the end)
#
#   DP eq
#     best_stint[k] = min over tires (f, r) of  f + f*r + ... + f*r^(k-1)
#     dp[i]         = min over k of  dp[i - k] + changeTime + best_stint[k]
#
#   init : dp[0] = 0          answer : dp[numLaps] - changeTime
#
# time = O(m * 18 + numLaps * 18), space = O(numLaps)    m = len(tires)
class Solution(object):
    def minimumFinishTime(self, tires, changeTime, numLaps):
        """
        :type tires: List[List[int]]
        :type changeTime: int
        :type numLaps: int
        :rtype: int
        """
        MAX_STINT = 18
        INF = float('inf')

        # best_stint[k] = cheapest way to run k laps without changing tires
        best_stint = [INF] * (MAX_STINT + 1)
        for first_lap, ratio in tires:
            lap_time = first_lap
            stint_total = 0
            for laps in range(1, MAX_STINT + 1):
                # NOTE !!! once this lap costs more than pitting + a fresh
                #          first lap, no longer stint on this tire can win
                if lap_time > changeTime + first_lap:
                    break
                stint_total += lap_time
                best_stint[laps] = min(best_stint[laps], stint_total)
                lap_time *= ratio

        # dp[i] = min time for i laps, paying changeTime before every stint
        dp = [INF] * (numLaps + 1)
        dp[0] = 0
        for i in range(1, numLaps + 1):
            # the last stint covers laps (i - laps, i]
            for laps in range(1, min(i, MAX_STINT) + 1):
                if best_stint[laps] == INF:
                    continue
                candidate = dp[i - laps] + changeTime + best_stint[laps]
                dp[i] = min(dp[i], candidate)

        # the first stint did not actually need a pit stop
        return dp[numLaps] - changeTime
