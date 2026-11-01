package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-maximum-number-of-non-intersecting-substrings/description/

import java.util.*;

/**
 * 3557. Find Maximum Number of Non Intersecting Substrings
 * Medium
 *
 * You are given a string word.
 *
 * Return the maximum number of non-intersecting substrings of word that are at
 * least four characters long and start and end with the same letter.
 *
 * Example 1:
 *
 * Input: word = "abcdeafdef"
 *
 * Output: 2
 *
 * Explanation:
 *
 * The two substrings are "abcdea" and "fdef".
 *
 * Example 2:
 *
 * Input: word = "bcdaaaab"
 *
 * Output: 1
 *
 * Explanation:
 *
 * The only substring is "aaaa". Note that we cannot also choose "bcdaaaab" since
 * it intersects with the other substring.
 *
 * Constraints:
 *
 * 1 <= word.length <= 2 * 10^5
 *
 * word consists only of lowercase English letters.
 *
 */
public class FindMaximumNumberOfNonIntersectingSubstrings {

    // V0
    // IDEA: INTERVAL SCHEDULING GREEDY — ACCEPT A SUBSTRING AT THE EARLIEST POSSIBLE END
    //       taking the interval that finishes first is optimal. scan left to right,
    //       remembering the first index of each letter since the last accepted piece;
    //       the moment a letter repeats at distance >= 4 (length >= 4, same first/last
    //       char), accept it and reset so nothing reaches back across it.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxSubstrings(String word) {
        int[] firstIndex = new int[26];
        Arrays.fill(firstIndex, -1);
        int count = 0;

        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i) - 'a';
            if (firstIndex[c] == -1) {
                firstIndex[c] = i;
            } else if (i - firstIndex[c] + 1 >= 4) {
                count++;
                // later substrings must start after i
                Arrays.fill(firstIndex, -1);
            }
        }
        return count;
    }
}
