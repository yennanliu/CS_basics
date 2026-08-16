package LeetCodeJava.Math;

// https://leetcode.com/problems/strobogrammatic-number-iii/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 248. Strobogrammatic Number III
 * Hard
 * Lock: Prime
 *
 * Given two strings low and high that represent two integers low and high where
 * low <= high, return the number of strobogrammatic numbers in the range [low, high].
 *
 * A strobogrammatic number is a number that looks the same when rotated 180 degrees
 * (looked at upside down).
 *
 *
 * Example 1:
 *
 * Input: low = "50", high = "100"
 * Output: 3
 *
 * Example 2:
 *
 * Input: low = "0", high = "0"
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= low.length, high.length <= 15
 * low and high consist of only digits.
 * low <= high
 * low and high do not contain any leading zeros except for zero itself.
 *
 */
public class StrobogrammaticNumber3 {

    // V0
    // IDEA: RECURSIVE CONSTRUCTION (build outward from the middle), then filter by range
    /**
     *  A strobogrammatic number is built by WRAPPING a strobogrammatic core with one of
     *  the self-rotating pairs: (0,0) (1,1) (6,9) (8,8) (9,6).
     *
     *  Base cases:
     *    - length 0 -> [""]              (even-length core)
     *    - length 1 -> ["0","1","8"]     (odd-length core, only self-rotating digits)
     *
     *  KEY DETAIL: the ('0','0') pair is FORBIDDEN at the OUTERMOST layer (u == n),
     *  because that would produce a LEADING ZERO.
     *
     *  NOTE !!! low/high can be 15 digits, which OVERFLOWS int -> compare as `long`.
     *
     *  time  = O(5^(L/2) * L), L = high.length
     *  space = O(5^(L/2) * L)
     */

    private static final char[][] PAIRS = {
            { '0', '0' }, { '1', '1' }, { '6', '9' }, { '8', '8' }, { '9', '6' }
    };

    public int strobogrammaticInRange(String low, String high) {
        long lo = Long.parseLong(low);
        long hi = Long.parseLong(high);

        int ans = 0;
        // only lengths between low.length and high.length can possibly land in range
        for (int n = low.length(); n <= high.length(); n++) {
            for (String cand : build(n, n)) {
                long v = Long.parseLong(cand);
                if (lo <= v && v <= hi) {
                    ans += 1;
                }
            }
        }
        return ans;
    }

    /** all strobogrammatic strings of length u, sitting inside a number of length n */
    private List<String> build(int u, int n) {
        if (u == 0) {
            return new ArrayList<>(Arrays.asList(""));
        }
        if (u == 1) {
            return new ArrayList<>(Arrays.asList("0", "1", "8"));
        }

        List<String> res = new ArrayList<>();
        for (String inner : build(u - 2, n)) {
            for (char[] p : PAIRS) {
                /** NOTE !!!
                 *
                 *  outermost layer -> NO leading zero
                 */
                if (p[0] == '0' && u == n) {
                    continue;
                }
                res.add(p[0] + inner + p[1]);
            }
        }
        return res;
    }


    // V1
    // IDEA: COUNT WITHOUT ENUMERATING (combinatorics + a boundary fix-up)
    /**
     *  For a length L strictly between len(low) and len(high) the count is a closed
     *  form: 5^(L/2 - 1) * 4 for even L (the outer pair may not be 0-0), times 3 for
     *  the middle digit when L is odd.
     *
     *  Only the two BOUNDARY lengths still need enumeration, so the work collapses
     *  from 5^(L/2) to a handful of terms plus two boundary scans.
     *
     *  time  = O(L + 5^(len(low)/2) + 5^(len(high)/2))
     *  space = O(5^(L/2))
     */
    public int strobogrammaticInRange_1(String low, String high) {
        long lo = Long.parseLong(low);
        long hi = Long.parseLong(high);
        int lenLo = low.length();
        int lenHi = high.length();

        int total = 0;
        for (int n = lenLo; n <= lenHi; n++) {
            if (n > lenLo && n < lenHi) {
                total += countOfLength(n);      // no boundary interaction
            } else {
                for (String cand : buildAll(n, n)) {
                    long v = Long.parseLong(cand);
                    if (v >= lo && v <= hi) {
                        total += 1;
                    }
                }
            }
        }
        return total;
    }

