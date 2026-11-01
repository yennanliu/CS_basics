package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/check-if-a-string-contains-all-binary-codes-of-size-k/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 1461. Check If a String Contains All Binary Codes of Size K
 * Medium
 *
 * Given a binary string s and an integer k, return true if every binary code of
 * length k is a substring of s. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: s = "00110110", k = 2
 * Output: true
 * Explanation: The binary codes of length 2 are "00", "01", "10" and "11". They can
 * be all found as substrings at indices 0, 1, 3 and 2 respectively.
 *
 * Example 2:
 *
 * Input: s = "0110", k = 1
 * Output: true
 * Explanation: The binary codes of length 1 are "0" and "1", it is clear that both
 * exist as a substring.
 *
 * Example 3:
 *
 * Input: s = "0110", k = 2
 * Output: false
 * Explanation: The binary code "00" is of length 2 and does not exist in the array.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 5 * 10^5
 * s[i] is either '0' or '1'.
 * 1 <= k <= 20
 *
 */
public class CheckIfAStringContainsAllBinaryCodesOfSizeK {

    // V0
    // IDEA: SLIDING WINDOW, WINDOW KEPT AS AN INTEGER + SEEN ARRAY (count the codes, do not build them)
    // there are exactly 2^k codes of length k; the answer is whether the n - k + 1 windows cover all of them.
    // the window is an int: shift left, drop in the new bit, mask off the bit that fell out the top.
    /**
     * time = O(N)
     * space = O(2^K)
     */
    public boolean hasAllCodes(String s, int k) {
        int n = s.length();
        // number of distinct codes of length k
        int need = 1 << k;

        // pigeonhole: n - k + 1 windows cannot cover 2^k distinct codes
        if (n - k + 1 < need) {
            return false;
        }

        // mask keeps only the lowest k bits, i.e. the current window
        int mask = need - 1;
        boolean[] seen = new boolean[need];
        int seenCount = 0;
        int window = 0;

        for (int i = 0; i < n; i++) {
            int bit = (s.charAt(i) == '1') ? 1 : 0;
            // slide: shift the window left by one, add the new bit, drop the oldest bit
            window = ((window << 1) | bit) & mask;

            // a full window of length k ends at index i once i >= k - 1
            if (i >= k - 1 && !seen[window]) {
                seen[window] = true;
                seenCount += 1;
                // every code found -> stop early
                if (seenCount == need) {
                    return true;
                }
            }
        }
        return seenCount == need;
    }

    // V1
    // IDEA: HASHSET OF EVERY LENGTH-k SUBSTRING (simplest to say; slower, builds a string per window)
    /**
     * time = O(N * K)
     * space = O(N * K)
     */
    public boolean hasAllCodes_1(String s, int k) {
        int need = 1 << k;
        // pigeonhole: too few windows to cover every code
        if (s.length() - k + 1 < need) {
            return false;
        }

        Set<String> codes = new HashSet<>();
        for (int start = 0; start + k <= s.length(); start++) {
            codes.add(s.substring(start, start + k));
            if (codes.size() == need) {
                return true;
            }
        }
        return codes.size() == need;
    }
}
