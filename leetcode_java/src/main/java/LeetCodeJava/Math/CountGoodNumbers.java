package LeetCodeJava.Math;

// https://leetcode.com/problems/count-good-numbers/description/

/**
 * 1922. Count Good Numbers
 * Medium
 *
 * A digit string is good if the digits (0-indexed) at even indices are even and the digits at odd indices are prime (2, 3, 5, or 7).
 *
 * For example, "2582" is good because the digits (2 and 8) at even positions are even and the digits (5 and 2) at odd positions are prime. However, "3245" is not good because 3 is at an even index but is not even.
 *
 * Given an integer n, return the total number of good digit strings of length n. Since the answer may be large, return it modulo 10^9 + 7.
 *
 * A digit string is a string consisting of digits 0 through 9 that may contain leading zeros.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: 5
 * Explanation: The good numbers of length 1 are "0", "2", "4", "6", "8".
 *
 * Example 2:
 *
 * Input: n = 4
 * Output: 400
 *
 * Example 3:
 *
 * Input: n = 50
 * Output: 564908303
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^15
 *
 */
public class CountGoodNumbers {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COMBINATORICS + FAST POWER -> 5^ceil(n/2) * 4^floor(n/2)
    //  even indices: 5 choices (0,2,4,6,8); odd indices: 4 choices (2,3,5,7).
    //  n is up to 10^15, so the powers need binary exponentiation.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int countGoodNumbers(long n) {
        long evenPositions = (n + 1) / 2;
        long oddPositions = n / 2;
        long result = modPow(5, evenPositions) * modPow(4, oddPositions) % MOD;
        return (int) result;
    }

    /** Computes base^exp mod MOD by binary exponentiation. */
    private long modPow(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            // lowest bit of exp set -> this power of base is part of the product
            if (exp % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exp /= 2;
        }
        return result;
    }
}
