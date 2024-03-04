package LeetCodeJava.Array;

// https://leetcode.com/problems/set-matrix-zeroes/description/

public class SetMatrixZeroes {

    // V0
    // IDEA : ARRAY OP
    // TODO: implement below
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/set-matrix-zeroes.py
    public void setZeroes(int[][] matrix) {

    }

    // V1
    public void setZeroes_1(int[][] matrix) {
        boolean fr = false,fc = false;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                if(matrix[i][j] == 0) {
                    if(i == 0) fr = true;
                    if(j == 0) fc = true;
                    matrix[0][j] = 0;
                    matrix[i][0] = 0;
                }
            }
        }
        for(int i = 1; i < matrix.length; i++) {
            for(int j = 1; j < matrix[0].length; j++) {
                if(matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }}
        }
        if(fr) {
            for(int j = 0; j < matrix[0].length; j++) {
                matrix[0][j] = 0;
            }
        }
        if(fc) {
            for(int i = 0; i < matrix.length; i++) {
                matrix[i][0] = 0;
            }
        }
    }

    // V2
    // https://leetcode.com/problems/set-matrix-zeroes/solutions/4800467/using-2-d-boolean-array/
    public void setZeroes_2(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean[][] isOriginalZero = new boolean[rows][cols];

        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                if(matrix[i][j] == 0)
                    isOriginalZero[i][j] = true;
                else
                    continue;
            }
        }

        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                int currRow = i;
                int currCol = j;
                if(matrix[currRow][currCol]==0 && isOriginalZero[currRow][currCol]){
                    for(int c=0;c<currCol;c++){
                        matrix[currRow][c] = 0;
                    }
                    for(int c=currCol;c<cols;c++){
                        matrix[currRow][c] = 0;
                    }

                    for(int r=0;r<currRow;r++){
                        matrix[r][currCol] = 0;
                    }
                    for(int r=currRow;r<rows;r++){
                        matrix[r][currCol] = 0;
                    }
                }
                else
                    continue;
            }
        }
    }

}