    /** how many strobogrammatic numbers have exactly n digits (no leading zero) */
    private int countOfLength(int n) {
        if (n == 1) {
            return 3;                    // 0, 1, 8
        }
        int pairs = n / 2;
        int res = 4;                     // the OUTER pair cannot be 0-0
        for (int i = 1; i < pairs; i++) {
            res *= 5;
        }
        if (n % 2 == 1) {
            res *= 3;                    // the middle digit
        }
        return res;
    }

    private static final char[][] PAIRS_V = {
            { '0', '0' }, { '1', '1' }, { '6', '9' }, { '8', '8' }, { '9', '6' } };

    private List<String> buildAll(int u, int n) {
        if (u == 0) {
            return new ArrayList<>(Arrays.asList(""));
        }
        if (u == 1) {
            return new ArrayList<>(Arrays.asList("0", "1", "8"));
        }
        List<String> res = new ArrayList<>();
        for (String inner : buildAll(u - 2, n)) {
            for (char[] p : PAIRS_V) {
                if (p[0] == '0' && u == n) {
                    continue;
                }
                res.add(p[0] + inner + p[1]);
            }
        }
        return res;
    }

    // V2
    // IDEA: ITERATIVE CONSTRUCTION (grow the set two digits at a time)
    /**
     *  Build the length-n strings by starting from the base case and WRAPPING the
     *  whole set, level by level, instead of recursing.
     *
     *  Same enumeration, no call stack, and the intermediate levels are reusable
     *  across the different lengths in the range.
     *
     *  time  = O(5^(L/2) * L)
     *  space = O(5^(L/2) * L)
     */
    public int strobogrammaticInRange_2(String low, String high) {
        long lo = Long.parseLong(low);
        long hi = Long.parseLong(high);

        int total = 0;
        for (int n = low.length(); n <= high.length(); n++) {
            List<String> level = (n % 2 == 0)
                    ? new ArrayList<>(Arrays.asList(""))
                    : new ArrayList<>(Arrays.asList("0", "1", "8"));

            for (int len = (n % 2 == 0) ? 2 : 3; len <= n; len += 2) {
                List<String> next = new ArrayList<>();
                for (String inner : level) {
                    for (char[] p : PAIRS_V) {
                        if (p[0] == '0' && len == n) {
                            continue;      // no leading zero on the OUTERMOST wrap
                        }
                        next.add(p[0] + inner + p[1]);
                    }
                }
                level = next;
            }

            for (String cand : level) {
                if (cand.isEmpty()) {
                    continue;
                }
                long v = Long.parseLong(cand);
                if (v >= lo && v <= hi) {
                    total += 1;
                }
            }
        }
        return total;
    }

    // V3
    // IDEA: BRUTE FORCE -- test every integer in the range
    /**
     *  Rotate each number 180 degrees and compare with itself.
     *
     *  O(hi - lo), so only usable for small ranges, but it checks the DEFINITION
     *  of strobogrammatic rather than relying on the pair construction.
     *
     *  time  = O((hi - lo) * L)
     *  space = O(1)
     */
    public int strobogrammaticInRange_3(String low, String high) {
        long lo = Long.parseLong(low);
        long hi = Long.parseLong(high);
        int total = 0;
        for (long v = lo; v <= hi; v++) {
            if (isStrobo(Long.toString(v))) {
                total += 1;
            }
        }
        return total;
    }

    private boolean isStrobo(String s) {
        int i = 0;
        int j = s.length() - 1;
        while (i <= j) {
            char a = s.charAt(i);
            char b = s.charAt(j);
            boolean ok = (a == '0' && b == '0') || (a == '1' && b == '1')
                    || (a == '8' && b == '8') || (a == '6' && b == '9')
                    || (a == '9' && b == '6');
            if (!ok) {
                return false;
            }
            i += 1;
            j -= 1;
        }
        return true;
    }

}
