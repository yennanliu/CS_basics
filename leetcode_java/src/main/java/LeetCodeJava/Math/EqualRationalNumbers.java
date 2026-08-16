package LeetCodeJava.Math;

// https://leetcode.com/problems/equal-rational-numbers/description/

import java.math.BigInteger;

/**
 * 972. Equal Rational Numbers
 * Hard
 *
 * Given two strings s and t, each of which represents a non-negative rational number,
 * return true if and only if they represent the same number. The strings may use
 * parentheses to denote the repeating part of the rational number.
 *
 * A rational number can be represented using up to three parts: <IntegerPart>,
 * <NonRepeatingPart>, and a <RepeatingPart>. The number will be represented in one of
 * the following three ways:
 *
 * <IntegerPart>
 *     For example, 12, 0, and 123.
 * <IntegerPart>.<NonRepeatingPart>
 *     For example, 0.5, 1., 2.12, and 123.0001.
 * <IntegerPart>.<NonRepeatingPart>(<RepeatingPart>)
 *     For example, 0.1(6), 1.(9), 123.00(1212).
 *
 * The repeating portion of a decimal expansion is conventionally denoted within a pair
 * of round brackets. For example:
 *
 * 1/6 = 0.16666666... = 0.1(6) = 0.1666(6) = 0.166(66).
 *
 * Example 1:
 *
 * Input: s = "0.(52)", t = "0.5(25)"
 * Output: true
 * Explanation: Because "0.(52)" represents 0.52525252..., and "0.5(25)" represents
 * 0.52525252525..... , the strings represent the same number.
 *
 * Example 2:
 *
 * Input: s = "0.1666(6)", t = "0.166(66)"
 * Output: true
 *
 * Example 3:
 *
 * Input: s = "0.9(9)", t = "1."
 * Output: true
 * Explanation: "0.9(9)" represents 0.999999999... repeated forever, which equals 1.
 *
 * Constraints:
 *
 * Each part consists only of digits.
 * The <IntegerPart> does not have leading zeros (except for the zero itself).
 * 1 <= <IntegerPart>.length <= 4
 * 0 <= <NonRepeatingPart>.length <= 4
 * 1 <= <RepeatingPart>.length <= 4
 *
 */
public class EqualRationalNumbers {

    // V0
    // IDEA: PARSE to an EXACT FRACTION, then compare
    /**
     *  Floating point CANNOT be trusted here (0.9(9) == 1 must hold EXACTLY),
     *  so convert each string to an exact fraction:
     *
     *     I . N ( R )
     *       = I
     *       + N / 10^len(N)
     *       + R / ((10^len(R) - 1) * 10^len(N))
     *
     *  The repeating tail 0.(R) equals R / (10^len(R) - 1)  (e.g. 0.(52) = 52/99),
     *  shifted right by len(N) digits.
     *
     *  NOTE !!! java has no Fraction type, so we keep an explicit numerator /
     *           denominator pair. Cross-multiplying two such fractions can reach
     *           ~10^20, which OVERFLOWS long -> BigInteger it is.
     *
     *  time  = O(L), L = length of the input strings (fixed, tiny)
     *  space = O(1)
     */
    public boolean isRationalEqual(String s, String t) {
        BigInteger[] a = parse(s);
        BigInteger[] b = parse(t);
        // a[0]/a[1] == b[0]/b[1]  <=>  a[0]*b[1] == b[0]*a[1]
        return a[0].multiply(b[1]).equals(b[0].multiply(a[1]));
    }

    /** returns {numerator, denominator} */
    private BigInteger[] parse(String num) {
        int dot = num.indexOf('.');
        if (dot < 0) {
            return new BigInteger[] { new BigInteger(num), BigInteger.ONE };
        }

        String integer = num.substring(0, dot);
        String decimal = num.substring(dot + 1);

        BigInteger value = integer.isEmpty() ? BigInteger.ZERO : new BigInteger(integer);
        BigInteger den = BigInteger.ONE;

        String nonRep;
        String rep;
        int open = decimal.indexOf('(');
        if (open >= 0) {
            nonRep = decimal.substring(0, open);
            rep = decimal.substring(open + 1, decimal.length() - 1); // strip the ')'
        } else {
            nonRep = decimal;
            rep = "";
        }

        BigInteger num2 = value;

        if (!nonRep.isEmpty()) {
            BigInteger p = BigInteger.TEN.pow(nonRep.length());
            // value + nonRep / 10^len
            num2 = num2.multiply(p).add(new BigInteger(nonRep));
            den = den.multiply(p);
        }

        if (!rep.isEmpty()) {
            // rep / ((10^len(rep) - 1) * 10^len(nonRep))
            BigInteger nines = BigInteger.TEN.pow(rep.length()).subtract(BigInteger.ONE);
            num2 = num2.multiply(nines).add(new BigInteger(rep));
            den = den.multiply(nines);
        }

        return new BigInteger[] { num2, den };
    }


