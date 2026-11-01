package LeetCodeJava.Array;

// https://leetcode.com/problems/cells-with-odd-values-in-a-matrix/description/

/**
 * 1252. Cells with Odd Values in a Matrix
 * Easy
 *
 * There is an m x n matrix that is initialized to all 0's. There is also a 2D array indices where each indices[i] = [ri, ci] represents a 0-indexed location to perform some increment operations on the matrix.
 *
 * For each location indices[i], do both of the following:
 *
 * 1. Increment all the cells on row ri.
 * 2. Increment all the cells on column ci.
 *
 * Given m, n, and indices, return the number of odd-valued cells in the matrix after applying the increment to all locations in indices.
 *
 * Example 1:
 *
 * Input: m = 2, n = 3, indices = [[0,1],[1,1]]
 * Output: 6
 * Explanation: Initial matrix = [[0,0,0],[0,0,0]].
 * After applying first increment it becomes [[1,2,1],[0,1,0]].
 * The final matrix is [[1,3,1],[1,3,1]], which contains 6 odd numbers.
 *
 * Example 2:
 *
 * Input: m = 2, n = 2, indices = [[1,1],[0,0]]
 * Output: 0
 * Explanation: Final matrix = [[2,2],[2,2]]. There are no odd numbers in the final matrix.
 *
 * Constraints:
 *
 * 1 <= m, n <= 50
 * 1 <= indices.length <= 100
 * 0 <= ri < m
 * 0 <= ci < n
 *
 * Follow up: Could you solve this in O(n + m + indices.length) time with only O(n + m) extra space?
 *
 */
public class CellsWithOddValuesInAMatrix {

    // V0
    // IDEA: COUNT ROW / COLUMN INCREMENTS ONLY + PARITY MATH
    //   cell (i, j) = rowIncrements[i] + colIncrements[j], which is ODD exactly
    //   when one of the two is odd and the other even.
    //   oddRows * evenCols + oddCols * evenRows, no m x n scan (the follow-up bound)
    /**
     * time = O(M + N + K), K = indices.length
     * space = O(M + N)
     */
    public int oddCells(int m, int n, int[][] indices) {
        int[] rowIncrements = new int[m];
        int[] colIncrements = new int[n];
        for (int[] location : indices) {
            rowIncrements[location[0]]++;
            colIncrements[location[1]]++;
        }

        int oddRows = 0;
        for (int count : rowIncrements) {
            if (count % 2 == 1) {
                oddRows++;
            }
        }
        int oddCols = 0;
        for (int count : colIncrements) {
            if (count % 2 == 1) {
                oddCols++;
            }
        }

        // odd row meets even column, or even row meets odd column
        return oddRows * (n - oddCols) + oddCols * (m - oddRows);
    }
}
