package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-distinct-islands/
// https://leetcode.ca/all/694.html
// https://leetcode.ca/2017-10-24-694-Number-of-Distinct-Islands/

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
//    public int numDistinctIslands(int[][] grid) {
//
//    }

    // V0-1
    // IDEA: DFS + PATH SIGNATURE (fixed by gemini)
    /** NOTE !!!
     *
     *   WE need to implement below, so be able to collect the `distinct` poth
     *
     *    1. record `direction` ; instead of grid (x,y) as path within DFS
     *      -> e.g. record (up, down, left,...) in path
     *      (Directional Encoding)
     *
     *    2. correct recursive call
     *
     *    3.`Backtrack Marker`
     *        - The mechanism to mark the return from a DFS branch is missing.
     *        - A unique delimiter (like 'O' for "Out" or 'X' for "Exit") is crucial to record when the DFS call returns from exploring a neighbor.
     */
    /**  NOTE !!!  backtrack marker'
     *
     *
     * That's an excellent point! The **backtrack marker** is perhaps the most crucial element in ensuring the path signature correctly captures the island's unique shape in LC 694.
     *
     * Here's a detailed explanation of why it's necessary and how it works, using a clear example.
     *
     * ---
     *
     * ## 🧭 Why the Backtrack Marker is Crucial
     *
     * The purpose of the path signature is to record the **relative movement** and the **structure** of the island. Without a marker when the recursion returns (backtracks), two topologically different shapes can produce the exact same path string, leading to an incorrect count of distinct islands.
     *
     * ### The Mechanism
     *
     * The DFS records two events for every land cell it visits:
     *
     * 1.  **Entry/Movement:** The directional character ('R', 'D', 'L', 'U') is recorded **before** the recursive call. This tells you *where* the island extends.
     * 2.  **Exit/Backtrack:** The delimiter ('O', 'X', or some other unique character) is recorded **after** the recursive call returns. This tells you *when* a branch of the island terminates.
     *
     * ### Example: L-Shape vs. T-Shape
     *
     * Consider the following two small islands (where '1' is land and 'S' is the starting cell), both starting at the top-left cell:
     *
     * | Island A: Simple L-Shape | Island B: T-Shape (with a dead-end) |
     * | :---: | :---: |
     * | S 1 0 0 | S 1 1 0 |
     * | 0 1 0 0 | 0 1 0 0 |
     * | 0 0 0 0 | 0 0 0 0 |
     *
     * ---
     *
     * ### Scenario 1: **Without** a Backtrack Marker
     *
     * If we only record the directional movements (D, R, U, L) but **omit the 'X' delimiter**:
     *
     * | Island | Path Sequence | Resulting Signature (Incorrect) |
     * | :--- | :--- | :--- |
     * | **A (L-Shape)** | Start at S $\rightarrow$ Go Right (R) $\rightarrow$ Go Down (D) $\rightarrow$ End | **`S R D`** |
     * | **B (T-Shape)** | Start at S $\rightarrow$ Go Right (R) $\rightarrow$ Go Right again (R) $\rightarrow$ Dead-end, backtrack $\rightarrow$ Go Down (D) $\rightarrow$ End | **`S R D`** |
     *
     * **Conclusion:** Without the 'X' marker, both islands result in the same signature, `S R D`, even though they are clearly different shapes. This is a false positive for similarity.
     *
     * ---
     *
     * ### Scenario 2: **With** the Backtrack Marker ('X' for Exit)
     *
     * Now, let's include the marker **`path.append('X');`** after *all* recursive calls return for a cell:
     *
     * | Island | Path Sequence | Resulting Signature (Correct) |
     * | :--- | :--- | :--- |
     * | **A (L-Shape)** | S $\rightarrow$ R (to first 1) $\rightarrow$ **D** (to second 1) $\rightarrow$ (returns) $\rightarrow$ **X** (exit second 1) $\rightarrow$ (returns) $\rightarrow$ **X** (exit first 1) $\rightarrow$ (returns) $\rightarrow$ **X** (exit S) | **`S R D X X X`** |
     * | **B (T-Shape)** | S $\rightarrow$ **R** (to first 1) $\rightarrow$ **R** (to second 1) $\rightarrow$ (dead-end) $\rightarrow$ **X** (exit second 1) $\rightarrow$ (returns) $\rightarrow$ **D** (to third 1) $\rightarrow$ (returns) $\rightarrow$ **X** (exit third 1) $\rightarrow$ (returns) $\rightarrow$ **X** (exit first 1) $\rightarrow$ (returns) $\rightarrow$ **X** (exit S) | **`S R R X D X X X`** |
     *
     * **Conclusion:**
     *
     * The unique pattern of directional moves and 'X'
     * markers now correctly distinguishes the two shapes
     * (`S R D X X X` vs. `S R R X D X X X`).
     * The markers precisely encode the moment a path branch terminates
     * and the traversal backtracks to a previous cell.
     *
     *
     *
     */
    private int rows_0_1;
    private int cols_0_1;

    /**
     * Finds the number of distinct island shapes using DFS and path encoding.
     * Time Complexity: O(R * C)
     */
    public int numDistinctIslands_0_1(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        this.rows_0_1 = grid.length;
        this.cols_0_1 = grid[0].length;

        // Set to store the unique string representations (signatures) of the island shapes.
        Set<String> uniqueIslandShapes = new HashSet<>();

        for (int r = 0; r < rows_0_1; r++) {
            for (int c = 0; c < cols_0_1; c++) {

                // Start DFS only on unvisited land cells (grid[r][c] == 1)
                if (grid[r][c] == 1) {

                    StringBuilder pathSignature = new StringBuilder();

                    // Start DFS. 'O' marks the beginning of the traversal (Origin).
                    // The traversal order is fixed to ensure the signature is canonical.
                    dfsDistinctHelper(grid, r, c, pathSignature, 'O');

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
     * @param grid The grid (used to check land/water and mark visited cells by setting to 0).
     * @param r The current row index.
     * @param c The current column index.
     * @param path The StringBuilder to record the path.
     * @param direction The direction taken to arrive at (r, c).
     */
    private void dfsDistinctHelper(int[][] grid, int r, int c, StringBuilder path, char direction) {

        // --- Base Case 1: Out of bounds ---
        if (r < 0 || r >= rows_0_1 || c < 0 || c >= cols_0_1) {
            return;
        }

        // --- Base Case 2: Water (0) or already visited land (already marked as 0) ---
        if (grid[r][c] == 0) {
            return;
        }

        // 1. Mark the current cell as visited by setting it to water (0).
        grid[r][c] = 0;

        // 2. Record the direction of movement into this cell (Normalization).
        path.append(direction);

        // 3. Recurse into neighbors in a fixed, canonical order (D, U, R, L) to ensure consistent signatures.

        // Down (D)
        dfsDistinctHelper(grid, r + 1, c, path, 'D');

        // Up (U)
        dfsDistinctHelper(grid, r - 1, c, path, 'U');

        // Right (R)
        dfsDistinctHelper(grid, r, c + 1, path, 'R');

        // Left (L)
        dfsDistinctHelper(grid, r, c - 1, path, 'L');

        // 4. Crucial Step: Add an "Out" (X) delimiter when the function returns from this cell.
        // This marks the end of a branch and ensures shapes with different branch structures
        // are correctly distinguished (e.g., L vs. T).
        path.append('X');
    }

    // V0-2
    // IDEA: DFS + PATH SIGNATURE (fixed by gpt)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int numDistinctIslands_0_2(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        Set<String> shapes = new HashSet<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    StringBuilder sb = new StringBuilder();
                    dfs(grid, r, c, r, c, sb);
                    shapes.add(sb.toString());
                }
            }
        }

        return shapes.size();
    }

    private void dfs(int[][] grid, int r0, int c0, int r, int c, StringBuilder sb) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Mark visited
        grid[r][c] = -1;

        // Record relative coordinate
        sb.append((r - r0) + "," + (c - c0) + ";");

        // directions: up, down, left, right
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];

            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                dfs(grid, r0, c0, nr, nc, sb);
            }
        }
    }

    // V0-3
    // IDEA: DFS + PATH SIGNATURE
    // NOTE !!! below is WRONG
    // just for reference
    /**  Issue of code below:
     *
     *   Here are the critical issues:
     *
     *   ## 🛑 Critical Issues in Island Encoding (LC 694)
     *
     * The provided approach for encoding island shape using
     * absolute coordinates fails due to the following critical flaws:
     *
     * ---
     *
     * ### 1. Absolute Coordinate Encoding Fails Normalization
     *
     *  **Issue:** Appending the absolute coordinates
     *             $(x, y)$ to the string (e.g., `sb.append(x + "-" + y)`)
     *             fails the core requirement of the problem.
     *
     *  * **Problem:** Two identical island shapes in different grid locations
     *                  will produce different signature strings (e.g., `"1-1"` vs. `"5-5"`).
     *
     *  * **Result:** This causes them to be **incorrectly counted as
     *                distinct** islands. **Normalization**
     *                (encoding the path relative to the starting cell) is required.
     *
     * ---
     *
     * ### 2. Missing Directional Encoding
     *
     * * **Issue:** The path string encoding is missing the
     *              **direction** of the movement that was just
     *              taken (e.g., 'U', 'D', 'L', 'R').
     *
     *
     * * **Problem:** The string must record the sequence
     *                of **relative steps** from one land cell
     *                to the next to capture the shape.
     *
     * ---
     *
     * ### 3. Incorrect Recursive Call
     *
     * * **Issue:** The recursive call is fundamentally broken:
     *               `dfsDistinctHelper(x, y, grid, sb)` uses the
     *               **same coordinates** $(x, y)$ inside the loop.
     *
     *
     * * **Problem:** It fails to pass the calculated
     *                **new neighbor coordinates**
     *                $(x\_, y\_)$ to the next DFS call.
     *
     *
     * * **Result:** This inevitably leads to an **infinite loop**
     *               or a traversal that never moves off the starting cell.
     *
     * ---
     *
     * ### 4. Missing Backtrack Marker (Delimiter)
     *
     * * **Issue:** The mechanism to mark the return from a DFS branch is missing.
     *
     * * **Problem:** A unique delimiter (like 'O' for "Out" or 'X' for "Exit")
     *                 is crucial to record when the DFS call
     *                 *returns* from exploring a neighbor.
     *
     * * **Result:** Without this marker, the resulting string cannot
     *               distinguish between different branching structures
     *               (e.g., an L-shape that immediately dead-ends vs. a T-shape that continues).
     *
     */
