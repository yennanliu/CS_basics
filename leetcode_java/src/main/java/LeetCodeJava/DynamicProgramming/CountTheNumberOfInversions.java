package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-the-number-of-inversions/description/

import java.util.*;

/**
 * 3193. Count the Number of Inversions
 * Hard
 *
 * You are given an integer n and a 2D array requirements, where requirements[i] = [end_i, cnt_i] represents the end index and the inversion count of each requirement.
 *
 * A pair of indices (i, j) from an integer array nums is called an inversion if:
 *
 * i < j and nums[i] > nums[j]
 *
 * Return the number of permutations perm of [0, 1, 2, ..., n - 1] such that for all requirements[i], perm[0..end_i] has exactly cnt_i inversions.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 3, requirements = [[2,2],[0,0]]
 * Output: 2
 * Explanation:
 * The two permutations are:
 * [2, 0, 1]
 *     Prefix [2, 0, 1] has inversions (0, 1) and (0, 2).
 *     Prefix [2] has 0 inversions.
 * [1, 2, 0]
 *     Prefix [1, 2, 0] has inversions (0, 2) and (1, 2).
 *     Prefix [1] has 0 inversions.
 *
 * Example 2:
 *
 * Input: n = 3, requirements = [[2,2],[1,1],[0,0]]
 * Output: 1
 * Explanation:
 * The only satisfying permutation is [2, 0, 1]:
 * Prefix [2, 0, 1] has inversions (0, 1) and (0, 2).
 * Prefix [2, 0] has an inversion (0, 1).
 * Prefix [2] has 0 inversions.
 *
 * Example 3:
 *
 * Input: n = 2, requirements = [[0,0],[1,0]]
 * Output: 1
 * Explanation:
 * The only satisfying permutation is [0, 1]:
 * Prefix [0] has 0 inversions.
 * Prefix [0, 1] has an inversion (0, 1).
 *
 *
 * Constraints:
 *
 * 2 <= n <= 300
 * 1 <= requirements.length <= n
 * requirements[i] = [end_i, cnt_i]
 * 0 <= end_i <= n - 1
 * 0 <= cnt_i <= 400
 * The input is generated such that there is at least one i such that end_i == n - 1.
 * The input is generated such that all end_i are unique.
 *
 */
public class CountTheNumberOfInversions {

    // V0
    // IDEA: BUILD THE PERMUTATION BY RELATIVE RANK + PREFIX-SUM DP OVER INVERSION COUNT
    //   Placing the (i+1)-th element at relative rank r among i placed ones adds (i - r)
    //   inversions, anywhere from 0 to i.
    //   dp[i][c] = arrangements of the first i+1 positions with exactly c inversions
    //            = sum over t = 0 .. min(i, c) of dp[i-1][c - t]   (a window -> prefix sums)
    //   A requirement at end i zeroes every dp[i][c] except c == cnt.
    //   Counts above the largest required cnt (<= 400) can never satisfy the last requirement.
    /**
     * time = O(N * maxCnt)
     * space = O(maxCnt)
     */
    public int numberOfPermutations(int n, int[][] requirements) {
        final long MOD = 1_000_000_007L;
        // required[end] = required inversion count of perm[0..end], or -1 if none
        int[] required = new int[n];
        Arrays.fill(required, -1);
        int cap = 0;
        for (int[] requirement : requirements) {
            required[requirement[0]] = requirement[1];
            cap = Math.max(cap, requirement[1]);
        }
        // a single element has no inversions
        if (required[0] > 0) {
            return 0;
        }

        long[] dp = new long[cap + 1];
        dp[0] = 1;
        for (int i = 1; i < n; i++) {
            // prefix[c + 1] = dp[0] + ... + dp[c]
            long[] prefix = new long[cap + 2];
            for (int c = 0; c <= cap; c++) {
                prefix[c + 1] = (prefix[c] + dp[c]) % MOD;
            }
            long[] next = new long[cap + 1];
            for (int c = 0; c <= cap; c++) {
                // the new element adds between 0 and i inversions
                int low = Math.max(0, c - i);
                next[c] = (prefix[c + 1] - prefix[low] + MOD) % MOD;
            }
            // enforce the requirement on this prefix, if there is one
            if (required[i] != -1) {
                int need = required[i];
                for (int c = 0; c <= cap; c++) {
                    if (c != need) {
                        next[c] = 0;
                    }
                }
            }
            dp = next;
        }
        return (int) dp[required[n - 1]];
    }
}
