package LeetCodeJava.Graph;

// https://leetcode.com/problems/surface-area-of-3d-shapes/description/
/**
 *  892. Surface Area of 3D Shapes
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * You are given an n x n grid where you have placed some 1 x 1 x 1 cubes. Each value v = grid[i][j] represents a tower of v cubes placed on top of cell (i, j).
 *
 * After placing these cubes, you have decided to glue any directly adjacent cubes to each other, forming several irregular 3D shapes.
 *
 * Return the total surface area of the resulting shapes.
 *
 * Note: The bottom face of each shape counts toward its surface area.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[1,2],[3,4]]
 * Output: 34
 * Example 2:
 *
 *
 * Input: grid = [[1,1,1],[1,0,1],[1,1,1]]
 * Output: 32
 * Example 3:
 *
 *
 * Input: grid = [[2,2,2],[2,1,2],[2,2,2]]
 * Output: 46
 *
 *
 * Constraints:
 *
 * n == grid.length == grid[i].length
 * 1 <= n <= 50
 * 0 <= grid[i][j] <= 50
 *
 *
 */
public class SurfaceAreaOf3DShapes {

    // V0
//    public int surfaceArea(int[][] grid) {
//
//    }

    // V1
    // IDEA: SQUARE BY SQUARE
    // https://leetcode.com/problems/surface-area-of-3d-shapes/editorial/
    public int surfaceArea_1(int[][] grid) {
        int[] dr = new int[] { 0, 1, 0, -1 };
        int[] dc = new int[] { 1, 0, -1, 0 };

        int N = grid.length;
        int ans = 0;

        for (int r = 0; r < N; ++r)
            for (int c = 0; c < N; ++c)
                if (grid[r][c] > 0) {
                    ans += 2;
                    for (int k = 0; k < 4; ++k) {
                        int nr = r + dr[k];
                        int nc = c + dc[k];
                        int nv = 0;
                        if (0 <= nr && nr < N && 0 <= nc && nc < N)
                            nv = grid[nr][nc];

                        ans += Math.max(grid[r][c] - nv, 0);
                    }
                }

        return ans;
    }


    // V2
    // https://leetcode.com/problems/surface-area-of-3d-shapes/solutions/5140012/simple-java-code-1-ms-beats-100-by-arobh-clmt/
    public int surfaceArea_2(int[][] grid) {
        int result = 0;
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[0].length; j++)
                result += getArea(grid, i, j);
        return result;
    }

    private int getArea(int[][] grid, int i, int j) {
        int towerHeight = grid[i][j];
        if (towerHeight == 0)
            return 0;
        int area = 2 + (towerHeight * 4);
        if (i > 0)
            area -= Math.min(towerHeight, grid[i - 1][j]);
        if (i < grid.length - 1)
            area -= Math.min(towerHeight, grid[i + 1][j]);
        if (j > 0)
            area -= Math.min(towerHeight, grid[i][j - 1]);
        if (j < grid[0].length - 1)
            area -= Math.min(towerHeight, grid[i][j + 1]);
        return area;
    }



    // V3




}
