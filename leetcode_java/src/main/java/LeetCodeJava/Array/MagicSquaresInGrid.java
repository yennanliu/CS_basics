package LeetCodeJava.Array;

// https://leetcode.com/problems/magic-squares-in-grid/

/**
 *  840. Magic Squares In Grid
 *  Medium
 *
 *  A 3 x 3 magic square is a 3 x 3 grid filled with distinct numbers from 1 to 9
 *  such that each row, column, and both diagonals all have the same sum.
 *
 *  Given a row x col grid of integers, how many 3 x 3 magic square subgrids are
 *  there?
 *
 *  Note: while a magic square can only contain numbers from 1 to 9, grid may
 *  contain numbers up to 15.
 *
 *  Example 1:
 *  Input: grid = [[4,3,8,4],[9,5,1,9],[2,7,6,2]]
 *  Output: 1
 *  (the only magic square is the 3x3 subgrid starting at column 0)
 *
 *  Example 2:
 *  Input: grid = [[8]]
 *  Output: 0
 *
 *  Constraints:
 *   - row == grid.length
 *   - col == grid[i].length
 *   - 1 <= row, col <= 10
 *   - 0 <= grid[i][j] <= 15
 */
public class MagicSquaresInGrid {

    // V0
    // IDEA: try every 3x3 top-left corner, validate distinct 1..9 + equal sums
    /**
     * time = O(m * n)   (each 3x3 check is O(1))
     * space = O(1)
     */
    public int numMagicSquaresInside(int[][] grid) {
        if (grid == null || grid.length < 3 || grid[0].length < 3) {
            return 0;
        }
        int rows = grid.length;
        int cols = grid[0].length;
        int res = 0;
        for (int r = 0; r + 2 < rows; r++) {
            for (int c = 0; c + 2 < cols; c++) {
                if (isMagic(grid, r, c)) {
                    res++;
                }
            }
        }
        return res;
    }

    private boolean isMagic(int[][] g, int r, int c) {
        // must be a permutation of 1..9
        boolean[] seen = new boolean[10];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int v = g[r + i][c + j];
                if (v < 1 || v > 9 || seen[v]) {
                    return false;
                }
                seen[v] = true;
            }
        }
        // all rows / cols / diagonals must sum to 15
        for (int i = 0; i < 3; i++) {
            if (g[r + i][c] + g[r + i][c + 1] + g[r + i][c + 2] != 15) {
                return false;
            }
            if (g[r][c + i] + g[r + 1][c + i] + g[r + 2][c + i] != 15) {
                return false;
            }
        }
        if (g[r][c] + g[r + 1][c + 1] + g[r + 2][c + 2] != 15) {
            return false;
        }
        if (g[r][c + 2] + g[r + 1][c + 1] + g[r + 2][c] != 15) {
            return false;
        }
        return true;
    }
}
