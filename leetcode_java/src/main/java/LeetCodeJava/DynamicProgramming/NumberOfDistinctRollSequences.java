package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-distinct-roll-sequences/description/

/**
 * 2318. Number of Distinct Roll Sequences
 * Hard
 *
 * You are given an integer n. You roll a fair 6-sided dice n times. Determine the total number of distinct sequences of rolls possible such that the following conditions are satisfied:
 *
 * The greatest common divisor of any adjacent values in the sequence is equal to 1.
 * There is at least a gap of 2 rolls between equal valued rolls. More formally, if the value of the ith roll is equal to the value of the jth roll, then abs(i - j) > 2.
 *
 * Return the total number of distinct sequences possible. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Two sequences are considered distinct if at least one element is different.
 *
 *
 * Example 1:
 *
 * Input: n = 4
 * Output: 184
 * Explanation: Some of the possible sequences are (1, 2, 3, 4), (6, 1, 2, 3), (1, 2, 3, 1), etc.
 * Some invalid sequences are (1, 2, 1, 3), (1, 2, 3, 6).
 * (1, 2, 1, 3) is invalid since the first and third roll have an equal value and abs(1 - 3) = 2 (i and j are 1-indexed).
 * (1, 2, 3, 6) is invalid since the greatest common divisor of 3 and 6 = 3.
 * There are a total of 184 distinct sequences possible, so we return 184.
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 22
 * Explanation: Some of the possible sequences are (1, 2), (2, 1), (3, 2).
 * Some invalid sequences are (3, 6), (2, 4) since the greatest common divisor is not equal to 1.
 * There are a total of 22 distinct sequences possible, so we return 22.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^4
 *
 */
public class NumberOfDistinctRollSequences {

    private static final int MOD = 1_000_000_007;
    private static final int FACES = 6;

    // V0
    // IDEA: DP ON THE LAST TWO ROLLS — dp[a][b] = SEQUENCES WHOSE LAST TWO VALUES ARE a, b
    /**
     * time = O(N * 6^3)
     * space = O(6^2)
     */
    public int distinctSequences(int n) {
        if (n == 1) {
            return FACES;
        }

        // canFollow[a][b]: faces a+1 and b+1 may be adjacent (different and coprime)
        boolean[][] canFollow = new boolean[FACES][FACES];
        for (int a = 0; a < FACES; a++) {
            for (int b = 0; b < FACES; b++) {
                canFollow[a][b] = (a != b) && gcd(a + 1, b + 1) == 1;
            }
        }

        // base case: length-2 sequences
        long[][] dp = new long[FACES][FACES];
        for (int a = 0; a < FACES; a++) {
            for (int b = 0; b < FACES; b++) {
                if (canFollow[a][b]) {
                    dp[a][b] = 1;
                }
            }
        }

        for (int length = 3; length <= n; length++) {
            long[][] next = new long[FACES][FACES];
            for (int a = 0; a < FACES; a++) {
                for (int b = 0; b < FACES; b++) {
                    if (dp[a][b] == 0) {
                        continue;
                    }
                    for (int c = 0; c < FACES; c++) {
                        // c must be coprime with and different from b, and differ from a (gap of 2)
                        if (c != a && canFollow[b][c]) {
                            next[b][c] = (next[b][c] + dp[a][b]) % MOD;
                        }
                    }
                }
            }
            dp = next;
        }

        long total = 0;
        for (int a = 0; a < FACES; a++) {
            for (int b = 0; b < FACES; b++) {
                total = (total + dp[a][b]) % MOD;
            }
        }
        return (int) total;
    }

    /** Greatest common divisor. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
