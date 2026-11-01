package LeetCodeJava.String;

// https://leetcode.com/problems/print-words-vertically/description/

import java.util.*;

/**
 * 1324. Print Words Vertically
 * Medium
 *
 * Given a string s. Return all the words vertically in the same order in which they appear in s.
 *
 * Words are returned as a list of strings, complete with spaces when is necessary.
 * (Trailing spaces are not allowed).
 *
 * Each word would be put on only one column and that in one column there will be only one word.
 *
 *
 * Example 1:
 *
 * Input: s = "HOW ARE YOU"
 *
 * Output: ["HAY","ORO","WEU"]
 *
 * Explanation: Each word is printed vertically.
 *
 *  "HAY"
 *
 *  "ORO"
 *
 *  "WEU"
 *
 * Example 2:
 *
 * Input: s = "TO BE OR NOT TO BE"
 *
 * Output: ["TBONTB","OEROOE","   T"]
 *
 * Explanation: Trailing spaces is not allowed.
 *
 * "TBONTB"
 *
 * "OEROOE"
 *
 * "   T"
 *
 * Example 3:
 *
 * Input: s = "CONTEST IS COMING"
 *
 * Output: ["CIC","OSO","N M","T I","E N","S G","T"]
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 200
 * s contains only upper case English letters.
 * It's guaranteed that there is only one space between 2 words.
 *
 */
public class PrintWordsVertically {

    // V0
    // IDEA: MATRIX TRANSPOSE WITH PADDING, THEN STRIP TRAILING SPACES
    //       output row j reads the j-th char of every word; a word shorter than j gives a
    //       space. interior spaces keep the columns aligned, trailing ones must go.
    /**
     * time = O(N * M), N = number of words, M = longest word length
     * space = O(N * M)
     */
    public List<String> printVertically(String s) {
        String[] words = s.split(" ");
        int width = 0;
        for (String word : words) {
            width = Math.max(width, word.length());
        }

        List<String> result = new ArrayList<>();
        for (int j = 0; j < width; j++) {
            StringBuilder row = new StringBuilder();
            for (String word : words) {
                row.append(j < word.length() ? word.charAt(j) : ' ');
            }
            // drop trailing spaces only
            int end = row.length();
            while (end > 0 && row.charAt(end - 1) == ' ') {
                end--;
            }
            row.setLength(end);
            result.add(row.toString());
        }
        return result;
    }
}
