package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-ways-to-build-good-strings/description/

/**
 * 2466. Count Ways To Build Good Strings
 * Medium
 *
 * Given the integers zero, one, low, and high, we can construct a string by starting with an
 * empty string, and then at each step perform either of the following:
 *
 * Append the character '0' zero times.
 * Append the character '1' one times.
 *
 * This can be performed any number of times.
 *
 * A good string is a string constructed by the above process having a length between low and
 * high (inclusive).
 *
 * Return the number of different good strings that can be constructed satisfying these
 * properties. Since the answer can be large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: low = 3, high = 3, zero = 1, one = 1
 * Output: 8
 * Explanation:
 * One possible valid good string is "011".
 * It can be constructed as follows: "" -> "0" -> "01" -> "011".
 * All binary strings from "000" to "111" are good strings in this example.
 *
 * Example 2:
 *
 * Input: low = 2, high = 3, zero = 1, one = 2
 * Output: 5
 * Explanation: The good strings are "00", "11", "000", "110", and "011".
 *
 *
 * Constraints:
 *
 * 1 <= low <= high <= 10^5
 * 1 <= zero, one <= 10^5
 *
 */
public class CountWaysToBuildGoodStrings {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: 1-D DP BY LENGTH - dp[i] = dp[i - zero] + dp[i - one] (ONLY THE LENGTH MATTERS, LIKE CLIMBING STAIRS)
    /**
     * time = O(high)
     * space = O(high)
     */
    public int countGoodStrings(int low, int high, int zero, int one) {
        // dp[i] = number of distinct strings of length exactly i
        long[] dp = new long[high + 1];
        dp[0] = 1;   // the empty string

        for (int i = 1; i <= high; i++) {
            // last move appended `zero` zeros
            if (i >= zero) {
                dp[i] += dp[i - zero];
            }
            // last move appended `one` ones
            if (i >= one) {
                dp[i] += dp[i - one];
            }
            dp[i] %= MOD;
        }

        // sum over every allowed length
        long total = 0;
        for (int length = low; length <= high; length++) {
            total = (total + dp[length]) % MOD;
        }
        return (int) total;
    }
}
