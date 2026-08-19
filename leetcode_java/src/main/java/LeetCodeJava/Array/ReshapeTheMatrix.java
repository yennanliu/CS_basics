package LeetCodeJava.Array;

// https://leetcode.com/problems/reshape-the-matrix/

/**
 *  566. Reshape the Matrix
 *  Easy
 *
 *  You are given an m x n matrix mat and two integers r and c representing the
 *  number of rows and the number of columns of the wanted reshaped matrix.
 *
 *  The reshaped matrix should be filled with all the elements of the original
 *  matrix in the same row-traversing order as they were.
 *
 *  If the reshape operation with given parameters is possible and legal, output
 *  the new reshaped matrix; otherwise, output the original matrix.
 *
 *  Example 1:
 *  Input: mat = [[1,2],[3,4]], r = 1, c = 4
 *  Output: [[1,2,3,4]]
 *
 *  Example 2:
 *  Input: mat = [[1,2],[3,4]], r = 2, c = 4
 *  Output: [[1,2],[3,4]]
 *
 *  Constraints:
 *  m == mat.length
 *  n == mat[i].length
 *  1 <= m, n <= 100
 *  -1000 <= mat[i][j] <= 1000
 *  1 <= r, c <= 300
 */
public class ReshapeTheMatrix {

    // V0
    // IDEA: map the flattened index k -> (k / c, k % c) in the new matrix
    /**
     * time = O(m * n)
     * space = O(r * c)  (the output)
     */
    public int[][] matrixReshape(int[][] mat, int r, int c) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return mat;
        }
        int m = mat.length;
        int n = mat[0].length;
        if (m * n != r * c) {
            return mat;
        }
        int[][] res = new int[r][c];
        for (int k = 0; k < m * n; k++) {
            res[k / c][k % c] = mat[k / n][k % n];
        }
        return res;
    }
}
