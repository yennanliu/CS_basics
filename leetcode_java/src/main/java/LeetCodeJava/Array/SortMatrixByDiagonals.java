package LeetCodeJava.Array;

// https://leetcode.com/problems/sort-matrix-by-diagonals/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 3446. Sort Matrix by Diagonals
 * Medium
 *
 * You are given an n x n square matrix of integers grid. Return the matrix such
 * that:
 *
 * The diagonals in the bottom-left triangle (including the middle diagonal) are
 * sorted in non-increasing order.
 * The diagonals in the top-right triangle are sorted in non-decreasing order.
 *
 * Example 1:
 *
 * Input: grid = [[1,7,3],[9,8,2],[4,5,6]]
 *
 * Output: [[8,2,3],[9,6,7],[4,5,1]]
 *
 * Explanation:
 *
 * The diagonals with a black arrow (bottom-left triangle) should be sorted in non-
 * increasing order:
 *
 * [1, 8, 6] becomes [8, 6, 1].
 * [9, 5] and [4] remain unchanged.
 *
 * The diagonals with a blue arrow (top-right triangle) should be sorted in non-
 * decreasing order:
 *
 * [7, 2] becomes [2, 7].
 * [3] remains unchanged.
 *
 * Example 2:
 *
 * Input: grid = [[0,1],[1,2]]
 *
 * Output: [[2,1],[1,0]]
 *
 * Explanation:
 *
 * The diagonals with a black arrow must be non-increasing, so [0, 2] is changed to
 * [2, 0]. The other diagonals are already in the correct order.
 *
 * Example 3:
 *
 * Input: grid = [[1]]
 *
 * Output: [[1]]
 *
 * Explanation:
 *
 * Diagonals with exactly one element are already in order, so no changes are
 * needed.
 *
 * Constraints:
 *
 * grid.length == grid[i].length == n
 * 1 <= n <= 10
 * -10^5 <= grid[i][j] <= 10^5
 *
 */
public class SortMatrixByDiagonals {

    // V0
    // IDEA: SORT EACH DIAGONAL (bottom-left incl. main: non-increasing; top-right: non-decreasing)
    /**
     * time = O(N^2 log N)
     * space = O(N)
     */
    public int[][] sortMatrix(int[][] grid) {
        int n = grid.length;

        // diagonals starting on the first column: (startRow, 0) -> bottom-left triangle
        for (int startRow = 0; startRow < n; startRow++) {
            sortDiagonal(grid, startRow, 0, true);
        }
        // diagonals starting on the first row, right of the main diagonal -> top-right triangle
        for (int startCol = 1; startCol < n; startCol++) {
            sortDiagonal(grid, 0, startCol, false);
        }
        return grid;
    }

    /** Sorts the down-right diagonal beginning at (row, col) in place. */
    private void sortDiagonal(int[][] grid, int row, int col, boolean descending) {
        int n = grid.length;

        // collect the diagonal top to bottom
        List<Integer> values = new ArrayList<>();
        for (int r = row, c = col; r < n && c < n; r++, c++) {
            values.add(grid[r][c]);
        }

        if (descending) {
            values.sort(Collections.reverseOrder());
        } else {
            Collections.sort(values);
        }

        // write it back in the same order
        int k = 0;
        for (int r = row, c = col; r < n && c < n; r++, c++) {
            grid[r][c] = values.get(k);
            k++;
        }
    }
}
