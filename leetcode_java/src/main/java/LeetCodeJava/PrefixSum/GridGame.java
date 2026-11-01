package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/grid-game/description/

/**
 * 2017. Grid Game
 * Medium
 *
 * You are given a 0-indexed 2D array grid of size 2 x n, where grid[r][c]
 * represents the number of points at position (r, c) on the matrix.
 * Two robots are playing a game on this matrix.
 *
 * Both robots initially start at (0, 0) and want to reach (1, n-1). Each robot
 * may only move to the right ((r, c) -> (r, c + 1)) or down ((r, c) -> (r + 1, c)).
 *
 * At the start of the game, the first robot moves from (0, 0) to (1, n-1),
 * collecting all the points from the cells on its path. For all cells (r, c)
 * traversed on the path, grid[r][c] is set to 0. Then, the second robot moves
 * from (0, 0) to (1, n-1), collecting the points on its path. Note that their
 * paths may intersect with one another.
 *
 * The first robot wants to minimize the number of points collected by the second
 * robot. In contrast, the second robot wants to maximize the number of points
 * collected. If both robots play optimally, return the number of points collected
 * by the second robot.
 *
 *
 * Example 1:
 *
 * Input: grid = [[2,5,4],[1,5,1]]
 * Output: 4
 * Explanation:
 * the first robot turns down at column 1, so it takes 2 + 5 + 5 + 1 and zeroes
 * those cells. what is left for the second robot is 4 on the top row and 1 on the
 * bottom row, and one path cannot pick up both -> it takes 4.
 *
 * Example 2:
 *
 * Input: grid = [[3,3,1],[8,5,2]]
 * Output: 4
 * Explanation:
 * the first robot turns down at column 0 (3 + 8 + 5 + 2), leaving 3 + 1 on the
 * top row for the second robot.
 *
 * Example 3:
 *
 * Input: grid = [[1,3,1,15],[1,3,3,1]]
 * Output: 7
 * Explanation:
 * the first robot turns down at the last column, leaving 1 + 3 + 3 on the bottom
 * row for the second robot.
 *
 *
 * Constraints:
 *
 * grid.length == 2
 * n == grid[r].length
 * 1 <= n <= 5 * 10^4
 * 1 <= grid[r][c] <= 10^5
 *
 */
public class GridGame {

    // V0
    // IDEA: PREFIX (BOTTOM) + SUFFIX (TOP) SUM OVER THE TURNING COLUMN (min-max)
    // a path in a 2 x n grid goes right on row 0, drops ONCE at some column i, then goes right on row 1,
    // so robot 1's path is just its turning column i. it leaves robot 2 two untouched blocks:
    //   top    = row0[i+1 .. n-1]   (right of the drop)
    //   bottom = row1[0   .. i-1]   (left of the drop)
    // robot 2 also drops once, so it can reach only one block and takes max(top, bottom);
    // robot 1 picks the i that minimises that max. (Dijkstra / greedy max-path for robot 1 does NOT work.)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long gridGame(int[][] grid) {
        // edge case: empty grid
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int n = grid[0].length;

        // top starts as the whole top row and shrinks to row0[i+1 .. n-1]
        long top = 0;
        for (int c = 0; c < n; c++) {
            top += grid[0][c];
        }
        // bottom starts empty and grows to row1[0 .. i-1]
        long bottom = 0;

        long best = Long.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            // column i is on robot 1's path, so it belongs to neither block:
            // drop it from top BEFORE the compare, add it to bottom only AFTER
            top -= grid[0][i];
            long robot2Score = Math.max(top, bottom);
            best = Math.min(best, robot2Score);
            bottom += grid[1][i];
        }
        return best;
    }
}
