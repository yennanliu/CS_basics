package LeetCodeJava.Array;

// https://leetcode.com/problems/special-positions-in-a-binary-matrix/description/

/**
 * 1582. Special Positions in a Binary Matrix
 * Easy
 *
 * Given an m x n binary matrix mat, return the number of special positions in mat.
 *
 * A position (i, j) is called special if mat[i][j] == 1 and all other elements in row i
 * and column j are 0 (rows and columns are 0-indexed).
 *
 *
 * Example 1:
 *
 * Input: mat = [[1,0,0],[0,0,1],[1,0,0]]
 * Output: 1
 * Explanation: (1, 2) is a special position because mat[1][2] == 1 and all other elements
 * in row 1 and column 2 are 0.
 *
 * Example 2:
 *
 * Input: mat = [[1,0,0],[0,1,0],[0,0,1]]
 * Output: 3
 * Explanation: (0, 0), (1, 1) and (2, 2) are special positions.
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 100
 * mat[i][j] is either 0 or 1.
 *
 */
public class SpecialPositionsInABinaryMatrix {

    // V0
    // IDEA: ROW / COLUMN SUMS — (i, j) is special iff mat[i][j] == 1 and it is the only 1
    //       in its row and in its column, i.e. rowSum[i] == 1 and colSum[j] == 1
    /**
     * time = O(M * N)
     * space = O(M + N)
     */
    public int numSpecial(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        int[] rowSum = new int[rows];
        int[] colSum = new int[cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rowSum[r] += mat[r][c];
                colSum[c] += mat[r][c];
            }
        }

        int special = 0;
        for (int r = 0; r < rows; r++) {
            // a row with zero or several 1s cannot hold a special position
            if (rowSum[r] != 1) {
                continue;
            }
            for (int c = 0; c < cols; c++) {
                if (mat[r][c] == 1 && colSum[c] == 1) {
                    special++;
                }
            }
        }
        return special;
    }
}
