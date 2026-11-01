package LeetCodeJava.Greedy;

// https://leetcode.com/problems/append-characters-to-string-to-make-subsequence/description/

import java.util.*;

/**
 * 2486. Append Characters to String to Make Subsequence
 * Medium
 *
 * You are given two strings s and t consisting of only lowercase English letters.
 *
 * Return the minimum number of characters that need to be appended to the end of s so that t becomes a subsequence of s.
 *
 * A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.
 *
 *
 * Example 1:
 *
 * Input: s = "coaching", t = "coding"
 * Output: 4
 * Explanation: Append the characters "ding" to the end of s so that s = "coachingding".
 * Now, t is a subsequence of s ("coachingding").
 * It can be shown that appending any 3 characters to the end of s will never make t a subsequence.
 *
 * Example 2:
 *
 * Input: s = "abcde", t = "a"
 * Output: 0
 * Explanation: t is already a subsequence of s ("abcde").
 *
 * Example 3:
 *
 * Input: s = "z", t = "abcde"
 * Output: 5
 * Explanation: Append the characters "abcde" to the end of s so that s = "zabcde".
 * Now, t is a subsequence of s ("zabcde").
 * It can be shown that appending any 4 characters to the end of s will never make t a subsequence.
 *
 *
 * Constraints:
 *
 * 1 <= s.length, t.length <= 10^5
 * s and t consist only of lowercase English letters.
 *
 */
public class AppendCharactersToStringToMakeSubsequence {

    // V0
    // IDEA: GREEDY TWO POINTERS (MATCH t's PREFIX IN s AS EARLY AS POSSIBLE)
    //       whatever of t is left unmatched after one pass over s must be appended.
    /**
     * time = O(N + M)
     * space = O(1)
     */
    public int appendCharacters(String s, String t) {
        // number of characters of t already matched as a subsequence of s
        int matched = 0;
        for (int i = 0; i < s.length() && matched < t.length(); i++) {
            if (s.charAt(i) == t.charAt(matched)) {
                matched++;
            }
        }
        return t.length() - matched;
    }
}
