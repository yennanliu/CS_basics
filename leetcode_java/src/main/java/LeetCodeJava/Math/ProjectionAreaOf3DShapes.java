package LeetCodeJava.Math;

// https://leetcode.com/problems/projection-area-of-3d-shapes/

/**
 *  883. Projection Area of 3D Shapes
 *  Easy
 *
 *  You are given an n x n grid where we place some 1 x 1 x 1 cubes that are
 *  axis-aligned with the x, y, and z axes. Each value v = grid[i][j]
 *  represents a tower of v cubes placed on top of the cell (i, j).
 *
 *  We view the projection of these cubes onto the xy, yz, and zx planes.
 *  A projection is the shadow that the cubes cast onto the plane.
 *
 *  Return the total area of all three projections.
 *
 *  Example 1:
 *   Input: grid = [[1,2],[3,4]]
 *   Output: 17
 *
 *  Example 2:
 *   Input: grid = [[2]]
 *   Output: 5
 *
 *  Example 3:
 *   Input: grid = [[1,0],[0,2]]
 *   Output: 8
 *
 *  Constraints:
 *   - n == grid.length == grid[i].length
 *   - 1 <= n <= 50
 *   - 0 <= grid[i][j] <= 50
 */
public class ProjectionAreaOf3DShapes {

    // V0
    // IDEA: top view = count of non-zero cells; front view = sum of row maxima;
    //       side view = sum of column maxima.
    /**
     * time = O(n^2)
     * space = O(1)
     */
    public int projectionArea(int[][] grid) {
        int n = grid.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            int maxRow = 0;
            int maxCol = 0;
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0) {
                    res += 1; // top (xy) projection
                }
                maxRow = Math.max(maxRow, grid[i][j]);
                maxCol = Math.max(maxCol, grid[j][i]);
            }
            res += maxRow + maxCol;
        }
        return res;
    }
}
