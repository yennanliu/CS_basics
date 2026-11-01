package LeetCodeJava.String;

// https://leetcode.com/problems/reformat-the-string/description/

import java.util.*;

/**
 * 1417. Reformat The String
 * Easy
 *
 * You are given an alphanumeric string s. (Alphanumeric string is a string consisting of lowercase English letters and digits).
 *
 * You have to find a permutation of the string where no letter is followed by another letter and no digit is followed by another digit. That is, no two adjacent characters have the same type.
 *
 * Return the reformatted string or return an empty string if it is impossible to reformat the string.
 *
 *
 * Example 1:
 *
 * Input: s = "a0b1c2"
 * Output: "0a1b2c"
 * Explanation: No two adjacent characters have the same type in "0a1b2c". "a0b1c2", "0a1b2c", "0c2a1b" are also valid permutations.
 *
 * Example 2:
 *
 * Input: s = "leetcode"
 * Output: ""
 * Explanation: "leetcode" has only characters so we cannot separate them by digits.
 *
 * Example 3:
 *
 * Input: s = "1229857369"
 * Output: ""
 * Explanation: "1229857369" has only digits so we cannot separate them by characters.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 500
 * s consists of only lowercase English letters and/or digits.
 *
 */
public class ReformatTheString {

    // V0
    // IDEA: SPLIT BY TYPE + INTERLEAVE, THE LONGER GROUP STARTS
    //       an alternating arrangement exists iff the letter and digit counts differ by at
    //       most 1. put the longer group first and interleave; its extra char lands last.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String reformat(String s) {
        List<Character> letters = new ArrayList<>();
        List<Character> digits = new ArrayList<>();
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                digits.add(c);
            } else {
                letters.add(c);
            }
        }
        if (Math.abs(letters.size() - digits.size()) > 1) {
            return "";
        }

        // longer = the group that must take the first slot
        List<Character> longer = letters;
        List<Character> shorter = digits;
        if (letters.size() < digits.size()) {
            longer = digits;
            shorter = letters;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shorter.size(); i++) {
            sb.append(longer.get(i));
            sb.append(shorter.get(i));
        }
        if (longer.size() > shorter.size()) {
            sb.append(longer.get(longer.size() - 1));
        }
        return sb.toString();
    }
}
