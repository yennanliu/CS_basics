package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-score-by-changing-two-elements/description/

import java.util.*;

/**
 * 2567. Minimum Score by Changing Two Elements
 * Medium
 *
 * You are given an integer array nums.
 *
 * The low score of nums is the minimum absolute difference between any two integers.
 * The high score of nums is the maximum absolute difference between any two integers.
 * The score of nums is the sum of the high and low scores.
 *
 * Return the minimum score after changing two elements of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,4,7,8,5]
 * Output: 3
 * Explanation:
 * Change nums[0] and nums[1] to be 6 so that nums becomes [6,6,7,8,5].
 * The low score is the minimum absolute difference: |6 - 6| = 0.
 * The high score is the maximum absolute difference: |8 - 5| = 3.
 * The sum of high and low score is 3.
 *
 * Example 2:
 *
 * Input: nums = [1,4,3]
 * Output: 0
 * Explanation:
 * Change nums[1] and nums[2] to 1 so that nums becomes [1,1,1].
 * The sum of maximum absolute difference and minimum absolute difference is 0.
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class MinimumScoreByChangingTwoElements {

    // V0
    // IDEA: SORT + GREEDY (the low score is always free)
    //       with two changes one value can always duplicate another, so low = 0.
    //       the high score is minimised by moving two elements off the ends of the sorted array:
    //       drop 2 smallest, 1 smallest + 1 largest, or 2 largest.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minimizeSum(int[] nums) {
        int n = nums.length;
        int[] sorted = Arrays.copyOf(nums, n);
        Arrays.sort(sorted);

        // n >= 3, so indexes 2 and n - 3 always exist
        int dropTwoSmallest = sorted[n - 1] - sorted[2];
        int dropOneEach = sorted[n - 2] - sorted[1];
        int dropTwoLargest = sorted[n - 3] - sorted[0];

        return Math.min(dropTwoSmallest, Math.min(dropOneEach, dropTwoLargest));
    }
}
