package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-increasing-quadruplets/description/

/**
 * 2552. Count Increasing Quadruplets
 * Hard
 *
 * Given a 0-indexed integer array nums of size n containing all numbers from 1 to n, return the
 * number of increasing quadruplets.
 *
 * A quadruplet (i, j, k, l) is increasing if:
 *
 * - 0 <= i < j < k < l < n, and
 * - nums[i] < nums[k] < nums[j] < nums[l].
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,2,4,5]
 * Output: 2
 * Explanation:
 * - When i = 0, j = 1, k = 2, and l = 3, nums[i] < nums[k] < nums[j] < nums[l].
 * - When i = 0, j = 1, k = 2, and l = 4, nums[i] < nums[k] < nums[j] < nums[l].
 * There are no other quadruplets, so we return 2.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4]
 * Output: 0
 * Explanation: There exists only one quadruplet with i = 0, j = 1, k = 2, l = 3, but since nums[j] < nums[k], we return 0.
 *
 *
 * Constraints:
 *
 * 4 <= nums.length <= 4000
 * 1 <= nums[i] <= nums.length
 * All the integers of nums are unique. nums is a permutation.
 *
 */
public class CountIncreasingQuadruplets {

    // V0
    // IDEA: DP OVER PAIRS - dp[j] = #"132" TRIPLES (i, j, k) WITH PIVOT j SEEN SO FAR; EACH NEW l > nums[j] COMPLETES THEM
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public long countQuadruplets(int[] nums) {
        int n = nums.length;

        // dp[j] = number of (i, k) with i < j < k (k already scanned) and nums[i] < nums[k] < nums[j]
        long[] dp = new long[n];
        long result = 0;

        // y is the newest index; it acts as either k or l for every earlier x
        for (int y = 0; y < n; y++) {
            int current = nums[y];

            // smallerBefore = #{ i < x : nums[i] < nums[y] }, kept up to date while x moves right
            long smallerBefore = 0;

            for (int x = 0; x < y; x++) {
                if (nums[x] < current) {
                    // (x, y) acts as (j, l): every triple stored at pivot x used some k < y
                    // and nums[k] < nums[x] < nums[y], so each one becomes a quadruplet
                    result += dp[x];
                    // x can serve as an "i" for a later j
                    smallerBefore++;
                } else {
                    // (x, y) acts as (j, k): any earlier i with nums[i] < nums[y] completes a triple
                    dp[x] += smallerBefore;
                }
            }
        }
        return result;
    }
}
