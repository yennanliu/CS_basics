package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-substrings-without-repeating-character/description/

import java.util.Arrays;

/**
 * 2743. Count Substrings Without Repeating Character
 * Medium
 *
 * You are given a string s consisting only of lowercase English letters. We call a substring
 * special if it contains no character which has occurred at least twice (in other words, it does
 * not contain a repeating character). Your task is to count the number of special substrings. For
 * example, in the string "pop", the substring "po" is a special substring, however, "pop" is not
 * special (since 'p' has occurred twice).
 *
 * Return the number of special substrings.
 *
 * A substring is a contiguous sequence of characters within a string. For example, "abc" is a
 * substring of "abcd", but "acd" is not.
 *
 *
 * Example 1:
 *
 * Input: s = "abcd"
 * Output: 10
 *
 * Example 2:
 *
 * Input: s = "ooo"
 * Output: 3
 *
 * Example 3:
 *
 * Input: s = "abab"
 * Output: 7
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of lowercase English letters
 *
 */
public class CountSubstringsWithoutRepeatingCharacter {

    // V0
    // IDEA: SLIDING WINDOW + COUNT SUBSTRINGS BY THEIR RIGHT END
    //  [left, i] is the longest window ending at i with no repeat, so exactly
    //  i - left + 1 special substrings end at i.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numberOfSpecialSubstrings(String s) {
        // lastSeen[c] = last index where character c appeared, -1 if never
        int[] lastSeen = new int[26];
        Arrays.fill(lastSeen, -1);

        int count = 0;
        int left = 0;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - 'a';
            // the previous copy of c is inside the window -> jump left just past it
            // (an older copy before `left` is already evicted, so left never rewinds)
            if (lastSeen[c] >= left) {
                left = lastSeen[c] + 1;
            }
            lastSeen[c] = i;
            count += i - left + 1;
        }
        return count;
    }
}
