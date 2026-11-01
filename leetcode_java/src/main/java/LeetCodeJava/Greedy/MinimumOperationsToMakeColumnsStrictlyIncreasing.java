package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-operations-to-make-columns-strictly-increasing/description/

/**
 * 3402. Minimum Operations to Make Columns Strictly Increasing
 * Easy
 *
 * You are given a m x n matrix grid consisting of non-negative integers.
 *
 * In one operation, you can increment the value of any grid[i][j] by 1.
 *
 * Return the minimum number of operations needed to make all columns of grid
 * strictly increasing.
 *
 * Example 1:
 *
 * Input: grid = [[3,2],[1,3],[3,4],[0,1]]
 *
 * Output: 15
 *
 * Explanation:
 *
 * To make the 0^th column strictly increasing, we can apply 3 operations on
 * grid[1][0], 2 operations on grid[2][0], and 6 operations on grid[3][0].
 * To make the 1^st column strictly increasing, we can apply 4 operations on
 * grid[3][1].
 *
 * Example 2:
 *
 * Input: grid = [[3,2,1],[2,1,0],[1,2,3]]
 *
 * Output: 12
 *
 * Explanation:
 *
 * To make the 0^th column strictly increasing, we can apply 2 operations on
 * grid[1][0], and 4 operations on grid[2][0].
 * To make the 1^st column strictly increasing, we can apply 2 operations on
 * grid[1][1], and 2 operations on grid[2][1].
 * To make the 2^nd column strictly increasing, we can apply 2 operations on
 * grid[1][2].
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 50
 * 0 <= grid[i][j] < 2500
 *
 */
public class MinimumOperationsToMakeColumnsStrictlyIncreasing {

    // V0
    // IDEA: PER-COLUMN GREEDY — RAISE EACH CELL TO THE MINIMUM LEGAL VALUE
    //       columns are independent. values only go up, so each cell must reach
    //       at least (previous final value + 1); raising it more only costs more.
    /**
     * time = O(M * N)
     * space = O(1)
     */
    public int minimumOperations(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int operations = 0;

        for (int c = 0; c < cols; c++) {
            int previous = grid[0][c];
            for (int r = 1; r < rows; r++) {
                int needed = previous + 1;
                if (grid[r][c] < needed) {
                    operations += needed - grid[r][c];
                    previous = needed;
                } else {
                    previous = grid[r][c];
                }
            }
        }
        return operations;
    }
}
