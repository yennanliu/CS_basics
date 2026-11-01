package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-valid-matrix-given-row-and-column-sums/description/

/**
 * 1605. Find Valid Matrix Given Row and Column Sums
 * Medium
 *
 * You are given two arrays rowSum and colSum of non-negative integers where
 * rowSum[i] is the sum of the elements in the ith row and colSum[j] is the sum of
 * the elements of the jth column of a 2D matrix. In other words, you do not know
 * the elements of the matrix, but you do know the sums of each row and column.
 *
 * Find any matrix of non-negative integers of size rowSum.length x colSum.length
 * that satisfies the rowSum and colSum requirements.
 *
 * Return a 2D array representing any matrix that fulfills the requirements. It's
 * guaranteed that at least one matrix that fulfills the requirements exists.
 *
 *
 * Example 1:
 *
 * Input: rowSum = [3,8], colSum = [4,7]
 * Output: [[3,0],
 *          [1,7]]
 * Explanation: Another possible matrix is: [[1,2],
 *                                           [3,5]]
 *
 * Example 2:
 *
 * Input: rowSum = [5,7,10], colSum = [8,6,8]
 * Output: [[0,5,0],
 *          [6,1,0],
 *          [2,0,8]]
 *
 *
 * Constraints:
 *
 * 1 <= rowSum.length, colSum.length <= 500
 * 0 <= rowSum[i], colSum[i] <= 10^8
 * sum(rowSum) == sum(colSum)
 *
 */
public class FindValidMatrixGivenRowAndColumnSums {

    // V0
    // IDEA: GREEDY (FILL EACH CELL WITH AS MUCH AS IT CAN TAKE)
    //       cell[i][j] = min(rowLeft[i], colLeft[j]), then subtract from both.
    //       Every step closes a row or a column, and since the totals are equal
    //       both run out together at the end.
    /**
     * time = O(M * N)
     * space = O(M + N) extra, besides the output
     */
    public int[][] restoreMatrix(int[] rowSum, int[] colSum) {
        int rows = rowSum.length;
        int cols = colSum.length;

        // copies, so the caller's arrays are not mutated
        int[] rowLeft = rowSum.clone();
        int[] colLeft = colSum.clone();
        int[][] matrix = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int value = Math.min(rowLeft[i], colLeft[j]);
                matrix[i][j] = value;
                rowLeft[i] -= value;
                colLeft[j] -= value;
            }
        }
        return matrix;
    }
}
