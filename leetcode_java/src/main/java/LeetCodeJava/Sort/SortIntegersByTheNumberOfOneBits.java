package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-integers-by-the-number-of-1-bits/

import java.util.Arrays;

/**
 *  1356. Sort Integers by The Number of 1 Bits
 *  Easy
 *
 *  You are given an integer array arr. Sort the integers in the array in
 *  ascending order by the number of 1's in their binary representation and in
 *  case of two or more integers have the same number of 1's you have to sort
 *  them in ascending order.
 *
 *  Return the array after sorting it.
 *
 *  Example 1:
 *    Input: arr = [0,1,2,3,4,5,6,7,8]
 *    Output: [0,1,2,4,8,3,5,6,7]
 *    Explanation: [0] has 0 bits, [1,2,4,8] have 1 bit, [3,5,6] have 2 bits,
 *                 [7] has 3 bits.
 *
 *  Example 2:
 *    Input: arr = [1024,512,256,128,64,32,16,8,4,2,1]
 *    Output: [1,2,4,8,16,32,64,128,256,512,1024]
 *
 *  Constraints:
 *    1 <= arr.length <= 500
 *    0 <= arr[i] <= 10^4
 */
public class SortIntegersByTheNumberOfOneBits {

    // V0
    // IDEA: COMPOSITE COMPARATOR (popcount ASC, value ASC)
    //       Integer.bitCount is the popcount; both criteria go the same
    //       direction, so compare popcount first and fall back to the value.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] sortByBits(int[] arr) {
        Integer[] boxed = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            boxed[i] = arr[i];
        }

        Arrays.sort(boxed, (a, b) -> {
            int ba = Integer.bitCount(a);
            int bb = Integer.bitCount(b);
            if (ba != bb) {
                return Integer.compare(ba, bb);
            }
            return Integer.compare(a, b);
        });

        int[] res = new int[arr.length];
        for (int i = 0; i < boxed.length; i++) {
            res[i] = boxed[i];
        }
        return res;
    }

    // V1
    // IDEA: ENCODE popcount INTO THE VALUE, THEN A PLAIN PRIMITIVE SORT
    //       since arr[i] <= 10^4, `popcount * 100000 + x` keeps the two sort
    //       keys in disjoint "digit ranges", so ONE numeric ascending sort of
    //       int[] reproduces the same order — no boxing, no comparator.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] sortByBitsEncoded(int[] arr) {
        int n = arr.length;
        int[] encoded = new int[n];
        for (int i = 0; i < n; i++) {
            encoded[i] = Integer.bitCount(arr[i]) * 100000 + arr[i];
        }

        Arrays.sort(encoded);

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = encoded[i] % 100000;
        }
        return res;
    }
}
