package LeetCodeJava.Graph;

// https://leetcode.com/problems/swim-in-rising-water/description/

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 778. Swim in Rising Water
 * Hard
 * Topics
 * Companies
 * Hint
 * You are given an n x n integer matrix grid where each value grid[i][j] represents the elevation at that point (i, j).
 *
 * The rain starts to fall. At time t, the depth of the water everywhere is t. You can swim from a square to another 4-directionally adjacent square if and only if the elevation of both squares individually are at most t. You can swim infinite distances in zero time. Of course, you must stay within the boundaries of the grid during your swim.
 *
 * Return the least time until you can reach the bottom right square (n - 1, n - 1) if you start at the top left square (0, 0).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[0,2],[1,3]]
 * Output: 3
 * Explanation:
 * At time 0, you are in grid location (0, 0).
 * You cannot go anywhere else because 4-directionally adjacent neighbors have a higher elevation than t = 0.
 * You cannot reach point (1, 1) until time 3.
 * When the depth of water is 3, we can swim anywhere inside the grid.
 * Example 2:
 *
 *
 * Input: grid = [[0,1,2,3,4],[24,23,22,21,5],[12,13,14,15,16],[11,17,18,19,20],[10,9,8,7,6]]
 * Output: 16
 * Explanation: The final route is shown.
 * We need to wait until time 16 so that (0, 0) and (4, 4) are connected.
 *
 *
 * Constraints:
 *
 * n == grid.length
 * n == grid[i].length
 * 1 <= n <= 50
 * 0 <= grid[i][j] < n2
 * Each value grid[i][j] is unique.
 *
 *
 */
public class SwimInRisingWater {

    // V0
//    public int swimInWater(int[][] grid) {
//
//    }

    // V0-1
    // IDEA:  Dijkstra's Algorithm (fixed by gpt)
    /**
     *
     *  NOTE !!! we DON'T need a regular queue,
     *
     *   all we need is :  `min PQ + BFS`
     *
     *     (val pop from min PQ)
     */
    /**
     * time = O((M * N) log (M * N))
     * space = O(M * N)
     */
    public int swimInWater_0_1(int[][] grid) {
        int n = grid.length;

    // Min-heap based on elevation
    /**
     *
     * NOTE !!! we use minHeap below
     * minHeap : [cost, x, y]
     */
    PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

        // NOTE !!! we use visited to NOT visit same cell again
        boolean[][] visited = new boolean[n][n];

        // Start from (0, 0)
        minHeap.offer(new int[] { grid[0][0], 0, 0 });
        visited[0][0] = true;

        int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        int res = 0;

        while (!minHeap.isEmpty()) {
            /**
             *
             * NOTE !!! we poll cur from `minHeap`
             */
            int[] cur = minHeap.poll();
            int elevation = cur[0], x = cur[1], y = cur[2];

            /**
             *
             * NOTE !!! we track the max of  res or next cost
             */
            res = Math.max(res, elevation); // Track max elevation

            if (x == n - 1 && y == n - 1) {
                return res; // Reached target
            }

            for (int[] d : directions) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (nx >= 0 && ny >= 0 && nx < n && ny < n && !visited[ny][nx]) {

                    // NOTE !!! we update visited before adding it to PQ
                    visited[ny][nx] = true;
                    minHeap.offer(new int[] { grid[ny][nx], nx, ny });
                }
            }
        }

