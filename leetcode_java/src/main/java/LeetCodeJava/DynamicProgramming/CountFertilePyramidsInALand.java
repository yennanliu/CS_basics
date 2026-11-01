package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-fertile-pyramids-in-a-land/description/

/**
 * 2088. Count Fertile Pyramids in a Land
 * Hard
 *
 * A farmer has a rectangular grid of land with m rows and n columns that can be divided into unit cells. Each cell is either fertile (represented by a 1) or barren (represented by a 0). All cells outside the grid are considered barren.
 *
 * A pyramidal plot of land can be defined as a set of cells with the following criteria:
 *
 * The number of cells in the set has to be greater than 1 and all cells must be fertile.
 * The apex of a pyramid is the topmost cell of the pyramid. The height of a pyramid is the number of rows it covers. Let (r, c) be the apex of the pyramid, and its height be h. Then, the plot comprises of cells (i, j) where r <= i <= r + h - 1 and c - (i - r) <= j <= c + (i - r).
 *
 * An inverse pyramidal plot of land can be defined as a set of cells with similar criteria outlined above with the following modification:
 *
 * Instead of the apex, the highest cell is the bottommost cell of the pyramid. Let (r, c) be the apex, and its height be h. Then, the plot comprises of cells (i, j) where r - h + 1 <= i <= r and c - (r - i) <= j <= c + (r - i).
 *
 * Given a 0-indexed m x n binary matrix grid representing the farmland, return the total number of pyramidal and inverse pyramidal plots that can be found in grid.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1,1,0],[1,1,1,1]]
 * Output: 2
 * Explanation: The 2 possible pyramidal plots are shown in blue and red respectively.
 * There are no inverse pyramidal plots in this grid.
 * Hence total number of pyramidal and inverse pyramidal plots is 2 + 0 = 2.
 *
 * Example 2:
 *
 * Input: grid = [[1,1,1],[1,1,1]]
 * Output: 2
 * Explanation: The pyramidal plot is shown in blue, and the inverse pyramidal plot is shown in red.
 * Hence the total number of plots is 1 + 1 = 2.
 *
 * Example 3:
 *
 * Input: grid = [[1,1,1,1,0],[1,1,1,1,1],[1,1,1,1,1],[0,1,0,0,1]]
 * Output: 13
 * Explanation: There are 7 pyramidal plots, 3 of which are shown in the blue and red plots.
 * There are 6 inverse pyramidal plots, 2 of which are shown in the purple plot.
 * The total number of plots is 7 + 6 = 13.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 1000
 * 1 <= m * n <= 10^5
 * grid[i][j] is either 0 or 1.
 *
 */
public class CountFertilePyramidsInALand {

    // V0
    // IDEA: DP "TALLEST PYRAMID WITH APEX HERE", RUN TWICE (ON THE GRID AND ON ITS VERTICAL FLIP)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int countPyramids(int[][] grid) {
        int rows = grid.length;

        // the inverse pyramids of grid are the upright pyramids of the vertically flipped grid
        int[][] flipped = new int[rows][];
        for (int i = 0; i < rows; i++) {
            flipped[i] = grid[rows - 1 - i];
        }

        return countUpright(grid) + countUpright(flipped);
    }

    /** Counts upright pyramidal plots (apex on top, widening downward). */
    private int countUpright(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // dp[i][j] = height of the tallest pyramid whose apex is (i, j); 0 if the cell is barren
        int[][] dp = new int[rows][cols];
        int total = 0;

        // fill bottom-up: a pyramid at (i, j) depends on the row below it
        for (int i = rows - 1; i >= 0; i--) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0) {
                    continue;
                }
                if (i == rows - 1 || j == 0 || j == cols - 1) {
                    // bottom row or border column: cannot grow below, height 1
                    dp[i][j] = 1;
                } else {
                    // growing one row taller needs all three cells below to support it;
                    // a barren cell stores 0, so the min caps the height there
                    int below = Math.min(dp[i + 1][j - 1], Math.min(dp[i + 1][j], dp[i + 1][j + 1]));
                    dp[i][j] = 1 + below;
                }
                // a height-h apex gives h - 1 plots (heights 2..h; height 1 does not count)
                total += dp[i][j] - 1;
            }
        }
        return total;
    }
}
