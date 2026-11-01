package LeetCodeJava.Array;

// https://leetcode.com/problems/shift-2d-grid/description/

import java.util.*;

/**
 * 1260. Shift 2D Grid
 * Easy
 *
 * Given a 2D grid of size m x n and an integer k. You need to shift the grid k times.
 *
 * In one shift operation:
 *
 * Element at grid[i][j] moves to grid[i][j + 1].
 * Element at grid[i][n - 1] moves to grid[i + 1][0].
 * Element at grid[m - 1][n - 1] moves to grid[0][0].
 *
 * Return the 2D grid after applying shift operation k times.
 *
 * Example 1:
 *
 * Input: grid = [[1,2,3],[4,5,6],[7,8,9]], k = 1
 * Output: [[9,1,2],[3,4,5],[6,7,8]]
 *
 * Example 2:
 *
 * Input: grid = [[3,8,1,9],[19,7,2,5],[4,6,11,10],[12,0,21,13]], k = 4
 * Output: [[12,0,21,13],[3,8,1,9],[19,7,2,5],[4,6,11,10]]
 *
 * Example 3:
 *
 * Input: grid = [[1,2,3],[4,5,6],[7,8,9]], k = 9
 * Output: [[1,2,3],[4,5,6],[7,8,9]]
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m <= 50
 * 1 <= n <= 50
 * -1000 <= grid[i][j] <= 1000
 * 0 <= k <= 100
 *
 */
public class Shift2DGrid {

    // V0
    // IDEA: FLATTEN THE GRID + INDEX MATH
    //   the grid is a 1D array of size m * n rotated right by k, so the value
    //   landing at flat index idx comes from flat index (idx - k) mod (m * n)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int total = m * n;
        // shifting total times is a full cycle
        int shift = k % total;

        List<List<Integer>> result = new ArrayList<>();
        for (int r = 0; r < m; r++) {
            List<Integer> row = new ArrayList<>();
            for (int c = 0; c < n; c++) {
                int index = r * n + c;
                // + total keeps the source index non-negative
                int source = (index - shift + total) % total;
                row.add(grid[source / n][source % n]);
            }
            result.add(row);
        }
        return result;
    }
}
