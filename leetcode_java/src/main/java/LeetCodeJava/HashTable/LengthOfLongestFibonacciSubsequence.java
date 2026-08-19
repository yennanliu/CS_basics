package LeetCodeJava.HashTable;

// https://leetcode.com/problems/length-of-longest-fibonacci-subsequence/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  873. Length of Longest Fibonacci Subsequence
 *  Medium
 *
 *  A sequence x1, x2, ..., xn is Fibonacci-like if:
 *    - n >= 3
 *    - xi + xi+1 == xi+2 for all i + 2 <= n
 *
 *  Given a strictly increasing array arr of positive integers forming a sequence,
 *  return the length of the longest Fibonacci-like subsequence of arr.
 *  If one does not exist, return 0.
 *
 *  Example 1:
 *  Input: arr = [1,2,3,4,5,6,7,8]
 *  Output: 5  ([1,2,3,5,8])
 *
 *  Example 2:
 *  Input: arr = [1,3,7,11,12,14,18]
 *  Output: 3  (e.g. [1,11,12])
 *
 *  Constraints:
 *  3 <= arr.length <= 1000
 *  1 <= arr[i] < arr[i + 1] <= 10^9
 */
public class LengthOfLongestFibonacciSubsequence {

    // V0
    // IDEA: DP ON PAIRS. dp[j][k] = length of the fib-like sequence ending with
    //       (arr[j], arr[k]). If arr[k] - arr[j] = arr[i] and arr[i] < arr[j],
    //       then dp[j][k] = dp[i][j] + 1 (default 2).
    /**
     * time = O(n^2)
     * space = O(n^2)
     */
    public int lenLongestFibSubseq(int[] arr) {

        // edge
        if (arr == null || arr.length < 3) {
            return 0;
        }

        int n = arr.length;
        Map<Integer, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) {
            idx.put(arr[i], i);
        }

        int[][] dp = new int[n][n];
        int res = 0;

        for (int k = 0; k < n; k++) {
            for (int j = 0; j < k; j++) {
                int prev = arr[k] - arr[j];
                // need prev < arr[j] so the sequence is strictly increasing
                Integer i = (prev < arr[j]) ? idx.get(prev) : null;
                if (i != null) {
                    dp[j][k] = Math.max(dp[i][j], 2) + 1;
                } else {
                    dp[j][k] = 2;
                }
                if (dp[j][k] > res) {
                    res = dp[j][k];
                }
            }
        }

        return res >= 3 ? res : 0;
    }

    // V1
    // IDEA: HASH SET + EXTEND EVERY PAIR GREEDILY
    /**
     * time = O(n^2 * log(max(arr)))
     * space = O(n)
     */
    public int lenLongestFibSubseq_1(int[] arr) {

        if (arr == null || arr.length < 3) {
            return 0;
        }

        Set<Integer> set = new HashSet<>();
        for (int x : arr) {
            set.add(x);
        }

        int n = arr.length;
        int res = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int a = arr[i];
                int b = arr[j];
                int len = 2;
                // guard int overflow : values are <= 10^9, use long for the sum
                while ((long) a + b <= Integer.MAX_VALUE && set.contains(a + b)) {
                    int next = a + b;
                    a = b;
                    b = next;
                    len++;
                }
                res = Math.max(res, len);
            }
        }

        return res >= 3 ? res : 0;
    }
}
