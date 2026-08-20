package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/decode-xored-array/

/**
 *  1720. Decode XORed Array
 *  Easy
 *
 *  There is a hidden integer array arr that consists of n non-negative integers.
 *
 *  It was encoded into another integer array encoded of length n - 1, such that
 *  encoded[i] = arr[i] XOR arr[i + 1]. For example, if arr = [1,0,2,1], then
 *  encoded = [1,2,3].
 *
 *  You are given the encoded array. You are also given an integer first, that is
 *  the first element of arr, i.e. arr[0].
 *
 *  Return the original array arr. It can be proved that the answer exists and is
 *  unique.
 *
 *  Example 1:
 *    Input: encoded = [1,2,3], first = 1
 *    Output: [1,0,2,1]
 *
 *  Example 2:
 *    Input: encoded = [6,2,7,3], first = 4
 *    Output: [4,2,0,7,4]
 *
 *  Constraints:
 *    2 <= n <= 10^4
 *    encoded.length == n - 1
 *    0 <= encoded[i] <= 10^5
 *    0 <= first <= 10^5
 */
public class DecodeXORedArray {

    // V0
    // IDEA: XOR IS ITS OWN INVERSE
    //       from encoded[i] = arr[i] ^ arr[i+1], XOR both sides by arr[i]:
    //           arr[i] ^ encoded[i] = arr[i+1]
    //       so each next element is prev ^ encoded[i]; seed the chain with
    //       `first` and unroll left to right.
    /**
     * time = O(N)
     * space = O(N)   // the output array
     */
    public int[] decode(int[] encoded, int first) {
        int n = encoded.length + 1;
        int[] res = new int[n];
        res[0] = first;
        for (int i = 0; i < encoded.length; i++) {
            res[i + 1] = res[i] ^ encoded[i];
        }
        return res;
    }
}
