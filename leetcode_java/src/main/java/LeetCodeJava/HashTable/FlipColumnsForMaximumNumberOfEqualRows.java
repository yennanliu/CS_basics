package LeetCodeJava.HashTable;

// https://leetcode.com/problems/flip-columns-for-maximum-number-of-equal-rows/description/

import java.util.*;

/**
 * 1072. Flip Columns For Maximum Number of Equal Rows
 * Medium
 *
 * You are given an m x n binary matrix matrix.
 *
 * You can choose any number of columns in the matrix and flip every cell in that
 * column (i.e., Change the value of the cell from 0 to 1 or vice versa).
 *
 * Return the maximum number of rows that have all values equal after some number of flips.
 *
 * Example 1:
 *
 * Input: matrix = [[0,1],[1,1]]
 * Output: 1
 * Explanation: After flipping no values, 1 row has all values equal.
 *
 * Example 2:
 *
 * Input: matrix = [[0,1],[1,0]]
 * Output: 2
 * Explanation: After flipping values in the first column, both rows have equal values.
 *
 * Example 3:
 *
 * Input: matrix = [[0,0,0],[0,0,1],[1,1,0]]
 * Output: 2
 * Explanation: After flipping values in the first two columns, the last two rows have equal values.
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 300
 * matrix[i][j] is either 0 or 1.
 *
 */
public class FlipColumnsForMaximumNumberOfEqualRows {

    // V0
    // IDEA: HASH MAP OF NORMALISED ROWS
    //   a column flip hits every row at once, so two rows can end up all-equal
    //   together exactly when they are identical OR exact complements.
    //   normalise each row relative to its first cell (same as first -> '0',
    //   different -> '1'): identical and complementary rows collapse to one key.
    //   answer = size of the biggest bucket
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int maxEqualRowsAfterFlips(int[][] matrix) {
        Map<String, Integer> patternCount = new HashMap<>();
        int best = 0;

        for (int[] row : matrix) {
            StringBuilder key = new StringBuilder();
            for (int value : row) {
                if (value == row[0]) {
                    key.append('0');
                } else {
                    key.append('1');
                }
            }
            String pattern = key.toString();
            int count = patternCount.getOrDefault(pattern, 0) + 1;
            patternCount.put(pattern, count);
            best = Math.max(best, count);
        }
        return best;
    }
}
