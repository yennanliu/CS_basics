package LeetCodeJava.BFS;

// https://leetcode.com/problems/maximum-number-of-points-from-grid-queries/description/

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 2503. Maximum Number of Points From Grid Queries
 * Hard
 *
 * You are given an m x n integer matrix grid and an array queries of size k.
 *
 * Find an array answer of size k such that for each integer queries[i] you start in the top left cell of the matrix and repeat the following process:
 *
 * If queries[i] is strictly greater than the value of the current cell that you are in, then you get one point if it is your first time visiting this cell, and you can move to any adjacent cell in all 4 directions: up, down, left, and right.
 * Otherwise, you do not get any points, and you end this process.
 *
 * After the process, answer[i] is the maximum number of points you can get. Note that for each query you are allowed to visit the same cell multiple times.
 *
 * Return the resulting array answer.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,2,3],[2,5,7],[3,5,1]], queries = [5,6,2]
 * Output: [5,8,1]
 * Explanation: The diagrams above show which cells we visit to get points for each query.
 *
 * Example 2:
 *
 * Input: grid = [[5,2,1],[1,1,2]], queries = [3]
 * Output: [0]
 * Explanation: We can not get any points because the value of the top left cell is already greater than or equal to 3.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 2 <= m, n <= 1000
 * 4 <= m * n <= 10^5
 * k == queries.length
 * 1 <= k <= 10^4
 * 1 <= grid[i][j], queries[i] <= 10^6
 *
 */
public class MaximumNumberOfPointsFromGridQueries {

    private static final int[][] DIRECTIONS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    // V0
    // IDEA: OFFLINE QUERIES (SORTED) + BEST-FIRST BFS WITH A MIN HEAP
    //   for one query q the score is the size of the 4-connected region of cells
    //   with value < q containing (0,0). That region only GROWS as q grows, so
    //   answer queries in ascending order and keep expanding one shared frontier.
    //   The frontier is a min heap by cell value: pop while the cheapest
    //   reachable cell is < q, count it, push its neighbours.
    //   Mark a cell visited when PUSHED so it enters the heap once.
    /**
     * time = O(M * N * log(M * N) + K log K)
     * space = O(M * N + K)
     */
    public int[] maxPoints(int[][] grid, int[] queries) {
        int rows = grid.length;
        int cols = grid[0].length;
        int queryCount = queries.length;

        // answer queries in ascending value, remembering each one's original index
        Integer[] order = new Integer[queryCount];
        for (int i = 0; i < queryCount; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(queries[a], queries[b]));

        // heap entries: {value, row, col}, cheapest value first
        PriorityQueue<int[]> frontier = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        boolean[][] visited = new boolean[rows][cols];
        visited[0][0] = true;
        frontier.offer(new int[] { grid[0][0], 0, 0 });

        int[] answer = new int[queryCount];
        int unlocked = 0;
        for (int queryIndex : order) {
            int limit = queries[queryIndex];
            // unlock every reachable cell strictly smaller than this query
            while (!frontier.isEmpty() && frontier.peek()[0] < limit) {
                int[] cell = frontier.poll();
                unlocked++;
                for (int[] dir : DIRECTIONS) {
                    int nr = cell[1] + dir[0];
                    int nc = cell[2] + dir[1];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || visited[nr][nc]) {
                        continue;
                    }
                    visited[nr][nc] = true;
                    frontier.offer(new int[] { grid[nr][nc], nr, nc });
                }
            }
            answer[queryIndex] = unlocked;
        }
        return answer;
    }
}
