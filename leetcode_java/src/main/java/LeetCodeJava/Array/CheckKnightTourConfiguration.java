package LeetCodeJava.Array;

// https://leetcode.com/problems/check-knight-tour-configuration/description/

/**
 * 2596. Check Knight Tour Configuration
 * Medium
 *
 * There is a knight on an n x n chessboard. In a valid configuration, the knight starts at the top-left cell of the board and visits every cell on the board exactly once.
 *
 * You are given an n x n integer matrix grid consisting of distinct integers from the range [0, n * n - 1] where grid[row][col] indicates that the cell (row, col) is the grid[row][col]th cell that the knight visited. The moves are 0-indexed.
 *
 * Return true if grid represents a valid configuration of the knight's movements or false otherwise.
 *
 * Note that a valid knight move consists of moving two squares vertically and one square horizontally, or two squares horizontally and one square vertically.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,11,16,5,20],[17,4,19,10,15],[12,1,8,21,6],[3,18,23,14,9],[24,13,2,7,22]]
 * Output: true
 * Explanation: The above diagram represents the grid. It can be shown that it is a valid configuration.
 *
 * Example 2:
 *
 * Input: grid = [[0,3,6],[5,8,1],[2,7,4]]
 * Output: false
 * Explanation: The above diagram represents the grid. The 8th move of the knight is not valid considering its position after the 7th move.
 *
 *
 * Constraints:
 *
 * n == grid.length == grid[i].length
 * 3 <= n <= 7
 * 0 <= grid[row][col] < n * n
 * All integers in grid are unique.
 *
 */
public class CheckKnightTourConfiguration {

    // V0
    // IDEA: INVERT THE GRID INTO A VISIT ORDER, THEN CHECK EACH STEP
    //       grid maps cell -> visit time; build the opposite (time -> cell), then every
    //       consecutive pair of cells must differ by a knight jump: {|dr|, |dc|} == {1, 2}.
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public boolean checkValidGrid(int[][] grid) {
        // the tour must start at the top-left cell
        if (grid[0][0] != 0) {
            return false;
        }

        int n = grid.length;
        // rowAt[t], colAt[t] = the cell visited at step t
        int[] rowAt = new int[n * n];
        int[] colAt = new int[n * n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                rowAt[grid[r][c]] = r;
                colAt[grid[r][c]] = c;
            }
        }

        for (int t = 1; t < n * n; t++) {
            int rowDiff = Math.abs(rowAt[t] - rowAt[t - 1]);
            int colDiff = Math.abs(colAt[t] - colAt[t - 1]);
            // a knight jump is 2 one way and 1 the other
            boolean isKnightMove = (rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1);
            if (!isKnightMove) {
                return false;
            }
        }
        return true;
    }
}
