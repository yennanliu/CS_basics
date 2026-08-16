package LeetCodeJava.String;

// https://leetcode.com/problems/keyboard-row/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 500. Keyboard Row
 * Easy
 *
 * Given an array of strings words, return the words that can be typed using letters of
 * the alphabet on only one row of American keyboard like the image below.
 *
 * In the American keyboard:
 *
 * - the first row consists of the characters "qwertyuiop",
 * - the second row consists of the characters "asdfghjkl", and
 * - the third row consists of the characters "zxcvbnm".
 *
 * Example 1:
 *
 * Input: words = ["Hello","Alaska","Dad","Peace"]
 * Output: ["Alaska","Dad"]
 * Explanation: Both "a" and "A" are in the 2nd row of the American keyboard due to
 * case insensitivity.
 *
 * Example 2:
 *
 * Input: words = ["omk"]
 * Output: []
 *
 * Example 3:
 *
 * Input: words = ["adsdf","sfd"]
 * Output: ["adsdf","sfd"]
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 20
 * 1 <= words[i].length <= 100
 * words[i] consists of English letters (both lowercase and uppercase).
 *
 */
public class KeyboardRow {

    // V0
    // IDEA: HASH TABLE (map every letter -> its keyboard row id)
    /**
     *  a word is valid if ALL of its letters map to the SAME row id
     *
     *  NOTE !!! the check is CASE INSENSITIVE -> lowercase every char first
     *
     *  time  = O(L)   // L = total number of characters over all words
     *  space = O(1)   // the letter -> row map has a fixed size (26)
     */
    public String[] findWords(String[] words) {
        String[] rows = { "qwertyuiop", "asdfghjkl", "zxcvbnm" };

        int[] rowOf = new int[26];
        for (int idx = 0; idx < rows.length; idx++) {
            for (char ch : rows[idx].toCharArray()) {
                rowOf[ch - 'a'] = idx;
            }
        }

        List<String> res = new ArrayList<>();
        for (String w : words) {
            String lower = w.toLowerCase();
            int first = rowOf[lower.charAt(0) - 'a'];

            boolean sameRow = true;
            for (int i = 1; i < lower.length(); i++) {
                if (rowOf[lower.charAt(i) - 'a'] != first) {
                    sameRow = false;
                    break;
                }
            }

            if (sameRow) {
                res.add(w);
            }
        }

        return res.toArray(new String[0]);
    }

}
