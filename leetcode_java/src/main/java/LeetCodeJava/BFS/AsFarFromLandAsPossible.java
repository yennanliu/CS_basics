package LeetCodeJava.BFS;

// https://leetcode.com/problems/as-far-from-land-as-possible/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 1162. As Far from Land as Possible
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an n x n grid containing only values 0 and 1, where 0 represents water and 1 represents land, find a water cell such that its distance to the nearest land cell is maximized, and return the distance. If no land or water exists in the grid, return -1.
 *
 * The distance used in this problem is the Manhattan distance: the distance between two cells (x0, y0) and (x1, y1) is |x0 - x1| + |y0 - y1|.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[1,0,1],[0,0,0],[1,0,1]]
 * Output: 2
 * Explanation: The cell (1, 1) is as far as possible from all the land with distance 2.
 * Example 2:
 *
 *
 * Input: grid = [[1,0,0],[0,0,0],[0,0,0]]
 * Output: 4
 * Explanation: The cell (2, 2) is as far as possible from all the land with distance 4.
 *
 *
 * Constraints:
 *
 * n == grid.length
 * n == grid[i].length
 * 1 <= n <= 100
 * grid[i][j] is 0 or 1
 *
 *
 */
public class AsFarFromLandAsPossible {

    // V0
//    public int maxDistance(int[][] grid) {
//
//    }

    // V0-1
    // IDEA: Multi-source BFS (fixed by gemini)
    /**
     * Logic:
     * 1. Add all land cells (1) to the queue as starting points.
     * 2. Traverse outwards into water cells (0).
     * 3. The distance of the last water cell reached is the maximum distance.
     */
    public int maxDistance_0_1(int[][] grid) {
        int n = grid.length;
        Queue<int[]> q = new LinkedList<>();

        /** NOTE !!!
         *
         *   we add LAND to queue (cell val = 1),
         *   instead of adding water
         */
        // 1. Add all land cells to the queue
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 1) {
                    q.offer(new int[] { r, c });
                }
            }
        }

        /** NOTE !!!
         *
         *   Edge Case
         */
        // Edge Case: If no water or no land, return -1
        if (q.isEmpty() || q.size() == n * n) {
            return -1;
        }

        int maxDist = -1;
        int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        // 2. Multi-source BFS
        while (!q.isEmpty()) {
            int size = q.size();
            maxDist++; // Each level of BFS represents 1 unit of distance

            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();
                int r = cur[0];
                int c = cur[1];

                for (int[] m : moves) {
                    int nr = r + m[0];
                    int nc = c + m[1];

                    /** NOTE !!!
                     *
                     *  1. ONLY enqueue if visit `water` cell (grid[nr][nc] == 0)
                     *
                     *  2. mark visited cell as 1 (land, visited)
                     *     -> so we DON'T revisit the visited cell
                     *
                     */
                    // If neighbor is water, mark as visited (turn to land) and enqueue
                    if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] == 0) {
                        grid[nr][nc] = 1; // Mark as visited
                        q.offer(new int[] { nr, nc });
                    }
                }
            }
        }

        return maxDist;
    }

    // V0-2
    // IDEA: Multi-source BFS (fixed by gpt)
    public int maxDistance_0_2(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;

        Queue<int[]> q = new LinkedList<>();
        boolean hasWater = false;

        // Push all land cells into queue
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < m; x++) {
                if (grid[y][x] == 1) {
                    q.offer(new int[] { y, x });
                } else {
                    hasWater = true;
                }
            }
        }

        // Edge cases
        if (q.isEmpty() || !hasWater)
            return -1;

        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        int dist = -1;

        // BFS level by level
        while (!q.isEmpty()) {
            int size = q.size();
            dist++;

            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();
                int y = cur[0];
                int x = cur[1];

                for (int[] d : dirs) {
                    int ny = y + d[0];
                    int nx = x + d[1];

                    if (ny >= 0 && ny < n && nx >= 0 && nx < m && grid[ny][nx] == 0) {
                        grid[ny][nx] = 1; // mark visited
                        q.offer(new int[] { ny, nx });
                    }
                }
            }
        }

        return dist;
    }


    // V1
    // IDEA: BFS
    // https://leetcode.ca/2019-02-04-1162-As-Far-from-Land-as-Possible/
    public int maxDistance_1(int[][] grid) {
        int n = grid.length;
        Deque<int[]> q = new ArrayDeque<>();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == 1) {
                    q.offer(new int[] {i, j});
                }
            }
        }
        int ans = -1;
        if (q.isEmpty() || q.size() == n * n) {
            return ans;
        }
        int[] dirs = {-1, 0, 1, 0, -1};
        while (!q.isEmpty()) {
            for (int i = q.size(); i > 0; --i) {
                int[] p = q.poll();
                for (int k = 0; k < 4; ++k) {
                    int x = p[0] + dirs[k], y = p[1] + dirs[k + 1];
                    if (x >= 0 && x < n && y >= 0 && y < n && grid[x][y] == 0) {
                        grid[x][y] = 1;
                        q.offer(new int[] {x, y});
                    }
                }
            }
            ++ans;
        }
        return ans;
    }


    // V2
    // IDEA: BFS
    // https://leetcode.com/problems/as-far-from-land-as-possible/solutions/3166138/day-41-c-bfs-easiest-beginner-friendly-a-z1ms/
    public int maxDistance_2(int[][] grid) {
        int n = grid.length;
        Queue<int[]> q = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1)
                    q.offer(new int[] { i, j });
            }
        }
        if (q.isEmpty() || q.size() == n * n)
            return -1;
        int res = -1;
        int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        while (!q.isEmpty()) {
            int size = q.size();
            res++;
            while (size-- > 0) {
                int[] cell = q.poll();
                int x = cell[0], y = cell[1];
                for (int[] dir : dirs) {
                    int i = x + dir[0], j = y + dir[1];
                    if (i >= 0 && i < n && j >= 0 && j < n && grid[i][j] == 0) {
                        grid[i][j] = 1;
                        q.offer(new int[] { i, j });
                    }
                }
            }
        }
        return res;
    }


    // V3
    // IDEA: BFS
    // https://leetcode.com/problems/as-far-from-land-as-possible/solutions/3166441/fast-java-solution-by-coding_menance-cwtf/
    public int maxDistance_3(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        Queue<int[]> q = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    visited[i][j] = true;
                    q.offer(new int[] { i, j });
                }
            }
        }
        int[][] dirs = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
        int result = -1;
        while (!q.isEmpty()) {
            int size = q.size();
            while (size-- > 0) {
                int[] cur = q.poll();
                result = Math.max(result, grid[cur[0]][cur[1]] - 1);
                for (int[] dir : dirs) {
                    int x = cur[0] + dir[0], y = cur[1] + dir[1];
                    if (x >= 0 && x < m && y >= 0 && y < n && !visited[x][y]) {
                        visited[x][y] = true;
                        grid[x][y] = grid[cur[0]][cur[1]] + 1;
                        q.offer(new int[] { x, y });
                    }
                }
            }
        }
        return result == 0 ? -1 : result;
    }



}
