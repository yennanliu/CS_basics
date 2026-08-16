package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/nth-magical-number/description/
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

}
