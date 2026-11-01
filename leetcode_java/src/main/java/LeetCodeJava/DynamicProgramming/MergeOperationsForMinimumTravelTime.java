package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/merge-operations-for-minimum-travel-time/description/

import java.util.*;

/**
 * 3538. Merge Operations for Minimum Travel Time
 * Hard
 *
 * You are given a straight road of length l km, an integer n, an integer k, and
 * two integer arrays, position and time, each of length n.
 *
 * The array position lists the positions (in km) of signs in strictly increasing
 * order (with position[0] = 0 and position[n - 1] = l).
 *
 * Each time[i] represents the time (in minutes) required to travel 1 km between
 * position[i] and position[i + 1].
 *
 * You must perform exactly k merge operations. In one merge, you can choose any
 * two adjacent signs at indices i and i + 1 (with i > 0 and i + 1 < n) and:
 *
 * Update the sign at index i + 1 so that its time becomes time[i] + time[i + 1].
 *
 * Remove the sign at index i.
 *
 * Return the minimum total travel time (in minutes) to travel from 0 to l after
 * exactly k merges.
 *
 * Example 1:
 *
 * Input: l = 10, n = 4, k = 1, position = [0,3,8,10], time = [5,8,3,6]
 *
 * Output: 62
 *
 * Explanation:
 *
 * Merge the signs at indices 1 and 2. Remove the sign at index 1, and change the
 * time at index 2 to 8 + 3 = 11.
 *
 * After the merge:
 *
 * position array: [0, 8, 10]
 *
 * time array: [5, 11, 6]
 *
 * Segment | Distance (km) | Time per km (min) | Segment Travel Time (min)
 * --------+---------------+-------------------+--------------------------
 * 0 -> 8  | 8             | 5                 | 8 x 5 = 40
 * 8 -> 10 | 2             | 11                | 2 x 11 = 22
 *
 * Total Travel Time: 40 + 22 = 62, which is the minimum possible time after
 * exactly 1 merge.
 *
 * Example 2:
 *
 * Input: l = 5, n = 5, k = 1, position = [0,1,2,3,5], time = [8,3,9,3,3]
 *
 * Output: 34
 *
 * Explanation:
 *
 * Merge the signs at indices 1 and 2. Remove the sign at index 1, and change the
 * time at index 2 to 3 + 9 = 12.
 *
 * After the merge:
 *
 * position array: [0, 2, 3, 5]
 *
 * time array: [8, 12, 3, 3]
 *
 * Segment | Distance (km) | Time per km (min) | Segment Travel Time (min)
 * --------+---------------+-------------------+--------------------------
 * 0 -> 2  | 2             | 8                 | 2 x 8 = 16
 * 2 -> 3  | 1             | 12                | 1 x 12 = 12
 * 3 -> 5  | 2             | 3                 | 2 x 3 = 6
 *
 * Total Travel Time: 16 + 12 + 6 = 34, which is the minimum possible time after
 * exactly 1 merge.
 *
 * Constraints:
 *
 * 1 <= l <= 10^5
 *
 * 2 <= n <= min(l + 1, 50)
 *
 * 0 <= k <= min(n - 2, 10)
 *
 * position.length == n
 *
 * position[0] = 0 and position[n - 1] = l
 *
 * position is sorted in strictly increasing order.
 *
 * time.length == n
 *
 * 1 <= time[i] <= 100
 *
 * 1 <= sum(time) <= 100
 *
 */
public class MergeOperationsForMinimumTravelTime {

    // V0
    // IDEA: DP OVER (PREVIOUS SURVIVOR, CURRENT SURVIVOR, MERGES USED)
    //   after k merges exactly k interior signs are gone, and the surviving signs
    //   0 = i_0 < i_1 < ... < i_last = n - 1 describe the outcome. When the block
    //   between survivors a and b is deleted, all of its time cascades into b, so
    //   b's time is time[a+1] + ... + time[b]: the rate charged on b -> c depends
    //   on the survivor BEFORE b too, hence two indices in the state.
    //   dp[a + 1][b][r] = cheapest way to reach survivor b with predecessor a and
    //                     r signs merged away (a = -1 means b is sign 0)
    //   step to c: dp[b + 1][c][r + (c - b - 1)] =
    //              min(.., dp[a + 1][b][r] + (position[c] - position[b]) * rate(a, b))
    /**
     * time = O(N^3 * K)
     * space = O(N^2 * K)
     */
    public int minTravelTime(int l, int n, int k, int[] position, int[] time) {
        // prefix[i] = time[0] + ... + time[i - 1]
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + time[i];
        }

        final long INF = Long.MAX_VALUE;
        long[][][] dp = new long[n + 1][n][k + 1];
        for (long[][] plane : dp) {
            for (long[] row : plane) {
                Arrays.fill(row, INF);
            }
        }
        // sign 0 is always the first survivor, with no predecessor
        dp[0][0][0] = 0;

        for (int b = 0; b < n - 1; b++) {
            for (int prevPlusOne = 0; prevPlusOne <= b; prevPlusOne++) {
                // time per unit distance after b = everything that cascaded into b
                long rate = prefix[b + 1] - prefix[prevPlusOne];
                for (int used = 0; used <= k; used++) {
                    long current = dp[prevPlusOne][b][used];
                    if (current == INF) {
                        continue;
                    }
                    // the next survivor c deletes the c - b - 1 signs in between
                    for (int c = b + 1; c < n; c++) {
                        int newUsed = used + (c - b - 1);
                        if (newUsed > k) {
                            break;
                        }
                        long candidate = current + (long) (position[c] - position[b]) * rate;
                        if (candidate < dp[b + 1][c][newUsed]) {
                            dp[b + 1][c][newUsed] = candidate;
                        }
                    }
                }
            }
        }

        // the last sign must survive, with exactly k merges done
        long best = INF;
        for (int prevPlusOne = 0; prevPlusOne < n; prevPlusOne++) {
            best = Math.min(best, dp[prevPlusOne][n - 1][k]);
        }
        return (int) best;
    }
}
