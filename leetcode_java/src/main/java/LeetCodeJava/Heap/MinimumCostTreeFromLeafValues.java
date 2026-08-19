package LeetCodeJava.Heap;

// https://leetcode.com/problems/minimum-cost-tree-from-leaf-values/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1130. Minimum Cost Tree From Leaf Values
 *  Medium
 *
 *  Given an array arr of positive integers, consider all binary trees such that:
 *    - Each node has either 0 or 2 children;
 *    - The values of arr correspond to the values of each leaf in an in-order
 *      traversal of the tree;
 *    - The value of each non-leaf node is equal to the product of the largest leaf
 *      value in its left and right subtree, respectively.
 *
 *  Among all possible binary trees considered, return the smallest possible sum of
 *  the values of each non-leaf node. It is guaranteed this sum fits into a 32-bit
 *  integer.
 *
 *  Example 1:
 *  Input: arr = [6,2,4]
 *  Output: 32
 *  Explanation: there are two possible trees, with costs 36 and 32; 32 is smaller.
 *
 *  Example 2:
 *  Input: arr = [4,11]
 *  Output: 44
 *
 *  Constraints:
 *  2 <= arr.length <= 40
 *  1 <= arr[i] <= 15
 *  It is guaranteed that the answer fits into a 32-bit signed integer.
 */
public class MinimumCostTreeFromLeafValues {

    // V0
    // IDEA: MONOTONIC DECREASING STACK. Every value must eventually be merged with
    //       a neighbour; the cheapest way to remove a local minimum `mid` is to pay
    //       mid * min(left neighbour, right neighbour). Repeatedly pop such minima.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int mctFromLeafValues(int[] arr) {

        // edge
        if (arr == null || arr.length < 2) {
            return 0;
        }

        int res = 0;
        Deque<Integer> stack = new ArrayDeque<>(); // decreasing from bottom to top

        for (int a : arr) {
            while (!stack.isEmpty() && stack.peek() <= a) {
                int mid = stack.pop();
                if (stack.isEmpty()) {
                    res += mid * a;
                } else {
                    res += mid * Math.min(stack.peek(), a);
                }
            }
            stack.push(a);
        }

        // the remaining stack is strictly decreasing -> merge from the top
        while (stack.size() > 1) {
            int top = stack.pop();
            res += top * stack.peek();
        }

        return res;
    }

    // V1
    // IDEA: INTERVAL DP. dp[i][j] = min cost of building the tree over arr[i..j],
    //       split at every k, cost = dp[i][k] + dp[k+1][j] + max(i..k) * max(k+1..j)
    /**
     * time = O(n^3)
     * space = O(n^2)
     */
    public int mctFromLeafValues_1(int[] arr) {

        if (arr == null || arr.length < 2) {
            return 0;
        }

        int n = arr.length;
        int[][] maxVal = new int[n][n];
        for (int i = 0; i < n; i++) {
            maxVal[i][i] = arr[i];
            for (int j = i + 1; j < n; j++) {
                maxVal[i][j] = Math.max(maxVal[i][j - 1], arr[j]);
            }
        }

        int[][] dp = new int[n][n];
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + maxVal[i][k] * maxVal[k + 1][j];
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }

        return dp[0][n - 1];
    }
}
