package LeetCodeJava.Array;

// https://leetcode.com/problems/remove-one-element-to-make-the-array-strictly-increasing/description/
/**
 * 1909. Remove One Element to Make the Array Strictly Increasing
 * Easy
 *
 * Given a 0-indexed integer array nums, return true if it can be made strictly increasing after
 * removing exactly one element, or false otherwise. If the array is already strictly increasing,
 * return true.
 *
 * The array nums is strictly increasing if nums[i - 1] < nums[i] for each index
 * (1 <= i < nums.length).
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,10,5,7]
 * Output: true
 * Explanation: By removing 10 at index 2 from nums, it becomes [1,2,5,7].
 * [1,2,5,7] is strictly increasing, so return true.
 *
 * Example 2:
 *
 * Input: nums = [2,3,1,2]
 * Output: false
 *
 * Example 3:
 *
 * Input: nums = [1,1,1]
 * Output: false
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 1000
 * 1 <= nums[i] <= 1000
 *
 */
public class RemoveOneElementToMakeTheArrayStrictlyIncreasing {

    // V0
    // IDEA: GREEDY - ONLY THE FIRST BAD PAIR MATTERS
    // find the first i with nums[i] >= nums[i + 1]. The removed element must be i or i + 1 (removing
    // anything else leaves that pair adjacent), so test both. An already-increasing array stops at
    // the last index, and removing it keeps the array increasing.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean canBeIncreasing(int[] nums) {
        int i = 0;
        while (i + 1 < nums.length && nums[i] < nums[i + 1]) {
            i++;
        }
        return isIncreasingWithout(nums, i) || isIncreasingWithout(nums, i + 1);
    }

    /** Is nums strictly increasing when index skip is ignored? */
    private boolean isIncreasingWithout(int[] nums, int skip) {
        boolean hasPrevious = false;
        int previous = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i == skip) {
                continue;
            }
            if (hasPrevious && previous >= nums[i]) {
                return false;
            }
            previous = nums[i];
            hasPrevious = true;
        }
        return true;
    }
}
