package LeetCodeJava.Math;

// https://leetcode.com/problems/fraction-to-recurring-decimal/

import java.util.HashMap;
import java.util.Map;

/**
 *  166. Fraction to Recurring Decimal
 *  Medium
 *
 *  Given two integers representing the numerator and denominator of a fraction,
 *  return the fraction in string format.
 *
 *  If the fractional part is repeating, enclose the repeating part in parentheses.
 *  If multiple answers are possible, return any of them.
 *
 *  It is guaranteed that the length of the answer string is less than 10^4 for
 *  all the given inputs.
 *
 *  Example 1:
 *    Input: numerator = 1, denominator = 2
 *    Output: "0.5"
 *
 *  Example 2:
 *    Input: numerator = 2, denominator = 1
 *    Output: "2"
 *
 *  Example 3:
 *    Input: numerator = 4, denominator = 333
 *    Output: "0.(012)"
 *
 *  Constraints:
 *    -2^31 <= numerator, denominator <= 2^31 - 1
 *    denominator != 0
 */
public class FractionToRecurringDecimal {

    // V0
    // IDEA: long division; remember the position where each remainder first appeared -> that is the cycle start
    /**
     * time = O(d)    (d = denominator, a remainder repeats within |denominator| steps)
     * space = O(d)
     */
    public String fractionToDecimal(int numerator, int denominator) {

        if (numerator == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();

        // sign (use long: -2^31 has no positive int counterpart)
        if ((numerator < 0) ^ (denominator < 0)) {
            sb.append("-");
        }

        long num = Math.abs((long) numerator);
        long den = Math.abs((long) denominator);

        // integer part
        sb.append(num / den);
        long remainder = num % den;
        if (remainder == 0) {
            return sb.toString();
        }

        sb.append(".");

        // remainder -> index in sb where its digit will be written
        Map<Long, Integer> seen = new HashMap<>();
        while (remainder != 0) {
            Integer prev = seen.get(remainder);
            if (prev != null) {
                sb.insert(prev.intValue(), "(");
                sb.append(")");
                break;
            }
            seen.put(remainder, sb.length());

            remainder *= 10;
            sb.append(remainder / den);
            remainder %= den;
        }

        return sb.toString();
    }
}
