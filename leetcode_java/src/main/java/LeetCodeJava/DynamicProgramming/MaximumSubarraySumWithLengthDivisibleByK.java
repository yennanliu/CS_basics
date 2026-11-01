package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-subarray-sum-with-length-divisible-by-k/description/

import java.util.Arrays;

/**
 * 3381. Maximum Subarray Sum With Length Divisible by K
 * Medium
 *
 * You are given an array of integers nums and an integer k.
 *
 * Return the maximum sum of a subarray of nums, such that the size of the subarray is divisible by k.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2], k = 1
 * Output: 3
 * Explanation:
 * The subarray [1, 2] with sum 3 has length equal to 2 which is divisible by 1.
 *
 * Example 2:
 *
 * Input: nums = [-1,-2,-3,-4,-5], k = 4
 * Output: -10
 * Explanation:
 * The maximum sum subarray is [-1, -2, -3, -4] which has length equal to 4 which is divisible by 4.
 *
 * Example 3:
 *
 * Input: nums = [-5,1,2,-3,4], k = 2
 * Output: 4
 * Explanation:
 * The maximum sum subarray is [1, 2, -3, 4] which has length equal to 4 which is divisible by 2.
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 2 * 10^5
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class MaximumSubarraySumWithLengthDivisibleByK {

    // V0
    // IDEA: PREFIX SUM + SMALLEST PREFIX PER (LENGTH % K) RESIDUE
    /**
     *  sum(nums[l..r]) = prefix[r+1] - prefix[l], and the length r - l + 1 is divisible by k
     *  exactly when (r + 1) % k == l % k. So for each right end, subtract the smallest
     *  earlier prefix of the same residue.
     *
     *  minPrefix[res] = smallest prefix sum seen so far whose length % k == res
     *  Init: minPrefix[0] = 0 (the empty prefix), every other residue is unseen.
     *  The answer starts at -infinity: the array may be all negative and a subarray
     *  cannot be empty.
     *
     *  time = O(N)
     *  space = O(K)
     */
    public long maxSubarraySum(int[] nums, int k) {
        long[] minPrefix = new long[k];
        boolean[] seen = new boolean[k];
        Arrays.fill(minPrefix, Long.MAX_VALUE);
        minPrefix[0] = 0;
        seen[0] = true;

        long best = Long.MIN_VALUE;
        long prefix = 0;
        for (int i = 0; i < nums.length; i++) {
            prefix += nums[i];
            int residue = (i + 1) % k;
            // best subarray ending at i whose length is a multiple of k
            if (seen[residue]) {
                best = Math.max(best, prefix - minPrefix[residue]);
            }
            if (prefix < minPrefix[residue]) {
                minPrefix[residue] = prefix;
                seen[residue] = true;
            }
        }
        return best;
    }
}
