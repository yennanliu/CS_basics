package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-palindromic-subsequence-ii/description/

import java.util.Arrays;

/**
 * 1682. Longest Palindromic Subsequence II
 * Medium
 *
 * A subsequence of a string s is considered a good palindromic subsequence if:
 *
 * - It is a subsequence of s.
 * - It is a palindrome (has the same value if reversed).
 * - It has an even length.
 * - No two consecutive characters are equal, except the two middle ones.
 *
 * For example, if s = "abcabcabb", then "abba" is considered a good palindromic subsequence, while
 * "bcb" (not even length) and "bbbb" (has equal consecutive characters) are not.
 *
 * Given a string s, return the length of the longest good palindromic subsequence in s.
 *
 *
 * Example 1:
 *
 * Input: s = "bbabab"
 * Output: 4
 * Explanation: The longest good palindromic subsequence of s is "baab".
 *
 * Example 2:
 *
 * Input: s = "dcbccacdb"
 * Output: 4
 * Explanation: The longest good palindromic subsequence of s is "dccd".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 250
 * s consists of lowercase English letters.
 *
 */
public class LongestPalindromicSubsequenceII {

    // index meaning "no outer character yet" (the 27th slot after 'a'..'z')
    private static final int NO_CHAR = 26;

    // V0
    // IDEA: INTERVAL DP WITH THE OUTERMOST CHARACTER IN THE STATE
    //       a good palindrome has even length and no two equal consecutive chars, except the
    //       middle two. Building it outside-in, the only new constraint is that a pair must
    //       differ from the pair just outside it -> remember that outer char.
    //       longest(i, j, outer) = longest good subsequence of s[i..j] whose outer pair != outer
    //         s[i] == s[j] and s[i] != outer -> 2 + longest(i+1, j-1, s[i])
    //         otherwise                       -> max(longest(i+1, j, outer), longest(i, j-1, outer))
    /**
     * time = O(N^2 * 27)
     * space = O(N^2 * 27)
     */
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        // memo[i][j][outer] = longest(i, j, outer); -1 = not computed
        int[][][] memo = new int[n][n][NO_CHAR + 1];
        for (int[][] plane : memo) {
            for (int[] line : plane) {
                Arrays.fill(line, -1);
            }
        }
        return longest(s, 0, n - 1, NO_CHAR, memo);
    }

    /** Longest good palindromic subsequence of s[i..j] whose outer pair is not `outer`. */
    private int longest(String s, int i, int j, int outer, int[][][] memo) {
        // fewer than two chars: no even-length palindrome fits
        if (i >= j) {
            return 0;
        }
        if (memo[i][j][outer] != -1) {
            return memo[i][j][outer];
        }

        int left = s.charAt(i) - 'a';
        int result;
        if (s.charAt(i) == s.charAt(j) && left != outer) {
            // use both ends as the next pair; the pair inside must differ from it
            result = 2 + longest(s, i + 1, j - 1, left, memo);
        } else {
            result = Math.max(longest(s, i + 1, j, outer, memo), longest(s, i, j - 1, outer, memo));
        }

        memo[i][j][outer] = result;
        return result;
    }
}
