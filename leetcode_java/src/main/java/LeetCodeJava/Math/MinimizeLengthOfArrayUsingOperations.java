package LeetCodeJava.Math;

// https://leetcode.com/problems/minimize-length-of-array-using-operations/description/

/**
 * 3012. Minimize Length of Array Using Operations
 * Medium
 *
 * You are given a 0-indexed integer array nums containing positive integers.
 *
 * Your task is to minimize the length of nums by performing the following operations any number of times (including zero):
 *
 * Select two distinct indices i and j from nums, such that nums[i] > 0 and nums[j] > 0.
 * Insert the result of nums[i] % nums[j] at the end of nums.
 * Delete the elements at indices i and j from nums.
 *
 * Return an integer denoting the minimum length of nums after performing the operation any number of times.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,4,3,1]
 * Output: 1
 *
 * Example 2:
 *
 * Input: nums = [5,5,5,10,5]
 * Output: 2
 *
 * Example 3:
 *
 * Input: nums = [2,3,4]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class MinimizeLengthOfArrayUsingOperations {

    // V0
    // IDEA: EVERYTHING TURNS ON THE MINIMUM m
    //       - some x not divisible by m -> x % m is a new, smaller minimum that wipes out the rest -> 1
    //       - m divides everything -> others die against m for free; the c copies of m pair off -> ceil(c / 2)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumArrayLength(int[] nums) {
        int minValue = Integer.MAX_VALUE;
        for (int num : nums) {
            minValue = Math.min(minValue, num);
        }

        int minCount = 0;
        for (int num : nums) {
            if (num % minValue != 0) {
                // a strictly smaller positive remainder can be created
                return 1;
            }
            if (num == minValue) {
                minCount++;
            }
        }
        // ceil(minCount / 2)
        return (minCount + 1) / 2;
    }
}
