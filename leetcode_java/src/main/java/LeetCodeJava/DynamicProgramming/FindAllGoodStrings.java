package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-all-good-strings/description/

import java.util.Arrays;

/**
 * 1397. Find All Good Strings
 * Hard
 *
 * Given the strings s1 and s2 of size n and the string evil, return the number of good
 * strings.
 *
 * A good string has size n, it is alphabetically greater than or equal to s1, it is
 * alphabetically smaller than or equal to s2, and it does not contain the string evil as
 * a substring. Since the answer can be a huge number, return this modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 2, s1 = "aa", s2 = "da", evil = "b"
 * Output: 51
 * Explanation: There are 25 good strings starting with 'a': "aa","ac","ad",...,"az". Then
 * there are 25 good strings starting with 'c': "ca","cc","cd",...,"cz" and finally there
 * is one good string starting with 'd': "da".
 *
 * Example 2:
 *
 * Input: n = 8, s1 = "leetcode", s2 = "leetgoes", evil = "leet"
 * Output: 0
 * Explanation: All strings greater than or equal to s1 and smaller than or equal to s2
 * start with the prefix "leet", therefore, there is not any good string.
 *
 * Example 3:
 *
 * Input: n = 2, s1 = "gx", s2 = "gz", evil = "x"
 * Output: 2
 *
 *
 * Constraints:
 *
 * s1.length == n
 * s2.length == n
 * s1 <= s2
 * 1 <= n <= 500
 * 1 <= evil.length <= 50
 * All strings consist of lowercase English letters.
 *
 */
public class FindAllGoodStrings {

    private static final int MOD = 1_000_000_007;
    private static final int ALPHABET = 26;

    // V0
    // IDEA: DIGIT DP + KMP AUTOMATON (BUILD THE STRING LEFT TO RIGHT)
    /**
     * count(i, matched, tightLow, tightHigh) = ways to fill positions i..n-1, where
     *   matched   = how many chars of evil the current suffix already matches
     *               (matched == evil.length -> evil appeared -> 0 ways)
     *   tightLow  = the prefix still equals s1's prefix -> next char >= s1[i]
     *   tightHigh = the prefix still equals s2's prefix -> next char <= s2[i]
     *
     * count(i, matched, lo, hi) = sum over c in [low .. high] of
     *   count(i + 1, next[matched][c], lo && c == low, hi && c == high)
     *
     * next[matched][c] is the KMP transition (precomputed from the failure function), so
     * each step is O(1). Both bounds are tracked: a prefix can hug s1 and s2 at once.
     *
     * base: i == n -> 1
     */
    /**
     * time = O(N * M * 26), M = evil.length
     * space = O(N * M)
     */
    public int findGoodStrings(int n, String s1, String s2, String evil) {
        int[][] transition = buildKmpTransitions(evil);

        // memo[i][matched][tightLow][tightHigh], -1 = not computed
        int[][][][] memo = new int[n][evil.length()][2][2];
        for (int[][][] byMatched : memo) {
            for (int[][] byLow : byMatched) {
                for (int[] byHigh : byLow) {
                    Arrays.fill(byHigh, -1);
                }
            }
        }
        return count(0, 0, 1, 1, n, s1, s2, evil, transition, memo);
    }

    /** Number of good completions of positions i..n-1 from this state (tight flags are 0/1). */
    private int count(int i, int matched, int tightLow, int tightHigh, int n, String s1, String s2,
                      String evil, int[][] transition, int[][][][] memo) {
        // evil fully matched: this branch contains it
        if (matched == evil.length()) {
            return 0;
        }
        // every position filled without ever completing evil
        if (i == n) {
            return 1;
        }
        if (memo[i][matched][tightLow][tightHigh] != -1) {
            return memo[i][matched][tightLow][tightHigh];
        }

        // the range of letters allowed at position i
        int low = (tightLow == 1) ? s1.charAt(i) - 'a' : 0;
        int high = (tightHigh == 1) ? s2.charAt(i) - 'a' : ALPHABET - 1;

        long ways = 0;
        for (int c = low; c <= high; c++) {
            // still hugging a bound only if we were, and picked exactly that bound's letter
            int nextLow = (tightLow == 1 && c == low) ? 1 : 0;
            int nextHigh = (tightHigh == 1 && c == high) ? 1 : 0;
            ways += count(i + 1, transition[matched][c], nextLow, nextHigh,
                    n, s1, s2, evil, transition, memo);
        }
        int result = (int) (ways % MOD);
        memo[i][matched][tightLow][tightHigh] = result;
        return result;
    }

    /** transition[state][c] = matched length of evil after appending letter c in `state`. */
    private int[][] buildKmpTransitions(String evil) {
        int m = evil.length();

        // KMP failure function: lps[i] = longest proper prefix of evil[0..i] that is also a suffix
        int[] lps = new int[m];
        int length = 0;
        for (int i = 1; i < m; i++) {
            while (length > 0 && evil.charAt(i) != evil.charAt(length)) {
                length = lps[length - 1];
            }
            if (evil.charAt(i) == evil.charAt(length)) {
                length++;
            }
            lps[i] = length;
        }

        int[][] transition = new int[m][ALPHABET];
        for (int state = 0; state < m; state++) {
            for (int c = 0; c < ALPHABET; c++) {
                char letter = (char) ('a' + c);
                // fall back along the failure links until letter extends the match
                int k = state;
                while (k > 0 && evil.charAt(k) != letter) {
                    k = lps[k - 1];
                }
                if (evil.charAt(k) == letter) {
                    k++;
                }
                transition[state][c] = k;
            }
        }
        return transition;
    }
}
