package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/largest-magic-square/description/
/**
 * 1895. Largest Magic Square
 * Medium
 *
 * A k x k magic square is a k x k grid filled with integers such that every row sum, every column
 * sum, and both diagonal sums are all equal. The integers in the magic square do not have to be
 * distinct. Every 1 x 1 grid is trivially a magic square.
 *
 * Given an m x n integer grid, return the size (i.e., the side length k) of the largest magic square
 * that can be found within this grid.
 *
 *
 * Example 1:
 *
 * Input: grid = [[7,1,4,5,6],[2,5,1,6,4],[1,5,4,3,2],[1,2,7,3,4]]
 * Output: 3
 * Explanation: The largest magic square has a size of 3.
 * Every row sum, column sum, and diagonal sum of this magic square is equal to 12.
 *
 * Example 2:
 *
 * Input: grid = [[5,1,3,1],[9,3,3,1],[1,3,3,8]]
 * Output: 2
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 50
 * 1 <= grid[i][j] <= 10^6
 *
 */
public class LargestMagicSquare {

    // V0
    // IDEA: ROW / COLUMN PREFIX SUMS + TRY SIZES FROM LARGEST DOWN
    // rowPrefix[i][j] = grid[i][0 .. j-1], colPrefix[i][j] = grid[0 .. i-1][j], so any row or column
    // slice of a candidate square is O(1). The first size with a valid square anywhere is the answer;
    // size 1 always works.
    /**
     * time = O(M * N * min(M, N)^2)
     * space = O(M * N)
     */
    public int largestMagicSquare(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        long[][] rowPrefix = new long[rows][cols + 1];
        long[][] colPrefix = new long[rows + 1][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rowPrefix[i][j + 1] = rowPrefix[i][j] + grid[i][j];
                colPrefix[i + 1][j] = colPrefix[i][j] + grid[i][j];
            }
        }

        for (int size = Math.min(rows, cols); size >= 2; size--) {
            for (int top = 0; top + size <= rows; top++) {
                for (int left = 0; left + size <= cols; left++) {
                    if (isMagic(grid, rowPrefix, colPrefix, top, left, size)) {
                        return size;
                    }
                }
            }
        }
        return 1;
    }

    /** Is the size x size square with top-left corner (top, left) magic? */
    private boolean isMagic(int[][] grid, long[][] rowPrefix, long[][] colPrefix, int top, int left, int size) {
        // every line must match the first row's sum
        long target = rowPrefix[top][left + size] - rowPrefix[top][left];

        for (int i = top + 1; i < top + size; i++) {
            if (rowPrefix[i][left + size] - rowPrefix[i][left] != target) {
                return false;
            }
        }
        for (int j = left; j < left + size; j++) {
            if (colPrefix[top + size][j] - colPrefix[top][j] != target) {
                return false;
            }
        }

        // the two diagonals are walked directly
        long mainDiagonal = 0;
        long antiDiagonal = 0;
        for (int t = 0; t < size; t++) {
            mainDiagonal += grid[top + t][left + t];
            antiDiagonal += grid[top + t][left + size - 1 - t];
        }
        return mainDiagonal == target && antiDiagonal == target;
    }
}
