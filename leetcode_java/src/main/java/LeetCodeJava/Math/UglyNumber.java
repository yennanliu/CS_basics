package LeetCodeJava.Math;

// https://leetcode.com/problems/ugly-number/

/**
 *  263. Ugly Number
 *  Easy
 *
 *  An ugly number is a positive integer which does not have a prime factor
 *  other than 2, 3, and 5.
 *
 *  Given an integer n, return true if n is an ugly number.
 *
 *  Example 1:
 *    Input: n = 6
 *    Output: true    (6 = 2 * 3)
 *
 *  Example 2:
 *    Input: n = 1
 *    Output: true    (1 has no prime factors)
 *
 *  Example 3:
 *    Input: n = 14
 *    Output: false   (14 = 2 * 7, 7 is not allowed)
 *
 *  Constraints:
 *    -2^31 <= n <= 2^31 - 1
 */
public class UglyNumber {

    // V0
    // IDEA: divide out every factor of 2, 3, 5; ugly iff what remains is 1
    /**
     * time = O(log n)
     * space = O(1)
     */
    public boolean isUgly(int n) {

        if (n <= 0) {
            return false;
        }

        int[] factors = {2, 3, 5};
        for (int f : factors) {
            while (n % f == 0) {
                n /= f;
            }
        }

        return n == 1;
    }
}
