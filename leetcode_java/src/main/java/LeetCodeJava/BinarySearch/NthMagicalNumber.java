package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/nth-magical-number/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 878. Nth Magical Number
 * Hard
 *
 * A positive integer is magical if it is divisible by either a or b.
 *
 * Given the three integers n, a, and b, return the nth magical number.
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 1, a = 2, b = 3
 * Output: 2
 *
 * Example 2:
 *
 * Input: n = 4, a = 2, b = 3
 * Output: 6
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 * 2 <= a, b <= 4 * 10^4
 *
 */
public class NthMagicalNumber {

    // V0
    // IDEA: BINARY SEARCH ON ANSWER + INCLUSION-EXCLUSION
    /**
     *   count(x) = how many magical numbers are <= x
     *            = x/a + x/b - x/lcm(a, b)
     *
     *   (the `- x/lcm` term removes the numbers counted TWICE, i.e. those
     *    divisible by BOTH a and b)
     *
     *   count(x) is monotonically non-decreasing, so we binary search the
     *   SMALLEST x with count(x) >= n. That x is itself magical (the count
     *   only increases AT multiples of a or b).
     *
     *   NOTE !!! the search space reaches min(a,b) * n = 4*10^4 * 10^9 = 4*10^13,
     *            which OVERFLOWS int -> everything here must be `long`.
     *
     *   time  = O(log(n * min(a, b)))
     *   space = O(1)
     */
    public int nthMagicalNumber(int n, int a, int b) {
        final long MOD = 1_000_000_007L;

        long lcm = (long) a / gcd(a, b) * b;

        // the n-th magical number is AT MOST n * min(a, b)
        long lo = Math.min(a, b);
        long hi = (long) Math.min(a, b) * n;

        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (mid / a + mid / b - mid / lcm >= n) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        /** NOTE !!!
         *
         *  take the modulo ONLY at the very end
         *  -> the binary search must run on the TRUE value
         */
        return (int) (lo % MOD);
    }

    private long gcd(long x, long y) {
        while (y != 0) {
            long tmp = x % y;
            x = y;
            y = tmp;
        }
        return x;
    }


    // V1
    // IDEA: PERIODICITY -- the pattern repeats every lcm(a, b)
    /**
     *  Inside one period of length L = lcm(a, b) there are exactly
     *      P = L/a + L/b - 1
     *  magical numbers, and the pattern is IDENTICAL in every period.
     *
     *  So jump (n / P) whole periods in O(1) and then walk the remaining
     *  (n % P) entries of one precomputed period.
     *
     *  -> no search at all; the cost is generating one period, O(L/a + L/b).
     *
     *  time  = O(L/a + L/b)
     *  space = O(L/a + L/b)
     */
    public int nthMagicalNumber_1(int n, int a, int b) {
        final long MOD = 1_000_000_007L;
        long lcm = (long) a / gcdV(a, b) * b;

        // the magical numbers inside (0, lcm]
        List<Long> period = new ArrayList<>();
        for (long v = a; v <= lcm; v += a) {
            period.add(v);
        }
        for (long v = b; v <= lcm; v += b) {
            period.add(v);
        }
        Collections.sort(period);
        // lcm itself was added twice
        List<Long> uniq = new ArrayList<>();
        for (long v : period) {
            if (uniq.isEmpty() || uniq.get(uniq.size() - 1) != v) {
                uniq.add(v);
            }
        }

        int p = uniq.size();
        long fullPeriods = (n - 1) / p;
        int idx = (int) ((n - 1) % p);

        long res = fullPeriods % MOD * (lcm % MOD) % MOD;
        res = (res + uniq.get(idx)) % MOD;
        return (int) res;
    }

    private long gcdV(long x, long y) {
        while (y != 0) {
            long t = x % y;
            x = y;
            y = t;
        }
        return x;
    }

    // V2
    // IDEA: TWO POINTER MERGE of the two multiple sequences
    /**
     *  Walk the multiples of a and of b together, emitting the smaller each step
     *  and skipping the duplicates where they coincide.
     *
     *  O(n) so it is useless at n = 10^9, but for small n it produces the whole
     *  ordered sequence, which makes it the natural oracle for the other three.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int nthMagicalNumber_2(int n, int a, int b) {
        final long MOD = 1_000_000_007L;
        long ca = 1;
        long cb = 1;
        long last = 0;

        for (int t = 0; t < n; t++) {
            long va = ca * a;
            long vb = cb * b;
            if (va < vb) {
                last = va;
                ca += 1;
            } else if (vb < va) {
                last = vb;
                cb += 1;
            } else {
                last = va;
                ca += 1;
                cb += 1; // the same number -- consume both
            }
        }

        return (int) (last % MOD);
    }

    // V3
    // IDEA: DENSITY ESTIMATE + LOCAL CORRECTION (no binary search)
    /**
     *  Magical numbers appear with density d = 1/a + 1/b - 1/lcm, so the n-th one
     *  is very close to n / d. Start from that estimate and walk to the exact
     *  answer with a handful of corrections.
     *
     *  Same idea as interpolation search: use the STRUCTURE of the distribution
     *  instead of halving a blind interval.
     *
     *  time  = O(1) amortised (a constant number of corrections)
     *  space = O(1)
     */
    public int nthMagicalNumber_3(int n, int a, int b) {
        final long MOD = 1_000_000_007L;
        long lcm = (long) a / gcdV(a, b) * b;

        double density = 1.0 / a + 1.0 / b - 1.0 / lcm;
        long x = Math.max(1L, (long) (n / density));

        // walk down while we are above the target
        while (countMagical(x, a, b, lcm) >= n) {
            x -= 1;
        }
        // then up to the first x whose count reaches n
        while (countMagical(x, a, b, lcm) < n) {
            x += 1;
        }

        return (int) (x % MOD);
    }

    private long countMagical(long x, long a, long b, long lcm) {
        return x / a + x / b - x / lcm;
    }

}
