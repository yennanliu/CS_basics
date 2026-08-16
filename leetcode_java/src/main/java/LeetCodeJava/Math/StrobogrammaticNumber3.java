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

}
