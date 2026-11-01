package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-value-sum-by-placing-three-rooks-i/description/

/**
 * 3256. Maximum Value Sum by Placing Three Rooks I
 * Hard
 *
 * You are given a m x n 2D array board representing a chessboard, where board[i][j] represents the value of the cell (i, j).
 *
 * Rooks in the same row or column attack each other. You need to place three rooks on the chessboard such that the rooks do not attack each other.
 *
 * Return the maximum sum of the cell values on which the rooks are placed.
 *
 *
 * Example 1:
 *
 * Input: board = [[-3,1,1,1],[-3,1,-3,1],[-3,2,1,1]]
 * Output: 4
 * Explanation:
 * We can place the rooks in the cells (0, 2), (1, 3), and (2, 1) for a sum of 1 + 1 + 2 = 4.
 *
 * Example 2:
 *
 * Input: board = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: 15
 * Explanation:
 * We can place the rooks in the cells (0, 0), (1, 1), and (2, 2) for a sum of 1 + 5 + 9 = 15.
 *
 * Example 3:
 *
 * Input: board = [[1,1,1],[1,1,1],[1,1,1]]
 * Output: 3
 * Explanation:
 * We can place the rooks in the cells (0, 2), (1, 1), and (2, 0) for a sum of 1 + 1 + 1 = 3.
 *
 *
 * Constraints:
 *
 * 3 <= m == board.length <= 100
 * 3 <= n == board[i].length <= 100
 * -10^9 <= board[i][j] <= 10^9
 *
 */
public class MaximumValueSumByPlacingThreeRooksI {

    // V0
    // IDEA: KEEP EACH ROW'S TOP 3 CELLS, ENUMERATE ROW TRIPLES (at most 2 columns are ever blocked)
    /**
     * time = O(M * N + M^3)
     * space = O(M)
     */
    public long maximumValueSum(int[][] board) {
        int rows = board.length;

        // top[r] = the column indexes of row r's three largest cells, best first
        int[][] top = new int[rows][];
        for (int r = 0; r < rows; r++) {
            top[r] = topThreeColumns(board[r]);
        }

        long best = Long.MIN_VALUE;
        for (int r1 = 0; r1 < rows; r1++) {
            for (int r2 = r1 + 1; r2 < rows; r2++) {
                for (int r3 = r2 + 1; r3 < rows; r3++) {
                    // try every combination of the three rows' candidate cells
                    for (int c1 : top[r1]) {
                        for (int c2 : top[r2]) {
                            if (c2 == c1) {
                                continue;
                            }
                            for (int c3 : top[r3]) {
                                if (c3 == c1 || c3 == c2) {
                                    continue;
                                }
                                long total = (long) board[r1][c1] + board[r2][c2] + board[r3][c3];
                                best = Math.max(best, total);
                            }
                        }
                    }
                }
            }
        }
        return best;
    }

    /** Returns the columns of the three largest values in the row, largest first. */
    private int[] topThreeColumns(int[] row) {
        int first = -1;
        int second = -1;
        int third = -1;
        for (int c = 0; c < row.length; c++) {
            if (first == -1 || row[c] > row[first]) {
                third = second;
                second = first;
                first = c;
            } else if (second == -1 || row[c] > row[second]) {
                third = second;
                second = c;
            } else if (third == -1 || row[c] > row[third]) {
                third = c;
            }
        }
        return new int[] {first, second, third};
    }
}
