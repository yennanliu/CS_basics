package LeetCodeJava.String;

// https://leetcode.com/problems/maximum-number-of-words-you-can-type/description/

/**
 * 1935. Maximum Number of Words You Can Type
 * Easy
 *
 * There is a malfunctioning keyboard where some letter keys do not work. All other keys on the keyboard work properly.
 *
 * Given a string text of words separated by a single space (no leading or trailing spaces) and a string brokenLetters of all distinct letter keys that are broken, return the number of words in text you can fully type using this keyboard.
 *
 *
 * Example 1:
 *
 * Input: text = "hello world", brokenLetters = "ad"
 * Output: 1
 * Explanation: We cannot type "world" because the 'd' key is broken.
 *
 * Example 2:
 *
 * Input: text = "leet code", brokenLetters = "lt"
 * Output: 1
 * Explanation: We cannot type "leet" because the 'l' and 't' keys are broken.
 *
 * Example 3:
 *
 * Input: text = "leet code", brokenLetters = "e"
 * Output: 0
 * Explanation: We cannot type either word because the 'e' key is broken.
 *
 *
 * Constraints:
 *
 * 1 <= text.length <= 10^4
 * 0 <= brokenLetters.length <= 26
 * text consists of words separated by a single space without any leading or trailing spaces.
 * Each word only consists of lowercase English letters.
 * brokenLetters consists of distinct lowercase English letters.
 *
 */
public class MaximumNumberOfWordsYouCanType {

    // V0
    // IDEA: BOOLEAN TABLE OF BROKEN KEYS, CHECK EACH WORD
    //       a word can be typed iff none of its letters is broken.
    /**
     * time = O(N)
     * space = O(1)   (26 slots)
     */
    public int canBeTypedWords(String text, String brokenLetters) {
        boolean[] broken = new boolean[26];
        for (char c : brokenLetters.toCharArray()) {
            broken[c - 'a'] = true;
        }

        int typeable = 0;
        for (String word : text.split(" ")) {
            boolean canType = true;
            for (char c : word.toCharArray()) {
                if (broken[c - 'a']) {
                    canType = false;
                    break;
                }
            }
            if (canType) {
                typeable++;
            }
        }
        return typeable;
    }
}
