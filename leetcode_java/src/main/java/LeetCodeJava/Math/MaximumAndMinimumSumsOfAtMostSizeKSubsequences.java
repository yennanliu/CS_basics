package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-and-minimum-sums-of-at-most-size-k-subsequences/description/

import java.util.*;

/**
 * 3428. Maximum and Minimum Sums of at Most Size K Subsequences
 * Medium
 *
 * You are given an integer array nums and a positive integer k. Return the sum of
 * the maximum and minimum elements of all subsequences of nums with at most k
 * elements.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3], k = 2
 *
 * Output: 24
 *
 * Explanation:
 *
 * The subsequences of nums with at most 2 elements are:
 *
 *   Subsequence | Minimum | Maximum | Sum
 *   [1] | 1 | 1 | 2
 *   [2] | 2 | 2 | 4
 *   [3] | 3 | 3 | 6
 *   [1, 2] | 1 | 2 | 3
 *   [1, 3] | 1 | 3 | 4
 *   [2, 3] | 2 | 3 | 5
 *   Final Total |  |  | 24
 *
 * The output would be 24.
 *
 * Example 2:
 *
 * Input: nums = [5,0,6], k = 1
 *
 * Output: 22
 *
 * Explanation:
 *
 * For subsequences with exactly 1 element, the minimum and maximum values are the
 * element itself. Therefore, the total is 5 + 5 + 0 + 0 + 6 + 6 = 22.
 *
 * Example 3:
 *
 * Input: nums = [1,1,1], k = 2
 *
 * Output: 12
 *
 * Explanation:
 *
 * The subsequences [1, 1] and [1] each appear 3 times. For all of them, the
 * minimum and maximum are both 1. Thus, the total is 12.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^9
 * 1 <= k <= min(70, nums.length)
 *
 */
public class MaximumAndMinimumSumsOfAtMostSizeKSubsequences {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: SORT, THEN COUNT HOW OFTEN EACH ELEMENT IS THE MAX / THE MIN (COMBINATORICS)
    //       after sorting, nums[i] is the maximum of a subsequence exactly when every other
    //       chosen element comes from the i positions before it (ties are fine: equal values
    //       still occupy different sorted positions, so each subsequence has one designated max).
    //       so nums[i] is the max of f(i) = sum_{j=0}^{k-1} C(i, j) subsequences, and by the
    //       mirror argument the min of f(n - 1 - i) of them.
    //       f is built in one pass with Pascal's rule summed over j:
    //         f(i) = 2 * f(i - 1) - C(i - 1, k - 1)   once the size cap k binds,
    //         f(i) = 2^i                              before that.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minMaxSums(int[] nums, int k) {
        int n = nums.length;
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        // factorial tables for C(a, b) mod MOD
        long[] fact = new long[n + 1];
        long[] invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        invFact[n] = modPow(fact[n], MOD - 2);
        for (int i = n; i > 0; i--) {
            invFact[i - 1] = invFact[i] * i % MOD;
        }

        // ways[i] = number of subsequences of size <= k in which sorted[i] is the max,
        // i.e. choose 0..k-1 companions from the i elements before it
        long[] ways = new long[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                ways[i] = 1;
            } else if (i <= k - 1) {
                // the cap does not bind yet: every subset of the i earlier elements works
                ways[i] = ways[i - 1] * 2 % MOD;
            } else {
                // doubling over-counts the subsets that reach size k; remove them
                long tooBig = comb(i - 1, k - 1, fact, invFact);
                ways[i] = ((ways[i - 1] * 2 - tooBig) % MOD + MOD) % MOD;
            }
        }

        long answer = 0;
        for (int i = 0; i < n; i++) {
            // sorted[i] as the maximum (ways[i]) plus as the minimum (ways[n - 1 - i])
            long timesUsed = (ways[i] + ways[n - 1 - i]) % MOD;
            answer = (answer + sorted[i] % MOD * timesUsed) % MOD;
        }
        return (int) answer;
    }

    /** C(a, b) modulo MOD from the precomputed factorial tables; 0 when b is out of range. */
    private long comb(int a, int b, long[] fact, long[] invFact) {
        if (b < 0 || b > a) {
            return 0;
        }
        return fact[a] * invFact[b] % MOD * invFact[a - b] % MOD;
    }

    /** base^exp modulo MOD by fast exponentiation. */
    private long modPow(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            // the lowest bit of exp decides whether this power of base is used
            if ((exp & 1) == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exp >>= 1;
        }
        return result;
    }
}
