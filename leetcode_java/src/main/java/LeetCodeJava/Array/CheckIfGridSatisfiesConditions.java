package LeetCodeJava.Array;

// https://leetcode.com/problems/check-if-grid-satisfies-conditions/description/

/**
 * 3142. Check if Grid Satisfies Conditions
 * Easy
 *
 * You are given a 2D matrix grid of size m x n. You need to check if each cell grid[i][j] is:
 *
 * Equal to the cell below it, i.e. grid[i][j] == grid[i + 1][j] (if it exists).
 * Different from the cell to its right, i.e. grid[i][j] != grid[i][j + 1] (if it exists).
 *
 * Return true if all the cells satisfy these conditions, otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,2],[1,0,2]]
 * Output: true
 * Explanation:
 * All the cells in the grid satisfy the conditions.
 *
 * Example 2:
 *
 * Input: grid = [[1,1,1],[0,0,0]]
 * Output: false
 * Explanation:
 * All cells in the first row are equal.
 *
 * Example 3:
 *
 * Input: grid = [[1],[2],[3]]
 * Output: false
 * Explanation:
 * Cells in the first column have different values.
 *
 *
 * Constraints:
 *
 * 1 <= n, m <= 10
 * 0 <= grid[i][j] <= 9
 *
 */
public class CheckIfGridSatisfiesConditions {

    // V0
    // IDEA: TWO LOCAL CHECKS PER CELL, GUARDED BY THE BOUNDS
    // "equal below" is vacuous on the last row, "different to the right" on the last column.
    /**
     * time = O(M * N)
     * space = O(1)
     */
    public boolean satisfiesConditions(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r + 1 < rows && grid[r][c] != grid[r + 1][c]) {
                    return false;
                }
                if (c + 1 < cols && grid[r][c] == grid[r][c + 1]) {
                    return false;
                }
            }
        }
        return true;
    }
}
