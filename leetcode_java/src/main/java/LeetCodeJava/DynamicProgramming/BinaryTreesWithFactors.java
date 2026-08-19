package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/binary-trees-with-factors/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  823. Binary Trees With Factors
 *  Medium
 *
 *  Given an array of unique integers, arr, where each integer arr[i] is
 *  strictly greater than 1.
 *
 *  We make a binary tree using these integers, and each number may be used for
 *  any number of times. Each non-leaf node's value should be equal to the
 *  product of the values of its children.
 *
 *  Return the number of binary trees we can make. The answer may be too large
 *  so return the answer modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: arr = [2,4]
 *    Output: 3
 *    Explanation: We can make these trees: [2], [4], [4, 2, 2]
 *
 *  Example 2:
 *    Input: arr = [2,4,5,10]
 *    Output: 7
 *    Explanation: We can make these trees:
 *                 [2], [4], [5], [10], [4, 2, 2], [10, 2, 5], [10, 5, 2].
 *
 *  Constraints:
 *    - 1 <= arr.length <= 1000
 *    - 2 <= arr[i] <= 10^9
 *    - All the values of arr are unique.
 */
public class BinaryTreesWithFactors {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: sort ascending, dp[i] = # trees rooted at arr[i];
    //       every child pair (j, arr[i]/arr[j]) must be smaller, so it is already computed
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int numFactoredBinaryTrees(int[] arr) {
        int n = arr.length;
        int[] a = Arrays.copyOf(arr, n);
        Arrays.sort(a);

        Map<Integer, Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) {
            idx.put(a[i], i);
        }

        long[] dp = new long[n];
        long res = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = 1;   // the single-node tree
            for (int j = 0; j < i; j++) {
                if (a[i] % a[j] != 0) {
                    continue;
                }
                int other = a[i] / a[j];
                Integer k = idx.get(other);
                if (k != null) {
                    dp[i] = (dp[i] + dp[j] * dp[k]) % MOD;
                }
            }
            res = (res + dp[i]) % MOD;
        }
        return (int) res;
    }
}
