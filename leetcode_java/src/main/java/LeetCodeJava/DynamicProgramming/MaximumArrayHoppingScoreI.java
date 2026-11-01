package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-array-hopping-score-i/description/
// https://leetcode.ca/2024-07-14-3205-Maximum-Array-Hopping-Score-I/

/**
 * 3205. Maximum Array Hopping Score I
 * Medium
 *
 * Given an array nums, you have to get the maximum score starting from index 0 and hopping until you reach the last element of the array.
 *
 * In each hop, you can jump from index i to an index j > i, and you get a score of (j - i) * nums[j].
 *
 * Return the maximum score you can get.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,5,8]
 * Output: 16
 * Explanation:
 * There are two possible ways to reach the last element:
 * 0 -> 1 -> 2 with a score of (1 - 0) * 5 + (2 - 1) * 8 = 13.
 * 0 -> 2 with a score of (2 - 0) * 8 = 16.
 *
 * Example 2:
 *
 * Input: nums = [4,5,2,8,9,1,3]
 * Output: 42
 * Explanation:
 * We can do the hopping 0 -> 4 -> 6 with a score of (4 - 0) * 9 + (6 - 4) * 3 = 42.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^3
 * 1 <= nums[i] <= 10^5
 *
 */
public class MaximumArrayHoppingScoreI {

    // V0
    // IDEA: O(N^2) DP OVER THE LANDING INDEX
    //   dp[i] = best score when standing on index i
    //         = max over j < i of dp[j] + (i - j) * nums[i]
    //   The walk must end on the last element, so the answer is dp[n - 1].
    //   (n <= 1000 here; the sequel LC 3221 needs a suffix-maximum greedy instead.)
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int maxScore(int[] nums) {
        int n = nums.length;
        long[] dp = new long[n];
        for (int i = 1; i < n; i++) {
            long best = Long.MIN_VALUE;
            for (int j = 0; j < i; j++) {
                long candidate = dp[j] + (long) (i - j) * nums[i];
                best = Math.max(best, candidate);
            }
            dp[i] = best;
        }
        return (int) dp[n - 1];
    }
}
