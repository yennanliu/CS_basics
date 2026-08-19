package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-subsequence-widths/

import java.util.Arrays;

/**
 *  891. Sum of Subsequence Widths
 *  Hard
 *
 *  The width of a sequence is the difference between the maximum and minimum
 *  elements in the sequence.
 *
 *  Given an array of integers nums, return the sum of the widths of all the
 *  non-empty subsequences of nums. Since the answer may be very large, return
 *  it modulo 10^9 + 7.
 *
 *  Example 1:
 *   Input: nums = [2,1,3]
 *   Output: 6
 *   Explanation: the subsequences are [1], [2], [3], [2,1], [2,3], [1,3], [2,1,3].
 *                The widths are 0, 0, 0, 1, 1, 2, 2 -> sum = 6.
 *
 *  Example 2:
 *   Input: nums = [2]
 *   Output: 0
 *
 *  Constraints:
 *   - 1 <= nums.length <= 10^5
 *   - 1 <= nums[i] <= 10^5
 */
public class SumOfSubsequenceWidths {

    // V0
    // IDEA: MATH + SORT. Order does not matter for a subsequence's width, so sort.
    //       After sorting, nums[i] is the max of 2^i subsequences and the min of
    //       2^(n-1-i) subsequences -> ans = sum( nums[i] * (2^i - 2^(n-1-i)) ).
    /**
     * time = O(n log n)
     * space = O(n)   (pow2 table)
     */
    public int sumSubseqWidths(int[] nums) {
        final int MOD = 1_000_000_007;
        Arrays.sort(nums);
        int n = nums.length;

        long[] pow2 = new long[n];
        pow2[0] = 1;
        for (int i = 1; i < n; i++) {
            pow2[i] = (pow2[i - 1] * 2) % MOD;
        }

        long res = 0;
        for (int i = 0; i < n; i++) {
            res = (res + (pow2[i] - pow2[n - 1 - i]) * nums[i]) % MOD;
        }
        return (int) ((res % MOD + MOD) % MOD);
    }
}
