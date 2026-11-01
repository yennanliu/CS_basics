package LeetCodeJava.Math;

// https://leetcode.com/problems/ways-to-split-array-into-good-subarrays/description/

/**
 * 2750. Ways to Split Array Into Good Subarrays
 * Medium
 *
 * You are given a binary array nums.
 *
 * A subarray of an array is good if it contains exactly one element with the value 1.
 *
 * Return an integer denoting the number of ways to split the array nums into good subarrays. As the number may be too large, return it modulo 10^9 + 7.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,0,0,1]
 * Output: 3
 *
 * Example 2:
 *
 * Input: nums = [0,1,0]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 1
 *
 */
public class WaysToSplitArrayIntoGoodSubarrays {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: MULTIPLICATION PRINCIPLE -- between consecutive 1s at j < i there are (i - j) places for the one cut
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numberOfGoodSubarraySplits(int[] nums) {
        long ways = 1;
        int previousOne = -1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                continue;
            }
            if (previousOne != -1) {
                ways = ways * (i - previousOne) % MOD;
            }
            previousOne = i;
        }
        // no 1 at all -> no part can hold exactly one 1
        if (previousOne == -1) {
            return 0;
        }
        return (int) ways;
    }
}
