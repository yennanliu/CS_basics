package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-number-of-jumps-to-reach-the-last-index/description/

import java.util.*;

/**
 * 2770. Maximum Number of Jumps to Reach the Last Index
 * Medium
 *
 * You are given a 0-indexed array nums of n integers and an integer target.
 *
 * You are initially positioned at index 0. In one step, you can jump from index i to any index j
 * such that:
 *
 * 0 <= i < j < n
 * -target <= nums[j] - nums[i] <= target
 *
 * Return the maximum number of jumps you can make to reach index n - 1.
 *
 * If there is no way to reach index n - 1, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,6,4,1,2], target = 2
 * Output: 3
 * Explanation: Jump 0 -> 1 -> 3 -> 5.
 *
 * Example 2:
 *
 * Input: nums = [1,3,6,4,1,2], target = 3
 * Output: 5
 * Explanation: Jump 0 -> 1 -> 2 -> 3 -> 4 -> 5.
 *
 * Example 3:
 *
 * Input: nums = [1,3,6,4,1,2], target = 0
 * Output: -1
 * Explanation: It can be proven that there is no jumping sequence that goes from 0 to n - 1.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length == n <= 1000
 * -10^9 <= nums[i] <= 10^9
 * 0 <= target <= 2 * 10^9
 *
 */
public class MaximumNumberOfJumpsToReachTheLastIndex {

    private static final int UNREACHABLE = -1;

    // V0
    // IDEA: 1-D DP (LIS-LIKE) - dp[j] = max(dp[i] + 1) OVER REACHABLE i < j WITH |nums[j] - nums[i]| <= target
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int maximumJumps(int[] nums, int target) {
        int n = nums.length;

        // dp[j] = most jumps that land on index j, or UNREACHABLE
        int[] dp = new int[n];
        Arrays.fill(dp, UNREACHABLE);
        dp[0] = 0;

        for (int j = 1; j < n; j++) {
            for (int i = 0; i < j; i++) {
                if (dp[i] == UNREACHABLE) {
                    continue;
                }
                // differences reach 4 * 10^9, so compare in long
                long difference = Math.abs((long) nums[j] - nums[i]);
                if (difference <= target) {
                    dp[j] = Math.max(dp[j], dp[i] + 1);
                }
            }
        }
        return dp[n - 1];
    }
}
