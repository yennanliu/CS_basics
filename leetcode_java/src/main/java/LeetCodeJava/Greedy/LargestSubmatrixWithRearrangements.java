package LeetCodeJava.Greedy;

// https://leetcode.com/problems/largest-submatrix-with-rearrangements/description/

import java.util.Arrays;

/**
 * 1727. Largest Submatrix With Rearrangements
 * Medium
 *
 * You are given a binary matrix matrix of size m x n, and you are allowed to
 * rearrange the columns of the matrix in any order.
 *
 * Return the area of the largest submatrix within matrix where every element of the
 * submatrix is 1 after reordering the columns optimally.
 *
 *
 * Example 1:
 *
 * Input: matrix = [[0,0,1],[1,1,1],[1,0,1]]
 * Output: 4
 *
 * Example 2:
 *
 * Input: matrix = [[1,0,1,0,1]]
 * Output: 3
 *
 * Example 3:
 *
 * Input: matrix = [[1,1,0],[1,0,1]]
 * Output: 2
 * Explanation: Notice that you must rearrange entire columns, and there is no way to
 * make a submatrix of 1s larger than an area of 2.
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m * n <= 10^5
 * matrix[i][j] is either 0 or 1.
 *
 */
public class LargestSubmatrixWithRearrangements {

    // V0
    // IDEA: COLUMN HISTOGRAM + SORT EACH ROW
    //       height[j] = consecutive 1s ending at the current row in column j.
    //       Columns can be permuted, so with row i as the bottom edge sort the
    //       heights descending: the k tallest give area k * sorted[k - 1].
    /**
     * time = O(M * N log N)
     * space = O(N)
     */
    public int largestSubmatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] height = new int[cols];
        int best = 0;

        for (int r = 0; r < rows; r++) {
            // extend or reset each column's run of 1s
            for (int c = 0; c < cols; c++) {
                if (matrix[r][c] == 1) {
                    height[c] = height[c] + 1;
                } else {
                    height[c] = 0;
                }
            }

            // ascending sort; walking from the right visits the tallest first
            int[] sorted = height.clone();
            Arrays.sort(sorted);
            for (int i = cols - 1; i >= 0; i--) {
                if (sorted[i] == 0) {
                    break;
                }
                int width = cols - i; // columns from i to the end are all >= sorted[i]
                best = Math.max(best, width * sorted[i]);
            }
        }
        return best;
    }
}
