package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/restore-the-array/description/

/**
 * 1416. Restore The Array
 * Hard
 *
 * A program was supposed to print an array of integers. The program forgot to print
 * whitespaces and the array is printed as a string of digits s and all we know is that
 * all integers in the array were in the range [1, k] and there are no leading zeros in
 * the array.
 *
 * Given the string s and the integer k, return the number of the possible arrays that can
 * be printed as s using the mentioned program. Since the answer may be very large, return
 * it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: s = "1000", k = 10000
 * Output: 1
 * Explanation: The only possible array is [1000]
 *
 * Example 2:
 *
 * Input: s = "1000", k = 10
 * Output: 0
 * Explanation: There cannot be an array that was printed this way and has all integer >= 1
 * and <= 10.
 *
 * Example 3:
 *
 * Input: s = "1317", k = 2000
 * Output: 8
 * Explanation: Possible arrays are [1317],[131,7],[13,17],[1,317],[13,1,7],[1,31,7],
 * [1,3,17],[1,3,1,7]
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of only digits and does not contain leading zeros.
 * 1 <= k <= 10^9
 *
 */
public class RestoreTheArray {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP OVER SUFFIXES (CUT THE FIRST NUMBER, COUNT THE REST)
    /**
     * dp[i] = number of valid splits of the suffix s[i..n-1]
     *
     * dp[i] = sum of dp[j + 1] over every j with 1 <= value(s[i..j]) <= k
     *   - s[i] == '0' -> no number may start here (leading zero), dp[i] = 0
     *   - k <= 10^9 has at most 10 digits, so the inner loop stops after ~10 steps
     *     (break as soon as the running number exceeds k) -> linear overall
     *
     * base: dp[n] = 1 (the empty tail)
     */
    /**
     * time = O(N * log10(K))
     * space = O(N)
     */
    public int numberOfArrays(String s, int k) {
        int n = s.length();
        long[] dp = new long[n + 1];
        dp[n] = 1;

        for (int i = n - 1; i >= 0; i--) {
            // a number cannot start with a leading zero
            if (s.charAt(i) == '0') {
                continue;
            }
            // long: one digit past k (~10^10) still has to be representable
            long number = 0;
            for (int j = i; j < n; j++) {
                number = number * 10 + (s.charAt(j) - '0');
                if (number > k) {
                    break;
                }
                dp[i] = (dp[i] + dp[j + 1]) % MOD;
            }
        }
        return (int) dp[0];
    }
}
