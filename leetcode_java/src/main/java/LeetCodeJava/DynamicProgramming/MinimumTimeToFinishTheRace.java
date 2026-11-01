package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-time-to-finish-the-race/description/

import java.util.Arrays;

/**
 * 2188. Minimum Time to Finish the Race
 * Hard
 *
 * You are given a 0-indexed 2D integer array tires where tires[i] = [fi, ri] indicates that
 * the ith tire can finish its xth successive lap in fi * ri^(x-1) seconds.
 *
 * For example, if fi = 3 and ri = 2, then the tire would finish its 1st lap in 3 seconds,
 * its 2nd lap in 3 * 2 = 6 seconds, its 3rd lap in 3 * 2^2 = 12 seconds, etc.
 *
 * You are also given an integer changeTime and an integer numLaps.
 *
 * The race consists of numLaps laps and you may start the race with any tire. You have an
 * unlimited supply of each tire and after every lap, you may change to any given tire
 * (including the current tire type) if you wait changeTime seconds.
 *
 * Return the minimum time to finish the race.
 *
 *
 * Example 1:
 *
 * Input: tires = [[2,3],[3,4]], changeTime = 5, numLaps = 4
 * Output: 21
 * Explanation:
 * Lap 1: Start with tire 0 and finish the lap in 2 seconds.
 * Lap 2: Continue with tire 0 and finish the lap in 2 * 3 = 6 seconds.
 * Lap 3: Change tires to a new tire 0 for 5 seconds and then finish the lap in another 2 seconds.
 * Lap 4: Continue with tire 0 and finish the lap in 2 * 3 = 6 seconds.
 * Total time = 2 + 6 + 5 + 2 + 6 = 21 seconds.
 * The minimum time to complete the race is 21 seconds.
 *
 * Example 2:
 *
 * Input: tires = [[1,10],[2,2],[3,4]], changeTime = 6, numLaps = 5
 * Output: 25
 * Explanation:
 * Lap 1: Start with tire 1 and finish the lap in 2 seconds.
 * Lap 2: Continue with tire 1 and finish the lap in 2 * 2 = 4 seconds.
 * Lap 3: Change tires to a new tire 1 for 6 seconds and then finish the lap in another 2 seconds.
 * Lap 4: Continue with tire 1 and finish the lap in 2 * 2 = 4 seconds.
 * Lap 5: Change tires to tire 0 for 6 seconds then finish the lap in another 1 second.
 * Total time = 2 + 4 + 6 + 2 + 4 + 6 + 1 = 25 seconds.
 * The minimum time to complete the race is 25 seconds.
 *
 *
 * Constraints:
 *
 * 1 <= tires.length <= 10^5
 * tires[i].length == 2
 * 1 <= fi, changeTime <= 10^5
 * 2 <= ri <= 10^5
 * 1 <= numLaps <= 1000
 *
 */
public class MinimumTimeToFinishTheRace {

    /** A stint longer than this never pays off: with r >= 2 the lap time at least doubles. */
    private static final int MAX_STINT = 18;

    private static final long INF = Long.MAX_VALUE / 2;

    // V0
    // IDEA: PRECOMPUTE "BEST STINT OF k LAPS", THEN A 1D DP OVER THE LAPS
    //   bestStint[k] = cheapest time to run k consecutive laps on one fresh tire (over all tires)
    //   dp[i]        = min time for i laps, paying changeTime BEFORE every stint (first included)
    //   dp[i]        = min over k of dp[i - k] + changeTime + bestStint[k]
    //   answer       = dp[numLaps] - changeTime  (the first stint needed no pit stop)
    /**
     * time = O(M * 18 + numLaps * 18)   M = tires.length
     * space = O(numLaps)
     */
    public int minimumFinishTime(int[][] tires, int changeTime, int numLaps) {
        // bestStint[k] = cheapest way to run k laps without changing tires
        long[] bestStint = new long[MAX_STINT + 1];
        Arrays.fill(bestStint, INF);

        for (int[] tire : tires) {
            long firstLap = tire[0];
            long ratio = tire[1];

            // long: firstLap * ratio^k overflows int within a few laps
            long lapTime = firstLap;
            long stintTotal = 0;

            for (int laps = 1; laps <= MAX_STINT; laps++) {
                // once this lap costs more than pitting + a fresh first lap,
                // no longer stint on this tire can win
                if (lapTime > changeTime + firstLap) {
                    break;
                }
                stintTotal += lapTime;
                bestStint[laps] = Math.min(bestStint[laps], stintTotal);
                lapTime *= ratio;
            }
        }

        // dp[i] = min time for i laps, paying changeTime before every stint
        long[] dp = new long[numLaps + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int i = 1; i <= numLaps; i++) {
            // the last stint covers laps (i - laps, i]
            int longestStint = Math.min(i, MAX_STINT);
            for (int laps = 1; laps <= longestStint; laps++) {
                if (bestStint[laps] == INF) {
                    continue;
                }
                long candidate = dp[i - laps] + changeTime + bestStint[laps];
                dp[i] = Math.min(dp[i], candidate);
            }
        }

        // the first stint did not actually need a pit stop
        return (int) (dp[numLaps] - changeTime);
    }
}
