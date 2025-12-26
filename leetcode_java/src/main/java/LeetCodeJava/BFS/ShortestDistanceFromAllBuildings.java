package LeetCodeJava.BFS;

// https://leetcode.com/problems/shortest-distance-from-all-buildings/description/
// https://leetcode.ca/2016-10-12-317-Shortest-Distance-from-All-Buildings/

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 317. Shortest Distance from All Buildings
 * You want to build a house on an empty land which reaches all buildings in the shortest amount of distance. You can only move up, down, left and right. You are given a 2D grid of values 0, 1 or 2, where:
 *
 * Each 0 marks an empty land which you can pass by freely.
 * Each 1 marks a building which you cannot pass through.
 * Each 2 marks an obstacle which you cannot pass through.
 * Example:
 *
 * Input: [[1,0,2,0,1],[0,0,0,0,0],[0,0,1,0,0]]
 *
 * 1 - 0 - 2 - 0 - 1
 * |   |   |   |   |
 * 0 - 0 - 0 - 0 - 0
 * |   |   |   |   |
 * 0 - 0 - 1 - 0 - 0
 *
 * Output: 7
 *
 * Explanation: Given three buildings at (0,0), (0,4), (2,2), and an obstacle at (0,2),
 *              the point (1,2) is an ideal empty land to build a house, as the total
 *              travel distance of 3+3+1=7 is minimal. So return 7.
 * Note:
 * There will be at least one building. If it is not possible to build such house according to the above rules, return -1.
 *
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Amazon ByteDance Facebook Goldman Sachs Google Mathworks Microsoft Snapchat Splunk Uber Zenefits
 * Problem Solution
 * 317-Shortest-Distance-from-All-Buildings
 *
 */
public class ShortestDistanceFromAllBuildings {

    // V0
//    public int shortestDistance(int[][] grid) {
//
//    }

