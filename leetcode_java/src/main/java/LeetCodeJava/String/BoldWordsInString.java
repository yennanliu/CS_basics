package LeetCodeJava.String;

// https://leetcode.com/problems/bold-words-in-string/

import java.util.*;

/**
 *  758. Bold Words in String
 *  Medium (premium)
 *
 *  Given an array of keywords `words` and a string `s`, make all appearances of
 *  all keywords in `s` bold. Any letters between <b> and </b> tags become bold.
 *
 *  Return `s` after adding the bold tags. The returned string should use the least
 *  number of tags possible, and the tags should form a valid combination.
 *
 *  Example 1:
 *    Input: words = ["ab","bc"], s = "aabcd"
 *    Output: "a<b>abc</b>d"
 *    Note that returning "a<b>a<b>b</b>c</b>d" would use more tags, so it is incorrect.
 *
 *  Example 2:
 *    Input: words = ["ab","cb"], s = "aabcd"
 *    Output: "a<b>ab</b>cd"
 *
 *  Constraints:
 *    1 <= s.length <= 500
 *    0 <= words.length <= 50
 *    1 <= words[i].length <= 10
 *    s and words[i] consist of lowercase English letters.
 *
 *  NOTE: identical to LC 616 (Add Bold Tag in String), only the parameter order differs.
 */
public class BoldWordsInString {

    // V0
    // IDEA: mark every covered index, then emit tags at the boundaries of each covered run
    /**
     * time = O(n * w * l), space = O(n)
     *   n = s.length(), w = words.length, l = max word length
     */
    public String boldWords(String[] words, String s) {
        if (s == null || s.isEmpty() || words == null || words.length == 0) {
            return s;
        }

        int n = s.length();
        boolean[] bold = new boolean[n];

        for (String w : words) {
            if (w == null || w.isEmpty()) {
                continue;
            }
            int from = s.indexOf(w);
            while (from != -1) {
                for (int i = from; i < from + w.length(); i++) {
                    bold[i] = true;
                }
                from = s.indexOf(w, from + 1);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            // open a tag at the start of a bold run
            if (bold[i] && (i == 0 || !bold[i - 1])) {
                sb.append("<b>");
            }
            sb.append(s.charAt(i));
            // close it at the end of the run
            if (bold[i] && (i == n - 1 || !bold[i + 1])) {
                sb.append("</b>");
            }
        }

        return sb.toString();
    }
}
