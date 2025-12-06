package LeetCodeJava.DFS;

// https://leetcode.com/problems/making-a-large-island/description/

import java.util.*;

/**
 * 827. Making A Large Island
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given an n x n binary matrix grid. You are allowed to change at most one 0 to be 1.
 *
 * Return the size of the largest island in grid after applying this operation.
 *
 * An island is a 4-directionally connected group of 1s.
 *
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0],[0,1]]
 * Output: 3
 * Explanation: Change one 0 to 1 and connect two 1s, then we get an island with area = 3.
 * Example 2:
 *
 * Input: grid = [[1,1],[1,0]]
 * Output: 4
 * Explanation: Change the 0 to 1 and make the island bigger, only one island with area = 4.
 * Example 3:
 *
 * Input: grid = [[1,1],[1,1]]
 * Output: 4
 * Explanation: Can't change any 0 to 1, only one island with area = 4.
 *
 *
 * Constraints:
 *
 * n == grid.length
 * n == grid[i].length
 * 1 <= n <= 500
 * grid[i][j] is either 0 or 1.
 *
 */
public class MakingALargeIsland {

    // V0
//    public int largestIsland(int[][] grid) {
//
//    }

    // V0-1
    // IDEA: 2 PASS DFS  (fixed by gemini)
    /**  NOTE !!!
     *
     *  Fixed & Optimized Java Solution (Two-Pass Approach)
     *
     *   The optimal solution uses a two-pass approach with DFS/BFS and a HashMap
     *   to achieve O(N^2) time complexity:
     *
     *   Pass 1 (Pre-calculate Areas):
     *       Run DFS over the entire grid to label each distinct island
     *       with a unique ID (e.g., 2, 3, 4, ...), and store
     *       the area of each ID in a HashMap.
     *
     *
     *   Pass 2 (Check Flips):
     *       Iterate over every zero cell (0). For each zero,
     *       check its four neighbors. Sum the areas of all unique neighboring
     *       islands (using the stored IDs) and add 1 (for the flipped cell).
     *
     */
    private int rows;
    private int cols;
    // Moves: (dr, dc)
    private final int[][] MOVES = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public int largestIsland_0_1(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        this.rows = grid.length;
        this.cols = grid[0].length;

        /** NOTE !!!
         *
         *   use hashMap save island
         *
         *    { Island ID : Area }
         */
        // Map to store: { Island ID : Area }
        Map<Integer, Integer> islandAreas = new HashMap<>();

        /** NOTE !!!
         *
         *   island ID starts from 2.
         *   e.g. 2, 3, 4 .....
         */
        // Start ID for the first island (using 2, since 0=water, 1=land)
        int currentIslandId = 2;
        int maxArea = 0;

        /** NOTE !!!
         *
         *   pass 1
         */
        // --- Pass 1: Label Islands and Pre-calculate Areas (O(N^2)) ---
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    /** NOTE !!!
                     *
                     *  the ONLY place dfs helper func is called
                     */
                    // DFS returns the area and simultaneously labels the island with currentIslandId
                    int area = dfsLabel(grid, r, c, currentIslandId);
                    // add to hashMap
                    islandAreas.put(currentIslandId, area);
                    maxArea = Math.max(maxArea, area);
                    currentIslandId++;
                }
            }
        }

        // Edge Case: If the entire grid is land (no zeros to flip), maxArea is the answer.
        if (maxArea == rows * cols) {
            return maxArea;
        }

        /** NOTE !!!
         *
         *   pass 2
         */
        // --- Pass 2: Check Every Zero Cell and Calculate Max Combined Area (O(N^2)) ---
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (grid[r][c] == 0) { // Found a cell to potentially flip

                    int combinedArea = 1; // Start with 1 for the cell being flipped (0 -> 1)

                    // Use a Set to avoid double-counting islands that neighbor the zero cell multiple times.
                    Set<Integer> neighborIslandIds = new HashSet<>();

                    for (int[] move : MOVES) {
                        int nextR = r + move[0];
                        int nextC = c + move[1];

                        /** NOTE !!!
                         *
                         *  we check if island id >= 2
                         *  ( grid[nextR][nextC] >= 2 )
                         *
                         *  -> we mark grid with island in the dfs func (dfsLabel)
                         *
                         *       // Mark the current cell with the unique island ID
                         *       grid[r][c] = id;
                         *
                         */
                        // Check bounds and ensure the neighbor is land (ID >= 2)
                        if (nextR >= 0 && nextR < rows && nextC >= 0 && nextC < cols && grid[nextR][nextC] >= 2) {

                            int neighborId = grid[nextR][nextC];

                            if (neighborIslandIds.add(neighborId)) { // Add only if unique ID
                                combinedArea += islandAreas.get(neighborId);
                            }
                        }
                    }

                    maxArea = Math.max(maxArea, combinedArea);
                }
            }
        }

        return maxArea;
    }

    /**
     * DFS helper to calculate the area and label the island.
     * @return The area of the island component found.
     */
    private int dfsLabel(int[][] grid, int r, int c, int id) {
        // Base Case: Check bounds or if already visited/water/already labeled.
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != 1) {
            return 0;
        }

        /** NOTE !!!
         *
         *  we Mark the current cell with the unique island ID
         */
        // Mark the current cell with the unique island ID
        grid[r][c] = id;

        // Recursive sum: 1 (for current cell) + area from neighbors
        return 1
                + dfsLabel(grid, r + 1, c, id)
                + dfsLabel(grid, r - 1, c, id)
                + dfsLabel(grid, r, c + 1, id)
                + dfsLabel(grid, r, c - 1, id);
    }


    // V0-2
    // IDEA: 2 PASS BFS (gemini)
