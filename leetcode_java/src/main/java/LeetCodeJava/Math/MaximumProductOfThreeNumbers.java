package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-product-of-three-numbers/

import java.util.Arrays;

/**
 *  628. Maximum Product of Three Numbers
 *  Easy
 *
 *  Given an integer array nums, find three numbers whose product is maximum
 *  and return the maximum product.
 *
 *  Example 1:
 *    Input: nums = [1,2,3]
 *    Output: 6
 *
 *  Example 2:
 *    Input: nums = [1,2,3,4]
 *    Output: 24
 *
 *  Example 3:
 *    Input: nums = [-1,-2,-3]
 *    Output: -6
 *
 *  Constraints:
 *   - 3 <= nums.length <= 10^4
 *   - -1000 <= nums[i] <= 1000
 */
public class MaximumProductOfThreeNumbers {

    // V0
    // IDEA: SORT. answer is either the 3 largest, or the 2 smallest (2 big
    //       negatives -> positive product) times the largest.
    /**
     * time = O(n log n)
     * space = O(1)
     */
    public int maximumProduct(int[] nums) {

        Arrays.sort(nums);
        int n = nums.length;

        int top3 = nums[n - 1] * nums[n - 2] * nums[n - 3];
        int bottom2TimesTop = nums[0] * nums[1] * nums[n - 1];

        return Math.max(top3, bottom2TimesTop);
    }

    // V1
    // IDEA: ONE PASS -- track the 3 largest and the 2 smallest values, no sort.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int maximumProduct_1(int[] nums) {

        int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
        int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE, max3 = Integer.MIN_VALUE;

        for (int x : nums) {
            if (x <= min1) {
                min2 = min1;
                min1 = x;
            } else if (x <= min2) {
                min2 = x;
            }

            if (x >= max1) {
                max3 = max2;
                max2 = max1;
                max1 = x;
            } else if (x >= max2) {
                max3 = max2;
                max2 = x;
            } else if (x >= max3) {
                max3 = x;
            }
        }

        return Math.max(max1 * max2 * max3, min1 * min2 * max1);
    }
}
