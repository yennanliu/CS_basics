package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/power-of-heroes/description/

import java.util.*;

/**
 * 2681. Power of Heroes
 * Hard
 *
 * You are given a 0-indexed integer array nums representing the strength of some heroes. The
 * power of a group of heroes is defined as follows:
 *
 * Let i0, i1, ... ,ik be the indices of the heroes in a group. Then, the power of this group is
 * max(nums[i0], nums[i1], ... ,nums[ik])^2 * min(nums[i0], nums[i1], ... ,nums[ik]).
 *
 * Return the sum of the power of all non-empty groups of heroes possible. Since the sum could be
 * very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,1,4]
 * Output: 141
 * Explanation:
 * 1st group: [2] has power = 2^2 * 2 = 8.
 * 2nd group: [1] has power = 1^2 * 1 = 1.
 * 3rd group: [4] has power = 4^2 * 4 = 64.
 * 4th group: [2,1] has power = 2^2 * 1 = 4.
 * 5th group: [2,4] has power = 4^2 * 2 = 32.
 * 6th group: [1,4] has power = 4^2 * 1 = 16.
 * 7th group: [2,1,4] has power = 4^2 * 1 = 16.
 * The sum of powers of all groups is 8 + 1 + 64 + 4 + 32 + 16 + 16 = 141.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1]
 * Output: 7
 * Explanation: A total of 7 groups are possible, and the power of each group will be 1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class PowerOfHeroes {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: SORT + CONTRIBUTION DP - FIX THE MIN a[i]; maxSquares = SUM OVER j > i OF 2^(j-i-1) * a[j]^2
    /**
     * time = O(N log N)
     * space = O(1) extra (besides the sort)
     */
    public int sumOfPower(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        long result = 0;
        // maxSquares = sum over j > i of 2^(j - i - 1) * a[j]^2: every group whose min is a[i] and
        // whose max is a[j] can pick any subset of the (j - i - 1) elements strictly between them
        long maxSquares = 0;

        for (int i = sorted.length - 1; i >= 0; i--) {
            long value = sorted[i];
            long square = value * value % MOD;

            // group of a[i] alone, plus every larger group with a[i] as its minimum
            result = (result + square * value % MOD + value * maxSquares % MOD) % MOD;

            // move to i - 1: every existing term doubles (one more middle element), and a[i] joins as a max
            maxSquares = (maxSquares * 2 + square) % MOD;
        }
        return (int) result;
    }
}
