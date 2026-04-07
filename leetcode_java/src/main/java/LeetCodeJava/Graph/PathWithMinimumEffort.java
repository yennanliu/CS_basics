package LeetCodeJava.Graph;

// https://leetcode.com/problems/path-with-minimum-effort/description/
// https://leetcode.cn/problems/path-with-minimum-effort/description/

import java.util.*;

/**
 * 1631. Path With Minimum Effort
 * Medium
 * Topics
 * Companies
 * Hint
 * You are a hiker preparing for an upcoming hike. You are given heights, a 2D array of size rows x columns, where heights[row][col] represents the height of cell (row, col). You are situated in the top-left cell, (0, 0), and you hope to travel to the bottom-right cell, (rows-1, columns-1) (i.e., 0-indexed). You can move up, down, left, or right, and you wish to find a route that requires the minimum effort.
 *
 * A route's effort is the maximum absolute difference in heights between two consecutive cells of the route.
 *
 * Return the minimum effort required to travel from the top-left cell to the bottom-right cell.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: heights = [[1,2,2],[3,8,2],[5,3,5]]
 * Output: 2
 * Explanation: The route of [1,3,5,3,5] has a maximum absolute difference of 2 in consecutive cells.
 * This is better than the route of [1,2,2,2,5], where the maximum absolute difference is 3.
 * Example 2:
 *
 *
 *
 * Input: heights = [[1,2,3],[3,8,4],[5,3,5]]
 * Output: 1
 * Explanation: The route of [1,2,3,4,5] has a maximum absolute difference of 1 in consecutive cells, which is better than route [1,3,5,3,5].
 * Example 3:
 *
 *
 * Input: heights = [[1,2,1,1,1],[1,2,1,2,1],[1,2,1,2,1],[1,2,1,2,1],[1,1,1,2,1]]
 * Output: 0
 * Explanation: This route does not require any effort.
 *
 *
 * Constraints:
 *
 * rows == heights.length
 * columns == heights[i].length
 * 1 <= rows, columns <= 100
 * 1 <= heights[i][j] <= 106
 *
 */
public class PathWithMinimumEffort {

    /** NOTE !!!
     *
     *   we CAN'T use DP for LC 1631
     *
     *   (No, standard DP (Top-Down or Bottom-Up) does not work for LC 1631 (Path With Minimum Effort).)
     *
     *   ------
     *
     *
     *   1. Why DP fails for LC 1631
     *
     *   In LC 64 (Minimum Path Sum), you can only move Right or Down. This creates a "Directed Acyclic Graph" (DAG), which is the requirement for DP.
     *
     *   -> In LC 1631, you can move in four directions
     *    (Up, Down, Left, Right).
     *    Because you can move `"backwards"` to find a path w
     *    ith a smaller maximum difference,
     *    a simple DP table CAN NOT capture the dependencies—you'd
     *    end up with `circular` dependencies.
     *
     *
     *   -> For this problem, the "Gold Standard" is
     *   Dijkstra's Algorithm or Binary Search + DFS/BFS.
     *
     *
     */

    /**  NOTE !!!  LC 64 VS LC 1631
     *
     *
     * ✅ Leetcode 64: Minimum Path Sum
     *
     * Key property:
     * 	•	You can only move right or down.
     * 	•	The cost is accumulative: you sum values along the path.
     * 	•	Since you can’t revisit a cell from a different direction, you don’t need visited.
     * 	•	DP is perfect here. Every cell is updated once with the best possible value from top or left.
     *
     * ✅ No visited needed:
     * 	•	Each cell is filled once.
     * 	•	You never have to worry about improving a previous path.
     * 	•	No cycles. No need to guard against revisiting.
     *
     * ⸻
     *
     * 🔁 Leetcode 1631: Path With Minimum Effort
     *
     * Key property:
     * 	•	You can move in all four directions (up/down/left/right).
     * 	•	Cost is not additive, it’s based on the maximum absolute height difference between steps.
     * 	•	You might find a better path to a cell after already visiting it.
     * 	•	This is Dijkstra-style, but the edge weight is non-linear (max of step costs).
     *
     * ✅ visited is needed here:
     * 	•	You must revisit nodes if a better path is found.
     * 	•	To avoid processing worse paths, you mark nodes as visited once the minimum effort to reach them is finalized.
     * 	•	Without visited, you could end up adding multiple paths for the same cell and wasting computation.
     *
     * ⸻
     *
     * 🔍 Summary:
     *
     * Problem	Move Directions	Cost Definition	Can revisit cells with better cost?	Needs visited?
     * Minimum Path Sum (64)	Right + Down only	Sum of grid values	❌ No	❌ No
     * Path With Minimum Effort (1631)	All 4 directions	Max of step differences	✅ Yes	✅ Yes
     *
     */

