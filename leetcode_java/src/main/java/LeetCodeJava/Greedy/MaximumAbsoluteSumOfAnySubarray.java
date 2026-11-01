package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-absolute-sum-of-any-subarray/description/

/**
 * 1749. Maximum Absolute Sum of Any Subarray
 * Medium
 *
 * You are given an integer array nums. The absolute sum of a subarray
 * [numsl, numsl+1, ..., numsr-1, numsr] is abs(numsl + numsl+1 + ... + numsr-1 + numsr).
 *
 * Return the maximum absolute sum of any (possibly empty) subarray of nums.
 *
 * Note that abs(x) is defined as follows:
 *
 * - If x is a negative integer, then abs(x) = -x.
 * - If x is a non-negative integer, then abs(x) = x.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,-3,2,3,-4]
 * Output: 5
 * Explanation: The subarray [2,3] has absolute sum = abs(2+3) = abs(5) = 5.
 *
 * Example 2:
 *
 * Input: nums = [2,-5,1,-4,3,-2]
 * Output: 8
 * Explanation: The subarray [-5,1,-4] has absolute sum = abs(-5+1-4) = abs(-8) = 8.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^4 <= nums[i] <= 10^4
 *
 */
public class MaximumAbsoluteSumOfAnySubarray {

    // V0
    // IDEA: KADANE TWICE (MAX SUBARRAY SUM AND MIN SUBARRAY SUM)
    //       |sum| is maximised by the most positive or the most negative subarray.
    //       maxEndingHere = max(maxEndingHere, 0) + x
    //       minEndingHere = min(minEndingHere, 0) + x
    //       the empty subarray (sum 0) is allowed, so the answer starts at 0.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxAbsoluteSum(int[] nums) {
        int maxEndingHere = 0;
        int minEndingHere = 0;
        int best = 0;
        for (int x : nums) {
            // extend the previous run only if it helps, otherwise start fresh at x
            maxEndingHere = Math.max(maxEndingHere, 0) + x;
            minEndingHere = Math.min(minEndingHere, 0) + x;
            best = Math.max(best, Math.max(maxEndingHere, -minEndingHere));
        }
        return best;
    }

    // V1
    // IDEA: PREFIX SUM (ANSWER = MAX PREFIX - MIN PREFIX)
    //       every subarray sum is prefix[j] - prefix[i]; its absolute value is
    //       largest when we take the largest and smallest prefix (including the
    //       empty prefix 0), in whichever order they occur.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxAbsoluteSum_1(int[] nums) {
        int prefix = 0;
        int maxPrefix = 0;
        int minPrefix = 0;
        for (int x : nums) {
            prefix += x;
            maxPrefix = Math.max(maxPrefix, prefix);
            minPrefix = Math.min(minPrefix, prefix);
        }
        return maxPrefix - minPrefix;
    }
}
