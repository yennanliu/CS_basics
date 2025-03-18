package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-colors/

import java.util.Arrays;
import java.util.Comparator;

/**
 * 75. Sort Colors
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an array nums with n objects colored red, white, or blue, sort them in-place so that objects of the same color are adjacent, with the colors in the order red, white, and blue.
 *
 * We will use the integers 0, 1, and 2 to represent the color red, white, and blue, respectively.
 *
 * You must solve this problem without using the library's sort function.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,0,2,1,1,0]
 * Output: [0,0,1,1,2,2]
 * Example 2:
 *
 * Input: nums = [2,0,1]
 * Output: [0,1,2]
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 300
 * nums[i] is either 0, 1, or 2.
 *
 *
 * Follow up: Could you come up with a one-pass algorithm using only constant extra space?
 *
 *
 */
public class SortColors {

    // V0
    public void sortColors(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return;
        }

        Integer[] nums2 = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            nums2[i] = nums[i];
        }

        // sort on nums2
        Arrays.sort(nums2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        for (int i = 0; i < nums2.length; i++) {
            nums[i] = nums2[i];
        }

        return;
    }

    // V1

    // V2
}
