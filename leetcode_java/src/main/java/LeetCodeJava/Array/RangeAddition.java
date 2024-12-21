package LeetCodeJava.Array;

// https://leetcode.com/problems/range-addition/description/
// https://leetcode.ca/all/370.html

import java.util.Arrays;

/**
 * 370. Range Addition Assume you have an array of length n initialized with all 0's and are given k
 * update operations.
 *
 * <p>Each operation is represented as a triplet: [startIndex, endIndex, inc] which increments each
 * element of subarray A[startIndex ... endIndex] (startIndex and endIndex inclusive) with inc.
 *
 * <p>Return the modified array after all k operations were executed.
 *
 * <p>Example:
 *
 * <p>Input: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]] Output: [-2,0,3,5,3] Explanation:
 *
 * <p>Initial state: [0,0,0,0,0]
 *
 * <p>After applying operation [1,3,2]: [0,2,2,2,0]
 *
 * <p>After applying operation [2,4,3]: [0,2,5,5,3]
 *
 * <p>After applying operation [0,2,-2]: [-2,0,3,5,3] Difficulty: Medium Lock: Prime Company: Google
 */
public class RangeAddition {

  // V0
  // IDEA : DIFFERENCE ARRAY
  // LC 1109
  // https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Array/CorporateFlightBookings.java
  public static int[] getModifiedArray(int length, int[][] updates) {

    int[] tmp = new int[length + 1]; // or new int[length]; both works
    for (int[] x : updates) {
      int start = x[0];
      int end = x[1];
      int amount = x[2];

      // add
      tmp[start] += amount;

      // subtract (remove the "adding affect" on "NEXT" element)
      /**
       * NOTE !!!
       *
       * <p>we remove the "adding affect" on NEXT element (e.g. end + 1)
       */
      if (end + 1 < length) { // NOTE !!! use `end + 1`
        tmp[end + 1] -= amount;
      }
    }

    // prepare final result
    for (int i = 1; i < tmp.length; i++) {
      tmp[i] += tmp[i - 1];
    }

    return Arrays.copyOfRange(tmp, 0, length); // return the sub array between 0, lengh index
  }

  // V1
  // https://leetcode.ca/2016-12-04-370-Range-Addition/
  public int[] getModifiedArray_1(int length, int[][] updates) {
    int[] d = new int[length];
    for (int[] e : updates) {
      int l = e[0], r = e[1], c = e[2];
      d[l] += c;
      if (r + 1 < length) {
        d[r + 1] -= c;
      }
    }
    for (int i = 1; i < length; ++i) {
      d[i] += d[i - 1];
    }
    return d;
  }

  // V2
}
