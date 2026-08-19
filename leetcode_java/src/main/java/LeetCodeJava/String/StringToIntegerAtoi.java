package LeetCodeJava.String;

// https://leetcode.com/problems/string-to-integer-atoi/

/**
 *  8. String to Integer (atoi)
 *  Medium
 *
 *  Implement the myAtoi(string s) function, which converts a string to a
 *  32-bit signed integer.
 *
 *  The algorithm is as follows:
 *   1. Read in and ignore any leading whitespace.
 *   2. Check if the next character is '-' or '+'. Read it in if it is either.
 *      This determines the sign; assume positive if neither is present.
 *   3. Read in the next characters until the next non-digit character or the
 *      end of the input is reached. The rest of the string is ignored.
 *   4. Convert those digits into an integer ("123" -> 123, "0032" -> 32).
 *      If no digits were read, the integer is 0. Apply the sign.
 *   5. If the integer is out of the 32-bit signed range [-2^31, 2^31 - 1],
 *      clamp it to that range.
 *
 *  Only the space character ' ' counts as whitespace.
 *
 *  Example 1:
 *    Input: s = "42"              Output: 42
 *  Example 2:
 *    Input: s = "   -42"          Output: -42
 *  Example 3:
 *    Input: s = "4193 with words" Output: 4193
 *
 *  Constraints:
 *    0 <= s.length <= 200
 *    s consists of English letters, digits, ' ', '+', '-' and '.'.
 */
public class StringToIntegerAtoi {

    // V0
    // IDEA: scan (skip spaces -> sign -> digits) and clamp with a long-free overflow check
    /**
     * time = O(n)
     * space = O(1)
     */
    public int myAtoi(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int n = s.length();
        int i = 0;

        // 1) skip leading spaces
        while (i < n && s.charAt(i) == ' ') {
            i++;
        }
        if (i == n) {
            return 0;
        }

        // 2) optional sign
        int sign = 1;
        char c = s.charAt(i);
        if (c == '+' || c == '-') {
            sign = (c == '-') ? -1 : 1;
            i++;
        }

        // 3) digits, clamping on overflow
        int res = 0;
        while (i < n) {
            char d = s.charAt(i);
            if (d < '0' || d > '9') {
                break;
            }
            int digit = d - '0';

            // res * 10 + digit would overflow int ?
            if (res > (Integer.MAX_VALUE - digit) / 10) {
                return (sign == 1) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            res = res * 10 + digit;
            i++;
        }

        return sign * res;
    }
}
