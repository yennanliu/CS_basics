package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-digit-one/description/
/**
 * 233. Number of Digit One
 * Hard
 *
 * Given an integer n, count the total number of digit 1 appearing in all non-negative
 * integers less than or equal to n.
 *
 *
 * Example 1:
 *
 * Input: n = 13
 * Output: 6
 *
 * Example 2:
 *
 * Input: n = 0
 * Output: 0
 *
 *
 * Constraints:
 *
 * 0 <= n <= 10^9
 *
 */
public class NumberOfDigitOne {

    // V0
    // IDEA: MATH - count the 1s contributed by each digit POSITION independently
    /**
     *  Split n around the position with place value i (1, 10, 100, ...):
     *      high = n / (i * 10),  cur = (n / i) % 10,  low = n % i
     *
     *  How many numbers in [0, n] have digit 1 AT THIS POSITION?
     *    - cur == 0 : high * i
     *         the position can only be 1 while high is STRICTLY below `high`
     *    - cur == 1 : high * i + low + 1
     *         same as above, PLUS the partial block capped by the low digits
     *    - cur >  1 : (high + 1) * i
     *         the whole block with prefix == high ALSO counts
     *
     *  NOTE !!! `i * 10` reaches 10^10 for n near 10^9, which OVERFLOWS int
     *           -> the place value and the running count are `long`.
     *
     *  time  = O(log n)
     *  space = O(1)
     */
    public int countDigitOne(int n) {
        if (n <= 0) {
            return 0;
        }

        long count = 0;
        long i = 1; // current place value: 1, 10, 100, ...

        while (i <= n) {
            long high = n / (i * 10);
            long cur = (n / i) % 10;
            long low = n % i;

            if (cur == 0) {
                count += high * i;
            } else if (cur == 1) {
                count += high * i + low + 1;
            } else {
                count += (high + 1) * i;
            }

            i *= 10;
        }

        return (int) count;
    }


    // V1
    // IDEA: DIGIT DP (memoised over position, tight flag, ones so far)
    /**
     *  The mechanical `count digits under a bound` template: walk the decimal
     *  digits deciding each one, carrying whether the prefix is still equal to n's
     *  prefix.
     *
     *  Slower than the closed form but it generalises immediately -- swap the
     *  counted digit, or count numbers with a property, and nothing else changes.
     *
     *  time  = O(len * 2 * len * 10)
     *  space = O(len * 2 * len)
     */
    public int countDigitOne_1(int n) {
        if (n <= 0) {
            return 0;
        }
        char[] digits = String.valueOf(n).toCharArray();
        Integer[][][] memo = new Integer[digits.length][2][digits.length + 1];
        return digitDp(digits, 0, 1, 0, memo);
    }

    private int digitDp(char[] digits, int pos, int tight, int ones, Integer[][][] memo) {
        if (pos == digits.length) {
            return ones;
        }
        if (memo[pos][tight][ones] != null) {
            return memo[pos][tight][ones];
        }
        int limit = tight == 1 ? digits[pos] - '0' : 9;
        int total = 0;
        for (int d = 0; d <= limit; d++) {
            total += digitDp(digits, pos + 1,
                    (tight == 1 && d == limit) ? 1 : 0,
                    ones + (d == 1 ? 1 : 0), memo);
        }
        memo[pos][tight][ones] = total;
        return total;
    }

    // V2
    // IDEA: BRUTE FORCE -- count the 1s in every number up to n
    /**
     *  O(n log n), so it only works for small n, but it is the definition and thus
     *  the oracle for the closed form.
     *
     *  time  = O(n log n)
     *  space = O(1)
     */
    public int countDigitOne_2(int n) {
        int total = 0;
        for (int v = 1; v <= n; v++) {
            int x = v;
            while (x > 0) {
                if (x % 10 == 1) {
                    total += 1;
                }
                x /= 10;
            }
        }
        return total;
    }

    // V3
    // IDEA: RECURSIVE COUNT ON THE LEADING DIGIT
    /**
     *  Let n have d digits with leading digit `high` and remainder `rest`. Then
     *
     *      f(n) = high * f(10^(d-1) - 1)                    (the blocks below the top)
     *           + (high > 1 ? 10^(d-1) : rest + 1)          (the 1s in the top position)
     *           + f(rest)                                   (inside the top block)
     *
     *  A clean divide-and-conquer over the leading digit rather than a sweep over
     *  place values.
     *
     *  time  = O(log n)
     *  space = O(log n)
     */
    public int countDigitOne_3(int n) {
        return (int) countRec(n);
    }

    private long countRec(long n) {
        if (n <= 0) {
            return 0;
        }
        if (n < 10) {
            return 1;   // exactly the number 1
        }

        long pow = 1;
        int d = 0;
        while (pow * 10 <= n) {
            pow *= 10;
            d += 1;
        }
        long high = n / pow;
        long rest = n % pow;

        // ones contributed inside each full lower block
        long inner = high * countRec(pow - 1);
        // ones contributed by the LEADING position itself
        long lead = high > 1 ? pow : rest + 1;
        return inner + lead + countRec(rest);
    }

}
