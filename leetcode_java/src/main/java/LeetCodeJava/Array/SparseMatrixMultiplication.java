package LeetCodeJava.Array;

// https://leetcode.com/problems/sparse-matrix-multiplication/description/
// https://leetcode.ca/all/311.html

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 311. Sparse Matrix Multiplication
 * Given two sparse matrices A and B, return the result of AB.
 *
 * You may assume that A's column number is equal to B's row number.
 *
 * Example:
 *
 * Input:
 *
 * A = [
 *   [ 1, 0, 0],
 *   [-1, 0, 3]
 * ]
 *
 * B = [
 *   [ 7, 0, 0 ],
 *   [ 0, 0, 0 ],
 *   [ 0, 0, 1 ]
 * ]
 *
 * Output:
 *
 *      |  1 0 0 |   | 7 0 0 |   |  7 0 0 |
 * AB = | -1 0 3 | x | 0 0 0 | = | -7 0 3 |
 *                   | 0 0 1 |
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Apple Bloomberg Facebook Goldman Sachs Google LinkedIn Microsoft Snapchat
 *
 */
public class SparseMatrixMultiplication {

  // V0
  // IDEA : ARRAY OP (fix by gpt)
  /**
   *  Why there is 3 loop ?
   *
   *   -> Matrix Multiplication: Ci,j = Sigma(Aik * Bkj)
   *
   *   -> so we have 3 layer loop as below:
   *    - i : Iterates over the rows of  A  (outer loop).
   * 	- j : Iterates over the columns of  B  (second loop).
   * 	- k : Iterates over the “shared dimension” (columns of  A  or rows of  B ) to compute the dot product (inner loop).
   *
   *
   *  ->
   *
   *  The Role of the Loops
   * 	1.	Outer loop ( i ): Iterates over the rows of mat1 to calculate each row of the result matrix.
   * 	2.	Middle loop ( j ): Iterates over the columns of mat2 to compute each element in a row of the result matrix.
   * 	3.	Inner loop ( k ): Iterates over the “shared dimension” to compute the dot product of the  i^{th}  row of mat1 and the  j^{th}  column of mat2.
   *
   *
   * ->  Why the Inner Loop ( k ) Exists ?
   *
   *    -> The inner loop is necessary
   *       because each element of the result matrix
   *       is computed as the dot product of a
   *       row from mat1 and a column from mat2.
   *       Without this loop, the computation of the dot product would be incomplete.
   */
  public static int[][] multiply(int[][] mat1, int[][] mat2) {
        // Edge case: Single element matrices
        if (mat1.length == 1 && mat1[0].length == 1 && mat2.length == 1 && mat2[0].length == 1) {
            return new int[][]{{mat1[0][0] * mat2[0][0]}};
        }

        int l_1 = mat1.length;    // Number of rows in mat1
        int w_1 = mat1[0].length; // Number of columns in mat1 (and rows in mat2)

        int w_2 = mat2[0].length; // Number of columns in mat2

        // Initialize the result matrix
        int[][] res = new int[l_1][w_2];

        // Perform matrix multiplication
        for (int i = 0; i < l_1; i++) {
            for (int j = 0; j < w_2; j++) {
                int sum = 0; // Sum for res[i][j]
                for (int k = 0; k < w_1; k++) {
                    sum += mat1[i][k] * mat2[k][j];
                }
                res[i][j] = sum;
            }
        }

        return res;
    }

    // V1
    // IDEA : ARRAY OP (gpt)
    public int[][] multiply_1(int[][] A, int[][] B) {
        int m = A.length; // Number of rows in A
        int n = A[0].length; // Number of columns in A
        int p = B[0].length; // Number of columns in B

        int[][] result = new int[m][p];

        // Iterate through rows of A
        for (int i = 0; i < m; i++) {
            for (int k = 0; k < n; k++) {
                if (A[i][k] != 0) { // Skip zero entries in A
                    for (int j = 0; j < p; j++) {
                        if (B[k][j] != 0) { // Skip zero entries in B
                            result[i][j] += A[i][k] * B[k][j];
                        }
                    }
                }
            }
        }

        return result;
    }

    // V2
    // https://leetcode.ca/2016-10-06-311-Sparse-Matrix-Multiplication/
    public int[][] multiply_2(int[][] mat1, int[][] mat2) {
        int m = mat1.length, n = mat2[0].length;
        int[][] ans = new int[m][n];
        List<int[]>[] g1 = f(mat1);
        List<int[]>[] g2 = f(mat2);
        for (int i = 0; i < m; ++i) {
            for (int[] p : g1[i]) {
                int k = p[0], x = p[1];
                for (int[] q : g2[k]) {
                    int j = q[0], y = q[1];
                    ans[i][j] += x * y;
                }
            }
        }
        return ans;
    }

    private List<int[]>[] f(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        List<int[]>[] g = new List[m];
        Arrays.setAll(g, i -> new ArrayList<>());
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (mat[i][j] != 0) {
                    g[i].add(new int[] {j, mat[i][j]});
                }
            }
        }
        return g;
    }

    // V3
}
