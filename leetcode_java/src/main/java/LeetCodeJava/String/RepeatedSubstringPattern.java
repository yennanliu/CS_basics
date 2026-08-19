package LeetCodeJava.String;

// https://leetcode.com/problems/repeated-substring-pattern/

/**
 *  459. Repeated Substring Pattern
 *  Easy
 *
 *  Given a string s, check if it can be constructed by taking a substring of
 *  it and appending multiple copies of the substring together.
 *
 *  Example 1:
 *    Input: s = "abab"          Output: true   ("ab" twice)
 *  Example 2:
 *    Input: s = "aba"           Output: false
 *  Example 3:
 *    Input: s = "abcabcabcabc"  Output: true   ("abc" four times)
 *
 *  Constraints:
 *    1 <= s.length <= 10^4
 *    s consists of lowercase English letters.
 */
public class RepeatedSubstringPattern {

    // V0
    // IDEA: try every candidate period that divides len, compare each block to the first
    /**
     * time = O(n * sqrt(n)) in practice, O(n^2) worst case
     * space = O(1)
     */
    public boolean repeatedSubstringPattern(String s) {
        if (s == null || s.length() < 2) {
            return false;
        }

        int n = s.length();
        for (int len = 1; len <= n / 2; len++) {
            if (n % len != 0) {
                continue;
            }
            boolean ok = true;
            for (int i = len; i < n; i++) {
                if (s.charAt(i) != s.charAt(i - len)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return true;
            }
        }
        return false;
    }

    // V1
    // IDEA: (s + s) with the first and last char removed still contains s
    //       iff s is built from a repeated block
    /**
     * time = O(n^2)  // indexOf is O(n^2) worst case
     * space = O(n)
     */
    public boolean repeatedSubstringPattern_1(String s) {
        if (s == null || s.length() < 2) {
            return false;
        }
        String doubled = (s + s).substring(1, 2 * s.length() - 1);
        return doubled.contains(s);
    }
}
