package LeetCodeJava.Math;

// https://leetcode.com/problems/valid-number/description/
/**
 * 65. Valid Number
 * Hard
 *
 * Given a string s, return whether s is a valid number.
 *
 * For example, all the following are valid numbers:
 * "2", "0089", "-0.1", "+3.14", "4.", "-.9", "2e10", "-90E3", "3e+7", "+6e-1",
 * "53.5e93", "-123.456e789",
 * while the following are not valid numbers:
 * "abc", "1a", "1e", "e3", "99e2.5", "--6", "-+3", "95a54e53".
 *
 * Formally, a valid number is defined using one of the following definitions:
 *
 * 1. An integer number followed by an optional exponent.
 * 2. A decimal number followed by an optional exponent.
 *
 * An integer number is defined with an optional sign '-' or '+' followed by digits.
 *
 * A decimal number is defined with an optional sign '-' or '+' followed by one of the
 * following definitions:
 *
 * 1. Digits followed by a dot '.'.
 * 2. Digits followed by a dot '.' followed by digits.
 * 3. A dot '.' followed by digits.
 *
 * An exponent is defined with an exponent notation 'e' or 'E' followed by an integer
 * number.
 *
 * The digits are defined as one or more digits.
 *
 *
 * Example 1:
 *
 * Input: s = "0"
 * Output: true
 *
 * Example 2:
 *
 * Input: s = "e"
 * Output: false
 *
 * Example 3:
 *
 * Input: s = "."
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 20
 * s consists of only English letters (both uppercase and lowercase), digits (0-9),
 * plus '+', minus '-', or dot '.'.
 *
 */
public class ValidNumber {

    // V0
    // IDEA: SPLIT ON 'e'/'E' -> validate `mantissa` + `exponent` separately
    /**
     *  the MANTISSA must be an integer OR a decimal, the EXPONENT must be an integer
     *
     *  NOTE !!! only the FIRST 'e' splits; a SECOND 'e' lands inside `exp` and
     *           correctly fails isInteger (e.g. "1e2e3").
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public boolean isNumber(String s) {
        String lower = s.toLowerCase();
        int i = lower.indexOf('e');

        if (i >= 0) {
            String mantissa = s.substring(0, i);
            String exp = s.substring(i + 1);
            return (isInteger(mantissa) || isDecimal(mantissa)) && isInteger(exp);
        }

        return isInteger(s) || isDecimal(s);
    }

    /** non-empty and EVERY char is 0-9 */
    private boolean allDigits(String t) {
        /** NOTE !!!
         *
         *  do NOT use Character.isDigit() -- it also accepts unicode digits
         */
        if (t.isEmpty()) {
            return false;
        }
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /** [+|-] digits */
    private boolean isInteger(String t) {
        if (!t.isEmpty() && (t.charAt(0) == '+' || t.charAt(0) == '-')) {
            t = t.substring(1);
        }
        return allDigits(t);
    }

    /** [+|-] ( digits "." | digits "." digits | "." digits ) */
    private boolean isDecimal(String t) {
        if (!t.isEmpty() && (t.charAt(0) == '+' || t.charAt(0) == '-')) {
            t = t.substring(1);
        }

        int dots = 0;
        int dotIdx = -1;
        for (int i = 0; i < t.length(); i++) {
            if (t.charAt(i) == '.') {
                dots += 1;
                dotIdx = i;
            }
        }
        if (dots != 1) {
            return false;
        }

        String left = t.substring(0, dotIdx);
        String right = t.substring(dotIdx + 1);

        // at least ONE side must carry digits (so "." alone is invalid)
        if (left.isEmpty() && right.isEmpty()) {
            return false;
        }
        if (!left.isEmpty() && !allDigits(left)) {
            return false;
        }
        if (!right.isEmpty() && !allDigits(right)) {
            return false;
        }
        return true;
    }

}
