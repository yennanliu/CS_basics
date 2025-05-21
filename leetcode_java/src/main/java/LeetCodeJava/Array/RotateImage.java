package LeetCodeJava.Array;

//https://leetcode.com/problems/rotate-image/
/**
 * 48. Rotate Image
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees (clockwise).
 *
 * You have to rotate the image in-place, which means you have to modify the input 2D matrix directly. DO NOT allocate another 2D matrix and do the rotation.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [[7,4,1],[8,5,2],[9,6,3]]
 * Example 2:
 *
 *
 * Input: matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
 * Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
 *
 *
 * Constraints:
 *
 * n == matrix.length == matrix[i].length
 * 1 <= n <= 20
 * -1000 <= matrix[i][j] <= 1000
 */

public class RotateImage {

    /**
     *  Exp 1:
     *
     *      matrix = [[1,2,3],[4,5,6],[7,8,9]]
     *
     *       // Step 1) : mirror ([i, j] -> [j, i])
     *
     *          [
     *              [1,4,7],
     *              [2,5,8],
     *              [3,6,9]
     *          ]
     *
     *     // Step 2) : reverse ([1,2,3] -> [3,2,1])
     *
     *          [
     *             [7,4,1],
     *             [8,5,2],
     *             [9,6,3]
     *          ]
     *
     */

    // V0
    // IDEA : ARRAY OP
    public void rotate(int[][] matrix){

        int len = matrix.length;
        int width = matrix[0].length;

        // Step 1) : mirror ([i, j] -> [j, i])
        /**
         *  Example :
         *
         *  matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
         *
         *  so, below double loop will visit :
         *
         *  (0,1), (0,2), (0,3)
         *  (1,2), (1,3)
         *  (2,3
         *
         */
        /** NOTE !!!
         *
         * for (int i = 0; i < len; i++)
         *   for (int j = i+1; j < width; j++)
         *
         * (j start from i+1)
         */
        for (int i = 0; i < len; i++){
            for (int j = i+1; j < width; j++){
                int tmp = matrix[i][j];
                //matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }

        // Step 2) : reverse ([1,2,3] -> [3,2,1])
        for (int i = 0; i < len; i++){
            _reverse(matrix[i]);
        }
    }

    private void _reverse(int[] input){

        int len = input.length;
        /**
         *  Exp 1
         *
         *   [1,2,3,4]
         *   l      r
         *
         *   [4,2,3,1]
         *      l r
         *
         *
         *  Exp 2
         *
         *  [1,2,3,4,5]
         *   l       r
         *
         *  [5,2,3,4,1]
         *     l   r
         *
         *  [5,4,3,2,1]
         *       l
         *       r
         */
        int l = 0;
        int r = len-1;
        while(r > l){
            int _tmp = input[l];
            // i, j = j, i
            input[l] = input[r];
            input[r] = _tmp;
            l += 1;
            r -= 1;
        }

    }

    // V1-1
    // https://neetcode.io/problems/rotate-matrix
    // IDEA: BRUTE FORCE
    public void rotate_1_1(int[][] matrix) {
        int n = matrix.length;
        int[][] rotated = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = rotated[i][j];
            }
        }
    }

    // V1-2
    // https://neetcode.io/problems/rotate-matrix
    // IDEA: Rotate By Four Cells
    public void rotate_1_2(int[][] matrix) {
        int l = 0;
        int r = matrix.length - 1;

        while ( l < r ) {
            for(int i = 0; i < r - l; i++) {
                int top = l;
                int bottom = r;
                //save the topleft
                int topLeft = matrix[top][l + i];

                //move bottom left into top left
                matrix[top][l + i] = matrix[bottom - i][l];

                // move bottom right into bottom left
                matrix[bottom - i][l] = matrix[bottom][r - i];

                // move top right into bottom right
                matrix[bottom][r - i] = matrix[top + i][r];

                // move top left into top right
                matrix[top + i][r] = topLeft;

            }
            r--;
            l++;
        }
    }

    // V1-3
    // https://neetcode.io/problems/rotate-matrix
    // IDEA: Reverse And Transpose
    public void rotate_1_3(int[][] matrix) {
        // Reverse the matrix vertically
        reverse(matrix);

        // Transpose the matrix
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i; j < matrix[i].length; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }

    private void reverse(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n / 2; i++) {
            int[] temp = matrix[i];
            matrix[i] = matrix[n - 1 - i];
            matrix[n - 1 - i] = temp;
        }
    }


    // V2-1
    // IDEA : Rotate Groups of Four Cells
    // https://leetcode.com/problems/rotate-image/editorial/
    public void rotate_2_1(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < (n + 1) / 2; i ++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = matrix[n - 1 - i][n - j - 1];
                matrix[n - 1 - i][n - j - 1] = matrix[j][n - 1 -i];
                matrix[j][n - 1 - i] = matrix[i][j];
                matrix[i][j] = temp;
            }
        }
    }

    // V2-2
    // IDEA : Reverse on the Diagonal and then Reverse Left to Right
    // https://leetcode.com/problems/rotate-image/editorial/
    public void rotate_2_2(int[][] matrix) {
        transpose(matrix);
        reflect(matrix);
    }

    public void transpose(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int tmp = matrix[j][i];
                matrix[j][i] = matrix[i][j];
                matrix[i][j] = tmp;
            }
        }
    }

    public void reflect(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[i][n - j - 1];
                matrix[i][n - j - 1] = tmp;
            }
        }
    }

}
