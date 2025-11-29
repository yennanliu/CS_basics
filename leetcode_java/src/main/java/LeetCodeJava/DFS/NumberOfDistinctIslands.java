package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-distinct-islands/
// https://leetcode.ca/all/694.html

import java.util.HashSet;
import java.util.Set;

/**
 * 694. Number of Distinct Islands
 * Given a non-empty 2D array grid of 0's and 1's, an island is a group of 1's (representing land) connected 4-directionally (horizontal or vertical.) You may assume all four edges of the grid are surrounded by water.
 *
 * Count the number of distinct islands. An island is considered to be the same as another if and only if one island can be translated (and not rotated or reflected) to equal the other.
 *
 * Example 1:
 *
 * 11000
 * 11000
 * 00011
 * 00011
 * Given the above grid map, return 1.
 * Example 2:
 *
 * 11011
 * 10000
 * 00001
 * 11011
 * Given the above grid map, return 3.
 *
 * Notice that:
 * 11
 * 1
 * and
 *  1
 * 11
 * are considered different island shapes, because we do not consider reflection / rotation.
 * Note: The length of each dimension in the given grid does not exceed 50.
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Apple Bloomberg Facebook Google Lyft Microsoft Uber
 * Problem Solution
 * 694-Number-of-Distinct-Islands
 * All Problems:
 *
 */
public class NumberOfDistinctIslands {

    // V0
//    public int numDistinctIslands_2(int[][] grid) {
//
//    }

    // V1
    // IDEA: DFS
    // https://leetcode.ca/2017-10-24-694-Number-of-Distinct-Islands/
    private int m;
    private int n;
    private int[][] grid;
    private StringBuilder path = new StringBuilder();

