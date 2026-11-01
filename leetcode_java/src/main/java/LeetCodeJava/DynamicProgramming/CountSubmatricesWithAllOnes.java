package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-submatrices-with-all-ones/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 1504. Count Submatrices With All Ones
 * Medium
 *
 * Given an m x n binary matrix mat, return the number of submatrices that have all ones.
 *
 *
 * Example 1:
 *
 * Input: mat = [[1,0,1],[1,1,0],[1,1,0]]
 * Output: 13
 * Explanation:
 * There are 6 rectangles of side 1x1.
 * There are 2 rectangles of side 1x2.
 * There are 3 rectangles of side 2x1.
 * There is 1 rectangle of side 2x2.
 * There is 1 rectangle of side 3x1.
 * Total number of rectangles = 6 + 2 + 3 + 1 + 1 = 13.
 *
 * Example 2:
 *
 * Input: mat = [[0,1,1,0],[0,1,1,1],[1,1,1,0]]
 * Output: 24
 * Explanation:
 * There are 8 rectangles of side 1x1.
 * There are 5 rectangles of side 1x2.
 * There are 2 rectangles of side 1x3.
 * There are 4 rectangles of side 2x1.
 * There are 2 rectangles of side 2x2.
 * There are 2 rectangles of side 3x1.
 * There is 1 rectangle of side 3x2.
 * Total number of rectangles = 8 + 5 + 2 + 4 + 2 + 2 + 1 = 24.
 *
 *
 * Constraints:
 *
 * 1 <= m, n <= 150
 * mat[i][j] is either 0 or 1.
 *
 */
public class CountSubmatricesWithAllOnes {

    // V0
    // IDEA: ROW RUN LENGTHS + FIX THE BOTTOM-RIGHT CORNER
    //       width[i][j] = number of consecutive 1s ending at (i, j) in row i.
    //       for a bottom-right corner (i, j), walk the top row k upward from i: the widest
    //       all-ones rectangle spanning rows k..i is min(width[k..i][j]), and every width
    //       1..that minimum is one distinct submatrix -> add the running minimum.
    /**
     * time = O(M^2 * N)
     * space = O(M * N)
     */
    public int numSubmat(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;

        // width[i][j] = length of the run of 1s in row i ending at column j
        int[][] width = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mat[i][j] == 1) {
                    width[i][j] = (j == 0) ? 1 : width[i][j - 1] + 1;
                }
            }
        }

        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // extend the rectangle upward one row at a time
                int minWidth = Integer.MAX_VALUE;
                for (int top = i; top >= 0; top--) {
                    minWidth = Math.min(minWidth, width[top][j]);
                    // a row of zeros at column j blocks every taller rectangle
                    if (minWidth == 0) {
                        break;
                    }
                    count += minWidth;
                }
            }
        }
        return count;
    }

    // V1
    // IDEA: HISTOGRAM HEIGHTS + MONOTONIC STACK
    //       height[j] = consecutive 1s ending at the current row in column j.
    //       endingAt[j] = # of all-ones submatrices whose bottom-right corner is (row, j).
    //       with prev = nearest column to the left with a smaller height,
    //         endingAt[j] = endingAt[prev] + height[j] * (j - prev)
    //       because columns prev+1..j can all reach height[j], and anything wider is limited
    //       by height[prev] exactly as it was for prev.
    /**
     * time = O(M * N)
     * space = O(N)
     */
    public int numSubmat_1(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        int[] height = new int[cols];
        int count = 0;

        for (int i = 0; i < rows; i++) {
            // update the histogram for this row
            for (int j = 0; j < cols; j++) {
                height[j] = (mat[i][j] == 1) ? height[j] + 1 : 0;
            }

            // stack of column indices with strictly increasing heights
            Deque<Integer> stack = new ArrayDeque<>();
            int[] endingAt = new int[cols];
            for (int j = 0; j < cols; j++) {
                while (!stack.isEmpty() && height[stack.peek()] >= height[j]) {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    int prev = stack.peek();
                    endingAt[j] = endingAt[prev] + height[j] * (j - prev);
                } else {
                    // no shorter column on the left: every width 1..j+1 reaches height[j]
                    endingAt[j] = height[j] * (j + 1);
                }
                stack.push(j);
                count += endingAt[j];
            }
        }
        return count;
    }
}
