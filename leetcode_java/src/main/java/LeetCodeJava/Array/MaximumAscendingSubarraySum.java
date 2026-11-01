package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-ascending-subarray-sum/description/
/**
 * 1800. Maximum Ascending Subarray Sum
 * Easy
 *
 * Given an array of positive integers nums, return the maximum possible sum of an strictly
 * increasing subarray in nums.
 *
 * A subarray is defined as a contiguous sequence of numbers in an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [10,20,30,5,10,50]
 * Output: 65
 * Explanation: [5,10,50] is the ascending subarray with the maximum sum of 65.
 *
 * Example 2:
 *
 * Input: nums = [10,20,30,40,50]
 * Output: 150
 * Explanation: [10,20,30,40,50] is the ascending subarray with the maximum sum of 150.
 *
 * Example 3:
 *
 * Input: nums = [12,17,15,13,10,11,12]
 * Output: 33
 * Explanation: [10,11,12] is the ascending subarray with the maximum sum of 33.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 *
 */
public class MaximumAscendingSubarraySum {

    // V0
    // IDEA: RUNNING SUM OF THE CURRENT ASCENDING RUN
    // the run ending at i continues when nums[i] > nums[i - 1], otherwise a new run starts at nums[i].
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxAscendingSum(int[] nums) {
        int best = 0;
        int runSum = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] > nums[i - 1]) {
                runSum += nums[i];
            } else {
                runSum = nums[i];
            }
            best = Math.max(best, runSum);
        }
        return best;
    }
}
