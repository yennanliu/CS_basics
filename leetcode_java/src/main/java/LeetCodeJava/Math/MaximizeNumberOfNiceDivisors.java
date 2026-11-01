package LeetCodeJava.Math;

// https://leetcode.com/problems/maximize-number-of-nice-divisors/description/

/**
 * 1808. Maximize Number of Nice Divisors
 * Hard
 *
 * You are given a positive integer primeFactors. You are asked to construct a positive integer n that satisfies the following conditions:
 *
 * The number of prime factors of n (not necessarily distinct) is at most primeFactors.
 * The number of nice divisors of n is maximized. Note that a divisor of n is nice if it is divisible by every prime factor of n. For example, if n = 12, then its prime factors are [2,2,3], then 6 and 12 are nice divisors, while 3 and 4 are not.
 *
 * Return the number of nice divisors of n. Since that number can be too large, return it modulo 10^9 + 7.
 *
 * Note that a prime number is a natural number greater than 1 that is not a product of two smaller natural numbers. The prime factors of a number n is a list of prime numbers such that their product equals n.
 *
 *
 * Example 1:
 *
 * Input: primeFactors = 5
 * Output: 6
 * Explanation: 200 is a valid value of n.
 * It has 5 prime factors: [2,2,2,5,5], and it has 6 nice divisors: [10,20,40,50,100,200].
 * There is not other value of n that has at most 5 prime factors and more nice divisors.
 *
 * Example 2:
 *
 * Input: primeFactors = 8
 * Output: 18
 *
 *
 * Constraints:
 *
 * 1 <= primeFactors <= 10^9
 *
 */
public class MaximizeNumberOfNiceDivisors {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: INTEGER BREAK (maximize a product of parts summing to primeFactors)
    //   n = p1^a1 * p2^a2 * ...; a nice divisor contains each pi 1..ai times -> count = a1 * a2 * ...
    //   so split primeFactors into positive parts with max product: use as many 3s as possible.
    //   r = primeFactors % 3: r == 0 -> 3^q; r == 1 -> 4 * 3^(q-1); r == 2 -> 2 * 3^q.
    //   primeFactors <= 3 is its own answer. primeFactors goes to 10^9 -> fast modular pow.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int maxNiceDivisors(int primeFactors) {
        if (primeFactors <= 3) {
            return primeFactors;
        }
        int threes = primeFactors / 3;
        int remainder = primeFactors % 3;

        if (remainder == 0) {
            return (int) modPow(3, threes);
        }
        if (remainder == 1) {
            // a 3 + 1 is worse than 2 + 2, so trade one 3 for a 4
            return (int) (4 * modPow(3, threes - 1) % MOD);
        }
        return (int) (2 * modPow(3, threes) % MOD);
    }

    /** base^exponent mod MOD by repeated squaring. */
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