    // V0-1
    // IDEA: multi-source BFS  + count, dist matrix (gemini)
    // TODO: validate
    /**  IDEA:
     *
     * To solve LeetCode 317: Shortest Distance from All Buildings,
     * the most efficient approach is to perform a Breadth-First Search
     * (BFS) starting from each building.
     *
     * Problem Logic
     *
     *  - Perspective:
     *      Instead of checking every empty land (0)
     *      and looking for all buildings (1),
     *      we start from each building and calculate
     *      the distance to all reachable empty land cells.
     *
     *  - Distance
     *     Accumulation: We maintain a totalDistance matrix
     *     to store the sum of distances from all buildings
     *     to each empty cell.
     *
     *  - Reachability:
     *     We maintain a reachCount matrix to track how many
     *     buildings can reach a specific empty cell.
     *     An empty cell is only valid if its reachCount equals
     *     the total number of buildings.
     *
     *  - Optimization (Space & Time):
     *    To avoid resetting a visited array for every BFS,
     *    we can use the grid itself or a mark variable to identify
     *    which empty cells were reachable by the previous building.
     *
     */
    public int shortestDistance_0_1(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return -1;

        int m = grid.length;
        int n = grid[0].length;

        int[][] totalDistance = new int[m][n];
        int[][] reachCount = new int[m][n];
        int buildingCount = 0;

        // Count total buildings and find their coordinates
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    buildingCount++;
                    if (!bfs(grid, i, j, totalDistance, reachCount, buildingCount)) {
                        // If a building can't reach all other buildings' areas (not strictly
                        // required by prompt but helps prune), we might return -1 early.
                        // Standard practice: continue BFS to update counts.
                    }
                }
            }
        }

        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Land must be reachable by all buildings and be an empty land (0)
                if (grid[i][j] == 0 && reachCount[i][j] == buildingCount) {
                    minDistance = Math.min(minDistance, totalDistance[i][j]);
                }
            }
        }

        return minDistance == Integer.MAX_VALUE ? -1 : minDistance;
    }

    private boolean bfs(int[][] grid, int row, int col, int[][] totalDistance, int[][] reachCount, int buildingCount) {
        int m = grid.length;
        int n = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{row, col});

        boolean[][] visited = new boolean[m][n];
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        int distance = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            distance++;
            for (int i = 0; i < size; i++) {
                int[] curr = queue.poll();

                for (int[] dir : dirs) {
                    int r = curr[0] + dir[0];
                    int c = curr[1] + dir[1];

                    // Only traverse through empty land (0)
                    if (r >= 0 && r < m && c >= 0 && c < n && !visited[r][c] && grid[r][c] == 0) {
                        visited[r][c] = true;
                        totalDistance[r][c] += distance;
                        reachCount[r][c]++;
                        queue.offer(new int[]{r, c});
                    }
                }
            }
        }

        // Optimization check: In some versions, you verify if this BFS reached any land at all.
        return true;
    }

    // V0-2
    // IDEA: multi-source BFS  + count, dist matrix (GPT)
    // TODO: validate
    /**  IDEA:
     *
     * 🧠 How It Works
     *
     * 1. Multi-source BFS from each building:
     * 	•	For every building (1) run BFS
     * 	•	Accumulate:
     * 	•	dist[r][c] — total distance from all buildings
     * 	•	reachCount[r][c] — how many buildings can reach that empty land
     *
     * 2. After all BFS runs:
     *
     * We check each empty cell (grid[r][c] == 0):
     * 	•	If reachCount[r][c] == buildingCount, then all buildings can reach here.
     * 	•	Pick the minimum dist[r][c].
     *
     * 3. If no such cell → return -1.
     *
     *
     */
    public int shortestDistance_0_2(int[][] grid) {
        int m = grid.length;
        if (m == 0) return -1;
        int n = grid[0].length;

        int[][] dist = new int[m][n];      // sum of distances from all buildings
        int[][] reachCount = new int[m][n]; // how many buildings can reach each cell

        int buildingCount = 0;

        // BFS from each building
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    buildingCount++;
                    bfs(grid, i, j, dist, reachCount);
                }
            }
        }

        int result = Integer.MAX_VALUE;

        // find minimum distance where reachable by all buildings
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0 && reachCount[i][j] == buildingCount) {
                    result = Math.min(result, dist[i][j]);
                }
            }
        }

        return result == Integer.MAX_VALUE ? -1 : result;
    }

    /**  NOTE !!!
     *
     *  Question: Why can we use the same `dist, reachCount`
     *            track all distance, reached count from all different buildings ?
     *            isn't the value being `overwritten` when another cell 1 running BFS ?
     *
     *
     *  ->
     * That is an excellent question! Understanding how the accumulation works is the key to this problem. The values are **not overwritten**; instead, they are **summed up** or **incremented**.
     *
     * Think of the `totalDistance` and `reachCount` matrices as "global scoreboards" that every building contributes to.
     *
     * ### 1. The `totalDistance` Matrix (Accumulation)
     *
     *   - When Building A performs its BFS, it finds every reachable empty land cell `(r, c)`
     *     and calculates the distance. It then **adds** that distance to `totalDistance[r, c]`.
     *
     *   - When Building B starts its BFS, it does the same.
     *     If it reaches the same cell `(r, c)`, it **adds its own
     *     distance** to the value already stored there.
     *
     * * **Building A BFS:** finds `(r, c)` at distance 3. `totalDistance[r, c]` becomes `0 + 3 = 3`.
     * * **Building B BFS:** finds `(r, c)` at distance 5. `totalDistance[r, c]` becomes `3 + 5 = 8`.
     *
     * By the time all buildings have finished their BFS, `totalDistance[r, c]`
     * contains the sum of distances from **all** buildings to that specific piece of land.
     *
     *
     *
     * ### 2. The `reachCount` Matrix (Validation)
     *
     * This matrix tracks how many buildings can "see" a specific cell.
     * Each time a building's BFS reaches an empty cell, it performs `reachCount[r][c]++`.
     *
     * If you have 3 buildings in total, and at the end of the process `reachCount[r][c]`
     * is only `2`, it means one building was blocked by walls or other buildings and
     * could never reach that spot. We use this to filter out invalid landing spots.
     *
     * ### 3. Why the values don't get messed up
     *
     * The only thing that gets "overwritten" or reset is the `visited` array
     * (or the `visited` state). Each building needs its own fresh `visited`
     * check to ensure it calculates the shortest path from *its* specific
     * starting position.
     *
     * However, `totalDistance` and `reachCount` must persist throughout the
     * entire execution because their goal is to provide the **aggregate data**
     * for all buildings.
     *
     * ---
     *
     * **Summary Table of Matrix Roles**
     *
     * | Matrix | Action during BFS | Final Purpose |
     * | --- | --- | --- |
     * | `visited` | Reset for **every** building | Prevents cycles and ensures shortest path for current BFS. |
     * | `reachCount` | **Incremented** (+1) | Ensures the land is reachable by **all** buildings. |
     * | `totalDistance` | **Summed** (+ dist) | Provides the total travel cost for that specific land. |
     *
     */
    private void bfs(int[][] grid, int sr, int sc, int[][] dist, int[][] reachCount) {
        int m = grid.length;
        int n = grid[0].length;

        /** NOTE !!
         *
         *  we `fresh` (init a new one) visited in every BFS call
         */
        boolean[][] visited = new boolean[m][n];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{sr, sc});
        visited[sr][sc] = true;

        int steps = 0;

        int[][] moves = {{1,0},{-1,0},{0,1},{0,-1}};

        while (!queue.isEmpty()) {
            int size = queue.size();
            steps++;
            for (int k = 0; k < size; k++) {
                int[] cur = queue.poll();
                int r = cur[0], c = cur[1];

                for (int[] mve : moves) {
                    int nr = r + mve[0];
                    int nc = c + mve[1];

                    if (nr >= 0 && nr < m && nc >= 0 && nc < n
                            && !visited[nr][nc] && grid[nr][nc] == 0) {

                        visited[nr][nc] = true;
                        dist[nr][nc] += steps;
                        reachCount[nr][nc]++;
                        queue.add(new int[]{nr, nc});
                    }
                }
            }
        }
    }


    // V1
    // IDEA: BFS
    // https://leetcode.ca/2016-10-12-317-Shortest-Distance-from-All-Buildings/
    // TODO: validate
    public int shortestDistance_1(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        Deque<int[]> q = new LinkedList<>();
        int total = 0;
        int[][] cnt = new int[m][n];
        int[][] dist = new int[m][n];
        int[] dirs = {-1, 0, 1, 0, -1};
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 1) {
                    ++total;
                    q.offer(new int[] {i, j});
                    int d = 0;
                    boolean[][] vis = new boolean[m][n];
                    while (!q.isEmpty()) {
                        ++d;
                        for (int k = q.size(); k > 0; --k) {
                            int[] p = q.poll();
                            for (int l = 0; l < 4; ++l) {
                                int x = p[0] + dirs[l];
                                int y = p[1] + dirs[l + 1];
                                if (x >= 0 && x < m && y >= 0 && y < n && grid[x][y] == 0
                                        && !vis[x][y]) {
                                    ++cnt[x][y];
                                    dist[x][y] += d;
                                    q.offer(new int[] {x, y});
                                    vis[x][y] = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 0 && cnt[i][j] == total) {
                    ans = Math.min(ans, dist[i][j]);
                }
            }
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }



    // V2

    // V3


}
