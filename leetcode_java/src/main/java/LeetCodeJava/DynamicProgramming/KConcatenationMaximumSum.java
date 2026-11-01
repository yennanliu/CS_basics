package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/k-concatenation-maximum-sum/description/

/**
 * 1191. K-Concatenation Maximum Sum
 * Medium
 *
 * Given an integer array arr and an integer k, modify the array by repeating it
 * k times.
 *
 * For example, if arr = [1, 2] and k = 3 then the modified array will be
 * [1, 2, 1, 2, 1, 2].
 *
 * Return the maximum sub-array sum in the modified array. Note that the length of
 * the sub-array can be 0 and its sum in that case is 0.
 *
 * As the answer can be very large, return the answer modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2], k = 3
 * Output: 9
 *
 * Example 2:
 *
 * Input: arr = [1,-2,1], k = 5
 * Output: 2
 *
 * Example 3:
 *
 * Input: arr = [-1,-2], k = 7
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 10^5
 * 1 <= k <= 10^5
 * -10^4 <= arr[i] <= 10^4
 *
 */
public class KConcatenationMaximumSum {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: PREFIX SUM (KADANE ON ONE COPY) + CASE ANALYSIS FOR THE K COPIES
    /**
     * total        = sum of one copy
     * maxPrefix    = best "head" of a copy  (max prefix sum)
     * minPrefix    = min prefix sum
     * maxSuffix    = best "tail" of a copy  = total - minPrefix
     * maxInside    = best subarray within ONE copy (Kadane via prefix sums)
     *
     * k == 1               -> maxInside
     * k >= 2               -> also maxSuffix + maxPrefix            (spans 2 copies)
     * k >= 2 and total > 0 -> also (k-2) * total + maxSuffix + maxPrefix
     *                         (every middle copy adds a positive total)
     *
     * the empty subarray is allowed, so every quantity starts at 0
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public int kConcatenationMaxSum(int[] arr, int k) {
        // long: (k - 2) * total reaches 10^5 * 10^9
        long prefix = 0;
        long maxPrefix = 0;
        long minPrefix = 0;
        long maxInside = 0;

        for (int x : arr) {
            prefix += x;
            maxPrefix = Math.max(maxPrefix, prefix);
            minPrefix = Math.min(minPrefix, prefix);
            // best subarray ending here = prefix minus the smallest earlier prefix
            maxInside = Math.max(maxInside, prefix - minPrefix);
        }
        long total = prefix;

        long best = maxInside;
        if (k >= 2) {
            long maxSuffix = total - minPrefix;
            best = Math.max(best, maxSuffix + maxPrefix);
            if (total > 0) {
                best = Math.max(best, (long) (k - 2) * total + maxSuffix + maxPrefix);
            }
        }
        // take the modulo only at the very end: comparing modded values breaks max()
        return (int) (best % MOD);
    }
}
