package LeetCodeJava.Array;

// https://leetcode.com/problems/modify-the-matrix/description/

import java.util.*;

/**
 * 3033. Modify the Matrix
 * Easy
 *
 * Given a 0-indexed m x n integer matrix matrix, create a new 0-indexed matrix called answer. Make answer equal to matrix, then replace each element with the value -1 with the maximum element in its respective column.
 *
 * Return the matrix answer.
 *
 *
 * Example 1:
 *
 * Input: matrix = [[1,2,-1],[4,-1,6],[7,8,9]]
 * Output: [[1,2,9],[4,8,6],[7,8,9]]
 * Explanation: The diagram above shows the elements that are changed (in blue).
 * - We replace the value in the cell [1][1] with the maximum value in the column 1, that is 8.
 * - We replace the value in the cell [0][2] with the maximum value in the column 2, that is 9.
 *
 * Example 2:
 *
 * Input: matrix = [[3,-1],[5,2]]
 * Output: [[3,2],[5,2]]
 * Explanation: The diagram above shows the elements that are changed (in blue).
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 2 <= m, n <= 50
 * -1 <= matrix[i][j] <= 100
 * The input is generated such that each column contains at least one non-negative integer.
 *
 */
public class ModifyTheMatrix {

    // V0
    // IDEA: ONE PASS FOR THE COLUMN MAXIMA, ONE PASS TO SUBSTITUTE
    // every column holds a non-negative value, so its max is never -1 itself.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] modifiedMatrix(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // pass 1: the maximum of each column
        int[] columnMax = new int[cols];
        Arrays.fill(columnMax, -1);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                columnMax[c] = Math.max(columnMax[c], matrix[r][c]);
            }
        }

        // pass 2: copy, replacing each -1 with its column's maximum
        int[][] answer = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (matrix[r][c] == -1) {
                    answer[r][c] = columnMax[c];
                } else {
                    answer[r][c] = matrix[r][c];
                }
            }
        }
        return answer;
    }
}
