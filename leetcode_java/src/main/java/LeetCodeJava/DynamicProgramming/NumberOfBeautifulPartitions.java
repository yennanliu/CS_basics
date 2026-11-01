package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-beautiful-partitions/description/

/**
 * 2478. Number of Beautiful Partitions
 * Hard
 *
 * You are given a string s that consists of the digits '1' to '9' and two integers k and minLength.
 *
 * A partition of s is called beautiful if:
 *
 * s is partitioned into k non-intersecting substrings.
 * Each substring has a length of at least minLength.
 * Each substring starts with a prime digit and ends with a non-prime digit. Prime digits are '2',
 * '3', '5', and '7', and the rest of the digits are non-prime.
 *
 * Return the number of beautiful partitions of s. Since the answer may be very large, return it
 * modulo 10^9 + 7.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "23542185131", k = 3, minLength = 2
 * Output: 3
 * Explanation: There exists three ways to create a beautiful partition:
 * "2354 | 218 | 5131"
 * "2354 | 21851 | 31"
 * "2354218 | 51 | 31"
 *
 * Example 2:
 *
 * Input: s = "23542185131", k = 3, minLength = 3
 * Output: 1
 * Explanation: There exists one way to create a beautiful partition: "2354 | 218 | 5131".
 *
 * Example 3:
 *
 * Input: s = "3312958", k = 3, minLength = 1
 * Output: 1
 * Explanation: There exists one way to create a beautiful partition: "331 | 29 | 58".
 *
 *
 * Constraints:
 *
 * 1 <= k, minLength <= s.length <= 1000
 * s consists of the digits '1' to '9'.
 *
 */
public class NumberOfBeautifulPartitions {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP OVER CUT POSITIONS + PREFIX SUMS - f[i][j] = g[i - minLength][j - 1] AT EVERY LEGAL CUT i
    /**
     * time = O(N * K)
     * space = O(N * K)
     */
    public int beautifulPartitions(String s, int k, int minLength) {
        int n = s.length();

        // the first piece must start prime and the last piece must end non-prime
        if (!isPrime(s.charAt(0)) || isPrime(s.charAt(n - 1))) {
            return 0;
        }

        // f[i][j] = ways to split the first i chars into j beautiful pieces
        // g[i][j] = f[0][j] + f[1][j] + ... + f[i][j]
        long[][] f = new long[n + 1][k + 1];
        long[][] g = new long[n + 1][k + 1];
        f[0][0] = 1;
        g[0][0] = 1;

        for (int i = 1; i <= n; i++) {
            // i is a legal cut: s[i-1] ends a piece (non-prime) and s[i] starts the next (prime)
            boolean endsNonPrime = !isPrime(s.charAt(i - 1));
            boolean nextStartsPrime = (i == n) || isPrime(s.charAt(i));
            if (i >= minLength && endsNonPrime && nextStartsPrime) {
                for (int j = 1; j <= k; j++) {
                    // the previous cut t must leave this piece at least minLength long: t <= i - minLength
                    f[i][j] = g[i - minLength][j - 1];
                }
            }
            for (int j = 0; j <= k; j++) {
                g[i][j] = (g[i - 1][j] + f[i][j]) % MOD;
            }
        }
        return (int) f[n][k];
    }

    /** True for the prime digits 2, 3, 5, 7. */
    private boolean isPrime(char digit) {
        return digit == '2' || digit == '3' || digit == '5' || digit == '7';
    }
}
