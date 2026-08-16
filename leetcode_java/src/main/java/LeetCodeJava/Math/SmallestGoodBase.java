package LeetCodeJava.Math;

// https://leetcode.com/problems/smallest-good-base/description/
/**
 * 483. Smallest Good Base
 * Hard
 *
 * Given an integer n represented as a string, return the smallest good base of n.
 *
 * We call k >= 2 a good base of n, if all digits of n base k are 1's.
 *
 * Example 1:
 *
 * Input: n = "13"
 * Output: "3"
 * Explanation: 13 base 3 is 111.
 *
 * Example 2:
 *
 * Input: n = "4681"
 * Output: "8"
 * Explanation: 4681 base 8 is 11111.
 *
 * Example 3:
 *
 * Input: n = "1000000000000000000"
 * Output: "999999999999999999"
 * Explanation: 1000000000000000000 base 999999999999999999 is 11.
 *
 * Constraints:
 *
 * n is an integer in the range [3, 10^18].
 * n does not contain any leading zeros.
 *
 */
public class SmallestGoodBase {

    // V0
    // IDEA: ENUMERATE THE NUMBER OF 1's + BINARY SEARCH THE BASE
    /**
     *  `all digits are 1` means:
     *
     *      num = 1 + k + k^2 + ... + k^m       (m + 1 ones in base k)
     *
     *  For a FIXED m the left side is STRICTLY INCREASING in k, so k can be binary
     *  searched. And the MORE ones we use, the SMALLER the base has to be -> try the
     *  LARGEST m first and return the first hit.
     *
     *  Bound on m: the smallest possible base is 2, where num = 2^(m+1) - 1, so
     *  m + 1 <= bitLength(num), i.e. m <= bitLength(num) - 1.
     *
     *  Fallback: every n >= 3 is "11" in base n - 1 (m = 1), so that ALWAYS works.
     *
     *  NOTE !!! num reaches 10^18, so everything is `long`, and the geometric sum
     *           must BAIL OUT as soon as it passes num or it would overflow.
     *
     *  time  = O(log(num)^2 * log(num))
     *  space = O(1)
     */
    public String smallestGoodBase(String n) {
        long num = Long.parseLong(n);

        for (int m = 63 - Long.numberOfLeadingZeros(num); m > 1; m--) {
            long lo = 2;
            long hi = num - 1;
            while (lo < hi) {
                long mid = lo + (hi - lo) / 2;
                if (total(mid, m, num) >= num) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            if (total(lo, m, num) == num) {
                return String.valueOf(lo);
            }
        }

        // m == 1 : num = 1 + (num - 1)  ->  "11" in base num - 1
        return String.valueOf(num - 1);
    }

    /** 1 + k + k^2 + ... + k^m , bailing out as soon as we pass num */
    private long total(long k, int m, long num) {
        long s = 1;
        long p = 1;
        for (int i = 0; i < m; i++) {
            /** NOTE !!!
             *
             *  the early bail-out is what keeps `p * k` from OVERFLOWING --
             *  without it, k^m for a large k would wrap around and break the
             *  monotonicity the binary search relies on
             */
            if (p > (num - s) / k) {
                return num + 1; // definitely bigger than num
            }
            p *= k;
            s += p;
            if (s > num) {
                return s;
            }
        }
        return s;
    }

}
