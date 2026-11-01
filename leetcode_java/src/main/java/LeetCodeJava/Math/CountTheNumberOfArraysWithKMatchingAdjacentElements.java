package LeetCodeJava.Math;

// https://leetcode.com/problems/count-the-number-of-arrays-with-k-matching-adjacent-elements/description/

/**
 * 3405. Count the Number of Arrays with K Matching Adjacent Elements
 * Hard
 *
 * You are given three integers n, m, k. A good array arr of size n is defined as follows:
 *
 * Each element in arr is in the inclusive range [1, m].
 * Exactly k indices i (where 1 <= i < n) satisfy the condition arr[i - 1] == arr[i].
 *
 * Return the number of good arrays that can be formed.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 3, m = 2, k = 1
 * Output: 4
 * Explanation:
 * There are 4 good arrays. They are [1, 1, 2], [1, 2, 2], [2, 1, 1] and [2, 2, 1].
 * Hence, the answer is 4.
 *
 * Example 2:
 *
 * Input: n = 4, m = 2, k = 2
 * Output: 6
 * Explanation:
 * The good arrays are [1, 1, 1, 2], [1, 1, 2, 2], [1, 2, 2, 2], [2, 1, 1, 1], [2, 2, 1, 1] and
 * [2, 2, 2, 1].
 * Hence, the answer is 6.
 *
 * Example 3:
 *
 * Input: n = 5, m = 2, k = 0
 * Output: 2
 * Explanation:
 * The good arrays are [1, 2, 1, 2, 1] and [2, 1, 2, 1, 2]. Hence, the answer is 2.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 1 <= m <= 10^5
 * 0 <= k <= n - 1
 *
 */
public class CountTheNumberOfArraysWithKMatchingAdjacentElements {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: COMBINATORICS — CHOOSE WHICH k OF THE n - 1 BOUNDARIES ARE EQUAL, THEN FILL THE VALUES:
    //   arr[0] has m choices, an equal boundary copies its neighbour, every other boundary has m - 1.
    //   answer = C(n - 1, k) * m * (m - 1)^(n - 1 - k)   (mod 1e9+7, C via Fermat inverse)
    /**
     * time = O(K + log N)
     * space = O(1)
     */
    public int countGoodArrays(int n, int m, int k) {
        // C(n - 1, k) = (n-1)(n-2)...(n-k) / k!
        long numerator = 1;
        long denominator = 1;
        for (int i = 0; i < k; i++) {
            numerator = numerator * (n - 1 - i) % MOD;
            denominator = denominator * (i + 1) % MOD;
        }
        // MOD is prime, so denominator^(MOD - 2) is its modular inverse (Fermat's little theorem)
        long choose = numerator * modPow(denominator, MOD - 2) % MOD;

        long answer = choose * m % MOD;
        answer = answer * modPow(m - 1, n - 1 - k) % MOD;
        return (int) answer;
    }

    /** base^exponent mod MOD by repeated squaring; 0^0 = 1. */
    private long modPow(long base, long exponent) {
        long result = 1;
        base %= MOD;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exponent /= 2;
        }
        return result;
    }
}
