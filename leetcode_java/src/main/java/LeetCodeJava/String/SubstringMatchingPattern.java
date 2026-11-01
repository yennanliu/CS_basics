package LeetCodeJava.String;

// https://leetcode.com/problems/substring-matching-pattern/description/

/**
 * 3407. Substring Matching Pattern
 * Easy
 *
 * You are given a string s and a pattern string p, where p contains exactly one
 * '*' character.
 *
 * The '*' in p can be replaced with any sequence of zero or more characters.
 *
 * Return true if p can be made a substring of s, and false otherwise.
 *
 * Example 1:
 *
 * Input: s = "leetcode", p = "ee*e"
 *
 * Output: true
 *
 * Explanation:
 *
 * By replacing the '*' with "tcod", the substring "eetcode" matches the pattern.
 *
 * Example 2:
 *
 * Input: s = "car", p = "c*v"
 *
 * Output: false
 *
 * Explanation:
 *
 * There is no substring matching the pattern.
 *
 * Example 3:
 *
 * Input: s = "luck", p = "u*"
 *
 * Output: true
 *
 * Explanation:
 *
 * The substrings "u", "uc", and "uck" match the pattern.
 *
 * Constraints:
 *
 * 1 <= s.length <= 50
 * 1 <= p.length <= 50
 * s contains only lowercase English letters.
 * p contains only lowercase English letters and exactly one '*'
 *
 */
public class SubstringMatchingPattern {

    // V0
    // IDEA: SPLIT AT '*', ANCHOR THE PREFIX AT EACH START, THEN LOOK FOR THE SUFFIX AFTER IT
    /**
     * time = O(N^2 * M)  (n <= 50, so the direct scan is fine)
     * space = O(N)
     */
    public boolean hasMatch(String s, String p) {
        int starIndex = p.indexOf('*');
        String prefix = p.substring(0, starIndex);
        String suffix = p.substring(starIndex + 1);

        for (int i = 0; i <= s.length(); i++) {
            if (!s.startsWith(prefix, i)) {
                continue;
            }
            // the suffix must start at or after the prefix's end; '*' absorbs the gap
            int afterPrefix = i + prefix.length();
            if (s.indexOf(suffix, afterPrefix) != -1) {
                return true;
            }
        }
        return false;
    }
}
