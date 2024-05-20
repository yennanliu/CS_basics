package LeetCodeJava.Array;

// https://leetcode.com/problems/set-matrix-zeroes/description/

import java.util.ArrayList;
import java.util.List;

public class SetMatrixZeroes {

    // V0
    // IDEA : ARRAY OP
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Array/set-matrix-zeroes.py
    public void setZeroes(int[][] matrix) {

        if (matrix.length == 0 && matrix[0].length == 0){
            return;
        }

        int l = matrix.length;
        int w = matrix[0].length;

        // get zero
        List<int[]> collected = new ArrayList<>();
        for (int i = 0; i < w; i++){
            for (int j = 0; j < l; j++){
                if(matrix[j][i] == 0){
                    collected.add(new int[]{i, j});
                }
            }
        }

        //System.out.println("collected = " + collected);
        for (int[] point : collected){

            //System.out.println("x = " + point[0] + " y = " + point[1]);
            int x = point[0];
            int y = point[1];

            // make x - direction as zero
            for (int i = 0; i < w; i++){
                matrix[y][i] = 0;
            }

            // make y - direction as zero
            for (int j = 0; j < l; j++){
                matrix[j][x] = 0;
            }
        }

        return;
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
