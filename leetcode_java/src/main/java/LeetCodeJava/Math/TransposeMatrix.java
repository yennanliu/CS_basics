package LeetCodeJava.Math;

// https://leetcode.com/problems/transpose-matrix/description/

import java.util.Arrays;

/**
 * 867. Transpose Matrix
 * Easy
 * Topics
 * Companies
 * Hint
 * Given a 2D integer array matrix, return the transpose of matrix.
 *
 * The transpose of a matrix is the matrix flipped over its main diagonal, switching the matrix's row and column indices.
 *

 * Example 1:
 *
 * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [[1,4,7],[2,5,8],[3,6,9]]
 * Example 2:
 *
 * Input: matrix = [[1,2,3],[4,5,6]]
 * Output: [[1,4],[2,5],[3,6]]
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 1000
 * 1 <= m * n <= 105
 * -109 <= matrix[i][j] <= 109
 *
 *
 */
public class TransposeMatrix {

    // V0
//    public int[][] transpose(int[][] matrix) {
//
//    }

    // V0-1
    // IDEA: MATH + ARRAY OP (fixed by gpt)
    public int[][] transpose_0_1(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new int[0][0];
        }

        int rows = matrix.length;
        int cols = matrix[0].length;

        int[][] result = new int[cols][rows]; // Transposed dimensions

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }

        return result;
    }

    // V1

    // V2
    // IDEA: COPY DIRECTLY
    // https://leetcode.com/problems/transpose-matrix/editorial/
    public int[][] transpose_2(int[][] A) {
        int R = A.length, C = A[0].length;
        int[][] ans = new int[C][R];
        for (int r = 0; r < R; ++r)
            for (int c = 0; c < C; ++c) {
                ans[c][r] = A[r][c];
            }
        return ans;
    }

}