        return -1; // Unreachable (shouldn't happen with valid input)
    }

    // V1-1
    // https://neetcode.io/problems/swim-in-rising-water
    // IDEA: BRUTE FORCE
    /**
     * time = O((M * N) * 4^(M * N))
     * space = O(M * N)
     */
    public int swimInWater_1_1(int[][] grid) {
        int n = grid.length;
        boolean[][] visit = new boolean[n][n];

        return dfs(grid, visit, 0, 0, 0);
    }

    /**
     * time = O(4^(M * N))
     * space = O(M * N)
     */
    private int dfs(int[][] grid, boolean[][] visit,
                    int r, int c, int t) {
        int n = grid.length;
        if (r < 0 || c < 0 || r >= n || c >= n || visit[r][c]) {
            return 1000000;
        }
        if (r == n - 1 && c == n - 1) {
            return Math.max(t, grid[r][c]);
        }
        visit[r][c] = true;
        t = Math.max(t, grid[r][c]);
        int res = Math.min(Math.min(dfs(grid, visit, r + 1, c, t),
                        dfs(grid, visit, r - 1, c, t)),
                Math.min(dfs(grid, visit, r, c + 1, t),
                        dfs(grid, visit, r, c - 1, t)));
        visit[r][c] = false;
        return res;
    }

    // V1-2
    // https://neetcode.io/problems/swim-in-rising-water
    // IDEA: DFS
    /**
     * time = O((M * N)^2)
     * space = O(M * N)
     */
    public int swimInWater_1_2(int[][] grid) {
        int n = grid.length;
        boolean[][] visit = new boolean[n][n];
        int minH = grid[0][0], maxH = grid[0][0];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                maxH = Math.max(maxH, grid[row][col]);
                minH = Math.min(minH, grid[row][col]);
            }
        }

        for (int t = minH; t < maxH; t++) {
            if (dfs_1_1(grid, visit, 0, 0, t)) {
                return t;
            }
            for (int r = 0; r < n; r++) {
                Arrays.fill(visit[r], false);
            }
        }
        return maxH;
    }

    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    private boolean dfs_1_1(int[][] grid, boolean[][] visit, int r, int c, int t) {
        if (r < 0 || c < 0 || r >= grid.length ||
                c >= grid.length || visit[r][c] || grid[r][c] > t) {
            return false;
        }
        if (r == grid.length - 1 && c == grid.length - 1) {
            return true;
        }
        visit[r][c] = true;
        return dfs_1_1(grid, visit, r + 1, c, t) ||
                dfs_1_1(grid, visit, r - 1, c, t) ||
                dfs_1_1(grid, visit, r, c + 1, t) ||
                dfs_1_1(grid, visit, r, c - 1, t);
    }


    // V1-3
    // https://neetcode.io/problems/swim-in-rising-water
    // IDEA: Binary Search + DFS
    /**
     * time = O((M * N) log (M * N))
     * space = O(M * N)
     */
    public int swimInWater_1_3(int[][] grid) {
        int n = grid.length;
        boolean[][] visit = new boolean[n][n];
        int minH = grid[0][0], maxH = grid[0][0];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                maxH = Math.max(maxH, grid[row][col]);
                minH = Math.min(minH, grid[row][col]);
            }
        }

        int l = minH, r = maxH;
        while (l < r) {
            int m = (l + r) >> 1;
            if (dfs_1_3(grid, visit, 0, 0, m)) {
                r = m;
            } else {
                l = m + 1;
            }
            for (int row = 0; row < n; row++) {
                Arrays.fill(visit[row], false);
            }
        }
        return r;
    }

    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    private boolean dfs_1_3(int[][] grid, boolean[][] visit, int r, int c, int t) {
        if (r < 0 || c < 0 || r >= grid.length ||
                c >= grid.length || visit[r][c] || grid[r][c] > t) {
            return false;
        }
        if (r == grid.length - 1 && c == grid.length - 1) {
            return true;
        }
        visit[r][c] = true;
        return dfs_1_3(grid, visit, r + 1, c, t) ||
                dfs_1_3(grid, visit, r - 1, c, t) ||
                dfs_1_3(grid, visit, r, c + 1, t) ||
                dfs_1_3(grid, visit, r, c - 1, t);
    }


    // V1-4
    // https://neetcode.io/problems/swim-in-rising-water
    // IDEA:  Dijkstra's Algorithm
    /**
     * time = O((M * N) log (M * N))
     * space = O(M * N)
     */
    public int swimInWater_1_4(int[][] grid) {
        int N = grid.length;
        boolean[][] visit = new boolean[N][N];
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(a -> a[0])
        );
        int[][] directions = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}
        };

        minHeap.offer(new int[]{grid[0][0], 0, 0});
        visit[0][0] = true;

        while (!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int t = curr[0], r = curr[1], c = curr[2];
            if (r == N - 1 && c == N - 1) {
                return t;
            }
            for (int[] dir : directions) {
                int neiR = r + dir[0], neiC = c + dir[1];
                if (neiR >= 0 && neiC >= 0 && neiR < N &&
                        neiC < N && !visit[neiR][neiC]) {
                    visit[neiR][neiC] = true;
                    minHeap.offer(new int[]{
                            // NOTE !!! we need to add the `max` value to min heap
                            // since
                            // 1) res could be bigger
                            //  or
                            // 2) grid[neiR][neiC] is bigger
                            Math.max(t, grid[neiR][neiC]),
                            neiR, neiC
                    });
                }
            }
        }
        return N * N;
    }


    // V2

}
