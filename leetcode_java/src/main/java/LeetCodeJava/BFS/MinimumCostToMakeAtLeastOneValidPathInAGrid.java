package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/description/

import java.util.*;

/**
 * 1368. Minimum Cost to Make at Least One Valid Path in a Grid
 * Hard
 *
 * Given an m x n grid. Each cell of the grid has a sign pointing to the next cell you should
 * visit if you are currently in this cell. The sign of grid[i][j] can be:
 *
 * 1 which means go to the cell to the right. (i.e go from grid[i][j] to grid[i][j + 1])
 * 2 which means go to the cell to the left. (i.e go from grid[i][j] to grid[i][j - 1])
 * 3 which means go to the lower cell. (i.e go from grid[i][j] to grid[i + 1][j])
 * 4 which means go to the upper cell. (i.e go from grid[i][j] to grid[i - 1][j])
 *
 * Notice that there could be some signs on the cells of the grid that point outside the grid.
 *
 * You will initially start at the upper left cell (0, 0). A valid path in the grid is a path
 * that starts from the upper left cell (0, 0) and ends at the bottom-right cell (m - 1, n - 1)
 * following the signs on the grid. The valid path does not have to be the shortest.
 *
 * You can modify the sign on a cell with cost = 1. You can modify the sign on a cell one time only.
 *
 * Return the minimum cost to make the grid have at least one valid path.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,1,1,1],[2,2,2,2],[1,1,1,1],[2,2,2,2]]
 * Output: 3
 * Explanation: You will start at point (0, 0).
 * The path to (3, 3) is as follows. (0, 0) --> (0, 1) --> (0, 2) --> (0, 3) change the arrow
 * to down with cost = 1 --> (1, 3) --> (1, 2) --> (1, 1) --> (1, 0) change the arrow to down
 * with cost = 1 --> (2, 0) --> (2, 1) --> (2, 2) --> (2, 3) change the arrow to down with
 * cost = 1 --> (3, 3)
 * The total cost = 3.
 *
 * Example 2:
 *
 * Input: grid = [[1,1,3],[3,2,2],[1,1,4]]
 * Output: 0
 * Explanation: You can follow the path from (0, 0) to (2, 2).
 *
 * Example 3:
 *
 * Input: grid = [[1,2],[4,3]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 100
 * 1 <= grid[i][j] <= 4
 *
 */
public class MinimumCostToMakeAtLeastOneValidPathInAGrid {

    // index 1..4 matches the sign values in the problem; index 0 is unused
    private static final int[][] SIGN_DIRECTIONS = {{0, 0}, {0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    // V0
    // IDEA: 0-1 BFS (DEQUE)
    //  a node is a cell, an edge goes to each of the 4 neighbours.
    //  Following the cell's arrow costs 0 (push to the FRONT), any other move costs 1
    //  (push to the BACK). The deque stays sorted by cost, so the first time a cell is
    //  popped its cost is final.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int minCost(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] settled = new boolean[rows][cols];

        // entries are {row, col, cost so far}
        Deque<int[]> deque = new ArrayDeque<>();
        deque.offerFirst(new int[] {0, 0, 0});

        while (!deque.isEmpty()) {
            int[] entry = deque.pollFirst();
            int r = entry[0];
            int c = entry[1];
            int cost = entry[2];

            // a cell can be pushed several times; only its first pop counts
            if (settled[r][c]) {
                continue;
            }
            settled[r][c] = true;

            if (r == rows - 1 && c == cols - 1) {
                return cost;
            }

            for (int sign = 1; sign <= 4; sign++) {
                int nr = r + SIGN_DIRECTIONS[sign][0];
                int nc = c + SIGN_DIRECTIONS[sign][1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || settled[nr][nc]) {
                    continue;
                }
                if (grid[r][c] == sign) {
                    // free: follow the existing arrow
                    deque.offerFirst(new int[] {nr, nc, cost});
                } else {
                    // rewrite this cell's arrow for a cost of 1
                    deque.offerLast(new int[] {nr, nc, cost + 1});
                }
            }
        }
        return -1;
    }
}
