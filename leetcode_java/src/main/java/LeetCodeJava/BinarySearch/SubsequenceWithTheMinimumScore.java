package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/subsequence-with-the-minimum-score/description/

import java.util.*;

/**
 * 2565. Subsequence With the Minimum Score
 * Hard
 *
 * You are given two strings s and t.
 *
 * You are allowed to remove any number of characters from the string t.
 *
 * The score of the string is 0 if no characters are removed from the string t, otherwise:
 *
 * - Let left be the minimum index among all removed characters.
 * - Let right be the maximum index among all removed characters.
 *
 * Then the score of the string is right - left + 1.
 *
 * Return the minimum possible score to make t a subsequence of s.
 *
 * A subsequence of a string is a new string that is formed from the original string by deleting
 * some (can be none) of the characters without disturbing the relative positions of the remaining
 * characters. (i.e., "ace" is a subsequence of "abcde" while "aec" is not).
 *
 *
 * Example 1:
 *
 * Input: s = "abacaba", t = "bzaa"
 * Output: 1
 * Explanation: In this example, we remove the character "z" at index 1 (0-indexed).
 * The string t becomes "baa" which is a subsequence of the string "abacaba" and the score is 1 - 1 + 1 = 1.
 *
 * Example 2:
 *
 * Input: s = "cde", t = "xyz"
 * Output: 3
 * Explanation: In this example, we remove characters "x", "y" and "z" at indices 0, 1, and 2 (0-indexed).
 * The string t becomes "" which is a subsequence of the string "cde" and the score is 2 - 0 + 1 = 3.
 *
 *
 * Constraints:
 *
 * 1 <= s.length, t.length <= 10^5
 * s and t consist of only lowercase English letters.
 *
 */
public class SubsequenceWithTheMinimumScore {

    // V0
    // IDEA: PREFIX / SUFFIX GREEDY MATCH + BINARY SEARCH ON THE LENGTH OF THE REMOVED WINDOW OF t
    /**
     * time = O(M + N log N)
     * space = O(N)
     */
    public int minimumScore(String s, String t) {
        int m = s.length();
        int n = t.length();

        // prefixEnd[j] = smallest index of s where t[0..j] finishes matching greedily (MAX_VALUE = impossible)
        int[] prefixEnd = buildPrefixEnd(s, t);
        // suffixStart[j] = largest index of s where t[j..] starts matching greedily (-1 = impossible)
        int[] suffixStart = buildSuffixStart(s, t);

        // feasibility is monotonic in the window length: a longer cut can mimic a shorter one
        int low = 0;
        int high = n;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (canRemoveWindow(mid, m, n, prefixEnd, suffixStart)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    /** Greedy left-to-right match: where each prefix of t finishes in s. */
    private int[] buildPrefixEnd(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[] prefixEnd = new int[n];
        Arrays.fill(prefixEnd, Integer.MAX_VALUE);
        int j = 0;
        for (int i = 0; i < m && j < n; i++) {
            if (s.charAt(i) == t.charAt(j)) {
                prefixEnd[j] = i;
                j++;
            }
        }
        return prefixEnd;
    }

    /** Greedy right-to-left match: where each suffix of t starts in s. */
    private int[] buildSuffixStart(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[] suffixStart = new int[n];
        Arrays.fill(suffixStart, -1);
        int j = n - 1;
        for (int i = m - 1; i >= 0 && j >= 0; i--) {
            if (s.charAt(i) == t.charAt(j)) {
                suffixStart[j] = i;
                j--;
            }
        }
        return suffixStart;
    }

    /** True if some window t[start .. start+length-1] can be cut so the rest is a subsequence of s. */
    private boolean canRemoveWindow(int length, int m, int n, int[] prefixEnd, int[] suffixStart) {
        for (int start = 0; start + length <= n; start++) {
            // an empty prefix ends before index 0; an empty suffix starts after the end of s
            int leftEnd = (start > 0) ? prefixEnd[start - 1] : -1;
            int rightStart = (start + length < n) ? suffixStart[start + length] : m + 1;
            // the kept prefix and kept suffix must match disjoint parts of s, in order
            if (leftEnd < rightStart) {
                return true;
            }
        }
        return false;
    }
}
