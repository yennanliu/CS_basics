package LeetCodeJava.Math;

// https://leetcode.com/problems/convert-to-base-2/description/

/**
 * 1017. Convert to Base -2
 * Medium
 *
 * Given an integer n, return a binary string representing its representation in base -2.
 *
 * Note that the returned string should not have leading zeros unless the string is "0".
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: "110"
 * Explantion: (-2)^2 + (-2)^1 = 2
 *
 * Example 2:
 *
 * Input: n = 3
 * Output: "111"
 * Explantion: (-2)^2 + (-2)^1 + (-2)^0 = 3
 *
 * Example 3:
 *
 * Input: n = 4
 * Output: "100"
 * Explantion: (-2)^2 = 4
 *
 *
 * Constraints:
 *
 * 0 <= n <= 10^9
 *
 */
public class ConvertToBase2 {

    // V0
    // IDEA: REPEATED DIVISION BY -2 (same as base conversion, but fix a negative remainder)
    //   n = q * (-2) + r must keep r in {0, 1}. Java's % can give r = -1; then
    //   r += 2 and q += 1, because q * (-2) + (-1) == (q + 1) * (-2) + 1.
    //   trace n = 2: r=0, n=-1 -> r=-1 => r=1, n=1 -> r=1, n=0 => "110"
    /**
     * time = O(log N)
     * space = O(log N)
     */
    public String baseNeg2(int n) {
        if (n == 0) {
            return "0";
        }

        // digits are collected least significant first
        StringBuilder digits = new StringBuilder();
        while (n != 0) {
            int remainder = n % -2;
            n = n / -2;
            // keep the digit non-negative by borrowing one from the quotient
            if (remainder < 0) {
                remainder += 2;
                n += 1;
            }
            digits.append(remainder);
        }
        return digits.reverse().toString();
    }
}
