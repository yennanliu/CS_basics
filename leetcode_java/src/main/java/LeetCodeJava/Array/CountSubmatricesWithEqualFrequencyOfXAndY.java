package LeetCodeJava.Array;

// https://leetcode.com/problems/count-submatrices-with-equal-frequency-of-x-and-y/description/

/**
 * 3212. Count Submatrices With Equal Frequency of X and Y
 * Medium
 *
 * Given a 2D character matrix grid, where grid[i][j] is either 'X', 'Y', or '.', return the number of submatrices that contain:
 *
 * grid[0][0]
 * an equal frequency of 'X' and 'Y'.
 * at least one 'X'.
 *
 *
 * Example 1:
 *
 * Input: grid = [["X","Y","."],["Y",".","."]]
 * Output: 3
 * Explanation:
 *
 * Example 2:
 *
 * Input: grid = [["X","X"],["X","Y"]]
 * Output: 0
 * Explanation:
 * No submatrix has an equal frequency of 'X' and 'Y'.
 *
 * Example 3:
 *
 * Input: grid = [[".","."],[".","."]]
 * Output: 0
 * Explanation:
 * No submatrix has at least one 'X'.
 *
 *
 * Constraints:
 *
 * 1 <= grid.length, grid[i].length <= 1000
 * grid[i][j] is either 'X', 'Y', or '.'.
 *
 */
public class CountSubmatricesWithEqualFrequencyOfXAndY {

    // V0
    // IDEA: 2D PREFIX SUM (a submatrix containing grid[0][0] is a prefix rectangle, one per bottom-right cell)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int numberOfSubmatrices(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // prefixX[i][j] = number of 'X' in the rectangle (0,0) .. (i-1, j-1); same for 'Y'
        int[][] prefixX = new int[rows + 1][cols + 1];
        int[][] prefixY = new int[rows + 1][cols + 1];
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int isX = grid[r][c] == 'X' ? 1 : 0;
                int isY = grid[r][c] == 'Y' ? 1 : 0;

                // inclusion-exclusion: top + left - overlap + this cell
                prefixX[r + 1][c + 1] = isX + prefixX[r][c + 1] + prefixX[r + 1][c] - prefixX[r][c];
                prefixY[r + 1][c + 1] = isY + prefixY[r][c + 1] + prefixY[r + 1][c] - prefixY[r][c];

                // the rectangle (0,0)..(r,c) qualifies: at least one X, and as many X as Y
                int xCount = prefixX[r + 1][c + 1];
                int yCount = prefixY[r + 1][c + 1];
                if (xCount > 0 && xCount == yCount) {
                    count++;
                }
            }
        }
        return count;
    }
}
