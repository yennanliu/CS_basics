package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-cost-path-with-teleportations/description/

import java.util.*;

/**
 * 3651. Minimum Cost Path with Teleportations
 * Hard
 *
 * You are given a m x n 2D integer array grid and an integer k. You start at
 * the top-left cell (0, 0) and your goal is to reach the bottom-right cell
 * (m - 1, n - 1).
 *
 * There are two types of moves available:
 *
 * Normal move: You can move right or down from your current cell (i, j), i.e.
 * you can move to (i, j + 1) (right) or (i + 1, j) (down). The cost is the
 * value of the destination cell.
 * Teleportation: You can teleport from any cell (i, j), to any cell (x, y)
 * such that grid[x][y] <= grid[i][j]; the cost of this move is 0. You may
 * teleport at most k times.
 *
 * Return the minimum total cost to reach cell (m - 1, n - 1) from (0, 0).
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,3,3],[2,5,4],[4,3,5]], k = 2
 * Output: 7
 * Explanation:
 * Initially we are at (0, 0) and cost is 0.
 *
 * Current Position   Move                 New Position   Total Cost
 * (0, 0)             Move Down            (1, 0)         0 + 2 = 2
 * (1, 0)             Move Right           (1, 1)         2 + 5 = 7
 * (1, 1)             Teleport to (2, 2)   (2, 2)         7 + 0 = 7
 *
 * The minimum cost to reach bottom-right cell is 7.
 *
 * Example 2:
 *
 * Input: grid = [[1,2],[2,3],[3,4]], k = 1
 * Output: 9
 * Explanation:
 * Initially we are at (0, 0) and cost is 0.
 *
 * Current Position   Move          New Position   Total Cost
 * (0, 0)             Move Down     (1, 0)         0 + 2 = 2
 * (1, 0)             Move Right    (1, 1)         2 + 3 = 5
 * (1, 1)             Move Down     (2, 1)         5 + 4 = 9
 *
 * The minimum cost to reach bottom-right cell is 9.
 *
 *
 * Constraints:
 *
 * 2 <= m, n <= 80
 * m == grid.length
 * n == grid[i].length
 * 0 <= grid[i][j] <= 10^4
 * 0 <= k <= 10
 *
 */
public class MinimumCostPathWithTeleportations {

    private static final int INF = Integer.MAX_VALUE;

    // V0
    // IDEA: ONE RIGHT / DOWN GRID DP PER TELEPORT COUNT, LINKED BY A SUFFIX MINIMUM OVER VALUES
    //   cost[i][j] for "t teleports used so far":
    //     cost[i][j] = min(arrive[i][j], min(cost[i-1][j], cost[i][j-1]) + grid[i][j])
    //   arrive[i][j] = cheapest way to land on (i, j) by a teleport (or the start, for t = 0).
    //   A teleport into (i, j) may come from any cell with value >= grid[i][j], free:
    //     nextArrive[i][j] = min{ cost[x][y] : grid[x][y] >= grid[i][j] }
    //   Sorting cells by value descending and sweeping a running minimum computes all
    //   of them at once; a whole group of equal values is folded in before it is read.
    //   answer = best cost of the bottom-right cell over t = 0..k.
    /**
     * time = O(K * M * N + M * N * log(M * N))
     * space = O(M * N)
     */
    public int minCost(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;

        // cells as {value, row, col}, highest value first
        int[][] cells = new int[m * n][];
        int index = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                cells[index++] = new int[] {grid[i][j], i, j};
            }
        }
        Arrays.sort(cells, (a, b) -> Integer.compare(b[0], a[0]));

        int[][] arrive = new int[m][n];
        for (int[] row : arrive) {
            Arrays.fill(row, INF);
        }
        // the walk starts at (0, 0) for free
        arrive[0][0] = 0;

        int best = INF;
        for (int teleports = 0; teleports <= k; teleports++) {
            // ordinary monotone grid DP for this slice
            int[][] cost = new int[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int value = arrive[i][j];
                    int fromNeighbour = INF;
                    if (i > 0) {
                        fromNeighbour = Math.min(fromNeighbour, cost[i - 1][j]);
                    }
                    if (j > 0) {
                        fromNeighbour = Math.min(fromNeighbour, cost[i][j - 1]);
                    }
                    if (fromNeighbour != INF) {
                        value = Math.min(value, fromNeighbour + grid[i][j]);
                    }
                    cost[i][j] = value;
                }
            }
            best = Math.min(best, cost[m - 1][n - 1]);

            // one more teleport: running minimum over cells of value >= this one
            int[][] nextArrive = new int[m][n];
            int runningMin = INF;
            int start = 0;
            while (start < cells.length) {
                int end = start;
                // fold in the whole group of equal values first (the rule is >=)
                while (end < cells.length && cells[end][0] == cells[start][0]) {
                    runningMin = Math.min(runningMin, cost[cells[end][1]][cells[end][2]]);
                    end++;
                }
                for (int t = start; t < end; t++) {
                    nextArrive[cells[t][1]][cells[t][2]] = runningMin;
                }
                start = end;
            }
            arrive = nextArrive;
        }
        return best;
    }
}
