package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/increment-submatrices-by-one/description/

/**
 * 2536. Increment Submatrices by One
 * Medium
 *
 * You are given a positive integer n, indicating that we initially have an n x n 0-indexed integer matrix mat filled with zeroes.
 *
 * You are also given a 2D integer array query. For each query[i] = [row1_i, col1_i, row2_i, col2_i], you should do the following operation:
 *
 * Add 1 to every element in the submatrix with the top left corner (row1_i, col1_i) and the bottom right corner (row2_i, col2_i). That is, add 1 to mat[x][y] for all row1_i <= x <= row2_i and col1_i <= y <= col2_i.
 *
 * Return the matrix mat after performing every query.
 *
 *
 * Example 1:
 *
 * Input: n = 3, queries = [[1,1,2,2],[0,0,1,1]]
 * Output: [[1,1,0],[1,2,1],[0,1,1]]
 * Explanation: The diagram above shows the initial matrix, the matrix after the first query, and the matrix after the second query.
 * - In the first query, we add 1 to every element in the submatrix with the top left corner (1, 1) and bottom right corner (2, 2).
 * - In the second query, we add 1 to every element in the submatrix with the top left corner (0, 0) and bottom right corner (1, 1).
 *
 * Example 2:
 *
 * Input: n = 2, queries = [[0,0,1,1]]
 * Output: [[1,1],[1,1]]
 * Explanation: The diagram above shows the initial matrix and the matrix after the first query.
 * - In the first query we add 1 to every element in the matrix.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 500
 * 1 <= queries.length <= 10^4
 * 0 <= row1_i <= row2_i < n
 * 0 <= col1_i <= col2_i < n
 *
 */
public class IncrementSubmatricesByOne {

    // V0
    // IDEA: 2D DIFFERENCE ARRAY (+ 2D PREFIX SUM TO RESTORE)
    //       mark each query with 4 corner stamps instead of touching its cells:
    //         diff[r1][c1] += 1, diff[r2+1][c1] -= 1, diff[r1][c2+1] -= 1, diff[r2+1][c2+1] += 1
    //       a 2D prefix sum then rebuilds the matrix, because cell (i, j) sums exactly
    //       the stamps of the rectangles covering it. stamps that fall outside the
    //       matrix are skipped -- nothing beyond the edge needs cancelling.
    /**
     * time = O(Q + N^2)
     * space = O(1) beyond the output
     */
    public int[][] rangeAddQueries(int n, int[][] queries) {
        int[][] mat = new int[n][n];
        for (int[] query : queries) {
            int row1 = query[0];
            int col1 = query[1];
            int row2 = query[2];
            int col2 = query[3];
            mat[row1][col1] += 1;
            if (row2 + 1 < n) {
                mat[row2 + 1][col1] -= 1;
            }
            if (col2 + 1 < n) {
                mat[row1][col2 + 1] -= 1;
            }
            if (row2 + 1 < n && col2 + 1 < n) {
                mat[row2 + 1][col2 + 1] += 1;
            }
        }

        // in-place 2D prefix sum: add top and left, remove the double-counted diagonal
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i > 0) {
                    mat[i][j] += mat[i - 1][j];
                }
                if (j > 0) {
                    mat[i][j] += mat[i][j - 1];
                }
                if (i > 0 && j > 0) {
                    mat[i][j] -= mat[i - 1][j - 1];
                }
            }
        }
        return mat;
    }

    // V1
    // IDEA: ONE 1D DIFFERENCE ARRAY PER ROW
    //       for each query, stamp +1 at col1 and -1 at col2 + 1 on every row it spans,
    //       then a running sum along each row restores the counts. simpler to reason
    //       about than the 2D version, at O(rows spanned) per query.
    /**
     * time = O(Q * N + N^2)
     * space = O(1) beyond the output
     */
    public int[][] rangeAddQueries_1(int n, int[][] queries) {
        int[][] mat = new int[n][n];
        for (int[] query : queries) {
            for (int row = query[0]; row <= query[2]; row++) {
                mat[row][query[1]] += 1;
                if (query[3] + 1 < n) {
                    mat[row][query[3] + 1] -= 1;
                }
            }
        }
        for (int row = 0; row < n; row++) {
            for (int col = 1; col < n; col++) {
                mat[row][col] += mat[row][col - 1];
            }
        }
        return mat;
    }
}
