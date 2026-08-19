package LeetCodeJava.Array;

// https://leetcode.com/problems/cyclically-rotating-a-grid/

import java.util.ArrayList;
import java.util.List;

/**
 *  1914. Cyclically Rotating a Grid
 *  Medium
 *
 *  You are given an m x n integer matrix grid, where m and n are both even
 *  integers, and an integer k.
 *
 *  The matrix is composed of several layers. A cyclic rotation of the matrix is
 *  done by cyclically rotating each layer in the matrix. To cyclically rotate a
 *  layer once, each element in the layer will take the place of the adjacent
 *  element in the counter-clockwise direction.
 *
 *  Return the matrix after applying k cyclic rotations to it.
 *
 *
 *  Example 1:
 *
 *  Input: grid = [[40,10],[30,20]], k = 1
 *  Output: [[10,20],[40,30]]
 *
 *  Example 2:
 *
 *  Input: grid = [[1,2,3,4],[5,6,7,8],[9,10,11,12],[13,14,15,16]], k = 2
 *  Output: [[3,4,8,12],[2,11,10,16],[1,7,6,15],[5,9,13,14]]
 *
 *
 *  Constraints:
 *
 *  m == grid.length
 *  n == grid[i].length
 *  2 <= m, n <= 50
 *  Both m and n are even integers.
 *  1 <= grid[i][j] <= 5000
 *  1 <= k <= 10^9
 */
public class CyclicallyRotatingAGrid {

    // V0
    // IDEA: flatten each layer into a cyclic list of coordinates (in the counter-clockwise
    //       order), then shift every value by k % layerLength positions along that list
    /**
     * time = O(m * n)
     * space = O(m * n)
     */
    public int[][] rotateGrid(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] res = new int[m][n];

        int layers = Math.min(m, n) / 2;
        for (int layer = 0; layer < layers; layer++) {

            List<int[]> ring = new ArrayList<>();
            // left column, going down
            for (int r = layer; r <= m - 1 - layer; r++) {
                ring.add(new int[] { r, layer });
            }
            // bottom row, going right
            for (int c = layer + 1; c <= n - 1 - layer; c++) {
                ring.add(new int[] { m - 1 - layer, c });
            }
            // right column, going up
            for (int r = m - 2 - layer; r >= layer; r--) {
                ring.add(new int[] { r, n - 1 - layer });
            }
            // top row, going left
            for (int c = n - 2 - layer; c >= layer + 1; c--) {
                ring.add(new int[] { layer, c });
            }

            int len = ring.size();
            int shift = k % len;
            for (int i = 0; i < len; i++) {
                int[] from = ring.get(i);
                int[] to = ring.get((i + shift) % len);
                res[to[0]][to[1]] = grid[from[0]][from[1]];
            }
        }
        return res;
    }
}
