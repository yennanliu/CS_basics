package LeetCodeJava.BFS;

// https://leetcode.com/problems/count-islands-with-total-value-divisible-by-k/description/

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 3619. Count Islands With Total Value Divisible by K
 * Medium
 *
 * You are given an m x n matrix grid and a positive integer k. An island is a
 * group of positive integers (representing land) that are 4-directionally
 * connected (horizontally or vertically).
 *
 * The total value of an island is the sum of the values of all cells in the
 * island.
 *
 * Return the number of islands with a total value divisible by k.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,2,1,0,0],[0,5,0,0,5],[0,0,1,0,0],[0,1,4,7,0],[0,2,0,0,8]],
 * k = 5
 * Output: 2
 * Explanation:
 * The grid contains four islands. The islands highlighted in blue have a total
 * value that is divisible by 5, while the islands highlighted in red do not.
 *
 * Example 2:
 *
 * Input: grid = [[3,0,3,0], [0,3,0,3], [3,0,3,0]], k = 3
 * Output: 6
 * Explanation:
 * The grid contains six islands, each with a total value that is divisible by 3.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 1000
 * 1 <= m * n <= 10^5
 * 0 <= grid[i][j] <= 10^6
 * 1 <= k <= 10^6
 *
 */
public class CountIslandsWithTotalValueDivisibleByK {

    // V0
    // IDEA: BFS FLOOD FILL, SUMMING EACH ISLAND WHILE IT IS CONSUMED
    //   an island is a 4-connected component of positive cells; one BFS per
    //   unvisited land cell visits it exactly once and adds up its values.
    //   Mark a cell visited when it is ENQUEUED so it is never pushed twice.
    //   Totals reach 10^5 * 10^6, so they are long.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int countIslands(int[][] grid, int k) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        int islands = 0;

        for (int startRow = 0; startRow < rows; startRow++) {
            for (int startCol = 0; startCol < cols; startCol++) {
                if (grid[startRow][startCol] <= 0 || visited[startRow][startCol]) {
                    continue;
                }
                // flood the island that starts here and total its values
                visited[startRow][startCol] = true;
                Queue<int[]> queue = new ArrayDeque<>();
                queue.offer(new int[] { startRow, startCol });
                long total = 0;
                while (!queue.isEmpty()) {
                    int[] cur = queue.poll();
                    total += grid[cur[0]][cur[1]];
                    for (int[] dir : directions) {
                        int nr = cur[0] + dir[0];
                        int nc = cur[1] + dir[1];
                        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                            continue;
                        }
                        if (grid[nr][nc] > 0 && !visited[nr][nc]) {
                            visited[nr][nc] = true;
                            queue.offer(new int[] { nr, nc });
                        }
                    }
                }
                if (total % k == 0) {
                    islands++;
                }
            }
        }
        return islands;
    }
}
