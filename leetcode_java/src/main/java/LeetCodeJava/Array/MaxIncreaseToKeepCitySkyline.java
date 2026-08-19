package LeetCodeJava.Array;

// https://leetcode.com/problems/max-increase-to-keep-city-skyline/

/**
 *  807. Max Increase to Keep City Skyline
 *  Medium
 *
 *  There is a city composed of n x n blocks, where each block contains a single
 *  building shaped like a vertical square prism. You are given a 0-indexed n x n
 *  integer matrix grid where grid[r][c] represents the height of the building
 *  located in the block at row r and column c.
 *
 *  A city's skyline is the outer contour formed by all the building when viewing
 *  the side of the city from a distance. The skyline from each cardinal direction
 *  north, east, south, and west may be different.
 *
 *  We are allowed to increase the height of any number of buildings by any amount
 *  (the amount can be different per building). The height of a 0-height building
 *  can also be increased. However, increasing the height of a building must not
 *  affect the city's skyline from any cardinal direction.
 *
 *  Return the maximum total sum that the height of the buildings can be increased
 *  by without changing the city's skyline from any cardinal direction.
 *
 *  Example 1:
 *    Input: grid = [[3,0,8,4],[2,4,5,7],[9,2,6,3],[0,3,1,0]]
 *    Output: 35
 *
 *  Example 2:
 *    Input: grid = [[0,0,0],[0,0,0],[0,0,0]]
 *    Output: 0
 *
 *  Constraints:
 *    n == grid.length == grid[r].length
 *    2 <= n <= 50
 *    0 <= grid[r][c] <= 100
 */
public class MaxIncreaseToKeepCitySkyline {

    // V0
    // IDEA: each cell can be raised to min(rowMax[r], colMax[c]) without altering
    //       any of the 4 skylines.
    /**
     * time = O(m * n)
     * space = O(m + n)
     */
    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] rowMax = new int[m];
        int[] colMax = new int[n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                rowMax[i] = Math.max(rowMax[i], grid[i][j]);
                colMax[j] = Math.max(colMax[j], grid[i][j]);
            }
        }

        int res = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res += Math.min(rowMax[i], colMax[j]) - grid[i][j];
            }
        }
        return res;
    }
}
