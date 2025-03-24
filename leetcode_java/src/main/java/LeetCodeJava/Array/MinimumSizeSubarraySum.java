package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-size-subarray-sum/description/
/**
 * 209. Minimum Size Subarray Sum
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an array of positive integers nums and a positive integer target, return the minimal length of a subarray whose sum is greater than or equal to target. If there is no such subarray, return 0 instead.
 *
 *
 *
 * Example 1:
 *
 * Input: target = 7, nums = [2,3,1,2,4,3]
 * Output: 2
 * Explanation: The subarray [4,3] has the minimal length under the problem constraint.
 * Example 2:
 *
 * Input: target = 4, nums = [1,4,4]
 * Output: 1
 * Example 3:
 *
 * Input: target = 11, nums = [1,1,1,1,1,1,1,1]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= target <= 109
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 104
 *
 *
 * Follow up: If you have figured out the O(n) solution, try coding another solution of which the time complexity is O(n log(n)).
 *
 */
public class MinimumSizeSubarraySum {

    // V0
    // IDEA: SLIDING WINDOW (fix by gpt)
    public int minSubArrayLen(int target, int[] nums) {
        // Check for empty input array
        if (nums == null || nums.length == 0) {
            return 0; // No valid subarray exists
        }

        int res = Integer.MAX_VALUE; // Initialize result with maximum possible value
        int l = 0; // Left pointer of the sliding window
        int curSum = 0; // Current sum of the sliding window

        /**
         * NOTE !!!
         *
         *   we move RIGHT pointer first, then move LEFT pointer (sliding window)
         *   when the `condition` (curSum >= target) is met
         */
        for (int r = 0; r < nums.length; r++) {
            curSum += nums[r]; // Add current element to the window sum

            // Shrink the window from the left as long as the current sum is >= target
            /**
             * NOTE !!!  below condition
             *
             *   -> while (curSum >= target)
             */
            while (curSum >= target) {
                /**
                 * NOTE !!!
                 *
                 *   within EVERY while loop, we get res updated again
                 *   e.g. res = Math.min(res, r - l + 1);
                 *
                 */
                res = Math.min(res, r - l + 1); // Update result with the minimum subarray length
                curSum -= nums[l]; // Subtract the element at the left pointer
                l++; // Move the left pointer to the right
            }
        }

        // If no valid subarray is found, return 0
        return res == Integer.MAX_VALUE ? 0 : res;
    }

    // V1
    // https://leetcode.com/problems/minimum-size-subarray-sum/editorial/
    // IDEA: SLIDING WINDOW
    public int minSubArrayLen_1(int target, int[] nums) {
        int left = 0, right = 0, sumOfCurrentWindow = 0;
        int res = Integer.MAX_VALUE;

        for(right = 0; right < nums.length; right++) {
            sumOfCurrentWindow += nums[right];

            while (sumOfCurrentWindow >= target) {
                res = Math.min(res, right - left + 1);
                sumOfCurrentWindow -= nums[left++];
            }
        }

        return res == Integer.MAX_VALUE ? 0 : res;
    }

    // V2
}
