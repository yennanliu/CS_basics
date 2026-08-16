package LeetCodeJava.Math;

// https://leetcode.com/problems/smallest-good-base/description/

import java.math.BigInteger;
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


    // V1
    // IDEA: ESTIMATE THE BASE BY THE m-th ROOT, then verify
    /**
     *  For a fixed number of ones m + 1, num is roughly k^m, so
     *  k is approximately num^(1/m). Rounding that estimate and checking a couple
     *  of neighbours replaces the whole binary search.
     *
     *  -> O(log num) work per m instead of O(log num * log num).
     *
     *  time  = O(log(num)^2)
     *  space = O(1)
     */
    public String smallestGoodBase_1(String n) {
        long num = Long.parseLong(n);

        for (int m = 63 - Long.numberOfLeadingZeros(num); m > 1; m--) {
            long k = (long) Math.pow(num, 1.0 / m);
            // the pow estimate can be off by one either way
            for (long cand = Math.max(2, k - 1); cand <= k + 1; cand++) {
                if (geomSum(cand, m, num) == num) {
                    return String.valueOf(cand);
                }
            }
        }
        return String.valueOf(num - 1);
    }

    /** 1 + k + ... + k^m, capped so it never overflows */
    private long geomSum(long k, int m, long num) {
        long s = 1;
        long p = 1;
        for (int i = 0; i < m; i++) {
            if (p > (num - s) / k) {
                return num + 1;
            }
            p *= k;
            s += p;
            if (s > num) {
                return s;
            }
        }
        return s;
    }

    // V2
    // IDEA: BigInteger ARITHMETIC (no overflow guards at all)
    /**
     *  The awkward part of V0 is proving that k^m never wraps. Doing the geometric
     *  sum in BigInteger removes that obligation entirely -- the bail-out exists
     *  only as a speed optimisation, not for correctness.
     *
     *  Slower per operation, but the code says exactly what it means.
     *
     *  time  = O(log(num)^2 * bigint cost)
     *  space = O(1)
     */
    public String smallestGoodBase_2(String n) {
        BigInteger num = new BigInteger(n);

        for (int m = num.bitLength() - 1; m > 1; m--) {
            BigInteger lo = BigInteger.valueOf(2);
            BigInteger hi = num.subtract(BigInteger.ONE);
            while (lo.compareTo(hi) < 0) {
                BigInteger mid = lo.add(hi).divide(BigInteger.TWO);
                if (geomBig(mid, m, num).compareTo(num) >= 0) {
                    hi = mid;
                } else {
                    lo = mid.add(BigInteger.ONE);
                }
            }
            if (geomBig(lo, m, num).equals(num)) {
                return lo.toString();
            }
        }
        return num.subtract(BigInteger.ONE).toString();
    }

    private BigInteger geomBig(BigInteger k, int m, BigInteger cap) {
        BigInteger s = BigInteger.ONE;
        BigInteger p = BigInteger.ONE;
        for (int i = 0; i < m; i++) {
            p = p.multiply(k);
            s = s.add(p);
            if (s.compareTo(cap) > 0) {
                return s;   // already too big
            }
        }
        return s;
    }

    // V3
    // IDEA: BRUTE FORCE over the base
    /**
     *  Try k = 2, 3, 4, ... and check whether num is all ones in that base.
     *
     *  O(num^(1/2)) at best -- fine for small inputs, hopeless at 10^18 -- but it
     *  scans the bases in ASCENDING order, so the first hit is trivially the
     *  smallest, with no reasoning about m required.
     *
     *  time  = O(sqrt(num) * log num)
     *  space = O(1)
     */
    public String smallestGoodBase_3(String n) {
        long num = Long.parseLong(n);
        for (long k = 2; k * k <= num; k++) {
            long x = num;
            boolean allOnes = true;
            while (x > 0) {
                if (x % k != 1) {
                    allOnes = false;
                    break;
                }
                x /= k;
            }
            if (allOnes) {
                return String.valueOf(k);
            }
        }
        return String.valueOf(num - 1);
    }

}
