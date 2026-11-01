package LeetCodeJava.String;

// https://leetcode.com/problems/longest-palindrome-by-concatenating-two-letter-words/description/

import java.util.*;

/**
 * 2131. Longest Palindrome by Concatenating Two Letter Words
 * Medium
 *
 * You are given an array of strings words. Each element of words consists of two lowercase English letters.
 *
 * Create the longest possible palindrome by selecting some elements from words and concatenating them in any order. Each element can be selected at most once.
 *
 * Return the length of the longest palindrome that you can create. If it is impossible to create any palindrome, return 0.
 *
 * A palindrome is a string that reads the same forward and backward.
 *
 *
 * Example 1:
 *
 * Input: words = ["lc","cl","gg"]
 * Output: 6
 * Explanation: One longest palindrome is "lc" + "gg" + "cl" = "lcggcl", of length 6.
 * Note that "clgglc" is another longest palindrome that can be created.
 *
 * Example 2:
 *
 * Input: words = ["ab","ty","yt","lc","cl","ab"]
 * Output: 8
 * Explanation: One longest palindrome is "ty" + "lc" + "cl" + "yt" = "tylcclyt", of length 8.
 * Note that "lcyttycl" is another longest palindrome that can be created.
 *
 * Example 3:
 *
 * Input: words = ["cc","ll","xx"]
 * Output: 2
 * Explanation: One longest palindrome is "cc", of length 2.
 * Note that "ll" is another longest palindrome that can be created, and so is "xx".
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 10^5
 * words[i].length == 2
 * words[i] consists of lowercase English letters.
 *
 */
public class LongestPalindromeByConcatenatingTwoLetterWords {

    // V0
    // IDEA: PAIR EACH WORD WITH ITS REVERSE; ONE LEFTOVER DOUBLE-LETTER WORD MAY SIT IN THE MIDDLE
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestPalindrome(String[] words) {
        Map<String, Integer> count = new HashMap<>();
        for (String word : words) {
            count.put(word, count.getOrDefault(word, 0) + 1);
        }

        int length = 0;
        boolean hasCenter = false;
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            String word = entry.getKey();
            int copies = entry.getValue();
            char first = word.charAt(0);
            char second = word.charAt(1);
            if (first == second) {
                // "xx": every two copies mirror each other (4 chars per pair)
                length += (copies / 2) * 4;
                // an odd copy left over can be the single centre word
                if (copies % 2 == 1) {
                    hasCenter = true;
                }
            } else if (first < second) {
                // "xy" pairs with "yx"; count each unordered pair once (only from the smaller side)
                String reversed = "" + second + first;
                int reversedCopies = count.getOrDefault(reversed, 0);
                length += Math.min(copies, reversedCopies) * 4;
            }
        }
        // only one centre exists in the whole palindrome
        if (hasCenter) {
            length += 2;
        }
        return length;
    }
}
