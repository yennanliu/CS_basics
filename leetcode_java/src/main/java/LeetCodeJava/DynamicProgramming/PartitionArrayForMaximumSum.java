package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/partition-array-for-maximum-sum/description/

/**
 * 1043. Partition Array for Maximum Sum
 * Medium
 *
 * Given an integer array arr, partition the array into (contiguous) subarrays of
 * length at most k. After partitioning, each subarray has their values changed to
 * become the maximum value of that subarray.
 *
 * Return the largest sum of the given array after partitioning.
 * Test cases are generated so that the answer fits in a 32-bit integer.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,15,7,9,2,5,10], k = 3
 * Output: 84
 * Explanation: arr becomes [15,15,15,9,10,10,10]
 *
 * Example 2:
 *
 * Input: arr = [1,4,1,5,7,3,6,1,9,9,3], k = 4
 * Output: 83
 *
 * Example 3:
 *
 * Input: arr = [1], k = 1
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 500
 * 0 <= arr[i] <= 10^9
 * 1 <= k <= arr.length
 *
 */
public class PartitionArrayForMaximumSum {

    // V0
    // IDEA: 1D DP OVER PREFIXES, TRY EVERY LENGTH OF THE LAST SUBARRAY
    /**
     * dp[i] = largest sum obtainable from the first i elements (arr[0..i-1])
     *
     * the last subarray ends at i-1 and has length L (1 <= L <= k):
     *   dp[i] = max over L of ( dp[i-L] + max(arr[i-L .. i-1]) * L )
     * a running max is kept while L grows, so the window is never re-scanned.
     *
     * base: dp[0] = 0
     */
    /**
     * time = O(N * K)
     * space = O(N)
     */
    public int maxSumAfterPartitioning(int[] arr, int k) {
        int n = arr.length;
        // long: a candidate (not the final answer) can pass Integer.MAX_VALUE
        long[] dp = new long[n + 1];

        for (int i = 1; i <= n; i++) {
            long windowMax = 0;
            // length = size of the last subarray, which ends at index i-1
            for (int length = 1; length <= Math.min(k, i); length++) {
                windowMax = Math.max(windowMax, arr[i - length]);
                dp[i] = Math.max(dp[i], dp[i - length] + windowMax * length);
            }
        }
        return (int) dp[n];
    }
}
