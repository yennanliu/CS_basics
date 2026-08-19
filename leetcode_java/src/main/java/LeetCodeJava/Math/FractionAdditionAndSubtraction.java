package LeetCodeJava.Math;

// https://leetcode.com/problems/fraction-addition-and-subtraction/

/**
 *  592. Fraction Addition and Subtraction
 *  Medium
 *
 *  Given a string expression representing an expression of fraction addition and
 *  subtraction, return the calculation result in string format.
 *
 *  The final result should be an irreducible fraction. If your final result is an
 *  integer, change it to the format of a fraction that has a denominator 1. So in
 *  this case, 2 should be converted to "2/1".
 *
 *  Example 1:
 *    Input: expression = "-1/2+1/2"
 *    Output: "0/1"
 *
 *  Example 2:
 *    Input: expression = "-1/2+1/2+1/3"
 *    Output: "1/3"
 *
 *  Constraints:
 *   - The input string only contains '0' to '9', '/', '+' and '-'.
 *   - Each fraction (input and output) has the format +/-numerator/denominator.
 *     If the first input fraction or the output is positive, then '+' will be omitted.
 *   - The input only contains valid irreducible fractions, where the numerator and
 *     denominator of each fraction will always be in the range [1, 10].
 *   - The number of given fractions will be in the range [1, 10].
 *   - The value of the final result is guaranteed to be in the range [-10^4, 10^4].
 */
public class FractionAdditionAndSubtraction {

    // V0
    // IDEA: SCAN THE STRING, ACCUMULATE (num/den) VIA CROSS MULTIPLICATION + GCD REDUCE
    /**
     * time = O(n * log m), n = expression length, m = max intermediate value
     * space = O(1)
     */
    public String fractionAddition(String expression) {

        long num = 0;   // running numerator
        long den = 1;   // running denominator (always > 0)

        int i = 0;
        int len = expression.length();

        while (i < len) {

            // 1) sign
            long sign = 1;
            if (expression.charAt(i) == '+') {
                i++;
            } else if (expression.charAt(i) == '-') {
                sign = -1;
                i++;
            }

            // 2) numerator
            long a = 0;
            while (i < len && Character.isDigit(expression.charAt(i))) {
                a = a * 10 + (expression.charAt(i) - '0');
                i++;
            }
            a *= sign;

            // 3) skip '/'
            i++;

            // 4) denominator
            long b = 0;
            while (i < len && Character.isDigit(expression.charAt(i))) {
                b = b * 10 + (expression.charAt(i) - '0');
                i++;
            }

            // 5) num/den + a/b
            num = num * b + a * den;
            den = den * b;

            long g = gcd(Math.abs(num), den);
            if (g != 0) {
                num /= g;
                den /= g;
            }
        }

        if (num == 0) {
            return "0/1";
        }
        return num + "/" + den;
    }

    private long gcd(long a, long b) {
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}
