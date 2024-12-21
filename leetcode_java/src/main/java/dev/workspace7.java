package dev;

import java.util.Arrays;

public class workspace7 {
  public static void main(String[] args) {

    int len = 5;
    int[][] updates ={ {1,3,2}, {2,4,3}, {0,2,-2} };
    int[] res = getModifiedArray(len, updates);
    System.out.println(">>> res = ");
    for (int x : res){
      // [-2,0,3,5,3]
      System.out.println(x);
    }
  }

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
