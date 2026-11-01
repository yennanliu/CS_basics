package LeetCodeJava.Math;

// https://leetcode.com/problems/three-divisors/description/

/**
 * 1952. Three Divisors
 * Easy
 *
 * Given an integer n, return true if n has exactly three positive divisors. Otherwise, return false.
 *
 * An integer m is a divisor of n if there exists an integer k such that n = k * m.
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: false
 * Explantion: 2 has only two divisors: 1 and 2.
 *
 * Example 2:
 *
 * Input: n = 4
 * Output: true
 * Explantion: 4 has three divisors: 1, 2, and 4.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^4
 *
 */
public class ThreeDivisors {

    // V0
    // IDEA: NUMBER THEORY - exactly 3 divisors <=> n is the SQUARE OF A PRIME ({1, p, p^2})
    /**
     * time = O(sqrt(N))
     * space = O(1)
     */
    public boolean isThree(int n) {
        // integer square root, corrected so we never trust a float near the boundary
        int root = (int) Math.sqrt(n);
        while ((long) root * root > n) {
            root--;
        }
        while ((long) (root + 1) * (root + 1) <= n) {
            root++;
        }

        // not a perfect square, or the root is 1 (n = 1 has one divisor)
        if (root * root != n || root < 2) {
            return false;
        }

        // the root must be prime
        for (int divisor = 2; divisor * divisor <= root; divisor++) {
            if (root % divisor == 0) {
                return false;
            }
        }
        return true;
    }
}
