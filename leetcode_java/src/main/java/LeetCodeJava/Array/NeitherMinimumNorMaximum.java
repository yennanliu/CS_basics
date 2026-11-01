package LeetCodeJava.Array;

// https://leetcode.com/problems/neither-minimum-nor-maximum/description/

/**
 * 2733. Neither Minimum nor Maximum
 * Easy
 *
 * Given an integer array nums containing distinct positive integers, find and return any number from the array that is neither the minimum nor the maximum value in the array, or -1 if there is no such number.
 *
 * Return the selected integer.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,1,4]
 * Output: 2
 * Explanation: In this example, the minimum value is 1 and the maximum value is 4. Therefore, either 2 or 3 can be valid answers.
 *
 * Example 2:
 *
 * Input: nums = [1,2]
 * Output: -1
 * Explanation: Since there is no number in nums that is neither the maximum nor the minimum, we cannot select a number that satisfies the given condition. Therefore, there is no answer.
 *
 * Example 3:
 *
 * Input: nums = [2,1,3]
 * Output: 2
 * Explanation: Since 2 is neither the maximum nor the minimum value in nums, it is the only valid answer.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 * All values in nums are distinct
 *
 */
public class NeitherMinimumNorMaximum {

    // V0
    // IDEA: MIN / MAX THEN FIRST SURVIVOR
    //       values are distinct, so exactly one element is the min and one the max; any
    //       other element is a valid answer. with <= 2 elements there is none -> -1.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int findNonMinOrMax(int[] nums) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int value : nums) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        for (int value : nums) {
            if (value != min && value != max) {
                return value;
            }
        }
        return -1;
    }
}
