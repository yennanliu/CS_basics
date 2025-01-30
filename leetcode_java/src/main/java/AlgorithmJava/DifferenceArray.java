package AlgorithmJava;

/**
 *  DifferenceArray implementation V1
 *
 *  https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/difference_array.md
 */

import java.util.Arrays;

public class DifferenceArray {

  // attr
  int[] array;

  // method
  public int[] getDifferenceArray(int[][] input, int n) {

    /** LC 1109. Corporate Flight Bookings input : [start, end, seats]
     *
     *  NOTE !!!
     *
     *   in java, index start from 0;
     *   but in LC 1109, index start from 1
     *
     */
    int[] tmp = new int[n + 1];
    for (int[] x : input) {
      int start = x[0];
      int end = x[1];
      int seats = x[2];

      // add
      tmp[start] += seats;

      // subtract
      if (end + 1 <= n) {
        tmp[end + 1] -= seats;
      }
    }

    for (int i = 1; i < tmp.length; i++) {
      //tmp[i] = tmp[i - 1] + tmp[i];
        tmp[i] += tmp[i - 1];
    }

    return Arrays.copyOfRange(tmp, 1, n+1);
  }
}
