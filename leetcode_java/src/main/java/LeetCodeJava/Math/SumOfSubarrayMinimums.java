package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-subarray-minimums/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  907. Sum of Subarray Minimums
 *  Medium
 *
 *  Given an array of integers arr, find the sum of min(b), where b ranges over
 *  every (contiguous) subarray of arr. Since the answer may be large, return
 *  the answer modulo 10^9 + 7.
 *
 *  Example 1:
 *   Input: arr = [3,1,2,4]
 *   Output: 17
 *   Explanation: subarrays are [3], [1], [2], [4], [3,1], [1,2], [2,4],
 *                [3,1,2], [1,2,4], [3,1,2,4].
 *                Minimums are 3, 1, 2, 4, 1, 1, 2, 1, 1, 1. Sum is 17.
 *
 *  Example 2:
 *   Input: arr = [11,81,94,43,3]
 *   Output: 444
 *
 *  Constraints:
 *   - 1 <= arr.length <= 3 * 10^4
 *   - 1 <= arr[i] <= 3 * 10^4
 */
public class SumOfSubarrayMinimums {

    // V0
    // IDEA: MONOTONIC STACK - for each i count how many subarrays have arr[i]
    //       as their minimum: left[i] = distance to previous strictly smaller
    //       element, right[i] = distance to next smaller-or-equal element
    //       (the strict/non-strict split avoids double counting duplicates).
    //       ans = sum( arr[i] * left[i] * right[i] )
    /**
     * time = O(n)
     * space = O(n)
     */
    public int sumSubarrayMins(int[] arr) {
        final int MOD = 1_000_000_007;
        int n = arr.length;
        int[] left = new int[n];   // # of subarrays ending at i where arr[i] is min
        int[] right = new int[n];  // # of subarrays starting at i where arr[i] is min

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            // previous strictly smaller
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                stack.pop();
            }
            left[i] = stack.isEmpty() ? i + 1 : i - stack.peek();
            stack.push(i);
        }

        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            // next smaller-or-equal
            while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
                stack.pop();
            }
            right[i] = stack.isEmpty() ? n - i : stack.peek() - i;
            stack.push(i);
        }

        long res = 0;
        for (int i = 0; i < n; i++) {
            res = (res + (long) arr[i] * left[i] % MOD * right[i]) % MOD;
        }
        return (int) res;
    }

    // V1
    // IDEA: single-pass monotonic stack, accumulating the running sum of
    //       minimums of all subarrays ending at index i.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int sumSubarrayMins_1(int[] arr) {
        final int MOD = 1_000_000_007;
        int n = arr.length;
        // stack holds indices with increasing arr values
        Deque<Integer> stack = new ArrayDeque<>();
        long[] dp = new long[n]; // sum of mins of all subarrays ending at i
        long res = 0;
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                stack.pop();
            }
            if (stack.isEmpty()) {
                dp[i] = (long) (i + 1) * arr[i] % MOD;
            } else {
                int j = stack.peek();
                dp[i] = (dp[j] + (long) (i - j) * arr[i]) % MOD;
            }
            stack.push(i);
            res = (res + dp[i]) % MOD;
        }
        return (int) res;
    }
}
