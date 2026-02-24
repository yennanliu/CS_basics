package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/unique-paths-iii/description/
/**
 *  980. Unique Paths III
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given an m x n integer array grid where grid[i][j] could be:
 *
 * 1 representing the starting square. There is exactly one starting square.
 * 2 representing the ending square. There is exactly one ending square.
 * 0 representing empty squares we can walk over.
 * -1 representing obstacles that we cannot walk over.
 * Return the number of 4-directional walks from the starting square to the ending square, that walk over every non-obstacle square exactly once.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[1,0,0,0],[0,0,0,0],[0,0,2,-1]]
 * Output: 2
 * Explanation: We have the following two paths:
 * 1. (0,0),(0,1),(0,2),(0,3),(1,3),(1,2),(1,1),(1,0),(2,0),(2,1),(2,2)
 * 2. (0,0),(1,0),(2,0),(2,1),(1,1),(0,1),(0,2),(0,3),(1,3),(1,2),(2,2)
 * Example 2:
 *
 *
 * Input: grid = [[1,0,0,0],[0,0,0,0],[0,0,0,2]]
 * Output: 4
 * Explanation: We have the following four paths:
 * 1. (0,0),(0,1),(0,2),(0,3),(1,3),(1,2),(1,1),(1,0),(2,0),(2,1),(2,2),(2,3)
 * 2. (0,0),(0,1),(1,1),(1,0),(2,0),(2,1),(2,2),(1,2),(0,2),(0,3),(1,3),(2,3)
 * 3. (0,0),(1,0),(2,0),(2,1),(2,2),(1,2),(1,1),(0,1),(0,2),(0,3),(1,3),(2,3)
 * 4. (0,0),(1,0),(2,0),(2,1),(1,1),(0,1),(0,2),(0,3),(1,3),(1,2),(2,2),(2,3)
 * Example 3:
 *
 *
 * Input: grid = [[0,1],[2,0]]
 * Output: 0
 * Explanation: There is no path that walks over every empty square exactly once.
 * Note that the starting and ending square can be anywhere in the grid.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 20
 * 1 <= m * n <= 20
 * -1 <= grid[i][j] <= 2
 * There is exactly one starting cell and one ending cell.
 *
 *
 */
public class UniquePaths3 {

    /** NOTE !!!
     *
     *
     *
     *  why CAN'T solve this LC (LC 980) with DP ?
     *
     * -> (gemini)
     *
     * The short answer is: **No,
     * you CAN NOT solve it with standard Grid DP**
     * (like the ones we did for LC 62 and 63).
     * You must use **Backtracking** (DFS) or **DP with Bitmasking**.
     *
     * Here is why:
     *
     * ### 1. The "Visit Every Square" Constraint
     *
     * In LC 62 and 63, the robot could only move **Right** and **Down**. This created a "topological order"—you always know that `dp[y][x]` only depends on `dp[y-1][x]` and `dp[y][x-1]`.
     *
     * In LC 980, the robot can move **Up, Down, Left, and Right**, and it **must visit every empty square exactly once**.
     *
     * * **Cycles:** Because you can move in all four directions, you can go in circles. Standard DP can't handle cycles because it doesn't know which squares you have already visited.
     * * **State:** To use DP, the "state" isn't just your current position `(y, x)`. The state must include **every single square you have visited so far**.
     *
     * ---
     *
     * ### 2. Why "Standard" DP Fails
     *
     * Imagine you are at square `(2, 2)`.
     *
     * * In LC 62, there is only one way to reach `(2, 2)`'s logic: from the top or left.
     * * In LC 980, you could have reached `(2, 2)` after visiting 5 squares, or 10 squares, or via a "S" shape or a "U" shape.
     * * The number of paths to the end depends on **which squares are left empty**. Standard DP doesn't store the "map of empty squares" in its table.
     *
     * ---
     *
     * ### 3. The "State" Problem (The Math)
     *
     * If you really wanted to use DP, your table would look like this:
     * `dp[y][x][visited_mask]`
     * Where `visited_mask` is a bitmask representing every square on the grid.
     *
     * * For a  grid (20 squares), the mask would be  (about 1 million states).
     * * This is called **DP with Bitmasking**, but for this specific problem size, a simple **Backtracking (DFS)** is more intuitive and usually faster.
     *
     */

