package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/earliest-finish-time-for-land-and-water-rides-ii/description/

import java.util.Arrays;

/**
 * 3635. Earliest Finish Time for Land and Water Rides II
 * Medium
 *
 * You are given two categories of theme park attractions: land rides and water
 * rides.
 *
 * Land rides
 *
 * landStartTime[i] – the earliest time the i^th land ride can be boarded.
 *
 * landDuration[i] – how long the i^th land ride lasts.
 *
 * Water rides
 *
 * waterStartTime[j] – the earliest time the j^th water ride can be boarded.
 *
 * waterDuration[j] – how long the j^th water ride lasts.
 *
 * A tourist must experience exactly one ride from each category, in either
 * order.
 *
 * A ride may be started at its opening time or any later moment.
 *
 * If a ride is started at time t, it finishes at time t + duration.
 *
 * Immediately after finishing one ride the tourist may board the other (if it
 * is already open) or wait until it opens.
 *
 * Return the earliest possible time at which the tourist can finish both rides.
 *
 * Example 1:
 *
 * Input: landStartTime = [2,8], landDuration = [4,1], waterStartTime = [6], waterDuration = [3]
 * Output: 9
 * Explanation:
 * Plan A (land ride 0 → water ride 0):
 * Start land ride 0 at time landStartTime[0] = 2. Finish at 2 +
 * landDuration[0] = 6.
 * Water ride 0 opens at time waterStartTime[0] = 6. Start immediately at 6,
 * finish at 6 + waterDuration[0] = 9.
 * Plan B (water ride 0 → land ride 1):
 * Start water ride 0 at time waterStartTime[0] = 6. Finish at 6 +
 * waterDuration[0] = 9.
 * Land ride 1 opens at landStartTime[1] = 8. Start at time 9, finish at 9 +
 * landDuration[1] = 10.
 * Plan C (land ride 1 → water ride 0):
 * Start land ride 1 at time landStartTime[1] = 8. Finish at 8 +
 * landDuration[1] = 9.
 * Water ride 0 opened at waterStartTime[0] = 6. Start at time 9, finish at 9 +
 * waterDuration[0] = 12.
 * Plan D (water ride 0 → land ride 0):
 * Start water ride 0 at time waterStartTime[0] = 6. Finish at 6 +
 * waterDuration[0] = 9.
 * Land ride 0 opened at landStartTime[0] = 2. Start at time 9, finish at 9 +
 * landDuration[0] = 13.
 * Plan A gives the earliest finish time of 9.
 *
 * Example 2:
 *
 * Input: landStartTime = [5], landDuration = [3], waterStartTime = [1], waterDuration = [10]
 * Output: 14
 * Explanation:
 * Plan A (water ride 0 → land ride 0):
 * Start water ride 0 at time waterStartTime[0] = 1. Finish at 1 +
 * waterDuration[0] = 11.
 * Land ride 0 opened at landStartTime[0] = 5. Start immediately at 11 and
 * finish at 11 + landDuration[0] = 14.
 * Plan B (land ride 0 → water ride 0):
 * Start land ride 0 at time landStartTime[0] = 5. Finish at 5 +
 * landDuration[0] = 8.
 * Water ride 0 opened at waterStartTime[0] = 1. Start immediately at 8 and
 * finish at 8 + waterDuration[0] = 18.
 * Plan A provides the earliest finish time of 14.
 *
 * Constraints:
 *
 * 1 <= n, m <= 5 * 10^4
 * landStartTime.length == landDuration.length == n
 * waterStartTime.length == waterDuration.length == m
 * 1 <= landStartTime[i], landDuration[i], waterStartTime[j], waterDuration[j] <= 10^5
 *
 */
public class EarliestFinishTimeForLandAndWaterRides2 {

    // V0
    // IDEA: SORT THE SECOND RIDE BY START + PREFIX-MIN / SUFFIX-MIN + BINARY SEARCH
    // fix the order (land then water) and the first ride, which hands over at time t. The
    // second ride j finishes at max(t, start[j]) + duration[j], which splits at t:
    //   start[j] <= t (already open) : t + duration[j]      -> prefix min of duration
    //   start[j] >  t (opens later)  : start[j] + duration[j] -> suffix min of start + duration
    // after sorting by start, one binary search finds the split. Run both orders.
    /**
     * time = O(N log N + M log M)
     * space = O(N + M)
     */
    public int earliestFinishTime(int[] landStartTime, int[] landDuration, int[] waterStartTime, int[] waterDuration) {
        RideTable land = new RideTable(landStartTime, landDuration);
        RideTable water = new RideTable(waterStartTime, waterDuration);

        long best = Long.MAX_VALUE;
        // land first, then water
        for (int i = 0; i < landStartTime.length; i++) {
            long handover = (long) landStartTime[i] + landDuration[i];
            best = Math.min(best, water.earliestFinish(handover));
        }
        // water first, then land
        for (int j = 0; j < waterStartTime.length; j++) {
            long handover = (long) waterStartTime[j] + waterDuration[j];
            best = Math.min(best, land.earliestFinish(handover));
        }
        return (int) best;
    }

    /** One ride category, sorted by start, answering "earliest finish if I arrive at t". */
    private static class RideTable {
        private final long[] starts;
        // prefixMinDuration[i] = min duration among the first i + 1 rides (by start)
        private final long[] prefixMinDuration;
        // suffixMinFinish[i] = min (start + duration) among rides i..m-1; [m] = +infinity
        private final long[] suffixMinFinish;

        RideTable(int[] startTime, int[] duration) {
            int m = startTime.length;
            long[][] rides = new long[m][2];
            for (int i = 0; i < m; i++) {
                rides[i][0] = startTime[i];
                rides[i][1] = duration[i];
            }
            Arrays.sort(rides, (a, b) -> Long.compare(a[0], b[0]));

            starts = new long[m];
            prefixMinDuration = new long[m];
            suffixMinFinish = new long[m + 1];
            long minDuration = Long.MAX_VALUE;
            for (int i = 0; i < m; i++) {
                starts[i] = rides[i][0];
                minDuration = Math.min(minDuration, rides[i][1]);
                prefixMinDuration[i] = minDuration;
            }
            suffixMinFinish[m] = Long.MAX_VALUE;
            for (int i = m - 1; i >= 0; i--) {
                suffixMinFinish[i] = Math.min(suffixMinFinish[i + 1], rides[i][0] + rides[i][1]);
            }
        }

        /** Earliest finish of a ride from this table when arriving at time t. */
        long earliestFinish(long t) {
            // openCount = number of rides with start <= t
            int left = 0;
            int right = starts.length;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (starts[mid] > t) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            int openCount = left;

            long best = suffixMinFinish[openCount];
            if (openCount > 0) {
                best = Math.min(best, t + prefixMinDuration[openCount - 1]);
            }
            return best;
        }
    }
}
