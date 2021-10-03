package com.yen.DataStructure;

// https://www.youtube.com/watch?v=shpy1IvCuJw&list=PLmOn9nNkQxJFvyhDYx0ya4F75uTtUHA_f&index=9

public class SparseArray {
    public static void main(String[] args) {

        // init
        // create a 11x11 2d matrix
        // 0 : no chess, 1: black, 2: blue
        int chessArr1[][] = new int[11][11];

        chessArr1[1][2] = 1;
        chessArr1[2][3] = 2;

        System.out.println("------- Original Matrix -------");

        for (int[] row:chessArr1){
            for (int data:row){
                System.out.printf("%d\t", data);
            }
            System.out.println();
        }

        System.out.println("=====================");

        /** 2d matrix -> sparse arrays */

        // step 1)
        int sum = 0;
        for (int i=0; i < 11; i++){
            for (int j=0; j < 11; j++){
                if (chessArr1[i][j] != 0){
                    sum += 1;
                }
            }
        }

        System.out.println("sum = " + sum);

        // step 2) init sparse arrays
        int sparseArray[][] = new int[sum+1][3];

        // step 3) give values to sparse arrays
        sparseArray[0][0] = 11;
        sparseArray[0][1] = 11;
        sparseArray[0][2] = sum;

        // step 4) go through 2d matrix, put non-zero values to sparse arrays
        int counter2 = 0; // record the order of non-zero element
        for (int i=0; i < 11; i++){
            for (int j=0; j < 11; j++){
                if (chessArr1[i][j] != 0){
                    counter2 += 1;
                    sparseArray[counter2][0] = i;
                    sparseArray[counter2][1] = j;
                    sparseArray[counter2][2] = chessArr1[i][j];
                }
            }
        }

        // print sparseArray
        System.out.println("");
        System.out.println("------- Sparse Arrays -------");
        for (int i=0; i < sparseArray.length; i++){
            // format string syntax
            System.out.printf("%d\t%d\t%d\n", sparseArray[i][0],sparseArray[i][1],sparseArray[i][2]);
        }

        /** sparse arrays -> 2d matrix */
    }
}
