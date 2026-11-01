package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/twisted-mirror-path-count/description/

import java.util.*;

/**
 * 3665. Twisted Mirror Path Count
 * Medium
 *
 * Given an m x n binary grid grid where:
 *
 * grid[i][j] == 0 represents an empty cell, and
 * grid[i][j] == 1 represents a mirror.
 *
 * A robot starts at the top-left corner of the grid (0, 0) and wants to reach
 * the bottom-right corner (m - 1, n - 1). It can move only right or down. If
 * the robot attempts to move into a mirror cell, it is reflected before
 * entering that cell:
 *
 * If it tries to move right into a mirror, it is turned down and moved into
 * the cell directly below the mirror.
 * If it tries to move down into a mirror, it is turned right and moved into
 * the cell directly to the right of the mirror.
 *
 * If this reflection would cause the robot to move outside the grid
 * boundaries, the path is considered invalid and should not be counted.
 *
 * Return the number of unique valid paths from (0, 0) to (m - 1, n - 1).
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Note: If a reflection moves the robot into a mirror cell, the robot is
 * immediately reflected again based on the direction it used to enter that
 * mirror: if it entered while moving right, it will be turned down; if it
 * entered while moving down, it will be turned right. This process will
 * continue until either the last cell is reached, the robot moves out of
 * bounds or the robot moves to a non-mirror cell.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1,0],[0,0,1],[1,0,0]]
 * Output: 5
 * Explanation:
 *
 * 1  (0, 0) -> (0, 1) [M] -> (1, 1) -> (1, 2) [M] -> (2, 2)
 * 2  (0, 0) -> (0, 1) [M] -> (1, 1) -> (2, 1) -> (2, 2)
 * 3  (0, 0) -> (1, 0) -> (1, 1) -> (1, 2) [M] -> (2, 2)
 * 4  (0, 0) -> (1, 0) -> (1, 1) -> (2, 1) -> (2, 2)
 * 5  (0, 0) -> (1, 0) -> (2, 0) [M] -> (2, 1) -> (2, 2)
 *
 * [M] indicates the robot attempted to enter a mirror cell and instead
 * reflected.
 *
 * Example 2:
 *
 * Input: grid = [[0,0],[0,0]]
 * Output: 2
 * Explanation:
 *
 * 1  (0, 0) -> (0, 1) -> (1, 1)
 * 2  (0, 0) -> (1, 0) -> (1, 1)
 *
 * Example 3:
 *
 * Input: grid = [[0,1,1],[1,1,0]]
 * Output: 1
 * Explanation:
 *
 * 1  (0, 0) -> (0, 1) [M] -> (1, 1) [M] -> (1, 2)
 *
 * (0, 0) -> (1, 0) [M] -> (1, 1) [M] -> (2, 1) goes out of bounds, so it is
 * invalid.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 2 <= m, n <= 500
 * grid[i][j] is either 0 or 1.
 * grid[0][0] == grid[m - 1][n - 1] == 0
 *
 */
public class TwistedMirrorPathCount {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: GRID DP ON THE FLOW THROUGH EACH CELL, SPLIT BY DIRECTION OF TRAVEL
    //   a mirror is never a resting place, so count the paths CROSSING each cell:
    //     enteredMovingRight[i][j] = paths entering (i, j) from the left
    //     enteredMovingDown[i][j]  = paths entering (i, j) from above
    //   empty cell : the robot picks its next move freely ->
    //                outRight = outDown = movingRight + movingDown
    //   mirror cell: it is forced to turn -> outRight = movingDown, outDown = movingRight
    //   flow leaving the grid is never read again, so invalid paths die for free.
    //   one row is rolled: right[c] holds the flow into column c from the left,
    //   down[c] the flow into column c from the row above.
    /**
     * time = O(M * N)
     * space = O(N)
     */
    public int uniquePaths(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        // down[c] = flow arriving at (row, c) from above
        long[] down = new long[n];
        // one path enters (0, 0) "from above"
        down[0] = 1;
        long answer = 0;

        for (int r = 0; r < m; r++) {
            // flow arriving at (r, c) from the left
            long fromLeft = 0;
            for (int c = 0; c < n; c++) {
                long fromAbove = down[c];
                if (r == m - 1 && c == n - 1) {
                    // reaching the target counts, whatever direction we came from
                    answer = (fromLeft + fromAbove) % MOD;
                }
                long outRight;
                long outDown;
                if (grid[r][c] == 1) {
                    // mirror: right turns into down and down turns into right
                    outRight = fromAbove;
                    outDown = fromLeft;
                } else {
                    long total = (fromLeft + fromAbove) % MOD;
                    outRight = total;
                    outDown = total;
                }
                down[c] = outDown;
                fromLeft = outRight;
            }
        }
        return (int) answer;
    }
}
