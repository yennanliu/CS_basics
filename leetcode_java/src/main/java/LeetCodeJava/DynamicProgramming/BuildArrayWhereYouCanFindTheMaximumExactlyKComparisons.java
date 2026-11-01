package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/build-array-where-you-can-find-the-maximum-exactly-k-comparisons/description/

/**
 * 1420. Build Array Where You Can Find The Maximum Exactly K Comparisons
 * Hard
 *
 * You are given three integers n, m and k. Consider the following algorithm to find the maximum element of an array of positive integers:
 *
 *   maximum_value = -1
 *   maximum_index = -1
 *   search_cost = 0
 *   n = arr.length
 *   for (i = 0; i < n; i++) {
 *       if (maximum_value < arr[i]) {
 *           maximum_value = arr[i]
 *           maximum_index = i
 *           search_cost = search_cost + 1
 *       }
 *   }
 *   return maximum_index
 *
 * You should build the array arr which has the following properties:
 *
 * arr has exactly n integers.
 * 1 <= arr[i] <= m where (0 <= i < n).
 * After applying the mentioned algorithm to arr, the value search_cost is equal to k.
 *
 * Return the number of ways to build the array arr under the mentioned conditions. As the answer may grow large, the answer must be computed modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 2, m = 3, k = 1
 * Output: 6
 * Explanation: The possible arrays are [1, 1], [2, 1], [2, 2], [3, 1], [3, 2] [3, 3]
 *
 * Example 2:
 *
 * Input: n = 5, m = 2, k = 3
 * Output: 0
 * Explanation: There are no possible arrays that satisfy the mentioned conditions.
 *
 * Example 3:
 *
 * Input: n = 9, m = 1, k = 1
 * Output: 1
 * Explanation: The only possible array is [1, 1, 1, 1, 1, 1, 1, 1, 1]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 50
 * 1 <= m <= 100
 * 0 <= k <= n
 *
 */
public class BuildArrayWhereYouCanFindTheMaximumExactlyKComparisons {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP ON (LENGTH, PREFIX MAX, COST) + PREFIX SUM
    //       search_cost counts how many times a NEW prefix maximum appears, so the state
    //       must remember the running maximum.
    //       dp[c][j] = # of arrays built so far whose prefix max is j and cost is c.
    //       appending one element:
    //         value <= j (j choices)   -> max and cost unchanged : dp[c][j] * j
    //         value == j as a new max  -> cost + 1              : sum of dp[c-1][j0], j0 < j
    //       the inner sum is a running prefix sum over j, which drops O(n*m^2*k) to O(n*m*k).
    /**
     * time = O(N * M * K)
     * space = O(M * K)
     */
    public int numOfArrays(int n, int m, int k) {
        // a non-empty array always has cost >= 1, and cost can never exceed the m distinct values
        if (k == 0 || k > m) {
            return 0;
        }

        // dp[c][j] for arrays of length 1: the single element j is the max, cost 1
        long[][] dp = new long[k + 1][m + 1];
        for (int j = 1; j <= m; j++) {
            dp[1][j] = 1;
        }

        for (int length = 2; length <= n; length++) {
            long[][] next = new long[k + 1][m + 1];
            // cost cannot exceed the length of the array
            int maxCost = Math.min(k, length);
            for (int c = 1; c <= maxCost; c++) {
                // prefixSum = sum of dp[c-1][j0] for every j0 < j
                long prefixSum = 0;
                for (int j = 1; j <= m; j++) {
                    // keep the max j: append any of 1..j (j choices)
                    long keepMax = dp[c][j] * j % MOD;
                    // j becomes the new max: the previous max was some j0 < j with cost c - 1
                    next[c][j] = (keepMax + prefixSum) % MOD;
                    prefixSum = (prefixSum + dp[c - 1][j]) % MOD;
                }
            }
            dp = next;
        }

        // any final maximum is allowed
        long total = 0;
        for (int j = 1; j <= m; j++) {
            total = (total + dp[k][j]) % MOD;
        }
        return (int) total;
    }
}
