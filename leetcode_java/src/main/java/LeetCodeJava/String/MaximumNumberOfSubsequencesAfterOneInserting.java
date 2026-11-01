package LeetCodeJava.String;

// https://leetcode.com/problems/maximum-number-of-subsequences-after-one-inserting/description/

/**
 * 3628. Maximum Number of Subsequences After One Inserting
 * Medium
 *
 * You are given a string s consisting of uppercase English letters.
 *
 * You are allowed to insert at most one uppercase English letter at any
 * position (including the beginning or end) of the string.
 *
 * Return the maximum number of "LCT" subsequences that can be formed in the
 * resulting string after at most one insertion.
 *
 * Example 1:
 *
 * Input: s = "LMCT"
 * Output: 2
 * Explanation:
 * We can insert a "L" at the beginning of the string s to make "LLMCT", which
 * has 2 subsequences, at indices [0, 3, 4] and [1, 3, 4].
 *
 * Example 2:
 *
 * Input: s = "LCCT"
 * Output: 4
 * Explanation:
 * We can insert a "L" at the beginning of the string s to make "LLCCT", which
 * has 4 subsequences, at indices [0, 2, 4], [0, 3, 4], [1, 2, 4] and [1, 3,
 * 4].
 *
 * Example 3:
 *
 * Input: s = "L"
 * Output: 0
 * Explanation:
 * Since it is not possible to obtain the subsequence "LCT" by inserting a
 * single letter, the result is 0.
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of uppercase English letters.
 *
 */
public class MaximumNumberOfSubsequencesAfterOneInserting {

    // V0
    // IDEA: COUNT "LCT" SUBSEQUENCES, THEN TRY THE THREE USEFUL INSERTIONS
    //   'L' at the front adds every existing "CT"; 'T' at the end adds every existing "LC";
    //   'C' in gap i adds (#L before i) * (#T after i). Take the best gain.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long numOfSubsequences(String s) {
        int n = s.length();

        // forward sweep: counts of "L", "LC" and complete "LCT" subsequences
        long countL = 0;
        long countLC = 0;
        long countLCT = 0;
        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            if (ch == 'L') {
                countL++;
            } else if (ch == 'C') {
                // this C closes an "LC" with every L before it
                countLC += countL;
            } else if (ch == 'T') {
                // this T closes an "LCT" with every "LC" before it
                countLCT += countLC;
            }
        }

        // backward sweep: counts of "T" and "CT" subsequences
        long countT = 0;
        long countCT = 0;
        for (int i = n - 1; i >= 0; i--) {
            char ch = s.charAt(i);
            if (ch == 'T') {
                countT++;
            } else if (ch == 'C') {
                countCT += countT;
            }
        }

        // inserting 'L' at the front or 'T' at the end
        long bestGain = Math.max(countCT, countLC);

        // inserting 'C' in the gap before index i (i = n is the gap at the end)
        long lBefore = 0;
        long tAfter = countT;
        for (int i = 0; i <= n; i++) {
            bestGain = Math.max(bestGain, lBefore * tAfter);
            if (i < n) {
                if (s.charAt(i) == 'L') {
                    lBefore++;
                } else if (s.charAt(i) == 'T') {
                    tAfter--;
                }
            }
        }
        return countLCT + bestGain;
    }
}
