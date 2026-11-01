package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-non-overlapping-palindrome-substrings/description/

import java.util.*;

/**
 * 2472. Maximum Number of Non-overlapping Palindrome Substrings
 * Hard
 *
 * You are given a string s and a positive integer k.
 *
 * Select a set of non-overlapping substrings from the string s that satisfy the following conditions:
 *
 * The length of each substring is at least k.
 * Each substring is a palindrome.
 *
 * Return the maximum number of substrings in an optimal selection.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "abaccdbbd", k = 3
 * Output: 2
 * Explanation: We can select the substrings underlined in s = "abaccdbbd".
 * Both "aba" and "dbbd" are palindromes and have a length of at least k = 3.
 * It can be shown that we cannot find a selection with more than two valid substrings.
 *
 * Example 2:
 *
 * Input: s = "adbcda", k = 2
 * Output: 0
 * Explanation: There is no palindrome substring of length at least 2 in the string.
 *
 *
 * Constraints:
 *
 * 1 <= k <= s.length <= 2000
 * s consists of lowercase English letters.
 *
 */
public class MaximumNumberOfNonOverlappingPalindromeSubstrings {

    // V0
    // IDEA: EXPAND AROUND CENTRE + INTERVAL SCHEDULING (TAKE THE EARLIEST END)
    //       for each end index keep the largest start of a palindrome of length >= k ending there
    //       (the shortest one); then sweep ends left to right, taking one whenever it starts
    //       after the last taken end.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int maxPalindromes(String s, int k) {
        int n = s.length();
        // bestStart[e] = largest l with s[l..e] a palindrome of length >= k, or -1
        int[] bestStart = new int[n];
        Arrays.fill(bestStart, -1);

        // 2n - 1 centres: even centre ids are single chars, odd ones sit between two chars
        for (int center = 0; center < 2 * n - 1; center++) {
            int left = center / 2;
            int right = left + center % 2;
            while (left >= 0 && right < n && s.charAt(left) == s.charAt(right)) {
                if (right - left + 1 >= k && left > bestStart[right]) {
                    bestStart[right] = left;
                }
                left--;
                right++;
            }
        }

        int count = 0;
        int lastEnd = -1;
        for (int end = 0; end < n; end++) {
            // the shortest palindrome ending here fits after the previous pick
            if (bestStart[end] > lastEnd) {
                count++;
                lastEnd = end;
            }
        }
        return count;
    }
}
