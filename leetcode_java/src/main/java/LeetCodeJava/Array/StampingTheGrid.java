package LeetCodeJava.Array;

// https://leetcode.com/problems/stamping-the-grid/description/

/**
 * 2132. Stamping the Grid
 * Hard
 *
 * You are given an m x n binary matrix grid where each cell is either 0 (empty) or 1 (occupied).
 *
 * You are then given stamps of size stampHeight x stampWidth. We want to fit the stamps such that they follow the given restrictions and requirements:
 *
 * Cover all the empty cells.
 * Do not cover any of the occupied cells.
 * We can put as many stamps as we want.
 * Stamps can overlap with each other.
 * Stamps are not allowed to be rotated.
 * Stamps must stay completely inside the grid.
 *
 * Return true if it is possible to fit the stamps while following the given restrictions and requirements. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,0,0],[1,0,0,0],[1,0,0,0],[1,0,0,0],[1,0,0,0]], stampHeight = 4, stampWidth = 3
 * Output: true
 * Explanation: We have two overlapping stamps (labeled 1 and 2 in the image) that are able to cover all the empty cells.
 *
 * Example 2:
 *
 * Input: grid = [[1,0,0,0],[0,1,0,0],[0,0,1,0],[0,0,0,1]], stampHeight = 2, stampWidth = 2
 * Output: false
 * Explanation: There is no way to fit the stamps onto all the empty cells without the stamps going outside the grid.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[r].length
 * 1 <= m, n <= 10^5
 * 1 <= m * n <= 2 * 10^5
 * grid[r][c] is either 0 or 1.
 * 1 <= stampHeight, stampWidth <= 10^5
 *
 */
public class StampingTheGrid {

    // V0
    // IDEA: 2D PREFIX SUM (WHERE CAN A STAMP GO?) + 2D DIFFERENCE ARRAY (WHAT IS COVERED?)
    /**
     *  Stamps may overlap, so greedily put a stamp EVERYWHERE it legally fits,
     *  then check that no empty cell is left uncovered.
     *   pass 1: prefix sums of grid -> a stamp at top-left (i, j) is legal iff
     *           its footprint sums to 0 (no occupied cell)
     *   pass 2: for each legal stamp, +1 its rectangle in a 2D difference array
     *           (4 corner updates instead of filling the rectangle)
     *   pass 3: prefix-sum the difference array into coverage counts and fail on
     *           any empty cell with coverage 0
     *
     * time = O(M * N)
     * space = O(M * N)
     */
    public boolean possibleToStamp(int[][] grid, int stampHeight, int stampWidth) {
        int rows = grid.length;
        int cols = grid[0].length;

        // occupied[i][j] = number of 1s in grid[0 .. i-1][0 .. j-1]
        int[][] occupied = new int[rows + 1][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                occupied[i + 1][j + 1] = grid[i][j] + occupied[i][j + 1] + occupied[i + 1][j] - occupied[i][j];
            }
        }

        int[][] diff = new int[rows + 1][cols + 1];
        for (int i = 0; i + stampHeight <= rows; i++) {
            for (int j = 0; j + stampWidth <= cols; j++) {
                // exclusive bottom-right corner of the stamp
                int bottom = i + stampHeight;
                int right = j + stampWidth;
                int inside = occupied[bottom][right] - occupied[i][right] - occupied[bottom][j] + occupied[i][j];
                if (inside == 0) {
                    diff[i][j] += 1;
                    diff[i][right] -= 1;
                    diff[bottom][j] -= 1;
                    diff[bottom][right] += 1;
                }
            }
        }

        // coverage[i + 1][j + 1] = how many stamps cover cell (i, j)
        int[][] coverage = new int[rows + 1][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                coverage[i + 1][j + 1] = diff[i][j] + coverage[i][j + 1] + coverage[i + 1][j] - coverage[i][j];
                if (grid[i][j] == 0 && coverage[i + 1][j + 1] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
