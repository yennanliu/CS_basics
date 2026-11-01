package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-ideal-subsequence/description/

/**
 * 2370. Longest Ideal Subsequence
 * Medium
 *
 * You are given a string s consisting of lowercase letters and an integer k. We call a string t ideal if the following conditions are satisfied:
 *
 * t is a subsequence of the string s.
 * The absolute difference in the alphabet order of every two adjacent letters in t is less than or equal to k.
 *
 * Return the length of the longest ideal string.
 *
 * A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.
 *
 * Note that the alphabet order is not cyclic. For example, the absolute difference in the alphabet order of 'a' and 'z' is 25, not 1.
 *
 *
 * Example 1:
 *
 * Input: s = "acfgbd", k = 2
 * Output: 4
 * Explanation: The longest ideal string is "acbd". The length of this string is 4, so 4 is returned.
 * Note that "acfgbd" is not ideal because 'c' and 'f' have a difference of 3 in alphabet order.
 *
 * Example 2:
 *
 * Input: s = "abcd", k = 3
 * Output: 4
 * Explanation: The longest ideal string is "abcd". The length of this string is 4, so 4 is returned.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * 0 <= k <= 25
 * s consists of lowercase English letters.
 *
 */
public class LongestIdealSubsequence {

    // V0
    // IDEA: DP OVER THE LAST LETTER — best[c] = LONGEST IDEAL SUBSEQUENCE SO FAR ENDING IN LETTER c
    /**
     * time = O(N * K)
     * space = O(26)
     */
    public int longestIdealString(String s, int k) {
        int[] best = new int[26];

        for (int i = 0; i < s.length(); i++) {
            int letter = s.charAt(i) - 'a';

            // the previous letter may be any of [letter - k, letter + k]
            int low = Math.max(0, letter - k);
            int high = Math.min(25, letter + k);
            int longestBefore = 0;
            for (int prev = low; prev <= high; prev++) {
                longestBefore = Math.max(longestBefore, best[prev]);
            }

            // append this character to the best compatible subsequence
            best[letter] = longestBefore + 1;
        }

        int answer = 0;
        for (int length : best) {
            answer = Math.max(answer, length);
        }
        return answer;
    }
}
