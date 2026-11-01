package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimize-maximum-value-in-a-grid/description/

import java.util.*;

/**
 * 2371. Minimize Maximum Value in a Grid
 * Hard
 *
 *
 * You are given an m x n integer matrix grid containing distinct positive integers.
 *
 * You have to replace each integer in the matrix with a positive integer satisfying the following conditions:
 *
 * The relative order of every two elements that are in the same row or column should stay the same after the replacements.
 * The maximum number in the matrix after the replacements should be as small as possible.
 *
 * The relative order stays the same if for all pairs of elements in the original matrix such that grid[r1][c1] > grid[r2][c2] where either r1 == r2 or c1 == c2, then it must be true that grid[r1][c1] > grid[r2][c2] after the replacements.
 *
 * For example, if grid = [[2, 4, 5], [7, 3, 9]] then a good replacement could be either grid = [[1, 2, 3], [2, 1, 4]] or grid = [[1, 2, 3], [3, 1, 4]].
 *
 * Return the resulting matrix. If there are multiple answers, return any of them.
 *
 *
 * Example 1:
 *
 * Input: grid = [[3,1],[2,5]]
 * Output: [[2,1],[1,2]]
 * Explanation: The above diagram shows a valid replacement.
 * The maximum number in the matrix is 2. It can be shown that we cannot get a lower value.
 *
 * Example 2:
 *
 * Input: grid = [[10]]
 * Output: [[1]]
 * Explanation: We replace the only number in the matrix with 1.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 1000
 * 1 <= m * n <= 10^5
 * 1 <= grid[i][j] <= 10^9
 * grid[i][j] is unique.
 *
 */
public class MinimizeMaximumValueInAGrid {

    // V0
    // IDEA: ASSIGN VALUES IN INCREASING ORDER, EACH AS SMALL AS ITS ROW / COLUMN ALLOWS
    //       when a cell is processed, every cell already placed in its row or column
    //       is smaller, so the tightest legal value is max(rowMax, colMax) + 1.
    /**
     * time = O(M * N * log(M * N))
     * space = O(M * N)
     */
    public int[][] minScore(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // every cell as {value, row, col}, sorted by original value
        List<int[]> cells = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells.add(new int[] {grid[r][c], r, c});
            }
        }
        cells.sort((a, b) -> Integer.compare(a[0], b[0]));

        // largest value assigned so far in each row / column
        int[] rowMax = new int[rows];
        int[] colMax = new int[cols];
        int[][] result = new int[rows][cols];

        for (int[] cell : cells) {
            int r = cell[1];
            int c = cell[2];
            int value = Math.max(rowMax[r], colMax[c]) + 1;
            result[r][c] = value;
            rowMax[r] = value;
            colMax[c] = value;
        }
        return result;
    }
}
