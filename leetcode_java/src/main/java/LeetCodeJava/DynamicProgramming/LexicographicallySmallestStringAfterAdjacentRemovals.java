package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/lexicographically-smallest-string-after-adjacent-removals/description/

import java.util.*;

/**
 * 3563. Lexicographically Smallest String After Adjacent Removals
 * Hard
 *
 * You are given a string s consisting of lowercase English letters.
 *
 * You can perform the following operation any number of times (including zero):
 *
 * Remove any pair of adjacent characters in the string that are consecutive in the alphabet, where the alphabet is treated as cyclic ('a' and 'z' are considered consecutive).
 * Shift the remaining characters to the left to fill the gap.
 *
 * Return the lexicographically smallest string that can be obtained after performing the operations optimally.
 *
 * Note: Consider the alphabet as circular, thus 'a' and 'z' are consecutive.
 *
 *
 * Example 1:
 *
 * Input: s = "abc"
 * Output: "a"
 * Explanation:
 * Remove "bc" from the string, leaving "a" as the remaining string. No further operations are possible. Thus, the lexicographically smallest achievable string is "a".
 *
 * Example 2:
 *
 * Input: s = "bcda"
 * Output: ""
 * Explanation:
 * Remove "cd" from the string, leaving "ba" as the remaining string. Remove "ba" from the string, leaving "" as the remaining string. Thus, the lexicographically smallest achievable string is "".
 *
 * Example 3:
 *
 * Input: s = "zdce"
 * Output: "zdce"
 * Explanation:
 * Remove "dc" from the string, leaving "ze" as the remaining string. No further operations are possible on "ze". However, since "ze" is lexicographically larger than "zdce", the smallest achievable string is "zdce".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 250
 * s consists only of lowercase English letters.
 *
 */
public class LexicographicallySmallestStringAfterAdjacentRemovals {

    // V0
    // IDEA: INTERVAL DP FOR "BLOCK CAN VANISH" + SUFFIX DP FOR THE SMALLEST TAIL
    //   canVanish[i][j]: s[i..j] can be removed completely (only even lengths can).
    //     s[i] must be removed together with some s[m] (m - i odd) adjacent in the
    //     alphabet, after s[i+1..m-1] vanished; then s[m+1..j] vanishes on its own:
    //     canVanish[i][j] = OR over m of adjacent(s[i], s[m]) && vanish(i+1, m-1) && vanish(m+1, j)
    //   best[i]: smallest string obtainable from s[i..]:
    //     best[i] = min( s[i] + best[i+1], best[j+1] for every j with canVanish[i][j] )
    /**
     * time = O(N^3)
     * space = O(N^2)
     */
    public String lexicographicallySmallestString(String s) {
        int n = s.length();
        boolean[][] canVanish = new boolean[n][n];

        for (int length = 2; length <= n; length += 2) {
            for (int i = 0; i + length - 1 < n; i++) {
                int j = i + length - 1;
                // s[i] pairs with s[m]; the inside and the rest must each vanish
                for (int m = i + 1; m <= j; m += 2) {
                    if (isAdjacent(s.charAt(i), s.charAt(m))
                            && vanishes(canVanish, i + 1, m - 1)
                            && vanishes(canVanish, m + 1, j)) {
                        canVanish[i][j] = true;
                        break;
                    }
                }
            }
        }

        // best[i] = lexicographically smallest result for the suffix s[i..]
        String[] best = new String[n + 1];
        best[n] = "";
        for (int i = n - 1; i >= 0; i--) {
            // keep s[i]
            String candidate = s.charAt(i) + best[i + 1];
            // or erase a vanishing block s[i..j]
            for (int j = i + 1; j < n; j += 2) {
                if (canVanish[i][j] && best[j + 1].compareTo(candidate) < 0) {
                    candidate = best[j + 1];
                }
            }
            best[i] = candidate;
        }
        return best[0];
    }

    /** An empty range (i > j) vanishes trivially. */
    private boolean vanishes(boolean[][] canVanish, int i, int j) {
        if (i > j) {
            return true;
        }
        return canVanish[i][j];
    }

    /** Consecutive letters in the circular alphabet ('a' and 'z' are adjacent too). */
    private boolean isAdjacent(char a, char b) {
        int diff = Math.abs(a - b);
        return diff == 1 || diff == 25;
    }
}
