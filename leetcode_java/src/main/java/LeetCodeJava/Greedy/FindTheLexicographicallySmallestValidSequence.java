package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-the-lexicographically-smallest-valid-sequence/description/

import java.util.*;

/**
 * 3302. Find the Lexicographically Smallest Valid Sequence
 * Medium
 *
 * You are given two strings word1 and word2.
 *
 * A string x is called almost equal to y if you can change at most one character in x to make it identical to y.
 *
 * A sequence of indices seq is called valid if:
 *
 * The indices are sorted in ascending order.
 * Concatenating the characters at these indices in word1 in the same order results in a string that is almost equal to word2.
 *
 * Return an array of size word2.length representing the lexicographically smallest valid sequence of indices. If no such sequence of indices exists, return an empty array.
 *
 * Note that the answer must represent the lexicographically smallest array, not the corresponding string formed by those indices.
 *
 *
 * Example 1:
 *
 * Input: word1 = "vbcca", word2 = "abc"
 * Output: [0,1,2]
 * Explanation:
 * The lexicographically smallest valid sequence of indices is [0, 1, 2]:
 * Change word1[0] to 'a'.
 * word1[1] is already 'b'.
 * word1[2] is already 'c'.
 *
 * Example 2:
 *
 * Input: word1 = "bacdc", word2 = "abc"
 * Output: [1,2,4]
 * Explanation:
 * The lexicographically smallest valid sequence of indices is [1, 2, 4]:
 * word1[1] is already 'a'.
 * Change word1[2] to 'b'.
 * word1[4] is already 'c'.
 *
 * Example 3:
 *
 * Input: word1 = "aaaaaa", word2 = "aaabc"
 * Output: []
 * Explanation:
 * There is no valid sequence of indices.
 *
 * Example 4:
 *
 * Input: word1 = "abc", word2 = "ab"
 * Output: [0,1]
 *
 *
 * Constraints:
 *
 * 1 <= word2.length < word1.length <= 3 * 10^5
 * word1 and word2 consist only of lowercase English letters.
 *
 */
public class FindTheLexicographicallySmallestValidSequence {

    // V0
    // IDEA: SUFFIX MATCH TABLE + GREEDY (SPEND THE ONE CHANGE AS EARLY AS POSSIBLE)
    //       suffixMatch[i] = how many chars of word2, counted from its end, word1[i:]
    //       can match exactly (one backward pass).
    //       forward sweep, taking the earliest index each time:
    //       - word1[i] == word2[j]                       -> take it for free
    //       - change unused and word1[i+1:] can still match word2[j+1:] exactly
    //         (suffixMatch[i+1] >= m - j - 1)            -> spend the change here
    /**
     * time = O(N + M)
     * space = O(N)
     */
    public int[] validSequence(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();

        // suffixMatch[i] = length of the longest suffix of word2 that is a subsequence of word1[i:]
        int[] suffixMatch = new int[n + 1];
        int j = m - 1;
        for (int i = n - 1; i >= 0; i--) {
            suffixMatch[i] = suffixMatch[i + 1];
            if (j >= 0 && word1.charAt(i) == word2.charAt(j)) {
                suffixMatch[i]++;
                j--;
            }
        }

        int[] result = new int[m];
        boolean changeUsed = false;
        j = 0;
        for (int i = 0; i < n && j < m; i++) {
            if (word1.charAt(i) == word2.charAt(j)) {
                result[j] = i;
                j++;
            } else if (!changeUsed && suffixMatch[i + 1] >= m - j - 1) {
                // using the single change here keeps the rest completable
                changeUsed = true;
                result[j] = i;
                j++;
            }
        }

        if (j < m) {
            return new int[0];
        }
        return result;
    }
}
