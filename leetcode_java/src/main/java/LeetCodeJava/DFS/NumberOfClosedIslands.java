package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-closed-islands/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 * 1254. Number of Closed Islands
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a 2D grid consists of 0s (land) and 1s (water).  An island is a maximal 4-directionally connected group of 0s and a closed island is an island totally (all left, top, right, bottom) surrounded by 1s.
 *
 * Return the number of closed islands.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: grid = [[1,1,1,1,1,1,1,0],[1,0,0,0,0,1,1,0],[1,0,1,0,1,1,1,0],[1,0,0,0,0,1,0,1],[1,1,1,1,1,1,1,0]]
 * Output: 2
 * Explanation:
 * Islands in gray are closed because they are completely surrounded by water (group of 1s).
 * Example 2:
 *
 *
 *
 * Input: grid = [[0,0,1,0,0],[0,1,0,1,0],[0,1,1,1,0]]
 * Output: 1
 * Example 3:
 *
 * Input: grid = [[1,1,1,1,1,1,1],
 *                [1,0,0,0,0,0,1],
 *                [1,0,1,1,1,0,1],
 *                [1,0,1,0,1,0,1],
 *                [1,0,1,1,1,0,1],
 *                [1,0,0,0,0,0,1],
 *                [1,1,1,1,1,1,1]]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= grid.length, grid[0].length <= 100
 * 0 <= grid[i][j] <=1
 *
 */
public class NumberOfClosedIslands {

    // V0
//    public int closedIsland(int[][] grid) {
//
//    }

    // V0-1
    // IDEA: DFS (fixed by gemini)
    private int rows;
    private int cols;
    private final int[][] MOVES = new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    /**
     * Performs DFS to change all connected land (0) to water (1).
     * @param grid The grid.
     * @param r Row index.
     * @param c Column index.
     */
    private void flood(int[][] grid, int r, int c) {
        // Base Case: Check bounds, check if current cell is already water (1), or out of bounds.
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 1) {
            return;
        }

        // Change land (0) to water (1) to mark it as visited/eliminated.
        grid[r][c] = 1;

        // Visit neighbors
        for (int[] move : MOVES) {
            flood(grid, r + move[0], c + move[1]);
        }
    }

    public int closedIsland_0_1(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        this.rows = grid.length;
        this.cols = grid[0].length;

        // --- PASS 1: Eliminate Border Islands ---
        // Flood any land (0) touching the top and bottom borders.
        for (int c = 0; c < cols; c++) {
            flood(grid, 0, c); // Top border (r=0)
            flood(grid, rows - 1, c); // Bottom border (r=rows-1)
        }

        // Flood any land (0) touching the left and right borders.
        for (int r = 0; r < rows; r++) {
            flood(grid, r, 0); // Left border (c=0)
            flood(grid, r, cols - 1); // Right border (c=cols-1)
        }

        // --- PASS 2: Count Remaining Closed Islands ---
        int closedIslandCount = 0;

        // Iterate through the interior of the grid (excluding the border, though checking border cells is fine now)
        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {

                // If we find remaining land (0), it must be a closed island.
                if (grid[r][c] == 0) {
                    closedIslandCount++;
                    // Flood the entire remaining island so we don't count it again.
                    flood(grid, r, c);
                }
            }
        }

        return closedIslandCount;
    }


    // V1-1
    // IDEA: BFS
    // https://leetcode.com/problems/number-of-closed-islands/editorial/
    public int closedIsland_1_1(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visit = new boolean[m][n];
        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0 && !visit[i][j] && bfs(i, j, m, n, grid, visit)) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean bfs(int x, int y, int m, int n, int[][] grid, boolean[][] visit) {
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[] { x, y });
        visit[x][y] = true;
        boolean isClosed = true;

        int[] dirx = { 0, 1, 0, -1 };
        int[] diry = { -1, 0, 1, 0 };

        while (!q.isEmpty()) {
            int[] temp = q.poll();
            x = temp[0];
            y = temp[1];

            for (int i = 0; i < 4; i++) {
                int r = x + dirx[i];
                int c = y + diry[i];
                if (r < 0 || r >= m || c < 0 || c >= n) {
                    // (x, y) is a boundary cell.
                    isClosed = false;
                } else if (grid[r][c] == 0 && !visit[r][c]) {
                    q.offer(new int[] { r, c });
                    visit[r][c] = true;
                }
            }
        }

        return isClosed;
    }

    // V1-2
    // IDEA: DFS
    // https://leetcode.com/problems/number-of-closed-islands/editorial/
    public int closedIsland_1_2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visit = new boolean[m][n];
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0 && !visit[i][j] && dfs(i, j, m, n, grid, visit)) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean dfs(int x, int y, int m, int n, int[][] grid, boolean[][] visit) {
        if (x < 0 || x >= m || y < 0 || y >= n) {
            return false;
        }
        if (grid[x][y] == 1 || visit[x][y]) {
            return true;
        }

        visit[x][y] = true;
        boolean isClosed = true;
        int[] dirx = { 0, 1, 0, -1 };
        int[] diry = { -1, 0, 1, 0 };

        for (int i = 0; i < 4; i++) {
            int r = x + dirx[i];
            int c = y + diry[i];
            if (!dfs(r, c, m, n, grid, visit)) {
                isClosed = false;
            }
        }

        return isClosed;
    }


    // V2
    // IDEA: DFS (NEEDCODE)
    // https://www.youtube.com/watch?v=X8k48xek8g8


    // V3
    // IDEA: DFS
    // https://leetcode.ca/2019-05-07-1254-Number-of-Closed-Islands/
    private int m;
    private int n;
    private int[][] grid;

    public int closedIsland_3(int[][] grid) {
        m = grid.length;
        n = grid[0].length;
        this.grid = grid;
        int ans = 0;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 0) {
                    ans += dfs(i, j);
                }
            }
        }
        return ans;
    }

    private int dfs(int i, int j) {
        int res = i > 0 && i < m - 1 && j > 0 && j < n - 1 ? 1 : 0;
        grid[i][j] = 1;
        int[] dirs = {-1, 0, 1, 0, -1};
        for (int k = 0; k < 4; ++k) {
            int x = i + dirs[k], y = j + dirs[k + 1];
            if (x >= 0 && x < m && y >= 0 && y < n && grid[x][y] == 0) {
                res &= dfs(x, y);
            }
        }
        return res;
    }



}
