package LeetCodeJava.Array;

// https://leetcode.com/problems/diagonal-traverse/description/

import java.util.ArrayList;
import java.util.Collections;

/**
 * 498. Diagonal Traverse
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an m x n matrix mat, return an array of all the elements of the array in a diagonal order.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: mat = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [1,2,4,7,5,3,6,8,9]
 * Example 2:
 *
 * Input: mat = [[1,2],[3,4]]
 * Output: [1,2,3,4]
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 104
 * 1 <= m * n <= 104
 * -105 <= mat[i][j] <= 105
 * Seen this question in a real interview before?
 *
 */
public class DiagonalTraverse {

    // V0
    // TODO : implement
//    public int[] findDiagonalOrder(int[][] mat) {
//
//    }

    // V0-1
    // IDEA : matrix op (gpt)
    public int[] findDiagonalOrder_0_1(int[][] mat) {
        // Edge case: empty matrix
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return new int[0];
        }

        int rows = mat.length, cols = mat[0].length;
        int[] result = new int[rows * cols];
        int index = 0;

        // Loop through all possible diagonals (0 to rows + cols - 2)
        for (int d = 0; d < rows + cols - 1; d++) {
            // Determine starting point for this diagonal
            int r = (d % 2 == 0) ? Math.min(d, rows - 1) : Math.max(0, d - cols + 1);
            int c = (d % 2 == 0) ? Math.max(0, d - rows + 1) : Math.min(d, cols - 1);

            // Traverse the diagonal
            while (r >= 0 && r < rows && c >= 0 && c < cols) {
                result[index++] = mat[r][c];
                if (d % 2 == 0) { // Upward diagonal
                    r--;
                    c++;
                } else { // Downward diagonal
                    r++;
                    c--;
                }
            }
        }

        return result;
    }

    // V0-2
    // IDEA: ARRAY OP (fixed by gpt)
    /**
     *  IDEA: (4 cases to move per `boundary` conditions)
     *
     *  Explanation:
     *
     *  Direction Switching: goingUp flag tells whether
     *  we're moving up-right or down-left.
     *
     * Boundary Conditions:
     *
     * If at top row and going up → move right.
     *
     * If at right column and going up → move down.
     *
     * If at bottom row and going down → move right.
     *
     * If at left column and going down → move down.
     *
     *
     * Each cell is visited exactly once,
     * and bounds are always checked before accessing the matrix.
     *
     *
     */
    public int[] findDiagonalOrder_0_2(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return new int[0];
        }

        int rows = mat.length;
        int cols = mat[0].length;
        int[] result = new int[rows * cols];
        int row = 0, col = 0;
        boolean goingUp = true;

        for (int i = 0; i < result.length; i++) {
            result[i] = mat[row][col];

            if (goingUp) {
                // Move up-right
                if (col == cols - 1) {
                    row++;
                    goingUp = false;
                } else if (row == 0) {
                    col++;
                    goingUp = false;
                } else {
                    row--;
                    col++;
                }
            } else {
                // Move down-left
                if (row == rows - 1) {
                    col++;
                    goingUp = true;
                } else if (col == 0) {
                    row++;
                    goingUp = true;
                } else {
                    row++;
                    col--;
                }
            }
        }

        return result;
    }


    // V1-1
    // https://leetcode.com/problems/diagonal-traverse/editorial/
    // IDEA: Diagonal Iteration and Reversal
    public int[] findDiagonalOrder_1_1(int[][] matrix) {

        // Check for empty matrices
        if (matrix == null || matrix.length == 0) {
            return new int[0];
        }

        // Variables to track the size of the matrix
        int N = matrix.length;
        int M = matrix[0].length;

        // The two arrays as explained in the algorithm
        int[] result = new int[N * M];
        int k = 0;
        ArrayList<Integer> intermediate = new ArrayList<Integer>();

        // We have to go over all the elements in the first
        // row and the last column to cover all possible diagonals
        for (int d = 0; d < N + M - 1; d++) {

            // Clear the intermediate array every time we start
            // to process another diagonal
            intermediate.clear();

            // We need to figure out the "head" of this diagonal
            // The elements in the first row and the last column
            // are the respective heads.
            int r = d < M ? 0 : d - M + 1;
            int c = d < M ? d : M - 1;

            // Iterate until one of the indices goes out of scope
            // Take note of the index math to go down the diagonal
            while (r < N && c > -1) {

                intermediate.add(matrix[r][c]);
                ++r;
                --c;
            }

            // Reverse even numbered diagonals. The
            // article says we have to reverse odd
            // numbered articles but here, the numbering
            // is starting from 0 :P
            if (d % 2 == 0) {
                Collections.reverse(intermediate);
            }

            for (int i = 0; i < intermediate.size(); i++) {
                result[k++] = intermediate.get(i);
            }
        }
        return result;
    }

    // V1-2
    // https://leetcode.com/problems/diagonal-traverse/editorial/
    // IDEA: Simulation
    public int[] findDiagonalOrder_1_2(int[][] matrix) {

        // Check for empty matrices
        if (matrix == null || matrix.length == 0) {
            return new int[0];
        }

        // Variables to track the size of the matrix
        int N = matrix.length;
        int M = matrix[0].length;

        // Incides that will help us progress through
        // the matrix, one element at a time.
        int row = 0, column = 0;

        // As explained in the article, this is the variable
        // that helps us keep track of what direction we are
        // processing the current diaonal
        int direction = 1;

        // The final result array
        int[] result = new int[N * M];
        int r = 0;

        // The uber while loop which will help us iterate over all
        // the elements in the array.
        while (row < N && column < M) {

            // First and foremost, add the current element to
            // the result matrix.
            result[r++] = matrix[row][column];

            // Move along in the current diagonal depending upon
            // the current direction.[i, j] -> [i - 1, j + 1] if
            // going up and [i, j] -> [i + 1][j - 1] if going down.
            int new_row = row + (direction == 1 ? -1 : 1);
            int new_column = column + (direction == 1 ? 1 : -1);

            // Checking if the next element in the diagonal is within the
            // bounds of the matrix or not. If it's not within the bounds,
            // we have to find the next head.
            if (new_row < 0 || new_row == N || new_column < 0 || new_column == M) {

                // If the current diagonal was going in the upwards
                // direction.
                if (direction == 1) {

                    // For an upwards going diagonal having [i, j] as its tail
                    // If [i, j + 1] is within bounds, then it becomes
                    // the next head. Otherwise, the element directly below
                    // i.e. the element [i + 1, j] becomes the next head
                    row += (column == M - 1 ? 1 : 0);
                    column += (column < M - 1 ? 1 : 0);

                } else {

                    // For a downwards going diagonal having [i, j] as its tail
                    // if [i + 1, j] is within bounds, then it becomes
                    // the next head. Otherwise, the element directly below
                    // i.e. the element [i, j + 1] becomes the next head
                    column += (row == N - 1 ? 1 : 0);
                    row += (row < N - 1 ? 1 : 0);
                }

                // Flip the direction
                direction = 1 - direction;

            } else {

                row = new_row;
                column = new_column;
            }
        }
        return result;
    }

    // V2
}
