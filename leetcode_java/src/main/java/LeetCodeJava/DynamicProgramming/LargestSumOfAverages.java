package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/largest-sum-of-averages/

/**
 *  813. Largest Sum of Averages
 *  Medium
 *
 *  You are given an integer array nums and an integer k. You can partition the
 *  array into at most k non-empty adjacent subarrays. The score of a partition
 *  is the sum of the averages of each subarray.
 *
 *  Note that the partition must use every integer in nums, and that the score
 *  is not necessarily an integer.
 *
 *  Return the maximum score you can achieve of all the possible partitions.
 *  Answers within 10^-6 of the actual answer will be accepted.
 *
 *  Example 1:
 *    Input: nums = [9,1,2,3,9], k = 3
 *    Output: 20.00000
 *    Explanation: The best choice is to partition nums into [9], [1, 2, 3], [9].
 *                 The answer is 9 + (1 + 2 + 3) / 3 + 9 = 20.
 *
 *  Example 2:
 *    Input: nums = [1,2,3,4,5,6,7], k = 4
 *    Output: 20.50000
 *
 *  Constraints:
 *    - 1 <= nums.length <= 100
 *    - 1 <= nums[i] <= 10^4
 *    - 1 <= k <= nums.length
 */
public class LargestSumOfAverages {

    // V0
    // IDEA: DP over (#groups, prefix length) with prefix sums for O(1) average lookup
    /**
     * time = O(K * N^2)
     * space = O(N)
     */
    public double largestSumOfAverages(int[] nums, int k) {
        int n = nums.length;
        double[] prefix = new double[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        // dp[i] = best score for nums[0..i) using the current number of groups
        double[] dp = new double[n + 1];
        for (int i = 1; i <= n; i++) {
            dp[i] = average(prefix, 0, i);   // 1 group
        }

        for (int g = 2; g <= k; g++) {
            double[] next = new double[n + 1];
            for (int i = g; i <= n; i++) {
                double best = dp[i];         // fewer groups is always allowed ("at most k")
                for (int m = g - 1; m < i; m++) {
                    best = Math.max(best, dp[m] + average(prefix, m, i));
                }
                next[i] = best;
            }
            dp = next;
        }
        return dp[n];
    }

    private double average(double[] prefix, int from, int to) {
        return (prefix[to] - prefix[from]) / (to - from);
    }
}