//
//    private int rows;
//    private int cols;
//    private final int[][] MOVES = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public int largestIsland_0_2(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        this.rows = grid.length;
        this.cols = grid[0].length;

        Map<Integer, Integer> islandAreas = new HashMap<>();
        int currentIslandId = 2; // Start IDs from 2 (0=water, 1=initial land)
        int maxArea = 0;

        // --- Pass 1: Label Islands and Pre-calculate Areas (BFS) ---
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    // BFS returns the area and simultaneously labels the island with currentIslandId
                    int area = bfsLabel(grid, r, c, currentIslandId);
                    islandAreas.put(currentIslandId, area);
                    maxArea = Math.max(maxArea, area);
                    currentIslandId++;
                }
            }
        }

        // Edge Case: If the entire grid is land (no zeros to flip), maxArea is the answer.
        if (maxArea == rows * cols) {
            return maxArea;
        }

        // --- Pass 2: Check Every Zero Cell and Calculate Max Combined Area (Iterative) ---
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (grid[r][c] == 0) { // Found a cell to potentially flip

                    int combinedArea = 1; // Start with 1 for the cell being flipped (0 -> 1)
                    Set<Integer> neighborIslandIds = new HashSet<>();

                    for (int[] move : MOVES) {
                        int nextR = r + move[0];
                        int nextC = c + move[1];

                        // Check bounds and ensure the neighbor is a labeled island (ID >= 2)
                        if (nextR >= 0 && nextR < rows && nextC >= 0 && nextC < cols && grid[nextR][nextC] >= 2) {

                            int neighborId = grid[nextR][nextC];

                            // Only add the area if this island hasn't been counted yet for this '0' cell
                            if (neighborIslandIds.add(neighborId)) {
                                combinedArea += islandAreas.get(neighborId);
                            }
                        }
                    }

                    maxArea = Math.max(maxArea, combinedArea);
                }
            }
        }

        return maxArea;
    }

    /**
     * BFS helper to calculate the area and label the island with a unique ID.
     * @return The area of the island component found.
     */
    private int bfsLabel(int[][] grid, int r, int c, int id) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] { r, c });
        grid[r][c] = id; // Mark start cell immediately
        int area = 0;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int curR = current[0];
            int curC = current[1];
            area++; // Count the current cell toward the total area

            for (int[] move : MOVES) {
                int nextR = curR + move[0];
                int nextC = curC + move[1];

                // Check bounds and ensure it's unvisited land (grid[r][c] == 1)
                if (nextR >= 0 && nextR < rows && nextC >= 0 && nextC < cols && grid[nextR][nextC] == 1) {

                    // Label the cell with the new ID and add to queue
                    grid[nextR][nextC] = id;
                    queue.offer(new int[] { nextR, nextC });
                }
            }
        }

        return area;
    }


    // V1-1
    // IDEA: DFS
    // https://leetcode.com/problems/making-a-large-island/editorial/

    public int largestIsland_1_1(int[][] grid) {
        Map<Integer, Integer> islandSizes = new HashMap<>();
        int islandId = 2;

        // Step 1: Mark all islands and calculate their sizes
        for (int currentRow = 0; currentRow < grid.length; ++currentRow) {
            for (int currentColumn = 0; currentColumn < grid[0].length; ++currentColumn) {
                if (grid[currentRow][currentColumn] == 1) {
                    islandSizes.put(
                            islandId,
                            exploreIsland(grid, islandId, currentRow, currentColumn));
                    ++islandId;
                }
            }
        }

        // If there are no islands, return 1
        if (islandSizes.isEmpty()) {
            return 1;
        }
        // If the entire grid is one island, return its size or size + 1
        if (islandSizes.size() == 1) {
            --islandId;
            return (islandSizes.get(islandId) == grid.length * grid[0].length)
                    ? islandSizes.get(islandId)
                    : islandSizes.get(islandId) + 1;
        }

        int maxIslandSize = 1;

        // Step 2: Try converting every 0 to 1 and calculate the resulting island size
        for (int currentRow = 0; currentRow < grid.length; ++currentRow) {
            for (int currentColumn = 0; currentColumn < grid[0].length; ++currentColumn) {
                if (grid[currentRow][currentColumn] == 0) {
                    int currentIslandSize = 1;
                    Set<Integer> neighboringIslands = new HashSet<>();

                    // Check down
                    if (currentRow + 1 < grid.length &&
                            grid[currentRow + 1][currentColumn] > 1) {
                        neighboringIslands.add(
                                grid[currentRow + 1][currentColumn]);
                    }

                    // Check up
                    if (currentRow - 1 >= 0 &&
                            grid[currentRow - 1][currentColumn] > 1) {
                        neighboringIslands.add(
                                grid[currentRow - 1][currentColumn]);
                    }

                    // Check right
                    if (currentColumn + 1 < grid[0].length &&
                            grid[currentRow][currentColumn + 1] > 1) {
                        neighboringIslands.add(
                                grid[currentRow][currentColumn + 1]);
                    }

                    // Check left
                    if (currentColumn - 1 >= 0 &&
                            grid[currentRow][currentColumn - 1] > 1) {
                        neighboringIslands.add(
                                grid[currentRow][currentColumn - 1]);
                    }

                    // Sum the sizes of all unique neighboring islands
                    for (int id : neighboringIslands) {
                        currentIslandSize += islandSizes.get(id);
                    }

                    maxIslandSize = Math.max(maxIslandSize, currentIslandSize);
                }
            }
        }

        return maxIslandSize;
    }

    private int exploreIsland(
            int[][] grid,
            int islandId,
            int currentRow,
            int currentColumn) {
        if (currentRow < 0 ||
                currentRow >= grid.length ||
                currentColumn < 0 ||
                currentColumn >= grid[0].length ||
                grid[currentRow][currentColumn] != 1)
            return 0;

        grid[currentRow][currentColumn] = islandId;
        return (1 +
                exploreIsland(grid, islandId, currentRow + 1, currentColumn) +
                exploreIsland(grid, islandId, currentRow - 1, currentColumn) +
                exploreIsland(grid, islandId, currentRow, currentColumn + 1) +
                exploreIsland(grid, islandId, currentRow, currentColumn - 1));
    }



    // V1-1
    // IDEA: Disjoint Set Union (DSU)
    // https://leetcode.com/problems/making-a-large-island/editorial/
    class DisjointSet {

        public int[] parent;
        public int[] islandSize;

        // Constructor to initialize DSU with `n` elements
        public DisjointSet(int n) {
            parent = new int[n];
            islandSize = new int[n];
            for (int node = 0; node < n; node++) {
                // Each node is its own parent initially with size 1
                parent[node] = node;
                islandSize[node] = 1;
            }
        }

        // Function to find the root of a node with path compression
        public int findRoot(int node) {
            if (parent[node] == node)
                return node;
            return parent[node] = findRoot(parent[node]); // Path compression
        }

        // Function to union two sets based on size
        public void unionNodes(int nodeA, int nodeB) {
            int rootA = findRoot(nodeA);
            int rootB = findRoot(nodeB);

            // Already in the same set
            if (rootA == rootB)
                return;

            // Union by size: Attach the smaller island to the larger one
            if (islandSize[rootA] < islandSize[rootB]) {
                // Attach rootA to rootB
                parent[rootA] = rootB;
                // Update size of rootB's island
                islandSize[rootB] += islandSize[rootA];
            } else {
                // Attach rootB to rootA
                parent[rootB] = rootA;
                // Update size of rootA's island
                islandSize[rootA] += islandSize[rootB];
            }
        }
    }

    public int largestIsland_1_2(int[][] grid) {
        int rows = grid.length;
        int columns = grid[0].length;

        // Initialize DSU for the entire grid
        DisjointSet ds = new DisjointSet(rows * columns);

        // Direction vectors for traversing up, down, left, and right
        int[] rowDirections = { 1, -1, 0, 0 };
        int[] columnDirections = { 0, 0, 1, -1 };

        // Step 1: Union adjacent `1`s in the grid
        for (int currentRow = 0; currentRow < rows; currentRow++) {
            for (int currentColumn = 0; currentColumn < columns; currentColumn++) {
                if (grid[currentRow][currentColumn] == 1) {
                    // Flatten 2D index to 1D
                    int currentNode = (columns * currentRow) + currentColumn;

                    for (int direction = 0; direction < 4; direction++) {
                        int neighborRow = currentRow + rowDirections[direction];
                        int neighborColumn = currentColumn + columnDirections[direction];

                        // Check bounds and ensure the neighbor is also `1`
                        if (neighborRow >= 0 &&
                                neighborRow < rows &&
                                neighborColumn >= 0 &&
                                neighborColumn < columns &&
                                grid[neighborRow][neighborColumn] == 1) {
                            int neighborNode = columns * neighborRow + neighborColumn;
                            ds.unionNodes(currentNode, neighborNode);
                        }
                    }
                }
            }
        }

        // Step 2: Calculate the maximum possible island size
        int maxIslandSize = 0;
        // Flag to check if there are any zeros in the grid
        boolean hasZero = false;
        // To store unique roots for a `0`'s neighbors
        Set<Integer> uniqueRoots = new HashSet<>();

        for (int currentRow = 0; currentRow < rows; currentRow++) {
            for (int currentColumn = 0; currentColumn < columns; currentColumn++) {
                if (grid[currentRow][currentColumn] == 0) {
                    hasZero = true;
                    // Start with the flipped `0`
                    int currentIslandSize = 1;

                    for (int direction = 0; direction < 4; direction++) {
                        int neighborRow = currentRow + rowDirections[direction];
                        int neighborColumn = currentColumn + columnDirections[direction];

                        // Check bounds and ensure the neighbor is `1`
                        if (neighborRow >= 0 &&
                                neighborRow < rows &&
                                neighborColumn >= 0 &&
                                neighborColumn < columns &&
                                grid[neighborRow][neighborColumn] == 1) {
                            int neighborNode = columns * neighborRow + neighborColumn;
                            int root = ds.findRoot(neighborNode);
                            uniqueRoots.add(root);
                        }
                    }

                    // Sum up the sizes of unique neighboring islands
                    for (int root : uniqueRoots) {
                        currentIslandSize += ds.islandSize[root];
                    }

                    // Clear the set for the next `0`
                    uniqueRoots.clear();

                    // Update the result with the largest island size found
                    maxIslandSize = Math.max(maxIslandSize, currentIslandSize);
                }
            }
        }

        // If there are no zeros, the largest island is the entire grid
        if (!hasZero)
            return rows * columns;

        return maxIslandSize;
    }


    // V2
    // https://leetcode.ca/2018-03-06-827-Making-A-Large-Island/
    private int n;
    private int[] p;
    private int[] size;
    private int ans = 1;
    private int[] dirs = new int[] {-1, 0, 1, 0, -1};

    public int largestIsland_2(int[][] grid) {
        n = grid.length;
        p = new int[n * n];
        size = new int[n * n];
        for (int i = 0; i < p.length; ++i) {
            p[i] = i;
            size[i] = 1;
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 1) {
                    for (int k = 0; k < 4; ++k) {
                        int x = i + dirs[k], y = j + dirs[k + 1];
                        if (x >= 0 && x < n && y >= 0 && y < n && grid[x][y] == 1) {
                            int pa = find(x * n + y), pb = find(i * n + j);
                            if (pa == pb) {
                                continue;
                            }
                            p[pa] = pb;
                            size[pb] += size[pa];
                            ans = Math.max(ans, size[pb]);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 0) {
                    int t = 1;
                    Set<Integer> vis = new HashSet<>();
                    for (int k = 0; k < 4; ++k) {
                        int x = i + dirs[k], y = j + dirs[k + 1];
                        if (x >= 0 && x < n && y >= 0 && y < n && grid[x][y] == 1) {
                            int root = find(x * n + y);
                            if (!vis.contains(root)) {
                                vis.add(root);
                                t += size[root];
                            }
                        }
                    }
                    ans = Math.max(ans, t);
                }
            }
        }
        return ans;
    }

    private int find(int x) {
        if (p[x] != x) {
            p[x] = find(p[x]);
        }
        return p[x];
    }


    // V3


}
