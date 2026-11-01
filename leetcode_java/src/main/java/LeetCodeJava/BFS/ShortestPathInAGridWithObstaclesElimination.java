package LeetCodeJava.BFS;

// https://leetcode.com/problems/shortest-path-in-a-grid-with-obstacles-elimination/description/

import java.util.*;

/**
 * 1293. Shortest Path in a Grid with Obstacles Elimination
 * Hard
 *
 * You are given an m x n integer matrix grid where each cell is either 0 (empty)
 * or 1 (obstacle). You can move up, down, left, or right from and to an empty cell
 * in one step.
 *
 * Return the minimum number of steps to walk from the upper left corner (0, 0)
 * to the lower right corner (m - 1, n - 1) given that you can eliminate at most k
 * obstacles. If it is not possible to find such walk return -1.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,0,0],[1,1,0],[0,0,0],[0,1,1],[0,0,0]], k = 1
 * Output: 6
 * Explanation:
 * The shortest path without eliminating any obstacle is 10.
 * The shortest path with one obstacle elimination at position (3,2) is 6.
 * Such path is (0,0) -> (0,1) -> (0,2) -> (1,2) -> (2,2) -> (3,2) -> (4,2).
 *
 * Example 2:
 *
 * Input: grid = [[0,1,1],[1,1,1],[1,0,0]], k = 1
 * Output: -1
 * Explanation: We need to eliminate at least two obstacles to find such a walk.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 40
 * 1 <= k <= m * n
 * grid[i][j] is either 0 or 1.
 * grid[0][0] == grid[m - 1][n - 1] == 0
 *
 */
public class ShortestPathInAGridWithObstaclesElimination {

    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    // V0
    // IDEA: BFS ON THE 3D STATE (row, col, eliminations left)
    //  visiting (r, c) is not enough: arriving with MORE eliminations left can be
    //  better later, so visited is keyed by (row, col, remaining).
    /**
     * time = O(M * N * K)
     * space = O(M * N * K)
     */
    public int shortestPath(int[][] grid, int k) {
        int rows = grid.length;
        int cols = grid[0].length;

        // shortcut: with enough eliminations the straight Manhattan path always works
        // (it crosses at most rows + cols - 3 cells besides the two empty corners)
        if (k >= rows + cols - 3) {
            return rows + cols - 2;
        }

        boolean[][][] visited = new boolean[rows][cols][k + 1];
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {0, 0, k});
        visited[0][0][k] = true;

        int steps = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                int[] state = queue.poll();
                int r = state[0];
                int c = state[1];
                int remaining = state[2];
                if (r == rows - 1 && c == cols - 1) {
                    return steps;
                }
                for (int[] dir : DIRECTIONS) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                        continue;
                    }
                    // stepping onto an obstacle (1) spends one elimination
                    int nextRemaining = remaining - grid[nr][nc];
                    if (nextRemaining >= 0 && !visited[nr][nc][nextRemaining]) {
                        visited[nr][nc][nextRemaining] = true;
                        queue.offer(new int[] {nr, nc, nextRemaining});
                    }
                }
            }
            steps++;
        }
        return -1;
    }
}
