package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/decremental-string-concatenation/description/

import java.util.*;

/**
 * 2746. Decremental String Concatenation
 * Medium
 *
 * You are given a 0-indexed array words containing n strings.
 *
 * Let's define a join operation join(x, y) between two strings x and y as concatenating them into
 * xy. However, if the last character of x is equal to the first character of y, one of them is
 * deleted.
 *
 * For example join("ab", "ba") = "aba" and join("ab", "cde") = "abcde".
 *
 * You are to perform n - 1 join operations. Let str0 = words[0]. Starting from i = 1 up to
 * i = n - 1, for the ith operation, you can do one of the following:
 *
 * Make stri = join(stri - 1, words[i])
 * Make stri = join(words[i], stri - 1)
 *
 * Your task is to minimize the length of strn - 1.
 *
 * Return an integer denoting the minimum possible length of strn - 1.
 *
 *
 * Example 1:
 *
 * Input: words = ["aa","ab","bc"]
 * Output: 4
 * Explanation: str0 = "aa", str1 = join(str0, "ab") = "aab", str2 = join(str1, "bc") = "aabc".
 *
 * Example 2:
 *
 * Input: words = ["ab","b"]
 * Output: 2
 * Explanation: join(str0, "b") = "ab" or join("b", str0) = "bab". The first one has the minimum length.
 *
 * Example 3:
 *
 * Input: words = ["aaa","c","aba"]
 * Output: 6
 * Explanation: str0 = "aaa", str1 = join(str0, "c") = "aaac", str2 = join("aba", str1) = "abaaac".
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 1000
 * 1 <= words[i].length <= 50
 * Each character in words[i] is an English lowercase letter
 *
 */
public class DecrementalStringConcatenation {

    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: DP ON (FIRST CHAR, LAST CHAR) OF THE ACCUMULATED STRING - ONLY THE TWO ENDS DECIDE FUTURE SAVINGS
    /**
     * time = O(N * 26^2)
     * space = O(26^2)
     */
    public int minimizeConcatenatedLength(String[] words) {
        // dp[first][last] = min length of an accumulated string starting with `first`, ending with `last`
        int[][] dp = newTable();
        String start = words[0];
        dp[start.charAt(0) - 'a'][start.charAt(start.length() - 1) - 'a'] = start.length();

        for (int i = 1; i < words.length; i++) {
            String word = words[i];
            int head = word.charAt(0) - 'a';
            int tail = word.charAt(word.length() - 1) - 'a';
            int size = word.length();

            int[][] next = newTable();
            for (int first = 0; first < 26; first++) {
                for (int last = 0; last < 26; last++) {
                    int current = dp[first][last];
                    if (current == INF) {
                        continue;
                    }

                    // accumulated + word: saves one char if last == head; new ends are (first, tail)
                    int appendLength = current + size;
                    if (last == head) {
                        appendLength--;
                    }
                    next[first][tail] = Math.min(next[first][tail], appendLength);

                    // word + accumulated: saves one char if tail == first; new ends are (head, last)
                    int prependLength = current + size;
                    if (tail == first) {
                        prependLength--;
                    }
                    next[head][last] = Math.min(next[head][last], prependLength);
                }
            }
            dp = next;
        }

        int result = INF;
        for (int first = 0; first < 26; first++) {
            for (int last = 0; last < 26; last++) {
                result = Math.min(result, dp[first][last]);
            }
        }
        return result;
    }

    /** A 26 x 26 table filled with INF (unreachable). */
    private int[][] newTable() {
        int[][] table = new int[26][26];
        for (int[] row : table) {
            Arrays.fill(row, INF);
        }
        return table;
    }
}
