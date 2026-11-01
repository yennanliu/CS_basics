package LeetCodeJava.Array;

// https://leetcode.com/problems/difference-of-number-of-distinct-values-on-diagonals/description/

import java.util.*;

/**
 * 2711. Difference of Number of Distinct Values on Diagonals
 * Medium
 *
 * Given a 2D grid of size m x n, you should find the matrix answer of size m x n.
 *
 * The cell answer[r][c] is calculated by looking at the diagonal values of the cell grid[r][c]:
 *
 * Let leftAbove[r][c] be the number of distinct values on the diagonal to the left and above the cell grid[r][c] not including the cell grid[r][c] itself.
 * Let rightBelow[r][c] be the number of distinct values on the diagonal to the right and below the cell grid[r][c], not including the cell grid[r][c] itself.
 * Then answer[r][c] = |leftAbove[r][c] - rightBelow[r][c]|.
 *
 * A matrix diagonal is a diagonal line of cells starting from some cell in either the topmost row or leftmost column and going in the bottom-right direction until the end of the matrix is reached.
 *
 * For example, for the cell with indices (2, 3):
 *   - Red-colored cells are left and above the cell.
 *   - Blue-colored cells are right and below the cell.
 *
 * Return the matrix answer.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,2,3],[3,1,5],[3,2,1]]
 * Output: [[1,1,0],[1,0,1],[0,1,1]]
 * Explanation:
 * To calculate the answer cells:
 *
 * answer  | left-above elements    | leftAbove   | right-below elements   | rightBelow  | |leftAbove - rightBelow|
 * [0][0]  | []                     | 0           | [grid[1][1], grid[2][2]] | |{1, 1}| = 1 | 1
 * [0][1]  | []                     | 0           | [grid[1][2]]           | |{5}| = 1    | 1
 * [0][2]  | []                     | 0           | []                     | 0           | 0
 * [1][0]  | []                     | 0           | [grid[2][1]]           | |{2}| = 1    | 1
 * [1][1]  | [grid[0][0]]           | |{1}| = 1   | [grid[2][2]]           | |{1}| = 1    | 0
 * [1][2]  | [grid[0][1]]           | |{2}| = 1   | []                     | 0           | 1
 * [2][0]  | []                     | 0           | []                     | 0           | 0
 * [2][1]  | [grid[1][0]]           | |{3}| = 1   | []                     | 0           | 1
 * [2][2]  | [grid[0][0], grid[1][1]] | |{1, 1}| = 1 | []                   | 0           | 1
 *
 * Example 2:
 *
 * Input: grid = [[1]]
 * Output: [[0]]
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n, grid[i][j] <= 50
 *
 */
public class DifferenceOfNumberOfDistinctValuesOnDiagonals {

    // V0
    // IDEA: DIAGONAL SWEEP + PREFIX / SUFFIX DISTINCT COUNTS
    //       every cell lies on exactly one top-left -> bottom-right diagonal, starting at
    //       (i, 0) or (0, j). walk each diagonal forward with a set to get the distinct count
    //       strictly before each cell, then backward for the count strictly after it.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] differenceOfDistinctValues(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] answer = new int[rows][cols];

        // diagonals starting on the left column
        for (int r = 0; r < rows; r++) {
            fillDiagonal(grid, answer, r, 0);
        }
        // diagonals starting on the top row (0, 0 is already done)
        for (int c = 1; c < cols; c++) {
            fillDiagonal(grid, answer, 0, c);
        }
        return answer;
    }

    /** Fills answer for every cell on the diagonal that starts at (startRow, startCol). */
    private void fillDiagonal(int[][] grid, int[][] answer, int startRow, int startCol) {
        int rows = grid.length;
        int cols = grid[0].length;
        int length = Math.min(rows - startRow, cols - startCol);

        // before[t] = distinct values strictly above-left of the t-th cell
        int[] before = new int[length];
        Set<Integer> seen = new HashSet<>();
        for (int t = 0; t < length; t++) {
            // read the count first: the cell itself is excluded
            before[t] = seen.size();
            seen.add(grid[startRow + t][startCol + t]);
        }

        // walk back: seen.size() is the distinct count strictly below-right
        seen.clear();
        for (int t = length - 1; t >= 0; t--) {
            int r = startRow + t;
            int c = startCol + t;
            answer[r][c] = Math.abs(before[t] - seen.size());
            seen.add(grid[r][c]);
        }
    }
}
