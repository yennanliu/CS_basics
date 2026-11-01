package LeetCodeJava.BFS;

// https://leetcode.com/problems/escape-the-spreading-fire/description/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * 2258. Escape the Spreading Fire
 * Hard
 *
 * You are given a 0-indexed 2D integer array grid of size m x n which represents a field. Each cell has one of three values:
 *
 * 0 represents grass,
 * 1 represents fire,
 * 2 represents a wall that you and fire cannot pass through.
 *
 * You are situated in the top-left cell, (0, 0), and you want to travel to the safehouse at the bottom-right cell, (m - 1, n - 1). Every minute, you may move to an adjacent grass cell. After your move, every fire cell will spread to all adjacent cells that are not walls.
 *
 * Return the maximum number of minutes that you can stay in your initial position before moving while still safely reaching the safehouse. If this is impossible, return -1. If you can always reach the safehouse regardless of the minutes stayed, return 10^9.
 *
 * Note that even if the fire spreads to the safehouse immediately after you have reached it, it will be counted as safely reaching the safehouse.
 *
 * A cell is adjacent to another cell if the former is directly north, east, south, or west of the latter (i.e., their sides are touching).
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,2,0,0,0,0,0],[0,0,0,2,2,1,0],[0,2,0,0,1,2,0],[0,0,2,2,2,0,2],[0,0,0,0,0,0,0]]
 * Output: 3
 * Explanation: The figure above shows the scenario where you stay in the initial position for 3 minutes.
 * You will still be able to safely reach the safehouse.
 * Staying for more than 3 minutes will not allow you to safely reach the safehouse.
 *
 * Example 2:
 *
 * Input: grid = [[0,0,0,0],[0,1,2,0],[0,2,0,0]]
 * Output: -1
 * Explanation: The figure above shows the scenario where you immediately move towards the safehouse.
 * Fire will spread to any cell you move towards and it is impossible to safely reach the safehouse.
 * Thus, -1 is returned.
 *
 * Example 3:
 *
 * Input: grid = [[0,0,0],[2,2,0],[1,2,0]]
 * Output: 1000000000
 * Explanation: The figure above shows the initial grid.
 * Notice that the fire is contained by walls and you will always be able to safely reach the safehouse.
 * Thus, 10^9 is returned.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 2 <= m, n <= 300
 * 4 <= m * n <= 2 * 10^4
 * grid[i][j] is either 0, 1, or 2.
 * grid[0][0] == grid[m - 1][n - 1] == 0
 *
 */
public class EscapeTheSpreadingFire {

    private static final int INF = Integer.MAX_VALUE;
    private static final int ALWAYS_SAFE = 1_000_000_000;
    private static final int[][] DIRECTIONS = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    // V0
    // IDEA: MULTI-SOURCE BFS FOR FIRE ARRIVAL TIMES + BINARY SEARCH ON THE WAIT
    //   step 1: BFS from every fire cell -> fireTime[r][c] (INF if never burnt).
    //   step 2: "can I escape after waiting t minutes?" is monotone in t
    //           (waiting longer never helps), so binary search the largest t.
    //   A walker may enter a cell only strictly before the fire
    //   (arrival < fireTime), except the safehouse where arrival <= fireTime is ok.
    //   If waiting m * n minutes still works, the fire never matters -> 10^9.
    /**
     * time = O(M * N * log(M * N))
     * space = O(M * N)
     */
    public int maximumMinutes(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // step 1: when does the fire reach each cell?
        int[][] fireTime = new int[rows][cols];
        Queue<int[]> queue = new ArrayDeque<>();
        for (int r = 0; r < rows; r++) {
            Arrays.fill(fireTime[r], INF);
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    fireTime[r][c] = 0;
                    queue.offer(new int[] { r, c });
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int[] dir : DIRECTIONS) {
                int nr = cur[0] + dir[0];
                int nc = cur[1] + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                // fire spreads over grass only, and each cell is burnt once
                if (grid[nr][nc] == 0 && fireTime[nr][nc] == INF) {
                    fireTime[nr][nc] = fireTime[cur[0]][cur[1]] + 1;
                    queue.offer(new int[] { nr, nc });
                }
            }
        }

        // step 2: binary search the largest wait that still escapes
        int limit = rows * cols;
        if (canEscape(grid, fireTime, limit)) {
            return ALWAYS_SAFE;
        }
        if (!canEscape(grid, fireTime, 0)) {
            return -1;
        }
        int low = 0;
        int high = limit;
        // invariant: canEscape(low) is true, canEscape(high + 1) is false
        while (low < high) {
            int mid = low + (high - low + 1) / 2;
            if (canEscape(grid, fireTime, mid)) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    /** BFS from (0,0) starting at minute `wait`; true if the safehouse is reached safely. */
    private boolean canEscape(int[][] grid, int[][] fireTime, int wait) {
        int rows = grid.length;
        int cols = grid[0].length;
        // the fire reaches the start while we are still waiting
        if (fireTime[0][0] <= wait) {
            return false;
        }
        boolean[][] visited = new boolean[rows][cols];
        visited[0][0] = true;
        // queue entries: {row, col, minute we stand there}
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] { 0, 0, wait });

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int arrival = cur[2] + 1;
            for (int[] dir : DIRECTIONS) {
                int nr = cur[0] + dir[0];
                int nc = cur[1] + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (grid[nr][nc] != 0 || visited[nr][nc]) {
                    continue;
                }
                // the safehouse tolerates the fire arriving at the same minute
                if (nr == rows - 1 && nc == cols - 1) {
                    if (arrival <= fireTime[nr][nc]) {
                        return true;
                    }
                    continue;
                }
                // any other cell must be entered strictly before the fire
                if (arrival < fireTime[nr][nc]) {
                    visited[nr][nc] = true;
                    queue.offer(new int[] { nr, nc, arrival });
                }
            }
        }
        return false;
    }
}
