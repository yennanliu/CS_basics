package LeetCodeJava.Math;

// https://leetcode.com/problems/check-if-number-is-a-sum-of-powers-of-three/description/

/**
 * 1780. Check if Number is a Sum of Powers of Three
 * Medium
 *
 * Given an integer n, return true if it is possible to represent n as the sum of distinct powers of three. Otherwise, return false.
 *
 * An integer y is a power of three if there exists an integer x such that y == 3^x.
 *
 * Example 1:
 *
 * Input: n = 12
 * Output: true
 * Explanation: 12 = 3^1 + 3^2
 *
 * Example 2:
 *
 * Input: n = 91
 * Output: true
 * Explanation: 91 = 3^0 + 3^2 + 3^4
 *
 * Example 3:
 *
 * Input: n = 21
 * Output: false
 *
 * Constraints:
 *
 * 1 <= n <= 10^7
 *
 */
public class CheckIfNumberIsASumOfPowersOfThree {

    // V0
    // IDEA: BASE-3 REPRESENTATION (distinct powers of 3 <=> every base-3 digit is 0 or 1)
    //   the base-3 digit at position k says how many times 3^k is used; reject on a 2.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public boolean checkPowersOfThree(int n) {
        while (n > 0) {
            if (n % 3 == 2) {
                return false;
            }
            n /= 3;
        }
        return true;
    }
}
