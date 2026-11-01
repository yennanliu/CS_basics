package LeetCodeJava.Math;

// https://leetcode.com/problems/count-numbers-with-non-decreasing-digits/description/

import java.math.BigInteger;
import java.util.*;

/**
 * 3519. Count Numbers with Non-Decreasing Digits
 * Hard
 *
 * You are given two integers, l and r, represented as strings, and an integer b.
 * Return the count of integers in the inclusive range [l, r] whose digits are in
 * non-decreasing order when represented in base b.
 *
 * An integer is considered to have non-decreasing digits if, when read from left
 * to right (from the most significant digit to the least significant digit), each
 * digit is greater than or equal to the previous one.
 *
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: l = "23", r = "28", b = 8
 *
 * Output: 3
 *
 * Explanation:
 *
 * The numbers from 23 to 28 in base 8 are: 27, 30, 31, 32, 33, and 34.
 *
 * Out of these, 27, 33, and 34 have non-decreasing digits. Hence, the output is 3.
 *
 * Example 2:
 *
 * Input: l = "2", r = "7", b = 2
 *
 * Output: 2
 *
 * Explanation:
 *
 * The numbers from 2 to 7 in base 2 are: 10, 11, 100, 101, 110, and 111.
 *
 * Out of these, 11 and 111 have non-decreasing digits. Hence, the output is 2.
 *
 * Constraints:
 *
 * 1 <= l.length <= r.length <= 100
 *
 * 2 <= b <= 10
 *
 * l and r consist only of digits.
 *
 * The value represented by l is less than or equal to the value represented by r.
 *
 * l and r do not contain leading zeros.
 *
 */
public class CountNumbersWithNonDecreasingDigits {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: DIGIT DP IN BASE b, WITH STARS-AND-BARS COUNTING THE FREE TAIL
    //       answer = count(r) - count(l - 1), where count(x) = how many of 1..x have
    //       non-decreasing base-b digits. l and r have up to 100 decimal digits -> BigInteger.
    //       key fact: the number of non-decreasing length-t strings over an alphabet of m
    //       symbols is the multiset count C(m + t - 1, t), so once a prefix drops below x the
    //       tail is counted in closed form instead of by a DP table.
    //       count(x) = (numbers shorter than x: every digit >= 1, so C(b - 2 + t, t) of length t)
    //                + (numbers of x's length: walk x's digits, at position i try each digit v
    //                   below x[i] that is >= the previous digit; the remaining places then use
    //                   the b - v digits >= v). if x itself is non-decreasing, it counts too.
    /**
     * time = O(L^2 + L * b)    (L = length of r in base b)
     * space = O(L + b)
     */
    public int countNumbers(String l, String r, int b) {
        BigInteger low = new BigInteger(l);
        BigInteger high = new BigInteger(r);

        // factorial tables, large enough for C(b - 1 + L, L)
        int limit = toBase(high, b).size() + b + 10;
        long[] fact = new long[limit + 1];
        long[] invFact = new long[limit + 1];
        fact[0] = 1;
        for (int i = 1; i <= limit; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        invFact[limit] = modPow(fact[limit], MOD - 2);
        for (int i = limit; i > 0; i--) {
            invFact[i - 1] = invFact[i] * i % MOD;
        }

        long answer = countUpTo(high, b, fact, invFact) - countUpTo(low.subtract(BigInteger.ONE), b, fact, invFact);
        answer = (answer % MOD + MOD) % MOD;
        if (low.signum() == 0) {
            // 0 is a single digit, trivially non-decreasing, but countUpTo starts at 1
            answer = (answer + 1) % MOD;
        }
        return (int) answer;
    }

    /** How many integers in [1, x] have non-decreasing base-b digits, modulo MOD. */
    private long countUpTo(BigInteger x, int b, long[] fact, long[] invFact) {
        if (x.signum() <= 0) {
            return 0;
        }
        List<Integer> digits = toBase(x, b);
        int length = digits.size();
        long result = 0;

        // every shorter length: digits drawn from 1..b-1 (a leading zero is not allowed,
        // and a non-decreasing number whose first digit is >= 1 has all digits >= 1)
        for (int t = 1; t < length; t++) {
            result = (result + multiset(b - 1, t, fact, invFact)) % MOD;
        }

        // same length as x: fix a prefix equal to x, then go strictly below x at position i
        int previous = 1; // the leading digit may not be 0
        boolean xItselfValid = true;
        for (int i = 0; i < length; i++) {
            int current = digits.get(i);
            for (int v = previous; v < current; v++) {
                // the remaining length-1-i places choose from the b - v digits in [v, b-1]
                result = (result + multiset(b - v, length - 1 - i, fact, invFact)) % MOD;
            }
            if (current < previous) {
                // x's own prefix already decreases: no longer prefix can match x
                xItselfValid = false;
                break;
            }
            previous = current;
        }
        if (xItselfValid) {
            result = (result + 1) % MOD;
        }
        return result;
    }

    /** Number of non-decreasing strings of length t over an alphabet of m symbols: C(m + t - 1, t). */
    private long multiset(int m, int t, long[] fact, long[] invFact) {
        return comb(m + t - 1, t, fact, invFact);
    }

    /** Digits of x in base b, most significant first. */
    private List<Integer> toBase(BigInteger x, int b) {
        List<Integer> digits = new ArrayList<>();
        BigInteger base = BigInteger.valueOf(b);
        BigInteger remaining = x;
        if (remaining.signum() == 0) {
            digits.add(0);
            return digits;
        }
        while (remaining.signum() > 0) {
            BigInteger[] quotientAndRemainder = remaining.divideAndRemainder(base);
            digits.add(quotientAndRemainder[1].intValue());
            remaining = quotientAndRemainder[0];
        }
        Collections.reverse(digits);
        return digits;
    }

    /** C(a, c) modulo MOD from the precomputed factorial tables; 0 when c is out of range. */
    private long comb(int a, int c, long[] fact, long[] invFact) {
        if (c < 0 || c > a) {
            return 0;
        }
        return fact[a] * invFact[c] % MOD * invFact[a - c] % MOD;
    }

    /** base^exp modulo MOD by fast exponentiation. */
    private long modPow(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            // the lowest bit of exp decides whether this power of base is used
            if ((exp & 1) == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exp >>= 1;
        }
        return result;
    }
}
