package dev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class workspace7 {
  public static void main(String[] args) {

    // ------------------- TEST 1
//    int len = 5;
//    int[][] updates ={ {1,3,2}, {2,4,3}, {0,2,-2} };
//    int[] res = getModifiedArray(len, updates);
//    System.out.println(">>> res = ");
//    for (int x : res){
//      // [-2,0,3,5,3]
//      System.out.println(x);
//    }

    // ------------------- TEST 2
//    int[][] mat1 = new int[][]{ {1,0,0}, {-1,0,3} };
//    int[][] mat2 = new int[][]{ {7,0,0}, {0,0,0}, {0,0,1} };
//    int[][] res = multiply(mat1, mat2);
//    System.out.println(">>> res = ");
//    for (int i = 0; i < res.length; i++){
//      for (int j = 0; j < res[0].length; j++){
//        System.out.println(res[i][j]);
//      }
//    }

    // ------------------- TEST 3
//    String a = "a";
//    String b = "b";
//    a += b;
//    System.out.println(">> a = " + a);

    // ------------------- TEST 4
    List<String> x = new ArrayList<>();
    x.add("a");
    x.add("b");
    System.out.println(x.toString());
  }


  public static int[][] multiply(int[][] mat1, int[][] mat2) {
    // Edge case: Single element matrices
    if (mat1.length == 1 && mat1[0].length == 1 && mat2.length == 1 && mat2[0].length == 1) {
      return new int[][]{{mat1[0][0] * mat2[0][0]}};
    }

    int l_1 = mat1.length;    // Number of rows in mat1
    int w_1 = mat1[0].length; // Number of columns in mat1 (and rows in mat2)

    int w_2 = mat2[0].length; // Number of columns in mat2

    // Initialize the result matrix
    int[][] res = new int[l_1][w_2];

    // Perform matrix multiplication
    for (int i = 0; i < l_1; i++) {
      for (int j = 0; j < w_2; j++) {
        int sum = 0; // Sum for res[i][j]
        for (int k = 0; k < w_1; k++) {
          sum += mat1[i][k] * mat2[k][j];
        }
        res[i][j] = sum;
      }
    }

    return res;
  }

//  public static int[][] multiply(int[][] A, int[][] B) {
//    int m = A.length; // Number of rows in A
//    int n = A[0].length; // Number of columns in A
//    int p = B[0].length; // Number of columns in B
//
//    int[][] result = new int[m][p];
//
//    // Iterate through rows of A
//    for (int i = 0; i < m; i++) {
//      for (int k = 0; k < n; k++) {
//        if (A[i][k] != 0) { // Skip zero entries in A
//          for (int j = 0; j < p; j++) {
//            if (B[k][j] != 0) { // Skip zero entries in B
//              result[i][j] += A[i][k] * B[k][j];
//            }
//          }
//        }
//      }
//    }
//
//    return result;
//  }

//  public static int[][] multiply(int[][] mat1, int[][] mat2) {
//
//    // edge case
//    if (mat1.length == 1 && mat1[0].length == 1 && mat2.length == 1 && mat2[0].length == 1){
//      int[][] res = new int[][]{};
//      res[0][0] = mat1[0][0] * mat2[0][0];
//      return res;
//    }
//
//    int l_1 = mat1.length;
//    int w_1 = mat1[0].length;
//
//    int l_2 = mat2.length;
//    int w_2 = mat2[0].length;
//
//    int[][] res = new int[l_1][w_2]; // ???
//
//    for(int i = 0; i < l_1; i++){
//      int tmp = 0;
//      for (int j = 0; j < w_2; j++){
//        System.out.println(">>> i = " + i + ", j = " + j + ", tmp = " + tmp);
//        tmp += (mat1[i][j] * mat2[j][i]);
//        res[i][j] = tmp;
//      }
//      //res[i][j] = tmp; // ?
//    }
//
//    return res;
//  }

  public static int[] getModifiedArray(int length, int[][] updates) {

    int[] tmp = new int[length+1]; // ?
    for (int[] x : updates){
      int start = x[0];
      int end = x[1];
      int amount = x[2];

      // add
      tmp[start] += amount;

      // subtract (remove the "adding affect" on "NEXT" element)
      if (end + 1 < length){ // ??
        tmp[end + 1] -= amount;
      }
    }

    // prepare final result
    for(int i = 1; i < tmp.length; i++){
      tmp[i] += tmp[i-1];
    }

    return Arrays.copyOfRange(tmp, 0, length); // ????
    //return tmp;
  }
}
