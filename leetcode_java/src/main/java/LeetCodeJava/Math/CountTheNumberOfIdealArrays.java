package LeetCodeJava.Math;

// https://leetcode.com/problems/count-the-number-of-ideal-arrays/description/

/**
 * 2338. Count the Number of Ideal Arrays
 * Hard
 *
 * You are given two integers n and maxValue, which are used to describe an ideal array.
 *
 * A 0-indexed integer array arr of length n is considered ideal if the following conditions hold:
 *
 * Every arr[i] is a value from 1 to maxValue, for 0 <= i < n.
 * Every arr[i] is divisible by arr[i - 1], for 0 < i < n.
 *
 * Return the number of distinct ideal arrays of length n. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 2, maxValue = 5
 * Output: 10
 * Explanation: The following are the possible ideal arrays:
 * - Arrays starting with the value 1 (5 arrays): [1,1], [1,2], [1,3], [1,4], [1,5]
 * - Arrays starting with the value 2 (2 arrays): [2,2], [2,4]
 * - Arrays starting with the value 3 (1 array): [3,3]
 * - Arrays starting with the value 4 (1 array): [4,4]
 * - Arrays starting with the value 5 (1 array): [5,5]
 * There are a total of 5 + 2 + 1 + 1 + 1 = 10 distinct ideal arrays.
 *
 * Example 2:
 *
 * Input: n = 5, maxValue = 3
 * Output: 11
 * Explanation: The following are the possible ideal arrays:
 * - Arrays starting with the value 1 (9 arrays):
 *    - With no other distinct values (1 array): [1,1,1,1,1]
 *    - With 2nd distinct value 2 (4 arrays): [1,1,1,1,2], [1,1,1,2,2], [1,1,2,2,2], [1,2,2,2,2]
 *    - With 2nd distinct value 3 (4 arrays): [1,1,1,1,3], [1,1,1,3,3], [1,1,3,3,3], [1,3,3,3,3]
 * - Arrays starting with the value 2 (1 array): [2,2,2,2,2]
 * - Arrays starting with the value 3 (1 array): [3,3,3,3,3]
 * There are a total of 9 + 1 + 1 = 11 distinct ideal arrays.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^4
 * 1 <= maxValue <= 10^4
 *
 */
public class CountTheNumberOfIdealArrays {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COUNT DISTINCT-VALUE DIVISOR CHAINS BY LENGTH, THEN PLACE THE STEP-UPS (COMBINATORICS + DP)
    //   an ideal array = a strict divisor chain v1 | v2 | ... | vk of distinct values
    //   + a choice of which k - 1 of the n - 1 gaps are "step up" points: C(n - 1, k - 1).
    //   dp[len][v] = number of strict divisor chains of length len ending at v;
    //   dp[1][v] = 1, dp[len][m] += dp[len - 1][d] for every proper multiple m of d.
    //   each step at least doubles, so len <= bitLength(maxValue) <= 14.
    /**
     * time = O(log(maxValue) * maxValue * log(maxValue) + n * log(maxValue))
     * space = O(log(maxValue) * maxValue + n * log(maxValue))
     */
    public int idealArrays(int n, int maxValue) {
        // longest possible chain: 1, 2, 4, ... while the value stays <= maxValue
        int maxChainLength = 0;
        long power = 1;
        while (power <= maxValue) {
            maxChainLength++;
            power *= 2;
        }

        // dp[len][v]: strict divisor chains of length len ending at value v
        long[][] dp = new long[maxChainLength + 1][maxValue + 1];
        for (int v = 1; v <= maxValue; v++) {
            dp[1][v] = 1;
        }
        for (int len = 2; len <= maxChainLength; len++) {
            for (int d = 1; d <= maxValue; d++) {
                if (dp[len - 1][d] == 0) {
                    continue;
                }
                // extend every chain ending at d by a proper multiple of d
                for (int multiple = 2 * d; multiple <= maxValue; multiple += d) {
                    dp[len][multiple] = (dp[len][multiple] + dp[len - 1][d]) % MOD;
                }
            }
        }

        // binom[i][j] = C(i, j) mod MOD, only j < maxChainLength is ever needed
        long[][] binom = new long[n][maxChainLength];
        for (int i = 0; i < n; i++) {
            binom[i][0] = 1;
            for (int j = 1; j < maxChainLength && j <= i; j++) {
                binom[i][j] = (binom[i - 1][j - 1] + binom[i - 1][j]) % MOD;
            }
        }

        long total = 0;
        for (int len = 1; len <= maxChainLength && len - 1 <= n - 1; len++) {
            long chains = 0;
            for (int v = 1; v <= maxValue; v++) {
                chains = (chains + dp[len][v]) % MOD;
            }
            // choose which len - 1 of the n - 1 gaps are the step-up points
            total = (total + chains * binom[n - 1][len - 1]) % MOD;
        }
        return (int) total;
    }
}
