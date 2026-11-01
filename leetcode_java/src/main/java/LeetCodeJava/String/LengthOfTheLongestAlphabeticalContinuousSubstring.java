package LeetCodeJava.String;

// https://leetcode.com/problems/length-of-the-longest-alphabetical-continuous-substring/description/

/**
 * 2414. Length of the Longest Alphabetical Continuous Substring
 * Medium
 *
 * An alphabetical continuous string is a string consisting of consecutive letters in the alphabet. In other words, it is any substring of the string "abcdefghijklmnopqrstuvwxyz".
 *
 * For example, "abc" is an alphabetical continuous string, while "acb" and "za" are not.
 *
 * Given a string s consisting of lowercase letters only, return the length of the longest alphabetical continuous substring.
 *
 *
 * Example 1:
 *
 * Input: s = "abacaba"
 * Output: 2
 * Explanation: There are 4 distinct continuous substrings: "a", "b", "c" and "ab".
 * "ab" is the longest continuous substring.
 *
 * Example 2:
 *
 * Input: s = "abcde"
 * Output: 5
 * Explanation: "abcde" is the longest continuous substring.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of only English lowercase letters.
 *
 */
public class LengthOfTheLongestAlphabeticalContinuousSubstring {

    // V0
    // IDEA: RUN LENGTH OF "EACH CHARACTER IS THE PREVIOUS ONE PLUS 1"
    //       the run extends when s[i] == s[i - 1] + 1 and restarts at 1
    //       otherwise ('z' -> 'a' is not continuous, and the test rejects it)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int longestContinuousSubstring(String s) {
        int longest = 1;
        int run = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1) + 1) {
                run++;
            } else {
                run = 1;
            }
            longest = Math.max(longest, run);
        }
        return longest;
    }
}
