package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-time-to-visit-a-cell-in-a-grid/description/

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 2577. Minimum Time to Visit a Cell In a Grid
 * Hard
 *
 * You are given a m x n matrix grid consisting of non-negative integers where grid[row][col]
 * represents the minimum time required to be able to visit the cell (row, col), which means you
 * can visit the cell (row, col) only when the time you visit it is greater than or equal to
 * grid[row][col].
 *
 * You are standing in the top-left cell of the matrix in the 0th second, and you must move to any
 * adjacent cell in the four directions: up, down, left, and right. Each move you make takes 1 second.
 *
 * Return the minimum time required in which you can visit the bottom-right cell of the matrix.
 * If you cannot visit the bottom-right cell, then return -1.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1,3,2],[5,1,2,5],[4,3,8,6]]
 * Output: 7
 * Explanation: One of the paths that we can take is the following:
 * - at t = 0, we are on the cell (0,0).
 * - at t = 1, we move to the cell (0,1). It is possible because grid[0][1] <= 1.
 * - at t = 2, we move to the cell (1,1). It is possible because grid[1][1] <= 2.
 * - at t = 3, we move to the cell (1,2). It is possible because grid[1][2] <= 3.
 * - at t = 4, we move to the cell (1,1). It is possible because grid[1][1] <= 4.
 * - at t = 5, we move to the cell (1,2). It is possible because grid[1][2] <= 5.
 * - at t = 6, we move to the cell (1,3). It is possible because grid[1][3] <= 6.
 * - at t = 7, we move to the cell (2,3). It is possible because grid[2][3] <= 7.
 * The final time is 7. It can be shown that it is the minimum time possible.
 *
 * Example 2:
 *
 * Input: grid = [[0,2,4],[3,2,1],[1,0,4]]
 * Output: -1
 * Explanation: There is no path from the top left to the bottom-right cell.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 2 <= m, n <= 1000
 * 4 <= m * n <= 10^5
 * 0 <= grid[i][j] <= 10^5
 * grid[0][0] == 0
 *
 */
public class MinimumTimeToVisitACellInAGrid {

    private static final int[][] DIRECTIONS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    // V0
    // IDEA: DIJKSTRA (MIN HEAP BY TIME) + PARITY WAITING TRICK
    //   we may not stand still, but we can bounce between two cells, which costs
    //   2 seconds. So a neighbour can be entered at any time >= t + 1 with the
    //   same parity as t + 1. If t + 1 < grid[x][y], arrive at grid[x][y] or
    //   grid[x][y] + 1, whichever has that parity.
    //   Bouncing needs a first move: if both grid[0][1] > 1 and grid[1][0] > 1
    //   we are stuck at time 0 -> -1. Otherwise every cell is reachable.
    /**
     * time = O(M * N * log(M * N))
     * space = O(M * N)
     */
    public int minimumTime(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        // no first step possible -> no bouncing possible -> never move
        if (grid[0][1] > 1 && grid[1][0] > 1) {
            return -1;
        }

        int[][] earliest = new int[rows][cols];
        for (int[] row : earliest) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        earliest[0][0] = 0;

        // heap entries: {time, row, col}, earliest time first
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        heap.offer(new int[] { 0, 0, 0 });

        while (!heap.isEmpty()) {
            int[] cur = heap.poll();
            int time = cur[0];
            int r = cur[1];
            int c = cur[2];
            if (r == rows - 1 && c == cols - 1) {
                return time;
            }
            // stale heap entry: a faster arrival was already processed
            if (time > earliest[r][c]) {
                continue;
            }
            for (int[] dir : DIRECTIONS) {
                int nr = r + dir[0];
                int nc = c + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                int arrival = time + 1;
                if (arrival < grid[nr][nc]) {
                    // wait by bouncing: keep the parity of (time + 1)
                    int gap = grid[nr][nc] - arrival;
                    arrival = grid[nr][nc] + (gap % 2);
                }
                if (arrival < earliest[nr][nc]) {
                    earliest[nr][nc] = arrival;
                    heap.offer(new int[] { arrival, nr, nc });
                }
            }
        }
        return -1;
    }
}