    // V0
    // IDEA: Dijkstra (fixed by gemini)
    public int minimumEffortPath(int[][] heights) {
        int l = heights.length; // rows (y)
        int w = heights[0].length; // cols (x)

        /** NOTE !!!
         *
         *    we need `effortArr` to track
         *    `processed` path (effort),
         *    so it can be used as Dijkstra logic
         *
         *    -> (e.g. ONLY process if new path cost < prev path cost)
         */
        // 1. Initialize 2D cost array with MAX_VALUE
        int[][] effortArr = new int[l][w];
        for (int[] row : effortArr) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        // 2. PriorityQueue: {x, y, effort} sorted by effort
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[2] - b[2]);

        // Starting point: 0 effort to start at (0,0)
        effortArr[0][0] = 0;
        pq.add(new int[] { 0, 0, 0 });

        int[][] moves = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int x = cur[0];
            int y = cur[1];
            int currentEffort = cur[2];

            // 3. Arrived at Destination
            if (x == w - 1 && y == l - 1) {
                return currentEffort;
            }

            /** NOTE !!!
             *
             *  skip if `last` effort < `cur` effort.
             *
             *  (e.g. if last process has less effort)
             */
            // 4. Optimization: Skip if we found a better way already
            if (currentEffort > effortArr[y][x]) {
                continue;
            }

