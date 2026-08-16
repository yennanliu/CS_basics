package LeetCodeJava.HashTable;

// https://leetcode.com/problems/longest-substring-with-at-most-two-distinct-characters/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 159. Longest Substring with At Most Two Distinct Characters
 * Medium
 * Lock: Prime
 *
 * Given a string s, return the length of the longest substring that contains
 * at most two distinct characters.
 *
 *
 * Example 1:
 *
 * Input: s = "eceba"
 * Output: 3
 * Explanation: The substring is "ece" which its length is 3.
 *
 * Example 2:
 *
 * Input: s = "ccaabbb"
 * Output: 5
 * Explanation: The substring is "aabbb" which its length is 5.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of English letters.
 *
 */
public class LongestSubstringWithAtMostTwoDistinctCharacters {

    // V0
    // IDEA: SLIDING WINDOW + HASH TABLE (char -> count inside window)
    /**
     *  grow the RIGHT edge, shrink the LEFT edge while there are > 2 distinct chars
     *
     *  NOTE !!! when a count drops to 0 the key must be REMOVED from the map,
     *           otherwise `cnt.size()` still counts it and the window never shrinks.
     *
     *  time  = O(n)
     *  space = O(1)   // the map holds at most 3 keys
     */
    public int lengthOfLongestSubstringTwoDistinct(String s) {
        Map<Character, Integer> cnt = new HashMap<>();
        int res = 0;
        int left = 0;

        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            cnt.put(c, cnt.getOrDefault(c, 0) + 1);

            // too many distinct chars -> shrink from the left
            while (cnt.size() > 2) {
                char lc = s.charAt(left);
                cnt.put(lc, cnt.get(lc) - 1);
                if (cnt.get(lc) == 0) {
                    cnt.remove(lc);
                }
                left += 1;
            }

            res = Math.max(res, right - left + 1);
        }

        return res;
    }

}
