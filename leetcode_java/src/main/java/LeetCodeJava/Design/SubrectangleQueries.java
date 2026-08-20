package LeetCodeJava.Design;

// https://leetcode.com/problems/subrectangle-queries/

import java.util.ArrayList;
import java.util.List;

/**
 *  1476. Subrectangle Queries
 *  Medium
 *
 *  Implement the class SubrectangleQueries which receives a rows x cols rectangle as a
 *  matrix of integers in the constructor and supports two methods:
 *
 *   1. updateSubrectangle(int row1, int col1, int row2, int col2, int newValue)
 *      - Updates all values with newValue in the subrectangle whose upper left coordinate
 *        is (row1, col1) and bottom right coordinate is (row2, col2).
 *
 *   2. getValue(int row, int col)
 *      - Returns the current value of the coordinate (row, col) from the rectangle.
 *
 *  Example 1:
 *
 *  Input
 *  ["SubrectangleQueries","getValue","updateSubrectangle","getValue","getValue",
 *   "updateSubrectangle","getValue","getValue"]
 *  [[[[1,2,1],[4,3,4],[3,2,1],[1,1,1]]],[0,2],[0,0,3,2,5],[0,2],[3,1],
 *   [3,0,3,2,10],[3,1],[0,2]]
 *  Output
 *  [null,1,null,5,5,null,10,5]
 *
 *  Example 2:
 *
 *  Input
 *  ["SubrectangleQueries","getValue","updateSubrectangle","getValue","getValue",
 *   "updateSubrectangle","getValue"]
 *  [[[[1,1,1],[2,2,2],[3,3,3]]],[0,0],[0,0,2,2,100],[0,0],[2,2],[1,1,2,2,20],[2,2]]
 *  Output
 *  [null,1,null,100,100,null,20]
 *
 *  Constraints:
 *
 *   There will be at most 500 operations considering both methods.
 *   1 <= rows, cols <= 100
 *   rows == rectangle.length, cols == rectangle[i].length
 *   0 <= row1 <= row2 < rows
 *   0 <= col1 <= col2 < cols
 *   1 <= newValue, rectangle[i][j] <= 10^9
 *   0 <= row < rows, 0 <= col < cols
 */
public class SubrectangleQueries {

    // V0
    // IDEA: WRITE-THROUGH -- just repaint the cells in place
    //       rows, cols <= 100 so a subrectangle holds at most 10^4 cells, and there are at
    //       most 500 operations -> eagerly overwriting is cheap and makes getValue O(1).
    /**
     * time = O((row2-row1+1) * (col2-col1+1)) per update, O(1) per getValue
     * space = O(rows * cols)
     */
    private final int[][] rectangle;

    public SubrectangleQueries(int[][] rectangle) {
        this.rectangle = rectangle;
    }

    public void updateSubrectangle(int row1, int col1, int row2, int col2, int newValue) {
        for (int r = row1; r <= row2; r++) {
            for (int c = col1; c <= col2; c++) {
                rectangle[r][c] = newValue;
            }
        }
    }

    public int getValue(int row, int col) {
        return rectangle[row][col];
    }

    // V1
    // IDEA: LOG THE UPDATES, RESOLVE ON READ (never touch the matrix)
    //       keep the original matrix untouched and append every update to a history list.
    //       a read scans the history BACKWARDS and returns the first update whose
    //       rectangle covers (row, col); if none does, fall back to the original value.
    //       useful when updates hugely outnumber reads (or the rectangle is huge).
    /**
     * time = O(1) per update, O(U) per getValue  (U = number of updates so far)
     * space = O(rows * cols + U)
     */
    public static class SubrectangleQueriesLazy {

        private final int[][] rectangle;
        // each entry = {row1, col1, row2, col2, newValue}
        private final List<int[]> history;

        public SubrectangleQueriesLazy(int[][] rectangle) {
            this.rectangle = rectangle;
            this.history = new ArrayList<>();
        }

        public void updateSubrectangle(int row1, int col1, int row2, int col2, int newValue) {
            history.add(new int[]{row1, col1, row2, col2, newValue});
        }

        public int getValue(int row, int col) {
            for (int i = history.size() - 1; i >= 0; i--) {
                int[] u = history.get(i);
                if (row >= u[0] && row <= u[2] && col >= u[1] && col <= u[3]) {
                    return u[4];
                }
            }
            return rectangle[row][col];
        }
    }
}
