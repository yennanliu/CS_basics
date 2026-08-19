package LeetCodeJava.String;

// https://leetcode.com/problems/custom-sort-string/

/**
 *  791. Custom Sort String
 *  Medium
 *
 *  You are given two strings order and s. All the characters of order are unique
 *  and were sorted in some custom order previously.
 *  Permute the characters of s so that they match the order that order was sorted.
 *  More specifically, if a character x occurs before a character y in order, then
 *  x should occur before y in the permuted string.
 *  Return any permutation of s that satisfies this property.
 *
 *  Example 1:
 *    Input:  order = "cba", s = "abcd"
 *    Output: "cbad"
 *
 *  Example 2:
 *    Input:  order = "cbafg", s = "abcd"
 *    Output: "cbad"
 *
 *  Constraints:
 *    1 <= order.length <= 26
 *    1 <= s.length <= 200
 *    order and s consist of lowercase English letters.
 *    All the characters of order are unique.
 */
public class CustomSortString {

    // V0
    // IDEA: counting sort — emit chars of `order` first (by count), then the leftovers
    /**
     * time = O(n + m)
     * space = O(1)  (fixed 26 counters)
     */
    public String customSortString(String order, String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        int[] cnt = new int[26];
        for (char c : s.toCharArray()) {
            cnt[c - 'a']++;
        }

        StringBuilder sb = new StringBuilder();
        if (order != null) {
            for (char c : order.toCharArray()) {
                int idx = c - 'a';
                while (cnt[idx] > 0) {
                    sb.append(c);
                    cnt[idx]--;
                }
            }
        }
        for (int i = 0; i < 26; i++) {
            while (cnt[i] > 0) {
                sb.append((char) ('a' + i));
                cnt[i]--;
            }
        }
        return sb.toString();
    }
}
