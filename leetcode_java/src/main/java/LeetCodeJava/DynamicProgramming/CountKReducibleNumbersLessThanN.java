package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-k-reducible-numbers-less-than-n/description/
/**
 * 3352. Count K-Reducible Numbers Less Than N
 * Hard
 *
 * You are given a binary string s representing a number n in its binary form.
 *
 * You are also given an integer k.
 *
 * An integer x is called k-reducible if performing the following operation at most k times
 * reduces it to 1:
 *
 * Replace x with the count of set bits in its binary representation.
 *
 * For example, the binary representation of 6 is "110". Applying the operation once reduces it
 * to 1 (since "110" has two set bits, and the binary representation of 2 is "10", which has one
 * set bit).
 *
 * Return an integer denoting the number of positive integers less than n that are k-reducible.
 *
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: s = "111", k = 1
 * Output: 3
 * Explanation:
 * n = 7. The 1-reducible integers less than 7 are 1, 2, and 4.
 *
 * Example 2:
 *
 * Input: s = "1000", k = 2
 * Output: 6
 * Explanation:
 * n = 8. The 2-reducible integers less than 8 are 1, 2, 3, 4, 5, and 6.
 *
 * Example 3:
 *
 * Input: s = "1", k = 3
 * Output: 0
 * Explanation:
 * There are no positive integers less than n = 1, so the answer is 0.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 800
 * s has no leading zeros.
 * s consists only of the characters '0' and '1'.
 * 1 <= k <= 5
 *
 */
public class CountKReducibleNumbersLessThanN {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DIGIT COUNTING BY POPCOUNT + PRECOMPUTED STEP COUNTS FOR SMALL VALUES
    /**
     *  One operation sends x to popcount(x) <= 800, so x is k-reducible iff
     *  steps[popcount(x)] + 1 <= k, where
     *     - steps[1] = 0, steps[v] = steps[popcount(v)] + 1
     *
     *  Counting x < n with popcount c: walk the bits of s; at each '1', set it to 0
     *  instead, and the remaining `free` lower bits are anything — choose `extra` of them
     *  to be 1 in C(free, extra) ways, giving popcount onesSoFar + extra.
     *
     *  time = O(L^2), L = s.length()
     *  space = O(L^2)
     */
    public int countKReducibleNumbers(String s, int k) {
        int length = s.length();

        // steps[v] = operations needed to bring v down to 1
        int[] steps = new int[length + 1];
        for (int v = 2; v <= length; v++) {
            steps[v] = steps[Integer.bitCount(v)] + 1;
        }

        // binomials C[i][j] mod p via Pascal's triangle
        long[][] binom = new long[length + 1][length + 1];
        for (int i = 0; i <= length; i++) {
            binom[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                binom[i][j] = (binom[i - 1][j - 1] + binom[i - 1][j]) % MOD;
            }
        }

        long result = 0;
        int onesSoFar = 0; // ones in the prefix that matches s exactly
        for (int i = 0; i < length; i++) {
            if (s.charAt(i) != '1') {
                continue;
            }
            // put a 0 here instead: every completion of the lower bits is < n
            int free = length - i - 1;
            for (int extra = 0; extra <= free; extra++) {
                int popcount = onesSoFar + extra;
                if (popcount == 0) {
                    continue; // that would be the number 0, not positive
                }
                if (steps[popcount] + 1 <= k) {
                    result = (result + binom[free][extra]) % MOD;
                }
            }
            onesSoFar++;
        }
        return (int) result;
    }
}
