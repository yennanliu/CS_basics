package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/description/
/**
 * 689. Maximum Sum of 3 Non-Overlapping Subarrays
 * Hard
 *
 * Given an integer array nums and an integer k, find three non-overlapping subarrays of
 * length k with maximum sum and return them.
 *
 * Return the result as a list of indices representing the starting position of each
 * interval (0-indexed). If there are multiple answers, return the lexicographically
 * smallest one.
 *
 * Example 1:
 *
 * Input: nums = [1,2,1,2,6,7,5,1], k = 2
 * Output: [0,3,5]
 * Explanation: Subarrays [1, 2], [2, 6], [7, 5] correspond to the starting indices
 * [0, 3, 5].
 * We could have also taken [2, 1], but an answer of [1, 3, 5] would be lexicographically
 * larger.
 *
 * Example 2:
 *
 * Input: nums = [1,2,1,2,1,2,1,2,1], k = 2
 * Output: [0,2,4]
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 10^4
 * 1 <= nums[i] < 2^16
 * 1 <= k <= floor(nums.length / 3)
 *
 */
public class MaximumSumOf3NonOverlappingSubarrays {

    // V0
    // IDEA: PREFIX SUM + BEST-ON-THE-LEFT / BEST-ON-THE-RIGHT ARRAYS
    /**
     *   Step 1: COLLAPSE each length-k window into ONE number
     *             w[i] = sum(nums[i, i+k))
     *
     *   Step 2: FIX the MIDDLE window at index j. The other two are then INDEPENDENT:
     *             - best window entirely to the LEFT  -> any start in [0, j-k]
     *             - best window entirely to the RIGHT -> any start in [j+k, m-1]
     *           Precompute those with two sweeps:
     *             left[i]  = argmax of w over [0, i]
     *             right[i] = argmax of w over [i, m-1]
     *
     *   Step 3: scan j and keep the best triple.
     *
     *   NOTE !!! LEXICOGRAPHIC tie-breaking -- all three comparisons must prefer the
     *            SMALLER index on a tie:
     *     - left  sweeps FORWARD  with strict `>`  -> keeps the earliest maximum
     *     - right sweeps BACKWARD with `>=`        -> keeps the earliest maximum
     *     - the j loop runs ASCENDING with strict `>` -> keeps the earliest triple
     *
     *   time  = O(n)
     *   space = O(n)
     */
    public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
        int n = nums.length;

        // prefix[i] = sum of nums[0, i)
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        // w[i] = sum of the length-k window starting at i
        int m = n - k + 1;
        long[] w = new long[m];
        for (int i = 0; i < m; i++) {
            w[i] = prefix[i + k] - prefix[i];
        }

        // left[i] = index of the best window within w[0..i] (EARLIEST on tie)
        int[] left = new int[m];
        int best = 0;
        for (int i = 0; i < m; i++) {
            if (w[i] > w[best]) {
                best = i;
            }
            left[i] = best;
        }

        // right[i] = index of the best window within w[i..m-1] (EARLIEST on tie)
        int[] right = new int[m];
        best = m - 1;
        for (int i = m - 1; i >= 0; i--) {
            if (w[i] >= w[best]) {
                best = i;
            }
            right[i] = best;
        }

        int[] res = new int[0];
        long bestTotal = -1;
        for (int j = k; j < m - k; j++) {
            int i = left[j - k];
            int l = right[j + k];
            long total = w[i] + w[j] + w[l];
            if (total > bestTotal) {
                bestTotal = total;
                res = new int[] { i, j, l };
            }
        }

        return res;
    }


    // V1
    // IDEA: GENERAL DP over (how many windows placed, position)
    /**
     *  dp[c][i] = best total using c windows from suffix i, with the choice
     *  recorded so the indices can be recovered.
     *
     *  Generalises to ANY number of windows (the problem's `3` becomes a
     *  parameter), which the fixed left/right arrays of V0 cannot do.
     *
     *  time  = O(n * c)
     *  space = O(n * c)
     */
    public int[] maxSumOfThreeSubarrays_1(int[] nums, int k) {
        final int C = 3;
        int n = nums.length;

        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        long[][] dp = new long[C + 1][n + 2];
        int[][] pick = new int[C + 1][n + 2];

        for (int c = 1; c <= C; c++) {
            for (int i = n - k; i >= 0; i--) {
                long take = prefix[i + k] - prefix[i] + dp[c - 1][i + k];
                long skip = dp[c][i + 1];
                if (take >= skip) {
                    dp[c][i] = take;
                    pick[c][i] = 1;
                } else {
                    dp[c][i] = skip;
                    pick[c][i] = 0;
                }
            }
        }

        int[] res = new int[C];
        int idx = 0;
        int i = 0;
        int c = C;
        while (c > 0 && i <= n - k) {
            if (pick[c][i] == 1) {
                res[idx++] = i;
                i += k;
                c -= 1;
            } else {
                i += 1;
            }
        }
        return res;
    }

    // V2
    // IDEA: BRUTE FORCE over the three window starts
    /**
     *  Try every (i, j, l) triple with the required gaps.
     *
     *  O(n^3), dead at n = 2 * 10^4, but it enumerates exactly what the statement
     *  asks for -- including the lexicographic tie-break, which falls out of the
     *  ascending loop order for free.
     *
     *  time  = O(n^3)
     *  space = O(n)
     */
    public int[] maxSumOfThreeSubarrays_2(int[] nums, int k) {
        int n = nums.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        int[] res = new int[3];
        long best = -1;
        for (int i = 0; i + k <= n; i++) {
            for (int j = i + k; j + k <= n; j++) {
                for (int l = j + k; l + k <= n; l++) {
                    long total = (prefix[i + k] - prefix[i])
                            + (prefix[j + k] - prefix[j])
                            + (prefix[l + k] - prefix[l]);
                    if (total > best) {
                        best = total;
                        res = new int[] { i, j, l };
                    }
                }
            }
        }
        return res;
    }

    // V3
    // IDEA: FIX THE MIDDLE WINDOW, sweep the best left with a running maximum
    /**
     *  V0 precomputes two argmax arrays. Here only the LEFT best is maintained
     *  incrementally as j advances, and the RIGHT best is precomputed once -- so
     *  one of the two sweeps disappears.
     *
     *  Same O(n), one array less.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int[] maxSumOfThreeSubarrays_3(int[] nums, int k) {
        int n = nums.length;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        int m = n - k + 1;
        long[] w = new long[m];
        for (int i = 0; i < m; i++) {
            w[i] = prefix[i + k] - prefix[i];
        }

        int[] right = new int[m];
        int bestRight = m - 1;
        for (int i = m - 1; i >= 0; i--) {
            if (w[i] >= w[bestRight]) {
                bestRight = i;
            }
            right[i] = bestRight;
        }

        int[] res = new int[0];
        long best = -1;
        int bestLeft = 0;
        for (int j = k; j + k < m + k - 1 + 1 && j < m - k; j++) {
            // extend the left candidate window as j moves right
            if (w[j - k] > w[bestLeft]) {
                bestLeft = j - k;
            }
            int l = right[j + k];
            long total = w[bestLeft] + w[j] + w[l];
            if (total > best) {
                best = total;
                res = new int[] { bestLeft, j, l };
            }
        }
        return res;
    }

}
