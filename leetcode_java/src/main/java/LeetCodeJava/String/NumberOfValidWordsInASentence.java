package LeetCodeJava.String;

// https://leetcode.com/problems/number-of-valid-words-in-a-sentence/description/

/**
 * 2047. Number of Valid Words in a Sentence
 * Easy
 *
 * A sentence consists of lowercase letters ('a' to 'z'), digits ('0' to '9'), hyphens ('-'), punctuation marks ('!', '.', and ','), and spaces (' ') only. Each sentence can be broken down into one or more tokens separated by one or more spaces ' '.
 *
 * A token is a valid word if all three of the following are true:
 *
 * It only contains lowercase letters, hyphens, and/or punctuation (no digits).
 * There is at most one hyphen '-'. If present, it must be surrounded by lowercase characters ("a-b" is valid, but "-ab" and "ab-" are not valid).
 * There is at most one punctuation mark. If present, it must be at the end of the token ("ab,", "cd!", and "." are valid, but "a!b" and "c.," are not valid).
 *
 * Examples of valid words include "a-b.", "afad", "ba-c", "a!", and "!".
 *
 * Given a string sentence, return the number of valid words in sentence.
 *
 *
 * Example 1:
 *
 * Input: sentence = "cat and  dog"
 * Output: 3
 * Explanation: The valid words in the sentence are "cat", "and", and "dog".
 *
 * Example 2:
 *
 * Input: sentence = "!this  1-s b8d!"
 * Output: 0
 * Explanation: There are no valid words in the sentence.
 * "!this" is invalid because it starts with a punctuation mark.
 * "1-s" and "b8d" are invalid because they contain digits.
 *
 * Example 3:
 *
 * Input: sentence = "alice and  bob are playing stone-game10"
 * Output: 5
 * Explanation: The valid words in the sentence are "alice", "and", "bob", "are", and "playing".
 * "stone-game10" is invalid because it contains digits.
 *
 *
 * Constraints:
 *
 * 1 <= sentence.length <= 1000
 * sentence only contains lowercase English letters, digits, ' ', '-', '!', '.', and ','.
 * sentence can be empty.
 * There are at most 1000 tokens in the sentence.
 *
 */
public class NumberOfValidWordsInASentence {

    // V0
    // IDEA: TOKENIZE, THEN CHECK THE THREE RULES DIRECTLY ON EACH TOKEN
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countValidWords(String sentence) {
        int count = 0;
        // tokens may be separated by several spaces, so split on runs of spaces
        for (String token : sentence.trim().split(" +")) {
            if (!token.isEmpty() && isValid(token)) {
                count++;
            }
        }
        return count;
    }

    /** Checks the no-digit, one-hyphen-between-letters and punctuation-at-end rules. */
    private boolean isValid(String token) {
        int hyphens = 0;
        int last = token.length() - 1;
        for (int i = 0; i <= last; i++) {
            char c = token.charAt(i);
            if (Character.isDigit(c)) {
                return false;
            }
            if (c == '-') {
                hyphens++;
                if (hyphens > 1) {
                    return false;
                }
                // a hyphen needs a lowercase letter on both sides
                if (i == 0 || i == last
                        || !Character.isLowerCase(token.charAt(i - 1))
                        || !Character.isLowerCase(token.charAt(i + 1))) {
                    return false;
                }
            } else if (c == '!' || c == '.' || c == ',') {
                // punctuation is only allowed as the last character
                if (i != last) {
                    return false;
                }
            }
        }
        return true;
    }
}
