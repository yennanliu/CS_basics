package LeetCodeJava.String;

// https://leetcode.com/problems/longest-nice-substring/description/

import java.util.*;

/**
 * 1763. Longest Nice Substring
 * Easy
 *
 * A string s is nice if, for every letter of the alphabet that s contains, it appears both in uppercase and lowercase. For example, "abABB" is nice because 'A' and 'a' appear, and 'B' and 'b' appear. However, "abA" is not because 'b' appears, but 'B' does not.
 *
 * Given a string s, return the longest substring of s that is nice. If there are multiple, return the substring of the earliest occurrence. If there are none, return an empty string.
 *
 * Example 1:
 *
 * Input: s = "YazaAay"
 * Output: "aAa"
 * Explanation: "aAa" is a nice string because 'A/a' is the only letter of the alphabet in s, and both 'A' and 'a' appear.
 * "aAa" is the longest nice substring.
 *
 * Example 2:
 *
 * Input: s = "Bb"
 * Output: "Bb"
 * Explanation: "Bb" is a nice string because both 'B' and 'b' appear. The whole string is a substring.
 *
 * Example 3:
 *
 * Input: s = "c"
 * Output: ""
 * Explanation: There are no nice substrings.
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s consists of uppercase and lowercase English letters.
 *
 */
public class LongestNiceSubstring {

    // V0
    // IDEA: DIVIDE AND CONQUER ON A "BAD" CHARACTER
    //       a character whose other-case partner is missing from s can never sit
    //       inside a nice substring -> split at it and recurse on both sides,
    //       keeping the longer answer (left wins ties = earliest occurrence).
    //       If every character has its partner, the whole string is nice.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public String longestNiceSubstring(String s) {
        if (s.length() < 2) {
            return "";
        }
        Set<Character> present = new HashSet<>();
        for (char c : s.toCharArray()) {
            present.add(c);
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (present.contains(Character.toUpperCase(c)) && present.contains(Character.toLowerCase(c))) {
                continue;
            }
            // c has no partner: the answer lies entirely left or right of it
            String left = longestNiceSubstring(s.substring(0, i));
            String right = longestNiceSubstring(s.substring(i + 1));
            if (right.length() > left.length()) {
                return right;
            }
            return left;
        }
        return s;
    }

    // V1
    // IDEA: BRUTE FORCE OVER EVERY START, GROWING TWO 26-BIT CASE MASKS
    //       s[start..end] is nice iff the set of lowercase letters equals the set
    //       of uppercase letters; extend end one char at a time and compare masks.
    //       N <= 100, so O(N^2) is fine; keep the first longest one.
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public String longestNiceSubstring_1(String s) {
        int bestStart = 0;
        int bestLength = 0;
        for (int start = 0; start < s.length(); start++) {
            // bit k of lowerMask / upperMask: letter k seen in lower / upper case
            int lowerMask = 0;
            int upperMask = 0;
            for (int end = start; end < s.length(); end++) {
                char c = s.charAt(end);
                if (Character.isLowerCase(c)) {
                    lowerMask |= 1 << (c - 'a');
                } else {
                    upperMask |= 1 << (c - 'A');
                }
                int length = end - start + 1;
                if (lowerMask == upperMask && length > bestLength) {
                    bestStart = start;
                    bestLength = length;
                }
            }
        }
        return s.substring(bestStart, bestStart + bestLength);
    }
}
