package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-strictly-increasing-cells-in-a-matrix/description/

import java.util.*;

/**
 * 2713. Maximum Strictly Increasing Cells in a Matrix
 * Hard
 *
 * Given a 1-indexed m x n integer matrix mat, you can select any cell in the matrix as your
 * starting cell.
 *
 * From the starting cell, you can move to any other cell in the same row or column, but only if
 * the value of the destination cell is strictly greater than the value of the current cell. You
 * can repeat this process as many times as possible, moving from cell to cell until you can no
 * longer make any moves.
 *
 * Your task is to find the maximum number of cells that you can visit in the matrix by starting
 * from some cell.
 *
 * Return an integer denoting the maximum number of cells that can be visited.
 *
 *
 * Example 1:
 *
 * Input: mat = [[3,1],[3,4]]
 * Output: 2
 *
 * Example 2:
 *
 * Input: mat = [[1,1],[1,1]]
 * Output: 1
 * Explanation: Since the cells must be strictly increasing, we can only visit one cell in this example.
 *
 * Example 3:
 *
 * Input: mat = [[3,1,6],[-9,5,7]]
 * Output: 4
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 10^5
 * 1 <= m * n <= 10^5
 * -10^5 <= mat[i][j] <= 10^5
 *
 */
public class MaximumStrictlyIncreasingCellsInAMatrix {

    // V0
    // IDEA: SORT CELLS BY VALUE + ROW / COLUMN BEST - dp(cell) = 1 + max(rowBest[r], colBest[c]); EQUAL VALUES IN ONE BATCH
    /**
     * time = O(M * N * log(M * N))
     * space = O(M * N)
     */
    public int maxIncreasingCells(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;

        // group cells by value, in increasing value order
        TreeMap<Integer, List<int[]>> cellsByValue = new TreeMap<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cellsByValue.computeIfAbsent(mat[r][c], key -> new ArrayList<>()).add(new int[]{r, c});
            }
        }

        // rowBest[r] = longest path ending at some already-processed (smaller) cell in row r
        int[] rowBest = new int[rows];
        int[] colBest = new int[cols];
        int result = 0;

        for (List<int[]> cells : cellsByValue.values()) {
            // pass 1: read only - equal values must not feed each other (moves are strictly increasing)
            int[] lengths = new int[cells.size()];
            for (int idx = 0; idx < cells.size(); idx++) {
                int r = cells.get(idx)[0];
                int c = cells.get(idx)[1];
                lengths[idx] = 1 + Math.max(rowBest[r], colBest[c]);
            }

            // pass 2: write back
            for (int idx = 0; idx < cells.size(); idx++) {
                int r = cells.get(idx)[0];
                int c = cells.get(idx)[1];
                rowBest[r] = Math.max(rowBest[r], lengths[idx]);
                colBest[c] = Math.max(colBest[c], lengths[idx]);
                result = Math.max(result, lengths[idx]);
            }
        }
        return result;
    }
}
