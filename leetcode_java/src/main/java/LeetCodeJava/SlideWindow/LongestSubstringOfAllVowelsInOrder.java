package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/longest-substring-of-all-vowels-in-order/

/**
 *  1839. Longest Substring Of All Vowels in Order
 *  Medium
 *
 *  A string is considered beautiful if it satisfies the following conditions:
 *
 *   - Each of the 5 English vowels ('a', 'e', 'i', 'o', 'u') must appear at
 *     least once in it.
 *   - The letters must be sorted in alphabetical order (i.e. all 'a's before
 *     'e's, all 'e's before 'i's, etc.).
 *
 *  For example, "aeiou" and "aaaaaaeiiiioou" are beautiful, but "uaeio",
 *  "aeoiu" and "aaaeeeooo" are not.
 *
 *  Given a string word consisting of English vowels, return the length of the
 *  longest beautiful substring of word. If no such substring exists, return 0.
 *
 *
 *  Example 1:
 *
 *  Input: word = "aeiaaioaaaaeiiiiouuuooaauuaeiu"
 *  Output: 13
 *  Explanation: the longest beautiful substring is "aaaaeiiiiouuu".
 *
 *  Example 2:
 *
 *  Input: word = "aeeeiiiioooauuuaeiou"
 *  Output: 5
 *
 *  Example 3:
 *
 *  Input: word = "a"
 *  Output: 0
 *
 *
 *  Constraints:
 *
 *  1 <= word.length <= 5 * 10^5
 *  word consists of characters 'a', 'e', 'i', 'o' and 'u'.
 */
public class LongestSubstringOfAllVowelsInOrder {

    // V0
    // IDEA: SLIDING WINDOW.
    //       Since 'a' < 'e' < 'i' < 'o' < 'u' alphabetically, "sorted" simply
    //       means non-decreasing chars. Track the current run length and how
    //       many DISTINCT vowels it contains (a strict increase = new vowel).
    /**
     * time = O(n)
     * space = O(1)
     */
    public int longestBeautifulSubstring(String word) {
        if (word == null || word.length() < 5) {
            return 0;
        }

        int res = 0;
        int curLen = 1;      // length of current non-decreasing run
        int distinct = 1;    // distinct vowels inside that run

        for (int i = 1; i < word.length(); i++) {
            char cur = word.charAt(i);
            char prev = word.charAt(i - 1);

            if (cur < prev) {
                // order broken -> restart the window at i
                curLen = 1;
                distinct = 1;
            } else {
                curLen++;
                if (cur > prev) {
                    distinct++;
                }
            }

            if (distinct == 5) {
                res = Math.max(res, curLen);
            }
        }
        return res;
    }
}
