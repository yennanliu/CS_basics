package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-factorization/

/**
 *  625. Minimum Factorization
 *  Medium
 *
 *  Given a positive integer a, find the smallest positive integer b whose
 *  multiplication of each digit equals to a.
 *
 *  If there is no answer or the answer is not fit in 32-bit signed integer,
 *  then return 0.
 *
 *  Example 1:
 *    Input: a = 48
 *    Output: 68
 *
 *  Example 2:
 *    Input: a = 15
 *    Output: 35
 *
 *  Constraints:
 *   - 1 <= a <= 2^31 - 1
 */
public class MinimumFactorization {

    // V0
    // IDEA: GREEDY -- peel off the BIGGEST digit factor (9 -> 2) first so that
    //       the resulting number has the fewest digits, and place the digits in
    //       ascending order (smaller digit at the more significant position).
    /**
     * time = O(log a)
     * space = O(1)
     */
    public int smallestFactorization(int a) {

        // edge: single digit is already the answer (1 -> 1, ... 9 -> 9)
        if (a < 10) {
            return a;
        }

        long result = 0;
        long mul = 1;
        long x = a;

        for (int d = 9; d >= 2; d--) {
            while (x % d == 0) {
                x /= d;
                // digits are produced 9 -> 2, so each new (smaller) digit goes
                // to a MORE significant position => ascending digit order
                result = mul * d + result;
                mul *= 10;

                if (result > Integer.MAX_VALUE) {
                    return 0;
                }
            }
        }

        // x > 1 means a has a prime factor > 9 => impossible
        if (x != 1) {
            return 0;
        }

        return (int) result;
    }
}
