package LeetCodeJava.Array;

//https://leetcode.com/problems/rotate-image/

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

    // V1
    // IDEA : Rotate Groups of Four Cells
    // https://leetcode.com/problems/rotate-image/editorial/
    public void rotate_1(int[][] matrix) {
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

    // V2
    // IDEA : Reverse on the Diagonal and then Reverse Left to Right
    // https://leetcode.com/problems/rotate-image/editorial/
    public void rotate_2(int[][] matrix) {
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
