package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/choose-numbers-from-two-arrays-in-range/description/

/**
 * 2143. Choose Numbers From Two Arrays in Range
 * Hard
 *
 * (premium / locked problem)
 *
 * You are given two 0-indexed integer arrays nums1 and nums2 of length n.
 *
 * A range [l, r] (inclusive) where 0 <= l <= r < n is balanced if:
 *
 * For every i in the range [l, r], you pick either nums1[i] or nums2[i].
 * The sum of the numbers you pick from nums1 equals to the sum of the numbers you pick from nums2 (the sum is considered to be 0 if you pick no numbers from an array).
 *
 * Two balanced ranges from [l1, r1] and [l2, r2] are considered to be different if at least one of the following is true:
 *
 * l1 != l2
 * r1 != r2
 * nums1[i] is picked in the first range, and nums2[i] is picked in the second range.
 *
 * Return the number of different ranges that are balanced. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,2,5], nums2 = [2,6,3]
 * Output: 3
 * Explanation: The balanced ranges are:
 * - [0, 1] where we choose nums2[0], and nums1[1].
 *   The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 2 = 2.
 * - [0, 2] where we choose nums1[0], nums2[1], and nums1[2].
 *   The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 1 + 5 = 6.
 * - [0, 2] where we choose nums1[0], nums1[1], and nums2[2].
 *   The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 1 + 2 = 3.
 * Note that the second and third balanced ranges are different.
 * In the second balanced range, we choose nums2[1] and in the third balanced range, we choose nums1[1].
 *
 * Example 2:
 *
 * Input: nums1 = [0,1], nums2 = [1,0]
 * Output: 4
 * Explanation: The balanced ranges are:
 * - [0, 0] where we choose nums1[0].
 *   The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 0 = 0.
 * - [1, 1] where we choose nums2[1].
 *   The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 0 = 0.
 * - [0, 1] where we choose nums1[0] and nums2[1].
 *   The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 0 = 0.
 * - [0, 1] where we choose nums2[0] and nums1[1].
 *   The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 1 = 1.
 *
 *
 * Constraints:
 *
 * n == nums1.length == nums2.length
 * 1 <= n <= 100
 * 0 <= nums1[i], nums2[i] <= 100
 *
 */
public class ChooseNumbersFromTwoArraysInRange {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: ROLLING DP OVER THE RUNNING DIFFERENCE (sum1 - sum2) FOR ALL OPEN RANGES AT ONCE
    /**
     * time = O(N * S), S = sum(nums1) + sum(nums2)
     * space = O(S)
     */
    public int countSubranges(int[] nums1, int[] nums2) {
        int n = nums1.length;

        // the difference d ranges over [-sum2, sum1]; shift by sum2 to index an array
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i < n; i++) {
            sum1 += nums1[i];
            sum2 += nums2[i];
        }
        int offset = sum2;
        int size = sum1 + sum2 + 1;

        // dp[d + offset] = number of (start, choice pattern) pairs whose open range
        // ending at the current index has difference d
        int[] dp = new int[size];
        long result = 0;

        for (int i = 0; i < n; i++) {
            int[] next = new int[size];

            // every open range must take index i from one side
            for (int idx = 0; idx < size; idx++) {
                if (dp[idx] == 0) {
                    continue;
                }
                int pickFirst = idx + nums1[i];   // take nums1[i]: difference grows
                int pickSecond = idx - nums2[i];  // take nums2[i]: difference shrinks
                if (pickFirst < size) {
                    next[pickFirst] = (next[pickFirst] + dp[idx]) % MOD;
                }
                if (pickSecond >= 0) {
                    next[pickSecond] = (next[pickSecond] + dp[idx]) % MOD;
                }
            }

            // a brand-new range starting at i, with either choice
            next[offset + nums1[i]] = (next[offset + nums1[i]] + 1) % MOD;
            next[offset - nums2[i]] = (next[offset - nums2[i]] + 1) % MOD;

            dp = next;

            // balanced ranges ending at i
            result = (result + dp[offset]) % MOD;
        }
        return (int) result;
    }
}
