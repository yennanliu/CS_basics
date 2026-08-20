package LeetCodeJava.Sort;

// https://leetcode.com/problems/sum-of-absolute-differences-in-a-sorted-array/

/**
 *  1685. Sum of Absolute Differences in a Sorted Array
 *  Medium
 *
 *  You are given an integer array nums sorted in non-decreasing order.
 *
 *  Build and return an integer array result with the same length as nums such
 *  that result[i] is equal to the summation of absolute differences between
 *  nums[i] and all the other elements in the array.
 *
 *  In other words, result[i] is equal to sum(|nums[i]-nums[j]|) where
 *  0 <= j < nums.length and j != i (0-indexed).
 *
 *  Example 1:
 *    Input: nums = [2,3,5]
 *    Output: [4,3,5]
 *    Explanation: result[0] = |2-2|+|2-3|+|2-5| = 4, etc.
 *
 *  Example 2:
 *    Input: nums = [1,4,6,8,10]
 *    Output: [24,15,13,15,21]
 *
 *  Constraints:
 *    2 <= nums.length <= 10^5
 *    1 <= nums[i] <= nums[i + 1] <= 10^4
 */
public class SumOfAbsoluteDifferencesInASortedArray {

    // V0
    // IDEA: PREFIX SUMS — THE SORTED INPUT KILLS THE ABSOLUTE VALUE
    //       because nums is sorted, for index i:
    //         every j < i has nums[j] <= nums[i]  -> |diff| = nums[i] - nums[j]
    //         every j > i has nums[j] >= nums[i]  -> |diff| = nums[j] - nums[i]
    //       so with pre = sum(nums[0..i-1]) and suf = sum(nums[i+1..n-1]):
    //         result[i] = (i * nums[i] - pre) + (suf - (n - 1 - i) * nums[i])
    //       one left-to-right sweep maintains pre and suf in O(1) per step.
    //       NOTE: totals reach 10^5 * 10^4 = 10^9, so accumulate in long and
    //             only narrow to int at the end (each result still fits int).
    /**
     * time = O(n)
     * space = O(1) extra   // excluding the output
     */
    public int[] getSumAbsoluteDifferences(int[] nums) {
        int n = nums.length;

        long total = 0;
        for (int v : nums) {
            total += v;
        }

        int[] res = new int[n];
        long pre = 0;
        for (int i = 0; i < n; i++) {
            long v = nums[i];
            long suf = total - pre - v;
            long left = i * v - pre;
            long right = suf - (long) (n - 1 - i) * v;
            res[i] = (int) (left + right);
            pre += v;
        }
        return res;
    }
}
