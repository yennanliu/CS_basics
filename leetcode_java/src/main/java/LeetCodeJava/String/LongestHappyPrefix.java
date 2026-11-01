package LeetCodeJava.String;

// https://leetcode.com/problems/longest-happy-prefix/description/

/**
 * 1392. Longest Happy Prefix
 * Hard
 *
 * A string is called a happy prefix if is a non-empty prefix which is also a suffix (excluding itself).
 *
 * Given a string s, return the longest happy prefix of s. Return an empty string "" if no such prefix exists.
 *
 *
 * Example 1:
 *
 * Input: s = "level"
 * Output: "l"
 * Explanation: s contains 4 prefix excluding itself ("l", "le", "lev", "leve"), and suffix ("l", "el", "vel", "evel"). The largest prefix which is also suffix is given by "l".
 *
 * Example 2:
 *
 * Input: s = "ababab"
 * Output: "abab"
 * Explanation: "abab" is the largest prefix which is also suffix. They can overlap in the original string.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s contains only lowercase English letters.
 *
 */
public class LongestHappyPrefix {

    // V0
    // IDEA: KMP FAILURE FUNCTION (LONGEST PROPER PREFIX THAT IS ALSO A SUFFIX)
    //       lps[i] = length of the longest proper prefix of s[0..i] that is also its suffix,
    //       which is exactly a "happy prefix"; the answer is s[0 .. lps[n-1]).
    //       build lps in one pass: matched = current border length; on a mismatch fall back
    //       to lps[matched - 1] (the next best border) instead of restarting from 0.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String longestPrefix(String s) {
        int n = s.length();
        int[] lps = new int[n];
        int matched = 0;

        // i starts at 1, so the border is always proper (never the whole string)
        for (int i = 1; i < n; i++) {
            while (matched > 0 && s.charAt(i) != s.charAt(matched)) {
                matched = lps[matched - 1];
            }
            if (s.charAt(i) == s.charAt(matched)) {
                matched++;
            }
            lps[i] = matched;
        }
        return s.substring(0, lps[n - 1]);
    }
}
