package LeetCodeJava.BFS;

// https://leetcode.com/problems/count-sub-islands/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 *  1905. Count Sub Islands
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given two m x n binary matrices grid1 and grid2 containing only 0's (representing water) and 1's (representing land). An island is a group of 1's connected 4-directionally (horizontal or vertical). Any cells outside of the grid are considered water cells.
 *
 * An island in grid2 is considered a sub-island if there is an island in grid1 that contains all the cells that make up this island in grid2.
 *
 * Return the number of islands in grid2 that are considered sub-islands.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid1 = [[1,1,1,0,0],[0,1,1,1,1],[0,0,0,0,0],[1,0,0,0,0],[1,1,0,1,1]], grid2 = [[1,1,1,0,0],[0,0,1,1,1],[0,1,0,0,0],[1,0,1,1,0],[0,1,0,1,0]]
 * Output: 3
 * Explanation: In the picture above, the grid on the left is grid1 and the grid on the right is grid2.
 * The 1s colored red in grid2 are those considered to be part of a sub-island. There are three sub-islands.
 * Example 2:
 *
 *
 * Input: grid1 = [[1,0,1,0,1],[1,1,1,1,1],[0,0,0,0,0],[1,1,1,1,1],[1,0,1,0,1]], grid2 = [[0,0,0,0,0],[1,1,1,1,1],[0,1,0,1,0],[0,1,0,1,0],[1,0,0,0,1]]
 * Output: 2
 * Explanation: In the picture above, the grid on the left is grid1 and the grid on the right is grid2.
 * The 1s colored red in grid2 are those considered to be part of a sub-island. There are two sub-islands.
 *
 *
 * Constraints:
 *
 * m == grid1.length == grid2.length
 * n == grid1[i].length == grid2[i].length
 * 1 <= m, n <= 500
 * grid1[i][j] and grid2[i][j] are either 0 or 1.
 *
 *
 */
public class CountSubIslands {

