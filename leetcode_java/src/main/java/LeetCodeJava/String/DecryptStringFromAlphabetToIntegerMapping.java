package LeetCodeJava.String;

// https://leetcode.com/problems/decrypt-string-from-alphabet-to-integer-mapping/description/

/**
 * 1309. Decrypt String from Alphabet to Integer Mapping
 * Easy
 *
 * You are given a string s formed by digits and '#'. We want to map s to English
 * lowercase characters as follows:
 *
 * - Characters ('a' to 'i') are represented by ('1' to '9') respectively.
 * - Characters ('j' to 'z') are represented by ('10#' to '26#') respectively.
 *
 * Return the string formed after mapping.
 *
 * The test cases are generated so that a unique mapping will always exist.
 *
 *
 * Example 1:
 *
 * Input: s = "10#11#12"
 * Output: "jkab"
 * Explanation: "j" -> "10#" , "k" -> "11#" , "a" -> "1" , "b" -> "2".
 *
 * Example 2:
 *
 * Input: s = "1326#"
 * Output: "acz"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consists of digits and the '#' letter.
 * s will be a valid string such that mapping is always possible.
 *
 */
public class DecryptStringFromAlphabetToIntegerMapping {

    // V0
    // IDEA: SIMULATION, SCAN LEFT TO RIGHT AND LOOK AHEAD FOR '#'
    //       s[i] s[i+1] form a two-digit code ("10#".."26#") only when s[i+2] == '#'.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String freqAlphabets(String s) {
        StringBuilder sb = new StringBuilder();
        int n = s.length();
        int i = 0;
        while (i < n) {
            int code;
            if (i + 2 < n && s.charAt(i + 2) == '#') {
                // two digits followed by '#': 'j'..'z'
                code = (s.charAt(i) - '0') * 10 + (s.charAt(i + 1) - '0');
                i += 3;
            } else {
                // a single digit: 'a'..'i'
                code = s.charAt(i) - '0';
                i += 1;
            }
            sb.append((char) ('a' + code - 1));
        }
        return sb.toString();
    }
}
