package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/ways-to-express-an-integer-as-sum-of-powers/description/

/**
 * 2787. Ways to Express an Integer as Sum of Powers
 * Medium
 *
 * Given two positive integers n and x.
 *
 * Return the number of ways n can be expressed as the sum of the xth power of unique positive
 * integers, in other words, the number of sets of unique integers [n1, n2, ..., nk] where
 * n = n1^x + n2^x + ... + nk^x.
 *
 * Since the result can be very large, return it modulo 10^9 + 7.
 *
 * For example, if n = 160 and x = 3, one way to express n is n = 2^3 + 3^3 + 5^3.
 *
 *
 * Example 1:
 *
 * Input: n = 10, x = 2
 * Output: 1
 * Explanation: We can express n as the following: n = 3^2 + 1^2 = 10.
 *
 * Example 2:
 *
 * Input: n = 4, x = 1
 * Output: 2
 * Explanation: We can express n in the following ways:
 * - n = 4^1 = 4.
 * - n = 3^1 + 1^1 = 4.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 300
 * 1 <= x <= 5
 *
 */
public class WaysToExpressAnIntegerAsSumOfPowers {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: 0/1 KNAPSACK COUNT - ITEMS ARE 1^x, 2^x, ... <= n, EACH USED AT MOST ONCE
    /**
     * time = O(N * N^(1/x))
     * space = O(N)
     */
    public int numberOfWays(int n, int x) {
        // ways[s] = number of sets of distinct powers (from the bases seen so far) summing to s
        long[] ways = new long[n + 1];
        ways[0] = 1;   // the empty set

        for (int base = 1; power(base, x) <= n; base++) {
            int item = power(base, x);
            // descending sums so each power is used at most once
            for (int sum = n; sum >= item; sum--) {
                ways[sum] = (ways[sum] + ways[sum - item]) % MOD;
            }
        }
        return (int) ways[n];
    }

    /** base^exponent; the caller stops once it passes n (<= 300), so it never overflows. */
    private int power(int base, int exponent) {
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        return result;
    }
}
