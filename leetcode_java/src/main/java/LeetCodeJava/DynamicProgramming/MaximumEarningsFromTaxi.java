package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-earnings-from-taxi/description/

import java.util.*;

/**
 * 2008. Maximum Earnings From Taxi
 * Medium
 *
 * There are n points on a road you are driving your taxi on. The n points on the road are
 * labeled from 1 to n in the direction you are going, and you want to drive from point 1
 * to point n to make money by picking up passengers. You cannot change the direction of
 * the taxi.
 *
 * The passengers are represented by a 0-indexed 2D integer array rides, where
 * rides[i] = [starti, endi, tipi] denotes the ith passenger requesting a ride from point
 * starti to point endi who is willing to give a tipi dollar tip.
 *
 * For each passenger i you pick up, you earn endi - starti + tipi dollars. You may only
 * drive at most one passenger at a time.
 *
 * Given n and rides, return the maximum number of dollars you can earn by picking up the
 * passengers optimally.
 *
 * Note: You may drop off a passenger and pick up a different passenger at the same point.
 *
 *
 * Example 1:
 *
 * Input: n = 5, rides = [[2,5,4],[1,5,1]]
 * Output: 7
 * Explanation: We can pick up passenger 0 to earn 5 - 2 + 4 = 7 dollars.
 *
 * Example 2:
 *
 * Input: n = 20, rides = [[1,6,1],[3,10,2],[10,12,3],[11,12,2],[12,15,2],[13,18,1]]
 * Output: 20
 * Explanation: We will pick up the following passengers:
 * - Drive passenger 1 from point 3 to point 10 for a profit of 10 - 3 + 2 = 9 dollars.
 * - Drive passenger 2 from point 10 to point 12 for a profit of 12 - 10 + 3 = 5 dollars.
 * - Drive passenger 5 from point 13 to point 18 for a profit of 18 - 13 + 1 = 6 dollars.
 * We earn 9 + 5 + 6 = 20 dollars in total.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 1 <= rides.length <= 3 * 10^4
 * rides[i].length == 3
 * 1 <= starti < endi <= n
 * 1 <= tipi <= 10^5
 *
 */
public class MaximumEarningsFromTaxi {

    // V0
    // IDEA: DP OVER THE ROAD POSITION (WEIGHTED INTERVAL SCHEDULING ON A LINE)
    /**
     *  DP def:
     *     - dp[p] = max earnings by the time the taxi stands at point p
     *
     *  DP eq:
     *     dp[p] = max( dp[p-1],                                   // drive past p empty
     *                  dp[start] + (p - start + tip) )            // drop a rider off at p
     *             for every ride (start, p, tip)
     *
     *  A ride is only consumed at its END point, so two chosen rides never overlap.
     *  Rides are bucketed by end point so each one is touched once.
     *
     *  time = O(N + M), M = rides.length
     *  space = O(N + M)
     */
    public long maxTaxiEarnings(int n, int[][] rides) {
        // ridesEndingAt.get(p) = list of {start, gain} for rides that end at p
        List<List<int[]>> ridesEndingAt = new ArrayList<>();
        for (int p = 0; p <= n; p++) {
            ridesEndingAt.add(new ArrayList<>());
        }
        for (int[] ride : rides) {
            int start = ride[0];
            int end = ride[1];
            int tip = ride[2];
            ridesEndingAt.get(end).add(new int[] {start, end - start + tip});
        }

        long[] dp = new long[n + 1];
        for (int p = 1; p <= n; p++) {
            // option 1: arrive at p without finishing a ride here
            long best = dp[p - 1];
            // option 2: finish one of the rides ending at p
            for (int[] ride : ridesEndingAt.get(p)) {
                long candidate = dp[ride[0]] + ride[1];
                best = Math.max(best, candidate);
            }
            dp[p] = best;
        }
        return dp[n];
    }

    // V1
    // IDEA: SORT RIDES BY END + BINARY SEARCH THE LAST COMPATIBLE RIDE (CLASSIC WEIGHTED JOB SCHEDULING)
    /**
     *  DP def:
     *     - best[i] = max earnings using only the first i rides (sorted by end)
     *
     *  DP eq:
     *     best[i] = max( best[i-1],                               // skip ride i-1
     *                    best[j] + gain(ride i-1) )               // take it
     *     where j = number of rides whose end <= start of ride i-1 (binary search)
     *
     *  Useful when n (road length) is huge but rides are few.
     *
     *  time = O(M log M)
     *  space = O(M)
     */
    public long maxTaxiEarnings_1(int n, int[][] rides) {
        int rideCount = rides.length;
        int[][] sorted = rides.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[1], b[1]));

        long[] best = new long[rideCount + 1];
        for (int i = 1; i <= rideCount; i++) {
            int[] ride = sorted[i - 1];
            long gain = ride[1] - ride[0] + ride[2];
            // how many rides (in sorted order) end at or before this ride's start
            int compatible = countEndingAtOrBefore(sorted, i - 1, ride[0]);
            best[i] = Math.max(best[i - 1], best[compatible] + gain);
        }
        return best[rideCount];
    }

    /** Number of rides among sorted[0..limit) whose end is <= point. */
    private int countEndingAtOrBefore(int[][] sorted, int limit, int point) {
        int left = 0;
        int right = limit;
        // find the first index in [0, limit) whose end > point
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid][1] <= point) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
