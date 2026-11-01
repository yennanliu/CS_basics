package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-number-of-flips-to-make-binary-grid-palindromic-i/description/

/**
 * 3239. Minimum Number of Flips to Make Binary Grid Palindromic I
 * Medium
 *
 * You are given an m x n binary matrix grid.
 *
 * A row or column is considered palindromic if its values read the same forward and backward.
 *
 * You can flip any number of cells in grid from 0 to 1, or from 1 to 0.
 *
 * Return the minimum number of cells that need to be flipped to make either all rows palindromic or all columns palindromic.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,0],[0,0,0],[0,0,1]]
 * Output: 2
 * Explanation:
 * Flipping the highlighted cells makes all the rows palindromic.
 *
 * Example 2:
 *
 * Input: grid = [[0,1],[0,1],[0,0]]
 * Output: 1
 * Explanation:
 * Flipping the highlighted cell makes all the columns palindromic.
 *
 * Example 3:
 *
 * Input: grid = [[1],[0]]
 * Output: 0
 * Explanation:
 * All rows are already palindromic.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m * n <= 2 * 10^5
 *
 */
public class MinimumNumberOfFlipsToMakeBinaryGridPalindromicI {

    // V0
    // IDEA: COUNT MISMATCHED MIRROR PAIRS FOR ROWS AND FOR COLUMNS, TAKE THE SMALLER
    /**
     * time = O(M * N)
     * space = O(1)
     */
    public int minFlips(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // each mismatched mirror pair inside a row costs exactly one flip
        int rowCost = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols / 2; c++) {
                if (grid[r][c] != grid[r][cols - 1 - c]) {
                    rowCost++;
                }
            }
        }

        // same count taken down the columns
        int colCost = 0;
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows / 2; r++) {
                if (grid[r][c] != grid[rows - 1 - r][c]) {
                    colCost++;
                }
            }
        }

        // either orientation is enough
        return Math.min(rowCost, colCost);
    }
}
