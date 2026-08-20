package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/longest-subarray-with-maximum-bitwise-and/

/**
 *  2419. Longest Subarray With Maximum Bitwise AND
 *  Medium
 *
 *  You are given an integer array nums of size n.
 *
 *  Consider a non-empty subarray from nums that has the maximum possible bitwise
 *  AND. In other words, let k be the maximum value of the bitwise AND of any
 *  subarray of nums. Then, only subarrays with a bitwise AND equal to k should be
 *  considered.
 *
 *  Return the length of the longest such subarray.
 *
 *  Example 1:
 *    Input: nums = [1,2,3,3,2,2]
 *    Output: 2
 *    Explanation: the maximum subarray AND is 3 and the longest subarray with
 *                 that value is [3,3].
 *
 *  Example 2:
 *    Input: nums = [1,2,3,4]
 *    Output: 1
 *    Explanation: the maximum subarray AND is 4, achieved only by [4].
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^6
 */
public class LongestSubarrayWithMaximumBitwiseAND {

    // V0
    // IDEA: ANDing MORE ELEMENTS ONLY CLEARS BITS -> THE BEST AND IS max(nums)
    //       a subarray's AND never exceeds any of its elements, so it never
    //       exceeds max(nums); and the single-element subarray holding the
    //       maximum achieves it.
    //       a longer subarray reaches that same value only if EVERY element equals
    //       the maximum (a smaller element would clear a bit), so the answer is the
    //       longest consecutive run of max(nums).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int longestSubarray(int[] nums) {
        int target = nums[0];
        for (int x : nums) {
            target = Math.max(target, x);
        }
        int res = 0;
        int run = 0;
        for (int x : nums) {
            run = (x == target) ? run + 1 : 0;
            res = Math.max(res, run);
        }
        return res;
    }
}
