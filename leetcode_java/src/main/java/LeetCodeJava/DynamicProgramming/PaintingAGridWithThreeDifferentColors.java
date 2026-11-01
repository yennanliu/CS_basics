package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/painting-a-grid-with-three-different-colors/description/

import java.util.*;

/**
 * 1931. Painting a Grid With Three Different Colors
 * Hard
 *
 * You are given two integers m and n. Consider an m x n grid where each cell is initially white.
 * You can paint each cell red, green, or blue. All cells must be painted.
 *
 * Return the number of ways to color the grid with no two adjacent cells having the same color.
 * Since the answer can be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: m = 1, n = 1
 * Output: 3
 *
 * Example 2:
 *
 * Input: m = 1, n = 2
 * Output: 6
 *
 * Example 3:
 *
 * Input: m = 5, n = 5
 * Output: 580986
 *
 *
 * Constraints:
 *
 * 1 <= m <= 5
 * 1 <= n <= 1000
 *
 */
public class PaintingAGridWithThreeDifferentColors {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COLUMN-STATE DP (BASE-3 ENCODED COLUMNS)
    //
    //   m <= 5, so one column of m cells is a base-3 number in [0, 3^m).
    //   step 1: keep only the VALID columns (no two vertically adjacent cells equal).
    //   step 2: two valid columns can sit side by side if they differ in every row.
    //   dp[x] = ways to paint the columns so far with the last column = x.
    //     first column: dp[x] = 1;  next: dp'[x] = sum of dp[y] over compatible y.
    //   answer = sum of dp after n columns.
    /**
     * time = O(3^(2M) + N * V^2), V = number of valid columns (48 for M = 5)
     * space = O(V^2)
     */
    public int colorTheGrid(int m, int n) {
        int totalColumns = 1;
        for (int i = 0; i < m; i++) {
            totalColumns *= 3;
        }

        // step 1: collect the valid columns and their per-row colors
        List<int[]> validColumns = new ArrayList<>();
        for (int code = 0; code < totalColumns; code++) {
            int[] colors = toDigits(code, m);
            boolean ok = true;
            for (int row = 0; row + 1 < m; row++) {
                if (colors[row] == colors[row + 1]) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                validColumns.add(colors);
            }
        }
        int count = validColumns.size();

        // step 2: compatible[a][b] = columns a and b differ in every row
        boolean[][] compatible = new boolean[count][count];
        for (int a = 0; a < count; a++) {
            for (int b = 0; b < count; b++) {
                boolean ok = true;
                for (int row = 0; row < m; row++) {
                    if (validColumns.get(a)[row] == validColumns.get(b)[row]) {
                        ok = false;
                        break;
                    }
                }
                compatible[a][b] = ok;
            }
        }

        // step 3: roll the DP column by column
        long[] dp = new long[count];
        Arrays.fill(dp, 1);
        for (int col = 1; col < n; col++) {
            long[] next = new long[count];
            for (int a = 0; a < count; a++) {
                long total = 0;
                for (int b = 0; b < count; b++) {
                    if (compatible[a][b]) {
                        total += dp[b];
                    }
                }
                next[a] = total % MOD;
            }
            dp = next;
        }

        long answer = 0;
        for (long ways : dp) {
            answer = (answer + ways) % MOD;
        }
        return (int) answer;
    }

    /** Decodes a base-3 column code into m row colors (0, 1, 2). */
    private int[] toDigits(int code, int m) {
        int[] digits = new int[m];
        for (int row = 0; row < m; row++) {
            digits[row] = code % 3;
            code /= 3;
        }
        return digits;
    }
}
