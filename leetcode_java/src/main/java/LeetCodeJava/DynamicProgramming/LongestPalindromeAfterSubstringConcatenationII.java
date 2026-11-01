package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-palindrome-after-substring-concatenation-ii/description/

/**
 * 3504. Longest Palindrome After Substring Concatenation II
 * Hard
 *
 * You are given two strings, s and t.
 *
 * You can create a new string by selecting a substring from s (possibly empty) and a
 * substring from t (possibly empty), then concatenating them in order.
 *
 * Return the length of the longest palindrome that can be formed this way.
 *
 *
 * Example 1:
 *
 * Input: s = "a", t = "a"
 * Output: 2
 * Explanation: Concatenating "a" from s and "a" from t results in "aa".
 *
 * Example 2:
 *
 * Input: s = "abc", t = "def"
 * Output: 1
 *
 * Example 3:
 *
 * Input: s = "b", t = "aaaa"
 * Output: 4
 *
 * Example 4:
 *
 * Input: s = "abcde", t = "ecdba"
 * Output: 5
 * Explanation: Concatenating "abc" from s and "ba" from t results in "abcba".
 *
 *
 * Constraints:
 *
 * 1 <= s.length, t.length <= 1000
 * s and t consist of lowercase English letters.
 *
 */
public class LongestPalindromeAfterSubstringConcatenationII {

    // V0
    // IDEA: PALINDROME TABLES + DIAGONAL DP PEELING MATCHED OUTER PAIRS (s[i] == t[j])
    /**
     *  DP def:
     *     - f[i][j] = best palindrome length whose s-part starts at i and t-part ends at j
     *     - bestS[i] = longest palindromic substring of s STARTING at i
     *     - bestT[j] = longest palindromic substring of t ENDING at j
     *
     *  DP eq:
     *     - f[i][j] = max( bestS[i],                         // t side empty
     *                      bestT[j],                         // s side empty
     *                      2 + f[i+1][j-1]  if s[i] == t[j] ) // peel a matched pair
     *     - out-of-range f (i == n or j < 0) is 0
     *
     *  Answer: max over all f[i][j] (and over bestS / bestT alone)
     *
     *  time  = O(N^2 + M^2 + N*M)
     *  space = O(N^2 + M^2)
     */
    public int longestPalindrome(String s, String t) {
        int n = s.length();
        int m = t.length();

        int[] bestS = longestStartingAt(s);
        // longest palindrome ending at j in t == longest starting at (m-1-j) in reversed t
        int[] reversedBest = longestStartingAt(new StringBuilder(t).reverse().toString());
        int[] bestT = new int[m];
        for (int j = 0; j < m; j++) {
            bestT[j] = reversedBest[m - 1 - j];
        }

        int answer = 0;
        for (int value : bestS) {
            answer = Math.max(answer, value);
        }
        for (int value : bestT) {
            answer = Math.max(answer, value);
        }

        // f[i][j]; row n and column "-1" stay 0, handled by the bounds checks
        int[][] f = new int[n + 1][m];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < m; j++) {
                int best = Math.max(bestS[i], bestT[j]);
                if (s.charAt(i) == t.charAt(j)) {
                    int inner = (j > 0) ? f[i + 1][j - 1] : 0;
                    best = Math.max(best, 2 + inner);
                }
                f[i][j] = best;
                answer = Math.max(answer, best);
            }
        }
        return answer;
    }

    /** best[i] = length of the longest palindromic substring of x starting at i. */
    private int[] longestStartingAt(String x) {
        int k = x.length();
        int[] best = new int[k];
        // isPal[i][j] = x[i..j] is a palindrome
        boolean[][] isPal = new boolean[k][k];
        for (int i = k - 1; i >= 0; i--) {
            for (int j = i; j < k; j++) {
                boolean endsMatch = x.charAt(i) == x.charAt(j);
                if (endsMatch && (j - i < 2 || isPal[i + 1][j - 1])) {
                    isPal[i][j] = true;
                    best[i] = Math.max(best[i], j - i + 1);
                }
            }
        }
        return best;
    }
}