            for (int[] m : moves) {
                int nx = x + m[1]; // x corresponds to col (moves[1])
                int ny = y + m[0]; // y corresponds to row (moves[0])

                if (nx >= 0 && nx < w && ny >= 0 && ny < l) {


                    /** NOTE !!!
                     *
                     *  how we get
                     *    1. step effort
                     *    2. next effort
                     */
                    // NEW EFFORT = MAX(previous effort, current step's diff)
                    int stepDiff = Math.abs(heights[ny][nx] - heights[y][x]);
                    int nextEffort = Math.max(currentEffort, stepDiff);

                    /** NOTE !!!
                     *
                     *  ONLY process if `new effort < cur effort`
                     *
                     */
                    if (nextEffort < effortArr[ny][nx]) {
                        effortArr[ny][nx] = nextEffort;
                        pq.add(new int[] { nx, ny, nextEffort });
                    }
                }
            }
        }

        return 0;
    }


    // V0-1
    // IDEA: Dijkstra's ALGO ( fixed by gpt) : min PQ + BFS
    /**
     * time = O((V + E) log V)
     * space = O(V)
     */
    public int minimumEffortPath_0_1(int[][] heights) {
        if (heights == null || heights.length == 0)
            return 0;

        int rows = heights.length;
        int cols = heights[0].length;

        // Min-heap: [effort, x, y]
        PriorityQueue<int[]> minPQ = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minPQ.offer(new int[] { 0, 0, 0 }); // effort, x, y

        boolean[][] visited = new boolean[rows][cols];
        int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        while (!minPQ.isEmpty()) {
            int[] cur = minPQ.poll();
            int effort = cur[0], x = cur[1], y = cur[2];

            if (x == rows - 1 && y == cols - 1) {
                return effort;
            }

      /**  NOTE !!!  need `visited, to NOT revisited visited cells (`Dijkstra algo`)
       *
       *   Reason:
       *
       *
       *   Great question — and you’re absolutely right to raise this.
       *
       * ✅ Short Answer:
       *
       * Yes, in Dijkstra’s algorithm for the “minimum effort path” problem,
       * we still need a visited check — but only after the shortest
       * effort to a cell has been finalized.
       *
       * That is:
       * 	•	Once we’ve popped a cell (x, y) from the priority queue,
       *     	the effort it took to reach it is `guaranteed` to be `minimal`,
       *      	due to how the min-heap works.
       *
       * 	•	After that point, there’s `NO need` to `revisit` that cell —
       *    	any future path that reaches (x, y) will have equal or greater effort,
       *    	and can be safely ignored.
       *
       * This is different from classic BFS where all edges are equal weight —
       * but in Dijkstra, this greedy behavior is valid and optimal.
       *
       * ⸻
       *
       * 🤔 Why Not Revisit?
       *
       * Let’s break it down:
       *
       * In Dijkstra:
       * 	•	The min-heap (priority queue) guarantees that we always expand the least effort path so far.
       * 	•	If a cell is reached for the first time, it’s the best effort you’ll ever see to reach it.
       * 	•	If you allow revisiting, you’ll reprocess worse paths and slow down the algorithm.
       *
       * ⸻
       *
       * 📌 Exception:
       *
       * If you were doing plain BFS with no heap, or non-Dijkstra variants,
       * you’d need to revisit when a better cost is found later (like in Bellman-Ford).
       * But with Dijkstra and a correct min-heap structure,
       * no revisits are necessary after finalization.
       *
       * ⸻
       *
       * ✅ Key Rule:
       *
       * In Dijkstra:
       * Once you pop a node (x, y) from the min-heap and mark it visited,
       * you do not need to revisit it — its shortest (or in this case, minimum effort) path is finalized.
       *
       */
      if (visited[x][y]) {
                continue;
            }

            visited[x][y] = true;

            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (nx >= 0 && ny >= 0 && nx < rows && ny < cols && !visited[nx][ny]) {
                    int newEffort = Math.max(effort, Math.abs(heights[nx][ny] - heights[x][y]));
                    minPQ.offer(new int[] { newEffort, nx, ny });
                }
            }
        }

        return -1; // Should never reach here if input is valid
    }

    // V1
    // https://www.youtube.com/watch?v=XQlxCCx2vI4
    // https://github.com/neetcode-gh/leetcode/blob/main/python%2F1631-path-with-minimum-effort.py
    // https://github.com/neetcode-gh/leetcode/blob/main/kotlin%2F1631-path-with-minimum-effort.kt
    // python

    // V2
    // https://leetcode.com/problems/path-with-minimum-effort/solutions/4049557/9767-optimal-dijkstra-with-heap-by-vanam-1rxv/
    // IDEA: MIN Heap (PQ)
    /**
     * time = O((V + E) log V)
     * space = O(V)
     */

    public int minimumEffortPath_2(int[][] heights) {

        int rows = heights.length;
        int cols = heights[0].length;

        int[][] dist = new int[rows][cols];

        // NOTE !!! we define `min heap` below
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        // add start point as initial state
        minHeap.add(new int[] { 0, 0, 0 });

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dist[i][j] = Integer.MAX_VALUE;
            }
        }
        dist[0][0] = 0;

        int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        while (!minHeap.isEmpty()) {

            int[] top = minHeap.poll();

            int effort = top[0];
            int x = top[1];
            int y = top[2];

            if (effort > dist[x][y]){
                continue;
            }

            if (x == rows - 1 && y == cols - 1)
                return effort;

            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols) {
                    int new_effort = Math.max(effort, Math.abs(heights[x][y] - heights[nx][ny]));
                    if (new_effort < dist[nx][ny]) {
                        dist[nx][ny] = new_effort;
                        minHeap.add(new int[] { new_effort, nx, ny });
                    }
                }
            }
        }
        return -1;
    }

    // V3-1
    // https://leetcode.com/problems/path-with-minimum-effort/solutions/4049576/9992-dijkstras-algorithm-binary-search-c-1jtm/
    // IDEA: Dijkstra's ALGO
    private int[][] effort = new int[105][105]; // Store effort for each cell
    private int[] dx = { 0, 1, -1, 0 }; // Changes in x coordinate for each direction
    private int[] dy = { 1, 0, 0, -1 }; // Changes in y coordinate for each direction

    // Dijkstra's Algorithm to find minimum effort
    private int dijkstra(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;

        // Priority queue to store {effort, {x, y}}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(-a[0], -b[0]));
        pq.add(new int[] { 0, 0, 0 }); // Start from the top-left cell
        effort[0][0] = 0; // Initial effort at the starting cell

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int cost = -current[0]; // Effort for the current cell

            int x = current[1];
            int y = current[2];

            // Skip if we've already found a better effort for this cell
            if (cost > effort[x][y])
                continue;

            // Stop if we've reached the bottom-right cell
            if (x == rows - 1 && y == cols - 1)
                return cost;

            // Explore each direction (up, down, left, right)
            for (int i = 0; i < 4; i++) {
                int new_x = x + dx[i];
                int new_y = y + dy[i];

                // Check if the new coordinates are within bounds
                if (new_x < 0 || new_x >= rows || new_y < 0 || new_y >= cols)
                    continue;

                // Calculate new effort for the neighboring cell
                int new_effort = Math.max(effort[x][y], Math.abs(heights[x][y] - heights[new_x][new_y]));

                // Update effort if a lower effort is found for the neighboring cell
                if (new_effort < effort[new_x][new_y]) {
                    effort[new_x][new_y] = new_effort;
                    pq.add(new int[] { -new_effort, new_x, new_y });
                }
            }
        }
        return effort[rows - 1][cols - 1]; // Minimum effort for the path to the bottom-right cell
    }

    // Function to find the minimum effort path
    /**
     * time = O(E * α(V))
     * space = O(V)
     */

    public int minimumEffortPath_3_1(int[][] heights) {
        int rows = heights.length;
        int cols = heights[0].length;

        // Initialize effort for each cell to maximum value
        for (int i = 0; i < rows; i++) {
            Arrays.fill(effort[i], Integer.MAX_VALUE);
        }

        return dijkstra(heights); // Find minimum effort using dijkstra
    }


    // V3-2
    // https://leetcode.com/problems/path-with-minimum-effort/solutions/4049576/9992-dijkstras-algorithm-binary-search-c-1jtm/
    // IDEA: BINARY SEARCH
    private boolean[][] visited; // Visited cells tracker
    private int[] directions_x = { 0, 1, -1, 0 }; // Changes in x coordinate for four directions
    private int[] directions_y = { 1, 0, 0, -1 }; // Changes in y coordinate for four directions
    private int numRows, numCols; // Number of rows and columns in the matrix

    // Depth-First Search to explore the path with a given limit effort
    private void dfs(int x, int y, int limitEffort, int[][] heights) {
        if (visited[x][y])
            return;
        visited[x][y] = true;

        // Stop if we've reached the bottom-right cell
        if (x == numRows - 1 && y == numCols - 1)
            return;

        // Explore each direction (up, down, left, right)
        for (int i = 0; i < 4; i++) {
            int new_x = x + directions_x[i];
            int new_y = y + directions_y[i];

            // Check if the new coordinates are within bounds
            if (new_x < 0 || new_x >= numRows || new_y < 0 || new_y >= numCols)
                continue;

            // Go to next cell if the effort is within limit
            int newEffort = Math.abs(heights[x][y] - heights[new_x][new_y]);
            if (newEffort <= limitEffort)
                dfs(new_x, new_y, limitEffort, heights);
        }
    }

    /**
     * time = O(V + E)


     * space = O(V)


     */


    public int minimumEffortPath_3_2(int[][] heights) {
        numRows = heights.length;
        numCols = heights[0].length;

        // Initialize visited array
        visited = new boolean[numRows][numCols];

        // Bound for our binary search
        int lowerLimit = 0, upperLimit = 1_000_000;

        while (lowerLimit < upperLimit) {
            int mid = (upperLimit + lowerLimit) / 2;
            for (boolean[] row : visited) {
                Arrays.fill(row, false);
            }

            dfs(0, 0, mid, heights);

            if (visited[numRows - 1][numCols - 1])
                upperLimit = mid;
            else
                lowerLimit = mid + 1;
        }

        return lowerLimit;
    }

    // V4-1
    // https://leetcode.com/problems/path-with-minimum-effort/solutions/1036518/java-3-clean-codes-dijkstras-algo-union-1k2ga/
    // IDEA: Dijkstra's algorithm
    private static final int[][] dir = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    /**
     * time = O((V + E) log V)


     * space = O(V)


     */
    public int minimumEffortPath_4_1(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++)
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        dist[0][0] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[2] - b[2]);
        pq.add(new int[] { 0, 0, 0 });
        while (!pq.isEmpty()) {
            int[] p = pq.poll();
            int i = p[0], j = p[1];
            if (i == m - 1 && j == n - 1)
                break;
            for (int[] d : dir) {
                int x = i + d[0], y = j + d[1];
                if (x < 0 || x >= m || y < 0 || y >= n)
                    continue;
                int alt = Math.max(p[2], Math.abs(heights[i][j] - heights[x][y]));
                if (alt < dist[x][y]) {
                    pq.add(new int[] { x, y, dist[x][y] = alt });
                }
            }
        }
        return dist[m - 1][n - 1];
    }


    // V4-2
    // https://leetcode.com/problems/path-with-minimum-effort/solutions/1036518/java-3-clean-codes-dijkstras-algo-union-1k2ga/
    // IDEA: Union Find / Kruskal's algorithm
    class UnionFind {
        private int[] parent, size;

        UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        /**
         * time = O(E * α(V))


         * space = O(V)


         */


        public int find(int x) {
            return x == parent[x] ? x : (parent[x] = find(parent[x]));
        }

        /**
         * time = O(E * α(V))


         * space = O(V)


         */


        public void union(int x, int y) {
            int a = find(x), b = find(y);
            if (a == b)
                return;
            if (size[a] < size[b]) {
                parent[a] = b;
                size[b] += size[a];
            } else {
                parent[b] = a;
                size[a] += size[b];
            }
        }
    }

    /**
     * time = O(V + E)


     * space = O(V)


     */


    public int minimumEffortPath_4_2(int[][] heights) {
        int m = heights.length, n = heights[0].length;
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            if (i > 0) {
                for (int j = 0; j < n; j++) {
                    edges.add(new int[] { (i - 1) * n + j, i * n + j, Math.abs(heights[i - 1][j] - heights[i][j]) });
                }
            }
            for (int j = 0; j < n - 1; j++) {
                edges.add(new int[] { i * n + j, i * n + j + 1, Math.abs(heights[i][j] - heights[i][j + 1]) });
            }
        }
        Collections.sort(edges, (a, b) -> a[2] - b[2]);
        UnionFind uf = new UnionFind(m * n);
        int src = 0, dest = m * n - 1, i = 0;
        while (uf.find(src) != uf.find(dest)) {
            uf.union(edges.get(i)[0], edges.get(i++)[1]);
        }
        return i == 0 ? 0 : edges.get(i - 1)[2];
    }


    // V4-3
    // https://leetcode.com/problems/path-with-minimum-effort/solutions/1036518/java-3-clean-codes-dijkstras-algo-union-1k2ga/
    // IDEA: Binary search + DFS
    private boolean dfs(int[][] h, int i, int j, int prev, int limit, boolean[][] visited) {
        if (i < 0 || i >= h.length || j < 0 || j >= h[0].length || visited[i][j] || Math.abs(h[i][j] - prev) > limit)
            return false;
        visited[i][j] = true;
        return (i == h.length - 1 && j == h[0].length - 1) || dfs(h, i - 1, j, h[i][j], limit, visited)
                || dfs(h, i + 1, j, h[i][j], limit, visited) || dfs(h, i, j - 1, h[i][j], limit, visited)
                || dfs(h, i, j + 1, h[i][j], limit, visited);
    }

    /**
     * time = O((V + E) log V)


     * space = O(V)


     */


    public int minimumEffortPath_4_3(int[][] heights) {
        int l = 0, r = 1_000_000;
        while (l < r) {
            int m = l + ((r - l) >> 1);
            if (dfs(heights, 0, 0, heights[0][0], m, new boolean[heights.length][heights[0].length])) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        return l;
    }




}
