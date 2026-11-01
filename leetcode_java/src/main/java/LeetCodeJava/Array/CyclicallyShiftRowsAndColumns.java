package LeetCodeJava.Array;

// https://leetcode.com/problems/cyclically-shift-rows-and-columns/description/

/**
 * 4052. Cyclically Shift Rows and Columns
 * Easy
 *
 * You are given an integer n, a 2D integer array grid of size n x n,
 * and two integer arrays rowShift and colShift, each of length n, where:
 *
 * rowShift[i] represents the number of positions to cyclically shift
 * the i-th row of grid to the left.
 *
 * colShift[j] represents the number of positions to cyclically shift
 * the j-th column of grid upward.
 *
 * First, cyclically shift each row according to rowShift, then cyclically
 * shift each column of the resulting grid according to colShift.
 *
 * Return the resulting grid after performing all the shifts.
 *
 * A cyclic left shift of a row by k positions moves the element at column j
 * to column (j - k + n) % n. All other rows remain unchanged.
 *
 * A cyclic upward shift of a column by k positions moves the element at row i
 * to row (i - k + n) % n. All other columns remain unchanged.
 *
 *
 * Example 1:
 *
 * Input: n = 2, grid = [[1,2],[3,4]], rowShift = [1,0], colShift = [0,1]
 * Output: [[2,4],[3,1]]
 * Explanation:
 * row 0 shifts left by 1 -> [2,1], row 1 stays -> [3,4], so the grid is [[2,1],[3,4]].
 * col 0 stays -> [2,3], col 1 shifts up by 1 -> [4,1], giving [[2,4],[3,1]].
 *
 * Example 2:
 *
 * Input: n = 3, grid = [[1,2,3],[4,5,6],[7,8,9]], rowShift = [1,2,0], colShift = [2,2,1]
 * Output: [[7,8,5],[2,3,9],[6,4,1]]
 * Explanation:
 * after the row shifts the grid is [[2,3,1],[6,4,5],[7,8,9]]
 * (row 0 left by 1, row 1 left by 2, row 2 unchanged).
 * then col 0 = [2,6,7] up by 2 -> [7,2,6], col 1 = [3,4,8] up by 2 -> [8,3,4],
 * col 2 = [1,5,9] up by 1 -> [5,9,1].
 *
 *
 * Constraints:
 *
 * 1 <= n == grid.length == grid[i].length <= 10
 * 1 <= grid[i][j] <= 100
 * rowShift.length == colShift.length == n
 * 0 <= rowShift[i], colShift[i] < n
 *
 */
public class CyclicallyShiftRowsAndColumns {

    // V0
    // IDEA: SIMULATE THE 2 PASSES (ROWS FIRST, THEN COLUMNS), EACH WRITING INTO A FRESH GRID
    //       the destination must be a NEW grid: shifting in place overwrites cells not yet read,
    //       and the column pass must read the grid the row pass produced
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int[][] cyclicShift(int n, int[][] grid, int[] rowShift, int[] colShift) {
        // edge case: nothing to shift
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return grid;
        }

        // pass 1) shift every ROW left: (i, j) lands at (i, (j - k + n) % n)
        int[][] afterRow = new int[n][n];
        for (int i = 0; i < n; i++) {
            int k = rowShift[i];
            for (int j = 0; j < n; j++) {
                afterRow[i][(j - k + n) % n] = grid[i][j];
            }
        }

        // pass 2) shift every COLUMN of the result upward: (i, j) lands at ((i - k + n) % n, j)
        int[][] result = new int[n][n];
        for (int j = 0; j < n; j++) {
            int k = colShift[j];
            for (int i = 0; i < n; i++) {
                result[(i - k + n) % n][j] = afterRow[i][j];
            }
        }

        return result;
    }

    // V0-1
    // IDEA: COMPOSE THE 2 SHIFTS INTO ONE INDEX FORMULA (PULL EACH ANSWER CELL ONCE)
    //       result[i][j] comes from afterRow[(i + colShift[j]) % n][j]  (up-shift read backwards)
    //       afterRow[r][j] comes from grid[r][(j + rowShift[r]) % n]    (left-shift read backwards)
    //       -> one pass, no intermediate grid; note rowShift is indexed by the SOURCE row r
    /**
     * time = O(N^2)
     * space = O(1) extra (excluding the returned grid)
     */
    public int[][] cyclicShift_0_1(int n, int[][] grid, int[] rowShift, int[] colShift) {
        // edge case: nothing to shift
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return grid;
        }

        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // the row this cell sat in before the column pass moved it
                int sourceRow = (i + colShift[j]) % n;
                // the column it sat in before that row's left shift
                int sourceCol = (j + rowShift[sourceRow]) % n;
                result[i][j] = grid[sourceRow][sourceCol];
            }
        }
        return result;
    }
}
