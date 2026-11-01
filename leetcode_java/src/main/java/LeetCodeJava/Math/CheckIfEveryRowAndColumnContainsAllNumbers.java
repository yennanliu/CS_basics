package LeetCodeJava.Math;

// https://leetcode.com/problems/check-if-every-row-and-column-contains-all-numbers/description/

/**
 * 2133. Check if Every Row and Column Contains All Numbers
 * Easy
 *
 * An n x n matrix is valid if every row and every column contains all the integers from 1 to n (inclusive).
 *
 * Given an n x n integer matrix matrix, return true if the matrix is valid. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: matrix = [[1,2,3],[3,1,2],[2,3,1]]
 * Output: true
 * Explanation: In this case, n = 3, and every row and column contains the numbers 1, 2, and 3.
 * Hence, we return true.
 *
 * Example 2:
 *
 * Input: matrix = [[1,1,1],[1,2,3],[1,2,3]]
 * Output: false
 * Explanation: In this case, n = 3, but the first row and the first column do not contain the numbers 2 or 3.
 * Hence, we return false.
 *
 *
 * Constraints:
 *
 * n == matrix.length == matrix[i].length
 * 1 <= n <= 100
 * 1 <= matrix[i][j] <= n
 *
 */
public class CheckIfEveryRowAndColumnContainsAllNumbers {

    // V0
    // IDEA: EVERY ROW AND COLUMN MUST HOLD n DISTINCT VALUES (values are already in [1, n])
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public boolean checkValid(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            // seenInRow / seenInCol mark which of 1..n appeared in row i / column i
            boolean[] seenInRow = new boolean[n + 1];
            boolean[] seenInCol = new boolean[n + 1];
            for (int j = 0; j < n; j++) {
                int rowValue = matrix[i][j];
                int colValue = matrix[j][i];
                // a repeat means some other value of 1..n is missing
                if (seenInRow[rowValue] || seenInCol[colValue]) {
                    return false;
                }
                seenInRow[rowValue] = true;
                seenInCol[colValue] = true;
            }
        }
        return true;
    }
}
