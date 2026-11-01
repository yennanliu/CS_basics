package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-element-after-replacement-with-digit-sum/description/

/**
 * 3300. Minimum Element After Replacement With Digit Sum
 * Easy
 *
 * You are given an integer array nums.
 *
 * You replace each element in nums with the sum of its digits.
 *
 * Return the minimum element in nums after all replacements.
 *
 *
 * Example 1:
 *
 * Input: nums = [10,12,13,14]
 * Output: 1
 * Explanation:
 * nums becomes [1, 3, 4, 5] after all replacements, with minimum element 1.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4]
 * Output: 1
 *
 * Example 3:
 *
 * Input: nums = [999,19,199]
 * Output: 10
 * Explanation:
 * nums becomes [27, 10, 19] after all replacements, with minimum element 10.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 10^4
 *
 */
public class MinimumElementAfterReplacementWithDigitSum {

    // V0
    // IDEA: DIGIT SUM BY % 10 AND / 10, KEEP THE RUNNING MINIMUM
    /**
     * time = O(N * D)
     * space = O(1)
     */
    public int minElement(int[] nums) {
        int minimum = Integer.MAX_VALUE;
        for (int value : nums) {
            int digitSum = 0;
            while (value > 0) {
                digitSum += value % 10;
                value /= 10;
            }
            minimum = Math.min(minimum, digitSum);
        }
        return minimum;
    }
}
