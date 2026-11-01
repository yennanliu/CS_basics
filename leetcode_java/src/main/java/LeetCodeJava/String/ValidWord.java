package LeetCodeJava.String;

// https://leetcode.com/problems/valid-word/description/

/**
 * 3136. Valid Word
 * Easy
 *
 * A word is considered valid if:
 *
 * It contains a minimum of 3 characters.
 * It contains only digits (0-9), and English letters (uppercase and lowercase).
 * It includes at least one vowel.
 * It includes at least one consonant.
 *
 * You are given a string word.
 *
 * Return true if word is valid, otherwise, return false.
 *
 * Notes:
 *
 * 'a', 'e', 'i', 'o', 'u', and their uppercases are vowels.
 * A consonant is an English letter that is not a vowel.
 *
 *
 * Example 1:
 *
 * Input: word = "234Adas"
 * Output: true
 * Explanation:
 * This word satisfies the conditions.
 *
 * Example 2:
 *
 * Input: word = "b3"
 * Output: false
 * Explanation:
 * The length of this word is fewer than 3, and does not have a vowel.
 *
 * Example 3:
 *
 * Input: word = "a3$e"
 * Output: false
 * Explanation:
 * This word contains a '$' character and does not have a consonant.
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 20
 * word consists of English uppercase and lowercase letters, digits, '@', '#', and '$'.
 *
 */
public class ValidWord {

    // V0
    // IDEA: FOUR INDEPENDENT CHECKS IN ONE PASS (LENGTH, ALPHANUMERIC, HAS VOWEL, HAS CONSONANT)
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean isValid(String word) {
        if (word == null || word.length() < 3) {
            return false;
        }
        boolean hasVowel = false;
        boolean hasConsonant = false;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            // digits are allowed but count as neither vowel nor consonant
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            boolean isLetter = (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
            // '@', '#', '$' make the whole word invalid
            if (!isLetter) {
                return false;
            }
            if (isVowel(ch)) {
                hasVowel = true;
            } else {
                hasConsonant = true;
            }
        }
        return hasVowel && hasConsonant;
    }

    /** True for a, e, i, o, u in either case. */
    private boolean isVowel(char ch) {
        char lower = Character.toLowerCase(ch);
        return lower == 'a' || lower == 'e' || lower == 'i' || lower == 'o' || lower == 'u';
    }
}
