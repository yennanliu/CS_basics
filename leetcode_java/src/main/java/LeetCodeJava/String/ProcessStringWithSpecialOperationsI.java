package LeetCodeJava.String;

// https://leetcode.com/problems/process-string-with-special-operations-i/description/

/**
 * 3612. Process String with Special Operations I
 * Medium
 *
 * You are given a string s consisting of lowercase English letters and the special
 * characters: *, #, and %.
 *
 * Build a new string result by processing s according to the following rules from
 * left to right:
 *
 * If the letter is a lowercase English letter append it to result.
 * A '*' removes the last character from result, if it exists.
 * A '#' duplicates the current result and appends it to itself.
 * A '%' reverses the current result.
 *
 * Return the final string result after processing all characters in s.
 *
 *
 * Example 1:
 *
 * Input: s = "a#b%*"
 * Output: "ba"
 *
 * Example 2:
 *
 * Input: s = "z*#"
 * Output: ""
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 20
 * s consists of only lowercase English letters and special characters *, #, and %.
 *
 */
public class ProcessStringWithSpecialOperationsI {

    // V0
    // IDEA: DIRECT SIMULATION ON A StringBuilder
    //   the operations are applied strictly left to right, so replaying them is the
    //   definition of the answer. '#' doubles, so with n <= 20 the result stays
    //   under 2^20 characters. (LC 3614 raises the bound and needs index-chasing.)
    /**
     * time = O(2^N)
     * space = O(2^N)
     */
    public String processStr(String s) {
        StringBuilder result = new StringBuilder();
        for (char ch : s.toCharArray()) {
            if (ch == '*') {
                // removing from an empty result is a no-op
                if (result.length() > 0) {
                    result.deleteCharAt(result.length() - 1);
                }
            } else if (ch == '#') {
                // append a copy of itself (copy first: the builder is about to change)
                String copy = result.toString();
                result.append(copy);
            } else if (ch == '%') {
                result.reverse();
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
