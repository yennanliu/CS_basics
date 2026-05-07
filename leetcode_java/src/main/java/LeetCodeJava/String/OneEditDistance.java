package LeetCodeJava.String;

// https://leetcode.ca/all/161.html
// https://leetcode.com/problems/one-edit-distance/description/

import java.util.Objects;

/**
 * 161. One Edit Distance
 * Given two strings s and t, determine if they are both one edit distance apart.
 *
 * Note:
 *
 * There are 3 possiblities to satisify one edit distance apart:
 *
 * Insert a character into s to get t
 * Delete a character from s to get t
 * Replace a character of s to get t
 * Example 1:
 *
 * Input: s = "ab", t = "acb"
 * Output: true
 * Explanation: We can insert 'c' into s to get t.
 * Example 2:
 *
 * Input: s = "cab", t = "ad"
 * Output: false
 * Explanation: We cannot get t from s by only one step.
 * Example 3:
 *
 * Input: s = "1203", t = "1213"
 * Output: true
 * Explanation: We can replace '0' with '1' to get t.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Microsoft Snapchat Twitter Uber
 * Problem Solution
 * 161-One-Edit-Distance
 *
 *
 */
public class OneEditDistance {

    // V0

    // V1-1
    // IDEA:
    // https://leetcode.ca/2016-05-09-161-One-Edit-Distance/
    // best solution ha!
    public boolean isOneEditDistance_1_1(String s, String t) {
        for (int i = 0; i < Math.min(s.length(), t.length()); ++i) {
            if (s.charAt(i) != t.charAt(i)) {
                if (s.length() == t.length()) {
                    return Objects.equals(s.substring(i + 1), t.substring(i + 1));
                }
                if (s.length() < t.length()) {
                    return Objects.equals(s.substring(i), t.substring(i + 1));
                } else {
                    return Objects.equals(s.substring(i + 1), t.substring(i));
                }
            }
        }

        // case: abc, abcdefg
        return Math.abs((int)s.length() - (int)t.length()) == 1;

    }


    // V1-2
    // IDEA: DP
    // https://leetcode.ca/2016-05-09-161-One-Edit-Distance/
    public boolean isOneEditDistance_1_2(String s, String t) {
        if (s.length() < t.length()) return isOneEditDistance_1_2(t, s); // so 't' is always the shorter one in below logic
        int m = s.length(), n = t.length(), diff = m - n;
        if (diff >= 2) return false;
        else if (diff == 1) {
            for (int i = 0; i < n; ++i) {
                if (s.charAt(i) != t.charAt(i)) {
                    return s.substring(i + 1).equals(t.substring(i));
                }
            }
            return true; // meaning, diff is happening at the final char
        } else {
            int cnt = 0;
            for (int i = 0; i < m; ++i) {
                if (s.charAt(i) != t.charAt(i)) ++cnt;
            }
            return cnt == 1;
        }
    }



    // V1-3
    // IDEA:
    // https://leetcode.ca/2016-05-09-161-One-Edit-Distance/
    public boolean isOneEditDistance_1_3(String s, String t) {
        int m = s.length(), n = t.length();
        if (m < n) {
            return isOneEditDistance_1_3(t, s);
        }
        if (m - n > 1) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            if (s.charAt(i) != t.charAt(i)) {
                if (m == n) {
                    return s.substring(i + 1).equals(t.substring(i + 1));
                }
                return s.substring(i + 1).equals(t.substring(i));
            }
        }
        return m == n + 1;
    }



    // V2





}
