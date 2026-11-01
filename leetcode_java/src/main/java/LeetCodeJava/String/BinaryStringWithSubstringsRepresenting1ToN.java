package LeetCodeJava.String;

// https://leetcode.com/problems/binary-string-with-substrings-representing-1-to-n/description/

import java.util.*;

/**
 * 1016. Binary String With Substrings Representing 1 To N
 * Medium
 *
 * Given a binary string s and a positive integer n, return true if the binary representation of all the integers in the range [1, n] are substrings of s, or false otherwise.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "0110", n = 3
 * Output: true
 *
 * Example 2:
 *
 * Input: s = "0110", n = 4
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s[i] is either '0' or '1'.
 * 1 <= n <= 10^9
 *
 */
public class BinaryStringWithSubstringsRepresenting1ToN {

    // V0
    // IDEA: ENUMERATE THE SUBSTRINGS, NOT THE NUMBERS
    //       n can be 10^9, so looping 1..n is impossible. but every value <= 10^9 fits
    //       in 30 bits, so s holds at most len(s) * 30 useful substrings.
    //       collect every substring that starts with '1' (no leading zero) and whose
    //       value is <= n; since all collected values lie in [1, n],
    //       "every number 1..n is present" is simply seen.size() == n.
    /**
     * time = O(L * 30), L = s.length()
     * space = O(L * 30)
     */
    public boolean queryString(String s, int n) {
        int len = s.length();
        Set<Long> seen = new HashSet<>();

        for (int i = 0; i < len; i++) {
            // a binary representation never starts with '0'
            if (s.charAt(i) == '0') {
                continue;
            }
            long value = 0;
            for (int j = i; j < len; j++) {
                value = value * 2 + (s.charAt(j) - '0');
                // extending further only makes the value bigger
                if (value > n) {
                    break;
                }
                seen.add(value);
            }
        }
        return seen.size() == n;
    }

    // V1
    // IDEA: ONLY CHECK THE UPPER HALF (n/2, n]
    //       if k <= n/2 then 2k <= n, and binary(2k) == binary(k) + "0", so binary(k)
    //       is a substring of binary(2k). checking k in (n/2, n] is therefore enough.
    //       a string of length L has fewer than L substrings of each length, so a huge n
    //       is rejected up front.
    /**
     * time = O(n * L) worst case (n is bounded by the guard)
     * space = O(log n)
     */
    public boolean queryString_1(String s, int n) {
        // s cannot hold that many distinct binary numbers
        if (n > 2L * s.length() * 31) {
            return false;
        }
        for (int k = n; k > n / 2; k--) {
            if (!s.contains(Integer.toBinaryString(k))) {
                return false;
            }
        }
        return true;
    }
}
