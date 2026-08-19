package LeetCodeJava.Stack;

// https://leetcode.com/problems/maximum-subarray-min-product/

/**
 *  1856. Maximum Subarray Min-Product
 *  Medium
 *
 *  The min-product of an array is equal to the minimum value in the array
 *  multiplied by the array's sum. For example, the array [3,2,5] (minimum
 *  value is 2) has a min-product of 2 * (3+2+5) = 20.
 *
 *  Given an array of integers nums, return the maximum min-product of any
 *  non-empty subarray of nums. Since the answer may be large, return it
 *  modulo 10^9 + 7.
 *
 *  Note that the min-product should be maximized BEFORE performing the modulo
 *  operation. Testcases are generated such that the maximum min-product
 *  without modulo will fit in a 64-bit signed integer.
 *
 *  Example 1:
 *    Input: nums = [1,2,3,2]
 *    Output: 14
 *    Explanation: subarray [2,3,2] -> 2 * 7 = 14
 *
 *  Example 2:
 *    Input: nums = [3,1,5,6,4,2]
 *    Output: 60
 *    Explanation: subarray [5,6,4] -> 4 * 15 = 60
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^7
 */
public class MaximumSubarrayMinProduct {

    // V0
    // IDEA: MONOTONIC STACK + PREFIX SUM (largest window where nums[i] is min)
    //       An optimal subarray is always MAXIMAL for its minimum: if nums[i]
    //       is the minimum, extending left/right while neighbours are >=
    //       nums[i] only adds positive numbers, so the best value is reached
    //       on the widest window in which nums[i] stays the minimum.
    //       For every i:
    //         left[i]  = index of previous strictly smaller element (-1 none)
    //         right[i] = index of next strictly smaller element     (n  none)
    //         candidate = nums[i] * (pre[right[i]] - pre[left[i] + 1])
    //       Use ">=" popping on one pass and ">" on the other so equal values
    //       are neither double-counted nor truncated.
    //       Take the modulo only at the very END.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxSumMinProduct(int[] nums) {
        final long MOD = 1_000_000_007L;
        int n = nums.length;

        int[] left = new int[n];
        int[] right = new int[n];
        int[] stack = new int[n];
        int top = -1;

        for (int i = 0; i < n; i++) {
            while (top >= 0 && nums[stack[top]] >= nums[i]) {
                top--;
            }
            left[i] = (top >= 0) ? stack[top] : -1;
            stack[++top] = i;
        }

        top = -1;
        for (int i = n - 1; i >= 0; i--) {
            while (top >= 0 && nums[stack[top]] > nums[i]) {
                top--;
            }
            right[i] = (top >= 0) ? stack[top] : n;
            stack[++top] = i;
        }

        // pre[k] = sum of nums[0 .. k-1]
        long[] pre = new long[n + 1];
        for (int i = 0; i < n; i++) {
            pre[i + 1] = pre[i] + nums[i];
        }

        long res = 0;
        for (int i = 0; i < n; i++) {
            long total = pre[right[i]] - pre[left[i] + 1];
            res = Math.max(res, (long) nums[i] * total);
        }
        return (int) (res % MOD);
    }
}
