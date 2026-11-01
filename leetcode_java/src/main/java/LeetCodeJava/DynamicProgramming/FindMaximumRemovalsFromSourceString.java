package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-maximum-removals-from-source-string/description/

import java.util.*;

/**
 * 3316. Find Maximum Removals From Source String
 * Medium
 *
 * You are given a string source of size n, a string pattern that is a subsequence of source, and a sorted integer array targetIndices that contains distinct numbers in the range [0, n - 1].
 *
 * We define an operation as removing a character at an index idx from source such that:
 *
 * idx is an element of targetIndices.
 * pattern remains a subsequence of source after removing the character.
 *
 * Performing an operation does not change the indices of the other characters in source. For example, if you remove 'c' from "acb", the character at index 2 would still be 'b'.
 *
 * Return the maximum number of operations that can be performed.
 *
 *
 * Example 1:
 *
 * Input: source = "abbaa", pattern = "aba", targetIndices = [0,1,2]
 * Output: 1
 * Explanation:
 * We can't remove source[0] but we can do either of these two operations:
 * Remove source[1], so that source becomes "a_baa".
 * Remove source[2], so that source becomes "ab_aa".
 *
 * Example 2:
 *
 * Input: source = "bcda", pattern = "d", targetIndices = [0,3]
 * Output: 2
 * Explanation:
 * We can remove source[0] and source[3] in two operations.
 *
 * Example 3:
 *
 * Input: source = "dda", pattern = "dda", targetIndices = [0,1,2]
 * Output: 0
 * Explanation:
 * We can't remove any character from source.
 *
 * Example 4:
 *
 * Input: source = "yeyeykyded", pattern = "yeyyd", targetIndices = [0,2,3,4]
 * Output: 2
 * Explanation:
 * We can remove source[2] and source[3] in two operations.
 *
 *
 * Constraints:
 *
 * 1 <= n == source.length <= 3 * 10^3
 * 1 <= pattern.length <= n
 * 1 <= targetIndices.length <= n
 * targetIndices is sorted in ascending order.
 * 0 <= targetIndices[i] < n
 * source and pattern consist only of lowercase English letters.
 * The input is generated such that targetIndices contains distinct elements and pattern is a subsequence of source.
 *
 */
public class FindMaximumRemovalsFromSourceString {

    // V0
    // IDEA: SUBSEQUENCE-MATCHING DP, COUNTING REMOVABLE TARGET INDICES
    /**
     *  Removals interact (deleting one char can starve a later match), so they
     *  cannot be chosen greedily -- track the matching process itself.
     *
     *  DP def:
     *     dp[j] = MOST removable target characters among source[0 .. i-1]
     *             while pattern[0 .. j-1] has been matched (-1 = unreachable)
     *
     *  DP eq (at source position i, gain = 1 if i is a target index):
     *     skip source[i]:  next[j]     = max(next[j],     dp[j] + gain)
     *     match it:        next[j + 1] = max(next[j + 1], dp[j])
     *                      (only when source[i] == pattern[j]; a matched char is
     *                       KEPT, so it earns no removal)
     *
     *  Init:   dp[0] = 0
     *  Answer: dp[m] after the whole source
     *
     *  time = O(N * M)
     *  space = O(N + M)
     */
    public int maxRemovals(String source, String pattern, int[] targetIndices) {
        int n = source.length();
        int m = pattern.length();

        boolean[] isTarget = new boolean[n];
        for (int index : targetIndices) {
            isTarget[index] = true;
        }

        final int UNREACHABLE = -1;
        int[] dp = new int[m + 1];
        Arrays.fill(dp, UNREACHABLE);
        dp[0] = 0;

        for (int i = 0; i < n; i++) {
            int[] next = new int[m + 1];
            Arrays.fill(next, UNREACHABLE);
            int gain = isTarget[i] ? 1 : 0;

            for (int j = 0; j <= m; j++) {
                if (dp[j] == UNREACHABLE) {
                    continue;
                }
                // option 1: do not use source[i] for matching (remove it if allowed)
                next[j] = Math.max(next[j], dp[j] + gain);

                // option 2: use source[i] to match pattern[j]; it must be kept
                if (j < m && source.charAt(i) == pattern.charAt(j)) {
                    next[j + 1] = Math.max(next[j + 1], dp[j]);
                }
            }
            dp = next;
        }
        return dp[m];
    }
}
