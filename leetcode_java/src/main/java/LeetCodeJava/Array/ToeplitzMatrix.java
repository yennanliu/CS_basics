package LeetCodeJava.Array;

// https://leetcode.com/problems/toeplitz-matrix/

/**
 *  766. Toeplitz Matrix
 *  Easy
 *
 *  Given an m x n matrix, return true if the matrix is Toeplitz. Otherwise,
 *  return false.
 *
 *  A matrix is Toeplitz if every diagonal from top-left to bottom-right has the
 *  same elements.
 *
 *  Example 1:
 *    Input: matrix = [[1,2,3,4],[5,1,2,3],[9,5,1,2]]
 *    Output: true
 *    Explanation: In the above grid, the diagonals are:
 *    "[9]", "[5, 5]", "[1, 1, 1]", "[2, 2, 2]", "[3, 3]", "[4]".
 *    In each diagonal all elements are the same, so the answer is true.
 *
 *  Example 2:
 *    Input: matrix = [[1,2],[2,2]]
 *    Output: false
 *    Explanation: The diagonal "[1, 2]" has different elements.
 *
 *  Constraints:
 *    m == matrix.length
 *    n == matrix[i].length
 *    1 <= m, n <= 20
 *    0 <= matrix[i][j] <= 99
 */
public class ToeplitzMatrix {

    // V0
    // IDEA: every cell (except the last row / column) must equal its
    //       bottom-right neighbour.
    /**
     * time = O(m * n)
     * space = O(1)
     */
    public boolean isToeplitzMatrix(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (matrix[i][j] != matrix[i + 1][j + 1]) {
                    return false;
                }
            }
        }
        return true;
    }
}
