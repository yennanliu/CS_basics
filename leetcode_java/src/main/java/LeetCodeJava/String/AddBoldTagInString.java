package LeetCodeJava.String;

// https://leetcode.com/problems/add-bold-tag-in-string/

/**
 *  616. Add Bold Tag in String
 *  Medium
 *
 *  You are given a string s and an array of strings words.
 *  You should add a closed pair of bold tag <b> and </b> to wrap the substrings
 *  in s that exist in words.
 *  If two such substrings overlap, you should wrap them together with only one
 *  pair of closed bold-tag.
 *  If two substrings wrapped by bold tags are consecutive, you should combine them.
 *
 *  Example 1:
 *    Input:  s = "abcxyz123", words = ["abc","123"]
 *    Output: "<b>abc</b>xyz<b>123</b>"
 *
 *  Example 2:
 *    Input:  s = "aaabbb", words = ["aa","b"]
 *    Output: "<b>aaabbb</b>"
 *
 *  Constraints:
 *    1 <= s.length <= 1000
 *    0 <= words.length <= 100
 *    1 <= words[i].length <= 1000
 *    s and words[i] consist of English letters and digits.
 */
public class AddBoldTagInString {

    // V0
    // IDEA: boolean "should be bold" mark array, then one scan emitting the tags
    /**
     * time = O(n * d * l), n = s.length, d = words.length, l = avg word length
     * space = O(n)
     */
    public String addBoldTag(String s, String[] words) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        int n = s.length();
        boolean[] bold = new boolean[n];

        if (words != null) {
            for (String w : words) {
                if (w == null || w.isEmpty()) {
                    continue;
                }
                int pos = s.indexOf(w);
                while (pos != -1) {
                    for (int i = pos; i < pos + w.length(); i++) {
                        bold[i] = true;
                    }
                    pos = s.indexOf(w, pos + 1);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (bold[i] && (i == 0 || !bold[i - 1])) {
                sb.append("<b>");
            }
            sb.append(s.charAt(i));
            if (bold[i] && (i == n - 1 || !bold[i + 1])) {
                sb.append("</b>");
            }
        }
        return sb.toString();
    }
}
