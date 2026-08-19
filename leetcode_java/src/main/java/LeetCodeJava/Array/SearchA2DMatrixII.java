package LeetCodeJava.Array;

// https://leetcode.com/problems/search-a-2d-matrix-ii/

/**
 *  240. Search a 2D Matrix II
 *  Medium
 *
 *  Write an efficient algorithm that searches for a value target in an
 *  m x n integer matrix. This matrix has the following properties:
 *
 *   - Integers in each row are sorted in ascending order from left to right.
 *   - Integers in each column are sorted in ascending order from top to bottom.
 *
 *  Example 1:
 *   Input: matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],
 *                    [10,13,14,17,24],[18,21,23,26,30]], target = 5
 *   Output: true
 *
 *  Example 2:
 *   (same matrix), target = 20
 *   Output: false
 *
 *  Constraints:
 *   m == matrix.length, n == matrix[i].length
 *   1 <= n, m <= 300
 *   -10^9 <= matrix[i][j] <= 10^9
 *   -10^9 <= target <= 10^9
 */
public class SearchA2DMatrixII {

    // V0
    // IDEA: STAIRCASE SEARCH - START FROM TOP-RIGHT, MOVE LEFT (too big) or DOWN (too small)
    /**
     * time = O(m + n)
     * space = O(1)
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int r = 0;
        int c = matrix[0].length - 1;

        while (r < matrix.length && c >= 0) {
            int cur = matrix[r][c];
            if (cur == target) {
                return true;
            } else if (cur > target) {
                // whole column below is bigger -> move left
                c--;
            } else {
                // whole row to the left is smaller -> move down
                r++;
            }
        }

        return false;
    }
}
