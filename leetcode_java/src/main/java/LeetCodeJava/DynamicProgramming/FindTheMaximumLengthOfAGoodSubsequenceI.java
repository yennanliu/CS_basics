package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-maximum-length-of-a-good-subsequence-i/description/

import java.util.*;

/**
 * 3176. Find the Maximum Length of a Good Subsequence I
 * Medium
 *
 * You are given an integer array nums and a non-negative integer k. A sequence of integers seq is called good if there are at most k indices i in the range [0, seq.length - 2] such that seq[i] != seq[i + 1].
 *
 * Return the maximum possible length of a good subsequence of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,1,1,3], k = 2
 * Output: 4
 * Explanation:
 * The maximum length subsequence is [1,2,1,1,3].
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5,1], k = 0
 * Output: 2
 * Explanation:
 * The maximum length subsequence is [1,2,3,4,5,1].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 500
 * 1 <= nums[i] <= 10^9
 * 0 <= k <= min(nums.length, 25)
 *
 */
public class FindTheMaximumLengthOfAGoodSubsequenceI {

    // V0
    // IDEA: DP OVER (LAST INDEX TAKEN, CHANGES SPENT)
    //   dp[i][j] = longest good subsequence ending at index i that uses j value changes.
    //   Extending from an earlier index t:
    //     nums[t] == nums[i] -> dp[i][j] = max(dp[i][j], dp[t][j] + 1)     (no new change)
    //     nums[t] != nums[i] -> dp[i][j] = max(dp[i][j], dp[t][j - 1] + 1) (one more change)
    //   Base: dp[i][j] = 1 (the subsequence [nums[i]] alone).
    /**
     * time = O(N^2 * K)
     * space = O(N * K)
     */
    public int maximumLength(int[] nums, int k) {
        int n = nums.length;
        int[][] dp = new int[n][k + 1];
        int best = 1;
        for (int i = 0; i < n; i++) {
            Arrays.fill(dp[i], 1);
            for (int t = 0; t < i; t++) {
                if (nums[t] == nums[i]) {
                    for (int j = 0; j <= k; j++) {
                        dp[i][j] = Math.max(dp[i][j], dp[t][j] + 1);
                    }
                } else {
                    for (int j = 1; j <= k; j++) {
                        dp[i][j] = Math.max(dp[i][j], dp[t][j - 1] + 1);
                    }
                }
            }
            for (int j = 0; j <= k; j++) {
                best = Math.max(best, dp[i][j]);
            }
        }
        return best;
    }
}