    public int numDistinctIslands_1(int[][] grid) {
        m = grid.length;
        n = grid[0].length;
        this.grid = grid;
        Set<String> paths = new HashSet<>();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 1) {
                    dfs(i, j, 0);
                    paths.add(path.toString());
                    path.setLength(0);
                }
            }
        }
        return paths.size();
    }

    private void dfs(int i, int j, int k) {
        grid[i][j] = 0;
        path.append(k);
        int[] dirs = {-1, 0, 1, 0, -1};
        for (int h = 1; h < 5; ++h) {
            int x = i + dirs[h - 1];
            int y = j + dirs[h];
            if (x >= 0 && x < m && y >= 0 && y < n && grid[x][y] == 1) {
                dfs(x, y, h);
            }
        }
        path.append(k);
    }


    // V2
    // IDEA: DFS (gemini)
    // TODO: validate
    // Set to store the unique string representations (signatures) of the island shapes.
    /**
     *   NOTE !!!  KEY IDEAS & QUESTIONS:
     *
     *    -> per your first code, how can we ensure that the DFS and ALWAYS
     *      get the same `path signature `
     *      for the same land comparison ?
     *
     *
     *   -> answer:
     *
     *   it's `possible` to get different path signatures for the same land
     *   set if the DFS traversal order is NOT strictly defined.
     *
     *
     *   However, the dfs code provided below ensure the `order of path signatures`
     *
     *   ->
     *
     *      1. Starting Point Normalization
     *
     *         The main loop is responsible for starting the DFS on every island.
     *
     *         ```
     *         // Main loop structure
     *        for (int r = 0; r < rows; r++) {
     *          for (int c = 0; c < cols; c++) {
     *              if (grid[r][c] == 1) { // Start DFS only on unvisited land cells
     *             // ...
     *             dfs(grid, r, c, pathSignature, 'S');
     *             // ...
     *            }
     *          }
     *         }
     *         ```
     *
     *
     *     -> Implicit Start Point: The search iterates through the grid
     *        in a fixed order (top-to-bottom, left-to-right).
     *
     *     -> Guaranteed Consistency: For any given island shape,
     *        the DFS will always be triggered by the top-most,
     *         left-most land cell of that island, because that cell will be
     *        the first one reached in the grid traversal.
     *
     *
     *     -> Result: This ensures that all identical island shapes are traversed
     *         starting from the same relative origin
     *
     *
     *    2. Canonical Traversal Order
     *
     *
     *     -> it must always check the four neighbor directions in
     *        the exact same sequence.
     *
     *     -> the order is fixed as: Down (D), Up (U), Right (R), Left (L):
     *
     *     ```
     *     // Fixed, canonical order of recursive calls
     *     dfs(grid, r + 1, c, path, 'D'); // Down (D)
     *    dfs(grid, r - 1, c, path, 'U'); // Up (U)
     *    dfs(grid, r, c + 1, path, 'R'); // Right (R)
     *    dfs(grid, r, c - 1, path, 'L'); // Left (L)
     *     ```
     *
     *
     *  So,
     *
     *  Summary
     *
     *   1. The DFS approach guarantees a consistent path signature
     *      for identical shapes through the combination of:
     *      Guaranteed Starting Point (top-most, left-most cell).
     *
     *   2. Canonical Traversal Order (fixed D, U, R, L sequence in the recursive calls).
     *
     *   3. Relative Path Encoding (using directional characters and 'O' delimiters)
     *
     *
     *
     */
    private Set<String> uniqueIslandShapes = new HashSet<>();

    // Grid dimensions
    private int rows;
    private int cols;

    /**
     * Main function to find the number of distinct islands.
     * Time Complexity: O(R * C), where R is rows and C is columns.
     * Space Complexity: O(R * C) for recursion stack and the HashSet.
     */
    public int numDistinctIslands_2(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        this.rows = grid.length;
        this.cols = grid[0].length;

        // Iterate through every cell in the grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                // Start DFS only on unvisited land cells
                if (grid[r][c] == 1) {

                    // StringBuilder to build the unique path signature for the current island
                    StringBuilder pathSignature = new StringBuilder();

                    // Start DFS from (r, c). The starting direction is an arbitrary 'S' for Start.
                    // The path is encoded relative to the first (r, c) cell.
                    dfs(grid, r, c, pathSignature, 'S');

                    // The completed signature is added to the set of unique shapes.
                    if (pathSignature.length() > 0) {
                        uniqueIslandShapes.add(pathSignature.toString());
                    }
                }
            }
        }

        return uniqueIslandShapes.size();
    }

    /**
     * Helper function for DFS to traverse the island and build its shape signature.
     * * @param grid The grid, used for land/water checks and for marking visited cells (by setting to 0).
     * @param r The current row index.
     * @param c The current column index.
     * @param path The StringBuilder to record the path.
     * @param direction The direction taken to arrive at (r, c).
     */
    private void dfs(int[][] grid, int r, int c, StringBuilder path, char direction) {

        // Base Case 1: Out of bounds
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return;
        }

        // Base Case 2: Water (0) or already visited land (already marked as 0)
        if (grid[r][c] == 0) {
            return;
        }

        // 1. Mark the current cell as visited by setting it to water (0).
        // This prevents double counting and replaces the need for a separate visited array.
        grid[r][c] = 0;

        // 2. Record the direction of movement into this cell.
        path.append(direction);

        // 3. Recurse into neighbors and record the new direction taken.

        // Down (D)
        dfs(grid, r + 1, c, path, 'D');

        // Up (U)
        dfs(grid, r - 1, c, path, 'U');

        // Right (R)
        dfs(grid, r, c + 1, path, 'R');

        // Left (L)
        dfs(grid, r, c - 1, path, 'L');

        // 4. Crucial Step: Add an "Out" (O) delimiter when returning from this cell.
        // This marks the end of a branch and distinguishes shapes that follow different paths.
        path.append('O');
    }



    // V3
    // IDEA: DFS (GPT)
    // TODO: validate
    public int numDistinctIslands_3(int[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        int rows = grid.length, cols = grid[0].length;
        boolean[][] seen = new boolean[rows][cols];
        Set<String> shapes = new HashSet<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!seen[r][c] && grid[r][c] == 1) {
                    StringBuilder sb = new StringBuilder();
                    dfs(grid, seen, r, c, r, c, sb);
                    shapes.add(sb.toString());
                }
            }
        }
        return shapes.size();
    }

    /**
     * DFS from (r0,c0) to build a canonical shape signature by recording
     * relative coordinates or a traversal path.
     */
    private void dfs(int[][] grid, boolean[][] seen, int r0, int c0, int r, int c, StringBuilder sb) {
        int rows = grid.length, cols = grid[0].length;
        if (r < 0 || r >= rows || c < 0 || c >= cols) return;
        if (seen[r][c] || grid[r][c] != 1) return;

        seen[r][c] = true;
        // record relative position
        sb.append((r - r0)).append('_').append((c - c0)).append(',');

        dfs(grid, seen, r0, c0, r + 1, c, sb);
        dfs(grid, seen, r0, c0, r - 1, c, sb);
        dfs(grid, seen, r0, c0, r, c + 1, sb);
        dfs(grid, seen, r0, c0, r, c - 1, sb);
    }



}
