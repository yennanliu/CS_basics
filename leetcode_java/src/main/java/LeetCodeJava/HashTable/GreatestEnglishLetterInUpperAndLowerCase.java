package LeetCodeJava.HashTable;

// https://leetcode.com/problems/greatest-english-letter-in-upper-and-lower-case/description/

/**
 * 2309. Greatest English Letter in Upper and Lower Case
 * Easy
 *
 * Given a string of English letters s, return the greatest English letter which occurs as both a lowercase and uppercase letter in s. The returned letter should be in uppercase. If no such letter exists, return an empty string.
 *
 * An English letter b is greater than another letter a if b appears after a in the English alphabet.
 *
 *
 * Example 1:
 *
 * Input: s = "lEeTcOdE"
 * Output: "E"
 * Explanation:
 * The letter 'E' is the only letter to appear in both lower and upper case.
 *
 * Example 2:
 *
 * Input: s = "arRAzFif"
 * Output: "R"
 * Explanation:
 * The letter 'R' is the greatest letter to appear in both lower and upper case.
 * Note that 'A' and 'F' also appear in both lower and upper case, but 'R' is greater than 'F' or 'A'.
 *
 * Example 3:
 *
 * Input: s = "AbCdEfGhIjK"
 * Output: ""
 * Explanation:
 * There is no letter that appears in both lower and upper case.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consists of lowercase and uppercase English letters.
 *
 */
public class GreatestEnglishLetterInUpperAndLowerCase {

    // V0
    // IDEA: TWO 26-SLOT PRESENCE ARRAYS, SCAN FROM 'Z' DOWN
    //   record which letters appear in lower case and which in upper case; the answer
    //   is the greatest letter present in both.
    /**
     * time = O(N)
     * space = O(1)
     */
    public String greatestLetter(String s) {
        boolean[] seenLower = new boolean[26];
        boolean[] seenUpper = new boolean[26];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)) {
                seenLower[c - 'a'] = true;
            } else {
                seenUpper[c - 'A'] = true;
            }
        }

        // the first hit scanning downward is the greatest one
        for (int letter = 25; letter >= 0; letter--) {
            if (seenLower[letter] && seenUpper[letter]) {
                return String.valueOf((char) ('A' + letter));
            }
        }
        return "";
    }
}
