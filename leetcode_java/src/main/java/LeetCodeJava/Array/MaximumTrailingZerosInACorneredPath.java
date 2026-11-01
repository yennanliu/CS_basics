package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-trailing-zeros-in-a-cornered-path/description/

/**
 * 2245. Maximum Trailing Zeros in a Cornered Path
 * Medium
 *
 * You are given a 2D integer array grid of size m x n, where each cell contains a positive integer.
 *
 * A cornered path is defined as a set of adjacent cells with at most one turn. More specifically, the path should exclusively move either horizontally or vertically up to the turn (if there is one), without returning to a previously visited cell. After the turn, the path will then move exclusively in the alternate direction: move vertically if it moved horizontally, and vice versa, also without returning to a previously visited cell.
 *
 * The product of a path is defined as the product of all the values in the path.
 *
 * Return the maximum number of trailing zeros in the product of a cornered path found in grid.
 *
 * Note:
 *
 * Horizontal movement means moving in either the left or right direction.
 * Vertical movement means moving in either the up or down direction.
 *
 *
 * Example 1:
 *
 * Input: grid = [[23,17,15,3,20],[8,1,20,27,11],[9,4,6,2,21],[40,9,1,10,6],[22,7,4,5,3]]
 * Output: 3
 * Explanation: The grid on the left shows a valid cornered path.
 * It has a product of 15 * 20 * 6 * 1 * 10 = 18000 which has 3 trailing zeros.
 * It can be shown that this is the maximum trailing zeros in the product of a cornered path.
 *
 * The grid in the middle is not a cornered path as it has more than one turn.
 * The grid on the right is not a cornered path as it requires a return to a previously visited cell.
 *
 * Example 2:
 *
 * Input: grid = [[4,3,2],[7,6,1],[8,8,8]]
 * Output: 0
 * Explanation: The grid is shown in the figure above.
 * There are no cornered paths in the grid that result in a product with a trailing zero.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 10^5
 * 1 <= m * n <= 10^5
 * 1 <= grid[i][j] <= 1000
 *
 */
public class MaximumTrailingZerosInACorneredPath {

    // V0
    // IDEA: TRAILING ZEROS = min(#2s, #5s) + ROW / COLUMN PREFIX SUMS OF THE 2 AND 5 EXPONENTS
    /**
     *  Replace each cell by how many factors of 2 and of 5 it holds. Every cell
     *  adds a non-negative amount, so the best arm in a direction always runs
     *  to the border. A cornered path is a horizontal arm (left or right) plus a
     *  vertical arm (up or down) meeting at a corner cell, so each cell has 4
     *  shapes; both arms include the corner, so subtract it once.
     *  A straight path is covered too: one arm may be just the corner.
     *
     * time = O(M * N)
     * space = O(M * N)
     */
    public int maxTrailingZeros(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // rowTwos[i][j + 1] = 2-exponents of row i over columns 0 .. j (same for fives)
        int[][] rowTwos = new int[rows][cols + 1];
        int[][] rowFives = new int[rows][cols + 1];
        // colTwos[i + 1][j] = 2-exponents of column j over rows 0 .. i
        int[][] colTwos = new int[rows + 1][cols];
        int[][] colFives = new int[rows + 1][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int twos = countFactor(grid[i][j], 2);
                int fives = countFactor(grid[i][j], 5);
                rowTwos[i][j + 1] = rowTwos[i][j] + twos;
                rowFives[i][j + 1] = rowFives[i][j] + fives;
                colTwos[i + 1][j] = colTwos[i][j] + twos;
                colFives[i + 1][j] = colFives[i][j] + fives;
            }
        }

        int best = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // the corner cell itself, counted in both arms
                int cornerTwos = rowTwos[i][j + 1] - rowTwos[i][j];
                int cornerFives = rowFives[i][j + 1] - rowFives[i][j];

                // horizontal arms: columns 0 .. j, and columns j .. cols-1
                int[] horizontalTwos = {rowTwos[i][j + 1], rowTwos[i][cols] - rowTwos[i][j]};
                int[] horizontalFives = {rowFives[i][j + 1], rowFives[i][cols] - rowFives[i][j]};
                // vertical arms: rows 0 .. i, and rows i .. rows-1
                int[] verticalTwos = {colTwos[i + 1][j], colTwos[rows][j] - colTwos[i][j]};
                int[] verticalFives = {colFives[i + 1][j], colFives[rows][j] - colFives[i][j]};

                for (int h = 0; h < 2; h++) {
                    for (int v = 0; v < 2; v++) {
                        int twos = horizontalTwos[h] + verticalTwos[v] - cornerTwos;
                        int fives = horizontalFives[h] + verticalFives[v] - cornerFives;
                        best = Math.max(best, Math.min(twos, fives));
                    }
                }
            }
        }
        return best;
    }

    /** How many times p divides x. */
    private int countFactor(int x, int p) {
        int count = 0;
        while (x % p == 0) {
            x /= p;
            count++;
        }
        return count;
    }
}
