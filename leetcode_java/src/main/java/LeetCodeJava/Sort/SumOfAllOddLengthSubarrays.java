package LeetCodeJava.Sort;

// https://leetcode.com/problems/sum-of-all-odd-length-subarrays/

/**
 *  1588. Sum of All Odd Length Subarrays
 *  Easy
 *
 *  Given an array of positive integers arr, return the sum of all possible
 *  odd-length subarrays of arr.
 *
 *  A subarray is a contiguous subsequence of the array.
 *
 *  Example 1:
 *    Input: arr = [1,4,2,5,3]
 *    Output: 58
 *    Explanation: 1+4+2+5+3 + 7+11+10 + 15 = 58
 *
 *  Example 2:
 *    Input: arr = [1,2]
 *    Output: 3
 *    Explanation: only [1] and [2] have odd length.
 *
 *  Example 3:
 *    Input: arr = [10,11,12]
 *    Output: 66
 *
 *  Constraints:
 *    1 <= arr.length <= 100
 *    1 <= arr[i] <= 1000
 *
 *  Follow up: could you solve this problem in O(n) time complexity?
 */
public class SumOfAllOddLengthSubarrays {

    // V0
    // IDEA: CONTRIBUTION COUNTING — HOW MANY ODD SUBARRAYS COVER INDEX i ?
    //       index i can be the start of (n - i) subarrays and the end of (i + 1)
    //       of them, so it belongs to (i + 1) * (n - i) subarrays in total, and
    //       exactly half of them (rounded up) have odd length:
    //         odd(i) = ((i + 1) * (n - i) + 1) / 2
    //       answer = sum(arr[i] * odd(i)) -> one pass, no subarray ever built.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int sumOddLengthSubarrays(int[] arr) {
        int n = arr.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            int oddCount = ((i + 1) * (n - i) + 1) / 2;
            res += arr[i] * oddCount;
        }
        return res;
    }
}
