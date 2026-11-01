package LeetCodeJava.String;

// https://leetcode.com/problems/check-if-string-is-a-prefix-of-array/description/

/**
 * 1961. Check If String Is a Prefix of Array
 * Easy
 *
 * Given a string s and an array of strings words, determine whether s is a prefix string of words.
 *
 * A string s is a prefix string of words if s can be made by concatenating the first k strings in words for some positive k no larger than words.length.
 *
 * Return true if s is a prefix string of words, or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: s = "iloveleetcode", words = ["i","love","leetcode","apples"]
 * Output: true
 * Explanation:
 * s can be made by concatenating "i", "love", and "leetcode" together.
 *
 * Example 2:
 *
 * Input: s = "iloveleetcode", words = ["apples","i","love","leetcode"]
 * Output: false
 * Explanation:
 * It is impossible to make s using a prefix of arr.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 20
 * 1 <= s.length <= 1000
 * words[i] and s consist of only lowercase English letters.
 *
 */
public class CheckIfStringIsAPrefixOfArray {

    // V0
    // IDEA: LINEAR SCAN (match each word against s at a moving cursor)
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean isPrefixString(String s, String[] words) {
        // cursor = how many characters of s the first k words have matched so far
        int cursor = 0;
        for (String word : words) {
            // the word must fit in what is left of s, and match it exactly
            if (!s.startsWith(word, cursor)) {
                return false;
            }
            cursor += word.length();
            // consumed all of s with exactly the first k words
            if (cursor == s.length()) {
                return true;
            }
        }
        // ran out of words before covering all of s
        return false;
    }
}
