package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/longest-repeating-substring/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 1062. Longest Repeating Substring
 * Medium
 *
 * Given a string s, return the length of the longest repeating substrings.
 * If no repeating substring exists, return 0.
 *
 *
 * Example 1:
 *
 * Input: s = "abcd"
 * Output: 0
 * Explanation: There is no repeating substring.
 *
 * Example 2:
 *
 * Input: s = "abbaba"
 * Output: 2
 * Explanation: The longest repeating substrings are "ab" and "ba", each of which occurs twice.
 *
 * Example 3:
 *
 * Input: s = "aabcaabdaab"
 * Output: 3
 * Explanation: The longest repeating substring is "aab", which occurs 3 times.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 2000
 * s consists of lowercase English letters.
 *
 */
public class LongestRepeatingSubstring {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER + HASH SET
    /**
     *  monotonic property:
     *    if a substring of length L appears twice, so does one of length L - 1
     *    (drop its last char) -> "has a repeat of length L" is true for all L <= answer
     *    and false after -> binary search the last true.
     */
    /**
     * time = O(N^2 log N)
     * space = O(N^2)
     */
    public int longestRepeatingSubstring(String s) {
        int n = s.length();
        int answer = 0;
        int left = 1;
        int right = n - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (hasRepeat(s, mid)) {
                // length mid works -> record it and try longer
                answer = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return answer;
    }

    /** True when some substring of the given length occurs at least twice. */
    private boolean hasRepeat(String s, int length) {
        Set<String> seen = new HashSet<>();
        for (int i = 0; i + length <= s.length(); i++) {
            String window = s.substring(i, i + length);
            if (seen.contains(window)) {
                return true;
            }
            seen.add(window);
        }
        return false;
    }

    // V1
    // IDEA: 2D DP (LONGEST COMMON SUFFIX, LCS STYLE)
    /**
     *  dp[i][j] (j < i) = length of the longest common suffix of s[..i] and s[..j]
     *  dp[i][j] = dp[i-1][j-1] + 1 if s[i] == s[j], else 0
     *  answer = max over all dp[i][j]; only the previous row is kept.
     */
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int longestRepeatingSubstring_1(String s) {
        int n = s.length();
        int answer = 0;
        int[] prevRow = new int[n];
        for (int i = 1; i < n; i++) {
            int[] curRow = new int[n];
            for (int j = 0; j < i; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    // extend the common suffix ending at (i - 1, j - 1)
                    int extend = 0;
                    if (j > 0) {
                        extend = prevRow[j - 1];
                    }
                    curRow[j] = extend + 1;
                    answer = Math.max(answer, curRow[j]);
                }
            }
            prevRow = curRow;
        }
        return answer;
    }
}
