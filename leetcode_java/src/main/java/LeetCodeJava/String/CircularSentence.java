package LeetCodeJava.String;

// https://leetcode.com/problems/circular-sentence/description/

/**
 * 2490. Circular Sentence
 * Easy
 *
 * A sentence is a list of words that are separated by a single space with no leading or trailing spaces.
 *
 * For example, "Hello World", "HELLO", "hello world hello world" are all sentences.
 *
 * Words consist of only uppercase and lowercase English letters. Uppercase and lowercase English letters are considered different.
 *
 * A sentence is circular if:
 *
 * The last character of a word is equal to the first character of the next word.
 * The last character of the last word is equal to the first character of the first word.
 *
 * For example, "leetcode exercises sound delightful", "eetcode", "leetcode eats soul" are all circular sentences. However, "Leetcode is cool", "happy Leetcode", "Leetcode" and "I like Leetcode" are not circular sentences.
 *
 * Given a string sentence, return true if it is circular. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: sentence = "leetcode exercises sound delightful"
 * Output: true
 * Explanation: The words in sentence are ["leetcode", "exercises", "sound", "delightful"].
 * - leetcode's last character is equal to exercises's first character.
 * - exercises's last character is equal to sound's first character.
 * - sound's last character is equal to delightful's first character.
 * - delightful's last character is equal to leetcode's first character.
 * The sentence is circular.
 *
 * Example 2:
 *
 * Input: sentence = "eetcode"
 * Output: true
 * Explanation: The words in sentence are ["eetcode"].
 * - eetcode's last character is equal to eetcode's first character.
 * The sentence is circular.
 *
 * Example 3:
 *
 * Input: sentence = "Leetcode is cool"
 * Output: false
 * Explanation: The words in sentence are ["Leetcode", "is", "cool"].
 * - Leetcode's last character is not equal to is's first character.
 * The sentence is not circular.
 *
 *
 * Constraints:
 *
 * 1 <= sentence.length <= 500
 * sentence consist of only lowercase and uppercase English letters and spaces.
 * The words in sentence are separated by a single space.
 * There are no leading or trailing spaces.
 *
 */
public class CircularSentence {

    // V0
    // IDEA: CHECK EVERY WORD AGAINST THE NEXT ONE, WRAPPING AT THE END
    //       next index = (i + 1) % words.length folds the "last wraps to
    //       first" rule into the loop; one word compares with itself
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isCircularSentence(String sentence) {
        String[] words = sentence.split(" ");
        for (int i = 0; i < words.length; i++) {
            String current = words[i];
            String next = words[(i + 1) % words.length];
            if (current.charAt(current.length() - 1) != next.charAt(0)) {
                return false;
            }
        }
        return true;
    }

    // V1
    // IDEA: NO SPLIT — EVERY SPACE MUST SIT BETWEEN TWO EQUAL CHARACTERS,
    //       AND THE FIRST CHARACTER MUST EQUAL THE LAST
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean isCircularSentence_1(String sentence) {
        int n = sentence.length();
        if (sentence.charAt(0) != sentence.charAt(n - 1)) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            // no leading/trailing space, so i - 1 and i + 1 are in range
            if (sentence.charAt(i) == ' ' && sentence.charAt(i - 1) != sentence.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