//    public int numDistinctIslands(int[][] grid) {
//        // edge
//        if(grid == null || grid.length == 0 || grid[0].length == 0){
//            return 0;
//        }
//        // ??
//        if(grid.length == 1 && grid[0].length == 1){
//            return grid[0][0];
//        }
//
//        int l = grid.length;
//        int w = grid[0].length;
//
//        // int cnt = 0;
//        // boolean[][] visited = new boolean[l][w];
//
//        // ????
//        Set<String> set = new HashSet<>();
//
//        // NOTE !!!
//        // we ALWAYS take the `start point`
//        // start from smallest y, smallest x
//        for(int y = 0; y < l; y++){
//            for(int x = 0; x < w; x++){
//                if(grid[y][x] == 1){
//                    String path = dfsDistinctHelper(x, y, grid, new StringBuilder());
//                    // ????
//                    if(!path.isEmpty()){
//                        set.add(path);
//                    }
//                }
//            }
//        }
//
//
//        System.out.println(">>>> set = " + set);
//
//        return set.size();
//    }
//
//    private String dfsDistinctHelper(int x, int y, int[][] grid, StringBuilder sb){
//        int l = grid.length;
//        int w = grid[0].length;
//
//        int[][] moves = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };
//
//        // add to set
//        sb.append(x + "-" + y); /// ????
//        // mark as visited
//        grid[y][x] = -1;
//
//        // NOTE !!!
//        // we ALWAYS move the `path`
//        // in `{ {0,1}, {0,-1}, {1,0}, {-1,0} }` ordering
//        for(int[] m: moves){
//            int x_ = x + m[1];
//            int y_ = y + m[0];
//            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && grid[y_][x_] == 1){
//                dfsDistinctHelper(x, y, grid, sb);
//            }
//        }
//
//        // ???
//        return sb.toString();
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

        /**
         *  NOTE !!!
         *
         *   we iterate from
         *     up to down,
         *     left to right
         *
         *  -> so it make sure that
         *    we ALWAYS pick the `SAME relative starting point` of land
         *    when build the `path signature`
         *
         */
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

        /**
         *  NOTE !!!
         *
         *   we ALWAYS iterate on below ordering:
         *
         *     down -> up -> right -> left
         *
         *  -> so it make sure that
         *    we ALWAYS `move the same way` of land
         *    when build the `path signature`
         */
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
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
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