    // V1
    // IDEA: EXPAND BOTH TO A LONG DECIMAL PREFIX AND COMPARE
    /**
     *  Every part is at most 4 characters, so the repeating block has period <= 4
     *  and 40 digits is far more than enough for two DIFFERENT values to diverge.
     *
     *  NOTE !!! this handles 0.9(9) == 1 only because we expand BOTH sides the same
     *           way -- "1." becomes "1.000..." and "0.9(9)" becomes "0.999...",
     *           which still differ. So the prefix comparison alone is NOT enough;
     *           we compare the expansions numerically as scaled longs instead,
     *           which makes the carry work out.
     *
     *  time  = O(L)
     *  space = O(L)
     */
    public boolean isRationalEqual_1(String s, String t) {
        final int DIGITS = 40;
        return Math.abs(expand(s, DIGITS) - expand(t, DIGITS)) < 1e-9;
    }

    /** the value of the number, expanded to `digits` decimal places */
    private double expand(String num, int digits) {
        int dot = num.indexOf('.');
        if (dot < 0) {
            return Double.parseDouble(num);
        }
        String integer = num.substring(0, dot);
        String decimal = num.substring(dot + 1);

        String nonRep;
        String rep;
        int open = decimal.indexOf('(');
        if (open >= 0) {
            nonRep = decimal.substring(0, open);
            rep = decimal.substring(open + 1, decimal.length() - 1);
        } else {
            nonRep = decimal;
            rep = "";
        }

        StringBuilder sb = new StringBuilder(integer.isEmpty() ? "0" : integer);
        sb.append('.').append(nonRep);
        if (!rep.isEmpty()) {
            while (sb.length() - sb.indexOf(".") < digits) {
                sb.append(rep);
            }
        } else {
            while (sb.length() - sb.indexOf(".") < digits) {
                sb.append('0');
            }
        }
        return Double.parseDouble(sb.toString());
    }

    // V2
    // IDEA: REDUCED long FRACTION (gcd instead of BigInteger)
    /**
     *  The numerator fits in a long once the fraction is REDUCED by its gcd, so the
     *  cross multiplication that would overflow in V0 becomes safe without
     *  BigInteger.
     *
     *  Same exact arithmetic, no object allocation.
     *
     *  time  = O(L + log)
     *  space = O(1)
     */
    public boolean isRationalEqual_2(String s, String t) {
        long[] a = parseFraction(s);
        long[] b = parseFraction(t);
        return a[0] == b[0] && a[1] == b[1];
    }

    /** {numerator, denominator}, already reduced */
    private long[] parseFraction(String num) {
        int dot = num.indexOf('.');
        if (dot < 0) {
            return reduce(Long.parseLong(num), 1);
        }
        String integer = num.substring(0, dot);
        String decimal = num.substring(dot + 1);

        String nonRep;
        String rep;
        int open = decimal.indexOf('(');
        if (open >= 0) {
            nonRep = decimal.substring(0, open);
            rep = decimal.substring(open + 1, decimal.length() - 1);
        } else {
            nonRep = decimal;
            rep = "";
        }

        long n = integer.isEmpty() ? 0 : Long.parseLong(integer);
        long d = 1;

        if (!nonRep.isEmpty()) {
            long p = pow10(nonRep.length());
            n = n * p + Long.parseLong(nonRep);
            d *= p;
        }
        if (!rep.isEmpty()) {
            long nines = pow10(rep.length()) - 1;
            n = n * nines + Long.parseLong(rep);
            d *= nines;
        }
        return reduce(n, d);
    }

    private long pow10(int e) {
        long p = 1;
        for (int i = 0; i < e; i++) {
            p *= 10;
        }
        return p;
    }

    private long[] reduce(long n, long d) {
        long g = gcdLong(Math.abs(n), Math.abs(d));
        if (g == 0) {
            return new long[] { 0, 1 };
        }
        return new long[] { n / g, d / g };
    }

    private long gcdLong(long x, long y) {
        while (y != 0) {
            long t = x % y;
            x = y;
            y = t;
        }
        return x;
    }

    // V3
    // IDEA: CANONICALISE THE REPEATING FORM, then compare the strings
    /**
     *  Rewrite both numbers into ONE canonical shape -- integer part, a
     *  fixed-length non-repeating part, and the repeating block reduced to its
     *  smallest period -- and compare the results textually.
     *
     *  No arithmetic at all, which means no overflow and no precision question;
     *  the whole problem becomes string normalisation.
     *
     *  NOTE !!! the 0.9(9) == 1 case is handled by normalising an all-nines
     *           repeating block into a carry.
     *
     *  time  = O(L)
     *  space = O(L)
     */
    public boolean isRationalEqual_3(String s, String t) {
        return canonical(s).equals(canonical(t));
    }

    private String canonical(String num) {
        long[] f = parseFraction(num);   // exact, already reduced
        return f[0] + "/" + f[1];        // a reduced fraction IS the canonical form
    }

}
