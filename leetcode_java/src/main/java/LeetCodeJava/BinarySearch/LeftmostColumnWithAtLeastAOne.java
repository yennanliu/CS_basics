package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/leftmost-column-with-at-least-a-one/description/

import java.util.*;

/**
 * 1428. Leftmost Column with at Least a One
 * Medium
 *
 * A row-sorted binary matrix means that all elements are 0 or 1 and each row of
 * the matrix is sorted in non-decreasing order.
 *
 * Given a row-sorted binary matrix binaryMatrix, return the index (0-indexed) of
 * the leftmost column with a 1 in it. If such an index does not exist, return -1.
 *
 * You can't access the Binary Matrix directly. You may only access the matrix
 * using a BinaryMatrix interface:
 *
 * - BinaryMatrix.get(row, col) returns the element of the matrix at index
 *   (row, col) (0-indexed).
 * - BinaryMatrix.dimensions() returns the dimensions of the matrix as a list of
 *   2 elements [rows, cols], which means the matrix is rows x cols.
 *
 * Submissions making more than 1000 calls to BinaryMatrix.get will be judged
 * Wrong Answer.
 *
 *
 * Example 1:
 *
 * Input: mat = [[0,0],[1,1]]
 * Output: 0
 *
 * Example 2:
 *
 * Input: mat = [[0,0],[0,1]]
 * Output: 1
 *
 * Example 3:
 *
 * Input: mat = [[0,0],[0,0]]
 * Output: -1
 *
 *
 * Constraints:
 *
 * rows == mat.length
 * cols == mat[i].length
 * 1 <= rows, cols <= 100
 * mat[i][j] is either 0 or 1.
 * mat[i] is sorted in non-decreasing order.
 *
 */
public class LeftmostColumnWithAtLeastAOne {

    /** This is the BinaryMatrix's API interface (provided by LC, not implemented here). */
    public interface BinaryMatrix {
        int get(int row, int col);

        List<Integer> dimensions();
    }

    // V0
    // IDEA: STAIRCASE SEARCH (start from the TOP-RIGHT corner)
    //       a 1 at (r, c) -> this column has a 1, record it and move LEFT;
    //       a 0 at (r, c) -> nothing at/left of c in this row, move DOWN.
    //       Each step consumes a row or a column -> at most rows + cols get() calls.
    /**
     * time = O(M + N)
     * space = O(1)
     */
    public int leftmostColumnWithOne(BinaryMatrix binaryMatrix) {
        List<Integer> dimensions = binaryMatrix.dimensions();
        int rows = dimensions.get(0);
        int cols = dimensions.get(1);

        int r = 0;
        int c = cols - 1;
        int result = -1;

        while (r < rows && c >= 0) {
            if (binaryMatrix.get(r, c) == 1) {
                // column c has a 1; a better answer can only be further left
                result = c;
                c--;
            } else {
                // row r is all 0 up to c (rows are sorted), so this row is done
                r++;
            }
        }
        return result;
    }

    // V1
    // IDEA: BINARY SEARCH PER ROW — find each row's leftmost 1, only searching left of
    //       the best answer so far. O(rows * log cols) calls, also within the budget.
    /**
     * time = O(M log N)
     * space = O(1)
     */
    public int leftmostColumnWithOne_1(BinaryMatrix binaryMatrix) {
        List<Integer> dimensions = binaryMatrix.dimensions();
        int rows = dimensions.get(0);
        int cols = dimensions.get(1);

        // best = leftmost column with a 1 found so far (cols means "none yet")
        int best = cols;

        for (int r = 0; r < rows; r++) {
            int left = 0;
            int right = best - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (binaryMatrix.get(r, mid) == 1) {
                    // a 1 at mid: record it, keep looking further left
                    best = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        if (best < cols) {
            return best;
        }
        return -1;
    }
}
