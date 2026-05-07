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
//    public boolean isOneEditDistance(String s, String t) {
//    }



    // V0-1
    // IDEA: 2 POINTERS (fixed by gpt)
    // TODO: validate
    public boolean isOneEditDistance_0_1(String s, String t) {

        // equal strings -> 0 edits
        if (s.equals(t)) {
            return false;
        }

        int len_s = s.length();
        int len_t = t.length();

        // length difference > 1 impossible
        if (Math.abs(len_s - len_t) > 1) {
            return false;
        }

        // ensure s is shorter (or equal)
        if (len_s > len_t) {
            String tmp = s;
            s = t;
            t = tmp;
        }

        len_s = s.length();
        len_t = t.length();

        boolean isLenEquals = (len_s == len_t);

        int i = 0;
        int j = 0;

        int op = 0;

        while (i < len_s && j < len_t) {

            if (s.charAt(i) == t.charAt(j)) {
                i++;
                j++;
            } else {

                op++;

                if (op > 1) {
                    return false;
                }

                // replace
                if (isLenEquals) {
                    i++;
                    j++;
                }
                // insert into shorter string
                else {
                    j++;
                }
            }
        }

        // leftover character
        if (j < len_t || i < len_s) {
            op++;
        }

        return op == 1;
    }


    // V0-2
    // IDEA: 2 POINTERS (fixed by gemini)
    // TODO: validate
    public boolean isOneEditDistance_0_2(String s, String t) {
        int ns = s.length();
        int nt = t.length();

        // Ensure s is always the shorter string
        if (ns > nt) {
            return isOneEditDistance_0_2(t, s);
        }

        // If length difference is more than 1, they can't be one edit apart
        if (nt - ns > 1) {
            return false;
        }

        for (int i = 0; i < ns; i++) {
            if (s.charAt(i) != t.charAt(i)) {
                // If lengths are the same, they must be equal after this character (Replace)
                if (ns == nt) {
                    return s.substring(i + 1).equals(t.substring(i + 1));
                } else {
                    // If lengths are different, s must match t starting from the next char of t (Insert)
                    // s.substring(i) because we haven't consumed s.charAt(i) yet
                    return s.substring(i).equals(t.substring(i + 1));
                }
            }
        }

        // If we get here, all characters of s matched the prefix of t.
        // They are one edit apart only if t has exactly one more character at the end.
        return (ns + 1 == nt);
    }



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
