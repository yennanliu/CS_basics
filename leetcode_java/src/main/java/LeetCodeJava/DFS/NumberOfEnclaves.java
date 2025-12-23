package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-enclaves/description/

import java.util.Arrays;

/**
 *  1020. Number of Enclaves
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an m x n binary matrix grid, where 0 represents a sea cell and 1 represents a land cell.
 *
 * A move consists of walking from one land cell to another adjacent (4-directionally) land cell or walking off the boundary of the grid.
 *
 * Return the number of land cells in grid for which we cannot walk off the boundary of the grid in any number of moves.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[0,0,0,0],[1,0,1,0],[0,1,1,0],[0,0,0,0]]
 * Output: 3
 * Explanation: There are three 1s that are enclosed by 0s, and one 1 that is not enclosed because its on the boundary.
 * Example 2:
 *
 *
 * Input: grid = [[0,1,1,0],[0,0,1,0],[0,0,1,0],[0,0,0,0]]
 * Output: 0
 * Explanation: All 1s are either on the boundary or can reach the boundary.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 500
 * grid[i][j] is either 0 or 1.
 *
 *
 */
public class NumberOfEnclaves {

    // V0
//    public int numEnclaves(int[][] grid) {
//
//    }

    // V0-1
    // IDEA: LC 130, DFS (fixed by gpt)
    public int numEnclaves_0_1(int[][] grid) {

        if (grid == null || grid.length == 0 || grid[0].length == 0)
            return 0;

        int l = grid.length;
        int w = grid[0].length;

        // Step 1: Flood fill from boundary "1" → mark as -1 (can't be enclave)
        for (int y = 0; y < l; y++) {
            if (grid[y][0] == 1)
                dfsColorHelper2(0, y, grid, 1, -1);
            if (grid[y][w - 1] == 1)
                dfsColorHelper2(w - 1, y, grid, 1, -1);
        }
        for (int x = 0; x < w; x++) { // FIXED (was w++)
            if (grid[0][x] == 1)
                dfsColorHelper2(x, 0, grid, 1, -1);
            if (grid[l - 1][x] == 1)
                dfsColorHelper2(x, l - 1, grid, 1, -1);
        }

        // Step 2: Count remaining 1’s (these are enclaves)
        int cnt = 0;
        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                if (grid[y][x] == 1)
                    cnt++;
            }
        }

        return cnt;
    }

    // DFS - recolor oldColor → newColor
    private void dfsColorHelper2(int x, int y, int[][] grid, int oldColor, int newColor) {
        int l = grid.length, w = grid[0].length;

        grid[y][x] = newColor; // mark visited

        int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } }; // (dy, dx)

        for (int[] m : moves) {
            int x_ = x + m[1];
            int y_ = y + m[0];

            if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && grid[y_][x_] == oldColor) {
                dfsColorHelper2(x_, y_, grid, oldColor, newColor);
            }
        }
    }


    // V0-2
    // IDEA: LC 130, DFS (fixed by gemini)
    private int rows;
    private int cols;
    private final int LAND = 1;
    private final int ESCAPED_LAND = 2; // Temporary marker for land connected to the boundary
    private final int[][] MOVES = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public int numEnclaves_0_2(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        this.rows = grid.length;
        this.cols = grid[0].length;

        // --- Step 1: Flood Fill Land Connected to the Boundary (Mark '1' -> 'ESCAPED_LAND') ---

        // 1. Traverse Top (r=0) and Bottom (r=rows-1) rows
        for (int c = 0; c < cols; c++) {
            // Top row
            if (grid[0][c] == LAND) {
                dfsFlood(grid, 0, c);
            }
            // Bottom row
            if (grid[rows - 1][c] == LAND) {
                dfsFlood(grid, rows - 1, c);
            }
        }

        // 2. Traverse Left (c=0) and Right (c=cols-1) columns
        for (int r = 0; r < rows; r++) {
            // Left column
            if (grid[r][0] == LAND) {
                dfsFlood(grid, r, 0);
            }
            // Right column
            if (grid[r][cols - 1] == LAND) {
                dfsFlood(grid, r, cols - 1);
            }
        }

        // --- Step 2: Count the Remaining Unmarked Land ('1's) ---
        int enclaveCount = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == LAND) {
                    enclaveCount++;
                }
            }
        }

        return enclaveCount;
    }

    /**
     * DFS helper to mark connected land (oldColor=1) with a new color (ESCAPED_LAND=2).
     */
    private void dfsFlood(int[][] grid, int r, int c) {
        // Base Case: Check bounds or if already visited/water.
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != LAND) {
            return;
        }

        // Mark the current land cell as escaped/visited
        grid[r][c] = ESCAPED_LAND;

        // Visit neighbors
        for (int[] move : MOVES) {
            int nextR = r + move[0];
            int nextC = c + move[1];

            // Note: Bounds check is handled by the base case in the recursive call.
            dfsFlood(grid, nextR, nextC);
        }
    }

    // V0-3
    // IDEA: DFS (fixed by gemini)
    public int numEnclaves_0_3(int[][] grid) {
        if (grid == null || grid.length == 0)
            return 0;

        int rows = grid.length;
        int cols = grid[0].length;

        // 1. Traverse boundaries. If we find land (1),
        // use DFS to mark it and all connected land as "visited" (0).

        // Left and Right boundaries
        for (int i = 0; i < rows; i++) {
            if (grid[i][0] == 1)
                dfs(i, 0, grid);
            if (grid[i][cols - 1] == 1)
                dfs(i, cols - 1, grid);
        }

        // Top and Bottom boundaries
        for (int j = 0; j < cols; j++) {
            if (grid[0][j] == 1)
                dfs(0, j, grid);
            if (grid[rows - 1][j] == 1)
                dfs(rows - 1, j, grid);
        }

        // 2. Count the remaining land cells (1s).
        // These are the enclaves because they couldn't reach the boundary.
        int enclaveCnt = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1) {
                    enclaveCnt++;
                }
            }
        }

        return enclaveCnt;
    }

    private void dfs(int r, int c, int[][] grid) {
        // Base case: out of bounds or not land
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] != 1) {
            return;
        }

        // CRITICAL FIX: Mark the current cell immediately so it's not processed again
        grid[r][c] = 0;

        // Visit all 4 neighbors
        dfs(r + 1, c, grid);
        dfs(r - 1, c, grid);
        dfs(r, c + 1, grid);
        dfs(r, c - 1, grid);
    }

    // V0-4
    // IDEA: DFS (fixed by gpt)
    public int numEnclaves_0_4(int[][] grid) {

        // edge case
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;

        // 1️⃣ Flood fill all boundary-connected land (value = 1)
        // Top & Bottom rows
        for (int x = 0; x < w; x++) {
            if (grid[0][x] == 1) {
                dfsColor(x, 0, grid);
            }
            if (grid[l - 1][x] == 1) {
                dfsColor(x, l - 1, grid);
            }
        }

        // Left & Right columns
        for (int y = 0; y < l; y++) {
            if (grid[y][0] == 1) {
                dfsColor(0, y, grid);
            }
            if (grid[y][w - 1] == 1) {
                dfsColor(w - 1, y, grid);
            }
        }

        // 2️⃣ Count remaining land cells (enclaves)
        int enclaveCnt = 0;
        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                if (grid[y][x] == 1) {
                    enclaveCnt++;
                }
            }
        }

        return enclaveCnt;
    }

    // DFS to remove land connected to boundary
    private void dfsColor(int x, int y, int[][] grid) {

        int l = grid.length;
        int w = grid[0].length;

        // base case
        if (x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != 1) {
            return;
        }

        // mark as visited (remove land)
        grid[y][x] = -1;

        // explore neighbors
        dfsColor(x + 1, y, grid);
        dfsColor(x - 1, y, grid);
        dfsColor(x, y + 1, grid);
        dfsColor(x, y - 1, grid);
    }

    // V1
    // IDEA: DFS
    // https://leetcode.ca/2018-09-15-1020-Number-of-Enclaves/
    private int m;
    private int n;
    private int[][] grid;

    public int numEnclaves_1(int[][] grid) {
        this.grid = grid;
        m = grid.length;
        n = grid[0].length;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 1 && (i == 0 || i == m - 1 || j == 0 || j == n - 1)) {
                    dfs_1(i, j);
                }
            }
        }
        int ans = 0;
        for (int[] row : grid) {
            for (int v : row) {
                ans += v;
            }
        }
        return ans;
    }

    private void dfs_1(int i, int j) {
        grid[i][j] = 0;
        int[] dirs = {-1, 0, 1, 0, -1};
        for (int k = 0; k < 4; ++k) {
            int x = i + dirs[k], y = j + dirs[k + 1];
            if (x >= 0 && x < m && y >= 0 && y < n && grid[x][y] == 1) {
                dfs_1(x, y);
            }
        }
    }


    // V2
    // IDEA: DFS
    // https://leetcode.com/problems/number-of-enclaves/solutions/7377896/leetcode-1020-number-of-enclaves-reverse-t7pv/
    public int numEnclaves_2(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // eliminate land cells connected to the first & last row
        for (int j = 0; j < cols; j++) {
            if (grid[0][j] == 1)
                dfs_2(grid, 0, j);
            if (grid[rows - 1][j] == 1)
                dfs_2(grid, rows - 1, j);
        }

        // eliminate land cells connected to the first & last column
        for (int i = 0; i < rows; i++) {
            if (grid[i][0] == 1)
                dfs_2(grid, i, 0);
            if (grid[i][cols - 1] == 1)
                dfs_2(grid, i, cols - 1);
        }

        // count remaining land cells (enclaves)
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                if (grid[i][j] == 1)
                    count++;
        }
        return count;
    }

    private void dfs_2(int[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] == 0)
            return;

        grid[i][j] = 0; // mark border-connected land as removed/safe

        dfs_2(grid, i, j - 1);
        dfs_2(grid, i - 1, j);
        dfs_2(grid, i, j + 1);
        dfs_2(grid, i + 1, j);
    }

    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/number-of-enclaves/solutions/3388131/pythonjavacsimple-solutioneasy-to-unders-y0m8/
    public int numEnclaves_3(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if ((i == 0 || j == 0 || i == m - 1 || j == n - 1) && grid[i][j] == 1) {
                    dfs_3(grid, i, j);
                }
            }
        }
        return Arrays.stream(grid).mapToInt(row -> Arrays.stream(row).sum()).sum();
    }

    private void dfs_3(int[][] grid, int i, int j) {
        grid[i][j] = 0;
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] direction : directions) {
            int x = i + direction[0];
            int y = j + direction[1];
            if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == 1) {
                dfs_3(grid, x, y);
            }
        }
    }



}
