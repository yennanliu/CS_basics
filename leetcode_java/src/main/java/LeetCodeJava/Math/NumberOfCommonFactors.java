package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-common-factors/description/

/**
 * 2427. Number of Common Factors
 * Easy
 *
 * Given two positive integers a and b, return the number of common factors of a and b.
 *
 * An integer x is a common factor of a and b if x divides both a and b.
 *
 *
 * Example 1:
 *
 * Input: a = 12, b = 6
 * Output: 4
 * Explanation: The common factors of 12 and 6 are 1, 2, 3, 6.
 *
 * Example 2:
 *
 * Input: a = 25, b = 30
 * Output: 2
 * Explanation: The common factors of 25 and 30 are 1, 5.
 *
 *
 * Constraints:
 *
 * 1 <= a, b <= 1000
 *
 */
public class NumberOfCommonFactors {

    // V0
    // IDEA: THE COMMON FACTORS OF a AND b ARE EXACTLY THE DIVISORS OF gcd(a, b)
    //   count divisors of g by scanning up to sqrt(g): 2 per factor pair, 1 for a perfect square.
    /**
     * time = O(log(min(a, b)) + sqrt(min(a, b)))
     * space = O(1)
     */
    public int commonFactors(int a, int b) {
        // Euclidean algorithm
        int g = a;
        int other = b;
        while (other != 0) {
            int remainder = g % other;
            g = other;
            other = remainder;
        }

        int count = 0;
        for (int d = 1; d * d <= g; d++) {
            if (g % d == 0) {
                // d and g / d form a pair, unless they are the same number
                if (d * d == g) {
                    count += 1;
                } else {
                    count += 2;
                }
            }
        }
        return count;
    }
}
