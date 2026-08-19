package LeetCodeJava.Array;

// https://leetcode.com/problems/spiral-matrix-ii/

/**
 *  59. Spiral Matrix II
 *  Medium
 *
 *  Given a positive integer n, generate an n x n matrix filled with
 *  elements from 1 to n^2 in spiral order.
 *
 *  Example 1:
 *   Input: n = 3
 *   Output: [[1,2,3],[8,9,4],[7,6,5]]
 *
 *  Example 2:
 *   Input: n = 1
 *   Output: [[1]]
 *
 *  Constraints:
 *   1 <= n <= 20
 */
public class SpiralMatrixII {

    // V0
    // IDEA: LAYER BY LAYER BOUNDARY SHRINKING
    /**
     * time = O(n^2)
     * space = O(1) (excluding output)
     */
    public int[][] generateMatrix(int n) {
        int[][] res = new int[n][n];

        int top = 0;
        int bottom = n - 1;
        int left = 0;
        int right = n - 1;
        int val = 1;

        while (top <= bottom && left <= right) {

            // left -> right (top row)
            for (int c = left; c <= right; c++) {
                res[top][c] = val++;
            }
            top++;

            // top -> bottom (right col)
            for (int r = top; r <= bottom; r++) {
                res[r][right] = val++;
            }
            right--;

            // right -> left (bottom row)
            if (top <= bottom) {
                for (int c = right; c >= left; c--) {
                    res[bottom][c] = val++;
                }
                bottom--;
            }

            // bottom -> top (left col)
            if (left <= right) {
                for (int r = bottom; r >= top; r--) {
                    res[r][left] = val++;
                }
                left++;
            }
        }

        return res;
    }
}
