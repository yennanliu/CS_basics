package LeetCodeJava.String;

// https://leetcode.com/problems/largest-substring-between-two-equal-characters/description/

import java.util.*;

/**
 * 1624. Largest Substring Between Two Equal Characters
 * Easy
 *
 * Given a string s, return the length of the longest substring between two equal characters, excluding the two characters. If there is no such substring return -1.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "aa"
 * Output: 0
 * Explanation: The optimal substring here is an empty substring between the two 'a's.
 *
 * Example 2:
 *
 * Input: s = "abca"
 * Output: 2
 * Explanation: The optimal substring here is "bc".
 *
 * Example 3:
 *
 * Input: s = "cbzxy"
 * Output: -1
 * Explanation: There are no characters that appear twice in s.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 300
 * s contains only lowercase English letters.
 *
 */
public class LargestSubstringBetweenTwoEqualCharacters {

    // V0
    // IDEA: HASH TABLE OF THE FIRST INDEX OF EVERY CHARACTER
    //       for a character c the widest gap is between its first and its last
    //       occurrence, so only the earliest index of c is needed. Scanning once,
    //       a repeat at i gives the candidate i - first[c] - 1 (the -1 drops both ends).
    /**
     * time = O(N)
     * space = O(1)   (26 slots)
     */
    public int maxLengthBetweenEqualCharacters(String s) {
        // firstIndex[c] = first position of letter c, or -1 if not seen yet
        int[] firstIndex = new int[26];
        Arrays.fill(firstIndex, -1);

        // -1 is also the answer when no character repeats
        int best = -1;
        for (int i = 0; i < s.length(); i++) {
            int letter = s.charAt(i) - 'a';
            if (firstIndex[letter] == -1) {
                firstIndex[letter] = i;
            } else {
                best = Math.max(best, i - firstIndex[letter] - 1);
            }
        }
        return best;
    }
}
