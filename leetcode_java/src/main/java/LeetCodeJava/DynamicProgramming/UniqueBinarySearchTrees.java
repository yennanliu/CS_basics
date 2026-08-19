package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/unique-binary-search-trees/

/**
 *  96. Unique Binary Search Trees
 *  Medium
 *
 *  Given an integer n, return the number of structurally unique BST's (binary search trees)
 *  which has exactly n nodes of unique values from 1 to n.
 *
 *  Example 1:
 *  Input: n = 3
 *  Output: 5
 *
 *  Example 2:
 *  Input: n = 1
 *  Output: 1
 *
 *  Constraints:
 *
 *   1 <= n <= 19
 */
public class UniqueBinarySearchTrees {

    // V0
    // IDEA: DP (CATALAN NUMBER)
    //       pick each value i as the root: the i-1 smaller values form the left subtree and
    //       the n-i bigger values form the right subtree, so
    //          dp[n] = sum over i in [1..n] of dp[i-1] * dp[n-i]
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int numTrees(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int nodes = 1; nodes <= n; nodes++) {
            int cnt = 0;
            for (int root = 1; root <= nodes; root++) {
                cnt += dp[root - 1] * dp[nodes - root];
            }
            dp[nodes] = cnt;
        }
        return dp[n];
    }

    // V1
    // IDEA: CLOSED FORM CATALAN NUMBER  C(n) = C(n-1) * 2 * (2n - 1) / (n + 1)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int numTrees_1(int n) {
        long res = 1L;
        for (int i = 1; i <= n; i++) {
            res = res * 2 * (2L * i - 1) / (i + 1);
        }
        return (int) res;
    }
}
