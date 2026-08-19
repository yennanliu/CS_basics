package LeetCodeJava.Greedy;

// https://leetcode.com/problems/path-with-maximum-minimum-value/

import java.util.*;

/**
 *  1102. Path With Maximum Minimum Value
 *  Medium
 *
 *  Given an m x n integer matrix grid, return the maximum score of a path
 *  starting at (0, 0) and ending at (m - 1, n - 1) moving in the 4 cardinal
 *  directions.
 *
 *  The score of a path is the minimum value in that path.
 *  For example, the score of the path 8 -> 4 -> 5 -> 9 is 4.
 *
 *  Example 1:
 *  Input: grid = [[5,4,5],[1,2,6],[7,4,6]]
 *  Output: 4
 *
 *  Example 2:
 *  Input: grid = [[2,2,1,2,2,2],[1,2,2,2,1,2]]
 *  Output: 2
 *
 *  Example 3:
 *  Input: grid = [[3,4,6,3,4],[0,2,1,1,7],[8,8,3,2,7],[3,2,4,9,8],[4,1,2,0,0],[4,6,5,4,3]]
 *  Output: 3
 *
 *  Constraints:
 *   - m == grid.length, n == grid[i].length
 *   - 1 <= m, n <= 100
 *   - 0 <= grid[i][j] <= 10^9
 */
public class PathWithMaximumMinimumValue {

    // V0
    // IDEA: MAX-HEAP (Dijkstra style) -> always expand the largest reachable cell,
    //       the answer is the smallest cell we are forced to step on.
    /**
     * time = O(m * n * log(m * n))
     * space = O(m * n)
     */
    public int maximumMinimumPath(int[][] grid) {

        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }

        int m = grid.length;
        int n = grid[0].length;
        int[][] dirs = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        boolean[][] visited = new boolean[m][n];

        // element : {value, x, y}, max heap on value
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return Integer.compare(b[0], a[0]);
            }
        });

        pq.add(new int[]{grid[0][0], 0, 0});
        visited[0][0] = true;

        int res = grid[0][0];

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            res = Math.min(res, cur[0]);

            if (cur[1] == m - 1 && cur[2] == n - 1) {
                return res;
            }

            for (int[] d : dirs) {
                int nx = cur[1] + d[0];
                int ny = cur[2] + d[1];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || visited[nx][ny]) {
                    continue;
                }
                visited[nx][ny] = true;
                pq.add(new int[]{grid[nx][ny], nx, ny});
            }
        }

        return -1;
    }

    // V1
    // IDEA: BINARY SEARCH on the answer + BFS reachability check
    //       (only walk over cells with value >= mid)
    /**
     * time = O(m * n * log(maxVal))
     * space = O(m * n)
     */
    public int maximumMinimumPath_1(int[][] grid) {

        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }

        int lo = 0;
        int hi = Math.min(grid[0][0], grid[grid.length - 1][grid[0].length - 1]);

        while (lo < hi) {
            int mid = lo + (hi - lo + 1) / 2;
            if (canReach(grid, mid)) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }

        return lo;
    }

    private boolean canReach(int[][] grid, int limit) {

        int m = grid.length;
        int n = grid[0].length;

        if (grid[0][0] < limit || grid[m - 1][n - 1] < limit) {
            return false;
        }

        int[][] dirs = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        boolean[][] visited = new boolean[m][n];

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{0, 0});
        visited[0][0] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            if (cur[0] == m - 1 && cur[1] == n - 1) {
                return true;
            }
            for (int[] d : dirs) {
                int nx = cur[0] + d[0];
                int ny = cur[1] + d[1];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n) {
                    continue;
                }
                if (visited[nx][ny] || grid[nx][ny] < limit) {
                    continue;
                }
                visited[nx][ny] = true;
                q.add(new int[]{nx, ny});
            }
        }

        return false;
    }
}
