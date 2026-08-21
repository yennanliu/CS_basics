package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/ones-and-zeroes/

/**
 *  474. Ones and Zeroes
 *  Medium
 *
 *  You are given an array of binary strings strs and two integers m and n.
 *
 *  Return the size of the largest subset of strs such that there are at most
 *  m 0's and n 1's in the subset.
 *
 *  A set x is a subset of a set y if all elements of x are also elements of y.
 *
 *  Example 1:
 *
 *  Input: strs = ["10","0001","111001","1","0"], m = 5, n = 3
 *  Output: 4
 *  Explanation: The largest subset with at most 5 0's and 3 1's is
 *  {"10", "0001", "1", "0"}, so the answer is 4.
 *
 *  Example 2:
 *
 *  Input: strs = ["10","0","1"], m = 1, n = 1
 *  Output: 2
 *  Explanation: The largest subset is {"0", "1"}, so the answer is 2.
 *
 *  Constraints:
 *
 *  1 <= strs.length <= 600
 *  1 <= strs[i].length <= 100
 *  strs[i] consists only of digits '0' and '1'.
 *  1 <= m, n <= 100
 */
public class OnesAndZeroes {

    // V0
    // IDEA: 0/1 KNAPSACK WITH 2 CAPACITIES (m zeros, n ones)
    //  dp[i][j] = max subset size using at most i zeros and j ones
    //  iterate capacities backwards so each string is used at most once
    /**
     * time = O(s * m * n), s = strs.length
     * space = O(m * n)
     */
    public int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];

        for (String s : strs) {
            int zeros = 0;
            int ones = 0;
            for (int k = 0; k < s.length(); k++) {
                if (s.charAt(k) == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            // NOTE !!! go backwards (0/1 knapsack)
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        return dp[m][n];
    }

    // V1
    // IDEA: TOP-DOWN MEMOIZATION - dfs(idx, zerosLeft, onesLeft) = best subset size
    //       reachable from string idx onward (take / skip)
    /**
     * time = O(s * m * n)
     * space = O(s * m * n)
     */
    public int findMaxForm_1(String[] strs, int m, int n) {
        int s = strs.length;
        int[][] cost = countZerosOnes(strs);
        Integer[][][] memo = new Integer[Math.max(s, 1)][m + 1][n + 1];
        return dfs_1(cost, 0, m, n, memo);
    }

    private int dfs_1(int[][] cost, int idx, int zeros, int ones, Integer[][][] memo) {
        if (idx == cost.length) {
            return 0;
        }
        if (memo[idx][zeros][ones] != null) {
            return memo[idx][zeros][ones];
        }
        // skip
        int res = dfs_1(cost, idx + 1, zeros, ones, memo);
        // take
        if (cost[idx][0] <= zeros && cost[idx][1] <= ones) {
            res = Math.max(res,
                    1 + dfs_1(cost, idx + 1, zeros - cost[idx][0], ones - cost[idx][1], memo));
        }
        memo[idx][zeros][ones] = res;
        return res;
    }

    // V2
    // IDEA: EXPLICIT 3D TABULATION over the item index - the un-rolled textbook
    //       knapsack table that V0 compresses into 2 dimensions
    /**
     * time = O(s * m * n)
     * space = O(s * m * n)
     */
    public int findMaxForm_2(String[] strs, int m, int n) {
        int s = strs.length;
        int[][] cost = countZerosOnes(strs);
        int[][][] dp = new int[s + 1][m + 1][n + 1];

        for (int i = 1; i <= s; i++) {
            int zeros = cost[i - 1][0];
            int ones = cost[i - 1][1];
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k <= n; k++) {
                    dp[i][j][k] = dp[i - 1][j][k]; // skip
                    if (j >= zeros && k >= ones) {
                        dp[i][j][k] = Math.max(dp[i][j][k], dp[i - 1][j - zeros][k - ones] + 1);
                    }
                }
            }
        }
        return dp[s][m][n];
    }

    // shared by V1 / V2 : cost[i] = { #zeros, #ones } of strs[i]
    private int[][] countZerosOnes(String[] strs) {
        int[][] cost = new int[strs.length][2];
        for (int i = 0; i < strs.length; i++) {
            for (int k = 0; k < strs[i].length(); k++) {
                cost[i][strs[i].charAt(k) == '0' ? 0 : 1]++;
            }
        }
        return cost;
    }
}
