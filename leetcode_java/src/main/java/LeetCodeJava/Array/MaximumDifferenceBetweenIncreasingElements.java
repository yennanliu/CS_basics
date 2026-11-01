package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-difference-between-increasing-elements/description/

/**
 * 2016. Maximum Difference Between Increasing Elements
 * Easy
 *
 * Given a 0-indexed integer array nums of size n, find the maximum difference between nums[i] and nums[j] (i.e., nums[j] - nums[i]), such that 0 <= i < j < n and nums[i] < nums[j].
 *
 * Return the maximum difference. If no such i and j exists, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [7,1,5,4]
 * Output: 4
 * Explanation:
 * The maximum difference occurs with i = 1 and j = 2, nums[j] - nums[i] = 5 - 1 = 4.
 * Note that with i = 1 and j = 3, the difference nums[j] - nums[i] = 4 - 1 = 3 is also valid, but 4 is a bigger difference.
 *
 * Example 2:
 *
 * Input: nums = [9,4,3,2]
 * Output: -1
 * Explanation:
 * There is no i and j such that i < j and nums[i] < nums[j].
 *
 * Example 3:
 *
 * Input: nums = [1,5,2,10]
 * Output: 9
 * Explanation:
 * The maximum difference occurs with i = 0 and j = 3, nums[j] - nums[i] = 10 - 1 = 9.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 2 <= n <= 1000
 * 1 <= nums[i] <= 10^9
 *
 */
public class MaximumDifferenceBetweenIncreasingElements {

    // V0
    // IDEA: RUNNING MIN (same shape as LC 121 Best Time to Buy and Sell Stock)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maximumDifference(int[] nums) {
        int best = -1;
        // smallest value seen so far = the best partner nums[i] for every later j
        int minSoFar = nums[0];
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] > minSoFar) {
                // strictly larger -> a valid pair (i < j, nums[i] < nums[j])
                best = Math.max(best, nums[j] - minSoFar);
            } else {
                // not larger -> it becomes the new (or equal) minimum
                minSoFar = nums[j];
            }
        }
        return best;
    }
}
