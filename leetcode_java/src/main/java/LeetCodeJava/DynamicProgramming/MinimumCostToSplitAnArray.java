package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-cost-to-split-an-array/description/

/**
 * 2547. Minimum Cost to Split an Array
 * Hard
 *
 * You are given an integer array nums and an integer k.
 *
 * Split the array into some number of non-empty subarrays. The cost of a split is the sum of the
 * importance value of each subarray in the split.
 *
 * Let trimmed(subarray) be the version of the subarray where all numbers which appear only once
 * are removed.
 *
 * For example, trimmed([3,1,2,4,3,4]) = [3,4,3,4].
 *
 * The importance value of a subarray is k + trimmed(subarray).length.
 *
 * For example, if a subarray is [1,2,3,3,3,4,4], then trimmed([1,2,3,3,3,4,4]) = [3,3,3,4,4].
 * The importance value of this subarray will be k + 5.
 *
 * Return the minimum possible cost of a split of nums.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,1,2,1,3,3], k = 2
 * Output: 8
 * Explanation: We split nums to have two subarrays: [1,2], [1,2,1,3,3].
 * The importance value of [1,2] is 2 + (0) = 2.
 * The importance value of [1,2,1,3,3] is 2 + (2 + 2) = 6.
 * The cost of the split is 2 + 6 = 8.
 *
 * Example 2:
 *
 * Input: nums = [1,2,1,2,1], k = 2
 * Output: 6
 * Explanation: We split nums to have two subarrays: [1,2], [1,2,1].
 * The importance value of [1,2] is 2 + (0) = 2.
 * The importance value of [1,2,1] is 2 + (2) = 4.
 * The cost of the split is 2 + 4 = 6.
 *
 * Example 3:
 *
 * Input: nums = [1,2,1,2,1], k = 5
 * Output: 10
 * Explanation: We split nums to have one subarray: [1,2,1,2,1].
 * The importance value of [1,2,1,2,1] is 5 + (3 + 2) = 10.
 * The cost of the split is 10.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] < nums.length
 * 1 <= k <= 10^9
 *
 */
public class MinimumCostToSplitAnArray {

    // V0
    // IDEA: SUFFIX DP + INCREMENTAL "APPEARS ONCE" COUNTER - dp[i] = min over j (k + trimmedLen(i..j) + dp[j+1])
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int minCost(int[] nums, int k) {
        int n = nums.length;

        // dp[i] = min cost of splitting the suffix nums[i..n-1]; dp[n] = 0
        long[] dp = new long[n + 1];

        for (int i = n - 1; i >= 0; i--) {
            // values are in [0, n), so a plain array counts them
            int[] count = new int[n];
            int distinctOnce = 0;   // distinct values appearing exactly once in nums[i..j]
            long best = Long.MAX_VALUE;

            // first piece is nums[i..j]; grow j one element at a time
            for (int j = i; j < n; j++) {
                int value = nums[j];
                count[value]++;
                if (count[value] == 1) {
                    distinctOnce++;          // a new singleton
                } else if (count[value] == 2) {
                    distinctOnce--;          // no longer a singleton; counts >= 3 change nothing
                }

                int pieceLength = j - i + 1;
                long trimmedLength = pieceLength - distinctOnce;
                long cost = k + trimmedLength + dp[j + 1];
                best = Math.min(best, cost);
            }
            dp[i] = best;
        }
        return (int) dp[0];
    }
}
