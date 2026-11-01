package LeetCodeJava.Greedy;

// https://leetcode.com/problems/reconstruct-a-2-row-binary-matrix/description/

import java.util.*;

/**
 * 1253. Reconstruct a 2-Row Binary Matrix
 * Medium
 *
 * Given the following details of a matrix with n columns and 2 rows :
 *
 * The matrix is a binary matrix, which means each element in the matrix can be 0 or 1.
 * The sum of elements of the 0-th(upper) row is given as upper.
 * The sum of elements of the 1-st(lower) row is given as lower.
 * The sum of elements in the i-th column(0-indexed) is colsum[i], where colsum is given as
 * an integer array with length n.
 *
 * Your task is to reconstruct the matrix with upper, lower and colsum.
 *
 * Return it as a 2-D integer array.
 *
 * If there are more than one valid solution, any of them will be accepted.
 *
 * If no valid solution exists, return an empty 2-D array.
 *
 *
 * Example 1:
 *
 * Input: upper = 2, lower = 1, colsum = [1,1,1]
 * Output: [[1,1,0],[0,0,1]]
 * Explanation: [[1,0,1],[0,1,0]], and [[0,1,1],[1,0,0]] are also correct answers.
 *
 * Example 2:
 *
 * Input: upper = 2, lower = 3, colsum = [2,2,1,1]
 * Output: []
 *
 * Example 3:
 *
 * Input: upper = 5, lower = 5, colsum = [2,1,2,0,1,0,1,2,0,1]
 * Output: [[1,1,1,0,1,0,0,1,0,0],[1,0,1,0,0,0,1,1,0,1]]
 *
 *
 * Constraints:
 *
 * 1 <= colsum.length <= 10^5
 * 0 <= upper, lower <= colsum.length
 * 0 <= colsum[i] <= 2
 *
 */
public class ReconstructA2RowBinaryMatrix {

    // V0
    // IDEA: GREEDY (a 2-column is forced; a 1-column goes to the row with more budget left)
    /**
     * time = O(N)
     * space = O(1) (excluding the output)
     */
    public List<List<Integer>> reconstructMatrix(int upper, int lower, int[] colsum) {
        int n = colsum.length;
        Integer[] upperRow = new Integer[n];
        Integer[] lowerRow = new Integer[n];

        for (int j = 0; j < n; j++) {
            upperRow[j] = 0;
            lowerRow[j] = 0;
            if (colsum[j] == 2) {
                // both rows must hold a 1 here
                upperRow[j] = 1;
                lowerRow[j] = 1;
                upper--;
                lower--;
            } else if (colsum[j] == 1) {
                // free choice: keeping the two budgets balanced never blocks a later 2
                if (upper > lower) {
                    upperRow[j] = 1;
                    upper--;
                } else {
                    lowerRow[j] = 1;
                    lower--;
                }
            }
            if (upper < 0 || lower < 0) {
                return new ArrayList<>();
            }
        }

        // both row sums must be used up exactly
        if (upper != 0 || lower != 0) {
            return new ArrayList<>();
        }

        List<List<Integer>> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(Arrays.asList(upperRow)));
        matrix.add(new ArrayList<>(Arrays.asList(lowerRow)));
        return matrix;
    }
}
