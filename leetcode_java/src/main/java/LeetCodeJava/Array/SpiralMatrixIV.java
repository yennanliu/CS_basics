package LeetCodeJava.Array;

// https://leetcode.com/problems/spiral-matrix-iv/description/

import LeetCodeJava.DataStructure.ListNode;
import java.util.Arrays;

/**
 * 2326. Spiral Matrix IV
 * Medium
 *
 * You are given two integers m and n, which represent the dimensions of a matrix.
 *
 * You are also given the head of a linked list of integers.
 *
 * Generate an m x n matrix that contains the integers in the linked list presented in spiral order (clockwise), starting from the top-left of the matrix. If there are remaining empty spaces, fill them with -1.
 *
 * Return the generated matrix.
 *
 *
 * Example 1:
 *
 * Input: m = 3, n = 5, head = [3,0,2,6,8,1,7,9,4,2,5,5,0]
 * Output: [[3,0,2,6,8],[5,0,-1,-1,1],[5,2,4,9,7]]
 * Explanation: The diagram above shows how the values are printed in the matrix.
 * Note that the remaining spaces in the matrix are filled with -1.
 *
 * Example 2:
 *
 * Input: m = 1, n = 4, head = [0,1,2]
 * Output: [[0,1,2,-1]]
 * Explanation: The diagram above shows how the values are printed from left to right in the matrix.
 * The last space in the matrix is set to -1.
 *
 *
 * Constraints:
 *
 * 1 <= m, n <= 10^5
 * 1 <= m * n <= 10^5
 * The number of nodes in the list is in the range [1, m * n].
 * 0 <= Node.val <= 1000
 *
 */
public class SpiralMatrixIV {

    // V0
    // IDEA: WALK THE SPIRAL WITH FOUR SHRINKING BOUNDARIES
    //       fill the matrix with -1, then traverse it clockwise consuming one list
    //       node per cell until the list runs out. the guards before the leftward
    //       and upward legs stop a single remaining row/column being visited twice.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] spiralMatrix(int m, int n, ListNode head) {
        int[][] matrix = new int[m][n];
        for (int[] row : matrix) {
            Arrays.fill(row, -1);
        }

        int top = 0;
        int bottom = m - 1;
        int left = 0;
        int right = n - 1;
        ListNode node = head;

        while (node != null && top <= bottom && left <= right) {
            // left -> right along the top row
            for (int c = left; c <= right && node != null; c++) {
                matrix[top][c] = node.val;
                node = node.next;
            }
            top++;

            // top -> bottom along the right column
            for (int r = top; r <= bottom && node != null; r++) {
                matrix[r][right] = node.val;
                node = node.next;
            }
            right--;

            // right -> left along the bottom row, only if a row is left
            if (top <= bottom) {
                for (int c = right; c >= left && node != null; c--) {
                    matrix[bottom][c] = node.val;
                    node = node.next;
                }
                bottom--;
            }

            // bottom -> top along the left column, only if a column is left
            if (left <= right) {
                for (int r = bottom; r >= top && node != null; r--) {
                    matrix[r][left] = node.val;
                    node = node.next;
                }
                left++;
            }
        }
        return matrix;
    }
}
