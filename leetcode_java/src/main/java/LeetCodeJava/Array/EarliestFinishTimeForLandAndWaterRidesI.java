package LeetCodeJava.Array;

// https://leetcode.com/problems/earliest-finish-time-for-land-and-water-rides-i/description/

/**
 * 3633. Earliest Finish Time for Land and Water Rides I
 * Easy
 *
 * You are given two categories of theme park attractions: land rides and water rides.
 *
 * Land rides
 * landStartTime[i] - the earliest time the i^th land ride can be boarded.
 * landDuration[i] - how long the i^th land ride lasts.
 *
 * Water rides
 * waterStartTime[j] - the earliest time the j^th water ride can be boarded.
 * waterDuration[j] - how long the j^th water ride lasts.
 *
 * A tourist must experience exactly one ride from each category, in either order.
 *
 * A ride may be started at its opening time or any later moment.
 * If a ride is started at time t, it finishes at time t + duration.
 * Immediately after finishing one ride the tourist may board the other (if it is
 * already open) or wait until it opens.
 *
 * Return the earliest possible time at which the tourist can finish both rides.
 *
 *
 * Example 1:
 *
 * Input: landStartTime = [2,8], landDuration = [4,1], waterStartTime = [6], waterDuration = [3]
 * Output: 9
 *
 * Example 2:
 *
 * Input: landStartTime = [5], landDuration = [3], waterStartTime = [1], waterDuration = [10]
 * Output: 14
 *
 *
 * Constraints:
 *
 * 1 <= n, m <= 100
 * landStartTime.length == landDuration.length == n
 * waterStartTime.length == waterDuration.length == m
 * 1 <= landStartTime[i], landDuration[i], waterStartTime[j], waterDuration[j] <= 1000
 *
 */
public class EarliestFinishTimeForLandAndWaterRidesI {

    // V0
    // IDEA: BRUTE FORCE OVER EVERY PAIR, IN BOTH ORDERS
    //   board the first ride at its opening time (waiting never helps), then
    //   finish = max(firstFinish, secondStart) + secondDuration.
    //   n, m <= 100, so all 2 * n * m plans can be enumerated.
    /**
     * time = O(N * M)
     * space = O(1)
     */
    public int earliestFinishTime(int[] landStartTime, int[] landDuration,
                                  int[] waterStartTime, int[] waterDuration) {
        int best = Integer.MAX_VALUE;

        // land first, then water
        for (int i = 0; i < landStartTime.length; i++) {
            int landFinish = landStartTime[i] + landDuration[i];
            for (int j = 0; j < waterStartTime.length; j++) {
                int finish = Math.max(landFinish, waterStartTime[j]) + waterDuration[j];
                best = Math.min(best, finish);
            }
        }

        // water first, then land
        for (int j = 0; j < waterStartTime.length; j++) {
            int waterFinish = waterStartTime[j] + waterDuration[j];
            for (int i = 0; i < landStartTime.length; i++) {
                int finish = Math.max(waterFinish, landStartTime[i]) + landDuration[i];
                best = Math.min(best, finish);
            }
        }
        return best;
    }

    // V1
    // IDEA: ONLY THE EARLIEST-FINISHING FIRST RIDE MATTERS
    //   for a fixed order, max(firstFinish, secondStart) + secondDuration never gets
    //   worse when firstFinish gets smaller, so the best first ride is the same for
    //   every second ride: the one with the smallest start + duration.
    /**
     * time = O(N + M)
     * space = O(1)
     */
    public int earliestFinishTime_1(int[] landStartTime, int[] landDuration,
                                    int[] waterStartTime, int[] waterDuration) {
        int landFirst = bestForOrder(landStartTime, landDuration, waterStartTime, waterDuration);
        int waterFirst = bestForOrder(waterStartTime, waterDuration, landStartTime, landDuration);
        return Math.min(landFirst, waterFirst);
    }

    /** Earliest finish when a ride of the first category is taken before one of the second. */
    private int bestForOrder(int[] firstStart, int[] firstDuration,
                             int[] secondStart, int[] secondDuration) {
        int earliestFirstFinish = Integer.MAX_VALUE;
        for (int i = 0; i < firstStart.length; i++) {
            earliestFirstFinish = Math.min(earliestFirstFinish, firstStart[i] + firstDuration[i]);
        }

        int best = Integer.MAX_VALUE;
        for (int j = 0; j < secondStart.length; j++) {
            int finish = Math.max(earliestFirstFinish, secondStart[j]) + secondDuration[j];
            best = Math.min(best, finish);
        }
        return best;
    }
}
