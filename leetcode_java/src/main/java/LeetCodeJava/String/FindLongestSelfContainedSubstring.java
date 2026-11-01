package LeetCodeJava.String;

// https://leetcode.com/problems/find-longest-self-contained-substring/description/

import java.util.Arrays;

/**
 * 3104. Find Longest Self-Contained Substring
 * Hard
 *
 *
 * Given a string s, your task is to find the length of the longest self-contained substring of s.
 *
 * A substring t of a string s is called self-contained if t != s and for every character in t, it doesn't exist in the rest of s.
 *
 * Return the length of the longest self-contained substring of s if it exists, otherwise, return -1.
 *
 *
 * Example 1:
 *
 * Input: s = "abba"
 * Output: 2
 * Explanation:
 * Let's check the substring "bb". You can see that no other "b" is outside of this substring. Hence the answer is 2.
 *
 * Example 2:
 *
 * Input: s = "abab"
 * Output: -1
 * Explanation:
 * Every substring we choose does not satisfy the described property (there is some character which is inside and outside that substring).
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 5 * 10^4
 * s consists only of lowercase English letters.
 *
 */
public class FindLongestSelfContainedSubstring {

    // V0
    // IDEA: THE START MUST BE A FIRST OCCURRENCE — AT MOST 26 STARTS
    //       t = s[i..j] is self-contained iff every letter in it has
    //       first[c] >= i and last[c] <= j. The letter at i itself forces
    //       first[s[i]] == i, so only first occurrences can start a window.
    //       From each start, extend j carrying minFirst / maxLast of the letters
    //       seen: minFirst < start means a letter also lives to the left (stop);
    //       maxLast <= j means the window is self-contained (t != s required).
    /**
     * time = O(26 * N)
     * space = O(1)
     */
    public int maxSubstringLength(String s) {
        int n = s.length();
        int[] first = new int[26];
        int[] last = new int[26];
        Arrays.fill(first, -1);
        for (int i = 0; i < n; i++) {
            int letter = s.charAt(i) - 'a';
            if (first[letter] == -1) {
                first[letter] = i;
            }
            last[letter] = i;
        }

        int longest = -1;
        for (int letter = 0; letter < 26; letter++) {
            int start = first[letter];
            if (start == -1) {
                continue;
            }
            int minFirst = n;
            int maxLast = -1;
            for (int j = start; j < n; j++) {
                int current = s.charAt(j) - 'a';
                minFirst = Math.min(minFirst, first[current]);
                maxLast = Math.max(maxLast, last[current]);
                if (minFirst < start) {
                    // this letter also occurs before the window; no larger j can fix that
                    break;
                }
                int length = j - start + 1;
                if (maxLast <= j && length < n) {
                    longest = Math.max(longest, length);
                }
            }
        }
        return longest;
    }
}
