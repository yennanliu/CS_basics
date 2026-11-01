package LeetCodeJava.Math;

// https://leetcode.com/problems/alternating-digit-sum/description/

/**
 * 2544. Alternating Digit Sum
 * Easy
 *
 * You are given a positive integer n. Each digit of n has a sign according to the following rules:
 *
 * The most significant digit is assigned a positive sign.
 * Each other digit has an opposite sign to its adjacent digits.
 *
 * Return the sum of all digits with their corresponding sign.
 *
 *
 * Example 1:
 *
 * Input: n = 521
 * Output: 4
 * Explanation: (+5) + (-2) + (+1) = 4.
 *
 * Example 2:
 *
 * Input: n = 111
 * Output: 1
 *
 * Example 3:
 *
 * Input: n = 886996
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 *
 */
public class AlternatingDigitSum {

    // V0
    // IDEA: SIMULATION -- walk the digits LEFT -> RIGHT (via the string), flipping the sign each step
    /**
     * time = O(log N)
     * space = O(log N)
     */
    public int alternateDigitSum(int n) {
        String digits = String.valueOf(n);
        int total = 0;
        // the most significant digit is always '+'
        int sign = 1;
        for (int i = 0; i < digits.length(); i++) {
            total += sign * (digits.charAt(i) - '0');
            sign = -sign;
        }
        return total;
    }
}
