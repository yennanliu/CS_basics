package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-paint-n-3-grid/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1411. Number of Ways to Paint N × 3 Grid
 * Hard
 *
 * You have a grid of size n x 3 and you want to paint each cell of the grid with
 * exactly one of the three colors: Red, Yellow, or Green while making sure that no
 * two adjacent cells have the same color (i.e., no two cells that share vertical or
 * horizontal sides have the same color).
 *
 * Given n the number of rows of the grid, return the number of ways you can paint
 * this grid. As the answer may grow large, the answer must be computed modulo
 * 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: 12
 * Explanation: There are 12 possible way to paint the grid as shown.
 *
 * Example 2:
 *
 * Input: n = 5000
 * Output: 30228214
 *
 *
 * Constraints:
 *
 * n == grid.length
 * 1 <= n <= 5000
 *
 */
public class NumberOfWaysToPaintN3Grid {

    private static final int MOD = 1_000_000_007;
    private static final int COLORS = 3;
    private static final int COLUMNS = 3;

    // V0
    // IDEA: DP ON ROW SHAPE (ONLY 2 SHAPES MATTER)
    /**
     * a legal row of 3 cells is either
     *   "ABA" (the two ends match)  -> 6 rows (3 * 2)
     *   "ABC" (all three distinct)  -> 6 rows (3!)
     *
     * counting the legal rows that can sit under each shape:
     *   ABA -> 3 x ABA + 2 x ABC
     *   ABC -> 2 x ABA + 2 x ABC
     *
     * endAba' = 3 * endAba + 2 * endAbc
     * endAbc' = 2 * endAba + 2 * endAbc
     *
     * base: endAba = endAbc = 6 for one row
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numOfWays(int n) {
        long endAba = 6;
        long endAbc = 6;
        for (int row = 2; row <= n; row++) {
            long nextAba = (3 * endAba + 2 * endAbc) % MOD;
            long nextAbc = (2 * endAba + 2 * endAbc) % MOD;
            endAba = nextAba;
            endAbc = nextAbc;
        }
        return (int) ((endAba + endAbc) % MOD);
    }

    // V1
    // IDEA: STATE COMPRESSION DP OVER ALL 3^3 = 27 ROW COLORINGS
    /**
     * A row is encoded in base 3 (one digit per cell). Keep only the 12 rows with no two
     * equal neighbours, precompute which rows may sit on top of each other (no equal
     * colour in any column), then count row by row. Slower, but generalises to wider grids.
     */
    /**
     * time = O(N * 12^2)
     * space = O(12^2)
     */
    public int numOfWays_1(int n) {
        int totalCodes = COLORS * COLORS * COLORS;

        // the rows with no two horizontally adjacent cells sharing a colour
        List<Integer> validRows = new ArrayList<>();
        for (int code = 0; code < totalCodes; code++) {
            if (isValidRow(code)) {
                validRows.add(code);
            }
        }
        int rowCount = validRows.size();

        // compatible[a][b]: row b may sit directly under row a
        boolean[][] compatible = new boolean[rowCount][rowCount];
        for (int a = 0; a < rowCount; a++) {
            for (int b = 0; b < rowCount; b++) {
                compatible[a][b] = noColumnMatches(validRows.get(a), validRows.get(b));
            }
        }

        // ways[a] = number of paintings whose last row is validRows[a]
        long[] ways = new long[rowCount];
        for (int a = 0; a < rowCount; a++) {
            ways[a] = 1;
        }
        for (int row = 2; row <= n; row++) {
            long[] next = new long[rowCount];
            for (int a = 0; a < rowCount; a++) {
                for (int b = 0; b < rowCount; b++) {
                    if (compatible[a][b]) {
                        next[b] = (next[b] + ways[a]) % MOD;
                    }
                }
            }
            ways = next;
        }

        long total = 0;
        for (long count : ways) {
            total = (total + count) % MOD;
        }
        return (int) total;
    }

    /** True if no two adjacent cells of the base-3 encoded row share a colour. */
    private boolean isValidRow(int code) {
        int previousColor = -1;
        for (int column = 0; column < COLUMNS; column++) {
            int color = code % COLORS;
            if (color == previousColor) {
                return false;
            }
            previousColor = color;
            code /= COLORS;
        }
        return true;
    }

    /** True if the two encoded rows differ in every column. */
    private boolean noColumnMatches(int upper, int lower) {
        for (int column = 0; column < COLUMNS; column++) {
            if (upper % COLORS == lower % COLORS) {
                return false;
            }
            upper /= COLORS;
            lower /= COLORS;
        }
        return true;
    }
}
