package LeetCodeJava.Math;

// https://leetcode.com/problems/valid-number/description/

import java.util.regex.Pattern;
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


    // V1
    // IDEA: SINGLE REGULAR EXPRESSION
    /**
     *  The grammar in the statement translates directly:
     *      [+-]? ( digits (. digits?)? | . digits ) ( [eE] [+-]? digits )?
     *
     *  One pattern, compiled once. The specification IS the code -- and there is no
     *  hand-written state to get wrong.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    private static final Pattern NUMBER = Pattern.compile(
            "[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?");

    public boolean isNumber_1(String s) {
        return NUMBER.matcher(s).matches();
    }

    // V2
    // IDEA: SINGLE PASS WITH FLAGS
    /**
     *  Walk the characters once carrying `seenDigit`, `seenDot`, `seenExp` and
     *  validate each character against them.
     *
     *  No splitting and no substring allocation -- the streaming version you would
     *  use inside a tokenizer that cannot look ahead.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public boolean isNumber_2(String s) {
        boolean seenDigit = false;
        boolean seenDot = false;
        boolean seenExp = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c >= '0' && c <= '9') {
                seenDigit = true;
            } else if (c == '+' || c == '-') {
                // a sign is legal only at the very start or right after e/E
                if (i > 0 && s.charAt(i - 1) != 'e' && s.charAt(i - 1) != 'E') {
                    return false;
                }
            } else if (c == '.') {
                if (seenDot || seenExp) {
                    return false;
                }
                seenDot = true;
            } else if (c == 'e' || c == 'E') {
                if (seenExp || !seenDigit) {
                    return false;
                }
                seenExp = true;
                seenDigit = false;   // the exponent needs its OWN digits
            } else {
                return false;
            }
        }
        return seenDigit;
    }

    // V3
    // IDEA: EXPLICIT DETERMINISTIC FINITE AUTOMATON
    /**
     *  Encode the grammar as a transition table over states
     *  {start, sign, intDigits, dot, fracDigits, dotNoInt, e, expSign, expDigits}
     *  and run the string through it.
     *
     *  The most verbose but also the most auditable: every accepting state is
     *  listed explicitly, so `is "4." valid?` is answered by reading the table
     *  rather than by tracing the flags.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public boolean isNumber_3(String s) {
        // state ids
        final int START = 0;
        final int SIGN = 1;
        final int INT_DIGITS = 2;
        final int DOT_AFTER_INT = 3;
        final int FRAC_DIGITS = 4;
        final int DOT_NO_INT = 5;
        final int EXP = 6;
        final int EXP_SIGN = 7;
        final int EXP_DIGITS = 8;

        int state = START;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int type;
            if (c >= '0' && c <= '9') {
                type = 0;                 // digit
            } else if (c == '+' || c == '-') {
                type = 1;                 // sign
            } else if (c == '.') {
                type = 2;                 // dot
            } else if (c == 'e' || c == 'E') {
                type = 3;                 // exponent
            } else {
                return false;
            }

            switch (state) {
                case START:
                    state = type == 0 ? INT_DIGITS : type == 1 ? SIGN
                            : type == 2 ? DOT_NO_INT : -1;
                    break;
                case SIGN:
                    state = type == 0 ? INT_DIGITS : type == 2 ? DOT_NO_INT : -1;
                    break;
                case INT_DIGITS:
                    state = type == 0 ? INT_DIGITS : type == 2 ? DOT_AFTER_INT
                            : type == 3 ? EXP : -1;
                    break;
                case DOT_AFTER_INT:
                    state = type == 0 ? FRAC_DIGITS : type == 3 ? EXP : -1;
                    break;
                case FRAC_DIGITS:
                    state = type == 0 ? FRAC_DIGITS : type == 3 ? EXP : -1;
                    break;
                case DOT_NO_INT:
                    state = type == 0 ? FRAC_DIGITS : -1;
                    break;
                case EXP:
                    state = type == 0 ? EXP_DIGITS : type == 1 ? EXP_SIGN : -1;
                    break;
                case EXP_SIGN:
                    state = type == 0 ? EXP_DIGITS : -1;
                    break;
                case EXP_DIGITS:
                    state = type == 0 ? EXP_DIGITS : -1;
                    break;
                default:
                    return false;
            }
            if (state == -1) {
                return false;
            }
        }

        // the ACCEPTING states
        return state == INT_DIGITS || state == DOT_AFTER_INT
                || state == FRAC_DIGITS || state == EXP_DIGITS;
    }

}