    // V0
    // IDEA: 2 pass DFS + COLOR (gemini)
    public int countSubIslands(int[][] grid1, int[][] grid2) {
        int rows = grid1.length;
        int cols = grid1[0].length;

        // 1st Pass: "Sink" all islands in grid2 that are NOT sub-islands.
        // If grid2 has land (1) but grid1 has water (0), that whole island in grid2 is invalid.
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid2[r][c] == 1 && grid1[r][c] == 0) {
                    // This island in grid2 is not a sub-island, sink it.
                    sink(grid2, r, c);
                }
            }
        }

        // 2nd Pass: Now every remaining island in grid2 is a valid sub-island.
        int subIslandCnt = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid2[r][c] == 1) {
                    subIslandCnt++;
                    sink(grid2, r, c); // Sink it so we don't count it again
                }
            }
        }

        return subIslandCnt;
    }

    private void sink(int[][] grid, int r, int c) {
        // Boundary check and water check
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] == 0) {
            return;
        }

        // Sink the land (turn it into water/0) to mark as visited
        grid[r][c] = 0;

        // DFS in 4 directions
        sink(grid, r + 1, c);
        sink(grid, r - 1, c);
        sink(grid, r, c + 1);
        sink(grid, r, c - 1);
    }

    // V0-1
    // IDEA: 2 pass DFS + pruning (gemini)
    /** IDEA:
     *
     *
     * 1. Eliminate Invalid Islands:
     *    In grid2, any island that touches a water cell ('0')
     *    in grid1 cannot be a sub-island.
     *    We can iterate through grid2 and sink/remove
     *    any island that violates this rule.
     *
     *  2. Count Remaining Islands:
     *     After sinking the invalid islands,
     *     the remaining islands in grid2 are guaranteed to
     *     be sub-islands.
     *     We simply count them using a standard
     *     island counting technique (DFS/BFS).
     *
     *
     *  ---------------
     *
     *  Further explanation on `Eliminate Invalid Islands`:
     *
     *   1. is the core optimization for solving the Sub Islands problem without tedious set comparisons.
     *   2. It leverages the problem's definition to efficiently prune the search space.
     *
     *
     *   -> the violation condition for grid2 island NOT the island in grid1:
     *
     *    1.  if found cell (x,y) that
     *      - grid2[y][x] = 1, but
     *        grid1[y][x] = 0
     *
     *        -> (x,y) in grid2 is land, BUT is water in grid1
     *
     */
    private int R;
    private int C;
    private final int[][] MOVES = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    /**
     * Counts the number of islands in grid2 that are entirely contained in land of grid1.
     * Time Complexity: O(R * C)
     */
    public int countSubIslands_0_1(int[][] grid1, int[][] grid2) {
        this.R = grid1.length;
        this.C = grid1[0].length;

        /** NOTE !!!
         *
         *   Step 1) find `invalid cell` in grid2 and `disable` them
         *
         */
        // --- Phase 1: Sink invalid islands in grid2 ---
        // An island in grid2 is invalid if any of its cells (r, c) corresponds to water (0) in grid1.
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {

                /** NOTE !!!
                 *
                 *  we ONLY do 1st DFS (flip non-valid grid2 cell)
                 *  when `grid2[r][c] == 1 && grid1[r][c] == 0`
                 *
                 *   e.g. ONLY flip when
                 *      - grid2 cell is land
                 *      - and grid1 cell is water
                 */
                // If a cell is land in grid2 BUT water in grid1,
                // the entire island connected to this cell in grid2 must be eliminated (sunk).
                if (grid2[r][c] == 1 && grid1[r][c] == 0) {
                    /** NOTE !!!
                     *
                     *   we `disable` the cells in grid2 via DFS call
                     *
                     *   (1st call of DFS)
                     */
                    sinkIsland(grid2, r, c); // Sinks the whole connected component in grid2
                }
            }
        }

        /** NOTE !!!
         *
         *   Step 2) count the remaining islands in grid2
         *
         */
        // --- Phase 2: Count the remaining islands in grid2 ---
        // The remaining islands are guaranteed to be sub-islands.
        int subIslandCount = 0;
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {

                if (grid2[r][c] == 1) {
                    subIslandCount++;
                    // Sinks the remaining valid island to prevent recounting
                    /** NOTE !!!
                     *
                     *   we `disable` the remaining cells in grid2 via DFS call
                     *
                     *   (2nd call of DFS)
                     */
                    sinkIsland(grid2, r, c);
                }
            }
        }

        return subIslandCount;
    }

    /**
     * DFS function to "sink" (change '1' to '0') an entire connected component in the grid.
     *
     * NOTE !!!
     *
     *  we ONLY need to pass 1 grid as param
     */
    private void sinkIsland(int[][] grid, int r, int c) {
        /**
         *  NOTE !!!
         *
         *   validation
         *
         *   1. if still in boundary
         *   2. if grid2 cell is water (NOTE !!!)
         *       - we are NOT proceed if grid2 cell is water,
         *         since the purpose of dfs if to `flip` in-valid grid2 cell
         *         (e.g. grid2 cell is land, but grid1 cell is water)
         */
        // Base case: Out of bounds or already water ('0')
        if (!isValid(r, c) || grid[r][c] == 0) {
            return;
        }

        // Mark current cell as water (sunk)
        grid[r][c] = 0;

        // Recurse into neighbors
        for (int[] move : MOVES) {
            sinkIsland(grid, r + move[0], c + move[1]);
        }
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < R && c >= 0 && c < C;
    }


    // V0-2
    // IDEA: DFS (gpt)
    public int countSubIslands_0_2(int[][] grid1, int[][] grid2) {
        int l = grid1.length;
        int w = grid1[0].length;

        int count = 0;

        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                if (grid2[y][x] == 1) {
                    // DFS collect and check if this island in grid2 is fully inside grid1
                    boolean isSub = dfsCheck(grid1, grid2, x, y);
                    if (isSub)
                        count++;
                }
            }
        }
        return count;
    }

    // DFS: returns true if island in grid2 is a sub-island of grid1
    private boolean dfsCheck(int[][] grid1, int[][] grid2, int x, int y) {
        int l = grid1.length;
        int w = grid1[0].length;

        // out of boundary
        if (x < 0 || x >= w || y < 0 || y >= l)
            return true;

        // if water or visited in grid2
        if (grid2[y][x] != 1)
            return true;

        // mark grid2 as visited
        grid2[y][x] = -1;

        // This cell must be land in grid1 to be a sub-island
        boolean valid = (grid1[y][x] == 1);

        int[][] moves = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        for (int[] m : moves) {
            int nx = x + m[0];
            int ny = y + m[1];
            boolean child = dfsCheck(grid1, grid2, nx, ny);

            // if any child is invalid, whole island invalid
            valid = valid && child;
        }
        return valid;
    }


    // V0-3
    // IDEA: BFS (gpt)
    public int countSubIslands_0_3(int[][] grid1, int[][] grid2) {
        int l = grid1.length;
        int w = grid1[0].length;

        int count = 0;

        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                if (grid2[y][x] == 1) {
                    if (bfsCheck(grid1, grid2, x, y)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private boolean bfsCheck(int[][] grid1, int[][] grid2, int startX, int startY) {
        int l = grid1.length;
        int w = grid1[0].length;

        int[][] moves = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[] { startX, startY });

        // Mark as visited in grid2
        grid2[startY][startX] = -1;

        boolean isSubIsland = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0], y = cur[1];

            // Check if grid1 is also land
            if (grid1[y][x] != 1) {
                /**  NOTE !!!
                 *
                 *  CAN'T return (quit) immediately
                 *  when we discover a cell in grid2 is NOT land in grid1.
                 *
                 *  -> e.g.: Even though the island is already disqualified from being a sub-island,
                 * you still must finish the BFS traversal to:
                 *
                 *
                 *  Reason:
                 *
                 *   1.  Mark every cell of this island in grid2 as visited
                 *   2.  Prevent re-visiting / re-counting the same island
                 *
                 */
                isSubIsland = false; // grid2 has land where grid1 does NOT
            }

            // explore 4 directions
            for (int[] m : moves) {
                int nx = x + m[0];
                int ny = y + m[1];

                if (nx >= 0 && nx < w && ny >= 0 && ny < l &&
                        grid2[ny][nx] == 1) {

                    grid2[ny][nx] = -1; // mark visited
                    q.offer(new int[] { nx, ny });
                }
            }
        }
        return isSubIsland;
    }

    // V0-4
    // IDEA: 1 pass DFS (fixed by gemini)
    /**
     * Counts how many islands in grid2 are sub-islands of grid1.
     * Time Complexity: O(R * C) where R is rows and C is columns.
     * Space Complexity: O(R * C) for the recursion stack in the worst case.
     */
    public int countSubIslands_0_4(int[][] grid1, int[][] grid2) {
        int rows = grid2.length;
        int cols = grid2[0].length;
        int subIslandCount = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // If we find land in grid2, it's a potential sub-island
                if (grid2[r][c] == 1) {
                    // We use a flag to track if the entire island is valid
                    if (isSubIslandDFS(grid1, grid2, r, c)) {
                        subIslandCount++;
                    }
                }
            }
        }
        return subIslandCount;
    }

    private boolean isSubIslandDFS(int[][] grid1, int[][] grid2, int r, int c) {
        // Base case: out of bounds or water in grid2
        if (r < 0 || r >= grid1.length || c < 0 || c >= grid1[0].length || grid2[r][c] == 0) {
            return true;
        }

        // Mark current land in grid2 as visited by turning it to water (0)
        grid2[r][c] = 0;

        // Check if current cell is valid: it must be land (1) in grid1
        boolean currentIsMatch = (grid1[r][c] == 1);

        // Continue DFS in all 4 directions
        // Note: Use the non-short-circuiting '&' to ensure we visit all land
        // cells of the island in grid2, even if we already found an invalid part.
        boolean res1 = isSubIslandDFS(grid1, grid2, r + 1, c);
        boolean res2 = isSubIslandDFS(grid1, grid2, r - 1, c);
        boolean res3 = isSubIslandDFS(grid1, grid2, r, c + 1);
        boolean res4 = isSubIslandDFS(grid1, grid2, r, c - 1);

        return currentIsMatch && res1 && res2 && res3 && res4;
    }



    // V1-1
    // IDEA: BFS
    // https://leetcode.com/problems/count-sub-islands/editorial/
    // Directions in which we can traverse inside the grids.
    private final int[][] directions = {
            { 0, 1 },
            { 1, 0 },
            { 0, -1 },
            { -1, 0 },
    };

    // Helper method to check if the cell at the position (x, y) in the 'grid'
    // is a land cell.
    private boolean isCellLand_1_1(int x, int y, int[][] grid) {
        return grid[x][y] == 1;
    }

    // Traverse all cells of island starting at position (x, y) in 'grid2',
    // and check this island is a sub-island in 'grid1'.
    private boolean isSubIsland_1_1(
            int x,
            int y,
            int[][] grid1,
            int[][] grid2,
            boolean[][] visited) {
        int totalRows = grid2.length;
        int totalCols = grid2[0].length;

        boolean isSubIsland_1_1 = true;

        Queue<int[]> pendingCells = new LinkedList<>();
        // Push the starting cell in the queue and mark it as visited.
        pendingCells.offer(new int[] { x, y });
        visited[x][y] = true;

        // Traverse on all cells using the breadth-first search method.
        while (!pendingCells.isEmpty()) {
            int[] curr = pendingCells.poll();
            int currX = curr[0];
            int currY = curr[1];

            // If the current position cell is not a land cell in 'grid1',
            // then the current island can't be a sub-island.
            if (!isCellLand_1_1(currX, currY, grid1)) {
                isSubIsland_1_1 = false;
            }

            for (int[] direction : directions) {
                int nextX = currX + direction[0];
                int nextY = currY + direction[1];
                // If the next cell is inside 'grid2', is never visited and
                // is a land cell, then we traverse to the next cell.
                if (nextX >= 0 &&
                        nextY >= 0 &&
                        nextX < totalRows &&
                        nextY < totalCols &&
                        !visited[nextX][nextY] &&
                        isCellLand_1_1(nextX, nextY, grid2)) {
                    // Push the next cell in the queue and mark it as visited.
                    pendingCells.offer(new int[] { nextX, nextY });
                    visited[nextX][nextY] = true;
                }
            }
        }

        return isSubIsland_1_1;
    }

    public int countSubIslands_1_1(int[][] grid1, int[][] grid2) {
        int totalRows = grid2.length;
        int totalCols = grid2[0].length;

        boolean[][] visited = new boolean[totalRows][totalCols];
        int subIslandCounts = 0;

        // Iterate on each cell in 'grid2'
        for (int x = 0; x < totalRows; ++x) {
            for (int y = 0; y < totalCols; ++y) {
                // If cell at the position (x, y) in the 'grid2' is not visited,
                // is a land cell in 'grid2', and the island
                // starting from this cell is a sub-island in 'grid1', then we
                // increment the count of sub-islands.
                if (!visited[x][y] &&
                        isCellLand_1_1(x, y, grid2) &&
                        isSubIsland_1_1(x, y, grid1, grid2, visited)) {
                    subIslandCounts += 1;
                }
            }
        }
        // Return total count of sub-islands.
        return subIslandCounts;
    }



    // V1-2
    // IDEA: DFS
    // https://leetcode.com/problems/count-sub-islands/editorial/
    // Directions in which we can traverse inside the grids.
    // Directions in which we can traverse inside the grids.
