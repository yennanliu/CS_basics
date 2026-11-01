package LeetCodeJava.Math;

// https://leetcode.com/problems/right-triangles/description/

/**
 * 3128. Right Triangles
 * Medium
 *
 * You are given a 2D boolean matrix grid.
 *
 * Return an integer that is the number of right triangles that can be made with the 3 elements of grid
 * such that all of them have a value of 1.
 *
 * Note:
 *
 * A collection of 3 elements of grid is a right triangle if one of its elements is in the same row with
 * another element and in the same column with the third element. The 3 elements do not have to be next
 * to each other.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1,0],[0,1,1],[0,1,0]]
 * Output: 2
 *
 * Example 2:
 *
 * Input: grid = [[1,0,0,0],[0,1,0,1],[1,0,0,0]]
 * Output: 0
 *
 * Example 3:
 *
 * Input: grid = [[1,0,1],[1,0,0],[1,0,0]]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= grid.length <= 1000
 * 1 <= grid[i].length <= 1000
 * 0 <= grid[i][j] <= 1
 *
 */
public class RightTriangles {

    // V0
    // IDEA: COUNT BY THE RIGHT-ANGLE CORNER — (rowOnes[i] - 1) * (colOnes[j] - 1) PER CELL OF 1
    /**
     * time = O(M * N)
     * space = O(M + N)
     */
    public long numberOfRightTriangles(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // count the ones in every row and every column
        int[] rowOnes = new int[rows];
        int[] colOnes = new int[cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rowOnes[r] += grid[r][c];
                colOnes[c] += grid[r][c];
            }
        }

        // a corner at (r, c) pairs any other 1 in its row with any other 1 in its column
        long triangles = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    triangles += (long) (rowOnes[r] - 1) * (colOnes[c] - 1);
                }
            }
        }
        return triangles;
    }
}
