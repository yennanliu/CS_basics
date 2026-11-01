package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-number-of-days-to-disconnect-island/description/

import java.util.*;

/**
 * 1568. Minimum Number of Days to Disconnect Island
 * Hard
 *
 * You are given an m x n binary grid grid where 1 represents land and 0 represents water.
 * An island is a maximal 4-directionally (horizontal or vertical) connected group of 1's.
 *
 * The grid is said to be connected if we have exactly one island, otherwise is said disconnected.
 *
 * In one day, we are allowed to change any single land cell (1) into a water cell (0).
 *
 * Return the minimum number of days to disconnect the grid.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1,1,0],[0,1,1,0],[0,0,0,0]]
 * Output: 2
 * Explanation: We need at least 2 days to get a disconnected grid.
 * Change land grid[1][1] and grid[0][2] to water and get 2 disconnected island.
 *
 * Example 2:
 *
 * Input: grid = [[1,1]]
 * Output: 2
 * Explanation: Grid of full water is also disconnected ([[1,1]] -> [[0,0]]), 0 islands.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 30
 * grid[i][j] is either 0 or 1.
 *
 */
public class MinimumNumberOfDaysToDisconnectIsland {

    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // V0
    // IDEA: BRUTE FORCE + DFS (the answer is always 0, 1 or 2)
    // any island has a corner cell whose (at most) 2 neighbours can be removed, so 2 days
    // always suffice -> only 0 and 1 need testing:
    //   - already disconnected (island count != 1)            -> 0
    //   - flipping one land cell leaves island count != 1     -> 1
    //   - otherwise                                           -> 2
    // NOTE: "disconnected" includes the 0-island case (all water).
    /**
     * time = O((M * N)^2)
     * space = O(M * N)
     */
    public int minDays(int[][] grid) {
        if (countIslands(grid) != 1) {
            return 0;
        }
        int rows = grid.length;
        int cols = grid[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    // try removing this one cell, then restore it
                    grid[r][c] = 0;
                    boolean disconnected = countIslands(grid) != 1;
                    grid[r][c] = 1;
                    if (disconnected) {
                        return 1;
                    }
                }
            }
        }
        return 2;
    }

    /** Counts the 4-directionally connected land groups with an iterative DFS. */
    private int countIslands(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] seen = new boolean[rows][cols];
        int islands = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != 1 || seen[r][c]) {
                    continue;
                }
                islands++;
                Deque<int[]> stack = new ArrayDeque<>();
                stack.push(new int[] {r, c});
                seen[r][c] = true;
                while (!stack.isEmpty()) {
                    int[] cell = stack.pop();
                    for (int[] dir : DIRECTIONS) {
                        int nr = cell[0] + dir[0];
                        int nc = cell[1] + dir[1];
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                                && grid[nr][nc] == 1 && !seen[nr][nc]) {
                            seen[nr][nc] = true;
                            stack.push(new int[] {nr, nc});
                        }
                    }
                }
            }
        }
        return islands;
    }
}
