package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-operations-to-make-the-array-increasing/description/

/**
 * 1827. Minimum Operations to Make the Array Increasing
 * Easy
 *
 * You are given an integer array nums (0-indexed). In one operation, you can
 * choose an element of the array and increment it by 1.
 *
 * For example, if nums = [1,2,3], you can choose to increment nums[1] to make
 * nums = [1,3,3].
 *
 * Return the minimum number of operations needed to make nums strictly
 * increasing.
 *
 * An array nums is strictly increasing if nums[i] < nums[i+1] for all
 * 0 <= i < nums.length - 1. An array of length 1 is trivially strictly increasing.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1]
 * Output: 3
 * Explanation: You can do the following operations:
 * 1) Increment nums[2], so nums becomes [1,1,2].
 * 2) Increment nums[1], so nums becomes [1,2,2].
 * 3) Increment nums[2], so nums becomes [1,2,3].
 *
 * Example 2:
 *
 * Input: nums = [1,5,2,4,1]
 * Output: 14
 *
 * Example 3:
 *
 * Input: nums = [8]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5000
 * 1 <= nums[i] <= 10^4
 *
 */
public class MinimumOperationsToMakeTheArrayIncreasing {

    // V0
    // IDEA: GREEDY LEFT -> RIGHT (raise each element to exactly max(nums[i], previous + 1))
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations(int[] nums) {
        int operations = 0;
        // value of the previous element AFTER it was fixed (not its original value)
        int previous = 0;

        for (int value : nums) {
            int required = previous + 1;
            if (value < required) {
                // raise it only as far as needed; any higher makes later elements costlier
                operations += required - value;
                previous = required;
            } else {
                previous = value;
            }
        }
        return operations;
    }
}
