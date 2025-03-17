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
  // IDEA : DIFFERENCE ARRAY + prefix sum
  // LC 1109
  // https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Array/CorporateFlightBookings.java
  public static int[] getModifiedArray(int length, int[][] updates) {

    /**
     *
     * We initialize an auxiliary array tmp with size length + 1 (one extra element).
     * -> The reason for this extra element (e.g. length + 1) is to help handle the boundary
     *    when applying the "subtract" operation later in the process.
     */
    int[] tmp = new int[length + 1]; // or new int[length]; both works
    for (int[] x : updates) {
      int start = x[0];
      int end = x[1];
      int amount = x[2];

      // add
      /** NOTE !!! below
       *
       * -> we add value at `start` idx ONLY
       */
      tmp[start] += amount;

      // subtract (remove the "adding affect" on "NEXT" element)
      /**
       * NOTE !!!
       *
       *  we remove the "adding side effect"
       *  at NEXT idx (e.g. end + 1)
       *
       *
       *  Subtracting the amount at end + 1:
       *
       *   -  After the update is applied to the range [start, end],
       *      we need to "undo" the effect just after the range ends.
       *
       *   - We subtract the amount from tmp[end + 1], which effectively
       *     removes the effect of the update from the next index,
       *     ensuring that only the range [start, end] gets the update.
       *
       *
       *  Why end + 1?
       *
       *   - The reason we subtract from tmp[end + 1] instead of
       *     tmp[end] is because we want the effect to be removed
       *     after the end index (inclusive). For example, if end = 3,
       *     the update should apply until index 3, but not beyond.
       *     By subtracting from end + 1,
       *     we ensure that the effect stops exactly at index end.
       *
       */
      if (end + 1 < length) { // NOTE !!! use `end + 1`
        tmp[end + 1] -= amount;
      }
    }

    /**
     * NOTE !!!
     *
     *  prepare the final result via below:
     *
     *
     *  - After applying all the updates,
     *    tmp contains the difference values
     *    (i.e., the change at each index),
     *    but we need to convert it into the actual modified values in the array.
     *
     *
     * - We accumulate the values in tmp by iterating from index 1 to tmp.length - 1,
     *   adding the value of the previous index to the current index.
     *   This gives us the final values for each index in the array, considering all the updates.
     *   The prefix sum is the key technique here: for any index i,
     *   tmp[i] will represent the total sum of all the updates that
     *   should be applied to that index.
     *
     */
    for (int i = 1; i < tmp.length; i++) {
      tmp[i] += tmp[i - 1];
    }

    return Arrays.copyOfRange(tmp, 0, length); // return the sub array between 0, lengh index
  }

  // V0-1
  // IDEA: BRUTE FORCE
  // TODO: validate below
//  public static int[] getModifiedArray_0_1(int length, int[][] updates) {
//    // edge
//    if(length == 0){
//      return null;
//    }
//    if(updates == null || updates.length == 0){
//      return new int[length]; // ???
//    }
//    // prefix sum
//    int[] preSum = new int[length];
//    for(int[] u: updates){
//      int start = u[0];
//      int end = u[1];
//      int amount = u[2];
//      for(int i = start; i < end; i++){
//        preSum[i] += amount;
//      }
//    }
//
//    return preSum;
//  }


  // V0-2
  // IDEA: PREFIX SUM (fixed by gpt)
  public static int[] getModifiedArray_0_2(int length, int[][] updates) {
    // Edge case: if the length is 0, return an empty array.
    if (length == 0) {
      return new int[0];
    }

    // Edge case: if no updates are provided, return an array of zeroes.
    if (updates == null || updates.length == 0) {
      return new int[length];
    }

    // Step 1: Initialize the preSum array.
    int[] preSum = new int[length];

    // Step 2: Apply the updates to the preSum array using the prefix sum technique.
    for (int[] u : updates) {
      int start = u[0];
      int end = u[1];
      int amount = u[2];

      preSum[start] += amount; // Add amount at the start index.
      if (end + 1 < length) {
        preSum[end + 1] -= amount; // Subtract amount just after the end index.
      }
    }

    // Step 3: Apply the prefix sum to get the final array values.
    for (int i = 1; i < length; i++) {
      preSum[i] += preSum[i - 1];
    }

    return preSum; // Return the modified array.
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
