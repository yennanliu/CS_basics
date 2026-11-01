package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-total-space-wasted-with-k-resizing-operations/description/

import java.util.Arrays;

/**
 * 1959. Minimum Total Space Wasted With K Resizing Operations
 * Medium
 *
 * You are currently designing a dynamic array. You are given a 0-indexed integer array nums,
 * where nums[i] is the number of elements that will be in the array at time i. In addition,
 * you are given an integer k, the maximum number of times you can resize the array (to any size).
 *
 * The size of the array at time t, size_t, must be at least nums[t] because there needs to be
 * enough space in the array to hold all the elements. The space wasted at time t is defined as
 * size_t - nums[t], and the total space wasted is the sum of the space wasted across every
 * time t where 0 <= t < nums.length.
 *
 * Return the minimum total space wasted if you can resize the array at most k times.
 *
 * Note: The array can have any size at the start and does not count towards the number of
 * resizing operations.
 *
 *
 * Example 1:
 *
 * Input: nums = [10,20], k = 0
 * Output: 10
 * Explanation: size = [20,20].
 *
 * Example 2:
 *
 * Input: nums = [10,20,30], k = 1
 * Output: 10
 * Explanation: size = [20,20,30].
 *
 * Example 3:
 *
 * Input: nums = [10,20,15,30,20], k = 2
 * Output: 15
 * Explanation: size = [10,20,20,30,30].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 200
 * 1 <= nums[i] <= 10^6
 * 0 <= k <= nums.length - 1
 *
 */
public class MinimumTotalSpaceWastedWithKResizingOperations {

    // V0
    // IDEA: PARTITION DP (K RESIZES == CUT NUMS INTO K + 1 SEGMENTS)
    /**
     *  Within one segment the size never changes, so the best size is the segment MAX:
     *     waste[i][j] = max(nums[i..j]) * (j - i + 1) - sum(nums[i..j])
     *
     *  DP def:
     *     - dp[i][s] = min waste when the first i elements are split into s segments
     *
     *  DP eq:
     *     - dp[i][s] = min over h < i of dp[h][s - 1] + waste[h][i - 1]
     *
     *  Init: dp[0][0] = 0, everything else "unreachable".
     *  Answer: dp[n][k + 1]  (k is the number of RESIZES, so there are k + 1 segments)
     *
     *  time = O(N^2 * K)
     *  space = O(N^2 + N * K)
     */
    public int minSpaceWastedKResizing(int[] nums, int k) {
        int n = nums.length;
        int segments = k + 1;

        // waste[i][j]: wasted space if nums[i..j] is served by one size (its max)
        long[][] waste = new long[n][n];
        for (int i = 0; i < n; i++) {
            long sum = 0;
            long max = 0;
            for (int j = i; j < n; j++) {
                sum += nums[j];
                max = Math.max(max, nums[j]);
                waste[i][j] = max * (j - i + 1) - sum;
            }
        }

        final long UNREACHABLE = Long.MAX_VALUE;
        long[][] dp = new long[n + 1][segments + 1];
        for (long[] row : dp) {
            Arrays.fill(row, UNREACHABLE);
        }
        dp[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            // cannot make more segments than elements
            int maxSegments = Math.min(segments, i);
            for (int s = 1; s <= maxSegments; s++) {
                // the last segment is nums[h .. i-1]
                for (int h = 0; h < i; h++) {
                    if (dp[h][s - 1] == UNREACHABLE) {
                        continue;
                    }
                    long candidate = dp[h][s - 1] + waste[h][i - 1];
                    if (candidate < dp[i][s]) {
                        dp[i][s] = candidate;
                    }
                }
            }
        }

        // "at most k resizes": using fewer segments is never better than k + 1
        // (an extra cut never adds waste), but take the min to be safe
        long best = UNREACHABLE;
        for (int s = 1; s <= segments; s++) {
            best = Math.min(best, dp[n][s]);
        }
        return (int) best;
    }
}