//    private final int[][] directions = {
//            { 0, 1 },
//            { 1, 0 },
//            { 0, -1 },
//            { -1, 0 },
//    };

    // Helper method to check if the cell at the position (x, y) in the 'grid'
    // is a land cell.
    private boolean isCellLand(int x, int y, int[][] grid) {
        return grid[x][y] == 1;
    }

    // Traverse all cells of island starting at position (x, y) in 'grid2',
    // and check if this island is a sub-island in 'grid1'.
    private boolean isSubIsland(
            int x,
            int y,
            int[][] grid1,
            int[][] grid2,
            boolean[][] visited) {
        int totalRows = grid2.length;
        int totalCols = grid2[0].length;
        // Traverse on all cells using the depth-first search method.
        boolean isSubIsland = true;

        // If the current cell is not a land cell in 'grid1', then the current island can't be a sub-island.
        if (!isCellLand(x, y, grid1)) {
            isSubIsland = false;
        }

        // Traverse on all adjacent cells.
        for (int[] direction : directions) {
            int nextX = x + direction[0];
            int nextY = y + direction[1];
            // If the next cell is inside 'grid2', is not visited, and is a land cell,
            // then we traverse to the next cell.
            if (nextX >= 0 &&
                    nextY >= 0 &&
                    nextX < totalRows &&
                    nextY < totalCols &&
                    !visited[nextX][nextY] &&
                    isCellLand(nextX, nextY, grid2)) {
                // Mark the next cell as visited.
                visited[nextX][nextY] = true;
                boolean nextCellIsPartOfSubIsland = isSubIsland(
                        nextX,
                        nextY,
                        grid1,
                        grid2,
                        visited);
                isSubIsland = isSubIsland && nextCellIsPartOfSubIsland;
            }
        }
        return isSubIsland;
    }

    public int countSubIslands_1_2(int[][] grid1, int[][] grid2) {
        int totalRows = grid2.length;
        int totalCols = grid2[0].length;

        boolean[][] visited = new boolean[totalRows][totalCols];
        int subIslandCounts = 0;

        // Iterate over each cell in 'grid2'.
        for (int x = 0; x < totalRows; ++x) {
            for (int y = 0; y < totalCols; ++y) {
                // If the cell at position (x, y) in 'grid2' is not visited,
                // is a land cell in 'grid2', and the island starting from this cell is a sub-island in 'grid1',
                // then increment the count of sub-islands.
                if (!visited[x][y] && isCellLand(x, y, grid2)) {
                    visited[x][y] = true;
                    if (isSubIsland(x, y, grid1, grid2, visited)) {
                        subIslandCounts += 1;
                    }
                }
            }
        }
        // Return total count of sub-islands.
        return subIslandCounts;
    }

    // V2-1
    // IDEA:
    // https://leetcode.ca/2021-07-29-1905-Count-Sub-Islands/
   // static int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

    public int countSubIslands_2_1(int[][] grid1, int[][] grid2) {
        int subIslands = 0;
        int m = grid1.length, n = grid1[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid2[i][j] == 1 && !visited[i][j]) {
                    boolean isSubisland = breadthFirstSearch(grid1, grid2, visited, m, n, i, j);
                    if (isSubisland)
                        subIslands++;
                }
            }
        }
        return subIslands;
    }

    public boolean breadthFirstSearch(int[][] grid1, int[][] grid2, boolean[][] visited, int m, int n, int startRow, int startColumn) {
        boolean isSubisland = true;
        Queue<int[]> queue = new LinkedList<int[]>();
        queue.offer(new int[]{startRow, startColumn});
        visited[startRow][startColumn] = true;
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0], column = cell[1];
            if (grid1[row][column] == 0)
                isSubisland = false;
            for (int[] direction : directions) {
                int newRow = row + direction[0], newColumn = column + direction[1];
                if (newRow >= 0 && newRow < m && newColumn >= 0 && newColumn < n && grid2[newRow][newColumn] == 1 && !visited[newRow][newColumn]) {
                    visited[newRow][newColumn] = true;
                    queue.offer(new int[]{newRow, newColumn});
                }
            }
        }
        return isSubisland;
    }


    // V2-2
    // IDEA:
    // https://leetcode.ca/2021-07-29-1905-Count-Sub-Islands/
    public int countSubIslands_2_2(int[][] grid1, int[][] grid2) {
        int m = grid1.length;
        int n = grid1[0].length;
        int ans = 0;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid2[i][j] == 1 && dfs(i, j, m, n, grid1, grid2)) {
                    ++ans;
                }
            }
        }
        return ans;
    }

    private boolean dfs(int i, int j, int m, int n, int[][] grid1, int[][] grid2) {
        boolean ans = grid1[i][j] == 1;
        grid2[i][j] = 0;
        int[] dirs = {-1, 0, 1, 0, -1};
        for (int k = 0; k < 4; ++k) {
            int x = i + dirs[k];
            int y = j + dirs[k + 1];
            if (x >= 0 && x < m && y >= 0 && y < n && grid2[x][y] == 1
                    && !dfs(x, y, m, n, grid1, grid2)) {
                ans = false;
            }
        }
        return ans;
    }



}