    // V0
//    public int uniquePathsIII(int[][] grid) {
//
//    }


    // V0-1
    // IDEA: DFS (gemini)
    int pathCnt = 0;
    int targetEmptyCells = 0;

    public int uniquePathsIII(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int startX = 0, startY = 0;

        // 1. Count total empty cells (0) and find the starting point (1)
        // We include the starting cell in our "must visit" count.
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 0) {
                    targetEmptyCells++;
                } else if (grid[r][c] == 1) {
                    startY = r;
                    startX = c;
                }
            }
        }

        // Start DFS. We need to visit targetEmptyCells + 1 (the start itself).
        backtrack_0_1(grid, startX, startY, 0);
        return pathCnt;
    }

    private void backtrack_0_1(int[][] grid, int x, int y, int visitedCount) {
        // 2. Boundary and Obstacle Check
        if (y < 0 || y >= grid.length || x < 0 || x >= grid[0].length || grid[y][x] == -1) {
            return;
        }

        // 3. Target Reached Check
        if (grid[y][x] == 2) {
            // Check if we visited all the 0s before hitting the end
            if (visitedCount == targetEmptyCells + 1) {
                pathCnt++;
            }
            return;
        }

        // 4. Mark current cell as visited (-1)
        int temp = grid[y][x];
        grid[y][x] = -1;

        // 5. Explore 4 neighbors
        backtrack_0_1(grid, x + 1, y, visitedCount + 1);
        backtrack_0_1(grid, x - 1, y, visitedCount + 1);
        backtrack_0_1(grid, x, y + 1, visitedCount + 1);
        backtrack_0_1(grid, x, y - 1, visitedCount + 1);

        // 6. Backtrack: Restore cell for other potential paths
        grid[y][x] = temp;
    }


    // V0-2
    // IDEA: DFS (gpt)
    int pathCnt_0_2 = 0;
    int emptyCells = 0; // number of 0 cells

    public int uniquePathsIII_0_2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int startX = 0, startY = 0;

        // 1. Find start & count empty cells
        for (int y = 0; y < m; y++) {
            for (int x = 0; x < n; x++) {
                if (grid[y][x] == 1) {
                    startX = x;
                    startY = y;
                }
                if (grid[y][x] == 0) {
                    emptyCells++;
                }
            }
        }

        // 2. DFS from start
        dfs_0_2(grid, startX, startY, emptyCells);

        return pathCnt_0_2;
    }

    private void dfs_0_2(int[][] grid, int x, int y, int remain) {

        int m = grid.length;
        int n = grid[0].length;

        // boundary or obstacle or visited
        if (x < 0 || x >= n || y < 0 || y >= m || grid[y][x] == -1) {
            return;
        }

        // reached end
        if (grid[y][x] == 2) {
            if (remain == -1) { // visited all empty cells + start
                pathCnt_0_2++;
            }
            return;
        }

        // mark visited
        grid[y][x] = -1;

        int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        for (int[] d : dirs) {
            dfs_0_2(grid, x + d[0], y + d[1], remain - 1);
        }

        // backtrack
        grid[y][x] = 0;
    }


    // V1
    // IDEA: DFS
    // https://leetcode.com/problems/unique-paths-iii/solutions/2973622/java-code-with-dfs-and-backtracking100-f-dmgw/
    public int uniquePathsIII_1(int[][] grid) {
        int zero = 0, a = 0, b = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 0)
                    zero++;
                else if (grid[r][c] == 1) {
                    a = r;
                    b = c;
                }
            }
        }
        return path(grid, a, b, zero);
    }

    private int path(int[][] grid, int x, int y, int zero) {
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length || grid[x][y] == -1)
            return 0;
        if (grid[x][y] == 2)
            return zero == -1 ? 1 : 0;
        grid[x][y] = -1;
        zero--;
        int totalCount = path(grid, x + 1, y, zero) + path(grid, x, y + 1, zero) +
                path(grid, x - 1, y, zero) + path(grid, x, y - 1, zero);
        grid[x][y] = 0;
        zero++;

        return totalCount;
    }


    // V2
    // IDEA: DFS
    // https://leetcode.com/problems/unique-paths-iii/solutions/1553873/java-easy-solution-dfs-backtracking-expl-172e/
    public int uniquePathsIII_2(int[][] grid) {
        int zero = 0; // Count the 0's
        int sx = 0; // starting x index
        int sy = 0; // starting y index

        for (int r = 0; r < grid.length; r++) { // r = row
            for (int c = 0; c < grid[0].length; c++) { // c = column
                if (grid[r][c] == 0)
                    zero++; // if current cell is 0, count it.
                else if (grid[r][c] == 1) {
                    sx = r; // starting x co-ordinate
                    sy = c; // starting y co-ordinate
                }
            }
        }
        return dfs(grid, sx, sy, zero);
    }

    public int dfs(int grid[][], int x, int y, int zero) {
        // Base Condition
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length || grid[x][y] == -1) {
            return 0;
        }
        if (grid[x][y] == 2) {
            return zero == -1 ? 1 : 0; // Why zero = -1, because in above example we have 9 zero's. So, when we reach the final cell we are covering one cell extra then the zero count.
            // If that's the case we find the path and return '1' otherwise return '0';
        }
        grid[x][y] = -1; // mark the visited cells as -1;
        zero--; // and reduce the zero by 1

        int totalPaths = dfs(grid, x + 1, y, zero) + // calculating all the paths available in 4 directions
                dfs(grid, x - 1, y, zero) +
                dfs(grid, x, y + 1, zero) +
                dfs(grid, x, y - 1, zero);

        // Let's say if we are not able to count all the paths. Now we use Backtracking over here
        grid[x][y] = 0;
        zero++;

        return totalPaths; // if we get all the paths, simply return it.
    }


    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/unique-paths-iii/solutions/1554601/java-simple-and-readable-solution-dfs-ba-i6x4/
    // DFS function
    public int DFS(int[][] obstacleGrid, int i, int j, int totalZeros, int currentZeros) {
        // Checking whether current position is valid or not
        if (i < 0 || i >= obstacleGrid.length || j < 0 || j >= obstacleGrid[0].length)
            return 0;
        // If current cell is an obstacle ,then we cant proceed further, hence no path exists
        if (obstacleGrid[i][j] == -1)
            return 0;
        // If we found ending square then we got a path. But also take care of number of zeros traversed.
        if (obstacleGrid[i][j] == 2 && totalZeros == currentZeros - 1)
            return 1;
        else if (obstacleGrid[i][j] == 2)
            return 0;
        // If all the above conditions were false, it means our cell is having 0 and we can move further, so first make that cell to be invalid.
        obstacleGrid[i][j] = -1;
        // Now, perform for the four directions of current cell
        int totalPaths = DFS(obstacleGrid, i + 1, j, totalZeros, currentZeros + 1) +
                DFS(obstacleGrid, i - 1, j, totalZeros, currentZeros + 1) +
                DFS(obstacleGrid, i, j + 1, totalZeros, currentZeros + 1) +
                DFS(obstacleGrid, i, j - 1, totalZeros, currentZeros + 1);
        // Backtrack=>Make that cell as valid so that we can use this cell for other paths.
        obstacleGrid[i][j] = 0;
        // Finally return total number of valid paths.
        return totalPaths;
    }

    public int uniquePathsIII_3(int[][] grid) {

        int totalZeros = 0, startX = 0, startY = 0;
        // Find total number of zeros, we should move exactly once and also find starting cell indices.
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 0)
                    totalZeros++;
                if (grid[i][j] == 1) {
                    startX = i;
                    startY = j;
                }
            }
        }
        // Calling DFS Function.
        return DFS(grid, startX, startY, totalZeros, 0);
    }




}
